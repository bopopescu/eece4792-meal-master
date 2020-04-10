import json
import os
import requests
import time
from PIL import Image
import pprint
import argparse
import re
from process import from_azure
import mysql.connector

import base64

def get_as_base64():
    url = r'https://github.com/Team-W4/eece4792-meal-master/blob/master/src/text-recognition/receipt-pics/tj1.jpg?raw=true'
    b64 = base64.b64encode(requests.get(url).content)
    return b64


RECEIPT_NON_FOODS = ["CRV", "BAG FEE", "CREW MEMBER DISCOUNT", "GROCERY NON TAXABLE", "CANVAS BAG"]

def get_azure_settings():
    azure = {
        "key": "955a8ee27fab47eab18b79a6f8f45a32",
        "endpoint": "https://teamw4-image-processing.cognitiveservices.azure.com/vision/v2.0/",
        "text recognition": "read/core/asyncBatchAnalyze"
    }
    return azure

def process_image_response(headers, response):
    # Process the response
    analysis = {}

    # Text recognition needs to be polled until complete
    while (True):
        final_response = requests.get(response.headers['Operation-Location'], headers=headers)
        analysis = final_response.json()

        if ('recognitionResults' in analysis):
            break

        if ('status' in analysis and analysis['status'] == 'Failed'):
            print('Analysis of', url, 'failed...')
            break

        time.sleep(1)

    return analysis


def request_from_local(path):
    # Get Azure settings
    key = get_azure_settings()['key']
    endpoint = os.path.join(get_azure_settings()['endpoint'], get_azure_settings()['text recognition'])

    # Setup API request
    headers = {'Ocp-Apim-Subscription-Key': key, 'Content-Type': 'application/octet-stream'}
    data = open(path, "rb").read()

    # Make request
    response = requests.post(endpoint, headers=headers, data=data)
    response.raise_for_status()
    return process_image_response(headers, response)

def request_from_url(url):
    # Get Azure settings
    key = get_azure_settings()['key']
    endpoint = os.path.join(get_azure_settings()['endpoint'], get_azure_settings()['text recognition'])

    # Setup API request
    headers = {'Ocp-Apim-Subscription-Key': key}
    data = {'url': url}

    # Make request
    response = requests.post(endpoint, headers=headers, json=data)
    response.raise_for_status()
    return process_image_response(headers, response)

def request_from_b64(stream):
    # Get Azure settings
    key = get_azure_settings()['key']
    endpoint = os.path.join(get_azure_settings()['endpoint'], get_azure_settings()['text recognition'])

    # Setup API request
    headers = {'Ocp-Apim-Subscription-Key': key, 'Content-Type': 'application/octet-stream'}
    data = stream

    # Make request
    response = requests.post(endpoint, headers=headers,  data=data)
    response.raise_for_status()
    return process_image_response(headers, response)

def parse_foods(recognition_results):
    lines = []

    for page in recognition_results['recognitionResults']:
        for line in page['lines']:
            lines.append(line['text'])

    if (any("trader joe" in line.lower() for line in lines)):
        return parse_trader_joes(lines)

    return lines

def parse_trader_joes(lines):
    for line in lines:
        if ("open" in line.lower()):
            lines = lines[lines.index(line) + 1 : -1]
            break

    for line in lines:
        if ("subtotal" in line.lower() or "bag fee" in line.lower()):
            lines = lines[0:lines.index(line)]
            break

    price_re = re.compile("^\d+ *\. *\d\d *-?$")
    lines = list(filter(lambda x : not (price_re.match(x) or x[0] == '@'), lines))
    lines = list(map(lambda x : x.split('@')[0].strip() if '@' in x else x, lines))

    foods = []
    counter = 0
    while (counter < len(lines)):
        line = lines[counter]
        next_line = lines[counter + 1] if counter < len(lines) - 1 else ""
        quantity_re = re.compile("^\d+\w+$")

        if (quantity_re.match(next_line)):
            foods.append({"name": line, "quantity": next_line})
            counter = counter + 2
        elif (line.upper() in RECEIPT_NON_FOODS):
            if (quantity_re.match(next_line)):
                counter = counter + 2
            else:
                counter = counter + 1
        else:
            foods.append({"name": line, "quantity": "1"})
            counter = counter + 1

    return foods


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Parses the provided image")
    parser.add_argument('--text_file', help="Path to image of receipt", required=True)
    args = parser.parse_args()

    # Needs renaming
    base64_string = open(args.text_file, 'r').read()
    if(base64_string[0:2] == "b'" ):
        base64_string = base64_string[2:]
    try:
        from_azure(parse_foods(request_from_b64(base64.b64decode(base64_string))))
    except:
        print([])
 
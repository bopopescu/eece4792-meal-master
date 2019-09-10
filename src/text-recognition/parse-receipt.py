import json
import os
import requests
import time

def get_azure_settings():
    return json.loads(open('settings.json', 'r').read())['azure']

def process_from_url(url):
    # Get Azure settings
    key = get_azure_settings()['key']
    endpoint = os.path.join(get_azure_settings()['endpoint'], get_azure_settings()['text recognition']) 

    # Setup API request
    headers = {'Ocp-Apim-Subscription-Key': key}
    data = {'url': url}
    
    # Make request
    response = requests.post(endpoint, headers=headers, json=data)
    response.raise_for_status()

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

def parse_foods(recognition_results):
    lines = []

    for page in recognition_results['recognitionResults']:
        for line in page['lines']:
            lines.append(line['text'])

    return lines

if __name__ == "__main__":
    print("Text:")
    print(parse_foods(process_from_url('https://i0.wp.com/clark.com/wp-content/uploads/2018/10/receipt.jpg')))
    print("\nJSON:")
    print(process_from_url('https://i0.wp.com/clark.com/wp-content/uploads/2018/10/receipt.jpg'))

# venv Python 3.7-32
import os, sys

# scrape sites for food info\
#    -Name
#    -Pantry/Fridge?etx
#    -Nutrition?
#    -Serving size
# save to excel
# connect to mysql 
# dump into neccesary tables
# 
# 
# ML model (auto correct input to get closest matches in db)

# selenium, beautiful soup, pandas

# STEPS
# Find the URL that you want to scrape
# Inspecting the Page
# Find the data you want to extract
# Write the code
# Run the code and extract the data
# Store the data in the required forma


from bs4 import BeautifulSoup
import pandas as pd
import requests


products=[] #List to store name of the product
prices=[] #List to store price of the product
ratings=[] #List to store rating of the product

def ex_url_scrape():
    ex_url = r'https://www.flipkart.com/laptops/~buyback-guarantee-on-laptops-/pr?sid=6bo%2Cb5g&uniqBStoreParam1=val1&wid=11.productCard.PMU_V2'
    page = requests.get(ex_url)
    soup = BeautifulSoup(page.content, 'html.parser')

    for a in soup.findAll('a',href=True, attrs={'class':'_31qSD5'}):
        name=a.find('div', attrs={'class':'_3wU53n'})
        price=a.find('div', attrs={'class':'_1vC4OE _2rQ-NK'})
        products.append(name.text)
        prices.append(price.text)

    df = pd.DataFrame({'Product Name':products,'Price':prices}) 
    # df.to_csv('products.csv', index=False, encoding='utf-8')
    print(df)

def TODAY_scrape():
    #   for TODAY site:
    # after the colon take food
    #  today_url = r'https://www.today.com/food/best-trader-joe-s-products-2018-according-customers-t146959'
    return

def delish_foods():        
    # for the delish.com
    # the header
    delish_url = r'https://www.delish.com/food-news/g22553381/things-to-buy-at-trader-joes/'
    page = requests.get(delish_url)
    soup = BeautifulSoup(page.content, 'html.parser')


    foods = []
    for a in soup.findAll(attrs={"class":'listicle-slide-hed-text'}):
        # <span class="listicle-slide-hed-text">
        # print(a.string)
        foods.append(a.string)
    
    df = pd.DataFrame({'Food Product Name':foods}) 
    return df

def refinery29():
    # for the refinery29
    # h3 after each slid (have to change site via pg number b/c slides)
    foods = []
    
    for i in range(124):
        refinery29_url = r'https://www.refinery29.com/en-us/best-trader-joes-food-products#slide-'+str(125-i)
        page = requests.get(refinery29_url)
        soup = BeautifulSoup(page.content, 'html.parser')
        # <span class="listicle-slide-hed-text">
        # print(a.string)
        foods.append(soup.findAll(attrs={"class":'listicle-slide-hed-text'}).string)
    
    df = pd.DataFrame({'Food Product Name':foods}) 
    return df

# whole foods online shopping./amazon's site
# get food, figure out expiration dates, THEN nutrition
# whole_foods_url = r'https://www.amazon.com/alm/storefront?almBrandId=VUZHIFdob2xlIEZvb2Rz&ref_=US_TRF_ALL_UFG_WFM_REFER_0417717'

if __name__ == "__main__":
    # print(delish_foods())
    
    # TODO
    print(refinery29)
    # print(TODAY_scrape)
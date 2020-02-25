import mysql.connector
import re, collections

def words(text):
    old_list= re.findall(r".*", text.lower())
    new_list = list(filter(None, old_list))
    # print(new_list)
    return new_list

# mydb = mysql.connector.connect(
#   host="database-1.cmnixox8v3fi.us-east-1.rds.amazonaws.com",
#   user="teamw4",
#   passwd="wordPass123!",
#   database="mealmaster"
# )
# mycursor = mydb.cursor()

#=============================================

# mycursor.execute("SELECT * FROM generic_foods")

# myresult = mycursor.fetchall()

# for x in myresult:
#   print(x)
#=============================================

food_list = words(open('big.txt').read())
food_list = [tuple(map(str, food.split('\n'))) for food in food_list]
try:
  sql = "INSERT INTO generic_foods (food) VALUES (%s)"
  mycursor.executemany(sql,food_list)
  mydb.commit()

  print(mycursor.rowcount, "record inserted.")
except:
  pass
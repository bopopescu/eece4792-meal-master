import re, collections
import nltk
# import MySQLdb
import mysql.connector


mydb = mysql.connector.connect(
	host="database-1.cmnixox8v3fi.us-east-1.rds.amazonaws.com",
	user="teamw4",
	passwd="wordPass123!",
	database="mealmaster")
mycursor = mydb.cursor()
sql = "SELECT name FROM mealmaster.generic_foods"

try:
	mycursor.execute(sql)
	result = mycursor.fetchall() #result = (1,2,3,) or  result =((1,3),(4,5),)
	final_result = [list(i) for i in result]
	flat_list = [item for sublist in final_result for item in sublist]

	# print(flat_list)

except:
   print("Error: unable to food column")

mydb.close()

def foods_dict():
	mydb.connect()
	cur = mydb.cursor()
	sql = "SELECT name, id FROM mealmaster.generic_foods"
	try:
		cur.execute(sql)
		rows = cur.fetchall()
		food_dict = dict(rows)
	except:
		print("Error: unable to food and id columns")
	mydb.close()
	return food_dict
GENERIC_FOOD_DICT = foods_dict()

def words(text):
	old_list= re.findall(r".*", text.lower())
	new_list = list(filter(None, old_list))
	return new_list

def train(features):    
	model = collections.defaultdict(lambda: 1)
	for f in features:
		model[f] += 1
	return model

NWORDS = train(flat_list)
# NWORDS = train(words(open('big.txt').read()))
alphabet = 'abcdefghijklmnopqrstuvwxyz'
drop_words = ["each", "fin", "raw", "org", "organic", "fin", "raw", "uncured", "slcd"]
abbr_words = {}



def edits1(word):
	s = [(word[:i], word[i:]) for i in range(len(word) + 1)]
	deletes    = [a + b[1:] for a, b in s if b]
	transposes = [a + b[1] + b[0] + b[2:] for a, b in s if len(b)>1]
	replaces   = [a + c + b[1:] for a, b in s for c in alphabet if b]
	inserts    = [a + c + b     for a, b in s for c in alphabet]
	return set(deletes + transposes + replaces + inserts)

def known_edits2(word):
	return set(e2 for e1 in edits1(word) for e2 in edits1(e1) if e2 in NWORDS)

def known(words):
	return set(w for w in words if w in NWORDS)

def known_edits3(words):
	return set(w for w in NWORDS if words in w)

def correct(word):
	
	# return set(list(known([word])) + list(known(edits1(word)))  + list(known_edits2(word)) + list(known_edits3(word)) + [word])
	return set(list(known([word])) + list(known_edits3(word)) + [word])

def from_azure(azure_input):
	# print(azure_input)
	food_ids = []

	for food_dict in azure_input:
		# get all matching content from corpus
		food_str = food_dict['name']
		re_str = re.split("[\W+]", food_str)
		re_str = [x.lower() for x in re_str]
		re_str = [food for food in re_str if food not in drop_words]

		re_str_nltk = nltk.pos_tag(nltk.word_tokenize(' '.join(re_str)))
		first_noun = None
		second_noun = None
		for word,pos in re_str_nltk:
			if (pos == 'NN' or pos == 'NNP' or pos == 'NNS' or pos == 'NNPS') and not first_noun:
				first_noun = word
			elif (pos == 'NN' or pos == 'NNP' or pos == 'NNS' or pos == 'NNPS'):
				second_noun = word
				break
		matchers = [re.sub(r'\b\w{1,3}\b', '', x).lower() for x in re_str]
		matching = [generic for generic in NWORDS if any(scanned in generic for scanned in filter(None, matchers))]
		# order by likelihood;  
		# 3rd by first noun 
		if first_noun in matching:
			matching.insert(0, matching.pop(matching.index(first_noun))) 
		
		# 2nd by most matching words
		# def matches_l(text):
		# 	return sum(word in text.lower() for word in re_str)
		# print(max(matching, key=matches_l))
		
		#1.5th 
		if (first_noun in matching) and (second_noun in matching):
			for food in matching:
				if all(x in food for x in [first_noun, second_noun]):
					matching.insert(0, matching.pop(matching.index(food)))
		# 1st if it is a full match
		if (' '.join(re_str)) in matching:
			matching.insert(0, matching.pop(matching.index((' '.join(re_str))))) 

		print(matching[0])
		food_ids.append(GENERIC_FOOD_DICT[matching[0]])
	print(food_ids)



	# txt = """Natural language processing (NLP) is a field of computer science, artificial intelligence, and computational linguistics concerned with the inter
	# actions between computers and human (natural) languages."""
	# print(TextBlob(txt).noun_phrases)


# https://stackoverflow.com/questions/49105473/running-a-python-script-from-spring-boot-web-application-on-embedded-tomcat
# https://github.com/Team-W4/eece4792-meal-master/tree/text-recognition

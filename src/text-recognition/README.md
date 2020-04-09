README
- to make the executable just run pyinstaller on parse_receipt.py on whichever os you want the independant executable to perform on

- current argument to script (and exe) is --text_file <.txt file with a base64 string>

IMPORTAN**
- add the mysql folder in this directory to the folder of the executable that is generated (mysql is not a python lib so it is not automatically added)

NOTES**
- to change the name of the executable and its folder use the pyinstaller argument --name <name>
- process.py is also a neccesary script that has functions called from in the parse+parse_receipt script

# CSCI 571 Homework 4: JSON Excercise

## Objective
* Get experience with the Python programming language.
* Get experience with Google News API.
* Get experience using a JSON parser in Python.
* Get experience using Flask and Django in Python.
* Getting hands-on experience in GCP, AWS or Azure.

## Technologies Used
* HTML
* CSS
* JavaScript
* Python
* Flask
* Amazon Web Services (AWS)
* D3 Cloud

## Project
#### Homepage
* In this homework, we are asked to create a webpage that allows you to search for news information using the Google News API and the results will be displayed in card format.
* On the homepage, the user can read top headlines provided by the Google News API along with specific headlines from CNN and Fox News. 
* The top headlines are displayed in a sliding format and the  headlines from CNN and Fox News are displayed in a card format. 
* A word cloud for frequently used words is also displayed on the homepage.
#### Searchpage
* The search form consists of the following fields:
	- Keyword: A required field that cannot be empty. It is an edit box the allows to enter keywords for searching news about the keyword. The default value is empty.
	- From: A required field for the date from which the articles are to be fetched. The default value when the form is initially displayed is the date 1 week ago from the current date.
	-  To: A required field for the date to which the articles are to be fetched. The default value is the current date.
	- Category: A drop down to select among categories. Default selection is all.
	- Source: A dynamically changing drop down that changes depending on the selected category. Default selection is all.
	- Search button: To allow search functionality only when a keyword is present. The input field validation is done on clicking the search button and appropriate errors need are displayed. Missing fields generate alerts.
	- Clear button: Resets the form to default values and clears search results.
#### Python Backend
* All the API calls happen to a Python backend which serves the request.
* The FLASK web application framework is used to implement the API's.
* The backend is deployed on AWS Cloud.

## Links
* [Website](http://csci571-km69564-python.us-east-1.elasticbeanstalk.com/)
* [Video](https://www.youtube.com/watch?v=FbRsiKZN7Nk)

## Contact
If you want to contact me you can reach me at <km69564@usc.edu> or <krishnamanoj14@gmail.com>.
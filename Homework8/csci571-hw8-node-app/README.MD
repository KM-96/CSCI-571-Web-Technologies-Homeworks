### Technologies Used
* Node.js
* Express.js
* React Library
* HTML
* CSS
* Javascript
* AJAX

### Project Setup -  Creating NodeJS Components 
* Download [NPM](https://www.npmjs.com/get-npm)
* Download [NODE](https://nodejs.org/en/)
* If you have already installed npm and want to update it to the latest version, use the following command [ For windows machine ]: 
    ```
    npm install npm@latest -g
    ```
* To get the version of npm installed in your machine:  ```npm -v```
* To get the version of node installed in your machine: ```node -v```
* Go to the directory where you want to save your project and run ```npm init```
    ```
    package name: (project) csci571-homework8
    version: (1.0.0)
    description: A RESTful node js application built to fulfill the requirements for HOMEWORK8 CSCI571.
    entry point: (application.js)
    test command:
    git repository:
    keywords: node js RESTful API
    author: KRISHNA MANOJ MADDIPATLA
    license: (ISC)
    ```
* Add express.js to the project using the following command in the project directory.
    ```
    npm install --save express
    ```
* Add request package to the project using the following command in the project directiory.
    ```
    npm install --save request
    ```
* Add cors
    ```
    npm install --save cors
    ```
* To start the application server use "node <application_name>"
    ```
    node application
    ```
* If you want to run the server in development mode, then set the following environment variable in windows before starting the server:
    ```
    set NODE_ENV=development
    ```

### Steps to get an API Key from Guardian News
* Visit [here](https://open-platform.theguardian.com/documentation/) to get the API key for Guardian News.
* Navigate to “sign up for an API key”
* Register for developer key and on the subsequent page.
* Enter details as asked. Once you click register, a key will be emailed to you on the email address you use.

### Steps to get an API Key from New York Times News
* Visit [here](https://developer.nytimes.com/get-started) and create an account by providing the details asked.
* Verify the account using the link emailed to you.
* Sign in to the account you just created.
* From the top right corner where your username/email is present, select the “Apps” option in the dropdown and click on “New App”.
* Provide a suitable name for your app and amongst the APIs, select the Article Search API and the Top Stories API.
* Click ‘Create’.
* You will be taken to a page where you can find your API key.

### Contact
If you want to contact me you can reach me at <km69564@usc.edu> or <krishnamanoj14@gmail.com>.

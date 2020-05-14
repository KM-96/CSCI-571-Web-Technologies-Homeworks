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

### Project Setup - Creating ReactJS Components
* Go to the parent directory where you want create the UI project and run the following commands.
    ```
    npx create-react-app csci571-hw8-ui
    cd csci571-hw8-ui
    ```
* To start the server use the following command
    ```
    npm start
    ```
* By default, npm starts the react application on port 3000. If you want to run the applcation on a different port, then edit the package.json file.
    ```
    "scripts": {
        "start": "set PORT=8080 && react-scripts start"
    }
    ``` 
* Add react-bootstrap and bootstrap components to your project using the following command. 
    ```
    npm install --save react-bootstrap bootstrap
    ```
    For more details on how to link the bootstrap and react-bootstrap visit [here](https://react-bootstrap.github.io/getting-started/introduction/).
* To get switch (toggle) related features, install the react-switch package.
    ```
    npm install --save react-switch
    ```
* Add the react select module
    ```
    npm install --save react-select@next 
    ```
* Add react icons
    ```
    npm install --save react-icons
    ```
* Add react tooltip
    ```
    npm install --save react-tooltip 
    ```
* Add react spinner
    ```
    npm install --save react-spinners 
    ```
* Add react responsive design
    ```
    npm install --save react-responsive
    ```
* Add react router
    ```
    npm install --save react-router-dom
    ```
* Add react text truncate
    ```
     npm install --save react-text-truncate
    ```
* Add react toastify
    ```
    npm install --save react-toastify
    ```
* Add react share
    ```
    npm install --save react-share
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

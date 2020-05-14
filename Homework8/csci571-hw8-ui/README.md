This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.<br />
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.<br />
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.<br />
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br />
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).

### Code Splitting

This section has moved here: https://facebook.github.io/create-react-app/docs/code-splitting

### Analyzing the Bundle Size

This section has moved here: https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size

### Making a Progressive Web App

This section has moved here: https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app

### Advanced Configuration

This section has moved here: https://facebook.github.io/create-react-app/docs/advanced-configuration

### Deployment

This section has moved here: https://facebook.github.io/create-react-app/docs/deployment

### `npm run build` fails to minify

This section has moved here: https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify


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


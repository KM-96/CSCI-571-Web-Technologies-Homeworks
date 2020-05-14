const cors = require('cors');
const http =  require('http');
const express = require('express');
const guardian_router = require('./guardian_news_api');
const google_trends_router = require('./google_trends_api');
const app = express();

const port = process.env.PORT || 3000;
const server =  http.createServer(app);
server.listen(port);

const router = express.Router();

app.use(cors());
app.use("/", router);
app.use("/guardian-news", guardian_router);
app.use("/google-trends", google_trends_router);

module.exports = app;
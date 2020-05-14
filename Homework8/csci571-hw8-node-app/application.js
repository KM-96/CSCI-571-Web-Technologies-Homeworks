const cors = require('cors');
const http =  require('http');
const express = require('express');
const guardian_router = require('./guardian_news_api');
const new_york_times_router = require('./new_york_times_news_api');
const app = express();

const port = process.env.PORT || 3000;
const server =  http.createServer(app);
server.listen(port);

const router = express.Router();

app.use(cors());
app.use("/", router);
app.use("/guardian-news", guardian_router);
app.use("/new-york-times", new_york_times_router);

module.exports = app;
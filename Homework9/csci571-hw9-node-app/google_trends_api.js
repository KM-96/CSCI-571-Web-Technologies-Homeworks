const googleTrends = require('google-trends-api');
const request = require('request');
const express = require('express');
const router = express.Router();
const error = {
    status: 500,
    message: "Internal server error occoured. Please try later"
}
const default_start_time = '2019-06-01';

router.get("/", function(req, res){
    try {
        let search = req.query.search;
        console.log("search: " + search);
        let searchObject = {
            keyword: search,
            startTime: new Date(default_start_time)
        }
        console.log(searchObject);
        googleTrends.interestOverTime(searchObject)
        .then(function(results){
            let results_json = JSON.parse(results);
            return res.status(200).json(results_json);
        })
        .catch(function(err){
            console.log(err);
            return res.status(500).json(error);
        });
    } catch (e) {
        console.log(e);
        return res.status(500).json(error);
    }
});

module.exports = router;
var app = {
    first: null,
    firstScore: null,
    second: null,
    secondScore: null,
    third: null,
    thirdScore: null,

    ranks: null,

    query: null,

    getTimeInSeconde: function (rank) {
        return parseInt(rank.get('time'), 10) / 1000 + ' sec';
    },
    getScore: function (rank) {
        return rank.get('score') + ' pts / '
    },
    bindRank: function () {
        if (!this.ranks || this.ranks.length === 0) {
            return;
        }
        var first = this.ranks[0];
        this.first.innerHTML = first.get('name');
        this.firstScore.innerHTML = this.getScore(first) + this.getTimeInSeconde(first);
        if (this.ranks.length > 1) {
            var second = this.ranks[1];
            this.second.innerHTML = second.get('name');
            this.secondScore.innerHTML = this.getScore(second) + this.getTimeInSeconde(second);
        }
        if (this.ranks.length > 2) {
            var third = this.ranks[2];
            this.third.innerHTML = third.get('name');
            this.thirdScore.innerHTML = this.getScore(third) + this.getTimeInSeconde(third);
        }
    },
    initialize: function () {
        this.first = document.getElementById('first');
        this.firstScore = document.getElementById('firstScore');
        this.second = document.getElementById('second');
        this.secondScore = document.getElementById('secondScore');
        this.third = document.getElementById('third');
        this.thirdScore = document.getElementById('thirdScore');

        var Rank = Parse.Object.extend("Rank");
        this.query = new Parse.Query(Rank);
        this.query.limit(3);
        this.query.ascending("-score,time");
    },
    getRanking: function () {
        this.query.find({
            success: function (ranks) {
                app.ranks = ranks;
                app.bindRank();
            },
            error: function (e) {
                console.log('Cannot get ranks');
                console.log(e);
            }
        });
    }
};

window.onload = function () {
    Parse.initialize("FfcLhJ6gRektlXkgfUqKrZlvTztaGlSS2ADOdN9y", "JNdFp4k3LmYFRJEAdSDYGn8lUqE1nNZRSk6PSzQO");
    app.initialize();
    app.getRanking();
    setInterval(function () {
        app.getRanking();
    }, 5000);
};
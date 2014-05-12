Sarah 'Stocks' Plugin
=====================

This plugin lets Sarah check your stocks for you. Just ask
Sarah for your stock quotes, and she/it will go out and get them,
and speak them back to you.


Properties File
---------------

The properties files is written in JSON, and now supports multiple
phrases you can speak, as well as multiple stocks, and looks like this:

    {
        "phrases" : [
            "check stock prices", 
            "get stock prices", 
            "get stocks"
        ],
        "stocks": [
            { "stock": { "symbol": "AAPL", "name": "Apple", "price": "0" }},
            { "stock": { "symbol": "FB",   "name": "Face book", "price": "0" }},
            { "stock": { "symbol": "GOOG", "name": "Google", "price": "0" }}
        ]
    }



Developers
----------

* Build the plugin with the _build-jar.sh script.


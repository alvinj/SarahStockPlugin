Sarah 'Stocks' Plugin
=====================

This plugin lets Sarah check your stocks for you. Just ask
Sarah for your stock quotes, and she/it will go out and get them,
and speak them back to you.


Building
--------

To build the plugin, first make sure you have the correct version of
Sarah in your _lib_ folder. For instance, for the current version of
Sarah3, copy this file to your _lib_ folder:

    /Users/al/Projects/Scala/Sarah3/target/scala-2.10/Sarah3-assembly-0.1.jar

Once that's done, use this script to build the plugin:

    _build-jar.sh

That script creates a Jar file that you can copy to Sarah's deployment
directory, i.e., $HOME/Sarah.


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


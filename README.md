Sarah 'Stocks' Plugin
=====================

This plugin lets Sarah check your stocks for you. Just ask
Sarah for your stock quotes, and she/it will go out and get them,
and speak them back to you.

This plugin requires some configuration information, though (sadly)
I haven't documented that yet. (I'm just trying to get all this code 
into Github today before I have to move on to something else.)

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
      { "stock": {
        "symbol": "AAPL",
        "name": "Apple",
        "price": "0"
      }},
      { "stock": {
        "symbol": "GOOG",
        "name": "Google",
        "price": "0"
      }}
      ]
    }



Developers
----------

* `sbt package` now works, though i need to clean up the dependencies
  in the lib directory.
* as an example of how to deploy the resulting plugin, see the 
  script in the deploy/ directory. i'll add more docs when i can.


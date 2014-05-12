#!/bin/bash

sbt package

if [ $? != 0 ]
then
  echo "'sbt package' failed, exiting now"
  exit 1
fi

cp target/scala-2.10/stocks_2.10-0.1.jar Stocks.jar

ls -l Stocks.jar

echo ""
echo "Created Stocks.jar. Copy that file to /Users/al/Sarah/plugins/DDStocks, like this:"
echo "cp Stocks.jar /Users/al/Sarah/plugins/DDStocks"


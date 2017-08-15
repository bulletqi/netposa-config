#!/bin/sh

cd ..
echo "==== starting to build config-service===="

mvn clean package -DskipTests -pl apollo-configservice -am

echo "==== building config-service finished ===="

echo "==== starting to build portal ===="

mvn clean package -DskipTests -pl apollo-portal -am

echo "==== building portal finished ===="

echo "==== starting to build client ===="

mvn clean install -DskipTests -pl apollo-client -am

echo "==== building client finished ===="




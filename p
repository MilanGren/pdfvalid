
D=$PWD

cd /home/gre/java/maven/pdfvalid

mvn clean
mvn package #-Dmaven.test.skip=true 

cd $D


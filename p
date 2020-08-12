
D=$PWD

cd /home/gre/java/maven/pdfvalid/demo

mvn clean
mvn package -Dmaven.test.skip=true 

cd $D


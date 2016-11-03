#Setting CLASS PATH
 #Program setting file
export CLASSPATH=$CLASSPATH:../library/pool/commons-pool2-2.4.2.jar

case "$1" in
  "CreateAgentTest" ) java -cp $CLASSPATH rda.test.CreateAgentTest $2;;
esac
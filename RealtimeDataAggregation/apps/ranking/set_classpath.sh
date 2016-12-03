#Setting CLASS PATH
 #Program setting file
export CLASSPATH=$CLASSPATH:../../apps/system/property

 #Apach
export CLASSPATH=$CLASSPATH:\
../../lib/pool/commons-pool2-2.4.2.jar:\
../../lib/math/commons-math3-3.5.jar

 #Log setting file
export CLASSPATH=$CLASSPATH:\
../../lib/Log/logback-access-1.1.3.jar:\
../../lib/Log/logback-classic-1.1.3.jar:\
../../lib/Log/logback-core-1.1.3.jar:\
../../lib/Log/slf4j-api-1.7.12.jar

 #CSV setting file
export CLASSPATH=$CLASSPATH:\
../../lib/csv/opencsv-3.5.jar

##
export CLASSPATH=`echo $CLASSPATH | tr ':' '\n' | sort -u | paste -d: -s -`;
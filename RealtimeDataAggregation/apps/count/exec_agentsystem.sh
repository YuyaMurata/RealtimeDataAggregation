#Initialise
mkdir -p logs/history
rm -f vmstat.log

#Setting CLASS PATH
source ./set_classpath.sh

#{1..10}
#Experimental
for i in {1..2}
do

echo "$i 回目の実験." 

java -cp $CLASSPATH -Xms4096m -Xmx4096m rda.agent.disposer.Dispose

# Change Property (System Parameter)
#java -cp $CLASSPATH rda.property.RewriteProperty ${i}

vmstat -n -S m -a 1 | awk '{print strftime("%Y-%m-%d %H:%M:%S.000"), $0} { system(":") }' > vmstat.log &

java -cp $CLASSPATH -Xms4096m -Xmx4096m rda.main.AgentSystemMain

killall vmstat
cat vmstat.log | awk '{print $1 " " $2 "," $15 "," $16}' > vmstat.csv

cp -r ../property logs/

mv vmstat.* logs/

#zip & dropbox
mv logs `hostname`_`date +%Y%m%d%H%M%S`_logs
zip -r `hostname`_`date +%Y%m%d%H%M%S`_logs.zip *_logs

rm -fr *_logs

done

mkdir logsfd
mv *_logs.zip logsfd/
zip -r `hostname`_`date +%Y%m%d%H%M%S`_logsfd.zip logsfd
rm -fr logsfd

cp *_logsfd.zip $CETA_HOME/App/dropbox_log
dropbox upload $CETA_HOME/App/dropbox_log/*.zip logs/
mv $CETA_HOME/App/dropbox_log/*.zip $CETA_HOME/App/dropbox_log/old/
rm -f *_logsfd.zip

mv Results.csv `hostname`_`date +%Y%m%d%H%M%S`_results.csv
cp *_results.csv $CETA_HOME/App/dropbox_log
dropbox upload $CETA_HOME/App/dropbox_log/*.csv logs/
mv $CETA_HOME/App/dropbox_log/*.csv $CETA_HOME/App/dropbox_log/old/
rm -f *_results.csv
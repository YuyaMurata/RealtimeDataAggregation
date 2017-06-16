for i in 1
do
    #遠隔で実験用スクリプトを起動
    sshpass -p 11m35584 ssh root@h$i \
    "source /etc/profile;\
    cd $CETA_HOME/App/AgentSystem/RealtimeDataAggregation/RealtimeDataAggregation/apps/count;\
    chmod 777 *.sh;\
    killall -9 java;\
   ./start.sh  `hostname`"
done

#確認
#for i in 1
#do
    #遠隔で実験用スクリプトを起動
    sshpass -p 11m35584 ssh root@h$i \
    "source /etc/profile;\
    cd $CETA_HOME/App/AgentSystem/RealtimeDataAggregation/RealtimeDataAggregation/apps/count;\
    showRegion"
#done
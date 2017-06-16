for i in `seq 1 $1`
do
    #遠隔で実験用スクリプトを起動
    sshpass -p 11m35584 ssh root@h$i \
    "source /etc/profile;\
    cd $CETA_HOME/App/AgentSystem/RealtimeDataAggregation/RealtimeDataAggregation/apps/count;\
    chmod 777 *.sh;\
    killall -9 java;\
    su -l h$i -c 'cd $CETA_HOME/App/AgentSystem/RealtimeDataAggregation/RealtimeDataAggregation/apps/count;pwd'" 
done

sleep 5

#確認
#for i in `seq 1 $1`
#do
#    #遠隔で実験用スクリプトを起動
#    sshpass -p 11m35584 ssh root@h$i \
#    "source /etc/profile;\
#    cd $CETA_HOME/App/AgentSystem/RealtimeDataAggregation/RealtimeDataAggregation/apps/count;\
#    showRegion"
#done
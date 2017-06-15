for i in 1 2 3 4 5 6
do
    #確認
    sshpass -p 11m35584 ssh root@h$i 'echo `hostname`'
    echo "確認後 Enter"
    read

    #遠隔で実験用スクリプトを起動
    sshpass -p 11m35584 ssh root@$1 \
    "source /etc/profile;\
    cd $CETA_HOME/App/AgentSystem/RealtimeDataAggregation/RealtimeDataAggregation/app/count;\
    chmod 777 *.sh;\
    killall -9 java;\
    ./start.sh"
done
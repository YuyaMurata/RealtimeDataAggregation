for i in `seq 1 $1`
do
    #遠隔で実験用スクリプトを終了
    sshpass -p 11m35584 ssh root@h$i "killall -9 java" &
done

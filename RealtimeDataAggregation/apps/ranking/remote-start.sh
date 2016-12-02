#確認
sshpass -p 11m35584 ssh root@$1 'echo `hostname`'
echo "確認後 Enter"
read

#遠隔で実験用スクリプトを起動
sshpass -p 11m35584 ssh root@$1 \
"source /etc/profile;\
cd $CETA_HOME/App/AgentSystem/AgentSystem/setting;\
./git-remote.sh;\
chmod 777 *.sh;\
./remote-script.sh"

#ログをDropboxに送る
dropbox upload $CETA_HOME/App/dropbox_log/*.zip logs
mv $CETA_HOME/App/dropbox_log/*.zip $CETA_HOME/App/dropbox_log/old/
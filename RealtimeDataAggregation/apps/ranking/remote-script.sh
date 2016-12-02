#ディレクトリ確認
pwd

#サーバーの起動
./start.sh
sleep 15

#プログラムの実行 NumberOFAgents = N
./exec_system.sh 10

#Logを圧縮し, h1のdropboxにファイルを移動する
zip -r `hostname`_`date +%Y%m%d%H%M%S`.zip logs
sshpass -p 11m35584 scp `hostname`_*.zip root@h1:$CETA_HOME/App/dropbox_log

#生成ファイルの削除
rm -f `hostname`_*.zip
rm -fR logs/user*

#サーバーの終了
./stop.sh
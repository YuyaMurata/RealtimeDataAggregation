git fetch origin
git reset --hard origin/master

sleep 1

while getopts f OPT
do
  case $OPT in
    "f" ) FLG_F="TRUE" ;;
  esac
done

if [ "$FLG_F" = "TRUE" ]; then
    echo "delete of classes folder"
    rm -fR $CETA_HOME/classes/*
fi

ant -f ../../build.xml > agentsystem_ant-buid.log
tail -n 2 agentsystem_ant-buid.log

echo "test "$CETA_HOME
echo "11m35584" | sudo -S \cp -fr ../../build/classes/* $CETA_HOME/classes

chmod 777 *.sh

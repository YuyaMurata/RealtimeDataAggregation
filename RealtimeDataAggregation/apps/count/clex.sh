if [ $# -ne 1 ]; then
    echo $# clsql -catalog:$1:11000 -user:cetaadmin -password:cetaadmin
else
    echo $# clsql -user:cetaadmin -password:cetaadmin  
fi

if [ $# -ne 1 ]; then
    clsql -catalog:$1:11000 -user:cetaadmin -password:cetaadmin
else
  clsql -user:cetaadmin -password:cetaadmin  
fi

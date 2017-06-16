if [ $# -gt 0 ]; then
    startCatalog &&
    startServer $1.s0 $1.s1 $1a0 $1.a1
else
    startCatalog &&
    startServer s0 s1 a0 a1
fi
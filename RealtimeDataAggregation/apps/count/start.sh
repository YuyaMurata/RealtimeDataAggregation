if [ $# -gt 0 ]; then
    startCatalog &&
    startServer $1.s0 $1.s1 $1.a0 $1.a1
else
    startCatalog &&
    startServer h0.s0 h0.s1 h0.a0 h0.a1
fi
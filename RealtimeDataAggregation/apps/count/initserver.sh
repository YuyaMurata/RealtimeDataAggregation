#!/usr/bin/env bash
#
# Licensed Materials - Property of IBM
#
# "Restricted Materials of IBM"
#
# solidDB Cluster
#
# (C) Copyright IBM Corp. 2008, 2009 All Rights Reserved.
#
# US Government Users Restricted Rights - Use, duplication or
# disclosure restricted by GSA ADP Schedule Contract with
# IBM Corp.
#

echo =========================================
echo serverName=$1
echo serverTypeName=$2
echo regionName=$3
echo roleName=$4
echo status=$5
echo =========================================

#if [ "$2" == "solidDB" ]; then
#  if [ "$4" == "PRIMARY" ]; then
#    if [ ! -d ../regioncopy ]; then
#      mkdir ../regioncopy
#    fi
#    if [ -f ../regioncopy/move$3.sh ]; then
#      . ../regioncopy/move$3.sh $1
#    fi
#    cp ../movedata.sh.template .
#    sed -e "s/@SERVERNAME@/$1/g" "../movedata.sh.template" | sed -e "s/@REGIONNAME@/$3/g" > ../regioncopy/move$3.sh
#    chmod u+x ../regioncopy/move$3.sh
#  fi
#fi


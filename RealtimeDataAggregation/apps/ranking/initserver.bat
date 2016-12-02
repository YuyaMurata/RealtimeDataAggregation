@echo =========================================
@echo serverName=%1
@echo serverTypeName=%2
@echo regionName=%3
@echo roleName=%4
@echo status=%5
@echo =========================================
@echo off
@ping localhost
@ping localhost
@ping localhost
rem setlocal enabledelayedexpansion

rem if "%2" == "solidDB" (
rem  if  "%4" == "PRIMARY" (
rem    if not exist "..\regioncopy" (
rem      md ..\regioncopy
rem    )
rem    if exist "..\regioncopy\move%3.bat" (
rem      call "..\regioncopy\move%3.bat" %1
rem    )
rem    sed -e "s/@SERVERNAME@/%1/g" "..\movedata.bat.template" | sed -e "s/@REGIONNAME@/%3/g" > ..\regioncopy\move%3.bat
rem  )
rem ) 

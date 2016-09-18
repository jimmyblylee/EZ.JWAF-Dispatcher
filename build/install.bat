@echo off
title Installing EZ.JWAF Dispatcher
call msg info "[INFO] Installing EZ.JWAF Dispatcher" & echo.

set BASEDIR=%~sdp0

pushd %BASEDIR%\..\src
  call mvn clean install -DperformTest=true -DperformSource=true
popd

call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 

timeout /t 10 >NUL 
@echo off
echo Ejecutando Formula 1 Simulator...
cd /d "%~dp0"
call mvn clean javafx:run
pause

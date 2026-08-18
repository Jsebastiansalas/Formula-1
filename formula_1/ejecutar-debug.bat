@echo off
echo Ejecutando Formula 1 con logs completos...
cd /d "%~dp0"

set JAVAFX_PATH=C:\Users\USUARIO\.m2\repository\org\openjfx
set JAVAFX_VERSION=21.0.1

java --module-path "%JAVAFX_PATH%\javafx-controls\%JAVAFX_VERSION%\javafx-controls-%JAVAFX_VERSION%-win.jar;%JAVAFX_PATH%\javafx-graphics\%JAVAFX_VERSION%\javafx-graphics-%JAVAFX_VERSION%-win.jar;%JAVAFX_PATH%\javafx-base\%JAVAFX_VERSION%\javafx-base-%JAVAFX_VERSION%-win.jar" --add-modules javafx.controls,javafx.fxml -cp "target\classes;C:\Users\USUARIO\.m2\repository\com\google\code\gson\gson\2.11.0\gson-2.11.0.jar" com.formula1.ui.AppFX 2>&1

pause

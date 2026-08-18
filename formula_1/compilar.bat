@echo off
echo Compilando proyecto Formula 1...
cd /d "%~dp0"
mvn clean compile
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo COMPILACION EXITOSA
    echo ========================================
    echo.
    echo Para ejecutar la aplicacion usa:
    echo mvn javafx:run
    echo.
) else (
    echo.
    echo ========================================
    echo ERROR EN LA COMPILACION
    echo ========================================
    echo.
)
pause

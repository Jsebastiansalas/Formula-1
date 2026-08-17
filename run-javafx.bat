@echo off
echo ================================================
echo   Simulador F1 - Iniciando aplicacion JavaFX
echo ================================================
echo.

REM Verificar si existe Maven
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Maven encontrado. Ejecutando con Maven...
    cd /d "%~dp0formula_1"
    call mvn clean javafx:run
    pause
    exit /b
)

echo Maven no encontrado. Instalando dependencias manualmente...
echo.

REM Crear directorio para JavaFX si no existe
set JAVAFX_DIR=%~dp0javafx-sdk
if not exist "%JAVAFX_DIR%" (
    echo Descargando JavaFX SDK...
    echo Por favor, descarga JavaFX SDK manualmente desde:
    echo https://gluonhq.com/products/javafx/
    echo.
    echo Descomprime el archivo en: %JAVAFX_DIR%
    echo Luego vuelve a ejecutar este script.
    pause
    exit /b
)

echo JavaFX encontrado en: %JAVAFX_DIR%
echo Compilando proyecto...

cd /d "%~dp0formula_1"

REM Compilar el proyecto
javac -cp ".;%JAVAFX_DIR%\lib\*;target\classes;lib\gson-2.11.0.jar" ^
    -d target\classes ^
    src\main\java\com\formula1\modelo\*.java ^
    src\main\java\com\formula1\gestor\*.java ^
    src\main\java\com\formula1\simulacion\*.java ^
    src\main\java\com\formula1\almacenamiento\*.java ^
    src\main\java\com\formula1\ui\*.java ^
    src\main\java\com\formula1\Main.java

if %ERRORLEVEL% NEQ 0 (
    echo Error al compilar el proyecto
    pause
    exit /b
)

echo Compilacion exitosa. Ejecutando aplicacion...

REM Ejecutar la aplicación JavaFX
java --module-path "%JAVAFX_DIR%\lib" ^
     --add-modules javafx.controls,javafx.fxml,javafx.graphics ^
     -cp "target\classes;lib\gson-2.11.0.jar" ^
     com.formula1.ui.AppFX

pause

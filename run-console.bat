@echo off
echo ================================================
echo   Simulador F1 - Version Consola
echo ================================================
echo.

cd /d "%~dp0formula_1"

REM Crear directorio target si no existe
if not exist "target\classes" mkdir target\classes

REM Copiar recursos
xcopy /E /I /Y src\main\resources target\classes 2>nul

echo Compilando proyecto...

REM Compilar el proyecto
javac -cp ".;lib\gson-2.11.0.jar" ^
    -d target\classes ^
    src\main\java\com\formula1\modelo\*.java ^
    src\main\java\com\formula1\gestor\*.java ^
    src\main\java\com\formula1\simulacion\*.java ^
    src\main\java\com\formula1\almacenamiento\*.java ^
    src\main\java\com\formula1\Main.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Error al compilar el proyecto
    pause
    exit /b
)

echo Compilacion exitosa!
echo.
echo Ejecutando simulador en modo consola...
echo.

REM Ejecutar la aplicación en consola
java -cp "target\classes;lib\gson-2.11.0.jar" com.formula1.Main

pause

@echo off
echo ================================================
echo   Simulador F1 - JavaFX con Maven Local
echo ================================================
echo.

cd /d "%~dp0formula_1"

REM Usar JDK 26 (con comillas para manejar espacios)
set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.2"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Usar Maven local
set "MAVEN_HOME=%~dp0apache-maven-3.9.16"
set "PATH=%MAVEN_HOME%\bin;%PATH%"

REM Configurar Maven para usar el JDK correcto
set "MAVEN_OPTS=-Dmaven.compiler.fork=true"

echo Verificando versiones...
call "%JAVA_HOME%\bin\java" -version
echo.
call "%MAVEN_HOME%\bin\mvn" --version
echo.

echo Compilando y ejecutando aplicacion JavaFX...
echo (Esto puede tardar la primera vez mientras descarga las librerias)
echo.

call "%MAVEN_HOME%\bin\mvn" clean javafx:run

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Error al ejecutar la aplicacion
    pause
    exit /b
)

pause

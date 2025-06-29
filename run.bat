@echo off
chcp 65001 >nul
echo Rinkko Game Runner Script for Windows
echo =====================================

rem Set Java path
set JAVA_BIN=C:\Program Files\Java\jdk-21\bin
set PROJECT_ROOT=%~dp0

rem Change to project directory
cd /d "%PROJECT_ROOT%"

rem Create target directories if they don't exist
if not exist "target\classes" mkdir target\classes
if not exist "target\dependency" mkdir target\dependency

rem Download SQLite dependency if not present
if not exist "target\dependency\sqlite-jdbc-3.45.1.0.jar" (
    echo Downloading SQLite JDBC dependency...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar' -OutFile 'target\dependency\sqlite-jdbc-3.45.1.0.jar'"
    if errorlevel 1 (
        echo Failed to download dependency. Please check your internet connection.
        pause
        exit /b 1
    )
)

rem Compile Java sources
echo Compiling Java sources...
"%JAVA_BIN%\javac.exe" -encoding UTF-8 -cp target\dependency\sqlite-jdbc-3.45.1.0.jar -d target\classes src\com\example\game\*.java src\com\example\game\db\*.java src\com\example\pet\*.java src\com\example\item\*.java

rem Check compilation success
if errorlevel 1 (
    echo Compilation failed. Please check for errors above.
    pause
    exit /b 1
)

echo Compilation successful. Starting Rinkko game...
echo ===========================================

rem Run the game with proper Windows classpath and UTF-8 encoding
"%JAVA_BIN%\java.exe" -Dfile.encoding=UTF-8 -Dconsole.encoding=UTF-8 -cp "target\classes;target\dependency\sqlite-jdbc-3.45.1.0.jar" com.example.game.Game

echo.
echo Game ended. Press any key to exit...
pause >nul
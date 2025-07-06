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

rem Download dependencies if not present
if not exist "target\dependency\sqlite-jdbc-3.45.1.0.jar" (
    echo Downloading SQLite JDBC dependency...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar' -OutFile 'target\dependency\sqlite-jdbc-3.45.1.0.jar'"
    if errorlevel 1 (
        echo Failed to download dependency. Please check your internet connection.
        pause
        exit /b 1
    )
)

if not exist "target\dependency\slf4j-simple-2.0.9.jar" (
    echo Downloading SLF4J Simple dependency...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar' -OutFile 'target\dependency\slf4j-simple-2.0.9.jar'"
    if errorlevel 1 (
        echo Failed to download SLF4J Simple dependency.
        pause
        exit /b 1
    )
)

if not exist "target\dependency\slf4j-api-2.0.9.jar" (
    echo Downloading SLF4J API dependency...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar' -OutFile 'target\dependency\slf4j-api-2.0.9.jar'"
    if errorlevel 1 (
        echo Failed to download SLF4J API dependency.
        pause
        exit /b 1
    )
)

if not exist "target\dependency\jline-terminal-3.25.1.jar" (
    echo Downloading JLine Terminal dependency...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/jline/jline-terminal/3.25.1/jline-terminal-3.25.1.jar' -OutFile 'target\dependency\jline-terminal-3.25.1.jar'"
    if errorlevel 1 (
        echo Failed to download JLine Terminal dependency.
        pause
        exit /b 1
    )
)

if not exist "target\dependency\jline-reader-3.25.1.jar" (
    echo Downloading JLine Reader dependency...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/jline/jline-reader/3.25.1/jline-reader-3.25.1.jar' -OutFile 'target\dependency\jline-reader-3.25.1.jar'"
    if errorlevel 1 (
        echo Failed to download JLine Reader dependency.
        pause
        exit /b 1
    )
)

rem Compile Java sources (Console version only - exclude Spring Boot classes)
echo Compiling Java sources...
"%JAVA_BIN%\javac.exe" -encoding UTF-8 -cp "target\dependency\sqlite-jdbc-3.45.1.0.jar;target\dependency\slf4j-simple-2.0.9.jar;target\dependency\slf4j-api-2.0.9.jar;target\dependency\jline-terminal-3.25.1.jar;target\dependency\jline-reader-3.25.1.jar" -d target\classes src\main\java\com\example\game\Game.java src\main\java\com\example\game\Player.java src\main\java\com\example\game\db\*.java src\main\java\com\example\pet\*.java src\main\java\com\example\item\*.java

rem Check compilation success
if errorlevel 1 (
    echo Compilation failed. Please check for errors above.
    pause
    exit /b 1
)

echo Compilation successful. Starting Rinkko game...
echo ===========================================

rem Run the game with proper Windows classpath and UTF-8 encoding
"%JAVA_BIN%\java.exe" -Dfile.encoding=UTF-8 -Dconsole.encoding=UTF-8 -cp "target\classes;target\dependency\sqlite-jdbc-3.45.1.0.jar;target\dependency\slf4j-simple-2.0.9.jar;target\dependency\slf4j-api-2.0.9.jar;target\dependency\jline-terminal-3.25.1.jar;target\dependency\jline-reader-3.25.1.jar" com.example.game.Game

echo.
echo Game ended. Press any key to exit...
pause >nul
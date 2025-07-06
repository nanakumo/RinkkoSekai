# Rinkko Game Runner Script for Windows PowerShell
# Set UTF-8 encoding for the console
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "Rinkko Game Runner Script for Windows PowerShell" -ForegroundColor Green
Write-Host "=================================================" -ForegroundColor Green

# Set Java path
$JavaBin = "C:\Program Files\Java\jdk-21\bin"
$ProjectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path

# Change to project directory
Set-Location $ProjectRoot

# Create target directories if they don't exist
if (!(Test-Path "target\classes")) { New-Item -ItemType Directory -Path "target\classes" -Force | Out-Null }
if (!(Test-Path "target\dependency")) { New-Item -ItemType Directory -Path "target\dependency" -Force | Out-Null }

# Download dependencies if not present
$SqliteJar = "target\dependency\sqlite-jdbc-3.45.1.0.jar"
$Slf4jSimpleJar = "target\dependency\slf4j-simple-2.0.9.jar"
$Slf4jApiJar = "target\dependency\slf4j-api-2.0.9.jar"

if (!(Test-Path $SqliteJar)) {
    Write-Host "Downloading SQLite JDBC dependency..." -ForegroundColor Yellow
    try {
        Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar" -OutFile $SqliteJar
        Write-Host "SQLite JDBC download completed successfully." -ForegroundColor Green
    }
    catch {
        Write-Host "Failed to download SQLite JDBC dependency: $($_.Exception.Message)" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}

if (!(Test-Path $Slf4jSimpleJar)) {
    Write-Host "Downloading SLF4J Simple dependency..." -ForegroundColor Yellow
    try {
        Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar" -OutFile $Slf4jSimpleJar
        Write-Host "SLF4J Simple download completed successfully." -ForegroundColor Green
    }
    catch {
        Write-Host "Failed to download SLF4J Simple dependency: $($_.Exception.Message)" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}

if (!(Test-Path $Slf4jApiJar)) {
    Write-Host "Downloading SLF4J API dependency..." -ForegroundColor Yellow
    try {
        Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar" -OutFile $Slf4jApiJar
        Write-Host "SLF4J API download completed successfully." -ForegroundColor Green
    }
    catch {
        Write-Host "Failed to download SLF4J API dependency: $($_.Exception.Message)" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}

# Compile Java sources
Write-Host "Compiling Java sources..." -ForegroundColor Yellow
$CompileCommand = "`"$JavaBin\javac.exe`" -encoding UTF-8 -cp `"$SqliteJar;$Slf4jSimpleJar;$Slf4jApiJar`" -d target\classes src\com\example\game\*.java src\com\example\game\db\*.java src\com\example\pet\*.java src\com\example\item\*.java"

try {
    Invoke-Expression $CompileCommand
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Compilation successful. Starting Rinkko game..." -ForegroundColor Green
        Write-Host "===========================================" -ForegroundColor Green
        
        # Run the game
        $RunCommand = "`"$JavaBin\java.exe`" -Dfile.encoding=UTF-8 -Dconsole.encoding=UTF-8 -cp `"target\classes;$SqliteJar;$Slf4jSimpleJar;$Slf4jApiJar`" com.example.game.Game"
        Invoke-Expression $RunCommand
    }
    else {
        Write-Host "Compilation failed. Please check for errors above." -ForegroundColor Red
    }
}
catch {
    Write-Host "Error during compilation: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "Game ended. Press Enter to exit..."
Read-Host
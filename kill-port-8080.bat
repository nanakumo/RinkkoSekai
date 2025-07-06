@echo off
chcp 65001 >nul
echo 检查并关闭占用端口8080的进程...
echo.

echo 查找占用端口8080的进程：
netstat -ano | findstr :8080

for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    echo 找到进程ID: %%a
    echo 正在终止进程...
    taskkill /PID %%a /F
)

echo.
echo 端口8080已释放！现在可以运行start-game.bat
pause
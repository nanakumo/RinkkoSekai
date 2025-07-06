@echo off
echo 正在启动凛喵喵游戏Web版...
echo.
echo 🐱 凛喵喵世界 - Web版游戏启动器
echo ================================
echo.
echo 正在检查Java环境...
java -version 2>nul
if errorlevel 1 (
    echo ❌ 错误：未找到Java环境
    echo 请确保已安装Java并配置PATH环境变量
    pause
    exit /b 1
)

echo ✅ Java环境检查通过
echo.
echo 正在启动游戏服务器...
echo 启动成功后请访问: http://localhost:8080
echo 按 Ctrl+C 可停止服务器
echo.

java -cp "target/classes;lib/*" com.example.game.RinkkoWebApplication

echo.
echo 游戏服务器已停止
pause
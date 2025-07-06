# 🐱 凛喵喵世界 - Rinkko Game

一个可爱的虚拟宠物模拟游戏，现在有Web版本了！

## 🎮 游戏特色

- 领养可爱的凛喵喵
- 喂食、喝水、玩耍、治疗
- 宠物状态实时显示
- SQLite数据库持久化存储
- 现代Web界面

## 🚀 快速开始

### 前置要求
- Java 17或更高版本
- Git（用于克隆仓库）

### 运行步骤

1. **克隆仓库**
   ```bash
   git clone <你的仓库地址>
   cd Rinkko
   ```

2. **启动游戏**
   
   **Windows用户：**
   ```bash
   start-game.bat
   ```
   
   **Linux/Mac用户：**
   ```bash
   chmod +x start-game.sh
   ./start-game.sh
   ```

3. **开始游戏**
   - 启动成功后，浏览器访问：`http://localhost:8080`
   - 开始你的凛喵喵养成之旅！

## 🎯 游戏说明

### 基本操作
- **领养宠物**：在主页输入名字创建新的凛喵喵
- **查看状态**：点击"照顾"按钮查看宠物详细信息
- **喂食**：使用金币购买食物恢复宠物饥饿度
- **喝水**：购买饮品恢复口渴度
- **娱乐**：和宠物玩耍提升心情和好感度
- **治疗**：使用药品恢复宠物健康

### 宠物属性
- **饥饿度** (0-100)：过低会影响健康
- **口渴度** (0-100)：需要定期补充水分
- **心情** (0-100)：影响宠物的整体状态
- **健康** (0-100)：宠物的身体状况
- **好感度** (0-100)：对主人的感情

### 游戏提示
- 初始金币：100
- 记得定期照顾你的凛喵喵
- 金币不足时无法购买物品
- 游戏数据会自动保存

## 🛠️ 技术栈

- **后端**：Java 17 + Spring Boot 3.2.0
- **前端**：Thymeleaf模板引擎 + HTML/CSS
- **数据库**：SQLite
- **Web服务器**：内嵌Tomcat

## 📁 项目结构

```
Rinkko/
├── src/main/java/
│   └── com/example/
│       ├── game/          # 游戏核心逻辑
│       ├── item/          # 物品系统
│       └── pet/           # 宠物系统
├── src/main/resources/
│   ├── templates/         # HTML模板
│   └── static/           # CSS样式
├── lib/                  # Spring Boot依赖
├── start-game.bat        # Windows启动脚本
├── start-game.sh         # Linux/Mac启动脚本
└── README.md            # 说明文档
```

## 🔧 故障排除

### 常见问题

**Q: 运行时提示找不到Java？**
A: 请确保安装了Java 17或更高版本，并正确配置了PATH环境变量。

**Q: 8080端口被占用？**
A: 请关闭占用8080端口的其他程序，或修改application.properties中的端口配置。

**Q: 网页打不开？**
A: 确认控制台显示"Tomcat started on port(s): 8080"，然后访问 http://localhost:8080

**Q: 宠物数据丢失？**
A: 游戏数据保存在rinkko_game.db文件中，请不要删除此文件。

## 🎨 版本历史

- **v1.0** - 控制台版本
- **v2.0** - Web版本（当前）

## 📄 开源协议

MIT License - 可自由使用和修改

## 🤝 贡献

欢迎提交Issue和Pull Request！

---

*🐱 愿你和你的凛喵喵度过美好时光！*
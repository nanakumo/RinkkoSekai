package com.example.game;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SimpleHttpServer {
    
    public static void main(String[] args) throws IOException {
        // 创建HTTP服务器，监听8080端口
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // 设置根路径处理器
        server.createContext("/", new RootHandler());
        server.createContext("/test", new TestHandler());
        
        // 启动服务器
        server.setExecutor(null);
        server.start();
        
        System.out.println("🎉 凛喵喵游戏服务器已启动！");
        System.out.println("📍 请访问: http://localhost:8080");
        System.out.println("🔧 测试页面: http://localhost:8080/test");
        System.out.println("⏹️  按 Ctrl+C 停止服务器");
    }
    
    static class RootHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                <!DOCTYPE html>
                <html lang="zh-CN">
                <head>
                    <meta charset="UTF-8">
                    <title>凛喵喵世界</title>
                    <style>
                        body { font-family: Arial, sans-serif; text-align: center; margin: 50px; }
                        h1 { color: #ff6b6b; }
                        .button { background: #4ecdc4; color: white; padding: 10px 20px; 
                                 text-decoration: none; border-radius: 5px; margin: 10px; }
                    </style>
                </head>
                <body>
                    <h1>🐱 欢迎来到凛喵喵世界！</h1>
                    <p>简单的Web服务器测试成功！</p>
                    <p>Java版本: """ + System.getProperty("java.version") + """
                    </p>
                    <a href="/test" class="button">测试页面</a>
                    <p><em>下一步将集成完整的Spring Boot游戏功能</em></p>
                </body>
                </html>
                """;
            
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes("UTF-8"));
            os.close();
        }
    }
    
    static class TestHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                <!DOCTYPE html>
                <html lang="zh-CN">
                <head>
                    <meta charset="UTF-8">
                    <title>测试页面 - 凛喵喵世界</title>
                    <style>
                        body { font-family: Arial, sans-serif; text-align: center; margin: 50px; }
                        h1 { color: #4ecdc4; }
                        .success { color: #00b894; font-weight: bold; }
                    </style>
                </head>
                <body>
                    <h1>✅ 测试成功！</h1>
                    <p class="success">Web服务器运行正常</p>
                    <p>服务器时间: """ + new java.util.Date() + """
                    </p>
                    <a href="/">返回首页</a>
                </body>
                </html>
                """;
            
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes("UTF-8"));
            os.close();
        }
    }
}
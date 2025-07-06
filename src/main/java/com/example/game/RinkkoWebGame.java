package com.example.game;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.game.db.DatabaseManager;
import com.example.game.db.PlayerDAO;
import com.example.game.db.RinkkoDAO;
import com.example.pet.Rinkko;
import com.example.item.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RinkkoWebGame {
    private static PlayerDAO playerDAO;
    private static RinkkoDAO rinkkoDAO;
    private static Player player;
    
    public static void main(String[] args) throws IOException {
        // 初始化数据库
        DatabaseManager.initializeDatabase();
        playerDAO = new PlayerDAO();
        rinkkoDAO = new RinkkoDAO();
        loadOrCreatePlayer();
        
        // 创建HTTP服务器
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // 设置路由
        server.createContext("/", new GameHandler());
        server.createContext("/create-pet", new CreatePetHandler());
        server.createContext("/pet/", new PetDetailHandler());
        server.createContext("/feed/", new FeedHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("🎉 凛喵喵游戏Web版已启动！");
        System.out.println("📍 请访问: http://localhost:8080");
        System.out.println("⏹️  按 Ctrl+C 停止服务器");
    }
    
    private static void loadOrCreatePlayer() {
        player = playerDAO.findById(1);
        if (player == null) {
            player = new Player();
            player.setId(1);
            player.setMoney(100);
            playerDAO.save(player);
        }
        
        List<Rinkko> pets = rinkkoDAO.findByPlayerId(player.getId());
        for (Rinkko pet : pets) {
            player.addPet(pet);
        }
    }
    
    // 主页处理器
    static class GameHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = generateHomePage();
            sendResponse(exchange, response);
        }
    }
    
    // 创建宠物处理器
    static class CreatePetHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, String> params = parseFormData(body);
                String name = params.get("name");
                
                if (name != null && !name.trim().isEmpty()) {
                    Rinkko newPet = new Rinkko(name.trim());
                    newPet.setPlayerId(player.getId());
                    rinkkoDAO.save(newPet, player.getId());
                    player.addPet(newPet);
                }
            }
            
            // 重定向到主页
            exchange.getResponseHeaders().set("Location", "/");
            exchange.sendResponseHeaders(302, -1);
        }
    }
    
    // 宠物详情处理器
    static class PetDetailHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");
            if (parts.length >= 3) {
                try {
                    int petId = Integer.parseInt(parts[2]);
                    Rinkko pet = rinkkoDAO.findById(petId);
                    if (pet != null) {
                        String response = generatePetDetailPage(pet);
                        sendResponse(exchange, response);
                        return;
                    }
                } catch (NumberFormatException ignored) {}
            }
            
            // 重定向到主页
            exchange.getResponseHeaders().set("Location", "/");
            exchange.sendResponseHeaders(302, -1);
        }
    }
    
    // 喂食处理器
    static class FeedHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String path = exchange.getRequestURI().getPath();
                String[] parts = path.split("/");
                if (parts.length >= 3) {
                    try {
                        int petId = Integer.parseInt(parts[2]);
                        Rinkko pet = rinkkoDAO.findById(petId);
                        if (pet != null && player.getMoney() >= 10) {
                            FoodItem food = new FoodItem("普通汉堡", 10, 30, 5);
                            pet.eat(food);
                            player.setMoney(player.getMoney() - 10);
                            rinkkoDAO.update(pet);
                            playerDAO.save(player);
                        }
                        
                        exchange.getResponseHeaders().set("Location", "/pet/" + petId);
                        exchange.sendResponseHeaders(302, -1);
                        return;
                    } catch (NumberFormatException ignored) {}
                }
            }
            
            exchange.getResponseHeaders().set("Location", "/");
            exchange.sendResponseHeaders(302, -1);
        }
    }
    
    private static String generateHomePage() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='zh-CN'><head><meta charset='UTF-8'>");
        html.append("<title>凛喵喵世界</title>");
        html.append("<style>body{font-family:Arial;text-align:center;margin:50px;}");
        html.append("h1{color:#ff6b6b;}.pet{border:1px solid #ddd;margin:10px;padding:15px;border-radius:10px;}");
        html.append(".btn{background:#4ecdc4;color:white;padding:8px 16px;text-decoration:none;border-radius:5px;margin:5px;border:none;cursor:pointer;}");
        html.append("</style></head><body>");
        
        html.append("<h1>🐱 凛喵喵世界</h1>");
        html.append("<p>当前金币: <strong>").append(player.getMoney()).append("</strong></p>");
        
        html.append("<h2>我的凛喵喵们</h2>");
        if (player.getPets().isEmpty()) {
            html.append("<p>你还没有凛喵喵，快去领养一只吧！</p>");
        } else {
            for (Rinkko pet : player.getPets()) {
                html.append("<div class='pet'>");
                html.append("<h3>").append(pet.getName()).append("</h3>");
                html.append("<p>饥饿度: ").append(pet.getHunger()).append("/100</p>");
                html.append("<p>口渴度: ").append(pet.getThirst()).append("/100</p>");
                html.append("<p>心情: ").append(pet.getMood()).append("/100</p>");
                html.append("<p>健康: ").append(pet.getHealth()).append("/100</p>");
                html.append("<p>好感度: ").append(pet.getAffection()).append("/100</p>");
                html.append("<a href='/pet/").append(pet.getId()).append("' class='btn'>照顾</a>");
                html.append("</div>");
            }
        }
        
        html.append("<h2>领养新凛喵喵</h2>");
        html.append("<form method='post' action='/create-pet'>");
        html.append("<input type='text' name='name' placeholder='给凛喵喵起个名字' required>");
        html.append("<button type='submit' class='btn'>领养</button>");
        html.append("</form>");
        
        html.append("</body></html>");
        return html.toString();
    }
    
    private static String generatePetDetailPage(Rinkko pet) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='zh-CN'><head><meta charset='UTF-8'>");
        html.append("<title>").append(pet.getName()).append(" - 凛喵喵世界</title>");
        html.append("<style>body{font-family:Arial;text-align:center;margin:50px;}");
        html.append("h1{color:#ff6b6b;}.btn{background:#4ecdc4;color:white;padding:10px 20px;text-decoration:none;border-radius:5px;margin:10px;border:none;cursor:pointer;}");
        html.append(".action{margin:20px;padding:15px;border:1px solid #ddd;border-radius:10px;}");
        html.append("</style></head><body>");
        
        html.append("<h1>🐱 ").append(pet.getName()).append("</h1>");
        html.append("<p>当前金币: <strong>").append(player.getMoney()).append("</strong></p>");
        
        html.append("<div class='action'>");
        html.append("<h3>宠物状态</h3>");
        html.append("<p>饥饿度: ").append(pet.getHunger()).append("/100</p>");
        html.append("<p>口渴度: ").append(pet.getThirst()).append("/100</p>");
        html.append("<p>心情: ").append(pet.getMood()).append("/100</p>");
        html.append("<p>健康: ").append(pet.getHealth()).append("/100</p>");
        html.append("<p>好感度: ").append(pet.getAffection()).append("/100</p>");
        html.append("</div>");
        
        html.append("<div class='action'>");
        html.append("<h3>🍔 喂食</h3>");
        html.append("<p>普通汉堡 - 恢复30饥饿度，+5心情 (10金币)</p>");
        html.append("<form method='post' action='/feed/").append(pet.getId()).append("'>");
        html.append("<button type='submit' class='btn'>喂食</button>");
        html.append("</form>");
        html.append("</div>");
        
        html.append("<a href='/' class='btn'>返回主页</a>");
        html.append("</body></html>");
        return html.toString();
    }
    
    private static Map<String, String> parseFormData(String formData) {
        Map<String, String> params = new HashMap<>();
        try {
            String[] pairs = formData.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    params.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }
    
    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes("UTF-8"));
        os.close();
    }
}
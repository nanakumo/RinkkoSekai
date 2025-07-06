package com.example.game;

import com.example.pet.Rinkko;
import com.example.item.*;
import com.example.game.db.DatabaseManager;
import com.example.game.db.RinkkoDAO;
import com.example.game.db.PlayerDAO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class GameController {
    private Player player;
    private Random random;
    private int currentLevel = 1;
    private static final int MAX_LEVEL = 5;
    
    private List<FoodItem> availableFoods;
    private List<DrinkItem> availableDrinks;
    private List<PlayActivityItem> availablePlayActivities;
    private List<MedicineItem> availableMedicines;
    
    private RinkkoDAO rinkkoDAO;
    private PlayerDAO playerDAO;

    public GameController() {
        DatabaseManager.initializeDatabase();
        this.rinkkoDAO = new RinkkoDAO();
        this.playerDAO = new PlayerDAO();
        this.random = new Random();
        initializeItems();
    }

    private void loadOrCreatePlayer() {
        this.player = playerDAO.findById(1);
        if (player == null) {
            player = new Player();
            player.setId(1);
            player.setMoney(100);
            playerDAO.save(player);
        }
        
        List<Rinkko> pets = rinkkoDAO.findByPlayerId(player.getId());
        player.setRinkkoList(new ArrayList<>(pets));
    }

    private void initializeItems() {
        this.availableFoods = new ArrayList<>();
        availableFoods.add(new FoodItem("普通汉堡", 10, 30, 5));
        availableFoods.add(new FoodItem("美味炸鸡", 25, 50, 10));

        this.availableDrinks = new ArrayList<>();
        availableDrinks.add(new DrinkItem("干净的水", 5, 30, 2));
        availableDrinks.add(new DrinkItem("肥宅快乐", 15, 45, 5));

        this.availablePlayActivities = new ArrayList<>();
        availablePlayActivities.add(new PlayActivityItem("玩球", 10, 30, 5, -5));
        availablePlayActivities.add(new PlayActivityItem("看电视", 20, 25, 15, -8));

        this.availableMedicines = new ArrayList<>();
        availableMedicines.add(new MedicineItem("感冒药", 30, 40, -5));
        availableMedicines.add(new MedicineItem("营养剂", 50, 60, -10));
    }

    @GetMapping("/")
    public String home(Model model) {
        try {
            loadOrCreatePlayer();
            model.addAttribute("player", player);
            model.addAttribute("currentLevel", currentLevel);
            return "game";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "加载游戏数据失败: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/pet/{id}")
    public String petDetail(@PathVariable int id, Model model) {
        Rinkko pet = rinkkoDAO.findById(id);
        if (pet != null) {
            model.addAttribute("pet", pet);
            model.addAttribute("player", player);
            return "pet-detail";
        }
        return "redirect:/";
    }

    @PostMapping("/create-pet")
    public String createPet(@RequestParam String name) {
        if (player.getRinkkoList().size() >= currentLevel) {
            return "redirect:/?error=max_pets";
        }
        
        Rinkko newPet = new Rinkko(name);
        newPet.setPlayerId(player.getId());
        rinkkoDAO.save(newPet, player.getId());
        
        return "redirect:/";
    }

    @PostMapping("/feed/{id}")
    public String feedPet(@PathVariable int id, @RequestParam int foodIndex) {
        Rinkko pet = rinkkoDAO.findById(id);
        if (pet != null && foodIndex >= 0 && foodIndex < availableFoods.size()) {
            FoodItem food = availableFoods.get(foodIndex);
            if (player.getMoney() >= food.getCost()) {
                player.setMoney(player.getMoney() - food.getCost());
                pet.eat(food);
                rinkkoDAO.update(pet);
                playerDAO.save(player);
            }
        }
        return "redirect:/pet/" + id;
    }

    @PostMapping("/drink/{id}")
    public String giveDrink(@PathVariable int id, @RequestParam int drinkIndex) {
        Rinkko pet = rinkkoDAO.findById(id);
        if (pet != null && drinkIndex >= 0 && drinkIndex < availableDrinks.size()) {
            DrinkItem drink = availableDrinks.get(drinkIndex);
            if (player.getMoney() >= drink.getCost()) {
                player.setMoney(player.getMoney() - drink.getCost());
                pet.drink(drink);
                rinkkoDAO.update(pet);
                playerDAO.save(player);
            }
        }
        return "redirect:/pet/" + id;
    }

    @PostMapping("/play/{id}")
    public String playWithPet(@PathVariable int id, @RequestParam int playIndex) {
        Rinkko pet = rinkkoDAO.findById(id);
        if (pet != null && playIndex >= 0 && playIndex < availablePlayActivities.size()) {
            PlayActivityItem activity = availablePlayActivities.get(playIndex);
            if (player.getMoney() >= activity.getCost()) {
                player.setMoney(player.getMoney() - activity.getCost());
                pet.playWith(activity);
                rinkkoDAO.update(pet);
                playerDAO.save(player);
            }
        }
        return "redirect:/pet/" + id;
    }

    @PostMapping("/medicine/{id}")
    public String giveMedicine(@PathVariable int id, @RequestParam int medicineIndex) {
        Rinkko pet = rinkkoDAO.findById(id);
        if (pet != null && medicineIndex >= 0 && medicineIndex < availableMedicines.size()) {
            MedicineItem medicine = availableMedicines.get(medicineIndex);
            if (player.getMoney() >= medicine.getCost()) {
                player.setMoney(player.getMoney() - medicine.getCost());
                pet.takeMedicine(medicine);
                rinkkoDAO.update(pet);
                playerDAO.save(player);
            }
        }
        return "redirect:/pet/" + id;
    }

    @GetMapping("/shop")
    public String shop(Model model) {
        model.addAttribute("player", player);
        model.addAttribute("foods", availableFoods);
        model.addAttribute("drinks", availableDrinks);
        model.addAttribute("activities", availablePlayActivities);
        model.addAttribute("medicines", availableMedicines);
        return "shop";
    }
    
    @GetMapping("/reset-confirm")
    public String resetConfirm(Model model) {
        model.addAttribute("player", player);
        return "reset-confirm";
    }
    
    @PostMapping("/reset-game")
    public String resetGame() {
        try {
            // 删除数据库文件并重新初始化
            DatabaseManager.resetDatabase();
            
            // 重新初始化数据库和玩家
            DatabaseManager.initializeDatabase();
            loadOrCreatePlayer();
            
            return "redirect:/?message=game_reset";
        } catch (Exception e) {
            return "redirect:/?error=reset_failed";
        }
    }

    private void updatePlayerData() {
        try {
            player = playerDAO.findById(1);
            if (player == null) {
                // 如果玩家不存在，创建新玩家
                player = new Player();
                player.setId(1);
                player.setMoney(100);
                playerDAO.save(player);
            }
            List<Rinkko> pets = rinkkoDAO.findByPlayerId(player.getId());
            if (pets == null) {
                pets = new ArrayList<>();
            }
            player.setRinkkoList(new ArrayList<>(pets));
        } catch (Exception e) {
            System.err.println("Error updating player data: " + e.getMessage());
            e.printStackTrace();
            // 创建默认玩家作为备用
            if (player == null) {
                player = new Player();
                player.setId(1);
                player.setMoney(100);
                player.setRinkkoList(new ArrayList<>());
            }
        }
    }
}
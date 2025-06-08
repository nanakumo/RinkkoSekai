package com.example.game;

import com.example.pet.Rinkko;
import java.util.Scanner;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import com.example.Item.*;

public class Game {
    private Player player;
    private Scanner scanner;
    private String[] petNamePrefiexs = {"傲娇", "可爱", "贪吃", "元气", "懒散"};
    private Random random;
    private int currentLevel = 1;
    private static final int maxLevel = 5;
  // 喝水吃饭
    private List<FoodItem> availableFoods;
    private List<DrinkItem> availableDrinks;
    //玩耍吃药的字段
    private List<PlayActivityItem> availablePlayActivities;
    private List<MedicineItem> availableMedicines;

    // 构造方法
    public Game(){
        this.player = new Player();
        this.scanner =new Scanner(System.in);
        this.random = new Random();

            // 初始化食物列表
        this.availableFoods = new ArrayList<>();
        availableFoods.add(new FoodItem("普通汉堡",10,30,5));
         availableFoods.add(new FoodItem("美味炸鸡",25,50,10));

        //  初始化饮料
        this.availableDrinks = new ArrayList<>();
        availableDrinks.add(new DrinkItem("干净的水",5,30,2));
         availableDrinks.add(new DrinkItem("肥宅快乐",15,45,5));
    }

    // 添加通过辅助方法
         private <T extends MenuItem> T selectItemFromMenu(List<T> items, String prompt){
         System.out.println(prompt);
         for(int i = 0; i < items.size(); i++){
            T item = items.get(i);
            System.out.println((i + 1) + ". " + item.getName() + " (" + item.getDescription() + ")");
        }
        System.out.println("0. 返回上一级");

        int choice = -1;
        while (choice < 0 || choice > items.size()) {
            System.out.println("请输入你的选择：");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next();
            }
        }

        if (choice == 0) {
            return null;
        } else {
            return items.get(choice - 1);
        }

        // 游戏和吃药
        availablePlayActivities = new ArrayList<>();
        availablePlayActivities.add(new PlayActivityItem("打游戏", 15, 30, 10, -5));
        availablePlayActivities.add(new PlayActivityItem("瑟瑟", 20, 40, 15, -8));

        availableMedicines = new ArrayList<>();
        availableMedicines.add(new MedicineItem("普通感冒药", 50, 40, -5));
        availableMedicines.add(new MedicineItem("特效活力药水", 80, 70, -10));
    }

    // handleWorkAction
    public boolean handlePlayAction(Rinkko pet){
        PlayActivityItem activity = selectItemFromMenu(availablePlayActivities, "请选择一项陪玩的活动：");

        if (activity == null) {
            return false;
        }

        if (player.getMoney() < activity.getCost()) {
            System.out.println("你没有足够的金币来来进行这个活动！");
        } else {
            player.spendMoney(activity.getCost());
            pet.playWith(activity);
        }

        return  true;
    }

    // handleTreatAction
    public boolean handleTreatAction(Rinkko pet){
        MedicineItem medicine = selectItemFromMenu(availableMedicines, "请选择要使用的药品：");
         if (medicine == null) {
            return false;
         }

         if (player.getMoney() < medicine.getCost()) {
            System.out.println("你没有足够的金币来买药！");
         } else {
            player.spendMoney(medicine.getCost());
            pet.takeMedicine(medicine);
         }

         return true;
    }

    // handleWorkAction
    public boolean handleWorkAction(Rinkko pet){
        int earned = pet.work();
        player.addMoney(earned);
        return true;
    }

    // 新增玩家动作方法
    // 喂食
    public void handleFeedAction(Rinkko pet){
        FoodItem selectedFood = selectItemFromMenu(availableFoods, "请选择要喂的食物：");

        if(selectedFood != null){
            int cost = selectedFood.getCost();
            if (player.getMoney() >= cost) {
                player.spendMoney(cost);
                pet.eat(selectedFood);
            } else {
                System.out.println("金钱不足，无法购买该食物。");
            }
        }
        System.out.println("温馨提示：喂食动作已消耗一回合");
    }

    //  喝水
    public void handleDrinkAction(Rinkko pet){
        DrinkItem selectedDrink = selectItemFromMenu(availableDrinks, "请选择凛喵喵要喝的饮料：");

        if(selectedDrink != null){
            int cost = selectedDrink.getCost();
            if (player.getMoney() >= cost) {
                player.spendMoney(cost);
                pet.drink(selectedDrink);
            } else {
                System.out.println("金钱不足，无法购买该饮品。");
            }
        }
        System.out.println("温馨提示：喝水动作已消耗一回合");
    }

    // 自动选择一只凛喵喵
    private Rinkko selectPetForAction(){
        List<Rinkko> pets = player.getPets()
    }




    // 前往下一关
    private void nextLevel(){
         if(currentLevel < maxLevel){
            currentLevel ++;
            System.out.println("正在前往下一关，现在是第" + currentLevel + "关。");
        } else {
            System.out.println("恭喜你已通过所有关卡，接下来就和所有凛喵喵们一起共度余生吧！");
        }
    }

    // 游戏启动
    public void start(){
        System.out.println("欢迎来到凛喵喵的世界！");
        boolean running = true;

        while(running){
            System.out.println("\n当前你在第" + currentLevel + "关");
            System.out.println("请选择操作：");
            System.out.println("1. 领养一只凛喵喵");
            System.out.println("2. 查看你的凛喵喵");
            System.out.println("3. 前往下一关");
            System.out.println("4. 退出游戏");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1 :adoptRinkko();
                case 2 : player.listPets();
                case 3 : nextLevel();
                case 4 : {
                    System.out.println("感谢游玩，期待下次相遇！");
                    running = false;
                }
                default : System.out.println("无效选择！");


            }

        }
    }

    // 领养凛喵喵功能

    private void adoptRinkko(){
        System.out.println("请你为你的可爱凛喵喵取一个名字吧！");
        String newName = scanner.nextLine();

        // 随机前缀

        String[] prefixes = {"A", "B", "C"};
        Random random = new Random();
        int randomIndex = random.nextInt(prefixes.length); // 生成 0, 1, 或 2
        String randomPrefix = prefixes[randomIndex];

        // 创建新的凛喵喵

        Rinkko newRinkko = new Rinkko();
        String fullName = randomPrefix + "的" + newName;
        newRinkko.setName(fullName);

        // 添加到玩家

        player.addPet(newRinkko);
        System.out.println("你成功领养了一只新的凛喵喵：" + fullName);

    }

    // 主方法 - 运行游戏
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}

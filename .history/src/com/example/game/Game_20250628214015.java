package com.example.game;

import com.example.pet.Rinkko;
import java.util.Scanner;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import com.example.item.*;


public class Game {
    private Player player;
    private Scanner scanner;
    private Random random;
    private int currentLevel = 1;
    private static final int MAX_LEVEL = 5;

    private List<FoodItem> availableFoods;
    private List<DrinkItem> availableDrinks;
    private List<PlayActivityItem> availablePlayActivities;
    private List<MedicineItem> availableMedicines;

    // 新增字段用于存储各项操作的最低费用，以便在菜单中动态显示。
    private int minFoodCost;
    private int minDrinkCost;
    private int minPlayCost;
    private int minMedicineCost;

    public Game() {
        this.player = new Player();
        this.scanner = new Scanner(System.in);
        this.random = new Random();

        this.availableFoods = new ArrayList<>();
        availableFoods.add(new FoodItem("普通汉堡", 10, 30, 5));
        availableFoods.add(new FoodItem("美味炸鸡", 25, 50, 10));

        this.availableDrinks = new ArrayList<>();
        availableDrinks.add(new DrinkItem("干净的水", 5, 30, 2));
        availableDrinks.add(new DrinkItem("肥宅快乐", 15, 45, 5));

        this.availablePlayActivities = new ArrayList<>();
        availablePlayActivities.add(new PlayActivityItem("打游戏", 15, 30, 10, -5));
        availablePlayActivities.add(new PlayActivityItem("瑟瑟", 20, 40, 15, -8));

        this.availableMedicines = new ArrayList<>();
        availableMedicines.add(new MedicineItem("普通感冒药", 50, 40, -5));
        availableMedicines.add(new MedicineItem("特效活力药水", 80, 70, -10));

        // 初始化各项操作的最低费用
        minFoodCost = availableFoods.stream().mapToInt(MenuItem::getCost).min().orElse(0);
        minDrinkCost = availableDrinks.stream().mapToInt(MenuItem::getCost).min().orElse(0);
        minPlayCost = availablePlayActivities.stream().mapToInt(MenuItem::getCost).min().orElse(0);
        minMedicineCost = availableMedicines.stream().mapToInt(MenuItem::getCost).min().orElse(0);
    }

    private <T extends MenuItem> T selectItemFromMenu(List<T> items, String prompt) {
        System.out.println(prompt);
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            System.out.println((i + 1) + ". " + item.getName() + " (" + item.getDescription() + ")");
        }
        System.out.println("0. 返回上一级");

        int choice = -1;
        while (choice < 0 || choice > items.size()) {
            System.out.println("请输入你的选择：");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } else {
                scanner.next();
            }
        }

        if (choice == 0) {
            return null;
        } else {
            return items.get(choice - 1);
        }
    }

    private Rinkko selectPetForAction() {
        List<Rinkko> pets = player.getPets();
        if (pets.isEmpty()) {
            System.out.println("你还没有任何凛喵喵！！");
            return null;
        }
        if (pets.size() == 1) {
            return pets.get(0);
        }

        System.out.println("请选择你要操作的凛喵喵：");
        for (int i = 0; i < pets.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, pets.get(i).getName());
        }
        System.out.println("0. 返回");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > pets.size()) {
            System.out.println("操作取消。");
            return null;
        }

        return pets.get(choice - 1);
    }

    public void endTurn() {
        for (Rinkko pet : player.getPets()) {
            pet.passTurnUpdate();
        }
        System.out.println("── 回合结束 ─────────────");
    }

    public boolean checkLevelWinCondition() {
        List<Rinkko> pets = player.getPets();
        if (pets.size() < currentLevel) return false;
        for (Rinkko pet : pets) {
            if (pet.getAffection() < Rinkko.MAX_STAT_VALUE) return false;
        }
        return true;
    }

    public boolean checkGameOverCondition() {
        if (player.getPets().isEmpty()) return false; // 防止没有凛喵喵时误判
        for (Rinkko pet : player.getPets()) {
            if (pet.getAffection() <= Rinkko.MIN_STAT_VALUE) {
                System.out.println(pet.getName() + "因为好感度降到了冰点，离开了你...");
                return true;
            }
        }
        return false;
    }

    public boolean handleFeedAction(Rinkko pet) {
        FoodItem item = selectItemFromMenu(availableFoods, "请选择食物：");
        if (item == null) return false;
        if (player.getMoney() >= item.getCost()) {
            player.spendMoney(item.getCost());
            pet.eat(item);
            return true;
        } else {
            System.out.println("金钱不足！");
            return false;
        }
    }

    public boolean handleDrinkAction(Rinkko pet) {
        DrinkItem item = selectItemFromMenu(availableDrinks, "请选择饮品：");
        if (item == null) return false;
        if (player.getMoney() >= item.getCost()) {
            player.spendMoney(item.getCost());
            pet.drink(item);
            return true;
        } else {
            System.out.println("金钱不足！");
            return false;
        }
    }

    public boolean handlePlayAction(Rinkko pet) {
        PlayActivityItem item = selectItemFromMenu(availablePlayActivities, "请选择活动：");
        if (item == null) return false;
        if (player.getMoney() >= item.getCost()) {
            player.spendMoney(item.getCost());
            pet.playWith(item);
            return true;
        } else {
            System.out.println("金钱不足！");
            return false;
        }
    }

    public boolean handleTreatAction(Rinkko pet) {
        MedicineItem item = selectItemFromMenu(availableMedicines, "请选择药品：");
        if (item == null) return false;
        if (player.getMoney() >= item.getCost()) {
            player.spendMoney(item.getCost());
            pet.takeMedicine(item);
            return true;
        } else {
            System.out.println("金钱不足！");
            return false;
        }
    }

    public boolean handleWorkAction(Rinkko pet) {
        int earned = pet.work();
        player.addMoney(earned);
        return true;
    }

    private void adoptNewPet() {
        System.out.println("请问你想：");
        String newName = scanner.nextLine();
        String[] prefixes = {"傲娇", "可爱", "元气", "懒散", "粘人"};
        String fullName = prefixes[random.nextInt(prefixes.length)] + "的" + newName;
        Rinkko newPet = new Rinkko();
        newPet.setName(fullName);
        player.addPet(newPet);
        System.out.println("你成功领养了凛喵喵：" + fullName);
    }

    public void startGame() {
        System.out.println("🌟 欢迎来到喵喵世界！你将领养属于你的可爱凛喵喵 🌟");

        adoptNewPet();

        while (true) {
            System.out.println("===== 当前关卡：" + currentLevel + "，你当前拥有金币：" + player.getMoney() + " =====");

            String menu = String.format("[F] 喂食:%d金币起 | [D] 喂水:%d金币起 | [P] 陪玩:%d金币起 | [T] 治疗:%d金币起 | [W] 工作(赚钱)",
                                      minFoodCost, minDrinkCost, minPlayCost, minMedicineCost);
            System.out.println(menu);
            if (player.getPets().size() < currentLevel) {
                System.out.println("[A] 领养新凛喵喵");
            }
            System.out.println("[S] 查看状态 | [Q] 退出游戏");

            String input = scanner.nextLine().trim().toUpperCase();
            boolean usedTurn = false;

            switch (input) {
                case "F": {
                    Rinkko pet = selectPetForAction();
                    if (pet != null) usedTurn = handleFeedAction(pet);
                    break;
                }
                case "D": {
                    Rinkko pet = selectPetForAction();
                    if (pet != null) usedTurn = handleDrinkAction(pet);
                    break;
                }
                case "P": {
                    Rinkko pet = selectPetForAction();
                    if (pet != null) usedTurn = handlePlayAction(pet);
                    break;
                }
                case "T": {
                    Rinkko pet = selectPetForAction();
                    if (pet != null) usedTurn = handleTreatAction(pet);
                    break;
                }
                case "W": {
                    Rinkko pet = selectPetForAction();
                    if (pet != null) usedTurn = handleWorkAction(pet);
                    break;
                }
                case "A": {
                    if (player.getPets().size() < currentLevel) {
                        adoptNewPet();
                        usedTurn = true;
                    } else {
                        System.out.println("你已经拥有本关允许的最大数量凛喵喵。");
                    }
                    break;
                }
                case "S": {
                    player.listPets();
                    break;
                }
                case "Q": {
                    System.out.println("感谢游玩，期待下次再见！");
                    return;
                }
                default:
                    System.out.println("无效的指令，请重新输入。");
            }

            if (usedTurn) {
                endTurn();

                if (checkGameOverCondition()) {
                    System.out.println("💀 游戏失败：所有凛喵喵失去了对你的信任。请好好反省！");
                    return;
                }

                if (checkLevelWinCondition()) {
                    if (currentLevel == MAX_LEVEL) {
                        System.out.println("🎉 恭喜你通关！所有凛喵喵都深深爱着你！");
                        return;
                    } else {
                        System.out.println("🎊 恭喜你通过第 " + currentLevel + " 关！");
                        currentLevel++;
                        System.out.println("你现在可以拥有 " + currentLevel + " 只凛喵喵！");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.startGame();
    }
}

package com.example.game;

import com.example.pet.Rinkko;
import java.util.Scanner;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import com.example.item.*;
import com.example.game.db.DatabaseManager;
import com.example.game.db.RinkkoDAO;
import com.example.game.db.PlayerDAO;

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

    private int minFoodCost;
    private int minDrinkCost;
    private int minPlayCost;
    private int minMedicineCost;

    private RinkkoDAO rinkkoDAO;
    private PlayerDAO playerDAO;

    public Game() {
        DatabaseManager.initializeDatabase();
        this.rinkkoDAO = new RinkkoDAO();
        this.playerDAO = new PlayerDAO();
        this.scanner = new Scanner(System.in, "UTF-8");
        this.random = new Random();
        loadOrCreatePlayer();

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

        minFoodCost = availableFoods.stream().mapToInt(MenuItem::getCost).min().orElse(0);
        minDrinkCost = availableDrinks.stream().mapToInt(MenuItem::getCost).min().orElse(0);
        minPlayCost = availablePlayActivities.stream().mapToInt(MenuItem::getCost).min().orElse(0);
        minMedicineCost = availableMedicines.stream().mapToInt(MenuItem::getCost).min().orElse(0);
    }

    private char readSingleChar() {
        try {
            // Unix/Linux/WSL - disable canonical mode for single key input
            ProcessBuilder pb = new ProcessBuilder("stty", "-icanon", "min", "1");
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
            
            int ch = System.in.read();
            System.out.println(); // 输出换行让界面更整洁
            
            // Restore canonical mode
            ProcessBuilder pb2 = new ProcessBuilder("stty", "icanon");
            pb2.inheritIO();
            Process p2 = pb2.start();
            p2.waitFor();
            
            return Character.toUpperCase((char) ch);
        } catch (Exception e) {
            // Fallback to regular input if stty fails
            System.out.print("输入后按回车: ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.isEmpty()) {
                return '\0';
            }
            return input.charAt(0);
        }
    }

    private void loadOrCreatePlayer() {
        Player loadedPlayer = playerDAO.findById(1);

        if (loadedPlayer == null) {
            System.out.println("未找到玩家存档，创建新玩家。");
            this.player = new Player();
            player.setId(1);
            playerDAO.save(player);
        } else {
            System.out.println("成功加载玩家存档。");
            this.player = loadedPlayer;
        }

        List<Rinkko> pets = rinkkoDAO.findByPlayerId(player.getId());
        if (!pets.isEmpty()) {
             System.out.println("成功加载宠物存档。");
            for(Rinkko pet : pets) {
                player.addPet(pet);
            }
        } else {
             System.out.println("玩家还没有宠物。");
        }
    }

    private void saveGame() {
        System.out.println("正在保存游戏...");
        
        playerDAO.save(player);

        for (Rinkko pet : player.getPets()) {
            if (pet.getId() == 0) {
                rinkkoDAO.save(pet, player.getId());
            } else {
                rinkkoDAO.update(pet);
            }
        }
        System.out.println("游戏已保存！");
    }

    private <T extends MenuItem> T selectItemFromMenu(List<T> items, String prompt) {
        System.out.println(prompt);
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            System.out.println((i + 1) + ". " + item.getName() + " (" + item.getDescription() + ")");
        }
        System.out.println("0. 返回上一级");
        System.out.println("💡 提示：直接按数字键即可");

        int choice = -1;
        while (choice < 0 || choice > items.size()) {
            char input = readSingleChar();
            if (Character.isDigit(input)) {
                choice = Character.getNumericValue(input);
            } else {
                System.out.println("请输入有效数字！");
                continue;
            }
        }

        if (choice == 0) {
            System.out.println("✅ 已返回上一级");
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
        System.out.println("💡 提示：直接按数字键即可");

        int choice = -1;
        while (choice < 0 || choice > pets.size()) {
            char input = readSingleChar();
            if (Character.isDigit(input)) {
                choice = Character.getNumericValue(input);
            } else {
                System.out.println("请输入有效数字！");
                continue;
            }
        }

        if (choice == 0) {
            System.out.println("✅ 已返回主菜单");
            return null;
        } else {
            return pets.get(choice - 1);
        }
    }

    public void endTurn() {
        System.out.println("\n⏰ 时间流逝中...");
        for (Rinkko pet : player.getPets()) {
            pet.passTurnUpdate();
        }
        System.out.println("──────── 回合结束 ────────");
    }

    public void triggerRandomEvent() {
        if (player.getPets().isEmpty()) {
            return;
        }
        
        if (random.nextInt(100) < 30) {
            int eventType = random.nextInt(3);
            Rinkko targetPet = player.getPet(random.nextInt(player.getPets().size()));

            System.out.println("【突发事件!】");
            switch (eventType) {
                case 0:
                    int foundMoney = random.nextInt(21) + 10;
                    player.addMoney(foundMoney);
                    System.out.println("你在路上捡到了 " + foundMoney + " 金币！真是幸运的一天！");
                    break;
                case 1:
                    System.out.println(targetPet.getName() + " 好像做了一个噩梦，心情似乎变差了。(-5 心情)");
                    targetPet.changeMood(-5);
                    break;
                case 2:
                    System.out.println("窗外的天气真好，" + targetPet.getName() + " 看起来很想出去玩。");
                    break;
            }
        }
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
        if (player.getPets().isEmpty()) return false;
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
        String[] petTypes = {"懒散", "活泼", "高贵", "顽皮", "温柔"};
        String[] petNames = {"小咪", "小雪", "小花", "小黑", "小白"};
        
        while (true) {
            System.out.println("请选择凛喵喵的性格：");
            for (int i = 0; i < petTypes.length; i++) {
                System.out.println((i + 1) + ". " + petTypes[i] + "的" + petNames[i]);
            }
            System.out.println("0. 返回上一级");
            System.out.println("💡 提示：直接按数字键即可");
            
            int choice = -1;
            while (choice < 0 || choice > petTypes.length) {
                char input = readSingleChar();
                if (Character.isDigit(input)) {
                    choice = Character.getNumericValue(input);
                } else {
                    System.out.println("请输入有效数字！");
                    continue;
                }
            }
            
            if (choice == 0) {
                System.out.println("✅ 已取消领养");
                return;
            }
            
            String selectedType = petTypes[choice - 1];
            String selectedName = petNames[choice - 1];
            String fullName = selectedType + "的" + selectedName;

            System.out.println("确认领养凛喵喵：" + fullName + " 吗？");
            System.out.println("[Y] 确认 | [N] 重新选择 | [B] 取消领养");
            System.out.println("💡 提示：直接按键即可，无需回车");
            char confirm = readSingleChar();
            
            switch (confirm) {
                case 'Y':
                    Rinkko newPet = new Rinkko();
                    newPet.setName(fullName);
                    player.addPet(newPet);
                    System.out.println("🎉 你成功领养了凛喵喵：" + fullName);
                    return;
                case 'N':
                    System.out.println("[重试] 重新选择");
                    continue;
                case 'B':
                    System.out.println("[√] 已取消领养");
                    return;
                default:
                    System.out.println("[X] 无效选择，默认重新选择");
                    continue;
            }
        }
    }

    public void startGame() {
        System.out.println("* 欢迎来到喵喵世界！你将领养属于你的可爱凛喵喵 *");

        if (player.getPets().isEmpty()) {
            adoptNewPet();
        }
        
        player.listPets();

        while (true) {
            System.out.println("===== 当前关卡：" + currentLevel + "，你当前拥有金币：" + player.getMoney() + " =====");

            String menu = String.format("[F] 喂食:%d金币起 | [D] 喂水:%d金币起 | [P] 陪玩:%d金币起 | [T] 治疗:%d金币起 | [W] 工作(20金币起)",
                                      minFoodCost, minDrinkCost, minPlayCost, minMedicineCost);
            System.out.println(menu);
            if (player.getPets().size() < currentLevel) {
                System.out.println("[A] 领养新凛喵喵");
            }
            System.out.println("[S] 查看状态 | [Q] 退出游戏");
            System.out.println("💡 提示：直接按键即可，无需回车");

            char input = readSingleChar();
            boolean usedTurn = false;

            switch (input) {
                case 'F': {
                    Rinkko pet = selectPetForAction();
                    if (pet != null) usedTurn = handleFeedAction(pet);
                    break;
                }
                case 'D': {
                    Rinkko pet = selectPetForAction();
                    if (pet != null) usedTurn = handleDrinkAction(pet);
                    break;
                }
                case 'P': {
                    Rinkko pet = selectPetForAction();
                    if (pet != null) usedTurn = handlePlayAction(pet);
                    break;
                }
                case 'T': {
                    Rinkko pet = selectPetForAction();
                    if (pet != null) usedTurn = handleTreatAction(pet);
                    break;
                }
                case 'W': {
                    Rinkko pet = selectPetForAction();
                    if (pet != null) usedTurn = handleWorkAction(pet);
                    break;
                }
                case 'A': {
                    if (player.getPets().size() < currentLevel) {
                        adoptNewPet();
                        usedTurn = true;
                    } else {
                        System.out.println("你已经拥有本关允许的最大数量凛喵喵。");
                    }
                    break;
                }
                case 'S': {
                    player.listPets();
                    break;
                }
                case 'Q': {
                    saveGame();
                    System.out.println("感谢游玩，期待下次再见！");
                    return;
                }
                default:
                    System.out.println("无效的指令，请重新输入。");
            }

            if (usedTurn) {
                endTurn();
                triggerRandomEvent();
                player.listPets();

                if (checkGameOverCondition()) {
                    System.out.println("[失败] 游戏失败：所有凛喵喵失去了对你的信任。请好好反省！");
                    return;
                }

                if (checkLevelWinCondition()) {
                    if (currentLevel == MAX_LEVEL) {
                        System.out.println("[通关] 恭喜你通关！所有凛喵喵都深深爱着你！");
                        return;
                    } else {
                        System.out.println("[升级] 恭喜你通过第 " + currentLevel + " 关！");
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

package com.example.pet;

// import java.util.function.IntFunction;
import com.example.item.*;



// 创建Rinkko类

public class Rinkko {

    // 创建字段
    private String name;
    private int hunger = 80;
    private int thirst = 80;
    private int mood = 70;
    private int health = 90;
    private int affection = 30;
    // Q4
    private DebuffType currentDebuff = DebuffType.NONE;
    private int debuffTurnsLeft = 0;

    public static final int MAX_STAT_VALUE = 100;
    public static final int MIN_STAT_VALUE = 0;

    // Q4：新增常量
    private static final int HUNGER_THRESHOLD = 20;
    private static final int THIRST_THRESHOLD = 20;
    private static final int MOOD_THRESHOLD = 30;
    private static final int HEALTH_THRESHOLD_FOR_SICK = 30;
    private static final int DEBUFF_DURATION = 3;



    // 创建构造函数（初始化字段
    public Rinkko( ){
        this.name = "Rinkko";
        this.hunger = 80;
        this.thirst = 80;
        this.mood = 70;
        this.health = 90;
        this.affection = 30;

    }

    // 获取字段（Getter
    public String getName(){
        return name;
    }

    public int getHunger(){
        return hunger;
    }

    public int getThirst(){
        return thirst;
    }

    public int getMood(){
        return mood;
    }

    public int getHealth(){
        return health;
    }

    public int getAffection(){
        return affection;
    }

    // 状态调整方法

    protected void changeHunger(int amount){
        this.hunger = Math.max(MIN_STAT_VALUE, Math.min(MAX_STAT_VALUE, this.hunger + amount));
    }

    protected void changeThirst(int amount){
        this.thirst = Math.max(MIN_STAT_VALUE, Math.min(MAX_STAT_VALUE, this.thirst + amount));
    }

    public void changeMood(int amount){
        this.mood = Math.max(MIN_STAT_VALUE, Math.min(MAX_STAT_VALUE, this.mood + amount));
    }

    protected void changeHealth(int amount){
        this.health = Math.max(MIN_STAT_VALUE, Math.min(MAX_STAT_VALUE, this.health + amount));
    }

    protected void changeAffection(int amount){
        this.affection = Math.max(MIN_STAT_VALUE, Math.min(MAX_STAT_VALUE, this.affection + amount));
    }

    // 设置名字的方法
    public void setName(String name) {
        this.name = name;
    }

    // 获取当前Debuff
    public DebuffType getCurrentDebuff(){
        return currentDebuff;
    }

    // 返回Debuff剩余回合
    public int getDebuffTurnsLeft(){
        return debuffTurnsLeft;
    }

    // 返回Debuff状态描述
    public String getDebuffStatusMessage(){
        if (currentDebuff != DebuffType.NONE) {
            return String.format("[负面状态: %s, 剩余: %d 回合]",
            currentDebuff.getDescription(), debuffTurnsLeft);
        }
        return "";
    }

    // 每回合状态衰减和Debuff处理
    public void passTurnUpdate() {
        System.out.println("🕒 " + name + " 的状态变化：");
        
        // 1. 状态自然衰减
        changeHunger(-5);
        changeThirst(-7);
        changeMood(-3);
        changeHealth(-2); // 修正：这是基础健康衰减

        // 显示基础变化
        System.out.println("   自然消耗: 饱食-5, 口渴-7, 心情-3, 健康-2");

        // 额外状态影响
        if (health < 50) {
            changeMood(-2); // 健康度低额外降低心情
            System.out.println("   💔 健康状况不佳，心情额外下降-2");
        }
        if (hunger < HUNGER_THRESHOLD || thirst < THIRST_THRESHOLD) {
            changeHealth(-3); // 饥饿或口渴额外降低健康
            System.out.println("   🚨 饥饿或口渴严重，健康额外下降-3");
        }

        // 2. Debuff处理
        if (currentDebuff != DebuffType.NONE) { // 如果当前有Debuff
            debuffTurnsLeft--;
            System.out.println("   ⚠️ " + currentDebuff.getDescription() + " 状态持续中 (剩余 " + debuffTurnsLeft + " 回合)");

            if (debuffTurnsLeft == 0) {
                System.out.println("   😠 长时间的 " + currentDebuff.getDescription() + " 让 " + name + " 对你失去了信任！(-15 好感)");
                changeAffection(-15);
                currentDebuff = DebuffType.NONE;
            }
        }

        // 3. 检查是否触发新的Debuff (与旧的Debuff处理分离)
        // 显示所有需要注意的状态警告
        if (hunger < HUNGER_THRESHOLD) {
            System.out.println("   ⚠️ " + name + " 饿坏了！需要食物");
        }
        if (thirst < THIRST_THRESHOLD) {
            System.out.println("   ⚠️ " + name + " 渴坏了！需要水分");
        }
        if (mood < MOOD_THRESHOLD) {
            System.out.println("   ⚠️ " + name + " 心情很糟！需要陪伴");
        }
        if (health < HEALTH_THRESHOLD_FOR_SICK) {
            System.out.println("   ⚠️ " + name + " 生病了！需要治疗");
        }
        
        // 按优先级设置主要Debuff（用于游戏机制）
        if (health < HEALTH_THRESHOLD_FOR_SICK && currentDebuff != DebuffType.SICK) {
            currentDebuff = DebuffType.SICK;
            debuffTurnsLeft = DEBUFF_DURATION + 1; // 生病持续更久
        } else if (hunger < HUNGER_THRESHOLD && currentDebuff != DebuffType.HUNGRY) {
            currentDebuff = DebuffType.HUNGRY;
            debuffTurnsLeft = DEBUFF_DURATION;
        } else if (thirst < THIRST_THRESHOLD && currentDebuff != DebuffType.THIRSTY) {
            currentDebuff = DebuffType.THIRSTY;
            debuffTurnsLeft = DEBUFF_DURATION;
        } else if (mood < MOOD_THRESHOLD && currentDebuff != DebuffType.UNHAPPY) {
            currentDebuff = DebuffType.UNHAPPY;
            debuffTurnsLeft = DEBUFF_DURATION;
        }
    }


    // 主动清除指定 Debuff
    public void clearDebuff(DebuffType typeToClear) {
    if (currentDebuff == typeToClear) {
        currentDebuff = DebuffType.NONE;
        debuffTurnsLeft = 0;
        System.out.println(name + " 的 " + typeToClear.getDescription() + " 状态被清除！");
    }
}

// 吃食物方法
public void eat(com.example.item.FoodItem food){
    changeHunger(food.getHungerBoost());
    changeMood(food.getMoodBoost());

    if (this.currentDebuff == DebuffType.HUNGRY) {
        clearDebuff(DebuffType.HUNGRY);
    }
}

// 喝饮料的方法
public void drink(com.example.item.DrinkItem drink){
    changeThirst(drink.getThirstBoost());
    changeHealth(drink.getHealthBoost());

    if (this.currentDebuff == DebuffType.THIRSTY) {
        clearDebuff(DebuffType.THIRSTY);
    }

    System.out.println(this.name + "满足地喝了" + drink.getName() + "!");
}




// 获取凛喵喵状态
    public String getStatus(){
        StringBuilder status = new StringBuilder();
        status.append(String.format("🐱 %s", name));
        
        // 状态条显示
        status.append(String.format("\n   饱食度: %s %d/100", getStatusBar(hunger), hunger));
        status.append(String.format("\n   口渴度: %s %d/100", getStatusBar(thirst), thirst));
        status.append(String.format("\n   心情值: %s %d/100", getStatusBar(mood), mood));
        status.append(String.format("\n   健康值: %s %d/100", getStatusBar(health), health));
        status.append(String.format("\n   好感度: %s %d/100", getStatusBar(affection), affection));
        
        // Debuff状态
        String debuffStatusMessage = getDebuffStatusMessage();
        if (!debuffStatusMessage.isEmpty()) {
            status.append("\n   ").append(debuffStatusMessage);
        }
        
        return status.toString();
    }
    
    // 生成状态条的辅助方法
    private String getStatusBar(int value) {
        if (value >= 80) return "😊"; // 很好
        else if (value >= 60) return "🙂"; // 良好
        else if (value >= 40) return "😐"; // 一般
        else if (value >= 20) return "😟"; // 不佳
        else return "😵"; // 很糟
    }

// 和凛喵喵玩耍方法
public void playWith(PlayActivityItem activity){
    changeMood(activity.getMoodBoost());
    changeAffection(activity.getAffectionBoost());
    changeHunger(activity.getHungerCost());

    if (currentDebuff == DebuffType.UNHAPPY) {
        clearDebuff(DebuffType.UNHAPPY);
    }

    System.out.println(name + "开心地参加了「" + activity.getName() + "」活动，心情和好感都提升了！");
}

// 吃药治疗方法

public void takeMedicine(MedicineItem medicine){
    changeHealth(medicine.getHealthBoost());
    changeMood(medicine.getMoodDecrease());

    if (currentDebuff == DebuffType.SICK) {
        clearDebuff(DebuffType.SICK);
    }

    System.out.println(name + "吃下了「" + medicine.getName() + "」感觉身体好多了，但好像不是很开心…");
}

// 工作方法
 public int work(){
    int earned = new java.util.Random().nextInt(31) + 20;
    changeHunger(-15);
    changeThirst(-10);
    changeMood(-10);

    System.out.println(name + "努力工作，为你赚取了" + earned + "金币！但他看起来有点累了。");

    return earned;
 }

}
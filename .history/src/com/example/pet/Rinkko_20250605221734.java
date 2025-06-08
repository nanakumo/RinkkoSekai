package com.example.pet;

import java.util.function.IntFunction;

import com.example.Item.MenuItem;

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
    private int DebuffTurnLeft = 0;

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

    // 定义 adjustStatus 方法

    private int adjustStatus(int currentStatus, int amount){
        int newStatus = currentStatus + amount;
        return Math.max(MIN_STAT_VALUE, Math.min(MAX_STAT_VALUE, newStatus));
    }

    // 状态调整方法

    protected void changeHunger(int amount){
        this.hunger = Math.max(MIN_STAT_VALUE, Math.min(MAX_STAT_VALUE, this.hunger + amount));
    }

    protected void changeThirst(int amount){
        this.thirst = Math.max(MIN_STAT_VALUE, Math.min(MAX_STAT_VALUE, this.hunger + amount));
    }

    protected void changeMood(int amount){
        this.mood = Math.max(MIN_STAT_VALUE, Math.min(MAX_STAT_VALUE, this.hunger + amount));
    }

    protected void changeHealth(int amount){
        this.health = Math.max(MIN_STAT_VALUE, Math.min(MAX_STAT_VALUE, this.hunger + amount));
    }

    protected void changeAffection(int amount){
        this.affection = Math.max(MIN_STAT_VALUE, Math.min(MAX_STAT_VALUE, this.hunger + amount));
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
    public int getDebuffTurnLeft(){
        return DebuffTurnLeft;
    }

    // 返回Debuff状态描述
    public String getdebuffStatusMessage(){
        if (currentDebuff != DebuffType.NONE) {
            return String.format("[负面状态: %s, 剩余: %d 回合]",
            currentDebuff.getDescription(), DebuffTurnLeft);
        }
        return "";
    }

    // 每回合状态衰减和Debuff处理
    public void passTurnUpdate(){
        changeHunger(-5);
        changeThirst(-7);
        changeMood(-3);
        if(health < 50){
            changeMood(-2);
        }
        changeMood(-2);
        if (hunger < HUNGER_THRESHOLD || thirst < THIRST_THRESHOLD) {
            changeHealth(-3);
        }
        //Debuff处理
        if (DebuffTurnLeft > 0) {
            DebuffTurnLeft--;
            if (DebuffTurnLeft == 0) {
                System.out.println("由于"+ name + "的" + currentDebuff.getDescription() + "状态未被照料，好感度下降了！ /(ㄒoㄒ)/~~");
                changeAffection(-15);
                currentDebuff = DebuffType.NONE;
            } else {
                // 检查是否会触发新的 Debuff
                if (health < HEALTH_THRESHOLD_FOR_SICK && currentDebuff == DebuffType.NONE) {
                    currentDebuff = DebuffType.SICK;
                    DebuffTurnLeft = DEBUFF_DURATION + 1;
                    System.out.println(name + "凛喵喵生病了！小猫娘快来照顾我！");
                } else if (hunger < HUNGER_THRESHOLD && currentDebuff == DebuffType.NONE) {
                    currentDebuff = DebuffType.HUNGRY;
                    DebuffTurnLeft = DEBUFF_DURATION;
                    System.out.println(name +"凛喵喵饿了！小猫娘快来喂饭！");
                } else if (mood < MOOD_THRESHOLD || currentDebuff == DebuffType.NONE) {
                    currentDebuff = DebuffType.UNHAPPY;
                    DebuffTurnLeft = DEBUFF_DURATION;
                    System.out.println(name + "凛喵喵不开心了！小猫娘快来哄我！");
                }

            }
        }
    }

    // 主动清除指定 Debuff
public void clearDebuff(DebuffType typeToClear) {
    if (currentDebuff == typeToClear) {
        currentDebuff = DebuffType.NONE;
        DebuffTurnLeft = 0;
        System.out.println(name + " 的 " + typeToClear.getDescription() + " 状态被清除！");
    }
}

// 吃食物方法
public void eat(com.example.Item.FoodItem food){
    changeHunger(food.getHungerBoost());
    changeMood(food.getMoodBoost());

    if (this.currentDebuff == DebuffType.HUNGRY) {
        clearDebuff(DebuffType.HUNGRY);
    }
}

// 喝饮料的方法
public void drink(com.example.Item.DrinkItem drink){
    changeThirst(drink.getThirstBoost());
    changeHealth(drink.getHealthBoost());

    if (this.currentDebuff == DebuffType.THIRSTY) {
        clearDebuff(DebuffType.THIRSTY);
    }

    System.out.println(this.name + "满足地喝了" + drink.getName() + "!");
}




// 获取凛喵喵状态
    public String getStatus(){
    String status = String.format("[凛喵喵名]:%s, 饱食度: %d/%d, 口渴度: %d/%d, 心情: %d/%d, 健康: %d/%d, 好感度: %d/%d]",
    name,hunger,MAX_STAT_VALUE,thirst,MAX_STAT_VALUE,mood,MAX_STAT_VALUE,health,MAX_STAT_VALUE,affection,MAX_STAT_VALUE);

    String debuffStatusMessage = getdebuffStatusMessage();
    if (!debuffStatusMessage.isEmpty()) {
        status += debuffStatusMessage;
    }
    return status;

}

// 和凛喵喵玩耍方法
public viod playWith(PlayActivityItem activity){
    changeMood(activity.getMoodBoost());
    changeAffection(activity.getAffectionBoost());
    changeHunger(activity.getHungerBoost());

    if (debuffs.contains(DebuffType.UNHAPPY)) {
        clearDebuff(DebuffType.UNHAPPY);
    }

    System.out.println(name + "开心地参加了「" + activity.getName() + "」活动，心情和好感都提升了！");
}

// 吃药治疗方法

public void takeMedicine(MenuItem medicine){
    changeHealth(medicine.getHealthBoost());
    changeMood(medicine.getMoodBoost());

    if (debuffs.contains(DebuffType.SICK)) {
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


public static void main(String[] args) {
    Rinkko rinkko = new Rinkko();
    rinkko.setName("可爱凛喵喵");

    System.out.println("新喵喵状态：");
    System.out.println(rinkko.getStatus());

//     // 测试状态调整方法

// rinkko.changeHunger(-30);
// rinkko.changeThirst(15);
// rinkko.changeMood(-50);
// rinkko.changeHealth(-10);
// rinkko.changeAffection(1314490);

// // 状态调整后

// System.out.println("状态调整后：");
// System.out.println(rinkko.getStatus());

//Debuff测试
System.out.println("--- 游戏开始：Debuff 测试 ---");
System.out.println(rinkko.getStatus());

// 模拟10回合
for(int i = 0; i < 10;i++){
    System.out.println("\n---第" + (i+1)+"回合---");
    rinkko.passTurnUpdate();
    System.out.println(rinkko.getStatus());
}
}




}
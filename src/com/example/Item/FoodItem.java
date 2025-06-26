package com.example.item;

public class FoodItem implements MenuItem{

    // 定义字段：不可变
    private final String name;
    private final int cost;
    private final int hungerBoost;
    private final int moodBoost;

    // 构造方法

    public FoodItem(String name, int cost, int hungerBoost, int moodBoost){
        this.name = name;
        this.cost = cost;
        this.hungerBoost = hungerBoost;
        this.moodBoost = moodBoost;
    }

    // Getter
    // 饱食度提升
    public int getHungerBoost(){
        return hungerBoost;
    }

    // 心情值提升
    public int getMoodBoost(){
        return moodBoost;
    }

    // 接口方法实现
    // 获取名称
    @Override
    public String getName(){
        return name;
    }

    // 获取价格
    @Override
    public int getCost(){
        return cost;
    }

    //返回效果描述
    @Override
    public String getDescription(){
        return "+" + hungerBoost + "饱食，+" + moodBoost + "心情，价格："  + cost;
    }
}

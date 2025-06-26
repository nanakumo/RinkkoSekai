package com.example.item;

public class DrinkItem implements MenuItem {

    // 定义字段：不可变
    private final String name;
    private final int cost;
    private final int thirstBoost;
    private final int healthBoost;

    public DrinkItem(String name, int cost, int thirstBoost, int healthBoost){
        this.name = name;
        this.cost = cost;
        this.thirstBoost = thirstBoost;
        this.healthBoost = healthBoost;
        }

        //  Getter
        // 口渴度
        public int getThirstBoost(){
            return thirstBoost;
        }
        // 健康度
        public int getHealthBoost(){
            return healthBoost;
        }

        //接口方法实现
        // 获取名字
        @Override
        public String getName(){
            return name;
        }
        // 获取价格
        @Override
        public int getCost(){
            return cost;
        }
        // 获取效果描述
        @Override
        public String getDescription(){
            return "+" + thirstBoost + "口渴，+" + healthBoost + "健康，价格" + cost;
        }

        // // 描述
        // public String getDescription(){
        //     return "+" + thirstBoost + "口渴度，+" + healthBoost + "健康度，价格："  + cost;
        // }
    }


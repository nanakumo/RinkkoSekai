package com.example.Item;

public interface MenuItem {

    // 获取物品名称
    String getName();

    // 获取物品价格
    int getCost();

    //获取物品描述
    String getDescription();

    int getHealthBoost();

    int getMoodBoost();

}
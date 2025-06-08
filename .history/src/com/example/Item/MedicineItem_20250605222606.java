package com.example.Item;

public class MedicineItem implements MenuItem {
    private final String name;
    private final int cost;
    private final int healthBoost;
    private final int moodDecrease;

    public MedicineItem(String name, int cost, int healthBoost, int moodDecrease){
        this.name = name;
        this.cost = cost;
        this.healthBoost = healthBoost;
        this.moodDecrease = moodDecrease;
    }

    public String getName(){return name;}
    public int getCost(){return cost;}
    public int getHealthBoost(){return healthBoost;}
    public int getMoodDecrease(){return moodDecrease;}
    public String getDescription(){
        return String.format("+%d健康, %d心情, 花费:%d",
                healthBoost, moodDecrease, cost);
    }

}

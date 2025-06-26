package com.example.item;

public class PlayActivityItem implements MenuItem{
    private final String name;
    private final int cost;
    private final int moodBoost;
    private final int affectionBoost;
    private final int hungerCost;

    public PlayActivityItem(String name, int cost, int moodBoost, int affectionBoost, int hungerCost){
        this.name = name;
        this.cost = cost;
        this.moodBoost = moodBoost;
        this.affectionBoost = affectionBoost;
        this.hungerCost = hungerCost;
    }

    public String getName(){return name;}
    public int getCost(){return cost;}
    public int getMoodBoost(){return moodBoost;}
    public int getAffectionBoost(){return affectionBoost;}
    public int getHungerCost(){return hungerCost;}

    public String getDescription(){
        return String.format("+%d心情, +%d好感, -%d饱食, 花费:%d",
                moodBoost, affectionBoost, Math.abs(hungerCost), cost);
    }

}
package com.example.game;
import java.util.ArrayList;

import com.example.pet.Rinkko;

// 创建类
public class Player {

    //创建字段
    private int id;
    private int money;
    private ArrayList<Rinkko> pets;


    // 构造方法，无参数
    public Player(){
        this.money = 500;
        this.pets = new ArrayList<>(); // 这是一个空的喵喵列表
    }

    // 方法是否返回：当获取信息是，需要返回；当执行操作时，不需要返回。
    // 方法：获取金钱
    public int getMoney(){
        return this.money;
    }

    // 方法：设置金钱
    public void setMoney(int money) {
        this.money = money;
    }

    //方法：增加金钱
    public void addMoney(int amount){
        if(amount > 0){
            this.money += amount;
        }
    }

    // 方法：花费金钱
    public boolean spendMoney(int amount){
        if(amount > 0 && this.money >= amount){
            this.money -= amount;
            return true;
        }
        return false;
    }

    // 方法：添加宠物凛喵喵
    public void addPet(Rinkko pet){
        if(pet != null){
            this.pets.add(pet);
        }
    }

    // getPets

    public ArrayList<Rinkko> getPets(){
        return pets;
    }

    // 方法：根据索引搜索凛喵喵
    public Rinkko getPet(int index){
        if (index >= 0 && index < pets.size()) {
            return pets.get(index);
        }
        return null;
    }

    // 方法：根据名称搜索凛喵喵
    public Rinkko getPetByName(String name) {
        for(Rinkko pet : pets){
            if (pet.getName().equals(name)) {
                return pet; // 找到则立即返回
            }
        }
        return null; // 如果没有找到任何宠物，返回 null
    }

    // 方法：获取ID
    public int getId() {
        return this.id;
    }

    // 方法：设置ID
    public void setId(int id) {
        this.id = id;
    }


    // 方法：返回整个凛喵喵列表
    public void listPets(){
        if (pets.isEmpty()) {
            System.out.println("你还没有任何一只凛喵喵，请加油！");
        } else {
            System.out.println("\n📋 当前凛喵喵状态报告：");
            System.out.println("═══════════════════════════════");
            for(int i = 0; i < pets.size(); i++){
                System.out.println(pets.get(i).getStatus());
                if (i < pets.size() - 1) { // 如果不是最后一只，添加分隔线
                    System.out.println("───────────────────────────────");
                }
            }
            System.out.println("═══════════════════════════════");
        }
    }

    // 方法：领养凛喵喵（消耗金币）
    public boolean adoptPet(int cost){
        if (money >= cost) {
            this.money -= cost;
            Rinkko newPet = new Rinkko();
            pets.add(newPet);
            System.out.println("恭喜你领养了一只凛喵喵！///");
            return true;

        } else {
            System.out.println("金币不足，暂时无法领养凛喵喵，请多多努力。");
            return false;
        }
    }

    

}


package com.example.pet;

public enum DebuffType {
    NONE("无"),
    HUNGRY("饿了"),
    THIRSTY("渴了"),
    UNHAPPY("不开心"),
    SICK("生病");

    private final String description;

    DebuffType(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

}

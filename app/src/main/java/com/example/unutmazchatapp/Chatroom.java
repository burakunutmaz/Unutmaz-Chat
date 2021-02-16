package com.example.unutmazchatapp;

public class Chatroom {
    private String name;
    private String desc;
    private String creator;
    private String iconName;

    public Chatroom(){

    }

    public Chatroom(String name, String desc, String creator, String iconName){
        this.name = name;
        this.desc = desc;
        this.creator = creator;
        this.iconName = iconName;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getCreator() {
        return creator;
    }

    public String getIconName(){
        return iconName;
    }
}

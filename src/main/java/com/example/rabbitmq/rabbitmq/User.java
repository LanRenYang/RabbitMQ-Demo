package com.example.rabbitmq.rabbitmq;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private int age;
    private String num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


    public static void main(String[] args) {
        User user = new User();
        System.out.println(user);
        System.out.println(user.getName());
    }
}

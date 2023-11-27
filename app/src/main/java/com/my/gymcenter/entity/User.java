package com.my.gymcenter.entity;

public class User {
    public int id;
    public String username;
    public String password;
    public String permission;
    public String phone_num;
    public String self_sign;
    public User() {

    }
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    public String get_self_sign(){return self_sign;}
    public void set_self_sign(String self_sign){this.self_sign=self_sign;}
    public String get_phone_num() {
        return phone_num;
    }
    public void set_phone_num(String phone_num) {
        this.phone_num = phone_num;
    }
    public String get_permission(){return permission;}

    public void set_permission(String permission){this.permission = permission;}

    public int get_id() { return id; }

    public void set_id(int id) {
        this.id = id;
    }
    public String get_username() {
        return username;
    }
    public void set_username(String username) {
        this.username = username;
    }
    public String get_password() {
        return password;
    }
    public void set_password(String password) {
        this.password = password;
    }
    public boolean equals(Object obj) {
        if(this==obj) return true;
        else if(obj instanceof User)  return this.id==((User) obj).id;
        else return false;
    }

}

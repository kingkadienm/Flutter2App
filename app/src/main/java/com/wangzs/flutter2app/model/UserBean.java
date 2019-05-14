package com.wangzs.flutter2app.model;
/**
 * @description:
 * @author: wangzs
 * @date: 2019-05-12 20:26
 * @version:
 *
*/

public class UserBean {
    String name;

    String data;

    String sex;

    int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", data='" + data + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }
}

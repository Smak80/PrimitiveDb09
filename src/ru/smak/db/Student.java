package ru.smak.db;

import java.sql.Date;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String group;
    private Date birthday;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student(){}
    public Student(
            int id,
            String firstName,
            String lastName,
            String group,
            Date birthday
    ){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.birthday = birthday;
    }

    @Override
    public String toString(){
        return id + ". " + firstName + " " + lastName + " " + group + " " + birthday;
     }

}

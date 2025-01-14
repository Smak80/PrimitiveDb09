package ru.smak.db;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            var dbh = DbHelper.getInstance();
            try {
                dbh.createStudentTable();
            } catch (SQLException e){
                System.out.println("Не удалось создать таблицу студентов");
                e.printStackTrace();
            }
            try {
                dbh.addStudent(new Student(
                        0,
                        "Петр",
                        "Петров",
                        "09-26x",
                        new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2004-03-19").getTime())
                        )
                );
            } catch (SQLException e) {
                System.out.println("Не удалось добавить студента");
                e.printStackTrace();
            } catch (ParseException e){
                System.out.println("Ошибка преобразования даты");
            }
            for(var s: dbh.getAllStudents()){
                System.out.println(s);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка установления соединения с базой данных");
        }
    }

}

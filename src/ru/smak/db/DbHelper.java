package ru.smak.db;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {
    private static @Nullable DbHelper instance = null;
    private Connection conn;

    private DbHelper() throws SQLException{
        conn = DriverManager
                .getConnection(
                        "jdbc:postgresql://localhost:5432/db09-26x?useSSL=false",
                        "postgres",
                        "postgres"
                );
    }

    public static DbHelper getInstance() throws SQLException{
        if (instance == null){
            instance = new DbHelper();
        }
        return instance;
    }

    public void createStudentTable() throws SQLException{
        var sql = "CREATE TABLE IF NOT EXISTS student(" +
                "id serial not null primary key," +
                "firstname varchar(50) not null," +
                "lastname varchar(50) not null," +
                "\"group\" varchar(7)," +
                "birthday date not null" +
                ")";
        var stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public void addStudent(Student s) throws SQLException{
        var sql = "INSERT INTO student (" +
                "firstname, lastname, \"group\", birthday" +
                ") VALUES (" +
                "?, ?, ?, ?" +
                ")";
        var stmt = conn.prepareStatement(sql);
        stmt.setString(1, s.getFirstName());
        stmt.setString(2, s.getLastName());
        stmt.setString(3, s.getGroup());
        stmt.setDate(4, s.getBirthday());
        stmt.execute();
    }

    public List<Student> getAllStudents() throws SQLException{
        var sql = "SELECT * FROM student";
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery(sql);
        List<Student> lst = new ArrayList<>();
        while (rs.next()){
            var s = new Student();
            s.setId(rs.getInt("Id"));
            s.setFirstName(rs.getString("firstname"));
            s.setLastName(rs.getString("lastname"));
            s.setGroup(rs.getString("group"));
            s.setBirthday(rs.getDate("birthday"));
            lst.add(s);
        }
        return lst;
    }
}

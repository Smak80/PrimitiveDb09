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

    public void createPointsTable() throws SQLException{
        var sql = "CREATE TABLE IF NOT EXISTS points(" +
                "id serial not null primary key," +
                "subject varchar(50) not null," +
                "id_student int not null REFERENCES student(id) ON DELETE RESTRICT ON UPDATE CASCADE," +
                "point int not null" +
                ")";
        var stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public void addStudent(Student s) throws SQLException{
        conn.setAutoCommit(false);
        var sp = conn.setSavepoint("adding");
        var sql = "INSERT INTO student (" +
                "firstname, lastname, \"group\", birthday" +
                ") VALUES (" +
                "?, ?, ?, ?" +
                ")";
        try {
            var stmt = conn.prepareStatement(sql);
            stmt.setString(1, s.getFirstName());
            stmt.setString(2, s.getLastName());
            stmt.setString(3, s.getGroup());
            stmt.setDate(4, s.getBirthday());
            stmt.execute();
            conn.commit();
        } catch (SQLException e){
            conn.rollback(sp);
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
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
        rs.close();
        return lst;

    }
    public List<Student> getStudentsByGroup(String group) throws SQLException{
        var sql = "SELECT * FROM student WHERE \"group\" like ?";
        var stmt = conn.prepareStatement(sql);
        stmt.setString(1, group);
        var rs = stmt.executeQuery();
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
        rs.close();
        return lst;

    }

    public List<Points> getAllPoints() throws SQLException{
        var sql = "SELECT * FROM student INNER JOIN points ON student.id = points.id_student";
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery(sql);
        List<Points> lst = new ArrayList<>();
        while (rs.next()){
            var p = new Points();
            p.setId(rs.getInt("id"));
            p.setSubject(rs.getString("subject"));
            p.setStudent_id(rs.getInt("id_student"));
            p.setFullName(rs.getString("firstname")+" "+rs.getString("lastname"));
            p.setPoint(rs.getInt("point"));
            lst.add(p);
        }
        rs.close();
        return lst;
    }
}

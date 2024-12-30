package org.example;

import java.sql.*;
import java.util.HashSet;

public class Main {
    static Connection connection = null;
    static Statement statement = null;
    public static void main(String[] args) throws SQLException {
        connect();

//        statement.execute("drop table if exists public.progress;\n" +
//                "\n" +
//                "drop table if exists public.students;\n" +
//                "\n" +
//                "drop table if exists public.subjects;");
//        statement.execute("CREATE TABLE IF NOT EXISTS students\n" +
//                "(id serial NOT NULL PRIMARY KEY,\n" +
//                " name varchar(30) NOT NULL,\n" +
//                " PassportSerial varchar(4) not null,\n" +
//                " PassportNumber varchar(6) not null,\n" +
//                " UNIQUE (PassportSerial, PassportNumber)\n" +
//                ");");
//        statement.execute("CREATE TABLE IF NOT EXISTS subjects\n" +
//                "(id serial NOT NULL PRIMARY KEY,\n" +
//                " name varchar(50) NOT NULL\n" +
//                ");");
//
//        statement.execute("CREATE TABLE IF NOT EXISTS progress\n" +
//                "(id serial NOT NULL PRIMARY KEY,\n" +
//                " student int NOT NULL REFERENCES students(id) ON DELETE CASCADE,\n" +
//                " subject int NOT NULL REFERENCES subjects(id),\n" +
//                " mark smallint NOT NULL CHECK(mark BETWEEN 2 and 5)\n" +
//                ");");
//        statement.execute("INSERT into students (id, name, PassportSerial, PassportNumber)\n" +
//                "values\n" +
//                "    (1, 'Вася', 3311, 435678),\n" +
//                "    (2, 'Коля', 3322, 342345),\n" +
//                "    (3, 'Петя', 3333, 123456),\n" +
//                "    (4, 'Маша', 3344, 234567),\n" +
//                "    (5, 'Даша', 3355, 345678),\n" +
//                "    (6, 'Саша', 3366, 456789)");
//        statement.execute("INSERT into subjects (id, name)\n" +
//                "values\n" +
//                "    (1, 'Математика'),\n" +
//                "    (2, 'Русский язык'),\n" +
//                "    (3, 'Физика'),\n" +
//                "    (4, 'Информатика');");
//        statement.execute("INSERT into progress (id, student, subject, mark)\n" +
//                "values\n" +
//                "    (1, 1, 1, 5),\n" +
//                "    (2, 1, 2, 4),\n" +
//                "    (3, 1, 3, 5),\n" +
//                "    (4, 1, 4, 4),\n" +
//                "    (5, 2, 2, 4),\n" +
//                "    (6, 2, 3, 3),\n" +
//                "    (7, 2, 4, 4),\n" +
//                "    (8, 3, 1, 5),\n" +
//                "    (9, 3, 2, 5),\n" +
//                "    (10, 3, 3, 5),\n" +
//                "    (11, 3, 4, 5),\n" +
//                "    (12, 4, 1, 4),\n" +
//                "    (13, 4, 2, 4),\n" +
//                "    (14, 4, 3, 4),\n" +
//                "    (15, 4, 4, 4),\n" +
//                "    (16, 5, 3, 3),\n" +
//                "    (17, 5, 4, 3),\n" +
//                "    (18, 6, 1, 2),\n" +
//                "    (19, 6, 2, 2),\n" +
//                "    (20, 6, 3, 2),\n" +
//                "    (21, 6, 4, 2);\n");
        System.out.println("Вывести список студентов, сдавших определенный предмет, на оценку выше 3");
        var res = statement.executeQuery("Select s.name, p.Mark, ss.name from Students s\n" +
                "INNER JOIN Progress p\n" +
                "on s.id = p.student\n" +
                "inner join subjects ss\n" +
                "on ss.id = p.subject\n" +
                "WHERE p.mark > 3 and ss.name = 'Математика';");
        while (res.next()) {
            System.out.println(res.getString(1));
        }
        System.out.println("Посчитать средний бал по определенному предмету");
        var res2 = statement.executeQuery("select avg(p.mark) as \"Средний балл\" from progress p\n" +
                "inner join subjects s on p.subject = s.id\n" +
                "where s.name = 'Математика';");
        while (res2.next()) {
            double avg = res2.getDouble(1);
            System.out.println(avg);
        }
        System.out.println("Посчитать средний балл по определенному студенту");
        var res3 = statement.executeQuery("select avg(p.mark) as \"Средний балл\" from progress p\n" +
                "inner join subjects s on p.subject = s.id\n" +
                "inner join students s2 on p.student = s2.id\n" +
                "where s2.name = 'Даша';");
        while (res3.next()) {
            double avg = res3.getDouble(1);
            System.out.println(avg);
        }
        System.out.println("Найти три предмета, которые сдали наибольшее количество студентов");
        var res4 = statement.executeQuery("SELECT count(*), s.name from progress p\n" +
                "inner join subjects s on s.id = p.subject\n" +
                "where p.mark > 2\n" +
                "group by s.name\n" +
                "order by count(*) desc limit 3;");
        while (res4.next()) {
            int cnt = res4.getInt(1);
            String name  =  res4.getString(2);
            System.out.println(cnt + " " + name);
        }

        System.out.println("Двоичники");
        var res5 = statement.executeQuery("SELECT s.name FROM Students s " +
                "LEFT JOIN Progress p ON s.id = p.student " +
                "GROUP BY s.name " +
                "HAVING COUNT(p.mark) < 4 OR COUNT(CASE WHEN p.mark < 3 THEN 1 END) > 0;");

        while (res5.next()) {
            String studentName = res5.getString(1);
            System.out.println(studentName);
        }
        disconnect();

    }
    public static void connect() {

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lab10", "postgres", "Vfvf18071981_");
            statement = connection.createStatement();
            System.out.println("Connected");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//вывести тех кто сдал как минимум один прредмет на два
    public static void disconnect() {
        try{
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
            System.out.println("Disconnected");
        }
        catch (SQLException ee) {
            ee.printStackTrace();
        }
    }
}
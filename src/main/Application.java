package main;

import institution.University;
import institution.interlink.Internship;
import person.Student;
import person.consciousness.Knowledge;

import java.sql.*;
import java.util.Scanner;

public class Application {


    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Відсутній JDBC");
            e.printStackTrace();
            return;
        }

        System.out.println("1-Переглянути студентів \n2-Додати студентів");
        Scanner key = new Scanner(System.in);
        BD bd = new BD();
        switch (key.nextInt()) {
            case 1: {
                bd.view();
                break;
            }
            case 2: {
                bd.addStud();
                main(args);
            }

        }

        /*University university = new University("CH.U.I.");
        university.addStudent(new Student("Andrew Kostenko"));
        Internship internship = new Internship("Interlink");
        System.out.println("List of internship's students:");
        System.out.println(internship.getStudents());*/
    }

}

class BD {
    private final String url = "jdbc:mysql://127.0.0.1:3306/students?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final String user = "root";
    private final String password = "";
    private Connection con;
    private Statement stmt, stmt2;
    private ResultSet rs, inquery;

    private String univ;
    private String nameS;
    private int know;


    public void view() {
        String query = "select * from list";
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                stmt2 = con.createStatement();
                if (rs.getString("Internship").length() == 0) {
                    if (rs.getInt("Knowledge") > 2) {
                        stmt2.execute("UPDATE `List` SET `Internship` = 'Interlink' WHERE `List`.`Id` = " + rs.getInt("Id") + ";");
                    } else {
                        stmt2.execute("UPDATE `List` SET `Internship` = 'Study' WHERE `List`.`Id` = " + rs.getInt("Id") + ";");
                    }
                }
            }
            stmt = con.createStatement();
            inquery = stmt.executeQuery("SELECT * FROM list WHERE Internship= 'Interlink';");
            System.out.println("Студенти які проходят на інтернатуру:");
            while (inquery.next()) {
                System.out.println(inquery.getString("University") + " " + inquery.getString("Name") + " " + inquery.getInt("Knowledge"));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public void addStud() {
        Scanner add = new Scanner(System.in);
        System.out.println("Ім'я нового студента");
        nameS = add.next();
        System.out.println("Рівень знать: 1-Низький, 2-Середній, 3-Високий");
        know = add.nextInt();
        System.out.println("Університет");
        univ = add.next();
        System.out.println("Студента додано");
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            stmt.execute("INSERT INTO `List` (`Id`, `University`, `Name`, `Knowledge`, `Internship`) VALUES (NULL, '" + univ + "', '" + nameS + "', '" + know + "', '');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Бажаєте ще додати? Y/N");
        switch (add.next()) {
            case "Y": {
                addStud();
                break;
            }
            case "N": {
                break;
            }
        }
    }
}
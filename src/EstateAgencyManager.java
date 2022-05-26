import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class EstateAgencyManager {

    private String user;
    private String password;
    private String url;
    private Statement statement;
    private final String creatDatabase = "create database EstateAndManager";
    private final String creatManagerTable = "create table Manager(" +
            "Manager_ID int," +
            "M_name varchar(10)," +
            "Gender varchar(1)," +
            "M_Phone varchar(12)," +
            "M_email varchar(50)," +
            "Salary int," +
            "primary key (Manager_ID))character set = utf8;";
    private final String creatUserTable = "create table User(" +
            "User_ID int," +
            "U_name varchar(10)," +
            "Gender varchar(1)," +
            "U_Phone varchar(12)," +
            "U_email varchar(50)," +
            "primary key (User_ID))character set = utf8;";
    private final String creatHouseTable = "create table House(" +
            "House_ID int," +
            "City varchar(15)," +
            "Address varchar(50)," +
            "Total_price int," +
            "Size int," +
            "Evaluation varchar(255)," +
            "Manager_ID int," +
            "User_ID int," +
            "primary key (House_ID)," +
            "foreign key (Manager_ID) references Manager(Manager_ID)," +
            "foreign key (User_ID) references User(User_ID))character set = utf8;";

    public EstateAgencyManager(String user, String password, String url){
        this.user = user;
        this.password = password;
        this.url = url;

    }

    public boolean connecting(){
        try{
            Connection con = DriverManager.getConnection(url,user,password);
            Statement statement = con.createStatement();

            ResultSet result = statement.executeQuery("show databases");

            while (result.next()){
                if(result.getString(1).equalsIgnoreCase("EstateAndManager")){
                    con = DriverManager.getConnection(url+"EstateAndManager",user,password);
                    this.statement = con.createStatement();
                    this.statement.executeQuery("show tables");
                    return true;
                }
            }

            System.out.print("No such database, build a new one(Y/N)>");
            Scanner scanner = new Scanner(System.in);
            if(scanner.nextLine().equalsIgnoreCase("Y")){
                statement.executeUpdate(creatDatabase);
                con = DriverManager.getConnection(url+"EstateAndManager",user,password);
                this.statement = con.createStatement();
                this.statement.executeUpdate(creatManagerTable);
                this.statement.executeUpdate(creatUserTable);
                this.statement.executeUpdate(creatHouseTable);
                this.statement.executeQuery("show tables");
                return true;
            }


        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }


    public String importExcel(String fileName){
        File file = new File(fileName);

        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String read = "";
            bufferedReader.readLine();
            while((read = bufferedReader.readLine()) != null){
                System.out.println(read);
                //Add house, manager, and user
            }
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            System.out.println("file not found");
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("IO error");
        }

        return null;
    }

    public boolean addHouseData(ArrayList<String> data){

        try{
            this.statement.executeUpdate("insert into House VALUEs (" + Integer.parseInt(data.get(0)) + ",'" + data.get(1) + "','" + data.get(2) + "'," + Integer.parseInt(data.get(3)) + "," + Integer.parseInt(data.get(4)) + ",'" + data.get(5) + "'," + Integer.parseInt(data.get(6))  + "," + Integer.parseInt(data.get(7)) + ");" );
        } catch (SQLException throwables) {
//            throwables.printStackTrace();
            System.out.println("SQL error:" + throwables);
            return false;
        } catch (NumberFormatException e){
            System.out.println("ID isn't a number");
            return false;
        }
        return true;
    }


    public boolean addManagerData(ArrayList<String> data){
        try{
            this.statement.executeUpdate("insert into Manager VALUEs (" + Integer.parseInt(data.get(0)) + ",'" + data.get(1) + "','" + data.get(2) + "','" + data.get(3) + "','" + data.get(4) + "'," + Integer.parseInt(data.get(5)) + ");" );
        } catch (SQLException throwables) {
//            throwables.printStackTrace();
            System.out.println("SQL error:" + throwables);
            return false;
        } catch (NumberFormatException e){
            System.out.println("ID isn't a number");
            return false;
        }
        return true;
    }


    public boolean addUserData(ArrayList<String> data){
        try{
            this.statement.executeUpdate("insert into User VALUEs (" + Integer.parseInt(data.get(0)) + ",'" + data.get(1) + "','" + data.get(2) + "','" + data.get(3) + "','" + data.get(4) + "');" );
        } catch (SQLException throwables) {
//            throwables.printStackTrace();
            System.out.println("SQL error:" + throwables);
            return false;
        } catch (NumberFormatException e){
            System.out.println("ID isn't a number");
            return false;
        }
        return true;
    }




    public String update(){

        return null;
    }

}

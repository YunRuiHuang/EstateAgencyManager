import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author yunrui huang
 */
public class EstateAgencyManager {

    private String user;
    private String password;
    private String url;
    private Statement statement;
    private final String creatDatabase = "create database EstateAndManager";
    private final String creatManagerTable = "create table Manager(" +
            "Manager_ID int," +
            "M_name varchar(10)," +
            "M_Gender varchar(1)," +
            "M_Phone varchar(12)," +
            "M_email varchar(50)," +
            "Salary int," +
            "primary key (Manager_ID))character set = utf8;";
    private final String creatUserTable = "create table User(" +
            "User_ID int," +
            "U_name varchar(10)," +
            "U_Gender varchar(1)," +
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

    /**
     * constructor of EstateAgencyManager class
     * @param user
     * the user name of database
     * @param password
     * the password of user of database
     * @param url
     * the url of database, don't add the name of database at the end
     */
    public EstateAgencyManager(String user, String password, String url){
        this.user = user;
        this.password = password;
        this.url = url;

    }

    /**
     * connection to the database using given info from constructor,
     * also check if database has been build or not
     * if not database, it will ask for build a new one or not
     * @return
     * return true if it has database or building database, otherwise return false
     */
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

    /**
     * import a .csv excel file in to database,
     * struct of data check on readMe
     * @param fileName
     * the filename of Excel used to import
     */
    public void importExcel(String fileName){
        File file = new File(fileName);

        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String read = "";
            bufferedReader.readLine();
            ArrayList<String> houseList = new ArrayList<>();
            ArrayList<String> managerList = new ArrayList<>();
            ArrayList<String> userList = new ArrayList<>();

            while((read = bufferedReader.readLine()) != null){
//                System.out.println(read);
                //Add house, manager, and user
                String[] spilt = read.split(",");
                for (int i = 0; i < 6; i++) {
                    houseList.add(spilt[i]);
                }
                houseList.add(spilt[11]);
                houseList.add(spilt[6]);
                for (int i = 6; i < 11 ; i++) {
                    userList.add(spilt[i]);
                }
                for (int i = 11; i < spilt.length; i++) {
                    managerList.add(spilt[i]);
                }

                addManagerData(managerList);
                addUserData(userList);
                addHouseData(houseList);

                managerList.clear();
                userList.clear();
                houseList.clear();

            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            System.out.println("file not found");
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("IO error");
        }

    }

    /**
     * Export all data from database to Excel file
     * @param fileName
     * the filename of Excel used to save the data
     */
    public void exportExcel(String fileName){
        File file = new File(fileName);

        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file),"utf-8");
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);

            String sequence = "House_id,City,Address,Total_Price,Size,Evaluation,User.User_ID,U_name,U_Gender,U_Phone,U_email,Manager.Manager_ID,M_name,M_Gender,M_Phone,M_Email,Salary";
            String sql = "select " + sequence + " from house join user on house.user_id = user.user_id join manager on house.manager_id = manager.manager_id";

            ResultSet result = statement.executeQuery(sql);
            writer.write('\ufeff');
            writer.flush();
            writer.write(sequence);
            writer.newLine();
            while(result.next()){
                String writein = "";
                for (int i = 1; i <= 17 ; i++) {
                    writein = writein + result.getString(i) + ",";
                }
                writein = writein.substring(0,writein.length()-1);
                writer.write(writein);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("IO error");
        } catch (SQLException e) {
            System.out.println("SQL error:" + e);
        }

    }

    /**
     * query from database and save data into Excel file
     * @param fileName
     * the file name of Excel used to save data
     * @param selectList
     * the select request list, struct check on readMe
     */
    public void queryToExcel(String fileName, ArrayList<String> selectList){
        File file = new File(fileName);
        String whereQuery = " where";
        if(!selectList.get(0).equalsIgnoreCase("*")){
            whereQuery = whereQuery + " city = " + selectList.get(0) + "&&";
        }else{
            whereQuery = whereQuery + " city = city&&";
        }
        if(!selectList.get(1).equalsIgnoreCase("*")){
            whereQuery = whereQuery + " total_price = " + selectList.get(1) + "&&";
        }else{
            whereQuery = whereQuery + " total_price = total_price&&";
        }
        if(!selectList.get(2).equalsIgnoreCase("*")){
            whereQuery = whereQuery + " size = " + selectList.get(2) + "&&";
        }else{
            whereQuery = whereQuery + " size = size&&";
        }
        if(!selectList.get(3).equalsIgnoreCase("*")){
            whereQuery = whereQuery + " Manager.manager_id = " + selectList.get(3) + "&&";
        }else{
            whereQuery = whereQuery + " Manager.manager_id = Manager.manager_id&&";
        }
        if(!selectList.get(4).equalsIgnoreCase("*")){
            whereQuery = whereQuery + " m_name = " + selectList.get(4) + "&&";
        }else{
            whereQuery = whereQuery + " m_name = m_name&&";
        }
        if(!selectList.get(5).equalsIgnoreCase("*")){
            whereQuery = whereQuery + " u_name = " + selectList.get(5);
        }else{
            whereQuery = whereQuery + " u_name = u_name";
        }

        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file),"utf-8");
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);

            String sequence = "House_id,City,Address,Total_Price,Size,Evaluation,User.User_ID,U_name,U_Gender,U_Phone,U_email,Manager.Manager_ID,M_name,M_Gender,M_Phone,M_Email,Salary";
            String sql = "select " + sequence + " from house join user on house.user_id = user.user_id join manager on house.manager_id = manager.manager_id" + whereQuery;

            ResultSet result = statement.executeQuery(sql);
            writer.write('\ufeff');
            writer.flush();
            writer.write(sequence);
            writer.newLine();
            while(result.next()){
                String writein = "";
                for (int i = 1; i <= 17 ; i++) {
                    writein = writein + result.getString(i) + ",";
                }
                writein = writein.substring(0,writein.length()-1);
                writer.write(writein);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("IO error");
        } catch (SQLException e) {
            System.out.println("SQL error:" + e);
        }

    }


    /**
     * Add new house data into database
     * @param data
     * the data use to add, struct check on ReadMe
     * @return
     * true if successful adding
     */
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


    /**
     * Add new manager data into database
     * @param data
     * the data used to add, struct check on ReadMe
     * @return
     * true if successful adding
     */
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

    /**
     * Add new user data into database
     * @param data
     * the data used to add, struct check on ReadMe
     * @return
     * true if successful adding
     */
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



    /**
     * update new house data in database
     * @param data
     * the data used to update, struct check on ReadMe
     * @return
     * true if successful update
     */
    public boolean updateHouseData(ArrayList<String> data){

        try{
            this.statement.executeUpdate("update House set city = '" + data.get(1) + "',address = '" + data.get(2) + "',total_price =" + Integer.parseInt(data.get(3)) + ",size =" + Integer.parseInt(data.get(4)) + ",evaluation ='" + data.get(5) + "',manager_id =" + Integer.parseInt(data.get(6))  + ",user_id =" + Integer.parseInt(data.get(7)) + " where house_id =" + Integer.parseInt(data.get(0)) +";" );
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


    /**
     * update new manager data in database
     * @param data
     * the data used to update, struct check on ReadMe
     * @return
     * true if successful update
     */
    public boolean updateManagerData(ArrayList<String> data){
        try{
            this.statement.executeUpdate("update Manager set M_name = '" + data.get(1) + "',m_gender = '" + data.get(2) + "',m_phone = '" + data.get(3) + "',m_email = '" + data.get(4) + "',salary =" + Integer.parseInt(data.get(5)) + " where manager_id =" + Integer.parseInt(data.get(0)) + ";" );
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

    /**
     * update new user data in database
     * @param data
     * the data used to update, struct check on ReadMe
     * @return
     * true if successful update
     */
    public boolean updateUserData(ArrayList<String> data){
        try{
            this.statement.executeUpdate("update user set u_name = '" + data.get(1) + "',u_gender = '" + data.get(2) + "',u_phone = '" + data.get(3) + "',u_email = '" + data.get(4) + "' where user_id =" + Integer.parseInt(data.get(0)) + ";" );
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


    /**
     * delete house data from database
     * @param houseID
     * the id of house used to delete
     * @return
     * true if successful delete
     */
    public boolean deleteHouseData(int houseID){
        try{
            this.statement.executeUpdate("delete from house where house_id =" + houseID);
        } catch (SQLException throwables) {
//            throwables.printStackTrace();
            System.out.println("SQL error: " + throwables);
            return false;
        }
        return true;
    }

    /**
     * delete user data from database
     * @param userID
     * the id of user used to delete
     * @return
     * true if successful delete
     */
    public boolean deleteUserData(int userID){
        try{
            this.statement.executeUpdate("delete from user where user_id =" + userID);
        } catch (SQLException throwables) {
//            throwables.printStackTrace();
            System.out.println("SQL error: " + throwables);
            return false;
        }
        return true;
    }

    /**
     * delete manager data from database
     * @param managerID
     * the id of manager used to delete
     * @return
     * true if successful delete
     */
    public boolean deleteManagerData(int managerID){
        try{
            this.statement.executeUpdate("delete from manager where manager_id =" + managerID);
        } catch (SQLException throwables) {
//            throwables.printStackTrace();
            System.out.println("SQL error: " + throwables);
            return false;
        }
        return true;
    }
}

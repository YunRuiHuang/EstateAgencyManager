import java.util.ArrayList;

public class Driver {

    public static void main(String[] args) {

        //start a connection
        EstateAgencyManager estateAgencyManager = new EstateAgencyManager("root","123456","jdbc:mysql://localhost:3306/");
        if(estateAgencyManager.connecting()){
            System.out.println("Connect success!");

            //import data from Excel to database
            estateAgencyManager.importExcel("testingData.csv");

            //export data from database to Excel
            estateAgencyManager.exportExcel("testExport.csv");
        }else{
            System.out.println("Connect fail");
        }

        //query data where manager_ID = 1 and export to testQuery Excel file
        ArrayList<String> list = new ArrayList<>();
        list.add("*");
        list.add("*");
        list.add("*");
        list.add("1");
        list.add("*");
        list.add("*");
        estateAgencyManager.queryToExcel("testQuery.csv",list);

        //update manager data
        list = new ArrayList<>();
        list.add("1");
        list.add("经理");
        list.add("男");
        list.add("98765432101");
        list.add("a@163.com");
        list.add("1000");
        if(estateAgencyManager.updateManagerData(list)){
            System.out.println("update!");
        }

        //update user data
        list = new ArrayList<>();
        list.add("1");
        list.add("房东");
        list.add("男");
        list.add("12345678901");
        list.add("1@163.com");
        if(estateAgencyManager.updateUserData(list)){
            System.out.println("update!");
        }

        //update house data and export to excel
        list = new ArrayList<>();
        list.add("1");
        list.add("城市1");
        list.add("XXXXX");
        list.add("100000");
        list.add("50");
        list.add("N/A");
        list.add("1");
        list.add("1");
        if(estateAgencyManager.updateHouseData(list)){
            estateAgencyManager.exportExcel("testExport.csv");
            System.out.println("import!");
        }

        //delete house_id = 1 record and manager_id = 1 record
        //delete house record expect to be success, and delete manager record expect to be fail
        estateAgencyManager.deleteHouseData(1);
        estateAgencyManager.deleteManagerData(1);
    }

}

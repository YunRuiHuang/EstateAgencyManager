import java.util.ArrayList;

public class Driver {

    public static void main(String[] args) {

        EstateAgencyManager estateAgencyManager = new EstateAgencyManager("root","123456","jdbc:mysql://localhost:3306/");
        if(estateAgencyManager.connecting()){
            System.out.println("Connect success!");
            estateAgencyManager.importExcel("testingData.csv");
        }else{
            System.out.println("Connect fail");
        }

        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("经理1");
        list.add("男");
        list.add("98765432101");
        list.add("a@163.com");
        list.add("1000");
        if(estateAgencyManager.addManagerData(list)){
            System.out.println("import!");
        }
        list = new ArrayList<>();
        list.add("1");
        list.add("房东1");
        list.add("男");
        list.add("12345678901");
        list.add("1@163.com");
        if(estateAgencyManager.addUserData(list)){
            System.out.println("import!");
        }
        list = new ArrayList<>();
        list.add("1");
        list.add("城市1");
        list.add("XXXXX");
        list.add("100000");
        list.add("50");
        list.add("N/A");
        list.add("1");
        list.add("1");
        if(estateAgencyManager.addHouseData(list)){
            System.out.println("import!");
        }
    }

}

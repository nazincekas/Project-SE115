import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    static double[][][] profit = new double[MONTHS][DAYS][COMMS];

    public static void loadData(){
        for(int i=0;i<MONTHS;i++){
            Scanner reader=null;
            try {
                File file = new File("Data_Files/" + months[i] + ".txt");
                reader=new Scanner(file);
                while(reader.hasNextLine()){
                    String Line=reader.nextLine();
                    String[] part=Line.split(",");
                    if(part.length!=3){
                        continue;
                    }
                    String day=part[0].trim();
                    String commodity=part[1].trim();
                    String profitSt=part[2].trim();
                    int dayIndex=Integer.parseInt(day)-1;
                    double profitValue=Double.parseDouble(profitSt);
                    if(dayIndex<0||dayIndex>DAYS){
                        continue;
                    }
                    int commodityIndex= -1;
                    for(int j=0;j<commodities.length;j++){
                        if(commodities[j].equals(commodity)){
                            commodityIndex=j;
                            break;
                        }
                    }
                    if(commodityIndex==-1){
                        continue;
                    }

                    profit[i][dayIndex][commodityIndex]=profitValue;
                }
            }
            catch(NumberFormatException ex){
                System.err.println("Invalid number format"+ months[i]);
            }
            catch(FileNotFoundException e){
                System.out.println(months[i]+ "skipped");
            }
            finally{
                if(reader!=null){
                    reader.close();
                }
            }
        }
    }
    public static String mostProfitableCommodityInMonth(int month){
        double max = 0;
        int bestIndex = 0;
        if (month < 0 || month > 11) return "INVALİD_MONTH";
        for (int i = 1; i < COMMS; i++) {
            double total = 0;
            for (int j = 0; j < DAYS; j++) {
                total += profit[month][j][i];
            }
            if (total > max) {
                max = total;
                bestIndex = i;
            }
        }
        return commodities[bestIndex] + "" + (int) max;
    }
    public static int totalProfitOnDay(int month, int day) {
        int totalProfit = 0;
        int dayIndex = day - 1;
        if (day < 1 || day > 28) return -99999;
        if (month < 0 || month > 11) return -99999;
        for (int i = 0; i < COMMS; i++) {
            totalProfit += profit[month][dayIndex][i];
        }
        return totalProfit;
    }
    public static int commodityProfitInRange(String commodity, int fromDay, int toDay) {
        int commodityIndex=-1;
        for(int i=0;i<commodities.length;i++){
            if(commodities[i].equals(commodity)){
                commodityIndex=i;
                break;
            }
        }
        if(commodityIndex==-1)  return -99999;
        if(fromDay>toDay || fromDay<1 || fromDay>28 || toDay<1 || toDay>28)  return -99999;
        int fromDayIndex=fromDay-1;
        int toDayIndex=toDay-1;
        int totalRange=0;
        for(int i=0;i<MONTHS;i++){
            for(int j=fromDayIndex;j<=toDayIndex;j++){
                totalRange+=profit[i][j][commodityIndex];//
            }
        }
        return totalRange;
    }

}

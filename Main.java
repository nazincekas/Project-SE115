import java.io.File;
import java.util.Scanner;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    static int[][][] profit = new int[MONTHS][DAYS][COMMS];
    public static void loadData() {
        for (int i = 0; i < MONTHS; i++) {
            Scanner reader = null;
            try {
                File file = new File("Data_Files/" + months[i] + ".txt");
                reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length != 3) {
                        continue;
                    }
                    String dayStr = parts[0].trim();
                    String commodityStr = parts[1].trim();
                    String profitStr = parts[2].trim();

                    if (dayStr.equals("Day")) continue;


                    int dayIndex = Integer.parseInt(dayStr) - 1;
                    int profitValue = Integer.parseInt(profitStr);
                    if (dayIndex < 0 || dayIndex >= DAYS) continue;
                    int commodityIndex = -1;
                    for (int j = 0; j < commodities.length; j++) {
                        if (commodities[j].equals(commodityStr)) {
                            commodityIndex = j;
                            break;
                        }
                    }
                    if (commodityIndex == -1) {
                        continue;
                    }


                    profit[i][dayIndex][commodityIndex] = profitValue;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month > 11) return "INVALID_MONTH";
        int bestTotal = 0;
        int bestIndex = 0;
        for (int i = 0; i < DAYS; i++) {
            bestTotal+=profit[month][i][0];
        }
        for (int j = 1; j < COMMS; j++) {
            int total = 0;
            for (int k = 0; k < DAYS; k++) {
                total += profit[month][k][j];
            }
            if (total > bestTotal) {
                bestTotal = total;
                bestIndex = j;
            }
        }
        return commodities[bestIndex] + " " +bestTotal;

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
        commodity=commodity.trim();
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
    public static int bestDayOfMonth(int month){
        if(month<0 || month>11) return -1;
        int bestTotal=0;
        int bestDayIndex=0;
        for(int i=0;i<COMMS;i++){
            bestTotal+=profit[month][0][i];
        }
        for(int j=1;j<DAYS;j++){
            int dayTotal=0;
            for(int i=0;i<COMMS;i++){
                dayTotal+=profit[month][j][i];
            }
            if(dayTotal>bestTotal){
                bestTotal=dayTotal;
                bestDayIndex=j;
            }
        }
        return bestDayIndex+1;
    }
    public static String bestMonthForCommodity(String commodity){
        commodity = commodity.trim();
        int commodityIndex=-1;
        for(int i=0;i<commodities.length;i++){
            if(commodities[i].equals(commodity)){
                commodityIndex=i;
                break;
            }
        }
        if (commodityIndex==-1)  return "INVALID_COMMODITY";
        int bestTotal=0;
        int bestMonthIndex=0;
        for(int j=0;j<DAYS;j++){
            bestTotal+=profit[0][j][commodityIndex];
        }
        for(int i=1;i<MONTHS;i++){
            int monthTotal=0;
            for(int j=0;j<DAYS;j++){
                monthTotal+=profit[i][j][commodityIndex];
            }
            if(monthTotal>bestTotal){
                bestTotal=monthTotal;
                bestMonthIndex=i;
            }
        }
        return months[bestMonthIndex];
    }
    public static int consecutiveLossDays(String commodity){
        commodity = commodity.trim();
        int commodityIndex=-1;
        for(int i=0;i<commodities.length;i++){
            if(commodities[i].equals(commodity)){
                commodityIndex=i;
                break;
            }
        }
        if(commodityIndex==-1)  return -1;
        int currentStreak=0;
        int maxStreak=0;
        for(int i=0;i<MONTHS;i++){
            for(int j=0;j<DAYS;j++){
                if(profit[i][j][commodityIndex]<0){
                    currentStreak++;
                    if(currentStreak>maxStreak)  maxStreak=currentStreak;
                }
                else{
                    currentStreak=0;
                }
            }
        }
        return maxStreak;
    }
}

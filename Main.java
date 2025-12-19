import java.io.*;
import java.util.*;
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
    public static int daysAboveThreshold(String commodity, int threshold){
        commodity = commodity.trim();
        int commodityIndex=-1;
        for(int i=0;i<commodities.length;i++){
            if(commodities[i].equals(commodity)){
                commodityIndex=i;
                break;
            }
        }
        if(commodityIndex==-1)  return -1;
        int count=0;
        for(int i=0;i<MONTHS;i++){
            for(int j=0;j<DAYS;j++){
                if(profit[i][j][commodityIndex]>threshold){
                    count++;
                }
            }
        }
        return count;
    }
    public static int biggestDailySwing(int month){
        if (month < 0 || month > 11) return -99999;
        int maxdiff=0;
        int diff=0;
        for(int i=0;i<DAYS-1;i++){
            int totalProfitday1=0;
            int totalProfitday2=0;
            for(int j=0;j<COMMS;j++ ){
                totalProfitday1+=profit[month][i][j];
                totalProfitday2+=profit[month][i+1][j];
            }
            diff=totalProfitday2-totalProfitday1;
            if(diff<0) {
                diff=diff*(-1);
            }
            if(diff>maxdiff){
                maxdiff=diff;

            }
        }
        return maxdiff;
    }
    public static String compareTwoCommodities(String c1, String c2){
        c1 = c1.trim();
        c2 = c2.trim();
        int commodityIndex1=-1;
        int commodityIndex2=-1;
        for(int i=0;i<commodities.length;i++){
            if(commodities[i].equals(c1)){
                commodityIndex1=i;
                break;
            }
        }
        for(int i=0;i<commodities.length;i++){
            if(commodities[i].equals(c2)){
                commodityIndex2=i;
                break;
            }
        }
        if (commodityIndex1==-1||commodityIndex2==-1)  return "INVALID_COMMODITY";
        int total1=0;
        int total2=0;

        for(int i=0;i<MONTHS;i++){
            for(int j=0;j<DAYS;j++){
                total1+=profit[i][j][commodityIndex1];
                total2+=profit[i][j][commodityIndex2];

            }
        }

        if(total1==total2) return "Equal";
        if(total1>total2) {
            return "C1 is better by " + (total1-total2) ;
        } else{
            return "C2 is better by " +  (total2-total1);
        }
    }
    public static String bestWeekOfMonth(int month) {
        if (month < 0 || month > 11) return "INVALID_MONTH";
        int bestWeek = 1;


        int bestWeekProfit = 0;
        for (int day = 0; day < 7; day++) {
            for (int i = 0; i < COMMS; i++){
                bestWeekProfit += profit[month][day][i];
            }
        }

        for (int start = 7; start < DAYS; start += 7) {
            int weekProfit = 0;
            for (int day = start; day < start + 7 && day < DAYS; day++) {
                for (int i = 0; i < COMMS; i++) {
                    weekProfit += profit[month][day][i];
                }
            }
            int weekNum = (start / 7) + 1;
            if (weekProfit > bestWeekProfit) {
                bestWeekProfit = weekProfit;
                bestWeek = weekNum;
            }
        }

        return "Week " + bestWeek;
    }
    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}
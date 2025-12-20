import java.io.*;
import java.util.*;
public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    static int[][][] profit = new int[MONTHS][DAYS][COMMS]; //şu ayda şu günde şu commoditynin karı ne
    public static int commodityIndexOf(String commodity) { //comms için case sensitive tarama
        if (commodity == null) return -1;
        commodity = commodity.trim();
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(commodity)) {
                return i;
            }
        }
        return -1;
    }
    public static void loadData() {
        for (int i = 0; i < MONTHS; i++) {
            Scanner reader = null;

            try {
                File file = new File("Data_Files/" + months[i] + ".txt");   //dosya okuma başlar
                reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    String[] parts = line.split(","); //splitting the line to 3
                    if (parts.length != 3) {
                        continue;
                    }
                    String dayStr = parts[0].trim();
                    String commodityStr = parts[1].trim();
                    String profitStr = parts[2].trim(); //boşlukları temizleme

                    if (dayStr.equals("Day")) continue;
                    int dayIndex = Integer.parseInt(dayStr) - 1; //günü integere çevirdim
                    int profitValue = Integer.parseInt(profitStr); //karı double veri tipine çevirme
                    if (dayIndex < 0 || dayIndex >= DAYS) continue;
                    int commodityIndex = commodityIndexOf(commodityStr);
                    if (commodityIndex == -1) continue;
                    profit[i][dayIndex][commodityIndex] = profitValue; //dosyada okunan kar değerini 3 boyutlu profit dizisi içine yerleştirme
                }
            } catch (Exception ex) {
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
        for (int i = 0; i < DAYS; i++) { //ilk commoditynin toplamı hesapladım(negatif çıkmasın diye)
            bestTotal+=profit[month][i][0];
        }
        for (int j = 1; j < COMMS; j++) { //kalan comları karşılaştırma
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
        int totalprofitDay = 0;
        int dayIndex = day - 1;
        if (day < 1 || day > 28) return -99999; //invalid
        if (month < 0 || month > 11) return -99999;
        for (int i = 0; i < COMMS; i++) { //her comms için aylık kar hesaplama
            totalprofitDay += profit[month][dayIndex][i];
        }
        return totalprofitDay;
    }

    public static int commodityProfitInRange(String commodity, int fromThisDay, int toThisDay) {
        int commodityIndex = commodityIndexOf(commodity);
        if (commodityIndex == -1) return -99999;
        if(fromThisDay>toThisDay || fromThisDay<1 || fromThisDay>28 || toThisDay<1 || toThisDay>28)  return -99999;
        int fromThisDayIndex=fromThisDay-1;
        int toThisDayIndex=toThisDay-1;
        int totalrange=0;
        for(int i=0;i<MONTHS;i++){     //belirtilen gün aralığında istenilen commoditynin karını toplama
            for(int j=fromThisDayIndex;j<=toThisDayIndex;j++){
                totalrange+=profit[i][j][commodityIndex];//
            }
        }
        return totalrange;
    }
    public static int bestDayOfMonth(int month){
        if(month<0 || month>11) return -1;
        int bestTotal=0;
        int bestDayIndex=0;
        for(int i=0;i<COMMS;i++){ //1.gün toplamı
            bestTotal+=profit[month][0][i];
        }
        for(int j=1;j<DAYS;j++){ //kalan günlerde kar
            int dailyTotal=0;
            for(int i=0;i<COMMS;i++){
                dailyTotal+=profit[month][j][i];
            }
            if(dailyTotal>bestTotal){
                bestTotal=dailyTotal;
                bestDayIndex=j;
            }
        }
        return bestDayIndex+1; //0. gün olamaz
    }
    public static String bestMonthForCommodity(String commodity){
        int commodityIndex = commodityIndexOf(commodity);
        if (commodityIndex==-1)  return "INVALID_COMMODITY";
        int bestTotal=0;
        int bestMonthIndex=0;
        for(int j=0;j<DAYS;j++){
            bestTotal+=profit[0][j][commodityIndex]; //ocak toplm
        }
        for(int i=1;i<MONTHS;i++){ // diğer aylardaki günlerdeki karlar
            int monthlyTotal=0;
            for(int j=0;j<DAYS;j++){
                monthlyTotal+=profit[i][j][commodityIndex];
            }
            if(monthlyTotal>bestTotal){
                bestTotal=monthlyTotal;
                bestMonthIndex=i;
            }
        }
        return months[bestMonthIndex];
    }
    public static int consecutiveLossDays(String commodity){
        int commodityIndex = commodityIndexOf(commodity);
        if(commodityIndex==-1)  return -1;
        int currentStreak=0;
        int maxStreak=0;
        for(int i=0;i<MONTHS;i++){ //tüm aylardaki günlerdeki kar değeri
            for(int j=0;j<DAYS;j++){
                if(profit[i][j][commodityIndex]<0){ //eğer bu kar değeri negatifese mevcut Streak artar
                    currentStreak++;
                    if(currentStreak>maxStreak)  maxStreak=currentStreak;
                }
                else{  //negatif değer yoksa sıfırda kalır.
                    currentStreak=0;
                }
            }
        }
        return maxStreak;
    }
    public static int daysAboveThreshold(String commodity, int threshold){
        int commodityIndex = commodityIndexOf(commodity);
        if(commodityIndex==-1)  return -1;
        int count=0;
        for(int i=0;i<MONTHS;i++){ //tüm aylardaki günlerdeki seçilen commodity icin kar miktarı eşik değerinden fazla mı bakar
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
            int totalProfitdayone=0;
            int totalProfitdaytwo=0;
            for(int j=0;j<COMMS;j++ ){
                totalProfitdayone+=profit[month][i][j];//ardısık günlerdeki toplam kar
                totalProfitdaytwo+=profit[month][i+1][j];
            }
            diff=totalProfitdaytwo-totalProfitdayone; //kar arası fark
            if(diff<0) { //fark negatifse (-1) ile çarp
                diff=diff*(-1);
            }
            if(diff>maxdiff){
                maxdiff=diff;

            }
        }
        return maxdiff;
    }
    public static String compareTwoCommodities(String c1, String c2){
        int commodityIndex1=commodityIndexOf(c1);
        int commodityIndex2=commodityIndexOf(c2);
        if (commodityIndex1==-1||commodityIndex2==-1)  return "INVALID_COMMODITY";
        int total1=0;
        int total2=0;

        for(int i=0;i<MONTHS;i++){  //tüm aylardaki günlerdeki iki commoditynin karı
            for(int j=0;j<DAYS;j++){
                total1+=profit[i][j][commodityIndex1];
                total2+=profit[i][j][commodityIndex2];

            }
        }

        if(total1==total2) return "Equal";
        if(total1>total2) {
            return  c1 + " is better by " + (total1-total2) ;
        } else{
            return  c2 + " is better by " +  (total2-total1); //her zaman pozitif
        }
    }
    public static String bestWeekOfMonth(int month) {
        if (month < 0 || month > 11) return "INVALID_MONTH";
        int bestWeek = 1;


        int bestWeekProfit = 0;
        for (int day = 0; day < 7; day++) { //1.haftanın commodityler için toplamı
            for (int i = 0; i < COMMS; i++){
                bestWeekProfit += profit[month][day][i];
            }
        }

        for (int start = 7; start < DAYS; start += 7) { //2-4. haftaların commodityler icin kar toplamı
            int weekProfit = 0;
            for (int day = start; day < start + 7 && day < DAYS; day++) { //haftalık kar hesaplama
                for (int i = 0; i < COMMS; i++) {
                    weekProfit += profit[month][day][i];
                }
            }
            int weekNo = (start / 7) + 1;
            if (weekProfit > bestWeekProfit) { //o hafta elde edilen kar,o zamana kadar elde edilen karlardan fazlaysa bestWeekProfit o olur.
                bestWeekProfit = weekProfit;
                bestWeek = weekNo;
            }
        }

        return "Week " + bestWeek;
    }
    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");



    }
}

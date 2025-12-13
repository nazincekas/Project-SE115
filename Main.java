import java.io.*;
import java.util.*;
public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    static int[][][] profitData = new int[MONTHS][DAYS][COMMS];

    public static void loadData() {
        for (int m = 0; m < MONTHS; m++) {
            BufferedReader br = null;
            try {
                String fileName = "Data_Files/" + months[m] + ".txt";
                br = new BufferedReader(new FileReader(fileName));
                String line;

                while ((line = br.readLine()) != null) {

                    String[] parts = line.split(",");
                    if (parts.length != 3) continue;

                    int day = Integer.parseInt(parts[0]) - 1;
                    String commodity = parts[1];
                    int profit = Integer.parseInt(parts[2]);

                    int commIndex = -1;
                    for (int c = 0; c < COMMS; c++) {
                        if (commodities[c].equals(commodity)) {
                            commIndex = c;
                            break;
                        }
                    }
                    if (day >= 0 && day < DAYS && commIndex != -1) {
                        profitData[m][day][commIndex] = profit;
                    }
                }
            } catch (FileNotFoundException e) {
                return;
            } catch (IOException e) {
                return;
            } catch (Exception e) {
                return;
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException e) {
                    return;
                }
            }
        }
    }
}





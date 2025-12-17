
import java.io.*;
import java.util.*;
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
            return "INVALİD MONTH";
        }
    }




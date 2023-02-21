/*
 *  Author: Daven Hill
 *  Course: COP3503
 *  Project #: 2
 *  Title  : Input/Output
 *  Due Date:  10/26/22
 *
 *  Calculates the differences and average of speed data.
 */
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Project2_n01486478 {
    //ArrayLists containing file data to be read in
    public static ArrayList<String> dates = new ArrayList<>();
    public static ArrayList<String> times = new ArrayList<>();
    public static ArrayList<Double> sensor2278 = new ArrayList<>();
    public static ArrayList<Double> sensor3276 = new ArrayList<>();
    public static ArrayList<Double> sensor4689 = new ArrayList<>();
    public static ArrayList<Double> sensor5032 = new ArrayList<>();

    //lists containing the data to be read out to the new file
    public static ArrayList<Double> section1Diff = new ArrayList<>();
    public static ArrayList<Double> section2Diff = new ArrayList<>();
    public static ArrayList<Double> totalAvg = new ArrayList<>();

    /**
     * Reads in a file and separates the values into their appropriate list
     * @param file The file to be read in.
     */
    public static void readIn(File file) throws FileNotFoundException {
        String line;
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            br.readLine();
            while ((line = br.readLine()) != null){

                String[] values = line.split(",");
                dates.add(values[0]);
                times.add(values[1]);
                try {
                    sensor2278.add(Double.parseDouble(values[2]));
                    sensor3276.add(Double.parseDouble(values[3]));
                    sensor4689.add(Double.parseDouble(values[4]));
                    sensor5032.add(Double.parseDouble(values[5]));
                }catch(NumberFormatException e){
                    Scanner in = new Scanner(System.in);
                    System.out.println("Bad Number Data in CSV File");
                    System.out.println("Check CSV file data and try again.");
                    System.out.println("Enter file name & location.");
                    String newFileName = in.nextLine();
                    readIn(new File(newFileName));
                }
            }

        }catch(FileNotFoundException e){
            Scanner in = new Scanner(System.in);
            System.out.println("File does not exist or path was entered incorrectly.");
            System.out.println("Please try again");
            String newFileName = in.nextLine();
            readIn(new File(newFileName));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }//end of method


    // Executes the commands to read in a file, convert the dates, calculating the differences and average
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Project 2 Data Preprocessing\n");
        System.out.println("Enter file name & location.");
        String fileName = in.nextLine();

        File file = new File(fileName);
        System.out.println("Reading in Data from the file " + fileName);
        readIn(file);
        System.out.println("Converting Dates from MM/DD/YYYY to YYYY/MM/DD");
        convertDates(dates);
        System.out.println("Calculating Speed Difference");
        section1Diff.addAll(sectionDifference(sensor2278, sensor3276));
        section2Diff.addAll(sectionDifference(sensor4689, sensor5032));
        System.out.println("Calculating Speed Average");
        totalAvg.addAll(getTotalAvg(sensor2278,sensor3276,sensor4689,sensor5032));
        System.out.println("Writing data to file Speed_Data_Difference.csv ");
        writeOut(fileName);
        System.out.println("Done! Exiting Program");
        System.exit(0);

        in.close();

    } //end of main

    /**
     * Converts the dates of the data to a yyyy/mm/dd format.
     * @param list The list to be operated on.
     */
    public static void convertDates(ArrayList<String> list) throws IOException{
        Scanner in = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");

        try {
            for (int i = 0; i < list.size(); i++) {
                String currentDate = list.get(i);
                Date date = sdf.parse(currentDate);
                currentDate = sdf1.format(date);
                list.set(i, currentDate);
            }

        }catch(ParseException e){
            System.out.println("Bad Date Data in CSV File.");
            System.out.println("Check CSV file data and try again.");
            String newFileName = in.nextLine();
            readIn(new File(newFileName));
        }
    }//end of method
    /**
     * Calculates the difference between corresponding elements in an arraylist.
     * @param y The first list containing the data.
     * @param x The second list containing the data.
     * @return The ArrayList containing the calculated differences.
     */
    public static ArrayList<Double> sectionDifference(ArrayList<Double> y, ArrayList<Double> x){
        ArrayList<Double> res = new ArrayList<>();
        for (int i = 0; i < y.size()-1; i++){
            res.add(y.get(i) - x.get(i));
        }
        return res;
    }//end of method

    /**
     * Calculates the averages between corresponding elements in an arraylist.
     * @param a The first list containing the data.
     * @param b The second list containing the data.
     * @param c the third list containing the data.
     * @param d the fourth list containing the data.
     * @return The ArrayList containing the calculated averages.
     */

    public static ArrayList<Double> getTotalAvg(ArrayList<Double> a, ArrayList<Double> b, ArrayList<Double> c, ArrayList<Double> d){
        ArrayList<Double> res = new ArrayList<>();
        double average;
        for (int i = 0; i < a.size()-1; i++){
            average = (a.get(i) + b.get(i) + c.get(i) + d.get(i)) / 4;
            res.add(average);
        }
        return res;
    }//end of method

    /**
     * Writes out the data from the ArrayLists into a new file
     * @param fileName The file to be written out to.
     */
    public static void writeOut(String fileName)  {
        String[] fileEnd;
        if (fileName.contains(".")){
            fileEnd = fileName.split("[.]");
            fileName = fileEnd[0];
            fileName = (fileName + "_Difference.csv");
        }
        try{
            FileWriter writer = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(writer);
            printWriter.println("Dates,Times,Sensor2278,Sensor3276,Sensor4689,Sensor5032,Section1Diff,Section2Diff,TotalAvg");
            for (int i = 0; i < dates.size()-1; i++){
                printWriter.printf("%s,", dates.get(i));
                printWriter.printf("%s,", times.get(i));
                printWriter.printf("%.6f,", sensor2278.get(i));
                printWriter.printf("%.6f,", sensor3276.get(i));
                printWriter.printf("%.6f,", sensor4689.get(i));
                printWriter.printf("%.6f,", sensor5032.get(i));
                printWriter.printf("%.6f,", section1Diff.get(i));
                printWriter.printf("%.6f,", section2Diff.get(i));
                printWriter.printf("%.6f,", totalAvg.get(i));
                printWriter.println("\n");
            }


        }catch(IOException e){
            System.out.println("Could not create file.");
        }
    }

}//end of class



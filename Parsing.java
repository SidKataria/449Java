/**
 * CPSC 449
 * Winter 2019
 * Parsing class for Java Scheduling Assignment
 *
 * @author Team Kwerty
 * @version Completely parses the file with error checking putting the required values in different structures
 */

import java.io.*;
import java.util.Arrays;
import java.lang.String;
import java.util.*; 

public class Parsing
{
   /**
     * Constructor for objects of class Parsing
     */

    public Parsing() {
        for (int i = 0; i<fmAssignment.length; i++) {
            fpAssignment[i] = null;
            System.out.println(fpAssignment[i]);
            for (int j = 0; j<fmAssignment.length; i++) {
                fmAssignment[i][j] = null;
                tntAssignment[i][j] = -1;
                System.out.println(fmAssignment[i]);
            }
        }
    }

    //Final Arrays for checking input
    private static final String[] Machines = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final String[] Tasks = {"A", "B", "C", "D", "E", "F", "G", "H"};
   
    //Keyword Strings
    public static String inputName, inputFPA, inputFM, inputTNT, inputMP, inputTNP;

    //Basic counter
    public static int counter, only1;

    //Array for Forced Partial Assignments
    //fpAssignment[i] = j; task j to machine i (*mach 1* = fpAssignment[0]) 
    public static String[] fpAssignment = new String[8];

    //Array for Forbidden Machine
    public static String[][] fmAssignment = new String[8][8];
    
    //Array for Too Near Tasks
    public static int[][] tntAssignment = new int[8][8];
    
    //Array for Machine Penalties
    public static int[][] mpAssignment = new int[8][8];
    
    //Array for Too-near Penalties 
    public static int[][] tnpAssignment = new int [8][8];

    //Checking for key words
    public static boolean name, fpa, fm, tnt, mp, tnp;
    
    //Parse to outer handles 
    public static Stack<String> parseFPA = new Stack<String>();
    public static Stack<String> parseFM  = new Stack<String>();
    public static Stack<String> parseTNT = new Stack<String>();
    public static Stack<String> parseMP  = new Stack<String>();
    public static Stack<String> parseTNP = new Stack<String>();
        //,parseFM, parseTNT,parseMP,parseTNP; 
    
    //Method to validate input as a correct Machine value
    public static boolean correctMach(String mach) {
        boolean isit = false;
        for (String i : Machines) {
            if (mach.equals(i))
                isit = true;
        }   return isit;
    }

    //Method to validate inut as a correct Task value
    public static boolean correctTask(String task) {
        boolean isit = false;
        for (String i : Tasks) {
            if (task.equals(i))
                isit = true;
        }   return isit;
    }

    public static int whichTask(String task) {
        switch (task) {
            case "A":
                return 1;
            case "B":
                return 2;
            case "C":
                return 3;
            case "D":
                return 4;
            case "E":
                return 5;
            case "F":
                return 6;
            case "G":
                return 7;
            case "H":
                return 8;
            default:
                return 0;
        }
    }
    
    //Method for parsing (maybe make it to return boolen for ful file parsed successfully
    //Parsed so far: Name, FPA, FM, TNT, MP, TNP 
    public static void parsing(String filename) throws Exception{
        FileReader fr = null;
        BufferedReader br;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null) {
                //Got to first line of file
                line = line.trim();
                if (line.isEmpty()) {
                    //Whitespace line
                    continue;
                }
                String foundWord = line.toLowerCase();
                //To check which line currently on: System.out.println("keyword found is: " + foundWord);
                switch (foundWord) {
                case "name:":
                    name = false;
                    inputName = br.readLine();
                    //Getting name of input file
                    inputName.trim();
                    if (inputName.isEmpty() != true) {
                        name = true;
                        System.out.println("Input file name: " + inputName);
                    }   else
                            System.out.println("No name for input file");
                    System.out.print('\n');        
                    break;
                case "forced partial assignment:":
                    fpa = false;
                    counter = 0;
                    only1 = 0; // for checking no similar valid entry in any pair
                    while ((inputFPA = br.readLine()) != null) {
                        inputFPA.trim();
                        if (inputFPA.isEmpty())
                            break;
                        String[] givenIn = inputFPA.split("[(,)]");                    
                        String givenMach = givenIn[1].trim();
                        String givenTask = givenIn[2].trim();                                                
                        if (!correctMach(givenMach)|| !correctTask(givenTask)) {
                            System.out.println("invalid machine/task");
                            //Quit system
                            return;
                        }
                        //STILL HAVE TO CHECK FOR MULTIPLE CORRECT MACHS/TASKS
                        if (counter != 8) {
                            if (fpAssignment[Integer.parseInt(givenMach) - 1] == null) {
                                fpAssignment[Integer.parseInt(givenMach) - 1] = givenTask;
                                System.out.println("Given mach: " + givenMach + " has forced partial assignment task: " + givenTask); 
                                parseFPA.push(givenMach);
                                parseFPA.push(givenTask);
                                fpa = true;
                            }   else {
                                //Quit System
                                fpa = false;
                                System.out.println("Forced Partial Assignment error");
                            }
                        }
                        else {
                            //Quit System
                            fpa = false;
                            System.out.println("Forced Partial Assignment error");
                        }
                        counter++;
                    }
                    //To check what the final Array of Forced Partial Assignments: System.out.println(Arrays.toString(fpAssignment));
                    System.out.print('\n');    
                    break;
                case "forbidden machine:":
                    fm = false;
                    counter = 0;
                    while ((inputFM = br.readLine()) != null) {
                        inputFM.trim();
                        if (inputFM.isEmpty())
                            break;
                        String[] givenIn = inputFM.split("[(,)]");                        
                        String givenMach = givenIn[1];
                        String givenTask = givenIn[2];                        
                        if (!correctMach(givenMach) || !correctTask(givenTask)) {
                            System.out.println("invalid machine/task");
                            break;
                            //Quit system
                        }
                        // Max reading is 64
                        if (counter < 65) {
                            for (int i = 0; i <8; i++) {
                                if (fmAssignment[Integer.parseInt(givenMach) -1][i] == null) {
                                    fmAssignment[Integer.parseInt(givenMach) -1][i] = givenTask;
                                    fm = true;
                                    System.out.println("Assigning task: " + givenTask + " to machine: " + givenMach +" is invalid");
                                    counter++;
                                    parseFM.push(givenMach);
                                    parseFM.push(givenTask);
                                    break;
                                }            
                                else if (fmAssignment[Integer.parseInt(givenMach) -1][i] == givenTask) {
                                    fm = false;
                                    System.out.println("FM Pair already exisits");
                                    break;
                                }
                                else {
                                System.out.println("Forbidden Machine Assignment Error");
                                fm = false;
                                break;
                                }
                            }
                        }   else {
                                fm = false;
                                System.out.println("Forbidden Machine Assignment Error");
                                break;
                        }
                    }
                    System.out.print('\n');
                    break;
                case "too-near tasks:":
                    tnt = false;
                    counter = 0;
                    while ((inputTNT = br.readLine()) != null) {
                        inputTNT.trim();
                        if (inputTNT.isEmpty())
                            break;
                        String[] givenIn = inputTNT.split("[(,)]");
                        String givenT1 = givenIn[1];
                        String givenT2 = givenIn[2];
                        if (!correctTask(givenT1) || !correctTask(givenT2)) {
                            System.out.println("invalid machine/task");
                            //Quit System
                            break;
                        }
                        if (counter < 65) {
                            int task1 = whichTask(givenT1);
                            int task2 = whichTask(givenT1);
                            if (tntAssignment[task1-1][task2-1] == 0) {
                                tntAssignment[task1-1][task2-1] = task2;
                                tnt = true;
                                counter++;
                                System.out.println("task 1: " + givenT1 + " is a too-near task pair with task 2: " + givenT2);
                                parseTNT.push(givenT1);
                                parseTNT.push(givenT2);
                                //break;
                            }
                            else if (tntAssignment[task1-1][task2-1] == task2) {
                                tnt = false;
                                System.out.println("Too-near task pair assignment already exists");
                                break;
                            }
                            else {
                                tnt = false;
                                System.out.println("Too-near tasks assignment error");
                                break;
                            }
                        }   else {
                                tnt = false;
                                System.out.println("Too-near tasks assignment error");
                                break;
                        }
                    }
                    System.out.print('\n');
                    break;
                case "machine penalties:":
                    mp = false;
                    counter = 0;
                    while ((inputMP = br.readLine()) != null) {
                        inputMP.trim();
                        if (inputMP.isEmpty())
                            break;
                        String[] givenIn = inputMP.split(" ");
                        if (givenIn.length != 8) {
                            System.out.println("machine penalties error");
                            mp = false;
                            break;
                        }
                        for (int i = 0; i < givenIn.length; i++) {
                            int machP;
                            try {
                                machP = Integer.parseInt(givenIn[i]);
                                mp = true;
                            }   
                            catch (NumberFormatException e) {
                                System.out.println("Invalid Machine Penalty");
                                mp = false;
                                break;
                            }   if (machP < 0) {
                                    System.out.println("Invalid Machine Penalty");
                                    mp = false;
                                    break;
                                }
                                mpAssignment[counter][i] = machP;
                                System.out.println("Machine Penalty Row: " + (counter + 1) + " is: " + machP);
                                parseMP.push(String.valueOf(machP));
                        }   counter++;
                        if (counter == 8)
                            break;
                    }
                    System.out.println('\n');
                    break;
                case "too-near penalties":
                    tnp = false;
                    counter = 0;
                    while ((inputTNP = br.readLine()) != null) {
                        inputTNP.trim();
                        if (inputTNP.isEmpty())
                            break;
                        String[] givenIn = inputTNP.split("[(,)]");
                        String givenT1 = givenIn[1];
                        String givenT2 = givenIn[2];
                        if (!correctTask(givenT1) || !correctTask(givenT2)) {
                            System.out.println("invalid task");
                            tnp = false;
                            break;
                        }
                        int pVal;
                        try {
                            pVal = Integer.parseInt(givenIn[3]);
                        }   catch (NumberFormatException e) {
                                System.out.println("invalid penalty");
                                tnp = false;
                                break;
                        }
                        if (pVal < 0) {
                            System.out.println("invalid penalty");
                            tnp = false;
                            break;
                        }
                        tnpAssignment[(whichTask(givenT1))-1][(whichTask(givenT2))-1] = pVal;
                        tnp = true;
                        System.out.println("Task: " + givenT1 + " and Task: " + givenT2 + " has p value: " + pVal);
                        counter++;
                        parseTNP.push(givenT1);
                        parseTNP.push(givenT2);
                        parseTNP.push(String.valueOf(pVal));
                        if (counter == 65)
                            break;
                    }
                    System.out.println('\n');
                }
            }
        }   catch (FileNotFoundException e) {
            System.out.println("Error 404: File not found");
        }   finally {
                if (fr != null)
                    fr.close();
        }
        //Printing Stack
        System.out.println("Parse Objects: \n");
        System.out.println("Forced Partial Assignment:" + parseFPA);
        System.out.println("Forbidden Machine:"+parseFM);
        System.out.println("Too near Tasks:" + parseTNT);
        System.out.println("Machine Penalties:" + parseMP);
        System.out.println("Too near Penalties:" + parseTNP);
    }

    public static void main(String[] args) throws Exception {        
         Scanner reader = new Scanner(System.in);
         String askFile;
         System.out.println("Please input the file name.");
         askFile = reader.nextLine();
         while (askFile.isEmpty()) {
               System.out.println("Please input the file name.");
                   askFile = reader.nextLine();
         }
         parsing(askFile);
        //String inputFile = args[0];
        //parsing(inputFile);
    }
}
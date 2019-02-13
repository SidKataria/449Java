/** 
 * CPSC 449
 * Assignment 1, Java
 * Parsing class for Java Scheduling Assignment
 * @Sid's Parser
 * @author Team Kwerty
 * @version Final
 */
import java.io.*;

import java.util.Arrays;
import java.lang.String;
import java.util.*; 

public class Parsing    {
    //Final Arrays for checking input
    private static final String[] Machines = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final String[] Tasks = {"A", "B", "C", "D", "E", "F", "G", "H"};

    //Keyword Strings
    public static String inputName, inputFPA, inputFM, inputTNT, inputMP, inputTNP;

    //Basic counter (only1 not used properly yet)
    public static int counter, only1;

    /**
    Array for Forced Partial Assignments
    fpAssignment[i] = j; task j to machine i (*mach 1* = fpAssignment[0]) */
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
    
    //Boolean for Parsed file (Check this, if true, then 
    //parsing successful, else check output for what error to be printed to outputFile
    public static boolean parsed;
    //Output string to file
    public static String output;
    
    //Parse to outer handles     
    public static LinkedList<String> parseFPA = new LinkedList<String>();
    public static LinkedList<String> parseFPAsplit = new LinkedList<String>();
    public static LinkedList<String> parseFM = new LinkedList<String>();
    public static LinkedList<String> parseTNT = new LinkedList<String>();
    public static LinkedList<String>parseMP = new LinkedList<String>();
    public static LinkedList<String>parseTNP =new LinkedList<String>();
    

    /**
     * Method for actual parsing of the file
     */
    public static void parsing(String filename) throws Exception{
        parsed = false;
        FileReader fr = null;
        BufferedReader br;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String line;
            //Reading file until EOF
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
                    if (inputName.isEmpty() != true) 
                        name = true;
                    else {
                        output = "Error while parsing file";
                        return;
                    }
                    break;
                case "forced partial assignment:":
                    fpa = false;
                    counter = 0;
                    only1 = 0; // for checking no similar valid entry in any pair
                    while ((inputFPA = br.readLine()) != null) {
                        inputFPA.trim();
                        if (inputFPA.isEmpty())
                            break;
                        //Splitting the Founnd pairs within *(_,_)*
                        String[] givenIn = inputFPA.split("[(,)]");                  
                        String givenMach = givenIn[1].trim();   //Machine
                        String givenTask = givenIn[2].trim();   //Task       
                              
                        //Checking if the given pairs are from our given Machines and Tasks
                        if (!correctMach(givenMach)|| !correctTask(givenTask)) {
                            output = "invalid machine/task";
                            //Quit system
                            return;
                        }
                        
                        //STILL HAVE TO CHECK FOR MULTIPLE CORRECT MACHS/TASKS?
                        
                        if (counter != 8)   //8 is the maximun number of pairs, otherwise invalid input 
                        {
                            if (fpAssignment[Integer.parseInt(givenMach) - 1] == null) {
                                //Adding to array and stacks
                                fpAssignment[Integer.parseInt(givenMach) - 1] = givenTask;
                                parseFPA.add(givenMach + givenTask);
                                parseFPAsplit.add(givenMach);
                                parseFPAsplit.add(givenTask);
                                //System.out.println("Given mach: " + givenMach + " has forced partial assignment task: " + givenTask);
                                fpa = true;
                            }
                            else {
                                //Quit System
                                fpa = false;
                                output = "partial assignment error";
                                return;
                            }
                        }
                        else {
                            //Quit System
                            fpa = false;
                            output = "Error while parsing input file";
                            return;
                        }
                        counter++;
                    }
                    break;
                case "forbidden machine:":
                    fm = false;
                    counter = 0;
                    while ((inputFM = br.readLine()) != null) {
                        inputFM.trim();
                        if (inputFM.isEmpty())
                            break;
                        //Splitting the given pairs as done before
                        String[] givenIn = inputFM.split("[(,)]");
                        String givenMach = givenIn[1];
                        String givenTask = givenIn[2];
                        //Checking if the given pairs match our Machines and Tasks
                        if (!correctMach(givenMach) || !correctTask(givenTask)) {
                            output = "invalid machine/task";
                            return;
                        }
                        if (counter < 65) {
                            for (int i = 0; i <8; i++) {
                                if (fmAssignment[Integer.parseInt(givenMach) -1][i] == null) {
                                    //Adding the values to the stack the arrays
                                    fmAssignment[Integer.parseInt(givenMach) -1][i] = givenTask;
                                    parseFM.add(givenMach);
                                    parseFM.add(givenTask);
                                    fm = true;
                                    //System.out.println("Assigning task: " + givenTask + " to machine: " + givenMach +" is invalid");
                                    counter++;
                                    break;
                                }            
                                else if (fmAssignment[Integer.parseInt(givenMach) -1][i] == givenTask) {
                                    fm = false;
                                break;
                                //System.out.println("FM Pair already exisits");
                            }
                            else{
                                output = "invalid machine/task";
                                fm = false;
                                return;
                                }
                            }
                        }   else {
                                fm = false;
                                output = "invalid machine/task";
                                return;
                        }
                    }
                    break;
                case "too-near tasks:":  
                    tnt = false;
                    counter = 0;
                    while ((inputTNT = br.readLine()) != null) {
                        inputTNT.trim();
                        if (inputTNT.isEmpty())
                            break;
                        //Splitting the pairs of tasks in *(_,_)*
                        String[] givenIn = inputTNT.split("[(,)]");
                        String givenT1 = givenIn[1];
                        String givenT2 = givenIn[2];
                        
                        //Checking if the given tasks are valid
                        if (!correctTask(givenT1) || !correctTask(givenT2)) {
                            output = "invalid machine/task";
                            return;
                        }
                        //Maximun number of pairs will be 64
                        if (counter < 65) {
                            int task1 = whichTask(givenT1);
                            int task2 = whichTask(givenT1);
                            if (tntAssignment[task1-1][task2-1] == 0) {
                                //Adding the tasks to the arrays and stacks 
                                tntAssignment[task1-1][task2-1] = task2;
                                parseTNT.add(givenT1);
                                parseTNT.add(givenT2);
                                tnt = true;
                                counter++;
                                //System.out.println("task 1: " + givenT1 + " is a too-near task pair with task 2: " + givenT2);
                            }
                            else if (tntAssignment[task1-1][task2-1] == task2) {
                                tnt = false;
                                //What to do when task already exists?
                            }
                            else {
                                tnt = false;
                                //Return?
                            }
                        }   else {
                                tnt = false;
                                output = "Error while parsing input file";
                                return;
                        }
                    }
                    break;
                case "machine penalties:":
                    mp = false;
                    counter = 0;
                    while ((inputMP = br.readLine()) != null) {
                        inputMP.trim();
                        if (inputMP.isEmpty())
                            break;
                        //Splitting the penalty values with spaces
                        String[] givenIn = inputMP.split(" ");
                        
                        //Checking if each line has exact 8 values, as definef                        
                        if (givenIn.length != 8) {
                            output = "machine penalty error";
                            mp = false;
                            return;
                        }
                        //Making sure the penalty is an interger above and including 0
                        for (int i = 0; i < givenIn.length; i++) {
                            int machP;
                            try {
                                machP = Integer.parseInt(givenIn[i]);
                                mp = true;
                            }   
                            catch (NumberFormatException e) {
                                //Penalty not an integer
                                mp = false;
                                output = "invalid penalty";
                                return;
                            }   if (machP < 0) {
                                    //Penalty value less than 0
                                    output = "invalid penalty";
                                    mp = false;
                                    return;
                                }
                            //Penalty checks out, adding to array and stack
                            mpAssignment[counter][i] = machP;
                            parseMP.add(givenIn[i]);
                        }   counter++;
                        //More than 9 penalties in 1 line? Not possible
                        if (counter > 8) {
                            output = "Error while parsing input file";
                            return;
                        }
                    }
                    break;
                case "too-near penalties":
                case "too-near penalities": //Typo in file, as in assignment
                    tnp = false;
                    counter = 0;
                    while ((inputTNP = br.readLine()) != null) {
                        inputTNP.trim();
                        if (inputTNP.isEmpty())
                            break;
                        //Splitting the given penalties in *(_,_)*
                        String[] givenIn = inputTNP.split("[(,)]");
                        String givenT1 = givenIn[1];
                        String givenT2 = givenIn[2]; 
                        //Checking if the given tasks are valin
                        if (!correctTask(givenT1) || !correctTask(givenT2)) {
                            output = "invalid task";
                            tnp = false;
                            return;
                        }
                        //Integer pValue
                        int pVal;
                        try {   //try not really required as already checking task before
                            pVal = Integer.parseInt(givenIn[3]);
                        }   catch (NumberFormatException e) {
                                //Given penalty not an integer
                                output = "invalid penalty";
                                tnp = false;
                                return;
                        }
                        if (pVal < 0) {
                            //pValue is negative
                            output = "invalid penalty";
                            tnp = false;
                            return;
                        }
                        //pValue checks out
                        tnpAssignment[(whichTask(givenT1))-1][(whichTask(givenT2))-1] = pVal;
                        parseTNP.add(givenT1);
                        parseTNP.add(givenT2);
                        parseTNP.add(Integer.toString(pVal));
                        tnp = true;
                        //System.out.println("Task: " + givenT1 + " and Task: " + givenT2 + " has p value: " + pVal);
                        counter++;
                        //Maximum 64 penalties
                        if (counter > 64) {
                            output = "Error parsing input file";
                            return;
                        }
                    }
                    //System.out.println("Im out");
                }
            }
            //File parsed successfully
            parsed = true;
            //System.out.println("parsed successfully");
        }catch (FileNotFoundException e) {
            //System.out.println("Error 404: File not found");
            output = "Error parsing input file";
            return;
        }finally {
            if (fr != null) {
                fr.close();
            }
        }
    }
    
    /**
     * Constructor for objects of class Parsing
     */
    public Parsing() {
        for (int i = 0; i<fmAssignment.length; i++) {
            fpAssignment[i] = null;
            //System.out.println(fpAssignment[i]);
            for (int j = 0; j<fmAssignment.length; j++) {
                fmAssignment[i][j] = null;
                tntAssignment[i][j] = -1;
            }
        }
    }
    
    //Checking if parsed
    public void failParsedOutput(boolean isit, String output) {
        if (isit == false) {
            try {
                File file = new File("myoutput.txt:");
                if(!file.exists())
                    file.createNewFile();
                PrintWriter pw = new PrintWriter(file);
                pw.print(output);
                pw.close();
            }
            catch (IOException e) {
                System.out.println("Error opening the file myoutput.txt.");
                System.exit(0);
            }
        }
    }   
    
    /**
     * Method to validate input as a correct Machine value
     */
    public static boolean correctMach(String mach) {
        boolean isit = false;
        for (String i : Machines) {
            if (mach.equals(i))
                isit = true;
        }   return isit;
    }

    /**
     * Method to validate inut as a correct Task value
     */
    public static boolean correctTask(String task) {
        boolean isit = false;
        for (String i : Tasks) {
            if (task.equals(i))
                isit = true;
        }   return isit;
    }
    
    /**
     * Method for returing Task array value
     */
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
    
    public static void main(String[] args) throws Exception {        
        Scanner reader = new Scanner(System.in);
        String askFile;
        System.out.println("Please input the file name.");
        askFile = reader.nextLine();
       
        parsing(askFile);
 
        System.out.println("Parse Objects: \n");
        System.out.println("Forced Partial Assignment:" + parseFPA);
        System.out.println("Forbidden Machine:"+parseFM);
        System.out.println("Too near Tasks:" + parseTNT);
        System.out.println("Machine Penalties:" + parseMP);
        System.out.println("Too near Penalties:" + parseTNP);
        
        //If parsing is failed, this will be the file output with exiting the system
        if (parsed == false) {
            try {
                File file = new File("myoutput.txt");
                if(!file.exists()) {
                    file.createNewFile();
                }
                PrintWriter pw = new PrintWriter(file);
                pw.print(output);
                pw.close();
                System.exit(0);
            }
            catch (IOException e) {
                System.out.println("Error opening the file myoutput.txt.");
                System.exit(0);
            }
        }
    }   
}
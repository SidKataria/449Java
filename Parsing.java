/**
 * Parsing class for Java Scheduling Assignment
 *
 * @author Team Kwerty
 * @version Can only get name of file, Forced Partial Assignments, and completely read through
 */
import java.io.*;
import java.util.Arrays;

public class Parsing
{

    /**
     * Constructor for objects of class Parsing
     */
    public Parsing()
    {
        for (int i = 0; i<8; i++) {
            fpAssignment[i] = null;
            System.out.println(fpAssignment[i]);
        }
    }
    //Final Arrays for checking input
    private static final String[] Machines = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final String[] Tasks = {"A", "B", "C", "D", "E", "F", "G", "H"};
    
    //Keyword Strings
    public static String inputName, inputFPA, inputFM;
    
    //Basic counter
    public static int counter, only1;
    //Array for Forced Partial Assignments
    //fpAssignment[i] = j; task j to machine i (*mach 1* = fpAssignment[0]) 
    public static String[] fpAssignment = new String[8];
    
    //Checking for key words
    public static boolean name, fpa, fm;
    
    //Method to validate input as a correct Machine value
    public static boolean correctMach(String mach) {
        boolean isit = false;
        for (String i : Machines) {
            if (mach.equals(i))
                isit = true;
        }   return isit;
    }
    
    //Method to validate inut as a coorect Task value
    public static boolean correctTask(String task) {
        boolean isit = false;
        for (String i : Tasks) {
            if (task.equals(i))
                isit = true;
        }   return isit;
    }
    
    //Method for parsing (maybe make it to return boolen for ful file parsed successfully
    //Parsed so far: Name, FPA
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
                    }
                    else
                        System.out.println("No name for input file");
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
                                System.out.println("Given mach: " + givenMach + " Given task: " + givenTask);
                                fpa = true;
                            }
                            else {
                                //Quit System
                                System.out.println("Forced Partial Assignment error");
                            }
                        }
                        else {
                            //Quit System
                            System.out.println("Forced Partial Assignment error");
                        }
                        counter++;
                    }
                    //To check what the final Array of Forced Partial Assignments: System.out.println(Arrays.toString(fpAssignment));
                    break;
                case "forbibben machine:":
                    fm = false;
                    while ((inputFM = br.readLine()) != null) {
                        inputFM.trim();
                        if (inputFM.isEmpty())
                            break;
                        String[] givenIn = inputFM.split("[(,)]");
                        String givenMach = givenIn[1];
                        String givenTask = givenIn[2];
                        if (!correctMach(givenMach) || !correctTask(givenTask)
                        ) {
                            System.out.println("invalid machine/task");
                            //Quit system
                            return;
                        }
                    }
                }
            }
        }catch (FileNotFoundException e) {
            System.out.println("Error 404: File not found");
        }finally {
            if (fr != null) {
                fr.close();
            }
        }
    }
   
    
    public static void main(String[] args) throws Exception {
         String inputFile = args[0];
         parsing(inputFile);
    }

}

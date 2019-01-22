/**
 * Parsing class for Java Scheduling Assignment
 *
 * @author Team Kwerty
 * @version Can only get name of file and completely read through
 */
import java.io.*;

public class Parsing
{

    /**
     * Constructor for objects of class Parsing
     */
    public Parsing()
    {
        
    }
    //Final Arrays for checking input
    private final String[] Machines = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private final String[] Tasks = {"A", "B", "C", "D", "E", "F", "G", "H"};
    
    //File name
    public static String inputName;
    
    //Checking for key words
    public static boolean name;
    
    //Method for parsing
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
                    line = br.readLine();
                    inputName = line.trim();
                    //Getting name of input file
                    if (inputName != null) {
                        name = true;
                        System.out.println("Input file name: " + inputName);
                    }
                    else //Else not reaching, input name can still take null value
                        System.out.println("No name for input File");
                    break;
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

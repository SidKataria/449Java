//Java Parser for Assignment 1
import java.io.*;

public class Parser {

	public static String inputName;
	public static boolean name; 
	public static void parsing(String filename) throws Exception{
		FileReader fr = null;
		BufferedReader br;
		System.out.println("1");
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty())
					continue;
				String foundWord = line.toLowerCase();
				switch (foundWord) {
				case "Name:":
					name = true;
					line = br.readLine();
					System.out.println("4");
					if (line != null) {
						inputName = line.trim();
						System.out.println("Input file name: " + inputName);
					}
					else 
						System.out.println("Non name for input File");
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

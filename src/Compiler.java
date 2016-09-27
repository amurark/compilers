import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * The main file with the main method. This takes in input parameter, runs the scanner on it and outputs a file.
 * @author Ankit
 *
 */
public class Compiler {
	public static void main(String[] args) {
		System.out.println("OUTPUT:");
		
		/**
		 * If the filename is provided as a parameter, throw error and exit
		 */
		if(args.length == 0) {
			System.out.println("No filename provided, please try again..");
			System.exit(0);
		} else {
			String filename = args[0];
			String param2 = args[1];
			/**
			 * Initialize the scanner object with the filename.
			 */
			Scanner scanner = new Scanner("./"+filename);
			/**
			 * Create a data structure for the reserved words.
			 */
			scanner.buildReservedWordsMap();
			
			/**
			 * The scanner function which tokenizes the input file and stores the tokens in a queue.
			 */
			int error = scanner.tokenize();
			
			if(error == 0) {
				System.out.println("Exiting....");
				System.exit(0);
			}
			
			if(param2.equals("1")) {
				Parser parser = new Parser(scanner);
				if(parser.initialize()) {
					System.out.println("Successfully Parsed");
				} else {
					System.out.println("Failed to parse");
				}
				
			} else if(param2.equals("2")) {
				/**
				 * Split the input file from the period(.) symbol to create the modified name for the output file.
				 */
				String inputFilename = filename.split("\\.")[0];
				/**
				 * Create a buffered writer to write to the output file.
				 */
				try (Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("./"+inputFilename+"_gen.c"), "utf-8"))) {
					/**
					 * Iterate through all the tokens in the queue.
					 */
					while(scanner.HasMoreTokens()) { 
						/**
						 * Get the next token to write to the output file.
						 */
				        Token t = scanner.GetNextToken();
				        //System.out.println(t.getTokenType()+" "+t.getName() +" hello");
				        /**
				         * If the next token is an identifier and its not 'main', then modify the token and prefix it with cs512 before writing to output file
				         */
				        if (t.getTokenType() == Tokentype.IDENTIFIER && !t.getName().equals("main")) {
				        	// print token with cs512 attached
				        	System.out.print("cs512"+t.getName());
				        	writer.write("cs512"+t.getName());
				        }
				        /**
				         * Else directly write to the output file.
				         */
				        else {
				        	System.out.print(t.getName());
				        	writer.write(t.getName());
				        } 
				    }	//Catch any input/output exceptions.
				} catch (IOException ex) {
					  System.out.println("Error in writing to file "+ex);
				} 
			}
		}
	}
}

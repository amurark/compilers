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
				//System.out.println("Exiting....");
				System.exit(0);
			}
			/**
			 * Hardcoded value to run the Parser Program.
			 */
			String param2 = "2";
			/**
			 * Run Parser if param2 = 1, otherwise just run scanner.
			 */
			if(param2.equals("1")) {
				Parser parser = new Parser(scanner);
				/**
				 * Initialize parser.
				 */
				parser.initialize();
				
			} else if(param2.equals("2")) {
				
				Parser parser = new Parser(scanner);
				/**
				 * Initialize parser.
				 */
				parser.initialize();
				
				/**
				 * Initialize the scanner object with the filename.
				 */
				Scanner sc = new Scanner("./"+filename);
				/**
				 * Create a data structure for the reserved words.
				 */
				sc.buildReservedWordsMap();
				
				/**
				 * The scanner function which tokenizes the input file and stores the tokens in a queue.
				 */
				int err = sc.tokenize();
				
				if(err == 0) {
					//System.out.println("Exiting....");
					System.exit(0);
				}
				
				
				
				
				
				/**
				 * Split the input file from the period(.) symbol to create the modified name for the output file.
				 */
				String inputFilename = filename.split("\\.")[0];
				/**
				 * Create a buffered writer to write to the output file.
				 */
				try (Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("./"+inputFilename+"_gen.c"), "utf-8"))) {
					
					IRGenerator ir = new IRGenerator(sc, writer);
					/**
					 * Initialize parser.
					 */
					ir.initialize();
				} catch (IOException ex) {
					  System.out.println("Error in writing to file "+ex);
				} 
				
				
				
				
				
				
				
				
			} else if(param2.equals("3")) {
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

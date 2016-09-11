import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Compiler {
	public static void main(String[] args) {
		System.out.println("Compilers Project 1: Lets go!!\n");
		
		if(args.length == 0) {
			System.out.println("No filename provided, please try again..");
			System.exit(0);
		} else {
			String filename = args[0];
			Scanner scanner = new Scanner("./"+filename);  // Initialize the scanner. 
			scanner.buildReservedWordsMap();
			//scanner.checkReservedWordsMap();
			/**
			 * The grand function.
			 */
			scanner.tokenize();
			
			String inputFilename = filename.split("\\.")[0];
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(inputFilename+"_gen.c"), "utf-8"))) {
				while(scanner.HasMoreTokens()) { 
			        Token t = scanner.GetNextToken();  
			        if (t.getTokenType() == Tokentype.IDENTIFIER && !t.getName().equals("main")) {
			        	// print token with cs512 attached
			        	System.out.print("cs512"+t.getName());
			        	writer.write("cs512"+t.getName());
			        }
			        else {
			        	System.out.print(t.getName());
			        	writer.write(t.getName());
			        } 
			    }	
			} catch (IOException ex) {
				  // report
			} 
			
		}
			
		
	}
}

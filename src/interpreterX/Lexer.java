package interpreterX;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lexer {
	
	// Constant Members
    public static final String EOL ="eol";
    private final static String MATH_CHARS = "(?<=[-+*/()=])|(?=[-+*/()=])";
    
    // Constructors
    public static List<String> Lexer(InputStreamReader streamReader) {
        List<String> words =new ArrayList<>();
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        Scanner scanner;
        
        try {
            while((line = reader.readLine())!=null) {
                scanner = new Scanner(line);
                
                while(scanner.hasNext()) {
                    String token = scanner.next();
                    
                    if (!token.startsWith("\"")) {
                        String[] splittedByMathChars = token.split(MATH_CHARS);

                        for (int i = 0; i < splittedByMathChars.length; i++) {
                            String word = splittedByMathChars[i];
                            
                            if (word.equals("<")||word.equals("=")||word.equals(">")||word.equals("!")){
                                
                            	if(i<splittedByMathChars.length-1 && splittedByMathChars[i+1].equals("=")){
                                    words.add(word+"=");
                                    i++;
                                    continue;
                                }
                            }
                            
                            words.add(word);
                        }
                    } else {
                        words.add(token);
                    }
                }
                words.add(EOL);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return words;
    }

    public static List<String> Lexer(String script) {
        return Lexer(new InputStreamReader(new ByteArrayInputStream(script.getBytes())));
    }

    public static List<String> Lexer(String[] script) {
        List<String> list =new ArrayList<>();
        
        for (String line : script) {
            list.addAll(Lexer.Lexer(line));
        }
        
        return list;
    }
}


/**
 * Implements a Lexical Analyzer that can identify alphanumeric lexemes.
 */

package lexicalanalyzer;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static java.lang.Character.isSpace;
import static java.lang.System.out;

/**
 * @author Dennis Khounviengxay
 */
public class LexicalAnalyzer
{        
    // Global Variables
    static int  charGroup;    // The class/group a char belongs to
    static char nextCharact;  // The char being analyzed/input
    static int  lexLength;    // Length of a given lexeme
    static int  nextToken;    // The token of nextChar
    
    static int data;          // Refers to the value of a char when reading the text file
    static int index = 0;     // Refers to the current index of a string builder
    static int maxIndex;      // Refers to the length of the whole text file (in terms of chars)
    static char aChar;        // Used to input chars into textSB : "WHOLE string builder"
    static String testString; // Used to test the tempSB : "TEMP string builder"
    static StringBuilder textSB = new StringBuilder(); // Holds the entire text file : "WHOLE string builder"
    static StringBuilder tempSB = new StringBuilder(); // Holds each lexeme to print : "TEMP string builder"
    
    
    // char group : Each char belongs to one of these categories
    final static int LETTER = 0;   // char is an alpha character
    final static int DIGIT = 1;    // char is a numeric character
    final static int UNKNOWN = 99; // char is a special/undefined character
    
    final static int EOF = -1;     // Applied to various places (usually to signify the end of something)
    
    // Token values : Each token represents what is contained in a lexeme
    final static int INT_LIT = 10; // int string literals
    final static int IDENT = 11;   // Identifier (words) string literals
    final static int ASSIGN = 20;  // Assignment operator '='
    final static int ADD = 21;     // Addition operator '+'
    final static int SUB = 22;     // Subtraction operator '-'
    final static int MULT = 23;    // Multiplation operator '*'
    final static int DIV = 24;     // Division operator '/'
    final static int L_PAR = 25;   // Left Parenthesis '('
    final static int R_PAR = 26;   // Right Parenthesis ')'
    
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception
    {
        // Stuff needed to read the text file
        InputStream streamFile = new FileInputStream("LexTest.txt");
        Reader readMe = new InputStreamReader(streamFile);
        
        // Reads first character of the text file
        data = readMe.read();
        
        // Reads the chars from the text file and inputs them into the "WHOLE string builder"
        while( (int) data != -1)
        {
            aChar = (char) data;
            data = readMe.read();
            textSB.append(aChar);                
        }

        // Refers to the length of the "WHOLE string builder" (in other words, the length of the text file)
        maxIndex = textSB.length();

        // Gets the class/group of the first char, which is then used in lex() for the first loop
        getChar();
        
        
        // loops until the end of the "WHOLE string builder" (text file) is reached
        do {
            lex();
        }while(nextToken != EOF);
            
        
        // Closes the text file
        readMe.close();
        
        out.println("\nEntire input from text file:\n" + textSB + "\n");
        
    }   // End main()
    
    
    /**
     * Looks up operators and parentheses, then returns the token.
     * @param ch The character being analyzed.
     * @return Token of the operator/()
     */
    public static int lookup(char ch)
    {
        switch(ch)
        {
            case '(':
                addChar();
                nextToken = L_PAR;
                break;
                
            case ')':
                addChar();
                nextToken = R_PAR;
                break;
                
            case '+':
                addChar();
                nextToken = ADD;
                break;
                
            case '-':
                addChar();
                nextToken = SUB;
                break;
                
            case '*':
                addChar();
                nextToken = MULT;
                break;
                
            case '/':
                addChar();
                nextToken = DIV;
                break;
                
            default:
                addChar();
                nextToken = EOF;
                break;
                
        }   // End switch-case
        
        return nextToken;
        
    }   // End lookup()
    
    
    /**
     * Adds the next character (nextChar) to the temporary string builder.
     * lexLen refers to the length of the lexeme.
     */
    public static void addChar()
    {
        if(lexLength <= 98)
        {
            tempSB.append(nextCharact);
            lexLength++;
        }
        
        else
            out.println("* ERROR - lexeme is too long \n");
        
    }   // End addChar()
    
    
    /**
     * Gets the next character of the input and determine its character class.
     */
    public static void getChar()
    {
        // if the current index is < the length of textSB, it will continue
        if(index < maxIndex)
        {
            nextCharact = textSB.charAt(index);
            
            if(isLetter(nextCharact))
                charGroup = LETTER;    // 0
            
            else if(isDigit(nextCharact))
                charGroup = DIGIT;     // 1
            
            else
                charGroup = UNKNOWN;   // 99
            
            // refers to next char in textSB
            index++;
        }
        
        else
            charGroup = EOF;           // -1
        
    }   // End getChar()
    
    
    /**
     * Calls getChar() until it reaches a non-whitespace character.
     */
    public static void getNonBlank()
    {
        while(isSpace(nextCharact))
            getChar();
        
    }   // End getNonBlank()
    
    
    /**
     * Determines each lexeme, internally looping for literals.
     * @return nextToken The token value of the char
     */
    static public int lex()
    {
        // resets lexeme size
        lexLength = 0;
        // moves to next non-space
        getNonBlank();
        
        switch(charGroup)
        {
            // Determines an identifier lexeme
            case LETTER:
                addChar();
                getChar();
                while(charGroup == LETTER || charGroup == DIGIT)
                {
                    addChar();
                    getChar();
                }
                nextToken = IDENT;
                break;
                
            // Determines a numerical lexeme
            case DIGIT:
                addChar();
                getChar();
                while(charGroup == DIGIT)
                {
                    addChar();
                    getChar();
                }
                nextToken = INT_LIT;
                break;
                
            // Determines paranthesis or operator lexemes   
            case UNKNOWN:
                lookup(nextCharact);
                getChar();
                break;
                
            // Determines when we've reached EOF
                case EOF:
                    nextToken = EOF;
                    tempSB.delete(0, tempSB.length());
                    tempSB.append("EOF");
                    break;
            
        }   // End switch-case
        
        // Saves my stringbuilder as a string
        testString = tempSB.toString();
        
        // catches things that aren't allowed, like: '=','$','#','@'
        if(nextToken == EOF && !"EOF".equals(testString))
        {
            out.println("* ERROR - Lexeme cannot be identified");
            return EOF;
        }
        
        // Prints out the token and lexeme
        out.printf("Current TOKEN: %d, and LEXEME: %s\n", nextToken, tempSB);
        // Clears out the temporary string builder to contain the next lexeme
        tempSB.delete(0, tempSB.length());
        return nextToken;
        
    }   // End lex()
    
    
}       // End class LexicalAnalyzer

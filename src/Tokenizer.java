package speek;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    private final String source;     
    private int    pos   = 0;         
    private int    line  = 1;        

    public Tokenizer(String source) {
        this.source = source.replace("\r\n", "\n").replace("\r", "\n");
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < source.length()) {
            char c = source.charAt(pos);

            if (c == ' ' || c == '\t') {
                pos++;
                continue;
            }

            if (c == '\n') {
                if (!tokens.isEmpty() &&
                    tokens.get(tokens.size() - 1).getType() != TokenType.NEWLINE) {
                    tokens.add(new Token(TokenType.NEWLINE, "\\n", line));
                }
                line++;
                pos++;
                continue;
            }

            if (c == '#') {
                skipToEndOfLine();
                continue;
            }

            if (c == '"') {
                tokens.add(readString());
                continue;
            }

            if (Character.isDigit(c)) {
                tokens.add(readNumber());
                continue;
            }

            if (c == '+') { tokens.add(new Token(TokenType.PLUS,  "+", line)); pos++; continue; }
            if (c == '-') { tokens.add(new Token(TokenType.MINUS, "-", line)); pos++; continue; }
            if (c == '*') { tokens.add(new Token(TokenType.STAR,  "*", line)); pos++; continue; }
            if (c == '/') { tokens.add(new Token(TokenType.SLASH, "/", line)); pos++; continue; }

            if (Character.isLetter(c) || c == '_') {
                tokens.add(readWord());
                continue;
            }

            throw new RuntimeException(
                "Line " + line + ": Unexpected character '" + c + "'");
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

   
    private Token readString() {
        pos++; 
        int startLine = line;
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() && source.charAt(pos) != '"') {
            if (source.charAt(pos) == '\n') {
                throw new RuntimeException("Line " + startLine +
                    ": String not closed — missing closing quote '\"'");
            }
            sb.append(source.charAt(pos));
            pos++;
        }

        if (pos >= source.length()) {
            throw new RuntimeException("Line " + startLine +
                ": String not closed — reached end of file");
        }

        pos++; 
        return new Token(TokenType.STRING, sb.toString(), startLine);
    }

    private Token readNumber() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() &&
               (Character.isDigit(source.charAt(pos)) || source.charAt(pos) == '.')) {
            sb.append(source.charAt(pos));
            pos++;
        }

        return new Token(TokenType.NUMBER, sb.toString(), startLine);
    }

    private Token readWord() {
        int startLine = line;
        String word = readSingleWord();

        switch (word) {
            case "let":     return new Token(TokenType.LET,    "let",    startLine);
            case "be":      return new Token(TokenType.BE,     "be",     startLine);
            case "say":     return new Token(TokenType.SAY,    "say",    startLine);
            case "if":      return new Token(TokenType.IF,     "if",     startLine);
            case "then":    return new Token(TokenType.THEN,   "then",   startLine);
            case "repeat":  return new Token(TokenType.REPEAT, "repeat", startLine);
            case "times":   return new Token(TokenType.TIMES,  "times",  startLine);
            case "else":    return new Token(TokenType.ELSE,   "else",   startLine);

            case "is": {
                skipSpaces();
                String next1 = peekWord();

                if (next1.equals("greater")) {
                    readSingleWord(); 
                    skipSpaces();
                    String next2 = peekWord();
                    if (next2.equals("than")) {
                        readSingleWord(); 
                        return new Token(TokenType.IS_GREATER_THAN, "is greater than", startLine);
                    }
                    throw new RuntimeException("Line " + startLine +
                        ": Expected 'than' after 'greater', got '" + next2 + "'");
                }

                if (next1.equals("less")) {
                    readSingleWord(); 
                    skipSpaces();
                    String next2 = peekWord();
                    if (next2.equals("than")) {
                        readSingleWord(); 
                        return new Token(TokenType.IS_LESS_THAN, "is less than", startLine);
                    }
                    throw new RuntimeException("Line " + startLine +
                        ": Expected 'than' after 'less', got '" + next2 + "'");
                }

                if (next1.equals("equal")) {
                    readSingleWord(); 
                    skipSpaces();
                    String next2 = peekWord();
                    if (next2.equals("to")) {
                        readSingleWord(); 
                        return new Token(TokenType.IS_EQUAL_TO, "is equal to", startLine);
                    }
                    throw new RuntimeException("Line " + startLine +
                        ": Expected 'to' after 'equal', got '" + next2 + "'");
                }

                return new Token(TokenType.IDENTIFIER, "is", startLine);
            }

            default:
                return new Token(TokenType.IDENTIFIER, word, startLine);
        }
    }

    private String readSingleWord() {
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() &&
               (Character.isLetterOrDigit(source.charAt(pos)) || source.charAt(pos) == '_')) {
            sb.append(source.charAt(pos));
            pos++;
        }
        return sb.toString();
    }

    private String peekWord() {
        int saved = pos;
        String w = readSingleWord();
        pos = saved; 
        return w;
    }

    private void skipSpaces() {
        while (pos < source.length() &&
               (source.charAt(pos) == ' ' || source.charAt(pos) == '\t')) {
            pos++;
        }
    }

    private void skipToEndOfLine() {
        while (pos < source.length() && source.charAt(pos) != '\n') {
            pos++;
        }
    }
}

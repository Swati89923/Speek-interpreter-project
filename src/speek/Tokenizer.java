package speek;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    private final String source;
    private int pos = 0;
    private int line = 1;

    public Tokenizer(String source) {
        this.source = source.replace("\r\n", "\n").replace("\r", "\n");
    }

    public List<Token<?>> tokenize() {
        List<Token<?>> tokens = new ArrayList<>();

        while (pos < source.length()) {
            char c = source.charAt(pos);

            if (c == ' ' || c == '\t') {
                pos++;
                continue;
            }

            if (c == '\n') {
                tokens.add(new Token<>(TokenType.NEWLINE, "\\n", line));
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

            if (c == '+') { tokens.add(new Token<>(TokenType.PLUS, "+", line)); pos++; continue; }
            if (c == '-') { tokens.add(new Token<>(TokenType.MINUS, "-", line)); pos++; continue; }
            if (c == '*') { tokens.add(new Token<>(TokenType.STAR, "*", line)); pos++; continue; }
            if (c == '/') { tokens.add(new Token<>(TokenType.SLASH, "/", line)); pos++; continue; }

            if (Character.isLetter(c) || c == '_') {
                tokens.add(readWord());
                continue;
            }

            throw new RuntimeException("Line " + line + ": Unexpected character '" + c + "'");
        }

        tokens.add(new Token<>(TokenType.EOF, "", line));
        return tokens;
    }

    private Token<String> readString() {
        pos++;
        int startLine = line;
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() && source.charAt(pos) != '"') {
            if (source.charAt(pos) == '\n') {
                throw new RuntimeException("Line " + startLine + ": String not closed");
            }
            sb.append(source.charAt(pos));
            pos++;
        }

        pos++;
        return new Token<>(TokenType.STRING, sb.toString(), startLine);
    }

    private Token<Double> readNumber() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() &&
                (Character.isDigit(source.charAt(pos)) || source.charAt(pos) == '.')) {
            sb.append(source.charAt(pos));
            pos++;
        }

        return new Token<>(TokenType.NUMBER, Double.parseDouble(sb.toString()), startLine);
    }

    private Token<String> readWord() {
        int startLine = line;
        String word = readSingleWord();

        switch (word) {
            case "let": return new Token<>(TokenType.LET, word, startLine);
            case "be": return new Token<>(TokenType.BE, word, startLine);
            case "say": return new Token<>(TokenType.SAY, word, startLine);
            case "if": return new Token<>(TokenType.IF, word, startLine);
            case "then": return new Token<>(TokenType.THEN, word, startLine);
            case "repeat": return new Token<>(TokenType.REPEAT, word, startLine);
            case "times": return new Token<>(TokenType.TIMES, word, startLine);
            case "else": return new Token<>(TokenType.ELSE, word, startLine);

            default:
                return new Token<>(TokenType.IDENTIFIER, word, startLine);
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

    private void skipToEndOfLine() {
        while (pos < source.length() && source.charAt(pos) != '\n') {
            pos++;
        }
    }
}

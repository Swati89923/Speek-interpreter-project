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

    private Token<?> readWord() {
    int startLine = line;
    String word = readSingleWord();

    // HANDLE MULTI-WORD OPERATORS
    if (word.equals("is")) {

        int savedPos = pos;

        skipSpaces();
        String next1 = peekWord();

        skipSpaces();
        String next2 = peekSecondWord(next1);

        String combined = "is " + next1 + " " + next2;

        if (combined.equals("is less than") ||
            combined.equals("is greater than") ||
            combined.equals("is equal to")) {

            // consume properly
            skipSpaces();
            readSingleWord(); // less/greater/equal
            skipSpaces();
            readSingleWord(); // than/to

            switch (combined) {
                case "is less than":
                    return new Token<>(TokenType.IS_LESS_THAN, combined, startLine);
                case "is greater than":
                    return new Token<>(TokenType.IS_GREATER_THAN, combined, startLine);
                case "is equal to":
                    return new Token<>(TokenType.IS_EQUAL_TO, combined, startLine);
            }
        }

        // rollback if not matched
        pos = savedPos;
    }

    // NORMAL KEYWORDS
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
    private void skipSpaces() {
    while (pos < source.length() && source.charAt(pos) == ' ') {
        pos++;
    }
}

private String peekWord() {
    int temp = pos;
    StringBuilder sb = new StringBuilder();

    while (temp < source.length() &&
           Character.isLetter(source.charAt(temp))) {
        sb.append(source.charAt(temp));
        temp++;
    }
    return sb.toString();
}

private String peekSecondWord(String firstWord) {
    int temp = pos;

    // skip first word
    while (temp < source.length() &&
           Character.isLetter(source.charAt(temp))) {
        temp++;
    }

    // skip spaces
    while (temp < source.length() && source.charAt(temp) == ' ') {
        temp++;
    }

    StringBuilder sb = new StringBuilder();
    while (temp < source.length() &&
           Character.isLetter(source.charAt(temp))) {
        sb.append(source.charAt(temp));
        temp++;
    }

    return sb.toString();
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

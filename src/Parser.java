package speek;

import java.util.*;

public class Parser {

    private List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Instruction> parse() {
        List<Instruction> list = new ArrayList<>();

        while (!isAtEnd()) {
            skipNewlines();
            if (isAtEnd()) break;
            list.add(parseInstruction());
        }
        return list;
    }

    private Instruction parseInstruction() {
        Token t = peek();

        if (t.getType() == TokenType.LET) return null;
        if (t.getType() == TokenType.SAY) return null;
        if (t.getType() == TokenType.IF) return null;
        if (t.getType() == TokenType.REPEAT) return null;

        throw new RuntimeException("Unknown instruction at line " + t.getLine());
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private void skipNewlines() {
        while (!isAtEnd() && peek().getType() == TokenType.NEWLINE) {
            pos++;
        }
    }
}
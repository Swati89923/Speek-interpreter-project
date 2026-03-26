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
// ================= EXPRESSION PARSING =================

private Expression parseExpression() {

    Expression left = parseTerm();

    while (!isAtEnd() &&
          (peek().getType() == TokenType.PLUS ||
           peek().getType() == TokenType.MINUS)) {

        String op = advance().getValue();
        Expression right = parseTerm();

        left = new BinaryOpNode(left, op, right);
    }

    return left;
}

private Expression parseTerm() {

    Expression left = parsePrimary();

    while (!isAtEnd() &&
          (peek().getType() == TokenType.STAR ||
           peek().getType() == TokenType.SLASH)) {

        String op = advance().getValue();
        Expression right = parsePrimary();

        left = new BinaryOpNode(left, op, right);
    }

    return left;
}

private Expression parsePrimary() {

    Token t = peek();

    if (t.getType() == TokenType.NUMBER) {
        advance();
        return new NumberNode(Double.parseDouble(t.getValue()));
    }

    if (t.getType() == TokenType.STRING) {
        advance();
        return new StringNode(t.getValue());
    }

    if (t.getType() == TokenType.IDENTIFIER) {
        advance();
        return new VariableNode(t.getValue());
    }

    throw new RuntimeException(
        "Line " + t.getLine() + " : Expected number / string / variable"
    );
}

private Token advance() {
    return tokens.get(pos++);
}
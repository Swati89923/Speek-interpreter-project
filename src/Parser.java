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

    if (t.getType() == TokenType.LET) return parseAssign();
    if (t.getType() == TokenType.SAY) return parsePrint();
    if (t.getType() == TokenType.IF) return parseIf();
    if (t.getType() == TokenType.REPEAT) return parseRepeat();

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

private Instruction parseAssign() {

    advance(); // LET

    Token name = peek();
    advance();

    expect(TokenType.BE, "Expected 'be'"); // BE

    Expression expr = parseExpression();

    return new AssignInstruction(name.getValue(), expr);
}

private Instruction parsePrint() {

    advance(); // SAY

    Expression expr = parseExpression();

    return new PrintInstruction(expr);
}

private Instruction parseIf() {

    advance(); // IF

    Expression condition = parseExpression();

    expect(TokenType.THEN, "Expected 'then'");// THEN

    List<Instruction> body = new ArrayList<>();

    skipNewlines();

   body.add(parseInstruction());


    return new IfInstruction(condition, body);
}


private Instruction parseRepeat() {

    advance(); // REPEAT

    Token num = peek();
    advance();

    int count = (int) Double.parseDouble(num.getValue());

    expect(TokenType.TIMES, "Expected 'times'"); // TIMES

    List<Instruction> body = new ArrayList<>();

    skipNewlines();

    body.add(parseInstruction());

    return new RepeatInstruction(count, body);
}
private void expect(TokenType type, String msg) {
    if (peek().getType() != type) {
        throw new RuntimeException(msg + " at line " + peek().getLine());
    }
    advance();
}
}

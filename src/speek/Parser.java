package speek;

import java.util.*;

public class Parser {

    private List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // ================= ENTRY =================

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

    // ================= BASIC HELPERS =================

    private Token peek() {
        return tokens.get(pos);
    }

    private Token advance() {
        return tokens.get(pos++);
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private void skipNewlines() {
        while (!isAtEnd() && peek().getType() == TokenType.NEWLINE) {
            pos++;
        }
    }

    private void expect(TokenType type, String msg) {
        if (peek().getType() != type) {
            throw new RuntimeException(msg + " at line " + peek().getLine());
        }
        advance();
    }

    // ================= EXPRESSIONS =================

    // lowest level → comparison
    private Expression parseComparison() {

        Expression left = parseExpression();

        while (!isAtEnd() &&
                (peek().getType() == TokenType.IS_GREATER_THAN ||
                 peek().getType() == TokenType.IS_LESS_THAN ||
                 peek().getType() == TokenType.IS_EQUAL_TO)) {

            String op = advance().getValue();
            Expression right = parseExpression();

            left = new BinaryOpNode(left, op, right);
        }

        return left;
    }

    // + -
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

    // * /
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

    // ================= INSTRUCTIONS =================

    private Instruction parseAssign() {

        advance(); // LET

        Token name = peek();
        if (name.getType() != TokenType.IDENTIFIER) {
            throw new RuntimeException("Expected variable name at line " + name.getLine());
        }
        advance();

        expect(TokenType.BE, "Expected 'be'");

        Expression expr = parseComparison();

        return new AssignInstruction(name.getValue(), expr);
    }

    private Instruction parsePrint() {

        advance(); // SAY

        Expression expr = parseComparison();

        return new PrintInstruction(expr);
    }

    private Instruction parseIf() {

        advance(); // IF

        Expression condition = parseComparison();

        expect(TokenType.THEN, "Expected 'then'");

        List<Instruction> thenBody = new ArrayList<>();
        List<Instruction> elseBody = new ArrayList<>();

        skipNewlines();

        // THEN block
        while (!isAtEnd() &&
               peek().getType() != TokenType.ELSE &&
               peek().getType() != TokenType.NEWLINE) {

            thenBody.add(parseInstruction());
            skipNewlines();
        }

        // ELSE block (optional)
        if (!isAtEnd() && peek().getType() == TokenType.ELSE) {
            advance(); // ELSE
            skipNewlines();

            while (!isAtEnd() &&
                   peek().getType() != TokenType.NEWLINE) {

                elseBody.add(parseInstruction());
                skipNewlines();
            }
        }

        return new IfInstruction(condition, thenBody, elseBody);
    }

    private Instruction parseRepeat() {

        advance(); // REPEAT

        Token num = peek();
        if (num.getType() != TokenType.NUMBER) {
            throw new RuntimeException("Expected number after repeat at line " + num.getLine());
        }
        advance();

        int count = (int) Double.parseDouble(num.getValue());

        expect(TokenType.TIMES, "Expected 'times'");

        List<Instruction> body = new ArrayList<>();

        skipNewlines();

        while (!isAtEnd() &&
               peek().getType() != TokenType.NEWLINE) {

            body.add(parseInstruction());
            skipNewlines();
        }

        return new RepeatInstruction(count, body);
    }
}

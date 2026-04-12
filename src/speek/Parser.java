package speek;

import java.util.*;

public class Parser {

    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = List.copyOf(tokens); // immutable defensive copy
    }

    // ================= ENTRY =================

    public List<Instruction> parse() {

        List<Instruction> list = new ArrayList<>();

        while (!isAtEnd()) {
            skipNewlines();
            if (isAtEnd()) break;

            list.add(parseInstruction());
        }

        return List.copyOf(list); // immutable result
    }

    private Instruction parseInstruction() {

        Token t = peek();

        switch (t.getType()) {
            case LET:
                return parseAssign();
            case SAY:
                return parsePrint();
            case IF:
                return parseIf();
            case REPEAT:
                return parseRepeat();
            default:
                throw new RuntimeException(
                        "Unknown instruction at line " + t.getLine());
        }
    }

    // ================= BASIC HELPERS =================

    private Token peek() {
        if (pos >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(pos);
    }

    private Token advance() {
        return tokens.get(pos++);
    }

    private boolean isAtEnd() {
        return pos >= tokens.size() || peek().getType() == TokenType.EOF;
    }

    private void skipNewlines() {
        while (!isAtEnd() && peek().getType() == TokenType.NEWLINE) {
            pos++;
        }
    }

    private Token expect(TokenType type, String msg) {

        Token t = peek();

        if (t.getType() != type) {
            throw new RuntimeException(
                    msg + " but found " + t.getType() +
                            " at line " + t.getLine());
        }

        return advance();
    }

    // GENERIC TOKEN MATCHER
    private boolean match(TokenType... types) {

        for (TokenType type : types) {

            if (!isAtEnd() && peek().getType() == type) {
                advance();
                return true;
            }
        }

        return false;
    }

    // ================= EXPRESSIONS =================

    // comparison
    private Expression parseComparison() {

        Expression left = parseExpression();

        while (match(TokenType.IS_GREATER_THAN,
                     TokenType.IS_LESS_THAN,
                     TokenType.IS_EQUAL_TO)) {

            String op = tokens.get(pos - 1).getValue();
            Expression right = parseExpression();

            left = new BinaryOpNode(left, op, right);
        }

        return left;
    }

    // + -
    private Expression parseExpression() {

        Expression left = parseTerm();

        while (match(TokenType.PLUS, TokenType.MINUS)) {

            String op = tokens.get(pos - 1).getValue();
            Expression right = parseTerm();

            left = new BinaryOpNode(left, op, right);
        }

        return left;
    }

    // * /
    private Expression parseTerm() {

        Expression left = parsePrimary();

        while (match(TokenType.STAR, TokenType.SLASH)) {

            String op = tokens.get(pos - 1).getValue();
            Expression right = parsePrimary();

            left = new BinaryOpNode(left, op, right);
        }

        return left;
    }

    private Expression parsePrimary() {

        Token t = peek();

        switch (t.getType()) {

            case NUMBER:
                advance();
                return new NumberNode(Double.parseDouble(t.getValue()));

            case STRING:
                advance();
                return new StringNode(t.getValue());

            case IDENTIFIER:
                advance();
                return new VariableNode(t.getValue());

            default:
                throw new RuntimeException(
                        "Line " + t.getLine()
                                + " : Expected number / string / variable"
                );
        }
    }

    // ================= INSTRUCTIONS =================

    private Instruction parseAssign() {

        advance(); // LET

        Token name = expect(TokenType.IDENTIFIER,
                "Expected variable name");

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

        while (!isAtEnd() &&
                peek().getType() != TokenType.ELSE &&
                peek().getType() != TokenType.NEWLINE) {

            thenBody.add(parseInstruction());
            skipNewlines();
        }

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

        Token num = expect(TokenType.NUMBER,
                "Expected number after repeat");

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

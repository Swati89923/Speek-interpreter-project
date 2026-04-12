package speek;

public class Token<T> {

    private final TokenType type;
    private final T value;       
    private final int line;

    public Token(TokenType type, T value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public TokenType getType() { return type; }
    public T getValue()        { return value; }
    public int getLine()       { return line; }

    @Override
    public String toString() {
        return "[" + type + " \"" + value + "\" L" + line + "]";
    }
}

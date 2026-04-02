package speek;

public class Test {
    public static void main(String[] args) {

        String code = """
        let x be 10
        let y be 20
        say x + y

        if x is less than y then
        say "x is smaller"

        repeat 3 times
        say "hello"
        """;

        Interpreter interpreter = new Interpreter();
        interpreter.run(code);
    }
}
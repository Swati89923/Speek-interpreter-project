package speek;

import java.util.List;

public class Interpreter {

    public void run(String sourceCode) {

        Tokenizer tokenizer = new Tokenizer(sourceCode);
        List<Token<?>> tokens = tokenizer.tokenize();

        Parser parser = new Parser(tokens);
        List<Instruction> instructions = parser.parse();

        Environment<Object> env = new Environment<>();

        for (Instruction inst : instructions) {
            inst.execute(env);
        }
    }
}
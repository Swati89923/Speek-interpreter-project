package speek;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private Map<String, Object> variables;

    public Environment() {
        variables = new HashMap<>();
    }

    // store value
    public void set(String name, Object value) {
        variables.put(name, value);
    }

    // get value
    public Object get(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Variable not defined: " + name);
        }
        return variables.get(name);
    }
}
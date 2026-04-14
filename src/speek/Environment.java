package speek;

import java.util.HashMap;
import java.util.Map;

public class Environment<T> {

    private Map<String, T> variables = new HashMap<>();

    public void set(String name, T value) {
        variables.put(name, value);
    }

    public T get(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Variable not defined: " + name);
        }
        return variables.get(name);
    }
}
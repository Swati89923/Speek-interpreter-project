package speek;

public class BinaryOpNode implements Expression<Object> {

    private Expression<?> left;
    private String operator;
    private Expression<?> right;

    public BinaryOpNode(Expression<?> left, String operator, Expression<?> right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object evaluate(Environment<?> env) {

        Object leftVal = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        if (leftVal instanceof Double && rightVal instanceof Double) {

            double l = (Double) leftVal;
            double r = (Double) rightVal;

            switch (operator) {
                case "+": return l + r;
                case "-": return l - r;
                case "*": return l * r;
                case "/": return l / r;
                case "is greater than": return l > r;
                case "is less than": return l < r;
                case "is equal to": return l == r;
            }
        }

        throw new RuntimeException("Invalid operation: " + operator);
    }
}

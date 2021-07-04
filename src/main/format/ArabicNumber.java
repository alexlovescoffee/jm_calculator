package main.format;

public class ArabicNumber extends Number {
    private String value;

    public ArabicNumber(String value) {
        this.value = value;
    }

    public String getValue() {return value;}
    public void setValue(String value) {this.value = value;}

    private int getIntValue(String str) {
        return Integer.parseInt(str);
    }

    @Override
    public String calculate(Number number, String sign) {
        int left = getIntValue(value), right = getIntValue(number.getValue());
        switch (sign) {
            case "+": return left + right + "";
            case "-": return left - right + "";
            case "*": return left * right + "";
            default: return left / right + "";
        }
    }
}

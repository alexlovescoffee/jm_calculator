package main;

import main.exception.CalculatorBadFormatException;
import main.format.ArabicNumber;
import main.format.Number;
import main.format.RomeNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    private static final Pattern pattern = Pattern.compile(
             "(^(10|1|[2-9])" +
              "\\s([+\\-*/])\\s" +
              "(10|1|[2-9])$)" +
             "|" +
             "(^(I{1,3}|I?V|VI{1,3}|I?X)" +
              "\\s([+\\-*/])\\s" +
              "(I{1,3}|I?V|VI{1,3}|I?X)$)");

    private Number a;
    private Number b;
    private String sign;

    public void readConsoleInput() throws IOException, CalculatorBadFormatException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input:");
        Matcher matcher = pattern.matcher(br.readLine());

        if (matcher.find()) {
            if (matcher.group(1) != null) {
                a = new ArabicNumber(matcher.group(2));
                b = new ArabicNumber(matcher.group(4));
                sign = matcher.group(3);
            } else {
                a = new RomeNumber(matcher.group(6));
                b = new RomeNumber(matcher.group(8));
                sign = matcher.group(7);
            }
        } else
            throw new CalculatorBadFormatException("Введенные данные не могут быть обработаны калькулятором!");
    }

    public void printResult() throws Exception {
        System.out.println("Output:");
        System.out.println(a.calculate(b, sign));
    }
}
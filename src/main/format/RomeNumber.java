package main.format;

import main.exception.CalculatorBadFormatException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*Римские числа счита*/
public class RomeNumber extends Number {
    private final Map<String, Integer> keyMap = new HashMap<>();
    {
        keyMap.put("I", 1);
        keyMap.put("V", 5);
        keyMap.put("X", 10);
        keyMap.put("L", 50);
        keyMap.put("C", 100);
        keyMap.put("D", 500);
        keyMap.put("M", 1000);
    }

    private final Map<Integer, String> numberMap = new HashMap<>();
    {
        numberMap.put(1, "I");
        numberMap.put(5, "V");
        numberMap.put(10, "X");
        numberMap.put(50, "L");
        numberMap.put(100, "C");
        numberMap.put(500, "D");
        numberMap.put(1000, "M");
    }

    private String value;

    public RomeNumber(String value) {
        this.value = value;
    }

    public String getValue() {return value;}
    public void setValue(String value) {this.value = value;}

    @Override
    public String calculate(Number number, String sign) throws CalculatorBadFormatException {
        ArabicNumber thisNumArab = new ArabicNumber(decrypt().toString());
        ArabicNumber otherNumArab = new ArabicNumber(((RomeNumber) number).decrypt().toString());
        int result = Integer.parseInt(thisNumArab.calculate(otherNumArab, sign));

        if (result < -3999 || result > 3999)
            throw new CalculatorBadFormatException("Запрос не может быть обработан! Превышен лимит вычисления(от 1 до 3999) для римских чисел");
        else if (result < 0)
            return "-" + encrypt(result);
        else if (result == 0)
            throw new CalculatorBadFormatException("Запрос не может быть обработан! В римских цифрах отсутствует ноль!");
        return encrypt(result);
    }

    private Integer decrypt() {
        int result = 0;
        String tempArr = value, curVal;

        Pattern pattern1 = Pattern.compile("I{3}|X{3}|C{3}|M{3}");
        Pattern pattern2 = Pattern.compile("I{2}|X{2}|C{2}|M{2}");
        Pattern pattern3 = Pattern.compile("IV|XL|CD|IX|XC|CM");
        Pattern pattern4 = Pattern.compile("[IVXLCDM]");

        Matcher matcher1 = pattern1.matcher(tempArr);
        while (matcher1.find()) {
            curVal = matcher1.group();
            result += keyMap.get(curVal.substring(0,1)) * 3;
        }
        tempArr = tempArr.replaceAll("I{3}|X{3}|C{3}|M{3}", "");

        Matcher matcher2 = pattern2.matcher(tempArr);
        while (matcher2.find()) {
            curVal = matcher2.group();
            result += keyMap.get(curVal.substring(0,1)) * 2;
        }
        tempArr = tempArr.replaceAll("I{2}|X{2}|C{2}|M{2}", "");

        Matcher matcher3 = pattern3.matcher(tempArr);
        while (matcher3.find()) {
            curVal = matcher3.group();
            result += keyMap.get(curVal.substring(1,2)) - keyMap.get(curVal.substring(0,1));
        }
        tempArr = tempArr.replaceAll("IV|XL|CD|IX|XC|CM", "");

        Matcher matcher4 = pattern4.matcher(tempArr);
        while (matcher4.find()) {
            curVal = matcher4.group();
            result += keyMap.get(curVal.substring(0,1));
        }

        return result;
    }

    private String encrypt(int num) {
        StringBuilder result = new StringBuilder();
        List<StringBuilder> list = new ArrayList<>();

        int count = 1, tmp, absNum = Math.abs(num);

        while (absNum > 0) {
            tmp = absNum % 10;
            StringBuilder currentSign = new StringBuilder();

            if (tmp > 0 && tmp < 4) {
                for (int i = 0; i < tmp; i++)
                    currentSign.append(numberMap.get(count));
            } else if (tmp == 4)
                currentSign.append(numberMap.get(count)).append(numberMap.get(5 * count));
            else if (tmp == 5)
                currentSign.append(numberMap.get(5 * count));
            else if (tmp > 5 && tmp < 9) {
                currentSign.append(numberMap.get(5 * count));
                for (int i = 0; i < tmp - 5; i++)
                    currentSign.append(numberMap.get(count));
            } else if (tmp == 9)
                currentSign.append(numberMap.get(count)).append(numberMap.get(count * 10));

            if (!currentSign.toString().equals(""))
                list.add(currentSign);

            absNum /= 10;
            count *= 10;
        }

        for (int i = list.size() - 1; i >= 0 ; i--)
            result.append(list.get(i));

        return result.toString();
    }

}

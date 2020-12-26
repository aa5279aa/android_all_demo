package com.common.util;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author lxl 负责字符串处理
 */
public final class StringUtil {

    public static final String EMPTY = "";

    public static boolean isValueHaveEmpty(Map<String, String> map) {
        if (map == null) {
            return false;
        }
        for (String str : map.keySet()) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    public static SpannableString makeStyledString(Context context, String content, int style) {
        SpannableString string = new SpannableString(content);
        string.setSpan(new TextAppearanceSpan(context, style), 0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return string;
    }

    public static int toInt(String str) {
        try {
            int i = Integer.parseInt(str);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String toHexFormat(String s) {
        int len = s.length();
        if (len % 2 == 1) {
            s = "0" + s;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i = i + 2) {
            sb.append(" ").append(s.substring(i, i + 2));
        }
        return sb.toString();
    }

    public static String[] toHexNoBlank(String s) {
        int len = s.length();
        if (len % 2 == 1) {
            s = "0" + s;
        }
        len = s.length() / 2;
        String[] result = new String[len];
        for (int i = 0; i < len; i++) {
            result[i] = s.substring(i * 2, (i + 1) * 2);
        }
        return result;
    }

    public static boolean equals(String str1, String str2) {
        if (isBlank(str1) || isBlank(str2)) {
            return false;
        }
        return str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (isBlank(str1) || isBlank(str2)) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    public static boolean emptyOrNull(CharSequence s) {
        return isBlank(s);
    }

    public static boolean isBlank(CharSequence s) {
        if (null == s) {
            return true;
        } else if ("".equals(s.toString().trim())) {
            return true;
        } else {
            return false;
        }
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    /**
     * 判断字符串是否数字
     *
     * @param str
     * @return
     */
    public static boolean isNumric(String str) {
        if (str == null) {
            return false;
        }
        if (str.length() == 0) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static String format(Double d) {
        return format(d, 2, 2);
    }

    public static String format(Double d, Integer max, Integer min) {
        if (null == d) {
            return "";
        }
        Integer _min = (null == min || min < 0) ? 2 : min;
        String pattern = "0";
        DecimalFormat formatter = new DecimalFormat(pattern);
        if (null != _min) {
            formatter.setMinimumFractionDigits(_min);
        }
        if (null != max) {
            formatter.setMaximumFractionDigits(max);
        }
        String result = formatter.format(d);
        // int lastIndex = result.lastIndexOf(".");
        // if (lastIndex != -1 && result.substring(lastIndex + 1,
        // result.length()).length() < max) {
        // int num = result.substring(lastIndex + 1, result.length()).length();
        // if (num < max) {
        // for (int i = 0; i < max - num; i++) {
        // result += "0";
        // }
        // }
        //
        // }

        return result;
    }

    /**
     * 过滤数字字符串前出现的“0”
     *
     * @param str
     * @return
     */
    public static String filterZero(final String str) {
        if (isNotBlank(str) && isNumric(str)) {
            return Long.valueOf(str).toString();
        }
        return str;
    }

    /**
     * 16进制转4位2进制
     *
     * @param s
     * @return
     */
    public static String convertHexToBinary(String s) {
        return convertHexToBinary(s, 4);
    }

    /**
     * 16进制转2进制,不足bytes位则前补0
     *
     * @param s
     * @return
     */
    public static String convertHexToBinary(String s, int bytes) {
        return String.format("%0" + bytes + "d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(s, 16))));
    }

    /**
     * 2进制转16进制,不足2位则前补0
     *
     * @param binaryCode
     * @return
     */
    public static String convertBinaryToHex(String binaryCode) {
        return String.format("%2s", Integer.toHexString(Integer.parseInt(binaryCode, 2))).replace(" ", "0");
    }

    /**
     * 十进制码转16进制,前自动补齐 例：44 toHex 2c return 0000002c
     *
     * @param vinPart
     * @return
     */
    public static String convertDecimalToHex(String vinPart, int bytes) {
        // 这里不允许有小数，如果带小数则去掉小数
        Log.i("convertDecimalToHex", vinPart);
        if (vinPart.contains(".")) {
            vinPart = vinPart.split("\\.")[0];
        }
        return String.format("%" + bytes * 2 + "s", Integer.toHexString(Integer.valueOf(vinPart))).replace(" ", "0");
    }

    /**
     * ASC码字符串转为16进制码
     *
     * @param str
     * @return
     */
    public static String convertASCStringToHex(String str) {

        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sb.append(Integer.toHexString((int) chars[i]));
        }
        return sb.toString();
    }

    public static String removeHeadZero(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.replaceAll("^(0+)", "");
    }

    /**
     * 匹配字符串中符合正则的字符串列表
     *
     * @param text
     *            原字符串
     * @param patternStr
     *            正则表达式
     * @return
     */
    public static List<String> match(String text, String patternStr) {
        if (isBlank(text)) {
            return null;
        }

        List<String> matchList = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            matchList.add(matcher.group());
        }
        return matchList;
    }

    /**
     * N_L模式正解析
     *
     * @param str
     * @return
     */
    public static String justAnalysis(final String str) {
        String value = " ";
        switch (str) {
            case "00":
            case "01":
            case "02":
            case "03":
            case "04":
            case "05":
            case "06":
            case "07":
            case "08":
            case "09":
            case "0a":
            case "0b":
            case "0c":
            case "0d":
            case "0e":
            case "0f":
                value = str.substring(1);
                break;
            case "10":
                value = "g";
                break;
            case "11":
                value = "h";
                break;
            // case "12":
            // value = "i";
            // break;
            case "12":
                value = "j";
                break;
            case "13":
                value = "k";
                break;
            case "14":
                value = "l";
                break;
            case "15":
                value = "m";
                break;
            case "16":
                value = "n";
                break;
            case "17":
                value = "p";
                break;
            case "18":
                value = "r";
                break;
            case "19":
                value = "s";
                break;
            case "1a":
                value = "t";
                break;
            case "1b":
                value = "u";
                break;
            case "1c":
                value = "w";
                break;
            case "1d":
                value = "x";
                break;
            case "1e":
                value = "y";
                break;
            case "1f":
                value = "z";
                break;
            case "20":
                value = " ";
                break;
            // case "21":
            // value = "x";
            // break;
            // case "22":
            // value = "y";
            // break;
            // case "23":
            // value = "z";
            // break;
        }
        return value.toUpperCase();
    }

    /**
     * N_L模式反解析
     *
     * @param str
     * @return
     */
    public static String contraryAnalysis(final String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String s = str.substring(i, i + 1);
            String value = "FF";
            switch (s) {
                case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                case "A":
                case "B":
                case "C":
                case "D":
                case "E":
                case "F":
                    value = "0" + s;
                    break;
                case "G":
                    value = "10";
                    break;
                case "H":
                    value = "11";
                    break;
                // case "I":
                // value = "12";
                // break;
                case "J":
                    value = "12";
                    break;
                case "K":
                    value = "13";
                    break;
                case "L":
                    value = "14";
                    break;
                case "M":
                    value = "15";
                    break;
                case "N":
                    value = "16";
                    break;
                // case "O":
                // value = "18";
                // break;
                case "P":
                    value = "17";
                    break;
                // case "Q":
                // value = "1A";
                // break;
                case "R":
                    value = "18";
                    break;
                case "S":
                    value = "19";
                    break;
                case "T":
                    value = "1A";
                    break;
                case "U":
                    value = "1B";
                    break;
                // case "V":
                // value = "1F";
                // break;
                case "W":
                    value = "1C";
                    break;
                case "X":
                    value = "1D";
                    break;
                case "Y":
                    value = "1E";
                    break;
                case "Z":
                    value = "1F";
                    break;

            }
            sb.append(value);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * N_L模式反解析
     *
     * @param str
     * @return
     */
    public static String contraryStringByte(final String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String s = str.substring(i, i + 1);
            String value = "0";
            switch (s) {
                case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                case "A":
                case "B":
                case "C":
                case "D":
                case "E":
                case "F":
                    value = s;
                    break;
            }
            sb.append(value);
        }
        return sb.toString().toUpperCase();
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    public static boolean startsWith(String str, String prefix) {
        return startsWith(str, prefix, false);
    }

    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return EMPTY;
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * Transform binary format.
     *
     * @param num
     *            num.
     * @return temp result of method.
     */
    public static String transBinary(int num) {
        String temp = "";
        while (num != 0) {
            temp = (num % 2) + " " + temp;
            num = num / 2;
        }
        return temp;
    }

    public static String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

    public static CharSequence getRichText(String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        String desc = text.replace("\n", "<br/>");
        CharSequence richText = Html.fromHtml(desc);
        return richText;
    }

    public static CharSequence getText(String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        String richText = text.replace("\n", "<br>");
        return richText + "<br/><br/>";
    }

    public static CharSequence getRichTitleText(String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        String richText = formatTitle(text);
        return richText + "";
    }

    private static String formatTitle(String title) {
        return "<font color=#2E74E1> <b>" + title + "</b></font><br>";
    }

    public static String paddingHex(String hex, int len) {
        if (hex.length() < len) {
            int fixLen = len - hex.length();
            for (int j = 0; j < fixLen; j++) {
                hex = "0" + hex;
            }
        }
        return hex;
    }

    public static Integer toInteger(String intStr) {
        if (intStr == null) {
            return -1;
        }
        Integer integer = -1;
        try {
            integer = Integer.valueOf(intStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return integer;
    }

    public static String add0(String hexStr) {
        return hexStr.length() % 2 == 0 ? hexStr : ("0" + hexStr);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    /**
     * 判断是不是车牌号
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isLicensePlate(String str) throws PatternSyntaxException {
        String regExp = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static int getNumber(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return Integer.parseInt(sb.toString());
    }

    public static int hexToInter(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str.trim(), 16);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static byte[] hex2bytes(String hex) {
        if (hex.contains(" ")) {
            hex = hex.replaceAll(" ", "");
        }
        if (hex.length() % 2 == 1) {
            hex = "0" + hex;
        }
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i++) {
            if (i * 2 >= length) {
                break;
            }
            String subHex = hex.substring(i * 2, (i + 1) * 2);
            bytes[i] = (byte) (Integer.parseInt(subHex, 16) & 0xff);
        }

        return bytes;
    }

    public static int parseInt(String value) {
        return parseInt(value, 0);
    }

    public static int parseInt(String value, int defaultResult) {
        try {
            if (value == null || value.equals("null") || value.trim().length() == 0) {
                return defaultResult;
            }
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultResult;
    }

    /**
     * @param sourceStr
     *            源字符串
     * @param rgex
     *            正则表达式
     * @return
     */
    public static List<String> getSubStrings(String sourceStr, String rgex) {
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(sourceStr);
        List<String> subStrings = new ArrayList<>(1);
        while (m.find()) {
            int i = 1;
            subStrings.add(m.group(i));
            i++;
        }
        return subStrings;
    }

    /**
     * @param sourceStr
     *            源字符串
     * @param rgex
     *            正则表达式
     * @return
     */
    public static String getSubString(String sourceStr, String rgex) {
        List<String> stringList = getSubStrings(sourceStr, rgex);
        if (stringList != null && !stringList.isEmpty()) {
            return stringList.get(0);
        }
        return null;
    }

    public static String valueOf(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

    public static String[] split(String src, String regex) {
        String[] split = src.split(regex);
        List<String> result = new ArrayList(1);
        for (int i = 0; i < split.length; i++) {
            if (split[i].length() > 0) {
                result.add(split[i]);
            }
        }
        return result.toArray(new String[0]);
    }

    public static String getIQAformatString(String number, String iqaFormat) {
        // 诸如这样的格式string{"8-1-7", "20-3-18", "5-1-3"，“0”}
        String[] matchArray = iqaFormat.split(",");
        String matchIqaFormat = "-1";
        for (int k = 0; k < matchArray.length; k++) {
            String[] arr = matchArray[k].split("-");
            if (arr[0].equals(String.valueOf(number.length()))) {
                matchIqaFormat = matchArray[k];
                break;
            }
            if (arr[0].equals(String.valueOf(0))) {
                return number;
            }
        }

        int startIndex = Integer.parseInt(matchIqaFormat.split("-")[1]) - 1;
        int endIndex = Integer.parseInt(matchIqaFormat.split("-")[2]);

        number = number.substring(startIndex, endIndex);
        return number;
    }

    public static boolean isAllNumber(String str) {
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static int searchByIndexOf(String cmd , String replace){
        int leng = cmd.length();
        cmd = cmd.replace(replace, "");
        int len = cmd.length();
        return (leng - len) / replace.length();
    }

    public static String calLength(String lentotal){
        int tl = lentotal.length();
        for (int i= tl ;i<4;i++) {
            lentotal = "0"+ lentotal;
        }
        lentotal = lentotal.substring(2, 4) + lentotal.substring(0, 2);
        return lentotal;
    }

    /**
     * 要进行高低位转换的字符串
     *
     * @param section
     *            16进制字符串
     * @return
     */
    public static String changePosition(String section) {
        if (section==null){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < section.length() / 2; i++) {
            String data=section.substring(i*2,(i+1)*2);
            sb.insert(0,data);
        }
        return sb.toString();
    }

    /**
     * 两位转ASCII码
     *
     * @param hexData
     * @return
     */
    public static String convertHexToString(String hexData) {
        if (hexData == null || hexData.equals("")) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < hexData.length(); i = i + 2) {
                String s = hexData.substring(i, i + 2);
                if (s.equals("00")) {
                    continue;
                }
                sb.append((char) Long.parseLong(s, 16));
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("转ASCII失败");
        }
        return sb.toString();
    }

    public static String getProtocol(String systemId){
        switch (systemId){
            case "SYSTEM_001456":
                return "cummins";
            case "SYSTEM_001717":
                return "weiChaiC15";
            default:
                return "";

        }
    }


}

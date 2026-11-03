package com.lrz.grovecore.utils;

import com.google.common.base.Joiner;
import com.lrz.grovecore.envm.Constant;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.IntStream;

import static com.lrz.grovecore.envm.Constant.Spec.EMPTY;


/**
 * 字符串工具类
 *
 * @author lirenzhi
 * @since 2022/12/12 2:03 下午
 */
public final class StringUtil {

    public static boolean isEmpty(String s) {
        return null == s || s.isEmpty();
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 如果字符串里有空格也会返回true
     *
     * @param str str
     * @return boolean
     */
    public static boolean isBlank(String str) {
        if (str != null && !str.isEmpty()) {
            for (int i = 0; i < str.length(); ++i) {
                if (Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 解析字符串为整数集合
     *
     * @param idRange id范围
     * @param spec    规范
     * @return {@link List}<{@link Integer}>
     */
    public static List<Integer> parseToIntegerList(String idRange, String spec) {
        //如果是1-10 则返回1至10的集合
        if (idRange.contains("-")) {
            String[] split = idRange.split(spec);
            int start = Integer.parseInt(split[0]);
            int end = Integer.parseInt(split[1]);

            List<Integer> ids = new ArrayList<>();
            IntStream.rangeClosed(start, end).forEach(ids::add);
            return ids;
        }
        //其他情况都是拆分转换成集合
        return ListUtil.adaptArray(idRange.split("[,，]")).select(StringUtil::isNumber).collect(Integer::parseInt);
    }

    /**
     * 如果是null就返回null，否则返回本身
     *
     * @param x x
     * @return {@link String}
     */
    public static String emptyToNull(String x) {
        if (ObjectUtils.isEmpty(x)) {
            return null;
        }
        return x;
    }

    public static String replaceInvalidChar(String str) {
        if (null == str || str.isEmpty()) {
            return str;
        }
        List<String> chars = Arrays.asList("%", "_");
        for (String e : chars) {
            if (str.contains(e)) {
                switch (e) {
                    case "%":
                    case "_":
                        str = str.replace(e, "\\" + e);
                        break;
                    default:
                }
            }
        }
        return str;
    }

    /**
     * 隐藏电话号
     *
     * @param phoneNo 电话没有
     * @return {@link String}
     */
    public static String hidePhoneNos(String phoneNo) {
        String phone;
        if (phoneNo == null || phoneNo.length() <= 2) {
            return phoneNo;
        } else if (phoneNo.length() == 3) {
            phone = phoneNo.replaceAll("(\\d{1})\\d{1}(\\d{1})", "$1*$2");
        } else if (phoneNo.length() == 4) {
            phone = phoneNo.replaceAll("(\\d{1})\\d{2}(\\d{1})", "$1**$2");
        } else if (phoneNo.length() == 5) {
            phone = phoneNo.replaceAll("(\\d{1})\\d{3}(\\d{1})", "$1***$2");
        } else if (phoneNo.length() == 6) {
            phone = phoneNo.replaceAll("(\\d{1})\\d{4}(\\d{1})", "$1****$2");
        } else if (phoneNo.length() == 7) {
            phone = phoneNo.replaceAll("(\\d{1})\\d{4}(\\d{2})", "$1****$2");
        } else if (phoneNo.length() == 8) {
            phone = phoneNo.replaceAll("(\\d{2})\\d{4}(\\d{2})", "$1****$2");
        } else {
            phone = phoneNo.replaceAll("(\\d{3})\\d{4}(\\d{0})", "$1****$2");
        }
        return phone;
    }

    public static String hideName(String name) {
        if (name == null || name.isEmpty() || name.length() == 1) {
            return name;
        }
        if (name.length() == 2) {
            name = name.replaceAll("(.)(.)", "$1*");
        } else if (name.length() == 3) {
            name = name.replaceAll("(.)(.{2})", "$1**");
        } else if (name.length() == 4) {
            name = name.replaceAll("(.{2})(.{2})", "$1**");
        } else {
            name = name.replaceAll("(?<=.).", "*");
        }
        return name;
    }

    /**
     * content不满足length长度往前补指定字符
     *
     * @param c       指定字符
     * @param length  长度
     * @param content 内容
     * @return {@link String}
     */
    public static String flushLeft(String content, long length, String c) {
        String str = "";
        String cs = "";
        if (content.length() > length) {
            str = content;
        } else {
            for (int i = 0; i < length - content.length(); i++) {
                cs = cs + c;
            }
            str = cs + content;
        }
        return str;
    }

    /**
     * 补零
     *
     * @param num    需要处理的数字
     * @param length 需要满足的长度
     * @return {@link String}
     */
    public static String complementZero(Integer num, Integer length) {
        return String.format("%0" + length + "d", num);
    }

    public static String replaceInvalidFileNameCharWith_(String name) {
        if (isEmpty(name)) return null;
        return name.replace(Constant.Spec.LEFT_SLASH, Constant.Spec.UNDERLINE);
    }

    public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    public static String join(Collection<?> collect, String spec) {
        if (ListUtil.isEmpty(collect)) return null;
        return Joiner.on(spec).join(collect);
    }

    public static int length(CharSequence charSequence) {
        return charSequence == null ? 0 : charSequence.length();
    }

    public static String trimToEmpty(String str) {
        return str == null ? EMPTY : str.trim();
    }

    /**
     * 中文长度
     * 一个中文汉子长度为2
     *
     * @param str str
     * @return int
     */
    public static int chineseLength(String str) {
        int strLength = 0;
        if (isEmpty(str)) {
            return strLength;
        }
        final String pattern = "[\u0391-\uFFE5]";
        for (int i = 0; i < str.length(); i++) {
            strLength += String.valueOf(str.charAt(i)).matches(pattern) ? 2 : 1;
        }
        return strLength;
    }

    public static String UUID() {
        return UUID.randomUUID().toString().replace(Constant.Spec.HORIZONTAL_BAR, EMPTY).toLowerCase();
    }

    public static String genFileNameViaUUID(String suffix) {
        return UUID() + Constant.Spec.SEPARATOR + suffix;
    }

    /**
     * 格式化
     * eg:format("{}{}{}", "1","2","3") = 123
     * 类似占位符
     *
     * @param source  源
     * @param objects 对象
     * @return {@link String}
     */
    public static String format(String source, Object... objects) {
        if (ObjectUtils.isEmpty(objects)) {
            return source;
        }
        return MessageFormatter.arrayFormat(source, objects).getMessage();
    }

    public static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    public static List<String> split(String str, String c) {
        if (str == null) return null;
        if (str.isEmpty()) return Collections.emptyList();
        return ListUtil.adaptArray(str.split(c));
    }

    public static boolean isAnyEmpty(String... any) {
        if (ListUtil.adaptItems(any).isEmpty()) return true;
        return ListUtil.adaptItems(any).anySatisfy(StringUtil::isEmpty);
    }

    /**
     * 集合里的元素是否出现在字符串里
     *
     * @param str      字符
     * @param iterable 集合
     * @return boolean
     */
    public static boolean containsAny(String str, Iterable<String> iterable) {
        //不包含空
        if (ListUtil.isEmpty(iterable)) {
            return false;
        }
        //移除空值，空对象比较结果恒为false.
        MutableList<String> values = ListUtil.adaptIterable(iterable);
        values.removeIf(Objects::isNull);
        for (String s : values) {
            //若包含任意一个，返回true
            if (str.contains(s)) {
                return true;
            }
        }
        return false;
    }
}

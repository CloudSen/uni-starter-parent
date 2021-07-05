package cn.uni.common.util;

/**
 * @author CloudS3n
 * @date 2021-07-05 10:46
 */
public class CamelUtils {

    public static String underline(String name) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < name.length(); ++i) {
            char ch = name.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                char lower = (char) (ch + 32);
                if (i > 0) {
                    buf.append('_');
                }
                buf.append(lower);
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        System.out.println(underline("sdfCloudS3n"));
    }
}

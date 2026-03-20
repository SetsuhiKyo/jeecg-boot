package org.jeecg.modules.yy.utils;

import dm.jdbc.util.StringUtil;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2026/03/08
 * @Version 0.1
 */
public class Base62Util {

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * encode
     */
    public static String encode(String id) {

        StringBuilder sb = new StringBuilder();
        long num = 0;
        if (id != null && !StringUtil.isEmpty(id)) {
            num = Long.parseLong(id);
        }

        while (num > 0) {
            int rem = (int) (num % 62);
            sb.append(BASE62.charAt(rem));
            num = num / 62;
        }

        return sb.reverse().toString();
    }

    /**
     * decode
     */
    public static String decode(String str) {

        long num = 0;

        for (char c : str.toCharArray()) {
            num = num * 62 + BASE62.indexOf(c);
        }

        return String.valueOf(num);
    }

//    public static void main(String[] args){
//        String orderId = "2028849143597359105";
//
//        String shortId = Base62Util.encode(orderId);
//
//        System.out.println(shortId);
//
//        String decodeOrderId = Base62Util.decode(shortId);
//
//        System.out.println(decodeOrderId);
//    }
}

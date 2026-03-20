package org.jeecg.config.yyi18n;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2026/03/08
 * @Version 0.1
 */
public class LangContext {

    private static final ThreadLocal<String> LANG_HOLDER = new ThreadLocal<>();

    public static void setLang(String lang) {
        LANG_HOLDER.set(lang);
    }

    public static String getLang() {
        return LANG_HOLDER.get();
    }

    public static void clear() {
        LANG_HOLDER.remove();
    }
}

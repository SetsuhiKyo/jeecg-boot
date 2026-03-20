package org.jeecg.config.yyi18n;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2026/03/08
 * @Version 0.1
 */
@Component
public class LangInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String lang = request.getHeader("X-Lang-Cd");

        if (lang == null || lang.isEmpty()) {
            lang = "en"; // 默认语言
        }

        LangContext.setLang(lang);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        LangContext.clear();
    }
}

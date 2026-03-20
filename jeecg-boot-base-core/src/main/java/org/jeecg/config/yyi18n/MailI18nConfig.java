package org.jeecg.config.yyi18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2026/02/20
 * @Version 0.1
 */
@Configuration
public class MailI18nConfig {

    @Bean("mailMessageSource")
    public MessageSource mailMessageSource() {
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasename("i18n/mail/messages");
        ms.setDefaultEncoding("UTF-8");
        ms.setFallbackToSystemLocale(false);
        return ms;
    }
}

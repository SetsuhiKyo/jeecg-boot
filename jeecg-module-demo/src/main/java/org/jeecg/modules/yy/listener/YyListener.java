package org.jeecg.modules.yy.listener;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.EmailTemplateEnum;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.yy.events.*;
import org.jeecg.modules.yy.place.entity.YyCustomer;
import org.jeecg.modules.yy.place.entity.YyOrder;
import org.jeecg.modules.yy.place.service.IYyCustomerService;
import org.jeecg.modules.yy.utils.Base62Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Component
public class YyListener {
    @Autowired
    private IYyCustomerService yyCustomerService;
    @Autowired
    private BaseCommonService baseCommonService;
    @Autowired
    private ISysBaseAPI sysBaseApi;
    @Resource(name = "mailMessageSource")
    private MessageSource messageSource;
    @Value("${jeecg.uni-app.customer.activate-link}")
    private String CUSTOMER_ACTIVATE_URL;
    @Value("${jeecg.uni-app.customer.profile-url}")
    private String CUSTOMER_RPOFILE_URL;

    @Value("${jeecg.uni-app.support.email}")
    private String SUPPORT_EMAIL;
    @Value("${jeecg.uni-app.payment.stripelink}")
    private String PAYMENT_STRIPE_LINK_URL;
    @Value("${jeecg.uni-app.payment.paypallink}")
    private String PAYMENT_PAYPAL_LINK_URL;
    /**
     *  发送邮件：用户注册成功（邮件内埋入激活链接）（给顾客）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendCustomerAddedMail(CustomerAddedEvent event) {

        YyCustomer customer = event.getCustomer();
        Locale locale = getUserLanguage(customer.getLangCd());
        String token = event.getToken();

        // 向顾客提供的邮箱发送用户激活邮件，邮件内埋入激活链接
        String link= CUSTOMER_ACTIVATE_URL+token;
        String subject = messageSource.getMessage(EmailTemplateEnum.CUSTOMER_ACCOUNT_ACTIVATE.getName()+".subject", null, locale);
        String email = customer.getEmail();
        JSONObject params = new JSONObject();
        params.put("activationLink", link);
        params.put("mail_title", messageSource.getMessage(EmailTemplateEnum.CUSTOMER_ACCOUNT_ACTIVATE.getName()+".title", null, locale));
        params.put("mail_content", messageSource.getMessage(EmailTemplateEnum.CUSTOMER_ACCOUNT_ACTIVATE.getName()+".content", null, locale));
        params.put("mail_button", messageSource.getMessage(EmailTemplateEnum.CUSTOMER_ACCOUNT_ACTIVATE.getName()+".button", null, locale));
        setMailFooter(params,locale);
        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.CUSTOMER_ACCOUNT_ACTIVATE,params);

        baseCommonService.addLog("添加顾客，username： " +customer.getCustomerName() ,CommonConstant.LOG_TYPE_2, 2);
    }


    /**
     *  发送邮件：订单已受理（预约成功）（给顾客）TODO:给客服
     *
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderCreatedMail(OrderCreatedEvent event) {

        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        Locale locale = getUserLanguage(customer.getLangCd());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.orderCreated.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.orderCreated.line1", null, locale));
        params.put("label_orderInfo", messageSource.getMessage("label.orderInfo", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", Base62Util.encode(orderInfo.getId()));
        params.put("label_orderName", messageSource.getMessage("label.orderName", null, locale));
        params.put("orderName", orderInfo.getOrderName());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        String formatted = "";
        if (orderInfo.getStartTime() != null) {
            formatted = orderInfo.getStartTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        params.put("serviceDate", formatted);
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.orderCreated.line2", null, locale));
        params.put("mail_orderCreated_line3", messageSource.getMessage("mail.orderCreated.line3", null, locale));
        setMailFooter(params,locale);

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.ORDER_CREATE,params);

        baseCommonService.addLog("订单已受理，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：订单已确认（等待付款）（给顾客）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderConfirmedMail(OrderConfirmedEvent event) {
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        Locale locale = getUserLanguage(customer.getLangCd());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.orderConfirmed.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderConfirmed_line1", messageSource.getMessage("mail.orderConfirmed.line1", null, locale));
        params.put("label_orderInfo", messageSource.getMessage("label.orderInfo", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", Base62Util.encode(orderInfo.getId()));
        params.put("label_orderName", messageSource.getMessage("label.orderName", null, locale));
        params.put("orderName", orderInfo.getOrderName());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        String formatted = "";
        if (orderInfo.getStartTime() != null) {
            formatted = orderInfo.getStartTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        params.put("serviceDate", formatted);
        params.put("label_allamount", messageSource.getMessage("label.allamount", null, locale));
        params.put("allamount", orderInfo.getServerPrice());
        params.put("label_deposit", messageSource.getMessage("label.deposit", null, locale));
//        params.put("deposit", orderInfo.getServerPrice().multiply(new BigDecimal(0.2))); // 最终服务价格的20%作为定金
        params.put("deposit", new BigDecimal(5000)); // 前期固定收定金5000日元
        params.put("label_balance", messageSource.getMessage("label.balance", null, locale));
        params.put("balance", orderInfo.getServerPrice().subtract(new BigDecimal(5000))); // 尾款
        params.put("label_balance_notes", messageSource.getMessage("label.balance.notes", null, locale));
        params.put("label_yen", messageSource.getMessage("label.yen", null, locale));
        params.put("label_paymentDeadline", messageSource.getMessage("label.paymentDeadline", null, locale));
        // 转换为 LocalDateTime
        LocalDateTime ldt = orderInfo.getStartTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        params.put("paymentDeadline", ldt.minusDays(6));

        params.put("label_aboutPayment", messageSource.getMessage("label.aboutPayment", null, locale));
        params.put("label_aboutPayment_line1", messageSource.getMessage("label.aboutPayment.line1", null, locale));
        params.put("label_aboutPayment_line2", messageSource.getMessage("label.aboutPayment.line2", null, locale));
        params.put("label_aboutPayment_method1", messageSource.getMessage("label.aboutPayment.method1", null, locale));
        params.put("label_aboutPayment_method1_linkurl", PAYMENT_STRIPE_LINK_URL);
        params.put("label_aboutPayment_method1_linklabel", messageSource.getMessage("label.aboutPayment.method1.linklabel", null, locale));
        params.put("label_aboutPayment_method2", messageSource.getMessage("label.aboutPayment.method2", null, locale));
        params.put("label_aboutPayment_method2_linkurl", PAYMENT_PAYPAL_LINK_URL);
        params.put("label_aboutPayment_method2_linklabel", messageSource.getMessage("label.aboutPayment.method2.linklabel", null, locale));
        params.put("label_aboutPayment_line3", messageSource.getMessage("label.aboutPayment.line3", null, locale));

        params.put("mail_orderConfirmed_line2", messageSource.getMessage("mail.orderConfirmed.line2", null, locale));
        params.put("mail_orderConfirmed_line3", messageSource.getMessage("mail.orderConfirmed.line3", null, locale));
        setMailFooter(params,locale);

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.ORDER_CONFIREMED,params);

        baseCommonService.addLog("订单已确认，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：提醒顾客付款（给顾客） TODO：批处理未实现。该邮件已测试但未触发
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPaymentReminderMail(PaymentReminderEvent event) {
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        Locale locale = getUserLanguage(customer.getLangCd());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.paymentReminder.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_paymentReminder_line1", messageSource.getMessage("mail.paymentReminder.line1", null, locale));
        params.put("label_orderInfo", messageSource.getMessage("label.orderInfo", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", Base62Util.encode(orderInfo.getId()));
        params.put("label_orderName", messageSource.getMessage("label.orderName", null, locale));
        params.put("orderName", orderInfo.getOrderName());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        String formatted = "";
        if (orderInfo.getStartTime() != null) {
            formatted = orderInfo.getStartTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        params.put("serviceDate", formatted);
        params.put("label_allamount", messageSource.getMessage("label.allamount", null, locale));
        params.put("allamount", orderInfo.getServerPrice());
        params.put("label_deposit", messageSource.getMessage("label.deposit", null, locale));
//        params.put("deposit", orderInfo.getServerPrice().multiply(new BigDecimal(0.2))); // 最终服务价格的20%作为定金
        params.put("deposit", new BigDecimal(5000)); // 前期固定收定金5000日元
        params.put("label_yen", messageSource.getMessage("label.yen", null, locale));
        params.put("label_paymentDeadline", messageSource.getMessage("label.paymentDeadline", null, locale));
        // 转换为 LocalDateTime
        LocalDateTime ldt = orderInfo.getStartTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        params.put("paymentDeadline", ldt.minusDays(6));
        params.put("mail_paymentReminder_warning1", messageSource.getMessage("mail.paymentReminder.warning1", null, locale));
        params.put("mail_paymentReminder_warning2", messageSource.getMessage("mail.paymentReminder.warning2", null, locale));
        params.put("mail_paymentReminder_line2", messageSource.getMessage("mail.paymentReminder.line2", null, locale));
        params.put("mail_paymentReminder_line3", messageSource.getMessage("mail.paymentReminder.line3", null, locale));
        setMailFooter(params,locale);

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.PAYMENT_REMINDER,params);

        baseCommonService.addLog("提示付款通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：已付款确认（给客服）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPaymentPaiedConfirmMail(PaymentPaiedConfirmEvent event) {
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        String email = SUPPORT_EMAIL;

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.paymentPaiedConfirm.subject", null, locale);
        params.put("mail_paymentPaiedConfirm_line1", messageSource.getMessage("mail.paymentPaiedConfirm.line1", null, locale));
        params.put("label_orderInfo", messageSource.getMessage("label.orderInfo", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", Base62Util.encode(orderInfo.getId()));
        params.put("label_customerName", messageSource.getMessage("label.customerName", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_paymentPaiedConfirm_line2", messageSource.getMessage("mail.paymentPaiedConfirm.line2", null, locale));
//        setMailFooter(params,locale);

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.PAYMENT_RECEIVED,params);

        baseCommonService.addLog("已付款确认，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：服务即将开始通知（给顾客）TODO:给司机公司
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendServiceReminderMail(ServiceReminderEvent event) {
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        Locale locale = getUserLanguage(customer.getLangCd());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.serviceReminder.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_serviceReminder_line1", messageSource.getMessage("mail.serviceReminder.line1", null, locale));
        params.put("label_orderInfo", messageSource.getMessage("label.orderInfo", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", Base62Util.encode(orderInfo.getId()));
        params.put("label_orderName", messageSource.getMessage("label.orderName", null, locale));
        params.put("orderName", orderInfo.getOrderName());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        String formatted = "";
        if (orderInfo.getStartTime() != null) {
            formatted = orderInfo.getStartTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        params.put("serviceDate", formatted);
        params.put("mail_serviceReminder_line2", messageSource.getMessage("mail.serviceReminder.line2", null, locale));
        params.put("mail_serviceReminder_line3", messageSource.getMessage("mail.serviceReminder.line3", null, locale));
        params.put("mail_serviceReminder_line4", messageSource.getMessage("mail.serviceReminder.line4", null, locale));
        setMailFooter(params,locale);

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.SERVICE_REMINDER,params);

        baseCommonService.addLog("服务即将开始通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：服务完成通知(给顾客) TODO:给司机公司
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendServiceCompletedMail(ServiceCompletedEvent event) {
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        Locale locale = getUserLanguage(customer.getLangCd());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.serviceCompleted.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_serviceCompleted_line1", messageSource.getMessage("mail.serviceCompleted.line1", null, locale));
        params.put("label_orderInfo", messageSource.getMessage("label.orderInfo", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", Base62Util.encode(orderInfo.getId()));
        params.put("label_orderName", messageSource.getMessage("label.orderName", null, locale));
        params.put("orderName", orderInfo.getOrderName());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        String formatted = "";
        if (orderInfo.getStartTime() != null) {
            formatted = orderInfo.getStartTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        params.put("serviceDate", formatted);
        params.put("mail_serviceCompleted_line2", messageSource.getMessage("mail.serviceCompleted.line2", null, locale));
        params.put("mail_serviceCompleted_line3", messageSource.getMessage("mail.serviceCompleted.line3", null, locale));
        setMailFooter(params,locale);

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.SERVICE_COMPLETED,params);

        baseCommonService.addLog("服务完成通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：订单取消通知（给顾客）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderCancelledMail(OrderCancelledEvent event) {
        boolean isCancelledByCustomer = true; // 平台上线前期只考虑客户主动取消的情况
        boolean isCancelledBySystem = !isCancelledByCustomer; // TODO:实现批处理，订单成约后规定时间内没有转账⇒提醒转账通知⇒自动取消订单处理
        boolean isNeedRefund = event.isNeedRefund();
        boolean isNoRefund = !isNeedRefund;

        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        Locale locale = getUserLanguage(customer.getLangCd());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.orderCancelled.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());

        params.put("isCancelledByCustomer", isCancelledByCustomer);
        params.put("isCancelledBySystem", isCancelledBySystem);
        params.put("isNoRefund", isNoRefund);
        params.put("isNeedRefund", isNeedRefund);

        if (isCancelledByCustomer){
            params.put("mail_orderCancelled_bycustomer", messageSource.getMessage("mail.orderCancelled.bycustomer", null, locale));
        }
        if (isCancelledBySystem){
            params.put("mail_orderCancelled_bysystem", messageSource.getMessage("mail.orderCancelled.bysystem", null, locale));
        }

        params.put("label_orderInfo", messageSource.getMessage("label.orderInfo", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", Base62Util.encode(orderInfo.getId()));
        params.put("label_orderName", messageSource.getMessage("label.orderName", null, locale));
        params.put("orderName", orderInfo.getOrderName());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        String formatted = "";
        if (orderInfo.getStartTime() != null) {
            formatted = orderInfo.getStartTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        params.put("serviceDate", formatted);

        if (isCancelledByCustomer) {
            if (isNoRefund) {
                params.put("label_aboutPrice", messageSource.getMessage("label.aboutPrice", null, locale));
                params.put("mail_orderCancelled_norefund1", messageSource.getMessage("mail.orderCancelled.norefund1", null, locale));
                params.put("mail_orderCancelled_norefund2", messageSource.getMessage("mail.orderCancelled.norefund2", null, locale));
            }
            if(isNeedRefund) {
                params.put("label_aboutPrice", messageSource.getMessage("label.aboutPrice", null, locale));
                params.put("mail_orderCancelled_needrefund1", messageSource.getMessage("mail.orderCancelled.needrefund1", null, locale));
                params.put("mail_orderCancelled_needrefund2", messageSource.getMessage("mail.orderCancelled.needrefund2", null, locale));

                params.put("label_aboutRefund", messageSource.getMessage("label.aboutRefund", null, locale));
                params.put("mail_orderCancelled_refundline1", messageSource.getMessage("mail.orderCancelled.refundline1", null, locale));
                params.put("mail_orderCancelled_refundline2", messageSource.getMessage("mail.orderCancelled.refundline2", null, locale));
                params.put("mail_orderCancelled_refundline3", messageSource.getMessage("mail.orderCancelled.refundline3", null, locale));
                params.put("mail_orderCancelled_refundline4", messageSource.getMessage("mail.orderCancelled.refundline4", null, locale));
                params.put("mail_orderCancelled_refundline5", messageSource.getMessage("mail.orderCancelled.refundline5", null, locale));
                params.put("mail_orderCancelled_refundline6", messageSource.getMessage("mail.orderCancelled.refundline6", null, locale));

            }
        }

        if (isCancelledBySystem){
            params.put("mail_orderCancelled_bysystem_info1", messageSource.getMessage("mail.orderCancelled.bysystem.info1", null, locale));
            params.put("mail_orderCancelled_bysystem_info2", messageSource.getMessage("mail.orderCancelled.bysystem.info2", null, locale));
            params.put("mail_orderCancelled_bysystem_info3", messageSource.getMessage("mail.orderCancelled.bysystem.info3", null, locale));
        }

        setMailFooter(params,locale);

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.ORDER_CANCELLED,params);

        baseCommonService.addLog("订单取消通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  TODO: 发送邮件：结算申请通知（给司机公司）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendSettlementRequestMail(SettlementRequestEvent event) {
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        Locale locale = getUserLanguage(customer.getLangCd());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.settlementRequest.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.settlementRequest.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", Base62Util.encode(orderInfo.getId()));
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        String formatted = "";
        if (orderInfo.getStartTime() != null) {
            formatted = orderInfo.getStartTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        params.put("serviceDate", formatted);
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.settlementRequest.line2", null, locale));
        setMailFooter(params,locale);

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.SETTLEMENT_REQUEST,params);

        baseCommonService.addLog("结算申请通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  TODO: 发送邮件：结算完成通知（给司机公司）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendSettlementCompletedMail(SettlementCompletedEvent event) {
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        Locale locale = getUserLanguage(customer.getLangCd());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.settlementCompleted.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.settlementCompleted.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", Base62Util.encode(orderInfo.getId()));
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        String formatted = "";
        if (orderInfo.getStartTime() != null) {
            formatted = orderInfo.getStartTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        params.put("serviceDate", formatted);
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.settlementCompleted.line2", null, locale));
        setMailFooter(params,locale);

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.SETTLEMENT_COMPLETED,params);

        baseCommonService.addLog("结算完成通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     * TODO: 发送邮件：退款完成通知（给顾客）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendRefundCompletedMail(RefundCompletedEvent event) {
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        Locale locale = getUserLanguage(customer.getLangCd());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.refundCompleted.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.refundCompleted.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", Base62Util.encode(orderInfo.getId()));
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        String formatted = "";
        if (orderInfo.getStartTime() != null) {
            formatted = orderInfo.getStartTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        params.put("serviceDate", formatted);
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.refundCompleted.line2", null, locale));
        setMailFooter(params,locale);

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.REFUND_COMPLETED,params);

        baseCommonService.addLog("退款完成通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：订单请求取消通知(给客服)
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendRequestCancelMail(RequestCancelEvent event) {
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        Locale locale = getUserLanguage(customer.getLangCd());
        String email = SUPPORT_EMAIL;

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.requestCancel.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_requestCancel_line1", messageSource.getMessage("mail.requestCancel.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", Base62Util.encode(orderInfo.getId()));
        params.put("label_paymentStatus", messageSource.getMessage("label.paymentStatus", null, locale));
        params.put("paymentStatus", orderInfo.getPaymentSts());
        if("UNPAID".equals(orderInfo.getPaymentSts())) {
            params.put("mail_requestCancel_line2", messageSource.getMessage("mail.requestCancel.line2", null, locale));
        }
        if("PENDING_CONFIRM".equals(orderInfo.getPaymentSts())) {
            params.put("mail_requestCancel_line3", messageSource.getMessage("mail.requestCancel.line3", null, locale));
        }
        if("PAID".equals(orderInfo.getPaymentSts())) {
            params.put("mail_requestCancel_line4", messageSource.getMessage("mail.requestCancel.line4", null, locale));
        }
//        setMailFooter(params,locale);

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.REQUEST_CANCEL,params);

        baseCommonService.addLog("订单请求取消通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    private Locale getUserLanguage(String langCd){
        // 使用YyListenerTest测试邮件内容时使用
//        langCd="zh-CN";
        // 用户使用语言，默认英语
        if (langCd == null) {
            langCd="en";
        }
        Locale locale = switch (langCd) {
            case "zh-CN" -> Locale.SIMPLIFIED_CHINESE;
            case "zh-TW" -> Locale.TRADITIONAL_CHINESE;
            case "ja" -> Locale.JAPANESE;
            default -> Locale.ENGLISH;
        };
        return locale;
    }

    private void setMailFooter(JSONObject params,Locale locale){
        params.put("mail_footer_reminder", messageSource.getMessage("mail.footer.reminder", null, locale));
        params.put("mail_footer_logo", messageSource.getMessage("mail.footer.logo", null, locale));
        params.put("mail_footer_logo_content", messageSource.getMessage("mail.footer.logo.content", null, locale));
        params.put("mail_footer_support", messageSource.getMessage("mail.footer.support", null, locale));
        params.put("mail_footer_whatsapp_url", messageSource.getMessage("mail.footer.whatsapp.url", null, locale));
        params.put("mail_footer_whatsapp_display", messageSource.getMessage("mail.footer.whatsapp.display", null, locale));
        params.put("mail_footer_line_url", messageSource.getMessage("mail.footer.line.url", null, locale));
        params.put("mail_footer_line_display", messageSource.getMessage("mail.footer.line.display", null, locale));
        params.put("mail_footer_wechat_id", messageSource.getMessage("mail.footer.wechat.id", null, locale));
        params.put("mail_footer_wechat_copy", messageSource.getMessage("mail.footer.wechat.copy", null, locale));
        params.put("mail_footer_webhat_copied", messageSource.getMessage("mail.footer.webhat.copied", null, locale));
        params.put("mail_footer_phone_no", messageSource.getMessage("mail.footer.phone.no", null, locale));
        params.put("mail_footer_phone_display", messageSource.getMessage("mail.footer.phone.display", null, locale));
        params.put("mail_footer_email_url", messageSource.getMessage("mail.footer.email.url", null, locale));
        params.put("mail_footer_email_display", messageSource.getMessage("mail.footer.email.display", null, locale));
        params.put("mail_footer_website_url", messageSource.getMessage("mail.footer.website.url", null, locale));
        params.put("mail_footer_website_display", messageSource.getMessage("mail.footer.website.display", null, locale));
        params.put("mail_footer_servicetime", messageSource.getMessage("mail.footer.servicetime", null, locale));
        params.put("mail_footer_servicetime_content", messageSource.getMessage("mail.footer.servicetime.content", null, locale));
        params.put("mail_footer_servicerange", messageSource.getMessage("mail.footer.servicerange", null, locale));
        params.put("mail_footer_servicerange_content", messageSource.getMessage("mail.footer.servicerange.content", null, locale));
        params.put("mail_footer_companyinfo", messageSource.getMessage("mail.footer.companyinfo", null, locale));
        params.put("mail_footer_companyinfo_name", messageSource.getMessage("mail.footer.companyinfo.name", null, locale));
        params.put("mail_footer_companyinfo_addr", messageSource.getMessage("mail.footer.companyinfo.addr", null, locale));

    }
}

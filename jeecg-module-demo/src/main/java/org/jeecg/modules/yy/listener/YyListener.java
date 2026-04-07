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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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

    /**
     *  发送邮件：用户注册成功（邮件内埋入激活链接）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendCustomerAddedMail(CustomerAddedEvent event) {
        Locale locale = getUserLanguage();
        YyCustomer customer = event.getCustomer();
        String token = event.getToken();

        // 向顾客提供的邮箱发送用户激活邮件，邮件内埋入激活链接
        String link= CUSTOMER_ACTIVATE_URL+token;
        String subject = messageSource.getMessage(EmailTemplateEnum.CUSTOMER_ACCOUNT_ACTIVATE.getName()+".mail.subject", null, locale);
        String email = customer.getEmail();
        JSONObject params = new JSONObject();
        params.put("activationLink", link);
        params.put("mail_title", messageSource.getMessage(EmailTemplateEnum.CUSTOMER_ACCOUNT_ACTIVATE.getName()+".mail.title", null, locale));
        params.put("mail_content", messageSource.getMessage(EmailTemplateEnum.CUSTOMER_ACCOUNT_ACTIVATE.getName()+".mail.content", null, locale));
        params.put("mail_button", messageSource.getMessage(EmailTemplateEnum.CUSTOMER_ACCOUNT_ACTIVATE.getName()+".mail.button", null, locale));
        params.put("mail_footer", messageSource.getMessage(EmailTemplateEnum.CUSTOMER_ACCOUNT_ACTIVATE.getName()+".mail.footer", null, locale));
        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.CUSTOMER_ACCOUNT_ACTIVATE,params);

        baseCommonService.addLog("添加顾客，username： " +customer.getCustomerName() ,CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：订单已受理（预约成功）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderCreatedMail(OrderCreatedEvent event) {
        Locale locale = getUserLanguage();
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.orderCreated.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.orderCreated.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", orderInfo.getId());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        params.put("serviceDate", LocalDateTime.now());
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.orderCreated.line2", null, locale));
        params.put("orderUrl", CUSTOMER_RPOFILE_URL);
        params.put("buttonText", messageSource.getMessage("mail.orderCreated.button", null, locale));
        params.put("signature_line1", messageSource.getMessage("signature.line1", null, locale));
        params.put("signature_line2", messageSource.getMessage("signature.line2", null, locale));

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.ORDER_CREATE,params);

        baseCommonService.addLog("订单已受理，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：订单已确认（等待付款）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderConfirmedMail(OrderConfirmedEvent event) {
        Locale locale = getUserLanguage();
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.orderConfirmed.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.orderConfirmed.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", orderInfo.getId());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        params.put("serviceDate", LocalDateTime.now());
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.orderConfirmed.line2", null, locale));
        params.put("signature_line1", messageSource.getMessage("signature.line1", null, locale));
        params.put("signature_line2", messageSource.getMessage("signature.line2", null, locale));

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.ORDER_CONFIREMED,params);

        baseCommonService.addLog("订单已确认，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：提示付款通知
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPaymentReminderMail(PaymentReminderEvent event) {
        Locale locale = getUserLanguage();
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.paymentReminder.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.paymentReminder.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", orderInfo.getId());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        params.put("serviceDate", LocalDateTime.now());
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.paymentReminder.line2", null, locale));
        params.put("signature_line1", messageSource.getMessage("signature.line1", null, locale));
        params.put("signature_line2", messageSource.getMessage("signature.line2", null, locale));

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.PAYMENT_REMINDER,params);

        baseCommonService.addLog("提示付款通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：已付款确认
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPaymentReceivedMail(PaymentReceivedEvent event) {
        Locale locale = getUserLanguage();
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.paymentReceived.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.paymentReceived.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", orderInfo.getId());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        params.put("serviceDate", LocalDateTime.now());
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.paymentReceived.line2", null, locale));
        params.put("signature_line1", messageSource.getMessage("signature.line1", null, locale));
        params.put("signature_line2", messageSource.getMessage("signature.line2", null, locale));

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.PAYMENT_RECEIVED,params);

        baseCommonService.addLog("已付款确认，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：服务即将开始通知
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendServiceReminderMail(ServiceReminderEvent event) {
        Locale locale = getUserLanguage();
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.serviceReminder.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.serviceReminder.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", orderInfo.getId());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        params.put("serviceDate", LocalDateTime.now());
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.serviceReminder.line2", null, locale));
        params.put("signature_line1", messageSource.getMessage("signature.line1", null, locale));
        params.put("signature_line2", messageSource.getMessage("signature.line2", null, locale));

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.SERVICE_REMINDER,params);

        baseCommonService.addLog("服务即将开始通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：服务完成通知
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendServiceCompletedMail(ServiceCompletedEvent event) {
        Locale locale = getUserLanguage();
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.serviceCompleted.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.serviceCompleted.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", orderInfo.getId());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        params.put("serviceDate", LocalDateTime.now());
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.serviceCompleted.line2", null, locale));
        params.put("signature_line1", messageSource.getMessage("signature.line1", null, locale));
        params.put("signature_line2", messageSource.getMessage("signature.line2", null, locale));

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.SERVICE_COMPLETED,params);

        baseCommonService.addLog("服务完成通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：订单取消通知
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderCancelledMail(OrderCancelledEvent event) {
        Locale locale = getUserLanguage();
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.orderCancelled.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.orderCancelled.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", orderInfo.getId());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        params.put("serviceDate", LocalDateTime.now());
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.orderCancelled.line2", null, locale));
        params.put("signature_line1", messageSource.getMessage("signature.line1", null, locale));
        params.put("signature_line2", messageSource.getMessage("signature.line2", null, locale));

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.ORDER_CANCELLED,params);

        baseCommonService.addLog("订单取消通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：结算申请通知（司机公司）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendSettlementRequestMail(SettlementRequestEvent event) {
        Locale locale = getUserLanguage();
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.settlementRequest.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.settlementRequest.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", orderInfo.getId());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        params.put("serviceDate", LocalDateTime.now());
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.settlementRequest.line2", null, locale));
        params.put("signature_line1", messageSource.getMessage("signature.line1", null, locale));
        params.put("signature_line2", messageSource.getMessage("signature.line2", null, locale));

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.SETTLEMENT_REQUEST,params);

        baseCommonService.addLog("结算申请通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：结算完成通知（司机公司）
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendSettlementCompletedMail(SettlementCompletedEvent event) {
        Locale locale = getUserLanguage();
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.settlementCompleted.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.settlementCompleted.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", orderInfo.getId());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        params.put("serviceDate", LocalDateTime.now());
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.settlementCompleted.line2", null, locale));
        params.put("signature_line1", messageSource.getMessage("signature.line1", null, locale));
        params.put("signature_line2", messageSource.getMessage("signature.line2", null, locale));

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.SETTLEMENT_COMPLETED,params);

        baseCommonService.addLog("结算完成通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：退款完成通知
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendRefundCompletedMail(RefundCompletedEvent event) {
        Locale locale = getUserLanguage();
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.refundCompleted.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_orderCreated_line1", messageSource.getMessage("mail.refundCompleted.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", orderInfo.getId());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        params.put("serviceDate", LocalDateTime.now());
        params.put("mail_orderCreated_line2", messageSource.getMessage("mail.refundCompleted.line2", null, locale));
        params.put("signature_line1", messageSource.getMessage("signature.line1", null, locale));
        params.put("signature_line2", messageSource.getMessage("signature.line2", null, locale));

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.REFUND_COMPLETED,params);

        baseCommonService.addLog("退款完成通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    /**
     *  发送邮件：订单请求取消通知
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendRequestCancelMail(RequestCancelEvent event) {
        Locale locale = getUserLanguage();
        YyOrder orderInfo = event.getOrder();
        YyCustomer customer = yyCustomerService.getById(orderInfo.getCustomerId());
        String email = customer.getEmail();

        JSONObject params = new JSONObject();
        String subject = messageSource.getMessage("mail.requestCancel.subject", null, locale);
        params.put("greeting", messageSource.getMessage("greeting", null, locale));
        params.put("customerName", customer.getCustomerName());
        params.put("mail_requestCancel_line1", messageSource.getMessage("mail.requestCancel.line1", null, locale));
        params.put("label_orderNo", messageSource.getMessage("label.orderNo", null, locale));
        params.put("orderNo", orderInfo.getId());
        params.put("label_serviceDate", messageSource.getMessage("label.serviceDate", null, locale));
        params.put("serviceDate", LocalDateTime.now());
        params.put("mail_requestCancel_line2", messageSource.getMessage("mail.requestCancel.line2", null, locale));
        params.put("signature_line1", messageSource.getMessage("signature.line1", null, locale));
        params.put("signature_line2", messageSource.getMessage("signature.line2", null, locale));

        sysBaseApi.sendHtmlTemplateEmail(email,subject, EmailTemplateEnum.REFUND_COMPLETED,params);

        baseCommonService.addLog("订单请求取消通知，username： " +customer.getCustomerName() , CommonConstant.LOG_TYPE_2, 2);
    }

    private Locale getUserLanguage(){
        // TODO: 用户使用语言，暂时写死，国际化对应时一起修正
        String lang = "ja";
        Locale locale = switch (lang) {
            case "zh" -> Locale.SIMPLIFIED_CHINESE;
            case "tw" -> Locale.TRADITIONAL_CHINESE;
            case "ja" -> Locale.JAPANESE;
            default -> Locale.ENGLISH;
        };
        return locale;
    }
}

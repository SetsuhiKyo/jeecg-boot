package org.jeecg.common.constant.enums;

import org.jeecg.common.util.oConvertUtils;

/**
 * 邮件html模板配置地址美剧
 *
 * @author: liusq
 * @Date: 2023-10-13
 */
public enum EmailTemplateEnum {
    /**
     * 流程催办
     */
    BPM_CUIBAN_EMAIL("bpm_cuiban_email", "/templates/email/bpm_cuiban_email.ftl"),
    /**
     * 流程新任务
     */
    BPM_NEW_TASK_EMAIL("bpm_new_task_email", "/templates/email/bpm_new_task_email.ftl"),
    /**
     * 表单新增记录
     */
    DESFORM_NEW_DATA_EMAIL("desform_new_data_email", "/templates/email/desform_new_data_email.ftl"),

    /**
     * 用户注册成功（邮件内埋入激活链接）
     */
    CUSTOMER_ACCOUNT_ACTIVATE("customer_account_activate","/templates/email/customer_account_activate.ftl"),

    /**
     * 订单已受理（预约成功）
     */
    ORDER_CREATE("01_order_created","/templates/email/01_order_created.ftl"),
    /**
     * 订单已确认（等待付款）
     */
    ORDER_CONFIREMED("02_order_confirmed","/templates/email/02_order_confirmed.ftl"),
    /**
     * 提示付款通知
     */
    PAYMENT_REMINDER("03_payment_reminder","/templates/email/03_payment_reminder.ftl"),
    /**
     * 已付款确认
     */
    PAYMENT_RECEIVED("04_payment_received","/templates/email/04_payment_received.ftl"),
    /**
     * 服务即将开始通知
     */
    SERVICE_REMINDER("05_service_reminder","/templates/email/05_service_reminder.ftl"),
    /**
     * 服务完成通知
     */
    SERVICE_COMPLETED("06_service_completed","/templates/email/06_service_completed.ftl"),
    /**
     * 订单取消通知
     */
    ORDER_CANCELLED("07_order_cancelled","/templates/email/07_order_cancelled.ftl"),
    /**
     * 结算申请通知（司机公司）
     */
    SETTLEMENT_REQUEST("08_settlement_request","/templates/email/08_settlement_request.ftl"),
    /**
     * 结算完成通知（司机公司）
     */
    SETTLEMENT_COMPLETED("09_settlement_completed","/templates/email/09_settlement_completed.ftl"),
    /**
     * 退款完成通知
     */
    REFUND_COMPLETED("10_refund_completed","/templates/email/10_refund_completed.ftl"),
    /**
     * 订单请求取消通知
     */
    REQUEST_CANCEL("11_request_cancel","/templates/email/11_request_cancel.ftl"),
    ;

    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板地址
     */
    private String url;

    EmailTemplateEnum(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static EmailTemplateEnum getByName(String name) {
        if (oConvertUtils.isEmpty(name)) {
            return null;
        }
        for (EmailTemplateEnum val : values()) {
            if (val.getName().equals(name)) {
                return val;
            }
        }
        return null;
    }
}

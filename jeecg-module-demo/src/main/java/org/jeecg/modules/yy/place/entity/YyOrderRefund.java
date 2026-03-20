package org.jeecg.modules.yy.place.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 退款表
 * @Author: jeecg-boot
 * @Date:   2026-03-03
 * @Version: V1.0
 */
@Data
@TableName("yy_order_refund")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_order_refund对象", description="退款表")
public class YyOrderRefund implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**订单ID*/
	@Excel(name = "订单ID", width = 15)
    @ApiModelProperty(value = "订单ID")
    private java.lang.String orderId;
	/**退款状态*/
	@Excel(name = "退款状态", width = 15, dicCode = "refund_status")
	@Dict(dicCode = "refund_status")
    @ApiModelProperty(value = "退款状态")
    private java.lang.String refundSts;
	/**退款金额*/
	@Excel(name = "退款金额", width = 15)
    @ApiModelProperty(value = "退款金额")
    private java.math.BigDecimal refundAmount;
	/**退款时间*/
	@Excel(name = "退款时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "退款时间")
    private java.util.Date refundTime;
	/**退款流水号*/
	@Excel(name = "退款流水号", width = 15)
    @ApiModelProperty(value = "退款流水号")
    private java.lang.String refundReferenceNo;
	/**退款确认人*/
	@Excel(name = "退款确认人", width = 15)
    @ApiModelProperty(value = "退款确认人")
    private java.lang.String refundOperator;
	/**退款备考*/
	@Excel(name = "退款备考", width = 15)
    @ApiModelProperty(value = "退款备考")
    private java.lang.String refundRemark;
}

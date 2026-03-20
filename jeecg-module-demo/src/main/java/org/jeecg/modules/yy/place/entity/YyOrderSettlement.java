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
 * @Description: 结算表
 * @Author: jeecg-boot
 * @Date:   2026-03-03
 * @Version: V1.0
 */
@Data
@TableName("yy_order_settlement")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_order_settlement对象", description="结算表")
public class YyOrderSettlement implements Serializable {
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
	/**结算状态*/
	@Excel(name = "结算状态", width = 15, dicCode = "settlement_status")
	@Dict(dicCode = "settlement_status")
    @ApiModelProperty(value = "结算状态")
    private java.lang.String settlementSts;
	/**结算申请时间*/
	@Excel(name = "结算申请时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结算申请时间")
    private java.util.Date settlementRequestTime;
	/**结算转账时间*/
	@Excel(name = "结算转账时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结算转账时间")
    private java.util.Date settlementTransferTime;
	/**到账确认时间*/
	@Excel(name = "到账确认时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "到账确认时间")
    private java.util.Date settlementConfirmTime;
	/**结算金额*/
	@Excel(name = "结算金额", width = 15)
    @ApiModelProperty(value = "结算金额")
    private java.math.BigDecimal settlementAmount;
	/**转账流水号*/
	@Excel(name = "转账流水号", width = 15)
    @ApiModelProperty(value = "转账流水号")
    private java.lang.String settlementReferenceNo;
	/**到账确认人*/
	@Excel(name = "到账确认人", width = 15)
    @ApiModelProperty(value = "到账确认人")
    private java.lang.String settlementConfirmBy;
	/**结算备注*/
	@Excel(name = "结算备注", width = 15)
    @ApiModelProperty(value = "结算备注")
    private java.lang.String settlementRemark;
}

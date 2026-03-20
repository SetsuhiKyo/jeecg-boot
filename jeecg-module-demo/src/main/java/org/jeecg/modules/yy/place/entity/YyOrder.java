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
 * @Description: 车务订单
 * @Author: jeecg-boot
 * @Date:   2025-12-04
 * @Version: V1.0
 */
@Data
@TableName("yy_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_order对象", description="车务订单")
public class YyOrder implements Serializable {
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
	/**顾客用户ID*/
	@Excel(name = "顾客用户ID", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "顾客用户ID")
    private java.lang.String customerId;
	/**订单种别*/
	@Excel(name = "订单种别", width = 15, dicCode = "order_type")
	@Dict(dicCode = "order_type")
    @ApiModelProperty(value = "订单种别")
    private java.lang.String orderType;
	/**订单区分*/
	@Excel(name = "订单区分", width = 15, dicCode = "order_kbn")
	@Dict(dicCode = "order_kbn")
    @ApiModelProperty(value = "订单区分")
    private java.lang.String orderKbn;
	/**订单状态*/
	@Excel(name = "订单状态", width = 15, dicCode = "order_status")
	@Dict(dicCode = "order_status")
    @ApiModelProperty(value = "订单状态")
    private java.lang.String orderSts;
	/**支付状态*/
	@Excel(name = "支付状态", width = 15, dicCode = "payment_status")
	@Dict(dicCode = "payment_status")
	@ApiModelProperty(value = "支付状态")
	private java.lang.String paymentSts;
	/**订单名称*/
	@Excel(name = "订单名称", width = 15)
    @ApiModelProperty(value = "订单名称")
    private java.lang.String orderName;
	/**行程ID*/
	@Excel(name = "行程ID", width = 15)
    @ApiModelProperty(value = "行程ID")
    private java.lang.String routeId;
	/**开始日时*/
	@Excel(name = "开始日时", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始日时")
    private java.util.Date startTime;
	/**上车地点ID*/
	@Excel(name = "上车地点ID", width = 15)
    @ApiModelProperty(value = "上车地点ID")
    private java.lang.String pickupPlaceId;
	/**终了地点ID*/
	@Excel(name = "终了地点ID", width = 15)
    @ApiModelProperty(value = "终了地点ID")
    private java.lang.String dropPlaceId;
	/**航班号*/
	@Excel(name = "航班号", width = 15)
    @ApiModelProperty(value = "航班号")
    private java.lang.String flightNo;
	/**返回日时*/
	@Excel(name = "返回日时", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "返回日时")
    private java.util.Date returnTime;
	/**返回上车地点ID*/
	@Excel(name = "返回上车地点ID", width = 15)
    @ApiModelProperty(value = "返回上车地点ID")
    private java.lang.String rtnPickupPlaceId;
	/**返回终了地点ID*/
	@Excel(name = "返回终了地点ID", width = 15)
    @ApiModelProperty(value = "返回终了地点ID")
    private java.lang.String rtnDropPlaceId;
	/**用车时间*/
	@Excel(name = "用车时间", width = 15)
    @ApiModelProperty(value = "用车时间")
    private java.lang.Integer useHours;
	/**订单距离*/
	@Excel(name = "订单距离", width = 15)
    @ApiModelProperty(value = "订单距离")
    private java.lang.Integer orderDistance;
	/**车辆种别*/
	@Excel(name = "车辆种别", width = 15)
    @ApiModelProperty(value = "车辆种别")
    private java.lang.String carCategory;
	/**代表者姓名*/
	@Excel(name = "代表者姓名", width = 15)
    @ApiModelProperty(value = "代表者姓名")
    private java.lang.String passengerName;
	/**成人人数*/
	@Excel(name = "成人人数", width = 15)
    @ApiModelProperty(value = "成人人数")
    private java.lang.Integer aldultNum;
	/**儿童人数*/
	@Excel(name = "儿童人数", width = 15)
    @ApiModelProperty(value = "儿童人数")
    private java.lang.Integer childNum;
	/**大件行李数*/
	@Excel(name = "大件行李数", width = 15)
    @ApiModelProperty(value = "大件行李数")
    private java.lang.Integer bigLuggageNum;
	/**小件行李数*/
	@Excel(name = "小件行李数", width = 15)
    @ApiModelProperty(value = "小件行李数")
    private java.lang.Integer smallLuggageNum;
	/**需要儿童座椅*/
	@Excel(name = "需要儿童座椅", width = 15, dicCode = "is_need")
	@Dict(dicCode = "is_need")
    @ApiModelProperty(value = "需要儿童座椅")
    private java.lang.String needChildSeat;
	/**需要婴儿座椅*/
	@Excel(name = "需要婴儿座椅", width = 15, dicCode = "is_need")
	@Dict(dicCode = "is_need")
    @ApiModelProperty(value = "需要婴儿座椅")
    private java.lang.String needBabySeat;
	/**需要协助值机*/
	@Excel(name = "需要协助值机", width = 15, dicCode = "is_need")
	@Dict(dicCode = "is_need")
    @ApiModelProperty(value = "需要协助值机")
    private java.lang.String needAirportCheckin;
	/**需要举牌接机*/
	@Excel(name = "需要举牌接机", width = 15, dicCode = "is_need")
	@Dict(dicCode = "is_need")
    @ApiModelProperty(value = "需要举牌接机")
    private java.lang.String needPickupInhall;
	/**需要协助入住*/
	@Excel(name = "需要协助入住", width = 15, dicCode = "is_need")
	@Dict(dicCode = "is_need")
    @ApiModelProperty(value = "需要协助入住")
    private java.lang.String needHotelCheckin;
	/**其它要求*/
	@Excel(name = "其它要求", width = 15)
    @ApiModelProperty(value = "其它要求")
    private java.lang.String needOther;
	/**最终服务价格*/
	@Excel(name = "最终服务价格", width = 15)
    @ApiModelProperty(value = "最终服务价格")
    private java.math.BigDecimal serverPrice;
	/**司导用户ID*/
	@Excel(name = "司导用户ID", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "司导用户ID")
    private java.lang.String driverId;
	/**分配车辆ID*/
	@Excel(name = "分配车辆ID", width = 15)
    @ApiModelProperty(value = "分配车辆ID")
    private java.lang.String carId;

	/**取消原因*/
	@Excel(name = "取消原因", width = 15, dicCode = "cancel_reason")
	@Dict(dicCode = "cancel_reason")
	@ApiModelProperty(value = "取消原因")
	private java.lang.String cancelReason;
	/**取消人*/
	@Excel(name = "取消人", width = 15)
	@ApiModelProperty(value = "取消人")
	private java.lang.String cancelBy;
	/**取消确认人*/
	@Excel(name = "取消确认人", width = 15)
	@ApiModelProperty(value = "取消确认人")
	private java.lang.String cancelConfirm;
}

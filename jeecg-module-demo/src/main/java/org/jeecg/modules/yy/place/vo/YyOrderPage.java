package org.jeecg.modules.yy.place.vo;

import java.util.List;
import org.jeecg.modules.yy.place.entity.YyOrder;
import org.jeecg.modules.yy.place.entity.YyOrderReview;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 车务订单
 * @Author: jeecg-boot
 * @Date:   2025-07-24
 * @Version: V1.0
 */
@Data
@ApiModel(value="yy_orderPage对象", description="车务订单")
public class YyOrderPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**顾客用户ID*/
	@Excel(name = "顾客用户ID", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@ApiModelProperty(value = "顾客用户ID")
    private String customerId;
	/**订单区分*/
	@Excel(name = "订单区分", width = 15)
	@ApiModelProperty(value = "订单区分")
    private String orderKbn;
	/**行程ID*/
	@Excel(name = "行程ID", width = 15)
	@ApiModelProperty(value = "行程ID")
    private String routeId;
	/**订单名称*/
	@Excel(name = "订单名称", width = 15)
	@ApiModelProperty(value = "订单名称")
    private String orderName;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
    private String orderSts;
	/**开始日时*/
	@Excel(name = "开始日时", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "开始日时")
    private Date startTime;
	/**结束日时*/
	@Excel(name = "结束日时", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "结束日时")
    private Date endTime;
	/**开始地点ID*/
	@Excel(name = "开始地点ID", width = 15)
	@ApiModelProperty(value = "开始地点ID")
    private String startPlaceId;
	/**终了地点ID*/
	@Excel(name = "终了地点ID", width = 15)
	@ApiModelProperty(value = "终了地点ID")
    private String endPlaceId;
	/**航班号*/
	@Excel(name = "航班号", width = 15)
	@ApiModelProperty(value = "航班号")
    private String flightNo;
	/**车辆种别*/
	@Excel(name = "车辆种别", width = 15)
	@ApiModelProperty(value = "车辆种别")
    private String carCategory;
	/**代表者姓名*/
	@Excel(name = "代表者姓名", width = 15)
	@ApiModelProperty(value = "代表者姓名")
    private String passengerName;
	/**代表者邮件*/
	@Excel(name = "代表者邮件", width = 15)
	@ApiModelProperty(value = "代表者邮件")
    private String passengerMail;
	/**代表者电话*/
	@Excel(name = "代表者电话", width = 15)
	@ApiModelProperty(value = "代表者电话")
    private String passengerPhone;
	/**成人人数*/
	@Excel(name = "成人人数", width = 15)
	@ApiModelProperty(value = "成人人数")
    private Integer aldultNum;
	/**儿童人数*/
	@Excel(name = "儿童人数", width = 15)
	@ApiModelProperty(value = "儿童人数")
    private Integer childNum;
	/**大件行李数*/
	@Excel(name = "大件行李数", width = 15)
	@ApiModelProperty(value = "大件行李数")
    private Integer bigLuggageNum;
	/**小件行李数*/
	@Excel(name = "小件行李数", width = 15)
	@ApiModelProperty(value = "小件行李数")
    private Integer smallLuggageNum;
	/**需要儿童座椅*/
	@Excel(name = "需要儿童座椅", width = 15)
	@ApiModelProperty(value = "需要儿童座椅")
    private String needChildSeat;
	/**需要婴儿座椅*/
	@Excel(name = "需要婴儿座椅", width = 15)
	@ApiModelProperty(value = "需要婴儿座椅")
    private String needBabySeat;
	/**需要协助值机*/
	@Excel(name = "需要协助值机", width = 15)
	@ApiModelProperty(value = "需要协助值机")
    private String needAirportCheckin;
	/**需要举牌接机*/
	@Excel(name = "需要举牌接机", width = 15)
	@ApiModelProperty(value = "需要举牌接机")
    private String needPickupInhall;
	/**需要协助入住*/
	@Excel(name = "需要协助入住", width = 15)
	@ApiModelProperty(value = "需要协助入住")
    private String needHotelCheckin;
	/**其它要求*/
	@Excel(name = "其它要求", width = 15)
	@ApiModelProperty(value = "其它要求")
    private String needOther;
	/**最终服务价格*/
	@Excel(name = "最终服务价格", width = 15)
	@ApiModelProperty(value = "最终服务价格")
    private java.math.BigDecimal serverPrice;
	/**司导用户ID*/
	@Excel(name = "司导用户ID", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@ApiModelProperty(value = "司导用户ID")
    private String driverId;
	/**分配车辆ID*/
	@Excel(name = "分配车辆ID", width = 15)
	@ApiModelProperty(value = "分配车辆ID")
    private String carId;

	@ExcelCollection(name="用户评价")
	@ApiModelProperty(value = "用户评价")
	private List<YyOrderReview> yyOrderReviewList;

}

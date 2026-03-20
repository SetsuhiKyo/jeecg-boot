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
 * @Description: 優惠情報
 * @Author: jeecg-boot
 * @Date:   2026-01-26
 * @Version: V1.0
 */
@Data
@TableName("yy_discount")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_discount对象", description="優惠情報")
public class YyDiscount implements Serializable {
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
	/**优惠简体名称*/
	@Excel(name = "优惠简体名称", width = 15)
    @ApiModelProperty(value = "优惠简体名称")
    private java.lang.String discountCn;
	/**优惠繁体名称*/
	@Excel(name = "优惠繁体名称", width = 15)
    @ApiModelProperty(value = "优惠繁体名称")
    private java.lang.String discountTw;
	/**优惠英文名称*/
	@Excel(name = "优惠英文名称", width = 15)
    @ApiModelProperty(value = "优惠英文名称")
    private java.lang.String discountEn;
	/**优惠日文名称*/
	@Excel(name = "优惠日文名称", width = 15)
    @ApiModelProperty(value = "优惠日文名称")
    private java.lang.String discountJp;
	/**优惠种别*/
	@Excel(name = "优惠种别", width = 15, dicCode = "discount_type")
	@Dict(dicCode = "discount_type")
    @ApiModelProperty(value = "优惠种别")
    private java.lang.String discountType;
	/**优惠区分*/
	@Excel(name = "优惠区分", width = 15, dicCode = "discount_kbn")
	@Dict(dicCode = "discount_kbn")
    @ApiModelProperty(value = "优惠区分")
    private java.lang.String discountKbn;
	/**优惠金额*/
	@Excel(name = "优惠金额", width = 15)
    @ApiModelProperty(value = "优惠金额")
    private java.math.BigDecimal discountNum;
	/**优惠率*/
	@Excel(name = "优惠率", width = 15)
    @ApiModelProperty(value = "优惠率")
    private java.lang.Integer discountRate;
	/**开始日时*/
	@Excel(name = "开始日时", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始日时")
    private java.util.Date startDate;
	/**终了日时*/
	@Excel(name = "终了日时", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "终了日时")
    private java.util.Date endDate;
	/**优惠达成条件*/
	@Excel(name = "优惠达成条件", width = 15)
    @ApiModelProperty(value = "优惠达成条件")
    private java.lang.String discountCon;
}

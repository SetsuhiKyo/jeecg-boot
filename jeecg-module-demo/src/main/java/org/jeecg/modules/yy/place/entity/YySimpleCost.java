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
 * @Description: 简易成本
 * @Author: jeecg-boot
 * @Date:   2026-02-23
 * @Version: V1.0
 */
@Data
@TableName("yy_simple_cost")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_simple_cost对象", description="简易成本")
public class YySimpleCost implements Serializable {
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
	/**成本计算种别*/
	@Excel(name = "成本计算种别", width = 15, dicCode = "cal_cost_type")
	@Dict(dicCode = "cal_cost_type")
    @ApiModelProperty(value = "成本计算种别")
    private java.lang.String calCostType;
	/**成本计算区分*/
	@Excel(name = "成本计算区分", width = 15, dicCode = "cal_cost_kbn")
	@Dict(dicCode = "cal_cost_kbn")
    @ApiModelProperty(value = "成本计算区分")
    private java.lang.String calCostKubun;
	/**车辆种别*/
	@Excel(name = "车辆种别", width = 15, dicCode = "car_categories")
	@Dict(dicCode = "car_categories")
    @ApiModelProperty(value = "车辆种别")
    private java.lang.String carType;
	/**简易成本*/
	@Excel(name = "简易成本", width = 15)
    @ApiModelProperty(value = "简易成本")
    private java.math.BigDecimal calCostValue;
}

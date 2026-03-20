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
 * @Description: 车辆情报
 * @Author: jeecg-boot
 * @Date:   2025-07-24
 * @Version: V1.0
 */
@Data
@TableName("yy_cars")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_cars对象", description="车辆情报")
public class YyCars implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
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
	/**司导用户ID*/
	@Excel(name = "司导用户ID", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "司导用户ID")
    private String userId;
	/**种别*/
	@Excel(name = "种别", width = 15, dicCode = "car_categories")
	@Dict(dicCode = "car_categories")
    @ApiModelProperty(value = "种别")
    private String carCategory;
	/**年式*/
	@Excel(name = "年式", width = 15)
    @ApiModelProperty(value = "年式")
    private String carMadeYear;
	/**颜色*/
	@Excel(name = "颜色", width = 15)
    @ApiModelProperty(value = "颜色")
    private String carColor;
	/**车牌*/
	@Excel(name = "车牌", width = 15)
    @ApiModelProperty(value = "车牌")
    private String carNo;
	/**图片1*/
	@Excel(name = "图片1", width = 15)
    @ApiModelProperty(value = "图片1")
    private String carImg1;
	/**图片2*/
	@Excel(name = "图片2", width = 15)
    @ApiModelProperty(value = "图片2")
    private String carImg2;
	/**图片3*/
	@Excel(name = "图片3", width = 15)
    @ApiModelProperty(value = "图片3")
    private String carImg3;
	/**图片4*/
	@Excel(name = "图片4", width = 15)
    @ApiModelProperty(value = "图片4")
    private String carImg4;
	/**图片5*/
	@Excel(name = "图片5", width = 15)
    @ApiModelProperty(value = "图片5")
    private String carImg5;
	/**图片6*/
	@Excel(name = "图片6", width = 15)
    @ApiModelProperty(value = "图片6")
    private String carImg6;
	/**图片7*/
	@Excel(name = "图片7", width = 15)
    @ApiModelProperty(value = "图片7")
    private String carImg7;
	/**图片8*/
	@Excel(name = "图片8", width = 15)
    @ApiModelProperty(value = "图片8")
    private String carImg8;
}

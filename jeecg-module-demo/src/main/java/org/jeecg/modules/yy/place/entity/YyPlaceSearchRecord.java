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
 * @Description: 地点采集记录
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
@Data
@TableName("yy_place_search_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_place_search_record对象", description="地点采集记录")
public class YyPlaceSearchRecord implements Serializable {
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
	/**中心维度*/
	@Excel(name = "中心维度", width = 15)
    @ApiModelProperty(value = "中心维度")
    private java.lang.Double latitude;
	/**中心经度*/
	@Excel(name = "中心经度", width = 15)
    @ApiModelProperty(value = "中心经度")
    private java.lang.Double longitude;
	/**采集半径*/
	@Excel(name = "采集半径", width = 15, dicCode = "place_get_radius")
	@Dict(dicCode = "place_get_radius")
    @ApiModelProperty(value = "采集半径")
    private java.lang.String radius;
	/**网格维数*/
	@Excel(name = "网格维数", width = 15, dicCode = "place_get_grids")
	@Dict(dicCode = "place_get_grids")
    @ApiModelProperty(value = "网格维数")
    private java.lang.String gridNums;
	/**Google主类型*/
	@Excel(name = "Google主类型", width = 15)
    @ApiModelProperty(value = "Google主类型")
    private java.lang.String primaryType;
	/**用戶評價*/
	@Excel(name = "用戶評價", width = 15)
    @ApiModelProperty(value = "用戶評價")
    private java.lang.Double userRating;
	/**用戶評價人數*/
	@Excel(name = "用戶評價人數", width = 15)
    @ApiModelProperty(value = "用戶評價人數")
    private java.lang.Integer userNums;
	/**采集结果*/
	@Excel(name = "采集结果", width = 15, dicCode = "place_get_rst")
	@Dict(dicCode = "place_get_rst")
    @ApiModelProperty(value = "采集结果")
    private java.lang.String placeSearchRst;
}

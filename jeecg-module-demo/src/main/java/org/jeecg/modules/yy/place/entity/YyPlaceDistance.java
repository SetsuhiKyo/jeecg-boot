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
 * @Description: 距离情报
 * @Author: jeecg-boot
 * @Date:   2026-01-18
 * @Version: V1.0
 */
@Data
@TableName("yy_place_distance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_place_distance对象", description="距离情报")
public class YyPlaceDistance implements Serializable {
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
	/**起始地点*/
	@Excel(name = "起始地点", width = 15)
    @ApiModelProperty(value = "起始地点")
    private java.lang.String startPlace;
	/**终了地点*/
	@Excel(name = "终了地点", width = 15)
    @ApiModelProperty(value = "终了地点")
    private java.lang.String endPlace;
	/**距离*/
	@Excel(name = "距离", width = 15)
    @ApiModelProperty(value = "距离")
    private java.lang.Integer distance;
	/**时间*/
	@Excel(name = "时间", width = 15)
    @ApiModelProperty(value = "时间")
    private java.lang.Integer duration;
	/**路线*/
	@Excel(name = "路线", width = 15)
    @ApiModelProperty(value = "路线")
    private java.lang.String polyline;
	/**来源*/
	@Excel(name = "来源", width = 15)
    @ApiModelProperty(value = "来源")
    private java.lang.String dataSource;
	/**热度*/
	@Excel(name = "热度", width = 15)
    @ApiModelProperty(value = "热度")
    private java.lang.Integer hitCount;
	/**热度最后更新*/
	@Excel(name = "热度最后更新", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "热度最后更新")
    private java.util.Date hitUpdatetime;
}

package org.jeecg.modules.yy.place.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
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
 * @Description: 行程明细经由地点
 * @Author: jeecg-boot
 * @Date:   2025-12-04
 * @Version: V1.0
 */
@Data
@TableName("yy_route_detail_places")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_route_detail_places对象", description="行程明细经由地点")
public class YyRouteDetailPlaces implements Serializable {
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
	/**行程ID*/
	@Excel(name = "行程ID", width = 15)
    @ApiModelProperty(value = "行程ID")
    private String routeId;
	/**行程明细ID*/
	@Excel(name = "行程明细ID", width = 15)
    @ApiModelProperty(value = "行程明细ID")
    private String detailId;
	/**经由支番*/
	@Excel(name = "经由支番", width = 15)
    @ApiModelProperty(value = "经由支番")
    private java.lang.Integer branchNo;
	/**到达时间*/
	@Excel(name = "到达时间", width = 20, format = "HH:mm")
    @ApiModelProperty(value = "到达时间")
    private String arriveTime;
	/**经由地点ID*/
	@Excel(name = "经由地点ID", width = 15)
    @ApiModelProperty(value = "经由地点ID")
    private String passPlaceId;

    @ApiModelProperty(value = "经由地点名称")
    private String passPlaceNm;
}

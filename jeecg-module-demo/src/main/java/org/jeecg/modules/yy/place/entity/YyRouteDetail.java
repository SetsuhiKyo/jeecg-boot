package org.jeecg.modules.yy.place.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 行程明细
 * @Author: jeecg-boot
 * @Date:   2025-12-03
 * @Version: V1.0
 */
@Data
@TableName("yy_route_detail")
@ApiModel(value="yy_route_detail对象", description="行程明细")
public class YyRouteDetail implements Serializable {
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
	/**行程ID*/
    @ApiModelProperty(value = "行程ID")
    private java.lang.String routeId;
	/**明细支番*/
	@Excel(name = "明细支番", width = 15)
    @ApiModelProperty(value = "明细支番")
    private java.lang.Integer detailBranchNo;
	/**明细标题日文*/
	@Excel(name = "明细标题日文", width = 15)
    @ApiModelProperty(value = "明细标题日文")
    private java.lang.String detailTitleJp;
	/**明细标题简体*/
	@Excel(name = "明细标题简体", width = 15)
    @ApiModelProperty(value = "明细标题简体")
    private java.lang.String detailTitleZh;
	/**明细标题繁体*/
	@Excel(name = "明细标题繁体", width = 15)
    @ApiModelProperty(value = "明细标题繁体")
    private java.lang.String detailTitleTw;
	/**明细标题英文*/
	@Excel(name = "明细标题英文", width = 15)
    @ApiModelProperty(value = "明细标题英文")
    private java.lang.String detailTitleEn;
}

package org.jeecg.modules.yy.place.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 行程情报
 * @Author: jeecg-boot
 * @Date:   2025-12-03
 * @Version: V1.0
 */
@Data
@TableName("yy_route")
@ApiModel(value="yy_route对象", description="行程情报")
public class YyRoute implements Serializable {
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
	/**代表图片URL*/
    @Excel(name = "代表图片URL", width = 15)
    @ApiModelProperty(value = "代表图片URL")
    private java.lang.String picUrl;
	/**标题日文*/
    @Excel(name = "标题日文", width = 15)
    @ApiModelProperty(value = "标题日文")
    private java.lang.String routeTitleJp;
	/**标题简体*/
    @Excel(name = "标题简体", width = 15)
    @ApiModelProperty(value = "标题简体")
    private java.lang.String routeTitleZh;
	/**标题繁体*/
    @Excel(name = "标题繁体", width = 15)
    @ApiModelProperty(value = "标题繁体")
    private java.lang.String routeTitleTw;
	/**标题英文*/
    @Excel(name = "标题英文", width = 15)
    @ApiModelProperty(value = "标题英文")
    private java.lang.String routeTitleEn;
    /**推荐行程*/
    @Excel(name = "推荐行程", width = 15)
    @Dict(dicCode = "place_get_obj")
    @ApiModelProperty(value = "推荐行程")
    private java.lang.String recommendFlg;
    /**行程总距离*/
    @Excel(name = "行程总距离", width = 15)
    @ApiModelProperty(value = "行程总距离")
    private java.lang.Integer totalDistance;
    private java.lang.String favoriteFlg;
}

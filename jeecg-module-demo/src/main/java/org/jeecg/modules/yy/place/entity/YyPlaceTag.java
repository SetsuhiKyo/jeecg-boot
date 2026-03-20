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
 * @Description: 地点标签
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
@Data
@TableName("yy_place_tag")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_place_tag对象", description="地点标签")
public class YyPlaceTag implements Serializable {
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
	/**Google主类型*/
	@Excel(name = "Google主类型", width = 15)
    @ApiModelProperty(value = "Google主类型")
    private java.lang.String primaryType;
	/**标签编码*/
	@Excel(name = "标签编码", width = 15)
    @ApiModelProperty(value = "标签编码")
    private java.lang.String tagCd;
	/**简体标签名*/
	@Excel(name = "简体标签名", width = 15)
    @ApiModelProperty(value = "简体标签名")
    private java.lang.String tagNameZh;
	/**繁体标签名*/
	@Excel(name = "繁体标签名", width = 15)
    @ApiModelProperty(value = "繁体标签名")
    private java.lang.String tagNameTw;
	/**英文标签名*/
	@Excel(name = "英文标签名", width = 15)
    @ApiModelProperty(value = "英文标签名")
    private java.lang.String tagNameEn;
	/**日文标签名*/
	@Excel(name = "日文标签名", width = 15)
    @ApiModelProperty(value = "日文标签名")
    private java.lang.String tagNameJp;
	/**地点采集对象*/
	@Excel(name = "地点采集对象", width = 15, dicCode = "place_get_obj")
	@Dict(dicCode = "place_get_obj")
    @ApiModelProperty(value = "地点采集对象")
    private java.lang.String placeSearchFlg;
}

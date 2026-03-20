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
 * @Description: 地点情报
 * @Author: jeecg-boot
 * @Date:   2025-12-06
 * @Version: V1.0
 */
@Data
@TableName("yy_place")
@ApiModel(value="yy_place对象", description="地点情报")
public class YyPlace implements Serializable {
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
	/**Google地点ID*/
    @Excel(name = "Google地点ID", width = 15)
    @ApiModelProperty(value = "Google地点ID")
    private java.lang.String googlePladeId;
	/**类型*/
    @Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private java.lang.String googleTypes;
	/**主要类型*/
    @Excel(name = "主要类型", width = 15)
    @ApiModelProperty(value = "主要类型")
    private java.lang.String googlePrimaryType;
	/**简体名称*/
    @Excel(name = "简体名称", width = 15)
    @ApiModelProperty(value = "简体名称")
    private java.lang.String nameZh;
	/**繁体名称*/
    @Excel(name = "繁体名称", width = 15)
    @ApiModelProperty(value = "繁体名称")
    private java.lang.String nameTw;
	/**英文名称*/
    @Excel(name = "英文名称", width = 15)
    @ApiModelProperty(value = "英文名称")
    private java.lang.String nameEn;
	/**日文名称*/
    @Excel(name = "日文名称", width = 15)
    @ApiModelProperty(value = "日文名称")
    private java.lang.String nameJp;
	/**简体概要*/
    @Excel(name = "简体概要", width = 15)
    @ApiModelProperty(value = "简体概要")
    private java.lang.String summaryZh;
	/**繁体概要*/
    @Excel(name = "繁体概要", width = 15)
    @ApiModelProperty(value = "繁体概要")
    private java.lang.String summaryTw;
	/**英文概要*/
    @Excel(name = "英文概要", width = 15)
    @ApiModelProperty(value = "英文概要")
    private java.lang.String summaryEn;
	/**日文概要*/
    @Excel(name = "日文概要", width = 15)
    @ApiModelProperty(value = "日文概要")
    private java.lang.String summaryJp;
	/**简体地址*/
    @Excel(name = "简体地址", width = 15)
    @ApiModelProperty(value = "简体地址")
    private java.lang.String addressZh;
	/**繁体地址*/
    @Excel(name = "繁体地址", width = 15)
    @ApiModelProperty(value = "繁体地址")
    private java.lang.String addressTw;
	/**英文地址*/
    @Excel(name = "英文地址", width = 15)
    @ApiModelProperty(value = "英文地址")
    private java.lang.String addressEn;
	/**日文地址*/
    @Excel(name = "日文地址", width = 15)
    @ApiModelProperty(value = "日文地址")
    private java.lang.String addressJp;
	/**經度*/
    @Excel(name = "經度", width = 15)
    @ApiModelProperty(value = "經度")
    private java.lang.Double longitude;
	/**緯度*/
    @Excel(name = "緯度", width = 15)
    @ApiModelProperty(value = "緯度")
    private java.lang.Double latitude;
	/**花费时间(分)*/
    @Excel(name = "花费时间(分)", width = 15)
    @ApiModelProperty(value = "花费时间(分)")
    private java.lang.Integer costTime;
	/**电话*/
    @Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private java.lang.String telphoneNo;
	/**网站URL*/
    @Excel(name = "网站URL", width = 15)
    @ApiModelProperty(value = "网站URL")
    private java.lang.String siteUrl;
	/**是否代理预定*/
    @Excel(name = "是否代理预定", width = 15)
    @ApiModelProperty(value = "是否代理预定")
    private java.lang.String canProxy;
	/**地图URL*/
    @Excel(name = "地图URL", width = 15)
    @ApiModelProperty(value = "地图URL")
    private java.lang.String placeMapUrl;
	/**代表图URL*/
    @Excel(name = "代表图URL", width = 15)
    @ApiModelProperty(value = "代表图URL")
    private java.lang.String placeImgUrl;
	/**图片1*/
    @Excel(name = "图片1", width = 15)
    @ApiModelProperty(value = "图片1")
    private java.lang.String imgsUrl1;
	/**图片2*/
    @Excel(name = "图片2", width = 15)
    @ApiModelProperty(value = "图片2")
    private java.lang.String imgsUrl2;
	/**图片3*/
    @Excel(name = "图片3", width = 15)
    @ApiModelProperty(value = "图片3")
    private java.lang.String imgsUrl3;
	/**图片4*/
    @Excel(name = "图片4", width = 15)
    @ApiModelProperty(value = "图片4")
    private java.lang.String imgsUrl4;
	/**图片5*/
    @Excel(name = "图片5", width = 15)
    @ApiModelProperty(value = "图片5")
    private java.lang.String imgsUrl5;
	/**信息来源*/
    @Excel(name = "信息来源", width = 15)
    @ApiModelProperty(value = "信息来源")
    private java.lang.String infoSource;
}

package org.jeecg.modules.yy.place.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyPlaceTagRel;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 地点情报
 * @Author: jeecg-boot
 * @Date:   2025-07-04
 * @Version: V1.0
 */
@Data
@ApiModel(value="yy_placePage对象", description="地点情报")
public class YyPlacePage {

	/**主键*/
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
	/**Google地点ID*/
	@Excel(name = "Google地点ID", width = 15)
	@ApiModelProperty(value = "Google地点ID")
    private String googlePladeId;
	/**类型*/
	@Excel(name = "类型", width = 15)
	@ApiModelProperty(value = "类型")
    private String googleTypes;
	/**主要类型*/
	@Excel(name = "主要类型", width = 15)
	@ApiModelProperty(value = "主要类型")
    private String googlePrimaryType;
	/**简体名称*/
	@Excel(name = "简体名称", width = 15)
	@ApiModelProperty(value = "简体名称")
    private String nameZh;
	/**繁体名称*/
	@Excel(name = "繁体名称", width = 15)
	@ApiModelProperty(value = "繁体名称")
    private String nameTw;
	/**英文名称*/
	@Excel(name = "英文名称", width = 15)
	@ApiModelProperty(value = "英文名称")
    private String nameEn;
	/**日文名称*/
	@Excel(name = "日文名称", width = 15)
	@ApiModelProperty(value = "日文名称")
    private String nameJp;
	/**简体概要*/
	@Excel(name = "简体概要", width = 15)
	@ApiModelProperty(value = "简体概要")
    private String summaryZh;
	/**繁体概要*/
	@Excel(name = "繁体概要", width = 15)
	@ApiModelProperty(value = "繁体概要")
    private String summaryTw;
	/**英文概要*/
	@Excel(name = "英文概要", width = 15)
	@ApiModelProperty(value = "英文概要")
    private String summaryEn;
	/**日文概要*/
	@Excel(name = "日文概要", width = 15)
	@ApiModelProperty(value = "日文概要")
    private String summaryJp;
	/**简体详细*/
	@Excel(name = "简体详细", width = 15)
	@ApiModelProperty(value = "简体详细")
    private String detailinfoZh;
	/**繁体详细*/
	@Excel(name = "繁体详细", width = 15)
	@ApiModelProperty(value = "繁体详细")
    private String detailinfoTw;
	/**英文详细*/
	@Excel(name = "英文详细", width = 15)
	@ApiModelProperty(value = "英文详细")
    private String detailinfoEn;
	/**日文详细*/
	@Excel(name = "日文详细", width = 15)
	@ApiModelProperty(value = "日文详细")
    private String detailinfoJp;
	/**简体地址*/
	@Excel(name = "简体地址", width = 15)
	@ApiModelProperty(value = "简体地址")
    private String addressZh;
	/**繁体地址*/
	@Excel(name = "繁体地址", width = 15)
	@ApiModelProperty(value = "繁体地址")
    private String addressTw;
	/**英文地址*/
	@Excel(name = "英文地址", width = 15)
	@ApiModelProperty(value = "英文地址")
    private String addressEn;
	/**日文地址*/
	@Excel(name = "日文地址", width = 15)
	@ApiModelProperty(value = "日文地址")
    private String addressJp;
	/**緯度*/
	@Excel(name = "緯度", width = 15)
	@ApiModelProperty(value = "緯度")
    private Double latitude;
	/**經度*/
	@Excel(name = "經度", width = 15)
	@ApiModelProperty(value = "經度")
    private Double longitude;
	/**花费时间(分)*/
	@Excel(name = "花费时间(分)", width = 15)
	@ApiModelProperty(value = "花费时间(分)")
    private Integer costTime;
	/**电话*/
	@Excel(name = "电话", width = 15)
	@ApiModelProperty(value = "电话")
    private String telphoneNo;
	/**网站URL*/
	@Excel(name = "网站URL", width = 15)
	@ApiModelProperty(value = "网站URL")
    private String siteUrl;
	/**是否代理预定*/
	@Excel(name = "是否代理预定", width = 15)
	@ApiModelProperty(value = "是否代理预定")
    private String canProxy;
	/**地图URL*/
	@Excel(name = "地图URL", width = 15)
	@ApiModelProperty(value = "地图URL")
    private String placeMapUrl;
	/**代表图URL*/
	@Excel(name = "代表图URL", width = 15)
	@ApiModelProperty(value = "代表图URL")
    private String placeImgUrl;
	/**图片1*/
	@Excel(name = "图片1", width = 15)
	@ApiModelProperty(value = "图片1")
    private String imgsUrl1;
	/**图片2*/
	@Excel(name = "图片2", width = 15)
	@ApiModelProperty(value = "图片2")
    private String imgsUrl2;
	/**信息来源*/
	@Excel(name = "信息来源", width = 15)
	@ApiModelProperty(value = "信息来源")
    private String infoSource;
	/**图片3*/
	@Excel(name = "图片3", width = 15)
	@ApiModelProperty(value = "图片3")
    private String imgsUrl3;
	/**图片4*/
	@Excel(name = "图片4", width = 15)
	@ApiModelProperty(value = "图片4")
    private String imgsUrl4;
	/**图片5*/
	@Excel(name = "图片5", width = 15)
	@ApiModelProperty(value = "图片5")
    private String imgsUrl5;

	@ExcelCollection(name="地点标签关系")
	@ApiModelProperty(value = "地点标签关系")
	private List<YyPlaceTagRel> yyPlaceTagRelList;

}

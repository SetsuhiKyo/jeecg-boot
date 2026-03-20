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
 * @Description: 司导情报
 * @Author: jeecg-boot
 * @Date:   2026-01-31
 * @Version: V1.0
 */
@Data
@TableName("yy_driver")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_driver对象", description="司导情报")
public class YyDriver implements Serializable {
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
	/**登录帐号*/
	@Excel(name = "登录帐号", width = 15)
    @ApiModelProperty(value = "登录帐号")
    private java.lang.String driverName;
	/**密码*/
	@Excel(name = "密码", width = 15)
    @ApiModelProperty(value = "密码")
    private java.lang.String driverPwd;
	/**MD5密码盐*/
	@Excel(name = "MD5密码盐", width = 15)
    @ApiModelProperty(value = "MD5密码盐")
    private java.lang.String driverSalt;
	/**司导状态*/
	@Excel(name = "司导状态", width = 15)
    @ApiModelProperty(value = "司导状态")
    private java.lang.String driverSts;
	/**审查状态*/
	@Excel(name = "审查状态", width = 15)
    @ApiModelProperty(value = "审查状态")
    private java.lang.String reviewSts;
	/**在线状态*/
	@Excel(name = "在线状态", width = 15)
    @ApiModelProperty(value = "在线状态")
    private java.lang.String onlineSts;
	/**真是姓名*/
	@Excel(name = "真是姓名", width = 15)
    @ApiModelProperty(value = "真是姓名")
    private java.lang.String realName;
	/**国家*/
	@Excel(name = "国家", width = 15)
    @ApiModelProperty(value = "国家")
    private java.lang.String countryCd;
	/**昵称*/
	@Excel(name = "昵称", width = 15)
    @ApiModelProperty(value = "昵称")
    private java.lang.String nickName;
	/**货币*/
	@Excel(name = "货币", width = 15)
    @ApiModelProperty(value = "货币")
    private java.lang.String currencyCd;
	/**语言*/
	@Excel(name = "语言", width = 15)
    @ApiModelProperty(value = "语言")
    private java.lang.String langCd;
	/**邮件*/
	@Excel(name = "邮件", width = 15)
    @ApiModelProperty(value = "邮件")
    private java.lang.String email;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private java.lang.String phoneNo;
	/**微信*/
	@Excel(name = "微信", width = 15)
    @ApiModelProperty(value = "微信")
    private java.lang.String webchatId;
	/**驾照编号*/
	@Excel(name = "驾照编号", width = 15)
    @ApiModelProperty(value = "驾照编号")
    private java.lang.String driverLicense;
	/**Line*/
	@Excel(name = "Line", width = 15)
    @ApiModelProperty(value = "Line")
    private java.lang.String lineId;
	/**驾照有效期限*/
	@Excel(name = "驾照有效期限", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "驾照有效期限")
    private java.util.Date licenseValidDate;
	/**Facebook*/
	@Excel(name = "Facebook", width = 15)
    @ApiModelProperty(value = "Facebook")
    private java.lang.String facebookId;
	/**车辆ID*/
	@Excel(name = "车辆ID", width = 15)
    @ApiModelProperty(value = "车辆ID")
    private java.lang.String carId;
	/**Twitter*/
	@Excel(name = "Twitter", width = 15)
    @ApiModelProperty(value = "Twitter")
    private java.lang.String twitterId;
	/**组织代码*/
	@Excel(name = "组织代码", width = 15)
    @ApiModelProperty(value = "组织代码")
    private java.lang.String orgCd;
	/**Instagram*/
	@Excel(name = "Instagram", width = 15)
    @ApiModelProperty(value = "Instagram")
    private java.lang.String instagramId;
	/**介绍顾客ID*/
	@Excel(name = "介绍顾客ID", width = 15)
    @ApiModelProperty(value = "介绍顾客ID")
    private java.lang.String introduceId;
}

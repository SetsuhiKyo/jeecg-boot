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
 * @Description: 顾客情报
 * @Author: jeecg-boot
 * @Date:   2026-02-06
 * @Version: V1.0
 */
@Data
@TableName("yy_customer")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yy_customer对象", description="顾客情报")
public class YyCustomer implements Serializable {
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
    private java.lang.String customerName;
	/**密码*/
	@Excel(name = "密码", width = 15)
    @ApiModelProperty(value = "密码")
    private java.lang.String customerPwd;
	/**MD5密码盐*/
	@Excel(name = "MD5密码盐", width = 15)
    @ApiModelProperty(value = "MD5密码盐")
    private java.lang.String customerSalt;
	/**顾客状态*/
	@Excel(name = "顾客状态", width = 15)
    @ApiModelProperty(value = "顾客状态")
    private java.lang.String custormerSts;
	/**邮件检证*/
	@Excel(name = "邮件检证", width = 15)
    @ApiModelProperty(value = "邮件检证")
    private java.lang.String emailVerify;
	/**昵称*/
	@Excel(name = "昵称", width = 15)
    @ApiModelProperty(value = "昵称")
    private java.lang.String nickName;
	/**国家代码*/
	@Excel(name = "国家代码", width = 15)
    @ApiModelProperty(value = "国家代码")
    private java.lang.String countryCd;
	/**语言代码*/
	@Excel(name = "语言代码", width = 15)
    @ApiModelProperty(value = "语言代码")
    private java.lang.String langCd;
	/**货币代码*/
	@Excel(name = "货币代码", width = 15)
    @ApiModelProperty(value = "货币代码")
    private java.lang.String currencyCd;
	/**邮件*/
	@Excel(name = "邮件", width = 15)
    @ApiModelProperty(value = "邮件")
    private java.lang.String email;
	/**国家区域号*/
	@Excel(name = "国家区域号", width = 15)
    @ApiModelProperty(value = "国家区域号")
    private java.lang.String areaCd;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private java.lang.String phoneNo;
	/**微信*/
	@Excel(name = "微信", width = 15)
    @ApiModelProperty(value = "微信")
    private java.lang.String webchatId;
	/**Line*/
	@Excel(name = "Line", width = 15)
    @ApiModelProperty(value = "Line")
    private java.lang.String lineId;
	/**Facebook*/
	@Excel(name = "Facebook", width = 15)
    @ApiModelProperty(value = "Facebook")
    private java.lang.String facebookId;
	/**Twitter*/
	@Excel(name = "Twitter", width = 15)
    @ApiModelProperty(value = "Twitter")
    private java.lang.String twitterId;
	/**Instagram*/
	@Excel(name = "Instagram", width = 15)
    @ApiModelProperty(value = "Instagram")
    private java.lang.String instagramId;
	/**whatsapp*/
	@Excel(name = "whatsapp", width = 15)
    @ApiModelProperty(value = "whatsapp")
    private java.lang.String whatsappId;
	/**介绍顾客ID*/
	@Excel(name = "介绍顾客ID", width = 15)
    @ApiModelProperty(value = "介绍顾客ID")
    private java.lang.String introduceId;
}

package org.jeecg.modules.yy.place.controller;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import dm.jdbc.util.StringUtil;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.EmailTemplateEnum;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.shiro.IgnoreAuth;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.yy.place.entity.YyActivationToken;
import org.jeecg.modules.yy.place.entity.YyCustomer;
import org.jeecg.modules.yy.place.service.IYyActivationTokenService;
import org.jeecg.modules.yy.place.service.IYyCustomerService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 顾客情报
 * @Author: jeecg-boot
 * @Date:   2026-01-31
 * @Version: V1.0
 */
@Api(tags="顾客情报")
@RestController
@RequestMapping("/place/yyCustomer")
@Slf4j
public class YyCustomerController extends JeecgController<YyCustomer, IYyCustomerService> {
	@Autowired
	private IYyCustomerService yyCustomerService;
	 @Autowired
	 private IYyActivationTokenService yyActivationTokenService;
	 @Autowired
	 private BaseCommonService baseCommonService;
	 @Autowired
	 private ISysBaseAPI sysBaseApi;
	 @Resource(name = "mailMessageSource")
	 private MessageSource messageSource;

	/**
	 * 分页列表查询
	 *
	 * @param yyCustomer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "顾客情报-分页列表查询")
	@ApiOperation(value="顾客情报-分页列表查询", notes="顾客情报-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyCustomer>> queryPageList(YyCustomer yyCustomer,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        // 自定义查询规则
        Map<String, QueryRuleEnum> customeRuleMap = new HashMap<>();
        // 自定义多选的查询规则为：LIKE_WITH_OR
        customeRuleMap.put("custormerSts", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("emailVerify", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("countryCd", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("langCd", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("currencyCd", QueryRuleEnum.LIKE_WITH_OR);
        QueryWrapper<YyCustomer> queryWrapper = QueryGenerator.initQueryWrapper(yyCustomer, req.getParameterMap(),customeRuleMap);
		Page<YyCustomer> page = new Page<YyCustomer>(pageNo, pageSize);
		IPage<YyCustomer> pageList = yyCustomerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *  添加
	 *
	 * @param yyCustomer
	 * @return
	 */
	@AutoLog(value = "顾客情报-添加")
	@ApiOperation(value="顾客情报-添加", notes="顾客情报-添加")
//	@RequiresPermissions("place:yy_customer:add")
	@PostMapping(value = "/add")
	@IgnoreAuth
	public Result<String> add(@RequestBody YyCustomer yyCustomer) {

		Result<String> result = new Result<String>();
		try {
			// 校验邮件是否已经存在
			LambdaQueryWrapper<YyCustomer> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(YyCustomer::getEmail,yyCustomer.getEmail());
			YyCustomer customer = yyCustomerService.getOne(queryWrapper);
			if(customer != null) {
				result = yyCustomerService.checkUserIsEffective(customer);
				if(!result.isSuccess()){
					return result;
				}
			}

			// 登录顾客情报
			boolean ret = yyCustomerService.addCustomer(yyCustomer);
			if (ret) {
				result.success("添加成功！已向顾客发送验证邮件！");
			} else {
				result.error500("顾客添加失败！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	 /**
	  *  顾客激活
	  *
	  * @param jsonObject
	  * @return
	  */
	 @AutoLog(value = "顾客情报-激活")
	 @ApiOperation(value="顾客情报-激活", notes="顾客情报-激活")
//	 @RequiresPermissions("place:yy_customer:add")
	 @PostMapping(value = "/customerActivate")
	 @IgnoreAuth
	 public Result<String> customerActivate(@RequestBody JSONObject jsonObject) {

		 Result<String> result = new Result<String>();
		 try {
			 // 校验Token是否存在
			 LambdaQueryWrapper<YyActivationToken> queryToken = new LambdaQueryWrapper<>();
			 queryToken.eq(YyActivationToken::getToken,jsonObject.getString("token"));
			 YyActivationToken activationToken = yyActivationTokenService.getOne(queryToken);
			 if(activationToken == null) {
				 return result.success("ACTIVATION_TOKEN_INVALID");
			 }

			 // token 是否已 used
			 if(CommonConstant.STATUS_1.equals(activationToken.getUsed())) {
				 return result.success("ACTIVATION_TOKEN_USED");
			 }

			 // token 是否过期
			 if (activationToken.getTokenExpireat().isBefore(LocalDateTime.now())) {
				 return result.success("ACTIVATION_TOKEN_EXPIRED");
			 }

			 // customer 是否存在
			 LambdaQueryWrapper<YyCustomer> queryCustomer = new LambdaQueryWrapper<>();
			 queryCustomer.eq(YyCustomer::getId,activationToken.getCustomerId());
			 YyCustomer yyCustomer = yyCustomerService.getOne(queryCustomer);
			 if (yyCustomer == null) {
				 return result.success("CUSTOMER_NOT_FOUND");
			 }

			 // customer 是否已激活
			 if (CommonConstant.CUSTOMER_ACTIVED.equals(yyCustomer.getCustormerSts())) {
				 return result.success("CUSTOMER_ALREADY_ACTIVATED");
			 }

			 // 激活顾客账户
			 boolean ret = yyCustomerService.activateCustomer(yyCustomer,activationToken);

			 if (ret) {
				 result.success("SUCCESS");
			 } else {
				 result.error500("操作失败");
			 }
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 result.error500("操作失败");
		 }
		 return result;
	 }

	 /**
	 *  编辑
	 *
	 * @param yyCustomer
	 * @return
	 */
	@AutoLog(value = "顾客情报-编辑")
	@ApiOperation(value="顾客情报-编辑", notes="顾客情报-编辑")
	@RequiresPermissions("place:yy_customer:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YyCustomer yyCustomer) {
		YyCustomer dbCustomer = yyCustomerService.getById(yyCustomer.getId());
		if (dbCustomer != null) {
			String passwordEncode = PasswordUtil.encrypt(dbCustomer.getCustomerName(), yyCustomer.getCustomerPwd(), dbCustomer.getCustomerSalt());
			yyCustomer.setCustomerPwd(passwordEncode);
			yyCustomerService.updateById(yyCustomer);
			return Result.OK("编辑成功!");
		} else {
			return Result.OK("该用户的数据不存在!");
		}
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "顾客情报-通过id删除")
	@ApiOperation(value="顾客情报-通过id删除", notes="顾客情报-通过id删除")
	@RequiresPermissions("place:yy_customer:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yyCustomerService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "顾客情报-批量删除")
	@ApiOperation(value="顾客情报-批量删除", notes="顾客情报-批量删除")
	@RequiresPermissions("place:yy_customer:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yyCustomerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "顾客情报-通过id查询")
	@ApiOperation(value="顾客情报-通过id查询", notes="顾客情报-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YyCustomer> queryById(@RequestParam(name="id",required=true) String id) {
		YyCustomer yyCustomer = yyCustomerService.getById(id);
		if(yyCustomer==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yyCustomer);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yyCustomer
    */
    @RequiresPermissions("place:yy_customer:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyCustomer yyCustomer) {
        return super.exportXls(request, yyCustomer, YyCustomer.class, "顾客情报");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_customer:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YyCustomer.class);
    }

	 /**
	  * 更新密码
	  *
	  * @param userId
	  * @param pwd
	  * @return
	  */
	 @RequestMapping(value = "/changePwd", method = RequestMethod.GET)
	 public Result<?> importExcel(@RequestParam(name="userId",required=true) String userId,@RequestParam(name="pwd",required=true) String pwd) {
		 YyCustomer customer = yyCustomerService.getById(userId);
		 String passwordEncode = PasswordUtil.encrypt(customer.getCustomerName(), pwd, customer.getCustomerSalt());
		 customer.setCustomerPwd(passwordEncode);

		 boolean ret = yyCustomerService.saveOrUpdate(customer);

		 if (ret){
			 return Result.OK();
		 }
		 return Result.error("密码更新失败，请稍后重试！");
	 }

}

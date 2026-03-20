package org.jeecg.modules.yy.place.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.yy.place.entity.YyActivationToken;
import org.jeecg.modules.yy.place.entity.YyCustomer;
import org.jeecg.modules.yy.place.service.IYyActivationTokenService;

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
 * @Description: 账户激活
 * @Author: jeecg-boot
 * @Date:   2026-01-31
 * @Version: V1.0
 */
@Api(tags="账户激活")
@RestController
@RequestMapping("/place/yyActivationToken")
@Slf4j
public class YyActivationTokenController extends JeecgController<YyActivationToken, IYyActivationTokenService> {
	@Autowired
	private IYyActivationTokenService yyActivationTokenService;
	
	/**
	 * 分页列表查询
	 *
	 * @param yyActivationToken
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "账户激活-分页列表查询")
	@ApiOperation(value="账户激活-分页列表查询", notes="账户激活-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyActivationToken>> queryPageList(YyActivationToken yyActivationToken,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<YyActivationToken> queryWrapper = QueryGenerator.initQueryWrapper(yyActivationToken, req.getParameterMap());
		Page<YyActivationToken> page = new Page<YyActivationToken>(pageNo, pageSize);
		IPage<YyActivationToken> pageList = yyActivationTokenService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yyActivationToken
	 * @return
	 */
	@AutoLog(value = "账户激活-添加")
	@ApiOperation(value="账户激活-添加", notes="账户激活-添加")
	@RequiresPermissions("place:yy_activation_token:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody YyActivationToken yyActivationToken) {
		yyActivationTokenService.save(yyActivationToken);
		return Result.OK("添加成功！");
	}

	 /**
	  *   重新发送激活邮件
	  *
	  * @param token
	  * @return
	  */
	 @AutoLog(value = "重新发送激活邮件")
	 @ApiOperation(value="重新发送激活邮件", notes="重新发送激活邮件")
	 @PostMapping(value = "/reSendVerifyMail")
	 public Result<String> reSendVerifyMail(@RequestBody String token) {
		 Result<String> result = new Result<String>();
		 LambdaQueryWrapper<YyActivationToken> queryToken = new LambdaQueryWrapper<>();
		 queryToken.eq(YyActivationToken::getToken,token);
		 YyActivationToken activationToken = yyActivationTokenService.getOne(queryToken);
		 if (activationToken == null) {
			 result.error500("无效的token");
		 }
		 yyActivationTokenService.reSendVerifyMail(activationToken.getCustomerId());
		 return result.success("ACTIVATION_RESEND");
	 }

	/**
	 *  编辑
	 *
	 * @param yyActivationToken
	 * @return
	 */
	@AutoLog(value = "账户激活-编辑")
	@ApiOperation(value="账户激活-编辑", notes="账户激活-编辑")
	@RequiresPermissions("place:yy_activation_token:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YyActivationToken yyActivationToken) {
		yyActivationTokenService.updateById(yyActivationToken);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "账户激活-通过id删除")
	@ApiOperation(value="账户激活-通过id删除", notes="账户激活-通过id删除")
	@RequiresPermissions("place:yy_activation_token:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yyActivationTokenService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "账户激活-批量删除")
	@ApiOperation(value="账户激活-批量删除", notes="账户激活-批量删除")
	@RequiresPermissions("place:yy_activation_token:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yyActivationTokenService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "账户激活-通过id查询")
	@ApiOperation(value="账户激活-通过id查询", notes="账户激活-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YyActivationToken> queryById(@RequestParam(name="id",required=true) String id) {
		YyActivationToken yyActivationToken = yyActivationTokenService.getById(id);
		if(yyActivationToken==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yyActivationToken);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yyActivationToken
    */
    @RequiresPermissions("place:yy_activation_token:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyActivationToken yyActivationToken) {
        return super.exportXls(request, yyActivationToken, YyActivationToken.class, "账户激活");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_activation_token:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YyActivationToken.class);
    }

}

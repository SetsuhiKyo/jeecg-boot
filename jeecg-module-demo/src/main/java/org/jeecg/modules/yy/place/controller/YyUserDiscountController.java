package org.jeecg.modules.yy.place.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.shiro.IgnoreAuth;
import org.jeecg.modules.yy.place.entity.YyUserDiscount;
import org.jeecg.modules.yy.place.service.IYyUserDiscountService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.yy.place.vo.DiscountInfoView;
import org.jeecg.modules.yy.place.vo.RouteInfoView;
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
 * @Description: 用户优惠
 * @Author: jeecg-boot
 * @Date:   2026-01-26
 * @Version: V1.0
 */
@Api(tags="用户优惠")
@RestController
@RequestMapping("/place/yyUserDiscount")
@Slf4j
public class YyUserDiscountController extends JeecgController<YyUserDiscount, IYyUserDiscountService> {
	@Autowired
	private IYyUserDiscountService yyUserDiscountService;
	
	/**
	 * 分页列表查询
	 *
	 * @param yyUserDiscount
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "用户优惠-分页列表查询")
	@ApiOperation(value="用户优惠-分页列表查询", notes="用户优惠-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyUserDiscount>> queryPageList(YyUserDiscount yyUserDiscount,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<YyUserDiscount> queryWrapper = QueryGenerator.initQueryWrapper(yyUserDiscount, req.getParameterMap());
		Page<YyUserDiscount> page = new Page<YyUserDiscount>(pageNo, pageSize);
		IPage<YyUserDiscount> pageList = yyUserDiscountService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yyUserDiscount
	 * @return
	 */
	@AutoLog(value = "用户优惠-添加")
	@ApiOperation(value="用户优惠-添加", notes="用户优惠-添加")
	@RequiresPermissions("place:yy_user_discount:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody YyUserDiscount yyUserDiscount) {
		yyUserDiscountService.save(yyUserDiscount);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param yyUserDiscount
	 * @return
	 */
	@AutoLog(value = "用户优惠-编辑")
	@ApiOperation(value="用户优惠-编辑", notes="用户优惠-编辑")
	@RequiresPermissions("place:yy_user_discount:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YyUserDiscount yyUserDiscount) {
		yyUserDiscountService.updateById(yyUserDiscount);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户优惠-通过id删除")
	@ApiOperation(value="用户优惠-通过id删除", notes="用户优惠-通过id删除")
	@RequiresPermissions("place:yy_user_discount:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yyUserDiscountService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用户优惠-批量删除")
	@ApiOperation(value="用户优惠-批量删除", notes="用户优惠-批量删除")
	@RequiresPermissions("place:yy_user_discount:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yyUserDiscountService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "用户优惠-通过id查询")
	@ApiOperation(value="用户优惠-通过id查询", notes="用户优惠-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YyUserDiscount> queryById(@RequestParam(name="id",required=true) String id) {
		YyUserDiscount yyUserDiscount = yyUserDiscountService.getById(id);
		if(yyUserDiscount==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yyUserDiscount);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yyUserDiscount
    */
    @RequiresPermissions("place:yy_user_discount:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyUserDiscount yyUserDiscount) {
        return super.exportXls(request, yyUserDiscount, YyUserDiscount.class, "用户优惠");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_user_discount:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YyUserDiscount.class);
    }

	 /**
	  * 我的优惠
	  *
	  * @param userId
	  * @return
	  */
	 @ApiOperation(value="我的优惠", notes="我的优惠")
	 @GetMapping(value = "/getMyDiscount")
	 public Result<List<DiscountInfoView>> getMyDiscount(@RequestParam(name="userId",required=true) String userId) {

		 Result<List<DiscountInfoView>> result = new Result<>();
		 List<DiscountInfoView> discoutInfoViewList = new ArrayList<>();

		 try {
			 discoutInfoViewList = yyUserDiscountService.getMyDiscount(userId);
			 result.setResult(discoutInfoViewList);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.setSuccess(false);
			 result.setMessage("查询失败");
		 }
		 return result;
	 }
}

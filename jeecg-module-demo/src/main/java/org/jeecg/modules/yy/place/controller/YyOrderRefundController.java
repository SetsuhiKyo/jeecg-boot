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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.yy.place.entity.YyOrderRefund;
import org.jeecg.modules.yy.place.service.IYyOrderRefundService;

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
 * @Description: 退款表
 * @Author: jeecg-boot
 * @Date:   2026-03-03
 * @Version: V1.0
 */
@Api(tags="退款表")
@RestController
@RequestMapping("/place/yyOrderRefund")
@Slf4j
public class YyOrderRefundController extends JeecgController<YyOrderRefund, IYyOrderRefundService> {
	@Autowired
	private IYyOrderRefundService yyOrderRefundService;
	
	/**
	 * 分页列表查询
	 *
	 * @param yyOrderRefund
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "退款表-分页列表查询")
	@ApiOperation(value="退款表-分页列表查询", notes="退款表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyOrderRefund>> queryPageList(YyOrderRefund yyOrderRefund,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<YyOrderRefund> queryWrapper = QueryGenerator.initQueryWrapper(yyOrderRefund, req.getParameterMap());
		Page<YyOrderRefund> page = new Page<YyOrderRefund>(pageNo, pageSize);
		IPage<YyOrderRefund> pageList = yyOrderRefundService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yyOrderRefund
	 * @return
	 */
	@AutoLog(value = "退款表-添加")
	@ApiOperation(value="退款表-添加", notes="退款表-添加")
	@RequiresPermissions("place:yy_order_refund:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody YyOrderRefund yyOrderRefund) {
		yyOrderRefundService.save(yyOrderRefund);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param yyOrderRefund
	 * @return
	 */
	@AutoLog(value = "退款表-编辑")
	@ApiOperation(value="退款表-编辑", notes="退款表-编辑")
	@RequiresPermissions("place:yy_order_refund:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YyOrderRefund yyOrderRefund) {
		yyOrderRefundService.updateByRefundInfo(yyOrderRefund);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "退款表-通过id删除")
	@ApiOperation(value="退款表-通过id删除", notes="退款表-通过id删除")
	@RequiresPermissions("place:yy_order_refund:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yyOrderRefundService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "退款表-批量删除")
	@ApiOperation(value="退款表-批量删除", notes="退款表-批量删除")
	@RequiresPermissions("place:yy_order_refund:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yyOrderRefundService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "退款表-通过id查询")
	@ApiOperation(value="退款表-通过id查询", notes="退款表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YyOrderRefund> queryById(@RequestParam(name="id",required=true) String id) {
		YyOrderRefund yyOrderRefund = yyOrderRefundService.getById(id);
		if(yyOrderRefund==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yyOrderRefund);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yyOrderRefund
    */
    @RequiresPermissions("place:yy_order_refund:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyOrderRefund yyOrderRefund) {
        return super.exportXls(request, yyOrderRefund, YyOrderRefund.class, "退款表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_order_refund:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YyOrderRefund.class);
    }

}

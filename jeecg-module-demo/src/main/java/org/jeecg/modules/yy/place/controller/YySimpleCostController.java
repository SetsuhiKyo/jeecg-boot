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
import org.jeecg.modules.yy.place.entity.YySimpleCost;
import org.jeecg.modules.yy.place.service.IYySimpleCostService;

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
 * @Description: 简易成本
 * @Author: jeecg-boot
 * @Date:   2026-02-23
 * @Version: V1.0
 */
@Api(tags="简易成本")
@RestController
@RequestMapping("/place/yySimpleCost")
@Slf4j
public class YySimpleCostController extends JeecgController<YySimpleCost, IYySimpleCostService> {
	@Autowired
	private IYySimpleCostService yySimpleCostService;
	
	/**
	 * 分页列表查询
	 *
	 * @param yySimpleCost
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "简易成本-分页列表查询")
	@ApiOperation(value="简易成本-分页列表查询", notes="简易成本-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YySimpleCost>> queryPageList(YySimpleCost yySimpleCost,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<YySimpleCost> queryWrapper = QueryGenerator.initQueryWrapper(yySimpleCost, req.getParameterMap());
		Page<YySimpleCost> page = new Page<YySimpleCost>(pageNo, pageSize);
		IPage<YySimpleCost> pageList = yySimpleCostService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yySimpleCost
	 * @return
	 */
	@AutoLog(value = "简易成本-添加")
	@ApiOperation(value="简易成本-添加", notes="简易成本-添加")
	@RequiresPermissions("place:yy_simple_cost:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody YySimpleCost yySimpleCost) {
		yySimpleCostService.save(yySimpleCost);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param yySimpleCost
	 * @return
	 */
	@AutoLog(value = "简易成本-编辑")
	@ApiOperation(value="简易成本-编辑", notes="简易成本-编辑")
	@RequiresPermissions("place:yy_simple_cost:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YySimpleCost yySimpleCost) {
		yySimpleCostService.updateById(yySimpleCost);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "简易成本-通过id删除")
	@ApiOperation(value="简易成本-通过id删除", notes="简易成本-通过id删除")
	@RequiresPermissions("place:yy_simple_cost:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yySimpleCostService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "简易成本-批量删除")
	@ApiOperation(value="简易成本-批量删除", notes="简易成本-批量删除")
	@RequiresPermissions("place:yy_simple_cost:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yySimpleCostService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "简易成本-通过id查询")
	@ApiOperation(value="简易成本-通过id查询", notes="简易成本-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YySimpleCost> queryById(@RequestParam(name="id",required=true) String id) {
		YySimpleCost yySimpleCost = yySimpleCostService.getById(id);
		if(yySimpleCost==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yySimpleCost);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yySimpleCost
    */
    @RequiresPermissions("place:yy_simple_cost:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YySimpleCost yySimpleCost) {
        return super.exportXls(request, yySimpleCost, YySimpleCost.class, "简易成本");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_simple_cost:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YySimpleCost.class);
    }

}

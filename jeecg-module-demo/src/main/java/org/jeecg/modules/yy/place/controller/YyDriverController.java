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
import org.jeecg.modules.yy.place.entity.YyDriver;
import org.jeecg.modules.yy.place.service.IYyDriverService;

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
 * @Description: 司导情报
 * @Author: jeecg-boot
 * @Date:   2026-01-31
 * @Version: V1.0
 */
@Api(tags="司导情报")
@RestController
@RequestMapping("/place/yyDriver")
@Slf4j
public class YyDriverController extends JeecgController<YyDriver, IYyDriverService> {
	@Autowired
	private IYyDriverService yyDriverService;
	
	/**
	 * 分页列表查询
	 *
	 * @param yyDriver
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "司导情报-分页列表查询")
	@ApiOperation(value="司导情报-分页列表查询", notes="司导情报-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyDriver>> queryPageList(YyDriver yyDriver,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        // 自定义查询规则
        Map<String, QueryRuleEnum> customeRuleMap = new HashMap<>();
        // 自定义多选的查询规则为：LIKE_WITH_OR
        customeRuleMap.put("driverSts", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("countryCd", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("currencyCd", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("langCd", QueryRuleEnum.LIKE_WITH_OR);
        QueryWrapper<YyDriver> queryWrapper = QueryGenerator.initQueryWrapper(yyDriver, req.getParameterMap(),customeRuleMap);
		Page<YyDriver> page = new Page<YyDriver>(pageNo, pageSize);
		IPage<YyDriver> pageList = yyDriverService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yyDriver
	 * @return
	 */
	@AutoLog(value = "司导情报-添加")
	@ApiOperation(value="司导情报-添加", notes="司导情报-添加")
	@RequiresPermissions("place:yy_driver:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody YyDriver yyDriver) {
		yyDriverService.save(yyDriver);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param yyDriver
	 * @return
	 */
	@AutoLog(value = "司导情报-编辑")
	@ApiOperation(value="司导情报-编辑", notes="司导情报-编辑")
	@RequiresPermissions("place:yy_driver:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YyDriver yyDriver) {
		yyDriverService.updateById(yyDriver);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "司导情报-通过id删除")
	@ApiOperation(value="司导情报-通过id删除", notes="司导情报-通过id删除")
	@RequiresPermissions("place:yy_driver:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yyDriverService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "司导情报-批量删除")
	@ApiOperation(value="司导情报-批量删除", notes="司导情报-批量删除")
	@RequiresPermissions("place:yy_driver:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yyDriverService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "司导情报-通过id查询")
	@ApiOperation(value="司导情报-通过id查询", notes="司导情报-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YyDriver> queryById(@RequestParam(name="id",required=true) String id) {
		YyDriver yyDriver = yyDriverService.getById(id);
		if(yyDriver==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yyDriver);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yyDriver
    */
    @RequiresPermissions("place:yy_driver:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyDriver yyDriver) {
        return super.exportXls(request, yyDriver, YyDriver.class, "司导情报");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_driver:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YyDriver.class);
    }

}

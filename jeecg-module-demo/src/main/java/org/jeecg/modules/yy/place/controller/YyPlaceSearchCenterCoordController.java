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
import org.jeecg.modules.yy.place.entity.YyPlaceSearchCenterCoord;
import org.jeecg.modules.yy.place.service.IYyPlaceSearchCenterCoordService;

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
 * @Description: 地点采集中心坐标
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
@Api(tags="地点采集中心坐标")
@RestController
@RequestMapping("/place/yyPlaceSearchCenterCoord")
@Slf4j
public class YyPlaceSearchCenterCoordController extends JeecgController<YyPlaceSearchCenterCoord, IYyPlaceSearchCenterCoordService> {
	@Autowired
	private IYyPlaceSearchCenterCoordService yyPlaceSearchCenterCoordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param yyPlaceSearchCenterCoord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "地点采集中心坐标-分页列表查询")
	@ApiOperation(value="地点采集中心坐标-分页列表查询", notes="地点采集中心坐标-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyPlaceSearchCenterCoord>> queryPageList(YyPlaceSearchCenterCoord yyPlaceSearchCenterCoord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        // 自定义查询规则
        Map<String, QueryRuleEnum> customeRuleMap = new HashMap<>();
        // 自定义多选的查询规则为：LIKE_WITH_OR
        customeRuleMap.put("radius", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("gridNums", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("placeSearchFlg", QueryRuleEnum.LIKE_WITH_OR);
        QueryWrapper<YyPlaceSearchCenterCoord> queryWrapper = QueryGenerator.initQueryWrapper(yyPlaceSearchCenterCoord, req.getParameterMap(),customeRuleMap);
		Page<YyPlaceSearchCenterCoord> page = new Page<YyPlaceSearchCenterCoord>(pageNo, pageSize);
		IPage<YyPlaceSearchCenterCoord> pageList = yyPlaceSearchCenterCoordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yyPlaceSearchCenterCoord
	 * @return
	 */
	@AutoLog(value = "地点采集中心坐标-添加")
	@ApiOperation(value="地点采集中心坐标-添加", notes="地点采集中心坐标-添加")
	@RequiresPermissions("place:yy_place_search_center_coord:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody YyPlaceSearchCenterCoord yyPlaceSearchCenterCoord) {
		yyPlaceSearchCenterCoordService.save(yyPlaceSearchCenterCoord);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param yyPlaceSearchCenterCoord
	 * @return
	 */
	@AutoLog(value = "地点采集中心坐标-编辑")
	@ApiOperation(value="地点采集中心坐标-编辑", notes="地点采集中心坐标-编辑")
	@RequiresPermissions("place:yy_place_search_center_coord:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YyPlaceSearchCenterCoord yyPlaceSearchCenterCoord) {
		yyPlaceSearchCenterCoordService.updateById(yyPlaceSearchCenterCoord);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "地点采集中心坐标-通过id删除")
	@ApiOperation(value="地点采集中心坐标-通过id删除", notes="地点采集中心坐标-通过id删除")
	@RequiresPermissions("place:yy_place_search_center_coord:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yyPlaceSearchCenterCoordService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "地点采集中心坐标-批量删除")
	@ApiOperation(value="地点采集中心坐标-批量删除", notes="地点采集中心坐标-批量删除")
	@RequiresPermissions("place:yy_place_search_center_coord:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yyPlaceSearchCenterCoordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "地点采集中心坐标-通过id查询")
	@ApiOperation(value="地点采集中心坐标-通过id查询", notes="地点采集中心坐标-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YyPlaceSearchCenterCoord> queryById(@RequestParam(name="id",required=true) String id) {
		YyPlaceSearchCenterCoord yyPlaceSearchCenterCoord = yyPlaceSearchCenterCoordService.getById(id);
		if(yyPlaceSearchCenterCoord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yyPlaceSearchCenterCoord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yyPlaceSearchCenterCoord
    */
    @RequiresPermissions("place:yy_place_search_center_coord:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyPlaceSearchCenterCoord yyPlaceSearchCenterCoord) {
        return super.exportXls(request, yyPlaceSearchCenterCoord, YyPlaceSearchCenterCoord.class, "地点采集中心坐标");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_place_search_center_coord:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YyPlaceSearchCenterCoord.class);
    }

}

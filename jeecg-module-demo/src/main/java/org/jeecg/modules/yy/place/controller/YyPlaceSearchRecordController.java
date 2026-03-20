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
import org.jeecg.modules.yy.place.entity.YyPlaceSearchRecord;
import org.jeecg.modules.yy.place.service.IYyPlaceSearchRecordService;

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
 * @Description: 地点采集记录
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
@Api(tags="地点采集记录")
@RestController
@RequestMapping("/place/yyPlaceSearchRecord")
@Slf4j
public class YyPlaceSearchRecordController extends JeecgController<YyPlaceSearchRecord, IYyPlaceSearchRecordService> {
	@Autowired
	private IYyPlaceSearchRecordService yyPlaceSearchRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param yyPlaceSearchRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "地点采集记录-分页列表查询")
	@ApiOperation(value="地点采集记录-分页列表查询", notes="地点采集记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyPlaceSearchRecord>> queryPageList(YyPlaceSearchRecord yyPlaceSearchRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        // 自定义查询规则
        Map<String, QueryRuleEnum> customeRuleMap = new HashMap<>();
        // 自定义多选的查询规则为：LIKE_WITH_OR
        customeRuleMap.put("radius", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("gridNums", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("placeSearchRst", QueryRuleEnum.LIKE_WITH_OR);
        QueryWrapper<YyPlaceSearchRecord> queryWrapper = QueryGenerator.initQueryWrapper(yyPlaceSearchRecord, req.getParameterMap(),customeRuleMap);
		Page<YyPlaceSearchRecord> page = new Page<YyPlaceSearchRecord>(pageNo, pageSize);
		IPage<YyPlaceSearchRecord> pageList = yyPlaceSearchRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yyPlaceSearchRecord
	 * @return
	 */
	@AutoLog(value = "地点采集记录-添加")
	@ApiOperation(value="地点采集记录-添加", notes="地点采集记录-添加")
	@RequiresPermissions("place:yy_place_search_record:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody YyPlaceSearchRecord yyPlaceSearchRecord) {
		yyPlaceSearchRecordService.save(yyPlaceSearchRecord);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param yyPlaceSearchRecord
	 * @return
	 */
	@AutoLog(value = "地点采集记录-编辑")
	@ApiOperation(value="地点采集记录-编辑", notes="地点采集记录-编辑")
	@RequiresPermissions("place:yy_place_search_record:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YyPlaceSearchRecord yyPlaceSearchRecord) {
		yyPlaceSearchRecordService.updateById(yyPlaceSearchRecord);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "地点采集记录-通过id删除")
	@ApiOperation(value="地点采集记录-通过id删除", notes="地点采集记录-通过id删除")
	@RequiresPermissions("place:yy_place_search_record:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yyPlaceSearchRecordService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "地点采集记录-批量删除")
	@ApiOperation(value="地点采集记录-批量删除", notes="地点采集记录-批量删除")
	@RequiresPermissions("place:yy_place_search_record:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yyPlaceSearchRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "地点采集记录-通过id查询")
	@ApiOperation(value="地点采集记录-通过id查询", notes="地点采集记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YyPlaceSearchRecord> queryById(@RequestParam(name="id",required=true) String id) {
		YyPlaceSearchRecord yyPlaceSearchRecord = yyPlaceSearchRecordService.getById(id);
		if(yyPlaceSearchRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yyPlaceSearchRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yyPlaceSearchRecord
    */
    @RequiresPermissions("place:yy_place_search_record:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyPlaceSearchRecord yyPlaceSearchRecord) {
        return super.exportXls(request, yyPlaceSearchRecord, YyPlaceSearchRecord.class, "地点采集记录");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_place_search_record:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YyPlaceSearchRecord.class);
    }

}

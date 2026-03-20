package org.jeecg.modules.yy.place.controller;

import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.jeecg.config.shiro.IgnoreAuth;
import org.jeecg.config.yyi18n.LangContext;
import org.jeecg.modules.yy.place.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.yy.place.entity.YyRouteDetail;
import org.jeecg.modules.yy.place.entity.YyRoutePrice;
import org.jeecg.modules.yy.place.entity.YyRoute;
import org.jeecg.modules.yy.place.service.IYyRouteService;
import org.jeecg.modules.yy.place.service.IYyRouteDetailService;
import org.jeecg.modules.yy.place.service.IYyRoutePriceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;
import org.apache.shiro.authz.annotation.RequiresPermissions;
 /**
 * @Description: 行程情报
 * @Author: jeecg-boot
 * @Date:   2025-07-25
 * @Version: V1.0
 */
@Api(tags="行程情报")
@RestController
@RequestMapping("/place/yyRoute")
@Slf4j
public class YyRouteController extends JeecgController<YyRoute, IYyRouteService> {

	@Autowired
	private IYyRouteService yyRouteService;

	@Autowired
	private IYyRouteDetailService yyRouteDetailService;

	@Autowired
	private IYyRoutePriceService yyRoutePriceService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param yyRoute
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "行程情报-分页列表查询")
	@ApiOperation(value="行程情报-分页列表查询", notes="行程情报-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyRoute>> queryPageList(YyRoute yyRoute,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
      	QueryWrapper<YyRoute> queryWrapper = QueryGenerator.initQueryWrapper(yyRoute, req.getParameterMap());
		Page<YyRoute> page = new Page<YyRoute>(pageNo, pageSize);
		IPage<YyRoute> pageList = yyRouteService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
     *   添加
     * @param yyRoute
     * @return
     */
    @AutoLog(value = "行程情报-添加")
    @ApiOperation(value="行程情报-添加", notes="行程情报-添加")
    @RequiresPermissions("place:yy_route:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody YyRoute yyRoute) {
        yyRouteService.save(yyRoute);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param yyRoute
     * @return
     */
    @AutoLog(value = "行程情报-编辑")
    @ApiOperation(value="行程情报-编辑", notes="行程情报-编辑")
    @RequiresPermissions("place:yy_route:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody YyRoute yyRoute) {
        yyRouteService.updateById(yyRoute);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "行程情报-通过id删除")
    @ApiOperation(value="行程情报-通过id删除", notes="行程情报-通过id删除")
    @RequiresPermissions("place:yy_route:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        yyRouteService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "行程情报-批量删除")
    @ApiOperation(value="行程情报-批量删除", notes="行程情报-批量删除")
    @RequiresPermissions("place:yy_route:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.yyRouteService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequiresPermissions("place:yy_route:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyRoute yyRoute) {
        return super.exportXls(request, yyRoute, YyRoute.class, "行程情报");
    }

    /**
     * 导入
     * @return
     */
    @RequiresPermissions("place:yy_route:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YyRoute.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-行程明细-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	//@AutoLog(value = "行程明细-通过主表ID查询")
	@ApiOperation(value="行程明细-通过主表ID查询", notes="行程明细-通过主表ID查询")
	@GetMapping(value = "/listYyRouteDetailByMainId")
    public Result<IPage<YyRouteDetail>> listYyRouteDetailByMainId(YyRouteDetail yyRouteDetail,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<YyRouteDetail> queryWrapper = QueryGenerator.initQueryWrapper(yyRouteDetail, req.getParameterMap());
        Page<YyRouteDetail> page = new Page<YyRouteDetail>(pageNo, pageSize);
        IPage<YyRouteDetail> pageList = yyRouteDetailService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param yyRouteDetail
	 * @return
	 */
	@AutoLog(value = "行程明细-添加")
	@ApiOperation(value="行程明细-添加", notes="行程明细-添加")
	@PostMapping(value = "/addYyRouteDetail")
	public Result<String> addYyRouteDetail(@RequestBody YyRouteDetail yyRouteDetail) {
		yyRouteDetailService.save(yyRouteDetail);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param yyRouteDetail
	 * @return
	 */
	@AutoLog(value = "行程明细-编辑")
	@ApiOperation(value="行程明细-编辑", notes="行程明细-编辑")
	@RequestMapping(value = "/editYyRouteDetail", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> editYyRouteDetail(@RequestBody YyRouteDetail yyRouteDetail) {
		yyRouteDetailService.updateById(yyRouteDetail);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "行程明细-通过id删除")
	@ApiOperation(value="行程明细-通过id删除", notes="行程明细-通过id删除")
	@DeleteMapping(value = "/deleteYyRouteDetail")
	public Result<String> deleteYyRouteDetail(@RequestParam(name="id",required=true) String id) {
		yyRouteDetailService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "行程明细-批量删除")
	@ApiOperation(value="行程明细-批量删除", notes="行程明细-批量删除")
	@DeleteMapping(value = "/deleteBatchYyRouteDetail")
	public Result<String> deleteBatchYyRouteDetail(@RequestParam(name="ids",required=true) String ids) {
	    this.yyRouteDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportYyRouteDetail")
    public ModelAndView exportYyRouteDetail(HttpServletRequest request, YyRouteDetail yyRouteDetail) {
		 // Step.1 组装查询条件
		 QueryWrapper<YyRouteDetail> queryWrapper = QueryGenerator.initQueryWrapper(yyRouteDetail, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<YyRouteDetail> pageList = yyRouteDetailService.list(queryWrapper);
		 List<YyRouteDetail> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 //此处设置的filename无效,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.FILE_NAME, "行程明细");
		 mv.addObject(NormalExcelConstants.CLASS, YyRouteDetail.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("行程明细报表", "导出人:" + sysUser.getRealname(), "行程明细"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importYyRouteDetail/{mainId}")
    public Result<?> importYyRouteDetail(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
       // 获取上传文件对象
			 MultipartFile file = entity.getValue();
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<YyRouteDetail> list = ExcelImportUtil.importExcel(file.getInputStream(), YyRouteDetail.class, params);
				 for (YyRouteDetail temp : list) {
                    temp.setRouteId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 yyRouteDetailService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
			 } catch (Exception e) {
				 log.error(e.getMessage(), e);
				 return Result.error("文件导入失败:" + e.getMessage());
			 } finally {
				 try {
					 file.getInputStream().close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		 }
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-行程明细-end----------------------------------------------*/

    /*--------------------------------子表处理-行程价格-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	//@AutoLog(value = "行程价格-通过主表ID查询")
	@ApiOperation(value="行程价格-通过主表ID查询", notes="行程价格-通过主表ID查询")
	@GetMapping(value = "/listYyRoutePriceByMainId")
    public Result<IPage<YyRoutePrice>> listYyRoutePriceByMainId(YyRoutePrice yyRoutePrice,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<YyRoutePrice> queryWrapper = QueryGenerator.initQueryWrapper(yyRoutePrice, req.getParameterMap());
        Page<YyRoutePrice> page = new Page<YyRoutePrice>(pageNo, pageSize);
        IPage<YyRoutePrice> pageList = yyRoutePriceService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param yyRoutePrice
	 * @return
	 */
	@AutoLog(value = "行程价格-添加")
	@ApiOperation(value="行程价格-添加", notes="行程价格-添加")
	@PostMapping(value = "/addYyRoutePrice")
	public Result<String> addYyRoutePrice(@RequestBody YyRoutePrice yyRoutePrice) {
		yyRoutePriceService.save(yyRoutePrice);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param yyRoutePrice
	 * @return
	 */
	@AutoLog(value = "行程价格-编辑")
	@ApiOperation(value="行程价格-编辑", notes="行程价格-编辑")
	@RequestMapping(value = "/editYyRoutePrice", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> editYyRoutePrice(@RequestBody YyRoutePrice yyRoutePrice) {
		yyRoutePriceService.updateById(yyRoutePrice);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "行程价格-通过id删除")
	@ApiOperation(value="行程价格-通过id删除", notes="行程价格-通过id删除")
	@DeleteMapping(value = "/deleteYyRoutePrice")
	public Result<String> deleteYyRoutePrice(@RequestParam(name="id",required=true) String id) {
		yyRoutePriceService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "行程价格-批量删除")
	@ApiOperation(value="行程价格-批量删除", notes="行程价格-批量删除")
	@DeleteMapping(value = "/deleteBatchYyRoutePrice")
	public Result<String> deleteBatchYyRoutePrice(@RequestParam(name="ids",required=true) String ids) {
	    this.yyRoutePriceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportYyRoutePrice")
    public ModelAndView exportYyRoutePrice(HttpServletRequest request, YyRoutePrice yyRoutePrice) {
		 // Step.1 组装查询条件
		 QueryWrapper<YyRoutePrice> queryWrapper = QueryGenerator.initQueryWrapper(yyRoutePrice, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<YyRoutePrice> pageList = yyRoutePriceService.list(queryWrapper);
		 List<YyRoutePrice> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 //此处设置的filename无效,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.FILE_NAME, "行程价格");
		 mv.addObject(NormalExcelConstants.CLASS, YyRoutePrice.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("行程价格报表", "导出人:" + sysUser.getRealname(), "行程价格"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importYyRoutePrice/{mainId}")
    public Result<?> importYyRoutePrice(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
       // 获取上传文件对象
			 MultipartFile file = entity.getValue();
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<YyRoutePrice> list = ExcelImportUtil.importExcel(file.getInputStream(), YyRoutePrice.class, params);
				 for (YyRoutePrice temp : list) {
                    temp.setRouteId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 yyRoutePriceService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
			 } catch (Exception e) {
				 log.error(e.getMessage(), e);
				 return Result.error("文件导入失败:" + e.getMessage());
			 } finally {
				 try {
					 file.getInputStream().close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		 }
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-行程价格-end----------------------------------------------*/


	 /**
	  * 主页推荐行程取得
	  *
	  * @param routeNum
	  * @return
	  */
	 @ApiOperation(value="主页推荐行程取得", notes="主页推荐行程取得")
	 @GetMapping(value = "/getTopRoutes")
	 @IgnoreAuth
	 public Result<List<RouteInfoView>> getTopRoutes(HttpServletRequest request,@RequestParam(name="routeNum",required=true) String routeNum,
													 @RequestParam(name="userId",required=true) String userId) {
		 Result<List<RouteInfoView>> result = new Result<>();
		 List<RouteInfoView> routeInfoViewList = new ArrayList<>();

		 try {
			 routeInfoViewList = yyRouteService.getRoutes("1",Integer.parseInt(routeNum),userId);
			 result.setResult(routeInfoViewList);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.setSuccess(false);
			 result.setMessage("查询失败");
		 }
		 return result;
	 }

	 /**
	  * 行程页推荐行程取得
	  *
	  * @param routeNum
	  * @return
	  */
	 @ApiOperation(value="行程页推荐行程取得", notes="行程页推荐行程取得")
	 @GetMapping(value = "/getInitRoutes")
	 @IgnoreAuth
	 public Result<YyRoutesPage> getInitRoutes(@RequestParam(name="routeNum",required=true) String routeNum,
											   @RequestParam(name="userId",required=true) String userId) {

		 Result<YyRoutesPage> result = new Result<YyRoutesPage>();
		 YyRoutesPage routes=new YyRoutesPage();
		 List<RouteInfoView> routeInfoViewList = new ArrayList<>();
		 try {
			 routeInfoViewList = yyRouteService.getRoutes("1",Integer.parseInt(routeNum),userId);
			 routes.setTopRoutes(routeInfoViewList);

			 routeInfoViewList = new ArrayList<>();
			 routeInfoViewList = yyRouteService.getRoutes("2",Integer.parseInt(routeNum),userId);
			 routes.setSeasonRoutes(routeInfoViewList);

			 routeInfoViewList = new ArrayList<>();
			 routeInfoViewList = yyRouteService.getRoutes("3",Integer.parseInt(routeNum),userId);
			 routes.setCountryRoutes(routeInfoViewList);

			 result.setResult(routes);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.setSuccess(false);
			 result.setMessage("查询失败");
		 }
		 return result;
	 }

	 /**
	  * 行程检索
	  *
	  * @param placeIds
	  * @return
	  */
	 @ApiOperation(value="行程检索", notes="行程检索")
	 @GetMapping(value = "/searchRoutes")
	 @IgnoreAuth
	 public Result<List<RouteInfoView>> searchRoutes(@RequestParam(name="placeIds",required=true) String[] placeIds,
													 @RequestParam(name="pageNum", defaultValue="1") Integer pageNum,
													 @RequestParam(name="pageSize", defaultValue="5") Integer pageSize,
													 @RequestParam(name="userId",required=true) String userId) {

		 Result<List<RouteInfoView>> result = new Result<>();
		 List<RouteInfoView> routeInfoViewList = new ArrayList<>();
		 try {
			 routeInfoViewList = yyRouteService.fetchRoutesPage(placeIds,pageNum,pageSize,userId);
			 result.setResult(routeInfoViewList);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.setSuccess(false);
			 result.setMessage("查询失败");
		 }
		 return result;
	 }

	 /**
	  * 行程详细取得
	  *
	  * @param routeId
	  * @return
	  */
	 @ApiOperation(value="行程详细取得", notes="行程详细取得")
	 @GetMapping(value = "/getRouteDetail")
	 @IgnoreAuth
	 public Result<YyRoutesDtail> getRouteDetail(@RequestParam(name="routeId",required=true) String routeId) {

		 Result<YyRoutesDtail> result = new Result<>();
		 YyRoutesDtail topRouteItem = new YyRoutesDtail();
		 try {
			 topRouteItem = yyRouteDetailService.getRouteDtailInfo(routeId);
			 result.setResult(topRouteItem);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.setSuccess(false);
			 result.setMessage("查询失败");
		 }
		 return result;
	 }

	 /**
	  * 我的行程（搜藏）
	  *
	  * @param userId
	  * @return
	  */
	 @ApiOperation(value="我的行程", notes="我的行程")
	 @GetMapping(value = "/getMyRoute")
	 public Result<List<RouteInfoView>> getMyRoute(@RequestParam(name="userId",required=true) String userId) {
		 Result<List<RouteInfoView>> result = new Result<>();
		 List<RouteInfoView> routeInfoViewList = new ArrayList<>();

		 try {
			 routeInfoViewList = yyRouteService.getRoutes("4",0,userId);
			 result.setResult(routeInfoViewList);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.setSuccess(false);
			 result.setMessage("查询失败");
		 }
		 return result;
	 }
}

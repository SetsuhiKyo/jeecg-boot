package org.jeecg.modules.yy.place.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.config.shiro.IgnoreAuth;
import org.jeecg.config.yyi18n.LangContext;
import org.jeecg.modules.yy.api.GooglePlacesAPIV1;
import org.jeecg.modules.yy.api.GooglePlacesAutoComplete;
import org.jeecg.modules.yy.api.GooglePlacesDetails;
import org.jeecg.modules.yy.place.entity.YyRoute;
import org.jeecg.modules.yy.place.vo.AutoCompleteText;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.yy.place.entity.YyPlaceTagRel;
import org.jeecg.modules.yy.place.entity.YyPlace;
import org.jeecg.modules.yy.place.vo.YyPlacePage;
import org.jeecg.modules.yy.place.service.IYyPlaceService;
import org.jeecg.modules.yy.place.service.IYyPlaceTagRelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;


 /**
 * @Description: 地点情报
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
@Api(tags="地点情报")
@RestController
@RequestMapping("/place/yyPlace")
@Slf4j
public class YyPlaceController {
	@Autowired
	private IYyPlaceService yyPlaceService;
	@Autowired
	private IYyPlaceTagRelService yyPlaceTagRelService;
	 @Autowired
	private GooglePlacesAutoComplete autoCompleteApi;
	 @Autowired
	 private GooglePlacesDetails googlePlacesDetails;
	 @Autowired
	 private GooglePlacesAPIV1 googlePlacesApi;

	/**
	 * 分页列表查询
	 *
	 * @param yyPlace
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "地点情报-分页列表查询")
	@ApiOperation(value="地点情报-分页列表查询", notes="地点情报-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyPlace>> queryPageList(YyPlace yyPlace,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<YyPlace> queryWrapper = QueryGenerator.initQueryWrapper(yyPlace, req.getParameterMap());
		Page<YyPlace> page = new Page<YyPlace>(pageNo, pageSize);
		IPage<YyPlace> pageList = yyPlaceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yyPlacePage
	 * @return
	 */
	@AutoLog(value = "地点情报-添加")
	@ApiOperation(value="地点情报-添加", notes="地点情报-添加")
    @RequiresPermissions("place:yy_place:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody YyPlacePage yyPlacePage) {
		YyPlace yyPlace = new YyPlace();
		BeanUtils.copyProperties(yyPlacePage, yyPlace);
		yyPlaceService.saveMain(yyPlace, yyPlacePage.getYyPlaceTagRelList());
		return Result.OK("添加成功！");
	}

	 /**
	  *   快速添加
	  *
	  * @param yyPlacePage
	  * @return
	  */
	 @AutoLog(value = "地点情报-快速添加")
	 @ApiOperation(value="地点情报-快速添加", notes="地点情报-快速添加")
	 @RequiresPermissions("place:yy_place:add")
	 @PostMapping(value = "/addFast")
	 public Result<String> addFast(@RequestBody YyPlacePage yyPlacePage) {
		 YyPlace yyPlace = new YyPlace();
		 List<YyPlaceTagRel> placeTagRelList = new ArrayList<>();
		 String getPlaceItemsForDetail = "id,rating,userRatingCount,displayName,types,primaryType,formattedAddress,location,internationalPhoneNumber,websiteUri,reservable,rating,editorialSummary";  //photos

		 try {
			 BeanUtils.copyProperties(yyPlacePage, yyPlace);
			 if (yyPlaceService.getCountByPlaceId(yyPlace.getGooglePladeId())==0) {
				 // 因为是单个地点信息登录，用户评价设定为11（10以下不采用），保证不做筛选
				 placeTagRelList = googlePlacesApi.getPlaceDetails(yyPlace, 11, getPlaceItemsForDetail);
				 yyPlaceService.saveMain(yyPlace, placeTagRelList);
				 return Result.OK("添加成功！");
			 } else {
				 return Result.OK("已经添加！");
			 }

		 } catch (Exception e) {
			 return Result.error("添加失敗！");
		 }


	 }
	
	/**
	 *  编辑
	 *
	 * @param yyPlacePage
	 * @return
	 */
	@AutoLog(value = "地点情报-编辑")
	@ApiOperation(value="地点情报-编辑", notes="地点情报-编辑")
    @RequiresPermissions("place:yy_place:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YyPlacePage yyPlacePage) {
		YyPlace yyPlace = new YyPlace();
		BeanUtils.copyProperties(yyPlacePage, yyPlace);
		YyPlace yyPlaceEntity = yyPlaceService.getById(yyPlace.getId());
		if(yyPlaceEntity==null) {
			return Result.error("未找到对应数据");
		}
		yyPlaceService.updateMain(yyPlace, yyPlacePage.getYyPlaceTagRelList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "地点情报-通过id删除")
	@ApiOperation(value="地点情报-通过id删除", notes="地点情报-通过id删除")
    @RequiresPermissions("place:yy_place:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yyPlaceService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "地点情报-批量删除")
	@ApiOperation(value="地点情报-批量删除", notes="地点情报-批量删除")
    @RequiresPermissions("place:yy_place:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yyPlaceService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "地点情报-通过id查询")
	@ApiOperation(value="地点情报-通过id查询", notes="地点情报-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YyPlace> queryById(@RequestParam(name="id",required=true) String id) {
		YyPlace yyPlace = yyPlaceService.getById(id);
		if(yyPlace==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yyPlace);

	}
	
	/**
	 * 通过place的id查询地点标签关系一览
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "地点标签关系通过主表ID查询")
	@ApiOperation(value="地点标签关系主表ID查询", notes="地点标签关系-通主表ID查询")
	@GetMapping(value = "/queryYyPlaceTagRelByMainId")
	public Result<List<YyPlaceTagRel>> queryYyPlaceTagRelListByMainId(@RequestParam(name="placeId",required=true) String id) {
		List<YyPlaceTagRel> yyPlaceTagRelList = yyPlaceTagRelService.selectByMainId(id);
		return Result.OK(yyPlaceTagRelList);
	}

	 /**
	  * 通过id查询地点名称。自动入力补完组件使用
	  *
	  * @param palceId
	  * @return
	  */
	 @ApiOperation(value="地点情报-通过id查询", notes="地点情报-通过id查询")
	 @GetMapping(value = "/getPlaceNmById")
	 public Result<AutoCompleteText> getPlaceNmById(@RequestParam(name="id",required=true) String palceId) {
		 AutoCompleteText item = new AutoCompleteText();
		 YyPlace yyPlace = yyPlaceService.selectByPlaceId(palceId);
		 if(yyPlace!=null) {
			 item.setPlaceId(yyPlace.getGooglePladeId());
			 item.setPlaceText(getResultByLang(yyPlace));
		 } else {
			 item.setPlaceId(palceId);
			 item.setPlaceText(googlePlacesDetails.getPlaceName(palceId));
		 }
		 return Result.OK(item);

	 }

	 private String getResultByLang(YyPlace obj){
		 String ret = "";

		 switch (LangContext.getLang()){
			 case "zh-CN":
				 ret = obj.getNameZh();
				 break;
			 case "zh-TW":
				 ret = obj.getNameTw();
				 break;
			 case "ja":
				 ret = obj.getNameJp();
				 break;
			 default:
				 ret = obj.getNameEn();
				 break;
		 }

		 return ret;
	 }
	 /**
	  * 景点入力补完
	  *
	  * @param text
	  * @return
	  */
	 @ApiOperation(value="景点入力补完", notes="景点入力补完")
	 @GetMapping(value = "/inputAutoComplete")
	 @IgnoreAuth
	 public Result<List<AutoCompleteText>> inputAutoComplete(@RequestParam(name="text",required=true) String text) {

		 Result<List<AutoCompleteText>> result = new Result<>();
		 try {
			 List<AutoCompleteText> autoCompleteText = yyPlaceService.inputAutocomplete(text);
			 result.setResult(autoCompleteText);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.setSuccess(false);
			 result.setMessage("查询失败");
		 }
		 return result;
	 }

	 /**
	  * 机场一览取得
	  *
	  * @return
	  */
	 @ApiOperation(value="机场一览取得", notes="机场一览取得")
	 @GetMapping(value = "/fetchAirports")
	 @IgnoreAuth
	 public Result<List<AutoCompleteText>> fetchAirports() {

		 Result<List<AutoCompleteText>> result = new Result<>();
		 try {
			 List<AutoCompleteText> autoCompleteText = yyPlaceService.fetchAirports();
			 result.setResult(autoCompleteText);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.setSuccess(false);
			 result.setMessage("查询失败");
		 }
		 return result;
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param yyPlace
    */
    @RequiresPermissions("place:yy_place:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyPlace yyPlace) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<YyPlace> queryWrapper = QueryGenerator.initQueryWrapper(yyPlace, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<YyPlace> yyPlaceList = yyPlaceService.list(queryWrapper);

      // Step.3 组装pageList
      List<YyPlacePage> pageList = new ArrayList<YyPlacePage>();
      for (YyPlace main : yyPlaceList) {
          YyPlacePage vo = new YyPlacePage();
          BeanUtils.copyProperties(main, vo);
          List<YyPlaceTagRel> yyPlaceTagRelList = yyPlaceTagRelService.selectByMainId(main.getId());
          vo.setYyPlaceTagRelList(yyPlaceTagRelList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "地点情报列表");
      mv.addObject(NormalExcelConstants.CLASS, YyPlacePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("地点情报数据", "导出人:"+sysUser.getRealname(), "地点情报"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_place:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
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
              List<YyPlacePage> list = ExcelImportUtil.importExcel(file.getInputStream(), YyPlacePage.class, params);
              for (YyPlacePage page : list) {
                  YyPlace po = new YyPlace();
                  BeanUtils.copyProperties(page, po);
                  yyPlaceService.saveMain(po, page.getYyPlaceTagRelList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}

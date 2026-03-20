package org.jeecg.modules.yy.place.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.shiro.IgnoreAuth;
import org.jeecg.modules.yy.place.PaymentStatus;
import org.jeecg.modules.yy.place.entity.YyUserFavorite;
import org.jeecg.modules.yy.place.service.IYyUserFavoriteService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.yy.place.vo.RouteInfoView;
import org.jeecg.modules.yy.utils.Base62Util;
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
 * @Description: 用户收藏
 * @Author: jeecg-boot
 * @Date:   2026-02-06
 * @Version: V1.0
 */
@Api(tags="用户收藏")
@RestController
@RequestMapping("/place/yyUserFavorite")
@Slf4j
public class YyUserFavoriteController extends JeecgController<YyUserFavorite, IYyUserFavoriteService> {
	@Autowired
	private IYyUserFavoriteService yyUserFavoriteService;
	
	/**
	 * 分页列表查询
	 *
	 * @param yyUserFavorite
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "用户收藏-分页列表查询")
	@ApiOperation(value="用户收藏-分页列表查询", notes="用户收藏-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyUserFavorite>> queryPageList(YyUserFavorite yyUserFavorite,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<YyUserFavorite> queryWrapper = QueryGenerator.initQueryWrapper(yyUserFavorite, req.getParameterMap());
		Page<YyUserFavorite> page = new Page<YyUserFavorite>(pageNo, pageSize);
		IPage<YyUserFavorite> pageList = yyUserFavoriteService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yyUserFavorite
	 * @return
	 */
	@AutoLog(value = "用户收藏-添加")
	@ApiOperation(value="用户收藏-添加", notes="用户收藏-添加")
	@RequiresPermissions("place:yy_user_favorite:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody YyUserFavorite yyUserFavorite) {
		yyUserFavoriteService.save(yyUserFavorite);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param yyUserFavorite
	 * @return
	 */
	@AutoLog(value = "用户收藏-编辑")
	@ApiOperation(value="用户收藏-编辑", notes="用户收藏-编辑")
	@RequiresPermissions("place:yy_user_favorite:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YyUserFavorite yyUserFavorite) {
		yyUserFavoriteService.updateById(yyUserFavorite);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户收藏-通过id删除")
	@ApiOperation(value="用户收藏-通过id删除", notes="用户收藏-通过id删除")
	@RequiresPermissions("place:yy_user_favorite:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yyUserFavoriteService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用户收藏-批量删除")
	@ApiOperation(value="用户收藏-批量删除", notes="用户收藏-批量删除")
	@RequiresPermissions("place:yy_user_favorite:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yyUserFavoriteService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "用户收藏-通过id查询")
	@ApiOperation(value="用户收藏-通过id查询", notes="用户收藏-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YyUserFavorite> queryById(@RequestParam(name="id",required=true) String id) {
		YyUserFavorite yyUserFavorite = yyUserFavoriteService.getById(id);
		if(yyUserFavorite==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yyUserFavorite);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yyUserFavorite
    */
    @RequiresPermissions("place:yy_user_favorite:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyUserFavorite yyUserFavorite) {
        return super.exportXls(request, yyUserFavorite, YyUserFavorite.class, "用户收藏");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_user_favorite:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YyUserFavorite.class);
    }

	 /*--------------------------------以上为自动生成代码----------------------------------------------*/

	 /**
	  * 更新用户搜藏
	  *
	  * @return
	  */
	 @GetMapping(value = "/upUserFavorite")
	 public Result<JSONObject> upUserFavorite(@RequestParam(name="userId",required=true) String userId,
											@RequestParam(name="routeId",required=true) String routeId,
											@RequestParam(name="favSts",required=true) String favSts) {
		 Result<JSONObject> result = new Result<>();
		 JSONObject obj = new JSONObject();

		 // 支付确认
		 boolean updateRet = yyUserFavoriteService.upUserFavorite(userId,routeId,favSts);

		 result.setResult(obj);
		 result.setSuccess(updateRet);
		 return result;
	 }

}

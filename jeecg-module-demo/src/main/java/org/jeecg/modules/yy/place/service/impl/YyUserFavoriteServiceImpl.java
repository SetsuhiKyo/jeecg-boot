package org.jeecg.modules.yy.place.service.impl;

import org.jeecg.modules.yy.place.entity.YyUserFavorite;
import org.jeecg.modules.yy.place.mapper.YyUserFavoriteMapper;
import org.jeecg.modules.yy.place.service.IYyUserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户收藏
 * @Author: jeecg-boot
 * @Date:   2026-02-06
 * @Version: V1.0
 */
@Service
public class YyUserFavoriteServiceImpl extends ServiceImpl<YyUserFavoriteMapper, YyUserFavorite> implements IYyUserFavoriteService {

    @Autowired
    public YyUserFavoriteMapper yyUserFavoriteMapper;
    /**
     * 更新用户搜藏
     * @param uesrId
     * @param routeId
     * @param favSts
     * @return
     */
    public boolean upUserFavorite(String uesrId,String routeId,String favSts){
        boolean updateRet = false;
        YyUserFavorite ret = yyUserFavoriteMapper.getResultByUserIdAndTargetId(uesrId,routeId);
        if (ret == null){
            ret = new YyUserFavorite();
            ret.setTargetKbn("02");
            ret.setTargetId(routeId);
            ret.setUserType("01");
            ret.setUserId(uesrId);
            ret.setFavoriteFlg("1");

        } else {
            ret.setFavoriteFlg(favSts);
        }
        updateRet = saveOrUpdate(ret);
        return updateRet;
    }
}

package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyUserFavorite;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 用户收藏
 * @Author: jeecg-boot
 * @Date:   2026-02-06
 * @Version: V1.0
 */
public interface IYyUserFavoriteService extends IService<YyUserFavorite> {

    /**
     * 更新用户搜藏
     * @param uesrId
     * @param routeId
     * @param favSts
     * @return
     */
    public boolean upUserFavorite(String uesrId,String routeId,String favSts);
}

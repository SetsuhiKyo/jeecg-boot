package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.SettlementStatus;
import org.jeecg.modules.yy.place.entity.YyOrderSettlement;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 结算表
 * @Author: jeecg-boot
 * @Date:   2026-03-03
 * @Version: V1.0
 */
public interface IYyOrderSettlementService extends IService<YyOrderSettlement> {
    /**
     * 更新结算情报
     */
    public boolean updateBySettlementInfo(YyOrderSettlement yyOrderSettlement);
}

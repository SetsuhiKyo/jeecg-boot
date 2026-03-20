package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.RefundStatus;
import org.jeecg.modules.yy.place.SettlementStatus;
import org.jeecg.modules.yy.place.entity.YyOrderRefund;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.yy.place.entity.YyOrderSettlement;

/**
 * @Description: 退款表
 * @Author: jeecg-boot
 * @Date:   2026-03-03
 * @Version: V1.0
 */
public interface IYyOrderRefundService extends IService<YyOrderRefund> {
    /**
     * 更新退款情报
     */
    public boolean updateByRefundInfo(YyOrderRefund yyOrderRefund);
}

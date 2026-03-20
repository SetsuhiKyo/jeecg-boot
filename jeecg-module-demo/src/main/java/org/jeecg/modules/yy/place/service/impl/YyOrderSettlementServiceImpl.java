package org.jeecg.modules.yy.place.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.yy.events.ServiceReminderEvent;
import org.jeecg.modules.yy.events.SettlementCompletedEvent;
import org.jeecg.modules.yy.place.OrderStatus;
import org.jeecg.modules.yy.place.PaymentStatus;
import org.jeecg.modules.yy.place.SettlementStatus;
import org.jeecg.modules.yy.place.entity.YyOrder;
import org.jeecg.modules.yy.place.entity.YyOrderSettlement;
import org.jeecg.modules.yy.place.mapper.YyOrderMapper;
import org.jeecg.modules.yy.place.mapper.YyOrderSettlementMapper;
import org.jeecg.modules.yy.place.service.IYyOrderService;
import org.jeecg.modules.yy.place.service.IYyOrderSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 结算表
 * @Author: jeecg-boot
 * @Date:   2026-03-03
 * @Version: V1.0
 */
@Service
public class YyOrderSettlementServiceImpl extends ServiceImpl<YyOrderSettlementMapper, YyOrderSettlement> implements IYyOrderSettlementService {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private YyOrderMapper yyOrderMapper;
    /**
     * 更新结算情报
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBySettlementInfo(YyOrderSettlement yyOrderSettlement){
        boolean updateSettlementSts = false;

        YyOrderSettlement dbOrderSettlement = getById(yyOrderSettlement.getId());
        String settlementSts = yyOrderSettlement.getSettlementSts();

        yyOrderSettlement.setSettlementSts(dbOrderSettlement.getSettlementSts());

        updateById(yyOrderSettlement);

        if (!settlementSts.equals(dbOrderSettlement.getSettlementSts())) {
            updateSettlementSts = updateSettlementSts(yyOrderSettlement.getOrderId(),settlementSts);
        }

        return updateSettlementSts;
    }
    /**
     * 更新结算状态
     */
    private boolean updateSettlementSts(String orderId, String settlementSts) {
        boolean updateRet = false;
        YyOrder order = yyOrderMapper.selectById(orderId);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id", orderId);
        YyOrderSettlement orderSettlement  = getOne(queryWrapper);

        Object targetEvent = "";

        switch (settlementSts) {
            case "TRANSFERRED":
                if (!SettlementStatus.REQUESTED.name().equals(orderSettlement.getSettlementSts())) {
                    throw new RuntimeException("当前状态不能更新为【已转账】,请重新【申请结算】。");
                }
                orderSettlement.setSettlementSts(SettlementStatus.TRANSFERRED.name());
            case "SETTLED":
                if (!SettlementStatus.TRANSFERRED.name().equals(orderSettlement.getSettlementSts())) {
                    throw new RuntimeException("当前状态不能更新为【已结算】,先请司机公司转账，然后更新为【已转账】。");
                }
                orderSettlement.setSettlementSts(SettlementStatus.SETTLED.name());
                targetEvent = new SettlementCompletedEvent(order);
        }

        updateRet = saveOrUpdate(orderSettlement);
        if (updateRet && !targetEvent.toString().isEmpty()) {
            publisher.publishEvent(targetEvent);
        }
        return updateRet;
    }
}

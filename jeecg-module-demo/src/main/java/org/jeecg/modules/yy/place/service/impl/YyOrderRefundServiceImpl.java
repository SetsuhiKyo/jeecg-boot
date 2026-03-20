package org.jeecg.modules.yy.place.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.yy.events.RefundCompletedEvent;
import org.jeecg.modules.yy.events.SettlementCompletedEvent;
import org.jeecg.modules.yy.place.RefundStatus;
import org.jeecg.modules.yy.place.SettlementStatus;
import org.jeecg.modules.yy.place.entity.YyOrder;
import org.jeecg.modules.yy.place.entity.YyOrderRefund;
import org.jeecg.modules.yy.place.entity.YyOrderSettlement;
import org.jeecg.modules.yy.place.mapper.YyOrderMapper;
import org.jeecg.modules.yy.place.mapper.YyOrderRefundMapper;
import org.jeecg.modules.yy.place.service.IYyOrderRefundService;
import org.jeecg.modules.yy.place.service.IYyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 退款表
 * @Author: jeecg-boot
 * @Date:   2026-03-03
 * @Version: V1.0
 */
@Service
public class YyOrderRefundServiceImpl extends ServiceImpl<YyOrderRefundMapper, YyOrderRefund> implements IYyOrderRefundService {
    @Autowired
    private YyOrderMapper yyOrderMapper;
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 更新退款情报
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByRefundInfo(YyOrderRefund yyOrderRefund){
        boolean updateRefundSts = false;

        YyOrderRefund dbOrderRefund = getById(yyOrderRefund.getId());
        String refundSts = yyOrderRefund.getRefundSts();

        yyOrderRefund.setRefundSts(dbOrderRefund.getRefundSts());

        updateById(yyOrderRefund);

        if (!refundSts.equals(dbOrderRefund.getRefundSts())) {
            updateRefundSts = updateRefundSts(yyOrderRefund.getOrderId(),refundSts);
        }

        return updateRefundSts;
    }
    /**
     * 更新退款状态
     */
    private boolean updateRefundSts(String orderId, String refundSts) {
        boolean updateRet = false;
        YyOrder order = yyOrderMapper.selectById(orderId);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id", orderId);
        YyOrderRefund orderRefund  = getOne(queryWrapper);

        Object targetEvent = "";

        switch (refundSts) {
            case "TRANSFERRED":
                if (!RefundStatus.REQUESTED.name().equals(orderRefund.getRefundSts())) {
                    throw new RuntimeException("当前状态不能更新为【已退款】,请重新【申请退款】。");
                }
                orderRefund.setRefundSts(RefundStatus.TRANSFERRED.name());
                targetEvent = new RefundCompletedEvent(order);
        }

        updateRet = saveOrUpdate(orderRefund);
        if (updateRet && !targetEvent.toString().isEmpty()) {
            publisher.publishEvent(targetEvent);
        }
        return updateRet;
    }
}

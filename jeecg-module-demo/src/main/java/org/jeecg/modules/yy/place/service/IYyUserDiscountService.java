package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyUserDiscount;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.yy.place.vo.DiscountInfo;
import org.jeecg.modules.yy.place.vo.DiscountInfoView;

import java.util.List;

/**
 * @Description: 用户优惠
 * @Author: jeecg-boot
 * @Date:   2026-01-26
 * @Version: V1.0
 */
public interface IYyUserDiscountService extends IService<YyUserDiscount> {

    /**
     * 取得我的优惠
     * @param userId
     * @return
     */
    public List<DiscountInfoView>  getMyDiscount(String userId);

    /**
     * 取得顾客优惠
     * @param customerId
     * @return
     */
    public List<DiscountInfo>  getCustomerDiscount(String customerId);
}

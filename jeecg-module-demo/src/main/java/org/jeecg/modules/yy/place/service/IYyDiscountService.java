package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyDiscount;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.yy.place.vo.DiscountInfo;

import java.util.List;

/**
 * @Description: 優惠情報
 * @Author: jeecg-boot
 * @Date:   2026-01-26
 * @Version: V1.0
 */
public interface IYyDiscountService extends IService<YyDiscount> {

    public List<DiscountInfo> getSysDiscount();

}

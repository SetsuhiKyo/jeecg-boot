package org.jeecg.modules.yy.place.service.impl;

import org.jeecg.modules.yy.place.entity.YyDiscount;
import org.jeecg.modules.yy.place.mapper.YyDiscountMapper;
import org.jeecg.modules.yy.place.service.IYyDiscountService;
import org.jeecg.modules.yy.place.vo.DiscountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 優惠情報
 * @Author: jeecg-boot
 * @Date:   2026-01-26
 * @Version: V1.0
 */
@Service
public class YyDiscountServiceImpl extends ServiceImpl<YyDiscountMapper, YyDiscount> implements IYyDiscountService {

    @Autowired
    YyDiscountMapper yyDiscountMapper;
    public List<DiscountInfo> getSysDiscount(){
        List<DiscountInfo> discounts = new ArrayList<>();
        DiscountInfo item = new DiscountInfo();
        List<YyDiscount> rets = yyDiscountMapper.getValidDiscount("01"); // 有效的系统优惠

        if (rets != null && rets.size() > 0) {
            for (YyDiscount sysdiscount : rets) {
                item = new DiscountInfo();
                item.setDisId(sysdiscount.getId());
                item.setDisName(sysdiscount.getDiscountCn());
                item.setDisType(sysdiscount.getDiscountType());
                item.setDisKbn(sysdiscount.getDiscountKbn());
                item.setDisRate(sysdiscount.getDiscountRate());
                item.setDisAmount(sysdiscount.getDiscountNum());
                discounts.add(item);
            }
        }
        return discounts;
    }

}

package org.jeecg.modules.yy.place.service.impl;

import org.jeecg.modules.yy.place.entity.YyUserDiscount;
import org.jeecg.modules.yy.place.mapper.YyUserDiscountMapper;
import org.jeecg.modules.yy.place.service.IYyUserDiscountService;
import org.jeecg.modules.yy.place.vo.DiscountInfo;
import org.jeecg.modules.yy.place.vo.DiscountInfoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户优惠
 * @Author: jeecg-boot
 * @Date:   2026-01-26
 * @Version: V1.0
 */
@Service
public class YyUserDiscountServiceImpl extends ServiceImpl<YyUserDiscountMapper, YyUserDiscount> implements IYyUserDiscountService {

    @Autowired
    YyUserDiscountMapper yyUserDiscountMapper;
    /**
     * 取得顾客优惠
     * @param userId
     * @return
     */
    @Override
    public List<DiscountInfoView> getMyDiscount(String userId) {
        List<DiscountInfoView> rets = new ArrayList<>();
        List<DiscountInfo> dises = new ArrayList<>();
        DiscountInfoView item = new DiscountInfoView();
        String img =  "";

        dises = yyUserDiscountMapper.getUserDiscount(userId);
        if (dises != null && dises.size() > 0) {
            for(DiscountInfo dis:dises) {

                if (dis.getStartDate().isBefore(LocalDateTime.now())) {
                    item = new DiscountInfoView();
                    if ("01".equals(dis.getDisKbn())) {
                        if (dis.getDisAmount().equals(new BigDecimal(1000))) {
                            img = "../../static/app/1000off.jpg";
                        } else if (dis.getDisAmount() == new BigDecimal(2000)) {
                            img = "../../static/app/2000off.jpg";
                        } else if (dis.getDisAmount() == new BigDecimal(3000)) {
                            img = "../../static/app/3000off.jpg";
                        } else if (dis.getDisAmount() == new BigDecimal(5000)) {
                            img = "../../static/app/5000off.jpg";
                        }
                    } else if ("02".equals(dis.getDisKbn())) {
                        if (dis.getDisRate() == 5) {
                            img = "../../static/app/5off.jpg";
                        } else if (dis.getDisRate() == 10) {
                            img = "../../static/app/10off.jpg";
                        } else if (dis.getDisRate() == 15) {
                            img = "../../static/app/15off.jpg";
                        } else if (dis.getDisRate() == 20) {
                            img = "../../static/app/20off.jpg";
                        } else if (dis.getDisRate() == 25) {
                            img = "../../static/app/25off.jpg";
                        } else if (dis.getDisRate() == 30) {
                            img = "../../static/app/30off.jpg";
                        }
                    }
                    item.setImg(img);
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    item.setValidyDate(dis.getEndDate().format(fmt));
                    item.setInvalid(false);
                    item.setDiscountStatus("01");
                    if (dis.getEndDate().isBefore(LocalDateTime.now())) {
                        item.setInvalid(true);
                        item.setInvalidStuts("02");
                        item.setDiscountStatus("03");
                    } else {
                        if("02".equals(dis.getUsed())){
                            item.setInvalid(true);
                            item.setInvalidStuts("01");
                            item.setDiscountStatus("02");
                        }
                    }

                    rets.add(item);
                }
            }
        }

        return rets;
    }

    @Override
    public List<DiscountInfo> getCustomerDiscount(String customerId) {
        List<DiscountInfo> rets = new ArrayList<>();
        List<DiscountInfo> dises = new ArrayList<>();

        dises = yyUserDiscountMapper.getUserDiscount(customerId);

        if (dises != null && dises.size() > 0) {
            for (DiscountInfo dis : dises) {
                if ("01".equals(dis.getUsed()) && dis.getEndDate().isBefore(LocalDateTime.now())) {
                    // 没有使用过并且没有过期的优惠
                    rets.add(dis);
                }
            }
        }
        return dises;
    }
}

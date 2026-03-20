package org.jeecg.modules.yy.place.service.impl;

import org.jeecg.modules.yy.place.entity.YyOrder;
import org.jeecg.modules.yy.place.entity.YyUserReview;
import org.jeecg.modules.yy.place.mapper.YyOrderMapper;
import org.jeecg.modules.yy.place.mapper.YyUserReviewMapper;
import org.jeecg.modules.yy.place.service.IYyUserReviewService;
import org.jeecg.modules.yy.place.vo.ReviewInfoPage;
import org.jeecg.modules.yy.utils.Base62Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 用户评价
 * @Author: jeecg-boot
 * @Date:   2026-02-06
 * @Version: V1.0
 */
@Service
public class YyUserReviewServiceImpl extends ServiceImpl<YyUserReviewMapper, YyUserReview> implements IYyUserReviewService {

    @Autowired
    private YyUserReviewMapper yyUserReviewMapper;
    @Autowired
    private YyOrderMapper yyOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReview(ReviewInfoPage reviewInfo) {
        YyUserReview userReview = new YyUserReview();
        YyOrder order = yyOrderMapper.selectById(Base62Util.decode(reviewInfo.getOrderNo()));

        if (order != null) {
            userReview.setUserType("01"); // 顾客
            userReview.setUserId(reviewInfo.getUserId());
            userReview.setTargetKbn("01"); // app评价
            userReview.setTargetId("0");
            userReview.setReviewRate(reviewInfo.getAppRate());
            userReview.setReviewContent(reviewInfo.getAppContent());
            this.save(userReview);

            userReview = new YyUserReview();
            userReview.setUserType("01"); // 顾客
            userReview.setUserId(reviewInfo.getUserId());
            userReview.setTargetKbn("02"); // 客服评价
            userReview.setTargetId("1");
            userReview.setReviewRate(reviewInfo.getServiceRate());
            userReview.setReviewContent(reviewInfo.getServiceContent());
            this.save(userReview);

            userReview = new YyUserReview();
            userReview.setUserType("01"); // 顾客
            userReview.setUserId(reviewInfo.getUserId());
            userReview.setTargetKbn("03"); // 行程评价
            userReview.setTargetId(order.getRouteId());
            userReview.setReviewRate(reviewInfo.getRouteRate());
            userReview.setReviewContent(reviewInfo.getRouteContent());
            this.save(userReview);

            userReview = new YyUserReview();
            userReview.setUserType("01"); // 顾客
            userReview.setUserId(reviewInfo.getUserId());
            userReview.setTargetKbn("04"); // 司机评价
            userReview.setTargetId(order.getDriverId());
            userReview.setReviewRate(reviewInfo.getDriverRate());
            userReview.setReviewContent(reviewInfo.getDriverContent());
            this.save(userReview);

            userReview = new YyUserReview();
            userReview.setUserType("01"); // 顾客
            userReview.setUserId(reviewInfo.getUserId());
            userReview.setTargetKbn("05"); // 车辆评价
            userReview.setTargetId(order.getCarId());
            userReview.setReviewRate(reviewInfo.getCarRate());
            userReview.setReviewContent(reviewInfo.getCarContent());
            this.save(userReview);
        }
    }
}

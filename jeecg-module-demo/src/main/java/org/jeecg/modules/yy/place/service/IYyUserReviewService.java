package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyUserReview;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.yy.place.vo.ReviewInfoPage;

/**
 * @Description: 用户评价
 * @Author: jeecg-boot
 * @Date:   2026-02-06
 * @Version: V1.0
 */
public interface IYyUserReviewService extends IService<YyUserReview> {

    public void addReview(ReviewInfoPage reviewInfo);
}

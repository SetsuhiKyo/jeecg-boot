package org.jeecg.modules.yy.utils;

import org.jeecg.modules.yy.place.vo.Location;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2025/07/02
 * @Version 0.1
 */
@Component
public class GeoGridGenerator {

    // 地球半径（单位：米）
    private static final double EARTH_RADIUS = 6378137;

    // 生成网格节点
    public static List<double[]> generateGrid(double centerLat, double centerLng, int gridSize, double cellSizeMeters) {
        List<double[]> points = new ArrayList<>();

        int halfGrid = gridSize / 2;

        for (int i = -halfGrid; i <= halfGrid; i++) {
            for (int j = -halfGrid; j <= halfGrid; j++) {

                double offsetLat = (cellSizeMeters * i) / EARTH_RADIUS;
                double offsetLng = (cellSizeMeters * j) / (EARTH_RADIUS * Math.cos(Math.toRadians(centerLat)));

                double lat = centerLat + Math.toDegrees(offsetLat);
                double lng = centerLng + Math.toDegrees(offsetLng);

                points.add(new double[]{lat, lng});
            }
        }

        return points;
    }

    // 测试
    public static void main(String[] args) {
        double centerLat = 35.6895; // 东京
        double centerLng = 139.6917;
        int gridSize = 5;           // 5x5
        double radius = 1000;       // 1000米间隔

        List<double[]> gridPoints = generateGrid(centerLat, centerLng, gridSize, radius);

        for (double[] point : gridPoints) {
            System.out.printf("Lat: %.6f, Lng: %.6f\n", point[0], point[1]);
        }
    }
}

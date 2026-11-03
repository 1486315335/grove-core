package com.lrz.grovecore.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * 经纬度距离工具类
 *
 * @author lirenzhi
 * @since 2023/08/16
 */
public class DistanceUtil {

    /**
     * 距离
     *
     * @param sourceLongitude 源经度
     * @param sourceLatitude  源纬度
     * @param targetLongitude 目标经度
     * @param targetLatitude  目标纬度
     * @return double
     */
    public static double distance(double sourceLongitude, double sourceLatitude, double targetLongitude, double targetLatitude) {
        GlobalCoordinates source = new GlobalCoordinates(sourceLatitude, sourceLongitude);
        GlobalCoordinates target = new GlobalCoordinates(targetLatitude, targetLongitude);
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, source, target);
        return geoCurve.getEllipsoidalDistance();
    }

    /**
     * 格式化距离
     *
     * @param distance 单位米
     * @return 1.00km 或者900m
     */
    public static String distancePretty(double distance) {
        if (distance >= 1000) {
            return String.format("%.2fkm", distance / 1000);
        }
        return (int) distance + "m";
    }
}
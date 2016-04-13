package com.smartsampa.model;

import com.smartsampa.utils.Point;

/**
 * Created by ruan0408 on 12/04/2016.
 */
public interface Shape {

    Point[] getPoints();
    double[] getTraveledDistances();
}

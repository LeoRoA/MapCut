package com.testcase.rendering.controller;

import com.testcase.rendering.model.MapData;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@EnableDiscoveryClient
public class RenderingController {

    @GetMapping("/render")
    @ResponseBody
    public Mono<MapData> renderMap(
            @RequestParam int width,
            @RequestParam int height,
            @RequestParam double minLat,
            @RequestParam double minLon,
            @RequestParam double maxLat,
            @RequestParam double maxLon

    ) {
        return Mono.fromCallable(() -> {
            double[] minDegrees = convertToDegrees(minLat, minLon);
            double[] maxDegrees = convertToDegrees(maxLat, maxLon);

            double centerLat = (minDegrees[1] + maxDegrees[1]) / 2;
            double centerLon = (minDegrees[0] + maxDegrees[0]) / 2;

            int zoomLevel = calculateZoomLevel(minLat, minLon, maxLat, maxLon, width, height);

            MapData mapData = new MapData();
            mapData.setMapUrl(buildMapLink(zoomLevel, centerLat, centerLon));
            mapData.setCenterLat(centerLat);
            mapData.setCenterLon(centerLon);
            mapData.setZoomLevel(zoomLevel);

            return mapData;

        });
    }

    private double[] convertToDegrees(double x, double y) throws TransformException, FactoryException {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(x, y));


        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:3857");
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);

        Point transformedPoint = (Point) JTS.transform(point, transform);

        double[] result = new double[2];
        result[0] = transformedPoint.getY();
        result[1] = transformedPoint.getX();

        return result;
    }


    private int calculateZoomLevel(double minX, double minY, double maxX, double maxY, int width, int height) {
        double diffX = maxX - minX;
        double diffY = maxY - minY;
        int zoomX = calculateZoomForDimension(diffX, width);
        int zoomY = calculateZoomForDimension(diffY, height);
        return Math.max(zoomX, zoomY);
    }

    private int calculateZoomForDimension(double dimension, int size) {
        double degreesPerPixel = dimension / size;
        return 20 - (int) Math.ceil(Math.log(360 / degreesPerPixel) / Math.log(2));
    }

    private String buildMapLink(double zoomLevel, double centerLat, double centerLon) {
        return "https://www.openstreetmap.org/#map=" +
                zoomLevel + "/" + centerLat + "/" + centerLon;
    }
}


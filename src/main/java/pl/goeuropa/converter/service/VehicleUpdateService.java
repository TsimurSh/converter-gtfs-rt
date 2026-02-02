package pl.goeuropa.converter.service;

import com.google.transit.realtime.GtfsRealtime;

public interface VehicleUpdateService {

    String getVehiclePositions();

    GtfsRealtime.FeedMessage getPBVehiclePositions();
}

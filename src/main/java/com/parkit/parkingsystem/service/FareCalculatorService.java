package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    private static final double ONE_HOUR_MILLIS = TimeUnit.HOURS.toMillis(1L);
    private static final double HALF_HOUR_MILLIS = TimeUnit.MINUTES.toMillis(30L);
    public void calculateFare( Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long durationMillis = ticket.getOutTime().getTime() - ticket.getInTime().getTime();

        if (durationMillis <= HALF_HOUR_MILLIS) {
            // <=30min is free
            ticket.setPrice(0);
            return;
        }


        //TODO: Some tests are failing here. Need to check if this logic is correct

        double rate;
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                rate = Fare.CAR_RATE_PER_HOUR;
                break;
            }
            case BIKE: {
                rate = Fare.BIKE_RATE_PER_HOUR;
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
        ticket.setPrice(durationMillis / ONE_HOUR_MILLIS * rate);
    }
}
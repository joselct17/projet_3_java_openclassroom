package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    private static final double ONE_HOUR_MILLIS = TimeUnit.HOURS.toMillis(1L);
    private static final double HALF_HOUR_MILLIS = TimeUnit.MINUTES.toMillis(30L);


    public void calculateFare( Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();


        //TODO: Some tests are failing here. Need to check if this logic is correct
        long duration = outHour - inHour;

        if(duration <= HALF_HOUR_MILLIS) {
            ticket.setPrice(0);
            return;
        }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration / ONE_HOUR_MILLIS * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration / ONE_HOUR_MILLIS * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}
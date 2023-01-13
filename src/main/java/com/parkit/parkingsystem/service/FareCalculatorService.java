package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;


import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    //CREATION DE DEUX VARIABLES POUR AVOIR LE TEMPS EN MILLISECUNDES. UNE HEURE ET 30MINUTES
    private static final double ONE_HOUR_MILLIS = TimeUnit.HOURS.toMillis(1L);
    private static final double HALF_HOUR_MILLIS = TimeUnit.MINUTES.toMillis(30L);

//
    public void calculateFare( Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        //TODO: Some tests are failing here. Need to check if this logic is correct


        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        // CALCULE DURATION
        long duration = outHour - inHour;
        // CONDITION POUR QUE LES PREMIERS 30MIN SOIENT GRATUITS
        if(duration <= HALF_HOUR_MILLIS) {
            ticket.setPrice(0);
            return;
        }

        // ON ENVOI UN PRIX DANS LE CAS DE LA VOITURE OU DE LA MOTO GRACE AU CALCUL EN MILISECONDES
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
package ch.fhnw.oopi2.ylfm.departureapp;

public class Departure {

    private String departureTime;
    private String trip;
    private String destination;
    private String via;
    private String track;
    private String status;

    public Departure(String departureTime, String trip, String destination, String via, String track, String status){
        this.departureTime = departureTime;
        this.trip = trip;
        this.destination = destination;
        this.via = via;
        this.track = track;
        this.status = status;
    }
    
    public String getTrip() {
        return trip;
    }

    public String getTrack() {
        return track;
    }

    public String getVia() {
        return via;
    }

    public String getDestination() {
        return destination;
    }

    public String getStatus() {
        return status;
    }

    public String getDepartureTime() {
        return departureTime;
    }
}

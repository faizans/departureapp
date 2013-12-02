package ch.fhnw.oopi2.ylfm.departureapp;

public class Departure {

	// Uhrzeit;Fahrt;in Richtung;über;Gleis;Status
	// 00:00;IC 747;Zürich HB;Olten 00:00 - Aarau 00:08 - Zürich HB 00:33;;

	private String departureTime;
	private String trip;
	private String destination;
	private String via;
	private String track;
	private String status;

	public String getTrip() {
		return trip;
	}

	public void setTrip(String trip) {
		this.trip = trip;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
}

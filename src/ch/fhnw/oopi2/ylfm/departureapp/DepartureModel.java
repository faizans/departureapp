package ch.fhnw.oopi2.ylfm.departureapp;

import java.util.HashSet;
import java.util.Set;

public class DepartureModel implements Observable {
	private final Set<Observer> observers = new HashSet<>();

	// departurelist and selected departure here

	// getAllDepartures

	// getSelected Departures

	// all Program Logic here...

	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	private void notifyObservers() {
		for (Observer observer : observers) {
			observer.update(this);
		}
	}

}

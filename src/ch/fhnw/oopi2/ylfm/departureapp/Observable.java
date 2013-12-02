package ch.fhnw.oopi2.ylfm.departureapp;

public interface Observable {
	public void addObserver(Observer observer);

	public void removeObserver(Observer observer);
}

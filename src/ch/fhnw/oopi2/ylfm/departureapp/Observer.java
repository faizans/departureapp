package ch.fhnw.oopi2.ylfm.departureapp;

public interface Observer {
	void update(Observable model);
	void repaint(Observable model);
    void searchResult(Observable model);
}

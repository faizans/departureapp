package ch.fhnw.oopi2.ylfm.departureapp;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DepartureModel extends DefaultTableModel implements Observable {
	private final Set<Observer> observers = new HashSet<>();

    private List<Departure> departureList = new ArrayList<>();

	// departurelist and selected departure here
    public void loadDataFromFile() {

    }

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

    @Override
    public int getRowCount() {
        return 10;    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int getColumnCount() {
        return 10;    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return "Row 1";
            default:
                return "no name";
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        return super.getValueAt(row, column);    //To change body of overridden methods use File | Settings | File Templates.
    }
}

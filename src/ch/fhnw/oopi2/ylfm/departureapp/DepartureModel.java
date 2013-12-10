package ch.fhnw.oopi2.ylfm.departureapp;

import javax.swing.table.AbstractTableModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DepartureModel implements Observable {
    private final Set<Observer> observers = new HashSet<>();
    private List<Departure> departures = createList();
    private int selectedDeparture;

    // getters
    public AbstractTableModel getAllDepartures() {
        return new FileAdapter(departures);
    }

    public int getIndexSelectedDeparture() {
        return this.selectedDeparture;
    }

    public Departure getSelectedDeparture() {
        return departures.get(selectedDeparture);
    }

    // setters
    public void setSelectedDeparture(int i) {
        this.selectedDeparture = i;
        notifyObservers();
    }

    public void editDeparture(int column, String s) {
        Departure d = departures.get(selectedDeparture);
        try {
            switch (column) {
            case 0:
                d.setDepartureTime(s);
                break;
            case 1:
                d.setTrip(s);
                break;
            case 2:
                d.setDestination(s);
                break;
            case 3:
                d.setVia(s);
                break;
            case 4:
                d.setTrack(s);
                break;
            case 5:
                d.setStatus(s);
                break;
            }
            System.out.println("Edit Departure New");
            notifyObservers();
            notifyRepaintObservers();
        } catch (Exception e) {
            System.err.println("editDeparture out of bound.");
        }
    }

    public Integer[] searchDeparture(String s) {
        // returns null, if s was not found within departures
        Integer[] result;
        System.out.println(getIndexSelectedDeparture() + "Current Index to start searching");
        Set<Integer> searchResult = new TreeSet<Integer>(); // TreeSet automatically eliminates
                                                            // duplicates & sorts from smallest to
                                                            // biggest
        for (int i = getIndexSelectedDeparture(); i < departures.size(); i++) {
            Departure d = departures.get(i);
            if (d.getDepartureTime().toString().contains(s) || d.getDestination().toString().contains(s)
                    || d.getTrack().toString().contains(s) || d.getTrip().toString().contains(s)
                    || d.getVia().toString().contains(s)) {
                searchResult.add(i);
            }
        }
        try {
            result = searchResult.toArray(new Integer[searchResult.size()]);
        } catch (Exception e) {
            result = null;
        }
        // return Array of searchResult
        return result;
    }

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

    private void notifyRepaintObservers() {
        for (Observer observer : observers) {
            observer.repaint(this);
        }
    }

    private void searchResultObservers() {
        for (Observer observer : observers) {
            observer.searchResult(this);
        }
    }

    // gibt List der eingelesenen Departures aus.
    public List<Departure> createList() {
        System.err.println("started reading file from disk");
        BufferedReader reader = null;
        List<Departure> departureList = new ArrayList<>();
        try {
            String line;
            String status = "hat Einfahrt";
            String emptyTrack = "";
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                String[] fragments = line.split(";");
                try {
                    departureList.add(new Departure(fragments[0], fragments[1], fragments[2], fragments[3],
                            fragments[4], status));
                } catch (ArrayIndexOutOfBoundsException e) {
                    departureList.add(new Departure(fragments[0], fragments[1], fragments[2], fragments[3], emptyTrack,
                            status));
                }
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return departureList;
    }

    private final File getFile() {
        final File file;
        final URL resource = DepartureModel.class.getResource("olten.txt");
        file = new File(URLDecoder.decode(resource.getFile()));
        return file;
    }

    public class FileAdapter extends AbstractTableModel {

        private List<Departure> departure;
        private int rowCount;

        public FileAdapter(List<Departure> departure) {
            this.departure = new ArrayList<Departure>(departure);
            // specify rowCount static to avoid too many calls on Arraylist.
            rowCount = departure.size() - 1;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public int getRowCount() {
            return rowCount;
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int column) {
            Departure firstRow = departure.get(0);
            String s = "";
            switch (column) {
            case 0:
                s = firstRow.getDepartureTime().toString();
                break;
            case 1:
                s = firstRow.getTrip().toString();
                break;
            case 2:
                s = firstRow.getDestination().toString();
                break;
            case 3:
                s = "Status";
                break;
            }
            return s;
        }

        @Override
        public Object getValueAt(int row, int column) {
            Departure selectedRow = departure.get(row + 1);
            String s = "";
            switch (column) {
            case 0:
                s = selectedRow.getDepartureTime().toString();
                break;
            case 1:
                s = selectedRow.getTrip().toString();
                break;
            case 2:
                s = selectedRow.getDestination().toString();
                break;
            case 3:
                s = selectedRow.getStatus().toString();
                break;
            }
            return s;
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            System.out.println("trying to change row: " + row);
            DepartureModel.this.editDeparture(column, value.toString());
        }

    }

}

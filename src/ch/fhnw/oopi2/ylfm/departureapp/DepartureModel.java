package ch.fhnw.oopi2.ylfm.departureapp;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

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

    // departurelist and selected departure here
    public AbstractTableModel getAllDepartures() {
        return new FileAdapter(departures);
    }

    public Departure getSelectedDeparture() {
        return departures.get(selectedDeparture);
    }

    public int getIndexSelectedDeparture() {
        return this.selectedDeparture;
    }

    public void setSelectedDeparture(int i) {
        this.selectedDeparture = i;
        notifyObservers();
    }
    
    public void updateDetailView(){
        notifyObservers();
    }

    public void setSearchedDeparture(int i) {
        this.selectedDeparture = i;
        searchResultObservers();
    }

    public void editDeparture(String[] s) {
        // get current departure
        Departure d = departures.get(selectedDeparture);
        // change current departure
        if (s[0] != null) {
            d.setDepartureTime(s[0]);
        } else if (s[1] != null) {
            d.setDestination(s[1]);
        } else if (s[2] != null) {
            d.setTrip(s[2]);
        } else if (s[3] != null) {
            d.setTrack(s[3]);
        } else if (s[4] != null) {
            d.setVia(s[4]);
        }
        // save current departure
        departures.set(selectedDeparture, d);
        // repaint jtable by invoking repaint method
        notifyRepaintObservers();
    }

    public Integer[] searchDeparture(String s) {
        //returns null, if s was not found within departures
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
        try{
            result = searchResult.toArray(new Integer[searchResult.size()]);
        }catch(Exception e){
            result = null; 
        }
        // return Array of searchResult
        return result;
    }

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
        System.err.println("file read from disk. row count: " + departureList.size());
        return departureList;
    }

    private final File getFile() {
        final File file;
        final URL resource = DepartureModel.class.getResource("olten.txt");
        file = new File(URLDecoder.decode(resource.getFile()));
        return file;
    }

}

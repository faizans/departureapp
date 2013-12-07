package ch.fhnw.oopi2.ylfm.departureapp;

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

public class DepartureModel implements Observable {
    private final Set<Observer> observers = new HashSet<>();
    private List<Departure> departures = createList();

    // departurelist and selected departure here
    public DefaultTableModel getAllDepartures() {
        return new FileAdapter(departures);
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

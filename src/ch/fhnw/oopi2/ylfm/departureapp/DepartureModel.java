package ch.fhnw.oopi2.ylfm.departureapp;

import javax.swing.table.AbstractTableModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static ch.fhnw.oopi2.ylfm.departureapp.Departure.DEPARTURETIME_PROPERTY;
import static ch.fhnw.oopi2.ylfm.departureapp.Departure.DESTINATION_PROPERTY;
import static ch.fhnw.oopi2.ylfm.departureapp.Departure.STATUS_PROPERTY;
import static ch.fhnw.oopi2.ylfm.departureapp.Departure.TRACK_PROPERTY;
import static ch.fhnw.oopi2.ylfm.departureapp.Departure.TRIP_PROPERTY;
import static ch.fhnw.oopi2.ylfm.departureapp.Departure.VIA_PROPERTY;

public class DepartureModel implements Observable {

    public static final String FILE_NAME = "olten.txt";
    public static final String DEFAULT_TEXT = "abgefahren";
    public static final String EXIT_TEXT = "abgefahren";
    public static final String ENTRY_TEXT = "im Bahnhof";
    private final Set<Observer> observers = new HashSet<>();
    private List<Departure> departures = createList();
    private int selectedDeparture = +1;// bug or FEATURE... somehow a positive selected Departure at
                                       // starttime helps...

    // undo/redo stuff
    private boolean isInputValid = true;
    private boolean isUndoAvailable = false;
    private boolean isRedoAvailable = false;
    private boolean isEditorEnabled = false;

    // departureboads visual stuff
    private boolean boardShowStatus = false;

    // getters
    public AbstractTableModel getAllDepartures() {
        return new FileAdapter(departures);
    }

    public int getIndexSelectedDeparture() {
        return this.selectedDeparture;
    }

    public Departure getSelectedDeparture() {
        try {
            return departures.get(selectedDeparture);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Departure getDeparture(int index) {
        try {
            return departures.get(index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // technically also a getter
    public Integer[] searchDeparture(String s) {
        // returns null, if s was not found within departures
        Integer[] result;
        System.out.println(getIndexSelectedDeparture() + "Current Index to start searching");
        Set<Integer> searchResult = new TreeSet<Integer>(); // TreeSet automatically eliminates
                                                            // duplicates & sorts from smallest to
                                                            // biggest
        for (int i = getIndexSelectedDeparture(); i < departures.size(); i++) {
            Departure d = departures.get(i);
            if (d.getProperty(DEPARTURETIME_PROPERTY).toString().contains(s)
                    || d.getProperty(DESTINATION_PROPERTY).toString().contains(s)
                    || d.getProperty(TRACK_PROPERTY).toString().contains(s)
                    || d.getProperty(TRIP_PROPERTY).toString().contains(s)
                    || d.getProperty(VIA_PROPERTY).toString().contains(s)) {
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

    // setters
    public void setSelectedDeparture(int i) {
        System.err.println("Currently selected Departure was changed.");
        this.selectedDeparture = i;
        notifyObservers();
    }

    public void editDeparture(String property, String value) {
        // test ensures, that upon first edit those settings are not enabled. (Covers GUI
        // enable/disable Editor)
        if (isEditorEnabled) {
            System.err.println("Departure got changed");
            Departure d = departures.get(selectedDeparture);
            d.setProperty(property, value);
            notifyObservers();
            notifyRepaintObservers();
        }
        if (!isEditorEnabled()) {
            setEditorEnabled();
        }

    }

    private boolean isEditorEnabled() {
        return isEditorEnabled;
    }

    public void setEditorEnabled() {
        isEditorEnabled = true;
    }

    // undo/redo stuff ------------------------------------------
    public boolean isInputValid() {
        return isInputValid;
    }

    public void setInputValid(boolean inputValid) {
        if (isInputValid == inputValid) {
            return;
        }
        isInputValid = inputValid;
        notifyObservers();
    }

    public boolean isUndoAvailable() {
        return isUndoAvailable;
    }

    public void setUndoAvailable(boolean undoAvailable) {
        if (isUndoAvailable == undoAvailable) {
            return;
        }
        isUndoAvailable = undoAvailable;
        notifyObservers();
    }

    public boolean isRedoAvailable() {
        return isRedoAvailable;
    }

    public void setRedoAvailable(boolean redoAvailable) {
        if (isRedoAvailable == redoAvailable) {
            return;
        }
        isRedoAvailable = redoAvailable;
        notifyObservers();
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

    public void saveChanges(String fileName) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(fileName);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            for (Departure d : departures) {
                bw.append(d.getProperty(DEPARTURETIME_PROPERTY) + ";" + d.getProperty(TRIP_PROPERTY) + ";"
                        + d.getProperty(DESTINATION_PROPERTY) + ";" + d.getProperty(VIA_PROPERTY) + ";"
                        + d.getProperty(TRACK_PROPERTY) + ";\r\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error occured during flush");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (bw != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private final File getFile() {
        final File file;
        final URL resource = DepartureModel.class.getResource(FILE_NAME);
        file = new File(URLDecoder.decode(resource.getFile()));
        return file;
    }

    public boolean isBoardShowStatus() {
        return boardShowStatus;
    }

    public void setBoardShowStatus(boolean boardShowStatus) {
        this.boardShowStatus = boardShowStatus;
        notifyObservers();
    }

    public class FileAdapter extends AbstractTableModel {

        private static final long serialVersionUID = 1L;
        private List<Departure> departure;
        private int rowCount;

        public FileAdapter(List<Departure> departure) {
            this.departure = new ArrayList<Departure>(departure);
            // specify rowCount static to avoid too many calls on Arraylist.
            rowCount = departure.size() - 1;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return (columnIndex == 3) ? false : true;
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
                s = firstRow.getProperty(DEPARTURETIME_PROPERTY).toString();
                break;
            case 1:
                s = firstRow.getProperty(TRIP_PROPERTY).toString();
                break;
            case 2:
                s = firstRow.getProperty(DESTINATION_PROPERTY).toString();
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
                s = selectedRow.getProperty(DEPARTURETIME_PROPERTY).toString();
                break;
            case 1:
                s = selectedRow.getProperty(TRIP_PROPERTY).toString();
                break;
            case 2:
                s = selectedRow.getProperty(DESTINATION_PROPERTY).toString();
                break;
            case 3:
                s = selectedRow.getProperty(STATUS_PROPERTY).toString();
                break;
            }
            return s;
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            switch (column) {
            case 0:
                System.out.println("edit from table");
                DepartureModel.this.editDeparture(DEPARTURETIME_PROPERTY, value.toString());
                break;
            case 1:
                DepartureModel.this.editDeparture(TRIP_PROPERTY, value.toString());
                break;
            case 2:
                DepartureModel.this.editDeparture(DESTINATION_PROPERTY, value.toString());
                break;
            case 3:
                DepartureModel.this.editDeparture(STATUS_PROPERTY, value.toString());
                break;
            }

        }
    }
}

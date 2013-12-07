package ch.fhnw.oopi2.ylfm.departureapp;

import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends DefaultTableModel {

    private List<Departure> departure;
    private int rowCount;

    public FileAdapter(List<Departure> departure) {
        this.departure = new ArrayList<Departure>(departure);
        // specify rowCount static to avoid too many calls on Arraylist.
        rowCount = departure.size()-1;
    }

    @Override
    public int getRowCount() {
        System.out.println("getRowCount called");
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        System.out.println("getColumntCount called");
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        Departure firstRow = departure.get(0);
        String s = "";
        switch (column) {
            case 0:
                s= firstRow.getDepartureTime().toString();
                break;
            case 1:
                s= firstRow.getTrip().toString();
                break;
            case 2:
                s= firstRow.getDestination().toString();
                break;
            case 3:
                s= "Status";
                break;
        }
        return s;
    }

    @Override
    public Object getValueAt(int row, int column) {
        System.out.println("getValueAt called");
        Departure selectedRow = departure.get(row+1);
        String s = "";
        switch (column) {
            case 0:
                s= selectedRow.getDepartureTime().toString();
                break;
            case 1:
                s= selectedRow.getTrip().toString();
                break;
            case 2:
                s= selectedRow.getDestination().toString();
                break;
            case 3:
                s= selectedRow.getStatus().toString();
                break;
        }
        return s;
    }

}
package ch.fhnw.oopi2.ylfm.departureapp;

import java.awt.BorderLayout;

import javax.swing.*;

public class DepartureController {
    private final DepartureModel model;
    private final MainView mainview;
    private final ToolbarView toolbar;
    private final DetailView detail;
    private final TableView table;
    // how often the same string got searched for
    private int searchCounter = 0;
    private String previousSearch = "";
    Integer[] searchResult;

    public int getSearchCounter() {
        return searchCounter;
    }
    
    public int getIndexSelectedDeparture(){
        return model.getIndexSelectedDeparture();
    }
    public String getPreviousSearch() {
        return previousSearch;
    }

    public void setPreviousSearch(String s) {
        this.previousSearch = s;
    }

    public void increaseSearchCounter() {
        // array out of bounds exception abfangen durch kürzen des Arrays
        this.searchCounter = (this.getSearchCounter() + 1) % searchResult.length;
    }

    public void resetSearchCounter() {
        this.searchCounter = 0;
    }

    public DepartureController(DepartureModel model) {
        this.model = model;
        this.mainview = new MainView(model, this);
        this.toolbar = new ToolbarView(model, this);
        this.detail = new DetailView(model, this);
        this.table = new TableView(model, this);
    }

    public JTable getAllDepartures() {
        return new JTable(model.getAllDepartures());
    }

    public void searchDeparture(String s) {
        if (s.equals("")) {
            System.out.println("empty string");
        } else {
            if (getPreviousSearch().equals(s)) {
                // gleiche Suche wie vorher
                System.out.println("gleiche Suche wie vorher");
                increaseSearchCounter(); // suchindex erhöhen um 1
                model.setSearchedDeparture(searchResult[getSearchCounter()]);
            } else {
                // neue Suche
                resetSearchCounter();
                searchResult = model.searchDeparture(s);
                setPreviousSearch(s);
                model.setSearchedDeparture(searchResult[getSearchCounter()]);
            }
        }
    }

    public String getSelectedDeparture(int i) {
        // returned values from selected Departure
        Departure d = model.getSelectedDeparture();
        String[] result = { d.getDepartureTime(), d.getDestination(), d.getTrip(), d.getTrack(), d.getVia() };
        return result[i];
    }

    public void setSelectedDeparture(int i) {
        model.setSelectedDeparture(i);
    }

    public void editDeparture(String s, int i) {
        // s = string to insert, i = position/value to be changed
        String[] departure = new String[5];
        departure[i] = s;
        model.editDeparture(departure);
    }

    // setSelectedDeparture(int selectedRow)

    // all controllers here

    public void initializeView() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainview.createAndShow();
                mainview.add(toolbar.createAndShow(), BorderLayout.PAGE_START);
                JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, table.createAndShow(), detail
                        .createAndShow());
                splitPane.setResizeWeight(0.9);
                splitPane.setDividerLocation(0.5);
                splitPane.setOneTouchExpandable(true);
                mainview.add(splitPane, BorderLayout.CENTER);
                mainview.pack();
            }
        });
    }
}

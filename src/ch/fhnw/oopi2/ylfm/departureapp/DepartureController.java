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

    // +++++++++++++++++++++++Local methods
    public int getSearchCounter() {
        // locally used variable for search functionality
        return searchCounter;
    }

    public String getPreviousSearch() {
        // locally used variable for search functionality
        return previousSearch;
    }

    public void setPreviousSearch(String s) {
        // locally used variable for search functionality
        this.previousSearch = s;
    }

    public void increaseSearchCounter() {
        // locally used variable for search functionality
        // array out of bounds exception abfangen durch kürzen des Arrays
        this.searchCounter = (this.getSearchCounter() + 1) % searchResult.length;
    }

    public void resetSearchCounter() {
        // locally used variable for search functionality
        this.searchCounter = 0;
    }

    public DepartureController(DepartureModel model) {
        this.model = model;
        this.mainview = new MainView(model, this);
        this.toolbar = new ToolbarView(model, this);
        this.detail = new DetailView(model, this);
        this.table = new TableView(model, this);
    }

    // getters QUESTION!!! does that comply with MVC-Pattern?
    public JTable getAllDepartures() {
        return new JTable(model.getAllDepartures());
    }

    public void searchDeparture(String s) {
        if (s.equals("")) {
            // do nothing as search is empty
        } else {
            if (getPreviousSearch().equals(s) && this.getSearchCounter() != 0) {
                // gleiche Suche wie vorher
                model.setSelectedDeparture(searchResult[getSearchCounter()]);
                increaseSearchCounter();
            } else {
                // neue Suche
                resetSearchCounter();
                searchResult = model.searchDeparture(s);
                try {
                    model.setSelectedDeparture(searchResult[getSearchCounter()]);
                    setPreviousSearch(s);
                    increaseSearchCounter();
                } catch (Exception e) {
                    // do nothing, because s was not found within departures.
                }
            }
        }
    }

    public void setSelectedDeparture(int i) {
        // test ob geändert
        if (model.getIndexSelectedDeparture() != i) {
            model.setSelectedDeparture(i);
        } else {
            // no changes to apply
        }

    }

    public void editDeparture(String property, String newValue) {
        System.out.println();
        if (model.getIndexSelectedDeparture() == -1
                || model.getSelectedDeparture().getProperty(property).toString().equals(newValue)) {
            // when no row is selected or the value has not changed
        } else {
            model.editDeparture(property, newValue);
        }

    }

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

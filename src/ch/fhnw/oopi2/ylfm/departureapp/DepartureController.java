package ch.fhnw.oopi2.ylfm.departureapp;

import java.awt.BorderLayout;

import javax.swing.*;

public class DepartureController {
    private final DepartureModel model;
    private final MainView mainview;
    private final ToolbarView toolbar;
    private final DetailView detail;
    private final TableView table;

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

    public String getSelectedDeparture(int i) {
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

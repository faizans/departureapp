package ch.fhnw.oopi2.ylfm.departureapp;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TableView {
    private final DepartureModel model;
    private final DepartureController controller;

    // declaration of all elements
    private JScrollPane scrollPane;
    private JTable table;

    public TableView(DepartureModel model, DepartureController controller) {
        super();
        this.model = model;
        this.controller = controller;
    }

    public JScrollPane createAndShow() {
        initializeComponents();
        return layoutComponents();
    }

    private void initializeComponents() {
        // initialization here
        table = controller.getAllDepartures();
        scrollPane = new JScrollPane(table);
    }

    //@SuppressWarnings("deprecation")
    private JScrollPane layoutComponents() {
        // layout here
        scrollPane.setMinimumSize(new Dimension(300, 0));
        scrollPane.setPreferredSize(new Dimension(500, 0));
        return scrollPane;
    }

    private void addEvents() {
        model.addObserver(new Observer() {
            @Override
            public void update(Observable m) {
                DepartureModel myModel = (DepartureModel) m;
            }
        });

        // all Events bundled with this view here

    }
}

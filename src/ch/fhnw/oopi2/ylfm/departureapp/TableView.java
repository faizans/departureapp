package ch.fhnw.oopi2.ylfm.departureapp;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
        addEvents();
        return layoutComponents();
    }

    private void initializeComponents() {
        // initialization here
        table = controller.getAllDepartures();
        scrollPane = new JScrollPane(table);
    }

    // @SuppressWarnings("deprecation")
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

            public void repaint(Observable m) {
                table.updateUI();
            }

            public void searchResult(Observable m) {
                table.setRowSelectionInterval(controller.getIndexSelectedDeparture() - 1,
                        controller.getIndexSelectedDeparture() - 1);
                table.scrollRectToVisible(table.getCellRect(controller.getIndexSelectedDeparture() - 1, 0, true));
                // afterwards scroll to visible includes %

            }
        });
        // wenn in der Tabelle eine neue Zeile angewählt wird
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.err.println(table.getSelectedRow());
                controller.setSelectedDeparture(table.getSelectedRow() + 1);
            }
        });

        // all Events bundled with this view here

    }
}

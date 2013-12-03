package ch.fhnw.oopi2.ylfm.departureapp;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TableView extends JTable {
    private final DepartureModel model;
    private final DepartureController controller;

    // declaration of all elements
    private JScrollPane panel;
    private JTable content;

    public TableView(DepartureModel model, DepartureController controller) {
        super();
        this.model = model;
        this.controller = controller;
    }

    public JScrollPane createAndShow() {
        initializeComponents();
        JScrollPane panel = layoutComponents();
        addEvents();
        add(panel);
        return panel;
    }

    private void initializeComponents() {
        // initialization here
        panel = new JScrollPane();
        content = controller.getTableContent();
    }

    @SuppressWarnings("deprecation")
    private JScrollPane layoutComponents() {
        // layout here
        panel.setMinimumSize(new Dimension(300, 0));
        panel.setPreferredSize(new Dimension(500, 0));
        panel.add(content);
        return panel;
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

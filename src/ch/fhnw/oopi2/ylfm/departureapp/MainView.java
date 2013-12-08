package ch.fhnw.oopi2.ylfm.departureapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainView extends JFrame {
    private final DepartureModel model;
    private final DepartureController controller;

    // declaration of all elements
    private JFrame frame;

    public MainView(DepartureModel model, DepartureController controller) {
        super("Abfahrt Bahnhof Olten");
        this.model = model;
        this.controller = controller;
    }

    public void createAndShow() {
        initializeComponents();
        frame = layoutComponents();
        addEvents();
        pack();
        setVisible(true);
    }

    private void initializeComponents() {
        // initialization here
        frame = new JFrame();
    }

    private JFrame layoutComponents() {
        // layout here
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(200, 100));
        frame.setMaximumSize(new Dimension(800, 500));
        frame.setPreferredSize(new Dimension(500, 300));
        return frame;
    }

    private void addEvents() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        model.addObserver(new Observer() {
            @Override
            public void update(Observable m) {
                DepartureModel myModel = (DepartureModel) m;
            }

            @Override
            public void repaint(Observable model) {
                // TODO Auto-generated method stub
                
            }
        });

        // all Events bundled with this view here

    }
}

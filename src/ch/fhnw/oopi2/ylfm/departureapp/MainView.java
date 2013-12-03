package ch.fhnw.oopi2.ylfm.departureapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import programmierprojektYvesLauber.DepartureView;

public class MainView extends JFrame {
    private final DepartureModel model;
    private final DepartureController controller;

    // declaration of all elements
    private JFrame panel;

    public MainView(DepartureModel model, DepartureController controller) {
        super("Abfahrt Bahnhof Olten");
        this.model = model;
        this.controller = controller;
    }

    public void createAndShow() {
        initializeComponents();
        JFrame contents = layoutComponents();
        addEvents();
        
        pack();
        setVisible(true);
    }

    private void initializeComponents() {
        // initialization here
        panel = new JFrame();
    }

    private JFrame layoutComponents() {
        // layout here
        panel.setLayout(new BorderLayout());
        panel.setMinimumSize(new Dimension(200, 100));
        panel.setMaximumSize(new Dimension(800, 500));
        panel.setPreferredSize(new Dimension(500, 300));
        return panel;
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
        });

        // all Events bundled with this view here

    }
}

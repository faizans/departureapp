package ch.fhnw.oopi2.ylfm.departureapp;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DetailView extends JPanel {
    private final DepartureModel model;
    private final DepartureController controller;

    // declaration of all elements
    private JPanel panel;
    private JLabel lblUhrzeit, lblRichtung, lblFahrt, lblGleis, lblVia;
    private JTextField fldUhrzeit, fldRichtung, fldFahrt, fldGleis;
    private JTextArea areaVia;
    private JButton btnEin, btnAus, btnFirstEntry;

    public DetailView(DepartureModel model, DepartureController controller) {
        super();
        this.model = model;
        this.controller = controller;
    }

    public JPanel createAndShow() {
        initializeComponents();
        JPanel panel = layoutComponents();
        addEvents();
        add(panel);
        return panel;
    }

    private void initializeComponents() {
        // initialization here
        panel = new JPanel();
        lblUhrzeit = new JLabel("Uhrzeit", SwingConstants.LEFT);
        lblRichtung = new JLabel("in Richtung", SwingConstants.LEFT);
        lblFahrt = new JLabel("Fahrt", SwingConstants.LEFT);
        lblGleis = new JLabel("Gleis", SwingConstants.LEFT);
        lblVia = new JLabel("�ber", SwingConstants.LEFT);
        fldUhrzeit = new JTextField();
        fldRichtung = new JTextField();
        fldFahrt = new JTextField();
        fldGleis = new JTextField();
        areaVia = new JTextArea();
        btnEin = new JButton("F�hrt Ein");
        btnAus = new JButton("F�hrt Aus");
        btnFirstEntry = new JButton("erster Eintrag auf Abfahrtstafel");
    }

    private JPanel layoutComponents() {
        // layout single components
        panel.setMinimumSize(new Dimension(300, 400));
        panel.setPreferredSize(new Dimension(300, 400));
        panel.setLayout(new GridBagLayout());
        areaVia.setWrapStyleWord(true); // newline only after word end
        areaVia.setLineWrap(true); // enable wrapping
        // put gridbag together - layout left labels
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(10, 15, 0, 15);
        c.gridx = 0;
        c.gridy = 0;
        panel.add(lblUhrzeit, c);
        c.gridy = 1;
        panel.add(lblRichtung, c);
        c.gridy = 2;
        panel.add(lblFahrt, c);
        c.gridy = 3;
        panel.add(lblGleis, c);
        c.gridy = 4;
        panel.add(lblVia, c);
        c.gridy = 5;
        panel.add(btnEin, c);
        // layout right fields
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(fldUhrzeit, c);
        c.gridy = 1;
        panel.add(fldRichtung, c);
        c.gridy = 2;
        panel.add(fldFahrt, c);
        c.gridy = 3;
        panel.add(fldGleis, c);
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 4;
        c.weighty = 1.0;
        panel.add(areaVia, c);
        // layout buttons
        c.fill = GridBagConstraints.NONE;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 5;
        panel.add(btnAus, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 6;
        c.insets = new Insets(10, 15, 10, 15);
        panel.add(btnFirstEntry, c);
        return panel;
    }

    private void addEvents() {
        model.addObserver(new Observer() {
            @Override
            public void update(Observable m) {
                System.err.println("DetailView.update aufgerufen");
                DepartureModel myModel = (DepartureModel) m;
                fldUhrzeit.setText(myModel.getSelectedDeparture().getDepartureTime());
                fldRichtung.setText(myModel.getSelectedDeparture().getDestination());
                fldFahrt.setText(myModel.getSelectedDeparture().getTrip());
                fldGleis.setText(myModel.getSelectedDeparture().getTrack());
                areaVia.setText(myModel.getSelectedDeparture().getVia());
            }

            @Override
            public void repaint(Observable model) {
                // TODO Auto-generated method stub

            }

            @Override
            public void searchResult(Observable model) {
                // TODO Auto-generated method stub

            }
        });
        fldUhrzeit.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent evt) {
                // not used
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                System.err.println("view.keyReleased aufgerufen");
                controller.editDeparture(fldUhrzeit.getText(), 0);
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                // not used

            }
        });
        fldRichtung.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent evt) {
                // not used
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                System.err.println("view.keyReleased aufgerufen");
                controller.editDeparture(fldRichtung.getText(), 1);
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                // not used

            }
        });
        fldFahrt.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent evt) {
                // not used
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                System.err.println("view.keyReleased aufgerufen");
                controller.editDeparture(fldFahrt.getText(), 2);
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                // not used

            }
        });
        fldGleis.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent evt) {
                // not used
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                System.err.println("view.keyReleased aufgerufen");
                controller.editDeparture(fldGleis.getText(), 3);
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                // not used

            }
        });
        areaVia.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent evt) {
                // not used
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                System.err.println("view.keyReleased aufgerufen");
                controller.editDeparture(areaVia.getText(), 4);
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                // not used

            }
        });

        // all Events bundled with this view here

    }
}

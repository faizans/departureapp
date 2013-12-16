package ch.fhnw.oopi2.ylfm.departureapp;

import ch.fhnw.oopi2.ylfm.playground.splitflap.board.ControlPanel;
import ch.fhnw.oopi2.ylfm.playground.splitflap.board.DepartureBoard;
import ch.fhnw.oopi2.ylfm.playground.splitflap.board.Row;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: faizan
 * Date: 11/12/13
 * Time: 21:18
 */
public class DepartureBoardView extends JFrame {
    DepartureBoard board;
    ControlPanel controlPanel;

    DepartureBoardModel model;
    DepartureController controller;

    public DepartureBoardView(DepartureBoardModel model, DepartureController controller) {
        // TODO: refactor to addEvents
        board = new DepartureBoard();
        controlPanel = new ControlPanel();
        this.model = model;
        this.controller = controller;

        ControlPanel.ControlEventListener listener = new ControlPanel.ControlEventListener() {
            public void controlEventPerformed(ControlPanel.ControlEvent event) {
                board.getRows().get(event.getRowIndex()).setBlinking(event.isBlink());
                board.getRows().get(event.getRowIndex()).setHour(event.getHour());
                board.getRows().get(event.getRowIndex()).setMinute(event.getMinute());
                board.getRows().get(event.getRowIndex()).setDestination(event.getDestination());
                board.getRows().get(event.getRowIndex()).setTrack(event.getTrack());
                board.getRows().get(event.getRowIndex()).setInfo(event.getInfo());
            }
        };
        controlPanel.addControlEventListener(listener);

    }

    public JFrame createAndShow() {
        initComponents();
        // TODO: addEvents
        return layoutComponents();
    }

    private void initComponents() {
        setTitle("Swing Departure Board");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagLayout layout = new GridBagLayout();
        getContentPane().setLayout(layout);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

//        gridBagConstraints.gridy = 0;
//        gridBagConstraints.ipady = 10;
//        getContentPane().add(controlPanel, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 0;
        getContentPane().add(board, gridBagConstraints);

        pack();
    }

    private JFrame layoutComponents() {
        this.setSize(870, 355);
        this.setBackground(Color.BLACK);
        this.setLocationRelativeTo(null);
        return this;
    }

    public void updateBoardRow(int index, Departure departure) {
        Row row = board.getRows().get(index);

        int hour = Integer.parseInt(departure.getProperty("departureTime").toString().split(":")[0]);
        int minute =Integer.parseInt(departure.getProperty("departureTime").toString().split(":")[1]);

        row.setHour(hour);
        row.setMinute(minute);
        row.setTrack((String)departure.getProperty("track"));
        row.setDestination((String)departure.getProperty("destination"));
    }

    private void addEvents() {
        model.addObserver(
                new Observer() {
                    @Override
                    public void update(Observable model) {
                        System.out.println("departureModelUpdated");
                    }

                    @Override
                    public void repaint(Observable model) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }
        );
    }
}

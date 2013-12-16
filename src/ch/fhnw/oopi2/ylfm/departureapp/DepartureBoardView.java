package ch.fhnw.oopi2.ylfm.departureapp;

import ch.fhnw.oopi2.ylfm.playground.splitflap.board.ControlPanel;
import ch.fhnw.oopi2.ylfm.playground.splitflap.board.DepartureBoard;
import ch.fhnw.oopi2.ylfm.playground.splitflap.board.Row;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
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

    DepartureController controller;

    public DepartureBoardView(DepartureController controller) {
        // TODO: refactor to addEvents
        board = new DepartureBoard();
        controlPanel = new ControlPanel();
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

    public void clearBoard() {
        for(Row row : board.getRows()) {
            row.setBlinking(false);
            row.setHour(0);
            row.setMinute(0);
            row.setTrack("");
            row.setDestination("");
        }
    }

    public void updateBoardRow(int rowIndex, Departure departure) {
        Row row = board.getRows().get(rowIndex);

        int hour = Integer.parseInt(departure.getProperty("departureTime").toString().split(":")[0]);
        int minute =Integer.parseInt(departure.getProperty("departureTime").toString().split(":")[1]);

        row.setHour(hour);
        row.setMinute(minute);
        row.setTrack((String)departure.getProperty("track"));
        row.setDestination((String)departure.getProperty("destination"));
    }

    public void setBlinking(int rowIndex) {
        Row row = board.getRows().get(rowIndex);

        row.setBlinking(true);
    }
}

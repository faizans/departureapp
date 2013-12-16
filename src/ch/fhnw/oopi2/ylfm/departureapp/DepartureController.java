package ch.fhnw.oopi2.ylfm.departureapp;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import static ch.fhnw.oopi2.ylfm.departureapp.Departure.STATUS_PROPERTY;

public class DepartureController {
    private final DepartureModel model;
    private final MainView mainview;
    private final ToolbarView toolbar;
    private final DetailView detail;
    private final TableView table;

    // undo/redo stuff
    private final Deque<ICommand> undoStack = new ArrayDeque<>();
    private final Deque<ICommand> redoStack = new ArrayDeque<>();

    // how often the same string got searched for
    private int searchCounter = 0;
    private String previousSearch = "";
    Integer[] searchResult;

    // departureboard variables
    ArrayList<Integer> showableDepartures;
    private final DepartureBoardView departureBoard;
    public static final String EXIT_TEXT = "abgefahren";
    public static final String ENTRY_TEXT = "im Bahnhof";

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
        // array out of bounds exception abfangen durch k�rzen des Arrays
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
        this.departureBoard = new DepartureBoardView(this);
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
                increaseSearchCounter();
                setSelectedDeparture(searchResult[getSearchCounter()]);
            } else {
                // neue Suche
                resetSearchCounter();
                searchResult = model.searchDeparture(s);
                try {
                    setPreviousSearch(s);
                    increaseSearchCounter();
                    setSelectedDeparture(searchResult[getSearchCounter()]);
                } catch (Exception e) {
                    // do nothing, because s was not found within departures.
                }
            }
        }
    }

    public void setSelectedDeparture(int i) {
        try {
            setSelectedDepartureUndoRedo(i);
        } catch (Exception e) {
            model.setInputValid(false);
            undoStack.clear();
            redoStack.clear();
            setUndoRedoStatus();
        }
    }

    private void setSelectedDepartureUndoRedo(int newValue) {
        if (model.getIndexSelectedDeparture() != newValue) {
            execute(new SetSelectedDepartureCommand(model, newValue));
        }
    }

    public void editDeparture(String property, String newValue) {
        try {
            editDepartureUndoRedo(property, newValue);
        } catch (Exception e) {
            model.setInputValid(false);
            undoStack.clear();
            redoStack.clear();
            setUndoRedoStatus();
        }
    }

    public void editDepartureUndoRedo(String property, String newValue) {
        if (!model.getSelectedDeparture().getProperty(property).toString().equals(newValue)) {
            execute(new EditDepartureCommand(model, property, newValue));
        }
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("nothing to undo, stack is empty");
            return;
        }
        ICommand cmd = undoStack.pop();
        redoStack.push(cmd);
        setUndoRedoStatus();
        System.err.println("undo ausgeführt");
        cmd.undo();
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("redostack is empty");
            return;
        }
        ICommand cmd = redoStack.pop();
        undoStack.push(cmd);
        setUndoRedoStatus();
        System.err.println("redo ausgeführt");
        cmd.execute();
    }

    private void execute(ICommand cmd) {
        undoStack.push(cmd);
        redoStack.clear();
        setUndoRedoStatus();
        cmd.execute();
    }

    private void setUndoRedoStatus() {
        model.setRedoAvailable(!redoStack.isEmpty());
        model.setUndoAvailable(!undoStack.isEmpty());
    }
    
    public void saveChanges(String filename) {
        model.saveChanges(filename);
        System.out.println("controller.saveChanges");
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
                departureBoard.createAndShow();
            }
        });
    }

    // board actions

    public void showBoard() {
        departureBoard.setVisible(true);
        model.setBoardShowStatus(true);
    }

    public void hideBoard() {
        departureBoard.setVisible(false);
        model.setBoardShowStatus(false);
    }

    public void updateBoard() {
        if (departureBoard.isVisible()) {
            departureBoard.clearBoard();

            showableDepartures = new ArrayList<Integer>();
            int index = model.getIndexSelectedDeparture();
            int counter = 0;
            while (counter < 5) {
                Departure departure = model.getDeparture(index); // ###
                if (departure.getProperty(STATUS_PROPERTY).equals("abgefahren")) {
                    System.out.println("departure " + index + " ist schon abgefahren");
                } else {
                    showableDepartures.add(index);
                    counter++;
                }
                index++;
                if (index > model.getAllDepartures().getRowCount()) {
                    counter = 5;
                }
            }
            int rowcounter = 0;
            for (Integer row : showableDepartures) {
                System.out.println("Departure to Show: " + row);
                departureBoard.updateBoardRow(rowcounter, model.getDeparture(row));
                rowcounter++;
            }
            blinkOutgoing();
        }
    }

    public void blinkOutgoing() {
        for (int i = 0; i < showableDepartures.size(); i++) {
            if (model.getDeparture(showableDepartures.get(i)).getProperty(STATUS_PROPERTY).equals(ENTRY_TEXT)) {
                departureBoard.setBlinking(i);
                System.out.println("departure " + showableDepartures.get(i) + " is blinking");
            }
        }
    }

    public void updateBoardIncoming() {
        editDeparture(STATUS_PROPERTY, ENTRY_TEXT);
        if (departureBoard.isVisible()) {
            blinkOutgoing();
        }
    }

    public void updateBoardOutgoing() {
        editDeparture(STATUS_PROPERTY, EXIT_TEXT);
        if (departureBoard.isVisible()) {
            departureBoard.clearBoard();
            model.setSelectedDeparture(showableDepartures.get(0));
            updateBoard();
        }
    }
}

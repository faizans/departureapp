package ch.fhnw.oopi2.ylfm.departureapp;

import java.awt.BorderLayout;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.*;

public class DepartureController {
    private final DepartureModel model;
    private final MainView mainview;
    private final ToolbarView toolbar;
    private final DetailView detail;
    private final TableView table;
    private final DepartureBoardView departureBoard;

    // undo/redo stuff
    private final Deque<ICommand> undoStack = new ArrayDeque<>();
    private final Deque<ICommand> redoStack = new ArrayDeque<>();

    // how often the same string got searched for
    private int searchCounter = 0;
    private String previousSearch = "";
    Integer[] searchResult;

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
        // array out of bounds exception abfangen durch kï¿½rzen des Arrays
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
        this.departureBoard = new DepartureBoardView();
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

    private void setSelectedDepartureUndoRedo(int newValue) {
        if (model.getIndexSelectedDeparture() != newValue) {
            execute(new SetSelectedDepartureCommand(model, newValue));
        }
    }

    private void execute(ICommand cmd) {
        undoStack.push(cmd);
        redoStack.clear();
        setUndoRedoStatus();
        cmd.execute();
    }

    public void editDeparture(String property, String newValue) {
        System.out.println();
        if (model.getIndexSelectedDeparture() == -1
                || model.getSelectedDeparture().getProperty(property).toString().equals(newValue)) {
            // when no row is selected or the value has not changed
        } else {
            model.editDeparture(property, newValue);
        }
    }

    private void setUndoRedoStatus() {
        model.setRedoAvailable(!redoStack.isEmpty());
        model.setUndoAvailable(!undoStack.isEmpty());
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

    public void showBoard() {
        departureBoard.setVisible(true);
    }

    public void hideBoard() {
        departureBoard.setVisible(false);
    }
}

package ch.fhnw.oopi2.ylfm.departureapp;

import java.awt.Dimension;
import java.awt.event.*;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import programmierprojektYvesLauber.DepartureView;

public class ToolbarView extends JToolBar {
    public static final String SEARCH_TEXT = "Suche ...";
    private static final long serialVersionUID = 1L;
    private final DepartureModel model;
    private final DepartureController controller;

    // declaration of all elements
    private JToolBar toolbar;
    private JToggleButton home;
    private JButton undo;
    private JButton redo;
    private JTextField search;
    final ImageIcon imgHomeOn = createImageIcon(DepartureView.class, "on.png");
    final ImageIcon imgHomeOff = createImageIcon(DepartureView.class, "off.png");
    final ImageIcon imgRedo = createImageIcon(DepartureView.class, "redo-icon.png");
    final ImageIcon imgUndo = createImageIcon(DepartureView.class, "undo-icon.png");

    public ToolbarView(DepartureModel model, DepartureController controller) {
        super();
        this.model = model;
        this.controller = controller;
    }

    public JToolBar createAndShow() {
        initializeComponents();
        JToolBar toolbar = layoutComponents();
        addEvents();
        return toolbar;
    }

    private void initializeComponents() {
        // initialization here
        toolbar = new JToolBar();
        home = new JToggleButton(imgHomeOff);
        undo = new JButton(imgUndo);
        redo = new JButton(imgRedo);
        search = new JTextField();
    }

    private JToolBar layoutComponents() {
        // toolbar big picture
        toolbar.setFloatable(false); // disable dragging the toolbar out of the window.
        // layout components and add to toolbar
        toolbar.add(home);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(undo);
        toolbar.add(redo);
        toolbar.add(Box.createHorizontalStrut(10));
        home.setPressedIcon(imgHomeOn);
        search.setMaximumSize(new Dimension(150, 28));
        search.setPreferredSize(new Dimension(150, 28));
        search.setMinimumSize(new Dimension(60, 28));
        search.setText("Suche ...");
        toolbar.add(search);
        undo.setEnabled(false);
        redo.setEnabled(false);
        return toolbar;
    }

    private void addEvents() {
        model.addObserver(new Observer() {
            @Override
            public void update(Observable m) {
                DepartureModel myModel = (DepartureModel) m;
                undo.setEnabled(myModel.isUndoAvailable());
                redo.setEnabled(myModel.isRedoAvailable());
            }

            @Override
            public void repaint(Observable model) {
                // TODO Auto-generated method stub

            }
        });

        search.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {
                if (search.getText().equals("")) {
                    search.setText(SEARCH_TEXT);
                }
            }

            @Override
            public void focusGained(FocusEvent arg0) {
                if (search.getText().equals(SEARCH_TEXT)) {
                    search.setText("");
                } else {
                    controller.resetSearchCounter();
                    search.selectAll();
                }

            }
        });

        search.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.searchDeparture(search.getText());
            }
        });

        home.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    controller.showBoard();
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    controller.hideBoard();
                }
            }
        });

        undo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.undo();

            }
        });

        redo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.redo();

            }
        });
        // all Events bundled with this view here

    }

    public static ImageIcon createImageIcon(Class callingClass, String path, String... description) {
        URL imgURL = callingClass.getResource(path);
        if (imgURL != null) {
            if (description.length > 0)
                return new ImageIcon(imgURL, description[0]);
            else
                return new ImageIcon(imgURL, "");
        } else {
            // no file not found exception implemented, because file is static imported in Project
            return null;
        }
    }
}

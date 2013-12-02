package programmierprojektYvesLauber;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class DepartureView extends JFrame{
	private final DepartureModel model;
	private final DepartureController controller;
	
	//following declaration of all elements used
	private JToolBar toolbar;
	private JToggleButton home;
	private JButton undo;
	private JButton redo;
	private JTextField search;
	private JLabel lblUhrzeit, lblRichtung, lblFahrt, lblGleis, lblVia;
	private JTextField fldUhrzeit, fldRichtung, fldFahrt, fldGleis;
	private JTextArea areaVia;
	private JButton btnEin, btnAus, btnFirstEntry;
	private JPanel panel;
	private JSplitPane splitPane;
	private JTable content;
	private JPanel rightContent;
	private JScrollPane leftContent;
	
	public DepartureView(DepartureModel model, DepartureController controller) {
		super("Abfahrt Bahnhof Olten");
		this.model = model;
		this.controller = controller;
	}
	
	public void createAndShow() {
		initializeComponents();
		JPanel contents = layoutComponents();
		addEvents();

		add(contents);
		pack();
		setVisible(true);
	}

	private void initializeComponents() {
		panel = new JPanel();
		content = model.getContent();
		rightContent = new JPanel();
		leftContent = new JScrollPane(model.getContent());
		//initialize toolbar
		toolbar = new JToolBar();
		final ImageIcon imgHomeOn = createImageIcon(DepartureView.class, "on.png");
		final ImageIcon imgHomeOff = createImageIcon(DepartureView.class, "off.png");
		final ImageIcon imgRedo = createImageIcon(DepartureView.class, "redo-icon.png");
		final ImageIcon imgUndo = createImageIcon(DepartureView.class, "undo-icon.png");
		home = new JToggleButton(imgHomeOff);
		undo = new JButton(imgUndo);
		redo = new JButton(imgRedo);
		search = new JTextField();
		//initialize right
		lblUhrzeit = new JLabel("Uhrzeit", SwingConstants.LEFT);
		lblRichtung = new JLabel("in Richtung", SwingConstants.LEFT);
		lblFahrt = new JLabel("Fahrt", SwingConstants.LEFT);
		lblGleis = new JLabel("Gleis", SwingConstants.LEFT);
		lblVia = new JLabel("über", SwingConstants.LEFT);
		fldUhrzeit = new JTextField();
		fldRichtung = new JTextField();
		fldFahrt = new JTextField();
		fldGleis = new JTextField();
		areaVia = new JTextArea();
		btnEin = new JButton("Fährt Ein");
		btnAus = new JButton("Fährt Aus");
		btnFirstEntry = new JButton("erster Eintrag auf Abfahrtstafel");
		//initialize others
	}
	
	private JPanel layoutComponents() {
		//layout basic controls
		areaVia.setWrapStyleWord(true);	//newline only after word end
		areaVia.setLineWrap(true); //enable wrapping
		rightContent.setLayout(new GridBagLayout());
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftContent, rightContent);
		//layout splitPane
		leftContent.setMinimumSize(new Dimension(300, 0)); //assign minimum size to left and right content.
		rightContent.setMinimumSize(new Dimension(300, 0));
		splitPane.setDividerLocation(0.5);
		splitPane.setOneTouchExpandable(true);
		//layout toolbar
		toolbar.add(home);
		toolbar.add(Box.createHorizontalStrut(10));
		toolbar.add(undo);
		toolbar.add(redo);
		toolbar.add(Box.createHorizontalStrut(10));
		search.setMaximumSize(new Dimension(150,28));
		search.setPreferredSize(new Dimension(150,28));
		toolbar.add(search);
		//layout right Content - layout left labels
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 15, 0, 15);
		c.gridx = 0;
		c.gridy = 0;
		rightContent.add(lblUhrzeit, c);
		c.gridy = 1;
		rightContent.add(lblRichtung, c);
		c.gridy = 2;
		rightContent.add(lblFahrt, c);
		c.gridy = 3;
		rightContent.add(lblGleis, c);
		c.gridy = 4;
		rightContent.add(lblVia, c);
		c.gridy = 5;
		rightContent.add(btnEin, c);
		//layout right fields
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		rightContent.add(fldUhrzeit, c);
		c.gridy = 1;
		rightContent.add(fldRichtung, c);
		c.gridy = 2;
		rightContent.add(fldFahrt, c);
		c.gridy = 3;
		rightContent.add(fldGleis, c);
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 4;
		c.weighty = 1.0;
		rightContent.add(areaVia, c);
		//layout buttons
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 5;
		rightContent.add(btnAus, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(10, 15, 10, 15);
		rightContent.add(btnFirstEntry, c);
		//layout left Content
		//content.removeColumn(content.getColumnModel().getColumn(4)); //remove column 3 & 4 from view.
		//content.removeColumn(content.getColumnModel().getColumn(3));
		//layout big picture - panels
		panel.setLayout(new BorderLayout());
		panel.add(toolbar, BorderLayout.PAGE_START);
		panel.add(splitPane, BorderLayout.CENTER);
		return panel;
	}
	
	private void addEvents() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int answer = JOptionPane.showConfirmDialog(
						DepartureView.this,
						"Departure App wirklich beenden? Allfällige Änderungen gehen verloren.",
						"Confirm",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE
				);
				if (answer == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
				
		//wenn in der Tabelle ein neues Feld angewählt wird
		content.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				controller.setFields();
				}
		});
		fldRichtung.addKeyListener(new KeyListener() {
					
			@Override
			public void keyTyped(KeyEvent evt) {
				//not used
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				System.out.println("view.keyReleased aufgerufen");
				controller.changeValue(fldRichtung.getText(), 2);	
				}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// not used
				
			}
		});
		
		search.addActionListener(new ActionListener() { //make it run version...
			String previousSearch = "";  //these will survive a new search.
			Set <Integer> searchResult = new TreeSet<Integer>();  //TreeSet automatically eliminates duplicates & sorts from smallest to biggest
			Integer[] foundElements;
			int counter = 0;
			@Override
			public void actionPerformed(ActionEvent e) {
				String value=search.getText();   //these values get reset every time Action is performed
				if(previousSearch.equals(value)){
					//Browse through matches in table
					System.out.println("gleich wie vorher: " + previousSearch);
					content.setRowSelectionInterval(foundElements[counter%foundElements.length], foundElements[counter%foundElements.length]);
					content.scrollRectToVisible(content.getCellRect(foundElements[counter%foundElements.length], 0, true));
					counter++;
				}else{
					System.out.println("new search: " + value);
					//neue Suche
					searchResult.clear();	//löscht vorheriges ergebnis
					counter = 0;
					for(int i=0; i<content.getColumnCount(); i++){ //von 0 bis 4 i=col, j=row
						for(int j=content.getSelectedRow(); j<content.getRowCount(); j++){ //beginnt bei zurzeit selektierter Row
							if(content.getValueAt(j,i).toString().contains(value)){
								searchResult.add(j); //speichert neue Ergebnisse
							}
						}
					}
					//convert data to array, and set focus to first occurence, if nothing found throws exception
					try {
						foundElements = searchResult.toArray(new Integer[searchResult.size()]);
						content.setRowSelectionInterval(foundElements[counter%foundElements.length], foundElements[counter%foundElements.length]);
						content.scrollRectToVisible(content.getCellRect(foundElements[counter%foundElements.length], 0, true));
						counter++;
						previousSearch = value;	
					} catch (Exception e1) {
						search.setBackground(new Color(255,65,65));//System.err.println("no match found");
					}
				}
			}
		});
							
		model.addObserver(new Observer() {
			@Override
			public void update(Observable m) {
				DepartureModel myModel = (DepartureModel) m;
				fldUhrzeit.setText(controller.getFields(0));
				fldRichtung.setText(controller.getFields(2));
				fldFahrt.setText(controller.getFields(1)); 
				fldGleis.setText(controller.getFields(4));
				areaVia.setText(controller.getFields(3));
			}
		});
		
		
		fldUhrzeit.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e){
				//controller.setText(fldUhrzeit.getText(), content.getSelectedRow(), 0);
				//content.setValueAt(fldUhrzeit.getText(), content.getSelectedRow(), 1);
			}
		});
		
		//zusätzliche Listeners und Registrierung der Observer folgen hier.
	}
	public static ImageIcon createImageIcon(Class callingClass, String path, String... description){
		URL imgURL = callingClass.getResource(path);
		if (imgURL != null) {
			if (description.length > 0)
				return new ImageIcon(imgURL, description[0]);
			else
				return new ImageIcon(imgURL, "");
		} else {
			//no file not found exception implemented, because file is static imported in Project
			return null;
		}
	}
}

package ch.fhnw.oopi2.ylfm.departureapp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DepartureView extends JFrame {
	private final DepartureModel model;
	private final DepartureController controller;

	// declaration of all elements
	private JPanel panel;

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
		// initialization here
		panel = new JPanel();
	}

	private JPanel layoutComponents() {
		// layout here
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

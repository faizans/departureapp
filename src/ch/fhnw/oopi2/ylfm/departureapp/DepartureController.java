package ch.fhnw.oopi2.ylfm.departureapp;

import javax.swing.SwingUtilities;

public class DepartureController {
	private final DepartureModel model;
	private final MainView view;

	public DepartureController(DepartureModel model) {
		this.model = model;
		this.view = new MainView(model, this);
	}

	// setSelectedDeparture(int selectedRow)

	// all controllers here

	public void initializeView() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				view.createAndShow();
			}
		});
	}
}

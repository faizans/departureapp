package ch.fhnw.oopi2.ylfm.departureapp;

import javax.swing.SwingUtilities;

public class DepartureController {
	private final DepartureModel model;
	private final DepartureView view;

	public DepartureController(DepartureModel model) {
		this.model = model;
		this.view = new DepartureView(model, this);
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

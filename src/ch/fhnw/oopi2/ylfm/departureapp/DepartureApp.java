package ch.fhnw.oopi2.ylfm.departureapp;

import com.alee.laf.WebLookAndFeel;

public class DepartureApp {

	public static void main(String[] args) {
		WebLookAndFeel.install();
		final DepartureModel model = new DepartureModel();
		final DepartureController controller = new DepartureController(model);
		controller.initializeView(); // controller Aufruf zum startdes guis
	}

}

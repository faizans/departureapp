package programmierprojektYvesLauber;

import com.alee.laf.WebLookAndFeel;


public class DepartureApp {
	public static void main(String[] args) {
		WebLookAndFeel.install();	//Set custom look and feel -->http://weblookandfeel.com/
	    final DepartureModel model = new DepartureModel();
		final DepartureController controller = new DepartureController(model);
		controller.initializeView();
	}

}

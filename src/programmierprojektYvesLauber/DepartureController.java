package programmierprojektYvesLauber;

import javax.swing.SwingUtilities;

public class DepartureController {
	private final DepartureModel model;
	private final DepartureView view;
	
	public DepartureController(DepartureModel model) {
		this.model = model;
		this.view = new DepartureView(model, this);
	}
	
	public void setFields(){
		System.out.println("set fields aufgerufen");
		model.setFields();
	}
	public String getFields(int i){
		System.out.println("controller.getFields aufgerufen");
		return model.getFields(i);
	}
	
	public void changeValue(String value, int column){
		System.out.println("controller.changevalue aufgerufen");
		model.changeValue(value, column);
	}
	
	public void initializeView() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				view.createAndShow();
			}
		});
	}
}

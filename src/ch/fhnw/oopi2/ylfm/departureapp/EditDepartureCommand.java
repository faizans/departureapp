package ch.fhnw.oopi2.ylfm.departureapp;

public class EditDepartureCommand implements ICommand{
    private final DepartureModel model;
    private final String newValue;
    private final String oldValue;
    private final String property;
    
    public EditDepartureCommand(DepartureModel model, String property, String newValue) {
        this.property = property;
        this.model = model;
        this.oldValue = model.getSelectedDeparture().getProperty(property);
        this.newValue = newValue;
    }
    
    @Override
    public void execute() {
        model.editDeparture(property, newValue);
        model.setInputValid(true);
    }

    @Override
    public void undo() {
        model.editDeparture(property, oldValue);
        model.setInputValid(true);
    }

}

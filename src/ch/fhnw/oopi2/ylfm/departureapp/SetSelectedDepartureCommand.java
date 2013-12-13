package ch.fhnw.oopi2.ylfm.departureapp;

public class SetSelectedDepartureCommand implements ICommand {
    private final DepartureModel model;
    private final int newValue;
    private final int oldValue;

    public SetSelectedDepartureCommand(DepartureModel model, int newValue) {
        this.model = model;
        this.oldValue = model.getIndexSelectedDeparture();
        this.newValue = newValue;
    }

    @Override
    public void execute() {
        model.setSelectedDeparture(newValue);
        model.setInputValid(true);
    }

    @Override
    public void undo() {
        model.setSelectedDeparture(oldValue);
        model.setInputValid(true);
    }
}

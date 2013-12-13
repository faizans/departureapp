package ch.fhnw.oopi2.ylfm.departureapp;

public interface ICommand {
    void execute();

    void undo();
}

package ch.fhnw.oopi2.ylfm.departureapp;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: faizan
 * Date: 16/12/13
 * Time: 05:24
 */
public class DepartureBoardModel implements Observable {

    private final Set<Observer> observers = new HashSet<>();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}

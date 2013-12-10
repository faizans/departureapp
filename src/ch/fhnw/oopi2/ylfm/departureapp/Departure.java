package ch.fhnw.oopi2.ylfm.departureapp;

import java.lang.reflect.Field;

public class Departure {

    public static final String DEPARTURETIME_PROPERTY = "departureTime";
    public static final String TRIP_PROPERTY = "trip";
    public static final String DESTINATION_PROPERTY = "destination";
    public static final String VIA_PROPERTY = "via";
    public static final String TRACK_PROPERTY = "track";
    public static final String STATUS_PROPERTY = "status";

    private String departureTime;
    private String trip;
    private String destination;
    private String via;
    private String track;
    private String status;

    public Departure(String departureTime, String trip, String destination, String via, String track, String status) {
        this.departureTime = departureTime;
        this.trip = trip;
        this.destination = destination;
        this.via = via;
        this.track = track;
        this.status = status;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String propertyName) {
        return (T) getValue(getField(propertyName));
    }

    public void setProperty(String propertyName, Object newValue) {
        final Field field = getField(propertyName);
        setValue(field, newValue);
    }

    private Field getField(String propertyName) {
        try {
            return getClass().getDeclaredField(propertyName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("unknown field " + propertyName + " in class " + getClass().getName());
        }
    }

    private Object getValue(Field field) {
        field.setAccessible(true);
        try {
            return field.get(this);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("can't access value of field " + field + " in class "
                    + getClass().getName());
        }
    }

    private void setValue(Field field, Object newValue) {
        field.setAccessible(true);
        try {
            field.set(this, newValue);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("can't access value of field " + field + " in class "
                    + getClass().getName());
        }
    }
}

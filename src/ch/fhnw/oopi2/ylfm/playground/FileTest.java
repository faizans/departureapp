package ch.fhnw.oopi2.ylfm.playground;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import ch.fhnw.oopi2.ylfm.departureapp.DepartureModel;

public class FileTest{
    
    public static void main(String[] args) {
        getData();
    }
    
    public static ArrayList<String> getData() {
        BufferedReader reader = null;
        // write data into ArrayList. if no value is given for a field empty string gets saved
        ArrayList<String> departureTime = new ArrayList<String>();
        ArrayList<String> trip = new ArrayList<String>();
        ArrayList<String> destination = new ArrayList<String>();
        ArrayList<String> via = new ArrayList<String>();
        ArrayList<String> track = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        try {
            String line;
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(getFile())));
            while ((line = reader.readLine()) != null) {
                String[] fragments = line.split(";");

                departureTime.add(fragments[0]);
                trip.add(fragments[1]);
                destination.add(fragments[2]);
                via.add(fragments[3]);
                try {
                    track.add(fragments[4]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    track.add("");
                }

                status.add("");

            }
            System.out.println(status.size());
            
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return destination;
    }

    private final static File getFile() {
        final File file;
        final URL resource = DepartureModel.class.getResource("olten.txt");
        file = new File(URLDecoder.decode(resource.getFile()));
        return file;
    }
}

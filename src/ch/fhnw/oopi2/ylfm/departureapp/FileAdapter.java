package ch.fhnw.oopi2.ylfm.departureapp;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: faizan
 * Date: 06/12/13
 * Time: 06:24
 */
public class FileAdapter extends DefaultTableModel { //Changed from AbstractTableModel --> method addColumn() available
    private final int rowCount;
    private final int colCount;
    private int currentRow = -1;
    private String currentLine = null;

    private final Pattern TAB_PATTERN = Pattern.compile(";");

    public FileAdapter() {
        rowCount = countRows();
        colCount = countColumns();
    }

    private final File getFile(){
        final File file;
        final URL resource = DepartureModel.class.getResource("olten.txt");
        file = new File(URLDecoder.decode(resource.getFile()));
        return file;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return colCount;
    }

    @Override
    public String getColumnName(int column) {
        return getColValue(getLine(0), column);
    }

    @Override
    public Object getValueAt(int row, int column) {
        return getColValue(getLine(row + 1), column);
    }

    private String getColValue(String line, int column) {
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(TAB_PATTERN);
        String value = null;
        for (int i = -1; i < column; i++) {
            value = scanner.next();
        }
        scanner.close();
        return value;
    }

    private String getLine(int row) { //complete
        if (currentRow == row) {
            return currentLine;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), "UTF-8"));
            for (int i = 0; i < row; i++) {
                reader.readLine();
            }
            currentLine = reader.readLine();
            currentRow = row;
            return currentLine;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    private int countRows() {  //complete
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(getFile()), "UTF-8"));
            while (reader.readLine() != null) {
            }
            return reader.getLineNumber() - 1;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private int countColumns() { //complete, tried to rewrite using buffered reader --> no benefit found
        final Scanner scanner = new Scanner(getLine(0));
        scanner.useDelimiter(TAB_PATTERN);
        int counter = 0;
        while (scanner.hasNext()) {
            scanner.next();
            counter++;
        }
        scanner.close();
        return counter;
    }
}
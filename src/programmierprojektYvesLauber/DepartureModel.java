package programmierprojektYvesLauber;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.View;
import javax.swing.text.AbstractDocument.Content;


//table wird hier initialisiert und mittels model.getContent() von View aufgerufen.
public class DepartureModel implements Observable{
	private final Set<Observer> observers = new HashSet<>();
	public JTable table = this.setContent();
	private final File getFile(){
		final File file;
		final URL resource = DepartureModel.class.getResource("olten.txt");
		file = new File(URLDecoder.decode(resource.getFile()));
		return file;
	}

	public JTable getContent(){
		return table;
	}

	public JTable setContent() {
		JTable table = new JTable(new FileAdapter());
		return  table;
	}	
	
	private class FileAdapter extends DefaultTableModel{ //Changed from AbstractTableModel --> method addColumn() available
		private final int rowCount;
		private final int colCount;
		private int currentRow = -1;
		private String currentLine = null;

		private final Pattern TAB_PATTERN = Pattern.compile(";");

		private FileAdapter() {
			rowCount = countRows();
			colCount = countColumns();
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
	
	public void setFields(){
		notifyObservers();
	}
	
	public String getFields(int i){	
		return (String)table.getValueAt(table.getSelectedRow(), i);
	}
	
	public void changeValue(String value, int column){
		//table.setValueAt(value, table.getSelectedRow(), column);
		table.setValueAt("test", 0, 0);
		System.out.println("object saved to table");
		//notifyObservers();
	}
	
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

package ch.fhnw.oopi2.ylfm.playground;

import java.awt.BorderLayout;
import java.awt.Component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * @author Dieter Holz
 */
public class TableExample {

	public static void main(String[] args) {
		final TableExample tableExample = new TableExample();
		tableExample.getAllData();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableExample.createAndShowUI();
			}
		});
	}

	private static final int NR_COL = 0;
	private static final int FIRSTNAME_COL = 1;
	private static final int LASTNAME_COL = 2;

	private List<Person> allPersons;

	public TableExample() {
		 getAllData();
	}

	private void getAllData() {
		allPersons = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			allPersons.add(new Person(i, "Unteregger-" + i, "Urs-" + i));
		}
	}

	private void createAndShowUI() {
		final JFrame frame = new JFrame("Table Example");
		frame.setLayout(new BorderLayout());

		frame.add(createTablePanel(), BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private Component createTablePanel() {
		final TableModel model = new PersonListAdapter();
		JTable table = new JTable(model);

		final TableColumn col = table.getColumnModel().getColumn(NR_COL);
		col.setMaxWidth(40);
		col.setResizable(false);

		return new JScrollPane(table);
	}

	private class PersonListAdapter extends AbstractTableModel {
		@Override
		public int getRowCount() {
			return allPersons.size();
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public String getColumnName(int column) {
			switch (column) {
				case NR_COL:
					return "Nr";
				case FIRSTNAME_COL:
					return "Vorname";
				case LASTNAME_COL:
					return "Nachname";
				default:
					throw new IllegalArgumentException("invalid column " + column);
			}
		}

		@Override
		public Object getValueAt(int row, int column) {
			Person person = allPersons.get(row);
			switch (column) {
				case NR_COL:
					return person.getPersonNr();
				case FIRSTNAME_COL:
					return person.getFirstName();
				case LASTNAME_COL:
					return person.getLastName();
				default:
					throw new IllegalArgumentException("invalid column " + column);
			}

		}
	}

	private class Person {
		private int personNr;
		private String lastName;
		private String firstName;

		public Person(int personNr, String lastName, String firstName) {
			this.personNr = personNr;
			this.lastName = lastName;
			this.firstName = firstName;
		}

		@Override
		public String toString() {
			return "Person{" +
					"personNr='" + personNr + '\'' +
					", lastName='" + lastName + '\'' +
					", firstName='" + firstName + '\'' +
					'}';
		}

		public Integer getPersonNr() {
			return personNr;
		}

		public String getLastName() {
			return lastName;
		}

		public String getFirstName() {
			return firstName;
		}
	}

}

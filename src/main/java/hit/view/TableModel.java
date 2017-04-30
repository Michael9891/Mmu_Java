package hit.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private int column = 15;
	private List<String[]> ram;
	
	public TableModel() {
		ram = new ArrayList<String[]>();
		for(int i = 0; i< 14; i++)
			ram.add(new String[column]);
	}

	@Override
	public int getColumnCount() {
		return column;
	}

	@Override
	public int getRowCount() {
		return 14;
	}

	@Override
	public String getValueAt(int arg0, int arg1) {
		String[] row = ram.get(arg0);
		return row[arg1];
	}
	public void setValueAt(int row, int col,String str) {
		String[] rows = ram.get(row);
		rows[col] =str;
	}

	public String[] getRow(int numRow) {
		
		return ram.get(numRow);
	}
	

}

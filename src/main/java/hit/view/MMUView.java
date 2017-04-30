package hit.view;

import javax.swing.JFrame;
import javax.swing.JTable;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JList;
import java.awt.Font;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import hit.controller.MMUController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JProgressBar;

public class MMUView extends JFrame implements View {
	public static final int BYTES_IN_PAGE = 5;
	public static final int NUM_MMU_PAGES = 20;
	private int ramCapacity;
	private JMenu menu;
	private JMenuBar menuBar;
	private List<String> selectedProc;
	private MyRenderer renderer;
	private int prevCurrentColTable = -1;
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JTextField countPageFault;
	private JTextField countPageR;
	private JList<String> listProcesses;
	private JProgressBar progressBar;
	protected String[] userDetails = null;
	private TableModel tableModel;
	private List<String> commandList = null;
	private int numOfCommand = 0;
	private int pf = 0;
	private int pr = 0;
	private int progValue = 0;
	private JLabel lblNewLabel;
	private ArrayList<String> pageInTable;
	private MMUController controller;

	public void open() {                         // open the graphic interface
		selectedProc = new LinkedList<String>();
		renderer = new MyRenderer();
		pageInTable = new ArrayList<>();
		getContentPane().setForeground(Color.DARK_GRAY);
		getContentPane().setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBackground(Color.BLACK);
		getContentPane().setLayout(null);

		JLabel lbmmu = new JLabel("MMU");
		lbmmu.setForeground(Color.BLUE);
		lbmmu.setFont(new Font("Tahoma", Font.BOLD, 30));
		lbmmu.setBounds(32, 11, 77, 30);
		getContentPane().add(lbmmu);

		JLabel lblProject = new JLabel("Project");
		lblProject.setForeground(Color.RED);
		lblProject.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblProject.setBounds(108, 0, 126, 52);
		getContentPane().add(lblProject);

		tableModel = new TableModel();
		table = new JTable();
		table.setModel(tableModel);
		table.setSurrendersFocusOnKeystroke(true);
		table.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLUE));
		table.setCellSelectionEnabled(true);
		table.setColumnSelectionAllowed(true);
		table.setFillsViewportHeight(true);
		table.setBounds(20, 52, 509, 200);
		getContentPane().add(table);

		JLabel lbPageF = new JLabel("Page fault");
		lbPageF.setFont(new Font("Tahoma", Font.BOLD, 15));
		lbPageF.setForeground(Color.WHITE);
		lbPageF.setBounds(610, 65, 89, 19);
		getContentPane().add(lbPageF);

		countPageFault = new JTextField();
		countPageFault.setBounds(777, 64, 39, 20);
		getContentPane().add(countPageFault);
		countPageFault.setColumns(3);

		JLabel lbPageR = new JLabel("Page replacement");
		lbPageR.setFont(new Font("Tahoma", Font.BOLD, 15));
		lbPageR.setForeground(Color.WHITE);
		lbPageR.setBounds(610, 133, 157, 20);
		getContentPane().add(lbPageR);

		countPageR = new JTextField();
		countPageR.setBounds(777, 133, 39, 20);
		getContentPane().add(countPageR);
		countPageR.setColumns(3);

		String[] procName = { "Process 1", "Process 2", "Process 3", "Process 4", "Process 5", "Process 6",
				"Process 7" };
		listProcesses = new JList<String>(procName);
		listProcesses.setBounds(671, 249, 145, 195);
		listProcesses.setToolTipText("Select pages you want to show ");
		listProcesses.setBackground(Color.BLACK);
		listProcesses.setForeground(Color.WHITE);
		listProcesses.setFont(new Font("Tahoma", Font.BOLD, 21));
		getContentPane().add(listProcesses);
		listProcesses.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectedProc = listProcesses.getSelectedValuesList();
			}
		});

		progressBar = new JProgressBar();
		progressBar.setBounds(20, 420, 509, 14);
		progressBar.setForeground(Color.ORANGE);
		progressBar.setBackground(Color.BLACK);
		getContentPane().add(progressBar);

		JButton play = new JButton("Play");
		play.setBounds(20, 263, 89, 23);
		play.setToolTipText("Load page");
		getContentPane().add(play);
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (commandList != null) {
					if (numOfCommand >= commandList.size()) {
						// play.removeActionListener(this);
						play.setEnabled(false);
						progressBar.setValue(100);
						return;
					}
					setTable(numOfCommand);
					numOfCommand++;
				}
			}
		});

		JButton playAll = new JButton("Play all");
		playAll.setBounds(119, 263, 89, 23);
		playAll.setToolTipText("Load all pages");
		getContentPane().add(playAll);
		playAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (commandList != null) {
					while (numOfCommand < commandList.size()) {
						setTable(numOfCommand);
						tableModel.fireTableDataChanged();
						numOfCommand++;
					}
					progressBar.setValue(100);
				}
			}
		});

		JButton reset = new JButton("Reset");
		reset.setBounds(226, 263, 89, 23);
		getContentPane().add(reset);
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				numOfCommand = 0;
				for (int i = 0; i < BYTES_IN_PAGE + 1; i++)
					for (int j = 0; j < ramCapacity; j++) {
						tableModel.setValueAt(i, j, null);
					}
				if (prevCurrentColTable >= 0)
					table.getColumnModel().getColumn(prevCurrentColTable).setCellRenderer(null);
				progressBar.setValue(0);
				tableModel.fireTableDataChanged();
				progValue = 0;
				prevCurrentColTable = 0;
				pf = 0;
				pr = 0;
				countPageFault.setText("0");
				countPageR.setText("0");
				play.setEnabled(true);

			}
		});

		lblNewLabel = new JLabel("");
		java.awt.Image img = new ImageIcon(this.getClass().getResource("/task.png")).getImage();
		lblNewLabel.setIcon(new ImageIcon(img));
		lblNewLabel.setBounds(20, 297, 509, 112);
		getContentPane().add(lblNewLabel);

		menuBar = new JMenuBar();
		menu = new JMenu("File");
		JMenuItem itemExit = new JMenuItem("exit");
		JMenuItem itemRemote = new JMenuItem("Connect to servere");
		JMenuItem itemLocal = new JMenuItem("Local file");
		setJMenuBar(menuBar);
		menu.add(itemLocal);
		menu.add(itemRemote);
		menu.add(itemExit);
		menuBar.add(menu);
		itemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		itemRemote.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// getContentPane()
				openLoginFrame();

			}
		});
		itemLocal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.inst();

			}
		});
		setSize(900, 550);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void setTable(int count) {                  //get comands and update the table
		progValue +=(100/commandList.size());
		int col = 0;
		String line = commandList.get(count);
		ArrayList<String> page = new ArrayList<String>();
		for (String s : line.split(" "))
			page.add(s);
		String pageToReplace = page.get(page.size() - 1);
		if(count>=ramCapacity){
			for (Integer i = 0; i < ramCapacity; i++) {
				if (pageInTable.get(i).equals(pageToReplace)) {
					if(!(pageToReplace.equals(page.get(1)))){
						pr++;
						countPageR.setText(""+pr);
					}
					col = i;
					pageInTable.remove(col);
					pageInTable.add(col, page.get(1));
					i = ramCapacity;
				}
			}
		}else{
			pf++;
			countPageFault.setText(""+pf);
			col = Integer.parseInt(pageToReplace);
			pageInTable.add(col,page.get(1));
		}
		
		if (!(selectedProc.isEmpty())&& !(selectedProc.contains(page.get(0).substring(0, 1) + "rocess " + page.get(0).substring(1)))) {
			progressBar.setValue(progValue);
			return;
		}
		for (int j = 0; j < BYTES_IN_PAGE + 1; j++){         //insert page to table
			tableModel.setValueAt(j, col, page.get(j + 1));
			progressBar.setValue(progValue);
		}
		table.getColumnModel().getColumn(col).setCellRenderer(renderer);   //painting column
		if(prevCurrentColTable>=0&&col!=prevCurrentColTable)
		table.getColumnModel().getColumn(prevCurrentColTable).setCellRenderer(null);
		tableModel.fireTableDataChanged();
		prevCurrentColTable = col;
	}
	
	public void setController(MMUController mmuController){
		this.controller = mmuController;
	}
	
	public String[] getDetails() {
		return userDetails;
	}

	public int getRamCapacity() {
		return ramCapacity;
	}

	public void setRamCapacity(int ramCapacity) {
		this.ramCapacity = ramCapacity;
	}

	public void setConfiguration(List<String> commands) {
		commandList = commands;
	}
	
	
	public void openLoginFrame() { // login window
		userDetails = null;
		JDialog wind = new JDialog();
		JTextField log = new JTextField("", 8);
		JTextField fileName = new JTextField("", 8);
		JTextField password = new JTextField("", 8);
		JLabel user = new JLabel("User Name");
		JLabel pas = new JLabel("Password");
		JLabel file = new JLabel("File Name");
		JButton enter = new JButton("Login");
		JPanel login = new JPanel();
		wind.setSize(320, 150);
		wind.setLayout(new BorderLayout());
		login.setLayout(new GridLayout(4, 2));
		login.add(user);
		login.add(log);
		login.add(pas);
		login.add(password);
		login.add(file);
		login.add(fileName);
		wind.add(login, BorderLayout.CENTER);
		wind.add(enter, BorderLayout.SOUTH);
		wind.setLocationRelativeTo(null);
		wind.setResizable(false);
		wind.setVisible(true);
		enter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (log.getText().equals("") || password.getText().equals("") || fileName.getText().equals("")) {
					JDialog d = new JDialog();
					d.setSize(200, 150);
					d.setTitle("Warning!"); // if any field empty get warning
											// dialog
					JLabel l = new JLabel("Any field empty");
					d.add(l);
					d.setResizable(false);
					d.setLocationRelativeTo(null);
					d.setVisible(true);
				} else {

					userDetails = new String[3];
					userDetails[0] = log.getText(); // insert data from user
					userDetails[1] = password.getText();
					userDetails[2] = fileName.getText();
					wind.dispose();
					controller.instRemote();
				}
			}
		});

	}

	public class MyRenderer extends DefaultTableCellHeaderRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int col) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			cell.setBackground(Color.RED);
			return cell;
		}
	}

	public void openDialog() {                //if file not found a dialog asking if try again or exit from  programm 
		JDialog d = new JDialog();
		d.setSize(200, 150);
		d.setTitle("Error");
		d.setLayout(new GridBagLayout());
		JButton again = new JButton("Enter");
		JButton exit = new JButton("Exit");
		JLabel l = new JLabel("File not found");
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 0, 0, 5);
		c.gridx = 0;
		c.gridy = 1;
		d.add(again,c);
		c.gridx = 1;
		c.gridy = 1;
		d.add(exit,c);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = new Insets(0, 10, 10, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
		d.add(l,c);
		again.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			   openLoginFrame();
			   d.dispose();
			}
		});
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		d.setLocationRelativeTo(null);
		d.setResizable(false);
		d.setVisible(true);
	}

}


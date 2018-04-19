package sql_project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JRadioButton;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.awt.event.ItemEvent;
import java.awt.Color;
import javax.swing.JTable;

public class GUI2 {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private String libraryFrom;
	private String libraryThru;
	private String dateFrom;
	private String dateThru;
	private String provenanceFr;
	private String provenanceTo;
	private SQLHandler handler;
	private String table;
	private ArrayList<String> attr = new ArrayList<String>();
	private int count;
	private ArrayList<String> attrs;
	private ArrayList<String> sections = new ArrayList<String>();
	private JTable table_1;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI2 window = new GUI2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public GUI2() throws SQLException {
		handler = SQLHandler.getSQLHandler();
		initialize();
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.setBounds(100, 100, 771, 633);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.CYAN);
		tabbedPane.setBounds(12, 12, 749, 572);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.ORANGE);
		tabbedPane.addTab("Load Info", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblLibraryFrom = new JLabel("Library From");
		lblLibraryFrom.setBounds(23, 29, 107, 15);
		panel.add(lblLibraryFrom);
		
		JLabel lblLibrarythru = new JLabel("Library Thru");
		lblLibrarythru.setBounds(23, 143, 121, 15);
		panel.add(lblLibrarythru);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(134, 28, 471, 92);
		panel.add(scrollPane);
		
		JList list = new JList();
		list.setBackground(Color.GREEN);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				libraryFrom = list.getSelectedValue().toString();
			}
		});
		scrollPane.setViewportView(list);
		ArrayList<String> libraries = handler.getLibraries();
		list.setModel(new AbstractListModel() {
			
			//String[] values = new String[] {"Rose", "Carrier", "music", "boom", "bam"};
			public int getSize() {
				return libraries.size();
			}
			public Object getElementAt(int index) {
				return libraries.get(index);
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(134, 141, 471, 104);
		panel.add(scrollPane_1);
		
		JList list_1 = new JList();
		list_1.setBackground(Color.GREEN);
		list_1.setEnabled(false);
		list_1.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				libraryThru = list_1.getSelectedValue().toString();
			}
		});
		scrollPane_1.setViewportView(list_1);
		list_1.setModel(new AbstractListModel() {
			//String[] values = new String[] {"heaven", "computer", "cs", "cis", "philosophy", "next", "thing", "in ", "the ", "list"};
			public int getSize() {
				return libraries.size();
			}
			public Object getElementAt(int index) {
				return libraries.get(index);
			}
		});
		
		JLabel lblDateFrom = new JLabel("Century From ");
		lblDateFrom.setBounds(23, 291, 107, 15);
		panel.add(lblDateFrom);
		
		JLabel lblDateThrough = new JLabel("Century Thru");
		lblDateThrough.setBounds(387, 291, 107, 15);
		panel.add(lblDateThrough);
		
		textField = new JTextField();
		textField.setBackground(Color.GREEN);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateFrom = textField.getText();
			}
		});
		textField.setBounds(134, 289, 116, 19);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBackground(Color.GREEN);
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateThru = textField_1.getText().toString();
			}
		});
		textField_1.setBounds(491, 289, 114, 19);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblProvenance = new JLabel("Provenance");
		lblProvenance.setBounds(23, 351, 96, 15);
		panel.add(lblProvenance);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(129, 349, 476, 49);
		panel.add(scrollPane_2);
		
		JList list_2 = new JList();
		list_2.setBackground(Color.GREEN);
		list_2.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				provenanceFr = list_2.getSelectedValue().toString();
			}
		});
		scrollPane_2.setViewportView(list_2);
		list_2.setModel(new AbstractListModel() {
			ArrayList<String> values = handler.populateList("SELECT provenanceID FROM Provenance");
			public int getSize() {
				return values.size();
			}
			public Object getElementAt(int index) {
				return values.get(index);
			}
		});
		
		JButton btnNewButton = new JButton("Show Info");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (libraryThru == null) {
					libraryThru = "zzz";
				}
				//String message = " ";
				//message += "library from: " + libraryFrom + '\n';
				//message += "library though: " + libraryThru + '\n';
				//message += "date from: " + dateFrom + '\n';
				//message += "date through: " + dateThru + '\n';
				//message += "provenance" + provenance + '\n';
				sections = handler.executePane1(libraryFrom, libraryThru, dateFrom, dateThru, provenanceFr, provenanceTo); //so instead of being an arrayList of sections its an arraylist of strings, since we would have just called toString
				
				for(String section: sections) {
					JOptionPane.showMessageDialog(null, section , "Section Information", JOptionPane.INFORMATION_MESSAGE);
					int dialogButton = JOptionPane.YES_NO_OPTION;
				    int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to see the next section?","Continue?",dialogButton);
				    if (dialogResult == JOptionPane.NO_OPTION) {
				    	break;
				    }
				}
				
				
				//JOptionPane.showMessageDialog(null, sections.get(1).toString() , "Section Information", JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		btnNewButton.setBounds(305, 486, 117, 25);
		panel.add(btnNewButton);
		
		JCheckBox chckbxEnable = new JCheckBox("Enable");
		chckbxEnable.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chckbxEnable.isSelected()) {
					list_1.setEnabled(true);
				} else {
					list_1.setEnabled(false);
				}
			}
		});
		chckbxEnable.setBounds(613, 175, 129, 23);
		panel.add(chckbxEnable);
		
		JLabel label = new JLabel("(0-9999)");
		label.setBounds(267, 291, 70, 15);
		panel.add(label);
		
		JLabel lblProvenanceThru = new JLabel("Provenance Thru");
		lblProvenanceThru.setBounds(23, 430, 129, 15);
		panel.add(lblProvenanceThru);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(151, 410, 458, 49);
		panel.add(scrollPane_3);
		
		JList list_3 = new JList();
		list_3.setBackground(Color.GREEN);
		scrollPane_3.setViewportView(list_3);
		list_3.setModel(new AbstractListModel() {
			ArrayList<String> values = handler.populateList("SELECT provenanceID FROM Provenance");
			public int getSize() {
				return values.size();
			}
			public Object getElementAt(int index) {
				return values.get(index);
			}
		});
		list_3.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				provenanceTo = list_3.getSelectedValue().toString();
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.PINK);
		tabbedPane.addTab("Insert", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblSelectTable = new JLabel("Select Table");
		lblSelectTable.setBounds(33, 41, 120, 15);
		panel_1.add(lblSelectTable);
		
		JLabel lblSelectMethodOf = new JLabel("Select method of display");
		lblSelectMethodOf.setBounds(33, 283, 178, 15);
		panel_1.add(lblSelectMethodOf);
		
		JLabel lblNewLabel = new JLabel("please select attributes");
		lblNewLabel.setBounds(27, 223, 184, 15);
		panel_1.add(lblNewLabel);
		
		JComboBox comboBox = new JComboBox();
		String[] tables = handler.getTables();
		for(String table: tables) {
			comboBox.addItem(table);
		}
		table = (String) comboBox.getSelectedItem();
		//comboBox.setModel(new DefaultComboBoxModel(handler.getTables()));
		//comboBox.addActionListener(new ActionListener() {
			//public void actionPerformed(ActionEvent e) {
				//table = (String) comboBox.getSelectedItem();
			//}
		//});
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				table = (String) comboBox.getSelectedItem();
				//JOptionPane.showMessageDialog(null, table);
				
			}
		});
		
		comboBox.setBounds(135, 36, 502, 31);
		panel_1.add(comboBox);
		JList list_4 = new JList();
		JCheckBox chckbxCheckIfYou = new JCheckBox("check if you want specific attributes of table");
		chckbxCheckIfYou.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chckbxCheckIfYou.isSelected()) {
					list_4.setEnabled(true);
				}
				else {
					list_4.setEnabled(false);
				}
			}
		});
		//chckbxCheckIfYou.addActionListener(new ActionListener() {
			//public void actionPerformed(ActionEvent e) {
				//list_4.setEnabled(true);
			//}
		//});
		chckbxCheckIfYou.setBounds(186, 93, 378, 23);
		panel_1.add(chckbxCheckIfYou);
		
		JRadioButton rdbtnOrderBy = new JRadioButton("order by");
		rdbtnOrderBy.setBounds(101, 319, 149, 23);
		panel_1.add(rdbtnOrderBy);
		
		JRadioButton rdbtnSortBy = new JRadioButton("select");
		rdbtnSortBy.setBounds(281, 319, 149, 23);
		panel_1.add(rdbtnSortBy);
		
		JRadioButton rdbtnGroupBy = new JRadioButton("group by");
		rdbtnGroupBy.setBounds(452, 319, 149, 23);
		panel_1.add(rdbtnGroupBy);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(209, 169, 443, 85);
		panel_1.add(scrollPane_4);
		
		//JList list_4 = new JList();
		scrollPane_4.setViewportView(list_4);
		list_4.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list_4.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//@SuppressWarnings("deprecation")
				attrs = handler.getAttributes(table);
			}
		});
		
		//JList list_4 = new JList();
		//attrs = handler.getAttributes(table);
		
		list_4.setEnabled(false);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(186, 391, 546, 142);
		panel_1.add(scrollPane_5);
		
		
		table_1 = new JTable();
		scrollPane_5.setViewportView(table_1);
		
		
		JButton btnExecuteQuery = new JButton("Execute Query");
		btnExecuteQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//String sen = "";
				//for(Object attribute: attrs) {
				//	sen += (String)attribute;
				//}
				//String c = "count" + ' ' + count; 
				//JOptionPane.showMessageDialog(null, sen);
				//sen = "";
				
				ListTableModel model;
				//try {
					try {
						model = ListTableModel.createModelFromResultSet(handler.executeStructuredQuery("select", attrs, table, ""));
						table_1.setModel(model);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//table_1 = new JTable(model);
					//table_1.setBounds(186, 391, 546, 142);
					//panel_1.add(table_1);
					
				//} catch (SQLException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				//}
				
				
				
				
				
				
			}
		});
		
		
		
		btnExecuteQuery.setBounds(12, 447, 164, 25);
		panel_1.add(btnExecuteQuery);
		
		JButton btnLoadAttributes = new JButton("Load Attributes");
		btnLoadAttributes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel dlm = new DefaultListModel();
				ArrayList<String> values = handler.getAttributes(table);
				//ListTableModel model = ListTableModel.createModelFromResultSet(handler.executeStructuredQuery("select", attrs, table, ""));
				for(String value: values) {
					dlm.addElement(value);
				}
				list_4.setModel(dlm);
				
			}
		});
		btnLoadAttributes.setBounds(56, 186, 117, 25);
		panel_1.add(btnLoadAttributes);
		
		//String[] attributes = new String[attrs.size()];
		//attrs.toArray(attributes);
		
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblSelectTable_1 = new JLabel("Select table");
		lblSelectTable_1.setBounds(34, 35, 105, 15);
		panel_2.add(lblSelectTable_1);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(146, 30, 521, 24);
		panel_2.add(comboBox_1);
		
		JLabel lblSelectAttributesYou = new JLabel("select attributes you want to insert into the table");
		lblSelectAttributesYou.setBounds(36, 95, 418, 15);
		panel_2.add(lblSelectAttributesYou);
	}
}

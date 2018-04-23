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



/**
 * GUI for sql project
 * there are two tabs in this GUI: one is the section tab which is a search tool for sections in libraries and provenance. In addition
 * you can dictate the date range to limit the number of possibilities.
 * the second tab is a query tool that performs query with where clauses if desired!
 * @author alavibx 
 * @version 4/22/2018
 *
 */
public class GUI2 {

	private JFrame Manuscript_GUI;
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
	private ArrayList<String> attrs = new ArrayList<String>(); ///hold the attributes
	private ArrayList<String> sections = new ArrayList<String>();
	private JTable table_1;
	private ArrayList<String> selected = new ArrayList<String>();
	private JTextField textField_2;
	private String oneAttr;
	private String criteria;
	private String qualifier;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI2 window = new GUI2();
					window.Manuscript_GUI.setVisible(true);
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
		handler = SQLHandler.getSQLHandler(); //establish connection with the server and crete a handler object to use for query
		initialize();	
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		Manuscript_GUI = new JFrame();
		Manuscript_GUI.setResizable(false);
		Manuscript_GUI.setBackground(Color.WHITE);
		Manuscript_GUI.setBounds(100, 100, 775, 724);
		Manuscript_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Manuscript_GUI.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.CYAN);
		tabbedPane.setBounds(12, 12, 749, 674);
		Manuscript_GUI.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(147, 112, 219));
		tabbedPane.addTab("Load Section", null, panel, null);
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
		
		JList<String> list = new JList<String>();
		list.setBackground(new Color(255, 240, 245));
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				libraryFrom = list.getSelectedValue().toString();
			}
		});
		scrollPane.setViewportView(list);
		ArrayList<String> libraries = handler.getLibraries();
		list.setModel(new AbstractListModel<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public int getSize() {
				return libraries.size();
			}
			public String getElementAt(int index) {
				return libraries.get(index);
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(134, 141, 471, 104);
		panel.add(scrollPane_1);
		
		JList list_1 = new JList();
		list_1.setBackground(new Color(255, 240, 245));
		list_1.setEnabled(false);
		list_1.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				libraryThru = list_1.getSelectedValue().toString();
			}
		});
		scrollPane_1.setViewportView(list_1);
		list_1.setModel(new AbstractListModel() {
			public int getSize() {
				return libraries.size();
			}
			public Object getElementAt(int index) {
				return libraries.get(index);
			}
		});
		
		JLabel lblDateFrom = new JLabel("Date From ");
		lblDateFrom.setBounds(23, 268, 107, 15);
		panel.add(lblDateFrom);
		
		JLabel lblDateThrough = new JLabel("Date Thru");
		lblDateThrough.setBounds(385, 268, 107, 15);
		panel.add(lblDateThrough);
		
		textField = new JTextField();
		textField.setBackground(new Color(255, 240, 245));
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateFrom = textField.getText();
			}
		});
		textField.setBounds(144, 266, 116, 19);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBackground(new Color(255, 240, 245));
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateThru = textField_1.getText().toString();
			}
		});
		textField_1.setBounds(491, 266, 114, 19);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblProvenance = new JLabel("Provenance From");
		lblProvenance.setBounds(23, 331, 143, 15);
		panel.add(lblProvenance);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(184, 315, 471, 72);
		panel.add(scrollPane_2);
		
		JList list_2 = new JList();
		list_2.setBackground(new Color(255, 240, 245));
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
				sections = handler.executePane1(libraryFrom, libraryThru, dateFrom, dateThru, provenanceFr, provenanceTo);
				for(String section: sections) {
					JOptionPane.showMessageDialog(null, section , "Section Information", JOptionPane.INFORMATION_MESSAGE);
					int dialogButton = JOptionPane.YES_NO_OPTION;
					if(sections.indexOf(section) != sections.size() - 1) {
						int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to see the next section?","Continue?",dialogButton);
					    if (dialogResult == JOptionPane.NO_OPTION) {
					    	break;
					    }
					}
				    
				}
			}
		});
		btnNewButton.setBounds(312, 508, 117, 25);
		panel.add(btnNewButton);
		
		JCheckBox chckbxEnable = new JCheckBox("Enable");
		chckbxEnable.setBackground(new Color(147, 112, 219));
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
		
		JLabel label = new JLabel("(0-2018)");
		label.setBounds(290, 268, 70, 15);
		panel.add(label);
		
		JLabel lblProvenanceThru = new JLabel("Provenance Thru");
		lblProvenanceThru.setBounds(23, 430, 129, 15);
		panel.add(lblProvenanceThru);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(184, 416, 471, 72);
		panel.add(scrollPane_3);
		
		JList list_3 = new JList();
		list_3.setBackground(new Color(255, 240, 245));
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
		panel_1.setBackground(new Color(147, 112, 219));
		tabbedPane.addTab("Query", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblSelectTable = new JLabel("Select Table");
		lblSelectTable.setBounds(33, 41, 120, 15);
		panel_1.add(lblSelectTable);
		
		JLabel lblSelectMethodOf = new JLabel("select attr");
		lblSelectMethodOf.setBounds(32, 260, 178, 15);
		panel_1.add(lblSelectMethodOf);
		
		JLabel lblNewLabel = new JLabel("select attributes");
		lblNewLabel.setBounds(32, 175, 130, 15);
		panel_1.add(lblNewLabel);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBackground(new Color(255, 240, 245));
		String[] tables = handler.getTables();
		for(String table: tables) {
			comboBox.addItem(table);
		}
		table = (String) comboBox.getSelectedItem();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				table = (String) comboBox.getSelectedItem();
			}
		});
		
		comboBox.setBounds(135, 36, 502, 31);
		panel_1.add(comboBox);
		JList list_4 = new JList();
		list_4.setBackground(new Color(255, 240, 245));
		JCheckBox chckbxCheckIfYou = new JCheckBox("check if you want specific attributes of table");
		chckbxCheckIfYou.setBackground(new Color(147, 112, 219));
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
		
		chckbxCheckIfYou.setBounds(186, 93, 378, 23);
		panel_1.add(chckbxCheckIfYou);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(180, 141, 457, 84);
		panel_1.add(scrollPane_4);
		
		scrollPane_4.setViewportView(list_4);
		list_4.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list_4.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//@SuppressWarnings("deprecation")
				attrs = handler.getAttributes(table);
				Object[] selectAttr = list_4.getSelectedValues();
				selected = new ArrayList<String>();
				for (int i = 0; i < selectAttr.length; i++) {
					if(!selected.contains((String) selectAttr[i])) {
						selected.add((String) selectAttr[i]);
					}
				}
				
			}
		});
				
		list_4.setEnabled(false);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(33, 430, 699, 205);
		panel_1.add(scrollPane_5);
		
		
		table_1 = new JTable();
		table_1.setBackground(new Color(255, 240, 245));
		scrollPane_5.setViewportView(table_1);	
		
		JComboBox comboBox_2 = new JComboBox();
		JButton btnLoadAttributes = new JButton("Load Attributes");
		btnLoadAttributes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel dlm = new DefaultListModel();
				DefaultComboBoxModel dcm = new DefaultComboBoxModel();
				ArrayList<String> values = handler.getAttributes(table);
				oneAttr = values.get(0); 
				comboBox_2.setSelectedItem(values.get(0));
				for(String value: values) {
					dlm.addElement(value);
					dcm.addElement(value);
				}
				list_4.setModel(dlm);
				comboBox_2.setModel(dcm);
				
				
				
			}
		});
		btnLoadAttributes.setBounds(27, 138, 117, 25);
		panel_1.add(btnLoadAttributes);
		
		ArrayList<String> values = handler.getAttributes(table);
		oneAttr = values.get(0); 
		comboBox_2.setSelectedItem(values.get(0));
		
		comboBox_2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				oneAttr = (String) comboBox_2.getSelectedItem();
			}
		});
		comboBox_2.setBounds(181, 255, 456, 24);
		comboBox_2.setEnabled(false);
		panel_1.add(comboBox_2);
		
		@SuppressWarnings("rawtypes")
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setModel(new DefaultComboBoxModel(new String[] {"Starts With", "Ends With", "Is", "Contains"}));
		qualifier = (String) comboBox_3.getSelectedItem();
		comboBox_3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				qualifier = (String) comboBox_3.getSelectedItem();
			}
		});
		
		comboBox_3.setBounds(180, 306, 457, 23);
		comboBox_3.setEnabled(false);
		panel_1.add(comboBox_3);
		
		JLabel lblSelectndAttr = new JLabel("relation");
		lblSelectndAttr.setBounds(33, 310, 120, 15);
		panel_1.add(lblSelectndAttr);
		
		textField_2 = new JTextField();
		textField_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				criteria = textField_2.getText();
			}
		});
		textField_2.setBounds(33, 358, 606, 31);
		textField_2.setEditable(false);
		panel_1.add(textField_2);
		textField_2.setColumns(10);
		
		JCheckBox chckbxWhereEnabled = new JCheckBox("Where enabled");
		chckbxWhereEnabled.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chckbxWhereEnabled.isSelected()) {
					comboBox_2.setEnabled(true);
					comboBox_3.setEnabled(true);
					textField_2.setEditable(true);
				}
				else {
					comboBox_2.setEnabled(false);
					comboBox_3.setEnabled(false);
					textField_2.setEditable(false);
				}
			}
		});
		chckbxWhereEnabled.setBackground(new Color(147, 112, 219));
		chckbxWhereEnabled.setBounds(33, 228, 164, 23);
		panel_1.add(chckbxWhereEnabled);
		
		JButton btnExecuteQuery = new JButton("Execute Query");
		btnExecuteQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				ListTableModel model;
					try {
						if(comboBox_2.isEnabled()) {
							model = ListTableModel.createModelFromResultSet(handler.executeStructuredQuery(selected, table,oneAttr, qualifier, criteria));
						} else {
							model = ListTableModel.createModelFromResultSet(handler.executeStructuredQuery(selected, table));
						}
						
						table_1.setModel(model);
					
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
			}
		});
		btnExecuteQuery.setBounds(33, 401, 164, 25);
		panel_1.add(btnExecuteQuery);
		
		
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblSelectTable_1 = new JLabel("Select table");
		lblSelectTable_1.setBounds(34, 35, 105, 15);
		panel_2.add(lblSelectTable_1);
		
		@SuppressWarnings("rawtypes")
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(146, 30, 521, 24);
		panel_2.add(comboBox_1);
		
		JLabel lblSelectAttributesYou = new JLabel("select attributes you want to insert into the table");
		lblSelectAttributesYou.setBounds(36, 95, 418, 15);
		panel_2.add(lblSelectAttributesYou);
	}
}

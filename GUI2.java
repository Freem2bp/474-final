package sql_project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
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
import javax.swing.JRadioButton;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class GUI2 {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private String libraryFrom;
	private String libraryThru;
	private String dateFrom;
	private String dateThru;
	private String provenance;
	private SQLHandler handler;
	

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
	 */
	public GUI2() {
		handler = SQLHandler.getSQLHandler();
		initialize();
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 775, 656);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 12, 749, 608);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
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
		
		JLabel lblDateFrom = new JLabel("Date From");
		lblDateFrom.setBounds(23, 291, 96, 15);
		panel.add(lblDateFrom);
		
		JLabel lblDateThrough = new JLabel("Date Thru");
		lblDateThrough.setBounds(320, 291, 86, 15);
		panel.add(lblDateThrough);
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateFrom = textField.getText();
			}
		});
		textField.setBounds(136, 289, 114, 19);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateThru = textField_1.getText().toString();
			}
		});
		textField_1.setBounds(424, 289, 114, 19);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblProvenance = new JLabel("Provenance");
		lblProvenance.setBounds(23, 351, 96, 15);
		panel.add(lblProvenance);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(129, 349, 476, 49);
		panel.add(scrollPane_2);
		
		JList list_2 = new JList();
		list_2.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				provenance = list_2.getSelectedValue().toString();
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
				String message = libraryFrom + ' ' + '\n' + libraryThru + ' ' + dateFrom + ' ' + dateThru + ' ' + provenance;
				JOptionPane.showMessageDialog(null, message, "Query", JOptionPane.INFORMATION_MESSAGE);
				handler.executePane1(libraryFrom, libraryThru, dateFrom, dateThru, provenance, "zz");
			}
		});
		btnNewButton.setBounds(300, 527, 117, 25);
		panel.add(btnNewButton);
		
		JLabel lblProvenanceThru = new JLabel("Provenance Thru");
		lblProvenanceThru.setBounds(23, 461, 107, 15);
		panel.add(lblProvenanceThru);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(134, 443, 476, 49);
		panel.add(scrollPane_3);
		
		JList list_3 = new JList();
		list_3.setEnabled(false);
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
			}
		});
		scrollPane_3.setViewportView(list_3);
		
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
		
		JCheckBox chckbxEnable_1 = new JCheckBox("Enable");
		chckbxEnable_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chckbxEnable_1.isSelected()) {
					list_3.setEnabled(true);
				}
				else {
					list_3.setEnabled(false);
				}
			}
		});
		chckbxEnable_1.setBounds(618, 457, 129, 23);
		panel.add(chckbxEnable_1);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Insert", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblSelectTable = new JLabel("Select Table");
		lblSelectTable.setBounds(33, 41, 120, 15);
		panel_1.add(lblSelectTable);
		
		JLabel lblSelectMethodOf = new JLabel("Select method of display");
		lblSelectMethodOf.setBounds(37, 284, 178, 15);
		panel_1.add(lblSelectMethodOf);
		
		JLabel lblNewLabel = new JLabel("please select attributes");
		lblNewLabel.setBounds(12, 170, 184, 15);
		panel_1.add(lblNewLabel);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"hello", "how ", "are ", "you"}));
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
		rdbtnOrderBy.setBounds(103, 339, 149, 23);
		panel_1.add(rdbtnOrderBy);
		
		JRadioButton rdbtnSortBy = new JRadioButton("sort by");
		rdbtnSortBy.setBounds(281, 339, 149, 23);
		panel_1.add(rdbtnSortBy);
		
		JRadioButton rdbtnGroupBy = new JRadioButton("group by");
		rdbtnGroupBy.setBounds(442, 339, 149, 23);
		panel_1.add(rdbtnGroupBy);
		
		//JList list_4 = new JList();
		list_4.setModel(new AbstractListModel() {
			String[] values = new String[] {"make", "sure ", "to ", "select"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list_4.setBounds(194, 169, 443, 85);
		list_4.setEnabled(false);
		panel_1.add(list_4);
	}
}

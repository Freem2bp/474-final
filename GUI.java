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

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import java.awt.event.ItemListener;
import java.io.IOException;
import java.sql.SQLException;
import java.awt.event.ItemEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTable;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Image;
import javax.swing.JRadioButton;



/**
 * GUI for sql project
 * there are two tabs in this GUI: one is the section tab which is a search tool for sections in libraries and provenance. In addition
 * you can dictate the date range to limit the number of possibilities.
 * the second tab is a query tool that performs query with where clauses if desired!
 * 
 * This work complies with the JMU HONOR CODE
 * @author alavibx 
 * @version 4/22/2018
 *
 */
public class GUI {

	private JFrame Manuscript_GUI;
	private JTextField fDateFrom;
	private JTextField fDateTo;
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
	private JTable queryTable;
	private ArrayList<String> selected = new ArrayList<String>();
	private JTextField fCriterion;
	private String oneAttr;
	private String criteria;
	private String qualifier;
	private ArrayList<String> qAttrs = new ArrayList<String>();
	private ArrayList<String> criterion = new ArrayList<String>();
	private ArrayList<String> qualifiers = new ArrayList<String>();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
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
	 * @throws IOException 
	 */
	public GUI() throws SQLException, IOException {
		handler = SQLHandler.getSQLHandler(); //establish connection with the server and crete a handler object to use for query
		initialize();	
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	private void initialize() throws SQLException, IOException {
		Manuscript_GUI = new JFrame();
		Manuscript_GUI.setResizable(false);
		Manuscript_GUI.setBackground(Color.WHITE);
		Manuscript_GUI.setBounds(100, 100, 813, 820);
		Manuscript_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Manuscript_GUI.getContentPane().setLayout(null);
		
		
		
		
		//create a tabbed pane to hold two tabs: one for query and one for loading sections
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.CYAN);
		tabbedPane.setBounds(12, 12, 787, 770);
		Manuscript_GUI.getContentPane().add(tabbedPane);
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////panel 1, sections code/////////////////////////////////////////////////////
		JPanel sectionPanel;
		try {
			Image image1 = ImageIO.read(getClass().getResource("now.jpg"));
			BackgroundPanel panel =
				    new BackgroundPanel(image1, BackgroundPanel.SCALED);
				GradientPaint paint =
				    new GradientPaint(0, 0, Color.BLUE, 600, 0, Color.RED);
				panel.setPaint(paint);
			sectionPanel = panel; 
			
			
		} catch(IllegalArgumentException e ) {
			sectionPanel = new JPanel();
		}
		

		JLabel lblLibraryThru = new JLabel("Library Thru");
		lblLibraryThru.setFont(new Font("eufm10", Font.BOLD, 16));
		lblLibraryThru.setForeground(new Color(255, 255, 255));
		lblLibraryThru.setBounds(25, 227, 107, 15);
		sectionPanel.add(lblLibraryThru);
		
		
		sectionPanel.setBackground(new Color(147, 112, 219));
		tabbedPane.addTab("Load Section", null, sectionPanel, null);
		sectionPanel.setLayout(null);
		
		
		
		////////////////////library from list///////////////////
		JLabel lblLibraryFrom = new JLabel("Library From");
		lblLibraryFrom.setForeground(Color.WHITE);
		lblLibraryFrom.setFont(new Font("eufm10", Font.BOLD, 16));
		lblLibraryFrom.setBounds(25, 72, 107, 15);
		sectionPanel.add(lblLibraryFrom);
		
		//make library list scrollable
		JScrollPane LibraryScrollPane = new JScrollPane();
		LibraryScrollPane.setBounds(134, 28, 608, 92);
		sectionPanel.add(LibraryScrollPane);
		
		JList<String> libraryFromList = new JList<String>(); //list to hold all the libraries
		libraryFromList.setFont(new Font("eufm10", Font.BOLD, 16));
		libraryFromList.setBackground(new Color(255, 240, 245));
		/*
		 * listener for choosing the library in section tab
		 */
		libraryFromList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				libraryFrom = libraryFromList.getSelectedValue().toString();
			}
		});
		LibraryScrollPane.setViewportView(libraryFromList);
		
		ArrayList<String> libraries = handler.getLibraries(); //get all the libraries from database handler objects
		
		/*
		 * model for library which gets populated upon opening the gui with the libraries array which was attained form the database
		 */
		libraryFromList.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			public int getSize() {
				return libraries.size();
			}
			public String getElementAt(int index) {
				return libraries.get(index);
			}
		});
		
		
		////////////////////library thru list///////////////////
		
		//make library list scrollable
		JScrollPane LibraryThruPane = new JScrollPane();
		LibraryThruPane.setBounds(134, 177, 608, 104);
		sectionPanel.add(LibraryThruPane);
		
		JList<String> libraryThruList = new JList<String>();
		libraryThruList.setFont(new Font("eufm10", Font.BOLD, 16));
		libraryThruList.setBackground(new Color(255, 240, 245));
		libraryThruList.setEnabled(false);
		libraryThruList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				libraryThru = libraryThruList.getSelectedValue().toString();
			}
		});
		LibraryThruPane.setViewportView(libraryThruList);
		
		/**
		 * library thru model is the same as library from list
		 */
		libraryThruList.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			public int getSize() {
				return libraries.size();
			}
			public String getElementAt(int index) {
				return libraries.get(index);
			}
		});
		
		
		//make libraryThru list selectable
		JCheckBox chckbxEnable = new JCheckBox("Library Thru");
		chckbxEnable.setFont(new Font("eufm10", Font.BOLD, 16));
		chckbxEnable.setBackground(new Color(0, 0, 0));
		chckbxEnable.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chckbxEnable.isSelected()) {
					libraryThruList.setEnabled(true);
				} else {
					libraryThruList.setEnabled(false);
				}
			}
		});
		chckbxEnable.setBounds(3, 223, 25, 23);
		sectionPanel.add(chckbxEnable);
		
		////////////////////date buttons///////////////////
		
		JLabel lDateFrom = new JLabel("Date From "); 
		lDateFrom.setForeground(Color.WHITE);
		lDateFrom.setFont(new Font("eufm10", Font.BOLD, 16));
		lDateFrom.setBounds(160, 328, 107, 15);
		sectionPanel.add(lDateFrom);
		
		JLabel lDateThrough = new JLabel("Date Thru");
		lDateThrough.setForeground(Color.WHITE);
		lDateThrough.setFont(new Font("eufm10", Font.BOLD, 16));
		lDateThrough.setBounds(473, 327, 107, 15);
		sectionPanel.add(lDateThrough);
		
		fDateFrom = new JTextField();
		fDateFrom.setFont(new Font("eufm10", Font.PLAIN, 16));
		fDateFrom.setBackground(new Color(255, 240, 245));
		fDateFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateFrom = fDateFrom.getText();
			}
		});
		fDateFrom.setBounds(251, 325, 117, 19);
		sectionPanel.add(fDateFrom);
		fDateFrom.setColumns(10);
		
		fDateTo = new JTextField();
		fDateTo.setFont(new Font("eufm10", Font.BOLD, 16));
		fDateTo.setBackground(new Color(255, 240, 245));
		fDateTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateThru = fDateTo.getText().toString();
			}
		});
		fDateTo.setBounds(553, 325, 102, 19);
		sectionPanel.add(fDateTo);
		fDateTo.setColumns(10);
		
		JLabel label = new JLabel("(0-2018)");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("eufm10", Font.BOLD, 16));
		label.setBounds(373, 328, 88, 15);
		sectionPanel.add(label);
		
		////////////////////provenance lists///////////////////
		
		//prov from
		JLabel lblProvenance = new JLabel("Provenance From");
		lblProvenance.setForeground(Color.WHITE);
		lblProvenance.setFont(new Font("eufm10", Font.BOLD, 16));
		lblProvenance.setBounds(23, 418, 143, 15);
		sectionPanel.add(lblProvenance);
		
		//make provenance scrollable
		JScrollPane provenanceFrPane = new JScrollPane();
		provenanceFrPane.setBounds(184, 388, 471, 72);
		sectionPanel.add(provenanceFrPane);
		
		JList<String> provFrList = new JList<String>();
		provFrList.setFont(new Font("eufm10", Font.BOLD, 16));
		provFrList.setBackground(new Color(255, 240, 245));
		
		//listener for the selected item in the provenance list
		provFrList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				provenanceFr = provFrList.getSelectedValue().toString();
			}
		});
		provenanceFrPane.setViewportView(provFrList);
		
		//prov to
		JLabel lblProvenanceThru = new JLabel("Provenance Thru");
		lblProvenanceThru.setForeground(Color.WHITE);
		lblProvenanceThru.setFont(new Font("eufm10", Font.BOLD, 16));
		lblProvenanceThru.setBounds(23, 528, 143, 15);
		sectionPanel.add(lblProvenanceThru);
		
		JScrollPane probToPane = new JScrollPane();
		probToPane.setBounds(184, 499, 471, 72);
		sectionPanel.add(probToPane);
		
		JList<String> provToList = new JList<String>();
		provToList.setFont(new Font("eufm10", Font.BOLD, 16));
		provToList.setBackground(new Color(255, 240, 245));
		probToPane.setViewportView(provToList);
		
		/**
		 * model populates provenance with the provenances attained from the database
		 */
		provToList.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			ArrayList<String> provenances = handler.populateList("SELECT provenanceID FROM Provenance");
			public int getSize() {
				return provenances.size();
			}
			public String getElementAt(int index) {
				return provenances.get(index);
			}
		});
		provToList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				provenanceTo = provToList.getSelectedValue().toString();
			}
		});
		
		/**
		 * model populates provenance with the provenances attained from the database
		 */
		provFrList.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			ArrayList<String> provenances = handler.populateList("SELECT provenanceID FROM Provenance"); ///query to get provenanaces
			public int getSize() {
				return provenances.size();
			}
			public String getElementAt(int index) {
				return provenances.get(index);
			}
		});
		
		
		////////////////////Show info Button Code///////////////////
		/**
		 * upon pressing the button, a number of joptionpane diologues show up, each having information about the sections you chose
		 */
		JButton btnShowInfo = new JButton("Show Info");
		btnShowInfo.setFont(new Font("eufm10", Font.BOLD, 16));
		btnShowInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sections = handler.executePane1(libraryFrom, libraryThru, dateFrom, dateThru, provenanceFr, provenanceTo); //get query from database
				for(String section: sections) {
					JOptionPane.showMessageDialog(null, section , "Section Information", JOptionPane.INFORMATION_MESSAGE);
					//ask if user wants to see another section! if not, the boxes will return early without showing all the results
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
		btnShowInfo.setBounds(314, 623, 201, 43);
		sectionPanel.add(btnShowInfo);
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////panel 2, query code/////////////////////////////////////////////////////
		
		JPanel QueryPanel;	
		try {
			Image image2 = ImageIO.read(getClass().getResource("a.jpg"));
			BackgroundPanel panel2 =
				    new BackgroundPanel(image2, BackgroundPanel.SCALED);
				GradientPaint paint2 =
				    new GradientPaint(0, 0, Color.BLUE, 600, 0, Color.RED);
				panel2.setPaint(paint2);
			QueryPanel = panel2;
		} catch(IllegalArgumentException e) {
			QueryPanel = new JPanel();
		}
		
		JLabel lblWhere = new JLabel("Where");
		lblWhere.setForeground(new Color(255, 255, 255));
		lblWhere.setFont(new Font("eufm10", Font.BOLD, 16));
		lblWhere.setBounds(57, 251, 70, 15);
		QueryPanel.add(lblWhere);
		
		JLabel lblSelectAttributes = new JLabel("Select attributes");
		lblSelectAttributes.setFont(new Font("eufm10", Font.BOLD, 16));
		lblSelectAttributes.setForeground(new Color(255, 255, 255));
		lblSelectAttributes.setBounds(57, 175, 119, 15);
		QueryPanel.add(lblSelectAttributes);
		
		JLabel lblOr = new JLabel("Or");
		lblOr.setFont(new Font("eufm10", Font.BOLD, 16));
		lblOr.setForeground(new Color(255, 255, 255));
		lblOr.setBounds(700, 291, 70, 15);
		QueryPanel.add(lblOr);
		
		JLabel lblAnd = new JLabel("And");
		lblAnd.setForeground(new Color(255, 255, 255));
		lblAnd.setFont(new Font("eufm10", Font.BOLD, 16));
		lblAnd.setBounds(700, 400, 70, 15);
		QueryPanel.add(lblAnd);
		
		JLabel lblMultipleWhere = new JLabel("Multiple Where");
		lblMultipleWhere.setForeground(new Color(255, 255, 255));
		lblMultipleWhere.setFont(new Font("eufm10", Font.BOLD, 16));
		lblMultipleWhere.setBounds(154, 250, 130, 15);
		QueryPanel.add(lblMultipleWhere);
		
		
		
		QueryPanel.setBackground(new Color(147, 112, 219));
		tabbedPane.addTab("Query", null, QueryPanel, null);
		QueryPanel.setLayout(null);
		
		////////////////////Select Table code///////////////////
		
		JLabel lblSelectTable = new JLabel("Select Table");
		lblSelectTable.setFont(new Font("eufm10", Font.BOLD, 16));
		lblSelectTable.setBounds(33, 44, 120, 15);
		QueryPanel.add(lblSelectTable);
		
		JLabel lblSelectAttr = new JLabel("select attr");
		lblSelectAttr.setForeground(new Color(255, 255, 255));
		lblSelectAttr.setFont(new Font("eufm10", Font.BOLD, 16));
		lblSelectAttr.setBounds(33, 291, 178, 15);
		QueryPanel.add(lblSelectAttr);
		
		JComboBox<String> selectTableBox = new JComboBox<String>();
		selectTableBox.setFont(new Font("eufm10", Font.BOLD, 16));
		selectTableBox.setBackground(new Color(255, 240, 245));
		String[] tables = handler.getTables(); //get all the tables from database
		for(String table: tables) {
			selectTableBox.addItem(table);
		}
		
		/**
		 * populate the combo box with all the tables
		 */
		table = (String) selectTableBox.getSelectedItem();
		selectTableBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				table = (String) selectTableBox.getSelectedItem();
			}
		});
		
		selectTableBox.setBounds(164, 36, 606, 31);
		QueryPanel.add(selectTableBox);
		
		////////////////////Select attributes of chosen table code///////////////////
		
		JList<String> selectAttrsList = new JList<String>();
		selectAttrsList.setFont(new Font("eufm10", Font.BOLD, 16));
		selectAttrsList.setBackground(new Color(255, 240, 245));
		JCheckBox chckbxCheckIfYou = new JCheckBox("");
		chckbxCheckIfYou.setForeground(new Color(0, 0, 0));
		chckbxCheckIfYou.setFont(new Font("eufm10", Font.BOLD, 16));
		chckbxCheckIfYou.setBackground(new Color(0, 0, 0));
		
		/**
		 * activate the attributes list only when the user wants to see it 
		 */
		chckbxCheckIfYou.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chckbxCheckIfYou.isSelected()) {
					selectAttrsList.setEnabled(true);
				}
				else {
					selectAttrsList.setEnabled(false);
				}
			}
		});
		
		chckbxCheckIfYou.setBounds(33, 167, 21, 23);
		QueryPanel.add(chckbxCheckIfYou);
		
		//make list scrollable
		JScrollPane selecAttrsPane = new JScrollPane();
		selecAttrsPane.setBounds(180, 141, 590, 84);
		QueryPanel.add(selecAttrsPane);
		
		//populate the list with the attributes
		selecAttrsPane.setViewportView(selectAttrsList);
		selectAttrsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		selectAttrsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//@SuppressWarnings("deprecation")
				attrs = handler.getAttributes(table);
				@SuppressWarnings("deprecation")
				Object[] selectAttr = selectAttrsList.getSelectedValues();
				selected = new ArrayList<String>();
				for (int i = 0; i < selectAttr.length; i++) {
					if(!selected.contains((String) selectAttr[i])) {
						selected.add((String) selectAttr[i]);
					}
				}
				
			}
		});
				
		selectAttrsList.setEnabled(false);
		
		////////////////////Table to hold the result code///////////////////
		
		JScrollPane tablePane = new JScrollPane();
		tablePane.setBounds(12, 472, 758, 259);
		QueryPanel.add(tablePane);
		
		
		queryTable = new JTable(); //table to hold the the query results
		queryTable.setBackground(new Color(255, 240, 245));
		tablePane.setViewportView(queryTable);	
		
		////////////////////load attributes for two lists code + code for creating the combo box to hold the qualifier attr///////////////////
		JComboBox<String> selectedAttributeBox = new JComboBox<String>();
		selectedAttributeBox.setFont(new Font("eufm10", Font.BOLD, 16));
		JButton btnLoadAttributes = new JButton("Load Attributes");
		btnLoadAttributes.setFont(new Font("eufm10", Font.BOLD, 16));
		/**
		 * this listener adds the attributes of the table that was selected to the attributes list and the qualifier attribute
		 * at the same time
		 */
		btnLoadAttributes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<String> dlm = new DefaultListModel<String>();
				DefaultComboBoxModel<String> dcm = new DefaultComboBoxModel<String>();
				ArrayList<String> attributes = handler.getAttributes(table);
				oneAttr = attributes.get(0); 
				selectedAttributeBox.setSelectedItem(attributes.get(0));
				for(String attr : attributes) {
					dlm.addElement(attr);
					dcm.addElement(attr);
				}
				selectAttrsList.setModel(dlm);
				selectedAttributeBox.setModel(dcm);
			}
		});
		btnLoadAttributes.setBounds(33, 102, 164, 25);
		QueryPanel.add(btnLoadAttributes);
		
		/**
		 * code to get the selected attribute from the comboBox;
		 * the first item of the attribute list is chosen by default
		 */
		ArrayList<String> attributes = handler.getAttributes(table);
		oneAttr = attributes.get(0); 
		selectedAttributeBox.setSelectedItem(attributes.get(0));
		selectedAttributeBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				oneAttr = (String) selectedAttributeBox.getSelectedItem();
				
			}
		});
		selectedAttributeBox.setBounds(164, 286, 242, 24);
		selectedAttributeBox.setEnabled(false);
		QueryPanel.add(selectedAttributeBox);
		
		////////////////////comboBox to hold relation code///////////////////
		@SuppressWarnings("rawtypes")
		JComboBox RelatoinBox = new JComboBox();
		RelatoinBox.setFont(new Font("eufm10", Font.BOLD, 16));
		RelatoinBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Starts With", "Ends With", "Is", "Contains"}));
		qualifier = (String) RelatoinBox.getSelectedItem();
		RelatoinBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				qualifier = (String) RelatoinBox.getSelectedItem();
				
			}
		});
		
		RelatoinBox.setBounds(164, 336, 242, 23);
		RelatoinBox.setEnabled(false);
		QueryPanel.add(RelatoinBox);
		
		JLabel lblRelation = new JLabel("relation");
		lblRelation.setForeground(new Color(255, 255, 255));
		lblRelation.setFont(new Font("eufm10", Font.BOLD, 16));
		lblRelation.setBounds(33, 340, 120, 15);
		QueryPanel.add(lblRelation);
		
		
		////////////////////code for entered criterion///////////////////
		fCriterion = new JTextField();
		fCriterion.setFont(new Font("eufm10", Font.PLAIN, 16));
		fCriterion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				criteria = fCriterion.getText();
			}
		});
		fCriterion.setBounds(164, 392, 242, 31);
		fCriterion.setEditable(false);
		QueryPanel.add(fCriterion);
		fCriterion.setColumns(10);
		
		JLabel lblCriterion = new JLabel("criterion");
		lblCriterion.setForeground(new Color(255, 255, 255));
		lblCriterion.setFont(new Font("eufm10", Font.BOLD, 16));
		lblCriterion.setBounds(33, 400, 70, 15);
		QueryPanel.add(lblCriterion);
		
		////////////////////code for enabling the additional where functionality///////////////////
		
		/*
		 * the where sections are only available if the user chooses it
		 * by default, selecting the where qualifier attribute, the relation box, and the criterion are unabled
		 */
		JCheckBox chckbxWhereEnabled = new JCheckBox("");
		chckbxWhereEnabled.setFont(new Font("eufm10", Font.BOLD, 16));
		chckbxWhereEnabled.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chckbxWhereEnabled.isSelected()) {
					selectedAttributeBox.setEnabled(true);
					RelatoinBox.setEnabled(true);
					fCriterion.setEditable(true);
				}
				else {
					selectedAttributeBox.setEnabled(false);
					RelatoinBox.setEnabled(false);
					fCriterion.setEditable(false);
				}
			}
		});
		chckbxWhereEnabled.setBackground(new Color(0, 0, 0));
		chckbxWhereEnabled.setBounds(33, 243, 21, 23);
		QueryPanel.add(chckbxWhereEnabled);
		
		////////////////////Button for add where///////////////////			
		JButton btnAddWhere = new JButton("add where");
		btnAddWhere.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				qualifiers.add(qualifier);
				qAttrs.add(oneAttr);
				criterion.add(criteria);				
			}
		});
		btnAddWhere.setBounds(467, 334, 117, 25);
		btnAddWhere.setEnabled(false);
		QueryPanel.add(btnAddWhere);
		
		
		
		////////////////////Button for and/or///////////////////	
		ButtonGroup buttons = new ButtonGroup();
		
		//and Button
		JRadioButton rdbtnAnd = new JRadioButton("And");
		rdbtnAnd.setBackground(new Color(0, 0, 0));
		rdbtnAnd.setForeground(Color.BLACK);
		rdbtnAnd.setBounds(672, 395, 21, 23);
		rdbtnAnd.setSelected(true);
		
		
		//Or button
		JRadioButton rdbtnOr = new JRadioButton("Or");
		rdbtnOr.setBackground(new Color(0, 0, 0));
		rdbtnOr.setForeground(Color.BLACK);
		rdbtnOr.setBounds(672, 286, 21, 23);
		//rdbtnOr.setSelected(true);
		
		buttons.add(rdbtnAnd);
		buttons.add(rdbtnOr);
		
		QueryPanel.add(rdbtnAnd);
		QueryPanel.add(rdbtnOr);
		rdbtnOr.setEnabled(false);
		rdbtnAnd.setEnabled(false);
		
		
		////////////////////Button for if want than more one where clause///////////////////	
		JCheckBox chckbxCheckIfU = new JCheckBox("check if u want more than one where");
		chckbxCheckIfU.setBackground(new Color(0, 0, 0));
		chckbxCheckIfU.setForeground(Color.BLACK);
		chckbxCheckIfU.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chckbxCheckIfU.isSelected()) {
					btnAddWhere.setEnabled(true);
					rdbtnOr.setEnabled(true);
					rdbtnAnd.setEnabled(true);
				}
				else {
					btnAddWhere.setEnabled(false);
					rdbtnOr.setEnabled(false);
					rdbtnAnd.setEnabled(false);
					qualifiers = new ArrayList<String>();
					qAttrs = new ArrayList<String>();
					criterion = new ArrayList<String>();
				}
			}
		});
		chckbxCheckIfU.setBounds(117, 243, 21, 23);
		QueryPanel.add(chckbxCheckIfU);
		
		
		
		////////////////////Button for getting the query and producing the table///////////////////
		JButton btnExecuteQuery = new JButton("Execute Query");
		btnExecuteQuery.setFont(new Font("eufm10", Font.BOLD, 16));
		
		
		
		/**
		 * this listener uses ListTableModel.java and RowTableModel.java which were obtained as an outside source.
		 * they are used to transform a result set from the database and turn in into a table
		 */
		btnExecuteQuery.addActionListener(new ActionListener() {
			/**
			 * two versions are implemented:
			 * the first version creates the table without a where clause
			 * the second version is only available if the user checks the where button; in this case,
			 * the table is produced with regards to the where clause
			 */
			public void actionPerformed(ActionEvent e){
				ListTableModel model;
					try {
						//if where chosen
						if(selectedAttributeBox.isEnabled()) {
							if(btnAddWhere.isEnabled()) {
								if(rdbtnAnd.isSelected()) {
									model = ListTableModel.createModelFromResultSet(handler.executeStructuredQuery(selected, table,qAttrs, qualifiers, criterion, "AND"));
								}
								else if(rdbtnOr.isSelected()) {
									model = ListTableModel.createModelFromResultSet(handler.executeStructuredQuery(selected, table,qAttrs, qualifiers, criterion, "OR"));
								}
								else {
									model = null;
								}
								
							}
							else {
								//executeStructuredQuery will produce the result set and pass it to createModelFromResultSet
								model = ListTableModel.createModelFromResultSet(handler.executeStructuredQuery(selected, table,oneAttr, qualifier, criteria));
							}
							
						}
						else {
							//simpler version without the where clause	
							model = ListTableModel.createModelFromResultSet(handler.executeStructuredQuery(selected, table));
						}
						
						queryTable.setModel(model);
					
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
			}
		});
		btnExecuteQuery.setBounds(12, 435, 164, 25);
		QueryPanel.add(btnExecuteQuery);		
		
		
		
	}
}

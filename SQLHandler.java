package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * 
 * Modifications: Adopted the Singleton Design Pattern (3/21)
 * 
 * 
 * @author Brian Freeman and Behan Alavi 
 * @version 1.@
 *
 */
public class SQLHandler
{
	private Connection con;
	private Statement stmt;
	
	private static SQLHandler handler;// variable for the Singleton
	private static HashMap<String, String> siglumMap;//mappings of libraries as they appear to their siglums
	
	/**
	 * constructor - Open the MySQL driver & make a connection & a default
	 * statement object
	 * we also now populate a mapping that will be useful later.
	 * 
	 * @param db
	 * @param user
	 * @param pw
	 */
	private SQLHandler()
	{
		if ( !init() )
			System.exit( 1 );
		ResultSet rs = null;
		HashMap<String, String> returner = new HashMap<String, String>();
		try
		{
			rs = stmt.executeQuery("SELECT libSiglum, city, library from Library" );
			
			while (rs.next()) {
				returner.put(rs.getString("city") + " - " + rs.getString("library"),rs.getString("libSiglum"));
			}
			
		siglumMap = returner;	
		
		} // end try
		
		catch ( SQLException e)
		{
			handleSQLError(e, "SELECT libSiglum, library from Library");
		
		} // end catch
		
	} // constructor
	
	/**
	 * Shutdown all resources
	 */
	public void close()
	{
		try
		{
			if ( con != null )
				con.close();
		
			if ( stmt != null )
				stmt.close();
			
		} // end try
		catch ( SQLException e )
		{
			handleSQLError( e, "Closing Resources" );
			System.exit( 2 );
		}
		
	}
	
	/**
	 * A generic populate, to convert a single column result into an arraylist of the the attributes
	 * @param query a standard non protected sql query
	 * @return the ArrayList created
	 */
	public ArrayList<String> populateList(String query) {
		{
			ResultSet rs = null;
			ArrayList<String> returner = new ArrayList<String>();
			try
			{
				rs = stmt.executeQuery( query );
			
			while(rs.next()) {
			returner.add(rs.getString(1));
			}
			} // end try
			
			catch ( SQLException e)
			{
				handleSQLError( e, query );
			
			} // end catch

			
			return returner;
		
		}	
	}
	
	/**
	 * The method behind the Section search pane of the Gui
	 * @param libraryFrom library from, in the absence of libraryTo it is set as a match to libSiglum
	 * @param LibraryTo the end of our between statement
	 * @param dateFrom the first date in a between qualifier
	 * @param dateTo
	 * @param provenance1
	 * @param provenance2
	 * @return
	 */
	public ArrayList<String> executePane1(String libraryFrom, String LibraryTo, String dateFrom, String dateTo, String provenance1, String provenance2) {
		
			ResultSet rs = null;
			ArrayList<String> returnList = new ArrayList<String>();
			String libSiglum1, libSiglum2;
			libSiglum1 = siglumMap.get(libraryFrom);
			libSiglum2 = siglumMap.get(LibraryTo);
			String query = "";
			
			
				
			
			if (LibraryTo == null && dateFrom != null) {
				query = "Select CONCAT(libSiglum,' ',msSiglum) AS siglum, liturgicalOccasion, date, provenanceID From Section where (libSiglum = '" + libSiglum1 + "') "
						+ "AND (date BETWEEN '" + dateFrom + "' AND '" + dateTo + "') AND (provenanceID = '" + provenance1 + "')" ;
			} else if(dateFrom == null  || dateFrom.equals("") || dateTo == null || dateTo.equals("")) {
				query = "Select CONCAT(libSiglum,' ',msSiglum) AS siglum, liturgicalOccasion, date, provenanceID From Section where (libSiglum = '" + libSiglum1 + "') "
						+ "AND (provenanceID = '" + provenance1 + "')" ;  
			} else {
				query = "Select CONCAT(libSiglum,' ',msSiglum) AS siglum, liturgicalOccasion, date, provenanceID From Section where (libSiglum BETWEEN '" + libSiglum1 + "' AND '" + libSiglum2 + "') "
						+ "AND (date BETWEEN '" + dateFrom + "' AND '" + dateTo + "') AND (provenanceID BETWEEN '" + provenance1 + "' AND '" + provenance2 + "')" ;
			}
			
			
			
			try
			{
				rs = stmt.executeQuery(query);
				String siglum, lio, date, prov;
				while (rs.next()) {
					siglum = rs.getString("siglum");

					lio = rs.getString("liturgicalOccasion");
					date = rs.getString("date");
					prov = rs.getString("provenanceID");
					
					if (lio == null || lio == "null") {
						lio = "?";
					}
					if (date == null || date == "null") {
						date = "?";
					}
					if (prov == null || prov == "null") {
						prov = "?";
					}
					
					returnList.add(new Section("1", siglum, lio, date, prov).toString());
				}
				if (returnList.isEmpty()) {
					returnList.add("Your search yielded no results");
				}
				
				
				
			} // end try
			
			catch ( SQLException e)
			{
				handleSQLError( e, query );
			
			} // end catch
			
			return returnList;	
	}
	
	/**
	 * @param attributes a list of attributes the user wants to see output
	 * @param table the origin table to get the attributes
	 * @param qualifier the relation of the criteria
	 * @param qualifierAtt the attribute that we will be restricting on
	 * @param criteria the search 
	 * @return a resultset formed from the query
	 *  reads as {"chantID" "FeastID}, Chant, leafNumber, Ends With, r 
	 */
	public ResultSet executeStructuredQuery(ArrayList<String> attributes , String table, String qualifierAtt, String qualifier, String criteria) {
		
			ResultSet rs = null;

			String butes;
			if (attributes == null || attributes.isEmpty()) {
			butes = "*";
			}
			else {
			butes = queryString(attributes);
			}
			String fixedCriteria = "%";
			switch (qualifier) { //switch over the qualifier, editing criteria as we see need
			case "Starts With":
				fixedCriteria = criteria + "%";
				break;
			case "Ends With":
				fixedCriteria = "%" + criteria;
				break;
			case "Is":
				fixedCriteria = criteria;
				break;
			case "Contains":
				fixedCriteria = "%" + criteria + "%";
				break;
			}
			
			
			
			
			String query =  "select " + butes + " FROM " + table + " where " + qualifierAtt  + " like '" + fixedCriteria + "'";
			//this query makes use of the fact that like operates identically to = when not using wildcards
			try
			{
				rs = stmt.executeQuery(query);

			} // end try
			
			catch ( SQLException e)
			{
				handleSQLError( e, query );
				
			} // end catch
			
			return rs;	
	}
	
	/**
	 * version of the method that is called if the extra qualifiers are disabled on pane2
	 * @param attributes a list of attributes the user wants to see output
	 * @param table the origin table to get the attributes
	 * @return
	 */
	public ResultSet executeStructuredQuery(ArrayList<String> attributes , String table) {
		
		ResultSet rs = null;

		String butes;
		if (attributes == null || attributes.isEmpty()) {
		butes = "*";
		}
		else {
		butes = queryString(attributes);
		}

		
		
		
		
		String query =  "select " + butes + " FROM " + table;
		//this query makes use of the fact that like operates identically to = when not using wildcards
		try
		{
			rs = stmt.executeQuery(query);

		} // end try
		
		catch ( SQLException e)
		{
			handleSQLError( e, query );
			
		} // end catch
		
		return rs;	
}
	
	/**
	 * a third version that allows for multiple where clauses, if we find a way to incorporate it in the gui.
	 * @param attributes a list of attributes the user wants to see output
	 * @param table the origin table to get the attributes
	 * @param qualifier the relation of the criteria
	 * @param qualifierAtt the attribute that we will be restricting on
	 * @param criteria the search 
	 * @return a resultset formed from the query
	 *  reads as {"chantID" "FeastID}, Chant, leafNumber, Ends With, r 
	 */
	public ResultSet executeStructuredQuery(ArrayList<String> attributes , String table, ArrayList<String> qualifierAtt, ArrayList<String> qualifier, ArrayList<String> criteria) {
		
			ResultSet rs = null;

			String butes;
			if (attributes == null || attributes.isEmpty()) {
			butes = "*";
			}
			else {
			butes = queryString(attributes);
			}
			ArrayList<String> fixedExtra = new ArrayList<String>();
			for (int i = 0; i < qualifier.size(); i++) {
			switch (qualifier.get(i)) { //switch over the qualifier, editing criteria as we see need
			case "Starts With":
				fixedExtra.add("(" + qualifierAtt.get(i) + " like '" + criteria.get(i) + "%')");
				break;
			case "Ends With":
				fixedExtra.add("(" + qualifierAtt.get(i) + " like " + "'%" + criteria.get(i) +"')");
				break;
			case "Is":
				fixedExtra.add("(" + qualifierAtt.get(i) + " like '" + criteria.get(i) + "')");
				break;
			case "Contains":
				fixedExtra.add("(" + qualifierAtt.get(i) + " like " + "'%" + criteria.get(i) + "%')");
				break;
			}
			}
			
			
			
			String query =  "select " + butes + " FROM " + table + " where ";
			for (int j = 0; j < fixedExtra.size(); j++) {
				query += fixedExtra.get(j);
				query += " AND ";
			}
			query += "1=1";
			//this query makes use of the fact that like operates identically to = when not using wildcards
			try
			{
				rs = stmt.executeQuery(query);

			} // end try
			
			catch ( SQLException e)
			{
				handleSQLError( e, query );
				
			} // end catch
			
			return rs;	
	}

	
	/**
	 * Execute a SQL Select statement
	 * 
	 * @param query
	 * @return the result set
	 */
	public ResultSet executeQuery( String query )
	{
		ResultSet rs = null;
		
		try
		{
			rs = stmt.executeQuery( query );
		
		} // end try
		
		catch ( SQLException e)
		{
			handleSQLError( e, query );
		
		} // end catch
		
		return rs;
	
	} // method executeQuery
	
	/**
	 * Execute an Insert/Update/Delete on a table
	 * 
	 * @param query
	 * @return the number of rows affected
	 */
	public int executeUpdate( String query )
	{
		int rows = 0;
		
		try
		{
			rows = stmt.executeUpdate( query );
		
		} // end try
		
		catch ( SQLException e )
		{
			handleSQLError( e, query );
		
		} // end catch
		
		return rows;
	
	} // method executeUpdate

	/**
	 * Get the column heading
	 * 
	 * @param rs
	 * @param col
	 * @return the column heading
	 */
	public String getHeading( ResultSet rs, int col )
	{
		String heading = null;
		
		ResultSetMetaData rsmd = null;
		
		try 
		{
			rsmd = rs.getMetaData();
			heading = rsmd.getColumnLabel( col );
			
		} // end try
		catch ( SQLException e )
		{
			handleSQLError( e, "Get Heading");
		}
		
		return heading;
	}
	
	/**
	 * Return the number of columns in the result set
	 * 
	 * @param rs
	 * @return the number of columns
	 */
	public int getNumColumns( ResultSet rs )
	{
		int numCols = 0;
		ResultSetMetaData rsmd;
		
		try
		{
			rsmd = rs.getMetaData();
			numCols = rsmd.getColumnCount();

		} // end try
		
		catch ( SQLException e )
		{
			handleSQLError( e, "Get Columns" );
		
		} // end catch
		
		return numCols;
		
	} // getColumns
	
	/**
	 * Get the number of rows in the result set
	 * 
	 * @param rs
	 * @return the number of rows
	 */
	public int getNumRows( ResultSet rs )
	{
		int currentRow = 0;
		int lastRow = 0;
		
		try
		{
			currentRow = rs.getRow();	// get cursor row
			rs.last();					// goto end of cursor
			lastRow = rs.getRow();		// get last row number
			
			// reset the cursor row
			if ( currentRow == 0 )
				rs.beforeFirst();
			else
				rs.absolute( currentRow ); // this may not work in MySQL
		
		} // end try
		
		catch ( SQLException e )
		{
			handleSQLError( e, "Get Rows" );
		
		} // end catch
		
		
		return lastRow;
	
	 } // method getNumRows
	
	/**
	 * Return the value of the column in a resultSet as a String
	 * 
	 * @param rs
	 * @param int
	 * @return String
	 */
	public String getString( ResultSet rs, int col )
	{
		String value = null;
		
		try
		{
			value = rs.getString( col );
			
		} // end try
		catch (SQLException e)
		{
			handleSQLError( e, "Get String" );
		} // end catch
		
		return value;
		
	} // method getString
	
	
	/**
	 * Move cursor to the next row of the resultset
	 * 
	 * @param rs
	 * @return boolean
	 */
	public boolean next( ResultSet rs )
	{
		boolean valid = false;
	
		try 
		{
			valid = rs.next();
		
		} // end try
		catch ( SQLException e )
		{
			handleSQLError( e, "Next Row" );
		} // end catch
		
		return valid;
		
	} // method next
	

	
	/**
	 * an unused in the gui method for testing the raw output of some of the queries
	 * @param rs
	 */
	protected void toBasicOutput(ResultSet rs) {
		try {
				
			int count = getNumColumns(rs);
			while (rs.next()) {
			for (int i = 0; i < count; i++) {
			System.out.print(rs.getString(i));
			System.out.print("\t");
			}
			System.out.println("");
			}
		
			
		} catch ( SQLException e )
		{
			handleSQLError( e, "Next Row" );
		} 
	}
	/**
	 * @return an ArrayList with the city - library combo that will fill various dropdowns
	 */
	public ArrayList<String> getLibraries() {
		ArrayList<String> returner = new ArrayList<String>();
		returner.addAll(siglumMap.keySet());
		Collections.sort(returner);
		return returner;
	}
	
	/**
	 * returns an arraylist of strings that are the various columns of a given table in the database
	 * @param table the table we want to see the column headings of
	 * @return 
	 */
	public ArrayList<String> getAttributes(String table) {
		ResultSet rs = null;
		ArrayList<String> returner = new ArrayList<String>();
		rs = this.executeQuery("SELECT * from " + table);
		for (int i = 1; i <= this.getNumColumns(rs); i++) {
			returner.add(this.getHeading(rs, i));
		}
		return returner;
		
	}
	
	/**
	 * @return the table names in the database
	 */
	public String[] getTables() {
		String[] returner = {"Century", "Chant", "Country", "Cursus", "Feast", "Genre", "Illumination", "IlluminationType", "Initial", "Leaf", "Library", "Manuscript", "MasterChant", "Melody", "MSType", "Notation", "Office", "Provenance", "Section", "SourceCompleteness" };
	return returner;
	}

	
	/**
	 * convert an arraylist into a string that is a syntactically correct list of attributes 
	 * @param the arraylist of attributes
	 * @return a list of attributes in a single string
	 */
	public String queryString(ArrayList<String> at) {
		if (at.size() == 1) {
			return at.get(0);
		}
		String returner = "";
		for(int i = 0; i < at.size() - 1;i++) {
			returner +=at.get(i);
			returner += ", ";
		}
		returner += at.get(at.size() - 1);
		return returner;
	}
	
	/**
	 * Generic SQL Error Handler - just print error messages
	 * 
	 * @param e
	 * @param query
	 */
	private void handleSQLError( SQLException e, String query )
	{
    	// handle any errors
    	System.out.println( "SQLException: " + e.getMessage() );
    	System.out.println( "SQLQuery: " + query );
    	System.out.println( "SQLState: " + e.getSQLState() );
    	System.out.println( "VendorError: " + e.getErrorCode() );
		
	} // method handleSQLError
	
	private boolean init()
	{
		boolean success = false;
		
		try
	    {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	         
	        // System.out.println("Driver Loaded");

	        try 
	        {
	        	con = DriverManager.getConnection("jdbc:mysql://mysql.cs.jmu.edu/Manuscript2018",
	                                              "nortonml", "visitatio");
	        	// System.out.println("Connection Made");
	            
	        	stmt = con.createStatement();
	        	
	        } // end try
	        catch ( SQLException e ) 
	        {
	        	handleSQLError( e, "Connection Error" );
	        	System.exit( 3 );
	        	
	        } // end catch
	        
	    } // end try
	    catch ( Exception e )
	    {
	    	System.out.println( "Cannot load driver" ); // handle the error
	    	System.out.println( "Error: " + e.getMessage());
	    	System.out.println( "Errno: " + e.getClass());
	    	
	    } // end catch
	    
	    success = true;
	    
	    return success;

	} // method init
	
	/**************************** static methods ***************************/
	/**
	 * This static method will return the single instance of this class.  If 
	 * the object has not yet been instantiated, it will be instantiated first.
	 * 
	 * @return this object
	 */
	public static SQLHandler getSQLHandler( )
	{
		if ( handler == null )
			handler = new SQLHandler( );
		return handler;
		
	}
		
} // class SQLHandler


// REFERENCES: these methods were initially designed to be utilized, their use has been repurposed elsewhere and these comments exist for our reference
//public ResultSet executeChantSearch(ArrayList<String> butes, String qualifier, String qualifierAtt, String criteria) {
//ResultSet rs = null;
//String fixedAttributes;
//if (butes == null || butes.isEmpty()) {
//fixedAttributes = "*";
//}
//else {
//fixedAttributes = queryString(butes);
//}
//String fixedCriteria = "%";
//switch (qualifier) {//	private HashMap<String,String> populateMap() {     REDACTED METHOD FOR REFERENCE
//
//ResultSet rs = null;
//HashMap<String, String> returner = new HashMap<String, String>();
//try
//{
//	rs = stmt.executeQuery("SELECT libSiglum, city, library from Library" );
//	
//	while (rs.next()) {
//		returner.put(rs.getString("city") + " - " + rs.getString("library"),rs.getString("libSiglum"));
//	}
//	
//	
//
//} // end try
//
//catch ( SQLException e)
//{
//	handleSQLError(e, "SELECT libSiglum, library from Library");
//
//} // end catch
//
//return returner;
//
//}

//case "Starts With":
//	fixedCriteria = criteria + "%";
//	break;
//case "Ends With":
//	fixedCriteria = "%" + criteria;
//	break;
//case "Is":
//	fixedCriteria = criteria;
//	break;
//case "Contains":
//	fixedCriteria = "%" + criteria + "%";
//	break;
//}

//String query =  "Select " + fixedAttributes + " FROM Chant where " + qualifierAtt  + " like '" + fixedCriteria + "'";
//try
//{
//	rs = stmt.executeQuery(query);
//} // end try
//
//catch ( SQLException e)
//{
//	handleSQLError( e, query );
//	
//} // end catch
//
//return rs;
//}
//private HashMap<String,String> populateMap() {     REDACTED METHOD FOR REFERENCE
//
//	ResultSet rs = null;
//	HashMap<String, String> returner = new HashMap<String, String>();
//	try
//	{
//		rs = stmt.executeQuery("SELECT libSiglum, city, library from Library" );
//		
//		while (rs.next()) {
//			returner.put(rs.getString("city") + " - " + rs.getString("library"),rs.getString("libSiglum"));
//		}
//		
//		
//	
//	} // end try
//	
//	catch ( SQLException e)
//	{
//		handleSQLError(e, "SELECT libSiglum, library from Library");
//	
//	} // end catch
//	
//	return returner;
//
//}



package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Class to handle SQL operations
 * ** 3/21/2008 **
 * ** 3/18/2008 **
 * 
 * Modifications: Adopted the Singleton Design Pattern (3/21)
 * 
 * @author Brian Freeman
 * @version 1.0
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
	
	public ArrayList<Section> executePane1(String libraryFrom, String LibraryTo, String dateFrom, String dateTo, String provenanceFrom, String provenanceTo) {
		
			ResultSet rs = null;
			ArrayList<Section> returnList = new ArrayList<Section>();
			String libSiglum1, libSiglum2;
			libSiglum1 = siglumMap.get(libraryFrom);
			libSiglum2 = siglumMap.get(LibraryTo);
			String query = "Select libSiglum + ' ' msSiglum AS siglum, liturgicalOccasion, date, provenanceID From Section where (libSiglum BETWEEN '" + libSiglum1 + "' AND '" + libSiglum2 + "') "
					+ "AND (date BETWEEN '" + dateFrom + "' AND '" + dateTo + "') AND (provenanceID BETWEEN '" + provenanceFrom + "' AND '" + provenanceTo + "')" ;
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
					
					returnList.add(new Section("1", siglum, lio, date, prov));
				}
				
				
				
			} // end try
			
			catch ( SQLException e)
			{
				handleSQLError( e, query );
			
			} // end catch
			
			return returnList;	
	}
	
	public ResultSet executeStructuredQuery(String selOrBy, String[] attributes, String table, String extra) {
		
			ResultSet rs = null;
			String tables = queryString(attributes);
			String query =  selOrBy + " " + tables + " FROM " + table + " " + extra;
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
	
//	private HashMap<String,String> populateMap() {     REDACTED METHOD FOR REFERENCE
//		
//			ResultSet rs = null;
//			HashMap<String, String> returner = new HashMap<String, String>();
//			try
//			{
//				rs = stmt.executeQuery("SELECT libSiglum, city, library from Library" );
//				
//				while (rs.next()) {
//					returner.put(rs.getString("city") + " - " + rs.getString("library"),rs.getString("libSiglum"));
//				}
//				
//				
//			
//			} // end try
//			
//			catch ( SQLException e)
//			{
//				handleSQLError(e, "SELECT libSiglum, library from Library");
//			
//			} // end catch
//			
//			return returner;
//		
//		}
	
	
	public void toBasicOutput(ResultSet rs) {
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

	
	private String queryString(String[] at) {
		if (at.length == 1) {
			return at[0];
		}
		String returner = "";
		for(int i = 0; i < at.length - 1;i++) {
			returner +=at[i];
			returner += ", ";
		}
		returner += at[at.length - 1];
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


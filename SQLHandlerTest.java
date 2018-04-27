package db;
/**
 * test class, for checking string manipulations 
 *
 */
import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.Test;

public class SQLHandlerTest {

	@Test
	public void test() {
		ArrayList<String> checker = new ArrayList<String>();
		SQLHandler test = SQLHandler.getSQLHandler();
		checker = test.executePane1("Klosterneuburg - Augustiner-Chorherrenstift - Bibliothek", "Klosterneuburg - Augustiner-Chorherrenstift - Bibliothek", "1100", "1200", "Klosterneuburg");
	}

}

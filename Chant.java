package db;

public class Chant {
	
	private String UID;

	private String feastID;
	private String fullText;
	
	
	public Chant(String u, String f, String t) {
		UID = new String(u);

		feastID = new String(f);
		fullText = new String(t);
	}
		
	public String getfeastID() {
		return feastID;
	}
	public String getUID() {
		return UID;
	}


	public String getfullText() {
		return fullText;
	}
	
	public String toString() {
		String returner = "";
		returner += "Chant: " + UID + "  performed at the feast: " + feastID+ "\n";
		returner += fullText;
		return returner;
	}
}

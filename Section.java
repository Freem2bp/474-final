package db;

public class Section {

	
	private String siglum;
	private String[] liocc;
	private String[] date;
	private String provenance;
	private String sectionID;
	
	public Section(String ID, String s, String l, String c, String p) {
		sectionID = new String(ID);
		siglum = new String(s);
		liocc = l.split(";(?! lacuna)"); //regex pattern
		date = c.split("[^0-9]+"); //Split
		provenance = new String(p);
		
	}
	
	public String getSectionID() {
		return sectionID;
	}
	public String getSiglum() {
		return siglum;
	}
	
	public String getLiturgicalOccasions() {
		String returner = "";
		for( int i = 0; i < liocc.length; i++) {
			returner += liocc[i] + "\n";
		}
		return returner;
	}
	
	public String getDate() {
		String returner ="";
		for (int i = 0; i < date.length; i++ ) {
			returner += date[i];
		}
		return returner;
	}
	
	public String getProvenance() {
		return provenance;
	}
	
	
	public String toString() {
		String returner;
		returner = "Section " + this.getSectionID() + " of Manuscript " + this.getSiglum() + "\n";
		returner += "Written in the " + this.getDate() + "'s in " + this.getProvenance() + "\n";
		returner += "This Section was performed during the following Occasions:\n";
		returner += getLiturgicalOccasions();
		return returner;
	}
}

package hr.restart.baza;

import com.borland.dx.sql.dataset.QueryDataSet;

public class Imageinfo extends KreirDrop {

	
	 private static Imageinfo Imageinfoclass;
	  
	  QueryDataSet Imageinfo = new raDataSet();
	
	  public static Imageinfo getDataModule() {
		    if (Imageinfoclass == null) {
		    	Imageinfoclass = new Imageinfo();
		    }
		    return Imageinfoclass;
		  }
	public QueryDataSet getQueryDataSet() {
		// TODO Auto-generated method stub
		return Imageinfo;
	}

	public Imageinfo() {
	    try {
	      modules.put(this.getClass().getName(), this);
	      initModule();
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	    }
	  }
	
}

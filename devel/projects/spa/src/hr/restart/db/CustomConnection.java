package hr.restart.db;

import hr.restart.util.Aus;
import hr.restart.util.FileHandler;
import hr.restart.util.VarStr;

import com.borland.dx.sql.dataset.Database;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

public abstract class CustomConnection {
  private Database db;

  public abstract Database connect() throws Exception;
  
  public com.borland.dx.sql.dataset.QueryDataSet getData(String file, String _pars) {
    VarStr vqry = new VarStr(
        FileHandler.readFile(Aus.findFileAnywhere(file).getAbsolutePath()));
    vqry = vqry.replaceLast(";", "");
    String[] pars = new VarStr(_pars).split(',');
    for (int i = 0; i < pars.length; i++) {
      vqry = vqry.replaceAll("&"+(i+1), pars[i]);
    }
    String qry = vqry.toString();
    return getData(qry);
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getData(String qry) {
    try {
      if (db == null) db = connect();
      System.out.println("*** QUERY::"+qry);
      QueryDataSet retSet = new QueryDataSet();
      retSet.setQuery(new QueryDescriptor(db,qry));
      retSet.open();
      return retSet;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public void exec(String qry) {
    getDb().executeStatement(qry);
  }
  public Database getDb() {
    if (db == null) {
      try {
        db = connect();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return db;
  }

  public void setDb(Database db) {
    this.db = db;
  }
  
}

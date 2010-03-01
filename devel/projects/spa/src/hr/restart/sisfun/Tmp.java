package hr.restart.sisfun;

import java.util.HashSet;
import java.util.Iterator;

import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Shkonta;
import hr.restart.util.VarStr;


public class Tmp {

  public static void updateSheme() {
    
    QueryDataSet ds = Shkonta.getDataModule().getTempSet();
    ds.open();
    VarStr buf = new VarStr();
    VarStr tmp = new VarStr();
    HashSet gbs = new HashSet();
    for (ds.first(); ds.inBounds(); ds.next()) {
      buf.clear().append(ds.getString("SQLCONDITION"));
      int gb = buf.indexOfIgnoreCase("group by");
      String[] grby = tmp.clear().append(buf.from(gb + 9)).splitTrimmed(',');
      
      if (gb > 0) gbs.add(buf.from(gb));
    }
    for (Iterator i = gbs.iterator(); i.hasNext(); 
           System.out.println(i.next()));
    
    
    
  }
}

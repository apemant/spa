/****license*****************************************************************
**   file: Refresher.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.baza;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;



public class Refresher extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Refresher Refresherclass;

  Timer t = new Timer();
  TimerTask refresh = new TimerTask() {
    public void run() {
    	if (dm.getDatabase1().getAutoCommit() &&
    	  System.currentTimeMillis() >= lastRefresh + delay)
    		SwingUtilities.invokeLater(new Runnable() {
    		  public void run() {
    		    if (dm.getDatabase1().getAutoCommit() &&
    		        System.currentTimeMillis() >= lastRefresh + delay) {
    		      System.out.println("refreshing after " +
    		          (System.currentTimeMillis() - lastRefresh) + " ms");
    		      lastRefresh = System.currentTimeMillis();
    		      ref.refresh();
    		    }
              }
            });
    }
  };
  
  int delay;
  static long lastRefresh;

  QueryDataSet ref = new QueryDataSet();

  Column refDUMMY = new Column();

  public static Refresher getDataModule() {
    if (Refresherclass == null) {
      Refresherclass = new Refresher();
    }
    return Refresherclass;
  }
  
  public static void postpone() {
    lastRefresh = System.currentTimeMillis();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return ref;
  }

  public void begin(int refreshRate) {
//    Valid.getValid().runSQL("DELETE FROM Refresher");
//    Valid.getValid().runSQL("INSERT INTO Refresher VALUES('D')");
    delay = refreshRate;
    lastRefresh = System.currentTimeMillis();
    t.schedule(refresh, refreshRate, refreshRate);
  }

  public void stop() {
    t.cancel();
  }

  public Refresher() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    refDUMMY.setCaption("Dummy polje");
    refDUMMY.setColumnName("DUMMY");
    refDUMMY.setDataType(com.borland.dx.dataset.Variant.STRING);
    refDUMMY.setPrecision(1);
    refDUMMY.setRowId(true);
    refDUMMY.setTableName("REFRESHER");
    refDUMMY.setServerColumnName("DUMMY");
    refDUMMY.setSqlType(1);
    ref.setResolver(dm.getQresolver());
    ref.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from Refresher", null, true, Load.ALL));
 setColumns(new Column[] {refDUMMY});
  }

  public void setall() {

    ddl.create("Refresher")
       .addChar("dummy", 1, true)
       .addPrimaryKey("dummy");


    Naziv = "Refresher";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}

/****license*****************************************************************
**   file: upZOP.java
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
package hr.restart.robno;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.dM;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpit;


public class upZOP extends raUpit {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  
  JraTextField jraBroj = new JraTextField();
  JraTextField jraGod = new JraTextField();
  rapancskl rpcskl = new rapancskl() {
    public void findFocusAfter() {
      jraBroj.requestFocus();
  }};
  
  TableDataSet tds = new TableDataSet();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  Valid vl = Valid.getValid();
  
  StorageDataSet rep;
  static upZOP inst;
  
  public upZOP() {
    try {
      inst = this;
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }
  
  public static upZOP getInstance() {
    return inst;
  }

  public StorageDataSet getDataSet() {
    return rep;
  }
  
  public String getCSKL() {
    return rpcskl.getCSKL();
  }
  
  public String getNAZSKL() {
    return rpcskl.jrfNAZSKL.getText();
  }
  
  public String getGOD() {
    return tds.getString("GOD");
  }
  
  public int getBroj() {
    return tds.getInt("RBR");
  }
  
  private void jbInit() throws Exception {
    this.addReport("hr.restart.robno.repZOP","hr.restart.robno.repZOP", 
        "ZOP", "Zapisnik o danim popustima");
    rpcskl.jrfCSKL.setRaDataSet(rut.getMPSklDataset());
    rpcskl.setRaMode('S');
    tds.setColumns(new Column[] {
        dM.createStringColumn("GOD", "Godina", 4),
        dM.createIntColumn("RBR","Redni broj")
    });
    tds.open();
    rep = new StorageDataSet();
    rep.setColumns(new Column[] {
        dM.createStringColumn("VRDOK", "VD", 3),
        dM.createIntColumn("BRDOK", "Broj"),
        dM.createTimestampColumn("DATDOK", "Datum"),
        dM.createBigDecimalColumn("IPRODSP", "Iznos raèuna", 2),
        dM.createBigDecimalColumn("IRAZ", "Razduženje", 2),
        dM.createBigDecimalColumn("UIRAB", "Popust", 2)
    });
    
    jraBroj.setColumnName("RBR");
    jraBroj.setDataSet(tds);
    jraGod.setColumnName("GOD");
    jraGod.setDataSet(tds);
    jraGod.setHorizontalAlignment(SwingConstants.CENTER);
    
    JPanel jp = new JPanel(new XYLayout(645, 100));
    setJPan(jp);
    
    jp.add(rpcskl, new XYConstraints(0, 0, 640, 50));
    jp.add(new JLabel("Godina"), new XYConstraints(15, 55, -1, -1));
    jp.add(jraGod, new XYConstraints(150, 55, 75, -1));
    jp.add(new JLabel("Redni broj zapisnika"), new XYConstraints(350, 55, -1, -1));
    jp.add(jraBroj, new XYConstraints(535, 55, 74, -1));

  }
  
  void showDefaultValues() {
    tds.setString("GOD", Aut.getAut().getKnjigodRobno());
    rpcskl.setCSKL(raUser.getInstance().getDefSklad());
    this.getJPTV().setDataSet(null);
  }
  
  public void componentShow() {
    showDefaultValues();
    if (rpcskl.getCSKL().length() > 0)
      jraBroj.requestFocusLater();
    else rpcskl.jrfCSKL.requestFocusLater();
  }
  
  public void okPress() {
    String q;
    StorageDataSet all = new StorageDataSet();
    all.setColumns(rep.cloneColumns());
    ut.fillReadonlyData(all, q="SELECT doki.vrdok, doki.brdok, " +
    		"MAX(doki.datdok) as datdok, " +
    		"sum(stdoki.iprodsp) as iprodsp, sum(stdoki.iraz) as iraz, " +
    		"sum(stdoki.iraz)-sum(stdoki.iprodsp) as uirab FROM doki,stdoki " +
    		"WHERE "+rut.getDoc("doki", "stdoki") + " AND doki.god = '"+
    		tds.getString("GOD")+"' and doki.brzap="+tds.getInt("RBR")+
    		" AND doki.vrdok='GOT' and doki.cskl='"+ rpcskl.getCSKL() + "'" +  
    		" GROUP BY doki.vrdok, doki.brdok" +
    		
    		" UNION ALL "+
    		
    		"SELECT doki.vrdok, doki.brdok, MAX(doki.datdok) as datdok, " +
            "sum(stdoki.iprodsp) as iprodsp, sum(stdoki.fmcprp*stdoki.kol) as iraz, " +
            "(sum(stdoki.fmcprp*stdoki.kol)-sum(stdoki.iprodsp)) as uirab FROM doki,stdoki " +
            "WHERE "+rut.getDoc("doki", "stdoki") + " AND doki.god = '"+
            tds.getString("GOD")+"' and doki.brzap="+tds.getInt("RBR")+
            " AND doki.vrdok='GRN' and stdoki.csklart='"+ rpcskl.getCSKL() + "'" +  
            " GROUP BY doki.vrdok, doki.brdok"
    );
    all.setSort(new SortDescriptor(new String[] {"BRDOK"}));
    System.out.println(q);
    
    rep.open();
    rep.empty();
    for (all.first(); all.inBounds(); all.next())
      if (all.getBigDecimal("UIRAB").signum() != 0) {
        rep.insertRow(false);
        all.copyTo(rep);
      }
    
    if (rep.rowCount() == 0) setNoDataAndReturnImmediately();
    
    this.getJPTV().setDataSetAndSums(rep, new String[] {"IPRODSP", "IRAZ", "UIRAB"});
  }
  
  public boolean Validacija() {
    if (vl.isEmpty(rpcskl.jrfCSKL)) return false;
    if (vl.isEmpty(jraGod) || vl.isEmpty(jraBroj)) return false;
    return true;
  }
  
  public boolean runFirstESC() {
    return !rpcskl.getCSKL().equals("");
  }
  
  public void firstESC() {
    if (this.getJPTV().getStorageDataSet() == null) {
      rcc.EnabDisabAll(this.rpcskl, true);
      rpcskl.setCSKL("");
    } else {
      this.getJPTV().setDataSet(null);
      rcc.EnabDisabAll(getJPan(), true);
      this.jraBroj.requestFocus();
    }
  }
}

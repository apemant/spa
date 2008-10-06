/****license*****************************************************************
**   file: frmIzvNacPl.java
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

import hr.restart.baza.dM;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.swing.layout.raXYLayout;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raUpitLite;

import java.awt.Color;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;

public class frmIzvNacPl extends raUpitLite {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  JPanel jp = new JPanel();
//  JPanel jpDatum = new JPanel();

  static double suma;
  static String nacPl;
  static Timestamp datOd, datDo;
  QueryDataSet qds = new QueryDataSet();
  QueryDataSet qdsPoj = new QueryDataSet();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  static frmIzvNacPl fINP;
  Valid vl = Valid.getValid();

  raComboBox rcbNacPl = new raComboBox();
  hr.restart.robno.Util utRobno = hr.restart.robno.Util.getUtil();
  boolean OKEsc=false;
  lookupData ld = lookupData.getlookupData();



  public static frmIzvNacPl getInstance() {
    if(fINP==null)
      fINP = new frmIzvNacPl();
    return fINP;
  }


  private rapancskl1 rpcskl = new rapancskl1(348) {
    public void findFocusAfter() {
    }
    public void MYpost_after_lookUp(){
      jraDatumOd.requestFocus();
    }
  };
  private raComboBox rcbVrsta = new raComboBox();
//  JLabel jlIspis = new JLabel("Ispis");
//  JLabel jlSljed = new JLabel("Sljed");
  private raXYLayout xYLayout1 = new raXYLayout();
//  private XYLayout xYLayout2 = new XYLayout();
  private JraTextField jraDatumOd = new JraTextField();
  private JraTextField jraDatumDo = new JraTextField();
  private JLabel jlMinus = new JLabel();
  private JLabel jlDatum = new JLabel();
  private Border border1;
  TableDataSet tds = new TableDataSet();
  JLabel jlNacPl = new JLabel();
  JLabel jlVrsta = new JLabel("Vrsta ispisa");
//  TitledBorder tbDatum;

  public frmIzvNacPl() {
    fINP = this;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  private void jbInit() throws Exception {
    border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    rpcskl.setDisabAfter(true);

//    tbDatum =  new TitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,new Color(224, 255, 255),new Color(76, 90, 98),new Color(109, 129, 140)),"");//
    xYLayout1.setWidth(650);
    xYLayout1.setHeight(130);
    jp.setLayout(xYLayout1);

//    xYLayout2.setWidth(400);
//    xYLayout2.setHeight(30);
//    jpDatum.setLayout(xYLayout2);

    tds.setColumns(new Column[] {dm.createTimestampColumn("pocDatum"), dm.createTimestampColumn("zavDatum"),
        dm.createStringColumn("CNACPL",3), dm.createStringColumn("SLJED",4) });
    tds.open();

    rcbVrsta.setDataSet(tds);
    rcbVrsta.setColumnName("SLJED");
    rcbVrsta.setRaItems(new String[][]{{"Datum raèuna","RAC"},{"Datum naplate","NAP"}});


    jraDatumOd.setColumnName("pocDatum");
    jraDatumOd.setDataSet(tds);
    jraDatumOd.setText("jraTextField1");
    jraDatumOd.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumDo.setColumnName("zavDatum");
    jraDatumDo.setDataSet(tds);
    jraDatumDo.setText("jraTextField2");
    jraDatumDo.setHorizontalAlignment(SwingConstants.CENTER);
    new raDateRange(jraDatumOd, jraDatumDo);

    rcbNacPl.setRaColumn("CNACPL");
    rcbNacPl.setRaDataSet(tds);
    setCBNacPlItems();

    jlDatum.setText("Datum (od - do)");
    jlNacPl.setText("Naè. pl.");
    jlNacPl.setHorizontalAlignment(SwingConstants.TRAILING);
    jlNacPl.setHorizontalTextPosition(SwingConstants.LEADING);
    jlVrsta.setHorizontalAlignment(SwingConstants.TRAILING);
    jlVrsta.setHorizontalTextPosition(SwingConstants.LEADING);
//    jpDatum.setBorder(tbDatum);

//    jpDatum.add(rcbVrsta, new XYConstraints(100,7,150,-1));
//    jpDatum.add(jlSljed, new XYConstraints(15,9,-1,-1));

//    jp.add(jlIspis, new XYConstraints(15,90,-1,-1));
    jp.add(rpcskl,    new XYConstraints(0, 0, 655, -1));
    jp.add(jraDatumOd,     new XYConstraints(150, 50, 100, -1));
    jp.add(jraDatumDo,     new XYConstraints(255, 50, 100, -1));
    jp.add(jlDatum,   new XYConstraints(15, 50, -1, -1));
    jp.add(rcbNacPl,              new XYConstraints(454, 50, 150, -1));
    jp.add(rcbVrsta, new XYConstraints(454,75,150,-1));
    jp.add(jlNacPl,   new XYConstraints(365, 52, 70, -1));
    jp.add(jlVrsta, new XYConstraints(365,77, 70, -1));
//    jp.add(jpDatum,       new XYConstraints(150, 75, 455, 45));
    this.setJPan(jp);
  }

  public void componentShow() {
    //*********** -> dodano jer Srkyeva Top lista artikla rasturi bindanje na getSklad()
    //*by SrkY* -> ma nemoj!! Srky ovo, Srky ono, Srky je kriv za sve.... goddamn!!!
    //*** obrisati kad Srky sredi stvar

    if(!dm.getSklad().isOpen())
      dm.getSklad().open();
    rpcskl.jrfCSKL.setRaDataSet(dm.getSklad());

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    rcbVrsta.setSelectedIndex(0);
    if(!tds.isOpen())tds.open();
    tds.setTimestamp("pocDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    tds.setString("SLJED","RAC");
    rpcskl.jrfCSKL.requestFocus();
    boolean cskl_corg = rdUtil.getUtil().getCSKL_CORG(raUser.getInstance().getDefSklad(), hr.restart.zapod.OrgStr.getKNJCORG());

  if(cskl_corg) {
    rpcskl.jrfCSKL.setText(raUser.getInstance().getDefSklad());
    rpcskl.jrfCSKL.forceFocLost();
   } else if(!cskl_corg)
      rpcskl.jrfCSKL.requestFocus();
  }


  public boolean runFirstESC() {
    if(OKEsc) {
      OKEsc = false;
      return false;
    }
    if(!rpcskl.jrfCSKL.getText().equals(""))
      return true;
    return false;
  }

  public void firstESC() {
    if(OKEsc) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          reset();}});
    }
    else if(!rpcskl.jrfCSKL.getText().equals("")) {
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.Clear();
      rpcskl.jrfCSKL.requestFocus();
    }
  }

  public void okPress(){
    OKEsc=true;
  };
  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow() {
    return true;
  }
  public boolean Validacija() {
    rcc.EnabDisabAll(jp,false);
    nacPl =tds.getString("CNACPL");
    if(rpcskl.jrfCSKL.getText().equals("")) {
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      rcc.EnabDisabAll(jp,true);
      rpcskl.jrfCSKL.requestFocus();
      return false;
    }
    if (tds.getTimestamp("zavDatum").before(tds.getTimestamp("pocDatum"))) {
      JOptionPane.showConfirmDialog(this.jp,"Po\u010Detni datum mora biti manji od završnog !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
      reset();}});
      jraDatumOd.requestFocus();
      return false;
    }

    if(preparePrint()==0) {
      JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
      reset();}});
      return false;
    }

    this.killAllReports();
    this.addReport("hr.restart.robno.repIzvNacPl","hr.restart.robno.repIzvNacPl","IzvNacPl","Izvještaj naèina plaæanja zbirno");
    this.addReport("hr.restart.robno.repIzvNacPlPoj","hr.restart.robno.repIzvNacPlPoj","IzvNacPlPoj","Izvještaj naèina plaæanja pojedinaèno");
    return true;
  }

  private int preparePrint() {
    suma = 0;

//    if (qds.isOpen()){
//      qds.deleteAllRows();
//      qdsPoj.deleteAllRows();
//      qds.close();
//      qdsPoj.close();
//    } else {
      qds.close();
      qdsPoj.close();
//    }

    datOd = tds.getTimestamp("pocDatum");
    datDo = tds.getTimestamp("zavDatum");
    String qStr = rdUtil.getUtil().getIzvNacPlZbirno(rpcskl.jrfCSKL.getText(), utRobno.getTimestampValue(tds.getTimestamp("pocDatum"), 0),
        utRobno.getTimestampValue(tds.getTimestamp("zavDatum"), 1),tds.getString("CNACPL"), isSljedPoDatRac());
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr));

    String qStrPoj = rdUtil.getUtil().getIzvNacPlPoj(rpcskl.jrfCSKL.getText(), utRobno.getTimestampValue(tds.getTimestamp("pocDatum"), 0),
        utRobno.getTimestampValue(tds.getTimestamp("zavDatum"), 1),tds.getString("CNACPL"), isSljedPoDatRac());
    qdsPoj.setQuery(new QueryDescriptor(dm.getDatabase1(),qStrPoj));

    qds.open();

    if (qds.rowCount() == 0) return qds.rowCount();

    qds.setSort(new SortDescriptor(new String[]{"NACPLR"}));
    qds.getColumn("CSKL").setRowId(true);
    qds.first();
    do {
      ld.raLocate(dm.getNacpl(), new String[]{"CNACPL"}, new String[]{qds.getString("NACPLR")});
      if(dm.getNacpl().getString("FL_CEK").equals("D"))
        qds.setInt("FL_CEK", 1);
      if(dm.getNacpl().getString("FL_KARTICA").equals("D"))
        qds.setInt("FL_KARTICA", 1);
      suma += qds.getBigDecimal("IZNOS").doubleValue();
    } while (qds.next());

    qds.post();

    qdsPoj.open();
    qdsPoj.setSort(new SortDescriptor(new String[]{"NACPLR"}));
    qdsPoj.getColumn("CSKL").setRowId(true);
    qdsPoj.first();
    do {
      ld.raLocate(dm.getNacpl(), new String[]{"CNACPL"}, new String[]{qdsPoj.getString("NACPLR")});
      if(dm.getNacpl().getString("FL_CEK").equals("D"))
        qdsPoj.setInt("FL_CEK", 1);
      if(dm.getNacpl().getString("FL_KARTICA").equals("D"))
        qdsPoj.setInt("FL_KARTICA", 1);
    } while (qdsPoj.next());

    qdsPoj.post();
    return qds.getRowCount();
  }

  public QueryDataSet getQDS() {
    return qds;
  }

  public QueryDataSet getQDSPoj() {
    return qdsPoj;
  }

  private void reset() {
    rcc.EnabDisabAll(jp, true);
    if(!rpcskl.jrfCSKL.getText().equals("")) {
      rpcskl.jrfCSKL.forceFocLost();
    }
  }

  public void showDefaultValues() {
  }

  private Timestamp setFirstDay(Timestamp today) {
    String strToday = today.toString();
    String date = strToday.substring(0,8)+"01"+strToday.substring(10, strToday.length());
    return Timestamp.valueOf(date);
  }

  private void setCBNacPlItems() {
    QueryDataSet qds = hr.restart.baza.nacpl.getDataModule().getFilteredDataSet("1=1");
    qds.open();
    qds.first();
    int rows = qds.getRowCount();
    String [][] items = new String[rows+1][2];
    for(int i = 0; i<=rows;i++) {
      if(i==0) {
        items[i][0]= "Svi naèini plaæanja";
        items[i][1]= "";
      } else {
        items[i][1]= qds.getString("CNACPL");
        items[i][0]= qds.getString("NAZNACPL");
        qds.next();
      }
    }
    rcbNacPl.setRaItems(items);
    try {
      tds.open();
      tds.setString("CNACPL","");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public boolean isSljedPoDatRac(){
    return tds.getString("SLJED").equals("RAC");
  }
}
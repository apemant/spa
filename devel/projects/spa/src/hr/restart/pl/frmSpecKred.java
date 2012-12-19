/****license*****************************************************************
**   file: frmSpecKred.java
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
package hr.restart.pl;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;

import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmSpecKred extends frmIzvjestajiPL {
  JPanel jpKred = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();
  private JlrNavField jlfCVrOdb = new JlrNavField();
  private JlrNavField jlfNazVrOdb = new JlrNavField();
  private JLabel jlVrodn = new JLabel();
  private JraButton jbVrOdb = new JraButton();
  private JLabel jlVrOdb = new JLabel();
  private JLabel jlSifra = new JLabel();
  private JLabel jlNaziv = new JLabel();
  StorageDataSet temp = new StorageDataSet();
  Column sifra = new Column();
  Column naziv = new Column();
  dM dm = dM.getDataModule();
  char mode;
  QueryDataSet lookUpSet = new QueryDataSet();
  String where;
  private static QueryDataSet qdsZB=new QueryDataSet();
  private static QueryDataSet qdsPOJ=new QueryDataSet();
  public static frmSpecKred fSpecK;
  private static BigDecimal suma ;
  private static BigDecimal sumaS ;

  public frmSpecKred()
  {
    this('O');
  }
  public frmSpecKred(char mode) {
    super(mode);
    this.mode = mode;
    try {
      jbInitA();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    bind();
  }

  public static frmSpecKred getFrmSpecKred()
  {
    if(fSpecK == null)
      fSpecK = new frmSpecKred();
    return fSpecK;
  }

  void jbInitA()throws Exception
  {
    int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-580;
    int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-310;
    this.setLocation((int)x/2,(int)y/2);
    this.setTitle("Specifikacija kredita");

    jpKred.setLayout(xYLayout1);
    xYLayout1.setWidth(585);
    xYLayout1.setHeight(70);
    jbVrOdb.setText("...");
    jlVrOdb.setText("Vrsta odbitka");
    jlSifra.setText("Šifra");
    jlNaziv.setText("Naziv");

    jpKred.add(jlVrodn,       new XYConstraints(193, 15, -1, -1));
    jpKred.add(jlfNazVrOdb,    new XYConstraints(255, 30, 285, -1));
    jpKred.add(jlfCVrOdb,   new XYConstraints(150, 30, 100, -1));
    jpKred.add(jbVrOdb,    new XYConstraints(545, 30, 21, 21));
    jpKred.add(jlVrOdb,    new XYConstraints(15, 30, -1, -1));
    jpKred.add(jlSifra,   new XYConstraints(150, 12, -1, -1));
    jpKred.add(jlNaziv,   new XYConstraints(255, 12, -1, -1));
    this.setMiddlePanel(jpKred);
  }

  public void componentShow()
  {
    fSpecK = this;
    killAllReports();
    this.addReport("hr.restart.pl.repSpecKredZB", "Specifikacija kredita - zbirno", 5);
    this.addReport("hr.restart.pl.repSpecKredPOJ", "Specifikacija kredita - pojedina\u010Dno", 5);
    setDifolt();
  }

  public void firstESC(){

    jlfCVrOdb.setText("");
    jlfCVrOdb.forceFocLost();
  }

  public boolean runFirstESC(){
    if(!jlfCVrOdb.getText().equals(""))
      return true;
    return false;
  }

  public void okPress(){};

  public boolean Validacija()
  {
    prepareIspis();
    if(!qdsZB.isOpen())
      qdsZB.open();



    if(qdsZB.getString("CORG").equals(""))
    {
      JOptionPane.showConfirmDialog(this,"Nema podataka koji zadovoljavaju traženi uvjet !",
                                    "Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  private String getTableName()
  {
    if(getRepMode()=='A')
      return "ODBICIARH";
    return "ODBICIOBR";
  }

  public void bind()
  {
    sifra.setColumnName("CVRODB");
    sifra.setDataType(com.borland.dx.dataset.Variant.STRING);
    naziv.setColumnName("OPISVRODB");
    naziv.setDataType(com.borland.dx.dataset.Variant.STRING);
    temp.setColumns(new Column[]{sifra, naziv});
    jlfCVrOdb.setColumnName("CVRODB");
    jlfCVrOdb.setDataSet(temp);
    jlfCVrOdb.setColNames(new String[] {"OPISVRODB"});
    jlfCVrOdb.setTextFields(new JTextField[] {jlfNazVrOdb});
    jlfCVrOdb.setVisCols(new int[] {0,1});
    jlfCVrOdb.setSearchMode(0);
    jlfCVrOdb.setRaDataSet(lookUpSet);
    jlfCVrOdb.setNavButton(this.jbVrOdb);
    jlfNazVrOdb.setSearchMode(1);
    jlfNazVrOdb.setNavProperties(jlfCVrOdb);
    jlfNazVrOdb.setColumnName("OPISVRODB");
  }


  public void show()
  {
    where = raOdbici.getInstance().getOdbiciWhereQuery(raOdbici.KREDITI_param, getTableName());
    String qStr = "select * from vrsteOdb where "+where;
    if(lookUpSet.isOpen())
      lookUpSet.close();
    lookUpSet.setColumns(new Column[]{
      (Column) dm.getVrsteodb().getColumn("LOKK").clone(),
      (Column) dm.getVrsteodb().getColumn("AKTIV").clone(),
      (Column) dm.getVrsteodb().getColumn("CVRODB").clone(),
      (Column) dm.getVrsteodb().getColumn("OPISVRODB").clone(),
      (Column) dm.getVrsteodb().getColumn("NIVOODB").clone(),
      (Column) dm.getVrsteodb().getColumn("TIPODB").clone(),
      (Column) dm.getVrsteodb().getColumn("VRSTAOSN").clone(),
      (Column) dm.getVrsteodb().getColumn("CPOV").clone(),
      (Column) dm.getVrsteodb().getColumn("IZNOS").clone(),
      (Column) dm.getVrsteodb().getColumn("STOPA").clone(),
      (Column) dm.getVrsteodb().getColumn("PARAMETRI").clone()
    });
    lookUpSet.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    super.show();
  }

  public static QueryDataSet getQdsZB()
  {
    qdsZB.open();
    return qdsZB;
  }

  public static QueryDataSet getQdsPOJ()
  {
    qdsPOJ.open();
    qdsPOJ.setSort(new SortDescriptor(new String[]{"CVRODB","PREZIME","IME","RBRODB"}));
    return qdsPOJ;
  }

  public void prepareIspis()
  {
    String table = getTableName();
    String sIspZbir = plUtil.getPlUtil().getSpecKredStr(table, where)+this.getWhereQuery("radnici")+getExclOvrha();
    String sIspPoj =  plUtil.getPlUtil().getSpecKredStr(table, where)+this.getWhereQuery("radnici")+getExclOvrha();

    if(qdsZB.isOpen())
      qdsZB.close();

    if(qdsPOJ.isOpen())
      qdsPOJ.close();

    if(jlfCVrOdb.getText().equals(""))
      sIspZbir+=" group by "+table+".cvrodb";
    else
      sIspZbir+=" and "+table+".cvrodb ='"+temp.getString("CVRODB")+"'";

    if(jlfCVrOdb.getText().equals(""))
      sIspPoj+=" group by "+table+".cvrodb, "+table+".cradnik, "+table+".rbrodb";
    else
      sIspPoj+=" and "+table+".cvrodb ='"+temp.getString("CVRODB")+"' group by "+table+".cradnik, "+table+".rbrodb";

System.out.println("sIspZbir :: " + sIspZbir);
System.out.println("sIspPoj :: " + sIspPoj);
    qdsZB.setQuery(new QueryDescriptor(dm.getDatabase1(), sIspZbir));
    qdsPOJ.setQuery(new QueryDescriptor(dm.getDatabase1(), sIspPoj));

    qdsZB.open();
    qdsPOJ.open();

    suma = Aus.zero0;
    sumaS = Aus.zero0;
    while(qdsPOJ.inBounds())
    {
      suma = suma.add(qdsPOJ.getBigDecimal("OBRIZNOS"));
      sumaS = sumaS.add(qdsPOJ.getBigDecimal("SALDO"));
      qdsPOJ.next();
    }
  }

  private String getExclOvrha() {
    String in = raIzvjestaji.getOdbiciWhQueryIzv(new short[] {10005,10});
    if ("".equals(in)) return "";
    String r = " AND NOT ("+in+")";
    System.out.println("ExclOvrha::"+r);
    return r;
  }
  public static BigDecimal getSuma()
  {
    return suma;
  }

  public static BigDecimal getSumaS()
  {
    return sumaS;
  }
}
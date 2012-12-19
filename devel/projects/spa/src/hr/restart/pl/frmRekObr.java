/****license*****************************************************************
**   file: frmRekObr.java
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
import hr.restart.swing.JraButton;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
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

public class frmRekObr extends frmIzvjestajiPL{
  ArrayList al = new ArrayList();
  lookupData ld = lookupData.getlookupData();
  JPanel jpVro = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();
  private JlrNavField jlfCVrOdn = new JlrNavField(){
    public void after_lookUp()
    {
      try {
        fieldSet.setString("CVRO", jlfCVrOdn.getText());
        fieldSet.open();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  };
  private JlrNavField jlfNazVrOdn = new JlrNavField();
  private JLabel jlVrodn = new JLabel();
  private JraButton jbVrOdn = new JraButton();
  Column column = new Column();
  public static frmRekObr fRekObr;

  private static QueryDataSet qdsZN = new QueryDataSet();
  private static QueryDataSet qdsVRO = new QueryDataSet();
  private static QueryDataSet qdsORG = new QueryDataSet();
  private static QueryDataSet qdsPOJ = new QueryDataSet();


  raOdbici raOdb = raOdbici.getInstance();
  private QueryDataSet doprinosDS = new QueryDataSet();
  private QueryDataSet doprinosRDS = new QueryDataSet();


  private static QueryDataSet qdsDoprinosNaZN = new QueryDataSet();
  private static QueryDataSet qdsDoprinosNaVRO = new QueryDataSet();
  private static QueryDataSet qdsDoprinosNaORG = new QueryDataSet();
  private static QueryDataSet qdsDoprinosNaPOJ = new QueryDataSet();

  private static QueryDataSet qdsDoprinosIzZN = new QueryDataSet();
  private static QueryDataSet qdsDoprinosIzVRO = new QueryDataSet();
  private static QueryDataSet qdsDoprinosIzORG = new QueryDataSet();
  private static QueryDataSet qdsDoprinosIzPOJ = new QueryDataSet();


  private static int  brRad = 0;
  private char mode;



  public static frmRekObr getFrmRekObr()
  {
    if(fRekObr==null)
      fRekObr = new frmRekObr();
    return fRekObr;
  }

  public frmRekObr() {
    this('O');
  }


  public frmRekObr(char mode) {
    super(mode);
    this.mode = mode;

    try {
      jbInitA();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    bindanje();
//    setDifolt();
//    fRekObr = this;
  }

  void jbInitA() throws Exception
  {
    xYLayout1.setWidth(585);
    xYLayout1.setHeight(25);
    jpVro.setLayout(xYLayout1);
    this.mainPanel.add(jpVro, BorderLayout.CENTER);
    jlVrodn.setText("Vrsta radnog odnosa");
    jbVrOdn.setText("...");
    jpVro.add(jlfCVrOdn,     new XYConstraints(150, 0, 100, -1));
    jpVro.add(jlfNazVrOdn,           new XYConstraints(255, 0, 295, -1));
    jpVro.add(jlVrodn,      new XYConstraints(15, 0, -1, -1));
    jpVro.add(jbVrOdn,        new XYConstraints(555, 0, 21, 21));
    column.setColumnName("CVRO");
    column.setDataType(com.borland.dx.dataset.Variant.STRING);
    if (fieldSet.hasColumn("CVRO")==null) fieldSet.addColumn(column);

    doprinosDS.setColumns(new Column[]{
    (Column) dm.getVrsteodb().getColumn("CVRODB").clone(),
    (Column) dm.getVrsteodb().getColumn("IZNOS").clone(),
    (Column) dm.getAllRadnicipl().getColumn("CORG").clone(),
    (Column) dm.getAllRadnicipl().getColumn("CVRO").clone()
    });

    doprinosRDS.setColumns(new Column[]{
    (Column) dm.getVrsteodb().getColumn("CVRODB").clone(),
    (Column) dm.getVrsteodb().getColumn("IZNOS").clone(),
    (Column) dm.getAllRadnicipl().getColumn("CORG").clone(),
    (Column) dm.getAllRadnicipl().getColumn("CVRO").clone()
    });
  }

  public void bindanje() {

    jlfCVrOdn.setColumnName("CVRO");
    jlfCVrOdn.setDataSet(fieldSet);
    jlfCVrOdn.setColNames(new String[] {"NAZIVRO"});
    jlfCVrOdn.setTextFields(new JTextField[] {jlfNazVrOdn});
    jlfCVrOdn.setVisCols(new int[] {0,1});
    jlfCVrOdn.setRaDataSet(dm.getVrodn());
    jlfCVrOdn.setSearchMode(0);
    jlfCVrOdn.setNavButton(this.jbVrOdn);
    jlfNazVrOdn.setNavProperties(jlfCVrOdn);
    jlfNazVrOdn.setSearchMode(1);

    jlfNazVrOdn.setColumnName("NAZIVRO");
  }


  public boolean Validacija()
  {
    prepareIspis();

    if(!qdsZN.isOpen())
      qdsZN.open();
    if(!qdsORG.isOpen())
      qdsORG.open();
    if(!qdsVRO.isOpen())
      qdsVRO.open();
    if(!qdsPOJ.isOpen())
      qdsPOJ.open();

    if(qdsZN.getString("CORG").equals(""))
    {
      JOptionPane.showConfirmDialog(this,"Nema podataka koji zadovoljavaju traženi uvjet !",
                                    "Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  public void prepareIspis()
  {
    if(qdsZN.isOpen())
      qdsZN.close();
    if(qdsVRO.isOpen())
      qdsVRO.close();
    if(qdsORG.isOpen())
      qdsORG.close();
    if(qdsPOJ.isOpen())
      qdsPOJ.close();

    if(!doprinosDS.isOpen()) doprinosDS.open();
    doprinosDS.deleteAllRows();

    if(!doprinosRDS.isOpen()) doprinosRDS.open();
    doprinosRDS.deleteAllRows();



    //String qStr = plUtil.getPlUtil().getRekObrStr(this.getKumOrgTableName());

    String qStrZN = plUtil.getPlUtil().getRekObrStr(0, this.getKumOrgTableName()) + this.getWhereQuery() + plUtil.getPlUtil().getRekObrGroupBy(0, fieldSet.getString("CVRO"));
    String qStrVRO = plUtil.getPlUtil().getRekObrStr(1, this.getKumOrgTableName()) + this.getWhereQuery() + plUtil.getPlUtil().getRekObrGroupBy(1, fieldSet.getString("CVRO"));
    String qStrORG = plUtil.getPlUtil().getRekObrStr(2, this.getKumOrgTableName()) + this.getWhereQuery() + plUtil.getPlUtil().getRekObrGroupBy(2, fieldSet.getString("CVRO"));
    String qStrPOJ = plUtil.getPlUtil().getRekObrStr(3, this.getKumOrgTableName()) + this.getWhereQuery() + plUtil.getPlUtil().getRekObrGroupBy(3, fieldSet.getString("CVRO"));

    qdsZN.setQuery(new QueryDescriptor(dm.getDatabase1(), qStrZN));
    qdsVRO.setQuery(new QueryDescriptor(dm.getDatabase1(), qStrVRO));
    qdsORG.setQuery(new QueryDescriptor(dm.getDatabase1(), qStrORG));
    qdsPOJ.setQuery(new QueryDescriptor(dm.getDatabase1(), qStrPOJ));

    System.out.println("qStrZN: " + qStrZN);
    setDoprinosiBasic();
    setDoprinosiRBasic();
    qdsDoprinosNaZN = setDoprinosiGrouped(0);
    qdsDoprinosNaVRO = setDoprinosiGrouped(1);
    qdsDoprinosNaORG = setDoprinosiGrouped(2);
    qdsDoprinosNaPOJ =setDoprinosiGrouped(3);

    qdsDoprinosIzZN = setDoprinosiRGrouped(0);
    qdsDoprinosIzVRO = setDoprinosiRGrouped(1);
    qdsDoprinosIzORG = setDoprinosiRGrouped(2);
    qdsDoprinosIzPOJ =setDoprinosiRGrouped(3);

//    sysoutTEST ST = new sysoutTEST(false);
//    ST.prn(qdsDoprinosIzORG);

    qdsZN = addOlaksice(qdsZN,0);
    qdsVRO = addOlaksice(qdsVRO,1);
    qdsORG = addOlaksice(qdsORG,2);
    qdsPOJ = addOlaksice(qdsPOJ,3);
  }

  public static QueryDataSet getQdsZN()
  {
    qdsZN.open();
    return qdsZN;
  }

  public static QueryDataSet getQdsVRO()
  {
    qdsVRO.open();
    return qdsVRO;
  }

  public static QueryDataSet getQdsORG()
  {
    qdsORG.open();
    return qdsORG;
  }

  public static QueryDataSet getQdsPOJ()
  {
    qdsPOJ.open();
    return qdsPOJ;
  }

///////////////////***************************** Prebaciti kod srkija u main klasu
  public int getbrDjelatnika(String cOrg)
  {
    String tabRad=getKumRadTableName();
    String tabRadArh=isArhMode()?tabRad:"radnici";
    String qStr = "select distinct count (*) from radnici, "+tabRad+" where radnici.cradnik = "+tabRad+".cradnik "+
                  "and "+getWhereQuery(tabRadArh);
    if(!cOrg.equals(""))
      qStr += " and "+tabRadArh+".corg='"+cOrg+"'";
    QueryDataSet rQDS = new QueryDataSet();
    rQDS.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    return Valid.getValid().getSetCount(rQDS,0);
  }

  public int getbrDjelatnikaVRO(String cvro, String cOrg)
  {
    String tabRad=getKumRadTableName();
    String tabRadArh=isArhMode()?tabRad:"radnici";
    String qStr = "select distinct count (*) from radnici,radnicipl,"+tabRad+" where radnici.cradnik ="+tabRad+".cradnik and radnici.cradnik = radnicipl.cradnik " +
            "and radnicipl.cradnik = "+tabRad+".cradnik"+
                  " and "+this.getWhereQuery(isArhMode()?tabRad:"radnici")+" and "+(isArhMode()?tabRad:"radnicipl")+".cvro='"+cvro+"'";

    if(!cOrg.equals(""))
      qStr += " and "+tabRadArh+".corg='"+cOrg+"'";
    QueryDataSet rQDS = new QueryDataSet();
    rQDS.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    return Valid.getValid().getSetCount(rQDS,0);
  }

  private String getKumOrgTableName()
  {
    if (mode=='A') return "kumulorgarh";
    return "kumulorg";
  }

  private String getKumRadTableName()
  {
    if (mode=='A') return "kumulradarh";
    return "kumulrad";
  }
  public void componentShow()
  {
    fieldSet.setString("CVRO", "");
    fRekObr = this;
    killAllReports();
    this.addReport("hr.restart.pl.repRekObrZN", "Rekapitulacija zbirno", 5);
    this.addReport("hr.restart.pl.repRekObrVRO", "Rekapitulacija po Vr. radnog odnosa", 5);
    this.addReport("hr.restart.pl.repRekObrORG", "Rekapitulacija po Org. jedinicama", 5);
    this.addReport("hr.restart.pl.repRekObrPOJ", "Rekapitulacija po Org. jedinicama i vrsti RO", 5);
    setDifolt();
  }

  public void show()
  {
    jlfCVrOdn.setText("");
    jlfCVrOdn.forceFocLost();
    super.show();
  }

  public void firstESC()
  {
    super.firstESC();
    rcc.EnabDisabAll(jpVro,true);
  }

  private String odbiciWh(short[] id){
    String rigowq = raIzvjestaji.getOdbiciWhQueryIzv(id);
    if (rigowq.equals("")) rigowq = "1=2";
      if (this.getRepMode() == 'A') {
        return " and ".concat(rigowq);
      } else {
        return " where ".concat(rigowq);
      }
  }

  private String getWhereSQL(String table) {
    if (this.getRepMode() == 'A'){
      String squereWhere = " where ".concat(raPlObrRange.getInQueryIsp(fieldSet.getShort("GODINAOD"),fieldSet.getShort("MJESECOD"),
          fieldSet.getShort("GODINADO"),fieldSet.getShort("MJESECDO"), table));
      return squereWhere;
    } else {
      return "";
    }
  }

  private String getOdbiciTableName() {
    String tableName = "";
    if (this.getRepMode() == 'A') tableName = " odbiciarh";
    else tableName = " odbiciobr";
    return tableName;
  }


  private void setDoprinosiBasic()
  {

    if(!qdsPOJ.isOpen()) qdsPOJ.open();
    qdsPOJ.first();
    while(qdsPOJ.inBounds())
    {
      menageDoprinosiNP(qdsPOJ.getString("CORG"), qdsPOJ.getString("CVRO"));
      qdsPOJ.next();
    }

  }

  private void setDoprinosiRBasic()
  {

    if(!qdsPOJ.isOpen()) qdsPOJ.open();
    qdsPOJ.first();
    while(qdsPOJ.inBounds())
    {
      menageDoprinosiRNP(qdsPOJ.getString("CORG"), qdsPOJ.getString("CVRO"));
      qdsPOJ.next();
    }

  }

  private QueryDataSet addOlaksice(QueryDataSet qds, int cas)
  {
    QueryDataSet temp1=null, temp2=null, temp3=null, tempOVRHA=null;
    BigDecimal ol_1=null, ol_2=null, ol_3=null, bOVRHA=null;
    if(!qds.isOpen()) qds.open();
    qds.first();

    while(qds.inBounds())
    {

      String ID1,ID2,ID3,OVRHA;
      String odbWh1=odbiciWh(raIzvjestaji.ID_3_1);
      String odbWh2=odbiciWh(raIzvjestaji.ID_3_2);
      String odbWh3=odbiciWh(raIzvjestaji.ID_3_3);
      
      String odbWhOvrha=odbiciWh(new short[] {10005,10});
      
      String uvjetRadnik1 = getCRadnik(qds.getString("CORG"), qds.getString("CVRO"), odbWh1, cas);
      String uvjetRadnik2 = getCRadnik(qds.getString("CORG"), qds.getString("CVRO"), odbWh2, cas);
      String uvjetRadnik3 = getCRadnik(qds.getString("CORG"), qds.getString("CVRO"), odbWh3, cas);

      String uvjetRadnikOvrha = getCRadnik(qds.getString("CORG"), qds.getString("CVRO"), odbWhOvrha, cas);

      ID1 = "select sum(obriznos) as obriznos from " + getOdbiciTableName() +
            getWhereSQL(getOdbiciTableName()) +
            odbWh1+ uvjetRadnik1;
      ID2 = "select sum(obriznos) as obriznos from " + getOdbiciTableName() +
            getWhereSQL(getOdbiciTableName()) +
            odbWh2+ uvjetRadnik2;
      ID3 = "select sum(obriznos) as obriznos from " + getOdbiciTableName() +
          getWhereSQL(getOdbiciTableName()) +
          odbWh3+ uvjetRadnik3;

      OVRHA = "select sum(obriznos) as obriznos from " + getOdbiciTableName() +
          (isArhMode()?" where 0=0 "+getBetweenAhrQuery(getOdbiciTableName()):"") +  
//          getWhereSQLZA(getOdbiciTableName()) +
            odbWhOvrha+ uvjetRadnikOvrha;
      
System.out.println("ID1 = "+ID1);
System.out.println("ID2 = "+ID2);
System.out.println("ID3 = "+ID3);
System.out.println("OVRHA = "+OVRHA);
      try {
        temp1 = Util.getNewQueryDataSet(ID1 , true );
        ol_1 = temp1.getBigDecimal("OBRIZNOS");
      } catch (Exception e) {
        e.printStackTrace();
      } 
      try {
        temp2 = Util.getNewQueryDataSet(ID2, true );
        ol_2 = temp2.getBigDecimal("OBRIZNOS");
      } catch (Exception e) {
        e.printStackTrace();
      } 
      try {
        temp3 = Util.getNewQueryDataSet(ID3, true );
        ol_3 = temp3.getBigDecimal("OBRIZNOS");
      } catch (Exception e) {
        e.printStackTrace();
      } 

      try {
        tempOVRHA = Util.getNewQueryDataSet(OVRHA, true );
        bOVRHA = tempOVRHA.getBigDecimal("OBRIZNOS");
      } catch (Exception e) {
        e.printStackTrace();
      } 
      System.out.println("bOVRHA = "+bOVRHA);
      

      qds.setRowId("CORG", true);
      if(ol_1 != null)
        qds.setBigDecimal("OL31", ol_1);

      if(ol_2 != null)
        qds.setBigDecimal("OL32", ol_2);

      if(ol_3 != null)
        qds.setBigDecimal("OL33", ol_3);
      
      if(bOVRHA != null) {
//        qds.setBigDecimal("NETO2", qds.getBigDecimal("NETO2").add(bOVRHA));
//        qds.setBigDecimal("NETOPK", qds.getBigDecimal("NETOPK").add(bOVRHA));
        qds.setBigDecimal("NARUKE", qds.getBigDecimal("NARUKE").add(bOVRHA));
        qds.setBigDecimal("KREDITI", qds.getBigDecimal("KREDITI").subtract(bOVRHA));
      }
      qds.post();
      qds.next();
    }
    return qds;
  }
  
  private String[] getAdditionalRadArhQuerys(int cs) {
    String radnicipl = isArhMode()?"kumulradarh":"radnicipl";
    String obrrange = "";
    if (isArhMode()) {
      obrrange = getBetweenAhrQuery(radnicipl);
      if (cs == 0) {
        obrrange = new VarStr(obrrange).replaceFirst("AND","WHERE").toString();
      }
    }
    return new String[] {radnicipl,obrrange};
  }
  
  private String getCRadnik(String cOrg, String cVro, String whCond, int cs)
  {
    String uvjet="" , strRadnik = "";
    QueryDataSet tmp;
    String[] _araq = getAdditionalRadArhQuerys(cs);
    String radnicipl = _araq[0];
    String obrrange = _araq[1];
    switch (cs) {
      case 0:
        uvjet = "select cradnik as cradnik from "+radnicipl+obrrange;
        break;
      case 1:
        uvjet = "select cradnik as cradnik from "+radnicipl+" where cvro='"+cVro+"'"+obrrange;
        break;
      case 2:
        uvjet = "select cradnik as cradnik from "+radnicipl+" where corg='"+cOrg+"'"+obrrange;
        break;
      case 3:
        uvjet = "select cradnik as cradnik from "+radnicipl+" where corg='"+cOrg+"' and cvro='"+cVro+"'"+obrrange;
        break;
    }
    System.out.println("getCRadnik "+uvjet);
    tmp = Util.getNewQueryDataSet(uvjet, true);
    tmp.first();
    String inStr="(";
    al.clear();
    while(tmp.inBounds())
    {
      if(tmp.getRow() < tmp.getRowCount()-1)
        inStr+="'" + tmp.getString("CRADNIK")+"', ";
      else
        inStr+="'" +  tmp.getString("CRADNIK")+"')";
      tmp.next();
    }


    if (whCond.equals(""))
    {
      if(isArhMode())
        strRadnik = " and cradnik in "+inStr;
      else
        strRadnik = " where cradnik in "+inStr;
    }
    else
      strRadnik = " and cradnik in "+inStr;
    return strRadnik;
  }

  private void menageDoprinosiNP(String cOrg, String cVro )
  {
    QueryDataSet tmp;
    String[] _araq = getAdditionalRadArhQuerys(1);
    String radnicipl = _araq[0];
    String obrrange = _araq[1];

    String uvjet = "select cradnik as cradnik from "+radnicipl+" where corg='"+cOrg+"' and cvro='"+cVro+"'"+obrrange;
    
    tmp = Util.getNewQueryDataSet(uvjet, true);
    tmp.first();
    al.clear();
    while(tmp.inBounds())
    {
      al.add(tmp.getString("CRADNIK"));
      tmp.next();
    }

    BigDecimal t = Aus.zero2;
    if(!doprinosDS.isOpen()) doprinosDS.open();
//    doprinosDS.deleteAllRows();

    for(int i = 0; i < al.size();i++)
    {
      QueryDataSet temp = null;
      if(this.getRepMode()=='A')
      {
        raOdb.setObrRange(fieldSet.getShort("GODINAOD"),fieldSet.getShort("MJESECOD"), fieldSet.getShort("RBROD"),
          fieldSet.getShort("GODINADO"),fieldSet.getShort("MJESECDO"), fieldSet.getShort("RBRDO"));
        temp = raOdb.getDoprinosiNa(al.get(i).toString(), raOdb.ARH);
      }
      else if (this.getRepMode()=='O')
        temp = raOdb.getDoprinosiNa(al.get(i).toString(), raOdb.OBR);
      if(!temp.isOpen()) temp.open();
      QueryDataSet rad_corg_vro_set = getRad_corg_vro_set();
      temp.first();

      while (temp.inBounds())
      {
        doprinosDS.insertRow(false);
        ld.raLocate(rad_corg_vro_set, new String[]{"CRADNIK"}, new String[]{temp.getString("CRADNIK")});
        doprinosDS.setString("CORG", rad_corg_vro_set.getString("CORG"));
        doprinosDS.setString("CVRO", rad_corg_vro_set.getString("CVRO"));
        doprinosDS.setShort("CVRODB", temp.getShort("CVRODB"));
        doprinosDS.setBigDecimal("IZNOS", temp.getBigDecimal("OBRIZNOS"));
        t = t.add(temp.getBigDecimal("OBRIZNOS"));
        temp.next();
      }
    }
    doprinosDS.post();

  }

  private void menageDoprinosiRNP(String cOrg, String cVro )
  {
    QueryDataSet tmp;
    String[] _araq = getAdditionalRadArhQuerys(1);
    String radnicipl = _araq[0];
    String obrrange = _araq[1];

    String uvjet = "select cradnik as cradnik from "+radnicipl+" where corg='"+cOrg+"' and cvro='"+cVro+"'"+obrrange;
    tmp = Util.getNewQueryDataSet(uvjet, true);
    tmp.first();
    al.clear();
    while(tmp.inBounds())
    {
      al.add(tmp.getString("CRADNIK"));
      tmp.next();
    }

    BigDecimal t = Aus.zero2;
    if(!doprinosRDS.isOpen()) doprinosRDS.open();

    for(int i = 0; i < al.size();i++)
    {
      QueryDataSet temp = null;
      if(this.getRepMode()=='A')
      {
        raOdb.setObrRange(fieldSet.getShort("GODINAOD"),fieldSet.getShort("MJESECOD"), fieldSet.getShort("RBROD"),
          fieldSet.getShort("GODINADO"),fieldSet.getShort("MJESECDO"), fieldSet.getShort("RBRDO"));
        temp = raOdb.getDoprinosiRadnik(al.get(i).toString(), raOdb.ARH);
      }
      else if (this.getRepMode()=='O')
        temp = raOdb.getDoprinosiRadnik(al.get(i).toString(), raOdb.OBR);
      if(!temp.isOpen()) temp.open();
      QueryDataSet rad_corg_vro_set = getRad_corg_vro_set();
      temp.first();
      while (temp.inBounds())
      {
        doprinosRDS.insertRow(false);
        ld.raLocate(rad_corg_vro_set, new String[]{"CRADNIK"}, new String[]{temp.getString("CRADNIK")});
        doprinosRDS.setString("CORG", rad_corg_vro_set.getString("CORG"));
        doprinosRDS.setString("CVRO", rad_corg_vro_set.getString("CVRO"));
        doprinosRDS.setShort("CVRODB", temp.getShort("CVRODB"));
        doprinosRDS.setBigDecimal("IZNOS", temp.getBigDecimal("OBRIZNOS"));
        t = t.add(temp.getBigDecimal("OBRIZNOS"));
        temp.next();
      }
    }
    doprinosRDS.post(); 

  }
  private QueryDataSet getRad_corg_vro_set() {
    String[] _araq = getAdditionalRadArhQuerys(0);
    String radnicipl = _araq[0];
    String obrrange = _araq[1];
    String q;
    QueryDataSet rad_corg_vro_set = Util.getNewQueryDataSet(q = "select cradnik, corg, cvro FROM "+radnicipl+" "+obrrange);
    System.out.println("rad_corg_vro_set "+q);
    return rad_corg_vro_set;
  }
  private QueryDataSet setDoprinosiGrouped( int cs)
  {
    QueryDataSet qdsGrouped = new QueryDataSet();

    qdsGrouped.setColumns(new Column[]{
    (Column) dm.getVrsteodb().getColumn("CVRODB").clone(),
    (Column) dm.getVrsteodb().getColumn("IZNOS").clone(),
    (Column) dm.getAllRadnicipl().getColumn("CORG").clone(),
    (Column) dm.getAllRadnicipl().getColumn("CVRO").clone()
    });

    switch (cs)
    {
      case 0:
        doprinosDS.setSort(new SortDescriptor(new String[]{"CVRODB"}));
        break;
      case 1:
        doprinosDS.setSort(new SortDescriptor(new String[]{ "CVRO","CVRODB"}));
        break;
      case 2:
        doprinosDS.setSort(new SortDescriptor(new String[]{"CORG", "CVRODB"}));
        break;
      case 3:
        break;

    }
    doprinosDS.first();

    if(!qdsGrouped.isOpen()) qdsGrouped.open();

    short cVrOd = -1;
    String cOrg ="";
    String cVro ="";
    while(doprinosDS.inBounds())
    {
      if(cs == 0)
      {
        if(cVrOd != doprinosDS.getShort("CVRODB"))
        {
          cVrOd = doprinosDS.getShort("CVRODB");
          qdsGrouped.insertRow(false);
          qdsGrouped.setShort("CVRODB", cVrOd);
          qdsGrouped.setString("CORG", doprinosDS.getString("CORG"));
          qdsGrouped.setString("CVRO", doprinosDS.getString("CVRO"));
        }
        qdsGrouped.setBigDecimal("IZNOS", qdsGrouped.getBigDecimal("IZNOS").add(doprinosDS.getBigDecimal("IZNOS")));
        doprinosDS.next();
      }
      else if(cs == 1)
      {
        if(cVrOd != doprinosDS.getShort("CVRODB") || !cVro.equals(doprinosDS.getString("CVRO")))
        {
          cVrOd = doprinosDS.getShort("CVRODB");
          cVro = doprinosDS.getString("CVRO");
          qdsGrouped.insertRow(false);
          qdsGrouped.setShort("CVRODB", cVrOd);
          qdsGrouped.setString("CORG", doprinosDS.getString("CORG"));
          qdsGrouped.setString("CVRO", doprinosDS.getString("CVRO"));
        }
        qdsGrouped.setBigDecimal("IZNOS", qdsGrouped.getBigDecimal("IZNOS").add(doprinosDS.getBigDecimal("IZNOS")));
        doprinosDS.next();
      }
      else if(cs == 2)
      {
        if(!cOrg.equals(doprinosDS.getString("CORG")) || cVrOd != doprinosDS.getShort("CVRODB"))
        {
          cOrg = doprinosDS.getString("CORG");
          cVrOd = doprinosDS.getShort("CVRODB");
          qdsGrouped.insertRow(false);
          qdsGrouped.setShort("CVRODB", cVrOd);
          qdsGrouped.setString("CORG", doprinosDS.getString("CORG"));
          qdsGrouped.setString("CVRO", doprinosDS.getString("CVRO"));
        }

        qdsGrouped.setBigDecimal("IZNOS", qdsGrouped.getBigDecimal("IZNOS").add(doprinosDS.getBigDecimal("IZNOS")));
        doprinosDS.next();
      }
      else
      {
        if(!cOrg.equals(doprinosDS.getString("CORG")) || cVrOd != doprinosDS.getShort("CVRODB") || !cVro.equals(doprinosDS.getString("CVRO")))
        {
          cOrg = doprinosDS.getString("CORG");
          cVrOd = doprinosDS.getShort("CVRODB");
          cVro = doprinosDS.getString("CVRO");
          qdsGrouped.insertRow(false);
          qdsGrouped.setShort("CVRODB", cVrOd);
          qdsGrouped.setString("CORG", doprinosDS.getString("CORG"));
          qdsGrouped.setString("CVRO", doprinosDS.getString("CVRO"));
        }

        qdsGrouped.setBigDecimal("IZNOS", qdsGrouped.getBigDecimal("IZNOS").add(doprinosDS.getBigDecimal("IZNOS")));
        doprinosDS.next();
      }
    }
    return qdsGrouped;
  }

  private QueryDataSet setDoprinosiRGrouped( int cs)
  {
    QueryDataSet qdsGrouped = new QueryDataSet();

    qdsGrouped.setColumns(new Column[]{
    (Column) dm.getVrsteodb().getColumn("CVRODB").clone(),
    (Column) dm.getVrsteodb().getColumn("IZNOS").clone(),
    (Column) dm.getAllRadnicipl().getColumn("CORG").clone(),
    (Column) dm.getAllRadnicipl().getColumn("CVRO").clone()
    });

    switch (cs)
    {
      case 0:
        doprinosRDS.setSort(new SortDescriptor(new String[]{"CVRODB"}));
        break;
      case 1:
        doprinosRDS.setSort(new SortDescriptor(new String[]{ "CVRO","CVRODB"}));
        break;
      case 2:
        doprinosRDS.setSort(new SortDescriptor(new String[]{"CORG", "CVRODB"}));
        break;
      case 3:
        break;

    }
    doprinosRDS.first();

    if(!qdsGrouped.isOpen()) qdsGrouped.open();

    short cVrOd = -1;
    String cOrg ="";
    String cVro ="";
    while(doprinosRDS.inBounds())
    {
      if(cs == 0)
      {
        if(cVrOd != doprinosRDS.getShort("CVRODB"))
        {
          cVrOd = doprinosRDS.getShort("CVRODB");
          qdsGrouped.insertRow(false);
          qdsGrouped.setShort("CVRODB", cVrOd);
          qdsGrouped.setString("CORG", doprinosRDS.getString("CORG"));
          qdsGrouped.setString("CVRO", doprinosRDS.getString("CVRO"));
        }
        qdsGrouped.setBigDecimal("IZNOS", qdsGrouped.getBigDecimal("IZNOS").add(doprinosRDS.getBigDecimal("IZNOS")));
        doprinosRDS.next();
      }
      else if(cs == 1)
      {
        if(cVrOd != doprinosRDS.getShort("CVRODB") || !cVro.equals(doprinosRDS.getString("CVRO")))
        {
          cVrOd = doprinosRDS.getShort("CVRODB");
          cVro = doprinosRDS.getString("CVRO");
          qdsGrouped.insertRow(false);
          qdsGrouped.setShort("CVRODB", cVrOd);
          qdsGrouped.setString("CORG", doprinosRDS.getString("CORG"));
          qdsGrouped.setString("CVRO", doprinosRDS.getString("CVRO"));
        }
        qdsGrouped.setBigDecimal("IZNOS", qdsGrouped.getBigDecimal("IZNOS").add(doprinosRDS.getBigDecimal("IZNOS")));
        doprinosRDS.next();
      }
      else if(cs == 2)
      {
        if(!cOrg.equals(doprinosRDS.getString("CORG")) || cVrOd != doprinosRDS.getShort("CVRODB"))
        {
          cOrg = doprinosRDS.getString("CORG");
          cVrOd = doprinosRDS.getShort("CVRODB");
          qdsGrouped.insertRow(false);
          qdsGrouped.setShort("CVRODB", cVrOd);
          qdsGrouped.setString("CORG", doprinosRDS.getString("CORG"));
          qdsGrouped.setString("CVRO", doprinosRDS.getString("CVRO"));
        }

        qdsGrouped.setBigDecimal("IZNOS", qdsGrouped.getBigDecimal("IZNOS").add(doprinosRDS.getBigDecimal("IZNOS")));
        doprinosRDS.next();
      }
      else
      {
        if(!cOrg.equals(doprinosRDS.getString("CORG")) || cVrOd != doprinosRDS.getShort("CVRODB") || !cVro.equals(doprinosRDS.getString("CVRO")))
        {
          cOrg = doprinosRDS.getString("CORG");
          cVrOd = doprinosRDS.getShort("CVRODB");
          cVro = doprinosRDS.getString("CVRO");
          qdsGrouped.insertRow(false);
          qdsGrouped.setShort("CVRODB", cVrOd);
          qdsGrouped.setString("CORG", doprinosRDS.getString("CORG"));
          qdsGrouped.setString("CVRO", doprinosRDS.getString("CVRO"));
        }

        qdsGrouped.setBigDecimal("IZNOS", qdsGrouped.getBigDecimal("IZNOS").add(doprinosRDS.getBigDecimal("IZNOS")));
        doprinosRDS.next();
      }
    }
    return qdsGrouped;
  }


  // Doprinosi NA
  public static QueryDataSet getDoprinosiNaZN()
  {
    if(!qdsDoprinosNaZN.isOpen()) qdsDoprinosNaZN.open();
    return qdsDoprinosNaZN;
  }

  public static QueryDataSet getDoprinosiNaORG()
  {
    if(!qdsDoprinosNaORG.isOpen()) qdsDoprinosNaORG.open();
    return qdsDoprinosNaORG;
  }

  public static QueryDataSet getDoprinosiNaVRO()
  {
    if(!qdsDoprinosNaVRO.isOpen()) qdsDoprinosNaVRO.open();
    return qdsDoprinosNaVRO;
  }

  public static QueryDataSet getDoprinosiNaPOJ()
 {
   if(!qdsDoprinosNaPOJ.isOpen()) qdsDoprinosNaPOJ.open();
   return qdsDoprinosNaPOJ;
  }

  // doprinosi IZ

  public static QueryDataSet getDoprinosiIzZN()
  {
    if(!qdsDoprinosIzZN.isOpen()) qdsDoprinosIzZN.open();
    return qdsDoprinosIzZN;
  }

  public static QueryDataSet getDoprinosiIzORG()
  {
    if(!qdsDoprinosIzORG.isOpen()) qdsDoprinosIzORG.open();
    return qdsDoprinosIzORG;
  }

  public static QueryDataSet getDoprinosiIzVRO()
  {
    if(!qdsDoprinosIzVRO.isOpen()) qdsDoprinosIzVRO.open();
    return qdsDoprinosIzVRO;
  }

  public static QueryDataSet getDoprinosiIzPOJ()
 {
   if(!qdsDoprinosIzPOJ.isOpen()) qdsDoprinosIzPOJ.open();
   return qdsDoprinosIzPOJ;
  }
}

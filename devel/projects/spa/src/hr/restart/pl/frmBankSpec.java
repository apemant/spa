/****license*****************************************************************
**   file: frmBankSpec.java
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
import hr.restart.baza.Condition;
import hr.restart.baza.Orgstruktura;
import hr.restart.robno.dlgKupac;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.frmVirmani;
import hr.restart.zapod.repDiskZapUN;

import java.awt.BorderLayout;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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

public class frmBankSpec extends frmIzvjestajiPL{

  JPanel jpIspMj = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();
  private JlrNavField jlfCIspMJ = new JlrNavField(){
    public void after_lookUp()
    {
      try {
        fieldSet.setString("CISPLMJ", jlfCIspMJ.getText());
        fieldSet.setString("TIPFILE", jrfTD.getText());
        setOpis(jrfTD.getText());
        fieldSet.open();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  };
  private JlrNavField jlfNazIsplMJ = new JlrNavField();
  private JLabel jlIsplMJ = new JLabel();
  private JraButton jbIsplMJ = new JraButton();
  public static String tipFile;
  public static String tipRep;
  public static String vbdi;
  private JLabel jlTipDat = new JLabel();
  JraTextField jrfTD = new JraTextField();
  JraTextField jrfTDOpis = new JraTextField();
  private JraCheckBox jcbIzn = new JraCheckBox();
  private JraCheckBox jcbSaldo = new JraCheckBox();
  private JraCheckBox jcbNula = new JraCheckBox();
  Column column = new Column();
  Column column2 = new Column();
  public static frmBankSpec fBankSpec;
  public static frmBankSpec fBankSpecA;
  private static QueryDataSet qds = new QueryDataSet();
  private static int  brRad = 0;
  private char mode;
  public static double suma;
  public static String labela="";
  public static String IMOj="";

  public static frmBankSpec getInstanceA()
  {
    if(fBankSpecA==null)
      fBankSpecA = new frmBankSpec('A');
      fBankSpec = fBankSpecA;
    return fBankSpecA;
  }
  public static frmBankSpec getInstance()
  {
    if(fBankSpec==null)
      fBankSpec = new frmBankSpec();
    return fBankSpec;
  }

  public frmBankSpec() {
    this('O');
  }


  public frmBankSpec(char mode) {
    super(mode);
    this.mode = mode;

    try {
      jbInitA();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    bindanje();
    setDifolt();
  }

  void jbInitA() throws Exception
  {
    xYLayout1.setWidth(585);
    xYLayout1.setHeight(80);
    jpIspMj.setLayout(xYLayout1);
    jcbIzn.setText("Ispisati Iznos u popisnoj listi");
    jcbIzn.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbIzn.setHorizontalTextPosition(SwingConstants.LEADING);

    jcbSaldo.setText("Ukupni iznos");
    jcbSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbSaldo.setHorizontalTextPosition(SwingConstants.LEADING);

    jcbNula.setText("Ukljuèiti iznos = 0");
    jcbNula.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbNula.setHorizontalTextPosition(SwingConstants.LEADING);

    this.mainPanel.add(jpIspMj, BorderLayout.CENTER);
    jlTipDat.setText("Tip datoteke");
    jrfTD.setVisible(false);

    jcbIzn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        jcbIzn_actionPerformed(e);
      }
    });

    jlIsplMJ.setText("Isplatno mjesto");
    jbIsplMJ.setText("...");
    jpIspMj.add(jlfCIspMJ,     new XYConstraints(150, 0, 100, -1));
    jpIspMj.add(jlfNazIsplMJ,           new XYConstraints(255, 0, 295, -1));
    jpIspMj.add(jlIsplMJ,      new XYConstraints(15, 0, -1, -1));
    jpIspMj.add(jbIsplMJ,        new XYConstraints(555, 0, 21, 21));

    jpIspMj.add(jlTipDat,      new XYConstraints(15, 25, -1, -1));
    jpIspMj.add(jrfTD,      new XYConstraints(150, 25, 100, 20));
    jpIspMj.add(jrfTDOpis,      new XYConstraints(150, 25, 100, 20));
    jpIspMj.add(jcbIzn,        new XYConstraints(370, 25, -1, -1));
    jpIspMj.add(jcbSaldo,        new XYConstraints(455, 50, -1, -1));
    jpIspMj.add(jcbNula,        new XYConstraints(150, 50, -1, -1));
    column.setColumnName("CISPLMJ");
    column.setDataType(com.borland.dx.dataset.Variant.STRING);
    if (fieldSet.hasColumn("CISPLMJ")==null) fieldSet.addColumn(column);
    column2.setColumnName("TIPFILE");
    column2.setDataType(com.borland.dx.dataset.Variant.STRING);
    if (fieldSet.hasColumn("TIPFILE")==null) fieldSet.addColumn(column2);
  }

  public void bindanje() {
    jlfCIspMJ.setColumnName("CISPLMJ");
    jlfCIspMJ.setDataSet(fieldSet);
    jlfCIspMJ.setColNames(new String[] {"NAZIV", "TIPFILE"});
    jlfCIspMJ.setTextFields(new JTextField[] {jlfNazIsplMJ,jrfTD});
    jlfCIspMJ.setVisCols(new int[] {0,1});
    jlfCIspMJ.setRaDataSet(dm.getIsplMJ());
    jlfCIspMJ.setSearchMode(0);
    jlfCIspMJ.setNavButton(this.jbIsplMJ);
    jlfNazIsplMJ.setNavProperties(jlfCIspMJ);
    jlfNazIsplMJ.setSearchMode(1);
    jlfNazIsplMJ.setColumnName("NAZIV");

    jrfTD.setColumnName("TIPFILE");
  }
  public static QueryDataSet getSpecBankDS(String[] sort)
  {
    qds.setSort(new SortDescriptor(sort));
    return qds;
  }

  public boolean Validacija()
  {
    String nazRep ="";
    tipRep ="";
    vbdi="";
    tipFile = fieldSet.getString("TIPFILE");
    suma = 0;
    killAllReports();
    if(jcbIzn.isSelected())
    {
      this.addReport("hr.restart.pl.repPotpListOJ_Izn", "Potpisna lista po org. jedinicama", 5);
      this.addReport("hr.restart.pl.repPopListIM_Izn", "Potpisna lista po isplatnim mjestima", 5);
    }
    else
    {
      if(!jcbSaldo.isSelected())
      {
        this.addReport("hr.restart.pl.repPotpListOJ", "Potpisna lista po org. jedinicama", 5);
        this.addReport("hr.restart.pl.repPopListIM", "Potpisna lista po isplatnim mjestima", 5);
      }
      else
      {
        this.addReport("hr.restart.pl.repPotpListOJ_IznNull", "Potpisna lista po org. jedinicama", 5);
        this.addReport("hr.restart.pl.repPopListIM_IznNull", "Potpisna lista po isplatnim mjestima", 5);
      }
    }
    this.addReport("hr.restart.pl.repSpecBank", "Specifikacija za banku", 5);
    if(!fieldSet.getString("TIPFILE").equals(""))
    {
      this.addReport("hr.restart.zapod.repDiskZapUN", "Univerzalna specifikacija za banku (IBAN)", 5);
      if(fieldSet.getString("TIPFILE").equals("1"))
      {
        this.addReport("hr.restart.pl.repSBDisk_Zaba", "Specifikacija za banku disketa - ZABA", 5);
        tipRep = "ZABA";
        vbdi = "2360000";
      }
      else if(fieldSet.getString("TIPFILE").equals("2"))
      {
        this.addReport("hr.restart.pl.repSBDisk_Pbz", "Specifikacija za banku disketa - PBZ", 5);
        tipRep = "PBZ";
        vbdi="2340009";
      }
      else if(fieldSet.getString("TIPFILE").equals("3"))
      {
        this.addReport("hr.restart.pl.repSBDisk_Rba", "Specifikacija za banku disketa - RBA", 5);
        tipRep = "RAIF";
        vbdi="2484008";
      }
      else if(fieldSet.getString("TIPFILE").equals("4"))
      {
         this.addReport("hr.restart.pl.repSBDisk_Splitska", "Specifikacija za banku disketa - Splitska banka", 5);
        tipRep = "STB";
        vbdi="2330003";
      }
      
    }
    
    if(!prepareIspis())
    {
      focusManage();
      JOptionPane.showConfirmDialog(this,"Nema podataka koji zadovoljavaju traženi uvjet !",
                                      "Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        return false;
      }
    return true;
  }

  public boolean prepareIspis()
  {
    labela = "Obra\u010Dun za "+jraMjesecOd.getText()+". mjesec "+jraGodinaOd.getText()+". (rbr. "+jraRbrOd.getText()+")";
    if(jlrCorg.getText().equals(""))
      IMOj="zbirno";
    else
      IMOj="za org. jed "+jlrNazorg.getText();
    String ispMjAdd = "";
    String cOrgAdd ="";
    String arhiva ="";

    if(jlrCorg.getText().equals(""))
      cOrgAdd = getPripOrg(hr.restart.zapod.OrgStr.getKNJCORG(), "RADNICIPL");
    else
      cOrgAdd = "radnicipl.corg='"+jlrCorg.getText()+"'";

    if(!jlfCIspMJ.getText().equals(""))
      ispMjAdd = " and radnicipl.cisplmj="+jlfCIspMJ.getText();
    
    if (mode=='A') {
      arhiva = getArhRangeQuery();
    }
    String qStr = null;
    String cvrodb = frmParam.getParam("pl","SPC"+jlfCIspMJ.getText(), "", "Za koju vrstu odbitka se radi specif.za ispl.mj."+jlfCIspMJ.getText()).trim();
    if (lookupData.getlookupData().raLocate(dm.getVrsteodb(), "CVRODB", cvrodb)) {
      QueryDataSet imset = Aus.q("SELECT cbanke from isplmj where cisplmj = "+jlfCIspMJ.getText());
      imset.open(); imset.first();
      int cbanke = imset.getInt("CBANKE");
      qStr = "select radnicipl.corg as corg, radnici.cradnik as cradnik, radnici.ime as ime, radnici.prezime as prezime, odbici.pnb2 as brojtek,"+
          "radnicipl.jmbg as jmbg, CAST ("+jlfCIspMJ.getText()+" as numeric(4)) as isplmj, "
              + ""+cbanke+" as cbanke, odbiciobr.obriznos as naruke, CAST ("+jlfCIspMJ.getText()+" as numeric(4)) as cisplmj "+
          "from radnici,radnicipl, odbici, odbiciobr where radnici.cradnik = radnicipl.cradnik "+
          " and odbiciobr.cradnik = radnicipl.cradnik AND odbici.ckey = odbiciobr.ckey and odbici.cvrodb = odbiciobr.cvrodb "
          + (jcbNula.isSelected()?"":" and odbiciobr.obriznos > 0 ")
          + "AND odbici.rbrodb = odbiciobr.rbrodb AND odbici.cvrodb = "+cvrodb
          //"and isplmj.cbanke = bankepl.cbanke and radnicipl.cisplmj =isplmj.cisplmj "+
          //"and "+getKumRadTableName()+".cradnik = radnicipl.cradnik"
              + " and "+cOrgAdd/*+ispMjAdd+arhiva*/;
    } else {
      qStr = "select radnicipl.corg as corg, radnici.cradnik as cradnik, radnici.ime as ime, radnici.prezime as prezime,radnicipl.brojtek as brojtek,"+
          "radnicipl.jmbg as jmbg, radnicipl.cisplmj as isplmj, bankepl.cbanke as cbanke, "+getKumRadTableName()+".naruke as naruke "+
          "from radnici,radnicipl, bankepl, isplmj, "+getKumRadTableName()+" where radnici.cradnik = radnicipl.cradnik "+
          "and isplmj.cbanke = bankepl.cbanke and radnicipl.cisplmj =isplmj.cisplmj "+
          (jcbNula.isSelected()?"":" and "+getKumRadTableName()+".naruke > 0 ")+ 
          "and "+getKumRadTableName()+".cradnik = radnicipl.cradnik and "+cOrgAdd+ispMjAdd+arhiva;
    }
    
System.out.println("BankSpec.qStr = "+qStr);
    if(qds.isOpen()) qds.close();
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));

    qds.open();
//    sysoutTEST ST = new sysoutTEST(false);
    //    ST.prn(qds);11
    //virmani
    String ckey = "bankspec"+OrgStr.getKNJCORG();
    Valid.getValid().runSQL("DELETE FROM virmani where ckey = '"+ckey+"'");
    frmVirmani fV = frmVirmani.getInstance();  
    fV.getRaQueryDataSet().emptyAllRows();
    fV.setKeys("zapod", OrgStr.getKNJCORG(), ckey);
    QueryDataSet naTDS = Orgstruktura.getDataModule().getFilteredDataSet(Condition.equal("CORG",OrgStr.getKNJCORG()));
    naTDS.open();
    raIniciranje.getInstance().posOrgsPl(OrgStr.getOrgStr().getKNJCORG());
    Timestamp datTS = dm.getOrgpl().getTimestamp("DATUMISPL");
    String svrha = frmParam.getParam("pl", "svrhaUN", "dohodak", "svrha doznake u IBAN datoteci za tekuæe");
    //
    /**
p1 Jedinica zavoda
p2 Na teret racuna
p3 Svrha doznake
p4 U korist racuna
p5 Broj racuna na teret ili IBAN
p6 Nacin izvrs
p7 Poziv na broj (zaduz.) 1
p8 Poziv na broj (zaduz.) 2
p9 Sifra 1
p10 Sifra 2
p11 Sifra 3
p12 Broj racuna u korist ili IBAN
p13 Poziv na broj (odobr.) 1
p14 Poziv na broj (odobr.) 2
p15 Iznos
p16 Mjesto
p17 Datum izvrsenja
p18 Datum predaje
     */
    qds.first();
    while(qds.inBounds())
    {
      suma += qds.getBigDecimal("NARUKE").doubleValue();
      
      fV.add("", "", svrha, "", naTDS.getString("ZIRO"), "", "", "", "", "", "", vbdi+"-"+qds.getString("BROJTEK"), "", "", 
          qds.getBigDecimal("NARUKE"), "mjesto", datTS, Valid.getValid().getToday());
      qds.next();
    }
    
    fV.save(false);
    repDiskZapUN.setVrstaNalogaUDatoteci("4");
    if(qds.getRowCount() > 0)
      return true;
    return false;
  }


private String getArhRangeQuery() {
  if (mode!='A') return "";
    return " AND "+new raPlObrRange(       
        fieldSet.getShort("GODINAOD"),
        fieldSet.getShort("MJESECOD"),
        fieldSet.getShort("RBROD")).getQuery(getKumRadTableName());
  }
///////////////////***************************** Prebaciti kod srkija u main klasu
  public int getbrDjelatnika(String cOrg)
  {
    String tabRad=getKumRadTableName();
    String qStr = "select distinct count (*) from radnici, "+tabRad+" where radnici.cradnik = "+tabRad+".cradnik "+
                  "and radnici."+this.getWhereQuery();
    if(!cOrg.equals(""))
     qStr += " and radnici.corg='"+cOrg+"'";
    QueryDataSet rQDS = new QueryDataSet();
    rQDS.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    return Valid.getValid().getSetCount(rQDS,0);
  }

  public int getbrDjelatnikaVRO(String cvro, String cOrg)
 {
   String tabRad=getKumRadTableName();
   String qStr = "select distinct count (*) from radnici,"+tabRad+", radnicipl where radnici.cradnik ="+tabRad+".cradnik "+
                 " and radnici.cradnik=radnicipl.cradnik and radnici."+this.getWhereQuery()+" and radnicipl.cvro='"+cvro+"'";

   if(!cOrg.equals(""))
     qStr += " and radnici.corg='"+cOrg+"'";
   if (mode == 'A') {
     
   }
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
    jlfCIspMJ.setText("");
    jlfCIspMJ.forceFocLost();
    rcc.setLabelLaF(jrfTDOpis,false);
    jcbSaldo.setSelected(true);
    jcbSaldo.setVisible(true);
    jcbNula.setSelected(false);
    super.componentShow();
  }

  public void firstESC()
  {
    if(firstesc==false)
    {
      if(!jlfCIspMJ.getText().equals(""))
      {
        jlfCIspMJ.setText("");
        jlfCIspMJ.forceFocLost();
        jlfCIspMJ.requestFocus();
      }
      else if (!jlrCorg.getText().equals(""))
      {
        jlrCorg.setText("");
        jlrCorg.forceFocLost();
        jlrCorg.requestFocus();
      }
    }
    else
    {
      jlfCIspMJ.setText("");
      jlfCIspMJ.forceFocLost();
      super.firstESC();

      rcc.EnabDisabAll(jpIspMj,true);

    }
    rcc.setLabelLaF(jrfTDOpis,false);
  }

  public boolean runFirstESC(){
    if(firstesc==false && (!jlfCIspMJ.getText().equals("") || !jlrCorg.getText().equals("")))
      return true;
    else if (firstesc==false && (jlfCIspMJ.getText().equals("") || jlrCorg.getText().equals("")))
      return false;
    else
      return firstesc;
  }

  public String getPripOrg(String str, String corgTable)
  {
//    int i = 0;
//    String cVrati;
//    cVrati=" "+corgTable.trim()+".CORG in (";
//    com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
//    tds.first();
//    do
//    {
//      if (i>0) {
//        cVrati=cVrati+',';
//      }
//      i++;
//      cVrati=cVrati+"'"+tds.getString("CORG")+"'";
//      tds.next();
//    } while (tds.inBounds());
//    cVrati=cVrati+")";
//    return cVrati;
    return " "+Condition.in("CORG", hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str)).qualified(corgTable);
  }

  public void setDifolt(){
    fieldSet.setString("CORG", "");
    jlrCorg.setText("");
    jlrCorg.forceFocLost();
    jlMjGodOd.setText("Obrada");
    jlRbr.setText("Redni broj");
    if (mode!='A') { 
      rcc.EnabDisabAll(jPanel2, false);
    }
    rcc.setLabelLaF(jraMjesecDo, false);
    rcc.setLabelLaF(jraGodinaDo, false);
    rcc.setLabelLaF(jraRbrDo, false);
    jraMjesecDo.setVisible(false);
    jraGodinaDo.setVisible(false);
    jraRbrDo.setVisible(false);
    
    jlrCorg.requestFocus();
  }

  private void setOpis(String i)
  {
    if(i.equals("1"))
      jrfTDOpis.setText("ZABA");
    else if(i.equals("2"))
      jrfTDOpis.setText("PBZ");
    else if(i.equals("3"))
      jrfTDOpis.setText("RBA");
    else if(i.equals("4"))
      jrfTDOpis.setText("STB");
    else
      jrfTDOpis.setText("");
  }

  private void focusManage()
  {
    if(!jlfCIspMJ.getText().equals(""))
    {
      jlfCIspMJ.requestFocus();
    }
    else
    {
      jlrCorg.requestFocus();
    }
  }

  public void jcbIzn_actionPerformed(java.awt.event.ActionEvent e)
  {
    if(jcbIzn.isSelected())
      jcbSaldo.setVisible(false);
    else
    {
      jcbSaldo.setVisible(true);
      jcbSaldo.setSelected(true);
    }
  }
}


/****license*****************************************************************
**   file: upObrAmortizacija.java
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
package hr.restart.os;

import hr.restart.baza.Condition;
import hr.restart.baza.Orgstruktura;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raImages;
import hr.restart.util.raUpitLite;
import hr.restart.util.sysoutTEST;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.dlgGetKnjig;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class upObrAmortizacija extends raUpitLite {
hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  boolean lIma=false;
  JraButton jbIspis = new JraButton();
  raosFakeKalk rofk = new raosFakeKalk();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.os.osUtil osu = hr.restart.os.osUtil.getUtil();
  int mon1, mon2, god1, god2, mProm;
  int mjesecL, godinaL, mjesecP, godinaP;
  Calendar cal = Calendar.getInstance();
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JraCheckBox jcbREVAAMOR = new JraCheckBox();
  JlrNavField jrfCORG = new JlrNavField();
  JlrNavField jrfNAZORG = new JlrNavField();
  JraButton jbCORG = new JraButton();
  JraTextField jtfDATUMOD = new JraTextField();
  JraTextField jtfDATUMDO = new JraTextField();
  raComboBox rcbTIPAMOR = new raComboBox() {
    public void this_itemStateChanged() {
      if (rcbTIPAMOR.getDataValue().equals("P")) {
        fillYearData();
      }
      else if (rcbTIPAMOR.getDataValue().equals("M")) {
        fillMonthData();
      }
      else if (rcbTIPAMOR.getDataValue().equals("K")) {
        fillYearData();
      }
      else if (rcbTIPAMOR.getDataValue().equals("S")) {
        fillSimData();
      }
      super.this_itemStateChanged();
    }
  };
  raComboBox rcbVRSTAAMOR = new raComboBox();
  raComboBox rcbVRSTAREVA = new raComboBox();
  private JraCheckBox jcbREAL = new JraCheckBox();

  public upObrAmortizacija() {
    try {
//      dm.getOS_Metaobrada().open();
//      dm.getOS_Kontrola().open();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jbIspis.setMaximumSize(new Dimension(100, 27));
    jbIspis.setMinimumSize(new Dimension(100, 27));
    jbIspis.setPreferredSize(new Dimension(100, 27));
    jbIspis.setText("Ispis");
    jbIspis.setIcon(raImages.getImageIcon(raImages.IMGPRINT));
    jbIspis.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbIspis_actionPerformed(e);
      }
    });
//    jbIspis.setIcon();
    jLabel1.setText("Org. jedinica");
    jp.setLayout(xYLayout1);
    jLabel2.setText("Datum (od-do)");
    jLabel3.setText("Tip amortizacije");
    jLabel4.setText("Vrsta amortizacije");
    jLabel5.setText("Vrsta revalorizacije");
    jcbREVAAMOR.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbREVAAMOR.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbREVAAMOR.setText("Revalorizacija amortizacije");
    jcbREVAAMOR.setDataSet(dm.getOS_Metaobrada());
    jcbREVAAMOR.setSelectedDataValue("D");
    jcbREVAAMOR.setUnselectedDataValue("N");
    jcbREVAAMOR.setColumnName("REVAAMOR");
    jrfCORG.setColumnName("CORG");
    jrfCORG.setDataSet(dm.getOS_Metaobrada());
    jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jrfCORG.setVisCols(new int[] {0,1});
    jrfCORG.setSearchMode(0);
    jrfCORG.setColNames(new String[] {"NAZIV"});
    jrfCORG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZORG});
    jrfCORG.setNavButton(jbCORG);
    jrfNAZORG.setColumnName("NAZIV");
    jrfNAZORG.setSearchMode(1);
    jrfNAZORG.setNavProperties(jrfCORG);
    jbCORG.setText("...");
    xYLayout1.setWidth(600);
    xYLayout1.setHeight(160);
    jtfDATUMOD.setDataSet(dm.getOS_Metaobrada());
    jtfDATUMOD.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUMOD.setColumnName("DATUMOD");
    jtfDATUMDO.setDataSet(dm.getOS_Metaobrada());
    jtfDATUMDO.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUMDO.setColumnName("DATUMDO");
    rcbTIPAMOR.setRaDataSet(dm.getOS_Metaobrada());
    rcbTIPAMOR.setRaColumn("TIPAMOR");
    rcbVRSTAAMOR.setRaDataSet(dm.getOS_Metaobrada());
    rcbVRSTAAMOR.setRaColumn("VRSTAAMOR");
    rcbVRSTAAMOR.setRaItems(new String [][] {
      {"Po pojedinom osnovnom sredstvu","P"},
      {"Po amortizacijskim grupa",      "A"}
    });
    rcbVRSTAREVA.setRaDataSet(dm.getOS_Metaobrada());
    rcbVRSTAREVA.setRaColumn("VRSTAREVA");
    rcbVRSTAREVA.setRaItems(new String [][] {
    {"Skupni koeficjent",             "S"},
    {"Po ravlorizacijskim skupinama", "R"}
    });
    jcbREAL.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbREAL.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbREAL.setText("Stvarni obraèun");
    jcbREAL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbREAL_actionPerformed(e);
      }
    });
    jp.add(jLabel1,  new XYConstraints(15, 20, -1, -1));
    jp.add(jLabel2,   new XYConstraints(15, 45, -1, -1));
    jp.add(jLabel3,   new XYConstraints(15, 70, -1, -1));
    jp.add(jLabel4,   new XYConstraints(15, 95, -1, -1));
    jp.add(jLabel5,   new XYConstraints(15, 120, -1, -1));
    jp.add(jrfCORG,  new XYConstraints(150, 20, 100, -1));
    jp.add(jrfNAZORG,         new XYConstraints(255, 20, 305, -1));
    jp.add(jbCORG,      new XYConstraints(564, 20, 21, 21));
    jp.add(jtfDATUMOD,     new XYConstraints(150, 45, 100, -1));
    jp.add(jtfDATUMDO,   new XYConstraints(255, 45, 100, -1));
    jp.add(rcbTIPAMOR,    new XYConstraints(150, 70, 205, -1));
    jp.add(rcbVRSTAAMOR,   new XYConstraints(150, 95, 205, -1));
    jp.add(rcbVRSTAREVA,   new XYConstraints(150, 120, 205, -1));
    jp.add(jcbREVAAMOR,     new XYConstraints(360, 120, 200, -1));
    jp.add(jcbREAL,    new XYConstraints(360, 45, 200, -1));
    this.setJPan(this.jp);
    this.getOKPanel().add(jbIspis, BorderLayout.WEST);
  }
  public void componentShow() {
    dm.getOS_Metaobrada().open();

    if (dm.getOS_Metaobrada().rowCount()>0) {

      if (dm.getOS_Metaobrada().getString("TIPAMOR").equals("K")) {
        JOptionPane.showConfirmDialog(jp, "Za tekuæu godinu je izvršen godišnji obraèun amortizacije !","Informacija", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
      }
      if (dm.getOS_Metaobrada().getString("TIPAMOR").equals("P")) {
        if (JOptionPane.showConfirmDialog(jp, "Postoji godišnji predraèun amortizacije. Želite li ga poništiti ?","Poništenje privremenih obrada",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
          hr.restart.os.osUtil.getUtil().emptyAmorTable();
        }
      }
      if (dm.getOS_Metaobrada().getString("TIPAMOR").equals("S")) {
        if (JOptionPane.showConfirmDialog(jp, "Postoji simulacija obraèuna amortizacije ("+hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(dm.getOS_Metaobrada().getTimestamp("DATUMOD"))+"-"+hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(dm.getOS_Metaobrada().getTimestamp("DATUMDO"))+"). Želite li ga poništiti ?","Poništenje privremenih obrada",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
          hr.restart.os.osUtil.getUtil().emptyAmorTable();
        }
      }
      if (!checkAmor()) {
        JOptionPane.showConfirmDialog(jp, "Obraèun amortizacije ("+hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(dm.getOS_Metaobrada().getTimestamp("DATUMOD"))+"-"+hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(dm.getOS_Metaobrada().getTimestamp("DATUMDO"))+") treba poništiti ili verificirati !","Informacija", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
      }
      if (dm.getOS_Metaobrada().rowCount()>0) {
        if(!dm.getOS_Metaobrada().getString("CORG").equals("")) {
          rcc.EnabDisabAll(jp, false);
          rcc.setLabelLaF(this.jbIspis, true);
          rcc.setLabelLaF(this.getOKPanel().jBOK, false);
          return;
        }
      }
    }
    dm.getOS_Metaobrada().insertRow(true);
    rcc.EnabDisabAll(jp, true);
    rcc.setLabelLaF(this.getOKPanel().jBOK, true);
    fillYearData();
    setSimulacija();
    jcbREAL.setSelected(false);

    jLabel5.setEnabled(false);
    rcc.setLabelLaF(this.rcbVRSTAREVA, false);
    rcc.setLabelLaF(this.jcbREVAAMOR, false);
    rcc.setLabelLaF(this.jbIspis, false);
//    rcc.setLabelLaF(this.getOKPanel().jBOK, false);
    jrfCORG.setText(hr.restart.zapod.OrgStr.getKNJCORG());
    jrfCORG.forceFocLost();
    dm.getOS_Metaobrada().setString("TIPAMOR", "P");
    dm.getOS_Metaobrada().setString("VRSTAAMOR", "P");
    dm.getOS_Metaobrada().setString("VRSTAREVA", "S");
//    dm.getOS_Metaobrada().setTimestamp("DATUMOD", util.findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    
    dm.getOS_Metaobrada().setTimestamp("DATUMOD", util.findFirstDayOfYear(Integer.parseInt(getOS_Kontrola().getString("GODINA"))));
    if (getOS_Kontrola().getString("GODINA").compareTo(vl.findYear())==0) {
      java.sql.Timestamp ts =   hr.restart.util.Util.getUtil().clearTime(vl.getToday());
    }
//    dm.getOS_Metaobrada().setTimestamp("DATUMDO", ts);
//    dm.getOS_Metaobrada().setTimestamp("DATUMDO", vl.getToday());
//    jrfCORG.requestFocus();
  }
  public void cancelPress() {
    dm.getOS_Metaobrada().cancel();
    this.hide();
  }
  public void firstESC() {

  }
  public boolean runFirstESC() {
    return false;
  }
  public boolean isIspis() {
    return false;
  }
  public void showMessage() {
//    if (dm.getOS_Obrada1().getRowCount()>0) {
      JOptionPane.showConfirmDialog(jp, "Obraèun je uspješno završen !","Informacija", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
/*    }
    else {
      JOptionPane.showConfirmDialog(jp,"Nema podataka za obradu !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    }*/
  }
  public boolean Validacija() {
    System.out.println("Validacija");
    if (vl.isEmpty(jrfCORG)) {
      return false;
    }
    if (vl.isEmpty(jtfDATUMOD)) {
      return false;
    }
    if (vl.isEmpty(jtfDATUMDO)) {
      return false;
    }
    if (osUtil.getUtil().getSifraObrAmor().equals("")) {
      JOptionPane.showConfirmDialog(jp,"Ne postoji definirana vrsta promjene za amortizaciju !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    cal.setTime(dm.getOS_Metaobrada().getTimestamp("DATUMOD"));
    mon1=cal.get(Calendar.MONTH);
    god1=cal.get(Calendar.YEAR);
    cal.setTime(dm.getOS_Metaobrada().getTimestamp("DATUMDO"));
    mon2=cal.get(Calendar.MONTH);
    god2=cal.get(Calendar.YEAR);
    if (dm.getOS_Metaobrada().getTimestamp("DATUMDO").before(dm.getOS_Metaobrada().getTimestamp("DATUMOD"))) {
      return false;
    }
    // kontrola za mjesecni obracun
    if (dm.getOS_Metaobrada().getString("TIPAMOR").equals("M")) {
      if (god1!=god2) {
        JOptionPane.showConfirmDialog(jp,"Pogre\u0161an datum !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        jtfDATUMOD.requestFocus();
        jtfDATUMOD.selectAll();
        return false;
      }
    }
    // kontrola za ostale obracune (predraèun i konaèni)
    else if (dm.getOS_Metaobrada().getString("TIPAMOR").equals("P") || dm.getOS_Metaobrada().getString("TIPAMOR").equals("K")) {
      if (mon1!=0 || mon2!=11 || god1!=god2) {
        JOptionPane.showConfirmDialog(jp,"Pogre\u0161an datum !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        jtfDATUMOD.requestFocus();
        jtfDATUMOD.selectAll();
        return false;
      }
    }
    dm.getOS_Metaobrada().post();
//
    sysoutTEST st = new sysoutTEST(false);
    st.prn(dm.getOS_Metaobrada());
//    dm.getOS_Metaobrada().setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());

    dm.getOS_Metaobrada().saveChanges();
    return true;
  }
  public void okPress() {
    if(!provjeraObrade())
    {
//      jrfCORG.requestFocus();
      return;
    }

    int x=0;
    if (!Validacija()) {
      return;
    }

/*************************************************************************************************************
 * OBRADA STAROG SREDSTVA
 *************************************************************************************************************/
    String sSql;
    sSql="select OS_SREDSTVO.CORG2, OS_SREDSTVO.INVBROJ, OS_SREDSTVO.NAZSREDSTVA, OS_SREDSTVO.CLOKACIJE, "+
            "OS_SREDSTVO.CARTIKLA, OS_SREDSTVO.CPAR, OS_SREDSTVO.BROJKONTA, OS_SREDSTVO.CGRUPE, "+
            "OS_AMGRUPE.ZAKSTOPA, OS_AMGRUPE.ODLSTOPA, OS_SREDSTVO.CSKUPINE,OS_REVSKUPINE.KOEFICIJENT, "+
            "OS_SREDSTVO.DATAKTIVIRANJA, OS_SREDSTVO.OSNPOCETAK, OS_SREDSTVO.ISPPOCETAK, OS_SREDSTVO.REVOSN, "+
            "OS_SREDSTVO.REVISP "+
            "from OS_SREDSTVO, OS_AMGRUPE, OS_REVSKUPINE "+
            "where OS_SREDSTVO.CGRUPE=OS_AMGRUPE.CGRUPE AND OS_SREDSTVO.CSKUPINE=OS_REVSKUPINE.CSKUPINE "+
            " AND DATAKTIVIRANJA<"+util.getTimestampValue(dm.getOS_Metaobrada().getTimestamp("DATUMDO"), util.NUM_LAST)+
            " AND "+getPripOrg()+" AND (OS_SREDSTVO.DATLIKVIDACIJE IS NULL or OS_SREDSTVO.DATLIKVIDACIJE>"+util.getTimestampValue(dm.getOS_Metaobrada().getTimestamp("DATUMDO"), util.NUM_LAST)+") "+
            "and (os_sredstvo.status='A')";
//            "and (os_sredstvo.status='A' or os_sredstvo.dataktiviranja>"+util.getTimestampValue(dm.getOS_Metaobrada().getTimestamp("DATUMDO"), util.NUM_LAST)+")";
    System.out.println(sSql);
    vl.execSQL(sSql);
    vl.RezSet.setColumns(new Column[] {
      (Column) dm.getOS_Sredstvo().getColumn("CORG2").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("INVBROJ").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("NAZSREDSTVA").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("CLOKACIJE").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("CARTIKLA").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("CPAR").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("BROJKONTA").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("CGRUPE").clone(),
      (Column) dm.getOS_Amgrupe().getColumn("ZAKSTOPA").clone(),
      (Column) dm.getOS_Amgrupe().getColumn("ODLSTOPA").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("CSKUPINE").clone(),
      (Column) dm.getOS_Revskupine().getColumn("KOEFICIJENT").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("DATAKTIVIRANJA").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("OSNPOCETAK").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("ISPPOCETAK").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("REVOSN").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("REVISP").clone(),
      });
    vl.RezSet.open();
    vl.RezSet.first();
    dm.getOS_Obrada1().open();
    System.out.println("Broj recorda za staro osnovno sredstvo: "+vl.RezSet.rowCount());
    if (vl.RezSet.rowCount()>0) {
      lIma=true;
      do {
        x++;
        dm.getOS_Obrada1().insertRow(false);
        dm.getOS_Obrada1().setString("CORG",           vl.RezSet.getString("CORG2"));
        dm.getOS_Obrada1().setString("InvBroj",        vl.RezSet.getString("InvBroj"));
        dm.getOS_Obrada1().setString("NAZSREDSTVA",    vl.RezSet.getString("NazSredstva"));
        dm.getOS_Obrada1().setString("cLokacije",      vl.RezSet.getString("cLokacije"));
        dm.getOS_Obrada1().setString("cArtikla",       vl.RezSet.getString("cArtikla"));
        dm.getOS_Obrada1().setInt("CPAR",              vl.RezSet.getInt("CPAR"));
        dm.getOS_Obrada1().setString("BROJKONTA",      vl.RezSet.getString("BrojKonta"));
        dm.getOS_Obrada1().setString("cGrupe",         vl.RezSet.getString("cGrupe"));
        dm.getOS_Obrada1().setBigDecimal("zakStopa",   vl.RezSet.getBigDecimal("zakStopa"));
        dm.getOS_Obrada1().setBigDecimal("odlStopa",   vl.RezSet.getBigDecimal("odlStopa"));
        dm.getOS_Obrada1().setString("cSkupine",       vl.RezSet.getString("cSkupine"));
        dm.getOS_Obrada1().setBigDecimal("Koeficijent",vl.RezSet.getBigDecimal("Koeficijent"));
        dm.getOS_Obrada1().setString("Mjesec",         "13");
        dm.getOS_Obrada1().setTimestamp("DatPromjene", vl.RezSet.getTimestamp("DATAKTIVIRANJA"));
        dm.getOS_Obrada1().setBigDecimal("Osnovica",   vl.RezSet.getBigDecimal("OsnPocetak"));
        dm.getOS_Obrada1().setBigDecimal("Ispravak",   vl.RezSet.getBigDecimal("IspPocetak"));
        dm.getOS_Obrada1().setBigDecimal("SadVrijed",  util.negateValue(vl.RezSet.getBigDecimal("OsnPocetak"), vl.RezSet.getBigDecimal("IspPocetak")));
        dm.getOS_Obrada1().setBigDecimal("RevOsn",     vl.RezSet.getBigDecimal("RevOsn"));
        dm.getOS_Obrada1().setBigDecimal("RevIsp",     vl.RezSet.getBigDecimal("RevIsp"));
        dm.getOS_Obrada1().setBigDecimal("RevSad",     util.nul);
        if (vl.RezSet.getBigDecimal("OsnPocetak").equals(util.nul)) {
          dm.getOS_Obrada1().setBigDecimal("Amortizacija", util.nul);
          dm.getOS_Obrada1().setBigDecimal("Amor1", util.nul);
          dm.getOS_Obrada1().setBigDecimal("Amor2", util.nul);
          dm.getOS_Obrada1().setBigDecimal("PAmortizacija", util.nul);
          dm.getOS_Obrada1().setBigDecimal("PAmor1", util.nul);
          dm.getOS_Obrada1().setBigDecimal("PAmor2", util.nul);
        }
        else {
          rofk.setUPValue(dm.getOS_Obrada1());
          dm.getOS_Obrada1().setBigDecimal("Amortizacija",  rofk.calcObrada(mon1, mon2));
          dm.getOS_Obrada1().setBigDecimal("Amor1",         rofk.calcObrada(1, mon1));
          dm.getOS_Obrada1().setBigDecimal("Amor2",         rofk.calcObrada(0, mon2));
          dm.getOS_Obrada1().setBigDecimal("PAmortizacija", rofk.calcPObrada(mon1, mon2));
          dm.getOS_Obrada1().setBigDecimal("PAmor1",        rofk.calcPObrada(1, mon1));
          dm.getOS_Obrada1().setBigDecimal("PAmor2",        rofk.calcPObrada(0, mon2));
        }
        dm.getOS_Obrada1().setString("COBRADA1", String.valueOf(x));
        dm.getOS_Obrada1().setBigDecimal("RevAmor", util.nul);
        dm.getOS_Obrada1().setString("Vk", "999");
        dm.getOS_Obrada1().post();
        vl.RezSet.next();
      } while (vl.RezSet.inBounds());
      dm.getOS_Obrada1().saveChanges();
    }
/*************************************************************************************************************
 * OBRADA PROMJENE SREDSTVA - radi sa ako nije u pitanju predracun
 *************************************************************************************************************/
    if (!dm.getOS_Metaobrada().getString("TIPAMOR").equals("P")) {
      sSql="select OS_SREDSTVO.CORG2, OS_SREDSTVO.INVBROJ, OS_SREDSTVO.NAZSREDSTVA, OS_SREDSTVO.CLOKACIJE, "+
            "OS_SREDSTVO.CARTIKLA, OS_SREDSTVO.CPAR, OS_SREDSTVO.BROJKONTA, OS_SREDSTVO.CGRUPE, "+
            "OS_AMGRUPE.ZAKSTOPA, OS_AMGRUPE.ODLSTOPA, OS_SREDSTVO.CSKUPINE,OS_REVSKUPINE.KOEFICIJENT, "+
            "OS_PROMJENE.DATPROMJENE, OS_PROMJENE.OSNDUGUJE, OS_PROMJENE.OSNPOTRAZUJE, OS_PROMJENE.ISPDUGUJE, "+
            "OS_PROMJENE.ISPPOTRAZUJE, OS_SREDSTVO.REVOSN, OS_SREDSTVO.REVISP "+
            "from OS_SREDSTVO, OS_PROMJENE, OS_AMGRUPE, OS_REVSKUPINE "+
            "where OS_SREDSTVO.INVBROJ=OS_PROMJENE.INVBROJ AND OS_SREDSTVO.CORG2=OS_PROMJENE.CORG2"+
            " AND OS_SREDSTVO.CGRUPE=OS_AMGRUPE.CGRUPE AND OS_SREDSTVO.CSKUPINE=OS_REVSKUPINE.CSKUPINE"+
            " AND OS_PROMJENE.DATPROMJENE>="+util.getTimestampValue(util.findFirstDayOfYear(Integer.parseInt(getOS_Kontrola().getString("GODINA"))), util.NUM_FIRST)+
//            " AND DATPROMJENE>="+util.getTimestampValue(util.findFirstDayOfYear(), util.NUM_FIRST)+
            " AND OS_PROMJENE.DATPROMJENE<="+util.getTimestampValue(util.findLastDayOfMonth(mon2, Integer.parseInt(vl.findYear(dm.getOS_Metaobrada().getTimestamp("DATUMDO")))), util.NUM_LAST)+
            " AND "+getPripOrg()+" AND (OS_SREDSTVO.DATLIKVIDACIJE IS NULL or OS_SREDSTVO.DATLIKVIDACIJE>"+util.getTimestampValue(dm.getOS_Metaobrada().getTimestamp("DATUMDO"), util.NUM_LAST)+") "+
            "and (os_sredstvo.status='A')";
//            "and (os_sredstvo.status='A' or os_sredstvo.dataktiviranja>"+util.getTimestampValue(dm.getOS_Metaobrada().getTimestamp("DATUMDO"), util.NUM_LAST)+")";

      System.out.println(sSql);
      vl.execSQL(sSql);
      vl.RezSet.setColumns(new Column[] {
        (Column) dm.getOS_Sredstvo().getColumn("CORG2").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("INVBROJ").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("NAZSREDSTVA").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("CLOKACIJE").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("CARTIKLA").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("CPAR").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("BROJKONTA").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("CGRUPE").clone(),
        (Column) dm.getOS_Amgrupe().getColumn("ZAKSTOPA").clone(),
        (Column) dm.getOS_Amgrupe().getColumn("ODLSTOPA").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("CSKUPINE").clone(),
        (Column) dm.getOS_Revskupine().getColumn("KOEFICIJENT").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("DATPROMJENE").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("OSNPOCETAK").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("ISPPOCETAK").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("REVOSN").clone(),
        (Column) dm.getOS_Sredstvo().getColumn("REVISP").clone(),
      });
      vl.RezSet.open();
      System.out.println("Broj recorda za promjene: "+vl.RezSet.rowCount());

      if (vl.RezSet.rowCount()>0 ) {
        lIma=true;
        vl.RezSet.first();
        dm.getOS_Obrada1().open();
        do {
          System.out.println("Obrada promjene:; "+vl.RezSet.getString("InvBroj"));
          cal.setTime(vl.RezSet.getTimestamp("DATPROMJENE"));
          mProm=cal.get(Calendar.MONTH);
          x++;
          dm.getOS_Obrada1().insertRow(false);
          dm.getOS_Obrada1().setString("CORG",           vl.RezSet.getString("CORG2"));
          dm.getOS_Obrada1().setString("InvBroj",        vl.RezSet.getString("InvBroj"));
          dm.getOS_Obrada1().setString("NazSredstva",    vl.RezSet.getString("NazSredstva"));
          dm.getOS_Obrada1().setString("cLokacije",      vl.RezSet.getString("cLokacije"));
          dm.getOS_Obrada1().setString("cArtikla",       vl.RezSet.getString("cArtikla"));
          dm.getOS_Obrada1().setInt("CPAR",              vl.RezSet.getInt("CPAR"));
          dm.getOS_Obrada1().setString("BROJKONTA",      vl.RezSet.getString("BROJKONTA"));
          dm.getOS_Obrada1().setString("cGrupe",         vl.RezSet.getString("cGrupe"));
          dm.getOS_Obrada1().setBigDecimal("zakStopa",   vl.RezSet.getBigDecimal("zakStopa"));
          dm.getOS_Obrada1().setBigDecimal("odlStopa",   vl.RezSet.getBigDecimal("odlStopa"));
          dm.getOS_Obrada1().setString("cSkupine",       vl.RezSet.getString("cSkupine"));
          dm.getOS_Obrada1().setBigDecimal("Koeficijent", vl.RezSet.getBigDecimal("Koeficijent"));
          dm.getOS_Obrada1().setString("Mjesec",         String.valueOf(mProm+1));
          dm.getOS_Obrada1().setTimestamp("DatPromjene", vl.RezSet.getTimestamp("DatPromjene"));
          dm.getOS_Obrada1().setBigDecimal("Osnovica",   util.negateValue(vl.RezSet.getBigDecimal("OSNDUGUJE"), vl.RezSet.getBigDecimal("OSNPOTRAZUJE")));
          dm.getOS_Obrada1().setBigDecimal("Ispravak",   util.negateValue(vl.RezSet.getBigDecimal("ISPPOTRAZUJE"), vl.RezSet.getBigDecimal("ISPDUGUJE")));
          dm.getOS_Obrada1().setBigDecimal("SadVrijed",  util.negateValue(dm.getOS_Obrada1().getBigDecimal("OSNOVICA"), dm.getOS_Obrada1().getBigDecimal("ISPRAVAK")));
          dm.getOS_Obrada1().setBigDecimal("RevOsn",     vl.RezSet.getBigDecimal("RevOsn"));
          dm.getOS_Obrada1().setBigDecimal("RevIsp",     vl.RezSet.getBigDecimal("RevIsp"));
          dm.getOS_Obrada1().setBigDecimal("RevSad",     util.nul);
  /*************************************************************************************************************
   * OBRACUN AMORTIZACIJE
   *************************************************************************************************************/
          if ((dm.getOS_Obrada1().getBigDecimal("ZAKSTOPA").equals(util.nul)) || (dm.getOS_Obrada1().getBigDecimal("OSNOVICA")).equals(util.nul)) {
            dm.getOS_Obrada1().setBigDecimal("Amortizacija", util.nul);
          }
          else {
            rofk.setUPValue(dm.getOS_Obrada1());
            if (mon1 > mProm) {
              dm.getOS_Obrada1().setBigDecimal("Amortizacija",  rofk.Obrada(mon1, mon2+1, mProm));
              dm.getOS_Obrada1().setBigDecimal("Amor1",         rofk.Obrada(0, mon1, mProm));
              dm.getOS_Obrada1().setBigDecimal("Amor2",         rofk.Obrada(0, mon2+1, mProm));
            }
            else {
              dm.getOS_Obrada1().setBigDecimal("Amortizacija",  rofk.Obrada(mon2, mProm));
              dm.getOS_Obrada1().setBigDecimal("Amor1",         rofk.Obrada(mon1, 0));
              dm.getOS_Obrada1().setBigDecimal("Amor2",         rofk.Obrada((mon2+mon1)-mProm, 0));
            }
          }
  /*************************************************************************************************************
   * OBRACUN POVISENE AMORTIZACIJE
   *************************************************************************************************************/
          if ((dm.getOS_Obrada1().getBigDecimal("ODLSTOPA").equals(util.nul)) || (dm.getOS_Obrada1().getBigDecimal("OSNOVICA")).equals(util.nul)) {
            dm.getOS_Obrada1().setBigDecimal("PAmortizacija", util.nul);
          }
          else {
            rofk.setUPValue(dm.getOS_Obrada1());
            if (mon1 > mProm) {
              dm.getOS_Obrada1().setBigDecimal("PAmortizacija", rofk.PObrada(mon1, mon2, mProm));
              dm.getOS_Obrada1().setBigDecimal("PAmor1",        rofk.PObrada(0, mon1, mProm));
              dm.getOS_Obrada1().setBigDecimal("PAmor2",        rofk.PObrada(0, mon2+1, mProm));
            }
            else {
              dm.getOS_Obrada1().setBigDecimal("PAmortizacija", rofk.PObrada(mon2, mProm));
              dm.getOS_Obrada1().setBigDecimal("PAmor1",        rofk.PObrada(mon1, 0));
              dm.getOS_Obrada1().setBigDecimal("PAmor2",        rofk.PObrada((mon2+mon1)-mProm, 0));
            }
          }
          dm.getOS_Obrada1().setString("COBRADA1", String.valueOf(x));
          dm.getOS_Obrada1().setBigDecimal("RevAmor", util.nul);
          dm.getOS_Obrada1().setString("Vk", "000");
          dm.getOS_Obrada1().post();
          vl.RezSet.next();
        } while (vl.RezSet.inBounds());
        dm.getOS_Obrada1().saveChanges();
      }
    }
/*    sSql="select OS_SREDSTVO.CORG2, OS_SREDSTVO.INVBROJ, OS_SREDSTVO.NAZSREDSTVA, OS_SREDSTVO.CLOKACIJE, "+
            "OS_SREDSTVO.CARTIKLA, OS_SREDSTVO.CPAR, OS_SREDSTVO.BROJKONTA, OS_SREDSTVO.CGRUPE, "+
            "OS_AMGRUPE.ZAKSTOPA, OS_AMGRUPE.ODLSTOPA, OS_SREDSTVO.CSKUPINE,OS_REVSKUPINE.KOEFICIJENT, "+
            "OS_SREDSTVO.DATAKTIVIRANJA, OS_SREDSTVO.DATPROMJENE, OS_SREDSTVO.DATLIKVIDACIJE, OS_SREDSTVO.OSNPOCETAK, OS_SREDSTVO.ISPPOCETAK, OS_SREDSTVO.OSNDUGUJE, "+
            "OS_SREDSTVO.ISPPOTRAZUJE, OS_SREDSTVO.REVOSN, OS_SREDSTVO.REVISP, OS_SREDSTVO.AMORTIZACIJA, OS_SREDSTVO.PAMORTIZACIJA "+
            "from OS_SREDSTVO, OS_AMGRUPE, OS_REVSKUPINE "+
            "where OS_SREDSTVO.CGRUPE=OS_AMGRUPE.CGRUPE AND OS_SREDSTVO.CSKUPINE=OS_REVSKUPINE.CSKUPINE "+
            " AND DATLIKVIDACIJE<"+util.getTimestampValue(dm.getOS_Metaobrada().getTimestamp("DATUMDO"), util.NUM_LAST)+
            " AND DATLIKVIDACIJE>"+util.getTimestampValue(dm.getOS_Metaobrada().getTimestamp("DATUMOD"), util.NUM_FIRST)+
            " AND "+getPripOrg();
    System.out.println(sSql);
    vl.execSQL(sSql);
    vl.RezSet.setColumns(new Column[] {
      (Column) dm.getOS_Sredstvo().getColumn("CORG2").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("INVBROJ").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("NAZSREDSTVA").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("CLOKACIJE").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("CARTIKLA").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("CPAR").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("BROJKONTA").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("CGRUPE").clone(),
      (Column) dm.getOS_Amgrupe().getColumn("ZAKSTOPA").clone(),
      (Column) dm.getOS_Amgrupe().getColumn("ODLSTOPA").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("CSKUPINE").clone(),
      (Column) dm.getOS_Revskupine().getColumn("KOEFICIJENT").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("DATAKTIVIRANJA").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("DATPROMJENE").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("OSNPOCETAK").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("ISPPOCETAK").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("REVOSN").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("REVISP").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("AMORTIZACIJA").clone(),
      (Column) dm.getOS_Sredstvo().getColumn("PAMORTIZACIJA").clone(),
    });
    vl.RezSet.open();
    if (vl.RezSet.rowCount()>0) {
      vl.RezSet.first();
      System.out.println("Broj recorda za likvidirano osnovno sredstvo: "+vl.RezSet.rowCount());
      dm.getOS_Obrada4().open();
      if (vl.RezSet.isEmpty()) {
        System.out.println("Nema podataka za likvidirana OS");
        return;
      }
      do {
//        if ((vl.RezSet.getBigDecimal("OsnPocetak").add(vl.RezSet.getBigDecimal("OsnDuguje"))).subtract(vl.RezSet.getBigDecimal("IspPocetak").add(vl.RezSet.getBigDecimal("IspPotrazuje"))).doubleValue()>0) {
          cal.setTime(vl.RezSet.getTimestamp("DatLikvidacije"));
          mjesecL=cal.get(Calendar.MONTH);
          godinaL=cal.get(Calendar.YEAR);
          cal.setTime(vl.RezSet.getTimestamp("DATAKTIVIRANJA"));
          mjesecP=cal.get(Calendar.MONTH);
          godinaP=cal.get(Calendar.YEAR);
          dm.getOS_Obrada4().insertRow(true);
          dm.getOS_Obrada4().setString("CORG",            vl.RezSet.getString("cOrg2"));
          dm.getOS_Obrada4().setString("InvBroj",         vl.RezSet.getString("InvBroj"));
          dm.getOS_Obrada4().setString("NazSredstva",     vl.RezSet.getString("NazSredstva"));
          dm.getOS_Obrada4().setString("cLokacije",       vl.RezSet.getString("cLokacije"));
          dm.getOS_Obrada4().setString("cArtikla",        vl.RezSet.getString("cArtikla"));
          dm.getOS_Obrada4().setInt("CPAR",               vl.RezSet.getInt("CPAR"));
          dm.getOS_Obrada4().setString("BROJKONTA",       vl.RezSet.getString("BrojKonta"));
          dm.getOS_Obrada4().setString("cGrupe",          vl.RezSet.getString("cGrupe"));
          dm.getOS_Obrada4().setBigDecimal("zakStopa",    vl.RezSet.getBigDecimal("zakStopa"));
          dm.getOS_Obrada4().setBigDecimal("odlStopa",    vl.RezSet.getBigDecimal("odlStopa"));
          dm.getOS_Obrada4().setString("cSkupine",        vl.RezSet.getString("cSkupine"));
          dm.getOS_Obrada4().setBigDecimal("Koeficijent", vl.RezSet.getBigDecimal("Koeficijent"));
          dm.getOS_Obrada4().setTimestamp("DatPromjene",  vl.RezSet.getTimestamp("DATPROMJENE"));
          dm.getOS_Obrada4().setTimestamp("DatLikvidacije",  vl.RezSet.getTimestamp("DatLikvidacije"));
          dm.getOS_Obrada4().setBigDecimal("Osnovica",    util.sumValue(vl.RezSet.getBigDecimal("OsnPocetak"), vl.RezSet.getBigDecimal("OsnDuguje")));
          dm.getOS_Obrada4().setBigDecimal("Ispravak",    util.negateValue(util.sumValue(vl.RezSet.getBigDecimal("IspPocetak"), vl.RezSet.getBigDecimal("IspPotrazuje")), vl.RezSet.getBigDecimal("AMORTIZACIJA")));
          dm.getOS_Obrada4().setBigDecimal("SadVrijed",   util.negateValue(dm.getOS_Obrada4().getBigDecimal("Osnovica"), dm.getOS_Obrada4().getBigDecimal("Ispravak")));

          if (godinaL == godinaP) {
            dm.getOS_Obrada4().setString("Mjesec", String.valueOf(mjesecL-mjesecP));
          }
          else {
            dm.getOS_Obrada4().setString("Mjesec", String.valueOf(mjesecL+1));
          }
          rofk.setUPValue(dm.getOS_Obrada4());
          dm.getOS_Obrada4().setBigDecimal("Amortizacija", vl.RezSet.getBigDecimal("AMORTIZACIJA"));
//          dm.getOS_Obrada4().setBigDecimal("Amortizacija", rofk.likObrada(Integer.parseInt(dm.getOS_Obrada4().getString("Mjesec"))));
          dm.getOS_Obrada4().setBigDecimal("UIspravak", dm.getOS_Obrada4().getBigDecimal("Ispravak"));
          dm.getOS_Obrada4().setBigDecimal("RevOsn", util.nul);
          dm.getOS_Obrada4().setBigDecimal("RevIsp", util.nul);
          dm.getOS_Obrada4().setBigDecimal("RevSad", util.nul);
//        }
        if (dm.getOS_Obrada4().getBigDecimal("Amortizacija").doubleValue() > dm.getOS_Obrada4().getBigDecimal("SadVrijed").doubleValue()) {
          dm.getOS_Obrada4().setBigDecimal("Amortizacija", dm.getOS_Obrada4().getBigDecimal("SadVrijed"));
          dm.getOS_Obrada4().setBigDecimal("UIspravak", dm.getOS_Obrada4().getBigDecimal("Ispravak").add(dm.getOS_Obrada4().getBigDecimal("SadVrijed")));
        }
          dm.getOS_Obrada4().post();
        vl.RezSet.next();
      } while (vl.RezSet.inBounds());
      dm.getOS_Obrada4().saveChanges();
    }
    */
    dm.getOS_Obrada1().open();
    if (dm.getOS_Obrada1().rowCount()==0) {
      dm.getOS_Metaobrada().deleteRow();
      dm.getOS_Metaobrada().saveChanges();
      return;
    }
/*************************************************************************************************************
 * IZRACUNAVANJE PREBACAM
 *************************************************************************************************************/
    dm.getOS_Obrada1().first();
    do {
      if (dm.getOS_Obrada1().getBigDecimal("Amortizacija").doubleValue()==0 && dm.getOS_Obrada1().getBigDecimal("PAmortizacija").doubleValue()==0) {
        dm.getOS_Obrada1().setBigDecimal("PrebacAM",      util.nul);
      }
      else {
/**
 * Ako je poèetna amortizacija veca ili jednaka sadasnjoj vrijednosti
 * - amortizacija = 0
 * - pamortizacija = 0
 */
        if (util.sumValue(dm.getOS_Obrada1().getBigDecimal("AMOR1"), dm.getOS_Obrada1().getBigDecimal("PAMOR1")).doubleValue()>=dm.getOS_Obrada1().getBigDecimal("SADVRIJED").doubleValue() && dm.getOS_Obrada1().getBigDecimal("SADVRIJED").doubleValue()>=0){
          dm.getOS_Obrada1().setBigDecimal("Amortizacija",  util.nul);
          dm.getOS_Obrada1().setBigDecimal("PAmortizacija", util.nul);
          dm.getOS_Obrada1().setBigDecimal("PrebacAM",      util.nul);
        }
/**
 * Ako su poèetna amortizacija manja, a završna manja ili jednaka sadasnjoj vrijednosti
 * - amortizacija = Amor2-Amor1
 * - pamortizacija = pAmor2-pAmor1
 */
        else if (util.sumValue(dm.getOS_Obrada1().getBigDecimal("AMOR2"), dm.getOS_Obrada1().getBigDecimal("PAMOR2")).doubleValue()<=dm.getOS_Obrada1().getBigDecimal("SADVRIJED").doubleValue()) {
          dm.getOS_Obrada1().setBigDecimal("Amortizacija",  util.negateValue(dm.getOS_Obrada1().getBigDecimal("Amor2"), dm.getOS_Obrada1().getBigDecimal("Amor1")));
          dm.getOS_Obrada1().setBigDecimal("PAmortizacija", util.negateValue(dm.getOS_Obrada1().getBigDecimal("PAmor2"), dm.getOS_Obrada1().getBigDecimal("PAmor1")));
          dm.getOS_Obrada1().setBigDecimal("PrebacAM",      util.nul);
        }
/**
 * Ako su poèetna amortizacija manja, zavrsna veca, a amortizacija veca od sadasnje vrijednosti
 * - amortizacija = SadVrijed-Amor1
 * - pamortizacija = 0
 */
        else if (util.negateValue(dm.getOS_Obrada1().getBigDecimal("Amor2"), dm.getOS_Obrada1().getBigDecimal("Amor1")).doubleValue() >= dm.getOS_Obrada1().getBigDecimal("SadVrijed").doubleValue() && dm.getOS_Obrada1().getBigDecimal("SADVRIJED").doubleValue()>=0){
          dm.getOS_Obrada1().setBigDecimal("Amortizacija",  util.negateValue(dm.getOS_Obrada1().getBigDecimal("SadVrijed"), dm.getOS_Obrada1().getBigDecimal("Amor1")));
          dm.getOS_Obrada1().setBigDecimal("PAmortizacija", util.nul);
          dm.getOS_Obrada1().setBigDecimal("PrebacAm",      util.negateValue(util.sumValue(dm.getOS_Obrada1().getBigDecimal("Amor2"), dm.getOS_Obrada1().getBigDecimal("PAmor2")), dm.getOS_Obrada1().getBigDecimal("Amortizacija")));
        }
/**
 * Ako su poèetna amortizacija manja, zavrsna veca, a amortizacija manja od sadasnje vrijednosti
 * - amortizacija = SadVrijed-Amor1
 * - pamortizacija = 0
 */
        else if (util.negateValue(dm.getOS_Obrada1().getBigDecimal("Amor2"), dm.getOS_Obrada1().getBigDecimal("Amor1")).doubleValue() < dm.getOS_Obrada1().getBigDecimal("SadVrijed").doubleValue()){
          dm.getOS_Obrada1().setBigDecimal("Amortizacija",  util.negateValue(dm.getOS_Obrada1().getBigDecimal("Amor2"), dm.getOS_Obrada1().getBigDecimal("Amor1")));
          dm.getOS_Obrada1().setBigDecimal("PAmortizacija", util.negateValue(dm.getOS_Obrada1().getBigDecimal("SadVrijed"), util.sumValue(dm.getOS_Obrada1().getBigDecimal("Amor2"), dm.getOS_Obrada1().getBigDecimal("PAmor1"))));
          dm.getOS_Obrada1().setBigDecimal("PrebacAm",      util.negateValue(util.sumValue(dm.getOS_Obrada1().getBigDecimal("Amor2"), dm.getOS_Obrada1().getBigDecimal("PAmor2")), dm.getOS_Obrada1().getBigDecimal("SadVrijed")));
        }
        else if (dm.getOS_Obrada1().getBigDecimal("SADVRIJED").doubleValue()>=0) {
          System.out.println("Ovo je problematicno.");
          dm.getOS_Obrada1().setBigDecimal("PrebacAm",      util.nul);
          dm.getOS_Obrada1().setBigDecimal("PAmortizacija", util.nul);
          dm.getOS_Obrada1().setBigDecimal("Amortizacija", util.nul);
        }
        else {
            dm.getOS_Obrada1().setBigDecimal("PrebacAm",      util.nul);
        }
      }
      dm.getOS_Obrada1().next();
    } while (dm.getOS_Obrada1().inBounds());
    dm.getOS_Obrada1().saveChanges();
 /*************************************************************************************************************
 * TU NAM NEDOSTAJE SKUPNA AMORTIZACIJA, REVALORIZACIJA i AMORTIZACIJA REVALORIZACIJE
 *************************************************************************************************************/

 /*************************************************************************************************************
 * KUMULATIVI ZA OSNOVNO SREDSTVO (IZ OBRADE1 u OBRADU2)
 *************************************************************************************************************/
    sSql=
      "insert into OS_OBRADA2 (CORG, INVBROJ, NAZSREDSTVA, CLOKACIJE, CARTIKLA, CPAR, BROJKONTA, CGRUPE,"+
      " ZAKSTOPA, ODLSTOPA, OTPSTOPA, CSKUPINE, KOEFICIJENT, MJESEC, DATPROMJENE, DATLIKVIDACIJE,"+
      " OSNOVICA, ISPRAVAK, SADVRIJED, REVOSN, REVISP, REVSAD, AMORTIZACIJA, PAMORTIZACIJA, REVAMOR,"+
      " PREBACAM, VK) "+
      "select CORG, INVBROJ, MAX(NAZSREDSTVA), MAX(CLOKACIJE), MAX(CARTIKLA), MAX(CPAR), MAX(BROJKONTA),"+
      " MAX(CGRUPE), MAX(ZAKSTOPA), MAX(ODLSTOPA), MAX(OTPSTOPA), MAX(CSKUPINE), MAX(KOEFICIJENT),"+
      " MAX(MJESEC), MAX(DATPROMJENE), MAX(DATLIKVIDACIJE), SUM(OSNOVICA), SUM(ISPRAVAK),"+
      " SUM(OSNOVICA)-(SUM(ISPRAVAK)+SUM(AMORTIZACIJA)+SUM(PAMORTIZACIJA)), SUM(REVOSN), SUM(REVISP),"+
      " SUM(REVSAD), SUM(AMORTIZACIJA), SUM(PAMORTIZACIJA), SUM(REVAMOR), SUM(PREBACAM), MAX(VK) "+
      "from OS_OBRADA1 "+
      "group by INVBROJ, CORG";
    System.out.println(sSql);
    vl.runSQL(sSql);
/*************************************************************************************************************
 * AZURIRANJE PODATAKA
 *************************************************************************************************************/
    System.out.println("Vrati: "+dm.getOS_Metaobrada().getString("TipAmor")+", "+dm.getOS_Metaobrada().getString("VrstaAmor"));
    if (dm.getOS_Metaobrada().getString("TipAmor").equals("K")) {
// Siniša: Prvo ja oznacimo u OSKONTROLI
      dm.getOS_Kontrola().open();
      getOS_Kontrola().setString("Amor", "D"); //nek pozicionira na prvom za svaki slucaj
      dm.getOS_Kontrola().setString("Reva", "D");
      dm.getOS_Kontrola().post();
      dm.getOS_Kontrola().saveChanges();
    }
// Siniša: Ako je obracun po pojednom OS sredstvu
    if (dm.getOS_Metaobrada().getString("VrstaAmor").equals("P") && dm.getOS_Metaobrada().getString("TipAmor").equals("K")) {
      // Siniša: Prepišemo amortizaciju iz OBRADE2 u OS_SREDSTVO
      System.out.println("Apdejt");
      String _s = "update OS_SREDSTVO set "+
      "Amortizacija = Amortizacija+(select Amortizacija from OS_OBRADA2 where InvBroj=OS_SREDSTVO.InvBroj and CORG=OS_SREDSTVO.cOrg2), "+
      "PAmortizacija = PAmortizacija+(select PAmortizacija from OS_OBRADA2 where InvBroj=OS_SREDSTVO.InvBroj and CORG=OS_SREDSTVO.cOrg2), "+
      "Ispravak = Ispravak + Amortizacija + PAmortizacija, "+
      "Saldo = Osnovica - Ispravak "+
      "where datlikvidacije is null AND "+getPripOrg();
      System.out.println(_s);
      vl.runSQL(_s);
      // Siniša: Dodamo zapis u OS_PROMJENE
      dm.getOS_Obrada2().refresh();
      dm.getOS_Obrada2().open();
      dm.getOS_Promjene().open();
      dm.getOS_Obrada2().first();
      sysoutTEST st = new sysoutTEST(false);
      st.prn(dm.getOS_Obrada2());
      do {
        if (!findAmor()) {
          System.out.println("insert promjene");
          dm.getOS_Promjene().insertRow(false);
        }
        // Rade: racunanje rbr-a i dohvat sifre obrade amortizacije
        dm.getOS_Promjene().setInt("RBR", osUtil.getUtil().getOS_PromjeneMaxRBR(dm.getOS_Obrada2().getString("CORG"), dm.getOS_Obrada2().getString("INVBROJ"), 'A'));
        dm.getOS_Promjene().setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
        dm.getOS_Promjene().setString("CORG2", dm.getOS_Obrada2().getString("CORG"));
        dm.getOS_Promjene().setString("STATUS", "A");
        dm.getOS_Promjene().setString("INVBROJ", dm.getOS_Obrada2().getString("INVBROJ"));
        dm.getOS_Promjene().setString("CPROMJENE", osUtil.getUtil().getSifraObrAmor());
        dm.getOS_Promjene().setTimestamp("DATPROMJENE", dm.getOS_Metaobrada().getTimestamp("DATUMDO"));
        dm.getOS_Promjene().setBigDecimal("ISPPOTRAZUJE", dm.getOS_Obrada2().getBigDecimal("AMORTIZACIJA").add(dm.getOS_Obrada2().getBigDecimal("PAMORTIZACIJA")));
        dm.getOS_Promjene().setBigDecimal("ISPDUGUJE", util.nul);
        dm.getOS_Promjene().setBigDecimal("OSNPOTRAZUJE", util.nul);
        dm.getOS_Promjene().setBigDecimal("OSNDUGUJE", util.nul);
        dm.getOS_Promjene().setBigDecimal("SALDO", dm.getOS_Promjene().getBigDecimal("ISPPOTRAZUJE").negate());
        dm.getOS_Promjene().setString("STATUSKNJ", "D");
        System.out.println("Saldo: "+dm.getOS_Promjene().getBigDecimal("SALDO"));
        dm.getOS_Promjene().post();
        dm.getOS_Obrada2().next();
      } while (dm.getOS_Obrada2().inBounds());
      dm.getOS_Promjene().saveChanges();
/*      vl.runSQL("insert into OS_PROMJENE (rbr, cOrg, InvBroj, cPromjene, DatPromjene, IspPotrazuje, "+
          "IspDuguje, OsnPotrazuje, OsnDuguje) select "+rbr+" , OS_OBRADA2.corg, OS_OBRADA2.InvBroj,"+cProm+", OS_METAOBRADA.DatumDo, "+
          "OS_OBRADA2.Amortizacija+OS_OBRADA2.PAmortizacija,0,0,0 from OS_OBRADA2, OS_METAOBRADA");
*/
    }
    System.out.println("Datum: "+dm.getOS_Metaobrada().getTimestamp("DATUMDO").toString());
    if (dm.getOS_Metaobrada().getString("TIPAMOR").equals("M")) {
      vl.runSQL("update OS_SREDSTVO set Amortizacija = Amortizacija+(select Amortizacija from OS_OBRADA2 where "+
          "InvBroj=OS_SREDSTVO.InvBroj and CORG=OS_SREDSTVO.cOrg2), PAmortizacija = PAmortizacija+(select PAmortizacija from OS_OBRADA2 "+
          "where InvBroj=OS_SREDSTVO.InvBroj and CORG=OS_SREDSTVO.cOrg2) where datlikvidacije is null AND "+getPripOrg());
      getOS_Kontrola().setString("MJESEC", dm.getOS_Metaobrada().getTimestamp("DATUMDO").toString().substring(5,7));
      dm.getOS_Kontrola().saveChanges();
      updateMonthData();
    }
    rcc.EnabDisabAll(jp, false);
    rcc.setLabelLaF(this.jbIspis, true);
    rcc.setLabelLaF(this.getOKPanel().jBOK, false);
//    this.componentShow();
  }

  /**
   * Pozicionira i vraca red os_kontrole za tekuce knjigovodstvo
   * @return
   */
  private QueryDataSet getOS_Kontrola() {
    lookupData.getlookupData().raLocate(dm.getOS_Kontrola(),"CORG",OrgStr.getKNJCORG());
    return dm.getOS_Kontrola();
  }
  void jbIspis_actionPerformed(ActionEvent e) {
    hr.restart.os.osMain osm = new hr.restart.os.osMain();
    if(!this.jrfCORG.getText().trim().equals(""))
      hr.restart.os.ispAmor.paramCOrg = jrfCORG.getText().trim();
    osm.showFrame("hr.restart.os.ispAmor", "Ispis amortizacije");
  }
  String getPripOrg() {
    if (jrfCORG.getText().equals(dlgGetKnjig.getKNJCORG())) {
      QueryDataSet knjigs = Orgstruktura.getDataModule().getTempSet(Condition.equal("NALOG", "1"));
      knjigs.open();
      if (knjigs.getRowCount() == 1) return "0=0";
    }
    com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(jrfCORG.getText());
    return Condition.in("CORG2", tds, "CORG").qualified("OS_SREDSTVO").toString();
//    int i=0;
//    String cVrati="OS_SREDSTVO.CORG2 in (";
//    com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(jrfCORG.getText());
//    tds.first();
//    do {
//      if (i>0) {
//        cVrati=cVrati+',';
//      }
//      i++;
//      cVrati=cVrati+"'"+tds.getString("CORG")+"'";
//      tds.next();
//    } while (tds.inBounds());
//    cVrati=cVrati+")";
//    return cVrati;
  }

  private boolean provjeraObrade() {
    dm.getOS_Metaobrada().open();
    if(dm.getOS_Metaobrada().getString("CORG").equals(""))
      return true;
    if (rdOSUtil.getUtil().isPromjene(dm.getOS_Metaobrada().getString("CORG"))) {
      String reva = getOS_Kontrola().getString("REVA");
      String amor = dm.getOS_Kontrola().getString("AMOR");

      if(amor.equals("D") && reva.equals("D")) {
        JOptionPane.showConfirmDialog(this.jp,"Veæ je obavljen konaèni obraèun amortizacije i revalorizacije !","Greška",
                                        JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        return false;
      }
      else {
        /// provjera  zadnje obrade
      }
    }
    else {
      JOptionPane.showConfirmDialog(this.jp,"Ne postoji kontrolni slog za knjigovodstvo !","Greška",
                                        JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  public void show() {
    dm.getOS_Kontrola().refresh();
    if(provjeraObrade()) {
      super.show();
    }
  }
  public void hide() {
    super.hide();
  }
  private boolean findAmor() {
    return false;
  }

  void jcbREAL_actionPerformed(ActionEvent e) {
    System.out.println("jcbSIMCHANGE");
    checkMe();
  }
  void checkMe() {
    if (jcbREAL.isSelected()) {
      fillMonthData();
      setObracun();
    }
    else {
      fillYearData();
      setSimulacija();
    }
  }

  void fillMonthData() {
    dm.getOS_Metaobrada().setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
    jrfCORG.forceFocLost();
    if (getOS_Kontrola().getString("MJESEC").equals("")) {
      dm.getOS_Metaobrada().setTimestamp("DATUMOD", util.findFirstDayOfYear(Integer.parseInt(getOS_Kontrola().getString("GODINA"))));
      dm.getOS_Metaobrada().setTimestamp("DATUMDO", util.findLastDayOfMonth(0, Integer.parseInt(getOS_Kontrola().getString("GODINA"))));
    }
    else {
      dm.getOS_Metaobrada().setTimestamp("DATUMOD", util.findFirstDayOfMonth(Integer.parseInt(getOS_Kontrola().getString("MJESEC")), Integer.parseInt(getOS_Kontrola().getString("GODINA"))));
      dm.getOS_Metaobrada().setTimestamp("DATUMDO", util.findLastDayOfMonth(Integer.parseInt(getOS_Kontrola().getString("MJESEC")), Integer.parseInt(getOS_Kontrola().getString("GODINA"))));
    }
    rcc.setLabelLaF(jrfCORG, false);
    rcc.setLabelLaF(jrfNAZORG, false);
    rcc.setLabelLaF(jbCORG, false);
    rcc.setLabelLaF(jtfDATUMOD, false);
    rcc.setLabelLaF(jtfDATUMDO, true);
  }
  void fillYearData() {
    jrfCORG.setText(hr.restart.zapod.OrgStr.getKNJCORG());
    jrfCORG.forceFocLost();
    dm.getOS_Metaobrada().setTimestamp("DATUMOD", util.findFirstDayOfYear(Integer.parseInt(getOS_Kontrola().getString("GODINA"))));
    dm.getOS_Metaobrada().setTimestamp("DATUMDO", util.findLastDayOfYear(Integer.parseInt(getOS_Kontrola().getString("GODINA"))));
    rcc.setLabelLaF(jrfCORG, false);
    rcc.setLabelLaF(jrfNAZORG, false);
    rcc.setLabelLaF(jbCORG, false);
    rcc.setLabelLaF(jtfDATUMOD, false);
    rcc.setLabelLaF(jtfDATUMDO, false);
  }
  void fillSimData() {
    rcc.setLabelLaF(jrfCORG, true);
    rcc.setLabelLaF(jrfNAZORG, true);
    rcc.setLabelLaF(jbCORG, true);
    rcc.setLabelLaF(jtfDATUMOD, true);
    rcc.setLabelLaF(jtfDATUMDO, true);
  }
  void setSimulacija() {
    rcbTIPAMOR.setRaItems(new String [][] {
      {"Godišnji predraèun",            "P"},
      {"Simulacija obraèuna",           "S"}
    });
    dm.getOS_Metaobrada().setString("TIPAMOR", "P");
  }
  void setObracun() {
    rcbTIPAMOR.setRaItems(new String [][] {
      {"Mjeseèni obraèun",              "M"},
      {"Godišnji obraèun",              "K"}
    });
    dm.getOS_Metaobrada().setString("TIPAMOR", "M");
  }
  void updateMonthData() {
    dm.getOS_Obrada2().open();
    dm.getOS_Promjene().open();
    dm.getOS_Arhiva().open();
    dm.getOS_Obrada2().first();
    dm.getOS_Obrada2().refresh();
    System.out.println("updateMonthData: "+dm.getOS_Obrada2().getRowCount());
    do {
      com.borland.dx.sql.dataset.QueryDataSet qdsPromjene= hr.restart.util.Util.getNewQueryDataSet(sjQuerys.getPromjeneAmor(dm.getOS_Obrada2().getString("CORG"), dm.getOS_Obrada2().getString("INVBROJ"), getOS_Kontrola().getString("GODINA")));
      if (qdsPromjene.getRowCount()>0) {
        System.out.println("ImamoHrvatsku");
        qdsPromjene.setTimestamp("DATPROMJENE", dm.getOS_Metaobrada().getTimestamp("DATUMDO"));
        qdsPromjene.setBigDecimal("ISPPOTRAZUJE", util.sumValue(qdsPromjene.getBigDecimal("ISPPOTRAZUJE"), dm.getOS_Obrada2().getBigDecimal("AMORTIZACIJA"), dm.getOS_Obrada2().getBigDecimal("PAMORTIZACIJA")));
        qdsPromjene.setBigDecimal("SALDO", qdsPromjene.getBigDecimal("ISPPOTRAZUJE").negate());
        qdsPromjene.saveChanges();
      }
      else {
// Upisujemo stavke u os_promjene
        dm.getOS_Promjene().insertRow(false);
        dm.getOS_Promjene().setInt("RBR", osUtil.getUtil().getOS_PromjeneMaxRBR(dm.getOS_Obrada2().getString("CORG"), dm.getOS_Obrada2().getString("INVBROJ"), 'A'));
        dm.getOS_Promjene().setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
        dm.getOS_Promjene().setString("CORG2", dm.getOS_Obrada2().getString("CORG"));
        dm.getOS_Promjene().setString("STATUS", "A");
        dm.getOS_Promjene().setString("INVBROJ", dm.getOS_Obrada2().getString("INVBROJ"));
        dm.getOS_Promjene().setString("CPROMJENE", osUtil.getUtil().getSifraObrAmor());
        dm.getOS_Promjene().setTimestamp("DATPROMJENE", dm.getOS_Metaobrada().getTimestamp("DATUMDO"));
        dm.getOS_Promjene().setBigDecimal("ISPPOTRAZUJE", dm.getOS_Obrada2().getBigDecimal("AMORTIZACIJA").add(dm.getOS_Obrada2().getBigDecimal("PAMORTIZACIJA")));
        dm.getOS_Promjene().setBigDecimal("ISPDUGUJE", util.nul);
        dm.getOS_Promjene().setBigDecimal("OSNPOTRAZUJE", util.nul);
        dm.getOS_Promjene().setBigDecimal("OSNDUGUJE", util.nul);
        dm.getOS_Promjene().setBigDecimal("SALDO", dm.getOS_Promjene().getBigDecimal("ISPPOTRAZUJE").negate());
        dm.getOS_Promjene().setString("STATUSKNJ", "D");
        dm.getOS_Promjene().post();
        dm.getOS_Promjene().saveChanges();
      }
    } while (dm.getOS_Obrada2().next());
    int rbr=osUtil.getUtil().getAmorRBR();
    vl.runSQL(sjQuerys.insertLog(rbr));
//    vl.runSQL(sjQuerys.insertArh(rbr));
    dm.getOS_Log().refresh();
  }
  private boolean checkAmor() {
    if (!checkOS_Log()) return false;
    if (dm.getOS_Metaobrada().getString("TIPAMOR").equals("M")) {
     if (JOptionPane.showConfirmDialog(jp, "Postoji obraèun amortizacije ("+hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(dm.getOS_Metaobrada().getTimestamp("DATUMOD"))+"-"+hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(dm.getOS_Metaobrada().getTimestamp("DATUMDO"))+"). Želite brisati radne tabele ?","Brisanje radnih tabela",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
       hr.restart.os.osUtil.getUtil().emptyAmorTable();
     }
   }
   return true;
  }
  private boolean checkOS_Log() {
    com.borland.dx.sql.dataset.QueryDataSet qds=hr.restart.util.Util.getNewQueryDataSet(sjQuerys.getNeknjizeniLog());
    if (qds.getRowCount()>0) return false;
    return true;
  }
}

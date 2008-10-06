/****license*****************************************************************
**   file: osTemplate.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableRunningSum;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**ra
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class osTemplate extends raMasterDetail {
  String tmp;
  boolean validDate=true;
  com.borland.dx.sql.dataset.QueryDataSet qdsLokacije;
  com.borland.dx.sql.dataset.QueryDataSet qdsObjekti;
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
  String presORG;
  /// preselect start
  hr.restart.util.PreSelect pres = new hr.restart.util.PreSelect(){
    public void SetFokus() {
      presCorg.setText(hr.restart.zapod.OrgStr.getKNJCORG());
      presCorg.forceFocLost();
      presCorg.requestFocus();
      rcbAKTIV.setSelectedIndex(0);
      rcbPripOrgJed.setSelectedIndex(0);
      stds.setString("OSTR","S");
//      jcbCORG.setSelected(true);
      if (rdOSUtil.getUtil().getKontrola()) {
        tsKontrola = dm.getOS_Kontrola().getTimestamp("DATUM");
        prom = dm.getOS_Kontrola().getString("PROM");
        amor = dm.getOS_Kontrola().getString("AMOR");
        reva = dm.getOS_Kontrola().getString("REVA");
      }
      else {
        JOptionPane.showConfirmDialog(jpMasterOS,"Ne postoji kontrolni slog za aktivno knjigovodstvo !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        pres.getPreSelDialog().hide();
      }
    }
    public boolean applySQLFilter() {
      QueryDataSet myDataSet=(QueryDataSet) getSelDataSet();
      String qStr;
      if (mod=='N' || mod=='S' || mod=='O' || mod=='L' || mod=='P') {
        if (rcbPripOrgJed.getDataValue().equals("S")) //jcbCORG.isSelected())
          qStr = sjQuerys.getOSFromCorg(getSelRow().getString("CORG2"), mod, rcbAKTIV.getDataValue());          
        else
          qStr = sjQuerys.getOSFromOnlyCorg(getSelRow().getString("CORG2"), mod, rcbAKTIV.getDataValue());
      } else {
        if (rcbPripOrgJed.getDataValue().equals("S")) //jcbCORG.isSelected())
          qStr = sjQuerys.getSIFromCorg(getSelRow().getString("CORG2"), rcbAKTIV.getDataValue());
        else
          qStr = sjQuerys.getSIFromOnlyCorg(getSelRow().getString("CORG2"), rcbAKTIV.getDataValue());
      }
      Aus.refilter(myDataSet, qStr);
      return true;
    }
    public boolean Validacija() {
      dm.getOrgstruktura().open();
//      myKeyF9 = rdOSUtil.getUtil().getMyF9DS(dm.getOrgstruktura().getString("CORG"));
      BindComp();
      if (!provjeraStatusaRada()) {
        pres.cancelSelect();
        return false;
      }
      presORG=presCorg.getText();
      return true;
    }
  };
  /// preselect end
  dM dm = dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  TableDataSet tds = new TableDataSet();
  QueryDataSet myVrPr = new QueryDataSet();
  QueryDataSet myKeyF9 = new QueryDataSet();
  hr.restart.util.Valid vl = new hr.restart.util.Valid();

  Timestamp tsKontrola;
  String prom, amor, reva;

//  Column colOsnovica = new Column();
//  Column colIspravak = new Column();
  private Column colOsnovica = dM.createBigDecimalColumn("osnovica");
  private Column colIspravak = dM.createBigDecimalColumn("ispravak");

  BigDecimal oldISP = new BigDecimal(0);
  BigDecimal oldOSN = new BigDecimal(0);

  int validYear = 0;
  int tempRBR = 0;
  char mod;
  Timestamp nullDate = Timestamp.valueOf("1970-01-01 01:00:00.0");

  JPanel jpMasterOS = new JPanel();
  JPanel jpDetailOS = new JPanel();

  XYLayout xYLayout1 = new XYLayout();
  XYLayout xYLayout2 = new XYLayout();

  //****** Master panel
  JLabel jlSifra = new JLabel();
  JLabel jlNaziv = new JLabel();

  JraTextField jtfInvBr = new JraTextField();
  JraTextField jtfNazInvBr = new JraTextField();
  JLabel jlInvBr = new JLabel();

  JlrNavField jrfNazLokacija = new JlrNavField();
  JlrNavField jrfLokacija = new JlrNavField();
  JLabel jlLokacija = new JLabel();
  JraButton jbLokacija = new JraButton();

  JlrNavField jrfAmGrup = new JlrNavField();
  JlrNavField jrfNazAmGrup = new JlrNavField();
  JLabel jlAmGrup = new JLabel();
  JraButton jbAmGr = new JraButton();

  JlrNavField jrfNazKonto = new JlrNavField();
  JlrNavField jrfKonto = new JlrNavField();
  JLabel jlKonto = new JLabel();
  JraButton jbKonto = new JraButton();

  JlrNavField jrfRevSk = new JlrNavField();
  JlrNavField jrfNazRevSk = new JlrNavField();
  JLabel jlRevSk = new JLabel();
  JraButton jbRevSk = new JraButton();

  JlrNavField jrfNazArtikl = new JlrNavField();
  JlrNavField jrfArtikl = new JlrNavField();
  JLabel jlArtikl = new JLabel();
  JraButton jbArtikl = new JraButton();

  JlrNavField jrfCPORIJEKLO = new JlrNavField();
  JlrNavField jrfNAZPORIJEKLO = new JlrNavField();
  JLabel jlPorijeklo = new JLabel();
  JraButton jbCPORIJEKLO = new JraButton();

  JraTextField jrfGodPr = new JraTextField() {
    public void valueChanged() {
      jrfGodPr_focusLost(null);
    }
  };
  JLabel jlGodPr = new JLabel();

  //****** Detail panel
  JLabel jlDSifra = new JLabel();
  JLabel jlDNaziv = new JLabel();

  JLabel jlVrPromjene = new JLabel();
  JlrNavField jrfCPROMJENE = new JlrNavField();
  JlrNavField jrfNAZPROMJENE = new JlrNavField();
  JraButton jbCPROMJENE = new JraButton();

  JraTextField jtfDatum = new JraTextField();
  JLabel jlDatum = new JLabel();

  JraTextField jtfDokument = new JraTextField();
  JLabel jlDokument = new JLabel();

  JLabel jlOsnovica = new JLabel();
  JraTextField jtfOsnovica = new JraTextField();

  JraTextField jtfIspravak = new JraTextField();
  JLabel jlIspravak = new JLabel();
  
  raNavAction rnvCopyCurr = new raNavAction("Kopiraj tekuæi zapis",
      raImages.IMGCOPYCURR, KeyEvent.VK_F2, KeyEvent.SHIFT_MASK) {
    public void actionPerformed(ActionEvent e) {
      rnvCopyCurr_action();
    }
  };

  public osTemplate() {
    super(1,2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  boolean copyRec = false;
  DataRow mRow = null;
  DataRow dRow = null;
  
  protected void rnvCopyCurr_action() {
    if (getMasterSet().rowCount() == 0) return;
    mRow = new DataRow(getMasterSet());
    getMasterSet().copyTo(mRow);
    refilterDetailSet();
    if (getDetailSet().rowCount() == 1) {
      dRow = new DataRow(getDetailSet());
      getDetailSet().copyTo(dRow);
    }
    copyRec = true;
    raMaster.rnvAdd_action();
    if (raMaster.getMode() != 'N') {
      copyRec = false;
      mRow = null;
      dRow = null;
    }
  }

  public void jbInitA() throws Exception {
    java.lang.String[] key={"CORG", "INVBROJ", "STATUS"};
    java.lang.String[] key2={"CORG", "INVBROJ", "STATUS", "RBR"};

    jpMasterOS.setLayout(xYLayout1);
    jpDetailOS.setLayout(xYLayout2);

    setMasterKey(key);
    setDetailKey(key2);

    xYLayout2.setWidth(650);
    xYLayout2.setHeight(220);

    jlInvBr.setText("Inventarski broj");
    jlLokacija.setText("Lokacija");
    jbLokacija.setText("jButton1");

    jbAmGr.setText("jButton1");
    jlAmGrup.setText("Amortizacijska grupa");
    jbKonto.setText("jButton1");
    jlKonto.setText("Konto");
    jbRevSk.setText("jButton1");
    jlRevSk.setText("Rev skupina");
    jbArtikl.setText("jButton1");
    jlArtikl.setText("Artikl");
    jbCPORIJEKLO.setText("jButton1");
    jlPorijeklo.setText("Porijeklo");
    jlGodPr.setText("Godina proizvodnje");
    jlSifra.setText("Šifra");
    jlNaziv.setText("Naziv");

    xYLayout1.setWidth(664);
    xYLayout1.setHeight(345);
    jlVrPromjene.setText("Vrsta promjene");
    jlDSifra.setText("Šifra");
    jlDNaziv.setText("Naziv");
    jbCPROMJENE.setText("jButton1");
    jlDatum.setText("Datum");
    jlDokument.setText("Dokument");
    jlOsnovica.setText("Osnovica");
    jlIspravak.setText("Ispravak");

    jrfLokacija.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jrfLokacija_focusGained(e);
      }
    });
    jpMasterOS.add(jtfInvBr, new XYConstraints(150, 45, 100, -1));
    jpMasterOS.add(jtfNazInvBr,  new XYConstraints(255, 45, 350, -1));
    jpMasterOS.add(jlInvBr, new XYConstraints(15, 45, -1, -1));
    jpMasterOS.add(jlSifra,  new XYConstraints(150, 20, -1, -1));
    jpMasterOS.add(jlNaziv,   new XYConstraints(255, 20, -1, -1));
    jpMasterOS.add(jlLokacija, new XYConstraints(15, 120, -1, -1));
    jpMasterOS.add(jrfLokacija, new XYConstraints(150, 120, 100, -1));
    jpMasterOS.add(jrfNazLokacija, new XYConstraints(255, 120, 350, -1));
    jpMasterOS.add(jbLokacija, new XYConstraints(610, 120, 21, 21));
    jpMasterOS.add(jlAmGrup, new XYConstraints(15, 145, -1, -1));
    jpMasterOS.add(jrfAmGrup, new XYConstraints(150, 145, 100, -1));
    jpMasterOS.add(jrfNazAmGrup, new XYConstraints(255, 145, 350, -1));
    jpMasterOS.add(jbAmGr, new XYConstraints(610, 145, 21, 21));
    jpMasterOS.add(jlKonto, new XYConstraints(15, 170, -1, -1));
    jpMasterOS.add(jrfKonto, new XYConstraints(150, 170, 100, -1));
    jpMasterOS.add(jrfNazKonto, new XYConstraints(255, 170, 350, -1));
    jpMasterOS.add(jbKonto, new XYConstraints(610, 170, 21, 21));
    jpMasterOS.add(jlRevSk, new XYConstraints(15, 195, -1, -1));
    jpMasterOS.add(jrfRevSk, new XYConstraints(150, 195, 100, -1));
    jpMasterOS.add(jrfNazRevSk, new XYConstraints(255, 195, 350, -1));
    jpMasterOS.add(jbRevSk, new XYConstraints(610, 195, 21, 21));
    jpMasterOS.add(jlArtikl, new XYConstraints(15, 220, -1, -1));
    jpMasterOS.add(jrfArtikl, new XYConstraints(150, 220, 100, -1));
    jpMasterOS.add(jrfNazArtikl, new XYConstraints(255, 220, 350, -1));
    jpMasterOS.add(jbArtikl, new XYConstraints(610, 220, 21, 21));
    jpMasterOS.add(jLabel2, new XYConstraints(15, 70, -1, -1));
    jpMasterOS.add(jrfCORG2, new XYConstraints(150, 70, 100, -1));
    jpMasterOS.add(jrfNAZORG2, new XYConstraints(255, 70, 350, -1));
    jpMasterOS.add(jbCORG2, new XYConstraints(610, 70, 21, 21));
    jpMasterOS.add(jLabel5,  new XYConstraints(15, 95, -1, -1));
    jpMasterOS.add(jrfCOBJEKT, new XYConstraints(150, 95, 100, -1));
    jpMasterOS.add(jrfNAZOBJEKT, new XYConstraints(255, 95, 350, -1));
    jpMasterOS.add(jbCOBJEKT, new XYConstraints(610, 95, 21, 21));
    jpMasterOS.add(jlPorijeklo, new XYConstraints(15, 270, -1, -1));
    jpMasterOS.add(jrfCPORIJEKLO, new XYConstraints(150, 270, 100, -1));
    jpMasterOS.add(jrfNAZPORIJEKLO, new XYConstraints(255, 270, 350, -1));
    jpMasterOS.add(jbCPORIJEKLO, new XYConstraints(610, 270, 21, 21));
    jpMasterOS.add(jlGodPr, new XYConstraints(15, 295, -1, -1));
    jpMasterOS.add(jrfGodPr, new XYConstraints(150, 295, 100, -1));
    jpMasterOS.add(jlCRADNIK, new XYConstraints(15, 245, -1, -1));
    jpMasterOS.add(jrfCRADNIK, new XYConstraints(150, 245, 100, -1));
    jpMasterOS.add(jrfIME,   new XYConstraints(255, 245, 175, -1));
    jpMasterOS.add(jbCRADNIK, new XYConstraints(610, 245, 21, 21));
    jpMasterOS.add(jrfPREZIME,    new XYConstraints(435, 245, 170, -1));
    jpMasterOS.add(jcbAKTIV,   new XYConstraints(505, 20, 100, 25));

    jpDetailOS.add(jlVrPromjene,  new XYConstraints(15, 45, -1, -1));
    jpDetailOS.add(jlDSifra,  new XYConstraints(150, 20, -1, -1));
    jpDetailOS.add(jlDNaziv,  new XYConstraints(255, 20, -1, -1));
    jpDetailOS.add(jrfCPROMJENE,  new XYConstraints(150, 45, 100, -1));
    jpDetailOS.add(jrfNAZPROMJENE,  new XYConstraints(255, 45, 355, -1));
    jpDetailOS.add(jbCPROMJENE,     new XYConstraints(614, 45, 21, 21));
    jpDetailOS.add(jtfDatum,     new XYConstraints(150, 70, 100, -1));
    jpDetailOS.add(jlDatum,  new XYConstraints(15, 70, -1, -1));
    jpDetailOS.add(jlOPIS,    new XYConstraints(15, 95, -1, -1));
    jpDetailOS.add(jlCPAR,     new XYConstraints(15, 120, -1, -1));
    jpDetailOS.add(jlCORG,   new XYConstraints(15, 145, -1, -1));
    jpDetailOS.add(jlDokument,  new XYConstraints(255, 70, -1, -1));
    jpDetailOS.add(jtfDokument,  new XYConstraints(350, 70, 260, -1));
    jpDetailOS.add(jtfOpis,      new XYConstraints(150, 95, 460, -1));
    jpDetailOS.add(jbCPAR,  new XYConstraints(614, 120, 21, 21));
    jpDetailOS.add(jrfNAZPAR,  new XYConstraints(255, 120, 355, -1));
    jpDetailOS.add(jrfCPAR,  new XYConstraints(150, 120, 100, -1));
    jpDetailOS.add(jbCORG,   new XYConstraints(614, 145, 21, 21));
    jpDetailOS.add(jrfNAZORG,  new XYConstraints(255, 145, 355, -1));
    jpDetailOS.add(jrfCORG,  new XYConstraints(150, 145, 100, -1));
    jpDetailOS.add(jlOsnovica, new XYConstraints(15, 170, -1, -1));
    jpDetailOS.add(jtfOsnovica, new XYConstraints(150, 170, 100, -1));
    jpDetailOS.add(jlIspravak, new XYConstraints(265, 170, -1, -1));
    jpDetailOS.add(jtfIspravak, new XYConstraints(330, 170, 100, -1));
    jpDetailOS.add(jLabel1, new XYConstraints(445, 170, -1, -1));
    jpDetailOS.add(jtfUIPRPOR, new XYConstraints(510, 170, 100, -1));
//    jpDetailOS.add(jlLOC, new XYConstraints(15, 170, -1, -1));
//    jpDetailOS.add(jrfLOC, new XYConstraints(150, 170, 100, -1));
//    jpDetailOS.add(jrfNAZLOC, new XYConstraints(255, 170, 355, -1));
//    jpDetailOS.add(jbLOC, new XYConstraints(614, 170, 21, 21));

/*    colOsnovica.setCaption("Osnovica");
    colOsnovica.setColumnName("OSNOVICA");
    colOsnovica.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    colOsnovica.setDisplayMask("###,###,##0.00");
    colOsnovica.setDefault("0");
    colOsnovica.setResolvable(false);
    colOsnovica.setSqlType(0);

    colIspravak.setCaption("Ispravak");
    colIspravak.setColumnName("ISPRAVAK");
    colIspravak.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    colIspravak.setDisplayMask("###,###,##0.00");
    colIspravak.setDefault("0");
    colIspravak.setResolvable(false);
    colIspravak.setSqlType(0);*/

    tds.setColumns(new Column[] {colOsnovica, colIspravak});
    raDetail.getJpTableView().addTableModifier(new raTableRunningSum("SALDO"));
    jrfAmGrup.setNavButton(jbAmGr);
    jrfArtikl.setNavButton(jbArtikl);
    jrfKonto.setNavButton(jbKonto);
    jrfLokacija.setNavButton(jbLokacija);
//    jrfLOC.setNavButton(jbLOC);
    jrfRevSk.setNavButton(jbRevSk);
    jrfCPROMJENE.setNavButton(jbCPROMJENE);
    presCorg.setNavButton(jbCOrg);
    jrfCPORIJEKLO.setNavButton(jbCPORIJEKLO);
    raDetail.getRepRunner().addReport("hr.restart.os.repKarticaOS", "Kartica osnovnog sredstva", 5);
//    raMaster.getRepRunner().addReport("hr.restart.os.repOrgOS", "Lista osnovnih sredstava", 5);
  }
  public boolean doWithSaveDetail(char mode) {
    if (mode=='B') {

    }
    else {
      hr.restart.os.osUtil.getUtil().afterUpdateOS(oldOSN, oldISP, mod);
      hr.restart.util.raTransaction.saveChanges(getMasterSet());
    }
    return true;
  }
  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jrfCPROMJENE))
      return false;
    if (mode =='I') {
      if (mod=='S' || mod=='N' || mod=='L' || mod=='O' || mod=='P') {
        oldOSN = hr.restart.os.osUtil.getUtil().getOldOSN(mod);
        oldISP = hr.restart.os.osUtil.getUtil().getOldISP(mod);
      }
      else {
        oldOSN = hr.restart.os.osUtil.getUtil().getOldSIOSN(mod);
        oldISP = hr.restart.os.osUtil.getUtil().getOldSIISP(mod);
      }
    }
    if (mod=='L' || mod=='O') {
      if (osUtil.getUtil().getSifraObrAmor().equals("")) {
        JOptionPane.showConfirmDialog(jpMasterOS,"Ne postoji definirana vrsta promjene za amortizaciju !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (mod=='L') {
        insertStavke('L');
      }
      else if (mod=='O') {
        insertStavke('O');
      }
    }

    if (mode=='N') {
      if (mod=='X' || mod=='Y' || mod=='Z') {
        getDetailSet().setInt("RBR", rdOSUtil.getUtil().getSIMaxRBR(getDetailSet().getString("INVBROJ"), getDetailSet().getString("CORG"), getDetailSet().getString("STATUS"))+1);
      }
      else if (mod=='N' || mod=='S' || mod=='P') {
        getDetailSet().setInt("RBR", rdOSUtil.getUtil().getOSMaxRBR(getDetailSet().getString("INVBROJ"), getDetailSet().getString("CORG"), getDetailSet().getString("STATUS"))+1);
      }
    }
    if (mod=='S' || mod=='N' || mod=='X' || mod=='Y' || mod=='Z' || mod=='P') {
      hr.restart.os.osUtil.getUtil().beforeUpdateOS(getTDS(), mod);
    }
    if (mod!='L' || mod!='Z') {
//      getMasterSet().setInt("CPAR", getDetailSet().getInt("CPAR"));
    }
    return true;
  }

  public boolean ValidacijaMaster(char mode) {
    getMasterSet().setString("CORG", hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(presORG));
//    getMasterSet().setString("CORG2", presORG);
    if (mode=='N' && (mod=='N' || mod=='P' || mod=='L' || mod=='S' || mod=='O')) {
      if (vl.isEmpty(jtfInvBr))
        return false;
      if (vl.isEmpty(jtfNazInvBr))
        return false;
      if (vl.isEmpty(jrfLokacija))
        return false;
      if (vl.isEmpty(jrfAmGrup))
        return false;
      if (vl.isEmpty(jrfKonto))
        return false;
      if (vl.isEmpty(jrfRevSk))
        return false;
      if (vl.isEmpty(jrfArtikl))
        return false;
      if (vl.isEmpty(jrfCPORIJEKLO))
        return false;

      if (getMasterSet().getString("PORIJEKLO").trim().equals(""))
        getMasterSet().setString("PORIJEKLO", "T");

      if (rdOSUtil.getUtil().getInvBr(jtfInvBr.getText().trim(), mod, presORG)) {
        jtfInvBr.requestFocus();
        JOptionPane.showConfirmDialog(jpMasterOS,"Postoji zapis sa tim inventarskim brojem !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    else {
        if (vl.isEmpty(jtfInvBr))
            return false;
          if (vl.isEmpty(jtfNazInvBr))
            return false;
          if (vl.isEmpty(jrfKonto))
            return false;

          if (getMasterSet().getString("PORIJEKLO").trim().equals(""))
            getMasterSet().setString("PORIJEKLO", "T");

          if (mode == 'N' && rdOSUtil.getUtil().getInvBr(jtfInvBr.getText().trim(), mod, presORG)) {
            jtfInvBr.requestFocus();
            JOptionPane.showConfirmDialog(jpMasterOS,"Postoji zapis sa tim inventarskim brojem !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
            return false;
          }
    }
    try {
        rdOSUtil.getUtil().provjeraGodine(jrfGodPr);
        return true;
      }
      catch (Exception ex) {
        jrfGodPr.setBackground(Color.red);
        jrfGodPr.requestFocus();
        return false;
      }

  }

  public TableDataSet getTDS() {
    return tds;
  }

  public void AfterSaveDetail(char mode) {
//    hr.restart.os.osUtil.getUtil().afterUpdateOS(oldOSN, oldISP, mod);
    oldOSN=util.nul;
    oldISP=util.nul;
    tds.deleteAllRows();
    beforeShowDetail();
    raDetail.getOKpanel().jPrekid_actionPerformed();
    setOsn_Isp();
  }

  public void AfterDeleteDetail() {
    int row=getDetailSet().getRow();
    beforeShowDetail();
    hr.restart.os.osUtil.getUtil().afterDeleteOS(oldOSN, oldISP, mod);
    oldOSN=util.nul;
    oldISP=util.nul;
    System.out.println("getDetailSet(): "+getDetailSet().getInt("RBR"));
    if (getDetailSet().getInt("RBR")>1) {
      String qStr = rdOSUtil.getUtil().recalcRBR(getDetailSet().getString("invbroj"),
                   getDetailSet().getInt("rbr")-1);
      try {
        getDetailSet().getDatabase().executeStatement(qStr);
        getDetailSet().refresh();
        getDetailSet().goToRow(row);
      }
      catch (Exception ex) {
      }
    }
    super.AfterDeleteDetail();
  }
  public void AfterDeleteMaster() {
    hr.restart.robno.Util.getUtil().emptyTable(getDetailSet());
/*    if (getDetailSet().getRowCount()>0) {
      System.out.println("rowCount>0");
      if (mod=='L' || mod=='S' || mod=='N' || mod=='P' || mod=='O') {
        System.out.println("swe je pod kontrolim");
        rdOSUtil.getUtil().deleteDetailSet(getMasterSet(), 0);
      }
      else
        rdOSUtil.getUtil().deleteDetailSet(getMasterSet(), 1);
    }*/
  }
  public boolean DeleteCheckDetail() {
    if (getDetailSet().getInt("RBR") < rdOSUtil.getUtil().getOSMaxRBR(getDetailSet().getString("INVBROJ"), getDetailSet().getString("CORG"),getDetailSet().getString("STATUS")) && mod=='N') {
      JOptionPane.showConfirmDialog(jpDetailOS, "Brisanje nije mogu?e ! Mogu?e je brisati samo zadnju stavku !", "Greška", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    else {
      if (getDetailSet().getInt("RBR") < rdOSUtil.getUtil().getSIMaxRBR(getDetailSet().getString("INVBROJ"), getDetailSet().getString("CORG"),getDetailSet().getString("STATUS")) && mod=='X') {
        JOptionPane.showConfirmDialog(jpDetailOS, "Brisanje nije mogu?e ! Mogu?e je brisati samo zadnju stavku !", "Greška", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    if (!getDetailSet().getString("CORG2").equals(getMasterSet().getString("CORG2"))) {
      JOptionPane.showConfirmDialog(jpDetailOS,"Brisanje nije mogu?e ! Promjenjena org. jedinica !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (rdOSUtil.getUtil().getTipPromjene(getDetailSet().getString("CPROMJENE")).equals("A")) {
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Nije moguæe brisanje sloga amortizacije! ","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mod=='S' || mod=='N' || mod=='L' || mod=='O' || mod=='P') {
      oldOSN = hr.restart.os.osUtil.getUtil().getOldOSN(mod);
      oldISP = hr.restart.os.osUtil.getUtil().getOldISP(mod);
    }
    else {
      oldOSN = hr.restart.os.osUtil.getUtil().getOldSIOSN(mod);
      oldISP = hr.restart.os.osUtil.getUtil().getOldSIISP(mod);
    }
    return true;
  }

  public boolean DeleteCheckMaster() {
    if (getMasterSet().getString("AKTIV").equals("N")) {
      JOptionPane.showConfirmDialog(this.jpMasterOS,"Sredstvo nije aktivno, nije dozvoljeno brisanje !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public void SetFokusDetail(char mode) {
    validDate=true;
    getDetailSet().setString("STATUS", getMasterSet().getString("STATUS"));
    setOsn_Isp();
    if (mod!='O') {
      rcc.setLabelLaF(jrfCORG, false);
      rcc.setLabelLaF(jrfNAZORG, false);
      rcc.setLabelLaF(jbCORG, false);
    }
    if (mode=='N') {
      jrfNAZPAR.setText("");
      getDetailSet().setString("CORG2", getMasterSet().getString("CORG2"));
      jrfCORG.forceFocLost();
      getDetailSet().setString("CORG", getMasterSet().getString("CORG"));
    }
    else if (mode=='I') {
      rcc.setLabelLaF(jrfCORG, false);
      rcc.setLabelLaF(jrfNAZORG, false);
      rcc.setLabelLaF(jbCORG, false);
    }
    if (getMasterSet().getString("AKTIV").equals("N")) {
      rcc.setLabelLaF(jtfOsnovica, false);
      rcc.setLabelLaF(jtfIspravak, false);
      rcc.setLabelLaF(jtfUIPRPOR, false);
    }
    
    if (mode == 'N' && dRow != null) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          copyDetailRow();
        }
      });
    }
  }
  
  void copyDetailRow() {
    dM.copyColumns(dRow, getDetailSet(), new String[] {
      "CPROMJENE", "DATPROMJENE", "DOKUMENT", "OPIS", "CPAR"
    });
    jrfCPROMJENE.forceFocLost();
    jrfCPAR.forceFocLost();
    tds.setBigDecimal("OSNOVICA", dRow.getBigDecimal("OSNDUGUJE").
        add(dRow.getBigDecimal("OSNPOTRAZUJE")));
    jtfDokument.requestFocusLater();
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'N' && copyRec && mRow != null) {
      dM.copyColumns(mRow, getMasterSet(), new String[] {
        "NAZSREDSTVA", "CORG2", "COBJEKT", "CLOKACIJE", "CARTIKLA",
        "BROJKONTA", "CGRUPE", "CSKUPINE", "PORIJEKLO", "CRADNIK", "GODPROIZ"
      });
    }
  }
    
  public void SetFokusMaster(char mode) {
    rcc.setLabelLaF(jcbAKTIV, false);
//    rcc.setLabelLaF(jrfNazLokacija, false);
//    rcc.setLabelLaF(jrfNazAmGrup, false);
//    rcc.setLabelLaF(jrfNazRevSk, false);
//    rcc.setLabelLaF(jrfNazArtikl, false);
//    rcc.setLabelLaF(jrfNazDobavljac, false);
//    rcc.setLabelLaF(jrfNazKonto, false);

    if (mode=='N') {
      if (mod=='P') {
        getMasterSet().setString("STATUS", "P");
      }
      else {
        getMasterSet().setString("STATUS", "A");
      }
      jrfCORG2.setText(presCorg.getText());
      jrfCORG2.forceFocLost();
      if (rcbPripOrgJed.getDataValue().equals("Z")){ //!jcbCORG.isSelected()) {
        rcc.setLabelLaF(jrfCORG2, false);
        rcc.setLabelLaF(jrfNAZORG2, false);
        rcc.setLabelLaF(jbCORG2, false);
      }
      getMasterSet().setString("INVBROJ", osUtil.getUtil().getInvBroj());
      jtfInvBr.requestFocus();
    }
    else if (mode=='I') {
      rcc.setLabelLaF(jtfInvBr, false);
      rcc.setLabelLaF(jrfCORG2, false);
      rcc.setLabelLaF(jrfNAZORG2, false);
      rcc.setLabelLaF(jbCORG2, false);
      jrfCOBJEKT.requestFocus();
    }
  }

  /**
   * ----------------------------------------
   * mod = N - Osnovna Sredstva - Nova godina
   * mod = S - Osnovna Sredstva - Stara godina
   * mod = L - Osnovna Sredstva - Likvidacija
   * mod = P - Osnovna Sredstva - Priprema
   * mod = O - Osnovna Sredstva - Promjena ogr. jedinice
   * ----------------------------------------
   * mod = X - Sitni inventar - Nova godina
   * mod = Y - Sitni inventar - Stara godina
   * mod = Z - Sitni inventar - Likvidacija
   * ----------------------------------------
   */


  private void getDS() {
    if (super.getClass().getName().equals("hr.restart.os.frmOSNovi")) {
      myVrPr = rdOSUtil.getUtil().getNonLikviDS();
      mod ='N';
    }
    else if (super.getClass().getName().equals("hr.restart.os.frmSINovi")) {
      myVrPr = rdOSUtil.getUtil().getNonLikviDS();
      mod ='X';
    }
    else if (super.getClass().getName().equals("hr.restart.os.frmOSStari")) {
      myVrPr = rdOSUtil.getUtil().getNonLikviDS();
      mod = 'S';
    }
    else if (super.getClass().getName().equals("hr.restart.os.frmSIStari")) {
      myVrPr = rdOSUtil.getUtil().getNonLikviDS();
      mod = 'Y';
    }
    else if (super.getClass().getName().equals("hr.restart.os.frmOSLikvi")) {
      myVrPr = rdOSUtil.getUtil().getLikviDS();
      mod ='L';
    }
    else if (super.getClass().getName().equals("hr.restart.os.frmSILikvi")) {
      myVrPr = rdOSUtil.getUtil().getLikviDS();
      mod ='Z';
    }
    else if (super.getClass().getName().equals("hr.restart.os.frmOSPriprema")) {
      myVrPr = rdOSUtil.getUtil().getNonLikviDS();
      mod ='P';
    }
    else {
      myVrPr = rdOSUtil.getUtil().getCorgDS();
      mod ='O';
    }
    System.out.println("mod = "+mod);
    myVrPr.open();
    myVrPr.getColumn("CPROMJENE").setCaption("Šifra");
    myVrPr.getColumn("NAZPROMJENE").setCaption("Naziv");
    myVrPr.close();
/*    myKeyF9.open();
    myKeyF9.getColumn("CLOKACIJE").setCaption("Šifra");
    myKeyF9.getColumn("NAZLOKACIJE").setCaption("Naziv");
    myKeyF9.close();*/
  }

  public void setOsn_Isp() {
    int secCompare = getDetailSet().getBigDecimal("OSNPOTRAZUJE").compareTo(new BigDecimal(0));
    if (secCompare==1) {
      tds.setBigDecimal("OSNOVICA", getDetailSet().getBigDecimal("OSNPOTRAZUJE"));
      tds.setBigDecimal("ISPRAVAK", getDetailSet().getBigDecimal("ISPDUGUJE"));
    }
    else {
      tds.setBigDecimal("OSNOVICA", getDetailSet().getBigDecimal("OSNDUGUJE"));
      tds.setBigDecimal("ISPRAVAK", getDetailSet().getBigDecimal("ISPPOTRAZUJE"));
    }
  }

  public void BindComp() {
    jtfInvBr.setDataSet(getMasterSet());
    jtfInvBr.setColumnName("INVBROJ");
    jtfNazInvBr.setDataSet(getMasterSet());
    jtfNazInvBr.setColumnName("NAZSREDSTVA");
    getDS();

    //org. jedinica na masteru
    jrfCORG2.setColumnName("CORG2");
    jrfCORG2.setNavColumnName("CORG");
    jrfCORG2.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(presCorg.getText()));
    jrfCORG2.setVisCols(new int[]{0,1});
    jrfCORG2.setColNames(new String[] {"NAZIV"});
    jrfCORG2.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZORG2});
    jrfCORG2.setDataSet(getMasterSet());
    
    jrfNAZORG2.setColumnName("NAZIV");
    jrfNAZORG2.setNavProperties(jrfCORG2);
    jrfNAZORG2.setSearchMode(1);

    //Objekti
    jrfCOBJEKT.setColumnName("COBJEKT");
    jrfCOBJEKT.setRaDataSet(qdsObjekti);
    jrfCOBJEKT.setVisCols(new int[]{3,4});
    jrfCOBJEKT.setColNames(new String[] {"NAZOBJEKT"});
    jrfCOBJEKT.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZOBJEKT});
    jrfCOBJEKT.setDataSet(getMasterSet());
    
    jrfNAZOBJEKT.setColumnName("NAZOBJEKT");
    jrfNAZOBJEKT.setNavProperties(jrfCOBJEKT);
    jrfNAZOBJEKT.setSearchMode(1);


    //lokacija
    jrfLokacija.setColumnName("CLOKACIJE");
    jrfLokacija.setRaDataSet(qdsLokacije);
    jrfLokacija.setVisCols(new int[]{4,5});
    jrfLokacija.setColNames(new String[] {"NAZLOKACIJE"});
    jrfLokacija.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazLokacija});
    jrfLokacija.setDataSet(getMasterSet());
    
    jrfNazLokacija.setColumnName("NAZLOKACIJE");
    jrfNazLokacija.setNavProperties(jrfLokacija);
    jrfNazLokacija.setSearchMode(1);

    //amortizacijske grupe
    jrfAmGrup.setColumnName("CGRUPE");
    jrfAmGrup.setRaDataSet(dm.getOS_Amgrupe());
    jrfAmGrup.setVisCols(new int[]{0,1});
    jrfAmGrup.setColNames(new String[] {"NAZGRUPE"});
    jrfAmGrup.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazAmGrup});
    jrfAmGrup.setDataSet(getMasterSet());
    
    jrfNazAmGrup.setColumnName("NAZGRUPE");
    jrfNazAmGrup.setNavProperties(jrfAmGrup);
    jrfNazAmGrup.setSearchMode(1);

    //rev skupina
    jrfRevSk.setColumnName("CSKUPINE");
    jrfRevSk.setRaDataSet(dm.getOS_Revskupine());
    jrfRevSk.setVisCols(new int[]{0,1});
    jrfRevSk.setColNames(new String[] {"NAZSKUPINE"});
    jrfRevSk.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazRevSk});
    jrfRevSk.setDataSet(getMasterSet());
    
    jrfNazRevSk.setColumnName("NAZSKUPINE");
    jrfNazRevSk.setNavProperties(jrfRevSk);
    jrfNazRevSk.setSearchMode(1);

    //artikli
    jrfArtikl.setColumnName("CARTIKLA");
    jrfArtikl.setRaDataSet(dm.getOS_Artikli());
    jrfArtikl.setVisCols(new int[]{0,1});
    jrfArtikl.setColNames(new String[] {"NAZARTIKLA"});
    jrfArtikl.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazArtikl});
    jrfArtikl.setDataSet(getMasterSet());
    
    jrfNazArtikl.setColumnName("NAZARTIKLA");
    jrfNazArtikl.setNavProperties(jrfArtikl);
    jrfNazArtikl.setSearchMode(1);

    //radnik
    jrfCRADNIK.setColumnName("CRADNIK");
    jrfCRADNIK.setRaDataSet(dm.getRadnici());
    jrfCRADNIK.setVisCols(new int[]{0,1,2});
    jrfCRADNIK.setColNames(new String[] {"IME", "PREZIME"});
    jrfCRADNIK.setTextFields(new javax.swing.text.JTextComponent[] {jrfIME, jrfPREZIME});
    jrfCRADNIK.setDataSet(getMasterSet());
    
    jrfIME.setColumnName("IME");
    jrfIME.setNavProperties(jrfCRADNIK);
    jrfIME.setSearchMode(1);
    jrfPREZIME.setColumnName("PREZIME");
    jrfPREZIME.setNavProperties(jrfCRADNIK);
    jrfPREZIME.setSearchMode(1);

    //porijeklo
    jrfCPORIJEKLO.setColumnName("PORIJEKLO");
    jrfCPORIJEKLO.setNavColumnName("CPORIJEKLO");
    jrfCPORIJEKLO.setRaDataSet(dm.getOS_Porijeklo());
    jrfCPORIJEKLO.setVisCols(new int[]{0,1});
    jrfCPORIJEKLO.setColNames(new String[] {"NAZPORIJEKLA"});
    jrfCPORIJEKLO.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPORIJEKLO});
    jrfCPORIJEKLO.setDataSet(getMasterSet());
    
    jrfNAZPORIJEKLO.setColumnName("NAZPORIJEKLA");
    jrfNAZPORIJEKLO.setNavProperties(jrfCPORIJEKLO);
    jrfNAZPORIJEKLO.setSearchMode(1);

    //konta
    jrfKonto.setColumnName("BROJKONTA");
    jrfKonto.setRaDataSet(dm.getKontaAnalitic());
    jrfKonto.setVisCols(new int[]{0,1});
    jrfKonto.setColNames(new String[] {"NAZIVKONTA"});
    jrfKonto.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazKonto});
    jrfKonto.setDataSet(getMasterSet());
    jrfKonto.setSearchMode(3);
    
    jrfNazKonto.setColumnName("NAZIVKONTA");
    jrfNazKonto.setNavProperties(jrfKonto);
    jrfNazKonto.setSearchMode(1);

    //god proiz
    jrfGodPr.setDataSet(getMasterSet());
    jrfGodPr.setColumnName("GODPROIZ");

    //vrsta promjene
    jrfCPROMJENE.setColumnName("CPROMJENE");
    jrfCPROMJENE.setRaDataSet(dm.getOS_Vrpromjene());
    jrfCPROMJENE.setVisCols(new int[]{2,3});
    jrfCPROMJENE.setColNames(new String[] {"NAZPROMJENE"});
    jrfCPROMJENE.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPROMJENE});
    jrfCPROMJENE.setDataSet(getDetailSet());
    
    jrfNAZPROMJENE.setColumnName("NAZPROMJENE");
    jrfNAZPROMJENE.setNavProperties(jrfCPROMJENE);
    jrfNAZPROMJENE.setSearchMode(1);

    //partner na stavci
    jrfCPAR.setColumnName("CPAR");
    jrfCPAR.setRaDataSet(dm.getPartneri());
    jrfCPAR.setVisCols(new int[]{0,1});
    jrfCPAR.setColNames(new String[] {"NAZPAR"});
    jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
    jrfCPAR.setDataSet(getDetailSet());
    
    jrfNAZPAR.setColumnName("NAZPAR");
    jrfNAZPAR.setNavProperties(jrfCPAR);
    jrfNAZPAR.setSearchMode(1);

    // Org. jedinica na stavci
    jrfCORG.setColumnName("CORG2");
    jrfCORG.setNavColumnName("CORG");
    jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jrfCORG.setVisCols(new int[]{0,1});
    jrfCORG.setColNames(new String[] {"NAZIV"});
    jrfCORG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZORG});
    jrfCORG.setDataSet(getDetailSet());
    
    jrfNAZORG.setColumnName("NAZIV");
    jrfNAZORG.setNavProperties(jrfCORG);
    jrfNAZORG.setSearchMode(1);



    // Lokacija na stavci
/*    jrfLOC.setRaDataSet(qdsLokacije);
    jrfLOC.setVisCols(new int[]{1,2});
    jrfLOC.setColNames(new String[] {"NAZLOKACIJE"});
    jrfLOC.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZLOC});
    jrfLOC.setDataSet(getMasterSet());
    jrfLOC.setColumnName("CLOKACIJE");
    jrfNAZLOC.setColumnName("NAZLOKACIJE");
    jrfNAZLOC.setNavProperties(jrfLOC);
    jrfNAZLOC.setSearchMode(1);
*/
    jtfDatum.setDataSet(getDetailSet());
    jtfDatum.setColumnName("DATPROMJENE");
    jtfDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDatum.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfDatum_focusGained(e);
      }
    });
    jtfDatum.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfDatum_focusLost(e);
      }
    });

    jtfDokument.setDataSet(getDetailSet());
    jtfDokument.setColumnName("DOKUMENT");

    jtfOpis.setDataSet(getDetailSet());
    jtfOpis.setColumnName("OPIS");

    jtfOsnovica.setDataSet(tds);
    jtfOsnovica.setHorizontalAlignment(SwingConstants.RIGHT);
    jtfOsnovica.setColumnName("OSNOVICA");

    jtfIspravak.setDataSet(tds);
    jtfIspravak.setHorizontalAlignment(SwingConstants.RIGHT);
    jtfIspravak.setColumnName("ISPRAVAK");

    jtfUIPRPOR.setDataSet(getDetailSet());
    jtfUIPRPOR.setHorizontalAlignment(SwingConstants.RIGHT);
    jtfUIPRPOR.setColumnName("UIPRPOR");

    // Checkbox AKTIVAN
    jcbAKTIV.setText("Aktivan");
    jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbAKTIV.setColumnName("AKTIV");
    jcbAKTIV.setDataSet(getMasterSet());
    jcbAKTIV.setSelectedDataValue("D");
    jcbAKTIV.setUnselectedDataValue("N");
  }
   //************* report
  static public QueryDataSet qDS = new QueryDataSet();
  XYLayout xYLayout3 = new XYLayout();
  JPanel jpSel = new JPanel();
  XYLayout xYLayout4 = new XYLayout();
  JLabel jlSifra1 = new JLabel();
  JlrNavField presCorg = new JlrNavField();
  JraButton jbCOrg = new JraButton();
  JLabel jlOrgjJed = new JLabel();
  JlrNavField presNazOrg = new JlrNavField();
  JLabel jlNaziv1 = new JLabel();
  private JLabel jlOPIS = new JLabel();
  private JLabel jlCPAR = new JLabel();
  private JLabel jlCORG = new JLabel();
  private JraTextField jtfOpis = new JraTextField();
  JraButton jbCPAR = new JraButton();
  JlrNavField jrfNAZPAR = new JlrNavField();
  JlrNavField jrfCPAR = new JlrNavField();
  JraButton jbCORG = new JraButton();
  JlrNavField jrfNAZORG = new JlrNavField();
  JlrNavField jrfCORG = new JlrNavField();
  private JLabel jLabel1 = new JLabel();
  JraTextField jtfUIPRPOR = new JraTextField();

//  private JraCheckBox jcbCORG = new JraCheckBox();
  private raComboBox rcbPripOrgJed = new raComboBox();

  private JLabel jLabel2 = new JLabel();
  private JlrNavField jrfNAZORG2 = new JlrNavField();
  private JraButton jbCORG2 = new JraButton();
  private JlrNavField jrfCORG2 = new JlrNavField(){
/*     public void keyF9Pressed() {
      newKeyF9Pressed();
    }*/
  };
  private JLabel jLabel4 = new JLabel();
  private raComboBox rcbAKTIV = new raComboBox();
  private JraButton jbCOBJEKT = new JraButton();
  private JlrNavField jrfCOBJEKT = new JlrNavField();
  private JlrNavField jrfNAZOBJEKT = new JlrNavField();
  private JLabel jLabel5 = new JLabel();
  private JlrNavField jrfIME = new JlrNavField();
  private JLabel jlCRADNIK = new JLabel();
  private JraButton jbCRADNIK = new JraButton();
  private JlrNavField jrfCRADNIK = new JlrNavField();
  private JlrNavField jrfPREZIME = new JlrNavField();
  private JraCheckBox jcbAKTIV = new JraCheckBox();
//  JlrNavField jrfLOC = new JlrNavField();
//  JlrNavField jrfNAZLOC = new JlrNavField();
//  JraButton jbLOC = new JraButton();
//  JLabel jlLOC = new JLabel();
  static public QueryDataSet getQuery() {
    qDS.open();

//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(qDS);
    return qDS;
  }

  public void prepareStavke(String qStr) {    
    Aus.refilter(qDS, qStr);
  }

  public void Funkcija_ispisa_detail() {
    prepareStavke(rdOSUtil.getUtil().getDetailStrIspis(getMasterSet().getString("INVBROJ"), getMasterSet().getString("CORG"),getMasterSet().getString("STATUS"), getMasterSet().getString("AKTIV"), mod));
    qDS.setSort(new SortDescriptor(new String[] {"RBR"}));
    super.Funkcija_ispisa_detail();
  }

  public void Funkcija_ispisa_master() {
    prepareStavke(rdOSUtil.getUtil().getMasterStrIspis(getMasterSet().getString("CORG"), getMasterSet().getString("STATUS"), mod));
    super.Funkcija_ispisa_master();
  }

  public void tabStateChangedDetail(int i) {
//    if (i==1) setOsn_Isp();
  }

  public int findYear(Timestamp ts, int condition) {
    if (condition == 0)
      return rdOSUtil.getUtil().StrToInt(ts.toString().substring(0,4))-1;
    else if (condition == 1)
      return rdOSUtil.getUtil().StrToInt(ts.toString().substring(0,4));
    return 0;
  }

  public Timestamp findLastYear(Timestamp ts) {
    int newYear = rdOSUtil.getUtil().StrToInt(ts.toString().substring(0,4))-1;
    StringBuffer sb = new StringBuffer(ts.toString());
    sb.replace(0,10, newYear+"-12-31");
    return ts.valueOf(sb.toString());
  }

  public String formatDatumStr(String initDate) {
    String newDate = initDate.substring(8,10)+"-"+initDate.substring(5,7)+"-"+initDate.substring(0,4);
    return newDate;
  }

  public void afterSetModeMaster(char oldMod, char newMod) {
    if (newMod != 'N') {
      copyRec = false;
      mRow = null;
    }
  }
  
  public void afterSetModeDetail(char old, char nev) {
        if (nev=='N' || nev=='I') {
      jrfCPROMJENE.setRaDataSet(myVrPr);
      jrfNAZPROMJENE.setRaDataSet(myVrPr);
    }
    else {
      jrfCPROMJENE.setRaDataSet(dm.getOS_Vrpromjene());
      jrfNAZPROMJENE.setRaDataSet(dm.getOS_Vrpromjene());
    }
        if (nev != 'N') dRow = null;
  }

  StorageDataSet stds = new StorageDataSet();

  private void jbInit() throws Exception {

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        presCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
//    this.setMasterDeleteMode(this.DELDETAIL);
    this.setValidateRange(false);
//    this.setVisibleColsMaster(new int[] {3,4,2,33,0,30});
//    this.setVisibleColsDetail(new int[] {14,3,4,5,6,7,8,20});
    this.setVisibleColsMaster(new int[] {2,3,4,30});
    this.setVisibleColsDetail(new int[] {14,3,4,5,6,7,8});
    raMaster.setkum_tak(true);
    raMaster.setstozbrojiti(new String[] {"OSNOVICA", "ISPRAVAK", "SALDO"});
    raMaster.setnaslovi(new String[] {"Osnovica", "Ispravak", "Saldo"});
    raMaster.getJpTableView().init_kum();
    raMaster.addOption(rnvCopyCurr, 1);
    this.set_kum_detail(true);
    this.stozbrojiti_detail(new String[] {"OSNDUGUJE", "OSNPOTRAZUJE", "ISPDUGUJE", "ISPPOTRAZUJE", "SALDO"});
    this.setnaslovi_detail(new String[] {"Osnovica duguje", "Osnovica potražuje", "Ispravak duguje", "Ispravak potražuje", "Saldo"});
    this.setNaslovDetail("Promjene osnovnog sredstva: ");



/*    raDetail.setkum_tak(true);
    raDetail.setstozbrojiti(new String[] {"SALDO"});
    raDetail.setnaslovi(new String[] {"Saldo"});
    raDetail.getJpTableView().init_kum();
*/
    jlNaziv1.setText("Naziv");
    presNazOrg.setColumnName("NAZIV");
    presNazOrg.setNavProperties(presCorg);
    jlOrgjJed.setText("Org. jedinica");
    jbCOrg.setText("...");
    presCorg.setColumnName("CORG2");
    presCorg.setNavColumnName("CORG");
    presCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    presCorg.setVisCols(new int[]{0,1});
    presCorg.setColNames(new String[] {"NAZIV"});
    presCorg.setTextFields(new javax.swing.text.JTextComponent[] {presNazOrg});
    presCorg.setSearchMode(0);
    jlSifra1.setText("Šifra");
    xYLayout4.setWidth(540);
    xYLayout4.setHeight(85);
    jpSel.setLayout(xYLayout4);
//    jpSel.setMinimumSize(new Dimension(522, 90));
//    jpSel.setPreferredSize(new Dimension(522, 90));
    /*jrfGodPr.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jrfGodPr_focusLost(e);
      }
    });*/
    jlOPIS.setText("Opis");
    jlCPAR.setText("Partner");
    jlCORG.setText("Org. jednica");
    jbCPAR.setText("...");
    jrfCPAR.setNavButton(jbCPAR);
    jbCORG.setText("...");
    jrfCORG.setNavButton(jbCORG);
    jLabel1.setText("Predporez");
//    jcbCORG.setHorizontalAlignment(SwingConstants.RIGHT);
//    jcbCORG.setHorizontalTextPosition(SwingConstants.LEFT);
//    jcbCORG.setText("Dohvat pripadaju?ih org. jedinica ");
    jLabel2.setText("Org. jedinica");
    jbCORG2.setText("...");
    jrfCORG2.setNavButton(jbCORG2);
//    jrfLOC.setNavButton(jbLOC);
//    jbLOC.setText("jButton1");
//    jlLOC.setText("Lokacija");
    jLabel4.setText("Aktivnost sredstava");
//    rcbAKTIV.setRaDataSet(getMasterSet());

    rcbAKTIV.setRaColumn("STATUS");
    rcbAKTIV.setRaItems(new String[][] {
      { "Aktivna","D"},
      { "Neaktivna", "N"},
      { "Sva", "S"}
     });

     stds.setColumns(new Column[] {dm.createStringColumn("OSTR",1)});
     stds.open();

     rcbPripOrgJed.setDataSet(stds);
     rcbPripOrgJed.setColumnName("OSTR");
     rcbPripOrgJed.setRaItems(new String[][] {
       { "Za cijelu org. strukturu", "S"},
       { "Za zadanu org. jedinicu", "Z"}
     });

    jbCOBJEKT.setText("jButton1");
    jrfCOBJEKT.setNavButton(jbCOBJEKT);
    jrfCOBJEKT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jrfCOBJEKT_focusGained(e);
      }
    });
    jLabel5.setText("Objekt");
    jlCRADNIK.setText("Radnik");
    jbCRADNIK.setText("jButton1");
    jrfCRADNIK.setNavButton(jbCRADNIK);
    jrfNAZOBJEKT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jrfNAZOBJEKT_focusGained(e);
      }
    });
    jrfNazLokacija.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jrfNazLokacija_focusGained(e);
      }
    });
    jpSel.add(jlSifra1,    new XYConstraints(150, 5, -1, -1));
    jpSel.add(presCorg,    new XYConstraints(150, 25, 100, -1));
    jpSel.add(jlOrgjJed,    new XYConstraints(15, 25, -1, -1));
    jpSel.add(presNazOrg,       new XYConstraints(255, 25, 255, -1));
    jpSel.add(jlNaziv1,    new XYConstraints(233, 5, -1, -1));
    jpSel.add(jbCOrg,     new XYConstraints(515, 25, 21, 21));
    jpSel.add(rcbPripOrgJed, new XYConstraints(255, 50, 255, -1)); //jcbCORG,             new XYConstraints(256, 48, 249, -1));
    jpSel.add(jLabel4, new XYConstraints(15, 50, -1, -1));
    jpSel.add(rcbAKTIV,  new XYConstraints(150, 50, 100, -1));
//    jbInitA();
  }

  public boolean checkInsertedDate(char modY) {
    if (modY=='S') {
      validYear = findYear(tsKontrola,0);
      int datasetYear = findYear(getDetailSet().getTimestamp("DATPROMJENE"),1);
      if (datasetYear > validYear || datasetYear < validYear)
        return false;
    }
    else if (modY=='N') {
      validYear = findYear(tsKontrola,1);
      int datasetYear = findYear(getDetailSet().getTimestamp("DATPROMJENE"),1);
      if (datasetYear < validYear || datasetYear > validYear)
        return false;
    }
    return true;
  }
  public void checkLikvidacija() {
    if (!getMasterSet().getTimestamp("DATLIKVIDACIJE").equals(nullDate)) {
      rcc.EnabDisabAll(jpDetailOS, false);
      int secCompare = getDetailSet().getBigDecimal("OSNPOTRAZUJE").compareTo(new BigDecimal(0));
      if (secCompare==1) {
        rcc.setLabelLaF(jtfDokument, true);
        rcc.setLabelLaF(jtfDatum, true);
      }
    }
  }
//  void newKeyF9Pressed() {
//    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
//    Frame frame = null;
//    Dialog dialog = null;
//    lookupFrame lookUp = null;
//    String[] result;
//
//    if (myKeyF9.isOpen()) myKeyF9.close();
//    String qStr = rdOSUtil.getUtil().getLokacijaStr(dm.getOrgstruktura().getString("CORG"));
//    myKeyF9.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
//    myKeyF9.setColumns(new Column[] {
//        (Column) dm.getOS_Lokacije().getColumn("CORG").clone(),
//        (Column) dm.getOS_Lokacije().getColumn("CLOKACIJE").clone(),
//        (Column) dm.getOS_Lokacije().getColumn("NAZLOKACIJE").clone()
//    });
//    myKeyF9.open();
//    myKeyF9.setTableName("NOVIDOHVATSAF9");
//    lookupData LD = lookupData.getlookupData();
//    result = LD.lookUp((java.awt.Frame)getFrameOwner(), myKeyF9, new int[] {0, 1, 2});
//    if (result != null) {
//      jrfLokacija.setText(result[1]);
//      jrfLokacija.forceFocLost();
//    }
//  }

  void jrfGodPr_focusLost(FocusEvent e) {
  }

  public boolean samoPregled;

  private boolean provjeraStatusaRada() {
    // Kontorola statusa rada
    if (rdOSUtil.getUtil().isPromjene(pres.getSelRow().getString("CORG2"))) {
      String prom = dm.getOS_Kontrola().getString("PROM");
      String reva = dm.getOS_Kontrola().getString("REVA");
      String amor = dm.getOS_Kontrola().getString("AMOR");
      samoPregled = false;

      if ((prom.equals("N") && (mod=='N' || mod=='L' || mod=='X' || mod=='Z' || mod=='O'))) {
        JOptionPane.showConfirmDialog(jpMasterOS,"Pogrešan mod rada !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if ((prom.equals("D") && (mod=='S' || mod=='Y'))) {
        JOptionPane.showConfirmDialog(jpMasterOS,"Pogrešan mod rada !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (reva.equals("D") && amor.equals("D") && !(mod == 'X' || mod == 'Y' || mod == 'Z')) {
        JOptionPane.showConfirmDialog(jpMasterOS, "Moguæ samo pregled!", "Upozorenje", JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        samoPregled = true;
//        return false;
      }
      return true;
    }
    else {
      JOptionPane.showConfirmDialog(jpMasterOS, "Ne postoji kontrolni slog za odabrano sredstvo !", "Greška", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

//  private boolean provjeraStatusaRada()
//  {
//    // Kontorola statusa rada
//    if (rdOSUtil.getUtil().isPromjene(pres.getSelRow().getString("CORG")))
//    {
//      String prom = dm.getOS_Kontrola().getString("PROM");
//      String reva = dm.getOS_Kontrola().getString("REVA");
//      String amor = dm.getOS_Kontrola().getString("AMOR");
//
//      if (( prom.equals("D") && mod=='S') || (prom.equals("N") && (mod=='N' || mod=='L')))
//      {
//         JOptionPane.showConfirmDialog(jpMasterOS,"Pogrešan mod rada !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//         return false;
//      }
//      if (reva.equals("D") && amor.equals("D"))
//      {
//           JOptionPane.showConfirmDialog(jpMasterOS,"Potrebno je prethodno formirati po?etno stanje !","Greška",
//                                        JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//          return false;
//      }
//      return true;
//    }
//    else
//    {
//          JOptionPane.showConfirmDialog(jpMasterOS,"Ne postoji kontrolni slog za odabrano sredstvo !","Greška",
//                                        JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//          return false;
//    }
//  }


  public boolean kontrolaDatUnosa() {
    System.out.println("Validate: "+validDate);
    if (!validDate) {
      System.out.println("return TRUE");
      return true;
    }
    java.sql.Timestamp datumunosa = hr.restart.util.Util.getUtil().clearTime(getDetailSet().getTimestamp("DATPROMJENE"));
    java.sql.Timestamp DSdat;

    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    for (int i=0; i<getDetailSet().rowCount();i++) {
      getDetailSet().getVariant("DATPROMJENE",i,v);
      DSdat = hr.restart.util.Util.getUtil().clearTime(v.getTimestamp());
      if (DSdat.after(datumunosa)) {
       return false;
      }
    }
    return true;
  }
  public void beforeShowMaster() {
    qdsLokacije=getLokacijeDS(getMasterSet().getString("CORG2"), getMasterSet().getString("COBJEKT"));
    jrfLokacija.setRaDataSet(qdsLokacije);
    qdsObjekti=getObjektiDS(getMasterSet().getString("CORG2"));
    jrfCOBJEKT.setRaDataSet(qdsObjekti);
  }

  public void beforeShowDetail() {
/*    getDetailSet().setSort(new SortDescriptor(new String[]{"RBR"}));
    getDetailSet().refresh();
    getDetailSet().last();*/
//    if (rdOSUtil.getUtil().checkLikvidacija(getMasterSet())) {
    if (getMasterSet().getString("AKTIV").equals("N")) {
      raDetail.disableAdd();
    }
    else {
      raDetail.enableAdd();
    }
    if (mod=='N' || mod=='S' || mod=='O' || mod=='L' || mod=='P') {
      this.setNaslovDetail("Promjene osnovnog sredstva: "+getMasterSet().getString("INVBROJ")+" "+getMasterSet().getString("NAZSREDSTVA"));
    }
    else {
      this.setNaslovDetail("Promjene sitnog inventara: "+getMasterSet().getString("INVBROJ")+" "+getMasterSet().getString("NAZSREDSTVA"));
    }
  }
  public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    if (raDetail.getMode()=='B') setOsn_Isp();
  }
  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    beforeShowMaster();
  }
  public void refilterDetailSet() {
    super.refilterDetailSet();
    setNaslovDetail(("Promjene osnovnog sredstva: "+getMasterSet().getString("INVBROJ")+" "+
      getMasterSet().getString("NAZSREDSTVA")));
  }

  /**
   * Insert stavke
   * @param stat char
   * - stat=L - likvidacija
   * - stat=O - promjena org jedinice
   */
  public void insertStavke(char stat) {
    String org = getDetailSet().getString("CORG2");
    String vrPr = getDetailSet().getString("CPROMJENE");
    String dokument = getDetailSet().getString("DOKUMENT");
    String opis = getDetailSet().getString("OPIS");
    Timestamp datLik = getDetailSet().getTimestamp("DATPROMJENE");

    System.out.println("Nesto: "+Integer.parseInt(dm.getOS_Kontrola().getString("GODINA")));
    BigDecimal bd=findAmortizacija(getMasterSet().getString("INVBROJ"), util.getTimestampValue(util.findFirstDayOfYear(Integer.parseInt(dm.getOS_Kontrola().getString("GODINA"))), util.NUM_FIRST));

    System.out.println("Privremena amortizacija: "+bd);

    bd=osUtil.getUtil().getAmor(datLik, getMasterSet().getBigDecimal("OSNDUGUJE").add(getMasterSet().getBigDecimal("OSNPOCETAK")), getMasterSet().getString("INVBROJ"), getMasterSet().getString("CORG"));

    System.out.println("Stvarna amortizacija: "+bd);

    if (bd.doubleValue()>getMasterSet().getBigDecimal("SALDO").doubleValue()) {
      bd=getMasterSet().getBigDecimal("SALDO");
    }
    BigDecimal rightAmor=util.negateValue(bd, getMasterSet().getBigDecimal("AMORTIZACIJA"));

    System.out.println("Konachna amortizacija: "+rightAmor);

    getMasterSet().setBigDecimal("AMORTIZACIJA", rightAmor);

    getDetailSet().cancel();
    getDetailSet().insertRow(false);
    getDetailSet().setString("AKTIV", "N");
    getDetailSet().setString("CORG", getMasterSet().getString("CORG"));
    getDetailSet().setString("CORG2", getMasterSet().getString("CORG2"));
    getDetailSet().setString("STATUS", getMasterSet().getString("STATUS"));
    getDetailSet().setString("INVBROJ", getMasterSet().getString("INVBROJ"));
    getDetailSet().setString("CPROMJENE", osUtil.getUtil().getSifraObrAmor());
    getDetailSet().setString("DOKUMENT", dokument);
    getDetailSet().setTimestamp("DATPROMJENE", datLik);
    getDetailSet().setInt("RBR", rdOSUtil.getUtil().getOSMaxRBR(getDetailSet().getString("INVBROJ"), getDetailSet().getString("CORG"), getDetailSet().getString("STATUS"))+1);
    getDetailSet().setBigDecimal("ISPPOTRAZUJE", rightAmor);
    getDetailSet().setBigDecimal("SALDO", util.negateValue(util.negateValue(getDetailSet().getBigDecimal("OSNDUGUJE"), getDetailSet().getBigDecimal("OSNPOTRAZUJE")), util.negateValue(getDetailSet().getBigDecimal("ISPPOTRAZUJE"), getDetailSet().getBigDecimal("ISPDUGUJE"))));
    if (stat=='O') {
      getDetailSet().setString("DOKUMENT", "ORG");
    } else {
      System.out.println("getDetailSet: "+getDetailSet().getInt("RBR"));

      getDetailSet().setString("DOKUMENT", "LIK");
    }
    getDetailSet().post();
//    hr.restart.os.osUtil.getUtil().afterUpdateOS(util.nul, util.nul, 'N');

    getDetailSet().insertRow(false);
    getDetailSet().setString("CORG", getMasterSet().getString("CORG"));
    getDetailSet().setString("CORG2", getMasterSet().getString("CORG2"));
    getDetailSet().setString("STATUS", getMasterSet().getString("STATUS"));
    getDetailSet().setString("INVBROJ", getMasterSet().getString("INVBROJ"));
    getDetailSet().setString("OPIS", opis);
    getDetailSet().setString("CPROMJENE", vrPr);
    getDetailSet().setString("DOKUMENT", dokument);
    getDetailSet().setTimestamp("DATPROMJENE", datLik);
    getDetailSet().setInt("RBR", rdOSUtil.getUtil().getOSMaxRBR(getDetailSet().getString("INVBROJ"), getDetailSet().getString("CORG"), getDetailSet().getString("STATUS"))+2);
//    if (stat=='O') {
//      getDetailSet().setBigDecimal("OSNDUGUJE", (getMasterSet().getBigDecimal("OSNDUGUJE").add(getMasterSet().getBigDecimal("OSNPOCETAK"))).negate());
//      getDetailSet().setBigDecimal("ISPPOTRAZUJE", (getMasterSet().getBigDecimal("ISPPOTRAZUJE").add(getMasterSet().getBigDecimal("ISPPOCETAK")).add(bd)).negate());
//    }
//    else {
      getDetailSet().setBigDecimal("OSNPOTRAZUJE", getMasterSet().getBigDecimal("OSNDUGUJE").add(getMasterSet().getBigDecimal("OSNPOCETAK")));
      getDetailSet().setBigDecimal("ISPDUGUJE", getMasterSet().getBigDecimal("ISPPOTRAZUJE").add(getMasterSet().getBigDecimal("ISPPOCETAK").add(bd)));
//    }
    BigDecimal osndug = getDetailSet().getBigDecimal("OSNPOTRAZUJE");
    BigDecimal isppot = getDetailSet().getBigDecimal("ISPDUGUJE");
    getDetailSet().setBigDecimal("SALDO", util.negateValue(util.negateValue(getDetailSet().getBigDecimal("OSNDUGUJE"), getDetailSet().getBigDecimal("OSNPOTRAZUJE")), util.negateValue(getDetailSet().getBigDecimal("ISPPOTRAZUJE"), getDetailSet().getBigDecimal("ISPDUGUJE"))));
    if (stat=='O') {
      getDetailSet().setString("DOKUMENT", "ORG");
    }
/*    else {
      System.out.println("getDetailSet: "+getDetailSet().getInt("RBR"));
      getDetailSet().setString("DOKUMENT", "LIK");
    }*/
    getDetailSet().post();

    if (stat=='O') {
      hr.restart.os.osUtil.getUtil().afterUpdateOS(util.nul, util.nul, 'L');
      getDetailSet().insertRow(false);
      getDetailSet().setString("CORG", getMasterSet().getString("CORG"));
      getDetailSet().setString("CORG2", org);
      getDetailSet().setInt("CPAR", getMasterSet().getInt("CPAR"));
      getDetailSet().setString("STATUS", getMasterSet().getString("STATUS"));
      getDetailSet().setString("INVBROJ", getMasterSet().getString("INVBROJ"));
      getDetailSet().setString("CPROMJENE", vrPr);
      getDetailSet().setString("OPIS", opis);
      getDetailSet().setString("DOKUMENT", dokument);
      getDetailSet().setTimestamp("DATPROMJENE", datLik);
      getDetailSet().setInt("RBR", rdOSUtil.getUtil().getOSMaxRBR(getDetailSet().getString("INVBROJ"), getDetailSet().getString("CORG"), getDetailSet().getString("STATUS"))+3);
      getDetailSet().setBigDecimal("OSNDUGUJE", osndug);
      getDetailSet().setBigDecimal("ISPPOTRAZUJE", isppot);
      getDetailSet().setBigDecimal("SALDO", util.negateValue(util.negateValue(getDetailSet().getBigDecimal("OSNDUGUJE"), getDetailSet().getBigDecimal("OSNPOTRAZUJE")), util.negateValue(getDetailSet().getBigDecimal("ISPPOTRAZUJE"), getDetailSet().getBigDecimal("ISPDUGUJE"))));

      String naziv=getMasterSet().getString("NAZSREDSTVA");
      String lokac=getMasterSet().getString("CLOKACIJE");
      String artik=getMasterSet().getString("CARTIKLA");
      int partn=getMasterSet().getInt("CPAR");
      String konto=getMasterSet().getString("BROJKONTA");
      String grupa=getMasterSet().getString("CGRUPE");
      String skupi=getMasterSet().getString("CSKUPINE");
      String godin=getMasterSet().getString("GODPROIZ");
      String porij=getMasterSet().getString("PORIJEKLO");
      Timestamp dat=getMasterSet().getTimestamp("DATNABAVE");

      getMasterSet().setTimestamp("DATLIKVIDACIJE", hr.restart.util.Util.getUtil().clearTime(vl.getToday()));

      getMasterSet().insertRow(true);
      getMasterSet().setString("OLDCORG", getDetailSet().getString("CORG2"));
      getMasterSet().setString("CORG", getDetailSet().getString("CORG"));
      getMasterSet().setString("CORG2", org);
      getMasterSet().setString("STATUS", "A");
      getMasterSet().setString("INVBROJ", getDetailSet().getString("INVBROJ"));
      getMasterSet().setString("NAZSREDSTVA", naziv);
      getMasterSet().setString("CLOKACIJE", lokac);
      getMasterSet().setString("CARTIKLA", artik);
      getMasterSet().setInt("CPAR", partn);
      getMasterSet().setString("BROJKONTA", konto);
      getMasterSet().setString("CGRUPE", grupa);
      getMasterSet().setString("CSKUPINE", skupi);
      getMasterSet().setString("GODPROIZ", godin);
      getMasterSet().setString("PORIJEKLO", porij);
      getMasterSet().setTimestamp("DATNABAVE", dat);
      getMasterSet().setTimestamp("DATAKTIVIRANJA", hr.restart.util.Util.getUtil().clearTime(vl.getToday()));
    }
    else {
      getDetailSet().saveChanges();
    }
  }
  public com.borland.dx.sql.dataset.QueryDataSet getLokacijeDS(String corg, String obj) {
    com.borland.dx.sql.dataset.QueryDataSet aa= hr.restart.util.Util.getNewQueryDataSet("select * from os_lokacije where corg='"+corg+"' and cobjekt='"+obj+"'");
    return aa;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getObjektiDS(String corg) {
    com.borland.dx.sql.dataset.QueryDataSet aa= hr.restart.util.Util.getNewQueryDataSet("select * from os_objekt where corg='"+corg+"'");
    return aa;
  }

  void jrfCOBJEKT_focusGained(FocusEvent e) {
    qdsObjekti=getObjektiDS(getMasterSet().getString("CORG2"));
    jrfCOBJEKT.setRaDataSet(qdsObjekti);
  }

  void jrfNAZOBJEKT_focusGained(FocusEvent e) {
    qdsObjekti=getObjektiDS(getMasterSet().getString("CORG2"));
    jrfCOBJEKT.setRaDataSet(qdsObjekti);
  }

  void jrfLokacija_focusGained(FocusEvent e) {
    qdsLokacije=getLokacijeDS(getMasterSet().getString("CORG2"), getMasterSet().getString("COBJEKT"));
    jrfLokacija.setRaDataSet(qdsLokacije);
  }

  void jrfNazLokacija_focusGained(FocusEvent e) {
    qdsLokacije=getLokacijeDS(getMasterSet().getString("CORG2"), getMasterSet().getString("COBJEKT"));
    jrfLokacija.setRaDataSet(qdsLokacije);
  }
  private BigDecimal findAmortizacija(String ib, String dat) {
    BigDecimal amor = new java.math.BigDecimal(0);
    QueryDataSet qdsOld4Amor = hr.restart.util.Util.getNewQueryDataSet(sjQuerys.getOldOS4Amor(ib));
    qdsOld4Amor.open();
    qdsOld4Amor.first();
    do {
      amor=amor.add(osUtil.getUtil().getAmor(getDetailSet().getTimestamp("DATPROMJENE"), getMasterSet().getBigDecimal("OSNDUGUJE").add(getMasterSet().getBigDecimal("OSNPOCETAK")), getMasterSet().getString("INVBROJ"), getMasterSet().getString("CORG")));
    } while (qdsOld4Amor.next());
    QueryDataSet qdsNew4Amor = hr.restart.util.Util.getNewQueryDataSet(sjQuerys.getNewOS4Amor(ib, dat));
    qdsNew4Amor.open();
    qdsNew4Amor.first();
    do {
      amor=amor.add(osUtil.getUtil().getAmor(getDetailSet().getTimestamp("DATPROMJENE"), getMasterSet().getBigDecimal("OSNDUGUJE").add(getMasterSet().getBigDecimal("OSNPOCETAK")), getMasterSet().getString("INVBROJ"), getMasterSet().getString("CORG")));
    } while (qdsNew4Amor.next());

    System.out.println("Izlaz: "+ib+", "+dat);
    return new java.math.BigDecimal(0);
  }
  void jtfDatum_focusGained(FocusEvent e) {
    tmp=jtfDatum.getText();
    System.out.println("Validate on focgain: "+validDate);
  }
  void jtfDatum_focusLost(FocusEvent e) {
    if (tmp.equals(jtfDatum.getText().toString())) {
      validDate=false;
    }
    else {
      validDate=true;
    }
    System.out.println("Validate on foclost: "+validDate);
  }
}

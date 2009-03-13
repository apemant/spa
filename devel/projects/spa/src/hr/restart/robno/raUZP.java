/****license*****************************************************************
**   file: raUZP.java
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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.robno.frmNarDob.StatusColorModifier;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raColors;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.MasterDetailChooser;
import hr.restart.util.VarStr;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raTransaction;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jb.util.TriStateProperty;
import com.borland.jbcl.layout.XYConstraints;


public class raUZP extends raIzlazTemplate {

  QueryDataSet zahStavkaNew = null;
  QueryDataSet zahStavkaOld = null;
  
  JraButton trans;
  
  private String[] vkey = {"CSKL", "VRDOK", "GOD", "BRDOK", "RBSID"};
  
  public void initialiser() {
      what_kind_of_dokument = "UZP";
      bPonudaZaKupca = false;
  }

  public void MyaddIspisMaster() {

      raMaster.getRepRunner().addReport("hr.restart.robno.repUpitPonuda",
              "hr.restart.robno.repIzlazni", "UpitPonuda", "Upit za ponudu");
  }

  public void MyaddIspisDetail() {
    raDetail.getRepRunner().addReport("hr.restart.robno.repUpitPonuda",
        "hr.restart.robno.repIzlazni", "UpitPonuda", "Upit za ponudu");
  }



  public void ConfigViewOnTable() {
      this.setVisibleColsMaster(new int[] { 4, 5, 6, 44, 34 }); 
//Requested
//by Mladen
//(Siniša)
      setVisibleColsDetail(new int[] { 4,
          Aut.getAut().getCARTdependable(5, 6, 7), 8, 9, 10 });
  }

  public raUZP() {
      isOJ=true;
//    isMaloprodajnaKalkulacija = true;
      setPreSel((jpPreselectDoc) presUZP.getPres());
      addButtons(false, false);
//    raMaster.addOption(rnvDellAll, 3);
      raDetail.addOption(rnvDellAllStav, 3);
      //raDetail.addOption(rnvKartica, 5, false);
      master_titel = "Upiti za ponude";
      detail_titel_mno = "Stavke upita za ponudu";
      detail_titel_jed = "Stavka upita za ponudu";
      QueryDataSet det = stdoki.getDataModule().getFilteredDataSet("1=0");
      zamraciDetail(det);
      setMasterSet(doki.getDataModule().getFilteredDataSet("1=0"));
      setDetailSet(det);
      getMasterSet().open();
      getDetailSet().open();
      
      rCD.setisNeeded(false);
      MP.BindComp();
      DP.BindComp();
      DP.rpcart.addSkladField(hr.restart.robno.Util.getSkladFromCorg());
      DP.rpcart.enableNameChange(true);
      DP.resizeDP();
      this.setVisibleColsMaster(new int[] { 4, 5, 9 });
  }
  
  public void zamraciDetail(DataSet ds) {

    ds.getColumn("CRADNAL").setVisible(TriStateProperty.FALSE);
    ds.getColumn("UPRAB").setVisible(TriStateProperty.FALSE);
    ds.getColumn("UIRAB").setVisible(TriStateProperty.FALSE);
    ds.getColumn("UPZT").setVisible(TriStateProperty.FALSE);
    ds.getColumn("UIZT").setVisible(TriStateProperty.FALSE);
    ds.getColumn("FC").setVisible(TriStateProperty.FALSE);
    ds.getColumn("INETO").setVisible(TriStateProperty.FALSE);
    ds.getColumn("FVC").setVisible(TriStateProperty.FALSE);
    ds.getColumn("IPRODBP").setVisible(TriStateProperty.FALSE);
    ds.getColumn("POR1").setVisible(TriStateProperty.FALSE);
    ds.getColumn("POR2").setVisible(TriStateProperty.FALSE);
    ds.getColumn("POR3").setVisible(TriStateProperty.FALSE);
    ds.getColumn("FMC").setVisible(TriStateProperty.FALSE);
    ds.getColumn("IPRODSP").setVisible(TriStateProperty.FALSE);
    ds.getColumn("NC").setVisible(TriStateProperty.FALSE);
    ds.getColumn("INAB").setVisible(TriStateProperty.FALSE);
    ds.getColumn("IMAR").setVisible(TriStateProperty.FALSE);
    ds.getColumn("VC").setVisible(TriStateProperty.FALSE);
    ds.getColumn("IBP").setVisible(TriStateProperty.FALSE);
    ds.getColumn("IPOR").setVisible(TriStateProperty.FALSE);
    ds.getColumn("MC").setVisible(TriStateProperty.FALSE);
    ds.getColumn("ISP").setVisible(TriStateProperty.FALSE);
    ds.getColumn("IRAZ").setVisible(TriStateProperty.FALSE);
    ds.getColumn("BRPRI").setVisible(TriStateProperty.FALSE);
    ds.getColumn("RBRPRI").setVisible(TriStateProperty.FALSE);
    ds.getColumn("PPOR1").setVisible(TriStateProperty.FALSE);
    ds.getColumn("PPOR2").setVisible(TriStateProperty.FALSE);
    ds.getColumn("PPOR3").setVisible(TriStateProperty.FALSE);
    ds.getColumn("CARTNOR").setVisible(TriStateProperty.FALSE);
    ds.getColumn("FMCPRP").setVisible(TriStateProperty.FALSE);
    ds.getColumn("REZKOL").setVisible(TriStateProperty.FALSE);
    ds.getColumn("VEZA").setVisible(TriStateProperty.FALSE);
    ds.getColumn("ID_STAVKA").setVisible(TriStateProperty.FALSE);
    ds.getColumn("ZC").setVisible(TriStateProperty.FALSE);
    ds.getColumn("KOL1").setVisible(TriStateProperty.FALSE);
    ds.getColumn("KOL2").setVisible(TriStateProperty.FALSE);
  }
  
  public void EntryDetail(char mode) {
    super.EntryDetail(mode);
    DP.rcc.setLabelLaF(trans, mode != 'B');
    zahStavkaNew = zahStavkaOld = null;
  }
  
  public boolean ValidacijaStanje() {
      return true;
  }

  public boolean DodatnaValidacijaDetail() {
      if (val.isEmpty(DP.jtfKOL))
          return false;
      
      if (zahStavkaNew != null) {
        if (checkDohChanged()) return false;
        if (getDetailSet().getBigDecimal("KOL").compareTo(
            zahStavkaNew.getBigDecimal("KOL")) != 0) {
          if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(DP,
              "Kolièina se razlikuje od kolièine na trebovanju!" +
              " Nastaviti ipak?", "Upozorenje", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.WARNING_MESSAGE)) return false;
        }
        if (!zahStavkaNew.getString("STATUS").equalsIgnoreCase("N")) {
          JOptionPane.showMessageDialog(DP, 
              "Odabrana stavka zahtjevnice je veæ naruèena!",
              "Prijenos", JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
      findOldStavka(raDetail.getMode());
      if (zahStavkaNew == null && zahStavkaOld != null) {
        if (getDetailSet().getBigDecimal("KOL").compareTo(
            zahStavkaOld.getBigDecimal("KOL")) != 0) {
          if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(DP,
              "Kolièina se razlikuje od kolièine na trebovanju!" +
              " Nastaviti ipak?", "Upozorenje", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.WARNING_MESSAGE)) return false;
        }
      }
      if (checkDohChanged()) return false;
      if (zahStavkaNew != null) 
        getDetailSet().setString("VEZA", zahStavkaNew.getString("ID_STAVKA"));
      else if (zahStavkaOld != null)
        getDetailSet().setString("VEZA", zahStavkaOld.getString("ID_STAVKA"));
      
      return true;
  }
  
  private void findOldStavka(char mode) {
    if (mode == 'I' || mode == 'B') {
      VarStr veza = new VarStr(getDetailSet().getString("VEZA"));
      if (veza.length() > 0 && veza.countOccurences('-') >= 4) {
        zahStavkaOld = stdoki.getDataModule().getTempSet(
            Condition.whereAllEqual(vkey, veza.splitTrimmed('-')));
        zahStavkaOld.open();
        if (zahStavkaOld.rowCount() != 1) {
          new Throwable("Greška kod prijenosa ROT->PRK").printStackTrace();
          zahStavkaOld = null;
        }
      }
    }
  }
  
  private boolean checkDohChanged() {
    if (zahStavkaNew == null) return false;
    zahStavkaNew.refresh();
    if (zahStavkaNew.rowCount() == 0 || 
        zahStavkaNew.getInt("CART") != getDetailSet().getInt("CART")) {
      JOptionPane.showMessageDialog(DP, 
          "Odabrana stavka trebovanja je u meðuvremenu promijenjena!",
          "Prijenos", JOptionPane.ERROR_MESSAGE);
      return true;
    }
    return false;
  }
  
  public boolean LocalValidacijaMaster() {
      return true;
  }

  public void RestPanelSetup() {
      DP.addRestOnlyKol();
      trans = new JraButton();
      trans.setIcon(raImages.getImageIcon(raImages.IMGSENDMAIL));
      trans.setAutomaticFocusLost(true);
      trans.setToolTipText("Dohvat stavki trebovanja");
      trans.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          zahDohvat();
        }    
      });
      DP.jpDetailCenter.add(trans, new XYConstraints(613, 17, 21, 21));
  }
  
  public void cskl2csklart(){}

  public void dodajRezervaciju() {
    //
  }   
  
  
  protected void zahDohvat() {
    String art = DP.rpcart.getCART().trim();
    boolean haveArt = art.length() > 0;
    String cskl = DP.rpcart.getCskl().trim();
    boolean haveCskl = cskl.length() > 0;
      
    boolean fullZah = frmParam.getParam("robno", "dohvatZAH", "D", 
          "Prikazati sve stavke trebovanja kod dohvata na narudžbenici (D,N)", true).
            equalsIgnoreCase("D");
    
    String dod = "";
    if (haveArt) dod = " AND stdoki.cart=" + art;
    else if (!fullZah) dod = " AND stdoki.status='N'";
    if (haveCskl) dod = dod + " AND stdoki.cskl='" + cskl + "'";
    
    String[] mcols = {"CUSER", "CORG", "CSKL", "VRDOK", "GOD", 
          "BRDOK", "DATDOK", "DATDOSP", "OPIS", "BRDOKIZ"};
      
    String[] dcols = {"RBR", "CART", "CART1", "BC", "NAZART", 
          "JM", "KOL", "KOL1", "KOL2", "STATUS"};
    
    String q = 
        "SELECT doki.cuser, doki.corg, doki.cskl, doki.vrdok, doki.god, " +
        "doki.brdok, doki.datdok, doki.datdosp, doki.opis, doki.brdokiz, " +
        "stdoki.rbr, stdoki.cart, stdoki.cart1, stdoki.bc, " +
        "stdoki.nazart, stdoki.jm, stdoki.kol, stdoki.kol1, " +
        "stdoki.kol2, stdoki.status FROM doki,stdoki WHERE " +
        Util.getUtil().getDoc("doki", "stdoki") + 
        " AND doki.god='" + getMasterSet().getString("GOD") +
        "' AND doki.vrdok='TRE' AND doki.statira='N'" + dod;
    System.out.println(q);
    
    QueryDataSet doh = new QueryDataSet();
      Aus.setFilter(doh, q);
      List cols = new ArrayList();
      for (int i = 0; i < mcols.length; i++)
        cols.add(doki.getDataModule().getColumn(mcols[i]).clone());
      for (int i = 0; i < dcols.length; i++)
        cols.add(stdoki.getDataModule().getColumn(dcols[i]).clone());
      doh.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+
          MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
      doh.setColumns((Column[]) cols.toArray(new Column[cols.size()]));
      doh.getColumn("KOL1").setCaption("Naruèeno");
      doh.getColumn("KOL2").setCaption("Isporuèeno");
      doh.open();
      if (doh.rowCount() == 0) {
        JOptionPane.showMessageDialog(raDetail.getWindow(), 
            "Nema nenaruèenih stavaka trebovanja u ovoj godini!",
            "Prijenos", JOptionPane.ERROR_MESSAGE);
        return;
      }
      doh.setSort(new SortDescriptor(new String[] {
          "DATDOK", "CSKL", "BRDOK", "RBR"}));
      
      MasterDetailChooser mdc = new MasterDetailChooser(doh, 
          haveArt ? "ndo-dohvat-zah-art" : "zah-dohvat-zah", 
              mcols, dcols, new int[] {2, 4, 5, 6, 10,
              Aut.getAut().getCARTdependable(11, 12, 13),14,15,16});
      mdc.addModifier(new StatusColorModifier(mdc.isTurboTable()));
      
      if (mdc.show(raDetail.getWindow(), 
          "Dohvat stavki trebovanja za upit za ponudu")) {
        if (!doh.getString("STATUS").equals("N")) {
          JOptionPane.showMessageDialog(raDetail.getWindow(), 
              "Odabrana stavka je veæ od prije naruèena!",
              "Prijenos", JOptionPane.ERROR_MESSAGE);
          return;
        }
        zahStavkaNew = stdoki.getDataModule().getTempSet(
            Condition.whereAllEqual(Util.dkey, doh));
        zahStavkaNew.open();
        if (DP.rpcart.getCskl().length() == 0)
          DP.rpcart.setCskl(zahStavkaNew.getString("CSKL"));
        if (!haveArt) DP.rpcart.setCART(doh.getInt("CART"));
        Aus.set(getDetailSet(), "KOL", zahStavkaNew);
        Aus.sub(getDetailSet(), "KOL", zahStavkaNew, "KOL1");
        getDetailSet().setString("NAZART", zahStavkaNew.getString("NAZART"));
      }
  }
  
  static class StatusColorModifier extends raTableModifier {
      Variant shared = new Variant();
      HashSet dset = new HashSet(Arrays.asList(new String[]
         {"RBR", "CART", "CART1", "BC", "NAZART", 
          "JM", "KOL", "KOL1", "KOL2", "FVC", "FMC", "STATUS"}));
      
      boolean turboTable;
      
      public StatusColorModifier(boolean turboTable) {
        this.turboTable = turboTable;
      }

      public boolean doModify() {
        if (getTable() instanceof JraTable2) {
          JraTable2 tab = (JraTable2) getTable();
          if (tab.getDataSet().getRowCount() > 0 && 
              tab.getDataSet().hasColumn("STATUS") != null) {
            if (turboTable && !dset.contains(tab.getDataSetColumn(getColumn()).
                getColumnName().toUpperCase())) return false;
            tab.getDataSet().getVariant("STATUS", this.getRow(), shared);
            return !shared.getString().equals("N");
          }
        }
        return false;
      }
      
      public void modify() {
        JComponent jRenderComp = (JComponent) renderComponent;
        if (isSelected()) {
          jRenderComp.setBackground(raColors.green);
          jRenderComp.setForeground(Color.black);
        } else {
          //jRenderComp.setBackground(getTable().getBackground());
          jRenderComp.setForeground(Color.green.darker().darker());
        }
      }
    }
}
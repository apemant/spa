/****license*****************************************************************
**   file: raPON.java
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

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.JOptionPane;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Partneri;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.raUser;
import hr.restart.swing.XYPanel;
import hr.restart.swing.raInputDialog;
import hr.restart.util.Aus;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;

public class raKOD extends raIzlazTemplate {
  
  raNavAction rnvCreate = new raNavAction("Automatsko generiranje",
      raImages.IMGIMPORT, java.awt.event.KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      generate();
    }
  };

    public void initialiser() {
        what_kind_of_dokument = "KOD";
    }

    public void MyaddIspisMaster() {

        raMaster.getRepRunner().addReport("hr.restart.robno.repOdjava", "hr.restart.robno.repOdjava", "Odjava", "Odjava 1 red");
        
        Aus.dumpModel(raMaster.getRepRunner().getReport("hr.restart.robno.repOdjava").getJavaTemplate().getUnmodifiedReportTemplate(), 0);
    }

    public void MyaddIspisDetail() {

       raDetail.getRepRunner().addReport("hr.restart.robno.repOdjava", "hr.restart.robno.repOdjava", "Odjava", "Odjava 1 red");
    }
    
    
    public raKOD() {
        setPreSel(presKOD.getPres());
        //addButtons(true, true);
        raMaster.addOption(rnvCreate, 5, false);
        raDetail.addOption(rnvDellAllStav, 3);
        raDetail.addOption(rnvKartica, 5, false);
        master_titel = "Komisijske odjave";
        detail_titel_mno = "Stavke odjave";
        detail_titel_jed = "Stavka odjave";
        setMasterSet(dm.getZagKod());
        setDetailSet(dm.getStKod());
        MP.BindComp();
        DP.BindComp();
        rCD.setisNeeded(false);
        DP.resizeDP();
    }
    
    public void stozbroiti(){
      stozbrojiti_detail(new String[] { "INAB", "IMAR", "IPOR", "IRAZ" });
    }

    public void RestPanelSetup() {
        DP.addRestOTP();
    }
    
    public void SetFokusMaster(char mode) {
      if (mode == 'N') {
        pressel.copySelValues();
        SetFocusNovi();
      } 
      findBRDOK();
    }
    
    public void Kalkulacija(String how) {
      lc.TransferFromDB2Class(AST.gettrenSTANJE(), rKD.stanje);
      if (Artikli.loc(getDetailSet())) rKD.setupArt(Artikli.get());
      rKD.setVrzal(getMasterSet().getString("CSKL"));
      rKD.pureKalkSkladPart();
      lc.TransferFromClass2DB(getDetailSet(), rKD.stavka);
    }
        
    public boolean ValidacijaStanje() {
        boolean isStanje = AST.findStanjeFor(getDetailSet(), isOJ);
        if (!isStanje) {
          JOptionPane.showMessageDialog(raDetail.getWindow(),
              "Ne postoji zapis na stanju za taj artikl !",
              "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
          return false;
        }
        lc.TransferFromDB2Class(AST.gettrenSTANJE(), rKD.stanje);
        rKD.setVrzal(getMasterSet().getString("CSKL"));
        rKD.pureKalkSkladPart();
        rKD.kalkResetFinancPart();
        lc.TransferFromClass2DB(getDetailSet(), rKD.stavka);
        return true;
    }

    public boolean DodatnaValidacijaDetail() {
        if (val.isEmpty(DP.jtfKOL))
            return false;
        return true;
    }

    public boolean LocalValidacijaMaster() {
        if (getMasterSet().isNull("CPAR"))
          getMasterSet().setString("TNAZPAR", MP.panelBasic.jrfNAZPAR.getText());
        return true;
    }

    public boolean ValidacijaMasterExtend() {
        return true;
    }
    
    public void brisiVezu() {
      if (idStavke != null && idStavke.trim().length() > 0) {
        QueryDataSet qds = stdoki.getDataModule().openTempSet(
            Condition.equal("ODJ", idStavke));
        for (qds.first(); qds.inBounds(); qds.next()) 
            qds.setAssignedNull("ODJ");
        
        if (qds.getRowCount() > 0)
            raTransaction.saveChanges(qds);
      }
    }
    
    public void ConfigViewOnTable() {
      this.setVisibleColsMaster(new int[] { 4, 5, 6, 31, 32, 34 }); // Requested
//    // by
//    // Mladen
//    // (Siniša)
//    this
//            .setVisibleColsDetail(new int[] { 4,
//                    Aut.getAut().getCARTdependable(5, 6, 7), 8, 11, 16, 12,
//                    19, 24 });
      setVisibleColsDetail(new int[] { 4,
              Aut.getAut().getCARTdependable(5, 6, 7), 8, 9, 11, 33, 34 });
      
  }
    
    StorageDataSet gen = Aus.createSet("CPAR @DATDO @DATDOK");
    
    void generate() {
      if (gen.rowCount() == 0) {
        gen.insertRow(false);
        gen.setTimestamp("DATDO", this.getPreSelect().getSelRow().getTimestamp("DATDOK-to"));
        gen.setTimestamp("DATDOK", this.getPreSelect().getSelRow().getTimestamp("DATDOK-to"));
      }
      
      XYPanel pan = new XYPanel(gen);
      pan.i_wid = 100;
      pan.label("Partner").nav("CPAR", dm.getPartneri(), "NAZPAR").nl();
      pan.label("Do dana").text("DATDO").label("Datum dokumenta", 200).text("DATDOK").nl().expand();
      raInputDialog dlg = new raInputDialog();
      dlg.setParams("Automatsko kreiranje odjava", pan, pan);
      if (dlg.show(raMaster.getWindow())) {
        raProcess.runChild(raMaster.getWindow(), new Runnable() {
          public void run() {
            generateImpl();
          }
        });
        if (raProcess.isFailed()) 
          raProcess.report("Greška prilikom kreiranja odjave!");
        else if (raProcess.isCompleted()) {
          raProcess.report("Kreiranje odjava završeno.");
          getMasterSet().refresh();
          raMaster.getJpTableView().fireTableDataChanged();
          raMaster.jeprazno();
        }
      }
    }
    
    void generateImpl() {
      QueryDataSet ds = Aus.q("SELECT stdoki.cskl, stdoki.vrdok, stdoki.god, stdoki.brdok, " +
      		"stdoki.rbr, stdoki.cart, stdoki.kol, stdoki.nc, stdoki.inab, stdoki.vc, " +
      		"stdoki.ibp, stdoki.imar, stdoki.mc, stdoki.ipor, stdoki.isp, stdoki.zc, stdoki.iraz, stdoki.odj FROM doki,stdoki WHERE " +
      		Aus.join("doki", "stdoki", Util.mkey) + " AND " +
      		Condition.equal("CSKL", getPreSelect().getSelRow()).
      		and(Condition.in("VRDOK", "OTP IZD ROT GOT")).
      		and(Condition.till("DATDOK", gen.getTimestamp("DATDO"))).qualified("doki").
      		and(Condition.emptyString("ODJ").orNull().qualified("stdoki")) + " ORDER BY stdoki.cart");
      System.out.println(ds.getOriginalQueryString());
      ds.getColumn("CSKL").setRowId(true);
      ds.getColumn("VRDOK").setRowId(true);
      ds.getColumn("GOD").setRowId(true);
      ds.getColumn("BRDOK").setRowId(true);
      ds.getColumn("RBR").setRowId(true);
      
      raProcess.installErrorTrace(ds, new String[] {"VRDOK", "GOD", "BRDOK", "RBR", "CART"}, "Popis grešaka");
      
      QueryDataSet rzag = doki.getDataModule().getTempSet("1=0");
      rzag.open();
      
      QueryDataSet rst = stdoki.getDataModule().getTempSet("1=0");
      rst.open();
      
      int seq = 0;
      int oldcart = -99;
      int zags = 0, currst = 0;
      HashMap stavs = new HashMap();
      String[] acols = {"CART", "CART1", "BC", "NAZART", "JM"};
      
      for (ds.first(); ds.inBounds(); ds.next()) {
        if (ds.getInt("CART") != oldcart) {
          if (!Artikli.loc(ds)) {
            raProcess.addError("Nepostojeæi artikl", ds);
            continue;
          }
          if (Artikli.get().isNull("CPAR")) {
            raProcess.addError("Artikl nema upisanog dobavljaèa", ds);
            continue;
          }
          if (!Partneri.loc(Artikli.get().getInt("CPAR"))) {
            raProcess.addError("Nepostojeæi dobavljaè za artikl", ds);
            continue;
          }
          if (rst.rowCount() > 0 && rst.getBigDecimal("KOL").signum() > 0) {
            Aus.div(rst, "NC", "INAB", "KOL");
            Aus.div(rst, "VC", "IBP", "KOL");
            Aus.div(rst, "MC", "ISP", "KOL");
            Aus.div(rst, "ZC", "IRAZ", "KOL");
          }
          
          oldcart = ds.getInt("CART");
          if (!lD.raLocate(rzag, "CPAR", Partneri.get())) {
            rzag.insertRow(false);
            rzag.setString("CSKL", getPreSelect().getSelRow().getString("CSKL"));
            rzag.setString("VRDOK", "KOD");
            rzag.setString("CUSER", raUser.getInstance().getUser());
            rzag.setTimestamp("DATDOK", gen.getTimestamp("DATDOK"));
            rzag.setTimestamp("DVO", gen.getTimestamp("DATDO"));
            rzag.setTimestamp("DATDOSP", gen.getTimestamp("DATDOK"));
            rzag.setInt("CPAR", Partneri.get().getInt("CPAR"));
            Util.getUtil().getBrojDokumenta(rzag, false);
            if (zags == 0) seq = rzag.getInt("BRDOK");
            else if (seq != rzag.getInt("BRDOK")) {
              raProcess.addError("Pogrešan redni broj dokumenta u sekvenci", ds);
              return;
            }
            rzag.setInt("BRDOK", rzag.getInt("BRDOK") + zags++);
            dm.getSeq().setInt("BROJ", rzag.getInt("BRDOK"));
            Aus.clear(rzag, "UIRAC");
            currst = 0;
            stavs.put(new Integer(rzag.getInt("BRDOK")), new Integer(currst = 0));
          } else 
            currst = ((Integer) stavs.get(new Integer(rzag.getInt("BRDOK")))).intValue();
          
          stavs.put(new Integer(rzag.getInt("BRDOK")), new Integer(++currst));
          rst.insertRow(false);
          dM.copyColumns(rzag, rst, Util.mkey);
          dM.copyColumns(Artikli.get(), rst, acols);
          rst.setShort("RBR", (short) currst);
          rst.setInt("RBSID", currst);
          rst.setString("ID_STAVKA",
              raControlDocs.getKey(rst, new String[] { "cskl",
                  "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
        }
        ds.setString("ODJ", rst.getString("ID_STAVKA"));
        Aus.add(rst, "KOL", ds);
        Aus.add(rst, "INAB", ds);
        Aus.add(rst, "IMAR", ds);
        Aus.add(rst, "IBP", ds);
        Aus.add(rst, "IPOR", ds);
        Aus.add(rst, "ISP", ds);
        Aus.add(rst, "IRAZ", ds);
        Aus.set(rst, "NC", ds);
        Aus.set(rst, "VC", ds);
        Aus.set(rst, "MC", ds);
        Aus.set(rst, "ZC", ds);
      }
      
      if (rst.rowCount() > 0 && rst.getBigDecimal("KOL").signum() > 0) {
        Aus.div(rst, "NC", "INAB", "KOL");
        Aus.div(rst, "VC", "IBP", "KOL");
        Aus.div(rst, "MC", "ISP", "KOL");
        Aus.div(rst, "ZC", "IRAZ", "KOL");
      } else {
        raProcess.addError("Nema artikala za odjaviti do tog datuma", ds);
        return;
      }
      
      if (!raTransaction.saveChangesInTransaction(new QueryDataSet[] {rzag, rst, ds, dm.getSeq()})) 
        raProcess.addError("Greška prilikom snimanja dokumenata", ds);

    }

    public boolean isKPR() {
        return false;
    }

}
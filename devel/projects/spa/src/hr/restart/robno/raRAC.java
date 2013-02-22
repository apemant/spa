/****license*****************************************************************
**   file: raRAC.java
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

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Kupci;
import hr.restart.baza.Partneri;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.raSaldaKonti;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raOptionDialog;
import hr.restart.swing.raSelectTableModifier;
import hr.restart.util.*;
import hr.restart.zapod.jpGetValute;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

final public class raRAC extends raIzlazTemplate {
	
    public void initialiser() {
        what_kind_of_dokument = "RAC";
    }

    raNavAction rnvNacinPlac = new raNavAction("Izrada i ispis otpremnica",
            raImages.IMGMOVIE, java.awt.event.KeyEvent.VK_F7) {
        public void actionPerformed(ActionEvent e) {
            ispisiizradaOTP();
        }
    };
    
    //  a REALLY REALLY ugly hardcoded hack
    boolean uglyHack = true;
    
    public boolean isKnjigen() {
      return uglyHack && super.isKnjigen();
    }
    
    public void enabdisabNavAction(raMatPodaci rmm, String[] izostavi,
        boolean kako) {
      super.enabdisabNavAction(rmm, izostavi, kako);
      if (!kako && izostavi != null && izostavi.length == 3 && rmm == raMaster) {
        try {
          uglyHack = false;
          if (checkAccess())
            rnvNacinPlac.setEnabled(true);
            
        } finally {
          uglyHack = true;
        }
      }
    }

    public void ispisiizradaOTP() {
      boolean stop = "D".equals(frmParam.getParam("robno", "stopAuto", "D",
          "Prekinuti automatsku izradu otpremnica kod prve greške (D,N)"));
    	raSelectTableModifier stm = raMaster.getSelectionTracker();
    	if (stm == null || stm.countSelected() == 0) {
        raAutoOtpfromRacMask rAOFRM = new raAutoOtpfromRacMask();
        rAOFRM.setCskl(getMasterSet().getString("CSKL"));
        rAOFRM.setBrdok(getMasterSet().getInt("BRDOK"));
        rAOFRM.setGod(getMasterSet().getString("GOD"));
        rAOFRM.setVrdok(getMasterSet().getString("VRDOK"));
        rAOFRM.ispisiizradaOTP(false, false);
    	} else {
    	    raMaster.getJpTableView().enableEvents(false);
    	    try {
        		for (getMasterSet().first(); getMasterSet().inBounds(); getMasterSet().next()) {
    	    		if (stm.isSelected(getMasterSet())) {
    	    			raAutoOtpfromRacMask rAOFRM = new raAutoOtpfromRacMask();
    	    			rAOFRM.setCskl(getMasterSet().getString("CSKL"));
    	          rAOFRM.setBrdok(getMasterSet().getInt("BRDOK"));
    	          rAOFRM.setGod(getMasterSet().getString("GOD"));
    	          rAOFRM.setVrdok(getMasterSet().getString("VRDOK"));
    	          rAOFRM.ispisiizradaOTP(true, stop);
    	          if (rAOFRM.wasError())
    	            break;
    	          stm.toggleSelection(getMasterSet());
    	          getMasterSet().refetchRow(getMasterSet());
    	    	  }
        		}
    	    } finally {
    	      raMaster.getJpTableView().enableEvents(true);
    	    }
    	}
    }

    public void cskl2csklart() {
      if (!isUslugaOrTranzit())
        getDetailSet().setString("REZKOL", "D");
    }

    private boolean flipflop = true;

    public void Funkcija_ispisa_master() {

        dm.getVTText().open();
        //dm.getVTText().refresh();
        flipflop = getMasterSet().getString("PARAM").equalsIgnoreCase("_A_");
        if (flipflop) {
            String sqldodat = "";
            Condition con = raMaster.getSelectCondition();
            if (con != null) {
                // con.qualified("doki");
                sqldodat = sqldodat + " and " + con;

            } else {
                sqldodat = sqldodat + "and brdok ="
                        + getMasterSet().getInt("BRDOK");
            }
            raAutomatRac.getraAutomatRac().prepareQuery(sqldodat);
            raMaster.getRepRunner().enableReport(
                    "hr.restart.robno.repRacUsluga");
        } else {
            raMaster.getRepRunner().disableReport(
                    "hr.restart.robno.repRacUsluga");
        }

        super.Funkcija_ispisa_master();
    }

    public void Funkcija_ispisa_detail() {
        dm.getVTText().open();
        //dm.getVTText().refresh();
        flipflop = getMasterSet().getString("PARAM").equalsIgnoreCase("_A_");
        if (flipflop) {
            raAutomatRac.getraAutomatRac().prepareQuery();
            raDetail.getRepRunner().enableReport(
                    "hr.restart.robno.repRacUsluga");
        } else {
            raDetail.getRepRunner().disableReport(
                    "hr.restart.robno.repRacUsluga");
        }

        super.Funkcija_ispisa_detail();
    }

    public void MyaddIspisMaster() {
        raMaster.getRepRunner().addReport("hr.restart.robno.repRac",
                "hr.restart.robno.repIzlazni", "Rac",
                ReportValuteTester.titleRAC1R);
        
        raMaster.getRepRunner().addReport("hr.restart.robno.repRacNp",
            "hr.restart.robno.repIzlazni", "RacNoPopust",
            "Raèun 1 red bez prikazanih popusta");
        
        raMaster.getRepRunner().addReport("hr.restart.robno.repRacV",
                "hr.restart.robno.repIzlazni", "Rac",
                ReportValuteTester.titleRAC1RV);
        raMaster.getRepRunner().addReport("hr.restart.robno.repRacPnP",
            "hr.restart.robno.repRacuniPnP",
            "RacSifKupPak","Raèun sa šifrom kupca");
        
        raMaster.getRepRunner().addReport("hr.restart.robno.repRacPnP2",
            "hr.restart.robno.repRacuniPnP","RacPnP",
            "Raèun s popustima");
        
        raMaster.getRepRunner().addReport("hr.restart.robno.repRacVert",
            "hr.restart.robno.repIzlazni", "RacVert",
            "Raèun 1 red okomito");

        raMaster.getRepRunner().addReport("hr.restart.robno.repRacUsluga",
                "hr.restart.robno.repUslIzlazni", "RacUsluga",
                ReportValuteTester.titleRAC1USL);
        raMaster.getRepRunner().addReport("hr.restart.robno.repRacValSingle",
            "hr.restart.robno.repIzlazni", "RacValSingle", 
            "Raèun s valutom bez kolièina");

        /*
         * flipflop = getMasterSet().getString("PARAM").equalsIgnoreCase("_A_");
         * if (flipflop) { raAutomatRac.getraAutomatRac().prepareQuery();
         * raMaster.getRepRunner().enableReport(
         * "hr.restart.robno.repRacUsluga"); } else {
         * raMaster.getRepRunner().disableReport(
         * "hr.restart.robno.repRacUsluga"); }
         */

        raMaster.getRepRunner().addReport("hr.restart.robno.repRac2",
            "hr.restart.robno.repIzlazni", "Rac2",
            ReportValuteTester.titleRAC2R);
        raMaster.getRepRunner().addReport("hr.restart.robno.repRac",
            "hr.restart.robno.repIzlazni", "RacNoPopust2Red",
            "Raèun 2 red bez prikazanih popusta");
        
        raMaster.getRepRunner().addReport("hr.restart.robno.repRac2V",
                "hr.restart.robno.repIzlazni", "Rac2",
                ReportValuteTester.titleRAC2RV);
        raMaster.getRepRunner().addReport("hr.restart.robno.repRacRnal",
            "hr.restart.robno.repIzlazni", "RacRnal", // "RacRnal",
            ReportValuteTester.titleRACFROMRNAL);
        raMaster.getRepRunner().addReport("hr.restart.robno.repPonuda",
                "hr.restart.robno.repIzlazni", "Ponuda", "Ponuda iz raèuna");
        
        if (hr.restart.sisfun.frmParam.getParam("robno","IspisGetroROTs","N","Stavke ispisa sadržavaju i ispis za Getro",true).equals("D")){
          raMaster.getRepRunner().addReport("hr.restart.robno.repRacGetro","hr.restart.robno.repRacuniPnP","RacGetroRac","Raèun za Getro");
        }

        raMaster.getRepRunner().addReport("hr.restart.robno.repMxRAC",
                "Matri\u010Dni ispis ra\u010Duna");
        
        //test
        raMaster.getRepRunner().addReport("hr.restart.robno.repInvoice",
                "hr.restart.robno.repIzlazni","ProformaInvoice","Invoice");
        
//        if (repFISBIH.isFISBIH()) raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHRN","FISKALNI ispis ra\u010Duna");
        if (repFISBIH.isFISBIH()) {
          if (getMasterSet().getInt("FBR")>0) {
            raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHDupli","Ispis DUPLIKATA FISKALNOG ra\u010Duna");
            raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHRekRN","REKLAMIRANJE FISKALNOG ra\u010Duna");
          } else raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHRN","FISKALNI ispis ra\u010Duna");
        }
        
    }

    public void MyaddIspisDetail() {
        raDetail.getRepRunner().addReport("hr.restart.robno.repRac",
                "hr.restart.robno.repIzlazni", "Rac",
                ReportValuteTester.titleRAC1R);
        
        raDetail.getRepRunner().addReport("hr.restart.robno.repRacNp",
                "hr.restart.robno.repIzlazni", "RacNoPopust",
                "Raèun 1 red bez prikazanih popusta");
        
        raDetail.getRepRunner().addReport("hr.restart.robno.repRacV",
                "hr.restart.robno.repIzlazni", "Rac",
                ReportValuteTester.titleRAC1RV);
        raDetail.getRepRunner().addReport("hr.restart.robno.repRacPnP",
            "hr.restart.robno.repRacuniPnP",
            "RacSifKupPak","Raèun sa šifrom kupca");
        
        raDetail.getRepRunner().addReport("hr.restart.robno.repRacPnP2",
            "hr.restart.robno.repRacuniPnP","RacPnP",
            "Raèun s popustima");
        
        raDetail.getRepRunner().addReport("hr.restart.robno.repRacVert",
            "hr.restart.robno.repIzlazni", "RacVert",
            "Raèun 1 red okomito");

        raDetail.getRepRunner().addReport("hr.restart.robno.repRacUsluga",
            "hr.restart.robno.repUslIzlazni", "RacUsluga",
            ReportValuteTester.titleRAC1USL);
        raDetail.getRepRunner().addReport("hr.restart.robno.repRacValSingle",
            "hr.restart.robno.repIzlazni", "RacValSingle", 
            "Raèun s valutom bez kolièina");

        raDetail.getRepRunner().addReport("hr.restart.robno.repRac2",
            "hr.restart.robno.repIzlazni", "Rac2",
            ReportValuteTester.titleRAC2R);
        raDetail.getRepRunner().addReport("hr.restart.robno.repRac",
            "hr.restart.robno.repIzlazni", "RacNoPopust2Red",
            "Raèun 2 red bez prikazanih popusta");
        
        raDetail.getRepRunner().addReport("hr.restart.robno.repRac2V",
            "hr.restart.robno.repIzlazni", "Rac2",
            ReportValuteTester.titleRAC2RV);
        raDetail.getRepRunner().addReport("hr.restart.robno.repRacRnal",
                "hr.restart.robno.repIzlazni", "RacRnal", // TODO napraviti RacRnal
                ReportValuteTester.titleRACFROMRNAL);
        raDetail.getRepRunner().addReport("hr.restart.robno.repPonuda",
                "hr.restart.robno.repIzlazni", "Ponuda", "Ponuda iz raèuna");

        if (hr.restart.sisfun.frmParam.getParam("robno","IspisGetroROTs","N","Stavke ispisa sadržavaju i ispis za Getro",true).equals("D")){
          raDetail.getRepRunner().addReport("hr.restart.robno.repRacGetro","hr.restart.robno.repRacuniPnP","RacGetroRac","Raèun za Getro");
        }
        
        raDetail.getRepRunner().addReport("hr.restart.robno.repMxRAC",
                "Matri\u010Dni ispis ra\u010Duna");
        
        //test
        raDetail.getRepRunner().addReport("hr.restart.robno.repInvoice",
                "hr.restart.robno.repIzlazni","ProformaInvoice","Invoice");
        //test
//        if (repFISBIH.isFISBIH()) raDetail.getRepRunner().addReport("hr.restart.robno.repFISBIHRN","FISKALNI ispis ra\u010Duna");
    }

    public raRAC() {
        isOJ = true;
        addButtons(true, true);
        // raMaster.addOption(rnvDellAll,3);
        raDetail.addOption(rnvDellAllStav, 3);
        raDetail.addOption(rnvKartica, 5, false);
        
        setPreSel((jpPreselectDoc) presRAC.getPres());
        master_titel = "Ra\u010Duni";
        detail_titel_mno = "Stavke ra\u010Duna";
        detail_titel_jed = "Stavka ra\u010Duna";
        setMasterSet(dm.getZagRac());
        setDetailSet(dm.getStRac());
        MP.BindComp();
        DP.BindComp();
        if (frmParam.getParam("robno", "racSklad", "D", 
            "Dodati skladište na RAC/PON (D,N)").equals("D")) {
          DP.rpcart.addSkladField(hr.restart.robno.Util.getSkladFromCorg());
        }
        DP.resizeDP();
        raMaster.addOption(rnvNacinPlac, 6);
        raMaster.addOption(rnvFisk, 6, false);
        
        if (frmParam.getParam("robno", "racTrans", "N", 
          "Dodati opciju prijevoza na RAC (D,N)").equals("D")) {
          raDetail.addOption(new raNavAction("Prijevoz", raImages.IMGSENDMAIL, KeyEvent.VK_F8) {
            public void actionPerformed(ActionEvent e) {
              addTrans();            
            }
          }, 6, false);
          
          initTrans();
        }
        
        MP.panelBasicExt.jlrCNACPL.setRaDataSet(dm.getNacplB());
    }

    public boolean ValidacijaStanje() {
        return testStanjeRACGRN();
    }
    
    public boolean isKPR() {
      return false;
    }

    public String dodatak(String odabrano) {
        String dodatakic = new String("");
        if (odabrano.equalsIgnoreCase("PON")) {
            String cp = MP.panelBasic.jrfCPAR.getText();
            if (cp.length() == 0) {
                dodatakic = " and param like 'OJ%'";// cp = " and 1=0";
            } else {
                dodatakic = " and (cpar=" + getMasterSet().getInt("CPAR")
                        + " or cpar is null) and param like 'OJ%'";
            }
            return dodatakic;
        } else {
            return super.dodatak(odabrano);
        }
    }

    public void prepareQuery(String odabrano) {
      boolean pk = !frmParam.getParam("zapod", "parToKup", "N",
      "Dodati/brisati slog kupca kod unosa/brisanja partnera (D,N,A)?").equalsIgnoreCase("N");
        String cp = MP.panelBasic.jrfCPAR.getText();
        
        if (!pk) {
          if (cp.length() == 0) {
              dodatakRN = " and (rn.kuppar = 'P' or cpar is not null)";
          } else {
              dodatakRN = " and ((rn.kuppar = 'P' and rn.ckupac = " + cp
                      + ") or cpar =" + cp + ")";
          }
        } else {
          if (cp.length() == 0) dodatakRN = "";
          else {
            DataSet par = Partneri.getDataModule().getTempSet("cpar = " + cp);
            par.open();
            if (par.getRowCount() == 1 && Kupci.getDataModule().getRowCount(
                 Condition.equal("CKUPAC", par)) == 1) {
              dodatakRN = " and ((rn.kuppar = 'K' and rn.ckupac = " + par.getInt("CKUPAC") + 
                ") or (rn.kuppar = 'P' and rn.ckupac = " + cp + ") + cpar = " + cp + ")"; 
            } else dodatakRN = " and ((rn.kuppar = 'P' and rn.ckupac = " + 
                cp + ") or cpar =" + cp + ")";
          }
        }
        super.prepareQuery(odabrano);
    }

    raOptionDialog trans = new raOptionDialog() {
      protected boolean checkOk() {
        return isTransOk();
      }
      protected void beforeShow() {
        gv.jtTECAJ.requestFocusLater();
      }
    };
    JPanel pan = new JPanel(new XYLayout(660, 180));
    JPanel all = new JPanel(new BorderLayout());
    jpGetValute gv = new jpGetValute();
    JraTextField jraIznos = new JraTextField();
    JraTextField jraGranica = new JraTextField();
    
    JlrNavField jlrCARTi = new JlrNavField();
    JlrNavField jlrNAZARTi = new JlrNavField();
    JraButton jbi = new JraButton();
    JlrNavField jlrGradi = new JlrNavField();
    JraButton jbGi = new JraButton();
    JraTextField jraKmi = new JraTextField() {
      public void setText(String txt) {
        txt=txt==null?"":txt;
        super.setText(txt);
        super.maskCheck();
      }
    };
    
    JlrNavField jlrCARTd = new JlrNavField();
    JlrNavField jlrNAZARTd = new JlrNavField();
    JraButton jbd = new JraButton();
    JlrNavField jlrGradd = new JlrNavField();
    JraButton jbGd = new JraButton();
    JraTextField jraKmd = new JraTextField() {
      public void setText(String txt) {
        txt=txt==null?"":txt;
        super.setText(txt);
        super.maskCheck();
      }
    };
    
    StorageDataSet tds = new StorageDataSet();
    
    StorageDataSet gds = new StorageDataSet();
    
    Properties gp = new Properties();
    
    void initTrans() {
      tds.setColumns(new Column[] {
          dM.createStringColumn("OZNVAL", "Valuta", 3),
          dM.createBigDecimalColumn("TECAJ", "Tecaj", 6),
          dM.createBigDecimalColumn("IZNOS", "Iznos", 2),
          dM.createStringColumn("GRANICA", "Granica", 50),
          dM.createIntColumn("CARTi", "Šifra"),
          dM.createStringColumn("NAZARTi", "Naziv", 100),
          dM.createIntColumn("CARTd", "Šifra"),
          dM.createStringColumn("NAZARTd", "Naziv", 100),
          dM.createStringColumn("GRADi", "Mjesto", 50),
          dM.createBigDecimalColumn("KMi", "Udaljenost", 2),
          dM.createStringColumn("GRADd", "Mjesto", 50),
          dM.createBigDecimalColumn("KMd", "Udaljenost", 2),
      });
      tds.open();
      tds.insertRow(false);
      tds.post();
      
      gds.setColumns(new Column[] {
          dM.createStringColumn("GRAD", "Mjesto", 50),
          dM.createBigDecimalColumn("KM", "Udaljenost", 2)
      });
      gds.open();
      
      jlrCARTi.setColumnName("CARTi");
      jlrCARTi.setNavColumnName("CART");
      jlrCARTi.setDataSet(tds);
      jlrCARTi.setColNames(new String[] {"NAZART"});
      jlrCARTi.setTextFields(new JTextComponent[] {jlrNAZARTi});
      jlrCARTi.setVisCols(new int[] {0,3});
      jlrCARTi.setRaDataSet(Artikli.getDataModule().getFilteredDataSet("vrart='U'"));
      jlrCARTi.getRaDataSet().open();
      jlrCARTi.setSearchMode(0);
      jlrCARTi.setNavButton(jbi);
      jlrCARTi.setFocusLostOnShow(false);
      jlrNAZARTi.setColumnName("NAZARTi");
      jlrNAZARTi.setNavColumnName("NAZART");
      jlrNAZARTi.setNavProperties(jlrCARTi);
      jlrNAZARTi.setDataSet(tds);
      jlrNAZARTi.setSearchMode(1);
      jlrNAZARTi.setFocusLostOnShow(false);
      
      jlrCARTd.setColumnName("CARTd");
      jlrCARTd.setNavColumnName("CART");
      jlrCARTd.setDataSet(tds);
      jlrCARTd.setColNames(new String[] {"NAZART"});
      jlrCARTd.setTextFields(new JTextComponent[] {jlrNAZARTd});
      jlrCARTd.setVisCols(new int[] {0,3});
      jlrCARTd.setRaDataSet(Artikli.getDataModule().getFilteredDataSet("vrart='U'"));
      jlrCARTd.getRaDataSet().open();
      jlrCARTd.setSearchMode(0);
      jlrCARTd.setNavButton(jbd);
      jlrCARTd.setFocusLostOnShow(false);
      jlrNAZARTd.setColumnName("NAZARTd");
      jlrNAZARTd.setNavColumnName("NAZART");
      jlrNAZARTd.setNavProperties(jlrCARTd);
      jlrNAZARTd.setDataSet(tds);
      jlrNAZARTd.setSearchMode(1);
      jlrNAZARTd.setFocusLostOnShow(false);
      
      gv.setTecajVisible(true);
      gv.setTecajEditable(true);
      gv.setDefaultEntryEnabled(true);
      gv.setRaDataSet(tds);
      
      jraIznos.setColumnName("IZNOS");
      jraIznos.setDataSet(tds);
      
      jraGranica.setColumnName("GRANICA");
      jraGranica.setDataSet(tds);
      
      jlrGradi.setColumnName("GRADi");
      jlrGradi.setNavColumnName("GRAD");
      jlrGradi.setDataSet(tds);
      jlrGradi.setColNames(new String[] {"KM"});
      jlrGradi.setTextFields(new JTextComponent[] {jraKmi});
      jlrGradi.setVisCols(new int[] {0,1});
      jlrGradi.setRaDataSet(gds);
      jlrGradi.setSearchMode(1);
      jlrGradi.setNavButton(jbGi);
      jlrGradi.setFocusLostOnShow(false);
      
      jraKmi.setColumnName("KMi");
      jraKmi.setDataSet(tds);
      
      jlrGradd.setColumnName("GRADd");
      jlrGradd.setNavColumnName("GRAD");
      jlrGradd.setDataSet(tds);
      jlrGradd.setColNames(new String[] {"KM"});
      jlrGradd.setTextFields(new JTextComponent[] {jraKmd});
      jlrGradd.setVisCols(new int[] {0,1});
      jlrGradd.setRaDataSet(gds);
      jlrGradd.setSearchMode(1);
      jlrGradd.setNavButton(jbGd);
      jlrGradd.setFocusLostOnShow(false);
      
      jraKmd.setColumnName("KMd");
      jraKmd.setDataSet(tds);
      
      tds.setString("OZNVAL", IntParam.getTag("transport.valuta"));
      tds.setBigDecimal("TECAJ", Aus.getDecNumber(IntParam.getTag("transport.tecaj")));
      tds.setString("GRANICA", IntParam.getTag("transport.granica"));
      tds.setInt("CARTi", Aus.getNumber(IntParam.getTag("transport.d1")));
      tds.setString("NAZARTi", IntParam.getTag("transport.n1"));
      tds.setInt("CARTd", Aus.getNumber(IntParam.getTag("transport.d2")));
      tds.setString("NAZARTd", IntParam.getTag("transport.n2"));
      tds.setString("GRADi", IntParam.getTag("transport.g1"));
      tds.setBigDecimal("KMi", Aus.getDecNumber(IntParam.getTag("transport.k1")));
      tds.setString("GRADd", IntParam.getTag("transport.g2"));
      tds.setBigDecimal("KMd", Aus.getDecNumber(IntParam.getTag("transport.k2")));      
      
      pan.add(gv, new XYConstraints(0, 15, -1, -1));
      pan.add(new JLabel("Ukupan iznos"), new XYConstraints(15, 70, -1, -1));
      pan.add(jraIznos, new XYConstraints(150, 70, 100, -1));
      pan.add(new JLabel("Granica"), new XYConstraints(350, 70, -1, -1));
      pan.add(jraGranica, new XYConstraints(440, 70, 205, -1));
      
      pan.add(new JLabel("Šifra", JLabel.TRAILING), new XYConstraints(150, 102, 50, -1));
      pan.add(new JLabel("Naziv"), new XYConstraints(205, 102, 200, -1));
      pan.add(new JLabel("Mjesto"), new XYConstraints(440, 102, 120, -1));
      pan.add(new JLabel("Km", JLabel.TRAILING), new XYConstraints(575, 102, 70, -1));
      
      pan.add(new JLabel("Dionica 1"), new XYConstraints(15, 120, -1, -1));
      pan.add(jlrCARTi, new XYConstraints(150, 120, 50, -1));
      pan.add(jlrNAZARTi, new XYConstraints(205, 120, 200, -1));
      pan.add(jbi, new XYConstraints(410, 120, 21, 21));
      pan.add(jlrGradi, new XYConstraints(440, 120, 100, -1));
      pan.add(jbGi, new XYConstraints(545, 120, 21, 21));
      pan.add(jraKmi, new XYConstraints(575, 120, 70, -1));
      
      pan.add(new JLabel("Dionica 2"), new XYConstraints(15, 145, -1, -1));
      pan.add(jlrCARTd, new XYConstraints(150, 145, 50, -1));
      pan.add(jlrNAZARTd, new XYConstraints(205, 145, 200, -1));
      pan.add(jbd, new XYConstraints(410, 145, 21, 21));
      pan.add(jlrGradd, new XYConstraints(440, 145, 100, -1));
      pan.add(jbGd, new XYConstraints(545, 145, 21, 21));
      pan.add(jraKmd, new XYConstraints(575, 145, 70, -1));
      
      all.add(pan);
      all.add(trans.getOkPanel(), BorderLayout.SOUTH);
    }

    boolean addGrad(String cg, String ck) {
      String g = tds.getString(cg).trim();
      if (g.length() == 0) return false;
      boolean ch = false;
      String gu = g.toUpperCase();
      String gl = g.toLowerCase();
      String v = tds.getBigDecimal(ck).toString();
      
      if (gp.containsKey(gu) && !g.equals(gu)) {
        ch = true;
        gp.remove(gu);
      }
      if (gp.containsKey(gl) && !g.equals(gl)) {
        ch = true;
        gp.remove(gl);
      }
      String old = gp.getProperty(g);
      if (old == null || !old.equals(v)) {
        ch = true;
        gp.setProperty(g, v);
      }     
      return ch;
    }
    
    boolean isTransOk() {
      return !Valid.getValid().isEmpty(jraIznos) &&
        !Valid.getValid().isEmpty(jlrCARTi) &&
        !Valid.getValid().isEmpty(jlrCARTd) &&
        !Valid.getValid().isEmpty(jlrGradi) &&
        !Valid.getValid().isEmpty(jlrGradd) &&
        !Valid.getValid().isEmpty(jraKmi) &&
        !Valid.getValid().isEmpty(jraKmd);
    }
    
    void addTrans() {
      if (isKnjigen() || getDetailSet().rowCount() > 0) return;
      
      gds.emptyAllRows();
      FileHandler.loadProperties("transport.properties", gp);
      for (Iterator i = gp.keySet().iterator(); i.hasNext(); ) {
        String key = (String) i.next();
        gds.insertRow(false);
        gds.setString("GRAD", key);
        gds.setBigDecimal("KM", Aus.getDecNumber(gp.getProperty(key)));
      }
      
      gv.setTecajDate(getMasterSet().getTimestamp("DATDOK"));
      gv.setDefaultEntryEnabled(tds.getString("OZNVAL").length() > 0);
      gv.initJP('N');
      if (tds.getBigDecimal("TECAJ").signum() != 0) gv.disableDohvat();
      if (trans.show(raDetail, all, "Podaci za prijevoz")) {
        System.out.println("OK pressed");
        boolean save = addGrad("GRADi", "KMi");
        save = save || addGrad("GRADd", "KMd");
        if (save) 
          FileHandler.storeProperties("transport.properties", gp);
        
        IntParam.setTag("transport.valuta", tds.getString("OZNVAL"));
        IntParam.setTag("transport.tecaj", tds.getBigDecimal("TECAJ").toString());
        IntParam.setTag("transport.granica", tds.getString("GRANICA"));
        IntParam.setTag("transport.d1", jlrCARTi.getText());
        IntParam.setTag("transport.n1", tds.getString("NAZARTi"));
        IntParam.setTag("transport.d2", jlrCARTd.getText());
        IntParam.setTag("transport.n2", tds.getString("NAZARTd"));
        IntParam.setTag("transport.g1", tds.getString("GRADi"));
        IntParam.setTag("transport.k1", tds.getBigDecimal("KMi").toString());
        IntParam.setTag("transport.g2", tds.getString("GRADd"));
        IntParam.setTag("transport.k2", tds.getBigDecimal("KMd").toString());
        
        unesiStavke();
      }
    }
    
    void unesiStavku(short rbs, int cart, String naz, BigDecimal c) {
      rKD.stavkaold.Init();
      rKD.stavka.Init();
      getDetailSet().insertRow(false);
      getDetailSet().setString("CSKL",getMasterSet().getString("CSKL"));
      getDetailSet().setString("GOD",getMasterSet().getString("GOD"));
      getDetailSet().setString("VRDOK",getMasterSet().getString("VRDOK"));
      getDetailSet().setInt("BRDOK",getMasterSet().getInt("BRDOK"));
      getDetailSet().setShort("RBR", rbs);
      getDetailSet().setInt("RBSID", rbs);

      getDetailSet().setInt("CART", cart);
      if (hr.restart.util.lookupData.getlookupData().
          raLocate(dm.getArtikli(), "CART", Integer.toString(cart))) {  
        getDetailSet().setString("CART1",dm.getArtikli().getString("CART1"));
        getDetailSet().setString("BC",dm.getArtikli().getString("BC"));
        getDetailSet().setString("JM",dm.getArtikli().getString("JM"));
      }
      getDetailSet().setString("NAZART", naz);
      
      if (lD.raLocate(dm.getPorezi(), new String[] { "CPOR" },
          new String[] { dm.getArtikli().getString("CPOR") })) {
        getDetailSet().setBigDecimal("PPOR1",
              dm.getPorezi().getBigDecimal("POR1"));
        getDetailSet().setBigDecimal("PPOR2",
              dm.getPorezi().getBigDecimal("POR2"));
        getDetailSet().setBigDecimal("PPOR3",
              dm.getPorezi().getBigDecimal("POR3"));
        getDetailSet().setBigDecimal("UPPOR",
              dm.getPorezi().getBigDecimal("UKUPOR"));
      }
      
      getDetailSet().setBigDecimal("KOL", Aus.one0);
      getDetailSet().setBigDecimal("FC", c);
      // napuni id stavke (ab.f)
      getDetailSet().setString("ID_STAVKA",
          raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
              "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
      
      lc.TransferFromDB2Class(getDetailSet(), rKD.stavka);
      rKD.KalkulacijaStavke(what_kind_of_dokument, "KOL", 'N',
              getMasterSet().getString("CSKL"), false);
      lc.TransferFromClass2DB(getDetailSet(), rKD.stavka); 
    }
    
    void unesiStavke() {
      BigDecimal uirac = Aus.zero2;
      
      BigDecimal total = tds.getBigDecimal("IZNOS");
      if (gv.jcbValuta.isSelected()) {
        BigDecimal jedval = raSaldaKonti.getJedVal(tds.getString("OZNVAL"));
        total = total.multiply(tds.getBigDecimal("TECAJ")).
          divide(jedval, 2, BigDecimal.ROUND_HALF_UP);
      }
      BigDecimal uk = tds.getBigDecimal("KMi").add(tds.getBigDecimal("KMd"));
      BigDecimal dio = total.multiply(tds.getBigDecimal("KMi")).divide(uk, 2, BigDecimal.ROUND_HALF_UP);
            
      unesiStavku((short) 1, tds.getInt("CARTi"), 
          tds.getString("NAZARTi") + " " + tds.getString("GRADi") +
          " - " + tds.getString("GRANICA") + "  (" + 
          tds.getBigDecimal("KMi").intValue() +
          " km)", dio);
      
      uirac = getDetailSet().getBigDecimal("IPRODSP");
      
      unesiStavku((short) 2, tds.getInt("CARTd"), 
          tds.getString("NAZARTd") + " " + tds.getString("GRANICA") +
          " - " + tds.getString("GRADd") + "  (" + 
          tds.getBigDecimal("KMd").intValue() +
          " km)", total.subtract(dio));
      
      uirac = uirac.add(getDetailSet().getBigDecimal("IPRODSP"));
      getMasterSet().setBigDecimal("UIRAC", uirac);
      raTransaction.saveChangesInTransaction(
          new QueryDataSet[] {getMasterSet(), getDetailSet()});
      raDetail.getJpTableView().fireTableDataChanged();
      raDetail.jeprazno();
    }
    
    public boolean ValidacijaMasterExtend() {
        String statpar = hr.restart.util.Util.getNewQueryDataSet(
                "SELECT STATUS FROM PARTNERI WHERE CPAR="
                        + getMasterSet().getInt("CPAR"), true).getString(
                "STATUS");

        if (statpar.equalsIgnoreCase("B")) {
            if (javax.swing.JOptionPane
                    .showConfirmDialog(
                            null,
                            "Partner je oznaèen za fakturiranje uz provjeru. Nastaviti ?",
                            "Greška", javax.swing.JOptionPane.OK_CANCEL_OPTION,
                            javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.OK_OPTION) {
                MP.panelBasic.jrfCPAR.requestFocus();
                return true;
            } else {
                MP.panelBasic.jrfCPAR.requestFocus();
                return false;
            }
        } else if (statpar.equalsIgnoreCase("C")) {

            javax.swing.JOptionPane.showMessageDialog(null,
                    "Partner ima oznaku zabrane fakturiranja !", "Obavijest",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            MP.panelBasic.jtfDATDOK.requestFocus();
            return false;
        }
        return true;
    }

    public boolean ValidacijaPrijeIzlazaDetail() {

        if (hr.restart.sisfun.frmParam.getParam("robno", "autoOTP", "N",
                "Automatska izrada OTP iz RAC-a i GRN-a").equalsIgnoreCase("N")) {
            return true;
        }

        /*
         * System.out.println ("select * from stdoki where
         * cskl='"+getMasterSet().getString("CSKL")+ "' and
         * vrdok='"+getMasterSet().getString("VRDOK")+ "' and
         * god='"+getMasterSet().getString("GOD")+"' and brdok="+
         * getMasterSet().getInt("BRDOK")+" and status='N'");
         */
        // provjeravam ima li stavaka koje nisu obradjene
        if (hr.restart.util.Util.getNewQueryDataSet(
                "select * from stdoki where cskl='"
                        + getMasterSet().getString("CSKL") + "' and vrdok='"
                        + getMasterSet().getString("VRDOK") + "' and god='"
                        + getMasterSet().getString("GOD") + "' and brdok="
                        + getMasterSet().getInt("BRDOK") + " and status='N'",
                true).getRowCount() == 0) {
            return true;
        }

        raAutoOtpfromRacMask rAOFRM = new raAutoOtpfromRacMask();
        rAOFRM.setCskl(getMasterSet().getString("CSKL"));
        rAOFRM.setBrdok(getMasterSet().getInt("BRDOK"));
        rAOFRM.setGod(getMasterSet().getString("GOD"));
        rAOFRM.setVrdok(getMasterSet().getString("VRDOK"));
        return rAOFRM.start();
    }

    public boolean DodatnaValidacijaDetail() {
        if (val.isEmpty(DP.jtfKOL))
            return false;
        // if (val.isEmpty(DP.jraFC))
        // return false;
        if (DP.jraFC.getDataSet().getBigDecimal(DP.jraFC.getColumnName())
                .compareTo(Aus.zero2) == 0) {
            if (javax.swing.JOptionPane.showConfirmDialog(this,
                    "Cijena je nula. Da li je to u redu ?", "Upit",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.NO_OPTION) {
                return false;

            }

        }

        if (raDetail.getMode() == 'I') {
            if (getDetailSet().getString("STATUS").equalsIgnoreCase("P")) {
                if (rKD.stavkaold.kol.compareTo(rKD.stavka.kol) != 0) {
                    JOptionPane
                            .showConfirmDialog(
                                    this.raDetail,
                                    "Ne smije se mijenjati kolièina jer je za ovu stavku veæ napravljena otpremnica !",
                                    "Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean LocalDeleteCheckDetail() {

        if (hr.restart.util.Util.getNewQueryDataSet(
                "select * from stdoki where cskl='"
                        + getMasterSet().getString("CSKL") + "' and vrdok='"
                        + getMasterSet().getString("VRDOK") + "' and god='"
                        + getMasterSet().getString("GOD") + "' and brdok="
                        + getMasterSet().getInt("BRDOK") + " and rbsid="
                        + getDetailSet().getInt("RBSID") + " and status='P'",
                true).getRowCount() != 0) {
            return (javax.swing.JOptionPane
                    .showConfirmDialog(
                            this.raDetail,
                            "Za ovaj je dokument veæ napravljena otpremnica. Da li želite nastaviti ?",
                            "Upit", javax.swing.JOptionPane.YES_NO_OPTION,
                            javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION);
            // JOptionPane.showConfirmDialog(this.raDetail,"Nije moguæe
            // izbrisati stavku raèuna jer za nju postoji stavka otpremnice !",
            // "Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
            // return false;
        }
        return true;
    }

    public boolean LocalDeleteCheckMaster() {
        if (hr.restart.util.Util.getNewQueryDataSet(
                "select * from stdoki where cskl='"
                        + getMasterSet().getString("CSKL") + "' and vrdok='"
                        + getMasterSet().getString("VRDOK") + "' and god='"
                        + getMasterSet().getString("GOD") + "' and brdok="
                        + getMasterSet().getInt("BRDOK") + " and status='P'",
                true).getRowCount() != 0) {
            return (javax.swing.JOptionPane
                    .showConfirmDialog(
                            this.raDetail,
                            "Za ovaj dokument su napravljene otpremnice. Da li želite nastaviti ?",
                            "Upit", javax.swing.JOptionPane.YES_NO_OPTION,
                            javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION);
            //
            //      	
            //      	
            // JOptionPane.showConfirmDialog(this.raDetail,"Nije moguæe
            // izbrisati raèun jer su po njemu veæ napravljene otpremnice !",
            // "Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
            // return false;
        }
        return true;
    }
}

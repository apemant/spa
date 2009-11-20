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

import hr.restart.baza.Condition;
import hr.restart.baza.Kupci;
import hr.restart.baza.Partneri;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.raSelectTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;

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
        raMaster.getRepRunner().addReport("hr.restart.robno.repRac",
                "hr.restart.robno.repIzlazni","ProformaInvoice","Invoice");
        //test

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
        raDetail.getRepRunner().addReport("hr.restart.robno.repRac",
                "hr.restart.robno.repIzlazni","ProformaInvoice","Invoice");
        //test
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
        DP.rpcart.addSkladField(hr.restart.robno.Util.getSkladFromCorg());
        DP.resizeDP();
        raMaster.addOption(rnvNacinPlac, 6);
        
        MP.panelBasicExt.jlrCNACPL.setRaDataSet(dm.getNacplB());
    }

    public boolean ValidacijaStanje() {
        return testStanjeRACGRN();
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

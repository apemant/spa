/****license*****************************************************************
**   file: raROT.java
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
import hr.restart.baza.Doku;
import hr.restart.baza.Stdoku;
import hr.restart.baza.dM;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raColors;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.swing.raStatusColorModifier;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.swing.raTableValueModifier;
import hr.restart.util.Aus;
import hr.restart.util.MasterDetailChooser;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.DataSetException;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jb.util.TriStateProperty;

/*
import javax.swing.*;
import java.awt.*;
import com.borland.jbcl.layout.*;
import java.awt.event.*;
import hr.restart.swing.*;
import hr.restart.util.*;
import com.borland.dbswing.*;
import javax.swing.border.*;
import com.borland.dx.dataset.*;
import com.borland.dx.sql.dataset.*;
import hr.restart.util.*;
import hr.restart.sisfun.*;
*/
public class raROT extends raIzlazTemplate  {

  raStatusColorModifier msc = new raStatusColorModifier("STATIRA", "N",
      raColors.green, Color.green.darker().darker());
  
  raStatusColorModifier dsc = new raStatusColorModifier("STATUS", "N",
      raColors.green, Color.green.darker().darker());
  
  raNavAction rnvDetailFlag = new raNavAction("Promjena statusa stavke",
      raImages.IMGPROPERTIES, java.awt.event.KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
      checkDetailObr();
    }
  };
  
  raNavAction rnvDetailVeza = new raNavAction("Povezivanje stavke s ulazom",
      raImages.IMGSENDMAIL, java.awt.event.KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      setDetailVeza();
    }
  };
  
  raNavAction rnvMasterFlag = new raNavAction("Promjena statusa dokumenta",
      raImages.IMGPROPERTIES, java.awt.event.KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
      checkMasterObr();
    }
  };
  
  public void initialiser(){
    what_kind_of_dokument = "ROT" ;
  }
  
  public void zamraciMaster(DataSet ds){}
  public void zamraciDetail(DataSet ds){
    
    if (frmParam.getParam("robno", "ROTzarada", "D",
    "Izraèunati stvarnu zaradu na ROT-u (D,N)").equals("D")) {
      dm.getStRot().getColumn("KOL2").setCaption("Jedinièni RuC");
    }
  
  	ds.getColumn("CRADNAL").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("UPRAB").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("UIRAB").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("UPZT").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("UIZT").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("FC").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("INETO").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("FVC").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("IPRODBP").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("POR1").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("POR2").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("POR3").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("FMC").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("IPRODSP").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("NC").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("INAB").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("IMAR").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("VC").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("IBP").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("IPOR").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("MC").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("ISP").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("IRAZ").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("BRPRI").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("RBRPRI").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("PPOR1").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("PPOR2").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("PPOR3").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("CARTNOR").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("FMCPRP").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("REZKOL").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("VEZA").setVisible(TriStateProperty.TRUE);
  	ds.getColumn("ID_STAVKA").setVisible(TriStateProperty.TRUE);
	ds.getColumn("ZC").setVisible(TriStateProperty.TRUE);  	
  } 
  public void stozbroiti(){
  	set_kum_detail(true);
    stozbrojiti_detail(new String[] {"INAB","IMAR","IPOR","IRAZ",
            "IPRODBP","POR1","IPRODSP"});
    setnaslovi_detail(new String[]  {"Razduženje nab. vri.",
            "Razduženje marže",
            "Razduženje poreza",
            "Razduženje zalihe",
            "Prihod",
            "Porez",
            "Dug kupca"});
    raDetail.addOption(rnvElementi,5);
    raDetail.addOption(rnvKartica,6, false);
  	
  }
  
  
  public raROT() {
////
    setPreSel((jpPreselectDoc) presROT.getPres());
    addButtons(true,true);
    raMaster.addOption(rnvFisk, 5, false);
    master_titel = "Ra\u010Duni - otpremnice";
    detail_titel_mno = "Stavke ra\u010Duna - otpremnice";
    detail_titel_jed = "Stavka ra\u010Duna - otpremnice";
    zamraciMaster(dm.getZagRot());
    zamraciDetail(dm.getStRot());
    setMasterSet(dm.getZagRot());
    setDetailSet(dm.getStRot());
    MP.BindComp();
    DP.BindComp();
    MP.panelBasicExt.jlrCNACPL.setRaDataSet(dm.getNacplB());
        
    stozbroiti();    
    
    if (frmParam.getParam("robno", "ROTzarada", "D",
        "Izraèunati stvarnu zaradu na ROT-u (D,N)").equals("D")) {
      raDetail.getJpTableView().addTableModifier(new raTableModifier() {
        Variant shared = new Variant();
        public void modify() {
          JraTable2 tab = (JraTable2) getTable();
          DataSet ds = tab.getDataSet();
          ds.getVariant("IPRODBP", getRow(), shared);
          BigDecimal ruc = shared.getBigDecimal();
          ds.getVariant("INAB", getRow(), shared);
          ruc = ruc.subtract(shared.getBigDecimal());
          shared.setBigDecimal(ruc);
          Column bcol = ds.getColumn("IMAR");
          setComponentText(bcol.format(shared));
        }
      
        public boolean doModify() {
          if (getTable() instanceof JraTable2) {
            JraTable2 tab = (JraTable2) getTable();
            Column col = tab.getDataSetColumn(getColumn());
            return (col != null && col.getColumnName().equalsIgnoreCase("IMAR"));
          }
          return false;
        }
      });
      
      raDetail.getJpTableView().addTableModifier(new raTableModifier() {
        Variant shared = new Variant();
        public void modify() {
          JraTable2 tab = (JraTable2) getTable();
          DataSet ds = tab.getDataSet();
          ds.getVariant("IPRODBP", getRow(), shared);
          BigDecimal ruc = shared.getBigDecimal();
          ds.getVariant("INAB", getRow(), shared);
          ruc = ruc.subtract(shared.getBigDecimal());
          ds.getVariant("KOL", getRow(), shared);
          if (shared.getBigDecimal().signum() != 0)
            shared.setBigDecimal(ruc.divide(shared.getBigDecimal(), 3, BigDecimal.ROUND_HALF_UP));
          else shared.setBigDecimal(ruc);
          Column bcol = ds.getColumn("KOL2");
          setComponentText(bcol.format(shared));
        }
      
        public boolean doModify() {
          if (getTable() instanceof JraTable2) {
            JraTable2 tab = (JraTable2) getTable();
            Column col = tab.getDataSetColumn(getColumn());
            return (col != null && col.getColumnName().equalsIgnoreCase("KOL2"));
          }
          return false;
        }
      });
    }
 }

  public void beforeShowMaster() {
    super.beforeShowMaster();
    raMaster.getJpTableView().removeTableModifier(msc);
    raDetail.getJpTableView().removeTableModifier(dsc);
    raMaster.removeOption(rnvMasterFlag);
    raDetail.removeOption(rnvDetailFlag);
    raDetail.removeOption(rnvDetailVeza);
    if (isTranzit) {
      raMaster.getJpTableView().addTableModifier(msc);
      raDetail.getJpTableView().addTableModifier(dsc);
      raMaster.addOption(rnvMasterFlag, 5, true);
      raDetail.addOption(rnvDetailFlag, 4, true);
      raDetail.addOption(rnvDetailVeza, 5, true);
    }
  }
  public void Funkcija_ispisa_master(){
    if (repFISBIH.isFISBIH()) {//talasamo samo kad treba
      raMaster.getRepRunner().clearAllCustomReports();
      isMasterInitIspis = false;
    }
    super.Funkcija_ispisa_master();
  }
 public void MyaddIspisMaster(){
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuni","hr.restart.robno.repIzlazni","Racuni",ReportValuteTester.titleROT1R);
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniV","hr.restart.robno.repIzlazni","Racuni",ReportValuteTester.titleROT1RV);
     //TODO e sad ovaj ispis da li ga dopustiti za sve raèune, ili samo za one koji imaju sheme popusta (tzv. popusti na popust)???

     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniPnP","hr.restart.robno.repRacuniPnP","RacuniPnP","Raèun-otpremnica s popustima");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniNorm","hr.restart.robno.repIzlazniNorm","RacuniNorm","Raèun-otpremnica s normativima");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSifKup","hr.restart.robno.repRacuniPnP","ROTSifKup","Raèun-otpremnica sa šifrom kupca");
     
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuni2","hr.restart.robno.repIzlazni","Racuni2",ReportValuteTester.titleROT2R);
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuni2V","hr.restart.robno.repIzlazni","Racuni2",ReportValuteTester.titleROT2RV);
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSifKupPak","hr.restart.robno.repRacuniPnP","RacuniSifKupPak","Raèun-otpremnica sa šifrom kupca i pakiranjem");
//     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repIzlazni","RacuniSKL",ReportValuteTester.titleROTSKL+ " ROT");
     
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repIzlazni","OTP","Otpremnica");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repIzlazni","OTPvri","Otpremnica vrijednosna");
/*     
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repRacuniPnP","OTPsifKup","Otpremnica s šifrom kupca");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OTPsifKupVri","Otpremnica vrijednosna s šifrom kupca");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repRacuniPnP","OTPkut","Otpremnica s pakiranjem");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OTPkutVri","Otpremnica vrijednosna s pakiranjem");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repRacuniPnP","OTPsifKupKut","Otpremnica s šifrom kupca i pakiranjem");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OTPsifKupKutVri","Otpremnica vrijednosna s šifrom kupca i pakiranjem");

     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repRacuniPnP","OTPmegablast","Otpremnica s šifrom kupca, pakiranjem i jedinicom mjere");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OTPVrimegablast","Otpremnica vrijednosna s šifrom kupca, pakiranjem i jedinicom mjere");
*/

     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OtpSpecialCase","Otpremnica vrijednosna sa šifrom kupca, pakiranjem i jedinicom mjere *");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OtpSpecialCase02","Otpremnica vrijednosna sa šifrom kupca, pakiranjem i jedinicom mjere bez cijena *");
     
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repIzlazni","OTP2","Otpremnica dvije jedinice mjere");
     
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniF","hr.restart.robno.repIzlazni","RacuniF","Raèun");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniEAN","hr.restart.robno.repIzlazni","RacuniEAN",ReportValuteTester.titleROT1R + " (s EAN kodom)");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniPnP","hr.restart.robno.repRacuniPnP","RacuniVelikiKupci","Raèun sa šifrom kupca");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OtpSpecialCase03","Otpremnica sa šifrom kupca");
     raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniPnPOtp","hr.restart.robno.repRacuniPnP","RacuniPnPOtp","Otpremnica s popustima");
     if (hr.restart.sisfun.frmParam.getParam("robno","IspisMetroROTs","N","Stavke ispisa sadržavaju i ispis za Metro",true).equals("D")){
       raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniMetro","hr.restart.robno.repIzlazni","RacuniMetro","Raèun za METRO");
       raMaster.getRepRunner().addReport("hr.restart.robno.repOtpremnicaMetro","hr.restart.robno.repIzlazni","OtpremnicaMetro","Otpremnica za METRO");
     }
     if (hr.restart.sisfun.frmParam.getParam("robno","IspisGetroROTs","N","Stavke ispisa sadržavaju i ispis za Getro",true).equals("D")){
       raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniGetro","hr.restart.robno.repRacuniPnP","RacGetro","Raèun za Getro");
     }
     raMaster.getRepRunner().addReport("hr.restart.robno.repInvoiceNew","hr.restart.robno.repIzlazni","InvoiceNew","Invoice");
     raMaster.getRepRunner().addReport("hr.restart.robno.repMxROT","Matri\u010Dni ispis ra\u010Duna");
     raMaster.getRepRunner().addReport("hr.restart.robno.repMxROTPop","Matri\u010Dni ispis ra\u010Duna s više popusta");
     
     //test
     
     //test
//     if (repFISBIH.isFISBIH()) raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHRN","FISKALNI ispis ra\u010Duna");
     if (repFISBIH.isFISBIH()) {
System.err.println("getMasterSet().getInt(FBR) = "+getMasterSet().getInt("FBR"));
       if (getMasterSet().getInt("FBR")>0) {
         raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHDupli","Ispis DUPLIKATA FISKALNOG ra\u010Duna");
         raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHRekRN","REKLAMIRANJE FISKALNOG ra\u010Duna");
       } else raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHRN","FISKALNI ispis ra\u010Duna");
     }
 }

 public void MyaddIspisDetail(){

     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuni","hr.restart.robno.repIzlazni","Racuni",ReportValuteTester.titleROT1R);
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniV","hr.restart.robno.repIzlazni","Racuni",ReportValuteTester.titleROT1RV);
     //TODO e sad ovaj ispis da li ga dopustiti za sve raèune, ili samo za one koji imaju sheme popusta (tzv. popusti na popust)???
     
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniPnP","hr.restart.robno.repRacuniPnP","RacuniPnP","Raèun-otpremnica s popustima");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniNorm","hr.restart.robno.repIzlazniNorm","RacuniNorm","Raèun-otpremnica s normativima");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSifKup","hr.restart.robno.repRacuniPnP","ROTSifKup","Raèun-otpremnica sa šifrom kupca");
     
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuni2","hr.restart.robno.repIzlazni","Racuni2",ReportValuteTester.titleROT2R);
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuni2V","hr.restart.robno.repIzlazni","Racuni2",ReportValuteTester.titleROT2RV);
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSifKupPak","hr.restart.robno.repRacuniPnP","RacuniSifKupPak","Raèun-otpremnica sa šifrom kupca i pakiranjem");
//     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repIzlazni","RacuniSKL",ReportValuteTester.titleROTSKL);
     
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repIzlazni","OTP","Otpremnica");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repIzlazni","OTPvri","Otpremnica vrijednosna");
/*
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repRacuniPnP","OTPsifKup","Otpremnica s šifrom kupca");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OTPsifKupVri","Otpremnica vrijednosna s šifrom kupca");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repRacuniPnP","OTPkut","Otpremnica s pakiranjem");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OTPkutVri","Otpremnica vrijednosna s pakiranjem");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repRacuniPnP","OTPsifKupKut","Otpremnica s šifrom kupca i pakiranjem");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OTPsifKupKutVri","Otpremnica vrijednosna s šifrom kupca i pakiranjem");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repRacuniPnP","OTPmegablast","Otpremnica s šifrom kupca, pakiranjem i jedinicom mjere");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OTPVrimegablast","Otpremnica vrijednosna s šifrom kupca, pakiranjem i jedinicom mjere");
*/     
     
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OtpSpecialCase","Otpremnica vrijednosna sa šifrom kupca, pakiranjem i jedinicom mjere *");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OtpSpecialCase02","Otpremnica vrijednosna sa šifrom kupca, pakiranjem i jedinicom mjere bez cijena *");
     
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repIzlazni","OTP2","Otpremnica dvije jedinice mjere");
     
//     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repIzlazni","RacuniSKL",ReportValuteTester.titleROTSKL);
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniF","hr.restart.robno.repIzlazni","RacuniF","Raèun");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniEAN","hr.restart.robno.repIzlazni","RacuniEAN",ReportValuteTester.titleROT1R + " (s EAN kodom)");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniPnP","hr.restart.robno.repRacuniPnP","RacuniVelikiKupci","Raèun sa šifrom kupca");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repRacuniPnP","OtpSpecialCase03","Otpremnica sa šifrom kupca");
     raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniPnPOtp","hr.restart.robno.repRacuniPnP","RacuniPnPOtp","Otpremnica s popustima");
     if (hr.restart.sisfun.frmParam.getParam("robno","IspisMetroROTs","N","Stavke ispisa sadržavaju i ispis za Metro",true).equals("D")){
       raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniMetro","hr.restart.robno.repIzlazni","RacuniMetro","Raèun za METRO");
       raDetail.getRepRunner().addReport("hr.restart.robno.repOtpremnicaMetro","hr.restart.robno.repIzlazni","OtpremnicaMetro","Otpremnica za METRO");
     }
     if (hr.restart.sisfun.frmParam.getParam("robno","IspisGetroROTs","N","Stavke ispisa sadržavaju i ispis za Getro",true).equals("D")){
       raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniGetro","hr.restart.robno.repRacuniPnP","RacGetro","Raèun za Getro");
     }
     raDetail.getRepRunner().addReport("hr.restart.robno.repInvoiceNew","hr.restart.robno.repIzlazni","InvoiceNew","Invoice");
     raDetail.getRepRunner().addReport("hr.restart.robno.repMxROT","Matri\u010Dni ispis ra\u010Duna");
     raDetail.getRepRunner().addReport("hr.restart.robno.repMxROTPop","Matri\u010Dni ispis ra\u010Duna s više popusta");
//     if (repFISBIH.isFISBIH()) raDetail.getRepRunner().addReport("hr.restart.robno.repFISBIHRN","FISKALNI ispis ra\u010Duna");
 }
 
 String tranzitVeza;
 public void prepareOldDetailValues() {
   super.prepareOldDetailValues();
   tranzitVeza = getDetailSet().getString("VEZA");
 }

  private String[] vkey = {"CSKL", "VRDOK", "GOD", "BRDOK", "RBSID"};
 
  public boolean doWithSaveDetailBrisi() {
   if (!super.doWithSaveDetailBrisi()) return false;
   
   if (isTranzit && tranzitVeza != null && tranzitVeza.length() > 0) {
     String[] vals = new VarStr(tranzitVeza).splitTrimmed('-');
     if (vals.length >= vkey.length) {
       QueryDataSet prk = Stdoku.getDataModule().getTempSet(
         Condition.whereAllEqual(vkey, vals));
       prk.open();
       if (prk.rowCount() == 1) {
         prk.setString("VEZA", "");
         raTransaction.saveChanges(prk);
       }
     }
   } else if (isTranzit) {
  	 DataSet nonp = stdoki.getDataModule().getTempSet(Condition.whereAllEqual(
         Util.mkey, getMasterSet()).and(Condition.equal("STATUS", "N")));
   		nonp.open();
   		if (nonp.rowCount() == 0) {
   			getMasterSet().setString("STATIRA", "P");
   			raTransaction.saveChanges(getMasterSet());
   		}
   }
   return true;
 }
 
  void checkMasterObr() {
  	if (!isTranzit || getMasterSet().rowCount() == 0 
	  		|| isKnjigen() || isPrenesen() || isKPR()) return;
  	
  	if (getMasterSet().getString("STATIRA").equals("P")) {
  		setMasterObr(false, null);
  		return;
  	}
  	QueryDataSet nonp = stdoki.getDataModule().getTempSet(Condition.whereAllEqual(
        Util.mkey, getMasterSet()).and(Condition.equal("STATUS", "N")));
  	nonp.open();
  	if (nonp.rowCount() == 0) {
  		setMasterObr(true, null);
  		return;
  	}
  	int i = 0;
  	int[] stav = new int[nonp.rowCount()];
  	for (nonp.first(); nonp.inBounds(); nonp.next()) {
  		stav[i++] = nonp.getShort("RBR");
  		nonp.setString("STATUS", "P");
  	}
  	VarStr text = VarStr.join(stav, ", ");
  	if (i == 1) text.insert(0, "Stavka ").append(" nije povezana!");
  	else text.insert(0, "Stavke ").append(" nisu povezane!");
  	text.append(" Oznaèiti dokument kao riješen?");
  	if (JOptionPane.OK_OPTION == 
 		 JOptionPane.showConfirmDialog(raDetail.getWindow(),
 				 text.toString(), "Upozorenje", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.WARNING_MESSAGE)) setMasterObr(true, nonp);
  }
  
  void setMasterObr(boolean obr, QueryDataSet other) {
  	getMasterSet().setString("STATIRA", obr ? "P" : "N");
  	boolean succ = true;
  	if (other != null) succ = raTransaction.saveChangesInTransaction(
  			new QueryDataSet[] {getMasterSet(), other});
  	else try {
			getMasterSet().saveChanges();
		} catch (DataSetException e) {
			succ = false;
			e.printStackTrace();
		}
		if (!succ) {
			JOptionPane.showMessageDialog(raDetail.getWindow(), 
          "Neuspješna promjena statusa!",
          "Greška", JOptionPane.ERROR_MESSAGE);
			try {
				getMasterSet().refetchRow(getMasterSet());
			} catch (Exception re) {
				re.printStackTrace();
			}
		}
		raMaster.getJpTableView().repaint();
  }
  
  void checkDetailObr() {
	  if (!isTranzit || getDetailSet().rowCount() == 0 
	  		|| isKnjigen() || isPrenesen() || isKPR()) return;

	  boolean isObr = getDetailSet().getString("STATUS").equals("P");
	  tranzitVeza = getDetailSet().getString("VEZA");
	  if (tranzitVeza != null && tranzitVeza.length() > 0) {
	  	 String[] vals = new VarStr(tranzitVeza).splitTrimmed('-');
	     if (vals.length >= vkey.length) {
	       QueryDataSet prk = Stdoku.getDataModule().getTempSet(
	         Condition.whereAllEqual(vkey, vals));
	       prk.open();
	       if (prk.rowCount() == 1) {
	      	 if (!isObr) setDetailObr(true, null);
	      	 else if (JOptionPane.OK_OPTION == 
	      		 JOptionPane.showConfirmDialog(raDetail.getWindow(),
	             "Stavka raèuna je povezana sa stavkom ulaza!" +
	             " Poništiti vezu?", "Upozorenje", JOptionPane.OK_CANCEL_OPTION,
	             JOptionPane.WARNING_MESSAGE)) {
	      		 prk.setString("VEZA", "");
	      		 setDetailObr(false, new QueryDataSet[] {prk});
	      	 }
	      	 return;
	       }
	     }
	  }
	  setDetailObr(!isObr, null);
  }
  
  void setDetailObr(boolean obr, QueryDataSet[] other) {
  	ArrayList qs = new ArrayList();
  	qs.add(getDetailSet());
  	if (other != null) qs.addAll(Arrays.asList(other));
  	getDetailSet().setString("STATUS", obr ? "P" : "N");
  	if (!obr) {
  		getDetailSet().setString("VEZA", "");
  		if (getMasterSet().getString("STATIRA").equals("P")) {
  			getMasterSet().setString("STATIRA", "N");
	    	qs.add(getMasterSet());
  		}
  	} else {
	  	DataSet nonp = stdoki.getDataModule().getTempSet(Condition.whereAllEqual(
	        Util.mkey, getDetailSet()).and(Condition.equal("STATUS", "N")));
	    nonp.open();
	    if (nonp.rowCount() == 0 || (nonp.rowCount() == 1 &&
	        dM.compareColumns(getDetailSet(), nonp, vkey) == null)) {
	    	getMasterSet().setString("STATIRA", "P");
	    	qs.add(getMasterSet());
	    }
  	}
    if (!raTransaction.saveChangesInTransaction(
    		(QueryDataSet[]) qs.toArray(new QueryDataSet[qs.size()]))) {
    	JOptionPane.showMessageDialog(raDetail.getWindow(), 
          "Neuspješna promjena statusa!",
          "Greška", JOptionPane.ERROR_MESSAGE);
    	for (int i = 0; i < qs.size(); i++)
    		try {
					((QueryDataSet) qs.get(i)).refetchRow((QueryDataSet) qs.get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
    }
    raDetail.getJpTableView().repaint();
    raMaster.getJpTableView().repaint();
  }
  
  public void extraStanje(char mode) {
  	if (isTranzit && mode != 'B') {
  		boolean isStanje = AST.findStanjeFor(getDetailSet(), false);
  		if (!isStanje) {
					AST.gettrenSTANJE().insertRow(false);
					dM.copyColumns(getDetailSet(), AST.gettrenSTANJE(), 
							new String[] {"CSKL", "GOD", "CART"});
  				nulaStanje(AST.gettrenSTANJE());
  				raTransaction.saveChanges(AST.gettrenSTANJE());
  		}
  	}
	}
  
  void setDetailVeza() {
  	if (!isTranzit || getDetailSet().rowCount() == 0 
	  		|| isKnjigen() || isPrenesen() || isKPR()) return;
  	
  	if (getDetailSet().getString("STATUS").equals("P")) {
  		JOptionPane.showMessageDialog(raDetail.getWindow(), 
          "Stavka je veæ povezana s ulazom!",
          "Povezivanje", JOptionPane.WARNING_MESSAGE);
  		return;
  	}
  	
  	String art = Integer.toString(getDetailSet().getInt("CART"));

    String[] mcols = {"CUSER", "CSKL", "VRDOK", "GOD", 
        "BRDOK", "CPAR", "DATDOK", "OPIS", "BRRAC"};
    
    String[] dcols = {"RBR", "CART", "CART1", "BC", "NAZART", 
        "JM", "KOL", "DC", "NC"};
    
    String q = "SELECT doku.cuser, doku.cskl, doku.vrdok, doku.god, " +
    		"doku.brdok, doku.cpar, doku.datdok, doku.opis, doku.brrac, " +
    		"stdoku.rbr, stdoku.cart, stdoku.cart1, stdoku.bc, " +
    		"stdoku.nazart, stdoku.jm, stdoku.kol, stdoku.dc, " +
    		"stdoku.nc FROM doku,stdoku WHERE " +
    		Util.getUtil().getDoc("doku", "stdoku") + 
    		" AND doku.cskl='" + getDetailSet().getString("CSKL") +
    		"' AND doku.god='" + getDetailSet().getString("GOD") +
    		"' AND doku.vrdok='PRK' AND stdoku.cart = " + art +
    		" AND (stdoku.veza is null OR stdoku.veza='')";
    System.out.println(q);
    		
    QueryDataSet doh = new QueryDataSet();
    Aus.setFilter(doh, q);
    List cols = new ArrayList();
    for (int i = 0; i < mcols.length; i++)
      cols.add(Doku.getDataModule().getColumn(mcols[i]).clone());
    for (int i = 0; i < dcols.length; i++)
      cols.add(Stdoku.getDataModule().getColumn(dcols[i]).clone());
    doh.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+
        MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    doh.setColumns((Column[]) cols.toArray(new Column[cols.size()]));
    doh.open();
    if (doh.rowCount() == 0) {
      JOptionPane.showMessageDialog(raDetail.getWindow(), 
          "Na skladištu nema nepovezanih stavki ulaza u ovoj godini!",
          "Prijenos", JOptionPane.ERROR_MESSAGE);
      return;
    }

    doh.setSort(new SortDescriptor(new String[] {"DATDOK", "RBR"}));
    
    MasterDetailChooser mdc = new MasterDetailChooser(doh, "rot-dohvat-prk",
            mcols, dcols, new int[] {2, 4, 5, 6, 9,
            Aut.getAut().getCARTdependable(10, 11, 12), 13,14,15,17});
    mdc.addModifier(new raTableColumnModifier("CPAR",
         new String[] { "CPAR", "NAZPAR" }, dm.getPartneri()));
    
    if (mdc.show(raDetail.getWindow(), 
        "Dohvat stavke ulaza za povezivanje")) {
    	QueryDataSet ulaz = Stdoku.getDataModule().getTempSet(
    			Condition.whereAllEqual(Util.dkey, doh));
    	ulaz.open();
    	if (!AST.findStanjeFor(getDetailSet(), false)) {
    		JOptionPane.showMessageDialog(raDetail.getWindow(), 
            "Greška na stanju artikla!", "Povezivanje", JOptionPane.ERROR_MESSAGE);
        return;
    	}
    	rKD.stavka.Init();
      rKD.stavkaold.Init();
      rKD.stanje.Init();
      lc.TransferFromDB2Class(getDetailSet(), rKD.stavkaold);
      lc.TransferFromDB2Class(getDetailSet(), rKD.stavka);
      lc.TransferFromDB2Class(AST.gettrenSTANJE(), rKD.stanje);
      rKD.setVrzal(getMasterSet().getString("CSKL"));
      if (ulaz.getBigDecimal("KOL").compareTo(rKD.stavka.kol) == 0)
        rKD.pureCopySkladPartFrom(ulaz);
      else rKD.pureKalkSkladPartFrom(ulaz);
      rKD.KalkulacijaStanje("ROT");
      lc.TransferFromClass2DB(getDetailSet(), rKD.stavka);
      lc.TransferFromClass2DB(AST.gettrenSTANJE(), rKD.stanje);
      getDetailSet().setString("VEZA", ulaz.getString("ID_STAVKA"));
      ulaz.setString("VEZA", getDetailSet().getString("ID_STAVKA"));
      setDetailObr(true, new QueryDataSet[] {ulaz, AST.gettrenSTANJE()});
    }
  }

 public boolean DodatnaValidacijaDetail() {

   if (val.isEmpty(DP.jtfKOL)) return false;
   if (DP.jraFC.getText().length()==0 && val.isEmpty(DP.jraFC)) return false;
   if (manjeNula()) return false;
   return true;
 }
/*
 public String dodatak(String odabrano){
  String dodatakic = new String("");
  if (odabrano.equalsIgnoreCase("PON")){
      String cp = MP.panelBasic.jrfCPAR.getText();
      if (cp.length() == 0) {
        dodatakic= " and param like 'P%'";//cp = " and 1=0";
      } else {
        dodatakic = " and (cpar="+getMasterSet().getInt("CPAR")+
            " or cpar is null) and param like 'P%'";
      }
System.out.println("dodatkic");
    return dodatakic;
  } else {
    return super.dodatak(odabrano);
  }
}
*/


 public boolean ValidacijaMasterExtend(){
   
   /*
    * tweaking zbog radi onog dolje... ne bi se smjela osjetiti razlika u prformansama tipa pada istih
    */
   
   QueryDataSet allAboutPartnersUneedNow = hr.restart.util.Util.getNewQueryDataSet(
       "SELECT STATUS, CAGENT FROM PARTNERI WHERE CPAR=" + 
       getMasterSet().getInt("CPAR"),true);
   
   String statpar = allAboutPartnersUneedNow.getString("STATUS");
   
//   String statpar = 
//   hr.restart.util.Util.getNewQueryDataSet("SELECT STATUS FROM PARTNERI WHERE CPAR="+
//                  getMasterSet().getInt("CPAR"),true).getString("STATUS");
   
   /*
    * OK, ovo je sad dodano zbog radi Frtavera i njihove berzerkarije sa agentima crm-om...
    * uglavnom, parametar chkValidAgent je po difoltu N i ne bi smjelo bit problema sa ostalima...
    * ako bude, ja sveèano polažem svoj vrat na panj, tako da bilo ko može sjekiricom presjeæ ga po pola.
    */
   
   if (frmParam.getParam("robno", "chkValidAgent", "N", "Provjera autentiènosti agenta za partnera").equalsIgnoreCase("D")) {
     
     System.out.println("getMasterSet().getInt(\"CAGENT\")" + getMasterSet().getInt("CAGENT")); //XDEBUG delete when no more needed
     System.out.println("allAboutP....getInt(\"CAGENT\")" + allAboutPartnersUneedNow.getInt("CAGENT")); //XDEBUG delete when no more needed
     
     if (allAboutPartnersUneedNow.getInt("CAGENT") == 0){
       int nemaAgenta = JOptionPane.showConfirmDialog(null, new String[]{"Partner nama agenta!","Ako je agent unesen ruèno bit æe upisan.","Želite li nastaviti s unosom?"}, "Upozorenje", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
       if (nemaAgenta == JOptionPane.NO_OPTION) return false;
       else if (getMasterSet().getInt("CAGENT") == 0){
         this.getMasterSet().setInt("CAGENT", 0);
       }
     } else {

        if (getMasterSet().getInt("CAGENT") != allAboutPartnersUneedNow.getInt("CAGENT")) {
          dm.getAgenti().open();
          String tren = "";
          String potr = "";

          if (getMasterSet().getInt("CAGENT") == 0)
            tren = "Agent nije naveden,";
          else {
            lookupData.getlookupData().raLocate(dm.getAgenti(), "CAGENT", getMasterSet().getInt("CAGENT") + "");
            tren = "Naveden je agent " + dm.getAgenti().getString("NAZAGENT");
          }
          lookupData.getlookupData().raLocate(dm.getAgenti(), "CAGENT", allAboutPartnersUneedNow.getInt("CAGENT") + "");
          potr = "a trebao bi biti " + dm.getAgenti().getString("NAZAGENT");

          int konfirm = JOptionPane.showConfirmDialog(null, new String[]{tren, potr, "Promjeniti?"}, "Upozorenje", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);

          if (konfirm == JOptionPane.OK_OPTION) {
            this.getMasterSet().setInt("CAGENT", allAboutPartnersUneedNow.getInt("CAGENT"));
            System.out.println("schengao sam agenta :) "); // XDEBUG delete
          } else {
            System.out.println("Nisam schangao agenta :)) "); // XDEBUG delete
          }
        }
      }
    }

   if (statpar.equalsIgnoreCase("B") && !checkLimit) {
     if (javax.swing.JOptionPane.showConfirmDialog(null,
       "Partner je oznaèen za fakturiranje uz provjeru. Da li da nastavim s dokumentom ?","Greška",javax.swing.JOptionPane.OK_CANCEL_OPTION,
       javax.swing.JOptionPane.QUESTION_MESSAGE)==javax.swing.JOptionPane.OK_OPTION){
       MP.panelBasic.jrfCPAR.requestFocus();
       return true;
     } else {
       MP.panelBasic.jrfCPAR.requestFocus();
       return false;
     }
   } else if (statpar.equalsIgnoreCase("C") && !checkLimit) {

     javax.swing.JOptionPane.showMessageDialog(null,
       "Partner ima oznaku zabrane fakturiranja !","Obavijest",javax.swing.JOptionPane.INFORMATION_MESSAGE);
     MP.panelBasic.jtfDATDOK.requestFocus();
     return false;
   }
   return true;
 }
}

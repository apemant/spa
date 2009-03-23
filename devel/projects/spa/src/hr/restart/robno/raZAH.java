/****license*****************************************************************
**   file: raZAH.java
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
/*
 * Created on 2005.04.20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.robno;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JOptionPane;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.VarStr;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.util.raTransaction;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jb.util.TriStateProperty;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class raZAH extends raIzlazTemplate {
  private raCommonClass rcc = raCommonClass.getraCommonClass();
  
  raNavAction navZatvori = new raNavAction("Zatvori stavku", raImages.IMGDOWN, KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
      closeZah();
    }
  };
  
  public raZAH() {
    setPreSel((jpPreselectDoc) presZAH.getPres());

    master_titel = "Trebovanje";
    detail_titel_mno = "Stavke trebovanja";
    detail_titel_jed = "Stavka trebovanja";
    // Added by Siniša
    stozbroiti();
    ConfigViewOnTable();
    zamraciMaster(dm.getZagZah());
    zamraciDetail(dm.getStZah());
    setMasterSet(dm.getZagZah());
    setDetailSet(dm.getStZah());
    
    DP.rpcart.enableNameChange(true);
    
    MP.BindComp();
    DP.BindComp();
    raDetail.addOption(navZatvori, 4);
    
    raMaster.getJpTableView().addTableModifier(new raTableColumnModifier(
        "CRADNIK", new String[] {"CRADNIK", "IME", "PREZIME"}, dm.getRadnici()));
    raMaster.getJpTableView().addTableModifier(new raTableColumnModifier(
        "CORG", new String[] {"CORG", "NAZIV"}, dm.getOrgstruktura()));
    
  }
  
  public void RestSetup() {
    super.RestSetup();
    alterMP();
  }
  private void alterMP() {
    MP.panelDodatni.jlAgent.setText("Voditelj");
    MP.panelDodatni.jlKontaktOsoba.setText("Naruèitelj");
    MP.panelDodatni.jrfKO.setNavColumnName("CRADNIK");
    setRadniciKO();
    MP.panelDodatni.jrfKO.setColNames(new String[] { "PREZIME" });
    MP.panelDodatni.jrfNAZKO.setColumnName("PREZIME");
    MP.panelDodatni.jrfNAZKO.setNavColumnName("PREZIME");
    MP.panelDodatni.jlOPIS.setText("Dod. mjestu troška");
  }

  private void setRadniciKO() {
    MP.panelDodatni.jrfKO.setRaDataSet(dm.getRadnici());
    MP.panelDodatni.jrfNAZKO.setRaDataSet(dm.getRadnici());
  }
  public void initTmpDataSet() {
//    super.initTmpDataSet();
    setRadniciKO();
  }
  public void SetFocusNovi() {
    super.SetFocusNovi();
    setRadniciKO();
    setDefaultsKO();
  }
  private void setDefaultsKO() {
    getMasterSet().setInt("CKO", getDefCKO());
    getMasterSet().setInt("CAGENT", getDefCAG());
  }
  
  private int getDefCKO() {
    return Integer.parseInt(frmParam.getParam("robno", "defCKOTRE", "0", "Defaultna vrijednost narucitelja potpisnika u TRE", true));
  }
  private int getDefCAG() {
    return Integer.parseInt(frmParam.getParam("robno", "defCAGTRE", "0", "Defaultna vrijednost voditelja u TRE", true));
  }

  private void putDefaultsKO() {
    getDefCAG();
    getDefCKO();//da kreira parametre
    frmParam.setParam("robno", "defCKOTRE", getMasterSet().getInt("CKO")+"");
    frmParam.setParam("robno", "defCAGTRE", getMasterSet().getInt("CAGENT")+"");
  }
  public void AfterAfterSaveMaster(char mode) {
    super.AfterAfterSaveMaster(mode);
    if (mode == 'N') putDefaultsKO();
  }

  public void initialiser() {
    what_kind_of_dokument = "TRE";
  }
  
  public void MyaddIspisMaster() {
    raMaster.getRepRunner().addReport("hr.restart.robno.repZAH",
            "hr.restart.robno.repIzlazni","ZAH","Trebovanje");
/*    raMaster.getRepRunner().addReport("hr.restart.robno.repOTP",
            "hr.restart.robno.repOTP", "OTP", "Otpremnica");
    raMaster.getRepRunner().addReport("hr.restart.robno.repOTPvri",
            "hr.restart.robno.repOTPvri", "OTPvri",
            "Otpremnica vrijednosna");
    raMaster.getRepRunner().addReport("hr.restart.robno.repMxOTP",
            "Matri\u010Dni ispis otpremnice");
*/  }

  public void MyaddIspisDetail() {
    raDetail.getRepRunner().addReport("hr.restart.robno.repZAH",
        "hr.restart.robno.repIzlazni","ZAH","Trebovanje");
/*      raDetail.getRepRunner().addReport("hr.restart.robno.repOTP",
              "hr.restart.robno.repOTP", "OTP", "Otpremnica");
      raDetail.getRepRunner().addReport("hr.restart.robno.repOTPvri",
              "hr.restart.robno.repOTPvri", "OTPvri",
              "Otpremnica vrijednosna");
      raDetail.getRepRunner().addReport("hr.restart.robno.repMxOTP",
              "Matri\u010Dni ispis otpremnice");
*/  }
  
    public void ConfigViewOnTable() {
        setVisibleColsMaster(new int[] { 4, 5, 12, 13, 29 });
        setVisibleColsDetail(new int[] { 4,
                Aut.getAut().getCARTdependable(5, 6, 7), 8, 9, 10 });
    }

    public void zamraciMaster(DataSet ds) {
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
        ds.getColumn("KOL1").setCaption("Naruèeno");
        ds.getColumn("KOL2").setCaption("Isporuèeno");
    }

    public void stozbroiti() {
        set_kum_detail(false);
    }

    public void RestPanelSetup() {
        DP.addRestOnlyKol();
    }
    
    public boolean ValidacijaStanje() {
      return true;
    }
    
    public void SetFocusNoviExtends(){
      if (MP.panelZah.jrfCORG.getText().equals("")){
        MP.panelZah.jrfCORG.requestFocus();
      }
      else {
        MP.panelZah.jrfCORG.forceFocLost();
        MP.panelZah.jtfDATDOK.requestFocusLater();
      }
    }
    
    public boolean FirstPartValidDetail() {
      if (val.isEmpty(MP.panelZah.jrfCORG))
        return false;
      if (val.isEmpty(MP.panelZah.jrfCRADNIK))
        return false;
      if (val.isEmpty(MP.panelZah.jtfDATDOK))
          return false;
      if (val.isEmpty(MP.panelZah.jtfDATDOSP))
        return false;
      
      return true;
    }
    
    public boolean LocalDeleteCheckDetail() {
      if (!getDetailSet().getString("STATUS").equalsIgnoreCase("N")) {
        JOptionPane.showMessageDialog(raDetail.getWindow(),
            "Stavka se ne može brisati jer je veæ naruèena!", "Greška",
            javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
      }
      return true;
    }
    
    public void enableDetailNavBar() {
      super.enableDetailNavBar();
      if (getDetailSet().rowCount() > 0 &&
          !getDetailSet().getString("STATUS").equalsIgnoreCase("N")) {
        raDetail.getNavBar().getStandardOption(raNavBar.ACTION_DELETE).setEnabled(false);
        raDetail.getNavBar().getStandardOption(raNavBar.ACTION_UPDATE).setEnabled(false);
      }
    }
    
    void closeZah() {
      if (getDetailSet().getRowCount() == 0) return;
      if (!getDetailSet().getString("STATUS").equals("N")) {
        JOptionPane.showMessageDialog(raDetail.getWindow(),
            "Stavka je veæ prenesena!", "Greška",
            javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
      }
      String arts = frmParam.getParam("robno", "manArt", "", 
          "Popis artikala koji se mogu ruèno zatvoriti na trebovanju");
      
      String[] ar = new VarStr(arts).splitTrimmed(',');
      HashSet ars = new HashSet(Arrays.asList(ar));
      
      if (!ars.contains(Integer.toString(getDetailSet().getInt("CART")))) {
        JOptionPane.showMessageDialog(raDetail.getWindow(),
            "Artikl se ne može ruèno zatvoriti!", "Greška",
            javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
      }
      
      if (JOptionPane.showConfirmDialog(raDetail.getWindow(),
            "Želite li zatvoriti stavku trebovanja?", "Zatvaranje",
            javax.swing.JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
        getDetailSet().setString("STATUS", "P");
        getDetailSet().saveChanges();
        
        DataSet nonp = stdoki.getDataModule().getTempSet(Condition.whereAllEqual(
            Util.mkey, getDetailSet()).and(Condition.equal("STATUS", "N")));
        nonp.open();
        
        if (nonp.rowCount() == 0) {
          // postavi status mastera na prenesen
          getMasterSet().setString("STATIRA", "P");
          getMasterSet().saveChanges();
        }
      }
    }
    
    public boolean isKPR() {
      return false;
    }
}

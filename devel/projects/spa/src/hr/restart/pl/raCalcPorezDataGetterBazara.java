/****license*****************************************************************
**   file: raCalcPorezDataGetterBazara.java
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
 * raCalcPorezDataGetterBazara.java
 *
 * Created on 2003. prosinac 09, 15:17
 */

package hr.restart.pl;

import hr.restart.swing.JraButton;
import hr.restart.swing.layout.raXYConstraints;
import hr.restart.swing.layout.raXYLayout;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raImages;
import hr.restart.util.startFrame;

import java.math.BigDecimal;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 *
 * @author  andrej
 */
public class raCalcPorezDataGetterBazara extends hr.restart.pl.raCalcPorezDataGetter {
  jpCalcPorez getpanel;
  JlrNavField jlrCradnik = new JlrNavField();
  JlrNavField jlrPrezime = new JlrNavField();
  JlrNavField jlrIme = new JlrNavField();
  StorageDataSet dset = new StorageDataSet();
  JraButton jbSelCradnik = new JraButton();
  private String cradnik;
  private QueryDataSet radnicipl;
  /** Creates a new instance of raCalcPorezDataGetterBazara */
  public raCalcPorezDataGetterBazara() {
    startFrame.getStartFrame();
  }
  
  public javax.swing.ImageIcon getImageIcon() {
    return raImages.getImageIcon(raImages.IMGDLGXUSER);
  }
  public void setFrmCalcPorez(frmCalcPorez f) {
    super.setFrmCalcPorez(f);
    getpanel = new jpCalcPorez(f);
    initPanel();
  }
  private void initPanel() {
    dset.addColumn("CRADNIK", "Matièni broj", Variant.STRING);
    dset.addColumn("IME", Variant.STRING);
    dset.addColumn("PREZIME", Variant.STRING);
    dset.open();
    jbSelCradnik.setText("...");
    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(dset);
    jlrCradnik.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime});
    jlrCradnik.setVisCols(new int[] {0, 1, 2});
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig());
    jlrCradnik.setNavButton(jbSelCradnik);
    
    jlrIme.setColumnName("IME");
    jlrIme.setNavProperties(jlrCradnik);
    jlrIme.setSearchMode(1);
    
    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrCradnik);
    jlrPrezime.setSearchMode(1);
    raXYLayout lay = new raXYLayout(false);
    lay.setMargins(0,0);
    JPanel jpCradnik = new JPanel(lay);
    jpCradnik.add(jlrCradnik,new raXYConstraints(0, 0, 100, -1));
    jpCradnik.add(jbSelCradnik,new raXYConstraints(105, 0, 21, 21));
    
    getpanel.builder.appendSeparator("Dohvat podataka radnika iz plaæe");
    getpanel.builder.append("Matièni broj", jpCradnik,3);
    getpanel.builder.append("Prezime", jlrPrezime,3);
    getpanel.builder.append("Ime", jlrIme, 3);
    getpanel.addAncestorListener(new AncestorListener() {
      public void ancestorAdded(AncestorEvent event) {
        changeOkp();
      }
      public void ancestorRemoved(AncestorEvent event) {
      }
      public void ancestorMoved(AncestorEvent event) {
      }
    });
  }
  private void changeOkp() {
    getFrmCalcPorez().getOKPanel().jBOK.setText("Dohvat");
    getFrmCalcPorez().getOKPanel().jBOK.setIcon(raImages.getImageIcon(raImages.IMGREFRESH));
  }
  public boolean validateData() {
    if (Valid.getValid().isEmpty(jlrCradnik)) return false;
    String newcradnik = dset.getString("CRADNIK");
    if (newcradnik.equals(cradnik)) return false;
    cradnik = newcradnik;
    radnicipl = hr.restart.baza.Radnicipl.getDataModule().getTempSet(hr.restart.baza.Condition.equal("CRADNIK",cradnik));
    radnicipl.open();
    if (radnicipl.getRowCount() == 0) {
      JOptionPane.showMessageDialog(jlrCradnik, "Djelatnik nije prijavljen u plaæama","Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  private BigDecimal[] stopepor;
  private String getParamPL(String colname) {
    hr.restart.baza.dM.getDataModule().getParametripl().open();
    hr.restart.baza.dM.getDataModule().getParametripl().first();
    return hr.restart.baza.dM.getDataModule().getParametripl().getBigDecimal(colname).toString();
  }
  public String getValue(String identifier) {
    if (identifier.equals("Bruto")) {
      return radnicipl.getBigDecimal("BRUTOSN").toString();
    }
    
    if (identifier.equals("Iskorištena olakšica")) {}
    if (identifier.equals("Iznos poreza1")) {}
    if (identifier.equals("Iznos poreza2")) {}
    if (identifier.equals("Iznos poreza3")) {}
    if (identifier.equals("Iznos poreza4")) {}
    if (identifier.equals("Iznos poreza5")) {}
    if (identifier.equals("Iznos1")) {}
    if (identifier.equals("Iznos2")) {}
    if (identifier.equals("Iznos3")) {}
    if (identifier.equals("Koeficijent")) {}
    if (identifier.equals("Korekcija-31860")) {
//      return hr.restart.sisfun.frmParam.getParam("pl","nbkmxs-".concat(cradnik), "1.000000", "Koef. za nto u bto kod 31860 sindroma za ".concat(cradnik));
    }
    if (identifier.equals("Koeficijent olakšice")) {
      QueryDataSet olak = raOdbici.getInstance().getOlaksice(cradnik, raOdbici.DEF);
      if (olak.getRowCount() == 0) return "1.00";
      BigDecimal koefol = Aus.zero2;
      for (olak.first(); olak.inBounds(); olak.next()) {
        koefol = koefol.add(olak.getBigDecimal("STOPA"));
      }
      if (koefol.compareTo(Aus.zero2) == 0) return "0.00";
      return koefol.add(new BigDecimal("1.00")).toString();
    }
    if (identifier.equals("Maksimalna za MIO 1. stup")) {
      return hr.restart.sisfun.frmParam.getParam("pl","maxosn1", "0.00", "Maksimalna osnovica za doprinos 1");
    }
    if (identifier.equals("Maksimalna za MIO 2. stup")) {
      return hr.restart.sisfun.frmParam.getParam("pl","maxosn2", "0.00", "Maksimalna osnovica za doprinos 1");
    }
    if (identifier.equals("Minimalna za doprinose")) {
      return getParamPL("MINOSDOP");
    }
    if (identifier.equals("Neto")) {
      
    }
    if (identifier.equals("Opis1")) {
      return "MIO 1. stup";
    }
    if (identifier.equals("Opis2")) {
      return "MIO 2. stup";
    }
    if (identifier.equals("Opis3")) {
      
    }
    if (identifier.equals("Osnovica1")) {
      
    }
    if (identifier.equals("Osnovica2")) {
      
    }
    if (identifier.equals("Osnovica3")) {
      
    }
    if (identifier.equals("Osnovna olakšica")) {
      return getParamPL("MINPL");
    }
    if (identifier.equals("Plaæa po odbitku doprinosa")) {
    }
    if (identifier.equals("Porez1")) {
      return "Porez 1";
    }
    if (identifier.equals("Porez2")) {
      return "Porez 2";
    }
    if (identifier.equals("Porez3")) {
      return "Porez 3";
    }
    if (identifier.equals("Porez4")) {
      return "Porez 4";
    }

    if (identifier.equals("Por.osnovica1")) {}
    if (identifier.equals("Por.osnovica2")) {}
    if (identifier.equals("Por.osnovica3")) {}
    if (identifier.equals("Por.osnovica4")) {}
    if (identifier.equals("Por.osnovica5")) {}
    if (identifier.equals("Porez 1")) {
      return getParamPL("OSNPOR1");
    }
    if (identifier.equals("Porez 2")) {
      return getParamPL("OSNPOR2");
    }
    if (identifier.equals("Porez 3")) {
      return getParamPL("OSNPOR3");
    }
    if (identifier.equals("Porez i prirez")) {
      
    }

    if (identifier.equals("Porezna osnovica")) {}
    if (identifier.equals("Premije osiguranja")) {
      QueryDataSet prem = raOdbici.getInstance().getPremije(cradnik, raOdbici.DEF);
      BigDecimal iznprem = Aus.zero2;
      for (prem.first(); prem.inBounds(); prem.next()) {
        iznprem = iznprem.add(prem.getBigDecimal("IZNOS"));
      }
      return iznprem.toString();
    } 
    
    if (identifier.equals("Prirez")) {}
    
    if (identifier.equals("Stopa poreza1")) {
      if (stopepor == null) stopepor = raObracunPL.getInstance().getStopePoreza(cradnik);
      return stopepor[0].multiply(new BigDecimal(100)).toString();      
    }
    if (identifier.equals("Stopa poreza2")) {
      if (stopepor == null) stopepor = raObracunPL.getInstance().getStopePoreza(cradnik);
      return stopepor[1].multiply(new BigDecimal(100)).toString();      
    }
    if (identifier.equals("Stopa poreza3")) {
      if (stopepor == null) stopepor = raObracunPL.getInstance().getStopePoreza(cradnik);
      return stopepor[2].multiply(new BigDecimal(100)).toString();      
    }
    if (identifier.equals("Stopa poreza4")) {
      if (stopepor == null) stopepor = raObracunPL.getInstance().getStopePoreza(cradnik);
      return stopepor[3].multiply(new BigDecimal(100)).toString();      
    }
    
    if (identifier.equals("Stopa poreza5")) {}
    
    if (identifier.equals("Stopa prireza")) {
      QueryDataSet q = raOdbici.getInstance().getPrirez(cradnik, raOdbici.DEF);
      if (q.getRowCount() == 0) return "0.00";
      q.first();
      return q.getBigDecimal("STOPA").toString();    
    }
    
    if (identifier.equals("Stopa1")) {
      QueryDataSet ds = raOdbici.getInstance().getDoprinosiRadnik(cradnik, raOdbici.DEF);
      ds.open();
      for (ds.first(); ds.inBounds(); ds.next()) {
        if (ds.getShort("CVRODB") == 1) {
          return ds.getBigDecimal("STOPA").toString();
        }
      }
      return "0.00";
    }
    if (identifier.equals("Stopa2")) {
      QueryDataSet ds = raOdbici.getInstance().getDoprinosiRadnik(cradnik, raOdbici.DEF);
      ds.open();
      for (ds.first(); ds.inBounds(); ds.next()) {
        if (ds.getShort("CVRODB") == 2) {
          return ds.getBigDecimal("STOPA").toString();
        }
      }
      return "0.00";     
    }
    if (identifier.equals("Stopa3")) {}
    if (identifier.equals("Ukupno olakšica")) {}
    
    return "0.00";
  }
  public java.awt.Container getContentPane() {
    return getpanel.getContentPane();
  }
  public String getTitle() {
    return "Dohvat djelatnika";
  }
}

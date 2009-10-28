/****license*****************************************************************
**   file: jpVlasnik.java
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
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpVlasnik extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmVlasnik fVlasnik;
  DataSet resolvSet;
  StorageDataSet dummySet;
  private boolean updated = false;

  private boolean allowedupdated = true;

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlAdr = new JLabel();
  JLabel jlCkupac = new JLabel();
  JLabel jlJmbg = new JLabel();
  JLabel jlPrezime = new JLabel();
  JraTextField jraAdr;
  public JraTextField jraCkupac;
  JraTextField jraEmadr;
  public JraTextField jraIme;
  JraTextField jraJmbg;
  JlrNavField jraMj;
  JlrNavField jraPbr;
  JraButton jbSelMj;
  JraTextField jraPrezime;
  JraTextField jraTel;
  JraButton jbGetKup;

  private int leftMargin = 0;
  private Insets insets;

  public jpVlasnik(frmVlasnik f) {
    this(f, 0, new Insets(15,0,15,5));
  }

  public jpVlasnik(frmVlasnik f, int _margin, Insets _insets) {
    try {
      fVlasnik = f;
      resolvSet = null;
      leftMargin = _margin;
      insets = _insets;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public jpVlasnik(DataSet _ds) {
    this(_ds,0,null);
  }

  public jpVlasnik(DataSet _ds, int _margin, Insets _insets) {
    try {
      resolvSet = _ds;
      fVlasnik = null;
      leftMargin = _margin;
      insets = _insets;
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public jpVlasnik() {
    this(new StorageDataSet(),-5, new Insets(5, 0, 0, 5));//instanciraju ga ovako uglavnom kada ga zele dodati u postojeci panel pa bezi pet u desno
    ((StorageDataSet)resolvSet).addColumn("CKUPAC",Variant.INT);
    resolvSet.open();
  }

  private int imewidth;

  private void jbInit() throws Exception {
    if (insets == null) insets = new Insets(0, 0, 0, 0);
    if (fVlasnik==null && resolvSet==null) throw new IllegalArgumentException("U kostruktoru ne smije biti null");
    jpDetail.setLayout(lay);
//    lay.setWidth(660);
//    lay.setHeight(135);
    jlAdr.setText("Adresa");
    jlCkupac.setText("Oznaka / Ime / Naziv");
    jlJmbg.setText("JMBG / Tel / E-Mail");
    jlPrezime.setText("Prezime");
    imewidth = 400-15;
    if (fVlasnik==null) {
      imewidth =  400-15-21-5; //button i razmak
      jraCkupac = createNavField();
      jraAdr = createNavField();
      jraEmadr = createNavField();
      jraMj = createNavField();
      jraIme = createNavField();
      jraPrezime = createNavField();
      jraJmbg = createNavField();
      jraPbr = createNavField();
      jraTel = createNavField();
      jraMj.setColumnName("MJ");
      jbSelMj = null;
      jbGetKup = new JraButton();
    } else {
      jraAdr = new JraTextField();
      jraCkupac = new JraTextField();
      jraEmadr = new JraTextField();
      jraIme = new JraTextField();
      jraPrezime = new JraTextField();
      jraJmbg = new JraTextField();
      jraTel = new JraTextField();
      jbSelMj = new JraButton();
      jraPbr = new JlrNavField();
      jraMj = new JlrNavField();
      jraPbr.setFocusLostOnShow(false);
      jraPbr.setRaDataSet(dm.getMjesta());
      jraPbr.setTextFields(new JTextComponent[] {jraMj});
      jraPbr.setColNames(new String[] {"NAZMJESTA"});
      jraPbr.setSearchMode(3);
      jraPbr.setNavButton(jbSelMj);
      jraPbr.setVisCols(new int[] {2,1});
      jraMj.setFocusLostOnShow(false);
      jraMj.setColumnName("MJ");
      jraMj.setNavColumnName("NAZMJESTA");
      jraMj.setNavProperties(jraPbr);
      jraMj.setSearchMode(1);
    }
    jraAdr.setColumnName("ADR");
    jraCkupac.setColumnName("CKUPAC");
    jraEmadr.setColumnName("EMADR");
    jraIme.setColumnName("IME");
    jraJmbg.setColumnName("JMBG");
    
    jraPbr.setColumnName("PBR");
    jraPrezime.setColumnName("PREZIME");
    jraTel.setColumnName("TEL");
    if (fVlasnik==null) {
      dummySet = new StorageDataSet();
      dummySet.setColumns(
        hr.restart.util.Util.getUtil().cloneCols(dm.getKupci().getColumns()));
      dummySet.open();
      rebind(dummySet);
      JlrNavField jlrCkupac = (JlrNavField)jraCkupac;
      jlrCkupac.setRaDataSet(dm.getKupci());
      jlrCkupac.setColNames(
        new String[] {"ADR", "EMADR", "MJ", "IME", "PREZIME", "JMBG", "PBR", "TEL"});
      jlrCkupac.setVisCols(new int[] {0,1,2});
      jlrCkupac.setTextFields(
        new javax.swing.text.JTextComponent[] {jraAdr,jraEmadr,jraMj,jraIme,jraPrezime,jraJmbg,jraPbr,jraTel});
      jlrCkupac.setSearchMode(0);
      //jlrCkupac.setDataSet(resolvSet);
      prepareNavField(jraAdr, jlrCkupac);
      prepareNavField(jraEmadr, jlrCkupac);
      prepareNavField(jraMj, jlrCkupac);
      prepareNavField(jraIme, jlrCkupac);
      prepareNavField(jraPrezime, jlrCkupac);
      prepareNavField(jraJmbg, jlrCkupac);
      prepareNavField(jraPbr, jlrCkupac);
      prepareNavField(jraTel, jlrCkupac);

      jlrCkupac.setNavButton(jbGetKup);

    } else {
      rebind(fVlasnik.getRaQueryDataSet());
    }
    createPanel(leftMargin, imewidth);
  }
  private void prepareNavField(JraTextField field, JlrNavField jlrCkupac) {
    if (!(field instanceof JlrNavField)) return;
    JlrNavField jlrfield = (JlrNavField)field;
    jlrfield.setSearchMode(1);
    jlrfield.setSearchMode(jlrfield.NULL, jlrfield.getSearchModeF9());
    jlrfield.setNavProperties(jlrCkupac);
    jlrfield.setFocusLostOnShow(false);
  }
  private JlrNavField createNavField() {
    return new JlrNavField() {
        public void after_lookUp() {
          aftLook(this);
        }
      };
  }
  private void createPanel(int left, int imewidth) {
    jpDetail.add(jlAdr, new XYConstraints(15+left, 50, -1, -1));
    jpDetail.add(jlCkupac, new XYConstraints(15+left, 0, -1, -1));
    jpDetail.add(jlJmbg, new XYConstraints(15+left, 75, -1, -1));
    jpDetail.add(jlPrezime, new XYConstraints(15+left, 25, -1, -1));

    jpDetail.add(jraCkupac, new XYConstraints(150+left, 0, 100, -1));
    jpDetail.add(jraIme, new XYConstraints(255+left, 0, imewidth, -1));//imewidth-15

    if (jbGetKup != null)
      jpDetail.add(jbGetKup, new XYConstraints(255+400-15-21+leftMargin, 0, 21, 21));


    jpDetail.add(jraPrezime, new XYConstraints(150+left, 25, 505-15, -1));

    jpDetail.add(jraAdr, new XYConstraints(150+left, 50, 265-5, -1));
    jpDetail.add(jraPbr, new XYConstraints(420+left-5, 50, 65-5, -1));
    if (jbSelMj == null)
      jpDetail.add(jraMj, new XYConstraints(490+left-10, 50, 165-5, -1));
    else {
      jpDetail.add(jraMj, new XYConstraints(490+left-10, 50, 165-10-21, -1));
      jpDetail.add(jbSelMj, new XYConstraints(255+400-15-21+leftMargin, 50, 21, 21));
    }

    jpDetail.add(jraJmbg, new XYConstraints(150+left, 75, 165-5, -1));
    jpDetail.add(jraTel, new XYConstraints(320+left-5, 75, 165-5, -1));
    jpDetail.add(jraEmadr, new XYConstraints(490+left-10, 75, 165-5, -1));
    
    this.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    this.add(jpDetail, BorderLayout.CENTER);
    //test
/*    jraIme.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
      public void insertUpdate(javax.swing.event.DocumentEvent e) {
        if (jraIme.getText().equals("")) System.out.println("insertUpdate - obrisano");
      }
      public void removeUpdate(javax.swing.event.DocumentEvent e) {
        if (jraIme.getText().equals("")) System.out.println("removeUpdate - obrisano");
      }
      public void changedUpdate(javax.swing.event.DocumentEvent e) {
        if (jraIme.getText().equals("")) System.out.println("changedUpdate - obrisano");
      }
    });*/
    //etest
  }

  public void rebind(DataSet ds) {
    jraAdr.setDataSet(ds);
    jraCkupac.setDataSet(ds);
    jraEmadr.setDataSet(ds);
    jraIme.setDataSet(ds);
    jraJmbg.setDataSet(ds);
    jraMj.setDataSet(ds);
    jraPbr.setDataSet(ds);
    jraPrezime.setDataSet(ds);
    jraTel.setDataSet(ds);
  }



  public boolean isUpdated() {
    return updated;
  }

  public void setAllowedUpdated(boolean u) {
    allowedupdated = u;
  }
  public void setUpdated(boolean u) {
    updated = u;
    if (allowedupdated) {
    rcc.setLabelLaF(jraCkupac,!updated);
    }
  }

  void aftLook(JlrNavField jlr) {
//    System.out.println("jpVlasnik.aftLook! field "+jlr.getColumnName());
//    System.out.println("jpVlasnik.aftLook! ckupac = "+jraCkupac.getDataSet().getInt("CKUPAC"));
//    if (jraCkupac.getDataSet().getInt("CKUPAC") == 0) new Throwable().printStackTrace();
//    if (!jraIme.isEnabled()) {
//      System.out.println("browse mode?");
//      return; //neki browse mode
//    }
//    System.out.println("nije browse mode!!!");
    setUpdated(jlr.isLastLookSuccessfull());
  }
  public int getKupac() {
    return jraCkupac.getDataSet().getInt("CKUPAC");
  }
  public boolean updateRecords() {
    if (Valid.getValid().chkIsEmpty(jraIme)) return false;
    try {
      int ckupac = jraCkupac.getDataSet().getInt("CKUPAC");
      System.out.println("ckupac = "+ckupac);
      
      QueryDataSet kupacSet = Kupci.getDataModule().getTempSet(Condition.equal("CKUPAC",ckupac));
      kupacSet.open();
/*
      QueryDataSet kupacSet = hr.restart.util.Util.getNewQueryDataSet(
      "SELECT * FROM Kupci where ckupac="+ckupac,true);
*/      
      if (kupacSet.getRowCount() == 0) {
        ckupac = frmVlasnik.getNextCKupac();
        kupacSet.insertRow(false);
      }
      dummySet.setInt("CKUPAC", ckupac);
      dummySet.setString("AKTIV", "D");
      dummySet.setString("LOKK", "N");
      dummySet.copyTo(kupacSet);
      kupacSet.saveChanges();
      dm.getSynchronizer().markAsDirty("kupci"); // ab.f. setiraj flag da je tablica promijenjena
//      if (kupacSet instanceof raDataSet)
//        ((raDataSet) kupacSet).forget();
/*      
      resolvSet.open(); // OVU LINIJU NI ZA DRAGOG BOGA 
      NE ODKOMENTIRAVATI ILI NEKU OVAKVU GLUPOST NAPISATI JER ODJEBE SVE U RAMASTERDETAILU
*/      
      resolvSet.setInt("CKUPAC", ckupac);
      
/*      if (kupacSet instanceof raDataSet) {
        System.out.println("propagating changes for kupci");
        ((raDataSet)kupacSet).propagateChanges();
      } else {
        System.out.println("refreshing dm.getKupci");
        dm.getKupci().refresh();
      } */

      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
  
  public void setFromSet(DataSet ds) {
    dummySet.setInt("CKUPAC", ds.getInt("CKUPAC"));
    if (ds.getInt("CKUPAC") != 0) {
      ((JlrNavField) jraCkupac).forceFocLost();
      if (!((JlrNavField) jraCkupac).isLastLookSuccessfull())
        ((JlrNavField) jraCkupac).setNormalColors();
    } else {
      dummySet.setUnassignedNull("CKUPAC");
      jraCkupac.setText("");
      ((JlrNavField) jraCkupac).setNormalColors();
      ((JlrNavField) jraCkupac).emptyTextFields();
      setUpdated(false);
    }
  }

  public void setDataSet(DataSet ds) {
//    if (resolvSet != null) {
//      resolvSet.removeNavigationListener(resolvListener);
//    }
    resolvSet = ds;
    jraCkupac.setDataSet(resolvSet==null?dummySet:resolvSet);
//    if (resolvSet != null) {
//      resolvSet.addNavigationListener(resolvListener);
//    }
  }

//  NavigationListener resolvListener = new NavigationListener() {
//    public void navigated(NavigationEvent event) {
//      if (dummySet == null) return;
//      if (resolvSet == null) return;
//      if (!resolvSet.isOpen()) return;
//      //event.
//      dummySet.open();
//      dummySet.setInt("CKUPAC", resolvSet.getInt("CKUPAC"));
//      dummySet.post();
//      System.out.println("setting dummySet.CKUPAC na "+dummySet.getInt("CKUPAC"));
//      System.out.println("jraCKUPAC.getText = "+jraCkupac.getText());
//      System.out.println("jraCKUPAC.getValue = "+jraCkupac.getDataSet().getInt("CKUPAC"));
//    }
//  };
}

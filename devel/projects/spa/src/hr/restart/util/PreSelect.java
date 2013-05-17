/****license*****************************************************************
**   file: PreSelect.java
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
package hr.restart.util;

import hr.restart.baza.Condition;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.swing.JraToggleButton;
import hr.restart.swing.raButtonGroup;
import hr.restart.swing.raDatePopup;
import hr.restart.swing.raDateRange;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.ColumnAware;
import com.borland.dx.dataset.Variant;
/**
 * Title:        Predselekcijski Dialog
 * <pre>
 * Služi za unos predselekcijskih podataka, nakon toga filterira {@link #setSelDataSet(com.borland.dx.dataset.DataSet) zadani dataset}
 * i metodom {@link #copySelValues() copySelValues} u SetFokus() metodi raMatPodaci unosi zadane vrijednosti u predselektirani dataset
 * NAJVAZNIJE!!!: {@link #setSelPanel(JPanel)} uvijek pozvati nakon sto su svi parametri setirani
 *
 * PRIMJER 1: Prikazuje se predselekcijski ekran sa jednim JraTextFieldom
 * public class frmTecajevi extends raMatPodaci {
 *  JPanel jselp = new JPanel();
 *  PreSelect psFrm = new PreSelect() {
 *    public void SetFokus() {
 *      jtDATUMVAL.requestFocus();
 *    }
 *    public boolean Validacija() {
 *      return !Vl.isEmpty(jtDATUMVAL);
 *    }
 *  };
 *  JraTextField jtDATUMVAL = new JraTextField();
 * ......
 *  private void jbInit() {
 * ......
 *    jtDATUMVAL.setColumnName("DATVAL");
 *    jtDATUMVAL.setDataSet(dm.getTecajevi());
 *    jselp.add(jtDATUMVAL,...);
 *    psFrm.setSelDataSet(dm.getTecajevi());
 *    psFrm.setSelPanel(jselp);
 * ......
 *  }
 * ......
 *  public void SetFokus(char mode) {
 *   if (mode=='N') {
 *     psFrm.copySelValues();
 * ......
 * // Ovo je akcija na gumbic koji prikazuje predselekcijski ekran
 * // - this je owner kojeg on prikazuje nakon uspjesne predselekcije ili skriva nakon neuspjesne
 *  void jBpreds_actionPerformed(ActionEvent e) {
 *   psFrm.showPreselect(this,"Datum tecaja");
 *  }
 * //STARTFRAME:
 * // potrebno je tako pozvati i taj ekran iz startFrame-a
 * void jzpMenuTec_actionPerformed(ActionEvent e) {
 *    ftecajevi.psFrm.showPreselect(ftecajevi,"Datum tecaja");
 * //a ne ovo:  showFrame(ftecajevi);
 * }
 *
 * PRIMJER 2: Predselekcijski ekran se ne prikazuje nego se simulira predselekcija da bi se napravio filter
 * svejedno je potrebno napraviti predselekcijski panel koji sadrzi polja za selekciju. Tu imamo panel (jpFilterZT)
 * u kojem su JraTextFieldovi koji su bindani na kolone CSKL,VRDOK,BRDOK i RBR
 * kod bi isao ovako:
 *   PreSelect pres = new PreSelect()
 * .....
 *   dm.getVtzavtr().open();
 *   dm.getDoku().open();
 *   pres.setSelDataSet(dm.getVtzavtr());
 *   pres.setSelPanel(jpFilterZT);
 *   pres.getSelRow().open();
 *   pres.getSelRow().setString("CSKL",dm.getDoku().getString("CSKL"));
 *   pres.getSelRow().setString("VRDOK",dm.getDoku().getString("VRDOK"));
 *   pres.getSelRow().setString("BRDOK",dm.getDoku().getString("BRDOK"));
 *   pres.getSelRow().setShort("RBR",(short) 0);
 *   pres.doSelect();
 *
 * </pre>
 */

public class PreSelect implements ResetEnabled {//extends JDialog {//ali ne prikazuje taj dialog
sysoutTEST ST= new sysoutTEST(false);
  Util Ut = Util.getUtil();
  Valid Vl = Valid.getValid();
  startFrame SF;
  OKpanel okpanel = new OKpanel() {
    public void jBOK_actionPerformed() {
      okSelect();
    }
    public void jPrekid_actionPerformed() {
      cancelSelect();
    }
  };
  selFilter selfilter1;
  BorderLayout borderLayout1 = new BorderLayout();
  private JPanel selPanel=null;
  private selRange[] selRanges;
  private com.borland.dx.dataset.DataSet selDataSet;
  com.borland.dx.dataset.StorageDataSet selRow;
  java.util.LinkedList ll;
//  java.util.Vector ll; 
  private static final String FROMSUFIX = "-from";
  private static final String TOSUFIX = "-to";
  private boolean selCanceled;
  private boolean preSelShowed=false;
  private boolean selNull = true;
  private boolean SQLFilter = true;
  private boolean userQuestion = true;
  private JDialog preSelDialog;
  private Object psowner;
  private JPanel jpUser;

  private JraToggleButton jtbUser;
//  private JraToggleButton jtbAll;

  private raButtonGroup rbgUser;
  

  public PreSelect() {
    initUserPanel();
  }
  public static String getFROMSUFIX() {
    return FROMSUFIX;
  }
  public static String getTOSUFIX() {
    return TOSUFIX;
  }
  public void initPreSeldialog() {
//    if (preSelDialog == null) {
      Object win = getSwingOwner();
      if (win == null) {
        preSelDialog = new JraDialog();
      } else if (win instanceof java.awt.Frame) {
        preSelDialog = new JraDialog((Frame)win);
      } else if (win instanceof java.awt.Dialog) {
        preSelDialog = new JraDialog((Dialog)win);
      }
//    }
    jInit();
  }

  private void addUserPanel() {
    if (jpUser!=null) okpanel.remove(jpUser);
    if (!isUserQuestion()) return;
    if (getSelDataSet().hasColumn("CUSER") == null) return;
    okpanel.add(jpUser,BorderLayout.WEST);
  }

  private void initUserPanel() {
    jpUser = new JPanel(new GridLayout(1,2));
    jtbUser = new JraToggleButton();
    jtbUser.setSelectedIcon(raImages.getImageIcon(raImages.IMGCHANGE));
    jtbUser.setIcon(raImages.getImageIcon(raImages.IMGPREVIEW));
    jtbUser.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        boolean s = e.getStateChange() == e.SELECTED;
        ((JraToggleButton)e.getSource()).setText(s?"Moji dokumenti":"Svi dokumenti");
      }
    });
//    rbgUser = new raButtonGroup(SwingConstants.RIGHT,SwingConstants.CENTER);
//    rbgUser.add(jtbUser,"Moji dokumenti","U");
//    rbgUser.add(jtbAll,"Svi dokumenti","A");
    jtbUser.setSelected(isUserSelected());
    jtbUser.setText(isUserSelected()?"Moji dokumenti":"Svi dokumenti");
    jtbUser.setEnabled(isUserEnabled());
    jpUser.add(jtbUser);
//    jpUser.add(jtbAll);
  }
  private boolean isUserEnabled() {
    return hr.restart.sisfun.frmParam.getParam("sisfun", "mojiDokEn", "D", 
        "Smije li na user predselekcijama odabrati 'Moji dokumenti' ili 'Svi dokumenti' (D/N)")
    .equals("D");
  }
  private boolean isUserSelected() {
    return hr.restart.sisfun.frmParam.getParam("sisfun", "mojiDok", "D", "Da li je na user predselekcijama inicijalno odabran 'Moji dokumenti' (D/N)")
      .equals("D");
  }

  public boolean isUserQuestion() {
    return userQuestion;
  }

  public void setUserQuestion(boolean q) {
    userQuestion = q;
  }

  private void jInit() {
    preSelDialog.getContentPane().setLayout(borderLayout1);
    okpanel.setPreferredSize(new Dimension(138, 25));
    preSelDialog.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        this_componentShown(e);
      }
    });
    okpanel.registerOKPanelKeys(preSelDialog);
    addUserPanel();
    addSelPanel();
    preSelDialog.getContentPane().add(okpanel, BorderLayout.SOUTH);
    preSelDialog.setModal(true);

    // (ab.f) zatvaranje prselect prozora na botun X
    preSelDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    preSelDialog.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        cancelSelect();
      }
    });
  }

  private Object getSwingOwner() {
    if (System.getProperty("os.name").startsWith("Mac")) return null;
      
    if (getPSOwner() instanceof raMasterDetail)
      return ((raMasterDetail)getPSOwner()).raMaster.getWindow();
    if (getPSOwner() instanceof raFrame) return ((raFrame)getPSOwner()).getWindow();
    return getPSOwner();
  }
  private void addSelPanel() {
    if (selPanel != null) {
      preSelDialog.getContentPane().remove(selPanel);
      preSelDialog.getContentPane().add(selPanel,BorderLayout.CENTER);
    }
  }
  void findSF(Object o) {
    if (SF==null) {
      if (o!=null) {
        SF = raLLFrames.getRaLLFrames().getMsgStartFrame(o.getClass());//SF.getStartFrame();
      } else {
        SF = raLLFrames.getRaLLFrames().getMsgStartFrame();
      }
    }
  }
/**
 * Izvrsava se pritiskom na OK.
 * Modificira query od selDataSet-a ili
 * kreira filter listener u odnosu na upisane vrijednosti u dbSwing komponentama na selPanelu
 * i dodaje ga zadanom selDataSetu ovisno o SQLFilter propertyu.
 * Metoda je public zato da se može napraviti predselekcija i bez selekcijskog ekrana
 * Vraca boolean da li je selekcija uspjesna
 */
  public void doSelect() {
    if (preSelDialog == null) {
      preSelShowed = false;
    } else {
      preSelShowed = preSelDialog.isVisible();
    }
    boolean success = true;
    createLL();
    selNull=false;
    if (SQLFilter) {
      success = applySQLFilter();
    } else {
      addSelFilter();
      selDataSet.refilter();
    }
//    return success;
  }

  public void installResetButton() {
    okpanel.addResetButton(this);
  }
  
  public void addSelFilter() {
    try {
      if (selfilter1!=null) selDataSet.removeRowFilterListener(selfilter1);
      selDataSet.refilter();
      selfilter1 = new selFilter();
      selDataSet.addRowFilterListener(selfilter1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void okSelect() {
    okpanel.jBOK.requestFocus();
    if (Validacija()) {
//      if (doSelect()) {
        doSelect();
        selCanceled=false;
        preSelDialog.dispose();  // (ab.f) zbog memory leaka
//      }
    }
  }

  private String getRangeCol(JTextComponent jt) {
    if (selRanges==null) {
//ST.prn("getRangeCol : selRanges==null...returning null");
      return null;
    }
    if (selRanges.length==0) {
//ST.prn("getRangeCol : selRanges.length==0...returning null");
      return null;
    }
    for (int i=0;i<selRanges.length;i++) {
      String jtclnm = selRanges[i].getColName(jt);
      if (jtclnm!=null) {
//ST.prn("getRangeCol : returning colName = "+jtclnm);
        return jtclnm;
      }
    }
//ST.prn("getRangeCol : forloop over...returning null");
    return null;
  }
  private String getRangeColSufix(JTextComponent jt) {
    if (selRanges==null) return null;
    if (selRanges.length==0) return null;
    for (int i=0;i<selRanges.length;i++) {
      String jtclnm = selRanges[i].getColName(jt);
      if (jtclnm!=null) {
        if (selRanges[i].textFrom.equals(jt)) {
//ST.prn("getRangeColSufix : returning FROMSUFIX");
          return FROMSUFIX;
        }
        if (selRanges[i].textTo.equals(jt)) {
//ST.prn("getRangeColSufix : returning TOSUFIX");
          return TOSUFIX;
        }
      }
    }
    return null;
  }

  private void createLL() {
    if (ll!=null) ll.clear();
    ll = Ut.getDBComps(selPanel);
    for (java.util.Iterator i = ll.iterator(); i.hasNext(); ) {
      Object item = i.next();
      JTextComponent jtll = null;
      try {
        jtll = (JTextComponent)item;
      }
      catch (Exception ex) {

      }
      if (getRangeCol(jtll)==null) {//nije u rangeu - ostaviti u LL
        com.borland.dx.dataset.ColumnAware llColItem = (com.borland.dx.dataset.ColumnAware)item;
        if (selDataSet.hasColumn(llColItem.getColumnName())==null) i.remove();
      }

    }
/*
    for (int i=0;i<ll.size();i++) {
      JTextComponent jtll = null;
      try {
        jtll = (JTextComponent)ll.get(i);
      }
      catch (Exception ex) {

      }
      if (getRangeCol(jtll)==null) {//nije u rangeu - ostaviti u LL
        com.borland.dx.dataset.ColumnAware llColItem = (com.borland.dx.dataset.ColumnAware)ll.get(i);
        if (selDataSet.hasColumn(llColItem.getColumnName())==null) ll.remove(i);
      }
    }
*/
//    testPrintll();
  }
  private void testPrintll() {
    if (getSelRow() == null) return;
    for (int i=0;i<ll.size();i++) {
      try {
        JTextComponent jtll = (JTextComponent)ll.get(i);
      }
      catch (Exception ex) {

      }
      com.borland.dx.dataset.ColumnAware llColItem = (com.borland.dx.dataset.ColumnAware)ll.get(i);
      com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
      getSelRow().getVariant(llColItem.getColumnName(),v);
      System.out.println(getSelRow().getTableName()+
                        "."+llColItem.getColumnName()+
                        " = "+v.toString());
    }
  }

  private void createSelRow() {
//ST.prn("BEGIN createSelRow ---------------------------------------------------------------------------");
    createLL();
    selRow = new com.borland.dx.dataset.StorageDataSet();
    for (int i=0;i<ll.size();i++) {
      String rCN = null;
      JTextComponent jtll = null;
      try {
        jtll = (JTextComponent)ll.get(i);
        rCN = getRangeCol(jtll); //column name iz range-a
      }
      catch (Exception ex) {
      }
      if (rCN==null) { //nije u rangeu - dodati normalno u dataset
        com.borland.dx.dataset.ColumnAware llColItem = (com.borland.dx.dataset.ColumnAware)ll.get(i);
        if (selRow.hasColumn(llColItem.getColumnName())==null) {
          com.borland.dx.dataset.Column newSelRowColumn = selDataSet.hasColumn(llColItem.getColumnName());
          if (newSelRowColumn != null)
            selRow.addColumn((com.borland.dx.dataset.Column)(newSelRowColumn.clone()));
        }
      } else { //u rangeu
        com.borland.dx.dataset.Column col = (com.borland.dx.dataset.Column)selDataSet.hasColumn(rCN).clone();
        if (col!=null) {
          col.setColumnName(rCN+getRangeColSufix(jtll));
//ST.prn("createSelRow : seting colName to "+col.getColumnName());
          selRow.addColumn(col);
          com.borland.dx.dataset.ColumnAware llColItem = (com.borland.dx.dataset.ColumnAware)ll.get(i);
          llColItem.setColumnName(col.getColumnName());
        }
      }
    }
    selRow.open();
//ST.prn(selRow);
//ST.prn("END createSelRow ---------------------------------------------------------------------------");
  }
  private void fillSelRowFromScreen() {
    for (int i=0;i<ll.size();i++) {
      try {
        selRow.setVariant(i,Ut.getBVariant(((javax.swing.text.JTextComponent)ll.get(i)).getText(),selRow.getColumn(i)));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
/**
 * Okida se pritiskom na Prekid na selekcijskom ekranu. Gasi selekcijski dialog i postavlja flag selCanceled=true
 */
  public void cancelSelect() {
    selCanceled=true;
    preSelDialog.dispose();  // (ab.f) zbog memory leaka
  }
/**
 * selPanel je po uzoru na raMatPodaci. Napravi si panel od predselekcijskog ekrana, a program ce iz
 * ColumnAware ekranskih komponenti kreirati StorageDataSet i njega pridruziti tim komponentama.
 * Ekranske komponente kojima je pridruzena kolona koja nije u selDataSetu bit ce ignorirana.
 */
  public void setSelPanel(javax.swing.JPanel newSelPanel) {
    selPanel = newSelPanel;
//    hr.restart.swing.layout.raLayUtil.parseXYLayout(selPanel);
    createLL();
//this.TESTprintLL();
    createSelRow();
    assignDataSet();
    addRangePopups();
    if (!SQLFilter) addSelFilter();
  }
  private void addRangePopup(String colname) {
    try {
      if (colname == null) return;
      if (colname.endsWith(FROMSUFIX)) {
        colname = colname.substring(0,colname.length()-FROMSUFIX.length());
      } else return;
      selRange range = getSelRange(colname);
      if (range == null) return;
      if (!(range.textFrom instanceof JraTextField)) return;
      if (!(range.textTo instanceof JraTextField)) return;
      JraTextField txFrom = (JraTextField)range.textFrom;
      JraTextField txTo = (JraTextField)range.textTo;
      if (txFrom.getRaPopup() != null) if (txFrom.getRaPopup() instanceof raDatePopup) new raDateRange(txFrom,txTo);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  private void addRangePopups() {
    for (int i=0;i<ll.size();i++) {
      try {
        com.borland.dx.dataset.ColumnAware ca = (com.borland.dx.dataset.ColumnAware)ll.get(i);
        addRangePopup(ca.getColumnName());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  private void assignDataSet() {
    for (int i=0;i<ll.size();i++) {
      try {
        com.borland.dx.dataset.ColumnAware ca = (com.borland.dx.dataset.ColumnAware)ll.get(i);
        ca.setDataSet(selRow);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
/**
 * Metoda koju bi bilo lijepo pozvati u raMatPodaci.SetFokus() tako da u svaki novi row raQueryDataSeta (selDataSeta
 * upise vrijednosti unesene u predselekcijski ekran.
 */
  public void copySelValues() {
    com.borland.dx.dataset.ReadRow.copyTo(getCopyColumns(selDataSet),selRow,getCopyColumns(selDataSet),selDataSet);
  }
/**
 * Radi isto sto i {@link #copySelValues()} samo za zadani dataset umjesto selDataSeta
 * Ignorira kolone koje nisu u zadanom datasetu
 */
  public void copySelValues(com.borland.dx.dataset.DataSet ds) {
    String[] copyCols = getCopyColumns(ds);
    if (copyCols!=null) com.borland.dx.dataset.ReadRow.copyTo(copyCols,selRow,copyCols,ds);
  }

  private String[] getCopyColumns(com.borland.dx.dataset.DataSet ds) {
    String[] selcols = selRow.getColumnNames(selRow.getColumnCount());
    int n = selcols.length;
    for (int i=0;i<selcols.length;i++) if (ds.hasColumn(selcols[i])==null) n--;
    if (n<=0) return null;
    String[] copyCols = new String[n];
    int j=0;
    for (int i=0;i<selcols.length;i++) {
      if (ds.hasColumn(selcols[i])!=null) {
        copyCols[j] = selcols[i];
        j++;
      }
    }
    return copyCols;
  }
  private String[] getSelColumns() {
    String[] str = selRow.getColumnNames(selRow.getColumnCount());
    String[] newStr;
    if (selRanges==null) {
      newStr = str;
    } else {
      newStr = new String[str.length-(selRanges.length*2)];
      int j=0;
      for (int i=0;i<str.length;i++) {
        if (
            (str[i].endsWith(FROMSUFIX)) ||
            (str[i].endsWith(TOSUFIX))
           ) {} else {
          newStr[j] = str[i];
          j++;
        }
      }
    }
    return newStr;
  }
/**
 * Evo i geter za selPanel iako vjerujem da je taj ionako pristupacan.
 */
  public javax.swing.JPanel getSelPanel() {
    return selPanel;
  }
/**
 * selDataset bi trebao biti isti dataset kao i raMatPodaci.raQueryDataSet. Na njemu se vrsi filteriranje u odnosu na
 * selekcijske kriterije upisane u selPanel
 */
  public void setSelDataSet(com.borland.dx.dataset.DataSet newSelDataSet) {
    selDataSet = newSelDataSet;
//-opn-    selDataSet.open();
  }
/**
 * Evo i geter za selDataSet iako vjerujem da je taj ionako pristupacan.
 */
  public com.borland.dx.dataset.DataSet getSelDataSet() {
    return selDataSet;
  }
/**
 * Za periode na selekcijskom ekranu. Npr. treba selekcija izmedju dva datuma: potrebno je staviti dve text komponente
 * (JraTextField) na selPanel koje trebaju biti bindane na selDataSet zbog formata.
 * Recimo da se one zovu jtDATUM1 i jtDATUM2 i njima se treba odabrati po koloni DATUM u datasetu
 * Nakon toga treba pozvati PreSelect.addSelRange(jtDATUM1,jtDATUM2);
 * pod uvjetom da jtDATUM1 ima getColunName() = "DATUM" ili
 * addSelRange("DATUM",jtDATUM1,jtDATUM2) ako iz nekog razloga nije tako
 */
  public void addSelRange(JraTextField txFrom,JraTextField txTo) {
    selRange newSelRange = getSelRange(txFrom,txTo);
    add2Ranges(newSelRange);
//    testPrintRanges();
  }
  public static int ISNULL = 0;
  public static int NOTNULL = 1;
/**
 * Za dodatni uvjet kolone, filtrira podatke u odnosu na zadani JraTextField i selekcijski property koji moze biti:
 * <pre>
 * ISNULL - uzima samo one gdje vrijednost kolone pridruzene zadanom JraTextFieldu NIJE upisana;
 * NOTNULL - uzima samo one gdje je vrijednost kolone pridruzene zadanom JraTextFieldu upisana ;
 * </pre>
 * zadani jratextfield mora se nalaziti na selPanelu
 */
  public void addSelRange(JraTextField txField,int selprop) {
    selRange newSelRange = getSelRange(txField,selprop);
    add2Ranges(newSelRange);
  }
/*
  public void addSelRange(String columnName,int selprop) {
    selRange newSelRange = new selRange(columnName,null,selprop);
    add2Ranges(newSelRange);
  }
*/
private void testPrintRanges() {
  for (int i = 0;i<selRanges.length;i++) {
    System.out.print("selRanges[");
    System.out.print(i);
    System.out.print("] = ");
    System.out.println(selRanges[i].colName);
  }
}
  public void addSelRange(String colName,JTextComponent txFrom,JTextComponent txTo) {
    selRange newSelRange = getSelRange(colName,txFrom,txTo);
    add2Ranges(newSelRange);
  }
/**
 * selRow je StorageDataSet iskreiran sa kolonama koje su u selDataSetu i bindane na komponenta u selPanelu.
 * U njemu se nalaze vrijednosti koje je korisnik unio na predselekcijski ekran. Moglo bi koristiti necemu a?
 */
  public com.borland.dx.dataset.StorageDataSet getSelRow() {
    return selRow;
  }
/**
 * boolean flag koji postaje true ako korisnik pritisne prekid na predselekcijskom ekranu.
 * Najbolje je tada ugasiti ili uopce ne prikazati ekran za unos, ili vratiti prijasnju selekciju.
 */
  public boolean isSelCanceled() {
    return selCanceled;
  }
/**
 * ako je setSQLFilter(false) selDataSet se filtrira dodavanjem rowFilterListenera, a
 * ako je setSQLFilter(true) modificira se query stavaka i tako se filtriraju.
 * Default je true.
 */
  public void setSQLFilter(boolean sqlf) {
    SQLFilter = sqlf;
  }
  public boolean isSQLFilter() {
    return SQLFilter;
  }
/**
 * Funkcija koja postavlja granice :) Ako je bilo addSelRange(jtf1,jtf2) onda setRangeText(jtf1)
 * ubacuje unesenu vrijednost iz jtf1 u jtf2 i nakon toga dobivamo da je jtf1.getText() == jtf2.getText()
 * Ako to ne zadovoljava probaj npr. getSelRow.setTimeStamp(jtf1.getColumnName,nekakavtimestamp)
 */
  public void setRangeText(JraTextField jt) {
    String sufix = getRangeColSufix(jt);
    String sufix2 = sufix.equals(FROMSUFIX)?TOSUFIX:FROMSUFIX;
    String colname = jt.getColumnName();
    colname = colname.substring(0,colname.length()-sufix.length());
    com.borland.dx.dataset.Variant vv = new com.borland.dx.dataset.Variant();
    selRow.getVariant(colname.concat(sufix),vv);
    selRow.setVariant(colname.concat(sufix2),vv);
  }
  /**
   * Da li kod range selekcije zadane kolone ukljuciti granicne vrijednosti ili ne. Default=true.
   */
  public void setIncludeBorders(String colName,boolean newIncludeBorders) {
    getSelRange(colName).includeBorders = newIncludeBorders;
  }

  public boolean getIncludeBorders(String colName) {
    return getSelRange(colName).includeBorders;
  }

  void this_componentShown(ComponentEvent e) {
    selNull=true;
    selDataSet.refilter();
    if (rbgUser != null) jtbUser.setSelected();
    Valid.setApp(this.getClass());
    SetFokus();
  }
  
  /**
   * Za overridanje. Trebalo bi postaviti inicijalne (uglavnom prazne)
   * vrijednosti na predselekcijski ekran,
   */
  public void resetDefaults() {
    
  }
/**
 * Overridati i u nju setirati focus po zelji na selPanelu (po uzoru na raMatPodaci).
 * Poziva se na componentshown od selekcijskog dialoga.
 */
  public void SetFokus() {
    createLL();
    ((JTextComponent)ll.getFirst()).requestFocus();
  }
/**
 * Overridati. Ova funkcija mora biti true da bi se napravila predselekcija (po uzoru na raMatPodaci).
 */
  public boolean Validacija() {
    return true;
  }
/**
 * Prikazuje predselekcijski ekran sa zadanim naslovom, ako je naslov="" ili null daje default naslov
 */
  public void showPreselect(String ftitle){
    showPreselect((JFrame)null,ftitle);
  }
/**
 * Setira preselshowed na true i showa ekran
 */
  public void show() {
    preSelShowed=true;
    preSelDialog.show();
  }

/**
 * evo koda: showPreselect(null);
 */
  public void showPreselect(){
    showPreselect(null);
  }
  /**
   * vra\u0107a ownera predselekcije koji može biti JDialog, JFrame, raFrame ili raMasterDetail,
   * castati po potrebi
   * @return
   */
  public Object getPSOwner() {
    return psowner;
  }

  private void setPSOwner(Object owner,boolean force) {
    if (psowner == null||force) {
      psowner = owner;
      if (psowner instanceof raPreSelectAware) {
        ((raPreSelectAware)psowner).setPreSelect(this);
      }
    }
  }

/**
 * Prikazuje predselekcijski ekran sa zadanim naslovom, centriran u odnosu na ownera, ako je pritisnut Prekid hida ownera,
 * a ako je pritisnut ok radi owner.show().
 * Pozvati na pritisak na dodatni gumb u raMatPodaci (raMatPodaci.AddButton(JButton, boolean)) i(ili)
 * umjesto startFrame.showFrame(frmClass) -> frmClass.preselectFrame.showPreselect(frame,title)
 */
  public void showPreselect(javax.swing.JFrame owner, String ftitle) {
    findSF(owner);
    setPSOwner(owner,false);
    initPreSeldialog();
    if ((ftitle==null)||(ftitle.equals(""))) ftitle="Predselekcija";
    SF.centerFrame(preSelDialog,15,ftitle);
    if (owner!=null) {
      centerByOwner(owner,ftitle);
    }
    SF.showFrame(preSelDialog);
    if (owner!=null) {
      if (isSelCanceled()) {
        owner.hide();
      } else {
        ownershow(owner);
      }
    }
  }
  /**
   * loada preSelClass i ownerClass i na temelju njih zove pripadajucu metodu
   * @param preSelClass naziv klase predselekcije
   * @param ownerClass naziv klase ownera
   * @param ftitle naslov
   * @param handleDataSets
   * @return instancirani PreSelect, dok sa getPSOwner() mozes dobiti instancirani ownerClass
   */
  public static PreSelect showPreselect(String preSelClass,String ownerClass,String ftitle,boolean handleDataSets) {
    PreSelect press = (PreSelect)raLoader.load(preSelClass);
    press.showPreselect(ownerClass,ftitle,handleDataSets);
    return press;
  }
/**
 * loada ownerclass i zove pripadajucu metodu
 * @param ownerClass
 * @param ftitle
 * @param handleDataSets
 * @throws Exception
 */
  public void showPreselect(String ownerClass,String ftitle,boolean handleDataSets) {
    try {
      Object obj = raLoader.load(ownerClass);
      findSF(obj);
      callShowPreselect(obj,ftitle,handleDataSets);
    }
    catch (Exception ex) {
      System.out.println("showPreselect("+ownerClass+","+ftitle+","+handleDataSets+") Exeption: "+ex);
      ex.printStackTrace();
    }
  }
  private void callShowPreselect(Object obj,String ftitle,boolean handleDataSets) throws Exception {
    IllegalArgumentException exc = new IllegalArgumentException("klasa mora biti tipa ili JFrame ili JDialog ili raFrame ili raMasterDetail");
    if (obj==null) throw exc;
    if (obj instanceof hr.restart.util.raMasterDetail) {
      showPreselect((hr.restart.util.raMasterDetail)obj,ftitle,handleDataSets);
      return;
    }
    if (obj instanceof hr.restart.util.raFrame) {
      showPreselect((hr.restart.util.raFrame)obj,ftitle);
      return;
    }
    if (obj instanceof javax.swing.JFrame) {
      showPreselect((javax.swing.JFrame)obj,ftitle);
      return;
    }
    if (obj instanceof javax.swing.JDialog) {
      showPreselect((javax.swing.JDialog)obj,ftitle);
      return;
    }
    throw exc;
  }
  /**
   * zove showPreselect(String preSelClass,String ownerClass,String ftitle,boolean handleDataSets)
   * @param preSelClass
   * @param ownerClass
   * @param ftitle
   */
  public static PreSelect showPreselect(String preSelClass,String ownerClass,String ftitle) {
    return showPreselect(preSelClass,ownerClass,ftitle,false);
  }
  /**
   * Prvo raMasterDetailu setira samog sebe kao predselekcijski ekran
   * - zove {@link #raMasterDetail.setPreSelect(PreSelect, String, boolean) raMD.setPreSelect(this,ftitle,handleDataSets)}
   * i nakon toga otvara predselekcijski ekran metodom {@link #showPreselect(java.awt.Frame, String) showPreselect(raMD.raMaster,ftitle)}
   * @param raMD raMasterDetail ciji se master ekran prikazuje nakon predselekcije
   * @param ftitle naslov predselekcije
   * @param handleDataSets da li da izjednaci selDataSet i raMasterDetail.MasterSet ili ne
   */
  public void showPreselect(raMasterDetail raMD, String ftitle, boolean handleDataSets) {
    setPSOwner(raMD,true);
    raMD.setPreSelect(this,ftitle,handleDataSets);
    showPreselect(raMD.raMaster,ftitle);
  }
  /**
   * Zove {@link #showPreselect(raMasterDetail, String, boolean) showPreselect(raMD, ftitle, false)}
   * @param raMD
   * @param ftitle
   */
  public void showPreselect(raMasterDetail raMD, String ftitle) {
    showPreselect(raMD,ftitle,false);
  }
  /**
   * @deprecated na componentShown eventu zove se SetFokus, a ako trebas nesto na componentHidden samo
   * pozovi metodu nakon showPreselect(.... pa to je JDialog
   */
  public void addComponentListener(java.awt.event.ComponentListener l) {
    System.out.println("Metoda PreSelect.addComponentListener je deprecated, nista nisam napravio... pogledaj dokumentaciju");
  }
/**
 * Vidi {@link #showPreselect(java.awt.Frame, String)}
 */
  public void showPreselect(javax.swing.JDialog owner, String ftitle) {
    setPSOwner(owner,false);
    findSF(owner);
    initPreSeldialog();
    if ((ftitle==null)||(ftitle.equals(""))) ftitle="Predselekcija";
    SF.centerFrame(preSelDialog,15,ftitle);
    if (owner!=null) {
      centerByOwner(owner,ftitle);
    }
    SF.showFrame(preSelDialog);
    if (owner!=null) {
      if (isSelCanceled()) {
        owner.hide();
      } else {
        ownershow(owner);
      }
    }
  }
  public JDialog getPreSelDialog() {
    return preSelDialog;
  }
/**
 * Vidi {@link #showPreselect(java.awt.Frame, String)}
 */
  public void showPreselect(raFrame owner, String ftitle) {
    if (owner instanceof hr.restart.util.raMatPodaci) {
      raMatPodaci rMP = (raMatPodaci)owner;
      if (rMP.getRaMasterDetail() != null) if (!rMP.getRaMasterDetail().equals(getPSOwner())) {
        System.out.println("Poziv predselekcije na ovaj na\u010Din je deprecated");
        System.out.println("Molim pozvati sa jednom od ovih metoda: ");
        System.out.println("* static showPreselect(String preSelClass,String ownerClass,String ftitle)");
        System.out.println("* static showPreselect(String preSelClass,String ownerClass,String ftitle,boolean handleDataSets)");
        System.out.println("* showPreselect(String ownerClass,String ftitle,boolean handleDataSets)");
        System.out.println("* showPreselect(raMasterDetail raMD, String ftitle, boolean handleDataSets)");
        System.out.println("* showPreselect(raMasterDetail raMD, String ftitle)");
        showPreselect(rMP.getRaMasterDetail(),ftitle);
        return;
      }
    }
    setPSOwner(owner,false);
    findSF(owner);
    initPreSeldialog();
    if ((ftitle==null)||(ftitle.equals(""))) ftitle="Predselekcija";
    SF.centerFrame(preSelDialog,15,ftitle);
    if ((owner!=null)&&(owner.isShowing())) {
      centerByOwner(owner,ftitle);
    }
    SF.showFrame(preSelDialog);
    if (owner!=null) {
      if (isSelCanceled()) {
        owner.hide();
      } else {
        ownershow(owner);
      }
    }
  }

  void ownershow(raFrame owner) {
    // fix by (ab.f)  - ne zovi beforeShow() dva puta
    if (owner.isShowing() && owner instanceof raMatPodaci) {
      ((raMatPodaci)owner).initColBean();
      ((raMatPodaci)owner).fireTableDataChanged();
      ((raMatPodaci)owner).beforeShow();//iliti raMasterDetail.beforeShowMaster
    }
    if (owner.isShowing()) return;
    SF.showFrame(owner);
//    owner.pack();
//    owner.show();
  }

  void ownershow(javax.swing.JFrame owner) {
    if (owner.isShowing()) return;
    SF.showFrame(owner);
//    owner.pack();
//    owner.show();
  }
  void ownershow(javax.swing.JDialog owner) {
    if (owner.isShowing()) return;
    SF.showFrame(owner);
//    owner.pack();
//    owner.show();
  }

  private void centerByOwner(java.awt.Frame owner,String ftitle) {
    preSelDialog.setLocation(owner.getLocation().x+((owner.getSize().width - preSelDialog.getSize().width)/2),owner.getLocation().y+((owner.getSize().height - preSelDialog.getSize().height)/2));
/* mozda bi iz ovoga neko nesto mogao i skuziti, a radi isto;
    Dimension downer = owner.getSize();
    Dimension dthis = preSelDialog.getSize();
    int relX=((downer.width - dthis.width)/2);
    int relY=((downer.height - dthis.height)/2);
    int x=owner.getLocation().x;
    int y=owner.getLocation().y;
    preSelDialog.setLocation(x+relX,y+relY);
*/
  }
  private void centerByOwner(java.awt.Dialog owner,String ftitle) {
    preSelDialog.setLocation(owner.getLocation().x+((owner.getSize().width - preSelDialog.getSize().width)/2),owner.getLocation().y+((owner.getSize().height - preSelDialog.getSize().height)/2));
  }

  private void centerByOwner(raFrame owner,String ftitle) {
    preSelDialog.setLocation(owner.getLocation().x+((owner.getSize().width - preSelDialog.getSize().width)/2),owner.getLocation().y+((owner.getSize().height - preSelDialog.getSize().height)/2));
  }

  selRange getSelRange(String colNameC,JTextComponent textFromC,JTextComponent textToC) {
    return new selRange(colNameC,textFromC,textToC);
  }

  selRange getSelRange(hr.restart.swing.JraTextField textFieldC,int props) {
    return new selRange(textFieldC.getColumnName(),textFieldC,props);
  }

  selRange getSelRange(hr.restart.swing.JraTextField textFromC,hr.restart.swing.JraTextField textToC) {
    return new selRange(textFromC.getColumnName(),textFromC,textToC);
  }

  selRange getSelRange(String colmnName) {
    for (int i=0;i<selRanges.length;i++) {
      if (selRanges[i].colName.equals(colmnName)) return selRanges[i];
    }
    return null;
  }

  void add2Ranges(selRange newSelRange) {
    if (selRanges == null) {
      selRanges = new selRange[] {newSelRange};
      return;
    }
    int contr = containsRange(selRanges,newSelRange);
    if (contr == -1) {
      int newLen = selRanges.length;
      selRange[] newRanges = new selRange[newLen+1];
      newRanges[newLen] = newSelRange;
      for (int i=0;i<selRanges.length;i++) newRanges[i] = selRanges[i];
      selRanges = newRanges;
    } else {
      selRanges[contr] = newSelRange;
    }
  }
  int containsRange(selRange[] srs, selRange sr) {
    if (srs==null) return -1;
    if (srs.length == 0) return -1;
    for (int i=0;i<srs.length;i++) {
      if (srs[i].equals(sr)) return i;
    }
    return -1;
  }
  int containsRange(selRange[] srs, String cn) {
//ST.prn("CONTAINSRANGE BEGIN----------------------");
    if (cn==null) {
//ST.prn("containsRange : cn is null returning -1");
      return -1;
    }
    for (int i=0;i<srs.length;i++) {
      if (srs[i].colName.equals(cn)) return i;
    }
//ST.prn("containsRange : cn not found returning -1");
    return -1;
  }
  private boolean dontOpenWASQLF = false;
  public void dontOpenWhenSQLFilterNextTime() {
    dontOpenWASQLF = true;
  }
  public boolean applySQLFilter() {
    if (selDataSet instanceof com.borland.dx.sql.dataset.QueryDataSet) {
      com.borland.dx.sql.dataset.QueryDataSet selQDS = (com.borland.dx.sql.dataset.QueryDataSet)selDataSet;
      String orgQuery = Vl.getNoWhereQuery(selQDS)+" WHERE ";
      String addQuery = "";
      com.borland.dx.dataset.Variant variant = new com.borland.dx.dataset.Variant();
      for (int i=0;i<ll.size(); i++) {
        com.borland.dx.dataset.ColumnAware txCol = ((com.borland.dx.dataset.ColumnAware)ll.get(i));
        if (!txCol.getColumnName().endsWith(TOSUFIX)) {//TOSUFIX samo ignoriraj
          if (txCol.getColumnName().endsWith(FROMSUFIX)) {
            String colfrom = txCol.getColumnName();
            String col = colfrom.substring(0,colfrom.length()-5);
            String colto = col.concat(TOSUFIX);
            com.borland.dx.dataset.Column colmn = selDataSet.getColumn(col);
            if (containsRange(selRanges,col) != -1) {
              selRange rng = getSelRange(col);
              if (rng.props == ISNULL || rng.props == NOTNULL) {
                addQuery = addQuery.concat(Vl.getNullQuerySintax(colmn,rng.props==ISNULL,false));
              } else {
                selRow.getVariant(colfrom,variant);
                String colvaluefrom = variant.toString();
                selRow.getVariant(colto,variant);
                String colvalueto = variant.toString();
                addQuery = addQuery.concat(Vl.getBetweenQuerySintax(colmn,colvaluefrom,colvalueto,false));
              }
            }
          } else {//endsWith(FROMSUFIX)
            if (isEditEmpty(ll.get(i))) {
              com.borland.dx.dataset.Column colmn = selDataSet.getColumn(txCol.getColumnName());
              selRow.getVariant(txCol.getColumnName(),variant);
              String colvalue = variant.toString();
              addQuery = addQuery.concat(Vl.getQuerySintax(colmn,colvalue,false));
            }
          }//else endswith(FROMSUFIX)
        }//endsWith(TOSUFIX)
      }//for
//      System.err.println("AddQuery ::: "+addQuery);
      if (addQuery.length()<5) addQuery = "1 = 1 AND "; 
      addQuery = addQuery.substring(0,addQuery.length()-5);
      lastFilterQuery = refineSQLFilter(addQuery.concat(getUserQuery()));
      String newQuery = orgQuery.concat(lastFilterQuery);

      System.out.println("Preselect :" + newQuery);
        if (!Vl.chkTooBigSet(newQuery)) return false;
        Aus.setFilter(selQDS, newQuery);
        if (dontOpenWASQLF) dontOpenWASQLF = false;
        else selQDS.open();
//      }
    } else {//  selDataSet is not QueryDataSet...
      addSelFilter();
      selDataSet.refilter();
    }
    return true;
  }
  
  public String refineSQLFilter(String orig) {
    return orig;
  }
  
  private String lastFilterQuery = null;
  public String getLastFilterQuery() {
    return lastFilterQuery;
  }
//
  public boolean validateEntry() {
    if (getSelDataSet() == null) return true;
    if (getSelRow() == null) return true;
    if (selfilter1 == null) selfilter1 = new selFilter();
    createLL();
//ST.prnc(getSelDataSet());
    selNull = false;
    try {
      return selfilter1.isRow(getSelDataSet());
    }
    catch (Exception ex) {
      System.out.println("presel = "+this);
      System.out.println("owner = "+getPSOwner());
      System.out.println("selRow = ");
      ST.prn(selRow);
      System.out.println("selDataSet = ");
      ST.prn(selDataSet);
      ex.printStackTrace();
      return false;
    }
  }
//

  public Condition getUserCondition() {
    if (!isUserQuery()) return Condition.none;
    return Condition.equal("CUSER", hr.restart.sisfun.raUser.getInstance().getUser());
  }
  
  private String getUserQuery() {
    if (isUserQuery())
      return " AND CUSER = '"+hr.restart.sisfun.raUser.getInstance().getUser()+"'";
    else
      return "";
  }

  private boolean isUserQuery() {
    if (!isUserQuestion()) return false;
    if (getSelDataSet().hasColumn("CUSER")==null) return false;
    if (jtbUser.isSelected()) return true;
    return false;
  }

  private boolean userIsRow(com.borland.dx.dataset.ReadRow row) {
    if (!isUserQuery()) return true;
    return row.getString("CUSER").equals(hr.restart.sisfun.raUser.getInstance().getUser());
  }

  class selRange {
    String colName;
    JTextComponent textFrom;
    JTextComponent textTo;
    boolean includeBorders=true;
    int props = -1;

    public selRange(String colNameC,JTextComponent textFromC,JTextComponent textToC) {
      colName = colNameC;
      textFrom = textFromC;
      textTo = textToC;
    }

    public selRange(String colNameC,JTextComponent textFromC,int propsC) {
      colName = colNameC;
      textFrom = textFromC;
      textTo = null;
      props = propsC;
    }

    public boolean equals(selRange sr) {
      return colName.equals(sr.colName);
    }
    public String getColName(JTextComponent jt) {
      if (textFrom==null) return null;
      if (textFrom.equals(jt)) {
        return colName;
      }
      if (textTo==null) return null;
      if (textTo.equals(jt)) {
        return colName;
      }
      return null;
    }
  }
  class selFilter implements com.borland.dx.dataset.RowFilterListener {
    public void filterRow(com.borland.dx.dataset.ReadRow row, com.borland.dx.dataset.RowFilterResponse resp) {
      if (isRow(row)&&userIsRow(row)) {
        resp.add();
      } else {
        resp.ignore();
      }
    }
    boolean isRow(com.borland.dx.dataset.ReadRow row) {
//ST.prn("BEGIN isRow ---------------------------------------------------------------------------");
//ST.prn("selNull = "+selNull);
//ST.prn("ll.size() = "+ll.size());
      if (selNull) return false;
      for (int i=0;i<ll.size(); i++) {
        com.borland.dx.dataset.ColumnAware txCol = ((com.borland.dx.dataset.ColumnAware)ll.get(i));
//ST.prn("checking "+txCol.getColumnName());
        if (!txCol.getColumnName().endsWith(TOSUFIX)) {//TOSUFIX samo ignoriraj
          if (txCol.getColumnName().endsWith(FROMSUFIX)) {
//ST.prn("isRow : FROMSUFIX found in col "+txCol.getColumnName());
            String colfrom = txCol.getColumnName();
            String col = colfrom.substring(0,colfrom.length()-5);
            String colto = col.concat(TOSUFIX);
            if (containsRange(selRanges,col) != -1) {
//ST.prn("isRow : containsRange returned ",containsRange(selRanges,col));
//ST.prn("Entering betweenBVariants(row,selRow,"+col+","+colfrom+","+colto+",includeBorders) :");
//ST.prn("betweenBVariants(row,selRow,"+col+","+colfrom+","+colto+",includeBorders) :",Ut.betweenBVariants(row,selRow,col,colfrom,colto,getSelRange(col).includeBorders));
              selRange slr = getSelRange(col);
              if (slr.props == -1) {
                if (!Ut.betweenBVariants(row,selRow,col,colfrom,colto,slr.includeBorders)) {
//  ST.prn("isRow : "+col+" not between "+colfrom+" and "+colto+" ... returning false");
                  return false;
                } else {
//  ST.prn("isRow : "+col+" between "+colfrom+" and "+colto+" ... continuing");
                }
              } else if (slr.props == ISNULL||slr.props == NOTNULL) {
                com.borland.dx.dataset.Variant v1 = new com.borland.dx.dataset.Variant();
                com.borland.dx.dataset.Column cl = row.getColumn(slr.colName);
                row.getVariant(slr.colName,v1);
                boolean isempty = Vl.chkIsEmpty(cl,v1.toString());
                if (slr.props == ISNULL) {
                  return isempty;
                } else if (slr.props == NOTNULL) {
                  return !isempty;
                }
              }
            }
          } else {//endsWith(FROMSUFIX)
//ST.prn("isRow : else...");
//ST.prn(ll.get(i));
//ST.prn(txCol.getColumnName()+" = "+( (hr.restart.swing.JraTextField)ll.get(i)).getText());
            if (isEditEmpty(ll.get(i))) {
//ST.prn("Kontroliram kolonu "+txCol.getColumnName()+"...");
              if (!Ut.equalsBVariant(row,selRow,txCol.getColumnName(),txCol.getColumnName())) return false;
            }
          }//else endswith(FROMSUFIX)
        }//endsWith(TOSUFIX)
      }//for
      return true;
    }
  }
//u glavnoj klasi jer se koristi i u metodi applyselfilter
  boolean isEditEmpty(java.lang.Object memb) {
    //if (!preSelShowed) return true; //uvijek je puno ako nije prikazan ekran
    if (!preSelShowed) {
      ColumnAware ca = (ColumnAware) memb;
      Variant v = new Variant();
      ca.getDataSet().getVariant(ca.getColumnName(), v);
      return !v.isNull() && v.toString().length() > 0;
    }
    if (memb instanceof javax.swing.text.JTextComponent) {
      return !Vl.chkIsEmpty((javax.swing.text.JTextComponent)memb); //true ako je puno
    } else if (memb instanceof raComboBox) {
      raComboBox rcb = (raComboBox)memb;
      return !Vl.chkIsEmpty(rcb.getDataSet().hasColumn(rcb.getColumnName()),rcb.getDataValue());
    } else {
      return true;
    }
  }
}
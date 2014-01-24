/****license*****************************************************************
**   file: JlrNavField.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.ActionExecutor;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableModifier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.AncestorEvent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StatusListener;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;



/**
 * Ekranski objekt koji extenda JraTextField i ima dodatne feature za lookup. Slusa event na focus lost
 * i na keypressed = F9. Na focuslost puni ekranske objekte zadane u textFields ovisno o
 * {@link #setSearchMode(int) searchMode} parametru, dok na F9 daje na odabir podatke iz zadanog
 * {@link #setRaDataSet(com.borland.dx.dataset.DataSet) raDataSeta}.
 * Protektirane su eventi focus_lost i keyF9, ali zato se moze overridati metoda
 * {@link #after_lookUp() after_lookUp} u kojoj se mogu pozvati metode lokalnog karaktera.
 * <pre>
 * PRIMJER KORISTENJA:
 *
 * public class Frame1 extends raMatPodaci {
 *   JlrNavField jTNazgrup = new JlrNavField();
 *   JlrNavField jlrNavField1 = new JlrNavField();
 * .
 * .
 * .
 *  private void jbInit() throws Exception {
 *
 *   jlrNavField1.setColumnName("CGRART");
 *   jlrNavField1.setDataSet(dM1.getArtikli());
 *   jlrNavField1.setColNames(new String[] {"CGRARTPRIP", "NAZGRART"});
 *   jlrNavField1.setVisCols(new int[] {0,2});
 *   jlrNavField1.setTextFields(new javax.swing.text.JTextComponent[] {jlrNavField2,jlrNavField3});
 *   jlrNavField1.setRaDataSet(dM1.getGrupart());
 *   jlrNavField1.setSearchMode(0);
 *   jlrNavField1.setNavProperties(null);
 *   //sve je setirano za prvu komponentu a ostale samo naslijedjuju propertie
 *   jlrNavField2.setColumnName("CGRARTPRIP");
 *   jlrNavField2.setSearchMode(2);
 *   jlrNavField2.setNavProperties(jlrNavField1); //naslijedi od jlrNavField1
 *   jlrNavField3.setColumnName("NAZGRART");
 *   jlrNavField3.setSearchMode(1);
 *   jlrNavField3.setNavProperties(jlrNavField1
 *  .
 *  .
 *  .
 *
 * </pre>
 */

public class JlrNavField extends JraTextField {
//sysoutTEST ST=new sysoutTEST(false);
//TimeTrack TT = new TimeTrack(false);
  private javax.swing.text.JTextComponent[] textFields;
  private String[] colNames;
  private String extraDirectColumn;
  private lookupData lD;
  private int[] visCols;
  private int[] currVisCols;
  public static int NULL = 99;
  private com.borland.dx.dataset.DataSet raDataSet;
  private boolean doFocus=true;
  private int lastKey = KeyEvent.VK_ENTER;
  private boolean doneOnShow = false, userAfterLookup;
  private int searchMode=0;
  private int luModeENTER=lD.EXACT;
  private int luModeF9=lD.ALL;
  private int currentLuMode=lookupData.EXACT;
  private int dataSetStatus = com.borland.dx.dataset.StatusEvent.EDIT_STARTED;
  private String[] thisColNames;
  private javax.swing.text.JTextComponent[] thisTextFields;
  private boolean sameSet=false;
  private boolean isInserted=false;
  private boolean focusLostOnShow=true;
  private boolean afterLookUpOnClear=true;
  private boolean afterLookAlways=false;
  private boolean handleError=true;
  
  // višestruk odabir
  private boolean allowMulti = false;
  
  //private String txtBefore=getText();
  private String navColumnName;
  private String lookupSaveName;
  private String lastNavValue = getText();
  private int lookupFrameWidth = 0;
  private FocusListener nextFocus = null;
  com.borland.dx.dataset.DataRow currRow;
  
  // za pozicioniranje na prethodni slog
  private String lastNav = "";
  
  private static boolean keepRow = false;

//  java.awt.event.FocusAdapter JlrNavFieldFocusListener = new java.awt.event.FocusAdapter() {
//    public void focusLost(FocusEvent e) {
//      this_focusLost();
//    }
//  };
  java.awt.event.KeyAdapter JrlNavFieldKeyListener = new java.awt.event.KeyAdapter() {
    public void keyPressed(KeyEvent e) {
      this_keyPressed(e);
    }
  };
  java.util.LinkedList dbComponents;
  String[] VDValues;
  private hr.restart.util.JlrNavField navProperties;
  
  private List modifiers;
/**
 * Initializer po uzoru na hr.restart.swing.JraTextField i pripadajuca mu metoda
 */
  {
/*    this.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        this_focusGained(e);
      }
    });*/
  }
/**
 * Konstruktor je najednostavniji. Najbolje je drag'n'drop u JBuilderu.
 */
  public JlrNavField() {
    try {
      jInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jInit() throws Exception {
    this.addAncestorListener(new javax.swing.event.AncestorListener() {
      public void ancestorAdded(AncestorEvent e) {
        doOnShow();
      }
      public void ancestorRemoved(AncestorEvent e) {
        doOnHide();
      }
      public void ancestorMoved(AncestorEvent e) {
      }
    });
    keepRow = "D".equalsIgnoreCase(frmParam.getParam("sisfun", "keepRow", 
              "N", "Zapamtiti zadnji red kod prošlog dohvata (D,N)"));
  }
  private void raInit() {
    if (navProperties!=null) this.inheritProperties(navProperties);  //naslijedjivanje propertija

    if (searchMode==-1) {
      updLabelUI();
    } else {
      lD=hr.restart.util.lookupData.getlookupData();
//      addFocusListener(JlrNavFieldFocusListener);
      /*addKeyListener(JrlNavFieldKeyListener);*/
      setDefCols();
      setDefHelpText();
    }
  }
  
  public List getModifiers() {
    if (navProperties == null) return modifiers;
    return navProperties.modifiers;
  }
  
  public void addModifier(raTableModifier modif) {
    if (navProperties == null) {
      if (modifiers == null) modifiers = new ArrayList();
      modifiers.add(modif);
    } else {
      if (navProperties.modifiers == null) navProperties.modifiers = new ArrayList();
      navProperties.modifiers.add(modif);
    }
  }
  
  public void removeModifier(raTableModifier modif) {
    if (navProperties == null) {
      if (modifiers != null) modifiers.remove(modif);
    } else {
      if (navProperties.modifiers != null) navProperties.modifiers.remove(modif);
    }
  }
  
  public void removeAllModifiers() {
    if (navProperties == null) modifiers = null;
    else navProperties.modifiers = null;
  }
  
  public void addNotify() {
    super.addNotify();
    AWTKeyboard.registerKeyListener(this, JrlNavFieldKeyListener);    
  }
  
  public void removeNotify() {
    super.removeNotify();
    AWTKeyboard.unregisterComponent(this);
  }
  
  private void setDefCols() {//textFields,colNames
    if (textFields == null) textFields = new javax.swing.text.JTextComponent[] {this};
//    if (colNames == null) colNames = new String[] {this.getColumnName()};
    if (colNames == null) colNames = new String[] {getNavColumnName()};
  }
  private void setDefHelpText() {
    if (getHelpText()!=null) return;
    if (getRaDataSet() != null) {
      Column col = getRaDataSet().hasColumn(getColumnName());
      if (col!=null) {
        if (searchMode == 0) {/** @todo malo te textove prilagodi luModeF9 i luModeENTER */
          setHelpText("Unesite podatak "+col.getCaption().toUpperCase()+" ili ga dohvatite sa tipkom "+KeyEvent.getKeyText(KeyEvent.VK_F9));
        } else {
          setHelpText("Unesite prvih nekoliko znakova podatka "+col.getCaption().toUpperCase()+" i dohvatite strani klju\u010D tipkom "+KeyEvent.getKeyText(KeyEvent.VK_F9));
        }
      }
    }
  }
  private void setDefErrText(String val) {
//    if (getErrText()!=null) return;
    if (getRaDataSet() != null) {
      Column col = getRaDataSet().hasColumn(getColumnName());
      if (col!=null) {
        setErrText("Strani klju\u010D "+col.getCaption().toUpperCase()+" = "+val+" ne postoji! Dohvat - "+KeyEvent.getKeyText(KeyEvent.VK_F9));
      }
    }
  }
 /**
  * Ako je get iz istog dataseta kao onaj u koji je unos OBAVEZNO staviti na true
  */
  public void setSameSet(boolean newSameSet) {
    sameSet = newSameSet;
  }
  public boolean getSameSet() {
    return sameSet;
  }
 /**
  * Ako se columnName kojim slucajem razlikuje od kolone po kojoj se vrsi lookup
  */

  public void setNavColumnName(String colname) {
    navColumnName = colname;
    if (lD != null) {
      colNames = null;
      setDefCols();
    }
  }

  public boolean isKeepRow() {
    return keepRow && (lastNav.equalsIgnoreCase(getText()));
  }
  
  public String getNavColumnName() {
    return navColumnName;
  }

  private void smartSetNavColumnName() {
    if (navColumnName == null) setNavColumnName(getColumnName());
  }

  public void setColumnName(String columnName) {
    super.setColumnName(columnName);
    smartSetNavColumnName();
  }
  
  public void setExtraDirectColumn(String columnName) {
    extraDirectColumn = columnName;
  }
 /**
  * U colNames Parametar upisuju se imena kolona u raDataSetu koja se pretrazuju.
  * Moraju biti navedena istim redoslijedom kao text komponente u textFields parametru.
  * Prvo ime kolone je uvijek this.getColumnName()
  */
  public void setColNames(java.lang.String[] newColNames) {
    colNames = newColNames;
  }

  public java.lang.String[] getColNames() {
    return colNames;
  }
 /**
  * U textFields parametar upisuju se exranski objekti u koje lookup metode ubacuju trazene vrijednosti
  * Moraju biti navedene istim redoslijedom kao imena kolona u colNames parametru.
  */
  public void setTextFields(javax.swing.text.JTextComponent[] newTextFields) {
    textFields = newTextFields;
  }

  public javax.swing.text.JTextComponent[] getTextFields() {
    return textFields;
  }
 /**
  * VisCols parametar proslijedjuje se columnsbeanu u slucaju potrebe prikazivanja ekrana za dohvat.
  * U njemu se naznacuje koje su kolone vidljive na tablici za dohvat
  */
  public void setVisCols(int[] newVisCols) {
    visCols = newVisCols;
  }

  JlrNavField_NavigationListener navField_Navigator = new JlrNavField_NavigationListener();
  StatusListener mysl = null;
  public void addNavListener() {
    if (getDataSet() != null && luModeENTER != NULL) {
      try {
        navField_Navigator.install(getDataSet());
        getDataSet().addStatusListener(mysl = new StatusListener() {
          public void statusMessage(com.borland.dx.dataset.StatusEvent ev) {
            dataSetStatus = ev.getCode();
          }
        });
      } catch (Exception e) {
//        ST.prn(getColumnName());
//        ST.prn(e.toString());
      }
    }
  }

  public void rmvNavListener() {
    if (getDataSet() != null && luModeENTER != NULL) {
      try {
        navField_Navigator.uninstall(getDataSet());
        getDataSet().removeStatusListener(mysl);
      } catch (Exception e) {
//        ST.prn(getColumnName());
//        ST.prn(e.toString());
      }
    }
  }

  public int[] getVisCols() {
    return visCols;
  }
 /**
  * Dataset po kojem se pretrazuje. Razlicit od dataseta koji postavljamo setDataSet() metodom.
  */
  public void setRaDataSet(com.borland.dx.dataset.DataSet newRaDataSet) {
    raDataSet = newRaDataSet;
  }

  public void setDataSet(com.borland.dx.dataset.DataSet newDataSet) {
    rmvNavListener();
    super.setDataSet(newDataSet);
    addNavListener();
  }
  public com.borland.dx.dataset.DataSet getRaDataSet() {
    return raDataSet;
  }
 /**
  * U navProperties unijeti JlrNavField od kojeg zelis naslijediti propertije. Naslijedjuje sve osim
  * JdbTextField.columnName koji je potrebno definirati prije ovog setera
  * <pre>
  * UVJETI:
  *  1. u textFields od JlrNavField od kojega naslijedjujemo propertije MORA biti prijavljen this (JlrNavField)
  *  2. u columnNames od JlrNavField od kojega naslijedjujemo propertije MORA biti prijavljen this.getColumnName
  *  3. ti prijavljeni elementi moraju biti na istim pozicijama u arrayu
  * </pre>
  */
  public void setNavProperties(hr.restart.util.JlrNavField newNavProperties) {
    navProperties = newNavProperties;
  }
  private void inheritProperties(hr.restart.util.JlrNavField nPrF) {
    thisColNames=(String[])nPrF.getColNames().clone();
    thisTextFields=(javax.swing.text.JTextComponent[])nPrF.getTextFields().clone();
    if (//                            getColumnName
        (findMember(thisColNames,this.getNavColumnName())!=findMember(thisTextFields,this))
        //                               getColumnName
        || (findMember(thisColNames,this.getNavColumnName())==-1)
        || (findMember(thisTextFields,this)==-1)
       ) {
      System.out.println("Neispravno definiran JlrNavField (***"+getColumnName()+"***)od kojeg naslijedjujem propertije!");
      return;
    } //kontrola
    //                                             getColumnName
    int ancOrd = this.findMember(thisColNames,this.getNavColumnName());
    //zamjenjuje njegovu kolonu sa kolonom pretka
    if (ancOrd!=-1) {
      //                        getColumnName
      thisColNames[ancOrd]=nPrF.getNavColumnName();
      thisTextFields[ancOrd]=nPrF;
    }
//TEST
//ST.prn("=============================================");
//ST.prn(this.getColumnName());
//ST.prn("---------------------------------------------");
//ST.prn(thisColNames);
//for (int i=0;i<thisTextFields.length;i++) {
//  ST.prn("TextComponent: "+((JlrNavField)thisTextFields[i]).getColumnName()+" -- br.:",i);
//}
//ST.prn("=============================================");
//ENDTEST

    this.setColNames(thisColNames);
    this.setTextFields(thisTextFields);
    this.setRaDataSet(nPrF.getRaDataSet());
    this.setVisCols(nPrF.getVisCols());
    this.setAdditionalLookupFilter(nPrF.getAdditionalLookupFilter());
    this.setLookupFrameWidth(nPrF.getLookupFrameWidth());
    this.setLookupSaveName(nPrF.getLookupSaveName());
  }
  
  
  public void setAllowMultiple(boolean mult) {
    if (mult && navProperties == null)
      throw new RuntimeException(
          "allowMultiple is allowed only on slave navfields!");
    allowMulti = true;
  }
  
  public boolean isAllowMultiple() {
    return allowMulti;
  }
  
  public Condition getCondition() {
    if (allowMulti && getText().length() > 0 && 
        navProperties != null && navProperties.getText().length() == 0) 
          return getMultiCondition();
    if (navProperties != null) 
      return navProperties.getCondition();
    if (textFields == null || getText().length() > 0) 
      return super.getCondition();
    for (int i = 0; i < textFields.length; i++)
      if (textFields[i] instanceof JlrNavField) {
        JlrNavField slave = (JlrNavField) textFields[i];
        if (slave.isAllowMultiple() && slave.getText().length() > 0)
          return slave.getMultiCondition();
      }
    return super.getCondition();          
  }
  
  Condition getMultiCondition() {
    VarStr tx = new VarStr(getText());
    String[] parts = tx.indexOf(',') < 0 ? tx.split() : tx.splitTrimmed(',');
    String col = navProperties.getColumnName();
    DataSet ds = navProperties.getDataSet();
    if (ds.getColumn(col).getDataType() == Variant.STRING)
      return Condition.in(col, parts);
    int[] ip = new int[parts.length];
    for (int i = 0; i < ip.length; i++) {
      if (!Aus.isDigit(parts[i])) return Condition.none;
      ip[i] = Aus.getNumber(parts[i]);
    }
    return Condition.in(col, ip);
  }

  public String getMasterColumnName() {
    if (navProperties == null) return getNavColumnName();
    return navProperties.getNavColumnName();
  }
  
  public void focusGained(FocusEvent e) {
    super.focusGained(e);
    setEnterMode();
  }

  private int findMember(Object[] array,Object member) {
    for (int i=0;i<array.length;i++) {
      if (array[i].equals(member)) return i;
    }
    return -1;
  }

 /**
  * <pre>
  * Modovi ponasanja ekranske komponente:
  * -1 ponasa se kao JLabel
  * 0 (za podatke u kljucu)
  *   - na focus lost pretrazuje po raDataSetu i ispunjava textFields sa nadjenim vrijednostima
  *     ili ih brise ako text u toj komponenti nije nadjen
  *   - na F9 daje sve slogove u raDataSetu neovisno o upisanoj vrijednosti
  * 1 (za podatke u nazivima)
  *   - na focus lost ne radi nista
  *   - na F9 daje slogove filterirane u odnosu na upisanu vrijednost po nekoliko slova naziva.
  *     npr. na upisano 'BAN' daje sve slogove kod kojih vrijednost tog podatka sadrzi 'BAN'
  * 2 (za ostale netekstualne podatke)
  *   - na focus lost ne radi nista
  *   - na F9 daje slogove filterirane po tocnoj vrijednosti upisanog podatka
  * 3 (za konto, barcode i slicno)
  *   - na focus lost pretrazuje po tocno upisanoj vrijednosti ali case insensitive
  *   - na F9 daje slogove koji pocinju sa upisanim textom
  * </pre>
  */
  public void setSearchMode(int newSearchMode) {
    searchMode = getParameterSearchMode(newSearchMode);
    setDefaultLuMode();
  }

  /**
   * Zamjenjuje searchmode u odnosu na parametar u frmParam.getParam(srchmodrplc
   * npr. ako postoji srchmodrplc1 sa vrijednosti 3 on ce za getParameterSearchMode(1)
   * vratiti 3
	 * @param newSearchMode
	 * @return
	 */
	private int getParameterSearchMode(int newSearchMode) {
	  try {
	    int sm;
	    if (newSearchMode == 1) {//da ne gnoji po parametrima samo jedinicu upisi default text i ostalo
	      sm = Integer.parseInt(frmParam.getParam("sisfun","srchmodrplc"+1,"1","Koji search mode u JlrNavFieldu podmetnuti umjesto 1?"));
	      if (sm == 3) sm = 4;
	      if (sm == 33) sm = 3;
	    } else {
	      if (newSearchMode == -1) return -1;
	      String param = frmParam.getParam("sisfun","srchmodrplc"+newSearchMode);
	      if (param!=null) {
	        sm = Integer.parseInt(param);
	      } else {
	        sm = newSearchMode;
	      }
	    }
	    return sm;
    } catch (Exception e) {
    }
	  return newSearchMode;
	}

  private void setDefaultLuMode() {
    switch (searchMode) {
      case -1:
        luModeENTER = NULL;
        luModeF9 = NULL;
        break;
      case 0:
        luModeENTER = lD.EXACT;
        luModeF9 = lD.ALL;
        break;
      case 1:
        luModeENTER = NULL;
        luModeF9 = lD.TEXTAW | lD.F9;
        break;
      case 2:
        luModeENTER = NULL;
        luModeF9 = lD.EXACTIC | lD.F9;
        break;
      case 3:
        luModeENTER = lD.EXACTIC;
        luModeF9 = lD.TEXT | lD.F9;
        break;
      case 4:
        luModeENTER = NULL;
        luModeF9 = lD.TEXT | lD.F9;
        break;
    }
  }

  /**
   * Postavlja ponasanje na browse, i na focuslost
   * Koristi parametre iz lookupdata :
   * <pre>
   *   lookupData.EXACT - trazi po tocno upisanom -> textu value.equals(filter)
   *   lookupData.TEXT - trazi po pocetku teksta -> value.startsWith(filter)
   *   lookupData.ALL - daje sve podatke -> (true)
   *   lookupData.TEXTAW - trazi po bilo kojem pojavljivanju u textu -> value.indexOf(filter) > 0
   *   lookupData.EXACTIC - trazi po tocno upisanom ali ignorira velika i mala slova -> value.equalsIgnoreCase(filter)
   *   
   *   lookupData.F9 -dodatni flag s kojim se modificira neki od gornjih, a koji forsira dohvat (ab.f) 
   * </pre>
   * i parametar NULL = 99 - ne radi ama bas nista
   * @param newLuModeENTER - ponasanje na focuslost
   * @param newLuModeModeF9 - ponasanje na browse tipku
   */
  public void setSearchMode(int newLuModeENTER,int newLuModeModeF9) {
    luModeENTER = newLuModeENTER;
    luModeF9 = newLuModeModeF9 | lookupData.F9;
  }

  public int getSearchMode() {
    return searchMode;
  }
  public int getSearchModeF9() {
    return luModeF9;
  }
  public int getSearchModeENTER() {
    return luModeENTER;
  }

  /**
   * nakon ove metode JlrNavField ce se ponasati kao da je u pitanju focusLost (enter) do prvg pritiska na browse tipku
   */
  public void setEnterMode() {
    currentLuMode = luModeENTER;
    currVisCols = null;
  }

  /**
   * nakon ove metode JlrNavField ce se ponasati kao da je u pritisnuta browse tipka (F9) do prvg focuslosta
   */
  public void setBrowseMode() {
    currentLuMode = luModeF9;
    currVisCols = visCols;
  }

/**
 * Parametar kojem mu se kaze da li da radi focusLost odnosno lookup na AncestorAdded event
 * focusLost ce napraviti lookup i after_lookup samo ako je searchMode = 0 pa je pozeljno ako ima na
 * ekranu vise komponenti koje rade lookup na istu stvar i imaju searchMode=0
 * samo na jednoj ostaviti true a ostalima reci setFocusLostOnShow(false).
 * Default je, naravno, true.
 */
  public void setFocusLostOnShow(boolean newFocusLostOnShow) {
    focusLostOnShow = newFocusLostOnShow;
  }
  public boolean isFocusLostOnShow() {
    return focusLostOnShow;
  }
/**
 * Ako korisnik obrise text u JlrNavFieldu, da li raditi focus_lost ili ne? Pitanje je sad...
 * Rijesivo i s uvjetom (!jlrnavfield.getText().equals("")), ali ...
 * Default je true.
 */
  public void setAfterLookUpOnClear(boolean newAfterLookUpOnClear) {
    afterLookUpOnClear = newAfterLookUpOnClear;
  }
  public boolean isAfterLookUpOnClear() {
    return afterLookUpOnClear;
  }
  
  
  public void setAfterLookAlways(boolean newAfterLookAlways) {
    afterLookAlways = newAfterLookAlways;
  }
  
  /**
   * Treba li zvati afterLookup cak i ako je komponenta nevidljiva ili disablana? 
   */
  public boolean isAfterLookAlways() {
    return afterLookAlways;
  }
  
  boolean callAfterLookup() {
    return afterLookAlways || (isShowing() && isEnabled());
  }
  
/**
 * Da li da se na losefocus pojavljuje handlanje errora (kao na JraTextField) ako je upisana nepostojeca
 * vrijednost ili ne. Default je true - urlaj na korisnika jer je krivo unio
 */
  public void setHandleError(boolean newHandleError) {
    handleError = newHandleError;
  }
  public boolean isHandleError() {
    return handleError;
  }
/**
 * Overridano zato da na setText odmah ubacuje i u bindani dataset (posta)!
 */
  public void setText(String txt) {
    txt=txt==null?"":txt;
    super.setText(txt);
//    super.getDataBinder().focusLost(null);
    super.maskCheck();
  }
/**
 * Prazni sve JTextComponent-e koji su setirani u setTextFields(JTextComponent[])
 * odnosno zove metodu {@link #setText(String) JTextComponent[n].setText("")}.
 * NAPOMENA: U JlrNv.getTextFields() se ne nalazi JlrNv odnosno JlrNv.emptyTextFields() ne prazni jlrNV.
 */
  public void emptyTextFields() {
    if (textFields==null) return;
    for (int i=0;i<textFields.length;i++) {
      textFields[i].setText("");
    }
  }
  private boolean lookres = true;

  private void look(int[] visC) {
    if (currentLuMode == NULL) {
      return;
    }
    String textValue = getText();
    if (visC==null) {
      if (getText().equals("")) {
        if (navFieldFlag) {
          return;
        }
        emptyTextFields();
        lookres=false;
        if (afterLookUpOnClear && callAfterLookup()) after_lookUp();
        return;
      }
    }
//    saveSetPosition();
    lookres=true;
//    if (!lightLook) {
//ST.prn(this.getColumnName() +" is going to invoke HARD LOOKUP");
    if (lD==null) raInit();
    lD.setAdditionalLokupFilter(additionalFilter);
    try {
      lD.setAdditionalExactColumn(extraDirectColumn);
      if (getTopLevelAncestor() instanceof java.awt.Frame) {
        lookres = invokeLookup((java.awt.Frame)getTopLevelAncestor(),visC);
      } else if (getTopLevelAncestor() instanceof java.awt.Dialog) {
        lookres = invokeLookup((java.awt.Dialog)getTopLevelAncestor(),visC);
      } else {
        lookres = invokeLookup(visC);
      }
    } finally {
      lD.setAdditionalExactColumn(null);
      lD.setAdditionalLokupFilter(null);
    }
//    }
//    restoreSetPosition();
//    if (lookres) jumpFocus(); -> focusLost(e);
    if (callAfterLookup()) after_lookUp();
    if (!lookres&&visC==null) makeNavFieldError(textValue);
    if (lookres) setLastNavValues();
    if (getRaDataSet() != null && getRaDataSet().getRowFilterListener() != null) {
      getRaDataSet().removeRowFilterListener(null);
      getRaDataSet().dropIndex();
    }
    //lD.setAdditionalLokupFilter(null);
  }

  private void jumpFocus() {
    if (!isShowing()) return;
    if (!isEnabled()) return;
    if (getTopLevelAncestor() == null) return;
    try {
      javax.swing.FocusManager focManager = javax.swing.FocusManager.getCurrentManager();
      if (focManager instanceof javax.swing.DefaultFocusManager) {
        javax.swing.DefaultFocusManager defFocManager = (javax.swing.DefaultFocusManager)focManager;
        java.awt.Component focusComponent = getFocusComponent(defFocManager,this);
        if (focusComponent==null) {//to bi moglo znaciti da vise nema komponenti za focus u tom containeru
          java.awt.Component lastComponent = defFocManager.getLastComponent(getParent());
          defFocManager.focusNextComponent(lastComponent);
        } else {
          focusComponent.requestFocus();
        }
        this.setCaretPosition(0);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  private java.awt.Component getFocusComponent(javax.swing.DefaultFocusManager manager,java.awt.Component component) {
    java.awt.Component focusComponent = getComponentAfter(manager,component,component.getParent());
    if (Util.getUtil().containsArr(textFields,focusComponent)) {
      return getFocusComponent(manager,focusComponent);
    } else {
      return focusComponent;
    }
  }

  private java.awt.Component getComponentAfter(javax.swing.DefaultFocusManager manager,java.awt.Component component,java.awt.Container container) {
    java.awt.Component potentialComponent = manager.getComponentAfter(container,component);
    if (potentialComponent == null) return null;
    if (potentialComponent.isFocusTraversable()) return potentialComponent;
    return getComponentAfter(manager,potentialComponent,container);
  }
  /**
   *
   * @return da li je uneseni podatak pronadjen u referentnoj tabeli
   */
  public boolean isLastLookSuccessfull() {
    return lookres;
  }
  /**
   * Ova metoda omogucava prevariti sistem i podmetnuti da je lookup pronadjen a nije i obrnuto
   * @param suc true - pronasao, false - nije pronasao
   */
  public void setLastLookSuccess(boolean suc) {
    lookres=suc;
  }

  private javax.swing.AbstractButton navButton;
  public javax.swing.AbstractButton getNavButton() {
    return navButton;
  }
  /**
   * Dodaje action listener zadanom buttonu koji kaze keyF9Pressed
   * @param b
   */
  public void setNavButton(javax.swing.AbstractButton b) {
    if (navButton != null) navButton.removeActionListener(getNavButtonAction(navButton));
    navButton = b;
    if (navButton != null) {
//      if (navButton instanceof JraButton)
//        ((JraButton) navButton).setAutomaticFocusLost(true);
      navButton.addActionListener(new navButtonAction());
      navButton.setText("...");
      navButton.setToolTipText("Dohvat F9");
    }
  }
  public navButtonAction getNavButtonAction(javax.swing.AbstractButton b) {
    java.util.EventListener[] lis = b.getListeners(ActionListener.class);
    for (int i = 0; i < lis.length; i++) {
      if (lis[i] instanceof navButtonAction) return (navButtonAction)lis[i];
    }
    return null;
  }
  /** Settira sirinu lookupFrame-a koji bi se mogao
   * pojaviti pritiskom tipke F9 ili srodne akcije.
   * lookupFrame <U>se ovime moze samo prosiriti</U>.
   * Ovaj property se inheritira na ostale u grupi
   * metodom <CODE>JlrNavField.setNavProperties(JlrNavField)</CODE>
   * @param _w Zeljena sirina lookupFramea
   */
  public void setLookupFrameWidth(int _w) {
    lookupFrameWidth = _w;
  }
  /** Vraca zadanu sirinu lookupFrame-a
   * koji bi se mogao pojaviti na F9.
   * Ako sirina nije zadana vraca 0
   * @return zadanu sirinu lookupFrame-a koji bi se mogao pojaviti na F9
   */
  public int getLookupFrameWidth() {
    return lookupFrameWidth;
  }
  
  public void setLookupSaveName(String saveName) {
    lookupSaveName = saveName;
  }
  
  public String getLookupSaveName() {
    return lookupSaveName;
  }

/*
  boolean lightLook = false;
  void lightLookup() {
    if (lD==null) raInit();
    if (isShowing()) {
      focLost();
    } else {
      if (searchMode == 0 && focusLostOnShow) {
//ST.prn("searchMode == 0 && focusLostOnShow..."+getColumnName()+" is going to invokeLookup(null)");
        lightLook = invokeLookup(null);
      }
    }
  }
*/
  private void makeNavFieldError(String val) {
    if (!handleError) return;
    setDefErrText(val);
    this_ExceptionHandling(new java.lang.Exception());
  }

  private boolean invokeLookup(java.awt.Frame frame,int[] visC) {
    try {
/*
      if (searchMode==0) {
        if (visC==null) // na focus_lost
          return lD.lookUP(frame,this.getRaDataSet(),this,textFields,colNames,visC,lD.EXACT);
        else
          return lD.lookUP(frame,this.getRaDataSet(),this,textFields,colNames,visC,lD.ALL);
      } else if (searchMode==1) {
        return lD.lookUP(frame,this.getRaDataSet(),this,textFields,colNames,visC,lD.TEXT);
      } else if (searchMode==2) {
        return lD.lookUP(frame,this.getRaDataSet(),this,textFields,colNames,visC,lD.EXACT);
      }
      return true;
*/
      return lD.lookUP(frame,this.getRaDataSet(),this,textFields,colNames,currVisCols,currentLuMode);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }

  }

  /**
   * @return dataset u kojem su redovi koji bi se trebali pokazati na ekranu za dohvat
   */
  public com.borland.dx.dataset.StorageDataSet getLookupView() {
    if (lD==null) raInit();
    lD.getingView = false;
    lD.killView();
    lD.getingView = true;
    boolean succ = lD.lookUP(null,this.getRaDataSet(),this,textFields,colNames,new int[]{0},luModeF9);
    lD.getingView = false;
    DataSet view = lD.raSetView;
    StorageDataSet lookupView = new StorageDataSet();
    lookupView.setColumns(Util.getUtil().cloneCols(getRaDataSet().getColumns()));
    lookupView.open();
    if (view == null && succ) { // nasao je samo jednog pomocu raLocate
      lookupView.insertRow(false);
      getRaDataSet().copyTo(lookupView);
      lookupView.post();
    } else if (view != null) {
      view.first();
      do {
        lookupView.insertRow(false);
        view.copyTo(lookupView);
        lookupView.post();
      } while (view.next());
      view = null;
      lD.killView();
    }
    return lookupView;
  }

  private boolean invokeLookup(java.awt.Dialog frame,int[] visC) {
    try {
/*
      if (searchMode==0) {
        if (visC==null) // na focus_lost
          return lD.lookUP(frame,this.getRaDataSet(),this,textFields,colNames,visC,lD.EXACT);
        else
          return lD.lookUP(frame,this.getRaDataSet(),this,textFields,colNames,visC,lD.ALL);
      } else if (searchMode==1) {
        return lD.lookUP(frame,this.getRaDataSet(),this,textFields,colNames,visC,lD.TEXT);
      } else if (searchMode==2) {
        return lD.lookUP(frame,this.getRaDataSet(),this,textFields,colNames,visC,lD.EXACT);
      }
      return true;
*/
      return lD.lookUP(frame,this.getRaDataSet(),this,textFields,colNames,currVisCols,currentLuMode);
    }
    catch (Exception ex) {
      return false;
    }
  }
  private boolean invokeLookup(int[] visC) {
    try {
/*
      if (searchMode==0) {
        if (visC==null) // na focus_lost
          return lD.lookUP(null,this.getRaDataSet(),this,textFields,colNames,visC,lD.EXACT);
        else
          return lD.lookUP(null,this.getRaDataSet(),this,textFields,colNames,visC,lD.ALL);
      } else if (searchMode==1) {
        return lD.lookUP(null,this.getRaDataSet(),this,textFields,colNames,visC,lD.TEXT);
      } else if (searchMode==2) {
        return lD.lookUP(null,this.getRaDataSet(),this,textFields,colNames,visC,lD.EXACT);
      }
      return true;
*/
      return lD.lookUP(null,this.getRaDataSet(),this,textFields,colNames,currVisCols,currentLuMode);
    }
    catch (Exception ex) {
      return false;
    }
  }

  private void saveSetPosition() {
    if (!sameSet) return;
    isInserted = (getRaDataSet().getStatus()==com.borland.dx.dataset.RowStatus.INSERTED);
//ST.prn("isinserted: ",isInserted);
    if (isInserted) {
      try {
        getDispValues();
        getRaDataSet().cancel();
      } catch (Exception e) {e.printStackTrace();}
      return;
    }
    currRow = new com.borland.dx.dataset.DataRow(getRaDataSet());
    getRaDataSet().getStorageDataSet().getDataRow(currRow);
  }
//  private void restoreSetPosition() {
//    if (!sameSet) return;
//    if (isInserted) {
//      getRaDataSet().insertRow(true);
//      putDispValues();
//      return;
//    }
//    getRaDataSet().locate(currRow,com.borland.dx.dataset.Locate.FIRST);
//    currRow=null;
//  }

  public void getDispValues() {
    getAllDbComponents(getTopLevelAncestor());
    VDValues = new String[dbComponents.size()];
    for (int i=0;i<dbComponents.size();i++) {
      if (dbComponents.get(i) instanceof javax.swing.text.JTextComponent)
        VDValues[i] = ((javax.swing.text.JTextComponent)dbComponents.get(i)).getText();
      if (dbComponents.get(i) instanceof javax.swing.JRadioButton) {
        if (((javax.swing.JRadioButton)dbComponents.get(i)).isSelected()) {
          VDValues[i] = (String)(((com.borland.dbswing.JdbRadioButton)dbComponents.get(i)).getSelectedDataValue());
        } else {
          VDValues[i] = (String)(((com.borland.dbswing.JdbRadioButton)dbComponents.get(i)).getUnselectedDataValue());
        }
      }
      if (dbComponents.get(i) instanceof javax.swing.JCheckBox) {
        if (((javax.swing.JCheckBox)dbComponents.get(i)).isSelected()) {
          VDValues[i] = (String)(((com.borland.dbswing.JdbCheckBox)dbComponents.get(i)).getSelectedDataValue());
        } else {
          VDValues[i] = (String)(((com.borland.dbswing.JdbCheckBox)dbComponents.get(i)).getUnselectedDataValue());
        }
      }

/*
      if (dbComponents.get(i) instanceof javax.swing.JToggleButton) {
        if (((javax.swing.JToggleButton)dbComponents.get(i)).isSelected()) {
          VDValues[i] = (String)(((com.borland.dbswing.DBButtonDataBinder)dbComponents.get(i)).getSelectedDataValue());
        } else {
          VDValues[i] = (String)(((com.borland.dbswing.DBButtonDataBinder)dbComponents.get(i)).getUnselectedDataValue());
        }
      }
*/
    }
//    ST.prn(VDValues);
  }
  private void putDispValues() {
    for (int i=0;i<dbComponents.size();i++) {
      String colN = ((com.borland.dx.dataset.ColumnAware)dbComponents.get(i)).getColumnName();
      int colType = getRaDataSet().getColumn(colN).getDataType();
//      setDSValue(colN,colType, VDValues[i]); // ovo bi trebalo insertirati drito u dataset, ali to valjda radi i ono ispod
      com.borland.dx.dataset.Variant varValue= new com.borland.dx.dataset.Variant();
      com.borland.dx.text.VariantFormatStr varFormatter = new com.borland.dx.text.VariantFormatStr(null,colType);
      try {
        varFormatter.parse(VDValues[i],varValue,colType);
        getRaDataSet().setVariant(colN,varValue);
      }catch(Exception e) {e.printStackTrace();}
/*try {
      if (dbComponents.get(i) instanceof javax.swing.text.JTextComponent)
        ((javax.swing.text.JTextComponent)dbComponents.get(i)).setText(VDValues[i].toString());
        ((javax.swing.text.JTextComponent)dbComponents.get(i)).requestFocus();
      if (dbComponents.get(i) instanceof javax.swing.JToggleButton) {
        if (VDValues[i] == "true") {
          ((javax.swing.JToggleButton)dbComponents.get(i)).setSelected(true);
          ((javax.swing.JToggleButton)dbComponents.get(i)).requestFocus();
        } else {
          ((javax.swing.JToggleButton)dbComponents.get(i)).setSelected(false);
          ((javax.swing.JToggleButton)dbComponents.get(i)).requestFocus();
        }
      }
}catch (Exception e) {e.printStackTrace();};*/
    }
  }
  private void setDSValue(String cName, int cType, String cValue) {
// getDataSet().setBigDecimal();
// getDataSet().setBoolean();
// getDataSet().setDate();
// getDataSet().setFloat(); **************
// getDataSet().setInt(); **************
// getDataSet().setLong();
// getDataSet().setShort();
// getDataSet().setString();
// getDataSet().setTime();
// getDataSet().setTimestamp();
// getDataSet().setVariant("",varValue);!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


    if (cType==com.borland.dx.dataset.Variant.INT) getDataSet().setInt(cName,new Integer(cValue).parseInt(cValue));
  }
  public void getAllDbComponents(java.awt.Container cont) {
    dbComponents = new java.util.LinkedList();
    getDbComponents(cont);
  }

  private void getDbComponents(java.awt.Container ContainerName) {
    java.awt.Component sve_comp;
    int i=ContainerName.getComponentCount();
    int j=0;
    if (i != 0) {
      while (j<i) {
      sve_comp = ContainerName.getComponent(j);
//       if (!(sve_comp instanceof javax.swing.JTabbedPane))
      try {//da li je swing
        if (((javax.swing.JComponent)sve_comp).getComponentCount()>0) {//da li ima komponenti
//ST.prn(sve_comp.getClass().getName()+" ima podkomponente");
          getDbComponents((java.awt.Container) sve_comp); // zovi da vidim dalje
 //         return;
        }
      } catch (Exception e) {
//        ST.prn(sve_comp.getClass().getName()+" Nije JComponent");
      }
      try {//da li je dbswing i da li je isti dataset
        if (((com.borland.dx.dataset.ColumnAware)sve_comp).getDataSet().getStorageDataSet()==getRaDataSet().getStorageDataSet()) {
//          ST.prn("DODAJEM "+sve_comp.getClass().getName());
          dbComponents.add(sve_comp);
        }
      } catch (Exception e) {
        //ST.prn(sve_comp.getClass().getName()+" Nije dbComponent");
      }

      j++;
      }
    }
  }
/**
 * Overridana metoda tako da kad se LookAndFeel promijeni i ako je searchMode=-1 ponovo postavi izgled kao da je JLabel
 */
  public void updateUI() {
    if (searchMode==-1) {
      this.updLabelUI();
    } else {
      super.updateUI();
    }
  }
  
  ActionExecutor execF9 = new ActionExecutor() {
	  public void run() {
          keyF9Pressed();
          if (lookres && !userAfterLookup) jumpFocus();
      }
  };
  
  protected void this_keyPressed(KeyEvent e) {
    lastKey = e.getKeyCode();
    if (e.isConsumed() || !isShowing() || !isEnabled()) return;
    if (e.getKeyCode() == e.VK_F9) {
      if (navProperties != null && !navProperties.isEnabled()) return;
      userAfterLookup = true;
      e.consume();
      execF9.invokeLater();
    }
  }
  /**
   * Metoda kojom se forsira zvanje ekrana za dohvat (pozvati na jButton_actionpreformed)
   */
  public void keyF9Pressed() {
    setBrowseMode();
    doFocus=false;
    String bef = getText();
    look(visCols);
    lastNav = bef;
    setEnterMode();
    doFocus=true;
  }

/*  protected void this_focusGained() {
    txtBefore=getText();
  }*/
  public void focusLost(FocusEvent e) {
    if (!maskCheck()) return;
    if (e == null || !e.isTemporary()) select(0, 0);
    if (lastKey == KeyEvent.VK_ESCAPE) return;
//    if (lastKey == KeyEvent.VK_F10) return;
    if (!isNavValuesChanged()) return;
    userAfterLookup = true;
    focLost();
    if (lookres && doFocus && luModeENTER!=NULL && !navFieldFlag && !userAfterLookup) jumpFocus();
    // && !navFieldFlag dodao Branko
  }

  public boolean isNavValuesChanged() {
//    System.out.println("textBefore = "+getTxtBefore());
//    System.out.println("lastNavValue = "+lastNavValue);
    if (textFields == null) return isValueChanged();
    if (isValueChanged()) return true;
    if (isNavValueChanged()) return true;
    for (int i = 0; i < textFields.length; i++) {
      if (textFields[i] instanceof JlrNavField) {
        if (((JlrNavField)textFields[i]).isNavValueChanged()) return true;
      }
    }
    return false;
  }

  public void setLastNavValues() {
    lastNavValue = getText();
    if (textFields == null) return;
    for (int i = 0; i < textFields.length; i++) {
      if (textFields[i] instanceof JlrNavField) {
        JlrNavField jlr = (JlrNavField)textFields[i];
        jlr.lastNavValue = jlr.getText();
      }
    }
  }

  public boolean isNavValueChanged() {
    return !lastNavValue.equals(getText());
  }

  public void forceFocLost() {
    if (lD==null) raInit();
//    setEnterMode();
    look(null);
  }

  void focLost() {
    if (doFocus && isShowing()) {
//ST.prn("Radim poso... look ");
//      setEnterMode();
      look(null);
    } else {
//      doFocus=true;
    }
  }
  void updLabelUI() {
      javax.swing.JLabel jl = new javax.swing.JLabel();
      this.setBackground(jl.getBackground());
      this.setForeground(jl.getForeground());
      this.setBorder(jl.getBorder());
      this.setFont(jl.getFont());
      this.setEditable(false);
      this.setEnabled(false);
      this.setOpaque(jl.isOpaque());
      if (nextFocus != null) this.removeFocusListener(nextFocus);
      this.addFocusListener(nextFocus = new FocusAdapter() {
        public void focusGained(FocusEvent e) {
          hr.restart.swing.JraKeyListener.focusNext(e);
        }
      });
      jl=null;
  }
 /**
  * Metoda koja se poziva nakon lookupa (na focuslost ili F9 ovisno o parametru)
  * Ako se overrida, ne treba zvati super().
  */
  public void after_lookUp() {
    userAfterLookup = false;
  }

  void doOnHide() {
//    this.removeFocusListener(JlrNavFieldFocusListener);
    this.removeKeyListener(JrlNavFieldKeyListener);
//    lightLook = true;
    doneOnShow = false;
  }
  private String getTableName() {
    if (super.getDataSet()!=null) return super.getDataSet().getTableName();
    return "null";
  }

  private boolean showSimulation = false;
  public boolean isShowing() {
    if (showSimulation) {
      showSimulation = false;
      return true;
    }
    return super.isShowing();
  }

  void doOnShow() {
    if (doneOnShow) return;
    showSimulation = true;
    try {
//      if (getTopLevelAncestor() instanceof com.borland.primetime.ide.Browser) return;
    } catch (java.lang.NoClassDefFoundError exc) {    }
    try {
 //ST.prn(getTableName()+"."+getColumnName()+": ancestorAdded");
      raInit();
// -> setDataSet     addNavListener();
      if (getTableName()!="null") {
        if (focusLostOnShow) focLost();
//        lightLook = false;
        doneOnShow = true;
      }
    } catch (Exception ex) {
      System.out.println("Neki JlrNavField objekt (***"+getColumnName()+"***)nije ispravno inicijaliziran!");
      ex.printStackTrace();
    }
  }

  private raAdditionalLookupFilter additionalFilter = null;
  /**
   * ako treba jos koji filter za lookup tu je pravi trenutak da se postavi
   * @param addfilter
   */
  public void setAdditionalLookupFilter(raAdditionalLookupFilter addfilter) {
    additionalFilter = addfilter;
  }
  public raAdditionalLookupFilter getAdditionalLookupFilter() {
    return additionalFilter;
  }

  class JlrNavField_NavigationListener extends NavigationAdapter {
    public void navigated(DataSet ds) {
      StorageDataSet sourceDS = (StorageDataSet) ds;

      //System.out.println("status "+sourceDS.getTableName()+" je "+dataSetStatus);
      if (sourceDS.getStatus() == com.borland.dx.dataset.RowStatus.INSERTED) return;
      if (dataSetStatus != com.borland.dx.dataset.StatusEvent.EDIT_STARTED) return;

      if (focusLostOnShow) focLost();
    }
  }
  class navButtonAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      requestFocus();
      userAfterLookup = true;
      int oldLu9 = luModeF9;
      luModeF9 = lD.ALL;
      try {
        keyF9Pressed();
      } finally {
        luModeF9 = oldLu9;
      }
      if (lookres && !userAfterLookup) jumpFocus();
    }
  }
}
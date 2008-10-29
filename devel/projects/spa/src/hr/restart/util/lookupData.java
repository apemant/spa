/****license*****************************************************************
**   file: lookupData.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.swing.raSelectTableModifier;
import hr.restart.swing.raTableModifier;

import java.util.List;
import java.util.TooManyListenersException;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Locate;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.RowFilterResponse;
import com.borland.dx.dataset.Variant;

/**
 * <pre>
 * 1. PRIMJER KORISTENJA lookUp-a (najkompliciraniji lookUp):
 * void jdbTGrupa_keyPressed(KeyEvent e) {
 *     if (e.getKeyCode() == e.VK_F9) {
 *       look4grupa();
 *     }
 * }
 * void look4grupa() {
 *   String[] filter = {jdbTGrupa.getText(),""}; // ubacuje upisani text u filter
 *   String[] colnames = {"CGRART","NAZGRART"};  // koje kolone da se filteriraju i u koje da vraca vrijednosti
 *   int[] viscols=new int[] {0,2};              // koje kolone su vidljive na pregledu (ako ga bude)
 *   //viscols=null;                             //ako su vidljive kolone null nece pokazati ekran za dohvat
 *   lF=hr.restart.util.lookupData.getlookupData();
 *   String[] result = lF.lookUp(this,dM1.getGrupart(),colnames,filter,viscols); // metoda poziva lookupa
 *   if (result!=null) {
 *     jdbTGrupa.setText(result[0]);       //puni polje na ekranu sa pronadjenom vrijednoscu
 *     this.jTNazgrup.setText(result[1]);  //puni polje na ekranu sa pronadjenom vrijednoscu
 *   } else {
 *     // nije nista odabrao! (result=null)
 *     // isprogramirati akcije u tom slucaju npr. poruka ili praznjenje polja i sl.
 *   }
 *}
 *
 * 2. PRIMJER KORISTENJA lookUp-a (bezuvjetni lookUP):
 *   lF=hr.restart.util.lookupData.getlookupData();
 *   int[] viscols={0,2};
 *   String[] result = lF.lookUp(this,dM1.getGrupart(),viscols);
 *   if (result!=null) {
 *     jdbTGrupa.setText(result[0]);       //puni polje na ekranu sa pronadjenom vrijednoscu
 *     this.jTNazgrup.setText(result[2]);  //puni polje na ekranu sa pronadjenom vrijednoscu koja je u stringu
 *                                         //na poziciji kolone u datasetu
 *   } else {
 *     // nije nista odabrao! (result=null)
 *     // isprogramirati akcije u tom slucaju npr. poruka ili praznjenje polja i sl.
 *   }
 *
 * 3. PRIMJER KORISTENJA lookUp-a (pronalazak naziva stranog kljuca)
 *    lF=hr.restart.util.lookupData.getlookupData();
 *    lF.lookUP(dM1.getGrupart(),jdbTGrupa,jTNazgrup,"NAZGRART");
 * </pre>
 */

public class lookupData {
//TimeTrack TT = new TimeTrack(false);
sysoutTEST ST=new sysoutTEST(false);
  private static lookupData myLookupData;
  public static com.borland.dx.dataset.DataSet raSetView = new com.borland.dx.dataset.DataSetView();
//  public static com.borland.dx.dataset.DataSet raSetView;
  private boolean isNewRow = false;
  RowFilterListener lookupFilterListener = new RowFilterListener() {
        public void filterRow(ReadRow readRow, RowFilterResponse rowFilterResponse) {
          raSetView_filterRow(readRow, rowFilterResponse);
        }
    };
   RowFilterListener aditionalFilterListener = new RowFilterListener() {
      public void filterRow(ReadRow row, RowFilterResponse response) {
        if (additionalFilter == null || additionalFilter.isRow(row)) {
          response.add();
        } else response.ignore();
      }
    };
  public static final int EXACT=0;
  public static final int TEXT=1;
  public static final int ALL=2;
  public static final int TEXTAW=3;
  public static final int EXACTIC=4;
  public static final int INDIRECT=10;
  private static final int MODE_MASK = 127;
  public static final int F9 = 128; // flag za forsiranje lookupa, tj. za preskakanje direktnog locate-a
  
  private String[] COLNAMES;
  private String[] COLFILTERS;
  private String[] retValues;
  private int LUMODE=EXACT;
  private boolean forceF9 = false;
  private boolean keepRow = false;
  
  raSelectTableModifier multi;
  
  public List modifiers;
  
  public String saveName;
  
  public String frameTitle = "Dohvat";
/**
 * Default constructor
*/
  protected lookupData() {
  }
/**
 * vraca klasu (po uzoru na datamodul)
 */
  public static lookupData getlookupData() {
    if (myLookupData == null) {
      myLookupData = new lookupData();
    }
    //myLookupData.setAdditionalLokupFilter(null);
    return myLookupData;
  }
  
  /*  (ab.f)  dodavanje lookup metoda, koje rade isto sto i locate,
  ali _ne pomicu poziciju dataseta_, sto znaci da ih je pozeljno koristiti
  gdje god ne treba raditi nikakve promjene na datasetu, nego samo dovuci
  vrijednosti. Tipican primjer: table modifieri. Jer inace, ako je set
  koji se pretrazuje bindan na nekom ekranu, a taj ekran otvoren, dolazi
  do ruznog trcanja pointera kad god se repainta tablica s modifierom. */

  Variant shared = new Variant();
  Variant shared2 = new Variant();

  public boolean raLookup(DataSet ds, DataRow result, String col, String value) {
    ds.open();
    DataRow find = new DataRow(ds, col);
    shared.setFromString(ds.getColumn(col).getDataType(), value);
    find.setVariant(col, shared);
    return raLookup(ds, find, result);
  }

  public boolean raLookup(DataSet ds, DataRow result, String[] cols, String[] values) {
    ds.open();
    DataRow find = new DataRow(ds, cols);
    for (int i = 0; i < cols.length; i++) {
      shared.setFromString(ds.getColumn(cols[i]).getDataType(), values[i]);
      find.setVariant(cols[i], shared);
    }
    return raLookup(ds, find, result);
  }

  public DataRow raLookup(DataSet ds, String col, String value) {
    ds.open();
    DataRow result = new DataRow(ds);
    return raLookup(ds, result, col, value) ? result : null;    
  }

  public DataRow raLookup(DataSet ds, String[] cols, String[] values) {
    ds.open();
    DataRow result = new DataRow(ds);
    return raLookup(ds, result, cols, values) ? result : null;    
  }

  public boolean raLookup(DataSet ds, DataRow result, String[] cols,
                          DataSet values, int row) {
    return raLookup(ds, result, cols, values, cols, row);
  }

  public boolean raLookup(DataSet ds, DataRow result, String[] cols,
                          DataSet values, String[] keys, int row) {
    try {
      ds.open();
      DataRow find = new DataRow(ds, cols);
      for (int i = 0; i < cols.length; i++) {
        values.getVariant(keys[i], row, shared);
        if (shared.getType() == find.getColumn(cols[i]).getDataType())
          find.setVariant(cols[i], shared);
        else {
          shared2.setFromString(ds.getColumn(cols[i]).getDataType(), shared.toString());
          find.setVariant(cols[i], shared2);
        }
      }
      return raLookup(ds, find, result);
    } catch (Exception e) {
      return false;
    }
  }

  public DataRow raLookup(DataSet ds, String[] cols, DataSet values, int row) {
    return raLookup(ds, cols, values, cols, row);
  }

  public DataRow raLookup(DataSet ds, String[] cols, DataSet values, String[] keys, int row) {
    ds.open();
    DataRow result = new DataRow(ds);
    return raLookup(ds, result, cols, values, keys, row) ? result : null;    
  }

  public boolean raLookup(DataSet ds, DataRow result, String[] cols, ReadRow values) {
    return raLookup(ds, result, cols, values, cols);
  }

  public boolean raLookup(DataSet ds, DataRow result, String[] cols,
                          ReadRow values, String[] keys) {
    ds.open();
    DataRow find = new DataRow(ds, cols);
    for (int i = 0; i < cols.length; i++) {
      values.getVariant(keys[i], shared);
      find.setVariant(cols[i], shared);
    }
    return raLookup(ds, find, result);
  }

  public DataRow raLookup(DataSet ds, String[] cols, ReadRow values) {
    return raLookup(ds, cols, values, cols);
  }

  public DataRow raLookup(DataSet ds, String[] cols, ReadRow values, String[] keys) {
    ds.open();
    DataRow result = new DataRow(ds);
    return raLookup(ds, result, cols, values, keys) ? result : null;
  }

  private boolean raLookup(DataSet ds, ReadRow find, DataRow result) {
    handleFilters(ds, null);
    return ds.lookup(find, result, Locate.CASE_INSENSITIVE | Locate.FIRST | Locate.FAST);
  }
  /**
   * Locira odredjeni zapis po filterima u datasetu
   * <pre>
   * Poziv: raLocate(
   *    dm.getStanje, //dataset po kojem trazimo
   *    new com.borland.dx.dataset.Dataset[] {dm.getDoku,dm.stDoku} //niz datasetova iz kojih trebam vrijednosti koje trazimo
   *    new String[] {"polje1","polje2"}, //polja u nizu datasetova za trazenje
   *    new String[] {"polje1","polje2"} //polja u datasetu po kojem trazimo
   *  )
   * </pre>
   */
  public boolean raLocate(
      com.borland.dx.dataset.DataSet setToLocate,
      com.borland.dx.dataset.DataSet[] filterSets,
      String[] filterColNames,
      String[] searchColNames) {
    try {
      //com.borland.dx.dataset.DataRow locRow = new com.borland.dx.dataset.DataRow(setToLocate,searchColNames);
      com.borland.dx.dataset.Variant[] filterVarValues = new com.borland.dx.dataset.Variant[filterColNames.length];
      for (int i=0;i<filterVarValues.length;i++) {
        filterVarValues[i] = this.getVarValue(filterSets[i],filterColNames[i]);
      }
      return raLocate(setToLocate,searchColNames,filterVarValues);
    }
    catch (Exception ex) {
      return false;
    }
  }
  private com.borland.dx.dataset.Variant getVarValue(com.borland.dx.dataset.DataSet ds,String colName) {
    com.borland.dx.dataset.Variant varValue = new com.borland.dx.dataset.Variant();
    ds.getVariant(colName,varValue);
    return varValue;
  }
  /**
   * Locira odredjeni zapis po filterima u datasetu
   * <pre>
   * Poziv: raLocate(
   *    dm.getArtikli,
   *    new String[] {"polje1","polje2"},
   *    new com.borland.dx.dataset.Variant[] {dm.getArtikli.getVariant("polje1"),dm.getArtikli.getVariant("polje2")})
   * </pre>
   */
  public boolean raLocate(
      com.borland.dx.dataset.DataSet setToLocate,
      String[] colNames,
      com.borland.dx.dataset.Variant[] colFilters) {
    com.borland.dx.dataset.DataRow locRow = new com.borland.dx.dataset.DataRow(setToLocate,colNames);
    for (int i=0;i<colNames.length;i++) {
      try {
        locRow.setVariant(i,colFilters[i]);
      } catch (Exception e) {e.printStackTrace();};
    }
//    setToLocate.enableDataSetEvents(false);
//    boolean b = setToLocate.locate(locRow,com.borland.dx.dataset.Locate.FIRST|com.borland.dx.dataset.Locate.FAST);
//    setToLocate.enableDataSetEvents(true);
//    return b;
      return jbLocate(setToLocate,locRow,com.borland.dx.dataset.Locate.FIRST|com.borland.dx.dataset.Locate.FAST);
  }

  private boolean jbLocate(DataSet ds,ReadRow locrow,int opts) {
    try {
//      System.out.println("defaultCollator = "+java.text.Collator.getInstance(java.util.Locale.getDefault()));
      handleFilters(ds, additionalFilter == null ? null : aditionalFilterListener);
      return ds.locate(locrow,opts);
    } catch (Exception ex) {
//      System.out.println("ex: defaultCollator = "+java.text.Collator.getInstance(java.util.Locale.getDefault()));
//      System.out.println("ex: defaultCollator.getCollationKey = "+java.text.Collator.getInstance(java.util.Locale.getDefault()).getCollationKey(null));
      ex.printStackTrace();
      return false;
    }
  }

  //private RowFilterListener aditionalFilterListener = null;
/*  private void addAFilter(DataSet d) {
    if (additionalFilter==null) return;
    try {
      d.addRowFilterListener(aditionalFilterListener);
      d.refilter();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  private void rmvAFilter(DataSet d) {
    //if (additionalFilter==null) return;
    if (d.getRowFilterListener() == null) return;
    try {
      dropIndex(d, d.getRowFilterListener());
      //aditionalFilterListener = null;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }*/

/*  private boolean makeColNames(String[] colNames,String[] colFilters) {
    java.util.LinkedList lc= new java.util.LinkedList();
    java.util.LinkedList lf= new java.util.LinkedList();
    for (int i=0;i<colFilters.length;i++) {
      if (!(colFilters[i].equals("")||colFilters[i]==null)) {
        lc.add(colNames[i]);
        lf.add(colFilters[i]);
      }
    }
    if (lc.size()==0) return false;
    colNames = ll2string(lc);
    colFilters = ll2string(lf);
//ST.prn(colNames);
//ST.prn(colFilters);
    return true;
  } */
  private String[] ll2string(java.util.LinkedList ll) {
    String[] stmp = new String[ll.size()];
    for (int i=0;i<ll.size();i++) {
      stmp[i]=(String)ll.get(i);
    }
    return stmp;
  }
/**
 * <pre>
 * Npr. Treba naci slog u datasetu ds1 u kojem je column cl1 = "666" i cl2='TEST" i onda pozoves:
 * raLocate(ds1,new String[] {"cl1","cl2"}, new String[] {"666","TEST"})
 * </pre>
 */
  public boolean raLocate(
      com.borland.dx.dataset.DataSet setToLocate,
      String[] colNames,
      String[] colFilters) {
      return raLocate(setToLocate,colNames,colFilters,Locate.FIRST);
  }
/**
 * <pre>
 * Npr. Treba naci slog u datasetu ds1 u kojem je column cl1 = "666" i cl2='TEST" i
 * ako je svejedno jel "TeSt" ili "test" ili "tEsT" onda pozoves:
 * raLocate(ds1,new String[] {"cl1","cl2"}, new String[] {"666","TEST"},com.borland.dx.dataset.Locate.CASE_INSENSITIVE)
 * (pogledaj dokumentaciju od com.borland.dx.dataset.Locate)
 * </pre>
 */
  public boolean raLocate(
      com.borland.dx.dataset.DataSet setToLocate,
      String[] colNames,
      String[] colFilters, int locOptions) {
//
    setToLocate.open();

// ???? BUG - kada ovo tu stavim javlja java.lang.NullPointerException
//    at com.borland.dx.memorystore.k.a(Unknown Source) pri DataSet.locate(locrow,opts) u jbLocate;
//    shit happens eh?
//    hr.restart.swing.JraTable2.setLocaleForCollate(setToLocate);
//    System.out.println("setToLocate = "+setToLocate);
// zato mozda...:
//    if (setToLocate instanceof com.borland.dx.dataset.StorageDataSet) {
//      ((com.borland.dx.dataset.StorageDataSet)setToLocate).setLocale(null);
//      if (setToLocate.refreshSupported()) setToLocate.refresh(); //uzas
//    }
    java.util.LinkedList lc= new java.util.LinkedList();
    java.util.LinkedList lf= new java.util.LinkedList();
    for (int i=0;i<colFilters.length;i++) {
      if (!(colFilters[i].equals("")||colFilters[i]==null)) {
        lc.add(colNames[i]);
        lf.add(colFilters[i]);
      }
    }
    if (lc.size()==0) return false;
    colNames = ll2string(lc);
    colFilters = ll2string(lf);
//
    com.borland.dx.dataset.DataRow locRow = new com.borland.dx.dataset.DataRow(setToLocate,colNames);
    com.borland.dx.dataset.Variant vv;
    for (int i=0;i<colNames.length;i++) {
      try {
        int vType=locRow.getColumn(i).getDataType();
//ST.prn(locRow.getColumn(i).getColumnName()+" = "+colFilters[i]);
        vv=new com.borland.dx.dataset.Variant(vType);
        vv.setFromString(vType,colFilters[i]);
        locRow.setVariant(i,vv);
      } catch (Exception e) {
        return false;
      };
    }
//    return setToLocate.locate(locRow,Locate.FIRST|Locate.FAST|locOptions);
    return jbLocate(setToLocate,locRow,Locate.FIRST|Locate.FAST|locOptions);
  }

  /**
   * return raLocate(setToLocate,new String[] {colName},new String[] {colFilter},locOptions);
   */
  public boolean raLocate(
      com.borland.dx.dataset.DataSet setToLocate,
      String colName,
      String colFilter, int locOptions) {
    return raLocate(setToLocate,new String[] {colName},new String[] {colFilter},locOptions);
  }

  /**
   * return raLocate(setToLocate,new String[] {colName},new String[] {colFilter});
   */
  public boolean raLocate(
      com.borland.dx.dataset.DataSet setToLocate,
      String colName,
      String colFilter) {
    return raLocate(setToLocate,new String[] {colName},new String[] {colFilter});
  }
  /**
   * <pre>
   * metoda koja daje na pregled sve slogove i vraca vrijednost svih kolona iz zadanog dataseta
   * - bezuvjetni dohvat
   * </pre>
   */
   public String[] lookUp(java.awt.Window destFrame, com.borland.dx.dataset.DataSet setToSearch,int[] visCols) {
      String[] cNames=setToSearch.getColumnNames(setToSearch.getColumnCount());
      String[] cFilters=(String[])cNames.clone();
      this.EmptyString(cFilters);
      return this.lookUp(destFrame,setToSearch,cNames,cFilters,visCols);
   }
  /**
   * <pre>
   * metoda koja pronalazi odredjeni slog u datasetu s obzirom na zadani JdbTextField i vraca u navedeni text field
   * - za pronalazak naziva stranog kljuca pri detaljnom prikazu, ako ga ne pronadje prazni ga
   * - nikad ne daje ekran za dohvat
   * </pre>
   */
  public void lookUP(com.borland.dx.dataset.DataSet setToSearch,
                          com.borland.dbswing.JdbTextField filterField,
                          javax.swing.text.JTextComponent resultField,
                          String resultColName
                          ) {
    String[] cNames={filterField.getColumnName(),resultColName};
    String[] cFilters={filterField.getText(),""};
    String[] results=this.lookUp(null,setToSearch,cNames,cFilters,null);
    if (results!=null) {
      resultField.setText(results[1]);
    } else {
      resultField.setText("");
    }
  }
  private String getText(JlrNavField nav) {
    String text = nav.getText();
    if (nav.getFieldMask() instanceof hr.restart.swing.raTextMask) {
      hr.restart.swing.raTextMask mask = (hr.restart.swing.raTextMask) nav.getFieldMask();
      if (mask.isMasked()) text = text.replace(mask.getMaskCharacter(), ' ').trim();
    }
    return text;
  }
  /**
   * <pre>
   * metoda koja pronalazi odredjeni slog u datasetu s obzirom na zadani JdbTextField i vraca u navedeni NIZ text fieldova
   * - za pronalazak naziva stranog kljuca pri detaljnom prikazu, ako ga ne pronadje prazni ga i vraca false
   * -*-*-+ replicaton handling
   * </pre>
   */
  public boolean lookUP(java.awt.Window frame,com.borland.dx.dataset.DataSet setToSearch,
                          JlrNavField filterField,
                          javax.swing.text.JTextComponent[] resultFields,
                          String[] resultColNames, int[] visCols, int luMode
                          ) {
    boolean returnValue;
    LUMODE = luMode & MODE_MASK;    
    lupFrWidth = filterField.getLookupFrameWidth();
    int strLength=resultColNames.length+1;
    String[] cNames = new String[strLength];
    String[] cFilters=(String[]) cNames.clone();
    EmptyString(cFilters);
    //prvi string u filteru i colnamesima je od filterfielda
    if (luMode!=this.ALL && !filterField.isAllowMultiple()) 
      cFilters[0]=getText(filterField);
//    cNames[0]=filterField.getColumnName();
    cNames[0]=filterField.getNavColumnName();
    //napuni ostale colnameove
    for (int i=0;i<resultColNames.length;i++) cNames[i+1]=resultColNames[i];
    boolean vget = getingView;
    String[] results = null;
    raSelectTableModifier msel = null;
    try {
      saveName = filterField.getLookupSaveName();
      forceF9 = (luMode & F9) == F9;
      modifiers = filterField.getModifiers();
      keepRow = filterField.isKeepRow();
      msel = multi = !filterField.isAllowMultiple() ? null :
               new raSelectTableModifier(filterField.getRaDataSet(),
                   filterField.getMasterColumnName());
      if (msel != null)
        msel.addToSelection(filterField.getText());

      results=this.lookUp(frame,setToSearch,cNames,cFilters,visCols);
    } finally {
      keepRow = false;
      forceF9 = false;
      modifiers = null;
      multi = null;
      saveName = null;
    }
    if (vget) return results!=null;
    if (results!=null) {
      if (msel == null || msel.getSelection() == null) {
        filterField.setText(results[0]);
        for (int i=0;i<resultFields.length;i++) {
          resultFields[i].setText(results[i+1]);
          resultFields[i].setCaretPosition(0);
        }
      } else {
        filterField.emptyTextFields();
        filterField.setText(VarStr.join(msel.getSelection(), ',').toString());
      }
      returnValue = true;
    } else {
      if (luMode==this.EXACT) {
        for (int i=0;i<resultFields.length;i++) {
          resultFields[i].setText("");
        }
        filterField.setText("");
      }
      returnValue = false;
    }
    /*
    if (!returnValue) {
      // replication handling sa ponovnim pozivom iste funkcije
      if (setToSearch instanceof com.borland.dx.sql.dataset.QueryDataSet) {
        com.borland.dx.sql.dataset.QueryDataSet qdsToSearch = (com.borland.dx.sql.dataset.QueryDataSet)setToSearch;
        String imetab = Valid.getTableName(qdsToSearch.getQuery().getQueryString());
        String nacinrep = hr.restart.db.raReplicate.getNacinRep(imetab);
        if (nacinrep.equals("2")) {

//          String qry = getLookupQuery(imetab);
        }
      }
    }
    */
    return returnValue;
  }
/**
 * Setira mod za lookup
 * @param mode
 */
  public void setLookMode(int mode) {
    LUMODE = mode & MODE_MASK;    
  }
  
  public void setFrameTitle(String title) {
    frameTitle = title;
  }
  /**
   * <pre>
   * metoda koja prema zadanom nizu vrijednosti kolona u datasetu (com.borland.dx.dataset.Column[])
   * filterira dataset vraca niz stringova(??) istim redoslijedom ako pronadje zadani row, ako ne vraca null
   * - poziva se iz svih ostalih lookup metoda
   * </pre>
   */
  public String[] lookUp(java.awt.Window destFrame, com.borland.dx.dataset.DataSet setToSearch,String[] colNames, String[] colFilters,int[] visCols) {
//TT.Start("ISEMPTY??");
    if (isStringEmpty(colFilters)&&visCols==null) return null;
//TT.ReStart("LOCATE!!!"+colNames[0]);
    String[] extraColNames = null;
    if (extraExactColumn != null) {
      extraColNames = new String[colNames.length];
      System.arraycopy(colNames, 0, extraColNames, 0, colNames.length);
      extraColNames[0] = extraExactColumn;
    }
    if (!forceF9 && LUMODE != INDIRECT && !isStringEmpty(colFilters) &&
        (raLocate(setToSearch,colNames,colFilters,Locate.CASE_INSENSITIVE) ||
        (extraExactColumn != null && raLocate(setToSearch,
            extraColNames,colFilters,Locate.CASE_INSENSITIVE)))) {
      if (!isAdditionalFilter(setToSearch)) return null;
      retValues=(String[])colNames.clone();
      EmptyString(retValues);
      for (int i=0;i<colNames.length;i++) {
        retValues[i]=getColStringVal(setToSearch.getColumn(colNames[i]),setToSearch);
      }
//      ST.prn(retValues);
//TT.Stop();
//ST.prn("locirano");
      return retValues;
    } else if (visCols==null) return null;
/*
IZBACENO            TT.ReStart("close...");
ZBOG                    raSetView.close();
OPTIMIZACIJE       TT.ReStart("setStorageDataSet...");
-> cloneDSView()        raSetView.setStorageDataSet(setToSearch.getStorageDataSet());
*/
//TT.ReStart("create raSetView");
    //raSetView = setToSearch.cloneDataSetView();
    raSetView = setToSearch;//15.07.2003. zbog collating sequenca koji u borlandu bugira, pa se izvodi sql-om
//TT.ReStart("COLNAMES=colNames...");
    COLNAMES=colNames;
//TT.ReStart("COLFILTERS=colFilters...");
    COLFILTERS=colFilters;
//TT.ReStart("retValues=(String[])COLNAMES.clone()...");
    retValues=(String[])COLFILTERS.clone();
//TT.ReStart("Open...");
    raSetView.open();
//TT.ReStart("SetFilter...");
    try {
      int lastRow = raSetView.getRow();
      if (frmParam.getParam("sisfun","sortgetview","D","Sortirati podatke kod dohvata (F9) po polju u kojem je stisnuto ").equalsIgnoreCase("D")) {
        raSetView.setSort(new com.borland.dx.dataset.SortDescriptor(new String[] {colNames[0]})); //cuda radi!!!?
       // handleFilters(raSetView, lookupFilterListener);
      }
      handleFilters(raSetView, (colFilters == null || multi != null || isStringEmpty(colFilters))
          ? null : lookupFilterListener);
//      setFilter(raSetView);
  //TT.ReStart("Refilter...");
//      raSetView.refilter();
  //TT.ReStart("getRowCount...");
      int foundRowsCount=raSetView.getRowCount();
  //TT.Stop();
      if (foundRowsCount==1 && LUMODE != INDIRECT) {
        //System.out.println("here");
        //System.out.println(raSetView);
        EmptyString(retValues);
        for (int i=0;i<colNames.length;i++) {
          retValues[i]=getColStringVal(setToSearch.getColumn(colNames[i]),setToSearch);
        }
        //System.out.println(VarStr.join(COLNAMES, ','));
        //System.out.println(VarStr.join(retValues, ','));
        killView();
        
        return retValues;
      } 
      if (keepRow && lastRow >= 0 && lastRow < foundRowsCount)
        raSetView.goToRow(lastRow);
      
      System.out.println("keeprow "+ keepRow + " lastRow "+lastRow);
      
        if (visCols==null) { //ako je viscols=null nemoj prikazati ekran i vrati null
          killView();
          return null;
        }
          if (foundRowsCount==0) {
            EmptyString(COLFILTERS);
            raSetView.refilter();
          }
          lookupFrame lupFr;
  /*        if (colNames.length > 1) {
            raSetView.toggleViewOrder(colNames[1]);
          }
          raSetView.toggleViewOrder(colNames[0]);
  */
          if (getingView) {
            getingView = false;
            return null;
          }        
          if (destFrame instanceof java.awt.Frame)
            lupFr = lookupFrame.getLookupFrame((java.awt.Frame)destFrame,raSetView,visCols);
          else
            lupFr = lookupFrame.getLookupFrame((java.awt.Dialog)destFrame,raSetView,visCols);
          if (modifiers != null)
            for (int i = 0; i < modifiers.size(); i++)
              lupFr.jdbTable1.addTableModifier((raTableModifier) modifiers.get(i));
          lupFr.setSaveName(saveName);
          lupFr.setTitle(frameTitle);
          if (multi != null) {
            lupFr.oKpanel1.setEnterEnabled(raSelectTableModifier.space);
            System.out.println("enter enabled: "+raSelectTableModifier.space);
            lupFr.jdbTable1.addTableModifier(multi);
          }
          /*int h = lupFr.oKpanel1.getPreferredSize().height;
          lupFr.oKpanel1.setPreferredSize(new java.awt.Dimension(lupFrWidth, h));*/
          lupFr.ShowCenter(true,!keepRow && LUMODE != INDIRECT,0,0);
          if (modifiers != null)
            for (int i = 0; i < modifiers.size(); i++)
              lupFr.jdbTable1.removeTableModifier((raTableModifier) modifiers.get(i));
          if (multi != null) {
            lupFr.oKpanel1.setEnterEnabled(true);
            lupFr.jdbTable1.removeTableModifier(multi);
          }
          killView();
          return lupFr.getRetValuesUI();
    } finally {
      /*if (raSetView != null) {
        dropIndex(raSetView, lookupFilterListener);*/
        raSetView = null;
      //}
    }
  }
  public int lupFrWidth = 0;
  boolean getingView = false;
/**
 * Stavlja sve clanove zadanog stringa na vrijednost ""
 */
  public void EmptyString(String[] strToClear) {
    for (int i=0;i<strToClear.length;i++) {
      strToClear[i]="";
    }
  }
/**
 *
 */
  public boolean isStringEmpty(String[] str) {
    if (str==null) return true;
    if (str.length==0) return true;
    for (int i=0;i<str.length;i++) {
      if (!str[i].equals("")) return false;
    }
    return true;
  }
  void killView() {
    if (getingView) {
      getingView = false;
      return;
    }
    try {
//zbog raSetView=setToSearch:
      /*if (raSetView != null) 
        dropIndex(raSetView, lookupFilterListener);*/
      raSetView = null;
    } catch (Exception e) {
      System.out.println("lookupData.killView: "+e);
      e.printStackTrace();
    }
  }
  
  void handleFilters(DataSet ds, RowFilterListener listener) {
    if (listener != null) {
      ds.removeRowFilterListener(null);
      try {
        ds.addRowFilterListener(listener);
      } catch (TooManyListenersException e) {
        e.printStackTrace();
      }
      ds.refilter();
    } else if (ds.getRowFilterListener() != null) {
      ds.removeRowFilterListener(null);
      ds.dropIndex();
    }
  }
  
  /*void dropIndex(DataSet ds, RowFilterListener rf) {
    long row = ds.getInternalRow();
    ds.enableDataSetEvents(false);
    ds.removeRowFilterListener(null);
    ds.dropIndex();
    ds.goToInternalRow(row);
    ds.enableDataSetEvents(true);
  }*/
/**
 * Vraca vrijednost zadanih imena kolona. Koristi se interno u lookupData i lookupFrame i zato je public
 */
  public String[] getCOLNAMES() {
    return COLNAMES;
  }
/**
 * Samo dodaje listener na dataset. Koristi se interno u lookupData i lookupFrame i zato je public
 */
  /*public void setFilter(com.borland.dx.dataset.DataSet ds) {
    try {
      if (ds.getRowFilterListener()!=lookupFilterListener) {
        ds.removeRowFilterListener(ds.getRowFilterListener());
        ds.addRowFilterListener(lookupFilterListener);//END LISTENER
      }
    } catch (Exception e) {e.printStackTrace();}
  }//end setfilter
*/
  boolean showRow(ReadRow row) {
    if (COLNAMES.length!=retValues.length) return true;
    for (int i=0;i<COLNAMES.length;i++) {
      com.borland.dx.dataset.Column col=row.hasColumn(COLNAMES[i]);
      if (col!=null) {
        String filt = COLFILTERS[i].trim();
//System.out.println("filtering "+COLNAMES[i]+" = "+COLFILTERS[i]+" ? "+getColStringVal(the_column,row));
        if (!filt.equals("")) {
          if (LUMODE==this.EXACT) {
            if (!filt.equals(getColStringVal(col,row).trim()))
              return false;
          } else if (LUMODE==this.TEXT) {
            if (filt.startsWith("*") && getColStringVal(col,row).trim().
                toLowerCase().indexOf(filt.substring(1).toLowerCase()) < 0)
              return false;
            if (!filt.startsWith("*") && !getColStringVal(col,row).trim().
                toLowerCase().startsWith(filt.toLowerCase()))
              return false;
          } else if (LUMODE==this.TEXTAW) {
            if (getColStringVal(col,row).trim().toLowerCase().
                    indexOf(filt.toLowerCase()) < 0)
              return false;
          } else if (LUMODE==EXACTIC) {
            if (!filt.equalsIgnoreCase(getColStringVal(col,row).trim()))
              return false;
          }
        }//endif varFormatter
        try {
          retValues[i]=getColStringVal(col,row);
        }
        catch (Exception ex) {
/*
          sysoutTEST ST = new sysoutTEST(false);
          System.out.println("Exception : " + ex);
          System.out.println("Row : "+ row);
          System.out.println("Column : "+the_column);
          System.out.println("retValues : ");
          ST.prn(retValues);
*/
        }
      }//endif colmn!=null

    }//end for
   return true;
  }//end showrow
/**
* vraca u string vrijednost zadane kolone iz dataseta
*/
  public String getColStringVal(com.borland.dx.dataset.Column colmn,com.borland.dx.dataset.ReadRow row) {
    com.borland.dx.dataset.Variant varValue= new com.borland.dx.dataset.Variant();
    com.borland.dx.text.VariantFormatter varFormatter;
    varFormatter=colmn.getFormatter();
    row.getVariant(colmn.getColumnName(),varValue);
    return varFormatter.format(varValue);
  }
/**
 * Vraca u string vrijednost zadane kolone (colmn), od currRow reda u datasetu te kolone
 */
  public String getCurrRowColValue(com.borland.dx.dataset.DataSet ds,String colName,int currRow) {
    com.borland.dx.dataset.Variant varValue= new com.borland.dx.dataset.Variant();
    com.borland.dx.text.VariantFormatter varFormatter;
    com.borland.dx.dataset.Column colmn = ds.getColumn(colName);
    varFormatter=colmn.getFormatter();
    ds.getVariant(colName,currRow,varValue);
    return varFormatter.format(varValue);
  }
/**
 * Mice char iz stringa (za numeric vrijednosti u stringu)
 */
  public String stripValue(String str,char toRemove) {
    char[] chr = str.toCharArray();
    String result = "";
    for (int i=0;i<chr.length;i++) {
      if (chr[i]!=toRemove) {
        result = result.concat(new String(chr,i,1));
      }
    }
    return result;
  }
  void raSetView_filterRow(ReadRow readRow, RowFilterResponse rowFilterResponse) {
    if (COLNAMES.length==0) rowFilterResponse.add();
    else {
      if (showRow(readRow)&&isAdditionalFilter(readRow)) {
        rowFilterResponse.add();
      } else {
        rowFilterResponse.ignore();
      }
    }
  }
//additionalfilter
  boolean isAdditionalFilter(ReadRow row) {
    if (additionalFilter == null) return true;
    return additionalFilter.isRow(row);
  }

  private raAdditionalLookupFilter additionalFilter = null;
  public void setAdditionalLokupFilter(raAdditionalLookupFilter f) {
    additionalFilter = f;
  }
  
  private String extraExactColumn = null;
  public void setAdditionalExactColumn(String columnName) {
    extraExactColumn = columnName;
  }

}//EOC
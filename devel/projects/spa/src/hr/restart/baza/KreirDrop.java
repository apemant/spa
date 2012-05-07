/****license*****************************************************************
**   file: KreirDrop.java
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
package hr.restart.baza;

import hr.restart.db.raPreparedStatement;
import hr.restart.sisfun.TextFile;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Int2;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JTextArea;

import org.w3c.tools.codec.Base64Decoder;
import org.w3c.tools.codec.Base64Encoder;
import org.w3c.tools.codec.Base64FormatException;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jb.util.TriStateProperty;


public abstract class KreirDrop {
  public static String ERROR_KEY = "$ERROR";
  
  dM dm = dM.getDataModule();

  public String SqlDefTabela ;
  public String Naziv;
  public String[] DefIndex;
  public String[] NaziviIdx;
  public String[] pkey;
  public String[] colnames;
  public DDLCreator ddl = new DDLCreator();
  protected Column[] origColumns;
  protected static HashMap modules = new HashMap(200);
  protected static HashMap moduleNames = new HashMap(400);

  private static raTransferNotifier track = null;

  public KreirDrop() {
    setall();
    pkey = ddl.getPrimaryKey();
    ddl.dispose();
  }
  
  protected void initModule() {
    try {
      if (!getDefinition()) setall();
      pkey = ddl.getPrimaryKey();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
    ddl.dispose();
  }

  public static void refreshModules() {
    System.out.println("Refreshing modules...");
    for (Iterator i = modules.keySet().iterator(); i.hasNext();) {
      KreirDrop m = getModule((String) i.next());
      m.setall();
      m.reconnectQueryDataSets();      
      m.ddl.dispose();
    }
    System.out.println("...done (refreshing modules).");
  }

  public Column[] getColumns() {
    return origColumns;
  }

  public Column getColumn(String name) {
    for (int i = 0; i < origColumns.length; i++)
      if (origColumns[i].getColumnName().equalsIgnoreCase(name))
        return origColumns[i];
    return null;
  }

  public static KreirDrop[] getModulesWithColumns(String[] cols) {
    if (cols.length == 1) return getModulesWithColumn(cols[0]);
    ArrayList mods = new ArrayList();
    for (Iterator it = modules.keySet().iterator(); it.hasNext();) {
      KreirDrop mod = getModule((String) it.next());
      boolean all = true;
      if (mod != null) {
        for (int i = 0; all && i < cols.length; i++)
          if (mod.getColumn(cols[i]) == null) all = false;
        if (all) mods.add(mod);
      }
    }
    return (KreirDrop[]) mods.toArray(new KreirDrop[mods.size()]);
  }

  public static KreirDrop[] getModulesWithColumn(String column) {
    ArrayList mods = new ArrayList();
    for (Iterator it = modules.keySet().iterator(); it.hasNext();) {
      KreirDrop mod = getModule((String) it.next());
      if (mod != null && mod.getColumn(column) != null)
        mods.add(mod);
    }
    return (KreirDrop[]) mods.toArray(new KreirDrop[mods.size()]);
  }

  public static KreirDrop getModule(String name) {
    if (modules.containsKey(name))
      return (KreirDrop) modules.get(name);
    return null;
  }

  public static KreirDrop[] getModulesByNames(String[] names) {
    KreirDrop[] ret = new KreirDrop[names.length];
    for (int i = 0; i < names.length; i++) {
      ret[i] = getModuleByName(names[i]);
      if (ret[i] == null)
        throw new IllegalArgumentException("No such module: '"+names[i]+"'");
    }
    return ret;
  }

  public static KreirDrop getModuleByName(String name) {
    if (moduleNames.containsKey(name))
      return getModule((String) moduleNames.get(name));
    for (Iterator i = modules.keySet().iterator(); i.hasNext(); ) {
      String mod = (String) i.next();
      if (mod.substring(mod.lastIndexOf('.') + 1).equalsIgnoreCase(name)) {
        moduleNames.put(name, mod);
        return getModule(mod);
      }
    }
    return null;
  }
  
  public static void loadData(StorageDataSet dest, File file) {
  	String sep = Aus.getDumpSeparator();
  	
  	loadData(dest, null, file, sep);
  }
  
  public static void loadData(StorageDataSet dest, String cols, File file) {
    String sep = Aus.getDumpSeparator();
  	
  	loadData(dest, cols, file, sep);
  }
  
  public static void loadData(StorageDataSet dest, File file, String sep) {
  	loadData(dest, null, file, sep);
  }
  
  public static void loadData(StorageDataSet dest, String cols, File file, String sep) {
  	
  	String[] cnames = null;
  	if (cols == null) cnames = dest.getColumnNames(dest.getColumnCount());
  	else if (cols.indexOf(',') < 0) cnames = new VarStr(cols).split();
  	else cnames = new VarStr(cols).splitTrimmed(',');
  	
  	Column[] dc = new Column[cnames.length];
  	for (int i = 0; i < cnames.length; i++)
  		if ((dc[i] = dest.hasColumn(cnames[i])) == null)
  			throw new UnsupportedOperationException("Missing column in dataset: "+cnames[i]);

  	TextFile dat = TextFile.read(file);
    if (dat == null) return;

  	if (track != null) track.rowTransfered(file.getName(), track.LOAD_STARTED, 0, null);
	
  	try {
      String line;
      int rows = 0;
      Variant v = new Variant();
       
      while ((line = dat.in()) != null) {
        String[] parts = new VarStr(line).split(sep);
        if (cnames.length != parts.length && 
        		!(line.endsWith(sep) && parts.length - 1 == cnames.length))
        	throw new UnsupportedOperationException("Incompatible data <-> columns");
        dest.insertRow(false);
        for (int i = 0; i < cnames.length; i++) {
          String val = new VarStr(parts[i]).trim().replaceAll("\\n", "\n").
                 replaceAll("</sep>", sep).toString();
          if (val.length() > 0) {
            v.setFromString(dc[i].getDataType(), val);
            dest.setVariant(cnames[i], v);
          }
        }
        dest.post();
        ++rows;
        if (track != null) track.rowTransfered(file.getName(), track.ROW_LOADED, rows, null);
      }
      if (track != null) track.rowTransfered(file.getName(), track.LOAD_FINISHED, rows, null);
    } finally {
      dat.close();
    }
  }

  /**
   * Vraca ukupan broj redova doticne tablice.<p>
   * @return ditto.
   */

  public int getRowCount() {
    return getRowCount("");
  }

  /**
   * Vraca broj redova tablice koji ispunjavaju uvjet where.<p>
   * @param where uvjet (nrp. "CPAR=6").
   * @return broj redova.
   */
  public int getRowCount(String where) {
    VarStr q = new VarStr(getQueryDataSet().getOriginalQueryString());
    int wh = q.indexOfIgnoreCase(" where");
    if (wh >= 0) q.truncate(wh);
    if (where != null && where.length() > 0)
      q.append(" WHERE ").append(where);
    q.replaceFirst("*"," COUNT(*) ");
    return Valid.getValid().getSetCount(Util.getNewQueryDataSet(q.toString()), 0);
  }

  /**
   * Vraca broj redova tablice filtrirane filtrom filter. :)<p>
   * @param filter ditto.
   * @return da ne povjerujes, broj redova!
   */
  public int getRowCount(Condition filter) {
    return getRowCount(filter.toString());
  }
  
  public StorageDataSet getReadonlySet() {
  	StorageDataSet ret = new StorageDataSet();
  	ret.setLocale(Aus.hr);
  	Column[] cols = new Column[origColumns.length];
  	for (int i = 0; i < cols.length; i++)
  		cols[i] = origColumns[i].cloneColumn();
  	ret.setColumns(cols);
  	ret.open();
  	return ret;
  }
  
  public StorageDataSet getScopedSet(String columns) {
  	if (columns.indexOf(',') < 0)
  		return getScopedSet(new VarStr(columns).split());
  	return getScopedSet(new VarStr(columns).splitTrimmed(','));
  }
  
  public StorageDataSet getScopedSet(String[] columns) {
  	StorageDataSet ret = new StorageDataSet();
  	ret.setLocale(Aus.hr);
  	Column[] cols = new Column[columns.length];
  	for (int i = 0; i < cols.length; i++)
  		cols[i] = getColumn(columns[i]).cloneColumn();
  	ret.setColumns(cols);
  	return ret;
  }

  /**
   * Vraca NOVI QueryDataSet za ovu tablicu, bez WHERE filtera.
   * (SELECT * FROM <table>), sa svim kloniranim kolonama.
   * Razlikuje se od copyDataSet() po tome sto uvijek vraca
   * QueryDataSet (nikad raDataSet) tako da se treba koristiti
   * kao privremene DataSet-ove (za koje ne treba automatski
   * refresh kojeg omogucuje raDataSet).<p>
   * @return novi QueryDataSet.
   */
  public QueryDataSet getTempSet() {
    return getTempSet("");
  }

  /**
   * Vraca NOVI QueryDataSet za ovu tablicu, sa definiranim filterom
   * (SELECT * FROM <table> WHERE filter), sa svim kloniranim kolonama.<p>
   * @param cond Condition koji predstavlja filter
   * @return novi QueryDataSet.
   */
  public QueryDataSet getTempSet(Condition cond) {
    return getTempSet(cond.toString());
  }
  
  public QueryDataSet getTempSet(String cols, Condition cond) {
    return getTempSet(cols, cond.toString());
  }
  
  public QueryDataSet getTempSet(String[] cols, Condition cond) {
    return getTempSet(cols, cond.toString());
  }

  /**
   * Vraca NOVI QueryDataSet za ovu tablicu, sa definiranim filterom
   * (SELECT * FROM <table> WHERE filter), sa svim kloniranim kolonama.<p>
   * @param filter Filter za ovaj QueryDataSet (ono iza "WHERE").
   * @return novi QueryDataSet.
   */
  public QueryDataSet getTempSet(String filter) {
    QueryDataSet filtered = new QueryDataSet();
    createFilteredDataSet(filtered, filter);
    return filtered;
  }
  
  public QueryDataSet getTempSet(String cols, String filter) {
    QueryDataSet filtered = new QueryDataSet();
    String[] cols2; 
    if (cols.indexOf(',') >= 0) cols2 = new VarStr(cols).splitTrimmed(',');
    else cols2 = new VarStr(cols).split();
    createFilteredDataSet(filtered, cols2, filter);
    return filtered;
  }
  
  public QueryDataSet getTempSet(String[] cols, String filter) {
    QueryDataSet filtered = new QueryDataSet();
    createFilteredDataSet(filtered, cols, filter);
    return filtered;
  }

  /**
   * Vraca NOVI QueryDataSet za ovu tablicu, bez WHERE filtera.
   * (SELECT * FROM <table>), sa svim kloniranim kolonama.<p>
   * @return novi QueryDataSet, ili raDataSet, ovisno o tipu
   * kojeg vraca getQueryDataSet().
   */
  public QueryDataSet copyDataSet() {
    return getFilteredDataSet("");
  }

  /**
   * Vraca NOVI QueryDataSet za ovu tablicu, sa definiranim filterom
   * (SELECT * FROM <table> WHERE filter), sa svim kloniranim kolonama.<p>
   * @param cond Condition koji predstavlja filter.
   * @return novi QueryDataSet, ili raDataSet, ovisno o tipu
   * kojeg vraca getQueryDataSet().
   */
  public QueryDataSet getFilteredDataSet(Condition cond) {
    return getFilteredDataSet(cond.toString());
  }

  /**
   * Vraca NOVI QueryDataSet za ovu tablicu, sa definiranim filterom
   * (SELECT * FROM <table> WHERE filter), sa svim kloniranim kolonama.<p>
   * @param where filter.
   * @return novi QueryDataSet, ili raDataSet, ovisno o tipu
   * kojeg vraca getQueryDataSet().
   */
  public QueryDataSet getFilteredDataSet(String where) {
    QueryDataSet filtered;
    if (getQueryDataSet() instanceof raDataSet)
     filtered = new raDataSet();
    else filtered = new QueryDataSet();
    createFilteredDataSet(filtered, where);
    return filtered;
  }
  
  /**
   * Vraca NOVI QueryDataSet za ovu tablicu, sa definiranim filterom
   * (SELECT cols FROM <table> WHERE filter), sa zadanim kloniranim kolonama.<p>
   * @params cols kolone koje trebaju biti u setu odvojene zarezom
   * @param filter filter.
   * @return novi QueryDataSet, ili raDataSet, ovisno o tipu
   * kojeg vraca getQueryDataSet().
   */
  public QueryDataSet getFilteredDataSet(String cols, String filter) {
    QueryDataSet filtered;
    if (getQueryDataSet() instanceof raDataSet)
     filtered = new raDataSet();
    else filtered = new QueryDataSet();
    String[] cols2; 
    if (cols.indexOf(',') >= 0) cols2 = new VarStr(cols).splitTrimmed(',');
    else cols2 = new VarStr(cols).split();
    createFilteredDataSet(filtered, cols2, filter);
    return filtered;
  }
  
  /**
   * Vraca NOVI QueryDataSet za ovu tablicu, sa definiranim filterom
   * (SELECT cols FROM <table> WHERE filter), sa zadanim kloniranim kolonama.<p>
   * @params cols kolone koje trebaju biti u setu odvojene zarezom
   * @param cond filter.
   * @return novi QueryDataSet, ili raDataSet, ovisno o tipu
   * kojeg vraca getQueryDataSet().
   */
  public QueryDataSet getFilteredDataSet(String cols, Condition cond) {
    return getFilteredDataSet(cols, cond.toString());
  }

  /**
   * Vraca NOVI QueryDataSet za ovu tablicu, sa definiranim filterom
   * (SELECT cols FROM <table> WHERE filter), sa zadanim kloniranim kolonama.<p>
   * @params cols array kolona koje trebaju biti u setu
   * @param cond filter.
   * @return novi QueryDataSet, ili raDataSet, ovisno o tipu
   * kojeg vraca getQueryDataSet().
   */
  public QueryDataSet getFilteredDataSet(String[] cols, Condition cond) {
    return getFilteredDataSet(cols, cond.toString());
  }
  
  /**
   * Vraca NOVI QueryDataSet za ovu tablicu, sa definiranim filterom
   * (SELECT cols FROM <table> WHERE filter), sa zadanim kloniranim kolonama.<p>
   * @params cols array kolona koje trebaju biti u setu
   * @param filter filter.
   * @return novi QueryDataSet, ili raDataSet, ovisno o tipu
   * kojeg vraca getQueryDataSet().
   */
  public QueryDataSet getFilteredDataSet(String[] cols, String filter) {
    QueryDataSet filtered;
    if (getQueryDataSet() instanceof raDataSet)
     filtered = new raDataSet();
    else filtered = new QueryDataSet();
    createFilteredDataSet(filtered, cols, filter);
    return filtered;
  }

  /**
   * Postavlja filter na default QueryDataSet za ovaj modul
   * (onaj koji se dobije sa getQueryDataSet()). Koristiti samo
   * ako se dobro zna sto se radi. :)<p>
   * @param cond Condition koji predstavlja filter (string koji dolazi iza "WHERE")
   */
  public void setFilter(Condition cond) {
    createFilteredDataSet(getQueryDataSet(), cond);
  }

  /**
   * Postavlja filter na default QueryDataSet za ovaj modul
   * (onaj koji se dobije sa getQueryDataSet()). Koristiti samo
   * ako se dobro zna sto se radi. :)<p>
   * @param where filter (string koji dolazi iza "WHERE")
   */
  public void setFilter(String where) {
    createFilteredDataSet(getQueryDataSet(), where);
  }

  private void reconnectQueryDataSet(QueryDataSet qds) {
    qds.close();
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(),
                 qds.getQuery().getQueryString(), null, true, Load.ALL));
  }

  private void reconnectQueryDataSets() {
//    System.out.print(this.getClass().getName()+":");
    Field[] fields = this.getClass().getDeclaredFields();
//    System.out.print("("+fields.length+")");
    for (int i = 0 ; i < fields.length; i++) {
      try {
        Object o = fields[i].get(this);
        if (o instanceof QueryDataSet)
          reconnectQueryDataSet((QueryDataSet) o);
      }
      catch (Exception e) {
//        System.out.println("Failed to get field " + fields[i].getName());
      }
    }
//    System.out.println();
  }

  /**
   * Postavlja filter na postojeci QueryDataSet (odn. raDataSet),
   * te klonira originalne kolone ukoliko ih doticni DataSet vec
   * prije nije dobio. <p>
   * @param filtered QueryDataSet odn. raDataSet kojem se dodaje filter.
   * @param cond Condition koji predstavlja filter.
   * @return poslani DataSet (ne stvara novog)
   */
  public QueryDataSet setFilter(QueryDataSet filtered, Condition cond) {
    createFilteredDataSet(filtered, cond);
    return filtered;
  }

  /**
   * Postavlja filter na postojeci QueryDataSet (odn. raDataSet),
   * te klonira originalne kolone ukoliko ih doticni DataSet vec
   * prije nije dobio. <p>
   * @param filtered QueryDataSet odn. raDataSet kojem se dodaje filter.
   * @param where receni filter (string iza "WHERE")
   * @return poslani DataSet (ne stvara novog)
   */
  public QueryDataSet setFilter(QueryDataSet filtered, String where) {
    createFilteredDataSet(filtered, where);
    return filtered;
  }

  protected void createFilteredDataSet(QueryDataSet filtered, Condition cond) {
    createFilteredDataSet(filtered, cond.toString());
  }

  protected void createFilteredDataSet(QueryDataSet filtered, String where) {
    createFilteredDataSet(filtered, null, where);    
  }
  
  protected void createFilteredDataSet(QueryDataSet filtered, String[] cols, String where) {
    String qs = "SELECT "+ (cols == null ? "*" : VarStr.join(cols, ',').toString()) + 
       " FROM "+Naziv+(where == null || where.equals("") ? "" : " WHERE " + where);
//    hr.restart.util.
//    if (orig.toLowerCase().indexOf("where"))
    filtered.close();
    RowFilterListener filt = filtered.getRowFilterListener();
    if (filt != null) filtered.removeRowFilterListener(filt);
    if (filtered.getLocale() == null) filtered.setLocale(Aus.hr);
    filtered.setQuery(new QueryDescriptor(getQueryDataSet().getDatabase(), qs, null, true, Load.ALL));
//    filtered.setQuery(new QueryDescriptor(dM.getDataModule().getDatabase1(), getQueryDataSet().getOriginalQueryString()+wh, null, true, Load.ALL));
    if (filtered != getQueryDataSet() && filtered.getResolver() == null) {
      filtered.setResolver(dm.getQresolver());
      if (cols == null) filtered.setColumns(getQueryDataSet().cloneColumns());
      else {
        Column[] scope = new Column[cols.length];
        for (int i = 0; i < cols.length; i++)
          scope[i] = (Column) getQueryDataSet().getColumn(cols[i]).clone();
        filtered.setColumns(scope);
      }
    }
    Refresher.postpone();
  }

  /**
   * Vraca defaultni QueryDataSet za ovaj modul (za neke module,
   * konkretno maticne podatke, ovo je zapravo raDataSet koji omogucuje
   * automatski refresh).
   * @return defaultnu instancu QueryDataSet-a odn. raDataSet-a.
   */
  public abstract QueryDataSet getQueryDataSet();

  public static void installNotifier(raTransferNotifier tn) {
    track = tn;
  }

  public static void removeNotifier() {
    track = null;
  }

  private File resolveFile(File orig) {
    try {
      return orig.getCanonicalFile();
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid directory "+orig.getAbsolutePath());
    }
  }

  private String getFieldNames(File dir, String name) {
    if (!dir.exists() || !dir.isDirectory())
      throw new IllegalArgumentException("Invalid directory "+dir.getAbsolutePath());
    TextFile def = TextFile.read(new File(dir, name + ".def"));
    if (def == null) {
//      throw new FileNotFoundException("Invalid filename "+dir.getAbsolutePath()+
//                                      dir.separator + name);
      return VarStr.join(colnames, ',').insert(0, '(').append(')').toString();
    }
    String fn = def.in();
    String enc = def.in();
    def.close();
    if (enc != null && enc.length() > 0) TextFile.setEncoding(enc);
    return fn.trim();
  }

  public int insertData(File dir) throws FileNotFoundException {
    return insertData(dir, origColumns[0].getTableName().toLowerCase());
  }

  public int insertData(File dir, String name) throws FileNotFoundException {
    if (track != null) track.rowTransfered(Naziv, track.INSERT_STARTED, 0, null);
    dir = resolveFile(dir);
    String enc = TextFile.getEncoding();
    String fieldLine = getFieldNames(dir, name).toUpperCase();
    try {
      HashMap rowData = track == null ? null : new HashMap();
      String sep = Aus.getDumpSeparator();

/*      String typesLine = getFieldTypes(fieldLine);
      fieldLine = getExistingFields(fieldLine);
      System.out.println("existing: "+fieldLine); */  // OLD WAY


      TextFile dat = TextFile.read(new File(dir, name + ".dat"));
      if (dat == null)
        throw new FileNotFoundException("Invalid filename "+dir.getAbsolutePath()+name);

      ArrayList existingFields = new ArrayList();
      String[] existingNames = new VarStr(fieldLine).chop(1, 1).splitTrimmed(',');
      Column[] existingCols = new Column[existingNames.length];
      for (int i = 0; i < existingNames.length; i++)
        if (null != (existingCols[i] = getColumn(existingNames[i])))
          existingFields.add(existingNames[i].toUpperCase());

      ArrayList missingKeyCols = new ArrayList();
      for (int i = 0; i < origColumns.length; i++)
        if (origColumns[i].isRowId() && !existingFields.contains(origColumns[i].getColumnName()))
          missingKeyCols.add(origColumns[i]);
      for (int i = 0; i < missingKeyCols.size(); i++) {
        existingFields.add(i, ((Column) missingKeyCols.get(i)).getColumnName().toUpperCase());
        System.err.println("nema kolone kljuèa: "+existingFields.get(i));
      }

      raPreparedStatement ps = raPreparedStatement.createIndependentInsert(Naziv,
          (String[]) existingFields.toArray(new String[existingFields.size()]));

      int rows = 0;
      try {
        String line;
        Variant v = new Variant();
        while ((line = dat.in()) != null) {
/*          s = "INSERT INTO " + Naziv + " " + fieldLine +
              " VALUES " + getFormattedLine(typesLine, line, sep);
          if (Valid.getValid().runSQL(s)) ++rows;
          ++total;  */  // OLD WAY
          boolean ok = false;
          try {
            ps.clearParameters();
            if (rowData != null) rowData.clear();

            String[] parts = new VarStr(line).split(sep);
            if (parts.length != existingCols.length && parts.length - 1 != existingCols.length)
              throw new UnsupportedOperationException("Incompatible dat<->def: "+line+" <-> "+fieldLine);

            for (int i = 0; i < existingCols.length; i++)
              if (existingCols[i] != null) {
                String val = new VarStr(parts[i]).trim().replaceAll("\\n", "\n").
                         replaceAll("</sep>", sep).toString();
                if (rowData != null) rowData.put(existingNames[i], val);
                if (val.length() > 0 || existingCols[i].getDataType() == Variant.STRING) {
                  if (existingCols[i].getDataType() == Variant.TIMESTAMP)
                    try {
                      v.setFromString(existingCols[i].getDataType(), val);
                      ps.setValue(existingNames[i], v.getAsObject(), false);
                    } catch (IllegalArgumentException ie) {
                      if (val.trim().length()<11) {
                        val = val.trim().concat(" 00:00:00.0");
                      }
                      ps.setTimestamp(existingNames[i], java.sql.Timestamp.valueOf(val), false);
                    }                    
                  if (existingCols[i].getDataType() == Variant.INPUTSTREAM) {
                    byte[] arr = decode(val);
                    ps.setBinaryStream(ps.getParameterIndex(existingNames[i], 
                        false), new ByteArrayInputStream(arr), arr.length);
                  } else {
                    v.setFromString(existingCols[i].getDataType(), val);
                    ps.setValue(existingNames[i], v.getAsObject(), false);
                  } 
                } else ps.setNull(ps.getParameterIndex(existingNames[i], false),
                        existingCols[i].getSqlType());
              }
            for (Iterator i = missingKeyCols.iterator(); i.hasNext(); ) {
              Column c = (Column) i.next();
              switch (c.getDataType()) {
                case Variant.STRING:
                  ps.setString(c.getColumnName(), "!", false);
                  break;
                case Variant.INT:
                case Variant.SHORT:
                case Variant.LONG:
                case Variant.DOUBLE:
                case Variant.FLOAT:
                case Variant.BIGDECIMAL:
                  v.setFromString(c.getDataType(), "-1");
                  ps.setValue(c.getColumnName(), v.getAsObject(), false);
                  break;
                case Variant.TIMESTAMP:
                  ps.setTimestamp(c.getColumnName(), new java.sql.Timestamp(0), false);
                  break;
                default:
                  throw new UnsupportedOperationException("Invalid column type "+
                      Variant.typeName(c.getDataType()));
              }
            }
            ps.executeUpdate();
            Refresher.postpone();
            ok = true;
            ++rows;
          } catch (Exception e) {
            if (track == null) System.err.println(e);
            if (rowData != null) rowData.put(ERROR_KEY, e);
          }
          if (track != null)
            track.rowTransfered(Naziv, ok ? track.ROW_INSERTED
                : track.ROW_INSERT_FAILED, rows, rowData);
        }
      } finally {
        dat.close();
      }
      if (track != null) track.rowTransfered(Naziv, track.INSERT_FINISHED, rows, null);
      TextFile.setEncoding(enc);
      return rows;
    } finally {
      TextFile.setEncoding(enc);
    }
  }

  public void loadData(DataSet dest, File dir) throws FileNotFoundException {
    _loadData(dest, dir, origColumns[0].getTableName().toLowerCase(), false);
  }

  public void loadData(DataSet dest, File dir, String name) throws FileNotFoundException {
    _loadData(dest, dir, name, false);
  }
  
  public void loadData(DataSet dest, File dir, String name, String defcontent) throws FileNotFoundException {
    _loadData(dest, dir, name, false, defcontent);
  }

  public QueryDataSet loadData(File dir) throws FileNotFoundException {
    return loadData(dir, origColumns[0].getTableName().toLowerCase());
  }

  public QueryDataSet loadData(File dir, String name) throws FileNotFoundException {
    QueryDataSet ds = getTempSet("1=0");
    ds.open();
    _loadData(ds, dir, name, true);
    return ds;
  }

  public QueryDataSet loadData(File dir, String name, String defcontent) throws FileNotFoundException {
    QueryDataSet ds = getTempSet("1=0");
    ds.open();
    _loadData(ds, dir, name, true, defcontent);
    return ds;
  }

  private void _loadData(DataSet ds, File dir, String name, boolean full)
      throws FileNotFoundException {
    _loadData(ds, dir, name, full, null);
  }
  private void _loadData(DataSet ds, File dir, String name, boolean full, String defcontent)
    throws FileNotFoundException {

    if (track != null) track.rowTransfered(Naziv, track.LOAD_STARTED, 0, ds);
    dir = resolveFile(dir);
    String enc = TextFile.getEncoding();
    String fields = (defcontent==null)?getFieldNames(dir, name):defcontent;
    try {
      String sep = Aus.getDumpSeparator();

      TextFile dat = TextFile.read(new File(dir, name + ".dat"));
      if (dat == null)
        throw new FileNotFoundException("Invalid filename "+dir.getAbsolutePath()+name);

      String[] existingNames = new VarStr(fields).chop(1, 1).splitTrimmed(',');
      Column[] existingCols = new Column[existingNames.length];
      for (int i = 0; i < existingNames.length; i++)
        existingCols[i] = full ? getColumn(existingNames[i]) : ds.hasColumn(existingNames[i]);

      try {
        String line;
        int rows = 0;
        Variant v = new Variant();
        while ((line = dat.in()) != null) {
          String[] parts = new VarStr(line).split(sep);
          if (parts.length != existingCols.length && parts.length - 1 != existingCols.length)
            throw new UnsupportedOperationException("Incompatible dat<->def: "+line+" <-> "+fields);
          ds.insertRow(false);
          for (int i = 0; i < existingCols.length; i++)
            if (existingCols[i] != null) {
              String val = new VarStr(parts[i]).trim().replaceAll("\\n", "\n").
                       replaceAll("</sep>", sep).toString();
              if (val.length() > 0) {
                v.setFromString(existingCols[i].getDataType(), val);
                ds.setVariant(existingCols[i].getColumnName(), v);
              }
            }
          ds.post();
          ++rows;
          if (track != null) track.rowTransfered(Naziv, track.ROW_LOADED, rows, null);
        }
        if (track != null) track.rowTransfered(Naziv, track.LOAD_FINISHED, rows, null);
      } finally {
        dat.close();
      }
    } finally {
      TextFile.setEncoding(enc);
    }
  }

/*  private String getFormattedLine(String ctypes, String line, String sep) {
    VarStr s = new VarStr(line).trim();
    int offset = 0, term;

    s.replaceAll("'", "''");
    if (!s.right(sep.length()).equals(sep))
      s.append(sep);

    String[] trims = new String[] {"        " + sep, " " + sep, sep + "        ", sep + " "};
    for (int i = 0; i < trims.length; i++)
      while (s.indexOf(trims[i]) >= 0) s.replaceAll(trims[i], sep);

    s.replaceAll("\\n", "\n");

    offset = 0;
    for (int i = 0; i < ctypes.length(); i++) {
      term = s.indexOf(sep);
      if (ctypes.charAt(i) == '1' || (ctypes.charAt(i) == '2' && term != offset)) {
        s.insert(offset, "'");
        s.insert(term + 1, "'");
        term += 2;
      } else if (term == offset) {
        s.insert(term, "NULL");
        term += 4;
      }
      s.replace(term, term + sep.length(), ",");
      if (ctypes.charAt(i) == '-')
        s.replace(offset, term + 1, "");
      else offset = term + 1;
//       System.out.println(s);

    }
//    System.out.println(s);
    s.chop();
    return "(" + s.replaceAll("</sep>", sep) + ")";
  } */
  
  public Int2 findBestKeyForSegments() {
    String tname = origColumns[0].getTableName().toLowerCase();
    
    int keyCount = 0;
    for (int i = 0; i < origColumns.length; i++)
      if (getColumns()[i].isRowId()) ++keyCount;
    
    int bestNum = 0;
    int bestCol = -1;
    for (int i = 0; i < origColumns.length; i++) {
      Column c = origColumns[i];
      if (c.isRowId() && (keyCount == 1 || c.getDataType() != Variant.STRING || 
          c.getPrecision() > 4)) {
        QueryDataSet ds = Util.getNewQueryDataSet("SELECT COUNT(DISTINCT "+c.getColumnName()
            +") FROM " + tname);
        int rows = Valid.getValid().getSetCount(ds, 0);
        if (bestCol < 0 || rows > bestNum) {
          bestNum = rows;
          bestCol = i;
        }
        ds.close();
      }
    }
    if (bestCol < 0) return null;
    return new Int2(bestCol, bestNum);
  }
  
  public Condition[] createSegments(String col, int segs) {
    String tname = origColumns[0].getTableName().toLowerCase();

    QueryDataSet ds = new QueryDataSet();
    ds.setQuery(new QueryDescriptor(dm.getDatabase1(),
        "SELECT "+col+" FROM "+tname+" ORDER BY "+col));
    ds.setMetaDataUpdate(MetaDataUpdate.NONE);
    ds.open();

    int totalRows = ds.rowCount();
    Condition[] conds = new Condition[segs];
    Condition prev = Condition.none;
    for (int i = 1; i < segs; i++) {
      ds.goToRow(totalRows * i / segs);
      conds[i - 1] = prev.and(Condition.where(col, Condition.LESS_OR_EQUAL, ds));
      prev = Condition.where(col, Condition.GREATER_THAN, ds);
    }
    conds[segs - 1] = prev;

    return conds;
  }
  
  public int dumpSegments(File dir, Condition[] segs) {
    String sep = Aus.getDumpSeparator();
    
    String name = origColumns[0].getTableName().toLowerCase();
    dir = resolveFile(dir);
    if (!dir.exists() || !dir.isDirectory())
      throw new IllegalArgumentException("Invalid directory "+dir.getAbsolutePath());    
    TextFile dat = TextFile.write(new File(dir, name + ".dat"));
    TextFile def = TextFile.write(new File(dir, name + ".def"));
    if (dat == null || def == null) {
      if (dat != null) dat.close();
      throw new IllegalArgumentException("Invalid filename "+dir.getAbsolutePath()+name);
    }
    int row = 0;
    int rows = 0;
    try {
      for (int i = 0; i < segs.length; i++) {
        System.out.println("SELECT * FROM "+name+" WHERE "+segs[i]);
        DataSet ds = Util.getNewQueryDataSet("SELECT * FROM "+name+" WHERE "+segs[i]);
        if (i == 0) def.out(getFieldNames(ds));
        rows += ds.getRowCount();
        if (track != null) track.rowTransfered(Naziv, track.DUMP_STARTED, rows, null);
        
        for (ds.first(); ds.inBounds(); ds.next()) {
          dat.out(getFieldsLine(ds, sep));
          if (track != null) track.rowTransfered(Naziv, track.ROW_STORED, ++row, null);
        }
        ds.close();
        
        if (track != null) track.rowTransfered(Naziv, track.DUMP_SEGMENT_FINISHED, row, null);
      }
      def.out(TextFile.getEncoding());
    } finally {
      dat.close();
      def.close();
    }
    if (track != null) track.rowTransfered(Naziv, track.DUMP_FINISHED, row, null);
    return rows;
  }

  public int dumpTable(File dir) {
    return dumpTable(getQueryDataSet(), dir, origColumns[0].getTableName().toLowerCase());
  }

  public int dumpTable(File dir, String name) {
    return dumpTable(getQueryDataSet(), dir, name);
  }

  public int dumpTable(DataSet ds, File dir) {
    String name = ds.getTableName().toLowerCase();
    return dumpTable(ds, dir, name != null ? name : origColumns[0].getTableName().toLowerCase());
  }

  public int dumpTable(DataSet ds, File dir, String name) {
    if (!ds.isOpen()) ds.open();
    int rows = ds.getRowCount();
    if (track != null) track.rowTransfered(Naziv, track.DUMP_STARTED, rows, ds);
    if (rows == 0) return 0;
    dir = resolveFile(dir);
    if (!dir.exists() || !dir.isDirectory())
      throw new IllegalArgumentException("Invalid directory "+dir.getAbsolutePath());
    TextFile dat = TextFile.write(new File(dir, name + ".dat"));
    TextFile def = TextFile.write(new File(dir, name + ".def"));
    if (dat == null || def == null) {
      if (dat != null) dat.close();
      throw new IllegalArgumentException("Invalid filename "+dir.getAbsolutePath()+name);
    }
    int row = 0;
    try {
      String sep = Aus.getDumpSeparator();

      for (ds.first(); ds.inBounds(); ds.next()) {
        dat.out(getFieldsLine(ds, sep));
        if (track != null) track.rowTransfered(Naziv, track.ROW_STORED, ++row, null);
      }

      def.out(getFieldNames(ds));
      def.out(TextFile.getEncoding());
    } finally {
      dat.close();
      def.close();
    }
    if (track != null) track.rowTransfered(Naziv, track.DUMP_FINISHED, row, null);
    return rows;
  }

/*  private String getExistingFields(String fields) {
    VarStr line = new VarStr(fields.length()).append("(");
    StringTokenizer st = new StringTokenizer(fields.substring(1, fields.length() - 1), ",");

    while (st.hasMoreTokens()) {
      Column orig = getColumn(st.nextToken().trim());
      if (orig != null)
        line.append(orig.getColumnName().toLowerCase()).append(",");
    }
    line.chop().append(")");
    return line.toString();
  }*/

/*  private String getFieldTypes(String fields) {
    VarStr line = new VarStr();
    StringTokenizer st = new StringTokenizer(fields.substring(1, fields.length() - 1), ",");

    while (st.hasMoreTokens()) {
      Column orig = getColumn(st.nextToken().trim());
//      String fieldName = fieldList.substring(offset, comma);
      if (orig != null) {
        int ctype = orig.getDataType();
        if (ctype == Variant.STRING)
          line.append("1");
        else if (ctype == Variant.TIMESTAMP || ctype == Variant.DATE || ctype == Variant.TIME)
          line.append("2");
        else
          line.append("0");
      } else line.append("-");
    }

    return line.toString();
  } */

  public static String getFieldsLine(DataSet table, String sep) {
    VarStr line = new VarStr();
    VarStr part = new VarStr();
    Variant v = new Variant();

    for (int i = 0; i < table.columnCount(); i++) {
      if (table.getColumn(i).getSqlType() != java.sql.Types.NULL) {
        table.getVariant(table.getColumn(i).getColumnName(), v);
        if (v.getType() == Variant.INPUTSTREAM)
         part.clear().append(encode(v.getInputStream())); 
        else {
          part.clear().append(v);
          part.replaceAll(sep, "</sep>");
        }
        line.append(part).append(sep);
      }
    }
    line.replaceAll("\n", "\\n");
    return line.toString();
  }
  
  public static String encode(InputStream is) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      new Base64Encoder(is, out).process();
    } catch (IOException e) {
      //
    }
    return out.toString();
  }
  
  public static byte[] decode(String val) {
    ByteArrayInputStream is = new ByteArrayInputStream(val.getBytes());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      new Base64Decoder(is, out).process();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Base64FormatException e) {
      e.printStackTrace();
    }
    return out.toByteArray();
  }

  private String getFieldNames(DataSet table) {
    VarStr line = new VarStr("(");

    for (int i = 0; i < table.columnCount(); i++)
      if (table.getColumn(i).getSqlType() != java.sql.Types.NULL)
        line.append(table.getColumn(i).getColumnName().toLowerCase()).append(",");
    line.chop().append(")");
    return line.toString();
  }

  public void setall(){
    // nothing
  }

//  public void Kreiranje(BazaOper baza) {


//    setall();
//    CommonTable.CommonCreateTabela(baza.DajKonekciju(),SqlDefTabela,Naziv);
//    CommonTable.CommonCreateIdx(baza.DajKonekciju(),DefIndex,NaziviIdx);
//
//  }

//  public void Dropanje(BazaOper baza) {

//    setall();
//    CommonTable.CommonIndexDrop(baza,NaziviIdx);
//    TabeleKreiranje.DropajTablu(baza.DajKonekciju(),Naziv);
//
//  }
  public boolean KreirTable() {
    try {
      System.err.println(SqlDefTabela);
      dm.database1.executeStatement(SqlDefTabela);
      System.err.println(Naziv+" ISKREIRAN !");
      return true;
    } catch (Exception e) {
      System.err.println("SQLException: "+ e.getMessage());
      return false;
    }
//    return CommonTable.CommonCreateTabela(dM.getDatabaseConnection(),SqlDefTabela,Naziv);
  }
//  public boolean KreirTable(BazaOper baza) {
//    return CommonTable.CommonCreateTabela(baza.DajKonekciju(),SqlDefTabela,Naziv);
//  }
  public boolean KreirIdx() {
    boolean succ = true;
    for (int i=0; i< DefIndex.length;i++)
      try {
        System.err.println(DefIndex[i]);
        dm.database1.executeStatement(DefIndex[i]);
        System.err.println(NaziviIdx[i]+" index iskreiran");
      } catch (Exception e )   {
        System.err.println("SQLException: "+ e.getMessage());
        succ = false;
      }
    return succ;
//    return CommonTable.CommonCreateIdx(dM.getDatabaseConnection(),DefIndex,NaziviIdx);
  }
//  public boolean KreirIdx(BazaOper baza) {
//    return CommonTable.CommonCreateIdx(baza.DajKonekciju(),DefIndex,NaziviIdx);
//  }
  public boolean DropTable() {
    try {
      dm.database1.executeStatement("drop table ".concat(Naziv));
      System.err.println(Naziv+" tabela dropana");
      return true;
    } catch (Exception e) {
      System.err.println("SQLException: "+ e.getMessage());
      return false;
    }
//    return TabeleKreiranje.DropajTablu(dM.getDatabaseConnection(),Naziv);
  }
//  public boolean DropTable(BazaOper baza) {
//    return TabeleKreiranje.DropajTablu(baza.DajKonekciju(),Naziv);
//  }
  public boolean DropIdx() {
    boolean succ = true;
    for (int i=0; i< DefIndex.length;i++)
      try {
        dm.database1.executeStatement("drop index ".concat(NaziviIdx[i]));
        System.err.println(NaziviIdx[i]+" index dropan");
      } catch (Exception e )   {
        System.err.println("SQLException: "+ e.getMessage());
        succ = false;
      }
    return succ;
//    return CommonTable.CommonIndexDrop(dM.getDatabaseConnection(),NaziviIdx);
  }
//  public boolean DropIdx(BazaOper baza) {
//    return CommonTable.CommonIndexDrop(baza,NaziviIdx);
//  }
  
  public void define() {
    VarStr def = new VarStr();
    String cname = getClass().getName();
    cname = cname.substring(cname.lastIndexOf('.') + 1);
    def.append('$').append(cname).append('\n');
    for (int i = 0; i < origColumns.length; i++) {
      Column c = origColumns[i];
      def.append(c.getColumnName().toUpperCase()).append(';');
      if (c.getDataType() == Variant.STRING)
        def.append("char;").append(c.getPrecision()).append(";");
      else if (c.getDataType() == Variant.INT)
        def.append("int;6;");
      else if (c.getDataType() == Variant.BIGDECIMAL)
        def.append("numeric;").append(c.getPrecision()).
            append(',').append(c.getScale()).append(';');
      else if (c.getDataType() == Variant.TIMESTAMP)
        def.append("date;;");
      else if (c.getDataType() == Variant.SHORT)
        def.append("short;2;");
      if (c.isRowId()) def.append("pkey");
      def.append(';');
      if (c.getDefault() != null) def.append(c.getDefault());
      def.append(';').append(c.getCaption());
      if (c.getVisible() == TriStateProperty.FALSE) def.append(";*");
      else if (c.getWidth() != c.getPrecision()) def.append(';').append(c.getWidth());
      def.append('\n');
    }
    for (int i = 0; i < NaziviIdx.length; i++) {
      String col = null;
      for (int c = 0; c < origColumns.length; c++)
        if (NaziviIdx[i].toLowerCase().indexOf(("idx" + cname + 
            origColumns[c].getColumnName()).toLowerCase()) >= 0)
          col = origColumns[c].getColumnName().toLowerCase();
      if (col != null)
        def.append(NaziviIdx[i].startsWith("u") ? "UIDX=" : "IDX=").append(col).append('\n');
    }
    JTextArea clip = new JTextArea();
    clip.setText(def.toString());
    clip.selectAll();
    clip.cut();
  }

  public String toString() {
    return Naziv;
  }

  protected void setColumns(Column[] cols) {
    getQueryDataSet().setColumns(cols);
    origColumns = getQueryDataSet().cloneColumns();
    colnames = new String[cols.length];
    for (int i = 0; i < cols.length; i++)
      colnames[i] = cols[i].getColumnName().toUpperCase();
  }
  
  private int defOffset;

  private void terror(String text) {
    throw new RuntimeException("Greska u liniji "+defOffset+" ("+Naziv+"): "+text);
  }
  
  protected boolean getDefinition() {
    String cname = getClass().getName();
    cname = cname.substring(cname.lastIndexOf('.') + 1);
    defOffset = dm.getTableOffset(cname);
    if (defOffset < 0) return false;

    Set cnames = new HashSet();
    List cols = new ArrayList();
    List idx = new ArrayList();
    List uidx = new ArrayList();
    List pkeylist = new ArrayList();
    VarStr buf = new VarStr();
    String line;
    
    ddl.create(Naziv = cname);
    
    while ((line = dm.getDefLine(defOffset++)) != null)
      if (line.length() > 0 && !line.startsWith("#")) {
        buf.clear().append(line);
        if (buf.countOccurences(';') > 0) {
          String[] parts = buf.split(';');
          Column c = parseColumnDef(parts);          
          cols.add(c);
          addDDLCommand(c);
          if (!cnames.add(c.getColumnName().toLowerCase()))
            terror("dvostruka kolona "+c.getColumnName());
          
          String pidx = parts[3].trim().toLowerCase();
          if (pidx.equals("yes") || pidx.equals("idx"))
            idx.add(c.getColumnName().toLowerCase());
          else if (pidx.equals("uidx"))
            uidx.add(c.getColumnName().toLowerCase());
          else if (!pidx.equals("pkey") && !pidx.equals("key") && pidx.length() > 0)
            terror("pogresno definiran index za kolonu "+c.getColumnName());
          
          if (c.isRowId()) pkeylist.add(c.getColumnName().toLowerCase());
        } else {
          line = line.toLowerCase();
          if (!line.startsWith("idx") && !line.startsWith("uidx"))
            terror("nije ni index ni definicija kolone");
          
          if (buf.countOccurences('=') != 1)
            terror("pogresan format za index (treba biti (U)IDX = kolona)");
          
          cname = buf.removeWhitespace().split('=')[1].toLowerCase();
          if (cname.indexOf(',') > 0) {
            String[] cns = new VarStr(cname).splitTrimmed(',');
            for (int i = 0; i < cns.length; i++)
              if (!cnames.contains(cns[i]))
                terror("nepostojeca kolona za index");
          } else if (!cnames.contains(cname))
            terror("nepostojeca kolona za index");
          
          if (line.startsWith("idx")) idx.add(cname);
          else uidx.add(cname);
        }
      }
    
    if (pkeylist.size() == 0)
      if (!line.startsWith("idx") && !line.startsWith("uidx"))
        terror("nijedna kolona nije primarni kljuc");
    
    ddl.addPrimaryKey(VarStr.join(pkeylist, ',').toString());
    SqlDefTabela = ddl.getCreateTableString();
    
    String[] aidx = (String[]) idx.toArray(new String[idx.size()]);
    String[] auidx = (String[]) uidx.toArray(new String[uidx.size()]);
    DefIndex = ddl.getIndices(aidx, auidx);
    NaziviIdx = ddl.getIndexNames(aidx, auidx);
 
    getQueryDataSet().setResolver(dm.getQresolver());
    getQueryDataSet().setQuery(new QueryDescriptor(dm.getDatabase1(), 
        "SELECT * FROM " + Naziv, null, true, Load.ALL));
    setColumns((Column[]) cols.toArray(new Column[cols.size()]));
    return true;
  }
  
  private void addDDLCommand(Column c) {
    String cname = c.getColumnName().toLowerCase();
    String def = c.getDefault() == null ? "" : c.getDefault();
    switch (c.getDataType()) {
      case Variant.STRING:
        ddl.addChar(cname, c.getPrecision(), def, c.isRowId());
        break;
      case Variant.BIGDECIMAL:
        ddl.addFloat(cname, c.getPrecision(), c.getScale(), c.isRowId());
        break;
      case Variant.INT:
        ddl.addInteger(cname, c.getPrecision(), c.isRowId());
        break;
      case Variant.SHORT:
        ddl.addShort(cname, c.getPrecision(), c.isRowId());
        break;
      case Variant.TIMESTAMP:
        ddl.addDate(cname, c.isRowId());
        break;
      case Variant.INPUTSTREAM:
        ddl.addBlob(cname, c.isRowId());
        break;
    }
  }
  
  protected void modifyColumn(Column c) {
    
  }
  
  private Column parseColumnDef(String[] parts) {
    if (parts.length < 6)
      terror("premalo tokena za definiciju kolone");
    
    String stype = parts[1].toLowerCase();
    int dtype = 0;
    if (stype.equals("char") || stype.equals("text") || stype.equals("alpha"))
      dtype = Variant.STRING;
    else if (stype.equals("numeric") || stype.equals("decimal"))
      dtype = Variant.BIGDECIMAL;
    else if (stype.equals("int") || stype.equals("integer"))
      dtype = Variant.INT;
    else if (stype.equals("short"))
      dtype = Variant.SHORT;
    else if (stype.equals("date") || stype.equals("timestamp"))
      dtype = Variant.TIMESTAMP;
    else if (stype.equals("blob"))
      dtype = Variant.INPUTSTREAM;
    else
      terror("pogresan tip kolone, "+stype);
    
    int scale = -1;
    if (dtype == Variant.BIGDECIMAL)
      if (parts[2].indexOf(',') > 0) {
        String sscale = new VarStr(parts[2]).removeWhitespace().split(',')[1];
        if (!Aus.isDigit(sscale))
          terror("pogresna preciznost bigdecimal kolone "+parts[0]);
        
        scale = Aus.getNumber(sscale);
        parts[2] = parts[2].substring(0, parts[2].indexOf(','));
      }
    
    if (!Aus.isDigit(parts[2]))
      terror("pogresna preciznost kolone "+parts[0]);
    
    int prec = Aus.getNumber(parts[2]);
    
    Column c = dM.createColumn(parts[0].toUpperCase(), parts[5], parts[4].trim(), 
        dtype, Dialect.getSqlType(dtype), prec, scale);
    
    modifyColumn(c);

    if (parts[3].equalsIgnoreCase("pkey") || parts[3].equalsIgnoreCase("key"))
      c.setRowId(true);
    
    c.setTableName(Naziv);
    
    if (parts.length > 6) {
      String ext = parts[6].trim();
      if (ext.equals("*"))
        c.setVisible(TriStateProperty.FALSE);
      else if (Aus.isDigit(ext))
        c.setWidth(Aus.getNumber(ext));
    }
    return c;
  }

/*  public boolean TestTabele(BazaOper baza) {

    java.sql.Statement Stmt ;

      try {

        Stmt=baza.DajKonekciju().createStatement() ;
        Stmt.execute("select 1 from "+ Naziv);
        Stmt.close();
        return true;

      } catch(java.sql.SQLException ex )   {
//           System.err.println("SQLException: "+ ex.getMessage());
        return false;
       }

  }*/

//  public String getNaziv
}
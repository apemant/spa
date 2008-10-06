/****license*****************************************************************
**   file: ScriptedMatPodaci.java
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
package hr.restart.sisfun;

import hr.restart.baza.KreirDrop;
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raLLFrames;
import hr.restart.util.raLoader;
import hr.restart.util.raMatPodaci;
import hr.restart.util.sysoutTEST;

import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.StringTokenizer;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class ScriptedMatPodaci extends AutomatedMatPodaci {
  private ScriptCommand root;
  private Calendar cal = Calendar.getInstance();

  private static String[] getColumns(KreirDrop kdp, String fieldLine) {
    StringTokenizer st = new StringTokenizer(fieldLine.trim().substring(1,
        fieldLine.trim().length() - 1), ",");
    String[] colnames = new String[st.countTokens()];
    for (int i = 0; st.hasMoreTokens(); i++) {
      String col = st.nextToken();
      if (kdp.getQueryDataSet().hasColumn(col) == null)
        throw new ScriptException("Invalid column '"+col+"' in module '"+kdp.Naziv+"'");
      colnames[i] = col;
    }
    return colnames;
  }

  private static void loadLine(String vals[], String[] cols, DataSet result) {
    if (vals.length < cols.length)
      throw new ScriptException("Incompatible number of columns in def and dat files, "
        + vals.length + " < " + cols.length);
    result.insertRow(false);
    for (int i = 0; i < cols.length; i++) {
      switch (result.getColumn(cols[i]).getDataType()) {
        case Variant.STRING:
          result.setString(cols[i], vals[i]);
          break;
        case Variant.INT:
          result.setInt(cols[i], Integer.parseInt(vals[i]));
          break;
        case Variant.BIGDECIMAL:
          result.setBigDecimal(cols[i], new BigDecimal(vals[i]));
          break;
        case Variant.TIMESTAMP:
          result.setTimestamp(cols[i], Timestamp.valueOf(vals[i]));
          break;
        case Variant.LONG:
          result.setLong(cols[i], Long.parseLong(vals[i]));
          break;
        case Variant.SHORT:
          result.setShort(cols[i], Short.parseShort(vals[i]));
          break;
        case Variant.DOUBLE:
          result.setDouble(cols[i], Double.parseDouble(vals[i]));
          break;
        case Variant.FLOAT:
          result.setFloat(cols[i], Float.parseFloat(vals[i]));
          break;
      }
    }
    result.post();
  }

  public static QueryDataSet loadFile(String name) {
    return loadFile(name, "");
  }

  public static QueryDataSet loadFile(String name, String path) {
    dM.getDataModule().loadModules();
    KreirDrop kdp = KreirDrop.getModuleByName(name);
    if (kdp == null)
      throw new ScriptException("Can't find module '"+name+"'");

    String tableName = kdp.Naziv.toLowerCase();
    System.err.println("Loading data for module '"+tableName+"'");

    String sep = hr.restart.sisfun.frmParam.getParam("sisfun", "dumpSeparator");
    if (sep == null || sep.trim().equals("") || sep.trim().equals(",")) sep = "#";

    TextFile tf;
    if (null == (tf = TextFile.read(path + tableName + ".def")))
      throw new ScriptException("Can't find def file for module '"+name+"'");

    String def = tf.in();
    tf.close();
    if (def == null || def.length() == 0)
      throw new ScriptException("Invalid def file for module '"+name+"'");

    String[] cols = getColumns(kdp, def);
    QueryDataSet result = kdp.getTempSet("1=0");
    result.open();

    if (null == (tf = TextFile.read(path + tableName + ".dat")))
      throw new ScriptException("Can't find dat file for module '"+name+"'");

    String line;
    while ((line = tf.in()) != null)
      loadLine(new VarStr(line).split(sep.charAt(0)), cols, result);
    tf.close();

    return result;
  }

  public ScriptedMatPodaci(ScriptCommand com) {
    root = com;
  }

  public static void setDelaysFor(AutomatedTask at, ScriptCommand s) {
    String act = s.getOuterArgumentFor("action");
    String mou = s.getOuterArgumentFor("mouse");
    String key = s.getOuterArgumentFor("typing");
    if (act != null && Aus.isDigit(act))
      at.setActionDelay(Aus.getNumber(act));
    if (mou != null && Aus.isDigit(mou))
      at.setMouseSpeed(Aus.getNumber(mou));
    if (key != null && Aus.isDigit(key))
      at.setTypingSpeed(Aus.getNumber(key));
  }

  public void execute() {
    try {
      AutomatedMenu am = new AutomatedMenu(raLLFrames.getRaLLFrames().getMsgStartFrame());
      setDelaysFor(am, root);
      am.clickMenuItem(root.getArgumentFor("menu"));

      String className = root.getArgumentFor("class");
      if (!raLoader.isLoaderLoaded(className))
        throw new ScriptException("Class not found: '"+className+"'", root.getChild("class"));
      setMatPodaci((raMatPodaci) raLoader.load(className));
      setDelaysFor(this, root);

      String file = root.getArgumentFor("file");
      if (file != null) inputFromFile(file);
      else inputFromScript();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected String[] getInputFields() {
    return new VarStr(root.getArgumentFor("input")).splitTrimmed(',');
  }

  protected int[] getLookupFields(String[] input) {
    int[] lookup = new int[input.length];
    Arrays.fill(lookup, -1);

    String arg = root.getArgumentFor("lookup");
    if (arg != null) {
      String[] lu = new VarStr(arg.toLowerCase()).split(',');
      for (int i = 0; i < lu.length; i++) {
        boolean found = false;
        for (int j = 0; j < input.length; j++)
          if (found = lu[i].startsWith(input[j].toLowerCase()))
            lookup[j] = lu[i].trim().length() == input[j].length() ? 0 :
                     Integer.parseInt(lu[i].substring(input[j].length()).trim());
        if (!found)
          throw new ScriptException("Invalid lookup field (not in input list)",
                                    root.getChild("lookup"));
      }
    }
    return lookup;
  }

  protected void inputAllFields(DataSet row, String[] input, int[] lookup)
      throws InterruptedException {
    String lr = root.getOuterArgumentFor("lookup-rarity");
    int rarity = lr == null ? 1 : Aus.getNumber(lr);
    for (int i = 0; i < input.length; i++) {
      if (lookup[i] < 0) setAnything(input[i], getStringValue(row, input[i]));
      else if (lookup[i] == 0) lookUp(input[i], row.getString(input[i]));
      else if (rand.nextInt(rarity) == 0) {
        String s = row.getString(input[i]);
        lookUp(input[i], s, s.substring(0, Math.min(s.length(), lookup[i])));
      } else setNavField(input[i], getStringValue(row, input[i]));
    }
  }

  protected void cancelAndCheck(String[] key, DataSet row) throws InterruptedException {
    String[] kv = dM.getDataValues(mp.getRaQueryDataSet(), key);
    cancel();
    lookupData.getlookupData().raLocate(mp.getRaQueryDataSet(), key, kv);
    compareDataSets(mp.getRaQueryDataSet(), row);
  }

  private void inputFromFile(String file) throws InterruptedException {
    DataSet data = loadFile(file, root.getScriptPath());
    String[] input = getInputFields();
    int[] lookup = getLookupFields(input);

    String chk = root.getOuterArgumentFor("check");
    boolean check = chk != null && chk.equalsIgnoreCase("on") && root.hasChild("key");
    String[] key = !root.hasChild("key") ? null :
                   new VarStr(root.getArgumentFor("key")).splitTrimmed(',');

    start();
    for (data.first(); data.inBounds(); data.next()) {
      inputAllFields(data, input, lookup);
      if (accept() && rand.nextInt(7) == 0) {
        if (!check) cancel();
        else cancelAndCheck(key, data);
        start();
      }
    }
    if (!check) cancel();
    else cancelAndCheck(key, data);
    type(KeyEvent.VK_ESCAPE);
    delay(getActionDelay() * 3);
  }

  private void inputFromScript() {

  }

  protected String getStringValue(DataSet ds, String col) {
    switch (ds.getColumn(col).getDataType()) {
      case Variant.STRING:
        return ds.getString(col);
      case Variant.INT:
        return String.valueOf(ds.getInt(col));
      case Variant.BIGDECIMAL:
        return ds.getBigDecimal(col).toString();
      case Variant.TIMESTAMP:
        return getFormattedDate(ds.getTimestamp(col));
      case Variant.SHORT:
        return String.valueOf(ds.getShort(col));
      case Variant.LONG:
        return String.valueOf(ds.getLong(col));
      case Variant.FLOAT:
        return String.valueOf(ds.getFloat(col));
      case Variant.DOUBLE:
        return String.valueOf(ds.getDouble(col));
    }
    return "";
  }

  private String getFormattedDate(Timestamp t) {
    cal.setTime(t);
    return cal.get(cal.DATE) + "." + cal.get(cal.MONTH) + "." + cal.get(cal.YEAR);
  }

  private static void main(String[] args) {
    sysoutTEST sys = new sysoutTEST(false);
    sys.prn(loadFile("tablice"));
  }
}


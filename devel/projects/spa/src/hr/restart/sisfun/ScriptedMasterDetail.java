/****license*****************************************************************
**   file: ScriptedMasterDetail.java
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

import hr.restart.util.VarStr;
import hr.restart.util.raLLFrames;
import hr.restart.util.raLoader;
import hr.restart.util.raMasterDetail;

import java.awt.event.KeyEvent;
import java.util.Calendar;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Locate;
import com.borland.dx.dataset.Variant;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class ScriptedMasterDetail extends AutomatedMasterDetail {
  private ScriptedMatPodaci sm, sd;
  private ScriptCommand root, pres;
  private Calendar cal = Calendar.getInstance();

  public ScriptedMasterDetail(ScriptCommand com) {
    root = com;
  }

  protected void setMasterDetail(raMasterDetail md) {
    this.md = md;
    sm = new ScriptedMatPodaci(root.getChild("master"));
    sd = new ScriptedMatPodaci(root.getChild("detail"));
    pres = root.getChild("preselect");
    createPreSelect();
  }

  public void execute() {
    try {
      AutomatedMenu am = new AutomatedMenu(raLLFrames.getRaLLFrames().getMsgStartFrame());
      ScriptedMatPodaci.setDelaysFor(am, root);
      am.clickMenuItem(root.getArgumentFor("menu"));

      String className = root.getArgumentFor("class");
      if (!raLoader.isLoaderLoaded(className))
        throw new ScriptException("Class not found: '"+className+"'");
      setMasterDetail((raMasterDetail) raLoader.load(className));

      String files = root.getArgumentFor("files");
      if (files != null) inputFromFiles(files);
      else inputFromScript();

    } catch (Exception e) {
    }
  }

  private void inputFromFiles(String files) throws InterruptedException {
    String[] file = new VarStr(files).splitTrimmed(',');
    if (file.length != 2)
      throw new ScriptException("Invalid number of files (must be 2) ", root.getChild("files"));
    DataSet master = sm.loadFile(file[0], root.getScriptPath());
    DataSet detail = sd.loadFile(file[1], root.getScriptPath());

    String[] key = new VarStr(root.getArgumentFor("key")).splitTrimmed(',');
    if (key == null || key.length < 1)
      throw new ScriptException("Invalid number of columns in key ", root.getChild("key"));
    boolean presel = true;
    for (master.first(); master.inBounds(); master.next()) {
      if (presel || !validPreselect(master)) {
        if (!presel) type(KeyEvent.VK_F12);
        enterPreselectValues(master);
        presel = false;
      }
      enterMasterValues(master);
      if (this.isDetailShowing()) {
        enterDetailValues(master, detail, key);
      } else {
        System.err.println("detail not shown for row");
        System.err.println(master);
        if (!sm.isOrigWindow()) {
          System.err.println("not original window");
          type(KeyEvent.VK_ESCAPE);
          if (!sm.isOrigWindow())
            throw new ScriptException("Unexpected error", root);
        }
      }
    }
  }

  private void inputFromScript() {

  }

  private void enterPreselectValues(DataSet row) throws InterruptedException {
    DataSet psd = md.getPreSelect().getSelRow();
    String[] pcols = new VarStr(pres.getArgumentFor("input")).splitTrimmed(',');
    for (int i = 0; i < pcols.length; i++) {
      if (psd.hasColumn(pcols[i]) != null)
        ps.setAnything(pcols[i], sm.getStringValue(row, pcols[i]));
      else {
        ps.setAnything(pcols[i].concat("-from"), sm.getStringValue(row, pcols[i]));
        ps.setAnything(pcols[i].concat("-to"), sm.getStringValue(row, pcols[i]));
      }
    }
    ps.accept();
  }

  private void enterMasterValues(DataSet row) throws InterruptedException {
    String[] input = sm.getInputFields();
    int[] lookup = sm.getLookupFields(input);
    sm.start();
    sm.inputAllFields(row, input, lookup);
    sm.accept();
    delay(getActionDelay() * 3);
  }

  private void enterDetailValues(DataSet master, DataSet detail, String[] key)
      throws InterruptedException {
    String[] input = sd.getInputFields();
    int[] lookup = sd.getLookupFields(input);
    DataRow loc = new DataRow(detail, key);
    DataSet.copyTo(key, master, key, loc);

    String chk = root.getOuterArgumentFor("check");
    boolean check = chk != null && chk.equalsIgnoreCase("on");

    int locOpt = Locate.FIRST;
    int stav = 0;
    while (detail.locate(loc, locOpt)) {
      if (stav == 0) sd.start();
      sd.inputAllFields(detail, input, lookup);
      if (sd.accept() && rand.nextInt(7) == 0) {
        sd.cancel();
        if (check) sd.compareDataSets(md.getDetailSet(), detail);
        sd.start();
      }
      locOpt = Locate.NEXT_FAST;
      ++stav;
    }
    sd.cancel();
    if (check) sd.compareDataSets(md.getDetailSet(), detail);
    type(KeyEvent.VK_ESCAPE);
    delay(getActionDelay() * 3);
  }

  private boolean validPreselect(DataSet row) {
    Variant v = new Variant(), v1 = new Variant(), v2 = new Variant();
    DataSet psd = md.getPreSelect().getSelRow();
    String[] pcols = new VarStr(pres.getArgumentFor("input")).splitTrimmed(',');
    for (int i = 0; i < pcols.length; i++) {
      if (row.hasColumn(pcols[i]) != null) row.getVariant(pcols[i], v);
      else throw new ScriptException("Invalid column "+pcols[i], pres);
      if (psd.hasColumn(pcols[i]) != null) {
        psd.getVariant(pcols[i], v1);
        if (v.getType() != v1.getType())
          throw new ScriptException("Incompatible column "+pcols[i], pres);
        if (!v.equals(v1)) return false;
      } else if (psd.hasColumn(pcols[i].concat("-from")) != null &&
                 psd.hasColumn(pcols[i].concat("-to")) != null) {
        psd.getVariant(pcols[i].concat("-from"), v1);
        psd.getVariant(pcols[i].concat("-to"), v2);
        if (v1.getType() != v2.getType() || v1.getType() != v.getType())
          throw new ScriptException("Incompatible column "+pcols[i], pres);
        if (v.compareTo(v1) < 0 || v.compareTo(v2) > 0) return false;
      } else new ScriptException("Invalid column "+pcols[i], pres);
    }
    return true;
  }
}


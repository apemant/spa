/****license*****************************************************************
**   file: dlgCVSChooser.java
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
 * dlgCVSChooser.java
 *
 * Created on 2004. sijeèanj 19, 12:40
 */

package hr.restart.util.versions;

import hr.restart.baza.dM;
import hr.restart.util.raTwoTableFrame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.StorageDataSet;
/**
 *
 * @author  andrej
 */
public class dlgCVSChooser extends raTwoTableFrame {
  private StorageDataSet leftDS = new StorageDataSet();
  private StorageDataSet rightDS = new StorageDataSet();
  private raPatchMaker patchmaker;
  private JPanel jp = new javax.swing.JPanel(new BorderLayout());
  private JButton jbCVS = new JButton("CVS Log");
  private raCVSHandler cvshandler = new raCVSHandler();
  /** Creates a new instance of dlgCVSChooser */
  public dlgCVSChooser(raPatchMaker _patchmaker, java.awt.Container owner) {
    super(owner);
    setTitle("Files for patch");
    patchmaker = _patchmaker;
    jbCVS.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String log = cvshandler.getFullLog(leftDS.getString("FILE"));
        System.out.println("log: "+log);
        JOptionPane.showMessageDialog(null, log);
      }
    });
    jp.add(jbCVS,BorderLayout.CENTER);
    setJPan(jp);
    createDataSets();
  }
  private void createDataSets() {
    leftDS.addColumn(dM.createStringColumn("FILE",500));
    leftDS.addColumn(dM.createTimestampColumn("DATE"));
    leftDS.open();
    rightDS.addColumn(dM.createStringColumn("FILE",500));
    rightDS.addColumn(dM.createTimestampColumn("DATE"));
    rightDS.open();
    for (Iterator i = patchmaker.getCVSEntries().keySet().iterator(); i.hasNext(); ) {
      File kfile = (File)i.next();
      Timestamp date = (Timestamp)patchmaker.getCVSEntries().get(kfile);
      if (patchmaker.patchfilter.accept(date) && (patchmaker.matchesExtension(kfile.getName()) || kfile.getName().endsWith(".java"))) {
        leftDS.insertRow(false);
        leftDS.setString("FILE", kfile.getAbsolutePath());
        leftDS.setTimestamp("DATE", date);
        leftDS.post();
      }
    }
    leftDS.setTableName("CVS-files");
    rightDS.setTableName("CVS-files");
    getTTC().setLeftDataSet(leftDS);
    getTTC().setRightDataSet(rightDS);
  }

  public void componentShow() {
   // getTTC().initialize();
  }

  public void firstESC() {

  }

  public boolean isIspis() {
    return false;
  }

  public void okPress() {
//    patchmaker.removeDateRange();
    patchmaker.fillclasses(getPickedFiles());
  }

  public void afterOKPress() {
    hide();
  }

  public boolean runFirstESC() {
    return false;
  }
  public TreeMap getPickedFiles() {
    TreeMap map = new TreeMap();
    for (rightDS.first(); rightDS.inBounds(); rightDS.next()) {
      map.put(new File(rightDS.getString("FILE")), rightDS.getTimestamp("DATE"));
    }
    return map;
  }
}

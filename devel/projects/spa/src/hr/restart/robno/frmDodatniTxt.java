/****license*****************************************************************
**   file: frmDodatniTxt.java
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

import hr.restart.baza.VTText;
import hr.restart.baza.dM;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.ActionExecutor;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.KeyAction;
import hr.restart.util.OKpanel;
import hr.restart.util.lookupData;
import hr.restart.util.lookupFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYLayout;

abstract public class frmDodatniTxt {
  
  hr.restart.util.lookupData LD = lookupData.getlookupData();
  dM dm = dM.getDataModule();

  private JPanel jp = new JPanel();
  private boolean postojiprijeQDS=false;
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private JLabel labelich = new JLabel("Tekst stavke");
  private hr.restart.swing.JraTextArea dodtxt = new hr.restart.swing.JraTextArea();
  JraScrollPane scroller = new JraScrollPane(dodtxt);
  JScrollBar hs = new JScrollBar(JScrollBar.HORIZONTAL) {
    public void setEnabled(boolean dummy) {
      super.setEnabled(true);
    }
  };
  JScrollBar vs = new JScrollBar(JScrollBar.VERTICAL) {
    public void setEnabled(boolean dummy) {
      super.setEnabled(true);
    }
  };
  ActionExecutor execF9 = new ActionExecutor() {
    public void run() {
      if (isDodatnaNapomena) presDodatnaNapomena();
      else if (cart != null) checkArtGroup();
      else if (cgrart != null) checkGroup(cgrart);
    }
  };
  private JraDialog miniFrame ;//= new JraDialog((java.awt.Frame)this.getJframe(),"Ažuriranje raèuna",true);
  private OKpanel miniOK = new OKpanel(){
    public void jPrekid_actionPerformed(){
      Cancelpress();
    }
    public void jBOK_actionPerformed(){
      OKpress();
    }
  };
  private void Cancelpress(){
    cancelOption();
    stoakonijesnimio(qds);
  }
  private void OKpress(){

    prijeuporabe();
    stoakojesnimio(qds);
    //if (cart != null && toSave) saveArtGroup();
    saveUplatu();
  }
  public void prijeuporabe(){

    if (qds != null) {
      qds.post();
      if (qds.getString("TEXTFAK").equalsIgnoreCase("")) {
//System.out.println("ydyasdas");
        qds.deleteRow();
      }
    }
  }

  abstract public void stoakojesnimio(QueryDataSet qds);
  abstract public void stoakonijesnimio(QueryDataSet qds);

  public void cancelOption(){
    if (postojiprijeQDS==false) qds.cancel();
    miniFrame.setVisible(false);
  }
  public void saveUplatu(){
    miniFrame.setVisible(false);
  }
  private QueryDataSet qds= null;
  private raControlDocs rCD = new raControlDocs();

  public frmDodatniTxt(){}

//  public frmDodatniTxt(java.awt.Frame frame,QueryDataSet ds,Point loc) {
  public void setUP(java.awt.Container con,QueryDataSet ds,Point loc,QueryDataSet qdsotprije){
    postojiprijeQDS=true;
    initsetUP(con,ds,loc);
    qds=qdsotprije;
    rest(loc);
  }
  private void initsetUP(java.awt.Container con,QueryDataSet ds,Point loc){
    if (con instanceof java.awt.Frame) {
      miniFrame = new JraDialog((java.awt.Frame) con,"Dodatni tekst",true);
    } else if (con instanceof java.awt.Dialog) {
      miniFrame = new JraDialog((java.awt.Dialog) con,"Dodatni tekst",true);
    } else {
      miniFrame = new JraDialog();
    }
    
    if (ds.hasColumn("CART") == null) cart = null; 
    else cart = Integer.toString(ds.getInt("CART"));
    toSave = ds.hasColumn("CART") != null && 
             ds.hasColumn("VRDOK") == null;
    
    if (ds.hasColumn("CGRART") == null) cgrart = null;
    else cgrart = ds.getString("CGRART");
  }
  public void setUP(java.awt.Container con,QueryDataSet ds,Point loc){
    initsetUP(con,ds,loc);
    postojiprijeQDS=false;
    setUP(ds,loc);
    rest(loc);
  }

  private JraButton dodNapomena = new JraButton();
  private boolean isDodatnaNapomena = false;
  String cart = null;
  boolean toSave = false;
  String cgrart = null;

  public void setDodatnaNapomena(boolean isDodatnaNapomena) {
	this.isDodatnaNapomena = isDodatnaNapomena;
  }
  private void setUP(QueryDataSet ds,Point loc) {

    String ckey = rCD.getKey(ds);
    qds= hr.restart.util.Util.getNewQueryDataSet("SELECT * from vttext where ckey='"+ckey+"'",true);
    if (qds.getRowCount()==0){
      qds.insertRow(true);
      qds.setString("CKEY",ckey);
    }
  }
  private void rest(Point loc) {

    miniOK.registerOKPanelKeys(miniFrame);
    AWTKeyboard.registerKeyStroke(miniFrame, AWTKeyboard.F9, new KeyAction() {
      public boolean actionPerformed() {
        execF9.invokeLater();
        return true;
      }
    });
    
    scroller.setBorder(BorderFactory.createLoweredBevelBorder());
     scroller.setHorizontalScrollBar(hs);
    scroller.setVerticalScrollBar(vs);
    scroller.setPreferredSize(new Dimension(400,200));
    
    dodNapomena.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        execF9.invokeLater();
      }
    });
    dodtxt.setDataSet(qds);
    dodtxt.setColumnName("TEXTFAK");
    dodtxt.setBorder(new JraTextField().getBorder());
    dodNapomena.setText("...");
    
    jp.setBorder(BorderFactory.createEtchedBorder());
    miniFrame.getContentPane().setLayout(new BorderLayout());
    miniFrame.getContentPane().add(jp,BorderLayout.NORTH);
    miniFrame.getContentPane().add(scroller,BorderLayout.CENTER);
    miniFrame.getContentPane().add(miniOK,BorderLayout.SOUTH);
    XYLayout xyz = new XYLayout();
    xyz.setHeight(130);
    xyz.setWidth(470);
    jp.setLayout(new FlowLayout());
    if (isDodatnaNapomena) labelich.setText("Opis dokumenta");
    jp.add(labelich);
    jp.add(dodNapomena);
    miniFrame.pack();
    miniFrame.setLocationRelativeTo(miniFrame.getParent());
    /*miniFrame.setLocation((int)(loc.getX()+150),
                          (int)(loc.getY()+100));*/
    miniFrame.setVisible(true);
  }

  void presDodatnaNapomena()  {
  	String[] result;
  	lookupFrame lookUp = null;
    result = LD.lookUp(miniFrame, dm.getNapomene(), new int[] {0, 1});
    if (result != null ) {
      if (dodtxt.getText().length() > 0)
        dodtxt.append(" " + result[3]);
      else dodtxt.setText(result[3]);
    }
  }
  
  /*void saveArtGroup() {
    String txt = dodtxt.getText();
    if (txt.trim().length() == 0) return;
    String opts = getGroupOptions();
    if (opts == null) opts = txt;
    else {
      List old = new VarStr(opts).splitAsList('|');
      if (!old.contains(txt)) old.add(txt);
      opts = VarStr.join(old, '|').toString();
    }
    DataSet dod = VTText.getDataModule().getTempSet(Condition.equal("CKEY", 
        "group-" + dm.getArtikli().getString("CGRART")));
    dod.open();
    if (dod.rowCount() == 0) {
      dod.insertRow(false);
      dod.setString("CKEY", "group-" + dm.getArtikli().getString("CGRART"));
    }
    dod.setString("TEXTFAK", opts);
    dod.saveChanges();
  }*/
  
  void checkGroup(String cg) {
    StorageDataSet doh = VTText.getDataModule().getScopedSet("textfak");
    doh.open();
   
    do {
      addOptions(doh, cg);
    } while (LD.raLocate(dm.getGrupart(), "CGRART", cg) &&
        !cg.equals(dm.getGrupart().getString("CGRARTPRIP")) &&
        (cg = dm.getGrupart().getString("CGRARTPRIP")) != null);
    
    doh.setSort(new SortDescriptor(new String[] {"TEXTFAK"}));
    doh.setTableName("art-group-extra");
    String[] result = LD.lookUp(miniFrame, doh, new String[] {"TEXTFAK"}, 
        new String[] {""}, new int[] {0});    
    if (result != null)
      if (dodtxt.getText().length() > 0) 
        dodtxt.append(" " + result[0]);
      else dodtxt.setText(result[0]);
  }
  
  void addOptions(StorageDataSet doh, String cg) {
    DataSet ds = VTText.getDataModule().openTempSet("ckey LIKE 'group-" + cg + "-%'");
    for (ds.first(); ds.inBounds(); ds.next()) {
      doh.insertRow(false);
      doh.setString("TEXTFAK", ds.getString("TEXTFAK"));
      doh.post();
    }
  }
  
  void checkArtGroup() {
    if (LD.raLocate(dm.getArtikli(), "CART", cart))
      checkGroup(dm.getArtikli().getString("CGRART"));
  }
  
  /*void checkArtGroup() {
    String opts = getGroupOptions();
    StorageDataSet doh = VTText.getDataModule().getScopedSet("textfak");
    doh.open();
    if (opts != null && opts.length() > 0) {
      String[] op = new VarStr(opts).split('|');
      for (int i = 0; i < op.length; i++) {
        doh.insertRow(false);
        doh.setString("TEXTFAK", op[i]);
        doh.post();
      }
      doh.setSort(new SortDescriptor(new String[] {"TEXTFAK"}));
    }
    doh.setTableName("art-group-extra");
    String[] result = LD.lookUp(miniFrame, doh, new String[] {"TEXTFAK"}, 
        new String[] {""}, new int[] {0});    
    if (result != null)
      if (dodtxt.getText().length() > 0) 
        dodtxt.append(" " + result[0]);
      else dodtxt.setText(result[0]);
  }*/
  
  /*String getGroupOptions() {
    if (LD.raLocate(dm.getArtikli(), "CART", cart)) {
      DataSet dod = VTText.getDataModule().getTempSet(Condition.equal("CKEY", 
              "group-" + dm.getArtikli().getString("CGRART")));
      dod.open();
      if (dod.rowCount() > 0)
        return dod.getString("TEXTFAK");
    }
    return null;
  }*/
}
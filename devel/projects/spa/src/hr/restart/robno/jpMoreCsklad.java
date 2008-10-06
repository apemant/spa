/****license*****************************************************************
**   file: jpMoreCsklad.java
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

import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.VarStr;
import hr.restart.util.raTwoTableChooser;
import hr.restart.util.sysoutTEST;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.StringTokenizer;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpMoreCsklad extends JPanel{
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private VarStr vs  = new VarStr("");
  protected JraTextField skladista = new JraTextField();
  protected JraButton jodbir = new JraButton();

  private raTwoTableChooser rTTC = new raTwoTableChooser(){
     public void saveChoose(){
       mysaveChoose();
     }
  };
  private JDialog cjuzer ;

  private Frame fowner;

  public void setFrameOwner(Frame frm){
    fowner = frm;
  }

  public jpMoreCsklad() {
    jodbir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        MyactionPerformed(e);
      }
    });
    jodbir.setText("...");
    this.setLayout(new XYLayout());
    this.add(new JLabel("Skladišta "), new XYConstraints(15,0,100,-1));
    setKnjig(hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldk, String newk) {
        setKnjig(newk);
      }
    });
    
    addFields();
  }
  
  private String knjig="", oj="";
  
  public jpMoreCsklad(String knjig, String oj){
    this();
    this.knjig = knjig;
    this.oj = oj;
  }
  
  public void addFields(){
      this.add(skladista, new XYConstraints(150,0,100,-1));
      this.add(jodbir, new XYConstraints(255,0,21,21));
  }

  public void setKnjig(String knjig) {
    System.out.println("knjig changed");
    this.knjig = knjig;
  }
  
  public void setOj(String oj) {
    System.out.println("Organizacijska jedinica changed - " + this.oj + " to " + oj);
    this.oj = oj;
  }
  public void MyactionPerformed(java.awt.event.ActionEvent e) {

    cjuzer = new JDialog(fowner,"Odabir skladišta",true);
    StorageDataSet lijevi = new StorageDataSet();
    lijevi.setColumns(new Column[] {
      dm.getSklad().getColumn("CSKL").cloneColumn(),
      dm.getSklad().getColumn("NAZSKL").cloneColumn()
    });
    StorageDataSet desni = new StorageDataSet();
    desni.setColumns(new Column[] {
      dm.getSklad().getColumn("CSKL").cloneColumn(),
      dm.getSklad().getColumn("NAZSKL").cloneColumn()
    });
    lijevi.open();
    desni.open();
    
    
    QueryDataSet skls;
    
//    if (!knjig.equalsIgnoreCase("") && !oj.equalsIgnoreCase("")){
      skls = hr.restart.baza.Sklad.getDataModule().getFilteredDataSet("knjig = '"+knjig+"'");
      
      System.out.println(skls.getQuery().toString());
//    } else {
//      skls = dm.getSklad();
//    }
    
    skls.open();
    
    sysoutTEST sys = new sysoutTEST(false);
    sys.prn(skls);
    
    for (skls.first();skls.inBounds();skls.next()) {
      lijevi.insertRow(true);
      lijevi.setString("CSKL",skls.getString("CSKL"));
      lijevi.setString("NAZSKL",skls.getString("NAZSKL"));
    }
    
    lijevi.first();

    cjuzer.getContentPane().setLayout(new BorderLayout());
    cjuzer.getContentPane().add(rTTC,BorderLayout.CENTER);
    lijevi.setTableName("jpSklad");
    desni.setTableName("jpSklad");
    rTTC.setLeftDataSet(lijevi);
    rTTC.setRightDataSet(desni);
    rTTC.initialize();
    rTTC.rnvSave.registerNavKey(cjuzer);
    rTTC.rnvLtoR.registerNavKey(cjuzer);
    rTTC.rnvLtoR_all.registerNavKey(cjuzer);
    rTTC.rnvRtoL.registerNavKey(cjuzer);
    rTTC.rnvRtoL_all.registerNavKey(cjuzer);

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    cjuzer.pack();
    cjuzer.setLocation(dim.width/2-cjuzer.getWidth()/2,dim.height/2-cjuzer.getHeight()/2);
    cjuzer.show();
  }

  private void mysaveChoose() {
    vs = new VarStr("");
    for (rTTC.getRightDataSet().first();rTTC.getRightDataSet().inBounds();rTTC.getRightDataSet().next()) {
      vs.append(rTTC.getRightDataSet().getString("CSKL")).append(",");
    }
    vs.chop(1);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        skladista.setText(vs.toString());
      }
    });
    cjuzer.hide();
  }

  public String getTokeni(){
    if (!skladista.getText().equalsIgnoreCase("")) {
      StringTokenizer st = new StringTokenizer(skladista.getText(),",");
      VarStr tokeni=new VarStr("('");
      while (st.hasMoreTokens()) {
        tokeni=tokeni.append((String)st.nextElement()).append("','");
      }
      tokeni.chopRight(2);
      tokeni.append(")");
      return tokeni.toString();
    }
    return "";
  }
  
  public boolean isThereTokens(){
    return !skladista.getText().equalsIgnoreCase(""); 
  }
}
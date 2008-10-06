/****license*****************************************************************
**   file: testValidDocs.java
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

import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class testValidDocs extends JraDialog {

  private raValidacijaDocs rVD = raValidacijaDocs.getraValidacijaDocs();
  private raValidacijaZaNivelaciju rVZN = new raValidacijaZaNivelaciju();
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private boolean moj = false ;
  private rapancskl1 rapancskl = new rapancskl1();
  private rapancartSimple rapancart = new rapancartSimple() {
    public void Myafter_lookUp(){
    }
  };
  private OKpanel okp = new OKpanel(){
    public void jPrekid_actionPerformed(){
      myPrekid();
    }
    public void jBOK_actionPerformed(){
      myOK();
    }
  };

  private JraCheckBox automatika = new JraCheckBox("Popravak");
  private JraCheckBox ppc = new JraCheckBox("Provjera preuzetih cijena");
  private JLabel vd = new JLabel("Vrsta dokumenta");
  private JraComboBox cb ;

//  private hr.restart.sisfun.dlgErrors dgE = new hr.restart.sisfun.dlgErrors(this,"Popis grešaka",true);

  public testValidDocs() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {

    okp.registerOKPanelKeys(this);
    String[] docs = new String[TypeDoc.araj_docs.length+TypeDoc.araj_docsOJ.length+1];
    docs[0] = "SVI";
    for (int i=0;i< TypeDoc.araj_docs.length;i++) docs[i+1] = TypeDoc.araj_docs[i];
    for (int i=0;i< TypeDoc.araj_docsOJ.length;i++) docs[i+TypeDoc.araj_docs.length+1] = TypeDoc.araj_docsOJ[i];
    cb = new JraComboBox(docs);
    this.getContentPane().setLayout(new BorderLayout());
    JPanel panel = new JPanel(new XYLayout());
    panel.setBorder(BorderFactory.createEtchedBorder());
    ((XYLayout) panel.getLayout()).setHeight(150);
    ((XYLayout) panel.getLayout()).setWidth(650);
    panel.add(rapancskl,new XYConstraints(0,0,-1,50));
    panel.add(rapancart,new XYConstraints(0,50,-1,75));
    panel.add(vd,new XYConstraints(15,125,120,-1));
    panel.add(cb,new XYConstraints(150,125,120,-1));
    panel.add(ppc,new XYConstraints(320,125,120,-1));
    panel.add(automatika,new XYConstraints(500,125,120,-1));


    this.getContentPane().add(okp,BorderLayout.SOUTH);
    this.getContentPane().add(panel,BorderLayout.CENTER);
  }

  public void mojprikaz() {
    javax.swing.JDialog d = new hr.restart.swing.JraDialog();
    d.setModal(true);
    hr.restart.util.raJPTableView jp = new hr.restart.util.raJPTableView();
    jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    jp.setDataSet(rVD.errors);
    jp.getColumnsBean().initialize();
    d.getContentPane().add(jp,java.awt.BorderLayout.CENTER);
//        startFrame.getStartFrame().centerFrame(d,0,title);
    d.pack();
    d.show();
  }
  public void prikazGreske() {
    if (moj) {
      mojprikaz();
    }
    else {
      hr.restart.sisfun.dlgErrors dgE = new hr.restart.sisfun.dlgErrors(this,"Popis grešaka",false);
      dgE.setDataSet(rVD.errors);
//      ST.showInFrame(rVD.errors,"Greške");
      dgE.show();
    }
  }

  private boolean Validacija() {

    if (Valid.getValid().isEmpty(rapancskl.jrfCSKL)) return false;

    return true;
  }
  private void myPrekid() {
    if (rapancskl.jrfCSKL.getText().equals("")) {
      this.hide();
    }
    else {
      rapancskl.jrfCSKL.setText("");
      rapancskl.jrfCSKL.emptyTextFields();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          rapancskl.jrfCSKL.requestFocus();
        }
      });
    }
  }
  private void myOK(){
    if (Validacija()) {
      rVD.errors.emptyAllRows();
      new Thread( new Runnable() {
        public void run() {
          startFrame.getStartFrame().getStatusBar().startTask(2,"Pregled i popravak u tijeku ...");
          go();
          startFrame.getStartFrame().getStatusBar().finnishTask();
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              if (rVD.errors.getRowCount()!=0) {
                prikazGreske();
              }
              else {
                javax.swing.JOptionPane.showConfirmDialog(null,"Da ne povjeruješ sve je OK !","Poruka",
                    javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
              }
            }
      });

        }
      }).start();
    }
  }

  public void nivel(final String cskl, final String cart,final String god) {
    Thread nivelacija = new Thread(new Runnable(){
      public void run(){

        rVZN.setraValidacijaDocs(rVD);
        rVZN.checkPrice(cskl,"2003",cart,true);
      }
    });
    nivelacija.start();
    try {
      nivelacija.join();
    }
    catch (InterruptedException ex) {
      ex.printStackTrace();
    }
  }

  private void go() {

    String cskl = rapancskl.jrfCSKL.getText();
    String cart = rapancart.jrfCART.getText();
    String vd = (String) cb.getSelectedItem();
    boolean automatishe = automatika.isSelected();
    boolean bppc = ppc.isSelected();
    if (bppc) {
      rVD.errors.emptyAllRows();
      nivel(cskl,cart,"2003");
    }
    rVD.setVrsklad(hr.restart.util.Util.getNewQueryDataSet("select vrzal from sklad where cskl='"+cskl+"'",true).getString("VRZAL"));
System.out.println("go() vrzal -> "+cskl+" - "+rVD.getVrsklad());
    if ("SVI".equalsIgnoreCase(vd)) {
      for (int i=0;i<TypeDoc.araj_docs.length;i++) {
          provjeraipopravak(cskl,cart,TypeDoc.araj_docs[i],automatishe,false);
          if (TypeDoc.araj_docs[i].equalsIgnoreCase("MES")) {
            provjeraipopravak(cskl,cart,TypeDoc.araj_docs[i],automatishe,true);
          }
      }
      for (int i=0;i<TypeDoc.araj_docsOJ.length;i++) {
          provjeraipopravak(cskl,cart,TypeDoc.araj_docsOJ[i],automatishe,false);
      }
    }
    else {
      provjeraipopravak(cskl,cart,vd,automatishe,false);
    }
  }

  public void provjeraipopravak(String cskl,String cart,String vrdok,boolean automatika,boolean ulaza) {

    QueryDataSet rkstavke;
    rVD.setDozvoljeno_odstupanje(Aus.zero2);
    rVD.setAutomatrepair(automatika);
    String Sqlrkstavke = "SELECT * FROM ";
    boolean ulaz = false;
    if (TypeDoc.getTypeDoc().isDocStdoku(vrdok)) {
      Sqlrkstavke = Sqlrkstavke+"STDOKU WHERE ";
    Sqlrkstavke = Sqlrkstavke+"VRDOK='"+vrdok+"' and cskl='"+cskl+"' ";
    }
    else if (TypeDoc.getTypeDoc().isDocStdoki(vrdok)){
      Sqlrkstavke = Sqlrkstavke+"STDOKI WHERE ";
    Sqlrkstavke = Sqlrkstavke+"VRDOK='"+vrdok+"' and cskl='"+cskl+"' ";
    }
    else if (TypeDoc.getTypeDoc().isDocStmeskla(vrdok)){
      Sqlrkstavke = Sqlrkstavke+"STMESKLA WHERE ";
      boolean input = vrdok.equalsIgnoreCase("MEU") || (vrdok.equalsIgnoreCase("MES") && ulaza);
      if (input) {
        Sqlrkstavke = Sqlrkstavke+"VRDOK='"+vrdok+"' and csklul='"+cskl+"'";
        ulaz = true;
      } else {
        Sqlrkstavke = Sqlrkstavke+"VRDOK='"+vrdok+"' and cskliz='"+cskl+"'";
        ulaz = false;
      }
    }

    if (!cart.equalsIgnoreCase("")) Sqlrkstavke = Sqlrkstavke+"and cart="+cart;
    rkstavke = hr.restart.util.Util.getNewQueryDataSet(Sqlrkstavke,true);
//if (TypeDoc.getTypeDoc().isDocStmeskla(vrdok)){
//System.out.println(Sqlrkstavke);
//}
    for (rkstavke.first();rkstavke.inBounds();rkstavke.next()) {
      rVD.testDocs(rkstavke,ulaz);
    }
  }
}
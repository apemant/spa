/****license*****************************************************************
**   file: prewievCalc.java
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
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraLabel;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.raFrame;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
/**

 ovo je mogu\u0107a klasa za Mariju iz ekosa gdje se treba raditi mini kalkulacija

 */

public class prewievCalc  extends raFrame{

  rapancskl1 rapancskl = new rapancskl1(349) {
    public void MYpost_after_lookUp(){
      MMYpost_after_lookUp();
    }
  };

  rapancart rapancart = new rapancart(){
    public void metToDo_after_lookUp(){
      MMyafter_lookUp();
    }
  };

  private hr.restart.util.raCommonClass cc = hr.restart.util.raCommonClass.getraCommonClass();
  private JPanel myjpanel = new JPanel(new XYLayout());
  private JLabel jlrabat = new JLabel("Popust");
  private JLabel jlmarza = new JLabel("RUC %");
  private JLabel jlmarzaprije = new JLabel("Prije popusta",javax.swing.SwingUtilities.TRAILING);
  private JLabel jlmarzaposlije = new JLabel("Poslije popusta",javax.swing.SwingUtilities.TRAILING);
  private JLabel jlcijenaBPbezpopustaprije = new JLabel("Cijena bez poreza");
  private JLabel jlcijenaSPbezpopustaprije = new JLabel("Cijena s porezom");
  private JraTextField jtfRabat = new JraTextField() {
    public void valueChanged() {
      Kalkulacija();
    }
  };
  private JraTextField jtfGljupan = new JraTextField();
  private JraLabel marzaprije = new JraLabel();
  private JraLabel marzaposlije = new JraLabel();
  private JraLabel cBPprije = new JraLabel();
  private JraLabel cBPposlije = new JraLabel();
  private JraLabel cSPprije = new JraLabel();
  private JraLabel cSPposlije = new JraLabel();
  private StorageDataSet dsIzracun = null;
  private JPanel panelzasve = new JPanel();
  private XYLayout myLayout = new XYLayout();
  private boolean bopen = false;

  private OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed(){
      OKpress();
    }

    public void jPrekid_actionPerformed(){
      Cancelpress();
  }};

  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

  private Border mybord = BorderFactory.createCompoundBorder(
      BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(0, 0, 0, 2));

  public prewievCalc() {
    try {
      jbInit();
    }
    catch (Exception ex) {
    }
  }

  public void jbInit() throws Exception {

//    okp.registerOKPanelKeys(this);
//    hr.restart.util.Aus.removeSwingKeyRecursive(
//        this,java.awt.event.KeyEvent.VK_ESCAPE);

    AWTKeyboard.registerKeyListener(getWindow(), 
        new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent e) {
        if (e.getKeyCode()== java.awt.event.KeyEvent.VK_ESCAPE) {
          myESC();
        }
        if (e.getKeyCode()== java.awt.event.KeyEvent.VK_F10) {
          OKpress();
        }
      }
    });

    this.addWindowListener(new java.awt.event.WindowAdapter(){
      public void windowActivated(java.awt.event.WindowEvent e){
        if (bopen) return;
        initdsIzracun();
        resetRapane();
        rapancskl.setDefaultCSKL();
        if (rapancskl.jrfCSKL.getText().equalsIgnoreCase("")) {
          rapancskl.jrfCSKL.requestFocus();
        } else {
          rapancart.SetDefFocus();
          cc.EnabDisabAll(rapancskl,false);
        }
        cc.EnabDisabAll(rapancart,true);
        bopen = true;
      }
      public void windowClosing(java.awt.event.WindowEvent e){
        bopen = false;
      }
    });

    jtfRabat.setColumnName("RABAT");
    /*jtfRabat.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent e) {
        if (jtfRabat.isValueChanged()) {
          Kalkulacija();
        }
//        jtfGljupan.requestFocus();
      }
    });*/
    jtfGljupan.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            jtfRabat.requestFocus();
               jtfRabat.selectAll();
          }
        });
      }
    });

    marzaprije.setColumnName("MARZAPRIJE");
    marzaprije.setBorder(mybord);
    marzaposlije.setColumnName("MARZAPOSLIJE");
    marzaposlije.setBorder(mybord);
    cBPprije.setColumnName("CBPPRIJE");
    cBPprije.setBorder(mybord);
    cBPposlije.setColumnName("CBPPOSLIJE");
    cBPposlije.setBorder(mybord);
    cSPprije.setColumnName("CSPPRIJE");
    cSPprije.setBorder(mybord);
    cSPposlije.setColumnName("CSPPOSLIJE");
    cSPposlije.setBorder(mybord);

    myjpanel.add(jlmarzaprije,                new XYConstraints(150,5,150,-1));
    myjpanel.add(jlmarzaposlije,              new XYConstraints(310,5,150,-1));
    myjpanel.add(jlrabat,                     new XYConstraints(15,25,-1,-1));
    myjpanel.add(jtfRabat,                    new XYConstraints(150,25,100,-1));
    myjpanel.add(jtfGljupan,                  new XYConstraints(150,25,1,1));
    myjpanel.add(jlmarza,                     new XYConstraints(15,60,-1,-1));
    myjpanel.add(marzaprije,                  new XYConstraints(150,60,150,-1));
    myjpanel.add(marzaposlije,                new XYConstraints(310,60,150,-1));
    myjpanel.add(jlcijenaBPbezpopustaprije,   new XYConstraints(15,95,-1,-1));
    myjpanel.add(cBPprije,                    new XYConstraints(150,95,150,-1));
    myjpanel.add(cBPposlije,                  new XYConstraints(310,95,150,-1));
    myjpanel.add(jlcijenaSPbezpopustaprije,   new XYConstraints(15,130,-1,-1));
    myjpanel.add(cSPprije,                    new XYConstraints(150,130,150,-1));
    myjpanel.add(cSPposlije,                  new XYConstraints(310,130,150,-1));

    this.getContentPane().setLayout(new BorderLayout());
    panelzasve.setBorder(BorderFactory.createEtchedBorder());
    panelzasve.setLayout(myLayout);
    myLayout.setHeight(310);
    panelzasve.add(rapancskl,                  new XYConstraints(1,1,635,50));
    panelzasve.add(rapancart,                   new XYConstraints(1,55,-1,90));
    panelzasve.add(myjpanel,                   new XYConstraints(1,150,-1,150));
    this.getContentPane().add(okp,BorderLayout.SOUTH);
    this.getContentPane().add(panelzasve,BorderLayout.CENTER);
    rapancart.setBorder(null);
    rapancart.setMode("DOH");
  }

  public void OKpress(){
    Kalkulacija();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jtfRabat.requestFocus();
      }
    });
  }

  public void Cancelpress(){
    bopen = false;
    this.hide();
  }

  private void setDataSet(){

    jtfRabat.setDataSet(dsIzracun);
    marzaprije.setDataSet(dsIzracun);
    marzaposlije.setDataSet(dsIzracun);
    cBPprije.setDataSet(dsIzracun);
    cBPposlije.setDataSet(dsIzracun);
    cSPprije.setDataSet(dsIzracun);
    cSPposlije.setDataSet(dsIzracun);
  }

  public void initdsIzracun() {
    if (dsIzracun !=null) {
      dsIzracun.close();
      dsIzracun =null;
    }
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    dsIzracun = new StorageDataSet();
    Column cart = dm.getStdoki().getColumn("CART").cloneColumn();
    cart.setColumnName("CART");
    Column crabat = dm.getStdoki().getColumn("UPRAB").cloneColumn();
    crabat.setColumnName("RABAT");
    Column cmarzaprije = dm.getStdoki().getColumn("UPRAB").cloneColumn();
    cmarzaprije.setColumnName("MARZAPRIJE");
    Column cmarzaposlije = dm.getStdoki().getColumn("UPRAB").cloneColumn();
    cmarzaposlije.setColumnName("MARZAPOSLIJE");
    Column cnc = dm.getStdoki().getColumn("NC").cloneColumn();
    cnc.setColumnName("NC");
    Column ccBPprije = dm.getStdoki().getColumn("IPRODSP").cloneColumn();
    ccBPprije.setColumnName("CBPPRIJE");
    Column ccBPposlije = dm.getStdoki().getColumn("IPRODSP").cloneColumn();
    ccBPposlije.setColumnName("CBPPOSLIJE");
    Column ccSPprije = dm.getStdoki().getColumn("IPRODSP").cloneColumn();
    ccSPprije.setColumnName("CSPPRIJE");
    Column ccSPposlije = dm.getStdoki().getColumn("IPRODSP").cloneColumn();
    ccSPposlije.setColumnName("CSPPOSLIJE");
    dsIzracun.setColumns(new Column[] {cart,crabat,cmarzaprije,cmarzaposlije,cnc,
    ccBPprije,ccBPposlije,ccSPprije,ccSPposlije});
    dsIzracun.open();
    dsIzracun.insertRow(true);
    setDataSet();
  }

  private void resetIzracun(){
    dsIzracun.setBigDecimal("RABAT", new java.math.BigDecimal("0.00"));
    dsIzracun.setBigDecimal("NC", new java.math.BigDecimal("0.00"));
    dsIzracun.setBigDecimal("MARZAPRIJE", new java.math.BigDecimal("0.00"));
    dsIzracun.setBigDecimal("MARZAPOSLIJE", new java.math.BigDecimal("0.00"));
    dsIzracun.setBigDecimal("CBPPRIJE", new java.math.BigDecimal("0.00"));
    dsIzracun.setBigDecimal("CBPPOSLIJE", new java.math.BigDecimal("0.00"));
    dsIzracun.setBigDecimal("CSPPRIJE", new java.math.BigDecimal("0.00"));
    dsIzracun.setBigDecimal("CSPPOSLIJE", new java.math.BigDecimal("0.00"));
  }

  private void resetRapane() {
    rapancskl.jrfCSKL.emptyTextFields();
    rapancart.jrfCART.emptyTextFields();
  }
  private void Kalkulacija(){
    dsIzracun.setBigDecimal("CBPPOSLIJE", izbijPostotak(dsIzracun.getBigDecimal("CBPPRIJE")));
    dsIzracun.setBigDecimal("CSPPOSLIJE", izbijPostotak(dsIzracun.getBigDecimal("CSPPRIJE")));
    dsIzracun.setBigDecimal("MARZAPOSLIJE", kolikoOD(dsIzracun.getBigDecimal("NC"),dsIzracun.getBigDecimal("CBPPOSLIJE")));
  }

  private java.math.BigDecimal izbijPostotak(java.math.BigDecimal bigdaddy){
    java.math.BigDecimal tmpBD = new java.math.BigDecimal("100.00");
    tmpBD = tmpBD.subtract(dsIzracun.getBigDecimal("RABAT"));
    tmpBD = bigdaddy.multiply(tmpBD).divide(new java.math.BigDecimal("100.00"),2,java.math.BigDecimal.ROUND_HALF_UP);
//    tmpBD = tmpBD.divide(tmpBD,2,java.math.BigDecimal.ROUND_HALF_UP);
    return tmpBD;
  }
  private java.math.BigDecimal kolikoOD(java.math.BigDecimal osnovica,java.math.BigDecimal value){

    java.math.BigDecimal tmpBD = value.subtract(osnovica);

    try {
      tmpBD =tmpBD.divide(osnovica,4,java.math.BigDecimal.ROUND_HALF_UP);
      tmpBD = tmpBD.multiply(new java.math.BigDecimal("100.00"));
    }
    catch (Exception ex) {
      tmpBD = new java.math.BigDecimal("0.00");
    }
    return tmpBD;
  }
  private void findStanje(){

    if (!(rapancskl.jrfCSKL.getText().equalsIgnoreCase("") ||
        rapancart.jrfCART.getText().equalsIgnoreCase(""))) {

      allStanje ALS = new allStanje();
      QueryDataSet stanje = ALS.gettrenSTANJE(hr.restart.util.Valid.getValid().findYear(),
          rapancskl.jrfCSKL.getText(), new Integer(rapancart.jrfCART.getText()).intValue());
//      ST.prn(stanje);
      dsIzracun.setBigDecimal("RABAT", new java.math.BigDecimal("0.00"));
      dsIzracun.setBigDecimal("NC", stanje.getBigDecimal("NC"));
      dsIzracun.setBigDecimal("CBPPRIJE", stanje.getBigDecimal("VC"));
      dsIzracun.setBigDecimal("CBPPOSLIJE", stanje.getBigDecimal("VC"));
      dsIzracun.setBigDecimal("CSPPRIJE", stanje.getBigDecimal("MC"));
      dsIzracun.setBigDecimal("CSPPOSLIJE", stanje.getBigDecimal("MC"));
      dsIzracun.setBigDecimal("MARZAPOSLIJE", kolikoOD(dsIzracun.getBigDecimal("NC"),dsIzracun.getBigDecimal("CBPPOSLIJE")));
      dsIzracun.setBigDecimal("MARZAPRIJE", kolikoOD(dsIzracun.getBigDecimal("NC"),dsIzracun.getBigDecimal("CBPPRIJE")));
    } else {
      resetIzracun();
    }
  }

  private void MMyafter_lookUp(){
    findStanje();
    cc.EnabDisabAll(rapancart,false);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jtfGljupan.requestFocus();
//        jtfRabat.requestFocus();
      }
    });
  }

  private void myESC(){

    if (!(rapancart.jrfCART.getText().equalsIgnoreCase(""))) {
      rapancart.jrfCART.setText("");
      rapancart.jrfCART.emptyTextFields();
      cc.EnabDisabAll(rapancart,true);
      resetIzracun();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          rapancart.SetDefFocus();
        }
      });
      return;
   }

    if (!(rapancskl.jrfCSKL.getText().equalsIgnoreCase(""))) {
      rapancskl.jrfCSKL.setText("");
      rapancskl.jrfCSKL.emptyTextFields();
      rapancart.jrfCART.setText("");
      rapancart.jrfCART.emptyTextFields();
      resetIzracun();
      cc.EnabDisabAll(rapancskl,true);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          rapancskl.jrfCSKL.requestFocus();
        }
      });
      return ;
    }
    Cancelpress();
  }
  public void MMYpost_after_lookUp() {
    if (!(rapancskl.jrfCSKL.getText().equalsIgnoreCase(""))) {
      findStanje();
      rapancart.setGodina(Aut.getAut().getKnjigodRobno());
      rapancart.setCskl(rapancskl.getCSKL());
      cc.EnabDisabAll(rapancskl,false);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          rapancart.SetDefFocus();
        }
      });
    } else {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          rapancskl.jrfCSKL.requestFocus();
        }
      });
    }

    hr.restart.db.raConnectionFactory rr;
  }
}


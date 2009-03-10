/****license*****************************************************************
**   file: frmObrPor.java
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
package hr.restart.pl;

import hr.restart.baza.Condition;
import hr.restart.baza.Parametripl;
import hr.restart.baza.Radnicipl;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raProcess;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
public class frmObrPor extends frmObradaPL {
  raIniciranje inicir = raIniciranje.getInstance();
  raObracunPL obracun = raObracunPL.getInstance();
  JCheckBox jcbDopr = new JCheckBox("Obra\u010Dun doprinosa",true);
  JCheckBox jcbPor = new JCheckBox("Obra\u010Dun poreza",true);
  JCheckBox jcbKred = new JCheckBox("Obra\u010Dun kredita",true);
  JCheckBox jcbPrir = new JCheckBox("Obra\u010Dun prireza",true);
  JCheckBox jcbLimitsPOR = new JCheckBox("Koristi osn. (porez)",true);
  JCheckBox jcbLimitsDOP = new JCheckBox("Koristi osn. (dopr.)",true);
  JCheckBox jcbCRP = new JCheckBox("Godišnji obraèun poreza",false);
  JraDialog dprw;
  protected frmCRP frmcrp = null;
  public frmObrPor() {
    this.addComponentListener(new ComponentAdapter() {//TEMP
      public void componentResized(ComponentEvent e) {
        System.out.println(frmObrPor.this.getSize());
      }
    });    
    jp.remove(jtfDAN);
    jp.remove(jLabel3);
    frmatCB(jcbDopr);
    frmatCB(jcbPor);
    frmatCB(jcbKred);
    frmatCB(jcbPrir);
    frmatCB(jcbLimitsPOR);
    frmatCB(jcbLimitsDOP);
    frmatCB(jcbCRP);
    jcbCRP.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) { 
        if (jcbCRP.isSelected()) {
          if (frmcrp == null) frmcrp = new frmCRP();
          frmPL.getStartFrame().centerFrame(frmcrp,0,frmcrp.getTitle());
          frmcrp.show();
        } else {
          frmcrp = null;
        }
      }
      
    });
    jp.add(jcbDopr,new XYConstraints(300, 50, 140, -1));
    jp.add(jcbPrir,new XYConstraints(445, 50, 140, -1));
    jp.add(jcbPor,new XYConstraints(300, 75, 140, -1));
    jp.add(jcbLimitsPOR,new XYConstraints(445, 75, 140, -1));
    jp.add(jcbKred,new XYConstraints(300, 100, 140, -1));
    jp.add(jcbLimitsDOP,new XYConstraints(445, 100, 140, -1));
    jp.add(jcbCRP, new XYConstraints(300, 125, 285, -1));
  }
  private void frmatCB(JCheckBox jcb) {
    jcb.setHorizontalTextPosition(SwingConstants.LEADING);
    jcb.setHorizontalAlignment(SwingConstants.RIGHT);
    jcb.addKeyListener(new hr.restart.swing.JraKeyListener());
//    jcb.setBackground(java.awt.Color.pink);
  }
  public void pack() {
    super.pack();
    setSize(new Dimension(617,215));
  }
  public boolean Validacija() {
    if (!super.Validacija()) return false;
    obracun.initObracun(tds.getShort("GODINA"),tds.getShort("MJESEC"),tds.getShort("RBR"),tds.getString("CORG"),tds.getTimestamp("DATUM"));
    if (!inicir.isInitObr(this)) {
      JOptionPane.showMessageDialog(this,"Obrada nije inicirana!","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (inicir.obrCount > 0) {
      int answ = JOptionPane.showOptionDialog(jp,"Neke org jedinice su obra\u0111ene! Želite li poništiti obrade?",
                "Poništavanje obrade",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
      if (answ == 0) {
        new Thread() {
          public void run() {
            hr.restart.sisfun.raDelayWindow dw = hr.restart.sisfun.raDelayWindow.show(frmObrPor.this.jp);
            boolean ponsucc = obracun.ponobracun();
            dw.close();
            frmObrPor.showMessage(frmObrPor.this,"Poništavanje obrade","no",ponsucc);
          }
        }.start();

      }
      return false;
    }
    return getObrConfirmation();
  }
  public void doAfterLookup() {
    setEnableCorg(true);
    setEnableObr(false);
    rcc.setLabelLaF(jtfDATUM,true);
  }
  private boolean getObrConfirmation() {
    String poreze = jcbPor.isSelected()?"poreze, ":"";
    String doprinose = jcbDopr.isSelected()?"doprinose, ":"";
    String kredite = jcbKred.isSelected()?"kredite, ":"";
    String dopporkred = doprinose+poreze+kredite;
    int i = dopporkred.lastIndexOf(",");
    if (i<0) return false;//nije nista odabrao
    dopporkred = dopporkred.substring(0,dopporkred.lastIndexOf(","));
    i = dopporkred.lastIndexOf(",");
    if (i>0) dopporkred = new StringBuffer(dopporkred).replace(i,i+1," i").toString();
    setRunMessage("Obra\u010Dunavam "+dopporkred+"...");
    int answ = JOptionPane.showOptionDialog(jp,"Obra\u010Dunati "+dopporkred+(jcbCRP.isSelected()?" i RAZLIKU POREZA I PRIREZA PO GODIŠNJEM OBRAÈUNU  ":"")+"?",
              "Obra\u010Dun pla\u0107e",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
    return (answ == 0);
  }
  boolean succ=false;
  boolean showRazlCaPo = false;
  public void okPress() {
    obracun.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("msg")) {
          raProcess.setMessage(evt.getNewValue(), false);
        }
      }
    });
    if (frmcrp!=null && frmcrp.getCRP() !=null) {//obr.prvo normalno onda razliku poreza
      succ = obracun.obracun(jcbDopr.isSelected(),jcbPor.isSelected(),jcbKred.isSelected(),jcbPrir.isSelected(),jcbLimitsPOR.isSelected(),jcbLimitsDOP.isSelected(),false);
      if (succ) {
        frmcrp.getCRP().setRadnici(Radnicipl.getDataModule().getFilteredDataSet(Condition.equal("AKTIV","D")+" AND "+Condition.in("CORG",obracun.orgs)));
        frmcrp.getCRP().calcRazl();
        if (frmcrp.getCRP().isPreview()) {
          //TODO preview
          frmTableDataView preview = new frmTableDataView(true,false) {
            protected void OKPress() {
              showRazlCaPo = true;
              frmTableDataView prwdetail = new frmTableDataView(true, false);
              prwdetail.setDataSet(frmcrp.getCRP().getDetailCaPo());
              JraDialog dprwdetail = new JraDialog(dprw);
              dprwdetail.setModal(true);
              dprwdetail.setContentPane(prwdetail.getContentPane());
              frmPL.getStartFrame().centerFrame(dprwdetail,0,"Pregled izraèuna razlika poreza i prireza");
              dprwdetail.show();
              
            }
          };
          preview.setDataSet(frmcrp.getCRP().getMasterCaPo());
          dprw = new JraDialog(raProcess.getDialog());
          dprw.setModal(true);
          dprw.setContentPane(preview.getContentPane());
          frmPL.getStartFrame().centerFrame(dprw,0,"Pregled razlika poreza i prireza");
          dprw.show();
          //frmcrp.getCRP().showMaster();
        }
        try {
          frmcrp.getCRP().addOdbici();
          if (!obracun.ponobracun()) throw new Exception("Ponavljanje obrade nije uspjelo!!");
          succ = obracun.obracun(jcbDopr.isSelected(),jcbPor.isSelected(),jcbKred.isSelected(),jcbPrir.isSelected(),jcbLimitsPOR.isSelected(),jcbLimitsDOP.isSelected(),true);
        } catch (Exception e) {
          e.printStackTrace();
          succ = false;
        }
      }
    } else {
      succ = obracun.obracun(jcbDopr.isSelected(),jcbPor.isSelected(),jcbKred.isSelected(),jcbPrir.isSelected(),jcbLimitsPOR.isSelected(),jcbLimitsDOP.isSelected(),jcbCRP.isSelected());
    }
  }
  public void showMessage() {
    showMessage(this,"Obra\u010Dun","an",succ);
    if (frmcrp!=null && frmcrp.getCRP()!=null && showRazlCaPo) {
      frmcrp.getCRP().showDetail();
    }
  }
  public static void showMessage(frmObradaPL fOPL,String poruka,String an,boolean _succ) {
    JOptionPane.showMessageDialog(fOPL.jp,
        poruka+((_succ)?" u":" ne")+"spješ"+an+"!",
        "Obra\u010Dun pla\u0107e",
        ((_succ)?JOptionPane.INFORMATION_MESSAGE:JOptionPane.ERROR_MESSAGE)
      );
  }
  public class frmCRP extends JraDialog {
    StorageDataSet inputData = new StorageDataSet();
    JLabel jlLimit = new JLabel("Minimalni iznos razlike");
    JraTextField jtfLimit = new JraTextField();
    JLabel jlGodina = new JLabel("Godina za obraèun razlike");
    JraTextField jtfGodina = new JraTextField();
    JCheckBox jcbCalc = new JCheckBox("Obraèunati razliku", true);
    JCheckBox jcbClean = new JCheckBox("Obrisati odbitke razlike p. iz prošlih obraèuna", true); 
    JCheckBox jcbRealOlak = new JCheckBox("Koristiti prave olakšice",true);
    JCheckBox jcbPreview = new JCheckBox("Pregled obraèunatih razlika", false);
    OKpanel okp = new OKpanel() {
      public void jBOK_actionPerformed() {
        setupCRP();
        frmCRP.this.hide();
      }
      public void jPrekid_actionPerformed() {
        frmCRP.this.hide();
      }
    };
    CalcRazPor crp;
    public frmCRP() {
      setModal(true);
      setTitle("Parametri obr. razlike poreza");
      initDS();
      initPanel();
    }
    private void initPanel() {
      JPanel content = new JPanel(new XYLayout());
      jtfLimit.setDataSet(inputData);
      jtfLimit.setColumnName("LIMIT");
      jtfGodina.setDataSet(inputData);
      jtfGodina.setColumnName("GOD");
      
      content.add(jcbCalc, new XYConstraints(15, 20, -1, -1));
      content.add(jlGodina, new XYConstraints(15, 45, -1, -1));
      content.add(jtfGodina, new XYConstraints(200, 45, 100, -1));
      content.add(jlLimit, new XYConstraints(15, 70, -1, -1));
      content.add(jtfLimit, new XYConstraints(200, 70, 100, -1));
      content.add(jcbClean, new XYConstraints(15, 95, -1, -1));
      content.add(jcbRealOlak, new XYConstraints(15, 120, -1, -1));
      content.add(jcbPreview, new XYConstraints(15, 145, -1, -1));
      
      frmCRP.this.getContentPane().add(raMatPodaci.addScrolledAndCentered(content,null,false),BorderLayout.CENTER);
      frmCRP.this.getContentPane().add(okp,BorderLayout.SOUTH);
      //privremeno
      frmCRP.this.addComponentListener(new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
          System.out.println(frmCRP.this.getSize());
        }
      });
    }
    private void initDS() {
      crp = new CalcRazPor();
      inputData.addColumn(dM.createBigDecimalColumn("LIMIT"));
      inputData.addColumn(dM.createIntColumn("GOD"));
      Parametripl.getDataModule().getQueryDataSet().open();
      inputData.open();
      inputData.setBigDecimal("LIMIT",crp.getLimit());
      inputData.setInt("GOD", tds.getShort("GODINA"));
      jcbRealOlak.setSelected(crp.isRealolak());
    }
    private void setupCRP() {
      if (jcbCalc.isSelected()) {
	      crp.setCleanOdbici(jcbClean.isSelected());
	      crp.setGodina(inputData.getInt("GOD"));
	      crp.setLimit(inputData.getBigDecimal("LIMIT"));
	      crp.setRealolak(jcbRealOlak.isSelected());
	      crp.setPreview(jcbPreview.isSelected());
      }
    }
    public void pack() {
      super.pack();
      setSize(new Dimension(330,240));
    }
    public CalcRazPor getCRP() {
      return jcbCalc.isSelected()?crp:null;
    }
  }
}
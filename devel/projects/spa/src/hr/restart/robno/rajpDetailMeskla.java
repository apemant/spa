/****license*****************************************************************
**   file: rajpDetailMeskla.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTextField;
import hr.restart.swing.JraTextMultyKolField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class rajpDetailMeskla extends JPanel {

  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  String GOD ;
  String CSKLIZ;
  String CSKLUL;
  int CART;
  raMeskla rAM;
  tmpDataSets TDS = new tmpDataSets();
  TableDataSet tdsSet ;

  rapancart rpcart = new rapancart(1){
    public void nextTofocus(){}
    public void metToDo_after_lookUp(){
      if (rAM.raDetail.getMode() == 'B') return;
      enable_rest(true);
      rAM.findStanjaiCijene(true);
      jtfKOL.requestFocus();
      jtfKOL.selectAll();
    }
  };

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanelCenter = new JPanel();
  JLabel jLabel1 = new JLabel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JPanel jPanelStanje = new JPanel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  XYLayout xYLayout2 = new XYLayout();
  JTextField focusField = new JTextField();
//  JraTextField jtfKOL = new JraTextField();
	JraTextMultyKolField jtfKOL = new JraTextMultyKolField(){

		public void propertyChange(PropertyChangeEvent evt) {
			
			if (evt.getPropertyName().equalsIgnoreCase("KOL")) 
				rAM.jtfKOL_focusLost(null);
			
		}
        
        public void valueChanged() {
          rAM.jtfKOL_focusLost(null);
        }
     };	
    
  
  
  JLabel jLabel8 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JraTextField jraFC = new JraTextField() {
  	public void valueChanged() {
  		rAM.izlazCijena();
  	};
  };
  JraTextField jraZADRAZIZ = new JraTextField();
  JraTextField jraZADRAZUL = new JraTextField();
  JraTextField jraZCUL = new JraTextField()  {
  	public void valueChanged() {
  		rAM.ulazCijena();
  	};
  };
  JraTextField jraBEFKOLIZ = new JraTextField();
  JraTextField jraAFTKOLIZ = new JraTextField();
  JraTextField jraBEFKOLUL = new JraTextField();
  JraTextField jraAFTKOLUL = new JraTextField();
  JraTextField jraBEFVRIIZ = new JraTextField();
  JraTextField jraAFTVRIIZ = new JraTextField();
  JraTextField jraBEFVRIUL = new JraTextField();
  JraTextField jraAFTVRIUL = new JraTextField();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JLabel jLabel13 = new JLabel();
  JLabel jlZCUL = new JLabel();
  JLabel jlVRIUL = new JLabel();

  JLabel jPop = new JLabel();
  JLabel jCijena = new JLabel();
  JraTextField jraVC = new JraTextField();
  JraTextField jraPPOP = new JraTextField();
  
  public rajpDetailMeskla(raMeskla rAM) {
    this.rAM = rAM;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void BindMeskla(){
    jtfKOL.setDataSet(rAM.getDetailSet());
    jraFC.setDataSet(rAM.getDetailSet());
    jraVC.setDataSet(rAM.getDetailSet());
    jraPPOP.setDataSet(rAM.getDetailSet());
    jraZADRAZIZ.setDataSet(rAM.getDetailSet());
    jraZCUL.setDataSet(rAM.getDetailSet());
    jraZADRAZUL.setDataSet(rAM.getDetailSet());
  }

  private void jbInit() throws Exception {
   focusField.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfKOL.requestFocus();
      }
    });
   
   boolean dodm = frmParam.getParam("robno", "mesklaHack", "N",
       "Popust i izlazna cijena na meðuskladišnicama").equals("D");

    tdsSet = TDS.getMesklaTempSet();
    this.setLayout(borderLayout1);
//    rpcart.setPreferredSize(new Dimension(567, 100));
    this.setPreferredSize(new Dimension(645, dodm ? 325 : 300));
    jLabel1.setText("Koli\u010Dina");
    jPanelCenter.setLayout(xYLayout1);
    jPanelStanje.setBorder(BorderFactory.createEtchedBorder());
    jPanelStanje.setPreferredSize(new Dimension(100, 110));
    jPanelStanje.setLayout(xYLayout2);
    jPanelCenter.setBorder(BorderFactory.createEtchedBorder());
//    jPanelCenter.setMinimumSize(new Dimension(635, 90));
    jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel2.setText("Izlazno skladište");
    jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel3.setText("Ulazno skladište");
    jLabel4.setText("Prije");
    jLabel5.setText("Poslije");
    jLabel6.setText("Prije");
    jLabel7.setText("Poslije");
    jtfKOL.setColumnName("KOL");
    /*jtfKOL.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        rAM.jtfKOL_focusLost(e);
      }
    });*/

    jCijena.setText("Izlazna cijena");
    jPop.setText("Popust na izlazu");
    jraVC.setColumnName("FC");
    jraPPOP.setColumnName("PPOP");
    jLabel8.setText("Cijena");
    jLabel9.setText("Iznos");
    jraFC.setColumnName("ZC");
    jraZADRAZIZ.setColumnName("ZADRAZIZ");
    jraZADRAZUL.setColumnName("ZADRAZUL");
    jraZCUL.setColumnName("ZCUL");
    jlZCUL.setText("Ulazna cijena");
    jlVRIUL.setText("Ulazna vrijednost");
    jraBEFKOLIZ.setDataSet(TDS.getMesklaTempSet());
    jraBEFKOLIZ.setFont(new java.awt.Font("SansSerif", 1, 12));
    jraBEFKOLIZ.setColumnName("BEFKOLIZ");
    jraAFTKOLIZ.setDataSet(TDS.getMesklaTempSet());
    jraAFTKOLIZ.setFont(new java.awt.Font("SansSerif", 1, 12));
    jraAFTKOLIZ.setColumnName("AFTKOLIZ");
    jraBEFKOLUL.setDataSet(TDS.getMesklaTempSet());
    jraBEFKOLUL.setFont(new java.awt.Font("SansSerif", 1, 12));
    jraBEFKOLUL.setColumnName("BEFKOLUL");
    jraAFTKOLUL.setDataSet(TDS.getMesklaTempSet());
    jraAFTKOLUL.setFont(new java.awt.Font("SansSerif", 1, 12));
    jraAFTKOLUL.setColumnName("AFTKOLUL");
    jraBEFVRIIZ.setFont(new java.awt.Font("SansSerif", 1, 12));
    jraBEFVRIIZ.setColumnName("BEFVRIIZ");
    jraBEFVRIIZ.setDataSet(TDS.getMesklaTempSet());
    jraAFTVRIIZ.setFont(new java.awt.Font("SansSerif", 1, 12));
    jraAFTVRIIZ.setColumnName("AFTVRIIZ");
    jraAFTVRIIZ.setDataSet(TDS.getMesklaTempSet());
    jraBEFVRIUL.setFont(new java.awt.Font("SansSerif", 1, 12));
    jraBEFVRIUL.setColumnName("BEFVRIUL");
    jraBEFVRIUL.setDataSet(TDS.getMesklaTempSet());
    jraAFTVRIUL.setFont(new java.awt.Font("SansSerif", 1, 12));
    jraAFTVRIUL.setColumnName("AFTVRIUL");
    jraAFTVRIUL.setDataSet(TDS.getMesklaTempSet());
    jLabel10.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel10.setText("Koli\u010Dina");
    jLabel11.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel11.setText("Iznos");
    jLabel12.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel12.setText("Koli\u010Dina");
    jLabel13.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel13.setText("Iznos");
    this.add(jPanelCenter, BorderLayout.CENTER);
    jPanelCenter.add(jLabel1, new XYConstraints(15, 10, -1, -1));
    jPanelCenter.add(jtfKOL, new XYConstraints(150, 10, 130, -1));
    jPanelCenter.add(jLabel8, new XYConstraints(15, 35, -1, -1));
    jPanelCenter.add(jLabel9, new XYConstraints(320, 35, -1, -1));
    jPanelCenter.add(jraFC, new XYConstraints(150, 35, 130, -1));
    jPanelCenter.add(jraZADRAZIZ, new XYConstraints(480, 35, 130, -1));
    jPanelCenter.add(jlZCUL, new XYConstraints(15, 60, -1, -1));
    jPanelCenter.add(jlVRIUL, new XYConstraints(320, 60, -1, -1));
    jPanelCenter.add(jraZCUL, new XYConstraints(150, 60, 130, -1));
    jPanelCenter.add(jraZADRAZUL, new XYConstraints(480, 60, 130, -1));
    jPanelCenter.add(focusField, new XYConstraints(360, 501, 1, -1));
    if (dodm) {
      jPanelCenter.add(jCijena, new XYConstraints(15, 85, -1, -1));
      jPanelCenter.add(jPop, new XYConstraints(320, 85, -1, -1));
      jPanelCenter.add(jraVC, new XYConstraints(150, 85, 130, -1));
      jPanelCenter.add(jraPPOP, new XYConstraints(480, 85, 130, -1));
    }
    this.add(jPanelStanje, BorderLayout.SOUTH);
    jPanelStanje.add(jLabel2, new XYConstraints(60, 10, 255, -1));
    jPanelStanje.add(jLabel3, new XYConstraints(367, 10, 255, -1));
    jPanelStanje.add(jLabel4, new XYConstraints(15, 45, -1, -1));
    jPanelStanje.add(jLabel5, new XYConstraints(15, 70, -1, -1));
    jPanelStanje.add(jraAFTKOLIZ, new XYConstraints(65, 70, 120, -1));
    jPanelStanje.add(jraBEFKOLIZ, new XYConstraints(65, 45, 120, -1));
    jPanelStanje.add(jraAFTVRIIZ, new XYConstraints(190, 70, 130, -1));
    jPanelStanje.add(jraBEFVRIIZ, new XYConstraints(190, 45, 130, -1));
    jPanelStanje.add(jraBEFVRIUL, new XYConstraints(500, 45, 130, -1));
    jPanelStanje.add(jraAFTVRIUL, new XYConstraints(500, 70, 130, -1));
    jPanelStanje.add(jraBEFKOLUL, new XYConstraints(375, 45, 120, -1));
    jPanelStanje.add(jraAFTKOLUL, new XYConstraints(375, 70, 120, -1));
    jPanelStanje.add(jLabel7, new XYConstraints(330, 70, -1, -1));
    jPanelStanje.add(jLabel6, new XYConstraints(330, 45, -1, -1));
    jPanelStanje.add(jLabel10, new XYConstraints(60, 25, 120, -1));
    jPanelStanje.add(jLabel11, new XYConstraints(185, 25, 130, -1));
    jPanelStanje.add(jLabel12, new XYConstraints(365, 25, 120, -1));
    jPanelStanje.add(jLabel13, new XYConstraints(490, 25, 130, -1));
    this.add(rpcart, BorderLayout.NORTH);
  }

  public void InitRaPanCartDP(){
    // (ab.f)  promjenio dm.getStmeskla u rAM.getDetailSet() itd. 07-08-2002
    rpcart.setGodina(hr.restart.util.Valid.getValid().findYear(rAM.getMasterSet().getTimestamp("DATDOK")));
    rpcart.setCskl(rAM.getDetailSet().getString("CSKLIZ"));
    rpcart.setTabela(rAM.getDetailSet());
    rpcart.setDefParam();
    rpcart.setMode("DOH");
//    rpcart.setnextFocusabile(this.jtfKOL);
//    rpcart.setSearchable(false);
    rpcart.InitRaPanCart();
  }

  public void enable_rapancart(boolean istina){
    raCommonClass.getraCommonClass().EnabDisabAll(rpcart,istina);
    rpcart.EnabDisab(istina);
  }

  public void enable_rest(boolean istina){
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraFC,istina && rAM.allowNeg);
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraZCUL,istina && rAM.allowNeg);
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(this.jraZADRAZIZ,false);
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(this.jraZADRAZUL,false);
    hr.restart.util.raCommonClass.getraCommonClass().EnabDisabAll(this.jPanelStanje,false);
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jtfKOL,istina);
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraVC,istina);
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraPPOP,istina);
  }
}
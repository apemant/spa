/****license*****************************************************************
**   file: jpAkcijeMaster.java
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
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.swing.raDateMask;
import hr.restart.swing.raDateRange;
import hr.restart.swing.raNumberMask;
import hr.restart.swing.raTextMask;
import hr.restart.util.raCommonClass;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpAkcijeMaster extends JPanel {
    raCommonClass rcc = raCommonClass.getraCommonClass();

    frmAkcije fAkcije;
    
    XYLayout lay = new XYLayout();
    
    JraCheckBox jcbAKTIV = new JraCheckBox();
    JLabel jlCAK = new JLabel();
    JraTextField jraCAK = new JraTextField();
    JLabel jlNAZAK = new JLabel();
    JraTextField jraNAZAK = new JraTextField();
    
    JLabel jlPOP = new JLabel();
    JraTextField jraPOP = new JraTextField();
    
    JraCheckBox jraDatum = new JraCheckBox("Akcija od-do");
    JraCheckBox jraVrijeme = new JraCheckBox("Happy hour");
    JraCheckBox jraDani = new JraCheckBox("Dani tjedna");
    
    JraCheckBox jraPon = new JraCheckBox("Pon");
    JraCheckBox jraUto = new JraCheckBox("Uto");
    JraCheckBox jraSri = new JraCheckBox("Sri");
    JraCheckBox jraCet = new JraCheckBox("Èet");
    JraCheckBox jraPet = new JraCheckBox("Pet");
    JraCheckBox jraSub = new JraCheckBox("Sub");
    JraCheckBox jraNed = new JraCheckBox("Ned");
    
    //raButtonGroup bgr = new raButtonGroup();
    
    JLabel jlDatum = new JLabel();
    JraTextField jraDATOD = new JraTextField();
    JraTextField jraDATDO = new JraTextField();
    
    JLabel jlVrijeme = new JLabel();
    JraTextField jraVRIOD = new JraTextField();
    JraTextField jraVRIDO = new JraTextField();

    public jpAkcijeMaster(frmAkcije fa) {
      try {
        fAkcije = fa;
        jbInit();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public void BindComponents(DataSet ds) {
        jcbAKTIV.setDataSet(ds);
        jraCAK.setDataSet(ds);
        jraNAZAK.setDataSet(ds);
        jraPOP.setDataSet(ds);
        jraDATOD.setDataSet(ds);
        jraDATDO.setDataSet(ds);
        jraVRIOD.setDataSet(ds);
        jraVRIDO.setDataSet(ds);
    }

    private void jbInit() throws Exception {
        lay.setWidth(540);
        lay.setHeight(200);
        setLayout(lay);
        
        jlCAK.setText("Šifra");
        jraCAK.setColumnName("CAK");
        jlNAZAK.setText("Opis akcije");
        jraNAZAK.setColumnName("NAZAK");
        jlPOP.setText("Popust");
        jraPOP.setColumnName("PPOP");
        
        jcbAKTIV.setText("Aktivan");
        jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
        jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
        jcbAKTIV.setColumnName("AKTIV");
        jcbAKTIV.setSelectedDataValue("D");
        jcbAKTIV.setUnselectedDataValue("N");
        
        /*bgr.setDataSet(fAkcije.getMasterSet());
        bgr.setColumnName("TIP");
        bgr.setHorizontalAlignment(SwingConstants.LEFT);
        bgr.setHorizontalTextPosition(SwingConstants.TRAILING);
        bgr.add(jraDatum, "Akcija od-do", "A");
        bgr.add(jraVrijeme, "Happy hour", "H");*/
        
        jraDatum.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
            updateTip();
          }
        });
        
        jraVrijeme.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
            updateTip();
          }
        });
        
        jraDani.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
            updateTip();
          }
        });
        
        jlDatum.setText("Period (od - do)");
        jlVrijeme.setText("Vrijeme (od - do)");
        jraDATOD.setColumnName("DATOD");
        jraDATDO.setColumnName("DATDO");
        jraVRIOD.setColumnName("VRIOD");
        jraVRIDO.setColumnName("VRIDO");
        
        add(jlCAK, new XYConstraints(15, 20, -1, -1));
        add(jraCAK, new XYConstraints(150, 20, 100, -1));
        add(jcbAKTIV, new XYConstraints(415, 18, 100, -1));
        
        add(jlNAZAK, new XYConstraints(15, 45, -1, -1));
        add(jraNAZAK, new XYConstraints(150, 45, 365, -1));
        
        add(jlPOP, new XYConstraints(15, 70, -1, -1));
        add(jraPOP, new XYConstraints(150, 70, 100, -1));
        
        add(jraDatum, new XYConstraints(15, 100, 130, -1));
        add(jraDATOD, new XYConstraints(150, 100, 100, -1));
        add(jraDATDO, new XYConstraints(255, 100, 100, -1));
        
        add(jraVrijeme, new XYConstraints(15, 130, 130, -1));
        add(jraVRIOD, new XYConstraints(150, 130, 100, -1));
        add(jraVRIDO, new XYConstraints(255, 130, 100, -1));
        
        add(jraDani, new XYConstraints(15, 160, 130, -1));
        add(jraPon, new XYConstraints(150, 160, 45, -1));
        add(jraUto, new XYConstraints(200, 160, 45, -1));
        add(jraSri, new XYConstraints(250, 160, 45, -1));
        add(jraCet, new XYConstraints(300, 160, 45, -1));
        add(jraPet, new XYConstraints(350, 160, 45, -1));
        add(jraSub, new XYConstraints(400, 160, 45, -1));
        add(jraNed, new XYConstraints(450, 160, 45, -1));
        
        
        BindComponents(fAkcije.getMasterSet());
        
        new raDateRange(jraDATOD, jraDATDO);
        new raTextMask(jraVRIOD, 2, false, raTextMask.DIGITS);
        new raTextMask(jraVRIDO, 2, false, raTextMask.DIGITS);
    }
    
    public void updateTip() {
      if (fAkcije.raMaster.getMode() != 'B') {
        rcc.setLabelLaF(jraDATOD, jraDatum.isSelected());
        rcc.setLabelLaF(jraDATDO, jraDatum.isSelected());
        rcc.setLabelLaF(jraVRIOD, jraVrijeme.isSelected());
        rcc.setLabelLaF(jraVRIDO, jraVrijeme.isSelected());
        rcc.setLabelLaF(jraPon, jraDani.isSelected());
        rcc.setLabelLaF(jraUto, jraDani.isSelected());
        rcc.setLabelLaF(jraSri, jraDani.isSelected());
        rcc.setLabelLaF(jraCet, jraDani.isSelected());
        rcc.setLabelLaF(jraPet, jraDani.isSelected());
        rcc.setLabelLaF(jraSub, jraDani.isSelected());
        rcc.setLabelLaF(jraNed, jraDani.isSelected());
      }
    }
    
}


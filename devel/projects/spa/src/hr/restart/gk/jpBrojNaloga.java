/****license*****************************************************************
**   file: jpBrojNaloga.java
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
package hr.restart.gk;

import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.sysoutTEST;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpBrojNaloga extends JPanel implements com.borland.dx.dataset.DataSetAware {
sysoutTEST ST = new sysoutTEST(false);
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  XYLayout xYLay = new XYLayout();
  JLabel jlBrojNaloga = new JLabel();
  JLabel jlBROJNALOGA = new JLabel();
  Valid Vl = Valid.getValid();
  String knjig;
  String god;
  String cvrnal;
  String cvrnalText = "";
  private boolean detail=false;
  int rbr=0;
  int rbs=0;
  JPanel jpStat = new JPanel();
  XYLayout xYLayStat = new XYLayout();
  JLabel jlID = new JLabel();
  JLabel jlIP = new JLabel();
  JLabel jlKONTRIZNOS = new JLabel();
  JLabel jlSALDO = new JLabel();
  JraTextField jralID = new JraTextField();
  JraTextField jralIP = new JraTextField();
  JraTextField jralKONTRIZNOS = new JraTextField();
  JraTextField jralSALDO = new JraTextField();
  public jpBrojNaloga() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  public jpBrojNaloga(boolean detailC) {
    this();
    detail = detailC;
    if (detail) jlBrojNaloga.setText("Stavka naloga");
  }
  void jbInit() throws Exception {
    setLayout(xYLay);
    jlBrojNaloga.setText("Nalog");
    jlBROJNALOGA.setText("BROJNALOGA");
    jlBROJNALOGA.setFont(jlBROJNALOGA.getFont().deriveFont(Font.BOLD));
//    xYLay.setHeight(45);
    jpStat.setLayout(xYLayStat);
//    xYLayStat.setWidth(611);
//    xYLayStat.setHeight(45);
//
    jlID.setText("Duguje");
    jlIP.setText("Potražuje");
    jlKONTRIZNOS.setText("Kontrolni iznos");
    jlSALDO.setText("Saldo");

    jralIP.setHorizontalAlignment(SwingConstants.RIGHT);
    jralIP.setText("0.00");
//    jralIP.setBackground(Color.white);
//    jralIP.setFont(jralIP.getFont().deriveFont(Font.BOLD));
//    jralIP.setBorder(BorderFactory.createEtchedBorder());
    jralIP.setColumnName("IP");

//    jralID.setBackground(Color.white);
//    jralID.setBorder(BorderFactory.createEtchedBorder());
    jralID.setHorizontalAlignment(SwingConstants.TRAILING);
    jralID.setText("0.00");
//    jralID.setFont(jralID.getFont().deriveFont(Font.BOLD));
    jralID.setColumnName("ID");

//    jralKONTRIZNOS.setBackground(Color.white);
//    jralKONTRIZNOS.setBorder(BorderFactory.createEtchedBorder());
    jralKONTRIZNOS.setHorizontalAlignment(SwingConstants.RIGHT);
    jralKONTRIZNOS.setText("0.00");
//    jralKONTRIZNOS.setFont(jralKONTRIZNOS.getFont().deriveFont(Font.BOLD));
    jralKONTRIZNOS.setColumnName("KONTRIZNOS");

//    jralSALDO.setBackground(Color.white);
//    jralSALDO.setBorder(BorderFactory.createEtchedBorder());
    jralSALDO.setHorizontalAlignment(SwingConstants.RIGHT);
    jralSALDO.setText("0.00");
//    jralSALDO.setFont(jralSALDO.getFont().deriveFont(Font.BOLD));
    jralSALDO.setColumnName("SALDO");
//
    xYLayStat.setWidth(605);
    xYLayStat.setHeight(48);
    add(jlBrojNaloga,  new XYConstraints(15, 20, -1, -1));
    add(jlBROJNALOGA,   new XYConstraints(150, 20, -1, -1));
    jpStat.add(jlID,   new XYConstraints(15, 0, -1, -1));
    jpStat.add(jlIP,   new XYConstraints(365, 0, -1, -1));
    jpStat.add(jlKONTRIZNOS,  new XYConstraints(15, 25, -1, -1));
    jpStat.add(jlSALDO,   new XYConstraints(365, 25, -1, -1));
    jpStat.add(jralID,     new XYConstraints(150, 0, 100, -1));
    jpStat.add(jralIP,      new XYConstraints(500, 0, 100, -1));
    jpStat.add(jralKONTRIZNOS,    new XYConstraints(150, 25, 100, -1));
    jpStat.add(jralSALDO,   new XYConstraints(500, 25, 100, -1));
  }

  public void noviBrojNaloga(String _knjig,String _god, String _cvrnal) {
    if (_cvrnal == null) return;
    knjig = _knjig;
    god = _god;
    cvrnal = _cvrnal;
    noviBrojNaloga();
  }

  public void noviBrojNaloga(com.borland.dx.dataset.DataSet ds) {
    knjig = ds.getString("KNJIG");
    god = ds.getString("GOD");
    cvrnal = ds.getString("CVRNAL");
    rbr = ds.getInt("RBR");
    noviBrojNaloga();
  }

  public void noviBrojNaloga() {
    checkFields();
    rbr = gkQuerys.getMaxNaloziRBR(knjig,god,cvrnal) + 1;
    initJP();
  }

  public void noviBrojStavke() {
    checkFields();
    rbs = gkQuerys.getMaxGkstavkeradRBS(knjig,god,cvrnal,rbr) + 1;
    initJP();
  }
  public void noviBrojStavke_offline(com.borland.dx.dataset.DataSet stavke) {
    checkFields();
    rbs = getMaxGkstavkeradRBS_offline(stavke);
    initJP();
  }
  private int getMaxGkstavkeradRBS_offline(com.borland.dx.dataset.DataSet stavke) {
    int maxrbr = 0;
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    for (int i=0; i<stavke.getRowCount();i++) {
      stavke.getVariant("RBS",i,v);
      if (v.getInt() > maxrbr) maxrbr = v.getInt();
    }
    return maxrbr+1;
  }
  public boolean isLastNalog() {
    return gkQuerys.getMaxNaloziRBR(knjig,god,cvrnal) == rbr;
  }
  private void checkFields() {
    if (god == null) god = Vl.findYear(new java.sql.Timestamp(System.currentTimeMillis()));
    if (knjig == null) knjig = hr.restart.zapod.dlgGetKnjig.getKNJCORG(false);
    if (cvrnal == null) cvrnal = "";
    if (cvrnal.equals("")) {
      cvrnalText = "";
    } else {
      if (lookupData.getlookupData().raLocate(dm.getVrstenaloga(),new String[] {"CVRNAL"},new String[] {cvrnal})) {
        cvrnalText = dm.getVrstenaloga().getString("OPISVRNAL");
      } else {
        cvrnalText = "";
      }
    }
  }
  public static String getCNaloga(com.borland.dx.dataset.ReadRow ds) {
    return getCNaloga(ds.getString("KNJIG"), 
                      ds.getString("GOD"), 
                      ds.getString("CVRNAL"), 
                      ds.getInt("RBR"));
  }
  public static String getCNaloga(String _knjig, String _god, String _cvrnal, int _rbr) {
    //!!!!!!!!! REDUNDANCA SA getCNaloga();
    return _knjig+"-"+getBrojNalogaText(_god, _cvrnal, _rbr);
  }
  
  public static String getBrojNalogaText(String _god, String _cvrnal, int _rbr) {
    return _god+"-"+_cvrnal+"-"+Valid.getValid().maskZeroInteger(new Integer(_rbr),6);
  }
  
  public String getCNaloga() {
    return knjig+"-"+getBrojNalogaText();
  }

  public String getBrojNalogaText() {
    checkFields();
    String brojNalogaText = getBrojNalogaText(god, cvrnal, rbr);//god+"-"+cvrnal+"-"+Vl.maskZeroInteger(new Integer(rbr),6);
    if (detail) {
      brojNalogaText = brojNalogaText+" / "+ rbs;
      brojNalogaText = brojNalogaText+"     "+cvrnalText;
    }
    return brojNalogaText;
  }
/**
 * Zvati samo jednom i to sa mastersetom
 * @param ds
 */
  public void initJPStat(com.borland.dx.dataset.DataSet ds) {
    jralID.setDataSet(ds);
    jralIP.setDataSet(ds);
    jralKONTRIZNOS.setDataSet(ds);
    jralSALDO.setDataSet(ds);
    add(jpStat,new XYConstraints(0,45,610,50));
  }

  public void initJP() {
    jlBROJNALOGA.setText(getBrojNalogaText());
  }

  public void setDataSet(com.borland.dx.dataset.DataSet ds) {
    initJP(ds);
  }

  public com.borland.dx.dataset.DataSet getDataSet() {
    return null;
  }

  public void initJP(com.borland.dx.dataset.DataSet ds) {
    knjig = ds.getString("KNJIG");
    god = ds.getString("GOD");
    cvrnal = ds.getString("CVRNAL");
    rbr = ds.getInt("RBR");
    checkFields();
    initJP();
  }
  public void initJP(int rbs_) {
    rbs = rbs_;
    checkFields();
    initJP();
  }
  public void initJP(String _knjig,String _god, String _cvrnal, int _rbr) {
    if (_cvrnal == null) return;
    knjig = _knjig;
    god = _god;
    cvrnal = _cvrnal;
    rbr = _rbr;
    checkFields();
    initJP();
  }

  public void copyBroj(com.borland.dx.dataset.DataSet ds) {
    checkFields();
    ds.setString("KNJIG",knjig);
    ds.setString("GOD",god);
    ds.setString("CVRNAL",cvrnal);
    ds.setInt("RBR",rbr);
    if (detail) ds.setInt("RBS",rbs);
  }
}
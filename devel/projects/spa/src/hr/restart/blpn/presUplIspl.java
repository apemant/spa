/****license*****************************************************************
**   file: presUplIspl.java
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
package hr.restart.blpn;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class presUplIspl extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();
  sgStuff ss = sgStuff.getStugg();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCblag = new JLabel();
  JLabel jlPocdat = new JLabel();
  JLabel jlaCblag = new JLabel();
  JLabel jlaNaziv = new JLabel();
  JLabel jlaOznval = new JLabel();
  JraButton jbSelCblag = new JraButton();
  JraTextField jraPocdat = new JraTextField();
  JraTextField jraZavdat = new JraTextField();
  JraTextField invizKnjig = new JraTextField();
  JraTextField invizGod = new JraTextField();
  JraTextField invizStatus = new JraTextField();
  JlrNavField jlrCblag = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrOznval = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public presUplIspl() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setSelDataSet(dm.getBlagizv());
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(105);

    jlCblag.setText("Blagajna");
    jlPocdat.setText("Period");
    jlaCblag.setHorizontalAlignment(SwingConstants.CENTER);
    jlaCblag.setText("Šifra");
    jlaNaziv.setHorizontalAlignment(SwingConstants.CENTER);
    jlaNaziv.setText("Naziv");
    jlaOznval.setHorizontalAlignment(SwingConstants.CENTER);
    jlaOznval.setText("Valuta");

    jraPocdat.setDataSet(getSelDataSet());
    jraPocdat.setColumnName("DATOD");
    jraPocdat.setHorizontalAlignment(SwingConstants.CENTER);

    jraZavdat.setDataSet(getSelDataSet());
    jraZavdat.setColumnName("DATOD");
    jraZavdat.setHorizontalAlignment(SwingConstants.CENTER);

    jlrCblag.setColumnName("CBLAG");
    jlrCblag.setDataSet(getSelDataSet());
    jlrCblag.setColNames(new String[] {"OZNVAL", "NAZIV"});
    jlrCblag.setTextFields(new JTextComponent[] {jlrOznval, jlrNaziv});
    jlrCblag.setVisCols(new int[] {1, 2, 3}); /**@todo: Dodati visible cols za lookup frame */
    jlrCblag.setSearchMode(0);
    jlrCblag.setRaDataSet(frmBlag.getBlagajneKnjig());
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        jlrCblag.setRaDataSet(frmBlag.getBlagajneKnjig());
        jlrOznval.setRaDataSet(frmBlag.getBlagajneKnjig());
        jlrNaziv.setRaDataSet(frmBlag.getBlagajneKnjig());
      }
    });

    jlrCblag.setNavButton(jbSelCblag);

    jlrOznval.setColumnName("OZNVAL");
    jlrOznval.setNavProperties(jlrCblag);
    jlrOznval.setSearchMode(1);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCblag);
    jlrNaziv.setSearchMode(1);

    invizKnjig.setDataSet(dm.getNalozi());
    invizKnjig.setColumnName("KNJIG");
    invizKnjig.setVisible(false);
    invizKnjig.setEnabled(false);

    invizGod.setDataSet(getSelDataSet());
    invizGod.setColumnName("GODINA");
    invizGod.setVisible(false);
    invizGod.setEnabled(false);

    invizStatus.setDataSet(getSelDataSet());
    invizStatus.setColumnName("STATUS");
    invizStatus.setVisible(false);
    invizStatus.setEnabled(false);

    jpDetail.add(invizKnjig, new XYConstraints(0,0,0,0));
    jpDetail.add(invizGod, new XYConstraints(0,0,0,0));
    jpDetail.add(invizStatus, new XYConstraints(0,0,0,0));

    jpDetail.add(jbSelCblag, new XYConstraints(555, 40, 21, 21));
    jpDetail.add(jlCblag, new XYConstraints(15, 40, -1, -1));
    jpDetail.add(jlPocdat, new XYConstraints(15, 65, -1, -1));
    jpDetail.add(jlaCblag, new XYConstraints(151, 23, 48, -1));
    jpDetail.add(jlaNaziv, new XYConstraints(256, 23, 293, -1));
    jpDetail.add(jlaOznval, new XYConstraints(206, 23, 43, -1));
    jpDetail.add(jlrCblag,  new XYConstraints(150, 40, 50, -1));
    jpDetail.add(jlrNaziv,   new XYConstraints(255, 40, 295, -1));
    jpDetail.add(jlrOznval,   new XYConstraints(205, 40, 45, -1));
    jpDetail.add(jraPocdat, new XYConstraints(150, 65, 100, -1));
    jpDetail.add(jraZavdat, new XYConstraints(255, 65, 100, -1));
    addSelRange(jraPocdat,jraZavdat);
    this.setSelPanel(jpDetail);
  }
  private boolean firstin = true;
  public void SetFokus(){
    getSelRow().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG());
    if (firstin) {
//      getSelRow().deleteAllRows();
      jlrCblag.emptyTextFields();
      vl.getCommonRange(jraPocdat, jraZavdat);
      firstin = false;
    }
    if(getPSOwner() instanceof frmBlagIzv){
      getSelRow().setString("STATUS", "");
    }
    else{
      getSelRow().setString("STATUS", "U");
    }
    jlrCblag.requestFocus();
    jlrCblag.selectAll();
  }

  public boolean Validacija() {
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(getSelDataSet());
//    syst.prn(getSelRow());
    short god = Short.parseShort(vl.findYear(getSelRow().getTimestamp("DATOD-from")));   //vl.getToday()));
//    System.out.println("DATUM iz getSelDataSet() " + getSelDataSet().getTimestamp("DATOD"));
//    System.out.println("GODINA ---------->>> " + god);
    getSelRow().setShort("GODINA", god);
//    System.out.println("GODINA ---------->>> " + getSelRow().getShort("GODINA"));
    return !vl.isEmpty(jlrCblag);
  }
}

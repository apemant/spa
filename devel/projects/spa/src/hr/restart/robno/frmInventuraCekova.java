/****license*****************************************************************
**   file: frmInventuraCekova.java
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
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmInventuraCekova extends raUpitLite {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JPanel jp = new JPanel();
  hr.restart.robno.Util utRobno = hr.restart.robno.Util.getUtil();
  static QueryDataSet qds = new QueryDataSet();
  Valid vl = Valid.getValid();

  TableDataSet tds = new TableDataSet();

  private JlrNavField jlfCBanka = new JlrNavField(){
    public void after_lookUp() {
    }
  };

  private JlrNavField jlfNazBanke = new JlrNavField(){
    public void after_lookUp() {
      jlfCBanka.after_lookUp();
    }
  };

  private JraButton jbCBanka = new JraButton();
  JLabel jlBanka = new JLabel("Banka");

  private JlrNavField jrfCSKL = new JlrNavField(){
  };

  private JlrNavField jrfNAZSKL = new JlrNavField(){
  };
// TODO oğe kondor :)
  private JraButton jbCSKL = new JraButton();
  private JLabel jlSkladiste = new JLabel("Skladište");

  private JlrNavField jrfCORG = new JlrNavField(){
  };

  private JlrNavField jrfNAZORG = new JlrNavField(){
  };

  private JraButton jbCORG = new JraButton();
  private JLabel jlOrganizacija = new JLabel("Org. jedinica");


  private JraTextField jraOdNaplate = new JraTextField();
  private JLabel jlOdNaplate = new JLabel("Od datuma primitka");
  private JraTextField jraDoPrimitka = new JraTextField();
  private JLabel jlDoPrimitka = new JLabel("Do datuma naplate");

  XYLayout xYLayout1 = new XYLayout();

  static frmInventuraCekova fic;

  public frmInventuraCekova() {
    try {
      jbInit();
      fic = this;
    }
    catch (Exception ex){
    }
  }

  public static frmInventuraCekova getInstance() {
    if (fic==null) fic = new frmInventuraCekova();
    return fic;
  }

  public void componentShow() {
    tds.setTimestamp("odNaplate", Valid.getValid().findDate(false,0));
    tds.setTimestamp("doPrimitka", Valid.getValid().findDate(false,0));
    jrfCORG.setText(hr.restart.zapod.OrgStr.getKNJCORG());
    jrfCORG.forceFocLost();
    jrfCSKL.requestFocus();
  }

  public boolean Validacija(){
    if (vl.isEmpty(jrfCORG) || vl.isEmpty(jrfCSKL)) return false;
    return true;
  }

  public void okPress() {
    qds.close();
    String qStr = getQuery(tds.getString("CORG"), tds.getString("CSKL"),
                           utRobno.getTimestampValue(tds.getTimestamp("odNaplate"), utRobno.NUM_FIRST),
                           utRobno.getTimestampValue(tds.getTimestamp("doPrimitka"), utRobno.NUM_LAST),
                           tds.getString("CBANKA"));
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr));
    qds.open();
    if (qds.rowCount() == 0) setNoDataAndReturnImmediately();
    qds.first();

//    System.out.println("Rambo ti si car\n\n"+getQuery(tds.getString("CSKL"),
//                                             utRobno.getTimestampValue(tds.getTimestamp("odNaplate"), utRobno.NUM_FIRST),
//                                             utRobno.getTimestampValue(tds.getTimestamp("doPrimitka"), utRobno.NUM_LAST),
//                                             tds.getString("CBANKA")));

//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(qds);
  }

  public String getQuery(String corg, String cskl, String odNaplata, String doPrimitak, String banka)  {
    String bankaAdd="";
    if(!banka.equals("")) bankaAdd = " and rate.cbanka='"+banka+"'";

//    String datumAdd = " and rate.datdok >="+odNaplata+" and rate.datum <="+doPrimitak+" order by rate.datum, rate.datdok";
    String datumAdd = " and rate.datdok <= '"+hr.restart.util.Util.getUtil().getLastSecondOfDay(hr.restart.util.Util.getUtil().getLastDayOfYear(tds.getTimestamp("odNaplate")))+"' and rate.datum between "+odNaplata+" and "+doPrimitak+" order by rate.datum, rate.datdok";

    String qStr= "select '"+cskl+"' as cskl, rate.vrdok as vrdok, rate.cnacpl as nacplR, rate.god as god, rate.brdok as brdok, "+
                 "rate.broj_cek as sbc, rate.broj_trg as trg, rate.irata as iznos, rate.datum as datnap, rate.datdok as datprim, "+
                 "rate.cbanka as cbanka from rate, nacpl where rate.cskl in ('"+cskl+"','"+corg+"') "+
                 "and rate.cnacpl=nacpl.cnacpl and nacpl.fl_cek='D'"+bankaAdd+datumAdd;
    return qStr;
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }

  public void firstESC() {
    rcc.EnabDisabAll(jp,true);
    if(!jlfCBanka.getText().equals("")) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          jlfCBanka.setText("");
          jlfCBanka.forceFocLost();
          jlfCBanka.requestFocus();
        }
      });
    } else if(!jrfCSKL.getText().equals("")) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          jrfCSKL.setText("");
          jrfCSKL.forceFocLost();
          jrfCSKL.requestFocus();
        }
      });
    } else if(!jrfCORG.getText().equals("")) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          tds.setTimestamp("odNaplate", Valid.getValid().findDate(false,0));
          tds.setTimestamp("doPrimitka", Valid.getValid().findDate(false,0));
          jrfCORG.setText("");
          jrfCORG.forceFocLost();
          jrfCORG.requestFocus();
        }
      });
    }
  }

  public boolean runFirstESC() {
    if(!jrfCORG.getText().equals(""))
      return true;
    return false;
  }

  private void jbInit() throws Exception {
    tds.setColumns(new Column[] {dm.createTimestampColumn("odNaplate"), dm.createTimestampColumn("doPrimitka"),
                                 dm.createStringColumn("CBANKA",4), dm.createStringColumn("CSKL","Skladište",12), dm.createStringColumn("CORG","Organizacijska jedinica",12)});

    jlfCBanka.setColumnName("CBANKA");
    jlfCBanka.setDataSet(tds);
    jlfCBanka.setColNames(new String[] {"NAZIV"});
    jlfCBanka.setTextFields(new JTextComponent[] {jlfNazBanke});
    jlfCBanka.setVisCols(new int[] {0, 1});
    jlfCBanka.setSearchMode(0);
    jlfCBanka.setRaDataSet(dm.getBanke());
    jlfCBanka.setNavButton(jbCBanka);

    jlfNazBanke.setColumnName("NAZIV");
    jlfNazBanke.setNavProperties(jlfCBanka);
    jlfNazBanke.setSearchMode(1);

    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setDataSet(tds);
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setTextFields(new JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setVisCols(new int[] {0, 1});
    jrfCSKL.setSearchMode(0);
    jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jrfCSKL.setNavButton(jbCSKL);

    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setSearchMode(1);

    jrfCORG.setColumnName("CORG");
    jrfCORG.setDataSet(tds);
    jrfCORG.setColNames(new String[] {"NAZIV"});
    jrfCORG.setTextFields(new JTextComponent[] {jrfNAZORG});
    jrfCORG.setVisCols(new int[] {0, 1});
    jrfCORG.setSearchMode(0);
    jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jrfCORG.setNavButton(jbCORG);

    jrfNAZORG.setColumnName("NAZIV");
    jrfNAZORG.setNavProperties(jrfCORG);
    jrfNAZORG.setSearchMode(1);

    jraOdNaplate.setColumnName("odNaplate");
    jraOdNaplate.setDataSet(tds);
    jraOdNaplate.setHorizontalAlignment(SwingConstants.CENTER);

    jraDoPrimitka.setColumnName("doPrimitka");
    jraDoPrimitka.setDataSet(tds);
    jraDoPrimitka.setHorizontalAlignment(SwingConstants.CENTER);

    xYLayout1.setWidth(555);
    xYLayout1.setHeight(150);

    jp.setLayout(xYLayout1);

    this.setJPan(jp);
    
    jp.add(jlOrganizacija,   new XYConstraints(15, 15, -1, -1));
    jp.add(jrfCORG,     new XYConstraints(150, 15, 100, -1));
    jp.add(jrfNAZORG,    new XYConstraints(255, 15, 259, -1));
    jp.add(jbCORG,     new XYConstraints(519, 15, 21, 21));

    jp.add(jlSkladiste,   new XYConstraints(15, 40, -1, -1));
    jp.add(jrfCSKL,     new XYConstraints(150, 40, 100, -1));
    jp.add(jrfNAZSKL,    new XYConstraints(255, 40, 259, -1));
    jp.add(jbCSKL,     new XYConstraints(519, 40, 21, 21));
    
    jp.add(jlfCBanka,       new XYConstraints(150, 65, 100, -1));
    jp.add(jlfNazBanke,     new XYConstraints(255, 65, 260, -1));
    jp.add(jbCBanka,     new XYConstraints(519, 65, 21, 21));
    jp.add(jlBanka,   new XYConstraints(15, 65, -1, -1));
    
    jp.add(jraOdNaplate,     new XYConstraints(150, 90, 100, -1));
    jp.add(jlOdNaplate,     new XYConstraints(15, 90, -1, -1));
    
    jp.add(jraDoPrimitka,     new XYConstraints(150, 115, 100, -1));
    jp.add(jlDoPrimitka,     new XYConstraints(15, 115, -1, -1));

    this.addReport("hr.restart.robno.repListaInventureCekova","hr.restart.robno.repListaInventureCekova","ListaInventureCekova","Lista inventure èekova");
  }

  public static QueryDataSet getQDS() {
    return qds;
  }

  public java.sql.Timestamp getOdPrimljeno(){
    return tds.getTimestamp("odNaplate");
  }

  public java.sql.Timestamp getDoNaplate(){
    return tds.getTimestamp("doPrimitka");
  }
}
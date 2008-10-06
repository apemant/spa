/****license*****************************************************************
**   file: frmNorme.java
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

import hr.restart.sisfun.Asql;
import hr.restart.swing.JraTextField;
import hr.restart.util.sysoutTEST;

import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmNorme extends raMasterFakeDetailArtikl {
  _Main ma;
//  raCommonClass rcc = raCommonClass.getraCommonClass();
//  Valid vl = Valid.getValid();

  JPanel jPanel1 = new JPanel();
  JLabel jlKol = new JLabel();
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jraKol = new JraTextField();
  rapancart rpn = new rapancart();
//  raArtiklUnos rpn = new raArtiklUnos();

  private com.borland.dx.sql.dataset.QueryDataSet qdsFakeArtikl;

  String[] key = new String[] {"CARTNOR"};
  
  private static frmNorme instanceOfMe = null;
  
  public static frmNorme getInstance(){
    if (instanceOfMe == null) instanceOfMe = new frmNorme();
    return instanceOfMe;
  }
  
  public frmNorme() {
    try {
      jbInit();
      instanceOfMe = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'N') {
      rpn.EnabDisab(true);
    }
    if (mode == 'I') {
      rpn.EnabDisab(false);
    }
  }

  public void SetFokusMaster(char mode) {
    /*System.out.println(this.getMasterSet());
    System.out.println(part);
    part.saveChanges(); */
    if (mode == 'N') {
      rpn.EnabDisab(true);
      rpn.setCART();
    } else {
      rpn.setCART(this.getMasterSet().getInt("CARTNOR"));
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (rpn.getCART().equals("")) {
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Potrebno je izabrati artikl!", "Greška", JOptionPane.ERROR_MESSAGE);
      rpn.setCART();
      return false;
    }
    mast.setInt("CARTNOR", Integer.valueOf(rpn.getCART()).intValue());
    if (mode == 'N' && MasterNotUnique()) {
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Normativ ve\u0107 postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
      rpn.setCART();
      return false;
    }
    mast.setInt("CART", mast.getInt("CARTNOR"));
    mast.setString("CART1", rpn.getCART1());
    mast.setString("BC", rpn.getBC());
    mast.setString("NAZART", rpn.getNAZART());
    mast.post();
    return true;
  }

  public boolean canDeleteMaster() {
    return true;
  }

  public void SetFokusIzmjena() {
    jraKol.requestFocus();
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jraKol)) return false;
    return true;
  }

  public void ClearFields() {
//    jraKol.setText("");
    this.getDetailSet().setBigDecimal("KOL", _Main.nul);
  }

  public String CheckMasterKeySQLString() {
    return "SELECT * FROM norme WHERE cartnor = " + mast.getInt("CARTNOR");
  }

  private void jbInit() throws Exception {
    Asql.createMasterNorme(mast);

    this.setMasterSet(mast);
    this.setNaslovMaster("Normativi");
    this.setVisibleColsMaster(new int[] {Aut.getAut().getCARTdependable(1,2,3),4});
    this.setMasterKey(key);

    this.setDetailSet(dm.getNorme());
    this.setNaslovDetail("Stavke normativa");
    this.setVisibleColsDetail(new int[] {Aut.getAut().getCARTdependable(1,2,3),4,5,6});
    this.setDetailKey(key);

    jlKol.setToolTipText("");
    jlKol.setText("Koli\u010Dina");
    jraKol.setDataSet(this.getDetailSet());
    jraKol.setColumnName("KOL");
    jraKol.setHorizontalAlignment(SwingConstants.TRAILING);
    jPanel1.setLayout(xYLayout1);
    xYLayout1.setWidth(430);
    xYLayout1.setHeight(45);
    jPanel1.add(jlKol, new XYConstraints(15, 0, -1, -1));
    jPanel1.add(jraKol, new XYConstraints(150, 0, 100, -1));

    SetPanels(rpn, jPanel1, false);
    initRpn();
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repFormatNorme","hr.restart.robno.repFormatNorme","FormatNorme","Normirani artikli s normama");
  }

  protected void initRpcart() {
    //rpc.setGodina(hr.restart.util.Valid.getValid().findYear(dm.getDoku().getTimestamp("DATDOK")));
    //rpc.setCskl(dm.getStdoku().getString("CSKL"));
    rpc.setTabela(dm.getNorme());
    rpc.setMyAfterLookupOnNavigate(false);
    rpc.setBorder(null);
    super.initRpcart();
    rpc.setAllowUsluga(true);
  }

  private void initRpn() {
    rpn.setTabela(Aut.getAut().getFakeArtikl());
//    rpn.setTabela(mast);
    rpn.setMode("DOH");
    rpn.setDefParam();
    rpn.InitRaPanCart();
  }
  
  private QueryDataSet detailReportSet = null;
  private HashMap nazivljeNormativa = null;
  
  private void makeDetailReportSet(){
    nazivljeNormativa = new HashMap();
    detailReportSet = dm.getNorme();
    detailReportSet.open();
    
    QueryDataSet normativi = hr.restart.util.Util.getUtil().getNewQueryDataSet("SELECT cart, nazart FROM Artikli WHERE cart in (select distinct cartnor from norme)");
    for (normativi.first();normativi.inBounds();normativi.next()){
      nazivljeNormativa.put(normativi.getInt("CART")+"",normativi.getString("NAZART"));
      System.out.println(normativi.getString("NAZART")); //XDEBUG delete when no more needed
    }
    
    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
    st.prn(detailReportSet);
  }

  public void Funkcija_ispisa_master() {
    makeDetailReportSet();
    super.Funkcija_ispisa_master();
  }
  
  public QueryDataSet getRepSet(){
    return detailReportSet;
  }
  
  public HashMap getNaNorm() {
    return nazivljeNormativa;
  }
  
}

/****license*****************************************************************
**   file: frmCjenik.java
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

import hr.restart.baza.Akcije;
import hr.restart.baza.dM;
import hr.restart.sisfun.Asql;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.jpGetValute;

import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmCjenik extends raMasterFakeDetailArtikl {
  _Main ma;
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  static frmCjenik cjenik;

  JPanel jpPres = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JPanel jpCjenik = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JLabel jlPartner = new JLabel();
  
  protected String whatNow;
  public final String DUMMY = "_!X!_";
  
  JlrNavField jlrPart = new JlrNavField()/* {
    public void after_lookUp() {
      jlrSkl.requestFocus();
    }
  }*/;
  JlrNavField jlrNazPart = new JlrNavField()/* {
    public void after_lookUp() {
      jlrSkl.requestFocus();
    }
  }*/;
  JraButton jbSelPart = new JraButton();
  
  JlrNavField jlrSkl = new JlrNavField()/* {
    public void after_lookUp() {
      jlrPart.requestFocus();
    }
  }*/;
  JlrNavField jlrNazSkl = new JlrNavField()/* {
    public void after_lookUp() {
      jlrPart.requestFocus();
    }
  }*/;
  JraButton jbSelSkl = new JraButton();
  
  JlrNavField jlrAk = new JlrNavField();
  JlrNavField jlrNazak = new JlrNavField();
  JraButton jbAk = new JraButton();
  
  //TODO njuli added
  
  JlrNavField jlrOrg = new JlrNavField() /*{
    public void after_lookUp() {
      jlrPart.requestFocus();
    }
  }*/;
  JlrNavField jlrNazOrg = new JlrNavField() /*{
    public void after_lookUp() {
      jlrPart.requestFocus();
    }
  }*/;
  JraButton jbSelOrg = new JraButton();
  
  jpGetValute val = new jpGetValute() {
    public void afterGet_Val() {
      valutaChange();
      jraPromjena.requestFocus();
    }
    
    public void afterClearVal() {
      revertToKuna();
    }
  };
  
  
//  JLabel jlVal = new JLabel("Valuta / Teèaj");
//  JlrNavField jlrCval = new JlrNavField(){
//    public void after_lookUp() {
//      if (!jlrCval.getText().equals("")) valutaChange();
//    }
//  };
//  JlrNavField jlrOznVal = new JlrNavField(){
//    public void after_lookUp() {
//      if (!jlrOznVal.getText().equals("")) valutaChange();
//    }
//  };
//  JlrNavField jlrNazVal = new JlrNavField(){
//    public void after_lookUp() {
//      if (!jlrNazVal.getText().equals("")) valutaChange();
//    }
//  };
//  JraButton jbSelVal = new JraButton();
  
//  JraTextField jraJedval = new JraTextField();
//  JraTextField jraTecaj = new JraTextField();  
  
  JLabel jlSkl = new JLabel();
  JLabel jlOrg = new JLabel("Org. jedinica");
  JLabel jlPromjena = new JLabel();
  JraTextField jraPromjena = new JraTextField() {
    public void valueChanged() {
      promjenaUpdated();
    }
  };
  JraTextField jraVCKALDOM = new JraTextField() {
    public void valueChanged() {
      updateOnUpis();
    }
  };
  JraTextField jraVCKALVAL = new JraTextField();
  JraTextField jraVC = new JraTextField() {
    public void valueChanged() {
      VCUpdated();
    }
  };
  JraTextField jraMC = new JraTextField() {
    public void valueChanged() {
      MCUpdated();
    }
  };
  JLabel jlVCKALDOM = new JLabel();
  JLabel jlVCKALVAL = new JLabel();
  JLabel jlVC = new JLabel();
  JLabel jlMC = new JLabel();
  JLabel jlPosto = new JLabel();
  BigDecimal porezMul;

  /*
   * instanciranje dialoga za promjenu cijena više artikala odjednom.
   */
  dlgPromArt pa = new dlgPromArt(this.getJframe(), "Artikli"); //FIXME usage of this

  //TODO amo vidit ovo....
  
  String[] key = new String[] {"CORG", "CSKL", "CPAR"};
  String[] keyd = new String[] {"CORG", "CSKL", "CPAR", "CART"};
  
  
  
  

  /*
   * DataSet za privremenu pohranu svih artikala iz tablice stanja, koji
   * pripadaju nekom skladištu/godini.
   */
  QueryDataSet allArt;
  QueryDataSet repQDS = new QueryDataSet();
  
 

  public frmCjenik() {
    this("CSKL");
  }
  
  public frmCjenik(String what){
    System.out.println(what);
    whatNow = what;
    try {
      cjenik = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmCjenik getInstance() {
    return cjenik;
  }

  public QueryDataSet getrepQDS() {
    return repQDS;
  }

  public void enabAll() {
//    Timer t = new Timer();
//    rcc.setLabelLaF(jlrCval, true);
//    rcc.setLabelLaF(jlrNazVal, true);
//    rcc.setLabelLaF(jlrOznVal, true);
    
    if (whatNow.equalsIgnoreCase("CORG")){
      rcc.setLabelLaF(jraVCKALDOM,true);		
      rcc.setLabelLaF(jraVCKALVAL,true);
    }
    
    rcc.EnabDisabAll(val,true);
    rcc.setLabelLaF(val.jtTECAJ, false);
    rcc.setLabelLaF(jraPromjena, true);
    rcc.setLabelLaF(jraVC, true);
    rcc.setLabelLaF(jraMC, true);
    rcc.setLabelLaF(jlrAk, true);
    rcc.setLabelLaF(jlrNazak, true);
    rcc.setLabelLaF(jbAk, true);
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'I') {
      rcc.EnabDisabAll(this.getJPanelMaster(), false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      System.out.println("SetFokusMaster - WhatNow = '"+whatNow+"'");
//      sysoutTEST st = new sysoutTEST(false);
//      st.prn(this.getMasterSet());
      jlrPart.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jlrPart) || ((whatNow.equals("CSKL") && vl.isEmpty(jlrSkl)) || (whatNow.equals("CORG") && vl.isEmpty(jlrOrg))))
      return false;
    if (mode == 'N' && MasterNotUnique()) {
      jlrPart.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Cjenik ve\u0107 postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
      //System.out.println(this.getMasterSet().rowCount());

      return false;
    }
//    sysoutTEST st = new sysoutTEST(false);
//    st.prn(mast);
    System.out.println("ValidacijaMaster - WhatNow = '"+whatNow+"'");
    if (whatNow.equals("CSKL")) this.getMasterSet().setString("CORG",DUMMY);
    else if (whatNow.equals("CORG")) this.getMasterSet().setString("CSKL",DUMMY);
    
    mast.post();
//    st.prn(this.getMasterSet());
    return true;
  }

  public boolean canDeleteMaster() {
    return true;
  }

  public void refilterDetailSet() {
//    System.out.println(Aut.getAut().arrayString(raDetail.getJpTableView().getKeyColumns()));
    super.refilterDetailSet();
    rpc.setGodina(vl.findYear(vl.getToday()));
    rpc.setCskl(this.getMasterSet().getString("CSKL"));
  }

  public void EntryPointDetail(char mode) {
//    System.out.println("!!!!!entery point detail!!!!!  *OLD* cached vc = " + vcDom + " cached mc = " + mcDom);
    super.EntryPointDetail(mode);
    if (mode == 'N') {
      if (whatNow.equalsIgnoreCase("CORG")) 
        rcc.EnabDisabAll(jpDetail, true);
//      QueryDataSet tmpSht = hr.restart.util.Util.getUtil().getNewQueryDataSet("select OZNVAL from valute where strval = 'N'");
//      jlrCval.setText(tmpSht.getShort("CVAL")+"");
//      jlrCval.forceFocLost();
//      this.getDetailSet().setShort("CVAL",tmpSht.getShort("CVAL"));
//      this.getDetailSet().setBigDecimal("TECAJ", new BigDecimal("1.000000"));
    }
    if (mode == 'I') {
      if (rpc.AST.findStanje(vl.findYear(vl.getToday()), mast.getString("CSKL"), this.getDetailSet().getInt("CART"))) {
        vcDom = rpc.gettrenStanje().getBigDecimal("VC");
        mcDom = rpc.gettrenStanje().getBigDecimal("MC");
//        System.out.println("!!!!!entery point detail!!!!!  *NEW* cached vc = " + vcDom + " cached mc = " + mcDom);
      }
      if (this.getDetailSet().getBigDecimal("VC").signum() == 0) porezMul = new BigDecimal(1.);
      else porezMul = this.getDetailSet().getBigDecimal("MC")
                .divide(this.getDetailSet().getBigDecimal("VC"), 2, BigDecimal.ROUND_HALF_UP);
    }
  }

  public void SetFokusIzmjena() {
    //TODO odje ovo sredit.....
//    jlrCval.requestFocus();
    // initJP
    if (whatNow.equalsIgnoreCase("CORG")){
      rcc.setLabelLaF(jpCjenik,true);
      jraVCKALDOM.requestFocus();
    } else {
      val.jtOZNVAL.requestFocus();
    }
  }
  
  public void SetFokusDetail(char mode) {
    val.initJP(mode);
    
    super.SetFokusDetail(mode);
    if (mode == 'N') {
      if (whatNow.equalsIgnoreCase("CORG"))
        this.getDetailSet().setString("CSKL", DUMMY);
      else if (whatNow.equalsIgnoreCase("CSKL"))
        this.getDetailSet().setString("CORG", DUMMY);
    }
  }

  public boolean Validacija(char mode) {
//    sysoutTEST st = new sysoutTEST(false);
//    st.prn(this.getDetailSet());
    /*if (whatNow.equalsIgnoreCase("CSKL") && !rpc.AST.findStanje(vl.findYear(vl.getToday()),mast.getString("CSKL"),
        this.getDetailSet().getInt("CART"))) {
      JOptionPane.showMessageDialog(jpCjenik, "Nema artikla na stanju!", "Greška", JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART();
      EraseFields();
      return false;
    }*/
    
    if (this.getDetailSet().getString("OZNVAL").equals("")){
     System.err.println("Odje je greskija.... triba bi uguzit domacu valutu.... radim na tom... i jerryju :)");
     QueryDataSet tmpSht = hr.restart.util.Util.getUtil().getNewQueryDataSet("select OZNVAL from valute where strval = 'N'");
     this.getDetailSet().setString("OZNVAL", tmpSht.getString("OZNVAL"));
    }
    return true;
  }

  public void ClearFields() {
//    jraPromjena.setText("");
//    jraSVC.setText("");
//    jraVC.setText("");
//    jraMC.setText(""); 
    if (val.jtOZNVAL.getText().equals("")) {
      QueryDataSet tmpSht = hr.restart.util.Util.getUtil().getNewQueryDataSet("select OZNVAL from valute where strval = 'N'");
      this.getDetailSet().setString("OZNVAL", tmpSht.getString("OZNVAL"));
      val.jtOZNVAL.forceFocLost();
    }
    this.getDetailSet().setBigDecimal("POSTO", _Main.nul);
    this.getDetailSet().setBigDecimal("VCKALDOM", _Main.nul);
    this.getDetailSet().setBigDecimal("VCKALVAL", _Main.nul);
    this.getDetailSet().setBigDecimal("VC", _Main.nul);
    this.getDetailSet().setBigDecimal("MC", _Main.nul);
  }

  /*
   * Metoda koja se poziva prilikom izlaska iz rapancarta. Ako se unosi novi
   * slog, treba popuniti odgovaraju\u0107a polja sa podacima iz tablice stanja.
   */
  
  BigDecimal vcDom, mcDom;
  
  protected boolean rpcOut() {
    if (whatNow.equalsIgnoreCase("CORG")) {
      EraseFields();
      rcc.setLabelLaF(jpCjenik,true);

      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          jraVCKALDOM.requestFocus();
          jraVCKALDOM.selectAll();
        }
      });
      
      System.out.println("super.rpcOut() - "+ super.rpcOut());
      return super.rpcOut();
    }
    // provjeri ima li artikla na stanju
    if (!rpc.AST.findStanje(vl.findYear(vl.getToday()),mast.getString("CSKL"),
        this.getDetailSet().getInt("CART"))) {
//      JOptionPane.showMessageDialog(jpCjenik, "Nema artikla na stanju!","Greška",
//       JOptionPane.ERROR_MESSAGE);
      EraseFields();
      //Aut.getAut().handleRpcErr(rpc, "Nema artikla na stanju!");
      return super.rpcOut();
    }
    if (!super.rpcOut()) return false;
    
    vcDom = rpc.gettrenStanje().getBigDecimal("VC");
    mcDom = rpc.gettrenStanje().getBigDecimal("MC");

    this.getDetailSet().setBigDecimal("VCKALDOM", rpc.gettrenStanje().getBigDecimal("VC"));
    this.getDetailSet().setBigDecimal("VCKALVAL", rpc.gettrenStanje().getBigDecimal("VC"));
    this.getDetailSet().setBigDecimal("VC", rpc.gettrenStanje().getBigDecimal("VC"));
    this.getDetailSet().setBigDecimal("MC", rpc.gettrenStanje().getBigDecimal("MC"));
    this.getDetailSet().setBigDecimal("POSTO", ma.nul);
    if (this.getDetailSet().getBigDecimal("VC").signum() == 0) porezMul = new BigDecimal(1.);
    else porezMul = this.getDetailSet().getBigDecimal("MC")
                .divide(this.getDetailSet().getBigDecimal("VC"), 2, BigDecimal.ROUND_HALF_UP);
    
    val.jtOZNVAL.requestFocus();
    
    return true;
  }
 

  public String CheckMasterKeySQLString() {
    if(whatNow.equals("CSKL"))
      return "select * from cjenik where " +
      "cskl = '" + mast.getString("CSKL") + "' and " +
      "cpar = " + mast.getInt("CPAR") +
      " and corg != '"+DUMMY+"'";
    else 
      return "select * from cjenik where " +
      "corg = '" + mast.getString("CORG") + "' and " +
      "cpar = " + mast.getInt("CPAR")+
      " and cskl != '"+DUMMY+"'";
  }

  public void Funkcija_ispisa_detail() {
    String qstr = "";
    
    if (whatNow.equalsIgnoreCase("CSKL"))
      qstr = "SELECT cjenik.cpar, cjenik.corg, cjenik.cskl, cjenik.cart, cjenik.cart1, cjenik.bc, " +
      "cjenik.nazart, cjenik.jm, cjenik.vc, cjenik.mc, cjenik.oznval, '" +
      mast.getString("NAZPAR") + "' as nazpar, '" +
      mast.getString("NAZSKL") + "' as nazskl " +
      "FROM cjenik WHERE cjenik.cskl = '" + mast.getString("CSKL") + "' AND " +
      "cjenik.cpar = " + mast.getInt("CPAR");
    else 
      qstr = "SELECT cjenik.cpar, cjenik.corg, cjenik.cskl, cjenik.cart, cjenik.cart1, cjenik.bc, " +
      "cjenik.nazart, cjenik.jm, cjenik.vc, cjenik.mc, cjenik.oznval, '" +
      mast.getString("NAZPAR") + "' as nazpar, '" +
      mast.getString("NAZSKL") + "' as nazskl " +
      "FROM cjenik WHERE cjenik.cskl = '" + mast.getString("CSKL") + "' AND " +
      "cjenik.cpar = " + mast.getInt("CPAR");
    
    repQDS.close();
    repQDS.closeStatement();
    repQDS.setQuery(new QueryDescriptor(dm.getDatabase1(),qstr));
    repQDS.open();
    super.Funkcija_ispisa_detail();
  }

  public void Funkcija_ispisa_master() {
    String qstr = "";
    
    if (whatNow.equalsIgnoreCase("CSKL"))
    qstr = "SELECT cjenik.cpar, cjenik.corg, cjenik.cskl, cjenik.cart, cjenik.cart1, cjenik.bc, " +
      "cjenik.nazart, cjenik.jm, cjenik.vc, cjenik.mc, cjenik.oznval, partneri.nazpar, sklad.nazskl " +
      "FROM cjenik,partneri,sklad WHERE cjenik.cpar = partneri.cpar AND cjenik.cskl = sklad.cskl " +
      "and cjenik.cskl != '"+DUMMY+"'";
    else 
      qstr = "SELECT cjenik.cpar, cjenik.corg, Orgstruktura.naziv, cjenik.cskl, cjenik.cart, cjenik.cart1, cjenik.bc, " +
      "cjenik.nazart, cjenik.jm, cjenik.vc, cjenik.mc, cjenik.oznval, partneri.nazpar " +
      "FROM cjenik,partneri,Orgstruktura WHERE cjenik.cpar = partneri.cpar and cjenik.corg=Orgstruktura.corg " +
      "and cjenik.corg != '"+DUMMY+"'";
    
    
    
    
//    if (whatNow.equals("CSKL")) qstr += "and cjenik.corg = '"+DUMMY+"' ";
//    else if (whatNow.equals("CORG")) qstr += "and cjenik.cskl = '"+DUMMY+"' ";
    repQDS.close();
    repQDS.closeStatement();
    repQDS.setQuery(new QueryDescriptor(dm.getDatabase1(),qstr));
    repQDS.open();
    super.Funkcija_ispisa_master();
  }
  
  private void updateOnUpis(){
   try {
    if (whatNow.equalsIgnoreCase("CORG")){
       porezMul = this.getDetailSet().getBigDecimal("VCKALDOM").add(this.getDetailSet().getBigDecimal("VCKALDOM").multiply(new BigDecimal("0.23"))).divide(this.getDetailSet().getBigDecimal("VCKALDOM"), 2, BigDecimal.ROUND_HALF_UP);
       vcDom = this.getDetailSet().getBigDecimal("VCKALDOM");
       mcDom = this.getDetailSet().getBigDecimal("VCKALDOM").add(this.getDetailSet().getBigDecimal("VCKALDOM").multiply(new BigDecimal("0.23")));
       if (!val.jtOZNVAL.getText().equals("")){
        valutaChange();
        return;
       }
       this.getDetailSet().setBigDecimal("VCKALVAL",this.getDetailSet().getBigDecimal("VCKALDOM"));
       this.getDetailSet().setBigDecimal("VC",this.getDetailSet().getBigDecimal("VCKALDOM"));
       this.getDetailSet().setBigDecimal("MC",this.getDetailSet().getBigDecimal("VCKALDOM").add(this.getDetailSet().getBigDecimal("VCKALDOM").multiply(new BigDecimal("0.23"))));
       /*if (porezMul == null) porezMul = new BigDecimal("0.23");
       promjenaUpdated();*/
     }
  } catch (Exception e) {
    // TODO Auto-generated catch block
    System.err.println("jos jedan divajd baj ziro....");
    e.printStackTrace();
  }
  }

  private void promjenaUpdated() {
    try {
      double diff = this.getDetailSet().getBigDecimal("POSTO").doubleValue();
      double oldvc = this.getDetailSet().getBigDecimal("VCKALVAL").doubleValue();
      this.getDetailSet().setBigDecimal("VC", new BigDecimal(oldvc * (diff + 100) / 100).setScale(2, BigDecimal.ROUND_HALF_UP));
      this.getDetailSet().setBigDecimal("MC", this.getDetailSet().getBigDecimal("VC").multiply(porezMul).setScale(2, BigDecimal.ROUND_HALF_UP));
    } catch (Exception e) {
    }
  }

  private void VCUpdated() {
    try {
      double diff = this.getDetailSet().getBigDecimal("VC").doubleValue() /
                    this.getDetailSet().getBigDecimal("VCKALVAL").doubleValue() * 100 - 100;

      this.getDetailSet().setBigDecimal("POSTO", new BigDecimal(diff).setScale(2, BigDecimal.ROUND_HALF_UP));
      this.getDetailSet().setBigDecimal("MC", this.getDetailSet().getBigDecimal("VC").multiply(porezMul).setScale(2, BigDecimal.ROUND_HALF_UP));
    } catch (Exception e) {
    }
  }

  private void MCUpdated() {
    try {
      this.getDetailSet().setBigDecimal("VC", this.getDetailSet().getBigDecimal("MC").divide(porezMul, 2, BigDecimal.ROUND_HALF_UP));
      double diff = this.getDetailSet().getBigDecimal("VC").doubleValue() /
                    this.getDetailSet().getBigDecimal("VCKALVAL").doubleValue() * 100 - 100;

      this.getDetailSet().setBigDecimal("POSTO", new BigDecimal(diff).setScale(2, BigDecimal.ROUND_HALF_UP));
    } catch (Exception e) {
    }
  }

  /**
   * Metoda koja otvara prozor za zadavanje kriterija za promjenu cijena više
   * artikala odjednom. Bira se grupa artikala ili svi artikli sa skladišta.
   */
  private void getMultiple() {
    pa.show();//FIXME usage of this
    if (pa.isOK()) {//FIXME usage of this

      if (pa.getPromjena().signum() == 0) {//FIXME usage of this
        JOptionPane.showMessageDialog(jpCjenik,
        "Nema promjene cijena!", "Greška", JOptionPane.ERROR_MESSAGE);
      } else {
        if (getStanjeArt()) {
           if (updateAllArt() == 0) {
             JOptionPane.showMessageDialog(jpCjenik,
                "Sve cijene ve\u0107 prije promijenjene!", "Greška", JOptionPane.ERROR_MESSAGE);
          }
        } else {
          JOptionPane.showMessageDialog(jpCjenik,
          "Nema više artikala na stanju!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
      }
    }

    // ako je svim artiklima promijenjena cijena, simulira se pritisak buttona Prekid.
    if (pa.isAll()) {//FIXME usage of this
      this.raDetail.setLockedMode('0');
      this.raDetail.getOKpanel().jPrekid_actionPerformed();
    }
  }

  /*
   * Metoda za dohva\u0107anje svih artikala koji pripadaju nekom skladištu/godini,
   * iz tablice stanja. Rezultiraju\u0107i dataset se \u010Duva u allArt.
   */
  private boolean getStanjeArt() {
    vl.execSQL("select * from stanje where " +
               "cskl = '" + mast.getString("CSKL") + "' and " +
               "god = '" + vl.findYear(vl.getToday()) +"'");
    allArt = vl.RezSet;
    allArt.open();
    return (!allArt.isEmpty());
  }

  private int updateAllArt() {
    int count = 0;
    String cart;
    BigDecimal mcmul;

    // izra\u010Dunaj faktor kojim treba množiti cijene ( (promjena+100) / 100 )
    BigDecimal vcmul = (pa.getPromjena().add(new BigDecimal(100.))).divide(new BigDecimal(100.), 4, BigDecimal.ROUND_HALF_DOWN);//FIXME usage of this

    // listaj jedan po jedan artikl iz dataseta allArt i provjeri je li mu cijena ve\u0107 promijenjena
    for (allArt.first(); allArt.inBounds(); allArt.next()) {
      cart = "" + allArt.getInt("CART");
      if (!artNotUnique(cart)) {

        // proma\u0111i ovaj artikl u tablici Artikli (za dohva\u0107anje grupe kojoj pripada)
        ld.raLocate(dm.getArtikli(), "CART", cart);

        // ako artikl pripada odabranoj grupi, promijeni mu cijenu
        if (pa.isAll() || dm.getArtikli().getString("CGRART").equals(pa.getGrupart())) {//FIXME usage of this
          ++count;

          // popuni odgovaraju\u0107a polja u tablici stdoku
          Aut.getAut().copyArtFields(this.getDetailSet(), dm.getArtikli());
          if (allArt.getBigDecimal("VC").signum() == 0) mcmul = new BigDecimal(1.);
            else mcmul = allArt.getBigDecimal("MC").divide(allArt.getBigDecimal("VC"), 4, BigDecimal.ROUND_HALF_UP);
          this.getDetailSet().setBigDecimal("VCKALDOM",allArt.getBigDecimal("VC"));
          this.getDetailSet().setBigDecimal("VC",allArt.getBigDecimal("VC").multiply(vcmul).setScale(2, BigDecimal.ROUND_HALF_UP));
          this.getDetailSet().setBigDecimal("POSTO", pa.getPromjena());//FIXME usage of this
          this.getDetailSet().setBigDecimal("MC",this.getDetailSet().getBigDecimal("VC").multiply(mcmul).setScale(2, BigDecimal.ROUND_HALF_UP));

          // ubaci promjene u tablicu stdoku
          this.getDetailSet().saveChanges();

          // ubaci novi red u tablicu stdoku
          this.raDetail.Insertiraj();
        }
      }
    }
    EraseFields();
    vl.RezSet = null;
    return count;
  }
//TODO jbInit
  private void jbInit() throws Exception {
    
//    if (whatNow.equalsIgnoreCase("CSKL")){
//      key = new String[] {"CPAR", "CSKL"};
//      keyd = new String[] {"CPAR", "CSKL", "CART"};
//    } else if (whatNow.equalsIgnoreCase("CORG")) {
//      key = new String[] {"CPAR", "CORG"};
//      keyd = new String[] {"CPAR", "CORG", "CART"};
//    }
//    
    System.err.println("---------------------");
    for (int hah = 0; hah<key.length; hah++){
      System.err.println(key[hah]);
    }
    System.err.println("---------------------");
    for (int hah = 0; hah<keyd.length; hah++){
      System.err.println(keyd[hah]);
    }
    System.err.println("---------------------");
    
    /*
     * 
     * key; = new String[] {"CPAR", "CSKL", "CORG"};
     * keyd; = new String[] {"CPAR", "CSKL", "CORG", "CART"};
     * 
     * */

//    hr.restart.sisfun.raDelayWindow load = hr.restart.sisfun.raDelayWindow.show(this.raMaster.getContentPane());

    Asql.createMasterCjenik(mast,whatNow,DUMMY);
    
//    sysoutTEST st = new sysoutTEST(false);
//    st.prn(mast);

    this.setMasterSet(mast);
    this.setNaslovMaster("Cjenik");
    this.setVisibleColsMaster(new int[] {0,1,2,3});
    this.setMasterKey(key);

    this.setDetailSet(dm.getCjenik());
    this.setNaslovDetail("Artikli cjenika");
    this.setVisibleColsDetail(new int[] {Aut.getAut().getCARTdependable(3,4,5),6});
    this.setDetailKey(keyd);

    jlPartner.setText("Poslovni partner");
    xYLayout1.setWidth(576);
    xYLayout1.setHeight(137);
    jpCjenik.setLayout(xYLayout2);
    jpPres.setLayout(xYLayout1);
    jbSelPart.setText("...");
    jbSelSkl.setText("...");
    jlSkl.setText("Skladište");

    jlrPart.setColumnName("CPAR");
    jlrPart.setTextFields(new JTextComponent[] {jlrNazPart});
    jlrPart.setColNames(new String[] {"NAZPAR"});
    jlrPart.setSearchMode(0);
    jlrPart.setDataSet(this.getMasterSet());
    jlrPart.setRaDataSet(dm.getPartneri());
    jlrPart.setVisCols(new int[] {0,1});
    jlrPart.setNavButton(jbSelPart);

    jlrNazPart.setColumnName("NAZPAR");
    jlrNazPart.setNavProperties(jlrPart);
    jlrNazPart.setDataSet(this.getMasterSet());
    jlrNazPart.setSearchMode(1);

    jlrSkl.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrSkl.setColumnName("CSKL");
    jlrSkl.setTextFields(new JTextComponent[] {jlrNazSkl});
    jlrSkl.setColNames(new String[] {"NAZSKL"});
    jlrSkl.setSearchMode(0);
    jlrSkl.setDataSet(this.getMasterSet());
    jlrSkl.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jlrSkl.setVisCols(new int[] {0,1});
    jlrSkl.setNavButton(jbSelSkl);

    jlrNazSkl.setColumnName("NAZSKL");
    jlrNazSkl.setNavProperties(jlrSkl);
    jlrNazSkl.setDataSet(this.getMasterSet());
    jlrNazSkl.setSearchMode(1);

    
    //jlrAk.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrAk.setColumnName("CAK");
    jlrAk.setTextFields(new JTextComponent[] {jlrNazak});
    jlrAk.setColNames(new String[] {"NAZAK"});
    jlrAk.setSearchMode(0);
    jlrAk.setDataSet(this.getDetailSet());
    jlrAk.setRaDataSet(Akcije.getDataModule().copyDataSet());
    jlrAk.setVisCols(new int[] {0,1});
    jlrAk.setNavButton(jbAk);

    jlrNazak.setColumnName("NAZAK");
    jlrNazak.setNavProperties(jlrSkl);
    //jlrNazak.setDataSet(this.getMasterSet());
    jlrNazak.setSearchMode(1);
    
    //TODO njuli dodato
    
    
    jlrOrg.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrOrg.setColumnName("CORG");
    jlrOrg.setTextFields(new JTextComponent[] {jlrNazOrg});
    jlrOrg.setColNames(new String[] {"NAZIV"});
    jlrOrg.setSearchMode(0);
    jlrOrg.setDataSet(this.getMasterSet());
    jlrOrg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig()); // TODO handle orgJed iz skladista
    jlrOrg.setVisCols(new int[] {0,1});
    jlrOrg.setNavButton(jbSelOrg);

    jlrNazOrg.setColumnName("NAZIV");
    jlrNazOrg.setNavProperties(jlrOrg);
//    jlrNazOrg.setDataSet(this.getMasterSet());
    jlrNazOrg.setSearchMode(1);    
    
    jlPromjena.setText("Promjena cijene");
    xYLayout2.setWidth(645);
    xYLayout2.setHeight(235);
    jlVCKALDOM.setText("Kalk. cijena bez poreza");
    jlVCKALVAL.setText("Val. cijena bez poreza");
    jlVC.setText("Cijena bez poreza");
    jlMC.setText("Cijena s porezom");
    jlPosto.setHorizontalAlignment(SwingConstants.LEADING);
    jlPosto.setText("%");

    jraPromjena.setHorizontalAlignment(SwingConstants.LEADING);
    jraPromjena.setColumnName("POSTO");
    jraPromjena.setDataSet(this.getDetailSet());
    
    jraVCKALDOM.setHorizontalAlignment(SwingConstants.LEADING);
    jraVCKALDOM.setColumnName("VCKALDOM");
    jraVCKALDOM.setDataSet(this.getDetailSet());
    
    jraVCKALVAL.setHorizontalAlignment(SwingConstants.LEADING);
    jraVCKALVAL.setColumnName("VCKALVAL");
    jraVCKALVAL.setDataSet(this.getDetailSet());
    
    val.setRaDataSet(this.getDetailSet());
    val.setTecajVisible(true);
    val.setAlwaysSelected(true);
    val.setTecajEditable(false);
    val.setTecajDate(vl.getToday());
    val.setDefaultEntryEnabled(true);
    
    
//    jraJedval.setHorizontalAlignment(SwingConstants.LEADING);
//    jraJedval.setColumnName("JEDVAL");
//    jraJedval.setDataSet(dm.getValute());
//    
//    jraTecaj.setHorizontalAlignment(SwingConstants.LEADING);
//    jraTecaj.setColumnName("TECAJ");
//    jraTecaj.setDataSet(this.getDetailSet());
//    
//    jlrCval.setHorizontalAlignment(SwingConstants.TRAILING);
//    jlrCval.setColumnName("CVAL");
//    jlrCval.setTextFields(new JTextComponent[] {jlrOznVal,jlrNazVal});
//    jlrCval.setColNames(new String[] {"OZNVAL","NAZVAL"});
//    jlrCval.setSearchMode(0);
//    jlrCval.setDataSet(this.getMasterSet());
//    jlrCval.setRaDataSet(dm.getValute()); // TODO handle orgJed iz skladista
//    jlrCval.setVisCols(new int[] {1,2,4});s
//    jlrCval.setNavButton(jbSelVal);
//
//    jlrOznVal.setColumnName("OZNVAL");
//    jlrOznVal.setNavProperties(jlrCval);
//    jlrOznVal.setSearchMode(1);    
//
//    jlrNazVal.setColumnName("NAZVAL");
//    jlrNazVal.setNavProperties(jlrCval);
//    jlrNazVal.setSearchMode(1);    
    
    
    jraVC.setHorizontalAlignment(SwingConstants.LEADING);
    jraVC.setColumnName("VC");
    jraVC.setDataSet(this.getDetailSet());
    
    jraMC.setHorizontalAlignment(SwingConstants.LEADING);
    jraMC.setColumnName("MC");
    jraMC.setDataSet(this.getDetailSet());

    jpPres.add(jlPartner, new XYConstraints(15, 20, -1, -1));
    jpPres.add(jlrPart, new XYConstraints(150, 20, 100, -1));
    jpPres.add(jlrNazPart, new XYConstraints(255, 20, 280, -1));
    jpPres.add(jbSelPart, new XYConstraints(540, 20, 21, 21));
    
    
    if (whatNow.equalsIgnoreCase("CSKL")) {
      jpPres.add(jlrSkl, new XYConstraints(150, 45, 100, -1));
      jpPres.add(jlrNazSkl, new XYConstraints(255, 45, 280, -1));
      jpPres.add(jbSelSkl, new XYConstraints(540, 45, 21, 21));
      jpPres.add(jlSkl, new XYConstraints(15, 45, -1, -1));
    } else if (whatNow.equalsIgnoreCase("CORG")) {
      jpPres.add(jlrOrg, new XYConstraints(150, 45, 100, -1));
      jpPres.add(jlrNazOrg, new XYConstraints(255, 45, 280, -1));
      jpPres.add(jbSelOrg, new XYConstraints(540, 45, 21, 21));
      jpPres.add(jlOrg, new XYConstraints(15, 45, -1, -1));
    }
    
//    jpPres.add(jlrValuta, new XYConstraints(150, 95, 100, -1));
//    jpPres.add(jlrNazVal, new XYConstraints(255, 95, 280, -1));
//    jpPres.add(jbSelVal, new XYConstraints(540, 95, 21, 21));
//    jpPres.add(jlvaluta, new XYConstraints(15, 95, -1, -1));
  	
    jpCjenik.add(jraVCKALDOM, new XYConstraints(150, 20, 100, -1));
    jpCjenik.add(jlVCKALDOM, new XYConstraints(15, 20, -1, -1));
    
//    jpCjenik.add(jlVal, new XYConstraints(15, 45, -1, -1));
//    jpCjenik.add(jlrCval, new XYConstraints(150, 45, 47, -1));
//    jpCjenik.add(jlrOznVal, new XYConstraints(202, 45, 48, -1));
//    jpCjenik.add(jlrNazVal, new XYConstraints(255, 45, 200, -1));
//    jpCjenik.add(jbSelVal, new XYConstraints(610, 45, 21, 21));
//    jpCjenik.add(jraJedval, new XYConstraints(460, 45, 40, -1));
//    jpCjenik.add(jraTecaj, new XYConstraints(505, 45, 100, -1));
    
    jpCjenik.add(val, new XYConstraints(0, 45, -1, -1));
    
    jpCjenik.add(jraVCKALVAL, new XYConstraints(150, 95, 100, -1));
    jpCjenik.add(jlVCKALVAL, new XYConstraints(15, 95, -1, -1));

    jpCjenik.add(jlVC, new XYConstraints(15, 120, -1, -1));
    jpCjenik.add(jraVC, new XYConstraints(150, 120, 100, -1));

    jpCjenik.add(jlMC, new XYConstraints(15, 145, -1, -1));
    jpCjenik.add(jraMC, new XYConstraints(150, 145, 100, -1));
    
    jpCjenik.add(jlPromjena, new XYConstraints(390, 95, -1, -1));
    jpCjenik.add(jraPromjena, new XYConstraints(505, 95, 100, -1));
    jpCjenik.add(jlPosto, new XYConstraints(610, 95, -1, -1));
    
    jpCjenik.add(jlrAk, new XYConstraints(150, 170, 100, -1));
    jpCjenik.add(jlrNazak, new XYConstraints(255, 170, 280, -1));
    jpCjenik.add(jbAk, new XYConstraints(540, 170, 21, 21));
    jpCjenik.add(new JLabel("Akcija"), new XYConstraints(15, 170, -1, -1));

    this.SetPanels(jpPres, jpCjenik, true);
    this.raMaster.getRepRunner().addReport("repCjenik", "hr.restart.robno.repCjenik", "Cjenik", "Ispis svih cjenika");
    this.raDetail.getRepRunner().addReport("repCjenik", "hr.restart.robno.repCjenik", "Cjenik", "Ispis cjenika");
//    this.raMaster.getRepRunner().2ort("hr.restart.robno.repCjenik","Report - ispis svih cjenika",42);
//    this.raDetail.getRepRunner().addReport("hr.restart.robno.repCjenik","Report - ispis cjenika",42);
//    repCjenikTemplate rep = new repCjenikTemplate();
//    rep.LabelCijenabezporeza.setHeight("123");

//    rep.LabelCijenabezporeza.;
//    raReportTemplate rep = new repCjenikTemplate();
//    rep.setReportModifier(new ReportModifier() {
//      public void modify(raReportTemplate rt) {
//
//      }
//    });
/*    this.raDetail.getRepRunner().setModifier(new ReportModifier() {
      public void modify(IModel m) {
        new raReportCreator(m);
//        Aut.getAut().dumpModel(m, 0);
      }
    }); */

    //FIXME ovo ishendlat kad se napokon dogovorimo kak i sto sa cjenikom!!!
    
    /*this.raDetail.addKeyAction(new raKeyAction(java.awt.event.KeyEvent.VK_F7) {
      public void keyAction() {
        if (!rpcLostFocus && raDetail.getMode() == 'N')
          getMultiple();
      }
    });*/
    
    /*jraVCKALDOM.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        updateOnUpis();
      }
    });
    
    jraPromjena.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        promjenaUpdated();
      }
    });
    jraVC.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        VCUpdated();
      }
    });
    jraMC.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        MCUpdated();
      }
    });*/
//    load.close();
  }
  
  private void valutaChange(){ 
    try {
      System.out.println("TECAJ " + hr.restart.zapod.Tecajevi.getTecaj(vl.getToday(), val.jtOZNVAL.getText()));
      System.out.println("bulin... "+(hr.restart.zapod.Tecajevi.getTecaj(vl.getToday(), val.jtOZNVAL.getText()).compareTo(ma.nul) == 1));
      BigDecimal divizor = val.getTecajForReal();
      if (hr.restart.zapod.Tecajevi.getTecaj(vl.getToday(), val.jtOZNVAL.getText()).compareTo(ma.nul) == 0){
        divizor = new BigDecimal("1.00");
      }
      try {
      this.getDetailSet().setBigDecimal("VCKALVAL", this.getDetailSet().getBigDecimal("VCKALDOM").divide(divizor,2,BigDecimal.ROUND_HALF_UP));
      this.getDetailSet().setBigDecimal("VC", vcDom.divide(divizor,2,BigDecimal.ROUND_HALF_UP));
      this.getDetailSet().setBigDecimal("MC", mcDom.divide(divizor,2,BigDecimal.ROUND_HALF_UP));
      } catch (ArithmeticException ae){
        System.err.println("java.lang.ArithmeticException: BigInteger/BigDecimal divide by zero");
        System.err.println("NEMA TEÈAJA!!!");
      }
    } catch (Exception ee) {
    }
  }
  
  private void revertToKuna() {
    System.out.println("RIVRTAM.....");
    try {
      this.getDetailSet().setBigDecimal("VCKALVAL", vcDom);
      this.getDetailSet().setBigDecimal("VC", vcDom);
      this.getDetailSet().setBigDecimal("MC", mcDom);
    } catch (Exception ee) {
    }
  }

  protected void initRpcart() {
    /*rpc.setGodina(vl.findYear(vl.getToday()));
    rpc.setCskl(dm.getCjenik().getString("CSKL"));*/
    rpc.setTabela(dm.getCjenik());
    rpc.setBorder(BorderFactory.createEtchedBorder());
    super.initRpcart();
    rpc.setAllowUsluga(true);
    //rpc.setnextFocusabile(this.jraPromjena);
  }
  
  public String getCCS(){
    return whatNow;
  }
  
  public String getDUMMY(){
    return DUMMY;
  }

//  public void ExitPointDetail(char mode) {
//    System.out.println("EXIT POINT DETAIL!!! WTF is this, and when....");
//    super.ExitPointDetail(mode);
//  }
//
//  public void AfterCancelDetail() {
//    System.out.println("AFTER CANCEL DETAIL!!! WTF is this, and when....");
//    super.AfterCancelDetail();
//  }
}

/****license*****************************************************************
**   file: UpStanjeRobno.java
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

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.JraToggleButton;
import hr.restart.swing.raButtonGroup;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raUpitFat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Timestamp;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class UpStanjeRobno extends raUpitFat {

  // LOGIKA
  RaLogicStanjeSkladiste rlss = new RaLogicStanjeSkladiste();
  RaLogicStanjePromet rlsp = new RaLogicStanjePromet();
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  
  //DATASETOVI
  TableDataSet tds = new TableDataSet();
  QueryDataSet mainData = null;
  
  //GRAFIKA
  JPanel jp = new JPanel();
  JPanel jPanel3 = new JPanel();
  BorderLayout localBorderLayout = new BorderLayout();
  XYLayout localXYLayout = new XYLayout();

  JLabel jlStanje = new JLabel();
  JraTextField jtfStanjeNaDan = new JraTextField();
  JraTextField jtfStanjeZaGod = new JraTextField();
  JraRadioButton jrbNaDan = new JraRadioButton();
  
  protected JraRadioButton jrbSaStanja = new JraRadioButton();

  raButtonGroup rbgStanje = new raButtonGroup();

  JraToggleButton jtbKolicinaNula = new JraToggleButton();
  rapancskl rpcskl = new rapancskl(){
    public void findFocusAfter() {
      rpcart.setCskl(rpcskl.getCSKL());
      if (rpcart.getCART().length() == 0) {
        rpcart.setDefParam();
        rpcart.setCART();
      }
    }
  };
  rapancart rpcart = new rapancart() {
    public void nextTofocus(){
      if (jrbSaStanja.isSelected())
        jtfStanjeZaGod.requestFocus();
      else jtfStanjeNaDan.requestFocus();
    }
  };
  
  JraCheckBox jcbAll = new JraCheckBox();
  
  //MISC
  String defSKL = raUser.getInstance().getDefSklad();
  boolean podgrupe = false;
  private int[] viskol;
  
  //KONSTRUKTOR(I)
  
  private static UpStanjeRobno instanceOfMe = null;
  
  public static UpStanjeRobno getInstance() {
    if (instanceOfMe == null) instanceOfMe = new UpStanjeRobno();
    return instanceOfMe;
  }
  
  public UpStanjeRobno() {
    try {
      initializer();
      instanceOfMe = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  //INICIJALIZACIJA
  private void initializer() throws Exception {
    jp.setLayout(localBorderLayout);

    tds.setColumns(new Column[] {
        dm.createTimestampColumn("NADAN"),
        dm.createStringColumn("SLJED",10),
        dm.createStringColumn("GODINA",4)}
    );

    jtfStanjeNaDan.setHorizontalAlignment(SwingConstants.CENTER);
    jtfStanjeNaDan.setColumnName("NADAN");
    jtfStanjeNaDan.setDataSet(tds);
    jlStanje.setText("Stanje");

    jtfStanjeZaGod.setHorizontalAlignment(SwingConstants.CENTER);
    jtfStanjeZaGod.setColumnName("GODINA");
    jtfStanjeZaGod.setDataSet(tds);

    rpcskl.setRaMode('S');
    rpcart.setBorder(null);
    rpcart.setMode(new String("DOH"));
    rpcart.setSearchable(false);
    jPanel3.setLayout(localXYLayout);
    
    jcbAll.setText(" Prikazati i artikle troškova ");
    jcbAll.setHorizontalTextPosition(SwingConstants.TRAILING);
    jcbAll.setSelected(false);

    localXYLayout.setWidth(655);
    localXYLayout.setHeight(135);

    jtbKolicinaNula.setText("Kol. 0");
    jtbKolicinaNula.setIcon(raImages.getImageIcon(raImages.IMGX));
    jtbKolicinaNula.setSelectedIcon(raImages.getImageIcon(raImages.IMGOK));
    jtbKolicinaNula.setToolTipText("Prikaz artikala s kolièinom 0");
    jtbKolicinaNula.setFocusPainted(false);

    rbgStanje.setHorizontalAlignment(SwingConstants.LEFT);
    rbgStanje.setHorizontalTextPosition(SwingConstants.LEADING);
    rbgStanje.add(jrbNaDan,"Na dan");
    rbgStanje.add(jrbSaStanja,"Kumulativi u godini");

    jrbNaDan.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent e) {
        lightsCameraAction();
      }
    });
    jrbSaStanja.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent e) {
        lightsCameraAction();
      }
    });

    jp.add(jPanel3, BorderLayout.CENTER);
    jp.add(rpcskl, BorderLayout.NORTH);

    jPanel3.add(rpcart,     new XYConstraints(0, 0, 655, 75));
    jPanel3.add(jrbSaStanja, new XYConstraints(150, 72, -1, -1));
    jPanel3.add(jrbNaDan,  new XYConstraints(400, 72, -1, -1));
    jPanel3.add(jtfStanjeZaGod,    new XYConstraints(305, 75, 50, -1));
    jPanel3.add(jtfStanjeNaDan,     new XYConstraints(500, 75, 104, -1));
    jPanel3.add(jlStanje, new XYConstraints(15, 75, -1, -1));
    jPanel3.add(jtbKolicinaNula,    new XYConstraints(500,100,104,21));
    jPanel3.add(jcbAll,    new XYConstraints(150,100,300,-1));

    this.setJPan(jp);
    
    //INICIJALIZACIJA PODATAKA
    boolean cskl_corg = rdUtil.getUtil().getCSKL_CORG(defSKL, hr.restart.zapod.OrgStr.getKNJCORG());
    if(!dm.getSklad().isOpen())
      dm.getSklad().open();
    rpcskl.jrfCSKL.setRaDataSet(dm.getSklad());
    tds.open();
    tds.setString("SLJED",Aut.getAut().getCARTdependable("CART","CART1","BC"));
    jtbKolicinaNula.setSelected(false);
    rpcart.setDefParam();
    if(cskl_corg) {
      rpcskl.setCSKL(defSKL);
    }
    rpcart.EnabDisab(true);
    tds.setTimestamp("NADAN", vl.findDate(false,0));
    tds.setString("GODINA", Aut.getAut().getKnjigodRobno());
    rbgStanje.setSelected(jrbSaStanja);
    lightsCameraAction();
    
    if(!cskl_corg)
      rpcskl.jrfCSKL.requestFocusLater();
    else rpcart.setCARTLater();
  }
  
  protected void handleReports() {
    try {
      killAllReports();
    } catch (Exception e) {
      System.err.println("ne postoje custom reporti");
    }
    if (!rpcskl.getCSKL().equals(""))
      this.addReport("hr.restart.robno.repStanje", "hr.restart.robno.repStanje", "Stanje", "Ispis stanja");
    else {
      this.addReport("hr.restart.robno.repStanjeSkl", "hr.restart.robno.repStanje", "StanjeSkl", "Ispis stanja po skladištima");
      this.addReport("hr.restart.robno.repStanjeArt", "hr.restart.robno.repStanje", "StanjeArt", "Ispis usporednog stanja artikala");
    }
  }

  public DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }
  
  public Timestamp getDatum(){
    return tds.getTimestamp("NADAN");
  }
  
  public String getCskl(){
   return rpcskl.getCSKL(); 
  }
  
  public boolean saStanja(){
    return jrbSaStanja.isSelected();
  }

  public String navDoubleClickActionName(){
    return "Kartica artikla";
  }

  public int[] navVisibleColumns() {
    return viskol; 
  }

  public void okPress() {
    if (rpcskl.getCSKL().equals(""))
      viskol = new int[]{0, 1, 2, 3, 4, 9};
    else
      viskol = new int[]{1, 2, 3, 4, 9};
    
    String parart = "";
    String parcart1 = "";

    if (rpcart.getCART().equals("") && rpcart.getCART1().equals("") && !rpcart.getNAZART().equals("")) parart = rpcart.getNAZART();
    else if (rpcart.getCART().equals("") && !rpcart.getCART1().equals("") && rpcart.getNAZART().equals("")) parcart1 = rpcart.getCART1();
    
    if (jrbSaStanja.isSelected()) {
      handleReports();
      mainData = rlss.datasetZaEkran(rpcskl.getCSKL(), rpcart.findCART("stanje"), rpcart.getCGRART(), podgrupe, tds.getString("GODINA"), kol0(),parart,parcart1,jcbAll.isSelected());

      if (mainData == null)
        setNoDataAndReturnImmediately();
      
      setTitle("Stanje artikala za godinu "+tds.getString("GODINA")+".");

      this.getJPTV().addTableModifier(new SignalColorModifier());
      setDataSetAndSums(mainData, new String[]{"NAB", "MAR", "POR", "VRI"});
    } else {
      handleReports();
      mainData = rlsp.datasetZaEkran(rpcskl.getCSKL(), rpcart.getCART(), rpcart.getCGRART(), rpcart.findCART("stanje"), /*tds.getString("GODINA"),*/ kol0(), tds.getTimestamp("NADAN"), podgrupe, parart, parcart1, jcbAll.isSelected());
      if (mainData == null)
        setNoDataAndReturnImmediately();
      
      setTitle("Stanje artikala na dan "+Aus.formatTimestamp(tds.getTimestamp("NADAN")));
      
      this.getJPTV().addTableModifier(new SignalColorModifier());
      setDataSetAndSums(mainData, new String[]{"NAB", "MAR", "POR", "VRI"});
    }
  }

  public void firstESC(){
    System.out.print("FIRST ESC : ");
    if ((rpcart.findCART().equals("")) && this.getJPTV().getDataSet()==null && !isInterrupted()) {
      System.out.println("(rpcart.findCART().equals(\"\")) && this.getJPTV().getDataSet()==null");
      rpcart.EnabDisab(true);
      rcc.EnabDisabAll(jPanel3, true);
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.setCSKL("");
    } else {
      System.out.println("else");
      rpcart.EnabDisab(true);
      rcc.EnabDisabAll(jPanel3, true);
      rcc.EnabDisabAll(rpcskl,true);
      lightsCameraAction();
      //tds.setTimestamp("NADAN", vl.findDate(false,0));
      this.getJPTV().clearDataSet();

      removeNav();
      setTitle("Stanje artikala");

      rpcart.setCART();
      rpcskl.jrfCSKL.requestFocus();
      
    }
  }

  public boolean runFirstESC() {
    if (rpcskl.getCSKL().equals("") && this.getJPTV().getDataSet() == null) {
      return false;
    } else {
      return true;
    }
  }

  public void componentShow() {
    this.getJPTV().clearDataSet();
    if (rpcskl.getCSKL().equalsIgnoreCase("")) {
      rpcskl.setCSKL(raUser.getInstance().getDefSklad());
    }
  }


  public boolean Validacija(){
    if (!rpcart.getCGRART().equals("") && (rpcart.getCART().equals("") && rpcart.getCART1().equals(""))){
      int grupe = JOptionPane.showConfirmDialog(this.jp,"Ukljuèiti i podgrupe?","Grupe artikala",JOptionPane.YES_NO_OPTION);
      if (grupe == JOptionPane.CANCEL_OPTION) {return false;}
      if (grupe == JOptionPane.NO_OPTION) {podgrupe = false;}
      else {podgrupe = true;}
    }
    return true;
  }

  private void lightsCameraAction(){
    rcc.setLabelLaF(jtfStanjeZaGod,jrbSaStanja.isSelected());
    rcc.setLabelLaF(jtfStanjeNaDan,jrbNaDan.isSelected());
  }
  
  private boolean kol0(){
    System.out.println("kolicina nula ukljuceno - " + jtbKolicinaNula.isSelected()); //XDEBUG delete when no more needed
    return jtbKolicinaNula.isSelected();
  }

  /*int nTransData=0;
  java.sql.Timestamp nTransDate, nTransDateOd;
  boolean nTransDonos = false;
  String nTransCskl="";*/

  public void jptv_doubleClick() {
    _Main.getStartFrame().showFrame("hr.restart.robno.upKartica", 
        15, res.getString("upFrmKartica_title"), false);
    if (jrbNaDan.isSelected()){
      upKartica.getupKartica().setOutsideData(
          rpcskl.getCSKL(), getJPTV().getDataSet().getInt("CART"),
          ut.getFirstDayOfYear(tds.getTimestamp("NADAN")),
          tds.getTimestamp("NADAN"));
    } else {
      upKartica.getupKartica().setOutsideData(
          rpcskl.getCSKL(), getJPTV().getDataSet().getInt("CART"),
          ut.getYearBegin(tds.getString("GODINA")),
          ut.getToday(ut.getYearBegin(tds.getString("GODINA")), 
              ut.getYearEnd(tds.getString("GODINA"))));
    }
    _Main.getStartFrame().showFrame("hr.restart.robno.upKartica", 
        res.getString("upFrmKartica_title"));
  }
  
  
  public void cancelPress() {
    super.cancelPress();
    setTitle("Stanje artikala");
    upKartica.getupKartica().clearOutsideData();
  }


  class SignalColorModifier extends raTableModifier {
    Variant v = new Variant();
    boolean signal = false;
    boolean coloring = false;
    
    public SignalColorModifier() {
      super();
      setColorParam();
    }

    public boolean doModify() {

      if (getTable() instanceof JraTable2) {
        JraTable2 tab = (JraTable2) getTable();
        if (tab.getDataSet().getRowCount() > 0 && tab.getDataSet().hasColumn("SIGMIN") != null) {
          tab.getDataSet().getVariant("SIGMIN", this.getRow(), v);
          if (v.getInt() == 1)signal = true;
          else signal = false;
          return (v.getInt() == 1 || v.getInt() == 2);
        }
      }
      return false;
    }
    
    public void setColorParam() {
      coloring = frmParam.getFrmParam().getParam("robno","stanjeAlert","D",
          "Bojanje stanja u ovisnosti o sig. i min. kol " +
          "D - šareni prikaz, N - monotoni prikaz").equals("D");
    }
    
    public void modify(){
      if (coloring){
      Color colorS = hr.restart.swing.raColors.yellow;//Color.green.darker().darker();
        Color colorN = hr.restart.swing.raColors.red;//Color.red;
        JComponent jRenderComp = (JComponent) renderComponent;
        if (isSelected()) {
          if (v.getInt() == 1) {
            jRenderComp.setBackground(colorS);
            jRenderComp.setForeground(Color.black);
          } else if (v.getInt() == 2) {
            jRenderComp.setBackground(colorN);
            jRenderComp.setForeground(Color.black);
          } else {
            jRenderComp.setBackground(getTable().getSelectionBackground());
            jRenderComp.setForeground(getTable().getSelectionForeground());
          }
        } else {
          if (v.getInt() == 1) {
            jRenderComp.setForeground(Color.yellow.darker().darker());
          } else if (v.getInt() == 2) {
            jRenderComp.setForeground(Color.red);
          } else {
            jRenderComp.setForeground(getTable().getForeground());
          }
        }
      }
    }
  }
}

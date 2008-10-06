/****license*****************************************************************
**   file: frmIsplataAgenta.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmIsplataAgenta extends hr.restart.util.raUpitFat {

  private boolean isFirstEscOver=true;
  private String status = "SVI";
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();  
  private QueryDataSet qdsAll = new QueryDataSet();
  private QueryDataSet qdsPojed = new QueryDataSet();
  private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  private hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  private JPanel panel_za_upit = new JPanel();
  private JLabel jlCAG = new JLabel("Agent");
  private JraButton jbCAG = new JraButton();
  private JlrNavField jrfCAG = new JlrNavField();
  private JlrNavField jrfNAZAG = new JlrNavField();

  private boolean isFromAllAgents = false;
  private HashMap hm = new HashMap();

  private JLabel jlSklad = new JLabel("Skladište");
  private JraButton jbsklad = new JraButton();
  private JlrNavField jrfCSKL = new JlrNavField();
  private JlrNavField jrfNAZSKL = new JlrNavField();

  private JLabel jlCorg = new JLabel("Org. jedinica");
  private JraButton jbcorg = new JraButton();
  private JlrNavField jrfCORG = new JlrNavField();
  private JlrNavField jrfNAZORG = new JlrNavField();
  private TableDataSet tds = new TableDataSet();
  private JraTextField jtfPocDatum = new JraTextField();
  private JraTextField jtfZavDatum = new JraTextField();

  private JraComboBox JCstatus = new JraComboBox(new String[]{"Svi","Neisplaæeni","Isplaæeni"});
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  /*raNavAction rnvRAC = new raNavAction("Raèuni",raImages.IMGSTAV,java.awt.event.KeyEvent.VK_F6) {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        if (jrfCAG.getText().length() == 0){
          jptv_doubleClick();
        } else jptv_doubleClickA();
      }
    };*/
  raNavAction rnvUPL = new raNavAction("Uplata",raImages.IMGCHANGE,java.awt.event.KeyEvent.VK_F4) {
    public void actionPerformed(java.awt.event.ActionEvent e) {
      azuriranjeNaplate();
    }
  };
  raNavAction rnvPonUPL = new raNavAction("Ponistavanje uplate",raImages.IMGDELETE,java.awt.event.KeyEvent.VK_F3) {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        ponUPL();
      }
    };

  /*raNavAction rnvPonIsp = new raNavAction("Ispis",raImages.IMGPRINT,java.awt.event.KeyEvent.VK_F5) {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        ispis();
      }
    };*/

  /*raNavAction rnvTogleall = new raNavAction("Okreni odabir",raImages.IMGALIGNJUSTIFY,java.awt.event.KeyEvent.VK_A,java.awt.event.KeyEvent.CTRL_MASK) {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        togle_ctrl_A();
      }
  };*/

  public boolean isIspis() {
    return false;
  }

  protected boolean createNavBar(){
    return true;
  }

  public void componentShow(){
    tds.open();
    tds.setTimestamp("pocDatum",hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(
                                hr.restart.util.Valid.getValid().findYear())));
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));

    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jrfCSKL.setText("");
    jrfCSKL.emptyTextFields();
    jrfCORG.setText("");
    jrfCORG.emptyTextFields();
    jrfCAG.setText("");
    jrfCAG.emptyTextFields();
    JCstatus.setSelectedIndex(0);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jrfCSKL.requestFocus();
      }
    });
  }
  public void firstESC(){
    getJPTV().uninstallSelectionTracker();
    mantancereports(isFromAllAgents);
    if (isFromAllAgents) {
      isFromAllAgents=false;
      fromHash();
      // TODO
      /*this.getJPTV().setDataSetAndSums(qdsAll,new String[] {"PROV","PLAC","SALDO"});
      removeNavs();
      addAllAgent();
      getJPTV().getColumnsBean().initialize();
      getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);*/
      if (!"".equalsIgnoreCase(selectidColumn)) {
//System.out.println("selectidColumn "+selectidColumn);
        getJPTV().getColumnsBean().setComboSelectedItem(selectidColumn);
      }
      return;
    }
    if (isFirstEscOver) {
      this.cancelPress();
    } else {
//      this.getJPTV().clearDataSet();
      setDataSet(null);
      removeNav();
      //this.getJPTV().getColumnsBean().initialize();
      rcc.EnabDisabAll(panel_za_upit, true);
      rcc.setLabelLaF(this.okp.jBOK,true);
      componentShow();
      isFirstEscOver= true;
    }
  }
  
  public void cancelPress(){
    /*this.getJPTV().setDataSet(null);
    removeNavs();
    this.getJPTV().getColumnsBean().initialize();
      getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);*/
    rcc.EnabDisabAll(panel_za_upit, true);
    rcc.setLabelLaF(this.okp.jBOK,true);
    isFirstEscOver=true;
    isFromAllAgents=false;
    super.cancelPress();
  }

  public boolean runFirstESC(){
    return true;
  }
  public void setUpQDS() {

    Column cskl = dm.getDoki().getColumn("CSKL").cloneColumn();
    cskl.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    Column god = dm.getDoki().getColumn("GOD").cloneColumn();
    god.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    Column brdok = dm.getDoki().getColumn("BRDOK").cloneColumn();
    Column vrdok = dm.getDoki().getColumn("VRDOK").cloneColumn();
    vrdok.setWidth(3);
    Column datdok = dm.getDoki().getColumn("DATDOK").cloneColumn();
    datdok.setWidth(8);
    Column cagent = dm.getDoki().getColumn("CAGENT").cloneColumn();
    cagent.setCaption("Šifra");
    cagent.setColumnName("CAGENT");
    cagent.setWidth(3);
    Column naziv = dm.getAgenti().getColumn("NAZAGENT").cloneColumn();
    naziv.setCaption("Naziv");
    naziv.setColumnName("NAZAGENT");
    naziv.setWidth(16);
    Column fakt = dM.createBigDecimalColumn("RAC", "Raèuni", 2);
    fakt.setWidth(8);
    Column nap = dM.createBigDecimalColumn("NAP", "Naplaæeni", 2);
    nap.setWidth(8);
    Column post = dM.createBigDecimalColumn("POST", "%", 2);
    post.setWidth(4);
    Column prov = dm.getDoki().getColumn("UIRAC").cloneColumn();
    Column iznos = dM.createBigDecimalColumn("IZNOS", "Iznos raèuna", 2);
    Column napl = dM.createStringColumn("STATNAP", "Naplaæen", 1);
    prov.setCaption("Provizija");
    prov.setColumnName("PROV");
    prov.setWidth(8);
    Column plac = dm.getDoki().getColumn("PLATITI").cloneColumn();
    plac.setCaption("Isplaæeno");
    plac.setColumnName("PLAC");
    plac.setWidth(8);
    Column broj = dm.getDoki().getColumn("PNBZ2").cloneColumn();
    broj.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    Column saldo = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    saldo.setCaption("Za isplatiti");
    saldo.setColumnName("SALDO");
    saldo.setPersist(false);
    saldo.setWidth(8);

    qdsAll.setColumns(new Column[]{cagent,naziv,fakt,nap,post,prov,plac,saldo});
    qdsAll.open();
    qdsPojed.setColumns(new Column[]{cskl,vrdok,god,brdok,datdok,broj,iznos,napl,
            post.cloneColumn(),prov.cloneColumn(),plac.cloneColumn(),saldo.cloneColumn()});

    qdsPojed.open();
  }

  public void mantancereports(boolean isAll){
    this.killAllReports();

    if (isAll)
      this.addReport("hr.restart.robno.repIsplataAgenataZbirno", "hr.restart.robno.repIsplataAgenataZbirno", "IsplataAgenataZbirno", "Ispis totala");
    else {
      this.addReport("hr.restart.robno.repIsplataAgenta", "hr.restart.robno.repIsplataAgenataZbirno", "IsplataAgenta", "Ispis agenta");
    }
  }

  public void okPress(){
    getJPTV().uninstallSelectionTracker();
    mantancereports(jrfCAG.getText().equalsIgnoreCase(""));
    rcc.setLabelLaF(this.okp.jBOK,false);

    isFirstEscOver=false;
    findKartica();
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(tds); // tds ispitivanje

  }

  private static frmIsplataAgenta inst;

  public frmIsplataAgenta() {
    keySupport.setNavContainer(getJPTV().getNavBar().getNavContainer());
    setUpQDS();
    tds.setColumns(new Column[] {
                   dm.createTimestampColumn("pocDatum", "Poèetni datum"),
                   dm.createTimestampColumn("zavDatum", "Završni datum"),
                   dm.createBigDecimalColumn("UPLATA", "Uplata",2),
                   dm.createTimestampColumn("DATUPL","Datum Uplate")});

    jrfCAG.setColumnName("CAGENT");
    jrfCAG.setColNames(new String[] {"NAZAGENT"});
    jrfCAG.setVisCols(new int[]{0,1,2});
    jrfCAG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZAG});
    jrfCAG.setRaDataSet(dm.getAgenti());
    jrfNAZAG.setColumnName("NAZAGENT");
    jrfNAZAG.setSearchMode(1);
    jrfNAZAG.setNavProperties(jrfCAG);
    jrfCAG.setNavButton(jbCAG);

    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setVisCols(new int[]{0,1,2});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setRaDataSet(dm.getSklad());
    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jbsklad.setText("...");
    jrfCSKL.setNavButton(jbsklad);

    jrfCORG.setColumnName("CORG");
    jrfCORG.setColNames(new String[] {"NAZIV"});
    jrfCORG.setVisCols(new int[]{0,1,2});
    jrfCORG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZORG});
    jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());//dm.getOrgstruktura());
    jrfNAZORG.setColumnName("NAZIV");
    jrfNAZORG.setSearchMode(1);
    jrfNAZORG.setNavProperties(jrfCORG);
    jbcorg.setText("...");
    jrfCORG.setNavButton(jbcorg);

    /*JCstatus.addItemListener(new java.awt.event.ItemListener(){
      public void itemStateChanged (java.awt.event.ItemEvent i){
        if (i.getItem().equals("Svi")){
          status="SVI";
        } else if (i.getItem().equals("Neplaæeno")){
          status = "NE";
        } else if (i.getItem().equals("Plaæeno")){
          status = "DA";
        }
      }});*/

    XYLayout xyl = new XYLayout();
    xyl.setWidth(700);
    xyl.setHeight(140);
    panel_za_upit.setLayout(xyl);

    panel_za_upit.add(jlSklad,new XYConstraints(15,15,-1,-1));
    panel_za_upit.add(jrfCSKL,new XYConstraints(150,15,100,-1));
    panel_za_upit.add(jrfNAZSKL,new XYConstraints(255,15,350,-1));
    panel_za_upit.add(jbsklad,new XYConstraints(610,15,21,21));

    panel_za_upit.add(jlCorg,new XYConstraints(15,40,-1,-1));
    panel_za_upit.add(jrfCORG,new XYConstraints(150,40,100,-1));
    panel_za_upit.add(jrfNAZORG,new XYConstraints(255,40,350,-1));
    panel_za_upit.add(jbcorg,new XYConstraints(610,40,21,21));

    panel_za_upit.add(jlCAG,new XYConstraints(15,65,-1,-1));
    panel_za_upit.add(jrfCAG,new XYConstraints(150,65,100,-1));
    panel_za_upit.add(jrfNAZAG,new XYConstraints(255,65,350,-1));
    panel_za_upit.add(jbCAG,new XYConstraints(610,65,21,21));

    panel_za_upit.add(new JLabel("Status"),new XYConstraints(15,90,-1,-1));
    panel_za_upit.add(JCstatus, new XYConstraints(150, 90, 100, -1));

    panel_za_upit.add(new JLabel("Datum (od-do)"),     new XYConstraints(15, 115, -1, -1));
    panel_za_upit.add(jtfPocDatum, new XYConstraints(150, 115, 100, -1));
    panel_za_upit.add(jtfZavDatum, new XYConstraints(255, 115, 100, -1));
    this.setJPan(panel_za_upit);
    initMiniPanel();
    inst = this;

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
  }

  public static frmIsplataAgenta getInstance(){
    if (inst == null) inst = new frmIsplataAgenta();
    return inst;
  }

  private HashSet pokDok = new HashSet();
  void findPokriveni() {
    pokDok.clear();
    DataSet sk = Skstavke.getDataModule().getTempSet("BROJDOK",
        Aus.getKnjigCond().and(Condition.equal("VRDOK", "IRN")).and(
            Condition.equal("POKRIVENO", "D")));
    if (!raProcess.isRunning()) sk.open();
    else raProcess.openScratchDataSet(sk);
    for (sk.first(); sk.inBounds(); sk.next())
      pokDok.add(sk.getString("BROJDOK"));
  }

  private BigDecimal agentProv = new BigDecimal(0);

  private void findKartica(){

    VarStr sql = new VarStr();
    sql.append("SELECT MAX(doki.cagent) as cagent, MAX(doki.datdok) as datdok, " +
        "MAX(doki.vrdok) as vrdok, MAX(doki.god) as god, MAX(doki.brdok) as brdok, " +
        "MAX(doki.provisp) as plac, MAX(doki.cskl) as cskl, " +
        "MAX(doki.provpost) as provpost, SUM(stdoki.iprodbp) as iprodbp, " +
        "MAX(doki.pnbz2) as pnbz2, MAX(doki.statpla) as statpla FROM doki,stdoki "+
        "WHERE "+Util.getUtil().getDoc("doki", "stdoki")+" AND iprodbp != 0");

    if (jrfCAG.getText().length() > 0)
      sql.append(" AND cagent = "+jrfCAG.getText());

    String sklad = jrfCSKL.getText();
    String corg = jrfCORG.getText();
    if (sklad.length() == 0 && corg.length() == 0)
      sql.append(" AND doki.vrdok in ('ROT','RAC','GOT','GRN')");
    else if (corg.length() == 0)
      sql.append(" AND doki.vrdok in ('ROT', 'GOT') AND doki.cskl='"+sklad+"'");
    else if (sklad.length() == 0)
      sql.append(" AND doki.vrdok in ('RAC', 'GRN') AND doki.cskl='"+corg+"'");
    else sql.append(" AND ((doki.vrdok in ('ROT', 'GOT') AND doki.cskl='"+sklad+"') OR ("+
        "doki.vrdok in ('RAC', 'GRN') AND doki.cskl='"+corg+"'))");

    sql.append(" AND ").append(Condition.between("dvo",
        tds.getTimestamp("pocDatum"), tds.getTimestamp("zavDatum")));
    sql.append(" GROUP BY doki.cskl, doki.vrdok, doki.god, doki.brdok ORDER BY cagent, datdok");

    boolean svi = jrfCAG.getText().length() == 0;

    if (svi) {
      qdsAll.emptyAllRows();
      /*qdsAll.getColumn("CPAR").setVisible(com.borland.jb.util.TriStateProperty.TRUE);
      qdsAll.getColumn("PNBZ2").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
      qdsAll.getColumn("DATDOSP").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
      qdsAll.getColumn("DVO").setVisible(com.borland.jb.util.TriStateProperty.FALSE);*/
    } else {
      qdsPojed.emptyAllRows();
      /*qdsPojed.getColumn("CPAR").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
      qdsPojed.getColumn("PNBZ2").setVisible(com.borland.jb.util.TriStateProperty.TRUE);
      qdsPojed.getColumn("DATDOSP").setVisible(com.borland.jb.util.TriStateProperty.TRUE);*/
    }

    findPokriveni();

    System.out.println("sqlPitanje " + sql);

    boolean fakt = true, agentFound = true;
    BigDecimal postprov = new BigDecimal(0);
    BigDecimal zero = new BigDecimal(0);
    int lastAgent = -2;

    if (!svi) {
      fakt = false;
      if (lookupData.getlookupData().raLocate(dm.getAgenti(),"CAGENT", jrfCAG.getText())) {
        fakt = dm.getAgenti().getString("VRSTAPROV").equalsIgnoreCase("F");
        agentProv = postprov = dm.getAgenti().getBigDecimal("POSTOPROV");
      }
    }
    String[] ccols = {"CSKL", "VRDOK", "GOD", "BRDOK", "DATDOK", "PNBZ2", "PLAC"};
    QueryDataSet tmpqds = hr.restart.util.Util.getNewQueryDataSet(sql.toString(), true);
    for (tmpqds.first();tmpqds.inBounds();tmpqds.next()){
      if (svi) {
        if (lastAgent != tmpqds.getInt("CAGENT")) {
          fakt = agentFound = false;
          if (agentFound = lookupData.getlookupData().raLocate(dm.getAgenti(),
                  "CAGENT",String.valueOf(lastAgent  = tmpqds.getInt("CAGENT")))) {
            qdsAll.insertRow(true);
            qdsAll.setInt("CAGENT", lastAgent);
            qdsAll.setString("NAZAGENT",dm.getAgenti().getString("NAZAGENT"));
            fakt = dm.getAgenti().getString("VRSTAPROV").equalsIgnoreCase("F");
            qdsAll.setBigDecimal("POST", postprov = dm.getAgenti().getBigDecimal("POSTOPROV"));
          }
        }
        if (agentFound) {
          BigDecimal plac = tmpqds.getBigDecimal("PLAC");
          BigDecimal osnova = zero;
          boolean placen = tmpqds.getString("STATPLA").equals("D") ||
                            pokDok.contains(tmpqds.getString("PNBZ2"));
          if (plac.signum() != 0) postprov = tmpqds.getBigDecimal("PROVPOST");
          else postprov = dm.getAgenti().getBigDecimal("POSTOPROV");
          if (fakt || placen) osnova = tmpqds.getBigDecimal("IPRODBP");
          BigDecimal prov = Util.getUtil().findIznos(osnova, postprov);
          qdsAll.setBigDecimal("RAC", qdsAll.getBigDecimal("RAC").add(tmpqds.getBigDecimal("IPRODBP")));
          if (placen) qdsAll.setBigDecimal("NAP", qdsAll.getBigDecimal("NAP").add(tmpqds.getBigDecimal("IPRODBP")));
          qdsAll.setBigDecimal("PROV",qdsAll.getBigDecimal("PROV").add(prov));
          qdsAll.setBigDecimal("PLAC",qdsAll.getBigDecimal("PLAC").add(plac));
          qdsAll.setBigDecimal("SALDO",qdsAll.getBigDecimal("SALDO").add(prov).subtract(plac));
          qdsAll.setBigDecimal("POST", Util.getUtil().findPostotak(
              qdsAll.getBigDecimal(fakt ? "RAC" : "NAP"), qdsAll.getBigDecimal("PROV")));
        }
      } else {
        BigDecimal osnova = zero;
        boolean placen = tmpqds.getString("STATPLA").equals("D") ||
                          pokDok.contains(tmpqds.getString("PNBZ2"));
        if (fakt || placen) osnova = tmpqds.getBigDecimal("IPRODBP");
        if (osnova.signum() != 0 || qdsPojed.getBigDecimal("PLAC").signum() != 0) {
          qdsPojed.insertRow(true);
          dm.copyColumns(tmpqds,qdsPojed,ccols);
          if (qdsPojed.getBigDecimal("PLAC").signum() == 0)
            postprov = dm.getAgenti().getBigDecimal("POSTOPROV");
          else postprov = tmpqds.getBigDecimal("PROVPOST");
          qdsPojed.setBigDecimal("IZNOS", tmpqds.getBigDecimal("IPRODBP"));
          qdsPojed.setString("STATNAP", placen ? "D" : "N");
          qdsPojed.setBigDecimal("POST", postprov);
          qdsPojed.setBigDecimal("PROV", Util.getUtil().findIznos(osnova, postprov));
          qdsPojed.setBigDecimal("SALDO", qdsPojed.getBigDecimal("PROV").subtract(qdsPojed.getBigDecimal("PLAC")));
        }
      }
    }
//    removeNavs();
    if (svi) {
      qdsAll.setTableName("all");
      qdsAll.setSort(new SortDescriptor(new String[] {"NAZAGENT"}));
      qdsAll.first();
      setDataSetAndSums(qdsAll,new String[] {"PROV","PLAC","SALDO"});
      //addAllAgent();
    } else {
      qdsPojed.setTableName("single");
      qdsPojed.setSort(new SortDescriptor(new String[] {"DATDOK"}));
      qdsPojed.first();
      setDataSetAndSums(qdsPojed,new String[] {"PROV","PLAC","SALDO"});
      //addPojed();
    }

/*    getJPTV().getColumnsBean().initialize();
    getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);*/
    if (svi) {
      getJPTV().installSelectionTracker(new String[] {"CAGENT"});
    }
  }
  
  protected void addNavBarOptions(){ 
    super.addNavBarOptions();
      if (jrfCAG.getText().length() == 0) {
        if (getJPTV().getNavBar().contains(rnvUPL))
          getJPTV().getNavBar().removeOption(rnvUPL);
        if (getJPTV().getNavBar().contains(rnvPonUPL))
          getJPTV().getNavBar().removeOption(rnvPonUPL);
      } else {
        if (!getJPTV().getNavBar().contains(rnvUPL))
          getJPTV().getNavBar().addOption(rnvUPL, 0);
        if (!getJPTV().getNavBar().contains(rnvPonUPL))
          getJPTV().getNavBar().addOption(rnvPonUPL, 1);
      }
    }

    public void navbarremoval(){
      super.navbarremoval();
      if (getJPTV().getNavBar().contains(rnvUPL))
        this.getJPTV().getNavBar().removeOption(rnvUPL);
      if (getJPTV().getNavBar().contains(rnvPonUPL))
        this.getJPTV().getNavBar().removeOption(rnvPonUPL);
    }
  
  /*
  private void removeNavs(){
    getJPTV().getNavBar().removeOption(rnvUPL);
    getJPTV().getNavBar().removeOption(rnvPonUPL);
    getJPTV().getNavBar().removeOption(rnvRAC);
    getJPTV().getNavBar().removeOption(rnvPonIsp);
    //getJPTV().getNavBar().removeOption(rnvTogleall);
    getJPTV().getNavBar().unregisterNavBarKeys(this);
  }
  private void addAllAgent(){
    getJPTV().getNavBar().addOption(rnvRAC,0);
    getJPTV().getNavBar().addOption(rnvPonIsp,1);
    //getJPTV().getNavBar().addOption(rnvTogleall,2);
    getJPTV().getNavBar().registerNavBarKeys(this);
  }
  private void addPojed(){
    getJPTV().getNavBar().addOption(rnvUPL,0);
    getJPTV().getNavBar().addOption(rnvPonUPL,1);
    getJPTV().getNavBar().addOption(rnvRAC,2);
    getJPTV().getNavBar().addOption(rnvPonIsp,3);
    getJPTV().getNavBar().registerNavBarKeys(this);
  }
*/
  private JPanel miniPanel = new JPanel();
  private JraDialog miniFrame;
  private JraTextField racun = new JraTextField();
  private JraTextField platiti = new JraTextField();
  private JraTextField uplata = new JraTextField();
  private OKpanel miniOK = new OKpanel(){
    public void jPrekid_actionPerformed(){
      cancelOption();
    }
    public void jBOK_actionPerformed(){
      saveUplatu();
    }
  };

  private void cancelOption(){
    getJPTV().uninstallSelectionTracker();
    miniFrame.dispose();
  }

  private void saveUplatu(){

    if (qdsPojed.getBigDecimal("PROV").compareTo(qdsPojed.getBigDecimal("PLAC").add(tds.getBigDecimal("UPLATA")))<0) {
      JOptionPane.showConfirmDialog(this,"Isplata ne moze biti veæa od iznosa provizije!","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          uplata.requestFocus();
        }
      });
      return;
    }

    qdsPojed.setBigDecimal("PLAC",qdsPojed.getBigDecimal("PLAC").add(tds.getBigDecimal("UPLATA")));
    qdsPojed.setBigDecimal("SALDO",qdsPojed.getBigDecimal("PROV").subtract(qdsPojed.getBigDecimal("PLAC")));

    String mysqlupit = "select * from doki where cskl='"+qdsPojed.getString("cskl")+"' and vrdok='"+
                       qdsPojed.getString("vrdok")+"' and god='"+qdsPojed.getString("god")+"' and brdok="+
                       qdsPojed.getInt("BRDOK");
    QueryDataSet myqds = hr.restart.util.Util.getNewQueryDataSet(mysqlupit,true);
    myqds.setBigDecimal("PROVISP",qdsPojed.getBigDecimal("PLAC"));
    myqds.setBigDecimal("PROVPOST",qdsPojed.getBigDecimal("POST"));

    if (qdsAll!= null && qdsAll.getRowCount()!=0) {
      qdsAll.setBigDecimal("PLAC",
                           qdsAll.getBigDecimal("PLAC").add(tds.getBigDecimal("UPLATA")));
      qdsAll.setBigDecimal("SALDO",
                           qdsAll.getBigDecimal("PROV").subtract(qdsAll.getBigDecimal("PLAC")));
    }

    raTransaction.saveChanges(myqds);
    getJPTV().fireTableDataChanged();
    miniFrame.dispose();
  }

  private void initMiniPanel(){
    
    /*miniFrame.addKeyListener(new java.awt.event.KeyAdapter(){
      public void keyPressed(java.awt.event.KeyEvent e){
        if (e.getKeyCode()== e.VK_ESCAPE) {
          e.consume();
          cancelOption();
        } else if (e.getKeyCode()== e.VK_F10) {
          e.consume();
          saveUplatu();
        }
      }
    });*/
    
    miniPanel.setLayout(new XYLayout());
    miniPanel.add(new JLabel("Iznos provizije"),new XYConstraints(15,15,-1,-1));
    miniPanel.add(racun,new XYConstraints(150,15,100,-1));
    miniPanel.add(new JLabel("Prijašnje isplate"),new XYConstraints(15,40,-1,-1));
    miniPanel.add(platiti,new XYConstraints(150,40,100,-1));
    miniPanel.add(new JLabel("Isplate"),new XYConstraints(15,65,-1,-1));
    miniPanel.add(uplata,new XYConstraints(150,65,100,-1));
    ((XYLayout) miniPanel.getLayout()).setHeight(100);
    ((XYLayout) miniPanel.getLayout()).setWidth(260);
    miniPanel.setBorder(BorderFactory.createEtchedBorder());
  }

  public void jptv_doubleClickA() {
    hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
    rut.showDocs(qdsPojed.getString("CSKL"), "", qdsPojed.getString("VRDOK"),qdsPojed.getInt("BRDOK"),qdsPojed.getString("GOD"));
  }

String selectidColumn ="";
  public void jptv_doubleClick() {
    getJPTV().uninstallSelectionTracker();
    if (jrfCAG.getText().length() == 0){
      getJPTV().getColumnsBean().saveSettings();
      toHash();
      this.isFromAllAgents = true;
      jrfCAG.setText(String.valueOf(qdsAll.getInt("CAGENT")));
      jrfCAG.forceFocLost();

      selectidColumn = getJPTV().getColumnsBean().getComboSelectedItem();
// ovdje zapamtiti koji je selektiran
      this.getJPTV().setDataSet(null);
      okPress();
   } else {
     azuriranjeNaplate();
   }
  }

  public void ponUPL(){
    if (!isFromAllAgents) return;

    if (qdsPojed.getBigDecimal("PLAC").doubleValue()==0) {
      JOptionPane.showMessageDialog(this,
                                    "Za uvaj raèun nije izvršena isplata !!!!","Poruka",
                                    JOptionPane.DEFAULT_OPTION);
      return;
    }
    String mysqlupit = "select * from doki where cskl='"+qdsPojed.getString("cskl")+"' and vrdok='"+
                    qdsPojed.getString("vrdok")+"' and god='"+qdsPojed.getString("god")+"' and brdok="+
                    qdsPojed.getInt("BRDOK");
    QueryDataSet myqds = hr.restart.util.Util.getNewQueryDataSet(mysqlupit,true);
    if (qdsAll!= null && qdsAll.getRowCount()!=0) {
      qdsAll.setBigDecimal("PLAC",
                           qdsAll.getBigDecimal("PLAC").subtract(qdsPojed.getBigDecimal("PLAC")));
      qdsAll.setBigDecimal("SALDO",
                           qdsAll.getBigDecimal("PROV").subtract(qdsAll.getBigDecimal("PLAC")));
    }
    qdsPojed.setBigDecimal("PLAC",Aus.zero2);
    qdsPojed.setBigDecimal("SALDO",qdsPojed.getBigDecimal("PROV"));
    myqds.setBigDecimal("PROVISP",qdsPojed.getBigDecimal("PLAC"));
    myqds.setBigDecimal("PROVPOST",qdsPojed.getBigDecimal("PLAC"));
    qdsPojed.setBigDecimal("POST", agentProv);
    raTransaction.saveChanges(myqds);

    getJPTV().fireTableDataChanged();
    JOptionPane.showMessageDialog(this,
                                  "Isplata poništena !!!","Poruka",
                                   JOptionPane.DEFAULT_OPTION);
  }

  public void azuriranjeNaplate(){
    if (jrfCAG.getText().length() == 0){
      return;
    }
    racun.setDataSet(qdsPojed);
    racun.setColumnName("PROV");
    platiti.setDataSet(qdsPojed);
    platiti.setColumnName("PLAC");
    uplata.setDataSet(tds);
    uplata.setColumnName("UPLATA");

    uplata.getDataSet().setBigDecimal("UPLATA",qdsPojed.getBigDecimal("SALDO"));

    rcc.setLabelLaF(racun,false);
    rcc.setLabelLaF(platiti,false);
    miniFrame = new JraDialog(getJframe(),"Ažuriranje isplate provizije",true);
    miniFrame.getContentPane().setLayout(new BorderLayout());
    miniFrame.getContentPane().add(miniPanel,BorderLayout.CENTER);
    miniFrame.getContentPane().add(miniOK,BorderLayout.SOUTH);
    miniFrame.pack();
    miniOK.registerOKPanelKeys(miniFrame);
    miniFrame.setLocation(this.getLocation().x+
                          (int)((this.getSize().getWidth() - miniFrame.getSize().getWidth())/2),
                          this.getLocation().y+
                          (int)((this.getSize().getHeight() - miniFrame.getSize().getHeight())/2));

    miniFrame.setVisible(true);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        uplata.requestFocus();
      }
    });
  }

  public QueryDataSet getQDS(){
    return this.getJPTV().getDataSet();
  }

  public String getPocDat(){
    return raDateUtil.getraDateUtil().dataFormatter(tds.getTimestamp("pocDatum"));
  }

  public String getZavDat(){
    return raDateUtil.getraDateUtil().dataFormatter(tds.getTimestamp("zavDatum"));
  }

  public String getCSKL(){
    return jrfCSKL.getText();
  }

  public String getCORG(){
    return jrfCORG.getText();
  }

  public String getStatus(){
    return status;
  }
  
  public int getCAGENT(){
   return Integer.valueOf(jrfCAG.getText()).intValue();
  }
  
  public String getNAZAGENT(){
   return jrfNAZAG.getText();
  }

  public void toHash(){
    hm.clear();
    hm.put("CORG",jrfCORG.getText());
    hm.put("CSKL",jrfCSKL.getText());
    hm.put("tds",tds);
  }

  public void fromHash(){

    jrfCAG.setText("");
    jrfCAG.emptyTextFields();
    if (hm.containsKey("CORG")){
      jrfCORG.setText((String) hm.get("CORG"));
      jrfCORG.forceFocLost();
    } else {
      jrfCORG.setText("");
      jrfCORG.emptyTextFields();
    }
    if (hm.containsKey("CSKL")){
      jrfCSKL.setText((String) hm.get("CSKL"));
      jrfCSKL.forceFocLost();
    } else {
      jrfCSKL.setText("");
      jrfCSKL.emptyTextFields();
    }

    if (hm.containsKey("tds")){
      tds = (TableDataSet) hm.get("tds");
    } else {
      tds = null;
    }

    this.getJPTV().setDataSetAndSums(qdsAll,new String[] {"PROV","PLAC","SALDO"});
    /*getJPTV().getColumnsBean().initialize();
    getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);*/
  }
  
  public String navDoubleClickActionName() {
    return "Raèuni";
  }
  
  private int[] visibleAll = new int[]{0,1,2,3,4,5,6,7};
  private int[] visibleSingle = new int[]{0,1,2,3,4,5,6,7,8};

  public int[] navVisibleColumns(){
    return jrfCAG.getText().length() == 0 ? visibleAll : visibleSingle;
  }

  /*public void togle_ctrl_A(){
    int row =0;
    if (getJPTV().getDataSet()!=null) {
      row = getJPTV().getDataSet().getRow();
    }
   getJPTV().enableEvents(false);
   for (getJPTV().getDataSet().first();getJPTV().getDataSet().inBounds();getJPTV().getDataSet().next()){
   rSTM.toggleSelection(getJPTV().getDataSet());
   }
   getJPTV().getDataSet().goToRow(row);
   getJPTV().enableEvents(true);
 }*/

 /*public void togle(){
   int row =0;
   if (getJPTV().getDataSet()!=null) {
     row = getJPTV().getDataSet().getRow();
   }
   rSTM.toggleSelection(getJPTV().getDataSet());
   getJPTV().getDataSet().goToClosestRow(row+1);
 }*/

/* protected void this_keyPressed(java.awt.event.KeyEvent e) {

  if (e.getKeyCode()==e.VK_ENTER) {
    e.consume();
    togle();
  }
  super.this_keyPressed(e);
 }
*/
 //TODO for future use if needed ;)

/*
 public java.sql.Timestamp pocDat(){
   return tds.getTimestamp("pocDatum");
 }

 public java.sql.Timestamp zavDat(){
   return tds.getTimestamp("zavDatum");
 }
*/
}

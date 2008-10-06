/****license*****************************************************************
**   file: raCatchDoc.java
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

import hr.restart.swing.JraDialog;


public class raCatchDoc extends JraDialog {
/*
  sysoutTEST ST = new sysoutTEST(false);
  java.math.BigDecimal Nula = java.math.BigDecimal.valueOf(0);
  Dimension screenSize;
  allSelect aSS = new allSelect();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
    public void jBOK_actionPerformed(){
      jBOK_action();
    }
    public void jPrekid_actionPerformed(){
      jPrekid_action();
    }
  };
  raJPTableView jpTableView = new raJPTableView() {
    public void mpTable_killFocus(java.util.EventObject e) {
//		jTabPane.requestFocus();
    }
    public void mpTable_doubleClicked() {
//		rnvUpdate_action();
    }
  };
  boolean bPrenos = false;
  BorderLayout borderLayout1 = new BorderLayout();
  JComboBox rcbTIPDOKUMENTA = new JComboBox();
  String vrsta_dok="";
  String odabrana_vrsta_dok="";
  raIzlazTemplate fdi ;
  QueryDataSet qDS = new QueryDataSet();
  QueryDataSet qZaglavlje = new QueryDataSet();
  QueryDataSet qStavke = new QueryDataSet();
  raKalkulDoc rKD = new raKalkulDoc();
  LinkClass lc = LinkClass.getLinkClass();
  allStanje AST = allStanje.getallStanje();

  public raCatchDoc() {
    try {
      jbInit();
    }
    catch (Exception e) {
        System.out.println(e);
    }
  }
  public void jbInit() throws Exception {
    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    ((hr.restart.swing.JraTable2)jpTableView.getMpTable()).fireTableDataChanged();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(okp,BorderLayout.SOUTH);
    getContentPane().add(jpTableView,BorderLayout.CENTER);
    setTitle("Dokumenti za podlogu");
    addKeyListener(new java.awt.event.KeyAdapter(){
        public void keyPressed(java.awt.event.KeyEvent e){
          if (e.getKeyCode()==java.awt.event.KeyEvent.VK_F10){
   		 jBOK_action();
          }
          else if (e.getKeyCode()==java.awt.event.KeyEvent.VK_ESCAPE){
		jPrekid_action();
          }
        }
    });
    initQDS();
  }

  public void jPrekid_action(){
    bPrenos = false;
    this.hide();
  }
  public void jBOK_action(){
    findZaglavlje();
    findStavke();
    if (qStavke.isEmpty()){
     javax.swing.JOptionPane.showMessageDialog(null,
          "Ovaj dokument nama stavke. Nema se što prenijeti !",
          "Upozorenje",javax.swing.JOptionPane.WARNING_MESSAGE);
      bPrenos = false;
    }
    else {
      if(testallStavke()) {
        bPrenos = true;
        addZaglavlje();
      }
      else {
        bPrenos = false;
      }
      this.hide();
    }
  }

  public boolean BrowseInit()  {
    if (qDS.isEmpty()){
      javax.swing.JOptionPane.showMessageDialog(null,
          "Ne postoje dokumenti ove vrste za podlogu !",
          "Greška",javax.swing.JOptionPane.WARNING_MESSAGE);
      return false;
    }
    else {
      jpTableView.setVisibleCols(new int [] {0,1,2});
      jpTableView.setKumTak(false);
      jpTableView.setDataSet(qDS);
      jpTableView.getColumnsBean().eventInit();
      ((hr.restart.swing.JraTable2)jpTableView.getMpTable()).fireTableDataChanged();
      return true ;
    }
  }

  public void initQDS(){
    qDS.setColumns(new Column[]{
    (Column) dm.getDoki().getColumn("CSKL").clone(),
    (Column) dm.getDoki().getColumn("GOD").clone(),
    (Column) dm.getDoki().getColumn("VRDOK").clone(),
    (Column) dm.getDoki().getColumn("BRDOK").clone(),
    (Column) dm.getDoki().getColumn("CPAR").clone()
    });
  }

  public void prepareQuery() {

//    to do provjera da li postoje stavke
//    provjera da li je dokument ve\u0107 prenesen

    String dodatak = "";
    qDS = hr.restart.util.Util.getNewQueryDataSet(aSS.getS4raCatchDoc(
                                fdi.getMasterSet().getString("GOD"),
                                fdi.getMasterSet().getString("CSKL"),
                                odabrana_vrsta_dok,dodatak),true);
  }

  public void findZaglavlje() {

    qZaglavlje = hr.restart.util.Util.getNewQueryDataSet(
        aSS.getQuery4rCD4findZaglavlje(qDS.getString("GOD"),
                                        qDS.getString("CSKL"),
                                        qDS.getString("VRDOK"),
                                        qDS.getInt("BRDOK")),true);
  }

  public void addZaglavlje() {
System.out.println("addZaglavlje()");
    String var_doh;
    qZaglavlje.first();
    var_doh = qZaglavlje.getString("CSKL").concat("-")
                    .concat(qZaglavlje.getString("VRDOK")).concat("-")
                    .concat(qZaglavlje.getString("GOD")).concat("-")
                    .concat(String.valueOf(qZaglavlje.getInt("BRDOK")));
    fdi.getMasterSet().setInt("cpar",qZaglavlje.getInt("cpar"));
    fdi.getMasterSet().setInt("pj",qZaglavlje.getInt("pj"));
    fdi.getMasterSet().setString("corg",qZaglavlje.getString("corg"));
    fdi.getMasterSet().setString("cvrtr",qZaglavlje.getString("cvrtr"));
    fdi.getMasterSet().setShort("ddosp",qZaglavlje.getShort("ddosp"));
    fdi.getMasterSet().setString("cug",qZaglavlje.getString("cug"));
    fdi.getMasterSet().setTimestamp("datug",qZaglavlje.getTimestamp("datug"));
    // datdosp se ra\u010Duna
    if (odabrana_vrsta_dok.equals("NKU")) {
      fdi.getMasterSet().setString("brnariz",var_doh);
      fdi.getMasterSet().setTimestamp("datnariz",qZaglavlje.getTimestamp("datdok"));
    }
    else if (odabrana_vrsta_dok.equals("PON")){
      fdi.getMasterSet().setString("brpon",var_doh);    /// razriješiti s ifom
      fdi.getMasterSet().setTimestamp("datpon",qZaglavlje.getTimestamp("datdok"));
    }
    else if (odabrana_vrsta_dok.equals("PRN")){
      fdi.getMasterSet().setString("brprd",var_doh);
      fdi.getMasterSet().setTimestamp("datprd",qZaglavlje.getTimestamp("datdok"));
    }
    else if (odabrana_vrsta_dok.equals("OTP")){
      fdi.getMasterSet().setString("brdokiz",qZaglavlje.getString("brdokiz"));    /// razriješiti s ifom
      fdi.getMasterSet().setTimestamp("datdokiz",qZaglavlje.getTimestamp("datdok"));
    }
    fdi.getMasterSet().setString("oznval",qZaglavlje.getString("oznval"));
    fdi.getMasterSet().setBigDecimal("tecaj",qZaglavlje.getBigDecimal("tecaj"));
    fdi.getMasterSet().setString("cradnal",qZaglavlje.getString("cradnal"));
    fdi.getMasterSet().setTimestamp("datradnal",qZaglavlje.getTimestamp("datradnal"));
    fdi.getMasterSet().setString("cfra",qZaglavlje.getString("cfra"));
    fdi.getMasterSet().setString("cnacpl",qZaglavlje.getString("cnacpl"));
    fdi.getMasterSet().setString("cnac",qZaglavlje.getString("cnac"));
    fdi.getMasterSet().setString("cnamj",qZaglavlje.getString("cnamj"));
    fdi.getMasterSet().setString("cnap",qZaglavlje.getString("cnap"));
    fdi.getMasterSet().setBigDecimal("upzt",qZaglavlje.getBigDecimal("upzt"));
    fdi.getMasterSet().setString("cshzt",qZaglavlje.getString("cshzt"));
    fdi.getMasterSet().setBigDecimal("uprab",qZaglavlje.getBigDecimal("uprab"));
    fdi.getMasterSet().setString("cshrab",qZaglavlje.getString("cshrab"));
    fdi.getMasterSet().setString("opis",qZaglavlje.getString("opis"));
    fdi.getMasterSet().setBigDecimal("uirac",qZaglavlje.getBigDecimal("uirac"));
    fdi.forceall_focuslost();
  }

  public void addStavke(){
System.out.println("addStavke()");
    qStavke.first();
    if (vrsta_dok.equals("PON") || vrsta_dok.equals("PRD") || vrsta_dok.equals("RAC")) {
      do {
        fdi.getDetailSet().insertRow(true);
        copyCommon();
        copyFinancPart();
        lc.TransferFromDB2Class(fdi.getDetailSet(),rKD.stavka);
        rKD.kalkResetSkladPart();
        lc.TransferFromClass2DB(fdi.getDetailSet(),rKD.stavka);
        lc.setBDField("rezkol",fdi.getMasterSet().getString("rezkol"),rKD.stavka);
        AST.findStanjeUnconditional(fdi.getDetailSet().getString("GOD"),
                                    fdi.getDetailSet().getString("CSKL"),
                                    fdi.getDetailSet().getInt("CART"));
        lc.TransferFromDB2Class(AST.gettrenSTANJE(),rKD.stanje);
        rKD.KalkulacijaStanje(vrsta_dok);
        rKD.VratiRezervu(this.odabrana_vrsta_dok);
        lc.TransferFromClass2DB(AST.gettrenSTANJE(),rKD.stanje);
        AST.gettrenSTANJE().saveChanges();
        fdi.getDetailSet().saveChanges();
      }
      while (qStavke.next());
    }  else if (vrsta_dok.equals("ROT")){
      do {
        fdi.getDetailSet().insertRow(true);
        copyCommon();
        copyFinancPart();
        AST.findStanjeUnconditional(fdi.getDetailSet().getString("GOD"),
                                    fdi.getDetailSet().getString("CSKL"),
                                    fdi.getDetailSet().getInt("CART"));
        lc.TransferFromDB2Class(fdi.getDetailSet(),rKD.stavka);
        lc.TransferFromDB2Class(AST.gettrenSTANJE(),rKD.stanje);
        rKD.kalkPrepareDoc();
        rKD.kalkSkladPart();
        rKD.KalkulacijaStanje(vrsta_dok);
        rKD.VratiRezervu(this.odabrana_vrsta_dok);
        lc.TransferFromClass2DB(fdi.getDetailSet(),rKD.stavka);
        lc.setBDField("rezkol",fdi.getMasterSet().getString("rezkol"),rKD.stavka);
        lc.printAll(rKD.stavka);
        lc.TransferFromClass2DB(AST.gettrenSTANJE(),rKD.stanje);
        fdi.getDetailSet().saveChanges();
        AST.gettrenSTANJE().saveChanges();
      }
      while (qStavke.next());
    }
    srediPreneseno();
    bPrenos = false;
  }
  public void srediPreneseno(){

//    var_doh = fdi.getMasterSet().getString("CSKL").concat("-")
//                 .concat(fdi.getMasterSet().getString("VRDOK")).concat("-")
//                 .concat(fdi.getMasterSet().getString("GOD")).concat("-")
//                 .concat(String.valueOf(fdi.getMasterSet().getInt("BRDOK")));
//    Timestamp dan = fdi.getMasterSet().getTimestamp("datdok")
//
//    if (this.vrsta_dok.equals("PON")) {
//      qZaglavlje.fdi.getMasterSet().setString("brnariz",var_doh);
//      fdi.getMasterSet().setTimestamp("datnariz",dan);
//    }
//    else if (vrsta.equals("PRD")){
//      if (odabrana_vrsta_dok.equals("PON")){
//        fdi.getMasterSet().setString("brpon",var_doh);
//        fdi.getMasterSet().setTimestamp("datpon",qZaglavlje.getTimestamp("datdok"));
//      }
//    }
//    else if (odabrana_vrsta_dok.equals("PRD")){
//      fdi.getMasterSet().setString("brprd",var_doh);
//      fdi.getMasterSet().setTimestamp("datprd",qZaglavlje.getTimestamp("datdok"));
//    }
//    else if (odabrana_vrsta_dok.equals("OTP")){
//      fdi.getMasterSet().setString("brdokiz",qZaglavlje.getString("brdokiz"));    /// razriješiti s ifom
//      fdi.getMasterSet().setTimestamp("datdokiz",qZaglavlje.getTimestamp("datdok"));
//    }
//    qZaglavlje.

    qZaglavlje.setString("AKTIV","N");
    qZaglavlje.saveChanges();
  }
  public void copyCommon() {

    fdi.getDetailSet().setString("CSKL",fdi.getMasterSet().getString("CSKL"));
    fdi.getDetailSet().setString("GOD",fdi.getMasterSet().getString("GOD"));
    fdi.getDetailSet().setString("VRDOK",fdi.getMasterSet().getString("VRDOK"));
    fdi.getDetailSet().setInt("BRDOK",fdi.getMasterSet().getInt("BRDOK"));

    fdi.getDetailSet().setShort("RBR",qStavke.getShort("rbr"));
    fdi.getDetailSet().setInt("CART",qStavke.getInt("CART"));
    fdi.getDetailSet().setString("CART1",qStavke.getString("CART1"));
    fdi.getDetailSet().setString("BC",qStavke.getString("BC"));
    fdi.getDetailSet().setString("NAZART",qStavke.getString("NAZART"));
    fdi.getDetailSet().setString("JM",qStavke.getString("JM"));
    fdi.getDetailSet().setBigDecimal("KOL",qStavke.getBigDecimal("KOL"));
  }

  public void copySkladPart() {
      fdi.getDetailSet().setBigDecimal("NC",qStavke.getBigDecimal("NC"));
      fdi.getDetailSet().setBigDecimal("INAB",qStavke.getBigDecimal("INAB"));
      fdi.getDetailSet().setBigDecimal("IMAR",qStavke.getBigDecimal("IMAR"));
      fdi.getDetailSet().setBigDecimal("VC",qStavke.getBigDecimal("VC"));
      fdi.getDetailSet().setBigDecimal("IBP",qStavke.getBigDecimal("IBP"));
      fdi.getDetailSet().setBigDecimal("IPOR",qStavke.getBigDecimal("IPOR"));
      fdi.getDetailSet().setBigDecimal("MC",qStavke.getBigDecimal("MC"));
      fdi.getDetailSet().setBigDecimal("ISP",qStavke.getBigDecimal("ISP"));
      fdi.getDetailSet().setBigDecimal("ZC",qStavke.getBigDecimal("ZC"));
      fdi.getDetailSet().setBigDecimal("IRAZ",qStavke.getBigDecimal("IRAZ"));
      fdi.getDetailSet().setString("BRPRI",qStavke.getString("BRPRI"));
      fdi.getDetailSet().setShort("RBRPRI",qStavke.getShort("RBRPRI"));
  }

  public void copyFinancPart() {

      fdi.getDetailSet().setBigDecimal("UPRAB",qStavke.getBigDecimal("UPRAB"));
      fdi.getDetailSet().setBigDecimal("UIRAB",qStavke.getBigDecimal("UIRAB"));
      fdi.getDetailSet().setBigDecimal("UPZT",qStavke.getBigDecimal("UPZT"));
      fdi.getDetailSet().setBigDecimal("UIZT",qStavke.getBigDecimal("UIZT"));
      fdi.getDetailSet().setBigDecimal("FC",qStavke.getBigDecimal("FC"));
      fdi.getDetailSet().setBigDecimal("INETO",qStavke.getBigDecimal("INETO"));
      fdi.getDetailSet().setBigDecimal("FVC",qStavke.getBigDecimal("FVC"));
      fdi.getDetailSet().setBigDecimal("IPRODBP",qStavke.getBigDecimal("IPRODBP"));
      fdi.getDetailSet().setBigDecimal("POR1",qStavke.getBigDecimal("POR1"));
      fdi.getDetailSet().setBigDecimal("POR2",qStavke.getBigDecimal("POR2"));
      fdi.getDetailSet().setBigDecimal("POR3",qStavke.getBigDecimal("POR3"));
      fdi.getDetailSet().setBigDecimal("FMC",qStavke.getBigDecimal("FMC"));
      fdi.getDetailSet().setBigDecimal("IPRODSP",qStavke.getBigDecimal("IPRODSP"));
  }

  public void findStavke() {
    qStavke = hr.restart.util.Util.getNewQueryDataSet(aSS.getQuery4rCD4findStavke(qDS.getString("GOD"),
                                      qDS.getString("CSKL"),
                                      qDS.getString("VRDOK"),
                                       qDS.getInt("BRDOK")),true);
  }

  public boolean testStavke(){
      rKD.stavkaold.Init();
      rKD.stanje.Init();
      rKD.stavka.Init();
      AST.findStanjeUnconditional(qStavke.getString("GOD"),qStavke.getString("CSKL"),
                                  qStavke.getInt("CART"));
//      ST.prn(AST.gettrenSTANJE());
      lc.TransferFromDB2Class(qStavke,rKD.stavka);
      lc.TransferFromDB2Class(AST.gettrenSTANJE(),rKD.stanje);
      lc.printAll(rKD.stanje);
      rKD.KalkulacijaStanje(vrsta_dok);
      lc.printAll(rKD.stanje);

      if (this.vrsta_dok.equals("ROT")) {
        if (rKD.TestStanje()==0) {
          return true;
        }
        else {
          javax.swing.JOptionPane.showMessageDialog(null,
            "Nemogu\u0107e prenijeti dokument. Nedovoljna zaliha !",
            "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
      else
        return true;
  }
  public boolean testallStavke() {
    qStavke.first();
    do {
      if(!testStavke()) {
        ST.prn(AST.gettrenSTANJE());
        lc.printAll(rKD.stavka);
        lc.printAll(rKD.stanje);
        return false;
      }
    }
    while (qStavke.next());
    return true;
  }

  public void tamodalje(String vrd){
      odabrana_vrsta_dok=vrd;
  }

  public void odabirDoc() {
    final JDialog odabir = new JraDialog();
    odabir.addKeyListener(new java.awt.event.KeyAdapter(){
      public void keyPressed(java.awt.event.KeyEvent e){
        if (e.getKeyCode()==java.awt.event.KeyEvent.VK_F10){
          odabir.hide();
          go_next();
        }
        else if (e.getKeyCode()==java.awt.event.KeyEvent.VK_ESCAPE){
          odabir.hide();
        }
      }
    });
    hr.restart.util.OKpanel okpOda = new hr.restart.util.OKpanel(){
      public void jBOK_actionPerformed(){
        odabir.hide();
        go_next();
      }
      public void jPrekid_actionPerformed(){
        odabir.hide();
      }};
    String[] zaodabrati;

    if (this.vrsta_dok.equals("PON")) {
       zaodabrati = new String[] {"Narudžba"};
       tamodalje("NKU");
    }
    else if (this.vrsta_dok.equals("PRD")) {
       zaodabrati = new String[] {"Narudžba","Ponuda"};
       tamodalje("NKU");
    }
    else if (this.vrsta_dok.equals("RAC")) {
       zaodabrati = new String[] {"Otpremnica","Ponuda","Radni nalog","Predra\u010Dun","Revers"};
       tamodalje("OTP");
    }
    else if (this.vrsta_dok.equals("ROT")) {
       zaodabrati = new String[] {"Narudžba","Ponuda","Predra\u010Dun"};
       tamodalje("PON");
    }
    else {
       zaodabrati=new String[] {"mal san puka","adasdas","asdasd"};
    }
    JComboBox jco = new JComboBox(zaodabrati);
    jco.addItemListener(new java.awt.event.ItemListener(){
      public void itemStateChanged (java.awt.event.ItemEvent i){
        if (i.getItem().equals("Ponuda")){
          tamodalje("PON");
        }else if (i.getItem().equals("Predra\u010Dun")){
          tamodalje("PRD");
        }else if (i.getItem().equals("Narudžba")){
          tamodalje("NKU");
        }else if (i.getItem().equals("Otpremnica")){
          tamodalje("OTP");
        }else if (i.getItem().equals("Revers")){
          tamodalje("REV");
        }
      }
    });

    JPanel jpo  = new JPanel();
    JLabel tekst = new JLabel("Vrsta dokumenta");
    jco.setName("Odabir dokumenta ");
    XYLayout xyl =new XYLayout();
    jpo.setLayout(xyl);
    xyl.setHeight(50);
    xyl.setWidth(315);
    jpo.setBorder(new javax.swing.border.EtchedBorder());
    jpo.add(jco, new XYConstraints(150, 15, 150, -1));
    jpo.add(tekst, new XYConstraints(15, 15, -1, -1));
    odabir.setTitle("Odabir vrste dokumenta za podlogu");
    odabir.setModal(true);
    odabir.getContentPane().setLayout(new BorderLayout());
    odabir.getContentPane().add(okpOda,BorderLayout.SOUTH);
    odabir.getContentPane().add(jpo,BorderLayout.CENTER);
    odabir.pack();
    odabir.setLocation((screenSize.width - odabir.getWidth()) / 2, (screenSize.height - odabir.getHeight()) / 2);
    odabir.show();
    jco.requestFocus();
  }
  public void go_next() {
    prepareQuery();
    if (BrowseInit()) show();
    else odabirDoc();
  }
  public boolean getbPrenos() {
    return bPrenos;
  }

  public void go(raIzlazTemplate fdi,String vrsta_dok) {
    this.vrsta_dok = vrsta_dok;
    this.fdi = fdi ;
    odabirDoc();
  }

  public void show() {
    this.setLocation(fdi.raMaster.getLocation().x+fdi.raMaster.getSize().width,fdi.raMaster.getLocation().y);
    this.pack();
    this.setModal(true);
    super.show();
  }
 */
}
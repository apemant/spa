/****license*****************************************************************
**   file: raMatObracun.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raMatObracun extends JraDialog {

  private TreeMap hm = new TreeMap();
  private HashMap hmgreska = new HashMap();
  private boolean stop = false;
  private Variant myVariant;
  private String msggreska = "Obra\u010Dun uspješno završen !";
  private raControlDocs rCD = new raControlDocs();
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private manipString mp = manipString.getmanipString();
  private JPanel panel4RadioButton = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  private XYLayout xYLayout1 = new XYLayout();
  private JPanel mainPanel = new JPanel();
  private JLabel tekst1 = new JLabel("Obrada");
  private JLabel tekst2 = new JLabel("Datum obra\u010Duna");
  private JraTextField jtf = new JraTextField();
  private QueryDataSet qdsTMP = null;
  private QueryDataSet qdsTMPOUTStdoki = null;
  private QueryDataSet qdsTMPOUTStmeskla = null;
  private JProgressBar jProgressBar1 = new JProgressBar();
  private rapancskl1 rpcskl = new rapancskl1(){
    public void MYafter_lookUp() {}
    public void MYpost_after_lookUp(){}
  };

  private raKalkulBDDoc rKBD = new raKalkulBDDoc();
  hr.restart.robno.Util rutil = hr.restart.robno.Util.getUtil();
  private java.util.Vector vec = new java.util.Vector();
  private JRadioButton JRBObr = new JRadioButton("Obra\u010Dun");
  private JRadioButton JRBPObr = new JRadioButton("Poništanje obra\u010Duna");
  private ButtonGroup bGroup = new ButtonGroup();
  private hr.restart.util.LinkClass lc = hr.restart.util.LinkClass.getLinkClassD();
  private QueryDataSet qdsStanje = null;

  private myLittleTimer timer = new myLittleTimer() {
    long time = System.currentTimeMillis();

    int delaySec = 1;
    int multiplikator = 1;
    boolean isActive = true;
    boolean isMyAlive = true;

    public void setMyAlive(boolean kako){
      isMyAlive = kako;
    }

    public void run() {
      isMyAlive = true;
      while (isMyAlive){
        if (isActive) {
          if ((System.currentTimeMillis()-time)>1000*delaySec* multiplikator){
            jProgressBar1.setValue(jProgressBar1.getValue()+1);
            jProgressBar1.setMaximum(jProgressBar1.getMaximum()+1);
            multiplikator ++;
          }
        }
        else {
          stopTime();
        }

      }
    }
    public void stopTime(){
      time = System.currentTimeMillis();
      multiplikator = 1;
      jProgressBar1.setValue(jProgressBar1.getValue()+1);
      jProgressBar1.setMaximum(jProgressBar1.getMaximum()+1);
      isActive = false;
    }
    public void startTime(int delaySec){
      isActive = true;
      this.delaySec = delaySec;
    }
  };

  private Runnable tredich = new Runnable() {

    public void run() {
      msggreska = "Obra\u010Dun uspješno završen !";
//      if (!stop)       timer.setMyAlive(true);
      jProgressBar1.setMaximum(10);
      jProgressBar1.setValue(0);
      new Thread(timer).start();
//      while(!stop) {}
      Obrada();
      message(msggreska,hmgreska.isEmpty());
      jProgressBar1.setValue(0);
      timer.setMyAlive(false);
    }
  };

  private hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
    public void jBOK_actionPerformed(){
      stop = false;
      okPress();
    }
    public void jPrekid_actionPerformed(){
      cancel();
    }
  };

  public raMatObracun(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public raMatObracun() {
    this(startFrame.getStartFrame(), "Obra\u010Dun troškova", false);
  }

  public void cancel(){
    DummySet = null;
    qdsStanje = null;
    hm = null;
    qdsTMP = null;
    qdsTMPOUTStdoki = null;
    qdsTMPOUTStmeskla = null;
    stop = true;
    setVisible(false);
  }

  public void componentShow(){
    JRBObr.setSelected(true);
    InitState(true);
    DummySet.setTimestamp("DATUM",val.getToday());
  }

  public void InitState(boolean how) {

    rpcskl.Clear();
    rpcskl.disabCSKL(how);
    rpcskl.jrfCSKL.requestFocus();

  }
  Column rplDATSTAZ = new Column();
  QueryDataSet DummySet = new QueryDataSet();
  {
    rplDATSTAZ.setColumnName("DATUM");
    rplDATSTAZ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    rplDATSTAZ.setPrecision(8);
    rplDATSTAZ.setDisplayMask("dd-MM-yyyy");
//    rplDATSTAZ.setEditMask("dd-MM-yyyy");
    DummySet.setColumns(new Column[] {rplDATSTAZ});
    DummySet.open();
  }

  void jbInit() throws Exception {
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        rpcskl.jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
      }
    });
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        componentShow();
      }
    });
    this.addKeyListener(new java.awt.event.KeyAdapter(){
      public void keyPressed(java.awt.event.KeyEvent e){
        if (e.getKeyCode()==java.awt.event.KeyEvent.VK_F10){
          okPress();
        }
        else if (e.getKeyCode()==java.awt.event.KeyEvent.VK_ESCAPE){
          cancel();
        }
      }
    });

    getContentPane().add(mainPanel,BorderLayout.CENTER);
    getContentPane().add(okp,BorderLayout.SOUTH);
    dm.getSklad().open();
    bGroup.add(JRBObr);
    bGroup.add(JRBPObr);

    xYLayout1.setWidth(555);
    xYLayout1.setHeight(140);

    rpcskl.setOverCaption(true);
    panel4RadioButton.setLayout(new XYLayout(552,35));
    panel4RadioButton.setBorder(BorderFactory.createRaisedBevelBorder());
    panel4RadioButton.add(JRBObr,new XYConstraints(15, 5, -1, -1));
    panel4RadioButton.add(JRBPObr,new XYConstraints(408, 5, -1, -1));
    JRBObr.setHorizontalTextPosition(SwingConstants.LEFT);
    jtf.setHorizontalAlignment(SwingConstants.CENTER);
    jtf.setColumnName("DATUM");
    jtf.setDataSet(DummySet);
    mainPanel.setBorder(BorderFactory.createEtchedBorder());
    mainPanel.setLayout(xYLayout1);
    mainPanel.add(panel4RadioButton,new XYConstraints(0, 0, -1, -1));
    mainPanel.add(rpcskl,new XYConstraints(0, 35, 550, -1) );
    mainPanel.add(tekst2,new XYConstraints(15, 85, -1, -1));
    mainPanel.add(jtf,new XYConstraints(150, 85, 100, -1));
    mainPanel.add(tekst1, new XYConstraints(15, 110, -1, -1));
    mainPanel.add(jProgressBar1, new XYConstraints(150, 110, 390, -1));

  }

  public String createHashKey(QueryDataSet ds,boolean forObrac){
    String SforObrac = forObrac?"B":"A";
    String sklad ="";
    try {
      sklad = ds.getString("CSKL");
    }
    catch (Exception ex){
      if (forObrac) {
        sklad = ds.getString("CSKLIZ");
      }
      else {
        sklad = ds.getString("CSKLUL");
      }
    }

    return (sklad+"-"+ds.getInt("CART")+"-"+
                           ds.getTimestamp("DATDOK")+"-"+SforObrac);

  }

  public void pusmi(QueryDataSet ds,boolean forObrac,String sklad,String zadraz){


    String SforObrac = forObrac?"B":"A";
    int pimpek = forObrac?1:0;
    ds.first();
ST.prn(String.valueOf(ds.getRowCount()));
jProgressBar1.setMaximum(jProgressBar1.getMaximum()+ds.getRowCount()*pimpek);
    if (ds.getRowCount()!=0) {
      do {
        if (!forObrac){
          if (!(ds.getString("STATUS").equals("O") || ds.getString("STATUS").equals("K"))){
            String key = rCD.getKey(ds);
            if (!hmgreska.containsKey(key)){
              hmgreska.put(key,ds.getString("STATUS"));
            };
          }
        }

        if (hm.containsKey(ds.getString(sklad)+"-"+ds.getInt("CART")+"-"+
                           ds.getTimestamp("DATDOK")+"-"+SforObrac)) {
          ((Struct4Obrac) (hm.get(ds.getString(sklad)+"-"+ds.getInt("CART")+"-"+
                                  ds.getTimestamp("DATDOK")+"-"+SforObrac))).add(
                                  new Struct4Obrac(ds.getInt("CART"),
                                  ds.getBigDecimal("KOL"),
                                  ds.getBigDecimal("NC"),ds.getBigDecimal("VC"),
                                  ds.getBigDecimal("MC"),ds.getBigDecimal(zadraz),forObrac));

        }
        else {
          hm.put(ds.getString(sklad)+"-"+ds.getInt("CART")+"-"+
                 ds.getTimestamp("DATDOK")+"-"+SforObrac,new Struct4Obrac(ds.getInt("CART"),
                 ds.getBigDecimal("KOL"), ds.getBigDecimal("NC"),ds.getBigDecimal("VC"),
                 ds.getBigDecimal("MC"),ds.getBigDecimal(zadraz),forObrac));
        }

      } while (ds.next());
    }
  }


  public void message(String meidj, boolean isOK){
    javax.swing.JOptionPane.showConfirmDialog(this,meidj,isOK?"Poruka !":"Greška",
        //isOK?javax.swing.JOptionPane.OK_OPTION:
        javax.swing.JOptionPane.DEFAULT_OPTION,
        isOK?javax.swing.JOptionPane.INFORMATION_MESSAGE:javax.swing.JOptionPane.ERROR_MESSAGE);
  }

  public void okPress(){
    if (validacija()){
      if (JRBObr.isSelected()) {
        new Thread(tredich).start();
      }
//      else (JRBPObr.isSelected())
    }
  }
//String dodaj = " and cart=1";
  public void Obrada(){
    String dodaj = "";

    pusmi(
        hr.restart.util.Util.getNewQueryDataSet(
        "SELECT doku.datdok,doku.status,stdoku.* FROM Doku,Stdoku "+
        "WHERE doku.cskl = stdoku.cskl "+
        "AND doku.vrdok = stdoku.vrdok "+
        "AND doku.god = stdoku.god "+
        "AND doku.brdok = stdoku.brdok "+
        "AND doku.datdok <="+rutil.getTimestampValue(DummySet.getTimestamp("DATUM"),1)+
        " and doku.cskl = '"+rpcskl.jrfCSKL.getText()+"'"+dodaj,
        true),false,"CSKL","IZAD");

    pusmi(
        hr.restart.util.Util.getNewQueryDataSet(
        "SELECT meskla.datdok,meskla.status ,stmeskla.* FROM meskla,stmeskla "+
        "WHERE meskla.cskliz = stmeskla.cskliz "+
        "AND meskla.csklul = stmeskla.csklul "+
        "AND meskla.vrdok = stmeskla.vrdok "+
        "AND meskla.god = stmeskla.god "+
        "AND meskla.brdok = stmeskla.brdok "+
        "AND meskla.datdok <="+rutil.getTimestampValue(DummySet.getTimestamp("DATUM"),1)+
        " AND meskla.csklul='"+rpcskl.jrfCSKL.getText()+"'"+dodaj,
        true),false,"CSKLUL","ZADRAZUL");

    qdsTMPOUTStdoki = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT doki.datdok,doki.status,stdoki.* FROM Doki,Stdoki "+
        "WHERE doki.cskl = stdoki.cskl "+
        "AND doki.vrdok = stdoki.vrdok "+
        "AND doki.god = stdoki.god "+
        "AND doki.brdok = stdoki.brdok " +
        "AND doki.datdok <="+rutil.getTimestampValue(DummySet.getTimestamp("DATUM"),1)+
        " AND doki.cskl = '"+rpcskl.jrfCSKL.getText()+"'"+dodaj,
        true);

    pusmi(qdsTMPOUTStdoki,true,"CSKL","IRAZ");

    qdsTMPOUTStmeskla = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT meskla.datdok,meskla.status, stmeskla.* FROM meskla,stmeskla "+
        "WHERE meskla.cskliz = stmeskla.cskliz "+
        "AND meskla.csklul = stmeskla.csklul "+
        "AND meskla.vrdok = stmeskla.vrdok "+
        "AND meskla.god = stmeskla.god "+
        "AND meskla.brdok = stmeskla.brdok "+
        "AND meskla.datdok <="+rutil.getTimestampValue(DummySet.getTimestamp("DATUM"),1)+
        " AND meskla.cskliz='"+rpcskl.jrfCSKL.getText()+"'"+dodaj,true);

    pusmi(qdsTMPOUTStmeskla,true,"CSKLIZ","ZADRAZIZ");
    obracun();

  }

  public void obracun() {

    if (!hmgreska.isEmpty()) {
      msggreska = "Postoje neobra\u0111ene ulazne kalkulacije !";
      return ;
    }

    Iterator it = hm.keySet().iterator();
    Struct4Obrac prethodni = null;
    Struct4Obrac tekuci = null;
    Object obj = null;
    ST.prn(mp.getString("KEY",30,'-')+" -o- "+
                       mp.getString("CART",15,'-') +" -o- "+
                       mp.getString("KOL",15,'-') +" -o- "+
                       mp.getString("VRI",15,'-')+" -o- "+
                       mp.getString("KOLIN",15,'-') +" -o- "+
                       mp.getString("VRIIN",15,'-')+" -o- "+
                       mp.getString("KOLOUT",15,'-') +" -o- "+
                       mp.getString("VRIOUT",15,'-')+" -o- "+
                       mp.getString("KOLSUM",15,'-') +" -o- "+
                       mp.getString("VRISUM",15,'-')+" -o- "+
                       mp.getString("ZC",15,'-') +" -o- "+
                       mp.getString("forObrac",15,'-'));
    while (it.hasNext()){
//jProgressBar1.setValue(jProgressBar1.getValue()+1);
      timer.stopTime();
      obj = it.next();
      tekuci = (Struct4Obrac)(hm.get(obj));
      if (prethodni!=null){

        tekuci.add(prethodni);
        if (!tekuci.forObrac) prethodni = tekuci;

      }
      else {
        prethodni = (Struct4Obrac)(hm.get(obj));
      }
      ST.prn(mp.getString(((String) obj),30)+" -o- "+tekuci);
    }
  }

  public boolean validacija(){
    boolean retValue = true;
    String greska="";
    retValue = !val.isEmpty(rpcskl.jrfCSKL);
    if (retValue) retValue = !val.isEmpty(jtf);
    if (retValue) {
      qdsTMP = hr.restart.util.Util.getNewQueryDataSet(
          "SELECT * FROM Sklad WHERE cskl = '"+rpcskl.jrfCSKL.getText()+"'",
          true);

      retValue = (qdsTMP.getRowCount()>0);
      greska=retValue?greska:"Ne postoji takvo skladište !";
    }
    if (retValue && !qdsTMP.getString("TIPSKL").equalsIgnoreCase("M")){
      retValue = false;
      greska = "Nije skladište materijala !";
    }

    if (retValue && !qdsTMP.getString("NACOBR").equalsIgnoreCase("P")){
      retValue = false;
      greska = "Ovo skladište nije predvi\u0111eno za obra\u010Dun !";

    }

    if (retValue && DummySet.getTimestamp("DATUM").before(qdsTMP.getTimestamp("DATOBRAC"))){
      if (JRBObr.isSelected()){
        retValue = false;
        greska = "Datum obra\u010Duna je manji od datuma prethodnog obra\u010Duna !";
      }
    }
    else {
      if (JRBPObr.isSelected()){
        retValue = false;
        greska = "Datum poništenja obra\u010Duna je ve\u0107i od datuma prethodnog obra\u010Duna !";
      }
    }

    if (retValue && DummySet.getTimestamp("DATUM").before(qdsTMP.getTimestamp("DATKNJIZ"))){
      retValue = false;
      greska = "Datum je manji od datuma knjiženja !";
    }

    if (retValue && DummySet.getTimestamp("DATUM").before(qdsTMP.getTimestamp("DATINV"))){
      retValue = false;
      greska = "Datum je manji od datuma zadnje inventure !";
    }

    if (retValue && !qdsTMP.getString("STATINV").equalsIgnoreCase("N")){
      retValue = false;
      greska = "Inventura je u tijeku ... Nemogu\u0107a akcija !";
    }

    if (!retValue  && !greska.equals("")){
      javax.swing.JOptionPane.showConfirmDialog(this,greska,"Greška !",
          javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
    }

    qdsTMP = null;
    return retValue;

  }

  public void radi_cudo(QueryDataSet stavke){
    Struct4Obrac struko ;
    stavke.first();
    if (stavke.getRow()!=0) {
      do {
        struko = (Struct4Obrac)hm.get(createHashKey(stavke,true));

        stavke.getVariant("CART",myVariant);
        hr.restart.util.lookupData.getlookupData().raLocate(qdsStanje,new String[]{"CART"},
            new Variant[] {myVariant});

        if (stavke.getString("STATUS").equals("N")) {
          stavke.setBigDecimal("ZC",struko.zc);
          stavke.setBigDecimal("NC",struko.nc);
          stavke.setBigDecimal("VC",struko.vc);
          stavke.setBigDecimal("MC",struko.mc);

        if (stavke.getString("VRDOK").equalsIgnoreCase("MES") ||
            stavke.getString("VRDOK").equalsIgnoreCase("MEI") ||
            stavke.getString("VRDOK").equalsIgnoreCase("MEU"))  {
          /** @todo kalkulacija meskla*/

        } else {
          lc.TransferFromDB2Class(stavke,rKBD.stavka);
          lc.TransferFromDB2Class(qdsStanje,rKBD.stavka);
          rKBD.kalkSkladPart();
          rKBD.KalkulacijaStanje(stavke.getString("VRDOK"));
          lc.TransferFromClass2DB(stavke,rKBD.stavka);
          lc.TransferFromClass2DB(stavke,rKBD.stavka);
        }
      }
      }while (stavke.next());
    }
    }

  public void a_sad_uzivo(){

    qdsStanje = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT * FROM Stanje "+
        "WHERE god = '"+hr.restart.util.Valid.getValid().findYear(DummySet.getTimestamp("DATUM"))+
        "' and cskl = '"+rpcskl.jrfCSKL.getText()+"'",true);

    radi_cudo(qdsTMPOUTStdoki);
    radi_cudo(qdsTMPOUTStmeskla);


    hr.restart.util.raTransaction rT = new hr.restart.util.raTransaction(){
      public boolean transaction() {

        try {
          saveChanges(qdsTMPOUTStdoki);
          saveChanges(qdsTMPOUTStmeskla);
          saveChanges(qdsStanje);
          return true;
        }
        catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
      }
    };
    rT.startTransaction();

/**
 @todo updatirati novi datum knjiženja na skladištu
 */

  }
}
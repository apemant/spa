/****license*****************************************************************
**   file: jpRadniNalog.java
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
import hr.restart.baza.RN;
import hr.restart.baza.Stanje;
import hr.restart.baza.VTRnl;
import hr.restart.baza.dM;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raTwoTableChooser;
import hr.restart.util.startFrame;
import hr.restart.util.sysoutTEST;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: jpRadniNalog</p>
 * <p>Description: Panel za odabir radnog naloga</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: REST-ART</p>
 * Panel koji služi za odabir radnog naloga. Sadrži tekst komponente za organizacijsku jedinicu,
 * godinu i redni broj naloga.<br>
 * Upotreba: instancirati i dodati na odgovaraju\u0107i panel, te zadati na\u010Din dohva\u0107anja i prepisivanja
 * stavki radnog naloga (metodom <code>setMode()</code>). Prilikom validacije
 * pozvati metodu <code>Validacija()</code> koja provjerava popunjenost odgovaraju\u0107ih tekst
 * komponenti na panelu.<p>
 * Dodatno je mogu\u0107e postaviti default organizacijsku jedinicu pomo\u0107u metode
 * <code>setDefaultCORG()</code>. Godina se po defaultu uzima iz teku\u0107eg datuma, a može se
 * i promijeniti metodom <code>setDefaultGOD()</code>.<p>
 * Nakon odabira radnog naloga (nakon validacije), može se pozvati metoda
 * <code>copyRNL()</code>, koja \u0107e ovisno o modu prepisati stavke radnog naloga u poseban
 * DataSet, koji ima kolone CART, CART1, BC, NAZART, JM i KOL (dohva\u0107a se metodom
 * <code>getRNLstavke()</code>).<p> Primjer:<p><pre>
 * jpRadniNalog jprnl = new jpRadniNalog();
 * ...
 * private void jbInit() throws Exception {
 *   ...
 *   jprnl.setMode("I");      // za izdatnice, "R" za ra\u010Dune, "P" za primke
 *   ...
 *   jpNekiDetailPanel.add(jprnl, new XYConstraints(0, 60, -1, -1));
 *   ...
 * }
 * ...
 * public void SetFokusMaster(char mode) {
 *   ...
 *   // popunjavanje tekst komponenti na panelu
 *   jprnl.init(this.getMasterSet().getString("CRADNAL"));
 *   ...
 * }
 * ...
 * public boolean ValidacijaMaster(char mode) {
 *   ...
 *   if (!jprnl.Validacija())
 *     return false;
 *   this.getMasterSet().setString("CRADNAL", jprnl.getCRADNAL());
 *   ...
 * }
 *
 * u nekakvoj aftersave metodi:
 *
 *    jprnl.copyRNL();
 *    DataSet ds = jprnl.getStavkeRNL();
 * </pre>
 * DataSet ds sadrži slogove prepisane iz stavki radnog naloga, kolone CART, CART1,
 * BC, NAZART, JM i KOL. Ako je mod rada "I" (izdatnice), to su upravo slogovi odabrani
 * u dialogu. Za mod "R" to su svi slogovi krajnjih artikala sa radnog naloga (normirani
 * artikli se ekpandaju), a za mod "P" to su samo slogovi normiranih artikala sa radnog naloga.
 * @author ab.f
 * @version 1.0
 */

public class jpRadniNalog extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  sysoutTEST sys = new sysoutTEST(false);

  XYLayout xYLayout1 = new XYLayout();
  JLabel jlRnl = new JLabel();

  JraTextField jraBroj = new JraTextField() {
    public void valueChanged() {
      checkBroj();
    }
  };

  JraButton jbSelRadnal = new JraButton();

  StorageDataSet podaci = new StorageDataSet();
  StorageDataSet leftData = new StorageDataSet();
  StorageDataSet rightData = new StorageDataSet();

  JPanel ttp = new JPanel();
  raTwoTableChooser jpttc = new raTwoTableChooser();
  JDialog selector = null;/* = new JDialog(null, "Odabir materijala iz radnog naloga", true)*/;
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  String copyMode;
  String corg, god, sklad;

  QueryDataSet rnls;
  QueryDataSet rnlsc = new QueryDataSet();
  private boolean isRastavljeno = false;
  private boolean forceTotalExpand = false;

  public void setRastavljeno(boolean brastav){
    isRastavljeno = brastav;
  }

//  QueryDataSet RN;

  /**
   * Default konstruktor.
   */

  public jpRadniNalog() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Metoda koja postavlja na\u010Din dohva\u0107anja i prepisivanja stavki radnog naloga.
   * Najbolje pozvati u <code>jbInit</code> metodi klase koja instancira ovaj panel.<p>
   * @param mode Na\u010Din dohva\u0107anja i kopiranja stavki: "I" (izdatnice) dohva\u0107a neobra\u0111ene radne naloge
   * i kopira sve materijale i robu iz radnog naloga, uklju\u010Duju\u0107i i one iz normiranih artikala;
   * "P" (primke) dohva\u0107a obra\u0111ene radne naloge i kopira noramirane artikle radnog naloga kakvi jesu;
   * "R" (ra\u010Duni) dohva\u0107a obra\u0111ene radne naloge i kopira sve stavke radnog naloga osim normiranih,
   * koje rastavlja  na sastavne artikle i tako prepisuje.
   */

  public void setMode(String mode) {
    copyMode = mode;
    boolean lazyIZDpre = frmParam.getParam("robno", "lazyIZDpre", "N", "Dopustiti prebacivanje" +
      " radnih naloga u predatnice prije izdavanja robe? (D,N)").equalsIgnoreCase("D");    
    Condition status = mode.equalsIgnoreCase("I") ? 
        Condition.in("STATUS", new String[] {"P", "F"}) : Condition.equal("STATUS", "O");
    if (mode.equalsIgnoreCase("P") && lazyIZDpre) 
        status = Condition.in("STATUS", new String[] {"P", "O"});
    Condition corgs = Condition.in("CSKL", OrgStr.getOrgStr().getOrgstrAndCurrKnjig(), "CORG");
    rnls = RN.getDataModule().getFilteredDataSet(corgs.and(status));
/*      
    if (mode.toUpperCase().equals("I")) rnls = dm.getRNp();
    else if (mode.toUpperCase().equals("P")) rnls = dm.getRNo();
    else if (mode.toUpperCase().equals("R")) rnls = dm.getRNo(); */
  }

  private boolean initSelector() {
    if (selector != null) return true;
    Container c = this.getTopLevelAncestor();
    if (c == null) return false;
    if (c instanceof Frame)
      selector = new JraDialog((Frame) c, "Odabir stavki radnog naloga", true);
    else if (c instanceof Dialog)
      selector = new JraDialog((Dialog) c, "Odabir stavki radnog naloga", true);
    else return false;
//    selector = new JDialog((Frame) null, "Odabir materijala iz radnog naloga", true);
    selector.getContentPane().setLayout(new BorderLayout());
    selector.getContentPane().add(ttp, BorderLayout.CENTER);
    selector.getContentPane().add(okp, BorderLayout.SOUTH);
    okp.registerOKPanelKeys(selector);
    selector.pack();
    selector.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        rightData.empty();
        after_Cancel();
      }
    });
    return true;
  }

  /**
   * Metoda koja inicijalizira panel iz šifre radnog naloga. Pozvati u metodi tipa
   * <code>SetFokus()</code>, da bi panel napunio svoje textfield-e. Može se zvati i
   * unutar kakvog <code>NavigationListener</code>-a. <p> Npr. <pre>
   * public void SetFokusMaster(char mode) {
   *   jprnl.init(this.getMasterSet().getString("CRADNAL"));
   *   ...
   * }</pre>
   *
   * @param cradnal Šifra radnog naloga.
   */

  boolean automaticAll = false;

  public void init(String cradnal) {
    System.out.println(cradnal);
    automaticAll = false;
    System.out.println("automatic all false");
    if (cradnal == null || cradnal.trim().equals("")) {
      podaci.setString("CSKL", "");
      //jraBroj.setText("");
      corg = "";
    } else {
      int slashPos, lastDashPos;
      String crg = cradnal.substring(cradnal.indexOf("RNL-")+4, slashPos = cradnal.indexOf("/"));
      String gd = cradnal.substring(slashPos + 1, lastDashPos = cradnal.lastIndexOf("-"));
      String brdok = cradnal.substring(lastDashPos + 1);
      podaci.setString("CSKL", brdok);
      corg = crg;
      god = gd;
    }
    forceTotalExpand = frmParam.getParam("robno", "totalExpand", "N", 
      "Rastaviti normativ kod proizvodnje do kraja? (D/N)").equalsIgnoreCase("D");
    System.out.println(corg + " | " + god + " | " + podaci.getString("CSKL"));
    rnls.open();
  }
  
  public boolean isCheckStanje() {
    return "D".equalsIgnoreCase(frmParam.getParam("rn", "flagStanje", "D", 
        "Oznaèavanje artikala zavedenih na stanju pri prijenosu RNL->IZD (D/N)"));
  }

  public boolean setAutomaticBroj(String br) {
    podaci.setString("CSKL", br);
    return automaticAll = existsCRADNAL();
  }

  /**
   * Metoda koja postavlja default vrijednost za organizacijsku jedinicu na
   * panelu. Može se zvati npr. nakon što korisnik unese CORG na parent
   * panelu, tako da se ista vrijednost kopira i na ovaj panel.<p>
   * @param defCORG Šifra default organizacijske jedinice.
   */

  public void setDefaultCORG(String defCORG) {
    corg = defCORG;
  }
  
  public void setCSKL(String cskl) {
    sklad = cskl;
  }
  
  public void setGod(String god) {
    this.god = god;
  }

  /**
   * Metoda koja vra\u0107a šifru radnog naloga konstruiranu pomo\u0107u podataka
   * unešenih na panelu. Vra\u0107a prazan string ako neki od textfield-ova nije
   * popunjen.<p>
   * @return Šifru radnog naloga.
   */
  public String getCRADNAL() {
    if (corg == null || corg.equals("") || god == null || god.equals("")
        || jraBroj.getText().trim().equals(""))
      return "";
    return Aut.getAut().getCradnal(corg, god, podaci.getString("CSKL"));
  }

  /**
   * Metoda koja provjerava jesu li unešene potrebne vrijednosti na panelu, po uzoru
   * na <code>raMatPodaci</code>. Najbolje pozvati prilikom validacije parent panela.<p>
   * @return true ako su svi podaci unešeni ili ako nedostaje više od jednog podatka,
   * u suprotnom otvara dialog s greškom i vra\u0107a false, uz odgovaraju\u0107e setiranje fokusa.
   */
  public boolean Validacija() {
    System.out.println("validacija");
    if (jraBroj.getText().trim().equals("")) return true;
    if (!existsCRADNAL()) {
      jraBroj.requestFocus();
      JOptionPane.showMessageDialog(this,"Radni nalog ne postoji!","Greška",
        JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  /**
   * Metoda koja dohva\u0107a stavke iz radnog naloga i ubacuje ih u privatni DataSet.
   * Na\u010Din na koji se stavke prepisuju ovisi o parametru type u metodi <code>setDataSets()</code>.
   * Za izdatnice ("I") otvara se prozor gdje se mogu odabrati odre\u0111eni artikli robe i materijala
   * (koji ve\u0107 nisu u tablici), a za ostale dokumente se dohva\u0107aju svi adekvatni artikli sa
   * radnog naloga.
   */
  public boolean copyRNL() {
    boolean chks = isCheckStanje();
    String cradnal = getCRADNAL();
    if (cradnal.equals("")) return false;

    if (copyMode.equals("I") || copyMode.equals("P")) {
      if (!initSelector()) return false;
      leftData.open();
      rightData.open();
      leftData.setTableName("jpRNL");
      rightData.setTableName("jpRNL");
      leftData.empty();
      rightData.empty();
      setVisibleCols();

//      copyUsluge(Asql.getRNLstavke(cradnal), cradnal);
      DataSet rn = RN.getDataModule().getTempSet(Condition.equal("CRADNAL", cradnal));
      rn.open();
      boolean proiz = rn.getString("SERPR").equalsIgnoreCase("P");
//      lookupData.getlookupData().raLocate(dm.getRN(), "CRADNAL", cradnal);
      QueryDataSet pr = new QueryDataSet();
      stdoki.getDataModule().setFilter(pr, "VRDOK = 'RNL' AND CRADNAL = '"+cradnal+"'");
      pr.open();

      if (copyMode.equals("P")) {  // predatnice, bez puno filozofije

        for (pr.first(); pr.inBounds(); pr.next()) {
          if (raVart.isNorma(pr.getInt("CART")) &&
              raVart.isStanje(pr.getInt("CART")) &&
              //Aut.getAut().artTipa(pr.getInt("CART"), "P") &&
              !pr.getString("STATUS").equalsIgnoreCase("Z")) {
            leftData.insertRow(false);
            Aut.getAut().copyArtFields(leftData, pr);
            leftData.setBigDecimal("KOL", pr.getBigDecimal("KOL"));
            leftData.setInt("RBSID", pr.getInt("RBSID"));
          }
        }
      } else {  // izdatnice. provjerava sve artikle u normativu
        Condition cg = Condition.equal("GOD", god).and(Condition.equal("CSKL", sklad));
        QueryDataSet vti = VTRnl.getDataModule().getTempSet(Condition.equal("CRADNAL", cradnal));
        vti.open();
        String[] cols = {"RBSRNL", "BRANCH"};
        String[] vals = new String[2];
        for (pr.first(); pr.inBounds(); pr.next()) {
          DataSet norm = Aut.getAut().expandArt(pr, !proiz || forceTotalExpand);
          vals[0] = String.valueOf(pr.getInt("RBSID"));
          for (norm.first(); norm.inBounds(); norm.next()) {
            vals[1] = norm.getString("BRANCH");
            if (lookupData.getlookupData().raLocate(vti, cols, vals)) {
              if (vti.getInt("RBSIZD") < 0) {
                leftData.insertRow(false);
                Aut.getAut().copyArtFields(leftData, norm);
                leftData.setBigDecimal("KOL", norm.getBigDecimal("KOL"));
                leftData.setString("BRANCH", norm.getString("BRANCH"));
                leftData.setInt("RBSID", pr.getInt("RBSID"));
                if (chks) {
                  DataSet st = Stanje.getDataModule().getTempSet(Condition.equal("CART", norm).and(cg));
                  st.open();
                  if (st.rowCount() == 1) {
                    String naz = (st.getBigDecimal("KOL").compareTo(norm.getBigDecimal("KOL")) >= 0 
                                    ? "(+) " : "(-) ") + norm.getString("NAZART");
                    if (naz.length() > 50) naz = naz.substring(0, 50);
                    leftData.setString("NAZART", naz);
                  }
                }
              }
            }
          }
        }

      }


//      new sysoutTEST(false).prn(pr);
/*      for (pr.first(); pr.inBounds(); pr.next()) {
System.out.println("pr.getString(STATUS) "+pr.getString("STATUS"));
System.out.println("copyMode "+copyMode);

        if (Aut.getAut().artTipa(pr.getInt("CART"), "PRM") &&
            ((pr.getString("STATUS").equalsIgnoreCase("N") && copyMode.equalsIgnoreCase("I")) ||
             (!pr.getString("STATUS").equalsIgnoreCase("Z") && copyMode.equalsIgnoreCase("P")))) {
System.out.println("Unishao");
          if (Aut.getAut().artTipa(pr.getInt("CART"), "P") && isRastavljeno && copyMode.equalsIgnoreCase("I")){
            QueryDataSet tmpRastav = Aut.getAut().expandArt(pr.getInt("CART"),pr.getBigDecimal("KOL"),true);
System.out.println("Normativ rastavljen");
sys.prn(tmpRastav);
System.out.println("KRAH Normativ rastavljen");


            QueryDataSet postojeci =
                hr.restart.util.Util.getNewQueryDataSet("select * from stdoki where cradnal='"+
                cradnal+"' and cartnor="+pr.getInt("CART")+" and vrdok='IZD'",true);
System.out.println(postojeci.getQuery().getQueryString());
sys.prn(postojeci);

            for (tmpRastav.first(); tmpRastav.inBounds(); tmpRastav.next()) {
//              if (hr.restart.util.lookupData.getlookupData().raLocate(postojeci,
//                new String[]{"CART"},new String[]{String.valueOf(
//                tmpRastav.getInt("CART"))})){
//System.out.println("nashao + "+tmpRastav.getInt("CART"));
//                 continue;
//              }
              leftData.insertRow(false);
              Aut.getAut().copyArtFields(leftData, tmpRastav);
              leftData.setInt("CARTNOR", pr.getInt("CART"));
              leftData.setBigDecimal("KOL", tmpRastav.getBigDecimal("KOL"));
              leftData.setInt("RBSID", pr.getInt("RBSID"));
            }
          } else {
            leftData.insertRow(false);
            Aut.getAut().copyArtFields(leftData, pr);
            leftData.setBigDecimal("KOL", pr.getBigDecimal("KOL"));
            leftData.setInt("RBSID", pr.getInt("RBSID"));
          }
        }
      }*/
//      copyUsluge(Asql.getRNLstavkeSkupine(dm.getRN().getString("CSKUPART"), cradnal), cradnal);

      if (leftData.rowCount() > 0) {
        jpttc.initialize();
        selector.pack();
        startFrame.getStartFrame().centerFrame(selector, 0, "Odabir stavki radnog naloga");
        if (!automaticAll) selector.show();
        else copyAllAutomatic();
        return true;
      } else return false;
    } else if (copyMode.equals("R")) {
      leftData.open();


//      copyAllDetail(Asql.getRNLstavke(cradnal));
//
//      lookupData.getlookupData().raLocate(dm.getRN(), new String[] {"CRADNAL"}, new String[] {cradnal});
//
//      copyAllDetail(Asql.getRNLstavkeSkupine(dm.getRN().getString("CSKUPART"), cradnal));

    }
    return true;
  }

  private void copyAllAutomatic() {
    for (leftData.first(); leftData.inBounds(); leftData.next()) {
      rightData.insertRow(false);
      leftData.copyTo(rightData);
    }
    after_OK();
  }

  /**
   * Metoda koja se poziva nakon što se u dialogu odabira materijala pritisne dugme OK.
   */
  public void after_OK() {
  }

  /**
   * Metoda koja se poziva nakon što se u dialogu odabira materijala pritisne dugme Cancel.
   */
  public void after_Cancel() {
  }

//  private void copyAllDetail(QueryDataSet st) {
//    QueryDataSet arts = Aut.getAut().expandArts(st);
//    if (arts.rowCount() > 0) {
//      for (arts.first(); arts.inBounds(); arts.next()) {
//        leftData.insertRow(false);
//        Aut.getAut().copyArtFields(leftData, arts);
//        leftData.setBigDecimal("KOL", arts.getBigDecimal("KOL"));
//      }
//    }
//  }

//  private void copyNorm(String upit) {
//    vl.execSQL(upit);
//    if (vl.RezSet.rowCount() > 0) {
//      vl.RezSet.first();
//      while (vl.RezSet.inBounds()) {
//        if (
//
//      }
//    }
//  }


//  private void copyUsluge(QueryDataSet st, String cradnal) {
//    QueryDataSet arts = Aut.getAut().expandArts(st);
//    sys.prn(arts);
//    if (arts.rowCount() > 0) {
//
//      for (arts.first(); arts.inBounds(); arts.next()) {
//        if (Aut.getAut().artTipa(arts.getInt("CART"), "RM") &&
//           !Aut.getAut().checkStavkaRM(arts.getInt("CART"), cradnal)) {
//          leftData.insertRow(false);
//          Aut.getAut().copyArtFields(leftData, arts);
//          leftData.setBigDecimal("KOL", arts.getBigDecimal("KOL"));
//        }
//      }
//      leftData.post();
//    }
//  }

  private void setVisibleCols() {
    leftData.getColumn("CART").setVisible(Aut.getAut().getCARTdependable(1,0,0));
    leftData.getColumn("CART1").setVisible(Aut.getAut().getCARTdependable(0,1,0));
    leftData.getColumn("BC").setVisible(Aut.getAut().getCARTdependable(0,0,1));
    rightData.getColumn("CART").setVisible(Aut.getAut().getCARTdependable(1,0,0));
    rightData.getColumn("CART1").setVisible(Aut.getAut().getCARTdependable(0,1,0));
    rightData.getColumn("BC").setVisible(Aut.getAut().getCARTdependable(0,0,1));
  }


  /**
   * Metoda vra\u0107a DataSet sa stavkama kopiranim iz radnog naloga.<p>
   * @return DataSet s kolonama CART, CART1, BC, NAZART, JM i KOL.
   */

  public DataSet getStavkeRNL() {
    if (copyMode.equals("I") || copyMode.equals("P")) {
      return rightData;
    } else
      return leftData;
  }

  private void jbInit() throws Exception {

    podaci.setColumns(new Column[] {
      (Column) dm.getDoku().getColumn("CSKL").clone()
    });

    rnlsc.setColumns(dm.getRN().cloneColumns());

    podaci.open();
    podaci.getColumn("CSKL").setCaption("Broj radnog naloga");
    podaci.insertRow(false);
    podaci.post();

//    cartnor.setVisible(com.borland.jbcl.util.TriState.NO);
    leftData.setColumns(new Column[] {
      (Column) dm.getStdoki().getColumn("CART").clone(),
      (Column) dm.getStdoki().getColumn("CART1").clone(),
      (Column) dm.getStdoki().getColumn("BC").clone(),
      (Column) dm.getStdoki().getColumn("NAZART").clone(),
      (Column) dm.getStdoki().getColumn("JM").clone(),
      (Column) dm.getStdoki().getColumn("KOL").clone(),
      (Column) dm.getStdoki().getColumn("RBSID").clone(),
      dM.createStringColumn("BRANCH", 120)
    });
    rightData.setColumns(new Column[] {
      (Column) dm.getStdoki().getColumn("CART").clone(),
      (Column) dm.getStdoki().getColumn("CART1").clone(),
      (Column) dm.getStdoki().getColumn("BC").clone(),
      (Column) dm.getStdoki().getColumn("NAZART").clone(),
      (Column) dm.getStdoki().getColumn("JM").clone(),
      (Column) dm.getStdoki().getColumn("KOL").clone(),
      (Column) dm.getStdoki().getColumn("RBSID").clone(),
      dM.createStringColumn("BRANCH", 120)
    });

    leftData.getColumn("JM").setVisible(0);
    leftData.getColumn("RBSID").setVisible(0);
    leftData.getColumn("BRANCH").setVisible(0);
    leftData.getColumn("KOL").setWidth(6);
    leftData.getColumn("CART").setWidth(5);
    leftData.getColumn("CART1").setWidth(9);
    leftData.getColumn("BC").setWidth(10);
    leftData.getColumn("NAZART").setWidth(20);
    rightData.getColumn("JM").setVisible(0);
    rightData.getColumn("RBSID").setVisible(0);
    rightData.getColumn("BRANCH").setVisible(0);
    rightData.getColumn("KOL").setWidth(6);
    rightData.getColumn("CART").setWidth(5);
    rightData.getColumn("CART1").setWidth(9);
    rightData.getColumn("BC").setWidth(10);
    rightData.getColumn("NAZART").setWidth(20);

    jpttc.rnvSave.setVisible(false);
    jpttc.setLeftDataSet(leftData);
    jpttc.setRightDataSet(rightData);
    jpttc.setLeftPrefferedSize(new Dimension(360,300));
    jpttc.setRightPrefferedSize(new Dimension(360,300));

    xYLayout1.setWidth(285);
    xYLayout1.setHeight(25);
    this.setLayout(xYLayout1);
    jlRnl.setText("Broj radnog naloga");
    jbSelRadnal.setText("...");

    jraBroj.setDataSet(podaci);
    jraBroj.setColumnName("CSKL");

    this.add(jlRnl, new XYConstraints(15, 0, -1, -1));
    this.add(jraBroj, new XYConstraints(150, 0, 100, -1));
    this.add(jbSelRadnal, new XYConstraints(255, 0, 21, 21));

    ttp.add(jpttc);
    ttp.setBorder(BorderFactory.createEtchedBorder());

    jbSelRadnal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jraBroj.requestFocus();
        getSvi();
      }
    });
    /*jraBroj.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
//        System.out.println(podaci.getInt("BRDOK"));
        checkBroj();
      }
    });*/
    jraBroj.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_F9) {
          getSvi();
          e.consume();
        }
      }
    });
  }

  private boolean existsCRADNAL() {
    if (corg == null || corg.equals("") || podaci.getString("CSKL").equals(""))
      return false;
    if (god != null && god.length() > 0 && lookupData.getlookupData().raLocate(
        rnls, new String[] {"CSKL", "GOD", "BRDOK"},
        new String[] {corg, god, podaci.getString("CSKL")})) return true;
    if (lookupData.getlookupData().raLocate(rnls, new String[] {"CSKL", "BRDOK"},
         new String[] {corg, podaci.getString("CSKL")})) {
      god = rnls.getString("GOD");
      return true;
    }
    return false;
  }

  private void checkBroj() {
    if (jraBroj.getText().trim().equals("")) return;
    if (corg == null || corg.equals("")) {
      podaci.setString("CSKL", "");
      jraBroj.setErrText("Nepoznato mjesto troška!");
      jraBroj.this_ExceptionHandling(new Exception());
      jraBroj.setErrText(null);
      return;
    }
    if (!existsCRADNAL())  {
      podaci.setString("CSKL", "");
      jraBroj.setErrText("Nepostoje\u0107i ili pogrešni radni nalog! F9 za dohvat");
      jraBroj.this_ExceptionHandling(new Exception());
      jraBroj.setErrText(null);
      return;
    }
    return;
  }

  private void getSvi() {
    if (corg.equals("")) return;
    //vl.execSQL("SELECT * FROM RN WHERE status = "+status);
//    System.out.println(dm.getRN().getRowFilterListener());
//    System.out.println("" + corg);
    rnlsc.close();
    rnlsc.setQuery(new QueryDescriptor(dm.getDatabase1(),
      rnls.getOriginalQueryString() + " AND cskl = '"+corg+"'"));
    rnlsc.open();

    String[] result = lookupData.getlookupData().lookUp((Window) this.getTopLevelAncestor(), rnlsc,
        new String[] {"GOD", "BRDOK"}, new String[] {"", ""},
        new int[] {1,2,3,12});
    if (result == null) return;
    god = result[0];
    podaci.setString("CSKL", result[1]);
//    System.out.println(corg + " " + god + " " + rnls.getInt("BRDOK"));
  }

  private void cancelPress() {
    selector.hide();
    rightData.empty();
    after_Cancel();
  }

  private void OKPress() {
    selector.hide();
    after_OK();
/*      dm.getStRND().refresh();
      if (raLoader.isLoaderLoaded("hr.restart.robno.frmStavkeRadnogNaloga"))
        frmStavkeRadnogNaloga.getFrmStavkeRadnogNaloga().getJpTableView().fireTableDataChanged();

    } */
  }
}

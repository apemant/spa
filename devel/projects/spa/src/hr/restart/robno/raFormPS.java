/****license*****************************************************************
**   file: raFormPS.java
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

import hr.restart.swing.raMultiLineMessage;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raFormPS extends raObradeZaNovuGodinu{
  public raFormPS() {
    try {
      inicir();
    }
    catch (Exception ex) {
      System.err.println("Probah inicirati formiranje pocetnog mi stanja, ali ne uspjeh :(");
    }
  }

  public void componentShow() {
    defolts();
    jcbStanjeArtiklaNula.setSelected(true);
    jlStGodina.setText("Sa stanja iz godine");
    jlGodina.setText("Form. P.S. za godinu");
  }



  private void inicir() throws Exception{
//    jlGodina.setVisible(false);
//    jraGodina.setVisible(false);
    jcbStanjeArtiklaNula.setVisible(false);
    jcbRazlikaPoZaokruzenju.setVisible(false);

    jlSklad.setText("Skladište");

    xYLayout1.setHeight(145);
    mainPanel.add(jlSklad, new com.borland.jbcl.layout.XYConstraints(15, 55, -1, -1));
    mainPanel.add(jlrSklad, new com.borland.jbcl.layout.XYConstraints(150, 55, 100, -1));
    mainPanel.add(jlrNazSkl, new com.borland.jbcl.layout.XYConstraints(255, 55, 295, -1));
    mainPanel.add(jbSelSklad, new com.borland.jbcl.layout.XYConstraints(555, 55, 21, 21));
    mainPanel.add(jlStGodina,    new com.borland.jbcl.layout.XYConstraints(15, 105, -1, -1));
    mainPanel.add(jraStGodina,    new com.borland.jbcl.layout.XYConstraints(150, 105, 100, -1));
    mainPanel.add(jlGodina,    new com.borland.jbcl.layout.XYConstraints(15, 80, -1, -1));
    mainPanel.add(jraGodina,    new com.borland.jbcl.layout.XYConstraints(150, 80, 100, -1));
  }

  public boolean Validacija(){
    onOff(false);
    if (fieldSet.getString("CORG").equals("")) {
      onOff(true);
      jlrKnjig.requestFocus();
      JOptionPane.showMessageDialog(this.mainPanel, "Obvezatan unos - ORGANIZACIJA !", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    setDummys();
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(fieldSet);
    skladische();
//    syst.prn(fieldSet);
    return true;
  }

  private boolean jelovogotovo = false;

  public void okPress(){
    if (fieldSet.getString("CSKL").equals("")){
      svaSkladista();
    } else {
      jednoSkladiste();
    }
//    jelovogotovo=true;
    //TODO produkcija.... popraviti po završetku
    if (saveDataInMyneTransaction(false, false, true, false)){
      jelovogotovo = true;
    } else {
      jelovogotovo = false;
    }
  }

  public void afterOKPress(){
    if (jelovogotovo){
    JOptionPane.showMessageDialog(this.mainPanel,
                                  new raMultiLineMessage(new String[] {"USPJEŠNO ;)","Over, gone, finished, done, out...","See U"}),
                                  "Formiranje po\u010Detnog stanja...",
                                  JOptionPane.INFORMATION_MESSAGE);
    } else {
    JOptionPane.showMessageDialog(this.mainPanel,
                                  new raMultiLineMessage(new String[] {"Formiranje po\u010Detnog stanja nije uspjelo"}),
                                  "Formiranje po\u010Detnog stanja...",
                                  JOptionPane.ERROR_MESSAGE);
    }

    rcc.EnabDisabAll(mainPanel, true);
    defolts();
    jlrKnjig.requestFocus();
  }

  private void svaSkladista(){
    skladistaSet.first();
    do {
      QueryDataSet brojNaStanju = ut.getNewQueryDataSet("select count(*) from stanje where cskl='" + skladistaSet.getString("CSKL") + "' and god = '"+fieldSet.getString("SGOD")+"'");
      if(vl.getSetCount(brojNaStanju,0) > 0){
        dokuStdoku(skladistaSet.getString("CSKL"));
      }
    } while (skladistaSet.next());
  }

  private void jednoSkladiste(){
    QueryDataSet brojNaStanju = ut.getNewQueryDataSet("select count(*) from stanje where cskl='" + fieldSet.getString("CSKL") + "' and god = '"+fieldSet.getString("SGOD")+"'");
    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    System.out.println("iz godine - " +fieldSet.getString("SGOD"));
//    System.out.println("u  godinu - " +fieldSet.getString("GODINA"));
    syst.prn(brojNaStanju);
    if(vl.getSetCount(brojNaStanju,0) > 0){
      dokuStdoku(fieldSet.getString("CSKL"));
    }
  }

  public void dokuStdoku(String skladiste){
    String queryString = "select stanje.*, artikli.cart1, artikli.bc, artikli.nazart, " +
                                "artikli.jm, sklad.vrzal from stanje, artikli, sklad where stanje.cskl='" +
                                skladiste + "' and stanje.god='"+fieldSet.getString("SGOD")+"' and stanje.cart=artikli.cart and stanje.cskl=sklad.cskl" +
                                " and stanje.kol != 0 order by stanje.cart";

//    System.out.println("qS ---\n"+queryString);

    QueryDataSet qdsDokuStdoku = ut.getNewQueryDataSet(queryString);

//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(qdsDokuStdoku);

//    setNoDataAndReturnImmediately();

    if (qdsDokuStdoku.rowCount() > 0){
      Integer broj= vl.findSeqInteger(qdsDokuStdoku.getString("CSKL") + "PST" + fieldSet.getString("GODINA"));
      insertIntoDoku(qdsDokuStdoku, broj);

      rbr = 0;
      do {
        insertIntoStDoku(qdsDokuStdoku);
      } while (qdsDokuStdoku.next());
    }
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(dummyDoku);
//    syst.prn(dummyStDoku);

  }

  public void skladische(){
    try {
      knjigodSet = hr.restart.baza.Sklad.getDataModule().getFilteredDataSet("CSKL='" + fieldSet.getString("CSKL")+"'");
      knjigodSet.open();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
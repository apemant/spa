/****license*****************************************************************
**   file: frmDvijeGodine.java
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

public class frmDvijeGodine extends raObradeZaNovuGodinu {

  public frmDvijeGodine() {
    try {
//      this.mainPanel.remove(jcbStanjeArtiklaNula);
      this.mainPanel.remove(jcbPrijelazBezObrade);
      xYLayout1.setHeight(135);
    }
    catch (Exception ex) {}
  }

  private boolean uspjesnost = false;

  public void okPress(){
//    long time = System.currentTimeMillis();
    jcbStanjeArtiklaNula.setSelected(true);
    jcbRazlikaPoZaokruzenju.setSelected(false);
    arrayCounter = 0;
//    System.out.println("1 - " + (System.currentTimeMillis() - time)); //XDEBUG delete when no more needed
    makeSkladisteSet();
//    System.out.println("2 - " + (System.currentTimeMillis() - time)); //XDEBUG delete when no more needed
    if(saveDataInMyneTransaction(false, true, true, true)){
      uspjesnost = true;
    } else {
      uspjesnost = false;
    }
//    System.out.println("3 - " + (System.currentTimeMillis() - time)); //XDEBUG delete when no more needed
  }


  public void afterOKPress(){
    if (uspjesnost){
      JOptionPane.showMessageDialog(this.mainPanel,
                                    new raMultiLineMessage(new String[] {"Rad u dvije godine uspješno iniciran"}),
                                    "Rad u dvije godine",
                                    JOptionPane.INFORMATION_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(this.mainPanel,
                                    new raMultiLineMessage(new String[] {"Iniciranje rada u dvije godine nije uspjelo",
          "Stare vrijednosti su ostale nepromjenjene"}),
          "Rad u dvije godine",
          JOptionPane.ERROR_MESSAGE);
    }
    onOff(true);
    defolts();
    jlrKnjig.requestFocus();
  }

  private void makeSkladisteSet() {
    skladistaSet.first();
    System.out.println("Petlja u makeSkladSet()"); //XDEBUG delete when no more needed
    do {
      QueryDataSet brojNaStanju = ut.getNewQueryDataSet("select count(*) from stanje where cskl='" + skladistaSet.getString("CSKL") + "'");/** @todo e ovde jos provjerit cili kljuc!!! */
      System.out.println("select count(*) from stanje where cskl='" + skladistaSet.getString("CSKL") + "'"); //XDEBUG delete when no more needed
      if(vl.getSetCount(brojNaStanju,0) > 0){
        /* punim dummy-je za svako skladiste, stanje, doku, stdoku*/
        addToDokuStdoku(skladistaSet.getString("CSKL"),""); /* dokument pocetno stanje sa pripadajucim zaglavljem */
        addToStanje(skladistaSet.getString("CSKL"),""); /* stanje */
      }
    } while (skladistaSet.next());
    System.out.println("Izasao iz petlje u makeSkladistaSet()"); //XDEBUG delete when no more needed
      /* priprema stringa za update tablica knjigod i sklad */
    updateSklad = ss.getStringObradeDvijeGodineUpdateSklad(fieldSet.getString("CORG"),"");
//    System.out.println("update skladista - "+updateSklad);
    updateKnjigod = ss.getStringObradeDvijeGodineUpdateKnjigod(fieldSet.getString("CORG"), "");
  }


  public boolean Validacija(){
    onOff(false);

    if (postojiLiRadUDvijeGodine()) return false;

    if (fieldSet.getString("CORG").equals("")) {
      onOff(true);
      jlrKnjig.requestFocus();
      JOptionPane.showMessageDialog(this.mainPanel, "Obvezatan unos - ORGANIZACIJA !", "Greška", JOptionPane.ERROR_MESSAGE);
      defolts();
      return false;
    }
    if (JOptionPane.showConfirmDialog(this.mainPanel,"Da li ste sigurni da želite aktivirati rad u dvije godine ?","Rad u dvije godine",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
      onOff(true);
      defolts();
      jlrKnjig.requestFocus();
      return false;
    }
    if (JOptionPane.showConfirmDialog(this.mainPanel,"Da li ste STVARNO SIGURNI da želite aktivirati rad u dvije godine ?","Rad u dvije godine",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
      onOff(true);
      defolts();
      jlrKnjig.requestFocus();
      return false;
    }
    return true;
  }


  private boolean postojiLiRadUDvijeGodine(){
    if (radU2God) {
      JOptionPane.showMessageDialog(this.mainPanel,
                                    new raMultiLineMessage(new String[]
                                    {"Akcija nije mogu\u0107a",
          "Rad u dvije godine je ve\u0107 napravljen!"}),
          "Obavijest",
          JOptionPane.WARNING_MESSAGE);
      onOff(true);
      defolts();
      jlrKnjig.requestFocus();
      return true;
    }

    arrayCounter = skladistaSet.rowCount();

    setDummys();
    updateSkladArray = new String[arrayCounter];
    return false;
  }
}
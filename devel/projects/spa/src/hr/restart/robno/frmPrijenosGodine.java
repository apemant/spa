/****license*****************************************************************
**   file: frmPrijenosGodine.java
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
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raLoader;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;

public class frmPrijenosGodine extends raObradeZaNovuGodinu {
  QueryDataSet stanjeNovaGodina;

  public frmPrijenosGodine() {
  }

  private boolean jeldobro = false;

  public void okPress(){
    if (radU2God && jcbPrijelazBezObrade.isSelected()){
      System.out.println("samo updejtam knjigod i sklad"); //XDEBUG delete when no more needed
      setUpdateStringz();
      if (saveDataInMyneTransaction(false, false, false, true)){
        jeldobro = true;
      } else {
        jeldobro = false;
      }
    } else  {
      countSkladista();
      if (saveDataInMyneTransaction(false, true, true, true)){
        jeldobro = true;
      } else {
        jeldobro = false;
      }
    }
  }
  
  public static void updateSklad(String cskl, String god) {
    raLoader.load("hr.restart.robno.frmPrijenosGodine");
    
    frmPrijenosGodine fpg = (frmPrijenosGodine) frmPrijenosGodine.inst;
    fpg.updateSingle(cskl, god);    
  }
  
  private void updateSingle(String cskl, String god) {
    dummyDoku = ut.getNewQueryDataSet("select * from doku where god='" + god + "' AND cskl='" + cskl + "' AND vrdok='PST'");
    dummyStDoku = ut.getNewQueryDataSet("select * from stdoku where god='" + god + "' AND cskl='" + cskl + "' AND vrdok='PST'");
    dummyStanje = ut.getNewQueryDataSet("select * from stanje where god='" + god + "' AND cskl='" + cskl + "'");
    
    updateSklad(cskl, Integer.toString(Integer.parseInt(god) - 1), true);
    
    saveDataInMyneTransaction(false, true, true, false);
  }

  public void afterOKPress(){
    
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
    
//    st.prn(dummyDoku);
//    st.prn(dummyStDoku);
//    st.prn(dummyStanje);
    
    if (jeldobro){
    JOptionPane.showMessageDialog(this.mainPanel,
                                  new raMultiLineMessage(new String[] {"Prijelaz u novu godinu zavrsen"}),
                                  "Prijelaz u novu godinu",
                                  JOptionPane.INFORMATION_MESSAGE);
    } else {
    JOptionPane.showMessageDialog(this.mainPanel,
                                  new raMultiLineMessage(new String[] {"Prijelaz u novu godinu nije uspio",
                                                                       "Stare vrijednosti su ostale nepromjenjene"}),
                                  "Prijelaz u novu godinu",
                                  JOptionPane.ERROR_MESSAGE);
    }
    onOff(true);
    this.setVisible(false);
//    defolts();
//    jlrKnjig.requestFocus();
  }

  public boolean Validacija(){
    onOff(false);
    if (fieldSet.getString("CORG").equals("")) {
      onOff(true);
      jlrKnjig.requestFocus();
      JOptionPane.showMessageDialog(this.mainPanel, "Obvezatan unos - ORGANIZACIJA !", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (JOptionPane.showConfirmDialog(this.mainPanel,
                                      "Da li želite napraviti prijenos u novu godinu ?",
                                      "Prijenos u novu godinu",
                                      JOptionPane.YES_NO_OPTION,
                                      JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
      onOff(true);
      defolts();
      jlrKnjig.requestFocus();
      return false;
    }
//    if (JOptionPane.showConfirmDialog(this.mainPanel,
//                                      new raMultiLineMessage(new String[]
//                                      {"Prometi iz stare godine \u0107e biti pobrisani!",
//                                       "Ako nemate sigurnosnu kopiju vaših podataka",
//                                       "preporu\u010Damo da je napravite prije nego što",
//                                       "           nastavite sa obradom",
//                                      "",
//                                      "Želite li nastaviti?"}),
//                                      "PREPORUKA",
//                                      JOptionPane.YES_NO_OPTION,
//                                      JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
//      onOff(true);
//      defolts();
//      jlrKnjig.requestFocus();
//      return false;
//    }
    return true;
  }

  private void kitchmaPrijenosa(){
    skladistaSet.first();
    arrayCounter = 0;
    System.out.println("!radU2God je " + !radU2God);
    System.out.println("radU2God - " + radU2God);
    if (!radU2God){
      setDummys();
      do {
        QueryDataSet brojNaStanju = ut.getNewQueryDataSet("select count(*) from stanje where cskl='" + skladistaSet.getString("CSKL") + "'");
//        System.out.println("brojNaStanju COUNT = " + vl.getSetCount(brojNaStanju,0));
        if(vl.getSetCount(brojNaStanju,0) > 0){
          addToDokuStdoku(skladistaSet.getString("CSKL"), "");
          addToStanje(skladistaSet.getString("CSKL"), "");
        }
      } while (skladistaSet.next());

    } else {
      setDummysFull(fieldSet.getString("GODINA"));
      do {
        checkPrometStaraGodina(skladistaSet.getString("CSKL"));
//        updateSkladArray();
      } while (skladistaSet.next());
    }
    setUpdateStringz();
  }

//  private void updateSkladArray() {
//    updateSkladArray[arrayCounter++] = updateSklad;
//  }

  private void setUpdateStringz() {
    updateSklad = ss.getStringObradeDvijeGodineUpdateSklad(fieldSet.getString("CORG"),fieldSet.getString("GODINA"));
    updateKnjigod = ss.getStringObradeDvijeGodineUpdateKnjigod(fieldSet.getString("CORG"), fieldSet.getString("GODINA"));
  }
  
  
  private void updateSklad(String cskl, String god, boolean isNull) {
    rbr = 0;
    for (dummyStDoku.first(); dummyStDoku.inBounds(); dummyStDoku.next()) 
      if (dummyStDoku.getShort("RBR") > rbr)
        rbr = dummyStDoku.getShort("RBR");
    
    QueryDataSet old = ss.getObradeRadDvijeGodineSetDokuStdokuPocetnoStanje(cskl, god, isNull);
    for (old.first(); old.inBounds(); old.next()) {
      if (!lookupData.getlookupData().raLocate(dummyStanje, "CART", Integer.toString(old.getInt("CART")))) {
        insertIntoStDoku(old);
        insetrIntoStanje(old, dummyDoku.getString("GOD"));
        continue;
      }
      
      System.out.println(old);
      
      BigDecimal tKol = old.getBigDecimal("KOL");
      BigDecimal tVri = old.getBigDecimal("VRI");
      BigDecimal tNab = old.getBigDecimal("NABUL").subtract(old.getBigDecimal("NABIZ"));
      BigDecimal tMar = old.getBigDecimal("MARUL").subtract(old.getBigDecimal("MARIZ"));
      BigDecimal tPor = old.getBigDecimal("PORUL").subtract(old.getBigDecimal("PORIZ"));
      
      if (dummyStanje.getBigDecimal("KOLPS").compareTo(tKol) == 0 &&
          dummyStanje.getBigDecimal("VPS").compareTo(tVri) == 0 &&
          dummyStanje.getBigDecimal("NABPS").compareTo(tNab) == 0 &&
          dummyStanje.getBigDecimal("MARPS").compareTo(tMar) == 0 &&
          dummyStanje.getBigDecimal("PORPS").compareTo(tPor) == 0) continue;
      
      Aus.set(dummyStanje, "NC", old);
      Aus.set(dummyStanje, "VC", old);
      Aus.set(dummyStanje, "MC", old);
      Aus.set(dummyStanje, "ZC", old);

      Aus.sub(dummyStanje, "KOLUL", "KOLPS");
      Aus.set(dummyStanje, "KOLPS", tKol);
      Aus.add(dummyStanje, "KOLUL", "KOLPS");
      Aus.sub(dummyStanje, "KOL", "KOLUL", "KOLIZ");
      
      Aus.sub(dummyStanje, "NABUL", "NABPS");
      Aus.set(dummyStanje, "NABPS", tNab);
      Aus.add(dummyStanje, "NABUL", "NABPS");
      
      Aus.sub(dummyStanje, "MARUL", "MARPS");
      Aus.set(dummyStanje, "MARPS", tMar);
      Aus.add(dummyStanje, "MARUL", "MARPS");
      
      Aus.sub(dummyStanje, "PORUL", "PORPS");
      Aus.set(dummyStanje, "PORPS", tPor);
      Aus.add(dummyStanje, "PORUL", "PORPS");
      
      Aus.sub(dummyStanje, "VUL", "VPS");
      Aus.set(dummyStanje, "VPS", tVri);
      Aus.add(dummyStanje, "VUL", "VPS");
      Aus.sub(dummyStanje, "VRI", "VUL", "VIZ");
      
      
      if (!lookupData.getlookupData().raLocate(dummyStDoku, "CART",  Integer.toString(old.getInt("CART")))) {
        insertIntoStDoku(old);
        continue;
      }
      
      System.out.println("before prom " + dummyStDoku);
      
      Aus.set(dummyStDoku, "KOL", old);
      
      Aus.set(dummyStDoku, "NC", old);
      Aus.set(dummyStDoku, "VC", old);
      Aus.set(dummyStDoku, "MC", old);
      Aus.set(dummyStDoku, "ZC", old);

      Aus.set(dummyStDoku, "IZAD", tVri);
      Aus.set(dummyStDoku, "INAB", tNab);
      Aus.set(dummyStDoku, "IMAR", tMar);
      Aus.set(dummyStDoku, "IPOR", tPor);
      
      System.out.println("after prom " + dummyStDoku);
      
    }
  }

  private void checkPrometStaraGodina(String skladiste){
    
    rbr = 0;
    for (dummyStDoku.first(); dummyStDoku.inBounds(); dummyStDoku.next()) 
      if (dummyStDoku.getString("CSKL").equals(skladiste) && dummyStDoku.getShort("RBR") > rbr)
        rbr = dummyStDoku.getShort("RBR");
    
    QueryDataSet provjeraOld = ss.getObradeRadDvijeGodineSetDokuStdokuPocetnoStanje(skladiste, skladistaSet.getString("GODINA"), isPrijenosStanjaNula());
    provjeraOld.first();
    
    do{

      /**
       * Lociram stavku pomoæu dijela kljuèa CSKL, jer je dummyDoku veæ filtriran  dataset
       * sa podacima zaglavlja ulaznog dokumenta PST iz tekuæe godine 
       */
      lookupData.getlookupData().raLocate(dummyDoku,
                                          new String[] {"CSKL"},
                                          new String[] {provjeraOld.getString("CSKL")});
      
      /**
       * Lociram stavku pomoæu dijela kljuèa CSKL i CART, jer je dummyStanje veæ filtriran dataset sa
       * podacima stanja iz tekuæe godine. vidi setDummysFull()
       */
      if (lookupData.getlookupData().raLocate(dummyStanje,
                                              new String[] {"CSKL","CART"},
                                              new String[] {provjeraOld.getString("CSKL"),
                                              String.valueOf(provjeraOld.getInt("CART"))})){
        
        

        /**
         * Pronalazim promet u prošloj godini na naèin da provjerava kolièinu poèetnog stanja (KOLPS) u datasetu
         * dummyStanje - stanje formirano poèetkom rada u dvije godine, sa kolièinom na stanju u prošloj godini
         * (KOL u datasetu provjeraOld).
         * Ako se kolièine razlikuju, postoji promet u staroj godini, i sukladno tome se ažurira 
         * slog stanja u tekuæoj godini.
         */
        if (dummyStanje.getBigDecimal("KOLPS").compareTo(provjeraOld.getBigDecimal("KOL")) != 0){

          System.out.println("postoji razlika za artikl " + dummyStanje.getInt("CART"));
          
          /**
           * razlika - razlika u kojièini u staroj i novoj godini.
           * Ako je razlika negativno bilo je prodaje u staroj godini
           * Ako je razlika pozitivna bilo je nabave u staroj godini
           */
          BigDecimal razlika = provjeraOld.getBigDecimal("KOL").subtract(dummyStanje.getBigDecimal("KOLPS"));
          
          
          System.out.println("Razlika je "+ razlika);
          
          
          /**
           * Cijene se kompletno prepisuju iz stanja u staroj godini
           */
          dummyStanje.setBigDecimal("NC", provjeraOld.getBigDecimal("NC"));
          dummyStanje.setBigDecimal("VC", provjeraOld.getBigDecimal("VC"));
          dummyStanje.setBigDecimal("MC", provjeraOld.getBigDecimal("MC"));
          dummyStanje.setBigDecimal("ZC", provjeraOld.getBigDecimal("ZC"));

          /**
           * Kolièina poèetnog stanja se prepisuje iz kolièine stanja u staroj godini
           */
          dummyStanje.setBigDecimal("KOLPS", provjeraOld.getBigDecimal("KOL"));
          /**
           * Kolièina ulaza se ažurira sa razlikom zbog moguænosti prometa u tekuæoj godini
           */
          dummyStanje.setBigDecimal("KOLUL", dummyStanje.getBigDecimal("KOLUL").add(razlika));
          /**
           * Kolièina zalihe se ažurira sa razlikom zbog moguænosti prometa u tekuæoj godini
           */
          dummyStanje.setBigDecimal("KOL", dummyStanje.getBigDecimal("KOLUL").subtract(dummyStanje.getBigDecimal("KOLIZ")));

//          dummyStanje.setBigDecimal("KOLPS", provjeraOld.getBigDecimal("KOL")); //dummyStanje.getBigDecimal("KOLPS").add(razlika));
//          dummyStanje.setBigDecimal("KOLUL", dummyStanje.getBigDecimal("KOLUL").add(razlika));
//          dummyStanje.setBigDecimal("KOL", dummyStanje.getBigDecimal("KOLUL").subtract(dummyStanje.getBigDecimal("KOLIZ")));
//          }
          


          if(provjeraOld.getString("VRZAL").equals("N")) { //-skladište se vodi po metodi prosjeèna nabavna cijena
            
            /**
             * 
             */
//          dummyStanje.setBigDecimal("NABPS", provjeraOld.getBigDecimal("NABUL").subtract(provjeraOld.getBigDecimal("NABIZ")));
//          dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("NC")));
            
            dummyStanje.setBigDecimal("NABPS", dummyStanje.getBigDecimal("KOLPS").multiply(dummyStanje.getBigDecimal("NC"))); //-nabavni iznos poèetnog stanja
            dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("KOLUL").multiply(dummyStanje.getBigDecimal("NC"))); //-nabavni iznos ulaza
                  
          } else if(provjeraOld.getString("VRZAL").equals("V")) { //-skladište se vodi po metodi zadnja prodajna cijena bez poreza
            
            /**
             *
             */
//          dummyStanje.setBigDecimal("NABPS", provjeraOld.getBigDecimal("NABUL").subtract(provjeraOld.getBigDecimal("NABIZ")));
//          dummyStanje.setBigDecimal("MARPS", provjeraOld.getBigDecimal("MARUL").subtract(provjeraOld.getBigDecimal("MARIZ")));
//
//          dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("NC")));
//          dummyStanje.setBigDecimal("MARUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("VC").subtract(dummyStanje.getBigDecimal("NC"))));
            
            dummyStanje.setBigDecimal("NABPS", dummyStanje.getBigDecimal("KOLPS").multiply(dummyStanje.getBigDecimal("NC"))); //-nabavni iznos poèetnog stanja
            dummyStanje.setBigDecimal("MARPS", dummyStanje.getBigDecimal("KOLPS").multiply(dummyStanje.getBigDecimal("VC")).
      			 							 subtract(dummyStanje.getBigDecimal("KOLPS").multiply(dummyStanje.getBigDecimal("NC")))); //-iznos marže poèetnog stanja

            dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("KOLUL").multiply(dummyStanje.getBigDecimal("NC"))); //-nabavni iznos ulaza
            dummyStanje.setBigDecimal("MARUL", dummyStanje.getBigDecimal("KOLUL").multiply(dummyStanje.getBigDecimal("VC")).
      			 							 subtract(dummyStanje.getBigDecimal("KOLUL").multiply(dummyStanje.getBigDecimal("NC")))); //-iznos marže ulaza
          
          } else { //-skladište se vodi po metodi zadnja prodajna cijena s porezom
            
            /**
             * 
             */
//          dummyStanje.setBigDecimal("NABPS", provjeraOld.getBigDecimal("NABUL").subtract(provjeraOld.getBigDecimal("NABIZ")));
//          dummyStanje.setBigDecimal("MARPS", provjeraOld.getBigDecimal("MARUL").subtract(provjeraOld.getBigDecimal("MARIZ")));
//          dummyStanje.setBigDecimal("PORPS", provjeraOld.getBigDecimal("PORUL").subtract(provjeraOld.getBigDecimal("PORIZ")));
//
//          dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("NC")));
//          dummyStanje.setBigDecimal("MARUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("VC").subtract(dummyStanje.getBigDecimal("NC"))));
//          dummyStanje.setBigDecimal("PORUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("MC").subtract(dummyStanje.getBigDecimal("VC"))));
            
            dummyStanje.setBigDecimal("NABPS", dummyStanje.getBigDecimal("KOLPS").multiply(dummyStanje.getBigDecimal("NC"))); //-nabavni iznos poèetnog stanja
            dummyStanje.setBigDecimal("MARPS", provjeraOld.getBigDecimal("VC").multiply(dummyStanje.getBigDecimal("KOLPS")).
      			 							 subtract(dummyStanje.getBigDecimal("NC").multiply(dummyStanje.getBigDecimal("KOLPS")))); //-iznos marže poèetnog stanja
            dummyStanje.setBigDecimal("PORPS", dummyStanje.getBigDecimal("MC").multiply(dummyStanje.getBigDecimal("KOLPS")).
      			 							 subtract(dummyStanje.getBigDecimal("VC").multiply(provjeraOld.getBigDecimal("KOLPS")))); //-iznos poreza poèetnog stanja

            dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("KOLUL").multiply(dummyStanje.getBigDecimal("NC"))); //-nabavni iznos ulaza
            dummyStanje.setBigDecimal("MARUL", dummyStanje.getBigDecimal("KOLUL").multiply(dummyStanje.getBigDecimal("VC")).
      			 							 subtract(dummyStanje.getBigDecimal("KOLUL").multiply(dummyStanje.getBigDecimal("NC")))); //-iznos marže ulaza
            dummyStanje.setBigDecimal("PORUL", dummyStanje.getBigDecimal("KOLUL").multiply(dummyStanje.getBigDecimal("MC")).
      			 							 subtract(dummyStanje.getBigDecimal("KOLUL").multiply((dummyStanje.getBigDecimal("VC"))))); //-iznos poreza ulaza
          
          }

//          if(provjeraOld.getString("VRZAL").equals("N")) {
//            dummyStanje.setBigDecimal("NABPS", provjeraOld.getBigDecimal("NABUL").subtract(provjeraOld.getBigDecimal("NABIZ")));
//            dummyStanje.setBigDecimal("MARPS", _Main.nul);
//            dummyStanje.setBigDecimal("PORPS", _Main.nul);
//
//            dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("NC")));
//          } else if(provjeraOld.getString("VRZAL").equals("V")) {
//            dummyStanje.setBigDecimal("NABPS", provjeraOld.getBigDecimal("NABUL").subtract(provjeraOld.getBigDecimal("NABIZ")));
//            dummyStanje.setBigDecimal("MARPS", provjeraOld.getBigDecimal("MARUL").subtract(provjeraOld.getBigDecimal("MARIZ")));
//            dummyStanje.setBigDecimal("PORPS", _Main.nul);
//
//            dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("NC")));
//            dummyStanje.setBigDecimal("MARUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("VC").subtract(dummyStanje.getBigDecimal("NC"))));
//          } else if(provjeraOld.getString("VRZAL").equals("M")) {
//            dummyStanje.setBigDecimal("NABPS", provjeraOld.getBigDecimal("NABUL").subtract(provjeraOld.getBigDecimal("NABIZ")));
//            dummyStanje.setBigDecimal("MARPS", provjeraOld.getBigDecimal("MARUL").subtract(provjeraOld.getBigDecimal("MARIZ")));
//            dummyStanje.setBigDecimal("PORPS", provjeraOld.getBigDecimal("PORUL").subtract(provjeraOld.getBigDecimal("PORIZ")));
//
//            dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("NC")));
//            dummyStanje.setBigDecimal("MARUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("VC").subtract(dummyStanje.getBigDecimal("NC"))));
//            dummyStanje.setBigDecimal("PORUL", dummyStanje.getBigDecimal("KOL").multiply(dummyStanje.getBigDecimal("MC").subtract(dummyStanje.getBigDecimal("VC"))));
//          }

          /**
           * Postavljam vrijednost poèetnog stanja da je jednaka vrijednosti satnja stare godine
           */
          dummyStanje.setBigDecimal("VPS", provjeraOld.getBigDecimal("VRI")); //- vrijednost poèetnoga stanja

          /**
           * Provjera da li je vrijednost zalihe jednaka kolièini pomnoženoj sa cijenom zalihe
           */
          if (provjeraOld.getBigDecimal("VRI").compareTo(provjeraOld.getBigDecimal("KOL").multiply(provjeraOld.getBigDecimal("ZC"))) != 0) {
            dummyStanje.setBigDecimal("VRI", provjeraOld.getBigDecimal("KOL").multiply(provjeraOld.getBigDecimal("ZC")));
          }
          
          /**
           * Ovo 
           */
          dummyStanje.setBigDecimal("VUL", (provjeraOld.getBigDecimal("KOLUL")).multiply(provjeraOld.getBigDecimal("ZC"))); //- vrijednost ulaza
          dummyStanje.setBigDecimal("VRI", (provjeraOld.getBigDecimal("KOLIZ")).multiply(provjeraOld.getBigDecimal("ZC"))); //- vrijednost zalihe
          


          BigDecimal ulazi = dummyStanje.getBigDecimal("NABUL").add(dummyStanje.getBigDecimal("MARUL").add(dummyStanje.getBigDecimal("PORUL")));
          BigDecimal pocst = dummyStanje.getBigDecimal("NABPS").add(dummyStanje.getBigDecimal("MARPS").add(dummyStanje.getBigDecimal("PORPS")));
          
          if (ulazi.compareTo(dummyStanje.getBigDecimal("VUL")) != 0){
            dummyStanje.setBigDecimal("MARUL",(dummyStanje.getBigDecimal("MARUL").add(dummyStanje.getBigDecimal("VUL").subtract(ulazi))));
            System.out.println("Provjera NABUL+MARUL+PORUL = VUL - POPRAVLJENO!");
          } else {
            System.out.println("Provjera NABUL+MARUL+PORUL = VUL - OK!");
          }

          if (pocst.compareTo(dummyStanje.getBigDecimal("VPS")) != 0){
            dummyStanje.setBigDecimal("MARPS",(dummyStanje.getBigDecimal("MARPS").add(dummyStanje.getBigDecimal("VPS").subtract(pocst))));
            System.out.println("Provjera NABPS+MARPS+PORPS = VPS - POPRAVLJENO!");
          } else {
            System.out.println("Provjera NABPS+MARPS+PORPS = VPS - OK!");
          }
          
//          dummyStanje.setBigDecimal("VPS", provjeraOld.getBigDecimal("VRI"));
//          dummyStanje.setBigDecimal("VUL", dummyStanje.getBigDecimal("NABUL").add(dummyStanje.getBigDecimal("MARUL").add(dummyStanje.getBigDecimal("PORUL"))));
//          dummyStanje.setBigDecimal("VRI", dummyStanje.getBigDecimal("VUL").subtract(dummyStanje.getBigDecimal("VIZ")));

          lookupData.getlookupData().raLocate(dummyStDoku,
                                              new String[] {"CSKL","CART"},
                                              new String[] {provjeraOld.getString("CSKL"),
                                              String.valueOf(provjeraOld.getInt("CART"))});

          dummyStDoku.setBigDecimal("KOL", provjeraOld.getBigDecimal("KOL"));
          
          dummyStDoku.setBigDecimal("NC", provjeraOld.getBigDecimal("NC"));
          dummyStDoku.setBigDecimal("VC", provjeraOld.getBigDecimal("VC"));
          dummyStDoku.setBigDecimal("MC", provjeraOld.getBigDecimal("MC"));
          dummyStDoku.setBigDecimal("DC", provjeraOld.getBigDecimal("NC"));
          dummyStDoku.setBigDecimal("ZC", provjeraOld.getBigDecimal("ZC"));
          
          
//          dummyStDoku.setBigDecimal("DC", provjeraOld.getBigDecimal("NC"));
//          dummyStDoku.setBigDecimal("MAR", provjeraOld.getBigDecimal("VC").subtract(provjeraOld.getBigDecimal("NC")));
//          
//          dummyStDoku.setBigDecimal("IDOB", provjeraOld.getBigDecimal("NC").multiply(provjeraOld.getBigDecimal("KOL")));
//          dummyStDoku.setBigDecimal("INAB", provjeraOld.getBigDecimal("NC").multiply(provjeraOld.getBigDecimal("KOL")));
//          dummyStDoku.setBigDecimal("IBP" , provjeraOld.getBigDecimal("VC").multiply(provjeraOld.getBigDecimal("KOL")));
//          dummyStDoku.setBigDecimal("ISP" , provjeraOld.getBigDecimal("MC").multiply(provjeraOld.getBigDecimal("KOL")));
//          dummyStDoku.setBigDecimal("IZAD", provjeraOld.getBigDecimal("ZC").multiply(provjeraOld.getBigDecimal("KOL")));


          /**
           * Kod punjenja IZAD-a prepisivanjem vrijednost sa stanja da li radim dobro??
           * Naime, da li je vrijednost (VRI) stanja po metodi voðenja skladišta???
           */
          dummyStDoku.setBigDecimal("IZAD", provjeraOld.getBigDecimal("VRI"));  // iznos zaduženja je vrijednost sa stanja
          
          /**
           * Provjera da li je vrijednost zalihe jednaka kolièini pomnoženoj sa cijenom zalihe
           */
          if (provjeraOld.getBigDecimal("VRI").compareTo(provjeraOld.getBigDecimal("KOL").multiply(provjeraOld.getBigDecimal("ZC"))) != 0) {
            System.out.println("DOKU - Skladiste " + provjeraOld.getString("CSKL") + " artikl " + provjeraOld.getInt("CART") + " vrijednost zalihe je razlicita od kolicina puta cijena zalihe");
            dummyStDoku.setBigDecimal("IZAD", provjeraOld.getBigDecimal("KOL").multiply(provjeraOld.getBigDecimal("ZC")));
          }           
          
          //prvo poništavam iznose
          dummyStDoku.setBigDecimal("INAB" , _Main.nul);
          dummyStDoku.setBigDecimal("IMAR" , _Main.nul);
          dummyStDoku.setBigDecimal("IPOR" , _Main.nul);
          
          // provjera metode voðenja skladišta!!
          
          if (provjeraOld.getString("VRZAL").equals("N")) { //-skladište se vodi po metodi prosjeèna nabavna cijena
            
            dummyStDoku.setBigDecimal("INAB" , provjeraOld.getBigDecimal("NC").multiply(provjeraOld.getBigDecimal("KOL"))); //-nabavni iznos
            
          } else if (provjeraOld.getString("VRZAL").equals("V")) { //-skladište se vodi po metodi zadnja prodajna cijena bez poreza
            
            dummyStDoku.setBigDecimal("INAB" , provjeraOld.getBigDecimal("NC").multiply(provjeraOld.getBigDecimal("KOL"))); //-nabavni iznos
            dummyStDoku.setBigDecimal("IBP" , provjeraOld.getBigDecimal("VC").multiply(provjeraOld.getBigDecimal("KOL")));  //-iznos bez poreza
            
            dummyStDoku.setBigDecimal("IMAR" , provjeraOld.getBigDecimal("VC").multiply(provjeraOld.getBigDecimal("KOL")).
                subtract(provjeraOld.getBigDecimal("NC").multiply(provjeraOld.getBigDecimal("KOL"))));  //-iznos marže (IBP-INAB)
            
          } else { //-skladište se vodi po metodi zadnja prodajna cijena s porezom
            
            dummyStDoku.setBigDecimal("INAB" , provjeraOld.getBigDecimal("NC").multiply(provjeraOld.getBigDecimal("KOL"))); //-nabavni iznos
            dummyStDoku.setBigDecimal("IBP" , provjeraOld.getBigDecimal("VC").multiply(provjeraOld.getBigDecimal("KOL")));  //-iznos bez poreza
            dummyStDoku.setBigDecimal("ISP" , provjeraOld.getBigDecimal("MC").multiply(provjeraOld.getBigDecimal("KOL")));  //-iznos s porezom
            
            dummyStDoku.setBigDecimal("IMAR" , provjeraOld.getBigDecimal("VC").multiply(provjeraOld.getBigDecimal("KOL")).
                subtract(provjeraOld.getBigDecimal("NC").multiply(provjeraOld.getBigDecimal("KOL"))));  //-iznos marže (IBP-INAB)
            dummyStDoku.setBigDecimal("IPOR" , provjeraOld.getBigDecimal("MC").multiply(provjeraOld.getBigDecimal("KOL")).
                subtract(provjeraOld.getBigDecimal("VC").multiply(provjeraOld.getBigDecimal("KOL"))));  //-iznos poreza (ISP -IBP)
            
            /**
             * U sluèaju razlièitih poreza, punim polja POR1 ili POR2 ili POR3
             * Nisam siguran koliko je relevantno i zašto puniti poreze (mogao bi biti dostatan ukupan iznos poreza)
             * u poèetnom stanju. Za sada ovako.
             */
           /* if (lookupData.getlookupData().raLocate(dm.getArtikli(), "CART", String.valueOf(provjeraOld.getInt("CART"))) && 
                lookupData.getlookupData().raLocate(dm.getPorezi(), "CPOR", dm.getArtikli().getString("CPOR"))) {
              dummyStDoku.setBigDecimal("POR1", provjeraOld.getBigDecimal("VC").multiply(dm.getPorezi().getBigDecimal("POR1").divide(new BigDecimal("100.00"), 2, BigDecimal.ROUND_HALF_UP)));
              dummyStDoku.setBigDecimal("POR2", provjeraOld.getBigDecimal("VC").multiply(dm.getPorezi().getBigDecimal("POR2").divide(new BigDecimal("100.00"), 2, BigDecimal.ROUND_HALF_UP)));
              dummyStDoku.setBigDecimal("POR3", provjeraOld.getBigDecimal("VC").multiply(dm.getPorezi().getBigDecimal("POR3").divide(new BigDecimal("100.00"), 2, BigDecimal.ROUND_HALF_UP)));
            } else {
              dummyStDoku.setBigDecimal("POR1", _Main.nul);
              dummyStDoku.setBigDecimal("POR2", _Main.nul);
              dummyStDoku.setBigDecimal("POR3", _Main.nul);
            }*/
          }
          
          BigDecimal sumnmp = dummyStDoku.getBigDecimal("INAB").add(dummyStDoku.getBigDecimal("IMAR").add(dummyStDoku.getBigDecimal("IPOR")));
          
          if (sumnmp.compareTo(dummyStDoku.getBigDecimal("IZAD")) != 0){
            dummyStDoku.setBigDecimal("IMAR",(dummyStDoku.getBigDecimal("IMAR").add(dummyStDoku.getBigDecimal("IZAD").subtract(sumnmp))));
            System.out.println("Provjera INAB+IMAR+IPOR = IZAD - POPRAVLJENO!");
          } else {
            System.out.println("Provjera INAB+IMAR+IPOR = IZAD - OK!");
          }
          
//          if(provjeraOld.getString("VRZAL").equals("N")) {
//            dummyStDoku.setBigDecimal("IPOR", _Main.nul);
//            dummyStDoku.setBigDecimal("IMAR", _Main.nul);
//          }
//          if(provjeraOld.getString("VRZAL").equals("V")) {
//            dummyStDoku.setBigDecimal("IPOR", _Main.nul);
//            dummyStDoku.setBigDecimal("IMAR", provjeraOld.getBigDecimal("KOL").multiply(provjeraOld.getBigDecimal("VC").subtract(provjeraOld.getBigDecimal("NC"))));
//          }
//          if(provjeraOld.getString("VRZAL").equals("M")){
//            dummyStDoku.setBigDecimal("IPOR", provjeraOld.getBigDecimal("KOL").multiply(provjeraOld.getBigDecimal("MC").subtract(provjeraOld.getBigDecimal("VC"))));
//            dummyStDoku.setBigDecimal("IMAR", provjeraOld.getBigDecimal("KOL").multiply(provjeraOld.getBigDecimal("VC").subtract(provjeraOld.getBigDecimal("NC"))));
//          }
        }
      } else /* if (provjeraOld.getBigDecimal("KOL").compareTo(_Main.nul) != 0 || isPrijenosStanjaNula()) */ {
        //Integer nStavka = new Integer(Rbr.getRbr().vrati_rbr("stdoku",provjeraOld.getString("CSKL"),"PST",fieldSet.getString("GODINA"),dummyDoku.getInt("BRDOK")));
        insertIntoStDoku(provjeraOld);
        insetrIntoStanje(provjeraOld, dummyDoku.getString("GOD"));
      }
    } while (provjeraOld.next());
  }

  private void countSkladista(){
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(skladistaSet);
//    System.out.println("godina u koju se prenosi : " +fieldSet.getString("GODINA"));
    updateSkladArray = new String[skladistaSet.rowCount()];
    skladistaSet.first();
    arrayCounter = 0;
    do {
      updateSkladArray[arrayCounter++] = skladistaSet.getString("CSKL");
    } while (skladistaSet.next());
    kitchmaPrijenosa();
  }

  private void setDummysFull(String ng){
    dummyDoku = ut.getNewQueryDataSet("select * from doku where god='" + ng + "' AND vrdok='PST'");
    dummyStDoku = ut.getNewQueryDataSet("select * from stdoku where god='" + ng + "' AND vrdok='PST'");
    dummyStanje = ut.getNewQueryDataSet("select * from stanje where god='" + ng + "'");
  }
}
/****license*****************************************************************
**   file: TypeDoc.java
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
import hr.restart.sisfun.frmParam;

/*
DOKUMENTI
Dokumenti koji idu u tabele doku i stdoku

PST po\u010Detno stanje
PRI primka
POR poravnanje
PRK primka- kalkulacija
KAL kalkulacija
PTE povratnica (tere\u0107enje) dobavlja\u010Du
INV inventurni višak

Dokumenti koji idu u tabele doki i stdoki

PRD predra\u010Dun
PON ponuda
POS prenos iz posa
OTP otpremnica
RAC ra\u010Dun
ROT ra\u010Dun otpremnica
GOT gotovinski R1 ra\u010Dun
POD povratnica (odobrenje) kupca
TER tere\u0107enje kupca
ODB odobrenje kupca
IZD izdatnica
INM inventurni manjak
OTR otpis robe
NDO narudžba dobavlja\u010Du ovaj dokument još ne postoji
NKU narudžba kupca
REV revers
PRv povratnica reversa
Dokumenti koji idu u tabele meskla i stmeskla
MES me\u0111uskladišnica
MEI me\u0111uskladišnica samo izlazni dio
MEU
PRE Predatnica


POV  POVRATNICA ////

*/



public class TypeDoc {

  static private TypeDoc TD ;
  static final String[] araj_docs = {"PST","PRI","POR","PRK","PTE","INV",
                              "PON","POS","OTP","ROT","GOT","POD","IZD","INM",
                              "OTR","NDO","NKU","MES","MEI","MEU","PRE","REV",
                              "PRV","POV", "ZAH", "TRE", "UZP"};
  static final String[] araj_docsOJ = {"PRD","RAC","TER","ODB","GRN","KAL","POS"};

  static public TypeDoc getTypeDoc(){
    if (TD == null) TD = new TypeDoc();
    return TD;
  }

  public boolean isDocOJ(String what_kind_of_document){
    boolean forreturn = false;
    for (int i=0 ;i<araj_docsOJ.length;i++) {
        if (araj_docsOJ[i].equalsIgnoreCase(what_kind_of_document)) {
          forreturn = true;
        }
    }
    return forreturn;
  }

 /**
   * Funkcija vra\u0107a ako je dokument skladišni i utje\u010Dena koli\u010Dine
   * I popunjava skladišni dio stavke
   * @param what_kind_of_document
   * @return true
   */

  public boolean isDocSkladAndPon(String what_kind_of_document){
    return ("PON".equalsIgnoreCase(what_kind_of_document) || isDocSklad(what_kind_of_document));
  }


  public boolean isDocSklad(String what_kind_of_document){
    if ("PST".equalsIgnoreCase(what_kind_of_document) ||
        "PRI".equalsIgnoreCase(what_kind_of_document) ||
        "PRK".equalsIgnoreCase(what_kind_of_document) ||
        "PRE".equalsIgnoreCase(what_kind_of_document) ||
        "MES".equalsIgnoreCase(what_kind_of_document) ||
        "MEI".equalsIgnoreCase(what_kind_of_document) ||
        "MEU".equalsIgnoreCase(what_kind_of_document) ||
        "OTP".equalsIgnoreCase(what_kind_of_document) ||
        "IZD".equalsIgnoreCase(what_kind_of_document) ||
        "POD".equalsIgnoreCase(what_kind_of_document) ||
        "OTR".equalsIgnoreCase(what_kind_of_document) ||
        "REV".equalsIgnoreCase(what_kind_of_document) ||
        "PRV".equalsIgnoreCase(what_kind_of_document) ||
        "ROT".equalsIgnoreCase(what_kind_of_document) ||
        "POV".equalsIgnoreCase(what_kind_of_document) ||
        "GOT".equalsIgnoreCase(what_kind_of_document) ||
        "INV".equalsIgnoreCase(what_kind_of_document) ||
        "POR".equalsIgnoreCase(what_kind_of_document) ||
        "PTE".equalsIgnoreCase(what_kind_of_document) ||		
        "INM".equalsIgnoreCase(what_kind_of_document)
        ) return true;
    return false;
  }
  public boolean isDocRNL(String what_kind_of_document){
    return "RNL".equalsIgnoreCase(what_kind_of_document);
  }

  /**
   * Funkcija koja vra\u0107a bulin prema vrsti dokumenta a da li je dokument
   * popunjava financijski dio stavke
   * @param what_kind_of_document
   * @return true
   */
  public boolean isDocFinanc(String what_kind_of_document){
    if ("PON".equalsIgnoreCase(what_kind_of_document) ||
        "PRD".equalsIgnoreCase(what_kind_of_document) ||
        "NKU".equalsIgnoreCase(what_kind_of_document) ||
        "RAC".equalsIgnoreCase(what_kind_of_document) ||
        "GRC".equalsIgnoreCase(what_kind_of_document) ||
        "ROT".equalsIgnoreCase(what_kind_of_document) ||
        "GOT".equalsIgnoreCase(what_kind_of_document) ||
        "PST".equalsIgnoreCase(what_kind_of_document) ||
        "PRK".equalsIgnoreCase(what_kind_of_document) ||
//        "POR".equalsIgnoreCase(what_kind_of_document) ||
        "GRN".equalsIgnoreCase(what_kind_of_document) ||
        "POD".equalsIgnoreCase(what_kind_of_document) ||
        "KAL".equalsIgnoreCase(what_kind_of_document) ||
        "TER".equalsIgnoreCase(what_kind_of_document) ||
        "POS".equalsIgnoreCase(what_kind_of_document) ||
        "ODB".equalsIgnoreCase(what_kind_of_document)) return true;
    return false;
  }
  /**
   * Vra\u0107a true ako je dokument ulazni
   * @param what_kind_of_document
   * @return true ako je dokument ulazni
   */
  public boolean isDocUlaz(String what_kind_of_document){
    if ("PST".equalsIgnoreCase(what_kind_of_document) ||
        "PRI".equalsIgnoreCase(what_kind_of_document) ||
        "POR".equalsIgnoreCase(what_kind_of_document) ||
        "PRK".equalsIgnoreCase(what_kind_of_document) ||
        "KAL".equalsIgnoreCase(what_kind_of_document) ||
        "MES".equalsIgnoreCase(what_kind_of_document) ||
        "MEU".equalsIgnoreCase(what_kind_of_document) ||
        "INV".equalsIgnoreCase(what_kind_of_document) ||
        "PRE".equalsIgnoreCase(what_kind_of_document) ||		
        "PTE".equalsIgnoreCase(what_kind_of_document)) return true;
    return false;
  }
  /**
   * Vra\u0107a true ako je dokument me\u0111uskladišnica
   * @param what_kind_of_document
   * @return true ako je dokument me\u0111uskladišnica
   */
  public boolean isDocMeskla(String what_kind_of_document){
    if ("MES".equalsIgnoreCase(what_kind_of_document) ||
        "MEI".equalsIgnoreCase(what_kind_of_document) ||
        "MEU".equalsIgnoreCase(what_kind_of_document)) return true;
    return false;
  }
  public boolean isDocRezerviraKol(String what_kind_of_document) {
    if ("PON".equalsIgnoreCase(what_kind_of_document) ||
        "NKU".equalsIgnoreCase(what_kind_of_document) ){
        return (hr.restart.sisfun.frmParam.getParam("robno","rezkol","Rezerviranje kolièine D/N","D").equals("D"));
    }
    else
      return false;
  }
  public boolean isDocDiraZalihu(String what_kind_of_document) {
    return !("PON".equalsIgnoreCase(what_kind_of_document) ||
        "NKU".equalsIgnoreCase(what_kind_of_document) ||
        "NDO".equalsIgnoreCase(what_kind_of_document) ||
        "RAC".equalsIgnoreCase(what_kind_of_document) ||
        "GRC".equalsIgnoreCase(what_kind_of_document) ||
        "POS".equalsIgnoreCase(what_kind_of_document) ||
        "GRN".equalsIgnoreCase(what_kind_of_document) ||
        "RNL".equalsIgnoreCase(what_kind_of_document) ||
        "PRD".equalsIgnoreCase(what_kind_of_document) ||
        "TER".equalsIgnoreCase(what_kind_of_document) ||
        "ZAH".equalsIgnoreCase(what_kind_of_document) ||
        "TRE".equalsIgnoreCase(what_kind_of_document) ||
        "UZP".equalsIgnoreCase(what_kind_of_document) ||
//        what_kind_of_document.equals("REV") ||
//        what_kind_of_document.equals("PRV") ||
        "ODB".equalsIgnoreCase(what_kind_of_document));

  }
  public boolean isDocDOS(String what_kind_of_document){
  
    return ("DOS".equalsIgnoreCase(what_kind_of_document));
  }
  
  public boolean isDocBezveze(String what_kind_of_document){
    return ("PON".equalsIgnoreCase(what_kind_of_document) ||
            "NKU".equalsIgnoreCase(what_kind_of_document) ||
            "NDO".equalsIgnoreCase(what_kind_of_document));
  }
  
  public boolean isDocStdoki(String what_kind_of_document){
    return ("RAC".equalsIgnoreCase(what_kind_of_document) ||
            "PRD".equalsIgnoreCase(what_kind_of_document) ||
            "IZD".equalsIgnoreCase(what_kind_of_document) ||
            "ROT".equalsIgnoreCase(what_kind_of_document) ||
            "GOT".equalsIgnoreCase(what_kind_of_document) ||
            "TER".equalsIgnoreCase(what_kind_of_document) ||
            "ODB".equalsIgnoreCase(what_kind_of_document) ||
            "NKU".equalsIgnoreCase(what_kind_of_document) ||
            "NDO".equalsIgnoreCase(what_kind_of_document) ||   // premjestio ab.f
            "ZAH".equalsIgnoreCase(what_kind_of_document) ||
            "TRE".equalsIgnoreCase(what_kind_of_document) ||
            "PON".equalsIgnoreCase(what_kind_of_document) ||
            "UZP".equalsIgnoreCase(what_kind_of_document) ||
            "OTP".equalsIgnoreCase(what_kind_of_document) ||
  
            "OTR".equalsIgnoreCase(what_kind_of_document) ||
            "POS".equalsIgnoreCase(what_kind_of_document) ||
            "GRN".equalsIgnoreCase(what_kind_of_document) ||
            "RNL".equalsIgnoreCase(what_kind_of_document) ||
            "INM".equalsIgnoreCase(what_kind_of_document) ||
            "REV".equalsIgnoreCase(what_kind_of_document) ||
            "PRV".equalsIgnoreCase(what_kind_of_document) ||
            "POV".equalsIgnoreCase(what_kind_of_document) ||
            "POD".equalsIgnoreCase(what_kind_of_document));
  }

  public boolean isDocStdoku(String what_kind_of_document){
    return ("PRK".equalsIgnoreCase(what_kind_of_document) ||
            "PST".equalsIgnoreCase(what_kind_of_document) ||
            "PRI".equalsIgnoreCase(what_kind_of_document) ||
            "KAL".equalsIgnoreCase(what_kind_of_document) ||
            "INV".equalsIgnoreCase(what_kind_of_document) ||
            "POR".equalsIgnoreCase(what_kind_of_document) ||
            // NDO maknio ab.f
            "PTE".equalsIgnoreCase(what_kind_of_document) ||
            "PRE".equalsIgnoreCase(what_kind_of_document));
  }

  public boolean isDocStmeskla(String what_kind_of_document){
    return ("MES".equalsIgnoreCase(what_kind_of_document) ||
            "MEI".equalsIgnoreCase(what_kind_of_document) ||
            "MEU".equalsIgnoreCase(what_kind_of_document));
  }

  public boolean isDoc4Temelj(String what_kind_of_document){
    com.borland.dx.sql.dataset.QueryDataSet qds =
        hr.restart.util.Util.getNewQueryDataSet("select * from vrdokum where vrdok='"+
           what_kind_of_document+"'",true);
    if (qds.isEmpty() || qds==null) return false;
    else {
      return ("D".equalsIgnoreCase(qds.getString("KNJIZ")));
    }

/*
    return (!("NKU".equalsIgnoreCase(what_kind_of_document) ||
              "NDO".equalsIgnoreCase(what_kind_of_document) ||
              "PON".equalsIgnoreCase(what_kind_of_document) ||
              "RN".equalsIgnoreCase(what_kind_of_document) ||
              "PST".equalsIgnoreCase(what_kind_of_document) ||
              "REV".equalsIgnoreCase(what_kind_of_document) ||
              "PRV".equalsIgnoreCase(what_kind_of_document)));
*/
  }

  public boolean isDocPST(String what_kind_of_document){
    return ("PST".equalsIgnoreCase(what_kind_of_document));
  }
  
  public int numberPanel(String what_kind_of_document){
    int forReturn = 0;
    if ( "IZD".equalsIgnoreCase(what_kind_of_document) ||
         "POV".equalsIgnoreCase(what_kind_of_document)) {
      forReturn = 1;
    }
    else if ( "GRN".equalsIgnoreCase(what_kind_of_document) ||
              "GOT".equalsIgnoreCase(what_kind_of_document)) {
      forReturn = 2;
    }
    else if ("ODB".equalsIgnoreCase(what_kind_of_document) ||
             "POD".equalsIgnoreCase(what_kind_of_document) ||
             "TER".equalsIgnoreCase(what_kind_of_document)) {
      forReturn = 0;
    }
    else if ("REV".equalsIgnoreCase(what_kind_of_document) ||
             "PRV".equalsIgnoreCase(what_kind_of_document)) {
      forReturn = 4; //4
    }
    else if ("PON".equalsIgnoreCase(what_kind_of_document)) {
      forReturn = 5; //4
    } else if ("OTP".equalsIgnoreCase(what_kind_of_document) ||
            "DOS".equalsIgnoreCase(what_kind_of_document)) {
      forReturn = 6;
    } else if ("ZAH".equalsIgnoreCase(what_kind_of_document) ||
            "TRE".equalsIgnoreCase(what_kind_of_document)) {
      forReturn = 7;
    } else if ("UZP".equalsIgnoreCase(what_kind_of_document)) {
      forReturn = 8; //4
    }
    return forReturn;
  }
  
  
  

  public boolean isDocNegateKol(String what_kind_of_document){
    if ("ODB".equalsIgnoreCase(what_kind_of_document) ||
        "POD".equalsIgnoreCase(what_kind_of_document) ||
        "POV".equalsIgnoreCase(what_kind_of_document) ||
        "PRV".equalsIgnoreCase(what_kind_of_document))
      return true;
    else return false;
  }

  public boolean isDoubleDoc(String what_kind_of_document){
    return ("MES".equalsIgnoreCase(what_kind_of_document));
  }
  public boolean isGOTGRN(String what_kind_of_document){
    return ("GRN".equalsIgnoreCase(what_kind_of_document) ||
        "GOT".equalsIgnoreCase(what_kind_of_document));
  }

  public boolean isCsklSklad(String what_kind_of_document){
    return !("PRD".equalsIgnoreCase(what_kind_of_document) ||
            "RAC".equalsIgnoreCase(what_kind_of_document) ||
            "TER".equalsIgnoreCase(what_kind_of_document) ||
            "ODB".equalsIgnoreCase(what_kind_of_document) ||
            "KAL".equalsIgnoreCase(what_kind_of_document) ||
            "POS".equalsIgnoreCase(what_kind_of_document) ||
            "GRN".equalsIgnoreCase(what_kind_of_document));
  }

  public String[] doc4prijenos(String what_kind_of_document) {
    if (what_kind_of_document.equalsIgnoreCase("RAC")){
      return new String[]{"OTP"};
    } else if (what_kind_of_document.equalsIgnoreCase("OTP")){
      return new String[]{"RAC"};
    }
    return null;
  }

  public boolean isMnogostrukPrijenos(String what_kind_of_document){
    return ("OTP".equalsIgnoreCase(what_kind_of_document)||
        "DOS".equalsIgnoreCase(what_kind_of_document)||
        "PON".equalsIgnoreCase(what_kind_of_document));
//    return false;
  }

  public String nazivDokumenta(String what_kind_of_document){

    if ("PST".equalsIgnoreCase(what_kind_of_document)){
      return "Poèetno stanje";
    } else if ("PRI".equalsIgnoreCase(what_kind_of_document)){
      return "Primka";
    } else if ("POR".equalsIgnoreCase(what_kind_of_document)){
      return "Poravnanje";
    } else if ("PRK".equalsIgnoreCase(what_kind_of_document)){
      return "Primka - kalkulacija";
    } else if ("KAL".equalsIgnoreCase(what_kind_of_document)){
      return "Kalkulacija";
    } else if ("PTE".equalsIgnoreCase(what_kind_of_document)){
      return "Povratnica tereæenje";
    } else if ("INV".equalsIgnoreCase(what_kind_of_document)){
      return "Inventurni višak";
    } else if ("PRD".equalsIgnoreCase(what_kind_of_document)){
      return "Raèun za predujam";
    } else if ("PON".equalsIgnoreCase(what_kind_of_document)){
      return "Ponuda";
    } else if ("POS".equalsIgnoreCase(what_kind_of_document)){
      return "Pos";
    } else if ("OTP".equalsIgnoreCase(what_kind_of_document)){
      return "Otpremnica";
    } else if ("RAC".equalsIgnoreCase(what_kind_of_document)){
      return "Raèun";
    } else if ("ROT".equalsIgnoreCase(what_kind_of_document)){
      return "Raèun - otpremnica";
    } else if ("GOT".equalsIgnoreCase(what_kind_of_document)){
      return "Gotovinski raèun - otpremnica";
    } else if ("POD".equalsIgnoreCase(what_kind_of_document)){
      return "Povratnica odobrenje";
    } else if ("TER".equalsIgnoreCase(what_kind_of_document)){
      return "Tereæenje";
    } else if ("ODB".equalsIgnoreCase(what_kind_of_document)){
      return "Odobrenje";
    } else if ("IZD".equalsIgnoreCase(what_kind_of_document)){
      return "Izdatnica";
    } else if ("INM".equalsIgnoreCase(what_kind_of_document)){
      return "Inventurni manjak";
    } else if ("OTR".equalsIgnoreCase(what_kind_of_document)){
      return "Otpis robe";
    } else if ("NDO".equalsIgnoreCase(what_kind_of_document)){
      return "Narudžba dobavljaèu";
    } else if ("NKU".equalsIgnoreCase(what_kind_of_document)){
      return "Narudžba kupca";
    } else if ("REV".equalsIgnoreCase(what_kind_of_document)){
      return "Revers";
    } else if ("PRV".equalsIgnoreCase(what_kind_of_document)){
      return "Povratnica reversa";
    } else if ("RNL".equalsIgnoreCase(what_kind_of_document)){
      return "Radni nalog";
    } else if ("MES".equalsIgnoreCase(what_kind_of_document)){
      return "Meðuskladišnica";
    } else if ("MEU".equalsIgnoreCase(what_kind_of_document)){
      return "Meðuskladišnica ulaz";
    } else if ("MEI".equalsIgnoreCase(what_kind_of_document)){
      return "Meðuskladišnica izlaz";
    } else if ("PRE".equalsIgnoreCase(what_kind_of_document)){
      return "Predatnica";
    } else if ("RN".equalsIgnoreCase(what_kind_of_document)) {
      return "Radni nalog";
    } else if ("RNI".equalsIgnoreCase(what_kind_of_document)) {
      return "Radni nalog po izdatnici";
    } else if ("DOS".equalsIgnoreCase(what_kind_of_document)) {
        return "Dostavnica";
    }


    
    return null;
  }
  public boolean  isPrikaz(String what_kind_of_document){
    boolean bvaldDoc = "ROT".equalsIgnoreCase(what_kind_of_document) ||
                       "OTP".equalsIgnoreCase(what_kind_of_document) ||
                       "POD".equalsIgnoreCase(what_kind_of_document);
    bvaldDoc = true;
    
    return frmParam.getParam("robno", "prikazKol", "N","Prikaz kolièina na rapancartu (D/N)").equalsIgnoreCase("D")
        && bvaldDoc;
  }
}

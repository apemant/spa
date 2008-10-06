/****license*****************************************************************
**   file: raInventuraUtil.java
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

import hr.restart.util.lookupData;

public class raInventuraUtil {

  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  private static raInventuraUtil riut;
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  lookupData lukeSkywalker = lookupData.getlookupData();


  public static raInventuraUtil getInventuraUtil() {
    if (riut == null) {
      riut = new raInventuraUtil();
    }
    return riut;
  }

  public raInventuraUtil() {
  }

//  boolean findStanjeIzlaza(QueryDataSet doki, QueryDataSet stdoki) {
// //    lookupData ldi = lookupData.getlookupData();
//    if (lukeSkywalker.raLocate(dm.getStanje(),
//                     new com.borland.dx.dataset.DataSet[] {doki, stdoki},
//                     new java.lang.String[] {"CSKL","CART"},
//                     new java.lang.String[] {"CSKL","CART"})) {
// //      System.out.println("Skladishche: "+ doki.getString("CSKL"));
// //      System.out.println("Fak'n cart : "+ stdoki.getInt("CART"));
// //      System.out.println("Find stanje: tru");
//      return true;
//    }
// //    System.out.println("Find stanje: fols");
//    return false;
//  }

//  boolean findStanjeUlaza(QueryDataSet doku, QueryDataSet stdoku) {
// //    lookupData ldu = lookupData.getlookupData();
//    if (lukeSkywalker.raLocate(dm.getStanje(),
//                     new com.borland.dx.dataset.DataSet[] {doku, stdoku},
//                     new java.lang.String[] {"CSKL","CART"},
//                     new java.lang.String[] {"CSKL","CART"})) {
// //      System.out.println("Skladishche: "+doku.getString("CSKL"));
// //      System.out.println("Fak'n cart : "+stdoku.getInt("CART"));
// //      System.out.println("Find stanje: tru");
//      return true;
//    }
// //    System.out.println("Find stanje: fols");
//    return false;
//  }

//  public void updateStanjeZaManjak(BigDecimal oldKOL, BigDecimal oldNAB, BigDecimal oldMAR, BigDecimal oldPOR, BigDecimal oldZAD, QueryDataSet doki, QueryDataSet stdoki){ //char mode, boolean isFind, char doc){
//    dm.getStanje().open();
//    if (findStanjeIzlaza(doki, stdoki)){
// //      System.out.println("update Stanje za artikli... " + stdoki.getString("NAZART"));
// //      System.out.println("i za skladishte............ " + doki.getString("CSKL"));
//
//      dm.getStanje().setBigDecimal("KOLIZ",     negateValue(sumValue(dm.getStanje().getBigDecimal("KOLIZ"), stdoki.getBigDecimal("KOL")), oldKOL));
//      dm.getStanje().setBigDecimal("NABIZ",     negateValue(sumValue(dm.getStanje().getBigDecimal("NABIZ"), stdoki.getBigDecimal("INAB")), oldNAB));
//      dm.getStanje().setBigDecimal("MARIZ",     negateValue(sumValue(dm.getStanje().getBigDecimal("MARIZ"), stdoki.getBigDecimal("IMAR")),  oldMAR));
//      dm.getStanje().setBigDecimal("PORIZ",     negateValue(sumValue(dm.getStanje().getBigDecimal("PORIZ"), stdoki.getBigDecimal("IPOR")),  oldPOR));
//      dm.getStanje().setBigDecimal("VIZ",       negateValue(sumValue(dm.getStanje().getBigDecimal("VIZ"), stdoki.getBigDecimal("IRAZ")), oldZAD));
//      dm.getStanje().setBigDecimal("VRI",       negateValue(dm.getStanje().getBigDecimal("VRI"), dm.getStanje().getBigDecimal("VIZ")));
//      dm.getStanje().setBigDecimal("KOL",       negateValue(dm.getStanje().getBigDecimal("KOLUL"), dm.getStanje().getBigDecimal("KOLIZ")));
//    }
//    dm.getStanje().post();
//    //    dm.getStanje().saveChanges();  // <-- u transakciji tamo odakle se poziva!!!
//  }

//  public void updateStanjeZaVishak(BigDecimal oldKOL, BigDecimal oldNAB, BigDecimal oldMAR, BigDecimal oldPOR, BigDecimal oldZAD, QueryDataSet doku, QueryDataSet stdoku){
//    dm.getStanje().open();
// //    System.out.println("update Stanje za artikli... " + stdoku.getString("NAZART"));
// //    System.out.println("i za skladishte............ " + doku.getString("CSKL"));
//    if (findStanjeUlaza(doku, stdoku)){
//      dm.getStanje().setBigDecimal("KOLUL",     negateValue(sumValue(dm.getStanje().getBigDecimal("KOLUL"), stdoku.getBigDecimal("KOL")), oldKOL));
//      dm.getStanje().setBigDecimal("NABUL",     negateValue(sumValue(dm.getStanje().getBigDecimal("NABUL"), stdoku.getBigDecimal("INAB")), oldNAB));
//      dm.getStanje().setBigDecimal("MARUL",     negateValue(sumValue(dm.getStanje().getBigDecimal("MARUL"), stdoku.getBigDecimal("IMAR")),  oldMAR));
//      dm.getStanje().setBigDecimal("PORUL",     negateValue(sumValue(dm.getStanje().getBigDecimal("PORUL"), stdoku.getBigDecimal("IPOR")),  oldPOR));
//      dm.getStanje().setBigDecimal("VUL",       negateValue(sumValue(dm.getStanje().getBigDecimal("VUL"), stdoku.getBigDecimal("IZAD")), oldZAD));
//      dm.getStanje().setBigDecimal("VRI",       negateValue(dm.getStanje().getBigDecimal("VUL"), dm.getStanje().getBigDecimal("VIZ")));
//      dm.getStanje().setBigDecimal("KOL",       negateValue(dm.getStanje().getBigDecimal("KOLUL"), dm.getStanje().getBigDecimal("KOLIZ")));
//    }
//    dm.getStanje().post();
//    //    dm.getStanje().saveChanges();  // <-- u transakciji tamo odakle se poziva!!!
//  }

  public java.math.BigDecimal sumValue(java.math.BigDecimal Osnovica1, java.math.BigDecimal Osnovica2) {
    return Osnovica1.add(Osnovica2);
  }

  public java.math.BigDecimal sumValue(java.math.BigDecimal Osnovica1, java.math.BigDecimal Osnovica2, java.math.BigDecimal Osnovica3) {
    return Osnovica1.add(Osnovica2.add(Osnovica3));
  }

  public java.math.BigDecimal negateValue(java.math.BigDecimal Osnovica1, java.math.BigDecimal Osnovica2) {
    return Osnovica1.add(Osnovica2.negate());
  }

  public java.math.BigDecimal multiValue(java.math.BigDecimal Osnovica1, java.math.BigDecimal Osnovica2) {
    return Osnovica1.multiply(Osnovica2);
  }

  public java.math.BigDecimal divideValue(java.math.BigDecimal Osnovica1, java.math.BigDecimal Osnovica2) {
    if (Osnovica2.doubleValue()>0) {
      return Osnovica1.divide(Osnovica2, 1);
    }
    else {
      return _Main.nul;
    }
  }
}
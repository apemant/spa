/****license*****************************************************************
**   file: frmKnjPN.java
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
package hr.restart.gk;
import hr.restart.baza.Condition;
import hr.restart.baza.Shkonta;
import hr.restart.util.Util;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmKnjPN extends frmKnjizenje {
  QueryDataSet pnalozi;
  public frmKnjPN() {
  }

  public boolean okPress() {
   String kontoI;
   String kontoZ;
   if (!getKnjizenje().startKnjizenje(this)) return false;
   String pnqry = "SELECT * FROM putnalarh WHERE knjig = '"
       + OrgStr.getKNJCORG(false)+"' AND status = 'I' AND datobr "+getDatumDoSQL();
   pnalozi = Util.getNewQueryDataSet(pnqry);
   
   DataSet shk = Shkonta.getDataModule().getTempSet(
       Condition.equal("VRDOK", "PN").and(Condition.equal("CSKL", "99")));
   shk.open();
   //paramblpn
   if (!ld.raLocate(dm.getParamblpn(),new String[] {"KNJIG"},new String[] {OrgStr.getKNJCORG(false)})) {
     getKnjizenje().setErrorMessage("Nisu upisani parametri knji¾enja putnih naloga!");
     return false;
   } else {
     String cskl = dm.getParamblpn().getString("CSKL");
     String vrdok = dm.getParamblpn().getString("VRDOK");
     String stZ = dm.getParamblpn().getString("STAVKAPNZ");
     String stI = dm.getParamblpn().getString("STAVKAPNI");
     kontoZ = getKnjizenje().getBrojKonta(vrdok,cskl,stZ);
     kontoI = getKnjizenje().getBrojKonta(vrdok,cskl,stI);
   }
   //ima li podataka
   if (pnalozi.getRowCount() == 0) {
     getKnjizenje().setErrorMessage("Nema putnih naloga za knjiženje!");
     return false;
   }
   //transfer info
   getKnjizenje().setTransferSet(pnalozi);
   getKnjizenje().setInfoKeys(new String[] {"KNJIG","GODINA","BROJ","INDPUTA"});
//   getKnjizenje().setInfColName("CNALOGA"); // default je CNALOGA
   pnalozi.first();
   do {
     QueryDataSet stavpn = getStavPN(pnalozi);
     if (stavpn.getRowCount() > 0) {
       getKnjizenje().setTransferInfoAddingEnabled(false);//nemoj dodavat info
       do {
         StorageDataSet stavka = getKnjizenje().getNewStavka(stavpn,pnalozi.getString("CORG"));
         getKnjizenje().setID(stavpn.getBigDecimal("PVIZNOS"));
//System.out.println("VRDOK-CSKL-STAVKA = "+stavpn.getString("VRDOK")+"-"+stavpn.getString("CSKL")+"-"+stavpn.getShort("STAVKA"));
//System.out.println("konto?="+getKnjizenje().getBrojKonta(stavpn.getString("VRDOK"),stavpn.getString("CSKL"),""+stavpn.getShort("STAVKA")));
//System.out.println("Knjizim konto "+getKnjizenje().getStavka().getString("BROJKONTA"));
//System.out.println("Knjizim konto(s) "+getKnjizenje().getStavkaSK().getString("BROJKONTA"));
         String cpn = getKnjizenje().isLastKontoZbirni()?"":stavpn.getString("CPN");
         stavka.setString("OPIS","Trošak putnog naloga "+cpn);
         stavka.setTimestamp("DATDOK",pnalozi.getTimestamp("DATOBR"));
         if (!getKnjizenje().saveStavka()) return false;
       } while (stavpn.next());
     }
     getKnjizenje().setTransferInfoAddingEnabled(true);//e sad dodaj info
     String kontoprom = pnalozi.getString("INDPUTA").equals("I")?kontoI:kontoZ;
     if (ld.raLocate(shk, "POLJE", pnalozi.getString("CRADNIK")))
       kontoprom = shk.getString("BROJKONTA");
     
     StorageDataSet promet = getKnjizenje().getNewStavka(kontoprom,pnalozi.getString("CORG"));
     getKnjizenje().setIP(pnalozi.getBigDecimal("TROSKOVI"));
     String cpn = getKnjizenje().isLastKontoZbirni()?"":stavpn.getString("CPN");
     promet.setString("OPIS","Obraèun putnog naloga "+cpn);
     promet.setTimestamp("DATDOK",pnalozi.getTimestamp("DATOBR"));
     if (!getKnjizenje().saveStavka()) return false;
   } while (pnalozi.next());
   return getKnjizenje().saveAll();
 }
 public boolean commitTransfer() {
   if (!super.commitTransfer()) return false;
   pnalozi.first();
   do {
     pnalozi.setString("STATUS","K");
   } while (pnalozi.next());
   // sad je u transakciji zajedno sa knjizenjem naloga
   //return raTransaction.saveChangesInTransaction(new QueryDataSet[] {pnalozi});
   try {
     raTransaction.saveChanges(pnalozi);
     return true;
	 } catch (Exception e) {
	   e.printStackTrace();
	   return false;
	 }
 }
 private QueryDataSet getStavPN(QueryDataSet pnal) {
   //knjig, godina, broj, indputa
   String spnqry = "SELECT * FROM stavpnarh WHERE knjig = '"
                 +pnal.getString("KNJIG")
                 +"' AND GODINA = "+pnal.getShort("GODINA")
                 +" AND BROJ = "+pnal.getInt("BROJ")
                 +" AND INDPUTA = '"+pnal.getString("INDPUTA")+"'";
   return Util.getNewQueryDataSet(spnqry);
 }

}

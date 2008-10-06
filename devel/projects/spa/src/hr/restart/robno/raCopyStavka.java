/****license*****************************************************************
**   file: raCopyStavka.java
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
import com.borland.dx.dataset.DataSet;

public class raCopyStavka {

  static raCopyStavka rCS ;

  public static raCopyStavka getraCopyStavka(){
    if (rCS == null) rCS = new raCopyStavka();
    return rCS;
  }


  public void copyFromZaglavlje(DataSet DSDoki, DataSet DSStdoki,short brstavke ) {
    DSStdoki.enableDataSetEvents(false);
    DSDoki.enableDataSetEvents(false);
    DSStdoki.setString("CSKL",DSDoki.getString("CSKL"));
    DSStdoki.setString("GOD",DSDoki.getString("GOD"));
    DSStdoki.setString("VRDOK",DSDoki.getString("VRDOK"));
    DSStdoki.setInt("BRDOK",DSDoki.getInt("BRDOK"));
    DSStdoki.setShort("RBR",brstavke);
    DSStdoki.enableDataSetEvents(false);
    DSDoki.enableDataSetEvents(false);
  }
  public void copyCommon(DataSet DSStdoki,DataSet DSZaPrenos) {
    DSStdoki.enableDataSetEvents(false);
    DSZaPrenos.enableDataSetEvents(false);
    DSStdoki.setInt("CART",DSZaPrenos.getInt("CART"));
    DSStdoki.setString("CART1",DSZaPrenos.getString("CART1"));
    DSStdoki.setString("BC",DSZaPrenos.getString("BC"));
    DSStdoki.setString("NAZART",DSZaPrenos.getString("NAZART"));
    DSStdoki.setString("JM",DSZaPrenos.getString("JM"));
    DSStdoki.setBigDecimal("KOL",DSZaPrenos.getBigDecimal("KOL"));
    DSStdoki.enableDataSetEvents(true);
    DSZaPrenos.enableDataSetEvents(true);

  }

  public void copySkladPart(DataSet DSStdoki,DataSet DSZaPrenos) {

      DSStdoki.enableDataSetEvents(false);
      DSZaPrenos.enableDataSetEvents(false);
      DSStdoki.setBigDecimal("NC",DSZaPrenos.getBigDecimal("NC"));
      DSStdoki.setBigDecimal("INAB",DSZaPrenos.getBigDecimal("INAB"));
      DSStdoki.setBigDecimal("IMAR",DSZaPrenos.getBigDecimal("IMAR"));
      DSStdoki.setBigDecimal("VC",DSZaPrenos.getBigDecimal("VC"));
      DSStdoki.setBigDecimal("IBP",DSZaPrenos.getBigDecimal("IBP"));
      DSStdoki.setBigDecimal("IPOR",DSZaPrenos.getBigDecimal("IPOR"));
      DSStdoki.setBigDecimal("MC",DSZaPrenos.getBigDecimal("MC"));
      DSStdoki.setBigDecimal("ISP",DSZaPrenos.getBigDecimal("ISP"));
      DSStdoki.setBigDecimal("ZC",DSZaPrenos.getBigDecimal("ZC"));
      DSStdoki.setBigDecimal("IRAZ",DSZaPrenos.getBigDecimal("IRAZ"));
      DSStdoki.setString("BRPRI",DSZaPrenos.getString("BRPRI"));
      DSStdoki.setShort("RBRPRI",DSZaPrenos.getShort("RBRPRI"));
      DSStdoki.enableDataSetEvents(true);
      DSZaPrenos.enableDataSetEvents(true);

  }

  public void resetSkladPart(DataSet DSStdoki) {
      DSStdoki.enableDataSetEvents(false);
      java.math.BigDecimal Nula = java.math.BigDecimal.valueOf((long) 0);
      DSStdoki.setBigDecimal("NC",Nula);
      DSStdoki.setBigDecimal("INAB",Nula);
      DSStdoki.setBigDecimal("IMAR",Nula);
      DSStdoki.setBigDecimal("VC",Nula);
      DSStdoki.setBigDecimal("IBP",Nula);
      DSStdoki.setBigDecimal("IPOR",Nula);
      DSStdoki.setBigDecimal("MC",Nula);
      DSStdoki.setBigDecimal("ISP",Nula);
      DSStdoki.setBigDecimal("ZC",Nula);
      DSStdoki.setBigDecimal("IRAZ",Nula);
      DSStdoki.setString("BRPRI","");
      DSStdoki.setShort("RBRPRI",(short) 0);
      DSStdoki.enableDataSetEvents(true);
  }

  public void copyFinancPart(DataSet DSStdoki,DataSet DSZaPrenos) {

      DSStdoki.enableDataSetEvents(false);
      DSZaPrenos.enableDataSetEvents(false);
      DSStdoki.setBigDecimal("UPRAB",DSZaPrenos.getBigDecimal("UPRAB"));
      DSStdoki.setBigDecimal("UIRAB",DSZaPrenos.getBigDecimal("UIRAB"));
      DSStdoki.setBigDecimal("UPZT",DSZaPrenos.getBigDecimal("UPZT"));
      DSStdoki.setBigDecimal("UIZT",DSZaPrenos.getBigDecimal("UIZT"));
      DSStdoki.setBigDecimal("FC",DSZaPrenos.getBigDecimal("FC"));
      DSStdoki.setBigDecimal("INETO",DSZaPrenos.getBigDecimal("INETO"));
      DSStdoki.setBigDecimal("FVC",DSZaPrenos.getBigDecimal("FVC"));
      DSStdoki.setBigDecimal("IPRODBP",DSZaPrenos.getBigDecimal("IPRODBP"));
      DSStdoki.setBigDecimal("POR1",DSZaPrenos.getBigDecimal("POR1"));
      DSStdoki.setBigDecimal("POR2",DSZaPrenos.getBigDecimal("POR2"));
      DSStdoki.setBigDecimal("POR3",DSZaPrenos.getBigDecimal("POR3"));
      DSStdoki.setBigDecimal("FMC",DSZaPrenos.getBigDecimal("FMC"));
      DSStdoki.setBigDecimal("IPRODSP",DSZaPrenos.getBigDecimal("IPRODSP"));
      DSStdoki.enableDataSetEvents(true);
      DSZaPrenos.enableDataSetEvents(true);

  }

  public void resetFinancPart(DataSet DSStdoki) {
      java.math.BigDecimal Nula = java.math.BigDecimal.valueOf((long)0);
      DSStdoki.enableDataSetEvents(false);
      DSStdoki.setBigDecimal("UPRAB",Nula);
      DSStdoki.setBigDecimal("UIRAB",Nula);
      DSStdoki.setBigDecimal("UPZT",Nula);
      DSStdoki.setBigDecimal("UIZT",Nula);
      DSStdoki.setBigDecimal("FC",Nula);
      DSStdoki.setBigDecimal("INETO",Nula);
      DSStdoki.setBigDecimal("FVC",Nula);
      DSStdoki.setBigDecimal("IPRODBP",Nula);
      DSStdoki.setBigDecimal("POR1",Nula);
      DSStdoki.setBigDecimal("POR2",Nula);
      DSStdoki.setBigDecimal("POR3",Nula);
      DSStdoki.setBigDecimal("FMC",Nula);
      DSStdoki.setBigDecimal("IPRODSP",Nula);
      DSStdoki.enableDataSetEvents(true);
  }

  public boolean testStanje(DataSet StavkeSet,String god,String cskl){

    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();

      boolean isUsluga= false;

      boolean returnValue = true;
      allStanje AST = new allStanje();
      StavkeSet.enableDataSetEvents(false);
      StavkeSet.first();
      do {

        AST.findStanjeUnconditional(god,
                                    cskl,
                                    StavkeSet.getInt("CART"));
        AST.gettrenSTANJE().enableDataSetEvents(false);
System.out.println("StavkeSet.getInt(CART) " + StavkeSet.getInt("CART"));
        if(lD.raLocate(dm.getArtikli(),
                     new String[]{"CART"},
                     new String[]{String.valueOf(StavkeSet.getInt("CART"))})){
System.out.println("StavkeSet.getInt(CART) " + StavkeSet.getInt("CART"));
System.out.println("dm.getArtikli().getInt(CART) " + dm.getArtikli().getInt("CART"));

          isUsluga = !raVart.isStanje(dm.getArtikli());
            /*dm.getArtikli().getString("VRART").equalsIgnoreCase("U") ||
                     dm.getArtikli().getString("VRART").equalsIgnoreCase("T"); */
          System.out.println("isUsluga="+isUsluga);
        }
        if (!isUsluga){
          if (AST.gettrenSTANJE().getBigDecimal("KOL").compareTo(StavkeSet.getBigDecimal("KOL"))<0){
            returnValue = false;
            javax.swing.JOptionPane.showMessageDialog(null,"Nedovoljna zaliha za prenos ",
                "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
            break;
          }
        }
      } while (StavkeSet.next());
      StavkeSet.enableDataSetEvents(true);
      return returnValue;
  }
}
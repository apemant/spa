/****license*****************************************************************
**   file: raosFakeKalk.java
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
package hr.restart.os;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raosFakeKalk {
  java.math.BigDecimal nul = new java.math.BigDecimal(0);
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  public double osnovica      = 0;
  public double ispravak      = 0;
  public double zakstopa      = 0;
  public double odlstopa      = 0;
  public double Amortizacija  = 0;
  public double pAmortizacija  = 0;
  hr.restart.util.LinkClass lc = hr.restart.util.LinkClass.getLinkClass();
  {
    lc.setUseBigDecimal(false);
  }
/**
 * Obrada amortizacije
 * @param mjesecod
 * @param mjesecdo
 * @param mjesecprom
 * @return
 */
  public java.math.BigDecimal Obrada(int mjesecod, int mjesecdo, int mjesecprom){
//    if (osnovica<0) {
//      return nul;
//    }
    Amortizacija=((osnovica/12) * (zakstopa/100) * (mjesecdo - mjesecprom)) -
                 ((osnovica/12) * (zakstopa/100) * (mjesecod - mjesecprom));
    return new java.math.BigDecimal(Amortizacija).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  }
  public java.math.BigDecimal Obrada(int mjesecdo, int mjesecprom){
//    if (osnovica<0) {
//      return nul;
//    }
    Amortizacija=((osnovica/12) * (zakstopa/100) * (mjesecdo - mjesecprom));
    return new java.math.BigDecimal(Amortizacija).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  }
/**
 * Obrada povisene amortizacije
 * @param mjesecod
 * @param mjesecdo
 * @param mjesecprom
 * @return
 */
  public java.math.BigDecimal PObrada(int mjesecod, int mjesecdo, int mjesecprom){
    if (osnovica<0) {
      return nul;
    }
    pAmortizacija=((osnovica/12) * (odlstopa/100) * (mjesecdo - mjesecprom + 1)) -
                 ((osnovica/12) * (odlstopa/100) * (mjesecod - mjesecprom));
    return new java.math.BigDecimal(pAmortizacija).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  }
  public java.math.BigDecimal PObrada(int mjesecdo, int mjesecprom){
    if (osnovica<0) {
      return nul;
    }
    pAmortizacija=((osnovica/12) * (odlstopa/100) * (mjesecdo - mjesecprom));
    return new java.math.BigDecimal(pAmortizacija).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  }

/**
 * Izraèun amortizacije za poèetna stanja
 * @param mjesecod
 * @param mjesecdo
 * @return
 */
  public java.math.BigDecimal calcObrada(int mjesecod, int mjesecdo){
    Amortizacija=((osnovica/12) * (zakstopa/100) * (mjesecdo - mjesecod + 1));
    System.out.println("Amortizacija: "+Amortizacija+" osn: "+osnovica+" stopa: "+zakstopa+" mjesec: "+(mjesecdo - mjesecod + 1));
    return new java.math.BigDecimal(Amortizacija).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  }
/**
 * Izraèun povišene amortizacije za poèetna stanja
 * @param mjesecod
 * @param mjesecdo
 * @return
 */
  public java.math.BigDecimal calcPObrada(int mjesecod, int mjesecdo){
    pAmortizacija=((osnovica/12) * (odlstopa/100) * (mjesecdo - mjesecod + 1));
    return new java.math.BigDecimal(pAmortizacija).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  }
  public java.math.BigDecimal likObrada(int mjesec) {
    pAmortizacija=((osnovica/12) * (zakstopa/100) * (mjesec));
    return new java.math.BigDecimal(pAmortizacija).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  }

/**
 * Setiranje fejkanih vrijednosti (www.askTomo)
 * @param Obrada_os_promjene
 */
  public void setUPValue(QueryDataSet qds){
    lc.TransferFromDB2Class(qds, this);
  }
}
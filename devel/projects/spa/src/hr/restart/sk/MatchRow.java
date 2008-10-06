/****license*****************************************************************
**   file: MatchRow.java
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
package hr.restart.sk;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.borland.dx.dataset.ReadRow;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class MatchRow extends raVrdokMatcher {
  private String csk, mkey, brdok, oznval, extbr, corg;
  private Timestamp datdok;
  private BigDecimal pvsaldo;
  private ArrayList pok, pokIznos;
  private boolean matchable;
  private double matchStrength;

  public MatchRow(ReadRow stavka) {
    super(stavka);
    brdok = stavka.getString("BROJDOK");
    csk = stavka.getString("CSKSTAVKE");
    extbr = stavka.getString("EXTBRDOK");
    corg = stavka.getString("CORG");
    oznval = raSaldaKonti.isSimple() ? "" : stavka.getString("OZNVAL");
    datdok = new Timestamp(stavka.getTimestamp("DATDOK").getTime());
    mkey = stavka.getInt("CPAR") + (isKup() ? "$K$" : "$D$") +
           stavka.getString("BROJKONTA") + "$" + oznval;/* + "$" +
           Util.getUtil().getYear(stavka.getTimestamp("DATUMKNJ"));*/
    pvsaldo = stavka.getBigDecimal(raSaldaKonti.colSaldo());
    matchable = false;
  }

  public String getCSK() {
    return csk;
  }

  public BigDecimal getPVSaldo() {
    return pvsaldo;
  }

  public void matchSaldo(BigDecimal pok) {
    if (pvsaldo.signum() < 0) pvsaldo = pvsaldo.add(pok);
    else pvsaldo = pvsaldo.subtract(pok);
  }

  public void addMatch(MatchRow other, BigDecimal iznos) {
    if (pok == null) {
      pok = new ArrayList();
      pokIznos = new ArrayList();
    }
    pok.add(other);
    pokIznos.add(iznos);
    matchSaldo(iznos);
    other.matchSaldo(iznos);
  }

  public BigDecimal getPokIznos(int i) {
    return (BigDecimal) pokIznos.get(i);
  }

  public String getPokCsk(int i) {
    return ((MatchRow) pok.get(i)).getCSK();
  }

  public int getPokCount() {
    return hasMatches() ? pok.size() : 0;
  }

  public boolean hasMatches() {
    return pok != null && pok.size() > 0;
  }

  public boolean isMatchable() {
    return matchable;
  }

  public void setMatchable() {
    matchable = true;
  }

  public double getMatchStrength() {
    return matchStrength;
  }

  public void setMatchStrength(double str) {
    matchStrength = str;
  }

  public String getMasterKey() {
    return mkey;
  }

  public String getBrojDok() {
    return brdok;
  }
  
  public String getExtBroj() {
    return extbr;
  }
  
  public String getCorg() {
    return corg;
  }

  public String getOznval() {
    return oznval;
  }

  public Timestamp getDatum() {
    return datdok;
  }

  public String toString() {
    return super.toString() + " csk=" + csk + " pvsaldo=" + pvsaldo;
  }

  public int compareByKey(MatchRow other) {
    return csk.compareTo(other.csk);
  }
}


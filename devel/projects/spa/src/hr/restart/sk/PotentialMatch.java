/****license*****************************************************************
**   file: PotentialMatch.java
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

import hr.restart.util.Int2;
import hr.restart.util.lookupData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class PotentialMatch {
  BigDecimal dobid, dobip, kupid, kupip;
  ArrayList dob, kup;

  public PotentialMatch() {
    dob = new ArrayList();
    kup = new ArrayList();
    dobid = dobip = kupid = kupip = raSaldaKonti.n0;
  }

  public PotentialMatch(ReadRow stavka) {
    this();
    addStavka(stavka);
  }

  public PotentialMatch(MatchRow st) {
    this();
    addStavka(st);
  }

  public void addStavka(ReadRow stavka) {
    addStavka(new MatchRow(stavka));
  }

  public void addStavka(MatchRow st) {
    if (st.getPVSaldo().signum() == 0) return;
    if (st.isKup()) {
      kup.add(st);
      if (st.isID()) kupid = kupid.add(st.getPVSaldo());
      else kupip = kupip.add(st.getPVSaldo());
    } else {
      dob.add(st);
      if (st.isID()) dobid = dobid.add(st.getPVSaldo());
      else dobip = dobip.add(st.getPVSaldo());
    }
  }

  private ArrayList getPokList_impl(ArrayList all) {
    ArrayList poks = new ArrayList();
    Collections.sort(all, new VMSaldoComparator());
    for (int i = 0; i < all.size(); i++) {
      MatchRow vm = (MatchRow) all.get(i);
      for (int j = i + 1; j < all.size(); j++) {
        MatchRow other = (MatchRow) all.get(j);
        if (vm.getPVSaldo().signum() != 0 && vm.matches(other) &&
            other.getPVSaldo().signum() != 0) {
          BigDecimal iznos = vm.getPVSaldo().abs().min(other.getPVSaldo().abs());
          vm.addMatch(other, iznos);
        }
      }
      if (vm.getPVSaldo().signum() != 0) {
        System.err.println("Illegal situation:\n"+vm);
        poks.clear();
        return poks;
      }
      if (vm.hasMatches()) poks.add(vm);
    }
    if (poks.size() > 0) System.out.println("Successfull matches:\n"+poks);
    return poks;
  }

  public ArrayList getPokList() {
    ArrayList poks = new ArrayList();
    if (kup.size() > 1 && kupid.compareTo(kupip) == 0)
      poks.addAll(getPokList_impl(kup));
    if (dob.size() > 1 && dobid.compareTo(dobip) == 0)
      poks.addAll(getPokList_impl(dob));
    return poks;
  }

  public Int2 realize(DataSet sks, DataSet pok) {
    int matched = 0, stavs = 0;
    ArrayList poks = getPokList();
    if (poks.size() > 0) {
      ++matched;
      for (int i = 0; i < poks.size(); i++, stavs++) {
        MatchRow vm = (MatchRow) poks.get(i);
        lookupData.getlookupData().raLocate(sks, "CSKSTAVKE", vm.getCSK());
        for (int j = 0; j < vm.getPokCount(); j++, stavs++)
          raSaldaKonti.matchIznos(sks, pok, vm.getPokIznos(j), vm.getPokCsk(j));
      }
    }
    return new Int2(matched, stavs);
  }

  private static class VMSaldoComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      return -((MatchRow) o1).getPVSaldo().abs().
          compareTo(((MatchRow) o2).getPVSaldo().abs());
    }
  }
}


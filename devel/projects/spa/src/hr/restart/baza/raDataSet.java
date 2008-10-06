/****license*****************************************************************
**   file: raDataSet.java
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
package hr.restart.baza;

import hr.restart.util.Aus;
import hr.restart.util.StackFrame;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: raDataSet</p>
 * <p>Description: <code>QueryDataSet</code> sa automatskim refreshanjem za iste tablice</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * Klasa koja glumi <code>QueryDataSet</code> sa podrskom za automatsko refreshanje
 * svih <code>raDataSet</code>-ova izgradjenih oko odredjene tablice ako se bilo koja od
 * njih promijeni. Metoda <code>saveChanges()</code> je overridana da nakon snimanja
 * pozove refresh na svim drugim <code>raDataSet</code>-ovima za istu tablicu. Da bi
 * stvar funkcionirala ovaj <code>raDataSet</code> mora imati setiran <code>tableName</code>
 * property, jer se pomocu tog propertija grupiraju razliciti <code>raDataSet</code>-ovi.
 * (U najvecem broju slucajeva <code>tableName</code> je setiran automatski, inace se
 * moze i rucno postaviti metodom <code>setTableName()</code>).<p>
 * Primjer koristenja:<pre>
 * raDataSet usluge = new raDataSet();
 * Artikli.getDataModule().setFilter(usluge, "vrart = 'U'");
 * </pre>
 * Nakon ovoga <code>raDataSet usluge</code> ima sve kolone klonirane iz tablice
 * Artikli, i automatski ce se refreshati kad god se promijeni bilo koji
 * raDataSet vezan uz tablicu Artikli (npr. kad se unese novi artikl u
 * maticnim podacima).
 * 
 * Update: klasa promijenjena kompletno, tako da trosi IDataSetSynchronizer
 * umjesto one stare skalamerije. Tako se na jednom mjestu rjesava i problem
 * sinkronizacije na jednom kao i na vise strojeva spojenih na istu bazu.
 *
 * @author ab.f
 * @version 1.0
 */

public class raDataSet extends QueryDataSet {
  /*
  static HashMap tables = new HashMap();
  boolean memorized, propagateChangesEnabled, autoRefreshEnabled;
  */
  
  /**
   *  String koji oznaèava odreðeni subset tablice. Ako je napunjen,
   *  onda se refresha jedino kad se promijeni dataset s istom oznakom subseta.
   *  Tipicno za doki. Will do it later.
   */ 
  private String subSet;

  public raDataSet() {
    setLocale(Aus.hr);
/*    autoRefreshEnabled = true;
    memorized = false;*/
  }

  /*public raDataSet(boolean refresh) {
    /*autoRefreshEnabled = refresh;
    memorized = false;
  }*/
  
  // glupi DataSet poziva open svako malo, npr. iz refresh() i iz saveChanges
  // Stoga ovaj flag sluzi tome da se privremeno iskljuci specijalni tretman
  // metode open(),
  boolean dsync = false;
  
  // flag za sprecavanje beskonacne rekurzije, vjerojatno nepotrebno, ali ne skodi. :)
  boolean inOpenMethod = false;
  
  /**
   * Iskljucuje odn. iskljucuje sinkronizaciju. Pozeljno iskljuciti prije
   * DataBase.saveChanges() metode, jer ista poziva open() a onda nastane
   * dernek. Poslije se moze ponovo iskljuciti (vidi raTransaction.saveChanges())
   * @param enab 
   */
  public void enableSync(boolean enab) {
    dsync = !enab;
  }
  
  /*
   * Oveeridano za provjeru azurnosti dataseta.
   */
  public boolean open() {
    /*if (!memorized && (propagateChangesEnabled = (getTableName() != null)))
    remember(getTableName().toLowerCase());
    return super.open();*/
    if (!isOpen() || dsync || inOpenMethod) {
      if (!dsync && !inOpenMethod) {
        Refresher.postpone();
        dM.getSynchronizer().markAsFresh(this);
      }
      long start = System.currentTimeMillis();
      boolean ret = super.open();
      long end = System.currentTimeMillis();
      if (end - start > 500) {
        System.out.println("Opened "+getTableName()+
            " (" + getQuery().getQueryString() + ") in "+ (end - start) + "ms");
      }
      return ret;
    }
    try {
      inOpenMethod = true;
      long start = System.currentTimeMillis();
      dM.getSynchronizer().synchronize(this);
      long end = System.currentTimeMillis();
      if (end - start > 500) {
        System.out.println("Synchronized "+getTableName()+
            " (" + getQuery().getQueryString() + ") in "+ (end - start) + "ms");
      }
      return true;
    } finally {
      inOpenMethod = false;
    }
  }
  
  public void refresh() {
    Refresher.postpone();
    enableSync(false);
    try {
      long start = System.currentTimeMillis();
      super.refresh();
      long end = System.currentTimeMillis();
      if (end - start > 500) {
        System.out.println("Refreshed "+getTableName()+
            " (" + getQuery().getQueryString() + ") in "+ (end - start) + "ms");
        StackFrame sf = StackFrame.getStackFrame();
        System.out.println("  at " + sf);
        for (int i = 0; i < 5 && sf != null; i++)
          System.out.println("  from " + (sf = sf.getParent()));
      }
    } finally {
      enableSync(true);
    }
  }

  public void saveChanges() {
    Refresher.postpone();
    enableSync(false);
    try {
      long start = System.currentTimeMillis();
      super.saveChanges();
      dM.getSynchronizer().propagateChanges(this);
      long end = System.currentTimeMillis();
      if (end - start > 500) {
        System.out.println("Saved "+getTableName()+" in "+ (end - start) + "ms");
      }
    } finally {
      enableSync(true);
    }
    /*propagateChanges();*/
  }

  /*
   * Overridano da unisti ReadRow imnplementaciju te metode, pa da se
   * moze koristiti kao kljuc za HashMap.
   */
  public boolean equals(Object o) {
    return o == this;
  }
  
  public boolean equals(raDataSet o) {
    return o == this;
  }

  /**
   * Metoda koja eksplicitno izbacuje ovaj <code>raDataSet</code> iz internih mehanizama
   * pamcenja. Bez poziva ove metode <code>raDataSet</code> nikako ne moze biti ociscen
   * (garbage collected), jer se referenca na njega drzi unutar staticke
   * <code>HashMap</code>-e.
   */
  /*public void forget() {
    /*if (memorized)
      for (Iterator i = tables.keySet().iterator(); i.hasNext();)
        ((HashSet) tables.get(i.next())).remove(this);
  }*/

  /**
   * Metoda koja eksplicitno poziva refresh svih <code>raDataSet</code>-ova na istoj
   * tablici kao i ovaj <code>raDataSet</code> (tablica koja se dobije pozivom
   * <code>this.getTableName()</code>). Obicno se poziva se automatski prilikom
   * <code>this.saveChanges()</code>, ali to mozda ne radi kod transakcija, pa je
   * stoga i ova metoda <code>public</code>.
   */
  public void propagateChanges() {
    dM.getSynchronizer().propagateChanges(this);
    /*if (memorized && propagateChangesEnabled && getTableName() != null) {
      String table = getTableName().toLowerCase();
      System.out.print("Propagating changes for table: "+table);
      if (table.length() > 0) {
        HashSet sets = (HashSet) tables.get(table);
        if (sets != null) {
          for (Iterator i = sets.iterator(); i.hasNext();)
            checkAndRefresh((raDataSet) i.next());
        }
      }
      System.out.println();
    }*/
  }

  /**
   * Flag koji odredjuje hoce li promjena u ovom <code>raDataSet</code>-u generirati
   * refresh u svim ostalima na istoj tablici.<p>
   * @param enab <code>true</code> za automatski refresh, <code>false</code> inace.
   */
  /*public void setPropagateChanges(boolean enab) {
    /*propagateChangesEnabled = enab;
  }*/

  /**
   * Flag koji odredjuje hoce li se ovaj <code>raDataSet</code> automatski refreshati
   * ako se neki drugi <code>raDataSet</code> na istoj tablici promijeni. Dakle te
   * dvije funkcionalnosti su odvojene, premda je po defaultu i jedan
   * i drugi flag postavljen na <code>true</code>.<p>
   * @param enab <code>true</code> za automatski refresh, <code>false</code> inace.
   */
  /*public void setAutoRefresh(boolean enab) {
    /*autoRefreshEnabled = enab;
  }*/

  /**
   * Eksplicitno postavlja ovisnost ovog <code>raDataSet</code>-a o promjeni na nekoj
   * tablici. Ako se promijeni bilo koji <code>raDataSet</code> na zadanoj tablici,
   * i ovaj <code>raDataSet</code> ce se refreshati. Tako je moguce da <code>raDataSet</code>
   * ovisi o vise od jedne tablice.<p>
   * @param table ime tablice (case insensitive) cija promjena generira
   * refresh ovog <code>raDataSet</code>-a
   */
  /*public void addDependency(String table) {
    /*remember(table.toLowerCase());
  }*/

  /**
   * Medota koja se poziva odmah nakon sto se ovaj dataset automatski
   * refresha. Za overridanje, ako je potrebno odredjena akcija nakon
   * automatskog refresha.
   */
  /*public void refreshed() {}*/

  /*private void checkAndRefresh(raDataSet ds) {
    /*if (ds != this && ds.autoRefreshEnabled && ds.isOpen()) {
      ds.refresh();
      ds.refreshed();
      System.out.print("+");
    } else System.out.print("-");
  }*/

  /*private void remember(String table) {
//    System.out.println("remembering "+table);
    /*memorized = true;
    if (table.length() > 0) {
      HashSet sets = (HashSet) tables.get(table);
      if (sets == null) {
        sets = new HashSet();
        tables.put(table, sets);
      }
      sets.add(this);
    }
  }*/

  /*private static void dumpTableDependency(String table, raDataSet except) {
    /*if (table.length() > 0) {
      HashSet sets = (HashSet) tables.get(table);
      if (sets != null && sets.size() > 1) {
        System.out.println("dependencies for "+table+":");
        for (Iterator i = sets.iterator(); i.hasNext();) {
          raDataSet dep = (raDataSet) i.next();
          if (dep != except)
            System.out.println("  "+dep.toStringBrief());
        }
      }
    }
  }*/

  /*public String toStringBrief() {
    return "raDataSet ("+this.getQuery().getQueryString()+"): refresh="+
           autoRefreshEnabled+" propagate="+propagateChangesEnabled;
  }*/

  /*public String toString() {
    return toStringBrief() + "\n" + super.toString();
  }*/

  /**
   * Ispisuje parametre ovog <code>raDataSeta</code> i sve <code>raDataSet</code>-ove
   * koji pripadaju istoj grupi (izgradjeni su na istoj tablici). Za debug.
   */
 /*public void dump() {
    System.out.println(this);
    if (getTableName() != null)
      dumpTableDependency(getTableName().toLowerCase(), this);
  }*/

  /**
   * Ispisuje sve grupe <code>raDataSet</code>-ova i njihove elemente. Za debug.
   */
  /*public static void dumpAll() {
    for (Iterator i = tables.keySet().iterator(); i.hasNext();)
      dumpTableDependency((String) i.next(), null);
  }*/
}

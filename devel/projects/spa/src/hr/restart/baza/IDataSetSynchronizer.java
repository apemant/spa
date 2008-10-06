/****license*****************************************************************
**   file: IDataSetSynchronizer.java
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
/*
 * Created on 2004.12.22
 */
package hr.restart.baza;

import com.borland.dx.dataset.DataSet;


/**
 * @author abf
 *
 * Interface za sinkronizaciju datasetova, smisljen tako da rijesi
 * problem sinkronizacije u mreznom radu, ali i na jednom stroju,
 * jer cesto moze biti vise datasetova po jednoj tablici (tipicno,
 * za maticne podatke postoji jedan dataset sa svim slogovima, i
 * jedan samo sa aktivnim. Ovaj prvi je bindan na ekranske komponente
 * i u njega se unose novi podaci, a ovaj drugi se koristi pri
 * dohvatu, tako da on mora znati ako je doslo do promjene na prvom.)
 */
public interface IDataSetSynchronizer {
  /**
   * Metoda koja treba vratiti poslani dataset tako da u njemu svakako
   * budu najsvjeziji podaci. Pozeljno je da ujedno otvori dataset ako
   * vec nije otvoren.<p>
   * @param ds dataset koji se sinkronizira.
   */
  void synchronize(DataSet ds);
  
  /**
   * Metoda koja treba poslati notifikaciju da je tablica promijenjena;
   * uglavnom pozvati nakon uspjesnog ds.saveChanges().<p>
   * @param ds dataset koji je promijenjen i snimljen.
   */
  void propagateChanges(DataSet ds);
  
  /**
   * Metoda koja eksplicitno postavlja obavijest da je neka tablica
   * promijenjna. Zvati u slucaju da se tablica mijenja direktno
   * putem sql-a, a ne preko nekog ds.saveChanges().<p>
   * @param table ime tablice koja je promijenjena.
   */
  void markAsDirty(String table);
  
  /**
   * Metoda koja treba eksplicitno oznaciti dataset kao svjez. Moze
   * biti potrebno ako se rucno zove metoda ds.refresh(), ili ds.open()
   * kad je dataset zatvoren, jer tada smo sigurno dovukli najsvjezije
   * podatke, pa bi bilo suvisno zvati synchronize jer bi ta metoda
   * jos jednom zvala refresh(). Dakle, cisto zbog optimizacije.<p> 
   * @param ds dataset koji se oznacava kao svjez.
   */
  void markAsFresh(DataSet ds);
  
  /**
   * Za rucna podesavanja: vraca serijski broj izmjene neke tablice.
   * Tipicna upotreba: zapamtiti ovaj broj za neku tablicu o kojoj
   * ovise podaci u rucno konstruiranom datasetu. Zatim provjeravati
   * je li se ovaj broj promijenio i onda eventualno rekonstruirati
   * ciljni dataset.
   * @param table tablica ciji serijski broj se trazi.
   * @return serijski broj izmjene tablice.
   */
  int getSerialNumber(String table);
  
}

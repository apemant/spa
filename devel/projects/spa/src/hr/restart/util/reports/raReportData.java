/****license*****************************************************************
**   file: raReportData.java
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
package hr.restart.util.reports;

/**
 * <p>Title: raReportData</p>
 * <p>Description: Interface za report data source. Klase koje implementiraju ovaj
 * interface trebale bi biti kolekcija odgovarajucih gettera za podatke u reportu,
 * tipa getCSKL(), getNAZSKL() itd. Takva klasa instancira se jednom prilikom pokretanja
 * ispisa (ubaciti odgovaraju\u0107u inicijalizaciju u konstruktor *bez parametara*), a za
 * svaki red, ukljucujuci i prvi, bit ce pozvana metoda setRow(int) koju je
 * potrebno implementirati za izvodjenje odgovarajuceg postupka koji dohvaca
 * pojedini redak, npr DataSet.goToRow() itd. Nakon zavrsetka renderiranja, bit
 * ce pozvana metoda close(), za oslobadjanje nekih resursa npr. ili za stvari
 * tipa raJpTableView.enableEvents(true) (koji su bili disablirani u konstruktoru npr.)</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public interface raReportData {
  /**
   * Metoda koja se poziva za dohvacanje svakog pojedinog retka.<p>
   * @param i redni broj retka.
   * @return instancu raReportData; ili samu sebe, ili novu, ako je bas potrebno.
   **/
  public raReportData getRow(int i);

  /**
   * Metoda koja *mora* vratiti tocan broj redaka.<p>
   * @return broj redaka za ispis.
   */
  public int getRowCount();

  /**
   * Metoda koja se poziva nakon zavrsetka renderiranja.
   */
  public void close();
}

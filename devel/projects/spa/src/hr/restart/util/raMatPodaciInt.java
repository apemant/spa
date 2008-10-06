/****license*****************************************************************
**   file: raMatPodaciInt.java
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
package hr.restart.util;

/**
 * INTERFACE
 */

public interface raMatPodaciInt {

  public void EntryPoint(char mode);
  public void ExitPoint(char mode);
  public void SetFokus(char mode);
  public boolean DeleteCheck();
  public void setRaDetailPanel(javax.swing.JPanel newRaDetailPanel) ;
  public javax.swing.JPanel getRaDetailPanel() ;
  public char getMode() ;
  public void setRaQueryDataSet(com.borland.dx.sql.dataset.QueryDataSet newRaQueryDataSet) ;
  public com.borland.dx.sql.dataset.QueryDataSet getRaQueryDataSet() ;
  public boolean  BeforeDelete();
  public void AfterDelete();
  public boolean ValidacijaPrijeIzlaza();
  public boolean Validacija(char mode);
  public void AfterSave(char mode);
  public void AfterAfterSave(char mode);
  public void setVisibleCols(int[] newVisibleCols);
  public void Funkcija_ispisa();
  public String[] getstozbrojiti();
  public void setstozbrojiti(String[] sto);
  public String[] getnaslovi();
  public void setnaslovi(String[] Nas);
  public void Zbrajalo();
  public void Minimiziraj();
  public void Restauriraj();
}
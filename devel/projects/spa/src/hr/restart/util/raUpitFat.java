/****license*****************************************************************
**   file: raUpitFat.java
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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public abstract class raUpitFat extends raUpit{
  hr.restart.util.reports.JTablePrintRun jtpr = new hr.restart.util.reports.JTablePrintRun();

  /**
   * U inicijalizaciji klase koja extenda raUpitFat upisati naslovGeneric report = "Naslov reporta";
   * ako se ne zeli naslov ekrana za naslov generickih reporta.
   * Ako se ostavi prazno onda je naslov generickih reporta jednak naslovu ekrana.
   */

  public String naslovGenericReport = "";

  public raUpitFat() {
    keySupport.setNavContainer(getJPTV().getNavBar().getNavContainer());
//    this.getJPTV().getColumnsBean().setSaveName(getClass().getName());
  }

  private raNavAction rnvIspis = new raNavAction("Ispis", raImages.IMGPRINT, java.awt.event.KeyEvent.VK_F5){
    public void actionPerformed(java.awt.event.ActionEvent e){
      ispis();
    }
  };

  protected raNavAction rnvDoubleClick = new raNavAction(navDoubleClickActionName(), raImages.IMGSTAV, java.awt.event.KeyEvent.VK_F6){
    public void actionPerformed(java.awt.event.ActionEvent e){
      jptv_doubleClick();
    }
  };
  
  private raNavAction rnvIzlaz = new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
    public void actionPerformed(ActionEvent e) {
      raKeyEsc.invokeLater();
    }
  };

  /**
   * Naslov grafièkog i matriènog ispisa
   * @return String naslov grafièkog i matriènog ispisa
   */

  public abstract String navDoubleClickActionName();

  /**
   * Kolone koje æe biti vidljive na ekranu
   * @return int[] brojevi kolona koje æe biti vidljive (u formatu new int[] {0,1,2,3,....} fixno ili nešto što æe pamtiti iste)
   */

  public abstract int[] navVisibleColumns();

  protected boolean createNavBar(){
    return true;
  }

  /*public void ok_action(){
    super.ok_action();
    if (validationPass){
      this.getJPTV().getNavBar().addOption(rnvDoubleClick, 0);
      this.getJPTV().getNavBar().addOption(rnvIspis, 1);
      this.getJPTV().getNavBar().registerNavBarKeys(this);
      this.getJPTV().getNavBar().setNavVisibleCols(navVisibleColumns());
      this.getJPTV().getColumnsBean().initialize();
      this.getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);
      rcc.setLabelLaF(this.okp.jBOK, false);
      rcc.setLabelLaF(this.okp.jReset, false);
    }
  }*/
  
  public void initColBean() {
    this.getJPTV().getNavBar().setNavVisibleCols(navVisibleColumns());
    this.getJPTV().getColumnsBean().setSaveName(getClass().getName());
    this.getJPTV().getColumnsBean().initialize();    
  }
  
  protected void addNavBarOptions(){
    if (!getJPTV().getNavBar().contains(rnvDoubleClick))
      getJPTV().getNavBar().addOption(rnvDoubleClick, 0);
    if (!getJPTV().getNavBar().contains(rnvIspis))
      getJPTV().getNavBar().addOption(rnvIspis, 1);
    if (!getJPTV().getNavBar().contains(rnvIzlaz))
      getJPTV().getNavBar().addOption(rnvIzlaz, 1);
  }
  
  protected void upitCompleted() {
/*    addNavBarOptions();
    this.getJPTV().getNavBar().registerNavBarKeys(this);
    initColBean();
    this.getJPTV().getColumnsBean().rnvRefresh.setEnabled(false); */
    rcc.setLabelLaF(this.okp.jBOK, false);
    super.upitCompleted();
  }

  public hr.restart.util.reports.raRunReport getRepRunner(){
    jtpr.setInterTitle(getClass().getName());
    jtpr.setColB(this.getJPTV().getColumnsBean());
    if (naslovGenericReport.equals("")) jtpr.setRTitle(this.getTitle());
    else jtpr.setRTitle(naslovGenericReport);
    return jtpr.getReportRunner();
  }

  public void ispis(){
    beforeReport();
    jtpr.runIt();
    afterReport();
  }
  
  protected void columnsCreated() {
    super.columnsCreated();
    
    addNavBarOptions();
    this.getJPTV().getNavBar().registerNavBarKeys(this);
    initColBean();
    this.getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);
  }

  /**
   * Metoda koja uklanja navBar sa tablice. Potrebito je pozvati u firstEsc metodi
   * na mjestu gdje se ukida tablica pritiskom na esc tipku.
   */
  
  protected void navbarremoval(){
    if (getJPTV().getNavBar().contains(rnvIspis))
      this.getJPTV().getNavBar().removeOption(rnvIspis);
    
    if (getJPTV().getNavBar().contains(rnvDoubleClick))
      this.getJPTV().getNavBar().removeOption(rnvDoubleClick);
    if (getJPTV().getNavBar().contains(rnvIzlaz))
      this.getJPTV().getNavBar().removeOption(rnvIzlaz);
  }

  public void removeNav(){
    this.getJPTV().getNavBar().unregisterNavBarKeys(this);
    navbarremoval();
    this.getJPTV().getColumnsBean().setVisible(false);
    rcc.setLabelLaF(this.okp.jBOK,true);
    rcc.setLabelLaF(this.okp.jReset,true);
  }

  public boolean isIspis() {
    return false;
  }

  public void cancelPress() {
    super.cancelPress();
    removeNav();
  }

}

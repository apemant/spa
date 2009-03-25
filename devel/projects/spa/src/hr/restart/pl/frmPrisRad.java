/****license*****************************************************************
**   file: frmPrisRad.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.util.PreSelect;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raPreSelectAware;
import hr.restart.util.startFrame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.StorageDataSet;


public class frmPrisRad extends raMatPodaci implements raPreSelectAware {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Util ut = Util.getUtil();
  Valid vl = Valid.getValid();

  jpPrisRad jpDetail;
  PreSelect preSelect;

  StorageDataSet stds;

  int maxDana;
  int god, mje;
  short dan, cvrp;
  String grupa;
  java.math.BigDecimal sati;

  public frmPrisRad() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  raNavAction rnvPreselectBotun = new raNavAction("Pred selekcija", raImages.IMGZOOM, KeyEvent.VK_F12) {
    public void actionPerformed(ActionEvent e) {
      getPreSelect().showPreselect();
      stds = getPreSelect().getSelRow();
      lookupData.getlookupData().raLocate(dm.getRadnici(),"CRADNIK",stds.getString("CRADNIK"));
      setTitle("Unos prisutnosti na radu - " + dm.getRadnici().getString("IME") + " " + dm.getRadnici().getString("PREZIME"));
      setMaxDate(); // da li je potrebno?? mislim da nije, ali za svaki slu\u010Daj evo ga ovdje ;)
      defaultValues();
      setDayToLast();
      jpDetail.jlrCvrp.forceFocLost();
      jpDetail.jlrCgrup.forceFocLost();
      getJpTableView().fireTableDataChanged();
    }
  };

  public void beforeShow(){
    String inQuery = " ("+hr.restart.zapod.OrgStr.getOrgStr().getInQuery(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig("1"))+") ";

    stds = getPreSelect().getSelRow();
    startFrame.getStartFrame().centerFrame(this, 0 ,"");
    lookupData.getlookupData().raLocate(dm.getRadnici(),"CRADNIK",stds.getString("CRADNIK"));
    setTitle("Unos prisutnosti na radu - " + dm.getRadnici().getString("IME") + " " + dm.getRadnici().getString("PREZIME"));
    setMaxDate();
    defaultValues();
    setDayToLast();
  }

  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraDan, false);
    } else if (mode == 'N') {
      defaultValues();
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      this.getRaQueryDataSet().setString("CRADNIK", stds.getString("CRADNIK"));
      fillDefaultOrRememberdValues();
      findNextDay();
      jpDetail.jraSat.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraSat.requestFocus();
    }
  }

  public boolean Validacija(char mode) {

    if (vl.notUnique(new JTextComponent[] {jpDetail.jraDan, jpDetail.jlrCvrp, jpDetail.jlrCgrup})){
      return false;
    }

    if (vl.isEmpty(jpDetail.jlrCgrup) || vl.isEmpty(jpDetail.jraDan) || vl.isEmpty(jpDetail.jraSat) || vl.isEmpty(jpDetail.jlrCvrp))
      return false;
    if (this.getRaQueryDataSet().getShort("DAN") > (short)getMaxDate()){
      jpDetail.jraDan.requestFocus();
      JOptionPane.showConfirmDialog(this,"Zadani dan ne postoji u kalendaru",
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    upamtiFrajeru();
    return true;
  }

  public void AfterSave(char mode){
  }

  public void ExitPoint(char mode){
  }

  public void AfterDelete(){
    setDayToLast();
  }

  public void afterSetMode(char pr, char po){
    if (pr == 'N' && po == 'B'){
      setDayToLast();
    }
  }

  private void setDayToLast() {
    if (this.getRaQueryDataSet().rowCount() != 0){
      this.getRaQueryDataSet().last();
      dan = this.getRaQueryDataSet().getShort("DAN");
    } else {
      dan = 0;
    }
  }

  private boolean shoWikend = true;

  public void defaultValues(){
    String shoWeekend = hr.restart.sisfun.frmParam.getParam("pl","shoWeekend","D");
    this.shoWikend = (shoWeekend.equals("D"));

    String CVRP = hr.restart.sisfun.frmParam.getParam("pl","defCVRP_ps","");
    String CGRPRIS = hr.restart.sisfun.frmParam.getParam("pl","defCGRPRIS","");
    String SATI = hr.restart.sisfun.frmParam.getParam("pl","defSATI_ps","");
    if (!CVRP.equals("")){
      cvrp = Short.valueOf(CVRP).shortValue();
    }
    if (!CGRPRIS.equals("")){
      grupa = CGRPRIS;
    }
    if (!SATI.equals("")){
      try {
        sati = new java.math.BigDecimal((SATI+".00"));
      }
      catch (Exception ex) {
        sati = new java.math.BigDecimal((SATI));
        System.out.println("neispravan format parametra defSATI_ps");
      }
    }
  }

  public void upamtiFrajeru(){
    dan = this.getRaQueryDataSet().getShort("DAN");
    cvrp = this.getRaQueryDataSet().getShort("CVRP");
    grupa = this.getRaQueryDataSet().getString("GRPRIS");
    sati = this.getRaQueryDataSet().getBigDecimal("SATI");
  }

  private void findNextDay() {
    dan++;

    if (dan > maxDana){
      dan = 1;
    }

    do {
      if(!shoWikend && !weekendDetect(dan).equals("R")){
        dan++;
      } else {
        break;
      }
    } while (dan <= maxDana);

    this.getRaQueryDataSet().setShort("DAN",(short)(dan));
  }

  private void fillDefaultOrRememberdValues(){
    this.getRaQueryDataSet().setShort("CVRP", cvrp);
    jpDetail.jlrCvrp.forceFocLost();
    this.getRaQueryDataSet().setString("GRPRIS",grupa);
    jpDetail.jlrCgrup.forceFocLost();
    this.getRaQueryDataSet().setBigDecimal("SATI", sati);
  }


  public void setPreSelect(PreSelect pres) {
    preSelect = pres;
  }

  public PreSelect getPreSelect() {
    return preSelect;
  }

  private void jbInit() throws Exception {
    this.addOption(rnvPreselectBotun, 5);
    this.setRaQueryDataSet(dm.getPrisutobr());
    this.setVisibleCols(new int[] {2,3,4});
    jpDetail = new jpPrisRad(this);
    this.setRaDetailPanel(jpDetail);
    this.getJpTableView().addTableModifier(weekendTableColorModifier);
  }

  void setMaxDate(){
    lookupData ld = lookupData.getlookupData();
    ld.raLocate(dm.getRadnicipl(),"CRADNIK",stds.getString("CRADNIK"));
    ld.raLocate(dm.getOrgpl(),"CORG",dm.getRadnicipl().getString("CORG"));
    god = dm.getOrgpl().getShort("GODOBR");
    mje = dm.getOrgpl().getShort("MJOBR");
    setMaxDate(god,mje);
  }

  void setMaxDate(int year, int month){
    java.util.Calendar cal = new java.util.GregorianCalendar();
    cal.set(year,month-1,1);
    java.sql.Date dejt = new java.sql.Date(cal.getTime().getTime());
    String datdo = new java.sql.Date(ut.getLastDayOfMonth(new java.sql.Timestamp(dejt.getTime())).getTime()).toString();
    String dan = datdo.substring(datdo.lastIndexOf("-")+1,datdo.length());
    maxDana = Integer.parseInt(dan);
  }

  public int getMaxDate(){
    return maxDana;
  }

  public String weekendDetect(short danD){
    java.util.Calendar cal = new java.util.GregorianCalendar();
    cal.set(god,mje-1,danD);
    int sedmicniDan = cal.get(java.util.Calendar.DAY_OF_WEEK);

    if (sedmicniDan == java.util.Calendar.SUNDAY) {
      return "N";
    } else if (sedmicniDan == java.util.Calendar.SATURDAY) {
      return "S";
    } else {
      return "R";
    }
  }

  hr.restart.swing.raTableModifier weekendTableColorModifier = new hr.restart.swing.raTableModifier() {
    public boolean doModify() {
      return (!isSelected());
    }
    public void modify() {
      com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
      getRaQueryDataSet().getVariant("DAN",getRow(),v);
      JComponent jRenderComp = (JComponent)renderComponent;
      if (weekendDetect(v.getShort()).equals("S")) {
        jRenderComp.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
      } else if (weekendDetect(v.getShort()).equals("N")) {
        jRenderComp.setBackground(UIManager.getColor("ComboBox.disabledForeground"));
      } else if (weekendDetect(v.getShort()).equals("P")) {
        jRenderComp.setBackground(new Color(0, 0, 255));
      } else {
        jRenderComp.setBackground(getTable().getBackground());
      }
    }
  };
}
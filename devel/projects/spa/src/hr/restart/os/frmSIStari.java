/****license*****************************************************************
**   file: frmSIStari.java
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

import java.math.BigDecimal;

import javax.swing.JOptionPane;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmSIStari extends osTemplate{

  public frmSIStari() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    dm.getOS_StSI().open();
    dm.getOS_SI().open();
    this.setMasterSet(dm.getOS_SI());
    this.setDetailSet(dm.getOS_StSI());
    pres.setSelDataSet(this.getMasterSet());
    pres.setSelPanel(this.jpSel);
    super.jbInitA();
    setJPanelMaster(jpMasterOS);
    setJPanelDetail(jpDetailOS);
    this.setNaslovMaster("Inventar iz prethodnih godina");
    //super.BindComp();
  }

  public void SetFokusDetail(char mode) {
    super.SetFokusDetail(mode);
    if(mode=='N')
    {
      if(!tds.isOpen())
        tds.open();
      if(tds.getRowCount()==0)
      {
        tds.insertRow(false);
      }
      jrfNAZPROMJENE.setText("");
      jtfDokument.setText("");
      jrfCPROMJENE.requestFocus();

      int oldrows =rdOSUtil.getUtil().getOldRows(getDetailSet().getString("CORG"),
                          getDetailSet().getString("INVBROJ"));
//      this.getDetailSet().setTimestamp("DATPROMJENE", findLastYear(hr.restart.util.Valid.getValid().findDate(false, 0)));
      getDetailSet().setTimestamp("DATPROMJENE", osUtil.getUtil().findOSDate());
      System.out.println("1");
      if(!rdOSUtil.getUtil().checkPrethodneStavke(getDetailSet()))
        rcc.EnabDisabAll(jpDetailOS, false);
    }
    else if (mode=='I')
    {
      if(amor.equals("D"))
      {
         rcc.EnabDisabAll(jpDetailOS, false);
         JOptionPane.showConfirmDialog(this.jpDetailOS,"Nije moguæa promjena !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      }

      if(!checkInsertedDate('S'))
         rcc.EnabDisabAll(this.jpDetailOS, false);
     // checkLikvidacija();
      jtfDatum.requestFocus();
    }
  }

  public void EntryPointDetail(char mode)
  {

    if (mode=='I')
    {
      if( !getMasterSet().getTimestamp("DATLIKVIDACIJE").toString().equals(nullDate.toString()))
      {
        rcc.EnabDisabAll(jpDetailOS, false);
      }
      else
      {
        rcc.EnabDisabAll(jpDetailOS, true);
      }
    }
  }

  public void beforeShowDetail()
  {
    if(getDetailSet().getRowCount()>0)
      raDetail.disableAdd();
    else
      raDetail.enableAdd();
  }

   public boolean ValidacijaDetail(char mode)
  {

    getDetailSet().setString("CORG", getMasterSet().getString("CORG"));

     if(!checkInsertedDate('S'))
    {
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Pogrešna godina !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.jtfDatum.requestFocus();
      return false;
    }

    if(!kontrolaDatUnosa())
    {
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Datum mora biti veæi ili jednak datumu na zadnjoj stavci",
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.jtfDatum.requestFocus();
      return false;
    }

//    if(!checkInsertedDate('S'))
//    {
//      JOptionPane.showConfirmDialog(this.jpDetailOS,"Godina mora biti manja od godine za koju se knjiži !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
//      this.jtfDatum.requestFocus();
//      return false;
//    }
//
//    int compare = tds.getBigDecimal("OSNOVICA").compareTo(tds.getBigDecimal("ISPRAVAK"));
//    if (compare < 0)
//    {
//      JOptionPane.showConfirmDialog(this.jpDetailOS,"Osnovica mora biti veæa od ispravka !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
//      jtfOsnovica.requestFocus();
//      return false;
//    }

    return super.ValidacijaDetail(mode);
  }

  public boolean DeleteCheckDetail()
  {
    if(!dm.getOS_Promjene().isOpen())
      dm.getOS_Promjene().open();
    oldOSN = hr.restart.os.osUtil.getUtil().getOldSIOSN(mod);
    oldISP = hr.restart.os.osUtil.getUtil().getOldSIISP(mod);
    if(!getMasterSet().getTimestamp("DATLIKVIDACIJE").equals(nullDate))
    {

      int compare = getDetailSet().getBigDecimal("OSNPOTRAZUJE").compareTo(new BigDecimal(0));
      if(compare==0)
      {
         JOptionPane.showConfirmDialog(this.jpDetailOS,"Brisanje nije moguæe ! Sredstvo likvidirano !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
         return false;
      }
    }

    if(rdOSUtil.getUtil().getTipPromjene(getDetailSet().getString("CPROMJENE")).equals("L"))
    {
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Brisanje nije moguæe !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }

    int validYear = findYear(hr.restart.util.Valid.getValid().findDate(false, 0),0);
    int datasetYear = findYear(getDetailSet().getTimestamp("DATPROMJENE"),1);
    return super.DeleteCheckDetail();

  }

  public void AfterSaveDetail(char mode) {
    if(mode=='N') {
      this.beforeShowDetail();
      this.raDetail.getOKpanel().jPrekid_actionPerformed();
    }
    super.AfterSaveDetail(mod);
  }
}
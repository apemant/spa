/****license*****************************************************************
**   file: frmStandOdb.java
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

import hr.restart.baza.Odbici;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

public class frmStandOdb extends raMatPodaci{

  short vrOd;
  jpStandOdbici jpDetail;
  String nivoOdb;
  String osnovica="";
  raCommonClass rcc = raCommonClass.getraCommonClass();

  public frmStandOdb(short vo) {
    vrOd = vo;
    try {
      jbInit();
    }
    catch (Exception ex) {
    }

  }

  private void jbInit() throws Exception
  {
    int x= (getToolkit().getDefaultToolkit().getScreenSize().width)-605;
   int y= (getToolkit().getDefaultToolkit().getScreenSize().height)-275;
    setLocation((int)x/2,(int)y/2);
    nivoOdb = plUtil.getPlUtil().getStrNivo(vrOd);
    this.setRaQueryDataSet(Odbici.getDataModule().getFilteredDataSet("CVRODB="+vrOd + " and ckey='$DEF'"));
    this.setVisibleCols(new int[] {0, 1, 2, 5, 6});
    this.setSize(605,275);
    this.setTitle("Standardni odbici");

    jpDetail = new jpStandOdbici(this);
    osnovica = plUtil.getPlUtil().getOsnovica(vrOd);
    enabDisabFields();
    this.setRaDetailPanel(jpDetail);
  }

  public void EntryPoint(char mode)
  {
    rcc.setLabelLaF(jpDetail.jraRBROdb, false);
    jpDetail.jraPnb1.requestFocus();
  }

  public void SetFokus(char mode) {
    if(mode=='N')
    {
      this.getRaQueryDataSet().setBigDecimal("IZNOS", plUtil.getPlUtil().getIznos(this.vrOd));
      this.getRaQueryDataSet().setBigDecimal("STOPA", plUtil.getPlUtil().getStopa(this.vrOd));
      this.getRaQueryDataSet().setShort("CVRODB", vrOd);
      if(nivoOdb.length()>2)
      {
        this.getRaQueryDataSet().setString("CKEY","$DEF");
        this.getRaQueryDataSet().setString("CKEY2","$DEF");
      }
      else
      {
        this.getRaQueryDataSet().setString("CKEY","$DEF");
        this.getRaQueryDataSet().setString("CKEY2","");
      }
      this.getRaQueryDataSet().setShort("RBRODB",plUtil.getPlUtil().getStandOdbRBR(vrOd, nivoOdb));
    }

  }

  public boolean Validacija(char mode) {
    return true;
  }

  private void enabDisabFields()
   {
     if( osnovica.equals("0"))
     {
       jpDetail.jlIznos.setVisible(true);
       jpDetail.jraIznos.setVisible(true);
       jpDetail.jlStopa.setVisible(false);
       jpDetail.jraStopa.setVisible(false);
       jpDetail.jraDatpoc.setVisible(true);
       jpDetail.jraDatzav.setVisible(true);
       jpDetail.jraGlavnica.setVisible(true);
       jpDetail.jraRata.setVisible(true);
       jpDetail.jraSaldo.setVisible(true);
       jpDetail.jlDatpoc.setVisible(true);
       jpDetail.jlDatzav.setVisible(true);
       jpDetail.jlGlavnica.setVisible(true);
       jpDetail.jlRata.setVisible(true);
       jpDetail.jlSaldo.setVisible(true);
       jpDetail.jlOznVal.setVisible(true);
       jpDetail.jraOznVal.setVisible(true);
       jpDetail.jbOznVal.setVisible(true);
       jpDetail.setSize(595, 195);
     }
     else
     {
       jpDetail.jlIznos.setVisible(false);
       jpDetail.jraIznos.setVisible(false);
       jpDetail.jlStopa.setVisible(true);
       jpDetail.jraStopa.setVisible(true);
       jpDetail.jraDatpoc.setVisible(false);
       jpDetail.jraDatzav.setVisible(false);
       jpDetail.jraGlavnica.setVisible(false);
       jpDetail.jraRata.setVisible(false);
       jpDetail.jraSaldo.setVisible(false);
       jpDetail.jlDatpoc.setVisible(false);
       jpDetail.jlDatzav.setVisible(false);
       jpDetail.jlGlavnica.setVisible(false);
       jpDetail.jlRata.setVisible(false);
       jpDetail.jlSaldo.setVisible(false);
       jpDetail.jlOznVal.setVisible(false);
       jpDetail.jraOznVal.setVisible(false);
       jpDetail.jbOznVal.setVisible(false);
       jpDetail.setSize(595, 195);
     }
   }
}


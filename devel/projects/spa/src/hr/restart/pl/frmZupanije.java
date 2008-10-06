/****license*****************************************************************
**   file: frmZupanije.java
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
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;





public class frmZupanije extends raMatPodaci {

  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  Valid vl = Valid.getValid();



  jpZupanije jpDetail;





  public frmZupanije() {

    super(2);

    try {

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }



  public void EntryPoint(char mode) {

    // Disabla tekst komponente kljuca kod izmjene

    if (mode == 'I') {

      rcc.setLabelLaF(jpDetail.jraCzup, false);

    }

  }



  public void SetFokus(char mode) {

    if (mode == 'N') {

      jpDetail.jraCzup.requestFocus();

    } else if (mode == 'I') {

      jpDetail.jraNazivzup.requestFocus();

    }

  }



  public boolean Validacija(char mode) {

    if (vl.isEmpty(jpDetail.jraCzup) || vl.isEmpty(jpDetail.jraNazivzup))

      return false;

    if (mode == 'N' && vl.notUnique(jpDetail.jraCzup))

      return false;

    return true;

  }



  private void jbInit() throws Exception {

    this.setRaQueryDataSet(dm.getZupanije());

    this.setVisibleCols(new int[] {0, 1});

    jpDetail = new jpZupanije(this);

    this.setRaDetailPanel(jpDetail);
    raDataIntegrity.installFor(this);
  }



/*  
 public boolean BeforeDelete()

  {

    if(plUtil.getPlUtil().checkZupStavke(getRaQueryDataSet().getShort("czup")))

    {

      JOptionPane.showConfirmDialog(this, "Nisu pobrisane stavke !", "Greška!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);

      return false;

    }



    return true;

	}
  */



}


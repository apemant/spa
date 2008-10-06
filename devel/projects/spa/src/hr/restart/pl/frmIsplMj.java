/****license*****************************************************************
**   file: frmIsplMj.java
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





public class frmIsplMj extends raMatPodaci {

  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  Valid vl = Valid.getValid();



  jpIsplMj jpDetail;





  public frmIsplMj() {

    try {

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }



  public void EntryPoint(char mode) {

    // Disabla tekst komponente kljuca kod izmjene

    if(mode=='N')

      this.jpDetail.jraCisplmj.requestFocus();

    if (mode == 'I') {

      rcc.setLabelLaF(jpDetail.jraCisplmj, false);

    }

  }



  public void SetFokus(char mode) {

    if (mode == 'N') {

      jpDetail.jlrCbanke.forceFocLost();

      jpDetail.jraCisplmj.requestFocus();

      jpDetail.jrbTipIM.setSelectedIndex(0);

      jpDetail.jrbTipFile.setSelectedIndex(0);

    } else if (mode == 'I') {

      jpDetail.jraNaziv.requestFocus();

    }

  }



  public boolean Validacija(char mode) {

    if (vl.isEmpty(jpDetail.jraCisplmj) )

      return false;

    if (mode == 'N' && vl.notUnique(jpDetail.jraCisplmj))

      return false;

    return true;

  }



  private void jbInit() throws Exception {

    this.setRaQueryDataSet(dm.getIsplMJ());

    this.setVisibleCols(new int[] {0, 1, 2, 5});

    jpDetail = new jpIsplMj(this);

    this.setRaDetailPanel(jpDetail);

    getJpTableView().addTableModifier(

      new hr.restart.swing.raTableColumnModifier("CBANKE",new String[] {"CBANKE","NAZBANKE"},dm.getBankepl())

    );



    getJpTableView().addTableModifier(

      new hr.restart.swing.raTableColumnModifier("TIPISPLMJ",new String[] {"TIPISPLMJ"},dm.getIsplMJ()) {

        public void replaceValues() {

          tableDs.getVariant("TIPISPLMJ", getRow(), shared);

          if (shared.toString().equals("T")) setComponentText("Teku\u0107i");

          else if (shared.toString().equals("G")) setComponentText("Gotovina");

          else if (shared.toString().equals("S")) setComponentText("Štednja");

          else setComponentText("Nedefinirano");

        }

      }

    );

//  replaceNames baca null pointer exception pa sam napravio sa replaceValues



//    getJpTableView().addTableModifier(

//      new raTableColumnModifier("TIPISPLMJ", new String[]{"BLABLA"}, null)

//      {

//        public void replaceNames()

//        {

//          Variant v = prepareVariants()[0];

//          if (v.getString().equals("T")) setComponentText("Teku\u0107i");

//          else if(v.getString().equals("G")) setComponentText("Gotovna");

//          else if(v.getString().equals("S")) setComponentText("Štednja");

//          else setComponentText("Nedefinirano");

//        }

//      }

//    );
    raDataIntegrity.installFor(this);
  }

}


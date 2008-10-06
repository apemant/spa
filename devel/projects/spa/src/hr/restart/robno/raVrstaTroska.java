/****license*****************************************************************
**   file: raVrstaTroska.java
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
package hr.restart.robno;
import hr.restart.util.raMatPodaci;

public class raVrstaTroska extends raMatPodaci{

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  rajpVrstaTroska rajpVrstaTroska = new rajpVrstaTroska();

  public raVrstaTroska() {
//    super(2);
    this.setRaQueryDataSet(dm.getVrtros());
    this.setVisibleCols(new int[] {0,1,2});
    this.setRaDetailPanel(rajpVrstaTroska);
  }

  public boolean Validacija (char mode) {
    return true;
  }
  public void SetFokus(char mode) {
    if (mode=='N')
      rajpVrstaTroska.jraCVRTR.requestFocus();
    else if (mode=='I')
      rajpVrstaTroska.jraNAZIV.requestFocus();
  }
  public void EntryPoint(char mode){
/*    if (mode=='N')
      hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(rajpVrstaTroska.jraCVRTR,true);
    else if (mode=='I')
      hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(rajpVrstaTroska.jraCVRTR,false);*/
  }
}
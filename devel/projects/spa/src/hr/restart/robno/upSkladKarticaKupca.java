/****license*****************************************************************
**   file: upSkladKarticaKupca.java
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

/**
 * @author S.G.
 *
 * Started 2005.04.21
 * 
 */

public class upSkladKarticaKupca extends upKarticaKupca {

  public upSkladKarticaKupca() {
    super();
  }

  protected void addReportsAndHandleColumns(){
    if (rpcart.getCART().equals("")) {
      upMas.getColumn("VC").setVisible(0);
//      this.addReport("hr.restart.robno.repIzdok", "hr.restart.robno.repIzdok",
//          "Izdok", "Pregled izlaznih dokumenata");
    } else {
      upDet.getColumn("VC").setVisible(0);
//      this.addReport("hr.restart.robno.repUldok", "hr.restart.robno.repUldok",
//          "Uldok", "Pregled ulaznih dokumenata");
    }
  }
  public int[] navVisibleColumns() {
    if (rpcart.getCART().equals("")) {
    return new int[] {0,1,2,3,4};
    } else {
      return new int[] {0,1,2,3,4,5};
    }
  }
  
}

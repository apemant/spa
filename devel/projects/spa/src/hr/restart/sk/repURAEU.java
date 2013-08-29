/****license*****************************************************************
**   file: repURAEU.java
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
package hr.restart.sk;

import java.math.BigDecimal;


public class repURAEU extends repURA {

  public repURAEU() {
    super();
  }
  
  public BigDecimal getKOL6() {
    return raIspisUraIra.getKolona(ds,"6");
//    if (!add6) return raIspisUraIra.getKolona(ds,"6");
//    return raIspisUraIra.getKolona(ds,"6").add(raIspisUraIra.getKolona(ds,"11"));
  }

  public BigDecimal getKOL8() {
    //return raIspisUraIra.getKolona(ds,"9").add(raIspisUraIra.getKolona(ds,"10"));
    return raIspisUraIra.getKolona(ds,"8");
  }

  public BigDecimal getKOL10() {
    //return raIspisUraIra.getKolona(ds,"10");
    return 
      raIspisUraIra.getKolona(ds,"11")
      .add(raIspisUraIra.getKolona(ds,"12"))
      .add(raIspisUraIra.getKolona(ds,"13"))
      .add(raIspisUraIra.getKolona(ds,"14"))
      .add(raIspisUraIra.getKolona(ds,"15"))
      .add(raIspisUraIra.getKolona(ds,"16"));
    
  }
}

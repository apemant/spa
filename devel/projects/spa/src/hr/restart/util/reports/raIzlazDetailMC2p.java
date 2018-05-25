/****license*****************************************************************
**   file: raIzlazDetailMC2p.java
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
package hr.restart.util.reports;


public class raIzlazDetailMC2p extends raIzlazDetailMC {

  public raIzlazDetailMC2p(raReportTemplate owner) {
    super(owner);
    
    TextNAZART.defaultAlterer().alterWidth(-400);
    TextKOL.defaultAlterer().moveHor(-400);
    TextJM.defaultAlterer().moveHor(-400);
    TextFMCPRP.defaultAlterer().moveHor(-400);
    TextFMCPRP.defaultAlterer().alterWidth(-180);
    TextUPRAB1.defaultAlterer().setControlSource("PRAB1");
    TextUPRAB1.defaultAlterer().moveHor(-580);
    raReportElement rab2 = copyToModify(TextUPRAB1);
    rab2.setLeft(TextUPRAB1.defaultAlterer().getLeft() + 580);
    rab2.setWidth(560);
    rab2.setControlSource("PRAB2");
  }
}

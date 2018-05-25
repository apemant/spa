/****license*****************************************************************
**   file: raIzlazSectionHeaderLines2p.java
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


public class raIzlazSectionHeaderLines2p extends raIzlazSectionHeaderLines {

  public raIzlazSectionHeaderLines2p(raReportTemplate owner) {
    super(owner);
    
    LabelNaziv.defaultAlterer().alterWidth(-440);
    LabelKolicina.defaultAlterer().moveHor(-440);
    LabelJmj.defaultAlterer().moveHor(-440);
    LabelCijena.defaultAlterer().moveHor(-440);
    LabelCijena.defaultAlterer().alterWidth(-200);
    LabelPop.defaultAlterer().moveHor(-640);
    LabelPop.defaultAlterer().setCaption("Pop1");
    raReportElement rab2 = copyToModify(LabelPop);
    rab2.setLeft(LabelPop.defaultAlterer().getLeft() + 640);
    rab2.setWidth(620);
    rab2.setCaption("Pop2");
    Label1.defaultAlterer().moveHor(-640);
    Label1.defaultAlterer().alterWidth(640);
  }
  /*
   TextNAZART.defaultAlterer().alterWidth(-400);
    TextKOL.defaultAlterer().moveHor(-400);
    TextKOL.defaultAlterer().alterWidth(-80);
    TextJM.defaultAlterer().moveHor(-480);
    TextFMCPRP.defaultAlterer().moveHor(-480);
    TextFMCPRP.defaultAlterer().alterWidth(-100);
    raReportElement rab2 = copyToModify(TextUPRAB1);
    TextUPRAB1.setControlSource("PRAB1");
    TextUPRAB1.defaultAlterer().moveHor(-580);
    rab2.setLeft(TextUPRAB1.defaultAlterer().getLeft() + 580);
    rab2.setWidth(560);
    rab2.setControlSource("PRAB2");
   */
}

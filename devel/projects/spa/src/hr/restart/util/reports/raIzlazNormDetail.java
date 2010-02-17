/****license*****************************************************************
**   file: raIzlazNormDetail.java
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

import hr.restart.robno.Aut;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

public class raIzlazNormDetail extends raIzlazDetail {

   
  public raReportElement TextNORMNAZART;
  private String[] TextNORMNAZARTProps = new String[] {"NORMNAZART", "", "", "", "", "", "Yes", "Yes", "1500", "240",
     "5000", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "Yes"};
  
  public raReportElement TextNORMKOL;
  private String[] TextNORMKOLProps = new String[] {"NORMKOL", "", "", "", "", "", "Yes", "Yes", "6600", "240",
     "500", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "Yes"};
  
  public raReportElement TextNORMJM;
  private String[] TextNORMJMProps = new String[] {"NORMJM", "", "", "", "", "", "Yes", "Yes", "7200", "240",
     "500", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "Yes"};


  public raIzlazNormDetail(raReportTemplate owner) {
    super(owner);
    addElements();
    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    TextNORMNAZART = addModel(ep.TEXT, TextNORMNAZARTProps);
    TextNORMKOL = addModel(ep.TEXT, TextNORMKOLProps);
    TextNORMJM = addModel(ep.TEXT, TextNORMJMProps);
  }

  private void modifyThis() {
  }
}

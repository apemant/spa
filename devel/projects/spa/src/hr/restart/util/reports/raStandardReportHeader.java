/****license*****************************************************************
**   file: raStandardReportHeader.java
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

public class raStandardReportHeader extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "Yes", "", "", "0"};
  //public raReportElement Text1;
  //private String[] Text1Props = new String[] {"=(create-var-cache)", "", "", "", "", "", "", "",
  //   "9240", "", "1540", "320", "", "", "", "", "", "Red", "", "", "Bold", "", "", "", ""};

  public raStandardReportHeader(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.REPORT_HEADER));
    this.setDefaults(thisProps);

    //addElements();
  }

  /*private void addElements() {
    Text1 = addModel(ep.TEXT, Text1Props);
  }*/
}

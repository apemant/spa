/****license*****************************************************************
**   file: raIzlazOTPsifKupDetail.java
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

public class raIzlazOTPsifKupDetail extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "340"};
  public raReportElement TextSTRANASIFRA;
  private String[] TextSTRANASIFRAProps = new String[] {"STRANASIFRA", "", "", "", "", "", "Yes", 
     "Yes", "1940", "", "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "3400", "", 
     "5000", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "", "", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "20", "", "440", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "9360", "", "1480", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "8420", "", "920", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};

  public raIzlazOTPsifKupDetail(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.DETAIL));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    TextSTRANASIFRA = addModel(ep.TEXT, TextSTRANASIFRAProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextJM = addModel(ep.TEXT, TextJMProps);
  }

  private void modifyThis() {
  }
}

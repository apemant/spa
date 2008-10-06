/****license*****************************************************************
**   file: raIzlazOTPsifKupDetailVri.java
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

public class raIzlazOTPsifKupDetailVri extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "340"};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "400", 
     "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "3340", "", 
     "3260", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "6660", "", "480", 
     "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "7160", "", "1000", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextZC;
  private String[] TextZCProps = new String[] {"ZC", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "8180", "", "1140", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", ""};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "420", "", "", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextIRAZ;
  private String[] TextIRAZProps = new String[] {"IRAZ", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9360", "", "1460", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextSTRANASIFRA;
  private String[] TextSTRANASIFRAProps = new String[] {"STRANASIFRA", "", "", "", "", "", "Yes", 
     "Yes", "1880", "", "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};

  public raIzlazOTPsifKupDetailVri(raReportTemplate owner) {
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
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextZC = addModel(ep.TEXT, TextZCProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextIRAZ = addModel(ep.TEXT, TextIRAZProps);
    TextSTRANASIFRA = addModel(ep.TEXT, TextSTRANASIFRAProps);
  }

  private void modifyThis() {
  }
}

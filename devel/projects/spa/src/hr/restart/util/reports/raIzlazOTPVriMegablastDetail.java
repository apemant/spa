/****license*****************************************************************
**   file: raIzlazOTPVriMegablastDetail.java
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

public class raIzlazOTPVriMegablastDetail extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "No", "No", "480"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "7180", "", "640", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "7840", "", "1480", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextIRAZ;
  private String[] TextIRAZProps = new String[] {"IRAZ", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9340", "", "1480", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "", "", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "1940", "", 
     "5220", "420", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "20", "", "440", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextKOLPAK;
  private String[] TextKOLPAKProps = new String[] {"KOLPAK", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "7840", "200", "1480", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextJMPAK;
  private String[] TextJMPAKProps = new String[] {"JMPAK", "", "", "", "", "", "", "", "7180", 
     "200", "640", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextSTRANASIFRA;
  private String[] TextSTRANASIFRAProps = new String[] {"STRANASIFRA", "", "", "", "", "", "Yes", 
     "Yes", "480", "200", "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};

  public raIzlazOTPVriMegablastDetail(raReportTemplate owner) {
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
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextIRAZ = addModel(ep.TEXT, TextIRAZProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextKOLPAK = addModel(ep.TEXT, TextKOLPAKProps);
    TextJMPAK = addModel(ep.TEXT, TextJMPAKProps);
    TextSTRANASIFRA = addModel(ep.TEXT, TextSTRANASIFRAProps);
  }

  private void modifyThis() {
  }
}

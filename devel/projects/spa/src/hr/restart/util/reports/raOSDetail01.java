/****license*****************************************************************
**   file: raOSDetail01.java
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

public class raOSDetail01 extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "320"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(put-var \"temp1\" [SumOsn]) ", "", "", "", "No", 
     "", "", "", "8740", "", "1160", "240", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", 
     "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(put-var \"row\" (+(get-var \"row\") 1))", "", "", 
     "", "No", "", "", "", "4580", "", "1300", "240", "", "", "", "", "", "", "", "", "", "", "", 
     "", ""};
  public raReportElement TextSadVr;
  private String[] TextSadVrProps = new String[] {"SadVr", "", "", "Number|true|#.##0,00", "", "", 
     "", "", "13240", "", "2420", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};
  public raReportElement TextIspVr;
  private String[] TextIspVrProps = new String[] {"IspVr", "", "", "Number|true|#.##0,00", "", "", 
     "", "", "10820", "", "2420", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};
  public raReportElement TextOsnVr;
  private String[] TextOsnVrProps = new String[] {"OsnVr", "", "", "Number|true|#.##0,00", "", "", 
     "", "", "8360", "", "2420", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};
  public raReportElement TextNazOblikListe;
  private String[] TextNazOblikListeProps = new String[] {"NazOblikListe", "", "", "", "", "", "", 
     "", "1960", "", "6240", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportElement TextOblikListe;
  private String[] TextOblikListeProps = new String[] {"OblikListe", "", "", "", "", "", "", "", "", 
     "", "1920", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};

  public raOSDetail01(raReportTemplate owner) {
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
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    TextSadVr = addModel(ep.TEXT, TextSadVrProps);
    TextIspVr = addModel(ep.TEXT, TextIspVrProps);
    TextOsnVr = addModel(ep.TEXT, TextOsnVrProps);
    TextNazOblikListe = addModel(ep.TEXT, TextNazOblikListeProps);
    TextOblikListe = addModel(ep.TEXT, TextOblikListeProps);
  }

  private void modifyThis() {
  }
}

/****license*****************************************************************
**   file: raUslSectionHeader.java
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

public class raUslSectionHeader extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", "480"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "60", "10810", "0", "", "Black", ""};
  public raReportElement LabelIznos;
  private String[] LabelIznosProps = new String[] {"Iznos", "", "8960", "100", "1860", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_artiklausluge;
  private String[] LabelNaziv_artiklauslugeProps = new String[] {"Naziv artikla/usluge", "", "460",
     "100", "8480", "220", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "",
     "", "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "100", "440", "220", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "340", "10810", "0", "", "Black", ""};

  public raUslSectionHeader(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 1));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    Line1 = addModel(ep.LINE, Line1Props);
    LabelIznos = addModel(ep.LABEL, LabelIznosProps);
    LabelNaziv_artiklausluge = addModel(ep.LABEL, LabelNaziv_artiklauslugeProps);
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}

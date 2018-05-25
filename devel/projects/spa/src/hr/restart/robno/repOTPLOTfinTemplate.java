/****license*****************************************************************
**   file: repOTPLOTfinTemplate.java
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
package hr.restart.robno;

import hr.restart.util.reports.ReportModifier;
import hr.restart.util.reports.raIzlazPageFooter;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raRPSectionHeaderROT;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class repOTPLOTfinTemplate extends repOTPvriOrigTemplate {

  public raReportSection createSectionHeader0() {
    raRPSectionHeaderROT sh = new raRPSectionHeaderROT(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nO T P R E M N I C A");
    sh.LabelR1.defaultAlterer().setVisible(false);
    sh.LabelObrazac.defaultAlterer().setVisible(false);
    return sh;
    /*
    raRPSectionHeader sh = new raRPSectionHeader(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nO T P R E M N I C A");
    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
        deleteElementsPushDown(sh, new raReportElement[] {sh.LabelNacinplacanja,
        sh.LabelDospijece, sh.TextSgetDATDOSP, sh.TextSgetDDOSP, sh.TextNAZNACPL});
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelObrazac.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    return sh;
    */
  }
  
  public raReportSection createSectionHeader1() {
    raReportSection sect = super.createSectionHeader1();
    
    raReportElement naz = LabelNaziv_artikla.defaultAlterer();
    raReportElement jm = LabelJmj.defaultAlterer();
    raReportElement kol = LabelKolicina.defaultAlterer();
    raReportElement fc = LabelCijena.defaultAlterer();
    raReportElement mc = LabelVrijednost.defaultAlterer();
    mc.setCaption("Cijena s PDV");
    raReportElement lot = sect.copyToModify(naz);
    lot.setCaption("Šarža");
    
    long spacing = jm.getLeft() - naz.getLeft() - naz.getWidth();
    
    long total = mc.getLeft() + mc.getWidth() - kol.getLeft();
    
    kol.setWidth((long) (total * 0.24));
    lot.setLeft(kol.getLeft() + kol.getWidth() + spacing);
    lot.setWidth((long) (total * 0.20));
    fc.setLeft(lot.getLeft() + lot.getWidth() + spacing);
    fc.setWidth((long) (total * 0.24));    
    mc.setLeft(fc.getLeft() + fc.getWidth() + spacing);
    mc.setWidth(total - mc.getLeft() + kol.getLeft());
    return sect;
  }
  
  
  public raReportSection createDetail() {
    raReportSection sect = super.createDetail();
    
    raReportElement naz = TextNAZART.defaultAlterer();
    raReportElement jm = TextJM.defaultAlterer();
    raReportElement kol = TextKOL.defaultAlterer();
    raReportElement fc = TextZC.defaultAlterer();
    fc.setControlSource("FC");
    raReportElement mc = TextIRAZ.defaultAlterer();    
    mc.setControlSource("FMC");
    raReportElement lot = sect.copyToModify(naz);
    lot.setControlSource("LOT");

    long spacing = jm.getLeft() - naz.getLeft() - naz.getWidth();
    
    long total = mc.getLeft() + mc.getWidth() - kol.getLeft();
    
    kol.setWidth((long) (total * 0.24));
    lot.setLeft(kol.getLeft() + kol.getWidth() + spacing);
    lot.setWidth((long) (total * 0.20));
    fc.setLeft(lot.getLeft() + lot.getWidth() + spacing);
    fc.setWidth((long) (total * 0.24));    
    mc.setLeft(fc.getLeft() + fc.getWidth() + spacing);
    mc.setWidth(total - mc.getLeft() + kol.getLeft());
    return sect;
  }
  
  public raReportSection createSectionFooter0() {
    raReportSection sect = super.createSectionFooter0();
    
    Text2.defaultAlterer().setControlSource("=(dsum \"FIZNOS\")");
    return sect;
  }
  
  public raReportSection createReportHeader() {
    return new raStandardReportHeader(this);
  }
  public raReportSection createReportFooter() {
    return new raStandardReportFooter(this);
  }
  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this); // return new raIzlazPageHeader(this);
  }
  public raReportSection createPageFooter() {
    return new raIzlazPageFooter(this);
  }
  public repOTPLOTfinTemplate() {
    this.addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }
  public void modifyThis() {
    this.SectionHeader1.restoreDefaults();
    this.Detail.restoreDefaults();
    this.LabelSifra.setCaption(Aut.getAut().getIzlazCARTdep("Šifra", "Oznaka", "Barcode"));
    this.SectionHeader1.resizeElement(this.LabelSifra, Aut.getAut().getIzlazCARTwidth(), this.LabelNaziv_artikla);
    this.Detail.resizeElement(this.TextCART, Aut.getAut().getIzlazCARTwidth(), this.TextNAZART);
  }
}
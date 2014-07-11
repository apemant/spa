/****license*****************************************************************
**   file: repInvoiceNewTemplate.java
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

import hr.restart.util.reports.raIzlazDetail;
import hr.restart.util.reports.raIzlazSectionFooterLines;
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raRPSectionHeaderROT;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repInvoiceNewTemplate extends repIzlazOrigTemplate {

  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this); // return new raIzlazPageHeader(this);
//    return createCustomSection("ROT", "H", super.createPageHeader());
  }

  public raReportSection createSectionHeader0() {
    raRPSectionHeaderROT sh = new raRPSectionHeaderROT(this);
    sh.LabelRACUNOTPREMNICA.defaultAlterer().setCaption("INVOICE");
    sh.LabelBroj.defaultAlterer().setCaption("No.");
    sh.LabelMjestoDatum.defaultAlterer().setCaption("City/date of issue");
    sh.LabelDatum_isporuke.defaultAlterer().setCaption("Delivery date");
    sh.LabelDospijeceDatum.defaultAlterer().setCaption("Payment due");
    sh.LabelNarudzba.defaultAlterer().setCaption("Order no.");
    sh.LabelNacin_placanja.defaultAlterer().setCaption("Payment mode");
    sh.LabelNacin_otpreme.defaultAlterer().setCaption("Ship via");
    sh.LabelParitet.defaultAlterer().setCaption("Parity");
    sh.LabelIsporuka.defaultAlterer().setCaption("Ship to");
    
    return sh;
  }

  public raReportSection createSectionHeader1() {
    raIzlazSectionHeaderLines sh = new raIzlazSectionHeaderLines(this); 
    sh.LabelNaziv.defaultAlterer().setCaption("Product");
    sh.LabelSifra.defaultAlterer().setCaption("Code");
    sh.LabelKolicina.defaultAlterer().setCaption("Quantity");
    sh.LabelRbr.defaultAlterer().setCaption("No.");
    sh.LabelIznos.defaultAlterer().setCaption("Total amount");
    sh.LabelCijena.defaultAlterer().setCaption("Unit price");
    sh.LabelJmj.defaultAlterer().setCaption("UM");
    sh.LabelPop.defaultAlterer().setCaption("Dsc.");
    sh.LabelPor.defaultAlterer().setCaption("VAT");
    
    return sh;
  }

  public raReportSection createDetail() {
    raIzlazDetail det = new raIzlazDetail(this);
    
    return det;
  }

  public raReportSection createSectionFooter1() {
    raIzlazSectionFooterLines sf = new raIzlazSectionFooterLines(this);
    sf.defaultAltererSect().removeModels(new raReportElement[] {
        sf.LabelREKAPITULACIJA_POREZA, sf.LabelGrupa, sf.LabelPorez, sf.LabelOsnovica, sf.Label2, 
        sf.TextPorezDepartmentUKUPOR, sf.TextPorezDepartmentCrtica, sf.TextPorezDepartmentCPOR, 
        sf.TextPorezDepartmentPOR1, sf.TextPorezDepartmentIPRODBP, sf.TextSLOVIMA, sf.LabelSlovima_
    });
    
    sf.LabelUkupno.defaultAlterer().setCaption("Total amount");
    sf.LabelPopust.defaultAlterer().setCaption("Discount");
    sf.LabelUkupno_bez_poreza.defaultAlterer().setCaption("Net value");
    sf.LabelUkupno_porez.defaultAlterer().setCaption("VAT amount");
    sf.LabelUkupno_s_porezom.defaultAlterer().setCaption("Gross value");

    return sf;
  }

  public repInvoiceNewTemplate() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepInvoiceNew");
//    hr.restart.util.Aus.dumpModel(this.template.getModel("Page Header"), 0);
  }
}

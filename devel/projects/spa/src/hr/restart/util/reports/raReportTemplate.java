/****license*****************************************************************
**   file: raReportTemplate.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import sg.com.elixir.reportwriter.xml.IModel;

/**
 * <p>Title: raReportTemplate </p>
 * <p>Description: Baza za sve report template wrappere.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: REST-ART</p>
 * <p>Klasa služi kao baza za sve report template wrappere (koji se automatski
 * generiraju iz repXXX.template fajlova). Osigurava osnovnu funkcionalnost koju
 * svi reporti moraju imati. Klase koje extendaju raReportTemplate moraju overridati
 * metodu createReportStructure koja generira strukturu pojedinih dijelova reporta.
 * Ovo se izvodi automatski tako da nema potrebe po tome petljati.</p>
 * <p>Nakon instanciranja klase koja extenda raReportTemplate report je spreman
 * za upotrebu. Pozivom metode getReportTemplate dobija se referenca na IModel
 * cijelog reporta koji se onda može postaviti metodom setReportTemplate(IModel)
 * klase ReportRuntime. Osim što vra\u0107a IModel referencu cijelog reporta, metoda
 * getReportTemplate prethodno \u0107e pozvati sve ReportModifiere koji su registrirani
 * sa tim reportom (metodom setReportModifier()). Ove modifiere je najbolje
 * registrirati iz klase koja extenda raReportTemplate.</p>
 * <p>Budu\u0107i da se klase repXXXOrigTemplate automatski kreiraju iz repXXX.template
 * fajlova, najbolje je ne pr\u010Dkati po njima direktno, nego ih extendati, pa onda
 * vršiti izmjene i dodavati ReportModifiere. Npr:</p>
 * <pre>
 * public class repCjenikTemplate extends repCjenikOrigTemplate() {
 *
 *   public repCjenikTemplate() {
 *     this.addReportModifier(new ReportModifier() {
 *       public void modify() {
 *         modifyThis();
 *       }
 *     });
 *   }
 *   private void modifyThis() {
 *     // ovdje ide kod koji radi neke izmjene ovisno o parametrima itd.
 *     // mogu se direktno mijenjati propertiji bilo kojeg elementa reporta.
 *   }
 * }
 * </pre>
 * @author ab.f
 * @version 1.0
 */

public abstract class raReportTemplate {
  private LinkedList sections = new LinkedList();
  public IModel template;
  private HashSet modifiers = new HashSet();

  private boolean automaticReset = true;

  /**
   * Default konstruktor.
   */
  public raReportTemplate() {
  }

  /**
   * Poziva sve ReportModifiere registrirane sa doti\u010Dnim reportom i vra\u0107a
   * IModel referencu.<p>
   * @return IModel refererncu cijelog report template-a.
   */

  public IModel getReportTemplate() {
    if (template != null) {
      if (automaticReset) setReportProperties();
      invokeModifiers();
      for (Iterator i = sections.iterator(); i.hasNext();
        ((raReportSection) i.next()).invokeModifiers());
      String nf = frmParam.getParam("sisfun", "globalFont", "", "Ime fonta koji zamjenjuje Lucida Bright");
      if (nf != null && nf.length() > 0)
        globalChangeFont(raElixirPropertyValues.LUCIDA_BRIGHT, nf);
    }
//    java.util.Hashtable h;
//    hr.restart.robno.Aut.getAut().dumpModel(template, 0);

    return template;
  }

  public void setAutomaticReset(boolean auto) {
    automaticReset = auto;
  }

  public void invokeModifiers() {
    for (Iterator i = modifiers.iterator(); i.hasNext(); ((ReportModifier) i.next()).modify());
  }

  public IModel getUnmodifiedReportTemplate() {
    return template;
  }

  protected raReportSection addSection(raReportSection section) {
    sections.add(section);
    return section;
  }

  /**
   * Registrira ReportModifier sa doti\u010Dnim reportom. Prilikom pozivanja metode
   * getReportTemplate(), pozivaju se metode modify() svih ovako registriranih
   * ReportModifiera.
   * @param mod ReportModifier koji radi odre\u0111enu izmjenu.
   */
  public void addReportModifier(ReportModifier mod) {
    modifiers.add(mod);
  }

  /**
   * Izbacuje ReportModifier iz liste registriranih uz ovaj report.
   * @param mod ReportModifier koji se izbacuje.
   */
  public void removeReportModifier(ReportModifier mod) {
    modifiers.remove(mod);
  }

  public void removeAllModifiers() {
    modifiers.clear();
  }

  /**
   * Postavlja sve propertije svih elemenata reporta na njihovu originalnu
   * vrijednost (prije eventualnih izmjena putem ReportModifiera).
   */
  public void setReportProperties() {
    Iterator i = sections.iterator();
    while (i.hasNext())
      ((raReportSection) i.next()).restoreDefaults();
  }

  public void globalChangeFont(String defaultFont, String newFont) {
    double ratio;
    String pr = frmParam.getParam("sisfun", "globalRatio", "1.0", "Faktor rastezanja reporta zbog promjene fonta");
    if (pr == null || pr.length() == 0)
      ratio = Aus.getFontHeightRatio(defaultFont, newFont);
    else ratio = Aus.getDecNumber(pr).doubleValue(); 
    raReportSection s;    
    Iterator i = sections.iterator();
    while (i.hasNext()) {
      s = (raReportSection) i.next();
      s.changeFont(defaultFont, newFont, ratio <= 0 ? 1.0 : ratio);
//      s.enlargeElementsVertical(1.1);
    }
  }

  public void destroyTemplate() {
    template = null;
    sections.clear();
    sections = null;
    modifiers.clear();
    modifiers = null;
  }

//  public raReportSection createCustomSection(String vrdok, String vrsta, raReportSection def) {
//    return raCustomSection.definedFor(vrdok, vrsta) ? raCustomSection.create(this, vrdok, vrsta) : def;
//  }

  public void recreateReportStructure() {
    sections.clear();
    createReportStructure();
  }
  /**
   * Apstraktna metoda koja bi trebala kreirati strukturu reporta.
   * (Dodati odgovaraju\u0107e elemente u odgovaraju\u0107e dijelove reporta itd.)
   */
  public abstract void createReportStructure();
}

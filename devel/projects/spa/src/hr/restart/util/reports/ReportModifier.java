/****license*****************************************************************
**   file: ReportModifier.java
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

/**
 * <p>Title: ReportModifier</p>
 * <p>Description: Interface za report template klase</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: REST-ART</p>
 * ReportModifier interface definira jednu metodu: modify(). Implementacija
 * ovog interfacea može se registrirati uz bilo koju klasu koja extenda raReportTemplate,
 * tako da se pri svakom ispisu doti\u010Dnog templatea pozivaju modify() metode svih
 * ReportModifiera registriranih uz taj report.
 * @author ab.f
 * @version 1.0
 */

public interface ReportModifier {
  /**
   * Metoda koju je potrebno implementirati. Poziva se pri svakom ispisu reporta
   * s kojim se ovaj ReportModifier registriran. Najbolje implementirati unutar
   * same klase koja extenda raReportTemplate.
   */
  public void modify();
}

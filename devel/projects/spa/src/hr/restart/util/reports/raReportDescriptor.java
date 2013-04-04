/****license*****************************************************************
**   file: raReportDescriptor.java
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

import hr.restart.baza.dM;
import hr.restart.util.lookupData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.design.JasperDesign;

import sg.com.elixir.reportwriter.datasource.IDataProvider;

import com.borland.dx.sql.dataset.QueryDataSet;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raReportDescriptor {
  public static final String DYNAMIC_CLASS_DS = "hr.restart.util.reports.raDynamicTableData";
  public static final String DYNAMIC_CLASS = "hr.restart.util.reports.repDynamicProvider";
  public static final String DYNAMIC_NAME = "JDOrepDynamicProvider";

  public static final String DYNAMIC_DSM = "hr.restart.util.reports/reports/dsm42.sav";
  
  public static final int TYPE_INVALID = 0;
  public static final int TYPE_ELIXIR = 1;
  public static final int TYPE_MX = 2;
  public static final int TYPE_CHART = 3;
  public static final int TYPE_JASPER = 4;

  private String pekidz;
  private String providerName;
  private Object provider;
  private String dataSource;
  private String dsProviderName;
  private String title;
  private String template;
  private raReportTemplate templateObject;
  private boolean extended, noSignature, isLogoPrinted;
  private JasperHook hook;
  
  private int reportType = TYPE_INVALID;
//  private boolean isCustomIzlaz, isCustomInter;
//  private Sections sect;

//  private static HashSet customIzlaz = new HashSet();
//  private static HashSet customInter = new HashSet();
//  private static HashMap customVrdok = new HashMap();
  private static HashMap customSect = new HashMap();
  private static HashMap sectDescr = new HashMap();

  static {
    temporaryFillReportProviders();
  }

//  public static void addCustomIzlaz(String providerClassName) {
//    customIzlaz.add(providerClassName);
//  }

//  public static void addCustomInter(String providerClassName) {
//    customInter.add(providerClassName);
//  }

//  public static void addCustomVrdok(String providerClassName, String vrdok) {
//    customVrdok.put(providerClassName, vrdok);
//  }

  public static void addCustomSect(String vrdok, String vrsec) {
    customSect.put(vrdok, Arrays.asList(new String[] {vrsec}));
  }

  public static void addCustomSect(String vrdok, String[] vrsec) {
    customSect.put(vrdok, Arrays.asList(vrsec));
  }

  public static raReportDescriptor create(String providerClassName) {
    return create(providerClassName, "", 0);
  }

  public static raReportDescriptor create(String providerClassName, String title) {
    return create(providerClassName, title, 0);
  }

  public static raReportDescriptor create(String providerClassName, String title, int dsIndex) {
    try {
      return new raReportDescriptor(providerClassName, title, dsIndex);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static raReportDescriptor create(String id, String source, String design, String title) {
    try {
      return new raReportDescriptor(id, source, design, title);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public static raReportDescriptor create(String id, String source, String jasper, String title, boolean jas) {
    try {
      return new raReportDescriptor(id, source, jasper, title, jas);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public static raReportDescriptor create(String id, String source, String title) {
    try {
      return new raReportDescriptor(id, source, title);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  private raReportDescriptor(String id, String source, String title) 
    throws Exception {
    providerName = id;
    reportType = TYPE_CHART;
    this.title = title;
    String pack = source.substring(0, source.lastIndexOf('.'));
    pekidz = pack.substring(pack.lastIndexOf('.') + 1);
    provider = source;    
  }

  private raReportDescriptor(String id, String source, String jasper, String title, boolean jas) 
    throws Exception {
    reportType = TYPE_JASPER;
    providerName = id;
    dataSource = source;
    this.title = title;
    String pack = source.substring(0, source.lastIndexOf('.'));
    pekidz = pack.substring(pack.lastIndexOf('.') + 1);
    this.template = pack.replace('.', '/') + "/reports/"+jasper;
    this.title = title;
  }

  private raReportDescriptor(String id, String source, String design, String title)
      throws Exception {
    extended = true;
    providerName = id;
    this.title = title;
    provider = raElixirDataProvider.getInstance();
    String pack = source.substring(0, source.lastIndexOf('.'));
    pekidz = pack.substring(pack.lastIndexOf('.') + 1);
    dataSource = source;
    this.dsProviderName = "JDO" + source.substring(source.lastIndexOf('.') + 1);
    reportType = TYPE_ELIXIR;
    try {
      this.templateObject = (raReportTemplate)
           Class.forName(pack + ".rep" + design + "Template").newInstance();
      this.template = "";
    } catch (Exception e) {
//      e.printStackTrace();
      this.templateObject = null;
      this.template = pack + "/reports/rep" + design + ".template";
    }
    setPrintLogo(shouldPrintLogo());
  }

  private raReportDescriptor(String providerClassName, String title, int dsIndex)
      throws Exception {
    extended = false;
    this.providerName = providerClassName;
    this.title = title;
    Class RPClass = Class.forName(providerClassName);
    if (providerClassName.equals(DYNAMIC_CLASS))
      provider = repDynamicProvider.getInstance();
    else provider = RPClass.newInstance();
    if (provider instanceof IDataProvider) {
      String pack = RPClass.getPackage().getName();
      if (providerClassName.equals(DYNAMIC_CLASS)) pekidz = "zapod";
      else pekidz = pack.substring(pack.lastIndexOf('.') + 1);
      String name = RPClass.getName().substring(RPClass.getName().lastIndexOf(".") + 1);
      if (dsIndex == 0 /*|| pack.equals("hr.restart.util.reports")*/)
        this.dataSource = pack + "/reports/dsm.sav";
      else this.dataSource = pack + "/reports/dsm" + dsIndex + ".sav";
      this.dsProviderName = "JDO" + name;
      reportType = TYPE_ELIXIR;
      try {
        this.templateObject = (raReportTemplate)
               Class.forName(pack + "." + name + "Template").newInstance();
        this.template = "";
      }
      catch (Exception e) {
        this.templateObject = null;
        this.template = pack + "/reports/" + name + ".template";
      }
    } else if (!(provider instanceof mxReport)) {
      throw new java.lang.RuntimeException("reportProvider nije instanca ni od IDataProvider ni od mxReport");
    } else reportType = TYPE_MX;
    setPrintLogo(shouldPrintLogo());
  }

  public void disableSignature() {
    noSignature = true;
  }

  public boolean isDisabledSignature() {
    return noSignature;
  }

  public boolean isJasper() {
    return reportType == TYPE_JASPER;
  }
  
  public boolean isExtended() {
    return extended;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getReportPackage() {
    return pekidz;
  }

  public String getDataSource() {
    return dataSource;
  }

  public String getProviderName() {
    return dsProviderName;
  }

  public String getName() {
    return providerName;
  }
  
  public Object getProvider() {
    if (reportType == TYPE_CHART && provider instanceof String)
      try {
        provider = Class.forName((String) provider).newInstance();
      } catch (Exception e) {
        e.printStackTrace();
      }
    return provider;
  }

  public boolean isMxReport() {
    return dataSource == null;
  }

  public boolean isJavaTemplate() {
    return templateObject != null;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public String getTemplate() {
    return template;
  }

  public raReportTemplate getJavaTemplate() {
    return templateObject;
  }

  public void setJavaTemplate(raReportTemplate temp) {
    templateObject = temp;
//    hr.restart.util.Aus.dumpModel(temp.getReportTemplate(), 0);
  }

//  public void setCustomInter(boolean yes) {
//    addCustomInter(providerName);
//  }

//  public void setCustomIzlaz(boolean yes) {
//    addCustomIzlaz(providerName);
//  }

  public boolean isCustomVrdok() {
    QueryDataSet rd = dM.getDataModule().getReportdef();
    return lookupData.getlookupData().raLocate(rd, "ID", providerName) &&
        rd.getString("VRDOK").length() > 0 && customSect.containsKey(rd.getString("VRDOK"));
  }

//  public void setCustomVrdok(String vrdok) {
//    customVrdok.put(providerName, vrdok);
//  }

  public String getCustomVrdok() {
    if (lookupData.getlookupData().raLocate(dM.getDataModule().getReportdef(), "ID", providerName))
      return dM.getDataModule().getReportdef().getString("VRDOK");
    else return null;
  }

  public List getCustomSect() {
    return (List) customSect.get(getCustomVrdok());
  }

  public static List getCustomSect(String vrdok) {
    return (List) customSect.get(vrdok);
  }

  public boolean isCustomIzlaz() {
    QueryDataSet rd = dM.getDataModule().getReportdef();
    return (lookupData.getlookupData().raLocate(rd, "ID", providerName) &&
       "D".equalsIgnoreCase(rd.getString("IZLAZNI"))) || isForeignIzlaz();
  }
  
  public boolean isForeignIzlaz() {
    QueryDataSet rd = dM.getDataModule().getReportdef();
    return lookupData.getlookupData().raLocate(rd, "ID", providerName) &&
       "F".equalsIgnoreCase(rd.getString("IZLAZNI"));
  }
  
  public boolean shouldPrintLogo() {
    String vrdok = getCustomVrdok();
    return ("D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","ispLogo","N","Ispis loga na dokumentima (D/N)")) &&
      isCustomIzlaz() && vrdok != null && lookupData.getlookupData().raLocate(
        dM.getDataModule().getVrdokum(), "VRDOK", vrdok) && 
        "D".equalsIgnoreCase(dM.getDataModule().getVrdokum().getString("ISPLOGO")));
  }
  
  public void setPrintLogo(boolean print) {
    isLogoPrinted = print;
  }

  public boolean isPrintLogo() {
    return isLogoPrinted;
  }
  
  /**
   * Vraca tip ovog izvjestaja.
   * @return ditto.
   */
  public int getReportType() {
    return reportType;
  }
//  public boolean isCustomInter() {
//    return customInter.contains(providerName);
//  }

  public String toString() {
    return super.toString() +
        " = [provider: " + provider +
        ", source: " + dataSource +
        ", name: " + dsProviderName +
        ", title: " + title +
        ", template: " + template +
        ", object: " + templateObject + "]";
  }

  public static String getSectionDescription(String sect) {
    return sectDescr.get(sect).toString();
  }

  public static String getSectionName(String sect) {
    return ((SectionDescriptor) sectDescr.get(sect)).name;
  }

  public static String getElixirModel(String sect) {
    return ((SectionDescriptor) sectDescr.get(sect)).elixir;
  }

  public static List getAllSections() {
    return new java.util.ArrayList(sectDescr.keySet());
  }

  static Map getSectionsMap() {
    return sectDescr;
  }

  private static void addSectionDescriptions() {
    sectDescr.put("SF0", new SectionDescriptor("Podnožje", "Podnožje izvještaja",
        raElixirProperties.SECTION_FOOTER + 0));
    sectDescr.put("SH1", new SectionDescriptor("Zaglavlje", "Zaglavlje izvještaja, nakon naslova",
        raElixirProperties.SECTION_HEADER + 1));
    sectDescr.put("PH", new SectionDescriptor("Header", "Zaglavlje logotipa",
        raElixirProperties.PAGE_HEADER));
    sectDescr.put("PF", new SectionDescriptor("Footer", "Podnožje logotipa",
        raElixirProperties.PAGE_FOOTER));
    sectDescr.put("PH2", new SectionDescriptor("Header", "Zaglavlje logotipa",
        raElixirProperties.PAGE_HEADER));
    sectDescr.put("PF2", new SectionDescriptor("Footer", "Podnožje logotipa",
        raElixirProperties.PAGE_FOOTER));
  }

 /* private static void fillIzlazReports() {
    String[] reports = {
      "hr.restart.robno.repRacuni", "hr.restart.robno.repRacuni2",
      "hr.restart.robno.repRacuniV", "hr.restart.robno.repRacuni2V",
      "hr.restart.robno.repRac", "hr.restart.robno.repRac2",
      "hr.restart.robno.repRacV", "hr.restart.robno.repRac2V",
      "hr.restart.robno.repRacRnal", "hr.restart.robno.repRacUsluga",
      "hr.restart.robno.repCjenik",
      "hr.restart.robno.repGotRac", "hr.restart.robno.repGotRac2",
      "hr.restart.robno.repGrnRac", "hr.restart.robno.repGrnRac2",
      "hr.restart.robno.repRacRnalKupac",
      "hr.restart.robno.repNarudzba", "hr.restart.robno.repNarudzba2",
      "hr.restart.robno.repNarudzbaV", "hr.restart.robno.repNarudzba2V",
      "hr.restart.robno.repOdobrenja",
      "hr.restart.robno.repOTP", "hr.restart.robno.repOTPvri",
      "hr.restart.robno.repPonuda", "hr.restart.robno.repPonuda2",
      "hr.restart.robno.repPonudaV", "hr.restart.robno.repPonuda2V",
      "hr.restart.robno.repPovratnicaOdobrenje",
      "hr.restart.robno.repPovratnicaTerecenje",
      "hr.restart.robno.repPredracuni", "hr.restart.robno.repPredracuni2",
      "hr.restart.robno.repPredracuniV", "hr.restart.robno.repPredracuni2V",
      "hr.restart.robno.repNarDob",
      "hr.restart.robno.repPriKalk", "hr.restart.robno.repPriKalkExtendedVersion", "hr.restart.robno.repPrkProvider"
    };
    for (int i = 0; i < reports.length; i++)
      addCustomIzlaz(reports[i]);
  }

  private static void fillReportVrdok() {
    String[][] repvrdok = {
    };
    for (int i = 0; i < repvrdok.length; i++)
      addCustomVrdok(repvrdok[i][0], repvrdok[i][1]);
  } */

  private static void fillVrdokSect() {
    String[][] vrdoksect = {
      {"GOT", "SF0"}, {"GRN", "SF0"}, {"ROT", "SF0"}, {"RAC", "SF0"},
      {"NAR", "SF0"}, {"PON", "SF0"}, {"PRD", "SF0"}, {"MES", "SF0"},
      {"MEI", "SF0"}, {"MEU", "SF0"}, {"OTP", "SF0"}, {"DOS", "SF0"}, {"NDO", "SF0"},
      {"PTE", "SF0"}, {"POD", "SF0"}, {"RNL", "SF0"}, {"PRK", "SF0"}, 
      {"ODB", "SF0"}, {"TER", "SF0"}, {"IZD", "SF0"},
      {"REV", "SF0"}, {"PRV", "SF0"}, {"PRE", "SF0"}
    };
    for (int i = 0; i < vrdoksect.length; i++)
      addCustomSect(vrdoksect[i][0], vrdoksect[i][1]);
  }

  private static void temporaryFillReportProviders() {
//    fillIzlazReports();
//    fillReportVrdok();
    fillVrdokSect();
    addSectionDescriptions();
    fillReportDefTable();
  }

  private static void fillReportDefTable() {
    String[][] repvrdok = {
      {"hr.restart.robno.repGotRac", "GOT", "D", ""},
      {"hr.restart.robno.repGotRac2", "GOT", "D", ""},
      {"hr.restart.robno.repGrnRac", "GRN", "D", ""},
      {"hr.restart.robno.repGrnRac2", "GRN", "D", ""},
      {"hr.restart.robno.repRacRnalKupac", "GRN", "D", ""},
      {"hr.restart.robno.repRacuni", "ROT", "D", ""},
      {"hr.restart.robno.repRacuni2", "ROT", "D", ""},
      {"hr.restart.robno.repRacuniV", "ROT", "D", ""},
      {"hr.restart.robno.repRacuni2V", "ROT", "D", ""},
      {"hr.restart.robno.repRacuniPnP", "ROT", "D", ""},
      {"hr.restart.robno.repRacuniSifKup", "ROT", "D", ""},
      {"hr.restart.robno.repMesklaPnP", "ROT", "D", ""},
      {"hr.restart.robno.repRacuniGetro", "ROT", "D", ""},
      {"hr.restart.robno.repRacuniMetro", "ROT", "D", ""},
      {"hr.restart.robno.repRacuniEAN", "ROT", "D", ""},
      {"hr.restart.robno.repRacuniF", "ROT", "D", ""},
      {"hr.restart.robno.repRacuniSKL", "ROT", "D", ""},
      {"hr.restart.robno.repRacuniSKLVri", "ROT", "D", ""},
      {"hr.restart.robno.repRacuniPnPOtp", "ROT", "D", ""},
      {"hr.restart.robno.repRac", "RAC", "D", ""},
      {"hr.restart.robno.repRacVert", "RAC", "D", ""},
      {"hr.restart.robno.repRacGetro", "RAC", "D", ""},
      {"hr.restart.robno.repRacNp", "RAC", "D", ""},
      {"hr.restart.robno.repRac2", "RAC", "D", ""},
      {"hr.restart.robno.repRacPnP", "RAC", "D", ""},
      {"hr.restart.robno.repRacPnP2", "RAC", "D", ""},
      {"hr.restart.robno.repRacUsluga", "RAC", "D", ""},
      {"hr.restart.robno.repRacValSingle", "RAC", "D", ""},
      {"hr.restart.robno.repRacV", "RAC", "D", ""},
      {"hr.restart.robno.repRac2V", "RAC", "D", ""},
      {"hr.restart.robno.repRacPnP", "ROT", "D", ""},
      {"hr.restart.robno.repRacRnal", "RAC", "D", ""},
      {"hr.restart.robno.repInvoice", "IVO", "F", ""},
      {"hr.restart.robno.repNarudzba", "NAR", "D", ""},
      {"hr.restart.robno.repNarudzba2", "NAR", "D", ""},
      {"hr.restart.robno.repNarudzbaV", "NAR", "D", ""},
      {"hr.restart.robno.repNarudzba2V", "NAR", "D", ""},
      {"hr.restart.robno.repPonuda", "PON", "D", ""},
      {"hr.restart.robno.repPonudaNop", "PON", "D", ""},
      {"hr.restart.robno.repPonudaValSingle", "PON", "D", ""},
      {"hr.restart.robno.repPonuda2", "PON", "D", ""},
      {"hr.restart.robno.repPonudaV", "PON", "D", ""},
      {"hr.restart.robno.repPonuda2V", "PON", "D", ""},
      {"hr.restart.robno.repPonudaKup", "PON", "D", ""},
      {"hr.restart.robno.repPonuda2Kup", "PON", "D", ""},
      {"hr.restart.robno.repPonudaVKup", "PON", "D", ""},
      {"hr.restart.robno.repPonuda2VKup", "PON", "D", ""},
      {"hr.restart.robno.repOffer", "OFF", "F", ""},
      {"hr.restart.robno.repUpitPonuda", "UZP", "D", ""},
      {"hr.restart.robno.repPredracuniKup", "PRD", "D", ""},
      {"hr.restart.robno.repPredracuni2Kup", "PRD", "D", ""},
      {"hr.restart.robno.repPredracuni", "PRD", "D", ""},
      {"hr.restart.robno.repPredracuni2", "PRD", "D", ""},
      {"hr.restart.robno.repPredracuniV", "PRD", "D", ""},
      {"hr.restart.robno.repPredracuni2V", "PRD", "D", ""},
      {"hr.restart.robno.repPredracuniValSingle", "PRD", "D", ""},
      {"hr.restart.robno.repProformaInvoice", "PIV", "F", ""},
      {"hr.restart.robno.repPovratnicaTerecenje", "PTE", "D", ""},
      {"hr.restart.robno.repMeskla", "MES", "", ""},
      {"hr.restart.robno.repMesklaSpec", "MES", "", ""},
      {"hr.restart.robno.repMesklaExtendedVersion", "MES", "", ""},
      {"hr.restart.robno.repMei", "MEI", "", ""},
      {"hr.restart.robno.repMeiExtendedVersion", "MEI", "", ""},
      {"hr.restart.robno.repMeu", "MEU", "", ""},
      {"hr.restart.robno.repMeuExtendedVersion", "MEU", "", ""},
      {"hr.restart.robno.repOTP", "OTP", "D", ""},
      {"hr.restart.robno.repOTPvri", "OTP", "D", ""},
      {"hr.restart.robno.repOTPsif", "OTP", "D", ""},
      {"hr.restart.robno.repDOS", "DOS", "D", ""},
      {"hr.restart.robno.repDOS2", "DOS", "D", ""},
      {"hr.restart.robno.repDOS3", "DOS", "D", ""},
      {"hr.restart.robno.repDOS4", "DOS", "D", ""},
      {"hr.restart.robno.repDOS5", "DOS", "D", ""},
      {"hr.restart.robno.repDOS6", "DOS", "D", ""},
      {"hr.restart.robno.repDOS7", "DOS", "D", ""},
      {"hr.restart.robno.repDOS8", "DOS", "D", ""},
      {"hr.restart.robno.repDOSGetro", "DOS", "D", ""},
      {"hr.restart.robno.repNarDob", "NDO", "D", ""},
      {"hr.restart.robno.repNarPop", "NDO", "D", ""},
      {"hr.restart.robno.repNarDobV", "NDO", "D", ""},
      {"hr.restart.robno.repNarDobKol", "NDO", "D", ""},
      {"hr.restart.robno.repRadniNalog", "RNL", "D", ""},
      {"hr.restart.robno.repRadniNalogMask", "RNL", "D", ""},
      {"hr.restart.robno.repObracunRadnogNaloga", "RNL", "D", ""},
      {"hr.restart.robno.repObracunRadnogNaloga2", "RNL", "D", ""},
      {"hr.restart.robno.repStavkeRadnogNaloga", "RNL", "D", ""},
      {"hr.restart.robno.repPrkKol", "PRK", "", ""},
      {"hr.restart.robno.repPrkVri", "PRK", "", ""},
      {"hr.restart.robno.repPrkKAl", "PRK", "", ""},
      {"hr.restart.robno.repCjenik", "", "D", ""},
      {"hr.restart.robno.repOdobrenja", "ODB", "D", ""},
      {"hr.restart.robno.repOdobrenjaP", "ODB", "D", ""},
      {"hr.restart.robno.repOdobrenjaV", "ODB", "D", ""},
      {"hr.restart.robno.repOdobrenjaPV", "ODB", "D", ""},
      {"hr.restart.robno.repTerecenja", "TER", "D", ""},
      {"hr.restart.robno.repTerecenjaV", "TER", "D", ""},
      {"hr.restart.robno.repPovratnicaOdobrenje", "POD", "D", ""},
      {"hr.restart.robno.repPovratnicaOdobrenjeNoc", "OTP", "D", ""},
      {"hr.restart.robno.repPovratnicaOdobrenjePnP", "POD", "D", ""},
      {"hr.restart.robno.repPODSifKup", "POD", "D", ""},
      {"hr.restart.robno.repPovratnicaTerecenje", "PTE", "D", ""},
      {"hr.restart.robno.repIzdatnica", "IZD", "D", ""},
      {"hr.restart.robno.repIzdatnicaExtendedVersion", "IZD", "D", ""},
      {"hr.restart.robno.repIzdatnicaIntRacun", "IZD", "D", ""},
      {"hr.restart.robno.repREVkol", "REV", "D", ""},
      {"hr.restart.robno.repREV", "REV", "D", ""},
      {"hr.restart.robno.repPRVkol", "PRV", "D", ""},
      {"hr.restart.robno.repPRV", "PRV", "D", ""}
    };
    
    try {
      QueryDataSet ds = dM.getDataModule().getReportdef();
      ds.open();
      for (int i = 0; i < repvrdok.length; i++) {
        if (!lookupData.getlookupData().raLocate(ds, "ID", repvrdok[i][0])) {
          ds.insertRow(false);
          ds.setString("ID", repvrdok[i][0]);
          if (repvrdok[i][1].length() > 0)
            ds.setString("VRDOK", repvrdok[i][1]);
          if (repvrdok[i][2].length() > 0)
            ds.setString("IZLAZNI", repvrdok[i][2]);
          if (repvrdok[i][3].length() > 0)
            ds.setString("ORIENT", repvrdok[i][3]);
          ds.post();
        }
      }
      ds.saveChanges();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void setJasperHook(JasperHook jhook) {
    hook = jhook;
  }
  
  public void adjustJasperDesign(JasperDesign design) {
    if (hook != null) hook.adjustDesign(providerName, design);
  }
}

class SectionDescriptor {
  String name, descr, elixir;
  public SectionDescriptor(String name, String descr, String elixir) {
    this.name = name;
    this.descr = descr;
    this.elixir = elixir;
  }
  public String toString() {
    return descr;
  }
}

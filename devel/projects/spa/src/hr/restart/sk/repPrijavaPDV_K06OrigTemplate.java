package hr.restart.sk;

import sg.com.elixir.*;
import sg.com.elixir.reportwriter.xml.*;

import hr.restart.util.reports.*;

public abstract class repPrijavaPDV_K06OrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepPrijavaPDV_K", 
     "", "", "10540", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"280", "567.0", "560", "567.0", "", "", "11880", 
     "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"NAZIV", "", "Yes", "", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"NAZIV", "", "Yes", "", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"NAZIV", "", "", "", "Yes", "", "Yes", "", 
     "13540"};
  public raReportElement LabelPDVK;
  private String[] LabelPDVKProps = new String[] {"PDV-K", "", "9680", "", "1040", "280", "", "", 
     "", "", "", "", "Lucida Bright", "13", "", "", "", ""};
  public raReportElement LabelOBRAZAC;
  private String[] LabelOBRAZACProps = new String[] {"OBRAZAC", "", "8560", "60", "1100", "220", "", 
     "", "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement LabelNADLEZNA_ISPOSTAVA_POREZNE_UPRAVE;
  private String[] LabelNADLEZNA_ISPOSTAVA_POREZNE_UPRAVEProps = new String[] {
     "NADLEŽNA ISPOSTAVA POREZNE UPRAVE", "", "5880", "300", "4840", "", "Normal", "-2302756", "", 
     "", "", "", "", "7", "Bold", "", "", "Center"};
  public raReportElement LabelPOREZNI_OBVEZNIK_NAZIVIME_I;
  private String[] LabelPOREZNI_OBVEZNIK_NAZIVIME_IProps = new String[] {
     "POREZNI OBVEZNIK (NAZIV/IME I PREZIME I ADRESA: MJESTO, ULICA I BROJ)", "", "", "300", "5760", 
     "", "Normal", "-2302756", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Center"};
  public raReportElement TextNAZIV;
  private String[] TextNAZIVProps = new String[] {"NAZIV", "", "", "", "", "", "", "Yes", "60", 
     "520", "5640", "640", "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "", ""};
  public raReportElement TextPORISP;
  private String[] TextPORISPProps = new String[] {"PORISP", "", "", "", "", "", "", "", "5940", 
     "520", "4700", "640", "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "", ""};
  public raReportElement Rectangle1;
  private String[] Rectangle1Props = new String[] {"", "5880", "700", "4840", "860", "", "", "", "", 
     ""};
  public raReportElement Rectangle2;
  private String[] Rectangle2Props = new String[] {"", "0", "700", "5760", "1400", "", "", "", "", 
     ""};
  public raReportElement TextADRESA;
  private String[] TextADRESAProps = new String[] {"ADRESA", "", "", "", "", "", "", "", "60", 
     "1220", "5640", "720", "Normal", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", "", 
     ""};
  public raReportElement LabelPRIJAVA_POREZA_NA_DODANU;
  private String[] LabelPRIJAVA_POREZA_NA_DODANUProps = new String[] {
     "PRIJAVA POREZA NA DODANU VRIJEDNOST ZA RAZDOBLJE", "", "5880", "1640", "4840", "680", "", "", 
     "", "", "", "", "Lucida Bright", "13", "Bold", "", "", "Center"};
  public raReportElement LabelMB_ili_JMBG__POREZNI;
  private String[] LabelMB_ili_JMBG__POREZNIProps = new String[] {"MB ili JMBG - POREZNI BROJ", "", 
     "3420", "2140", "2340", "500", "Normal", "-2302756", "", "", "", "", "Lucida Bright", "7", 
     "Bold", "", "", "Center"};
  public raReportElement LabelBROJCANA_OZNAKA_SIFRA_DJELATNOSTI;
  private String[] LabelBROJCANA_OZNAKA_SIFRA_DJELATNOSTIProps = new String[] {
     "BROJ\u010CANA OZNAKA (ŠIFRA) DJELATNOSTI PREMA NACIONALNOJ KLASIFIKACIJI", "", "", "2140", 
     "3380", "500", "Normal", "-2302756", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", 
     "Center"};
  public raReportElement TextRAZDOBLJE;
  private String[] TextRAZDOBLJEProps = new String[] {"RAZDOBLJE", "", "", "", "", "", "", "", 
     "5860", "2440", "4880", "460", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", 
     "Center", ""};
  
  public raReportElement TextODLABEL;
  private String[] TextODLABELProps = new String[] {"ODLABEL", "", "", "", "", "", "", "", 
     "6200", "2620", "400", "240", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", 
     "Center", ""};  
  public raReportElement TextODDAN;
  private String[] TextODDANProps = new String[] {"ODDAN", "", "", "", "", "", "", "", 
     "6640", "2620", "400", "240", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", 
     "Center", ""};
  public raReportElement TextODMJ;
  private String[] TextODMJProps = new String[] {"ODMJ", "", "", "", "", "", "", "", 
     "7100", "2620", "400", "240", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", 
     "Center", ""};
  
  public raReportElement TextDOLABEL;
  private String[] TextDOLABELProps = new String[] {"DOLABEL", "", "", "", "", "", "", "", 
     "7620", "2620", "400", "240", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", 
     "Center", ""};
  public raReportElement TextDODAN;
  private String[] TextDODANProps = new String[] {"DODAN", "", "", "", "", "", "", "", 
     "8060", "2620", "400", "240", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", 
     "Center", ""};
  public raReportElement TextDOMJ;
  private String[] TextDOMJProps = new String[] {"DOMJ", "", "", "", "", "", "", "", 
     "8520", "2620", "400", "240", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", 
     "Center", ""};
  public raReportElement TextGODLABEL;
  private String[] TextGODLABELProps = new String[] {"GODLABEL", "", "", "", "", "", "", "", 
     "9040", "2620", "600", "240", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", 
     "Center", ""};
  public raReportElement TextGOD;
  private String[] TextGODProps = new String[] {"GOD", "", "", "", "", "", "", "", 
     "9700", "2620", "680", "240", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", 
     "Center", ""};

  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "8420", "2640", "0", "240", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "8880", "2640", "0", "240", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "8040", "2640", "0", "240", "", "", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "10420", "2640", "0", "240", "", "", ""};
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "7020", "2640", "0", "240", "", "", ""};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "7460", "2640", "0", "240", "", "", ""};
  public raReportElement Line7;
  private String[] Line7Props = new String[] {"", "", "7080", "2640", "0", "240", "", "", ""};
  public raReportElement Line8;
  private String[] Line8Props = new String[] {"", "", "6620", "2640", "0", "240", "", "", ""};
  public raReportElement Line9;
  private String[] Line9Props = new String[] {"", "", "8480", "2640", "0", "240", "", "", ""};
  public raReportElement Line10;
  private String[] Line10Props = new String[] {"", "", "9660", "2640", "0", "240", "", "", ""};
  public raReportElement TextSIFDJEL;
  private String[] TextSIFDJELProps = new String[] {"SIFDJEL", "", "", "", "", "", "", "", "", 
     "2680", "3380", "220", "", "", "Solid", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", 
     ""};
  public raReportElement TextMB;
  private String[] TextMBProps = new String[] {"MB", "", "", "", "", "", "", "", "3420", "2680", 
     "2340", "220", "", "", "Solid", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement Line11;
  private String[] Line11Props = new String[] {"", "", "9660", "2880", "760", "0", "", "", ""};
  public raReportElement Line12;
  private String[] Line12Props = new String[] {"", "", "8500", "2880", "380", "0", "", "", ""};
  public raReportElement Line13;
  private String[] Line13Props = new String[] {"", "", "8040", "2880", "380", "0", "", "", ""};
  public raReportElement Line14;
  private String[] Line14Props = new String[] {"", "", "7080", "2880", "380", "0", "", "", ""};
  public raReportElement Line15;
  private String[] Line15Props = new String[] {"", "", "6620", "2880", "380", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "3000", "4980", "400", "Normal", 
     "-2302756", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "5100", "3000", "2760", "400", "Normal", 
     "-2302756", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label3;
  private String[] Label3Props = new String[] {"", "", "7960", "3000", "2760", "400", "Normal", 
     "-2302756", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelVRIJEDNOST_ISPORUKE_IZNOS_U;
  private String[] LabelVRIJEDNOST_ISPORUKE_IZNOS_UProps = new String[] {
     "VRIJEDNOST ISPORUKE\nIZNOS U KUNAMA I LIPAMA", "", "5080", "3060", "3000", "300", "", 
     "-2302756", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", "Center"};
  public raReportElement LabelPOREZ_PO_STOPI_OD;
  private String[] LabelPOREZ_PO_STOPI_ODProps = new String[] {
     "POREZ PO STOPI OD 22%\nIZNOS U KUNAMA I LIPAMA", "", "7840", "3060", "2880", "300", "", 
     "-2302756", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", "Center"};
  public raReportElement LabelOPIS;
  private String[] LabelOPISProps = new String[] {"OPIS", "", "", "3140", "4980", "160", "", 
     "-2302756", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", "Center"};
  public raReportElement Label4;
  private String[] Label4Props = new String[] {"", "", "", "3460", "10740", "380", "Normal", 
     "-1315861", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelOBRACUN_POREZA_U_OBAVLJENIM;
  private String[] LabelOBRACUN_POREZA_U_OBAVLJENIMProps = new String[] {
     "OBRA\u010CUN POREZA U OBAVLJENIM ISPORUKAMA DOBARA I USLUGA U OBRA\u010CUNSKOM RAZDOBLJU ISPORUKE - UKUPNO (I + II)", 
     "", "80", "3480", "5040", "340", "", "-2302756", "", "", "", "", "Lucida Bright", "6", "Bold", 
     "", "", ""};
  public raReportElement Label5;
  private String[] Label5Props = new String[] {"", "", "5200", "3520", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelX_X_X_X_X;
  private String[] LabelX_X_X_X_XProps = new String[] {"X   X   X   X   X", "", "7860", "3520", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement TextUKUPNO;
  private String[] TextUKUPNOProps = new String[] {"UKUPNO", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "3560", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label6;
  private String[] Label6Props = new String[] {"", "", "", "3900", "10740", "2660", "Normal", 
     "-1315861", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelI;
  private String[] LabelIProps = new String[] {"I.", "", "40", "3960", "260", "260", "", "", "", "", 
     "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement LabelISPORUKE_KOJE_NE_PODLJEZU;
  private String[] LabelISPORUKE_KOJE_NE_PODLJEZUProps = new String[] {
     "ISPORUKE KOJE NE PODLJEŽU OPOREZIVANJU, KOJE SU OSLOBO\u0110ENE I PO STOPI OD 0% - UKUPNO (1.+2.+3.)", 
     "", "460", "3960", "4660", "340", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", 
     ""};
  public raReportElement Label7;
  private String[] Label7Props = new String[] {"", "", "5200", "3960", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelX_X_X_X_X1;
  private String[] LabelX_X_X_X_X1Props = new String[] {"X   X   X   X   X", "", "7860", "3960", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement TextI;
  private String[] TextIProps = new String[] {"I", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "5200", "4000", "2560", "220", "", "", "", "", "", "", "Lucida Bright", "9", 
     "", "", "", "Right", ""};
  public raReportElement Label8;
  private String[] Label8Props = new String[] {"", "", "360", "4400", "10120", "", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKOJE_NE_PODLJEZU_OPOREZIVANJU;
  private String[] LabelKOJE_NE_PODLJEZU_OPOREZIVANJUProps = new String[] {
     "KOJE NE PODLJEŽU OPOREZIVANJU", "", "680", "4400", "3580", "180", "", "", "", "", "", "", 
     "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label9;
  private String[] Label9Props = new String[] {"", "", "5200", "4420", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelX_X_X_X_X2;
  private String[] LabelX_X_X_X_X2Props = new String[] {"X   X   X   X   X", "", "7860", "4420", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement TextI1;
  private String[] TextI1Props = new String[] {"I1", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "5200", "4460", "2560", "220", "", "", "", "", "", "", "Lucida Bright", "9", 
     "", "", "", "Right", ""};
  public raReportElement Label11;
  private String[] Label11Props = new String[] {"1.", "", "440", "4480", "300", "260", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Labelcl_2_u_svezi_s_cl;
  private String[] Labelcl_2_u_svezi_s_clProps = new String[] {
     "(\u010Dl. 2. u svezi s \u010Dl. 5. i \u010Dl. 8. st. 8. Zakona)", "", "680", "4560", "3460", 
     "200", "", "", "", "", "", "", "Lucida Bright", "6", "", "", "", ""};
  public raReportElement Label10;
  private String[] Label10Props = new String[] {"", "", "360", "4800", "10120", "1320", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label111;
  private String[] Label111Props = new String[] {"", "", "5200", "4820", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelX_X_X_X_X3;
  private String[] LabelX_X_X_X_X3Props = new String[] {"X   X   X   X   X", "", "7860", "4820", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement LabelOSLOBODjENE_POREZA__UKUPNO;
  private String[] LabelOSLOBODjENE_POREZA__UKUPNOProps = new String[] {
     "OSLOBO\u0110ENE POREZA - UKUPNO (2.1+2.2)", "", "680", "4840", "3960", "200", "", "", "", "", 
     "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label21;
  private String[] Label21Props = new String[] {"2.", "", "440", "4840", "320", "300", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement TextI2;
  private String[] TextI2Props = new String[] {"I2", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "5200", "4860", "2560", "220", "", "", "", "", "", "", "Lucida Bright", "9", 
     "", "", "", "Right", ""};
  public raReportElement Label12;
  private String[] Label12Props = new String[] {"", "", "1140", "5140", "9320", "960", "Normal", 
     "-1973791", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label13;
  private String[] Label13Props = new String[] {"", "", "5200", "5180", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelX_X_X_X_X4;
  private String[] LabelX_X_X_X_X4Props = new String[] {"X   X   X   X   X", "", "7860", "5180", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement TextI21;
  private String[] TextI21Props = new String[] {"I21", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "5220", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement Label211;
  private String[] Label211Props = new String[] {"2.1.", "", "1180", "5220", "440", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement LabelIZVOZNE__s_pravom;
  private String[] LabelIZVOZNE__s_pravomProps = new String[] {"IZVOZNE - s pravom na odbitak", "", 
     "1600", "5260", "3540", "220", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", 
     ""};
  public raReportElement Label14;
  private String[] Label14Props = new String[] {"", "", "5200", "5480", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelX_X_X_X_X5;
  private String[] LabelX_X_X_X_X5Props = new String[] {"X   X   X   X   X", "", "7860", "5480", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement TextI22;
  private String[] TextI22Props = new String[] {"I22", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "5520", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement Label22;
  private String[] Label22Props = new String[] {"2.2.", "", "1180", "5520", "440", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement LabelTUZEMNE__bez_prava;
  private String[] LabelTUZEMNE__bez_pravaProps = new String[] {"TUZEMNE - bez prava na odbitak", 
     "", "1600", "5560", "3540", "220", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", 
     "", ""};
  public raReportElement Label15;
  private String[] Label15Props = new String[] {"", "", "5200", "5780", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelX_X_X_X_X6;
  private String[] LabelX_X_X_X_X6Props = new String[] {"X   X   X   X   X", "", "7860", "5780", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement TextI23;
  private String[] TextI23Props = new String[] {"I23", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "5820", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement Label23;
  private String[] Label23Props = new String[] {"2.3.", "", "1180", "5840", "440", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement LabelOSTALO;
  private String[] LabelOSTALOProps = new String[] {"OSTALO", "", "1600", "5860", "3520", "220", "", 
     "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label16;
  private String[] Label16Props = new String[] {"", "", "360", "6160", "10120", "340", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label17;
  private String[] Label17Props = new String[] {"", "", "5200", "6180", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelX_X_X_X_X7;
  private String[] LabelX_X_X_X_X7Props = new String[] {"X   X   X   X   X", "", "7860", "6180", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement Label31;
  private String[] Label31Props = new String[] {"3.", "", "440", "6200", "300", "260", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement TextI3;
  private String[] TextI3Props = new String[] {"I3", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "5200", "6220", "2560", "220", "", "", "", "", "", "", "Lucida Bright", "", "", 
     "", "", "Right", ""};
  public raReportElement LabelISPORUKE_PO_STOPI_0;
  private String[] LabelISPORUKE_PO_STOPI_0Props = new String[] {
     "ISPORUKE PO STOPI 0%    (\u010Dl. 10a. Zakona)", "", "680", "6220", "3960", "240", "", "", "", 
     "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label18;
  private String[] Label18Props = new String[] {"", "", "", "6600", "10740", "2020", "Normal", 
     "-1315861", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelII;
  private String[] LabelIIProps = new String[] {"II.", "", "40", "6640", "640", "300", "", "", "", 
     "", "", "", "Lucida Bright", "12", "Bold", "", "", ""};
  public raReportElement Label19;
  private String[] Label19Props = new String[] {"", "", "7860", "6660", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label20;
  private String[] Label20Props = new String[] {"", "", "5200", "6660", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextIIv;
  private String[] TextIIvProps = new String[] {"IIv", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "6700", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement TextIIp;
  private String[] TextIIpProps = new String[] {"IIp", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "6700", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement LabelOPOREZIVE_ISPORUKE__UKUPNO;
  private String[] LabelOPOREZIVE_ISPORUKE__UKUPNOProps = new String[] {
     "OPOREZIVE ISPORUKE - UKUPNO (1.+2.+3.+4.+5.)", "", "460", "6700", "4660", "200", "", "", "", 
     "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label212;
  private String[] Label212Props = new String[] {"", "", "360", "6980", "10120", "380", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label221;
  private String[] Label221Props = new String[] {"", "", "5200", "7020", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label231;
  private String[] Label231Props = new String[] {"", "", "7860", "7020", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelZA_KOJE_SU_IZDANI;
  private String[] LabelZA_KOJE_SU_IZDANIProps = new String[] {
     "ZA KOJE SU IZDANI RA\u010CUNI, NEZARA\u010CUNANE I VLASTITA POTROŠNJA po stopi od 10%", "", 
     "680", "7040", "4520", "320", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement TextII1v;
  private String[] TextII1vProps = new String[] {"II1v", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "7060", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement TextII1p;
  private String[] TextII1pProps = new String[] {"II1p", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "7060", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label110;
  private String[] Label110Props = new String[] {"1.", "", "440", "7080", "300", "300", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Label24;
  private String[] Label24Props = new String[] {"", "", "360", "7400", "10120", "", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label25;
  private String[] Label25Props = new String[] {"", "", "5200", "7420", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label26;
  private String[] Label26Props = new String[] {"", "", "7860", "7420", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextII2v;
  private String[] TextII2vProps = new String[] {"II2v", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "7460", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement TextII2p;
  private String[] TextII2pProps = new String[] {"II2p", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "7460", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement LabelZA_KOJE_SU_IZDANI1;
  private String[] LabelZA_KOJE_SU_IZDANI1Props = new String[] {
     "ZA KOJE SU IZDANI RA\u010CUNI, NEZARA\u010CUNANE I VLASTITA POTROŠNJA po stopi od 22%", "", 
     "680", "7460", "4520", "320", "", "", "", "", "", "", "Lucida Bright", "6", "", "", "", ""};
  public raReportElement Label27;
  private String[] Label27Props = new String[] {"2.", "", "440", "7480", "300", "300", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Label271;
  private String[] Label271Props = new String[] {"", "", "360", "7800", "10120", "", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label28;
  private String[] Label28Props = new String[] {"", "", "7860", "7820", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label29;
  private String[] Label29Props = new String[] {"", "", "5200", "7820", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextII3v;
  private String[] TextII3vProps = new String[] {"II3v", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "7860", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement TextII3p;
  private String[] TextII3pProps = new String[] {"II3p", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "7860", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label32;
  private String[] Label32Props = new String[] {"3.", "", "440", "7880", "300", "300", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement LabelNENAPLACENI_IZVOZ;
  private String[] LabelNENAPLACENI_IZVOZProps = new String[] {"NENAPLA\u0106ENI IZVOZ", "", "680", 
     "7920", "4520", "260", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label30;
  private String[] Label30Props = new String[] {"", "", "360", "8200", "10120", "", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label311;
  private String[] Label311Props = new String[] {"", "", "7860", "8220", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label321;
  private String[] Label321Props = new String[] {"", "", "5200", "8220", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextII4v;
  private String[] TextII4vProps = new String[] {"II4v", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "8260", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement TextII4p;
  private String[] TextII4pProps = new String[] {"II4p", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "8260", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label41;
  private String[] Label41Props = new String[] {"4.", "", "440", "8280", "300", "300", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement LabelOSTALO1;
  private String[] LabelOSTALO1Props = new String[] {"OSTALO", "", "680", "8320", "4540", "260", "", 
     "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label33;
  private String[] Label33Props = new String[] {"", "", "", "8660", "10740", "2920", "Normal", 
     "-1315861", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  
  public raReportElement LabelNAKNADNO_OSLOB_IZVOZA_U;
  private String[] LabelNAKNADNO_OSLOB_IZVOZA_UProps = new String[] {
     "NAKNADNO OSLOB. IZVOZA U OKVIRU OSOBNOG PUTN. PROMETA", "", "680", "8300", "4540", "260", "", 
     "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};

  
  public raReportElement LabelOBRACUNANI_PRETPOREZ_U_PRIMLJENIM;
  private String[] LabelOBRACUNANI_PRETPOREZ_U_PRIMLJENIMProps = new String[] {
     "OBRA\u010CUNANI PRETPOREZ U PRIMLJENIM ISPORUKAMA DOBARA I USLUGA - UKUPNO (1.+2.+3.+4.)", "", 
     "460", "8740", "4680", "400", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label34;
  private String[] Label34Props = new String[] {"", "", "5200", "8780", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label35;
  private String[] Label35Props = new String[] {"", "", "7860", "8780", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelIII;
  private String[] LabelIIIProps = new String[] {"III.", "", "40", "8780", "640", "300", "", "", "", 
     "", "", "", "Lucida Bright", "12", "Bold", "", "", ""};
  public raReportElement TextIIIv;
  private String[] TextIIIvProps = new String[] {"IIIv", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "8820", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement TextIIIp;
  private String[] TextIIIpProps = new String[] {"IIIp", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "8820", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label36;
  private String[] Label36Props = new String[] {"", "", "360", "9140", "10120", "380", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label37;
  private String[] Label37Props = new String[] {"", "", "7860", "9160", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label38;
  private String[] Label38Props = new String[] {"", "", "5200", "9160", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextIII1v;
  private String[] TextIII1vProps = new String[] {"III1v", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "9200", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement TextIII1p;
  private String[] TextIII1pProps = new String[] {"III1p", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "9200", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label112;
  private String[] Label112Props = new String[] {"1.", "", "440", "9220", "300", "300", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMA;
  private String[] LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMAProps = new String[] {
     "PRETPOREZ U PRIMLJENIM RA\u010CUNIMA po stopi od 10%", "", "680", "9260", "4220", "260", "", 
     "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label39;
  private String[] Label39Props = new String[] {"", "", "360", "9540", "10120", "380", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label40;
  private String[] Label40Props = new String[] {"", "", "5200", "9560", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label411;
  private String[] Label411Props = new String[] {"", "", "7860", "9560", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextIII2v;
  private String[] TextIII2vProps = new String[] {"III2v", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "9600", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement TextIII2p;
  private String[] TextIII2pProps = new String[] {"III2p", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "9600", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label210;
  private String[] Label210Props = new String[] {"2.", "", "440", "9620", "300", "300", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMA1;
  private String[] LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMA1Props = new String[] {
     "PRETPOREZ U PRIMLJENIM RA\u010CUNIMA po stopi od 22%", "", "680", "9660", "4240", "260", "", 
     "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label42;
  private String[] Label42Props = new String[] {"", "", "360", "9940", "10120", "380", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label43;
  private String[] Label43Props = new String[] {"", "", "5200", "9960", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label44;
  private String[] Label44Props = new String[] {"", "", "7860", "9960", "2560", "280", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextIII3v;
  private String[] TextIII3vProps = new String[] {"III3v", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "10000", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement TextIII3p;
  private String[] TextIII3pProps = new String[] {"III3p", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "10000", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label310;
  private String[] Label310Props = new String[] {"3.", "", "440", "10020", "300", "300", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement LabelPLACENI_PRETPOREZ_PRI_UVOZU;
  private String[] LabelPLACENI_PRETPOREZ_PRI_UVOZUProps = new String[] {
     "PLA\u0106ENI PRETPOREZ PRI UVOZU", "", "680", "10060", "4260", "260", "", "", "", "", "", "", 
     "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label45;
  private String[] Label45Props = new String[] {"", "", "360", "10340", "10120", "380", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label46;
  private String[] Label46Props = new String[] {"", "", "7860", "10360", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label47;
  private String[] Label47Props = new String[] {"", "", "5200", "10360", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextIII4v;
  private String[] TextIII4vProps = new String[] {"III4v", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "10400", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement TextIII4p;
  private String[] TextIII4pProps = new String[] {"III4p", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "10400", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement LabelPLACENI_PRETPOREZ_NA_USLUGE;
  private String[] LabelPLACENI_PRETPOREZ_NA_USLUGEProps = new String[] {
     "PLA\u0106ENI PRETPOREZ NA USLUGE INOZEMNIH PODUZETNIKA po stopi od 10%", "", "680", "10400", 
     "4260", "320", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label48;
  private String[] Label48Props = new String[] {"4.", "", "440", "10420", "300", "300", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Label481;
  private String[] Label481Props = new String[] {"", "", "360", "10740", "10120", "380", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label49;
  private String[] Label49Props = new String[] {"", "", "7860", "10760", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label50;
  private String[] Label50Props = new String[] {"", "", "5200", "10760", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextIII5p;
  private String[] TextIII5pProps = new String[] {"III5p", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "10800", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement TextIII5v;
  private String[] TextIII5vProps = new String[] {"III5v", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5200", "10800", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement LabelPLACENI_PRETPOREZ_NA_USLUGE1;
  private String[] LabelPLACENI_PRETPOREZ_NA_USLUGE1Props = new String[] {
     "PLA\u0106ENI PRETPOREZ NA USLUGE INOZEMNIH PODUZETNIKA po stopi od 22%", "", "680", "10800", 
     "4240", "320", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label51;
  private String[] Label51Props = new String[] {"5.", "", "440", "10820", "300", "300", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Label511;
  private String[] Label511Props = new String[] {"", "", "360", "11140", "10120", "380", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelX_X_X_X_X8;
  private String[] LabelX_X_X_X_X8Props = new String[] {"X   X   X   X   X", "", "5200", "11160", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement Label52;
  private String[] Label52Props = new String[] {"", "", "7860", "11160", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextIII6;
  private String[] TextIII6Props = new String[] {"III6", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7860", "11200", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label61;
  private String[] Label61Props = new String[] {"6.", "", "440", "11220", "300", "300", "", "", "", 
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement LabelISPRAVCI_PRETPOREZA_PREMA_CL;
  private String[] LabelISPRAVCI_PRETPOREZA_PREMA_CLProps = new String[] {
     "ISPRAVCI PRETPOREZA PREMA \u010CL. 20. ST. 5. ZAKONA", "", "680", "11260", "4240", "260", "", 
     "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label53;
  private String[] Label53Props = new String[] {"", "", "", "11620", "10740", "380", "Normal", 
     "-1315861", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPOREZNA_OBVEZA_U_OBRACUNSKOM;
  private String[] LabelPOREZNA_OBVEZA_U_OBRACUNSKOMProps = new String[] {
     "POREZNA OBVEZA U OBRA\u010CUNSKOM RAZDOBLJU:\nZA UPLATU (II.-III.) ILI ZA POVRAT (III.-II.)", 
     "", "460", "11620", "4680", "420", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", 
     "", ""};
  public raReportElement Label54;
  private String[] Label54Props = new String[] {"", "", "7860", "11660", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelX_X_X_X_X9;
  private String[] LabelX_X_X_X_X9Props = new String[] {"X   X   X   X   X", "", "5200", "11660", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement LabelIV;
  private String[] LabelIVProps = new String[] {"IV.", "", "40", "11680", "640", "280", "", "", "", 
     "", "", "", "Lucida Bright", "12", "Bold", "", "", ""};
  public raReportElement TextIV;
  private String[] TextIVProps = new String[] {"IV", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "7860", "11700", "2560", "220", "", "", "", "", "", "", "Lucida Bright", "", 
     "", "", "", "Right", ""};
  public raReportElement Label55;
  private String[] Label55Props = new String[] {"", "", "", "12060", "10740", "380", "Normal", 
     "-1315861", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPO_PRETHODNOM_OBRACUNU_NEUPLACENI;
  private String[] LabelPO_PRETHODNOM_OBRACUNU_NEUPLACENIProps = new String[] {
     "UPLA\u0106ENO DO DANA PODNOŠENJA OVE PRIJAVE", 
     "", "460", "12060", "4780", "380", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", 
     "", ""};
  public raReportElement Label56;
  private String[] Label56Props = new String[] {"", "", "7860", "12100", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelX_X_X_X_X10;
  private String[] LabelX_X_X_X_X10Props = new String[] {"X   X   X   X   X", "", "5200", "12100", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement LabelV;
  private String[] LabelVProps = new String[] {"V.", "", "40", "12120", "640", "280", "", "", "", 
     "", "", "", "Lucida Bright", "12", "Bold", "", "", ""};
  public raReportElement TextV;
  private String[] TextVProps = new String[] {"V", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "7860", "12140", "2560", "220", "", "", "", "", "", "", "Lucida Bright", "", 
     "", "", "", "Right", ""};
  public raReportElement Label57;
  private String[] Label57Props = new String[] {"", "", "", "12500", "10740", "380", "Normal", 
     "-1315861", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label58;
  private String[] Label58Props = new String[] {"", "", "7860", "12540", "2560", "280", "Normal", 
     "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelX_X_X_X_X11;
  private String[] LabelX_X_X_X_X11Props = new String[] {"X   X   X   X   X", "", "5200", "12540", 
     "2560", "280", "", "", "", "", "", "", "Dialog", "11", "Bold", "", "", "Center"};
  public raReportElement LabelVI;
  private String[] LabelVIProps = new String[] {"VI.", "", "40", "12560", "640", "280", "", "", "", 
     "", "", "", "Lucida Bright", "12", "Bold", "", "", ""};
  public raReportElement TextVI;
  private String[] TextVIProps = new String[] {"VI", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "7860", "12580", "2560", "220", "", "", "", "", "", "", "Lucida Bright", "", 
     "", "", "", "Right", ""};
  public raReportElement LabelUKUPNO_RAZLIKA_ZA_UPLATU;
  private String[] LabelUKUPNO_RAZLIKA_ZA_UPLATUProps = new String[] {
     "UKUPNO RAZLIKA: ZA UPLATU / ZA POVRAT", "", "460", "12600", "4680", "260", "", "", "", "", "", 
     "", "Lucida Bright", "6", "Bold", "", "", ""};
  
  public raReportElement LabelKn;
  private String[] LabelKnProps = new String[] {"\nKn", "", "6280", "12940", "300", "380", "", "", 
     "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement LabelKn1;
  private String[] LabelKn1Props = new String[] {"\nKn", "", "10480", "12940", "300", "380", "", "", 
     "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement LabelKn2;
  private String[] LabelKn2Props = new String[] {"\nKn", "", "1960", "12940", "300", "380", "", "", 
     "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Rectangle3;
  private String[] Rectangle3Props = new String[] {"", "4320", "12960", "280", "300", "", "", "", 
     "", ""};
  public raReportElement Rectangle4;
  private String[] Rectangle4Props = new String[] {"", "8520", "12960", "280", "300", "", "", "", 
     "", ""};
  public raReportElement Rectangle5;
  private String[] Rectangle5Props = new String[] {"", "0", "12960", "280", "300", "", "", "", "", 
     ""};
  public raReportElement LabelPREDUJAM;
  private String[] LabelPREDUJAMProps = new String[] {"\nPREDUJAM", "", "4600", "13120", "1640", 
     "300", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", "Center"};
  public raReportElement LabelUSTUP_POVRATA;
  private String[] LabelUSTUP_POVRATAProps = new String[] {"\nUSTUP POVRATA", "", "8800", "13120", 
     "1640", "300", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", "Center"};
  public raReportElement LabelZA_POVRAT;
  private String[] LabelZA_POVRATProps = new String[] {"\nZA POVRAT", "", "280", "13120", "1640", 
     "300", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", "Center"};

  public raReportElement Line116;
  private String[] Line116Props = new String[] {"", "", "4620", "13240", "1620", "0", "", "", ""};
  public raReportElement Line117;
  private String[] Line117Props = new String[] {"", "", "8820", "13240", "1620", "0", "", "", ""};
  public raReportElement Line118;
  private String[] Line118Props = new String[] {"", "", "300", "13240", "1620", "0", "", "", ""};

  
//  public raReportElement LabelZA_IZNOS_PREPLATE_PODNOSI;
//  private String[] LabelZA_IZNOS_PREPLATE_PODNOSIProps = new String[] {
//     "ZA IZNOS PREPLATE PODNOSI SE / NE PODNOSI SE (VIRMANSKI) NALOG ZA POVRAT (Nepotrebno precrtati)", 
//     "", "", "12980", "10740", "220", "", "", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", 
//     ""};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"NAZIV", "", "", "", "Yes", "", "", "", 
     "13520"};
  public raReportElement LabelPDVK1;
  private String[] LabelPDVK1Props = new String[] {"PDV-K", "", "9680", "", "1040", "280", "", "", 
     "", "", "", "", "Lucida Bright", "13", "", "", "", ""};
  public raReportElement LabelOBRAZAC1;
  private String[] LabelOBRAZAC1Props = new String[] {"OBRAZAC", "", "8560", "60", "1100", "220", 
     "", "", "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement Label59;
  private String[] Label59Props = new String[] {"", "", "", "300", "4980", "500", "Normal", 
     "-2302756", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label60;
  private String[] Label60Props = new String[] {"", "", "5100", "300", "5640", "500", "Normal", 
     "-2302756", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelVRIJEDNOST_ISPORUKE_IZNOS_U1;
  private String[] LabelVRIJEDNOST_ISPORUKE_IZNOS_U1Props = new String[] {
     "VRIJEDNOST ISPORUKE\nIZNOS U KUNAMA I LIPAMA", "", "5100", "360", "2980", "400", "", 
     "-2302756", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Center"};
  public raReportElement LabelOPIS1;
  private String[] LabelOPIS1Props = new String[] {"OPIS", "", "", "440", "4980", "200", "", 
     "-2302756", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Center"};
  public raReportElement Label611;
  private String[] Label611Props = new String[] {"", "", "", "860", "10740", "4460", "Normal", 
     "-1315861", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelVII;
  private String[] LabelVIIProps = new String[] {"VII.", "", "40", "920", "640", "300", "", "", "", 
     "", "", "", "Lucida Bright", "12", "Bold", "", "", "Right"};
  public raReportElement Label62;
  private String[] Label62Props = new String[] {"", "", "5280", "940", "2560", "340", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextVII;
  private String[] TextVIIProps = new String[] {"VII", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5280", "1000", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement LabelOSTALI_PODACI;
  private String[] LabelOSTALI_PODACIProps = new String[] {"OSTALI PODACI", "", "620", "1000", 
     "4320", "180", "", "-2302756", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", ""};
  public raReportElement Label63;
  private String[] Label63Props = new String[] {"", "", "600", "1300", "9960", "3000", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label64;
  private String[] Label64Props = new String[] {"", "", "5280", "1320", "2560", "340", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label113;
  private String[] Label113Props = new String[] {"1.", "", "680", "1380", "320", "300", "", "", "", 
     "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement TextVII1;
  private String[] TextVII1Props = new String[] {"VII1", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5280", "1380", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement LabelZA_ISPRAVAK_PRETPOREZA;
  private String[] LabelZA_ISPRAVAK_PRETPOREZAProps = new String[] {"ZA ISPRAVAK PRETPOREZA", "", 
     "980", "1420", "3200", "200", "", "", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", ""};
  public raReportElement Label65;
  private String[] Label65Props = new String[] {"", "", "1220", "1680", "9320", "2580", "Normal", 
     "-1973791", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label66;
  private String[] Label66Props = new String[] {"", "", "5280", "1720", "2560", "340", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelNABAVA_NEKRETNINA__ISPORUCITELJ;
  private String[] LabelNABAVA_NEKRETNINA__ISPORUCITELJProps = new String[] {
     "NABAVA NEKRETNINA - ISPORU\u010CITELJ (PRODAVATELJ) NEKRETNINA", "", "1680", "1740", "3520", 
     "320", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label114;
  private String[] Label114Props = new String[] {"1.1.", "", "1260", "1780", "440", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextVII12;
  private String[] TextVII12Props = new String[] {"VII12", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5280", "1780", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label67;
  private String[] Label67Props = new String[] {"", "", "5280", "2080", "2560", "340", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelPRODAJA_NEKRETNINA__PRIMATELJ;
  private String[] LabelPRODAJA_NEKRETNINA__PRIMATELJProps = new String[] {
     "PRODAJA NEKRETNINA - PRIMATELJ (KUPAC) NEKRETNINA", "", "1680", "2120", "3520", "340", "", "", 
     "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label121;
  private String[] Label121Props = new String[] {"1.2.", "", "1260", "2140", "440", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextVII13;
  private String[] TextVII13Props = new String[] {"VII13", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5280", "2140", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label68;
  private String[] Label68Props = new String[] {"", "", "5280", "2440", "2560", "340", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelNABAVA_OSOBNIH_VOZILA_;
  private String[] LabelNABAVA_OSOBNIH_VOZILA_Props = new String[] {
     "NABAVA OSOBNIH VOZILA  ( TRGOVINA AUTOMOBILIMA NIJE PRIMARNA DJELATNOST)", "", "1680", "2460", 
     "3520", "340", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement TextVII14;
  private String[] TextVII14Props = new String[] {"VII14", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5280", "2500", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label131;
  private String[] Label131Props = new String[] {"1.3.", "", "1260", "2500", "440", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement Label69;
  private String[] Label69Props = new String[] {"", "", "5280", "2800", "2560", "340", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelPRODAJA_OSOBNIH_VOZILA_;
  private String[] LabelPRODAJA_OSOBNIH_VOZILA_Props = new String[] {
     "PRODAJA OSOBNIH VOZILA  ( TRGOVINA AUTOMOBILIMA NIJE PRIMARNA DJELATNOST)", "", "1680", 
     "2840", "3520", "340", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement TextVII141;
  private String[] TextVII141Props = new String[] {"VII14", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5280", "2860", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label141;
  private String[] Label141Props = new String[] {"1.4.", "", "1260", "2860", "440", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement Label70;
  private String[] Label70Props = new String[] {"", "", "5280", "3160", "2560", "340", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelNABAVA_RABLJ_OSOB_VOZIL;
  private String[] LabelNABAVA_RABLJ_OSOB_VOZILProps = new String[] {
     "NABAVA RABLJ. OSOB. VOZIL. I PROD. ISTIH - RAZLIKA U CIJENI (ZA TRGOVCE TIM VOZIL.)", "", 
     "1680", "3180", "3520", "340", "", "", "", "", "", "", "Lucida Bright", "6", "Bold", "", "", 
     ""};
  public raReportElement Label151;
  private String[] Label151Props = new String[] {"1.5.", "", "1260", "3200", "440", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextVII15;
  private String[] TextVII15Props = new String[] {"VII15", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5280", "3220", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label71;
  private String[] Label71Props = new String[] {"", "", "5280", "3520", "2560", "340", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextVII16;
  private String[] TextVII16Props = new String[] {"VII16", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5280", "3580", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label161;
  private String[] Label161Props = new String[] {"1.6.", "", "1260", "3600", "440", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement LabelNABAVA_OSTALE_DUGOTRAJNE_MIROVINE;
  private String[] LabelNABAVA_OSTALE_DUGOTRAJNE_MIROVINEProps = new String[] {
     "NABAVA OSTALE DUGOTRAJNE MIROVINE", "", "1680", "3640", "3520", "160", "", "", "", "", "", "", 
     "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label72;
  private String[] Label72Props = new String[] {"", "", "5280", "3880", "2560", "340", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextVII17;
  private String[] TextVII17Props = new String[] {"VII17", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5280", "3940", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label171;
  private String[] Label171Props = new String[] {"1.7.", "", "1260", "3940", "440", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement LabelPRODAJA_OSTALE_DUGOTRAJNE_IMOVINE;
  private String[] LabelPRODAJA_OSTALE_DUGOTRAJNE_IMOVINEProps = new String[] {
     "PRODAJA OSTALE DUGOTRAJNE IMOVINE", "", "1680", "3980", "3520", "160", "", "", "", "", "", "", 
     "Lucida Bright", "6", "Bold", "", "", ""};
  public raReportElement Label73;
  private String[] Label73Props = new String[] {"", "", "600", "4320", "9960", "380", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label74;
  private String[] Label74Props = new String[] {"", "", "5280", "4340", "2560", "340", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelOTUDjENJESTJECANJE_GOSPODARSKE_CJELINE_ILI;
  private String[] LabelOTUDjENJESTJECANJE_GOSPODARSKE_CJELINE_ILIProps = new String[] {
     "OTU\u0110ENJE/STJECANJE GOSPODARSKE CJELINE ILI POGONA", "", "1000", "4340", "4200", "340", 
     "", "", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", ""};
  public raReportElement Label213;
  private String[] Label213Props = new String[] {"2.", "", "680", "4400", "320", "300", "", "", "", 
     "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement TextVII2;
  private String[] TextVII2Props = new String[] {"VII2", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5280", "4400", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement Label75;
  private String[] Label75Props = new String[] {"", "", "600", "4720", "9960", "380", "Normal", 
     "-2631721", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label76;
  private String[] Label76Props = new String[] {"", "", "5280", "4740", "2560", "340", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelPOCETAK_I_PRESTANAK_OBAVLJANJA;
  private String[] LabelPOCETAK_I_PRESTANAK_OBAVLJANJAProps = new String[] {
     "PO\u010CETAK I PRESTANAK OBAVLJANJA DJELATNOSTI", "", "1000", "4740", "4200", "340", "", "", 
     "", "", "", "", "Lucida Bright", "7", "Bold", "", "", ""};
  public raReportElement Label312;
  private String[] Label312Props = new String[] {"3.", "", "680", "4780", "320", "300", "", "", "", 
     "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement TextVII3;
  private String[] TextVII3Props = new String[] {"VII3", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5280", "4800", "2560", "220", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Right", ""};
  public raReportElement LabelZA_IZNOS_PREPLATE_PODNOSI1;
  private String[] LabelZA_IZNOS_PREPLATE_PODNOSI1Props = new String[] {
 //    "ZA IZNOS PREPLATE PODNOSI SE / NE PODNOSI SE (VIRMANSKI) NALOG ZA POVRAT (Nepotrebno precrtati)",
      "",
     "", "", "5400", "10740", "220", "", "", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", 
     ""};
  /*
   *   String[] LINE_PROPS = {LINE_SLANT, VISIBLE, LEFT, TOP, WIDTH, HEIGHT,
           BORDER_STYLE, BORDER_COLOR, BORDER_WIDTH};
   */
  public raReportElement Line16;
  private String[] Line16Props = new String[] {"", "", "5720", "5820", "4940", "0", "", "", ""};
  public raReportElement Line17;
  private String[] Line17Props = new String[] {"", "", "10680", "5820", "0", "1680", "", "", ""};
  public raReportElement Line18;
  private String[] Line18Props = new String[] {"", "", "5720", "5840", "0", "1660", "", "", ""};
  public raReportElement LabelOBRACUN_SASTAVIO_IME_PREZIME;
  private String[] LabelOBRACUN_SASTAVIO_IME_PREZIMEProps = new String[] {
     "OBRA\u010CUN SASTAVIO\n(IME, PREZIME I POTPIS)", "", "", "5900", "2160", "400", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement Line19;
  private String[] Line19Props = new String[] {"", "", "8940", "6180", "0", "200", "", "", ""};
  public raReportElement Line20;
  private String[] Line20Props = new String[] {"", "", "8660", "6180", "0", "200", "", "", ""};
  public raReportElement Line21;
  private String[] Line21Props = new String[] {"", "", "9220", "6180", "0", "200", "", "", ""};
  public raReportElement Line22;
  private String[] Line22Props = new String[] {"", "", "9660", "6180", "0", "200", "", "", ""};
  public raReportElement Line23;
  private String[] Line23Props = new String[] {"", "", "9400", "6180", "0", "200", "", "", ""};
  public raReportElement Line24;
  private String[] Line24Props = new String[] {"", "", "9940", "6180", "0", "200", "", "", ""};
  public raReportElement Line25;
  private String[] Line25Props = new String[] {"", "", "10220", "6180", "0", "200", "", "", ""};
  public raReportElement Line26;
  private String[] Line26Props = new String[] {"", "", "10520", "6180", "0", "200", "", "", ""};
  public raReportElement Line27;
  private String[] Line27Props = new String[] {"", "", "8160", "6180", "0", "200", "", "", ""};
  public raReportElement Line28;
  private String[] Line28Props = new String[] {"", "", "7880", "6180", "0", "200", "", "", ""};
  public raReportElement Line29;
  private String[] Line29Props = new String[] {"", "", "8460", "6180", "0", "200", "", "", ""};
  public raReportElement LabelNADNEVAK_PRIMITKA;
  private String[] LabelNADNEVAK_PRIMITKAProps = new String[] {"NADNEVAK PRIMITKA", "", "5780", 
     "6220", "2000", "260", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement Line30;
  private String[] Line30Props = new String[] {"", "", "2200", "6280", "3440", "0", "", "", ""};
  public raReportElement Line31;
  private String[] Line31Props = new String[] {"", "", "8660", "6400", "560", "0", "", "", ""};
  public raReportElement Line32;
  private String[] Line32Props = new String[] {"", "", "7880", "6400", "580", "0", "", "", ""};
  public raReportElement Line33;
  private String[] Line33Props = new String[] {"", "", "9400", "6400", "1120", "0", "", "", ""};
  public raReportElement LabelPOTPIS_I_PECAT_POREZNOG;
  private String[] LabelPOTPIS_I_PECAT_POREZNOGProps = new String[] {
     "POTPIS I PE\u010CAT\nPOREZNOG OBVEZNIKA", "", "", "6880", "2160", "400", "", "", "", "", "", 
     "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelPOTPIS_SLUZBENIKA_POREZNE_UPRAVE;
  private String[] LabelPOTPIS_SLUZBENIKA_POREZNE_UPRAVEProps = new String[] {
     "POTPIS SLUŽBENIKA\nPOREZNE UPRAVE", "", "5820", "6940", "1880", "400", "", "", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", ""};
  public raReportElement Line34;
  private String[] Line34Props = new String[] {"", "", "2160", "7260", "3440", "0", "", "", ""};
  public raReportElement Line35;
  private String[] Line35Props = new String[] {"", "", "7700", "7280", "", "0", "", "", ""};
  public raReportElement Label77;
  private String[] Label77Props = new String[] {"", "", "", "7480", "10740", "160", "Normal", "", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelNAPOMENA;
  private String[] LabelNAPOMENAProps = new String[] {"\nNAPOMENA:", "", "", "7500", "10740", 
     "5940", "Normal", "-1315861", "Solid", "-2631721", "", "", "Lucida Bright", "9", "Bold", "", 
     "", ""};
  public raReportElement Line36;
  private String[] Line36Props = new String[] {"", "", "5720", "7500", "4940", "0", "", "", ""};
  public raReportElement TextNAPOMENA;
  private String[] TextNAPOMENAProps = new String[] {"NAPOMENA", "", "", "", "", "", "", "", "1380", 
     "7720", "9260", "5640", "Normal", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportSection Detail;
  public raReportSection PageFooter;
  public raReportSection ReportFooter;

  public repPrijavaPDV_K06OrigTemplate() {
    createReportStructure();
    setReportProperties();
  }

  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
    return sect;
  }

  public abstract raReportSection createReportHeader();

  public abstract raReportSection createPageHeader();
  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    Rectangle1 = sect.addModel(ep.RECTANGLE, Rectangle1Props);
    Rectangle2 = sect.addModel(ep.RECTANGLE, Rectangle2Props);
    LabelPDVK = sect.addModel(ep.LABEL, LabelPDVKProps);
    LabelOBRAZAC = sect.addModel(ep.LABEL, LabelOBRAZACProps);
    LabelPOREZNI_OBVEZNIK_NAZIVIME_I = sect.addModel(ep.LABEL, LabelPOREZNI_OBVEZNIK_NAZIVIME_IProps);
    LabelNADLEZNA_ISPOSTAVA_POREZNE_UPRAVE = sect.addModel(ep.LABEL, LabelNADLEZNA_ISPOSTAVA_POREZNE_UPRAVEProps);
    TextPORISP = sect.addModel(ep.TEXT, TextPORISPProps);
    TextNAZIV = sect.addModel(ep.TEXT, TextNAZIVProps);
    TextADRESA = sect.addModel(ep.TEXT, TextADRESAProps);
    LabelPRIJAVA_POREZA_NA_DODANU = sect.addModel(ep.LABEL, LabelPRIJAVA_POREZA_NA_DODANUProps);
    LabelBROJCANA_OZNAKA_SIFRA_DJELATNOSTI = sect.addModel(ep.LABEL, LabelBROJCANA_OZNAKA_SIFRA_DJELATNOSTIProps);
    LabelMB_ili_JMBG__POREZNI = sect.addModel(ep.LABEL, LabelMB_ili_JMBG__POREZNIProps);
    //TextRAZDOBLJE = sect.addModel(ep.TEXT, TextRAZDOBLJEProps);
    TextODLABEL = sect.addModel(ep.TEXT, TextODLABELProps);
    TextODDAN = sect.addModel(ep.TEXT, TextODDANProps);
    TextODMJ = sect.addModel(ep.TEXT, TextODMJProps);
    TextDOLABEL = sect.addModel(ep.TEXT, TextDOLABELProps);
    TextDODAN = sect.addModel(ep.TEXT, TextDODANProps);
    TextDOMJ = sect.addModel(ep.TEXT, TextDOMJProps);
    TextGODLABEL = sect.addModel(ep.TEXT, TextGODLABELProps);
    TextGOD = sect.addModel(ep.TEXT, TextGODProps);
    Line1 = sect.addModel(ep.LINE, Line1Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    Line3 = sect.addModel(ep.LINE, Line3Props);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    Line5 = sect.addModel(ep.LINE, Line5Props);
    Line6 = sect.addModel(ep.LINE, Line6Props);
    Line7 = sect.addModel(ep.LINE, Line7Props);
    Line8 = sect.addModel(ep.LINE, Line8Props);
    Line9 = sect.addModel(ep.LINE, Line9Props);
    Line10 = sect.addModel(ep.LINE, Line10Props);
    TextMB = sect.addModel(ep.TEXT, TextMBProps);
    TextSIFDJEL = sect.addModel(ep.TEXT, TextSIFDJELProps);
    Line11 = sect.addModel(ep.LINE, Line11Props);
    Line12 = sect.addModel(ep.LINE, Line12Props);
    Line13 = sect.addModel(ep.LINE, Line13Props);
    Line14 = sect.addModel(ep.LINE, Line14Props);
    Line15 = sect.addModel(ep.LINE, Line15Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    Label3 = sect.addModel(ep.LABEL, Label3Props);
    LabelVRIJEDNOST_ISPORUKE_IZNOS_U = sect.addModel(ep.LABEL, LabelVRIJEDNOST_ISPORUKE_IZNOS_UProps);
    LabelPOREZ_PO_STOPI_OD = sect.addModel(ep.LABEL, LabelPOREZ_PO_STOPI_ODProps);
    LabelOPIS = sect.addModel(ep.LABEL, LabelOPISProps);
    Label4 = sect.addModel(ep.LABEL, Label4Props);
    LabelOBRACUN_POREZA_U_OBAVLJENIM = sect.addModel(ep.LABEL, LabelOBRACUN_POREZA_U_OBAVLJENIMProps);
    Label5 = sect.addModel(ep.LABEL, Label5Props);
    LabelX_X_X_X_X = sect.addModel(ep.LABEL, LabelX_X_X_X_XProps);
    TextUKUPNO = sect.addModel(ep.TEXT, TextUKUPNOProps);
    Label6 = sect.addModel(ep.LABEL, Label6Props);
    LabelI = sect.addModel(ep.LABEL, LabelIProps);
    LabelISPORUKE_KOJE_NE_PODLJEZU = sect.addModel(ep.LABEL, LabelISPORUKE_KOJE_NE_PODLJEZUProps);
    Label7 = sect.addModel(ep.LABEL, Label7Props);
    LabelX_X_X_X_X1 = sect.addModel(ep.LABEL, LabelX_X_X_X_X1Props);
    TextI = sect.addModel(ep.TEXT, TextIProps);
    Label8 = sect.addModel(ep.LABEL, Label8Props);
    LabelKOJE_NE_PODLJEZU_OPOREZIVANJU = sect.addModel(ep.LABEL, LabelKOJE_NE_PODLJEZU_OPOREZIVANJUProps);
    Label9 = sect.addModel(ep.LABEL, Label9Props);
    LabelX_X_X_X_X2 = sect.addModel(ep.LABEL, LabelX_X_X_X_X2Props);
    TextI1 = sect.addModel(ep.TEXT, TextI1Props);
    Label11 = sect.addModel(ep.LABEL, Label11Props);
    Labelcl_2_u_svezi_s_cl = sect.addModel(ep.LABEL, Labelcl_2_u_svezi_s_clProps);
    Label10 = sect.addModel(ep.LABEL, Label10Props);
    Label111 = sect.addModel(ep.LABEL, Label111Props);
    LabelX_X_X_X_X3 = sect.addModel(ep.LABEL, LabelX_X_X_X_X3Props);
    Label21 = sect.addModel(ep.LABEL, Label21Props);
    LabelOSLOBODjENE_POREZA__UKUPNO = sect.addModel(ep.LABEL, LabelOSLOBODjENE_POREZA__UKUPNOProps);
    TextI2 = sect.addModel(ep.TEXT, TextI2Props);
    Label12 = sect.addModel(ep.LABEL, Label12Props);
    Label13 = sect.addModel(ep.LABEL, Label13Props);
    LabelX_X_X_X_X4 = sect.addModel(ep.LABEL, LabelX_X_X_X_X4Props);
    Label211 = sect.addModel(ep.LABEL, Label211Props);
    TextI21 = sect.addModel(ep.TEXT, TextI21Props);
    LabelIZVOZNE__s_pravom = sect.addModel(ep.LABEL, LabelIZVOZNE__s_pravomProps);
    Label14 = sect.addModel(ep.LABEL, Label14Props);
    LabelX_X_X_X_X5 = sect.addModel(ep.LABEL, LabelX_X_X_X_X5Props);
    Label22 = sect.addModel(ep.LABEL, Label22Props);
    TextI22 = sect.addModel(ep.TEXT, TextI22Props);
    LabelTUZEMNE__bez_prava = sect.addModel(ep.LABEL, LabelTUZEMNE__bez_pravaProps);
    Label15 = sect.addModel(ep.LABEL, Label15Props);
    LabelX_X_X_X_X6 = sect.addModel(ep.LABEL, LabelX_X_X_X_X6Props);
    TextI23 = sect.addModel(ep.TEXT, TextI23Props);
    Label23 = sect.addModel(ep.LABEL, Label23Props);
    LabelOSTALO = sect.addModel(ep.LABEL, LabelOSTALOProps);
    Label16 = sect.addModel(ep.LABEL, Label16Props);
    Label17 = sect.addModel(ep.LABEL, Label17Props);
    LabelX_X_X_X_X7 = sect.addModel(ep.LABEL, LabelX_X_X_X_X7Props);
    Label31 = sect.addModel(ep.LABEL, Label31Props);
    LabelISPORUKE_PO_STOPI_0 = sect.addModel(ep.LABEL, LabelISPORUKE_PO_STOPI_0Props);
    TextI3 = sect.addModel(ep.TEXT, TextI3Props);
    Label18 = sect.addModel(ep.LABEL, Label18Props);
    LabelII = sect.addModel(ep.LABEL, LabelIIProps);
    Label19 = sect.addModel(ep.LABEL, Label19Props);
    Label20 = sect.addModel(ep.LABEL, Label20Props);
    LabelOPOREZIVE_ISPORUKE__UKUPNO = sect.addModel(ep.LABEL, LabelOPOREZIVE_ISPORUKE__UKUPNOProps);
    TextIIv = sect.addModel(ep.TEXT, TextIIvProps);
    TextIIp = sect.addModel(ep.TEXT, TextIIpProps);
    Label212 = sect.addModel(ep.LABEL, Label212Props);
    Label221 = sect.addModel(ep.LABEL, Label221Props);
    Label231 = sect.addModel(ep.LABEL, Label231Props);
    LabelZA_KOJE_SU_IZDANI = sect.addModel(ep.LABEL, LabelZA_KOJE_SU_IZDANIProps);
    TextII1v = sect.addModel(ep.TEXT, TextII1vProps);
    TextII1p = sect.addModel(ep.TEXT, TextII1pProps);
    Label110 = sect.addModel(ep.LABEL, Label110Props);
    Label24 = sect.addModel(ep.LABEL, Label24Props);
    Label25 = sect.addModel(ep.LABEL, Label25Props);
    Label26 = sect.addModel(ep.LABEL, Label26Props);
    LabelZA_KOJE_SU_IZDANI1 = sect.addModel(ep.LABEL, LabelZA_KOJE_SU_IZDANI1Props);
    TextII2v = sect.addModel(ep.TEXT, TextII2vProps);
    TextII2p = sect.addModel(ep.TEXT, TextII2pProps);
    Label27 = sect.addModel(ep.LABEL, Label27Props);
    Label271 = sect.addModel(ep.LABEL, Label271Props);
    Label28 = sect.addModel(ep.LABEL, Label28Props);
    Label29 = sect.addModel(ep.LABEL, Label29Props);
    TextII3v = sect.addModel(ep.TEXT, TextII3vProps);
    TextII3p = sect.addModel(ep.TEXT, TextII3pProps);
    Label32 = sect.addModel(ep.LABEL, Label32Props);
    LabelNENAPLACENI_IZVOZ = sect.addModel(ep.LABEL, LabelNENAPLACENI_IZVOZProps);
    Label30 = sect.addModel(ep.LABEL, Label30Props);
    Label311 = sect.addModel(ep.LABEL, Label311Props);
    Label321 = sect.addModel(ep.LABEL, Label321Props);
    TextII4v = sect.addModel(ep.TEXT, TextII4vProps);
    TextII4p = sect.addModel(ep.TEXT, TextII4pProps);
    Label41 = sect.addModel(ep.LABEL, Label41Props);
    LabelNAKNADNO_OSLOB_IZVOZA_U = sect.addModel(ep.LABEL, LabelNAKNADNO_OSLOB_IZVOZA_UProps);
    Label33 = sect.addModel(ep.LABEL, Label33Props);
    LabelOBRACUNANI_PRETPOREZ_U_PRIMLJENIM = sect.addModel(ep.LABEL, LabelOBRACUNANI_PRETPOREZ_U_PRIMLJENIMProps);
    Label34 = sect.addModel(ep.LABEL, Label34Props);
    Label35 = sect.addModel(ep.LABEL, Label35Props);
    LabelIII = sect.addModel(ep.LABEL, LabelIIIProps);
    TextIIIv = sect.addModel(ep.TEXT, TextIIIvProps);
    TextIIIp = sect.addModel(ep.TEXT, TextIIIpProps);
    Label36 = sect.addModel(ep.LABEL, Label36Props);
    Label37 = sect.addModel(ep.LABEL, Label37Props);
    Label38 = sect.addModel(ep.LABEL, Label38Props);
    TextIII1v = sect.addModel(ep.TEXT, TextIII1vProps);
    TextIII1p = sect.addModel(ep.TEXT, TextIII1pProps);
    Label112 = sect.addModel(ep.LABEL, Label112Props);
    LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMA = sect.addModel(ep.LABEL, LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMAProps);
    Label39 = sect.addModel(ep.LABEL, Label39Props);
    Label40 = sect.addModel(ep.LABEL, Label40Props);
    Label411 = sect.addModel(ep.LABEL, Label411Props);
    TextIII2v = sect.addModel(ep.TEXT, TextIII2vProps);
    TextIII2p = sect.addModel(ep.TEXT, TextIII2pProps);
    Label210 = sect.addModel(ep.LABEL, Label210Props);
    LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMA1 = sect.addModel(ep.LABEL, LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMA1Props);
    Label42 = sect.addModel(ep.LABEL, Label42Props);
    Label43 = sect.addModel(ep.LABEL, Label43Props);
    Label44 = sect.addModel(ep.LABEL, Label44Props);
    TextIII3v = sect.addModel(ep.TEXT, TextIII3vProps);
    TextIII3p = sect.addModel(ep.TEXT, TextIII3pProps);
    Label310 = sect.addModel(ep.LABEL, Label310Props);
    LabelPLACENI_PRETPOREZ_PRI_UVOZU = sect.addModel(ep.LABEL, LabelPLACENI_PRETPOREZ_PRI_UVOZUProps);
    Label45 = sect.addModel(ep.LABEL, Label45Props);
    Label46 = sect.addModel(ep.LABEL, Label46Props);
    Label47 = sect.addModel(ep.LABEL, Label47Props);
    LabelPLACENI_PRETPOREZ_NA_USLUGE = sect.addModel(ep.LABEL, LabelPLACENI_PRETPOREZ_NA_USLUGEProps);
    TextIII4v = sect.addModel(ep.TEXT, TextIII4vProps);
    TextIII4p = sect.addModel(ep.TEXT, TextIII4pProps);
    Label48 = sect.addModel(ep.LABEL, Label48Props);
    Label481 = sect.addModel(ep.LABEL, Label481Props);
    Label49 = sect.addModel(ep.LABEL, Label49Props);
    Label50 = sect.addModel(ep.LABEL, Label50Props);
    TextIII5p = sect.addModel(ep.TEXT, TextIII5pProps);
    TextIII5v = sect.addModel(ep.TEXT, TextIII5vProps);
    LabelPLACENI_PRETPOREZ_NA_USLUGE1 = sect.addModel(ep.LABEL, LabelPLACENI_PRETPOREZ_NA_USLUGE1Props);
    Label51 = sect.addModel(ep.LABEL, Label51Props);
    Label511 = sect.addModel(ep.LABEL, Label511Props);
    LabelX_X_X_X_X8 = sect.addModel(ep.LABEL, LabelX_X_X_X_X8Props);
    Label52 = sect.addModel(ep.LABEL, Label52Props);
    TextIII6 = sect.addModel(ep.TEXT, TextIII6Props);
    Label61 = sect.addModel(ep.LABEL, Label61Props);
    LabelISPRAVCI_PRETPOREZA_PREMA_CL = sect.addModel(ep.LABEL, LabelISPRAVCI_PRETPOREZA_PREMA_CLProps);
    Label53 = sect.addModel(ep.LABEL, Label53Props);
    LabelPOREZNA_OBVEZA_U_OBRACUNSKOM = sect.addModel(ep.LABEL, LabelPOREZNA_OBVEZA_U_OBRACUNSKOMProps);
    Label54 = sect.addModel(ep.LABEL, Label54Props);
    LabelX_X_X_X_X9 = sect.addModel(ep.LABEL, LabelX_X_X_X_X9Props);
    LabelIV = sect.addModel(ep.LABEL, LabelIVProps);
    TextIV = sect.addModel(ep.TEXT, TextIVProps);
    Label55 = sect.addModel(ep.LABEL, Label55Props);
    LabelPO_PRETHODNOM_OBRACUNU_NEUPLACENI = sect.addModel(ep.LABEL, LabelPO_PRETHODNOM_OBRACUNU_NEUPLACENIProps);
    Label56 = sect.addModel(ep.LABEL, Label56Props);
    LabelX_X_X_X_X10 = sect.addModel(ep.LABEL, LabelX_X_X_X_X10Props);
    LabelV = sect.addModel(ep.LABEL, LabelVProps);
    TextV = sect.addModel(ep.TEXT, TextVProps);
    Label57 = sect.addModel(ep.LABEL, Label57Props);
    Label58 = sect.addModel(ep.LABEL, Label58Props);
    LabelX_X_X_X_X11 = sect.addModel(ep.LABEL, LabelX_X_X_X_X11Props);
    LabelVI = sect.addModel(ep.LABEL, LabelVIProps);
    TextVI = sect.addModel(ep.TEXT, TextVIProps);
    LabelUKUPNO_RAZLIKA_ZA_UPLATU = sect.addModel(ep.LABEL, LabelUKUPNO_RAZLIKA_ZA_UPLATUProps);
    LabelKn = sect.addModel(ep.LABEL, LabelKnProps);
    LabelKn1 = sect.addModel(ep.LABEL, LabelKn1Props);
    LabelKn2 = sect.addModel(ep.LABEL, LabelKn2Props);
    Rectangle3 = sect.addModel(ep.RECTANGLE, Rectangle3Props);
    Rectangle4 = sect.addModel(ep.RECTANGLE, Rectangle4Props);
    Rectangle5 = sect.addModel(ep.RECTANGLE, Rectangle5Props);
    LabelPREDUJAM = sect.addModel(ep.LABEL, LabelPREDUJAMProps);
    LabelUSTUP_POVRATA = sect.addModel(ep.LABEL, LabelUSTUP_POVRATAProps);
    LabelZA_POVRAT = sect.addModel(ep.LABEL, LabelZA_POVRATProps);
    Line116 = sect.addModel(ep.LINE, Line116Props);
    Line117 = sect.addModel(ep.LINE, Line117Props);
    Line118 = sect.addModel(ep.LINE, Line118Props);
    return sect;
  }
  public raReportSection old_createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    LabelPDVK = sect.addModel(ep.LABEL, LabelPDVKProps);
    LabelOBRAZAC = sect.addModel(ep.LABEL, LabelOBRAZACProps);
    LabelNADLEZNA_ISPOSTAVA_POREZNE_UPRAVE = sect.addModel(ep.LABEL, LabelNADLEZNA_ISPOSTAVA_POREZNE_UPRAVEProps);
    LabelPOREZNI_OBVEZNIK_NAZIVIME_I = sect.addModel(ep.LABEL, LabelPOREZNI_OBVEZNIK_NAZIVIME_IProps);
    TextNAZIV = sect.addModel(ep.TEXT, TextNAZIVProps);
    TextPORISP = sect.addModel(ep.TEXT, TextPORISPProps);
    Rectangle1 = sect.addModel(ep.RECTANGLE, Rectangle1Props);
    Rectangle2 = sect.addModel(ep.RECTANGLE, Rectangle2Props);
    TextADRESA = sect.addModel(ep.TEXT, TextADRESAProps);
    LabelPRIJAVA_POREZA_NA_DODANU = sect.addModel(ep.LABEL, LabelPRIJAVA_POREZA_NA_DODANUProps);
    LabelMB_ili_JMBG__POREZNI = sect.addModel(ep.LABEL, LabelMB_ili_JMBG__POREZNIProps);
    LabelBROJCANA_OZNAKA_SIFRA_DJELATNOSTI = sect.addModel(ep.LABEL, LabelBROJCANA_OZNAKA_SIFRA_DJELATNOSTIProps);
    TextRAZDOBLJE = sect.addModel(ep.TEXT, TextRAZDOBLJEProps);
    Line1 = sect.addModel(ep.LINE, Line1Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    Line3 = sect.addModel(ep.LINE, Line3Props);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    Line5 = sect.addModel(ep.LINE, Line5Props);
    Line6 = sect.addModel(ep.LINE, Line6Props);
    Line7 = sect.addModel(ep.LINE, Line7Props);
    Line8 = sect.addModel(ep.LINE, Line8Props);
    Line9 = sect.addModel(ep.LINE, Line9Props);
    Line10 = sect.addModel(ep.LINE, Line10Props);
    TextSIFDJEL = sect.addModel(ep.TEXT, TextSIFDJELProps);
    TextMB = sect.addModel(ep.TEXT, TextMBProps);
    Line11 = sect.addModel(ep.LINE, Line11Props);
    Line12 = sect.addModel(ep.LINE, Line12Props);
    Line13 = sect.addModel(ep.LINE, Line13Props);
    Line14 = sect.addModel(ep.LINE, Line14Props);
    Line15 = sect.addModel(ep.LINE, Line15Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    Label3 = sect.addModel(ep.LABEL, Label3Props);
    LabelVRIJEDNOST_ISPORUKE_IZNOS_U = sect.addModel(ep.LABEL, LabelVRIJEDNOST_ISPORUKE_IZNOS_UProps);
    LabelPOREZ_PO_STOPI_OD = sect.addModel(ep.LABEL, LabelPOREZ_PO_STOPI_ODProps);
    LabelOPIS = sect.addModel(ep.LABEL, LabelOPISProps);
    Label4 = sect.addModel(ep.LABEL, Label4Props);
    LabelOBRACUN_POREZA_U_OBAVLJENIM = sect.addModel(ep.LABEL, LabelOBRACUN_POREZA_U_OBAVLJENIMProps);
    Label5 = sect.addModel(ep.LABEL, Label5Props);
    LabelX_X_X_X_X = sect.addModel(ep.LABEL, LabelX_X_X_X_XProps);
    TextUKUPNO = sect.addModel(ep.TEXT, TextUKUPNOProps);
    Label6 = sect.addModel(ep.LABEL, Label6Props);
    LabelI = sect.addModel(ep.LABEL, LabelIProps);
    LabelISPORUKE_KOJE_NE_PODLJEZU = sect.addModel(ep.LABEL, LabelISPORUKE_KOJE_NE_PODLJEZUProps);
    Label7 = sect.addModel(ep.LABEL, Label7Props);
    LabelX_X_X_X_X1 = sect.addModel(ep.LABEL, LabelX_X_X_X_X1Props);
    TextI = sect.addModel(ep.TEXT, TextIProps);
    Label8 = sect.addModel(ep.LABEL, Label8Props);
    LabelKOJE_NE_PODLJEZU_OPOREZIVANJU = sect.addModel(ep.LABEL, LabelKOJE_NE_PODLJEZU_OPOREZIVANJUProps);
    Label9 = sect.addModel(ep.LABEL, Label9Props);
    LabelX_X_X_X_X2 = sect.addModel(ep.LABEL, LabelX_X_X_X_X2Props);
    TextI1 = sect.addModel(ep.TEXT, TextI1Props);
    Label11 = sect.addModel(ep.LABEL, Label11Props);
    Labelcl_2_u_svezi_s_cl = sect.addModel(ep.LABEL, Labelcl_2_u_svezi_s_clProps);
    Label10 = sect.addModel(ep.LABEL, Label10Props);
    Label111 = sect.addModel(ep.LABEL, Label111Props);
    LabelX_X_X_X_X3 = sect.addModel(ep.LABEL, LabelX_X_X_X_X3Props);
    LabelOSLOBODjENE_POREZA__UKUPNO = sect.addModel(ep.LABEL, LabelOSLOBODjENE_POREZA__UKUPNOProps);
    Label21 = sect.addModel(ep.LABEL, Label21Props);
    TextI2 = sect.addModel(ep.TEXT, TextI2Props);
    Label12 = sect.addModel(ep.LABEL, Label12Props);
    Label13 = sect.addModel(ep.LABEL, Label13Props);
    LabelX_X_X_X_X4 = sect.addModel(ep.LABEL, LabelX_X_X_X_X4Props);
    TextI21 = sect.addModel(ep.TEXT, TextI21Props);
    Label211 = sect.addModel(ep.LABEL, Label211Props);
    LabelIZVOZNE__s_pravom = sect.addModel(ep.LABEL, LabelIZVOZNE__s_pravomProps);
    Label14 = sect.addModel(ep.LABEL, Label14Props);
    LabelX_X_X_X_X5 = sect.addModel(ep.LABEL, LabelX_X_X_X_X5Props);
    TextI22 = sect.addModel(ep.TEXT, TextI22Props);
    Label22 = sect.addModel(ep.LABEL, Label22Props);
    LabelTUZEMNE__bez_prava = sect.addModel(ep.LABEL, LabelTUZEMNE__bez_pravaProps);
    Label15 = sect.addModel(ep.LABEL, Label15Props);
    LabelX_X_X_X_X6 = sect.addModel(ep.LABEL, LabelX_X_X_X_X6Props);
    TextI23 = sect.addModel(ep.TEXT, TextI23Props);
    Label23 = sect.addModel(ep.LABEL, Label23Props);
    LabelOSTALO = sect.addModel(ep.LABEL, LabelOSTALOProps);
    Label16 = sect.addModel(ep.LABEL, Label16Props);
    Label17 = sect.addModel(ep.LABEL, Label17Props);
    LabelX_X_X_X_X7 = sect.addModel(ep.LABEL, LabelX_X_X_X_X7Props);
    Label31 = sect.addModel(ep.LABEL, Label31Props);
    TextI3 = sect.addModel(ep.TEXT, TextI3Props);
    LabelISPORUKE_PO_STOPI_0 = sect.addModel(ep.LABEL, LabelISPORUKE_PO_STOPI_0Props);
    Label18 = sect.addModel(ep.LABEL, Label18Props);
    LabelII = sect.addModel(ep.LABEL, LabelIIProps);
    Label19 = sect.addModel(ep.LABEL, Label19Props);
    Label20 = sect.addModel(ep.LABEL, Label20Props);
    TextIIv = sect.addModel(ep.TEXT, TextIIvProps);
    TextIIp = sect.addModel(ep.TEXT, TextIIpProps);
    LabelOPOREZIVE_ISPORUKE__UKUPNO = sect.addModel(ep.LABEL, LabelOPOREZIVE_ISPORUKE__UKUPNOProps);
    Label212 = sect.addModel(ep.LABEL, Label212Props);
    Label221 = sect.addModel(ep.LABEL, Label221Props);
    Label231 = sect.addModel(ep.LABEL, Label231Props);
    LabelZA_KOJE_SU_IZDANI = sect.addModel(ep.LABEL, LabelZA_KOJE_SU_IZDANIProps);
    TextII1v = sect.addModel(ep.TEXT, TextII1vProps);
    TextII1p = sect.addModel(ep.TEXT, TextII1pProps);
    Label110 = sect.addModel(ep.LABEL, Label110Props);
    Label24 = sect.addModel(ep.LABEL, Label24Props);
    Label25 = sect.addModel(ep.LABEL, Label25Props);
    Label26 = sect.addModel(ep.LABEL, Label26Props);
    TextII2v = sect.addModel(ep.TEXT, TextII2vProps);
    TextII2p = sect.addModel(ep.TEXT, TextII2pProps);
    LabelZA_KOJE_SU_IZDANI1 = sect.addModel(ep.LABEL, LabelZA_KOJE_SU_IZDANI1Props);
    Label27 = sect.addModel(ep.LABEL, Label27Props);
    Label271 = sect.addModel(ep.LABEL, Label271Props);
    Label28 = sect.addModel(ep.LABEL, Label28Props);
    Label29 = sect.addModel(ep.LABEL, Label29Props);
    TextII3v = sect.addModel(ep.TEXT, TextII3vProps);
    TextII3p = sect.addModel(ep.TEXT, TextII3pProps);
    Label32 = sect.addModel(ep.LABEL, Label32Props);
    LabelNENAPLACENI_IZVOZ = sect.addModel(ep.LABEL, LabelNENAPLACENI_IZVOZProps);
    Label30 = sect.addModel(ep.LABEL, Label30Props);
    Label311 = sect.addModel(ep.LABEL, Label311Props);
    Label321 = sect.addModel(ep.LABEL, Label321Props);
    TextII4v = sect.addModel(ep.TEXT, TextII4vProps);
    TextII4p = sect.addModel(ep.TEXT, TextII4pProps);
    Label41 = sect.addModel(ep.LABEL, Label41Props);
    LabelOSTALO1 = sect.addModel(ep.LABEL, LabelOSTALO1Props);
    Label33 = sect.addModel(ep.LABEL, Label33Props);
    LabelOBRACUNANI_PRETPOREZ_U_PRIMLJENIM = sect.addModel(ep.LABEL, LabelOBRACUNANI_PRETPOREZ_U_PRIMLJENIMProps);
    Label34 = sect.addModel(ep.LABEL, Label34Props);
    Label35 = sect.addModel(ep.LABEL, Label35Props);
    LabelIII = sect.addModel(ep.LABEL, LabelIIIProps);
    TextIIIv = sect.addModel(ep.TEXT, TextIIIvProps);
    TextIIIp = sect.addModel(ep.TEXT, TextIIIpProps);
    Label36 = sect.addModel(ep.LABEL, Label36Props);
    Label37 = sect.addModel(ep.LABEL, Label37Props);
    Label38 = sect.addModel(ep.LABEL, Label38Props);
    TextIII1v = sect.addModel(ep.TEXT, TextIII1vProps);
    TextIII1p = sect.addModel(ep.TEXT, TextIII1pProps);
    Label112 = sect.addModel(ep.LABEL, Label112Props);
    LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMA = sect.addModel(ep.LABEL, LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMAProps);
    Label39 = sect.addModel(ep.LABEL, Label39Props);
    Label40 = sect.addModel(ep.LABEL, Label40Props);
    Label411 = sect.addModel(ep.LABEL, Label411Props);
    TextIII2v = sect.addModel(ep.TEXT, TextIII2vProps);
    TextIII2p = sect.addModel(ep.TEXT, TextIII2pProps);
    Label210 = sect.addModel(ep.LABEL, Label210Props);
    LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMA1 = sect.addModel(ep.LABEL, LabelPRETPOREZ_U_PRIMLJENIM_RACUNIMA1Props);
    Label42 = sect.addModel(ep.LABEL, Label42Props);
    Label43 = sect.addModel(ep.LABEL, Label43Props);
    Label44 = sect.addModel(ep.LABEL, Label44Props);
    TextIII3v = sect.addModel(ep.TEXT, TextIII3vProps);
    TextIII3p = sect.addModel(ep.TEXT, TextIII3pProps);
    Label310 = sect.addModel(ep.LABEL, Label310Props);
    LabelPLACENI_PRETPOREZ_PRI_UVOZU = sect.addModel(ep.LABEL, LabelPLACENI_PRETPOREZ_PRI_UVOZUProps);
    Label45 = sect.addModel(ep.LABEL, Label45Props);
    Label46 = sect.addModel(ep.LABEL, Label46Props);
    Label47 = sect.addModel(ep.LABEL, Label47Props);
    TextIII4v = sect.addModel(ep.TEXT, TextIII4vProps);
    TextIII4p = sect.addModel(ep.TEXT, TextIII4pProps);
    LabelPLACENI_PRETPOREZ_NA_USLUGE = sect.addModel(ep.LABEL, LabelPLACENI_PRETPOREZ_NA_USLUGEProps);
    Label48 = sect.addModel(ep.LABEL, Label48Props);
    Label481 = sect.addModel(ep.LABEL, Label481Props);
    Label49 = sect.addModel(ep.LABEL, Label49Props);
    Label50 = sect.addModel(ep.LABEL, Label50Props);
    TextIII5p = sect.addModel(ep.TEXT, TextIII5pProps);
    TextIII5v = sect.addModel(ep.TEXT, TextIII5vProps);
    LabelPLACENI_PRETPOREZ_NA_USLUGE1 = sect.addModel(ep.LABEL, LabelPLACENI_PRETPOREZ_NA_USLUGE1Props);
    Label51 = sect.addModel(ep.LABEL, Label51Props);
    Label511 = sect.addModel(ep.LABEL, Label511Props);
    LabelX_X_X_X_X8 = sect.addModel(ep.LABEL, LabelX_X_X_X_X8Props);
    Label52 = sect.addModel(ep.LABEL, Label52Props);
    TextIII6 = sect.addModel(ep.TEXT, TextIII6Props);
    Label61 = sect.addModel(ep.LABEL, Label61Props);
    LabelISPRAVCI_PRETPOREZA_PREMA_CL = sect.addModel(ep.LABEL, LabelISPRAVCI_PRETPOREZA_PREMA_CLProps);
    Label53 = sect.addModel(ep.LABEL, Label53Props);
    LabelPOREZNA_OBVEZA_U_OBRACUNSKOM = sect.addModel(ep.LABEL, LabelPOREZNA_OBVEZA_U_OBRACUNSKOMProps);
    Label54 = sect.addModel(ep.LABEL, Label54Props);
    LabelX_X_X_X_X9 = sect.addModel(ep.LABEL, LabelX_X_X_X_X9Props);
    LabelIV = sect.addModel(ep.LABEL, LabelIVProps);
    TextIV = sect.addModel(ep.TEXT, TextIVProps);
    Label55 = sect.addModel(ep.LABEL, Label55Props);
    LabelPO_PRETHODNOM_OBRACUNU_NEUPLACENI = sect.addModel(ep.LABEL, LabelPO_PRETHODNOM_OBRACUNU_NEUPLACENIProps);
    Label56 = sect.addModel(ep.LABEL, Label56Props);
    LabelX_X_X_X_X10 = sect.addModel(ep.LABEL, LabelX_X_X_X_X10Props);
    LabelV = sect.addModel(ep.LABEL, LabelVProps);
    TextV = sect.addModel(ep.TEXT, TextVProps);
    Label57 = sect.addModel(ep.LABEL, Label57Props);
    Label58 = sect.addModel(ep.LABEL, Label58Props);
    LabelX_X_X_X_X11 = sect.addModel(ep.LABEL, LabelX_X_X_X_X11Props);
    LabelVI = sect.addModel(ep.LABEL, LabelVIProps);
    TextVI = sect.addModel(ep.TEXT, TextVIProps);
    LabelUKUPNO_RAZLIKA_ZA_UPLATU = sect.addModel(ep.LABEL, LabelUKUPNO_RAZLIKA_ZA_UPLATUProps);
//    LabelZA_IZNOS_PREPLATE_PODNOSI = sect.addModel(ep.LABEL, LabelZA_IZNOS_PREPLATE_PODNOSIProps);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    LabelPDVK1 = sect.addModel(ep.LABEL, LabelPDVK1Props);
    LabelOBRAZAC1 = sect.addModel(ep.LABEL, LabelOBRAZAC1Props);
    Label59 = sect.addModel(ep.LABEL, Label59Props);
    Label60 = sect.addModel(ep.LABEL, Label60Props);
    LabelVRIJEDNOST_ISPORUKE_IZNOS_U1 = sect.addModel(ep.LABEL, LabelVRIJEDNOST_ISPORUKE_IZNOS_U1Props);
    LabelOPIS1 = sect.addModel(ep.LABEL, LabelOPIS1Props);
    Label611 = sect.addModel(ep.LABEL, Label611Props);
    LabelVII = sect.addModel(ep.LABEL, LabelVIIProps);
    Label62 = sect.addModel(ep.LABEL, Label62Props);
    TextVII = sect.addModel(ep.TEXT, TextVIIProps);
    LabelOSTALI_PODACI = sect.addModel(ep.LABEL, LabelOSTALI_PODACIProps);
    Label63 = sect.addModel(ep.LABEL, Label63Props);
    Label64 = sect.addModel(ep.LABEL, Label64Props);
    Label113 = sect.addModel(ep.LABEL, Label113Props);
    TextVII1 = sect.addModel(ep.TEXT, TextVII1Props);
    LabelZA_ISPRAVAK_PRETPOREZA = sect.addModel(ep.LABEL, LabelZA_ISPRAVAK_PRETPOREZAProps);
    Label65 = sect.addModel(ep.LABEL, Label65Props);
    Label66 = sect.addModel(ep.LABEL, Label66Props);
    LabelNABAVA_NEKRETNINA__ISPORUCITELJ = sect.addModel(ep.LABEL, LabelNABAVA_NEKRETNINA__ISPORUCITELJProps);
    Label114 = sect.addModel(ep.LABEL, Label114Props);
    TextVII12 = sect.addModel(ep.TEXT, TextVII12Props);
    Label67 = sect.addModel(ep.LABEL, Label67Props);
    LabelPRODAJA_NEKRETNINA__PRIMATELJ = sect.addModel(ep.LABEL, LabelPRODAJA_NEKRETNINA__PRIMATELJProps);
    Label121 = sect.addModel(ep.LABEL, Label121Props);
    TextVII13 = sect.addModel(ep.TEXT, TextVII13Props);
    Label68 = sect.addModel(ep.LABEL, Label68Props);
    LabelNABAVA_OSOBNIH_VOZILA_ = sect.addModel(ep.LABEL, LabelNABAVA_OSOBNIH_VOZILA_Props);
    TextVII14 = sect.addModel(ep.TEXT, TextVII14Props);
    Label131 = sect.addModel(ep.LABEL, Label131Props);
    Label69 = sect.addModel(ep.LABEL, Label69Props);
    LabelPRODAJA_OSOBNIH_VOZILA_ = sect.addModel(ep.LABEL, LabelPRODAJA_OSOBNIH_VOZILA_Props);
    TextVII141 = sect.addModel(ep.TEXT, TextVII141Props);
    Label141 = sect.addModel(ep.LABEL, Label141Props);
    Label70 = sect.addModel(ep.LABEL, Label70Props);
    LabelNABAVA_RABLJ_OSOB_VOZIL = sect.addModel(ep.LABEL, LabelNABAVA_RABLJ_OSOB_VOZILProps);
    Label151 = sect.addModel(ep.LABEL, Label151Props);
    TextVII15 = sect.addModel(ep.TEXT, TextVII15Props);
    Label71 = sect.addModel(ep.LABEL, Label71Props);
    TextVII16 = sect.addModel(ep.TEXT, TextVII16Props);
    Label161 = sect.addModel(ep.LABEL, Label161Props);
    LabelNABAVA_OSTALE_DUGOTRAJNE_MIROVINE = sect.addModel(ep.LABEL, LabelNABAVA_OSTALE_DUGOTRAJNE_MIROVINEProps);
    Label72 = sect.addModel(ep.LABEL, Label72Props);
    TextVII17 = sect.addModel(ep.TEXT, TextVII17Props);
    Label171 = sect.addModel(ep.LABEL, Label171Props);
    LabelPRODAJA_OSTALE_DUGOTRAJNE_IMOVINE = sect.addModel(ep.LABEL, LabelPRODAJA_OSTALE_DUGOTRAJNE_IMOVINEProps);
    Label73 = sect.addModel(ep.LABEL, Label73Props);
    Label74 = sect.addModel(ep.LABEL, Label74Props);
    LabelOTUDjENJESTJECANJE_GOSPODARSKE_CJELINE_ILI = sect.addModel(ep.LABEL, LabelOTUDjENJESTJECANJE_GOSPODARSKE_CJELINE_ILIProps);
    Label213 = sect.addModel(ep.LABEL, Label213Props);
    TextVII2 = sect.addModel(ep.TEXT, TextVII2Props);
    Label75 = sect.addModel(ep.LABEL, Label75Props);
    Label76 = sect.addModel(ep.LABEL, Label76Props);
    LabelPOCETAK_I_PRESTANAK_OBAVLJANJA = sect.addModel(ep.LABEL, LabelPOCETAK_I_PRESTANAK_OBAVLJANJAProps);
    Label312 = sect.addModel(ep.LABEL, Label312Props);
    TextVII3 = sect.addModel(ep.TEXT, TextVII3Props);
    LabelZA_IZNOS_PREPLATE_PODNOSI1 = sect.addModel(ep.LABEL, LabelZA_IZNOS_PREPLATE_PODNOSI1Props);
    Line16 = sect.addModel(ep.LINE, Line16Props);
    Line17 = sect.addModel(ep.LINE, Line17Props);
    Line18 = sect.addModel(ep.LINE, Line18Props);
    LabelOBRACUN_SASTAVIO_IME_PREZIME = sect.addModel(ep.LABEL, LabelOBRACUN_SASTAVIO_IME_PREZIMEProps);
    Line19 = sect.addModel(ep.LINE, Line19Props);
    Line20 = sect.addModel(ep.LINE, Line20Props);
    Line21 = sect.addModel(ep.LINE, Line21Props);
    Line22 = sect.addModel(ep.LINE, Line22Props);
    Line23 = sect.addModel(ep.LINE, Line23Props);
    Line24 = sect.addModel(ep.LINE, Line24Props);
    Line25 = sect.addModel(ep.LINE, Line25Props);
    Line26 = sect.addModel(ep.LINE, Line26Props);
    Line27 = sect.addModel(ep.LINE, Line27Props);
    Line28 = sect.addModel(ep.LINE, Line28Props);
    Line29 = sect.addModel(ep.LINE, Line29Props);
    LabelNADNEVAK_PRIMITKA = sect.addModel(ep.LABEL, LabelNADNEVAK_PRIMITKAProps);
    Line30 = sect.addModel(ep.LINE, Line30Props);
    Line31 = sect.addModel(ep.LINE, Line31Props);
    Line32 = sect.addModel(ep.LINE, Line32Props);
    Line33 = sect.addModel(ep.LINE, Line33Props);
    LabelPOTPIS_I_PECAT_POREZNOG = sect.addModel(ep.LABEL, LabelPOTPIS_I_PECAT_POREZNOGProps);
    LabelPOTPIS_SLUZBENIKA_POREZNE_UPRAVE = sect.addModel(ep.LABEL, LabelPOTPIS_SLUZBENIKA_POREZNE_UPRAVEProps);
    Line34 = sect.addModel(ep.LINE, Line34Props);
    Line35 = sect.addModel(ep.LINE, Line35Props);
    Label77 = sect.addModel(ep.LABEL, Label77Props);
    LabelNAPOMENA = sect.addModel(ep.LABEL, LabelNAPOMENAProps);
    Line36 = sect.addModel(ep.LINE, Line36Props);
    TextNAPOMENA = sect.addModel(ep.TEXT, TextNAPOMENAProps);
    return sect;
  }

  public abstract raReportSection createDetail();

  public abstract raReportSection createPageFooter();

  public abstract raReportSection createReportFooter();

  public void createReportStructure() {
    template = ModelFactory.getModel(ep.REPORT_TEMPLATE);
    ModelFactory.setCurrentReport(template);

    ReportTemplate = addSection(new raReportSection(template, ReportTemplateProps));

    PageSetup = addSection(new raReportSection(template.getModel(ep.PAGE_SETUP), PageSetupProps));
    Sections = addSection(createSections());

    ReportHeader = addSection(createReportHeader());
    PageHeader = addSection(createPageHeader());
    SectionHeader0 = addSection(createSectionHeader0());
    SectionHeader1 = addSection(createSectionHeader1());
    Detail = addSection(createDetail());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}

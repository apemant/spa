/****license*****************************************************************
**   file: zpRes.java
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
package hr.restart.zapod;


public class zpRes extends java.util.ListResourceBundle {
  static final Object[][] contents = new String[][]{
	{ "jzpMenu_text", "Zajedni\u010Dki podaci" },
	{ "jzpMenuOrgJed_text", "Organizacijske jedinice" },
	{ "jzpMenuKonta_text", "Kontni plan" },
	{ "jzpMenuPpar_text", "Poslovni partneri" },
	{ "jzpMenuZirorn_text", "Žiro ra\u010Duni" },
	{ "jzpMenuValute_text", "Valute" },
	{ "jzpMenuTec_text", "Te\u010Dajne liste" },
	{ "jzpMenuAgenti_text", "Agenti" },
	{ "jzpMenuLog_text", "Logotipovi" },
	{ "jpOsnovniPodaci_text", " Osnovni podaci " },
	{ "jpDodatniPodaci_text", " Dodatni podaci " },
	{ "jpPoslovneJedinice_text", " Poslovne jedinice " },
	{ "jcbAKTIV_text", "Aktivan" },
	{ "jlNAZPAR_text", "Naziv" },
	{ "jlPtMJ_text", "Mjesto" },
	{ "jlADR_text", "Adresa" },
	{ "jlMB_text", "Mati\u010Dni broj" },
	{ "jlCDJEL_text", "Djelatnost" },
	{ "jlZR_text", "Žiro ra\u010Dun" },
	{ "jlTEL_text", "Telefoni" },
	{ "jlTELFAX_text", "Faks" },
	{ "jlEMADR_text", "E-mail" },
	{ "jlKO_text", "Osoba" },
	{ "jlULOGA_text", "Uloga" },
	{ "jcbULOGAkupac_text", "Kupac" },
	{ "jcbULOGAdobavljac_text", "Dobavlja\u010D" },
	{ "jlCGRPAR_text", "Grupa" },
	{ "jlREGIJA_text", "Regija-ruta" },
	{ "jlPRAB_text", "Rabat" },
	{ "jlUGOVOR_text", "Ugovor" },
	{ "jlLIMKRED_text", "Limit kreditiranja" },
	{ "jlAGENT_text", "Oznaka" },
	{ "jlBRRAC_text", "Broj ra\u010Duna:" },
	{ "jlBRDOK_text", "Broj dokumenta:" },
	{ "jlBRDOKUL_text", "Ulazni dokument:" },
	{ "jlUIZT_text", "Iznos ZT:" },
	{ "jlDVO_text", "DVO:" },
	{ "jlDATDOSP_text", "Datum dosp." },
	{ "jlDATDOKUL_text", "Datum:" },
	{ "jlMAR_text", "Marža:" },
	{ "jcbULOGAoboje_text", "Oboje" },
	{ "jLBrojKonta_text", "Broj" },
	{ "jLNazivKonta_text", "Naziv" },
	{ "jdbCBOrgStr_text", "Pridruženost organizacijskoj jedinici" },
	{ "jdbRBDugovni_text", "Dugovni" },
	{ "jdbRBPotrazni_text", "Potražni" },
	{ "jdbRBOboje_text", "Oboje" },
	{ "jdbRBKupci_text", "Kupac" },
	{ "jdbRBDobavljaci_text", "Dobavlja\u010D" },
	{ "jdbCBispisBB_text", "Ispis na bruto bilanci" },
	{ "jlVRSTAKONTA_text", "Vrsta" },
	{ "jLCorg_text", "Oznaka" },
	{ "jLNazOrg_text", "Naziv" },
	{ "jLAdresaOrg_text", "Ulica i broj" },
	{ "jLMjestoOrg_text", "Mjesto" },
	{ "jLPttBrojOrg_text", "PT broj" },
	{ "jLZiroOrg_text", "Žiro ra\u010Dun" },
	{ "jdbCBnalog_text", "Samostalno knjigovodstvo" },
	{ "tBorderOrgPrip_text", "Pripadnost organizacijskoj jedinici" },
	{ "errKont_klasa", "Za upisani broj konta nije otvorena vi\u0161a siteti\u010Dka grupa." },
	{ "errKont_unos", "Neispravan unos broja konta!" },
	{ "jLCorgPrip_text", "Pripadnost" },
	{ "errPj_unos", "Potrebno je prvo ispravno unijeti podatke o poslovnom partneru!" },
	{ "jlKARAKTERISTIKA_text", "Karakteristika" },
	{ "jlSALDAK_text", "Konto za salda konti" },
	{ "jlDI_text", "Partner" },
        { "jlCPAR_text", "Oznaka"},
	{ "frmGrupPart_title", "Grupe partnera" },
	{ "frmKontaPar_title", "Konta za poslovne partnere" },
	{ "frmUgovori_title", "Ugovori" },
	{ "frmBanke_title", "Banke" },
	{ "frmKartice_title", "Kartice" },
	{ "frmNapomene_title", "Napomene" },
	{ "frmVrtros_title", "Vrste tro\u0161ka" },
	{ "frmNacPl_title", "Na\u010Dini pla\u0107anja" },
	{ "jlDOSP_text", "Dospije\u0107e" }};
  public Object[][] getContents() {
    return contents;
  }
}
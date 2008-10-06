/****license*****************************************************************
**   file: Res_.java
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
package hr.restart.util;


public class Res_ extends java.util.ListResourceBundle {
  static final Object[][] contents = new String[][]{
	{ "Tabli_u010Dni_prikaz", "Tabli\u010Dni prikaz" },
	{ "Detaljni_prikaz", "Detaljni prikaz" },
	{ "jBIzmjena", "Izmjena" },
	{ "jBBrisanje", "Brisanje" },
	{ "jBIspis", "Ispis" },
	{ "jIzlaz", "Izlaz" },
	{ "jBNovi", "Novi" },
	{ "jPrekid", "Prekid" },
        { "jBStavke","Stavke" },
	{ "jBOK", "OK" },
	{ "jlSifraPoreza_text", "Šifra poreza" },
	{ "jlNazivPoreza_text", "Naziv poreza" },
	{ "jlAktivan_text", "Aktivan" },
	{ "jlPorez1_text", "Porez 1" },
	{ "jlPorez2_text", "Porez 2" },
	{ "jlPorez3_text", "Porez 3" },
	{ "jlPorezUkupno_text", "Porez - ukupno" },
	{ "jlPorez1unazad_text", "Porez 1-unazad" },
	{ "jlPorez2unazad_text", "Porez 2-unazad" },
	{ "jlPorez3unazad_text", "Porez 3-unazad" },
	{ "jlUnazadUkupno_text", "Unazad - ukupno" },
	{ "jlSifraPripGrupeArt_text", "Šifra" },
	{ "jcbGlavnaGrupa_text", "Glavna grupa artikala" },
	{ "jlNazivGrupArt_text", "Naziv grupe artikla" },
	{ "jlSifraGrupArt_text", "Šifra grupe artikla" },
	{ "jtpPripGrupArt", "Pripadnost grupi artikla" },
	{ "skladiste", "Skladište" },
	{ "orgjed", "Organizacijska jedinica" },
	{ "Datdok", "Datum" },
	{ "cpar", "Partner" },
	{ "tecaj", "Te\u010Daj" },
        {"0",""},
        {"00",""},
        {"000",""},
        {"0E1",""},
        {"0E2","tisu\u0107a"},
        {"0E3","miliona"},
        {"0E4","milijardi"},
        {"1","jedna"},
        {"10","deset"},
        {"100","sto"},
        {"11","jedanaest"},
        {"12","dvanaest"},
        {"13","trinaest"},
        {"14","\u010Detrnaest"},
        {"15","petnaest"},
        {"16","šestnaest"},
        {"17","sedamnaest"},
        {"18","osamnaest"},
        {"19","devetnaest"},
        {"1E1",""},
        {"1E2","jednatisu\u0107a"},
        {"1E3","jedanmilion"},
        {"1E4","jednamilijarda"},
        {"2","dvije"},
        {"20","dvadeset"},
        {"200","dvjesto"},
        {"2E1",""},
        {"2E2","dvijetisu\u0107e"},
        {"2E3","dvamiliona"},
        {"2E4","dvijemilijarde"},
        {"3","tri"},
        {"30","trideset"},
        {"300","tristo"},
        {"3E1",""},
        {"3E2","tritisu\u0107e"},
        {"3E3","trimiliona"},
        {"3E4","trimilijarde"},
        {"4","\u010Detiri"},
        {"40","\u010Detrdeset"},
        {"400","\u010Detristo"},
        {"4E1",""},
        {"4E2","\u010Detiritisu\u0107e"},
        {"4E3","\u010Detirimiliona"},
        {"4E4","\u010Detirimilijarde"},
        {"5","pet"},
        {"50","pedeset"},
        {"500","petsto"},
        {"5E1",""},
        {"5E2","pettisu\u0107a"},
        {"5E3","petmiliona"},
        {"5E4","petmilijardi"},
        {"6","šest"},
        {"60","šezdeset"},
        {"600","šesto"},
        {"6E1",""},
        {"6E2","šesttisu\u0107a"},
        {"6E3","šestmiliona"},
        {"6E4","šestmilijardi"},
        {"7","sedam"},
        {"70","sedamdeset"},
        {"700","sedamsto"},
        {"7E1",""},
        {"7E2","sedamtisu\u0107a"},
        {"7E3","sedammiliona"},
        {"7E4","sedammilijardi"},
        {"8","osam"},
        {"80","osamdeset"},
        {"800","osamsto"},
        {"8E1",""},
        {"8E2","osamtisu\u0107a"},
        {"8E3","osammiliona"},
        {"8E4","osammilijardi"},
        {"9","devet"},
        {"90","devedeset"},
        {"900","devetsto"},
        {"9E1",""},
        {"9E2","devettisu\u0107a"},
        {"9E3","devetmiliona"},
        {"9E4","devetmilijardi"}
      };


  static final Object[][] contentseng = new String[][]{
	{ "Tabli_u010Dni_prikaz", "Table view" },
	{ "Detaljni_prikaz", "Detail view" },
	{ "jBIzmjena", "Change" },
	{ "jBBrisanje", "Delete" },
	{ "jBIspis", "Print" },
	{ "jIzlaz", "Exit" },
	{ "jBNovi", "New" },
	{ "jPrekid", "Cancel" },
	{ "jBOK", "OK" },
        { "jBStavke","Stavke"}};

  public Object[][] getContents() {

    return getContents("hr");
  }
  public Object[][] getContents(String lang) {
  if (lang.equals("eng"))
      return contentseng ;
  else
    return contents;
  }
}
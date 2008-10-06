/****license*****************************************************************
**   file: raRes.java
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
package hr.restart;


public class raRes extends java.util.ListResourceBundle {
  static final Object[][] contents = new String[][]{
	{ "APLzapod", "hr.restart.zapod.frmZapod" },
  { "APLsklad", "hr.restart.sklad.frmSklad" },
  { "APLcstd", "hr.restart.cstd.frmCstd" },
  { "APLrobno", "hr.restart.robno._Main" },//<disabled>
  { "APLmp", "hr.restart.mp.frmMp" },
  { "APLpos", "hr.restart.pos.posMain" },
  { "APLrac", "hr.restart.rac.frmRac" },
  { "APLrn", "hr.restart.rn.rnMain" },
  { "APLposl", "hr.restart.posl.frmPosl" },
  { "APLsk", "hr.restart.sk.frmSK" },
  { "APLgk", "hr.restart.gk.frmGK" },
	{ "APLpl", "hr.restart.pl.frmPL" },
  { "APLblpn", "hr.restart.blpn.frmBLPN" },
  { "APLur", "hr.restart.ur.frmUR" },  
  { "APLos", "hr.restart.os.osMain" },
  { "APLok", "hr.restart.ok.frmOK" },
  { "APLvir", "hr.restart.zapod.frmVir" },
	{ "APLsisfun", "hr.restart.sisfun.frmSistem" },
	{ "jBzapod_text", "Zajedni\u010Dki podaci" },
	{ "jBok_text", "Obraèun kamata" },
  { "jBvir_text", "Pisanje virmana" },
	{ "jBsisfun_text", "Sistemske funkcije" },
	{ "jBrn_text", "Servis i proizvodnja" },
	{ "jBgk_text", "Glavna knjiga" },
	{ "jBsk_text", "Salda konti" },
	{ "jBrobno_text", "Robno knjigovodstvo" },
	{ "jBpos_text", "Maloprodaja (POS)" },
	{ "jBos_text", "Osnovna sredstva" },
        { "jBmp_text", "Maloprodaja" },
        { "jBsklad_text", "Skladišno poslovanje" },
        { "jBcstd_text", "Carinsko skladište tipa D" },
        { "jBrac_text", "Obrada raèuna" },
        { "jBposl_text", "Komercijalno poslovanje" },
	{ "jBpl_text", "Pla\u0107e" },
	{ "jBblpn_text", "Blagajna i putni nalozi" },
    { "jBur_text", "Urudžbeno" },
	{ "jMPos_text", "Pozicija" },
	{ "jMPosTop_text", "Gore" },
	{ "jMPosRight_text", "Desno" },
	{ "jMexit_text", "Izlaz" }};
  public Object[][] getContents() {
    return contents;
  }
}
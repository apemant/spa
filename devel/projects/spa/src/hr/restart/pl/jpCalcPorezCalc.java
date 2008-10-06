/****license*****************************************************************
**   file: jpCalcPorezCalc.java
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
/*
 * jpCalcPorezCalc.java
 *
 * Created on 2003. prosinac 03, 14:52
 */

package hr.restart.pl;

import hr.restart.swing.JraButton;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *  polja <read-only> 1-n - do 5 stavki
 *    bruto,    (koefsindrom31860)
 *    stdop1-n,
 *    <iznosdop1-n>
 *    <ukdop>
 *    osnolak, koefolak
 *    premije1-n
 *    <sumolak>
 *    <iskolak>
 *    <porosn>
 *    stpor1-n,
 *    <iznospor1-n>
 *    <ukporez>
 *    stprir
 *    <iznosprir>
 *    <por+prir>
 *    neto
 * 
 *    maxosndop1-n,
 *    maxosnpor1-n,
 * @author  andrej
 */
public class jpCalcPorezCalc extends jpCalcPorez {
  String tit = "Izraèun";
  int mode = 0;
  public jpCalcPorezCalc(frmCalcPorez f) {
    super(f);
    jInit();
  }
  
  public jpCalcPorezCalc(frmCalcPorez f, String _tit, int _mode) {
    super(f);
    tit = _tit;
    mode = _mode;
    jInit();
  }
  
  public void jInit() {
    if (mode == 0 || mode == 1 || mode == 4) initBrutoDoprinosi();
    if (mode == 0 || mode == 2 || mode == 4) initOlaksice();
    if (mode == 0 || mode == 3) initPoreziINeto();
    addAncestorListener(new AncestorListener() {
      public void ancestorAdded(AncestorEvent event) {
        fCalcPorez.fixOkp();
      }
      public void ancestorRemoved(AncestorEvent event) {
      }
      public void ancestorMoved(AncestorEvent event) {
      }
    });
  }
  public void initBrutoDoprinosi() {
    builder.appendSeparator("Plaæa");
    addTextFieldRow("Bruto",true,getCalcPorezButton(false));
//    builder.append("Korekcija-31860",new Container(),getNumericTextField("Korekcija-31860",6),1);
    
    //addTextFieldRow("Doprinosi",true);
    addListColsRow("Doprinosi", new String[] {"Opis","Osnovica","Stopa","Iznos"},2);
    builder.appendSeparator();
    addTextFieldRow("Plaæa po odbitku doprinosa",false);
  }
  public void initOlaksice() {
    builder.appendSeparator("Porezne olakšice");
    addTextFieldRow("Osnovna olakšica",true);
    addTextFieldRow("Koeficijent olakšice",true);
    addTextFieldRow("Premije osiguranja",true);
    addTextFieldRow("Ukupno olakšica",false);
    addTextFieldRow("Iskorištena olakšica",false);
    builder.appendSeparator();
    addTextFieldRow("Porezna osnovica",false);    
  }
  public void initPoreziINeto() {
    addListColsRow("Porez", new String[] {"Porez","Por.osnovica","Stopa poreza","Iznos poreza"},4);
    builder.appendSeparator("Prirez");
    addTextFieldRow("Stopa prireza",true);
    addTextFieldRow("Prirez",false);
    addTextFieldRow("Porez i prirez",false);
    builder.appendSeparator("Plaæa po odbitku doprinosa, poreza i prireza");
    addTextFieldRow("Neto",true, getCalcPorezButton(true));
  }
  public Container getCalcPorezButton(final boolean isBack) {
    if (mode != 0) return new Container();
    JraButton b = new JraButton();
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        fCalcPorez.calcGo(isBack);
      }
    });
    b.setText("Izraèunaj "+(isBack?"bruto":"neto"));
    return b;
  }
  public String getTitle() {
    return tit;
  }
}

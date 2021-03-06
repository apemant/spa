/****license*****************************************************************
**   file: raKIZ.java
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

import hr.restart.util.raTransaction;

public class raKIZ extends raIzlazTemplate {

    public void initialiser() {
        what_kind_of_dokument = "KIZ";
    }

    public void MyaddIspisMaster() {

        raMaster.getRepRunner().addReport("hr.restart.robno.repPonuda",
                "hr.restart.robno.repIzlazni", "Ponuda", "Ponuda 1 red");
        raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaV",
                "hr.restart.robno.repIzlazni", "Ponuda",
                "Ponuda 1 red u valuti");
        raMaster.getRepRunner().addReport("hr.restart.robno.repPonuda2",
                "hr.restart.robno.repIzlazni", "Ponuda2", "Ponuda 2 red");
        raMaster.getRepRunner().addReport("hr.restart.robno.repPonuda2V",
                "hr.restart.robno.repIzlazni", "Ponuda2",
                "Ponuda 2 red u valuti");
        raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaPop",
            "hr.restart.robno.repIzlazni", "PonudaPop", "Ponuda 1 red s iznosom popusta");
        raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaNoi",
            "hr.restart.robno.repIzlazni", "PonudaNoi", "Ponuda bez iznosa i suma");
        raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaNop",
            "hr.restart.robno.repIzlazni", "PonudaNop", "Ponuda bez cijena stavki");
        raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaGroup",
            "hr.restart.robno.repGroupIzlazni", "PonudaGroup", "Ponuda po grupama artikala");
        raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaGroupPop",
            "hr.restart.robno.repGroupIzlazni", "PonudaGroupPop", "Ponuda po grupama artikala i iznosom popusta");
        raMaster.getRepRunner().addReport("hr.restart.robno.repOffer",
            "hr.restart.robno.repIzlazni","ProformaInvoice3","Offer");
        raMaster.getRepRunner().addReport("hr.restart.robno.repMxPON",
                "Matri\u010Dni ispis ponude");
        raMaster.getRepRunner().addReport("hr.restart.robno.repMxPONPop",
        "Matri\u010Dni ispis ponude s vi�e popusta");
    }

    public void MyaddIspisDetail() {

        raDetail.getRepRunner().addReport("hr.restart.robno.repPonuda",
                "hr.restart.robno.repIzlazni", "Ponuda", "Ponuda 1 red");
        raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaV",
                "hr.restart.robno.repIzlazni", "Ponuda",
                "Ponuda 1 red u valuti");
        raDetail.getRepRunner().addReport("hr.restart.robno.repPonuda2",
                "hr.restart.robno.repIzlazni", "Ponuda2", "Ponuda 2 red");
        raDetail.getRepRunner().addReport("hr.restart.robno.repPonuda2V",
                "hr.restart.robno.repIzlazni", "Ponuda2",
                "Ponuda 2 red u valuti");
        raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaPop",
            "hr.restart.robno.repIzlazni", "PonudaPop", "Ponuda 1 red s iznosom popusta");
        raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaNoi",
            "hr.restart.robno.repIzlazni", "PonudaNoi", "Ponuda bez iznosa i suma");
        raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaNop",
                "hr.restart.robno.repIzlazni", "PonudaNop", "Ponuda bez cijena stavki");
        raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaGroup",
            "hr.restart.robno.repGroupIzlazni", "PonudaGroup", "Ponuda po grupama artikala");
        raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaGroupPop",
            "hr.restart.robno.repGroupIzlazni", "PonudaGroupPop", "Ponuda po grupama artikala i iznosom popusta");
        raDetail.getRepRunner().addReport("hr.restart.robno.repOffer",
            "hr.restart.robno.repIzlazni","ProformaInvoice3","Offer");
        raDetail.getRepRunner().addReport("hr.restart.robno.repMxPON",
                "Matri\u010Dni ispis ponude");
        raDetail.getRepRunner().addReport("hr.restart.robno.repMxPONPop",
        "Matri\u010Dni ispis ponude s vi�e popusta");

    }


    
    
    public raKIZ() {
        setPreSel((jpPreselectDoc) presPON.getPres());
        addButtons(true, true);
//      raMaster.addOption(rnvDellAll, 3);
        raDetail.addOption(rnvDellAllStav, 3);
        raDetail.addOption(rnvKartica, 5, false);
        master_titel = "Ponude";
        detail_titel_mno = "Stavke ponude";
        detail_titel_jed = "Stavka ponude";
        setMasterSet(dm.getZagPonPar());
        setDetailSet(dm.getStPon());
        MP.BindComp();
        DP.BindComp();
        rCD.setisNeeded(false);
        DP.resizeDP();
        raDetail.addOption(rnvCopyPon, 6, false);
        raDetail.addOption(rnvMultiPon, 7, false);
    }

    public void RestPanelSetup() {
        DP.addRest();
        DP.instalRezervaciju();
    }
    
    public void SetFokusDetail(char mode) {
        super.SetFokusDetail(mode);
        if (mode=='N'){
            DP.setRezervacija();            
//          if (hr.restart.sisfun.frmParam.getParam("robno", "rezkol",
//                  "Rezerviranje koli�ine D/N", "D").equalsIgnoreCase("D")) {
//              getDetailSet().setString("REZKOL", "D");
//              DP.jrtbRezervacija.setSelected(true);
//              
//          } else {
//              getDetailSet().setString("REZKOL", "N");
//              DP.jrtbRezervacija.setSelected(false);
//          }           
        }
    }
    
    public void brisiRezervaciju() {
        if (!rezkoldel.equalsIgnoreCase("D")) return;
        AST.findStanjeUnconditional(god4del,cskl4del,cart4del);
        boolean bSnimanje = !(AST.gettrenSTANJE() == null
                || AST.gettrenSTANJE().getRowCount() == 0) ;
        if (bSnimanje) { 
            AST.gettrenSTANJE().setBigDecimal("KOLREZ", 
                    AST.gettrenSTANJE().getBigDecimal("KOLREZ")
                    .subtract(rKD.stavkaold.kol));
            raTransaction.saveChanges(AST.gettrenSTANJE());
        }
    }

    public void dodajRezervaciju() {

        if (!isUslugaOrTranzit()) {
            AST.findStanjeUnconditional(
                    getDetailSet().getString("GOD"),
                    getDetailSet().getString("CSKL"),
                    getDetailSet().getInt("CART"));
            
            boolean nemaGa = AST.gettrenSTANJE() == null
            || AST.gettrenSTANJE().getRowCount() == 0; 
            if (nemaGa) {
                AST.gettrenSTANJE().insertRow(false);
                AST.gettrenSTANJE().setString("GOD",
                        getMasterSet().getString("GOD"));
                AST.gettrenSTANJE().setString("CSKL",
                        getDetailSet().getString("CSKL"));
                AST.gettrenSTANJE().setInt("CART",
                        getDetailSet().getInt("CART"));
                nulaStanje(AST.gettrenSTANJE());
            }
            lc.TransferFromDB2Class(AST.gettrenSTANJE(), rKD.stanje);
            if (raDetail.getMode()=='N'){
                if (!getDetailSet().getString("REZKOL").equalsIgnoreCase("D")) return;              
                rKD.stanje.kolrez = 
                    rKD.stanje.kolrez.add(rKD.stavka.kol);
            } else if (raDetail.getMode()=='I'){
                if (rKD.stavkaold.rezkol.equalsIgnoreCase("D")){
                    // vrati staru rezervaciju ako treba
                    rKD.stanje.kolrez = 
                        rKD.stanje.kolrez.subtract(rKD.stavkaold.kol);
                }
                if (getDetailSet().getString("REZKOL").equalsIgnoreCase("D")){
                    // stavi novu ako treba rezervaciju ako treba                   
                    rKD.stanje.kolrez = rKD.stanje.kolrez.add(rKD.stavka.kol);
                }
            }
            AST.gettrenSTANJE().setBigDecimal("KOLREZ",rKD.stanje.kolrez);
            if (nemaGa){
                AST.gettrenSTANJE().setBigDecimal("VC",getDetailSet().getBigDecimal("FC"));
                AST.gettrenSTANJE().setBigDecimal("MC",getDetailSet().getBigDecimal("FMCPRP"));             
            }
            raTransaction.saveChanges(AST.gettrenSTANJE());
        }

    }   

    public boolean ValidacijaStanje() {
        return true;
    }

    public boolean DodatnaValidacijaDetail() {

        if (val.isEmpty(DP.jtfKOL))
            return false;
        if (val.isEmpty(DP.jraFC))
            return false;
        if (manjeNula())
            return false;
        return true;
    }

    public boolean LocalValidacijaMaster() {
        if (getMasterSet().isNull("CPAR"))
          getMasterSet().setString("TNAZPAR", MP.panelBasic.jrfNAZPAR.getText());
        return true;
    }

    public boolean ValidacijaMasterExtend() {
        getMasterSet().setString("PARAM", "P");
        return true;
    }

    public void ConfigViewOnTable() {
        this.setVisibleColsMaster(new int[] { 4, 5, 6, 44, 34 }); // Requested
                                                                  // by Mladen
                                                                  // (Sini�a)
        this
                .setVisibleColsDetail(new int[] { 4,
                        Aut.getAut().getCARTdependable(5, 6, 7), 8, 11, 16, 12,
                        19, 24 });
    }

    public boolean isKPR() {
        return false;
    }

}
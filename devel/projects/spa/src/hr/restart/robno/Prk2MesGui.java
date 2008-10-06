/****license*****************************************************************
**   file: Prk2MesGui.java
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

import hr.restart.swing.JraDialog;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.raTwoTableChooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;

abstract class Prk2MesGui extends JraDialog {

    private String skladiste="";
    private String godina="";
    
    private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

    private raTwoTableChooser rTTC = new raTwoTableChooser(this);

    private QueryDataSet LijeviSet = new QueryDataSet();

    private QueryDataSet DesniSet = new QueryDataSet();

    OKpanel okpanel = new OKpanel() {
        public void jBOK_actionPerformed() {
            okSelect();
        }

        public void jPrekid_actionPerformed() {
            cancelSelect();
        }
    };
    
    public void okSelect(){
        
        if (DesniSet.getRowCount()>1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Smije se odabrati samo jedna primka za prijenos",
                    "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
            
            return;
        }
        if (DesniSet.getRowCount()==1) { 
            interFunc(DesniSet.getString("CSKL"),
                    DesniSet.getString("GOD"),
                    DesniSet.getString("VRDOK"),
                    DesniSet.getInt("BRDOK"));
        }
        this.setVisible(false);
        
    }
    
    abstract void interFunc(String cskl,String god,String vrdok,int brdok);
    
    public void cancelSelect(){
        this.setVisible(false);
    }
    
    public Prk2MesGui(){
        setModal(true);
        setTitle("Prijenos PRK u MES");
        rTTC.rnvSave.setVisible(false);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(rTTC, BorderLayout.CENTER);
        this.getContentPane().add(okpanel, BorderLayout.SOUTH);
        okpanel.registerOKPanelKeys(this);
        initAll();
        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2
                - getHeight() / 2);
    }
    
    void initAll() {
        Column col1 = dm.getDoku().getColumn("CSKL").cloneColumn();
        Column col2 = dm.getDoku().getColumn("VRDOK").cloneColumn();
        Column col4 = dm.getDoku().getColumn("BRDOK").cloneColumn();
        Column col3 = dm.getDoku().getColumn("GOD").cloneColumn();
        LijeviSet.setColumns(new Column[]{col1,col2,col3,col4});
        DesniSet.setColumns(new Column[]{col1.cloneColumn(),col2.cloneColumn(),
                col3.cloneColumn(),col4.cloneColumn()});
        LijeviSet.open();
        DesniSet.open();
    }
    
    public void findAll() {
        LijeviSet.enableDataSetEvents(false);
        DesniSet.enableDataSetEvents(false);
        LijeviSet.emptyAllRows();
        DesniSet.emptyAllRows();
        QueryDataSet qds = Util.getNewQueryDataSet(
                "select * from doku where vrdok='PRK' and statpla = 'N' and cskl='"+skladiste+"'");
        for(qds.first();qds.inBounds();qds.next()){
            LijeviSet.insertRow(false);
            LijeviSet.setString("CSKL",qds.getString("CSKL"));
            LijeviSet.setString("VRDOK",qds.getString("VRDOK"));
            LijeviSet.setInt("BRDOK",qds.getInt("BRDOK"));
            LijeviSet.setString("GOD",qds.getString("GOD"));
        }
        LijeviSet.enableDataSetEvents(true);
        DesniSet.enableDataSetEvents(true);
        LijeviSet.setTableName("PRK");
        DesniSet.setTableName("PRK");
        rTTC.setLeftDataSet(LijeviSet);
        rTTC.setRightDataSet(DesniSet);
        rTTC.initialize();
    }
    
    public void setGodina(String godina) {
        this.godina = godina;
    }
    public void setSkladiste(String skladiste) {
        this.skladiste = skladiste;
    }
    
}

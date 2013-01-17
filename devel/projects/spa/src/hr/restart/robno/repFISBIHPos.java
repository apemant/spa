package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.pos.frmMasterBlagajna;
import hr.restart.sisfun.frmParam;
import hr.restart.util.lookupData;

import com.borland.dx.sql.dataset.QueryDataSet;

public class repFISBIHPos extends repFISBIHRN {
  QueryDataSet master;
  public repFISBIHPos() {
    super("POS");
    setPrint("pos.INP");
    setResponse("pos");
  }
  public void fill() throws Exception {
    frmMasterBlagajna fmb = frmMasterBlagajna.getInstance();
    master = fmb.getMasterSet();
    ds = hr.restart.baza.Stpos.getDataModule().getTempSet("cskl='"+master.getString("CSKL")+"' and vrdok = 'GRC' and god =  '"+master.getString("GOD")+"' and brdok =  "+master.getInt("BRDOK"));
    ds.open();
    filelines.emptyAllRows();
    for (ds.first(); ds.inBounds(); ds.next()) {
      String row = "S,"+getLogickiBroj()+",______,_,__;";
      row = row + getString(ds, "NAZART", 32)+";"
        + getCijena(ds)+";"
        + getKolicina(ds)+";"
        + getOdjeljenje(ds)+";"
        + getGrupaArtikla(ds)+";"
        + getPoreskaGrupa(ds)+";"
        + "0;"
        + getKodArtikla(ds)+";"
        + getRabat(ds)+";"
        +";"//rezervisano
        + getMjera(ds)+";"
      ;
      filelines.insertRow(false);
      filelines.setString("LINE", row);
      filelines.post();
    }
    //kraj racuna @todo razraditi vise nacina placanja
    String row = "T,"+getLogickiBroj()+",______,_,__;"+getNacinPlacanja(master);
    addLine(row);
    row = "G,"+getLogickiBroj()+",______,_,__;LastReceiptNumber";
    addLine(row);
  }
  
  public String getMjera(QueryDataSet ds2) {
    /*
Predefinisane mjere su:
0- Komad (ne prikazuje se naziv mjere)
1 - Kilogram (kg)
2 - Gram (g)
3 - Tona (t)
4 - Litar (l)
5 - Decilitar (dl)
6 - Metar (m)
7 - Metar kvadratni (m2)
8 - Metar kubni (m3)
9 - Sat (h)
10 -
11 -
12 -
13 -
14 -
Maksimalno mjera može sadržati dva karaktera.
Posljednjih pet mjera možete sami definirati u programu PGM.
     */
    String jm = ds2.getString("JM");
    String cjm = frmParam.getParam("robno","FISBIHmjera"+jm,"0","Oznaka jed.mjere "+jm+" u Fiskalnoj blagajni BIH");
    return cjm;
  }
  public String getCijena(QueryDataSet ds2) {
    return ds2.getBigDecimal("MC")+"";
  }
  public String getRabat(QueryDataSet ds2) {
    // TODO Auto-generated method stub
    return ds2.getBigDecimal("PPOPUST1").add(ds2.getBigDecimal("PPOPUST2"))+"";
  }
  public String getKolicina(QueryDataSet ds2) {
    return ds2.getBigDecimal("KOL")+"";
  }
  protected void handleResponse(String sLRN) {
    try {
      master.setInt("FBR", Integer.parseInt(sLRN));
      master.saveChanges();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

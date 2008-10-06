package hr.restart.robno;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JOptionPane;

import hr.restart.baza.Condition;
import hr.restart.baza.KreirDrop;
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.util.Aus;
import hr.restart.util.LinkClass;
import hr.restart.util.Valid;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;

import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class DOStoIZD {
  private String cskl, godina, seqO;
  private Timestamp datumDo;
  private int brdok;
  public QueryDataSet izd_doki, izd_stdoki, qstanje, dos_doki;
  private HashSet dos_stdoki_sets;
  
  public DOStoIZD() {
    
  }
  /*bsh::
import hr.restart.robno.DOStoIZD;
DOStoIZD d2i = new DOStoIZD();
d2i.setCskl("402");
d2i.start();
   */
  private void msg(String msg) {
    if (raProcess.isRunning()) {
      raProcess.setMessage(msg, true);
    }
  }
  public void start() {
    if (getCskl() == null) {
      raProcess.fail();
      throw new IllegalArgumentException("Cskl mora biti postavljen");
    }
    System.out.println("DOStoIZD.start() :: cskl = "+getCskl()+", datum = "+getDatumDo());
    QueryDataSet izd_stdoki_greske = stdoki.getDataModule().getTempSet(Condition.nil);
    izd_stdoki_greske.open();
    msg("Dohvat stanja ...");
    qstanje = Stanje.getDataModule().getTempSet(Condition.whereAllEqual(new String[] {"CSKL","GOD"}, new String[] {getCskl(), getGodina()}));
    qstanje.open();
    setBrdok();
    msg("Obrada dokumenata ...");
    getIzdFromDosStdoki();
    msg("Provjera kolièina ...");
    if (!raIZD.isStanjeExist4allDS(izd_stdoki, izd_stdoki_greske, getGodina(), getCskl(),true)) {
      JOptionPane.showMessageDialog(null, "Za neke artikle ne postoje zalihe na skladištu.");
      frmTableDataView dw = new frmTableDataView(false, false);
      dw.setDataSet(izd_stdoki_greske);
      dw.setVisibleCols(new int[] {3,4,5,6,7,8,9,11});
      dw.show();
      raProcess.fail();
//test      
//      dw = new frmTableDataView(false, false);
//      dw.setDataSet(izd_stdoki);
//      dw.show();
    } else {
      msg("Snimanje promjena ...");
//test
    //test      
//      frmTableDataView dw = new frmTableDataView(false, false);
//      dw.setDataSet(izd_stdoki);
//      dw.show();
//
//      dw = new frmTableDataView(false, false);
//      dw.setDataSet(izd_doki);
//      dw.show();
//
//      dw = new frmTableDataView(false, false);
//      dw.setDataSet(dos_doki);
//      dw.show();
    //endtest
     
      //seq!!!!
      Valid.getValid().setSeqFilter(seqO);
      dM.getDataModule().getSeq().setDouble("BROJ", brdok);
//      raTransaction.saveChangesInTransaction(new QueryDataSet[] {
//          dos_doki,
//          izd_doki,
//          izd_stdoki,
//          dM.getDataModule().getSeq()});

      final QueryDataSet[] qdses = new QueryDataSet[] {
      dos_doki,
      izd_doki,
      izd_stdoki,
      dM.getDataModule().getSeq()};

      new raLocalTransaction() {
        public boolean transaction() {
          for (int i = 0; i < qdses.length; i++) {
            raTransaction.saveChanges(qdses[i]);
          }
          for (Iterator iterator = dos_stdoki_sets.iterator(); iterator.hasNext();) {
            QueryDataSet qds = (QueryDataSet) iterator.next();
            raTransaction.saveChanges(qds);
          }
          return true;
        }
      }.execTransaction();
      msg("Rekalkulacija stanja");
      BatchRekalkulacijaStanja.start(getCskl(), getGodina());
    }
  }
  private void setBrdok() {
    StorageDataSet dsq = new StorageDataSet();
    dsq.addColumn(doki.getDataModule().getColumn("VRDOK").cloneColumn());
    dsq.addColumn(doki.getDataModule().getColumn("CSKL").cloneColumn());
    dsq.addColumn(doki.getDataModule().getColumn("GOD").cloneColumn());
    dsq.open();
    dsq.setString("VRDOK", "IZD");
    dsq.setString("CSKL", getCskl());
    dsq.setString("GOD", getGodina());
    seqO = hr.restart.robno.Util.getUtil().getSeqString(dsq);
    brdok = Valid.getValid().findSeqInt(seqO,false);
  }
  private void getIzdFromDosStdoki() {
    dos_stdoki_sets = new HashSet();
    izd_doki = doki.getDataModule().getTempSet(Condition.nil);
    izd_doki.open();
    izd_stdoki = stdoki.getDataModule().getTempSet(Condition.nil);
    izd_stdoki.open();

    dos_doki = doki.getDataModule().getTempSet(
        Condition.whereAllEqual(
            new String[] {"CSKL","VRDOK","GOD","STATUS"}, new String[] {cskl, "DOS", getGodina(),"N"})
            .and(Condition.till("DATDOK", getDatumDo()))+" ORDER BY CSKL, VRDOK, GOD, BRDOK");
    dos_doki.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    dos_doki.open();
    for (dos_doki.first(); dos_doki.inBounds(); dos_doki.next()) {
      izd_doki.insertRow(false);
      dM.copyColumns(dos_doki, izd_doki); // 1 za 1 da ne kompliciramo
      izd_doki.setString("VRDOK", "IZD");
      izd_doki.setInt("BRDOK", brdok);//**************
      izd_doki.setTimestamp("DATDOK", getDatumDo());
      izd_doki.post();
//      hr.restart.robno.Util.getUtil().getBrojDokumenta(izd_doki);
      izd_doki.post();
      dos_doki.setString("STATUS", "P"); //Prenesen?
      QueryDataSet dos_stdoki = stdoki.getDataModule().getTempSet(Condition.whereAllEqual(new String[] {"CSKL","VRDOK","GOD","BRDOK"}, dos_doki));
      dos_stdoki.open();
      for (dos_stdoki.first(); dos_stdoki.inBounds(); dos_stdoki.next()) {
        izd_stdoki.insertRow(false);
        dM.copyColumns(dos_stdoki, izd_stdoki);
        izd_stdoki.setString("VRDOK", "IZD");
        izd_stdoki.setInt("BRDOK", izd_doki.getInt("BRDOK"));
        izd_stdoki.setString("VEZA", dos_stdoki.getString("ID_STAVKA"));
      // c/p iz raIz*
        izd_stdoki.setString("ID_STAVKA",
            raControlDocs.getKey(izd_stdoki, new String[] { "cskl",
                "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
        dos_stdoki.setString("VEZA", izd_stdoki.getString("ID_STAVKA"));
        izd_stdoki.setString("CSKLART", izd_stdoki.getString("CSKL"));
        calcSklad(izd_stdoki, qstanje);
        izd_stdoki.post();
        dos_stdoki.post();
//System.out.println(izd_stdoki);
      }
      dos_stdoki_sets.add(dos_stdoki);
      brdok ++;
    }
  }
  public static void calcSklad(QueryDataSet stavka, StorageDataSet stanje) {
          if (!hr.restart.util.lookupData.getlookupData().raLocate(stanje,"CART",
              stavka.getInt("CART")+"")) {
            System.err.println("OOOPS:: nisam nasao stanje za cart = "+stavka.getInt("CART"));
            return;
          }
          LinkClass lc = LinkClass.getLinkClass();
          raKalkulBDDoc rKBD = new raKalkulBDDoc();
          rKBD.setVrzal(stanje.getString("CSKL"));
          rKBD.setWhat_kind_of_document("IZD");
          rKBD.mode = 'N';
//System.out.println("STANJE :: "+stanje);          
//            stavka.setBigDecimal("ZC",stanje.getBigDecimal("ZC"));
//            stavka.setBigDecimal("NC",stanje.getBigDecimal("NC"));
//            stavka.setBigDecimal("VC",stanje.getBigDecimal("VC"));
//            stavka.setBigDecimal("MC",stanje.getBigDecimal("MC"));
//            stavka.post();
            lc.TransferFromDB2Class(stavka,rKBD.stavka);
            lc.TransferFromDB2Class(stanje,rKBD.stanje);
//System.out.println("prije "+rKBD.stavka);            
            rKBD.kalkSkladPart();
//System.out.println("poslije kalkSkladPart "+rKBD.stavka);            
            rKBD.KalkulacijaStanje(stavka.getString("VRDOK"));
//System.out.println("poslije KalkulacijaStanje"+rKBD.stavka);
            lc.TransferFromClass2DB(stanje,rKBD.stanje);
            lc.TransferFromClass2DB(stavka,rKBD.stavka);
  }
  public Timestamp getDatumDo() {
    if (datumDo == null) setDatumDo(new Timestamp(System.currentTimeMillis()));
    return datumDo;
  }

  public void setDatumDo(Timestamp datumDo) {
    this.datumDo = datumDo;
  }
  
  public String getCskl() {
    return cskl;
  }
  
  public void setCskl(String cskl) {
    this.cskl = cskl;
  }
  
  public String getGodina() {
    if (godina == null) godina = Valid.getValid().findYear(getDatumDo());
    return godina;
  }
  
  public void setGodina(String godina) {
    this.godina = godina;
  }

}

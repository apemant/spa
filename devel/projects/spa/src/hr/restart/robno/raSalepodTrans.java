package hr.restart.robno;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.help.MsgDispatcher;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.Util.LoadingConversionRules;
import hr.restart.zapod.OrgStr;


public class raSalepodTrans {
  
  String tsif;
  String comm;
  lookupData ld = lookupData.getlookupData();
  int lastsucc = 0;
  DataSet sk, rob;
  
  static boolean busy = false;
  
  public static synchronized boolean isBusy() {
    if (busy) return true;
    busy = true;
    return false;
  }
  
  public static synchronized void release() {
    busy = false;
  }
  
  public static synchronized void hold() {
    busy = true;
  }
  
  public static void install(int delay) {
    Timer t = new Timer(delay*1000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          new raSalepodTrans().exportData();
      }
    });
    t.start();
  }
  
  public void exportKartica() {
    int when = Aus.getNumber(hr.restart.util.IntParam.getTag("salepod.hour"));
    if (when <= 0) when = 5;
    System.out.println("export kartica, when " + when);
   
    tsif = frmParam.getParam("sisfun", "salespodSifra", "39001",
    "Šifra tvrtke za salespod");

    comm = frmParam.getParam("sisfun", "salepodComm", "",
    "Komanda za sinkronizaciju kartica salepod-a");
    
    dM.getDataModule().installPodConnection();

    if (!dM.getDataModule().isPod()) {
      return;
    }

    if (isBusy()) return;
    try {
      String god = Valid.getValid().findYear();
      god = Integer.toString(Aus.getNumber(god) - 1);
      
      findDoks();
            
      DataSet shema = Aus.q("SELECT * FROM shkonta WHERE app='sisfun' and vrdok='SPD'");
      String parq = "SELECT * from partneri where exists " +
        "(select * from doki WHERE doki.cpar = partneri.cpar " +
        "and doki.god>='#GOD')";
      if (ld.raLocate(shema, "POLJE", "PAR")) {
        parq = shema.getString("SQLCONDITION");
      }
      parq = new VarStr(parq).replace("#GOD", god).toString();
      System.out.println(parq);
    
      DataSet par = Aus.q(parq);
      DataSet pj = Aus.q("SELECT * FROM pjpar order by cpar");
      
      Connection crc = dM.getDataModule().getPodConnection();
      if (crc == null) return;
      
      try {    
        Statement d = crc.createStatement();
        d.executeUpdate("DELETE FROM Input_Kartica");
        d.close();
        
        PreparedStatement ps = crc.prepareStatement("INSERT INTO Input_Kartica(" +
            "TvrtkaSifra,KupacSifra,KupacLokacijaSifra,Rbr,JeHtml,Sadrzaj) " +
            "VALUES (?,?,?,?,?,?)"); 
        int tot = 0, succ = 0;
        for (par.first(); par.inBounds(); par.next()) {
          ps.setString(1, tsif);
          ps.setString(2, par.getInt("CPAR")+"");
          ps.setString(3, "0");
          ps.setInt(4, 1);
          ps.setBoolean(5, false);
          ps.setString(6, getCard(par.getInt("CPAR")));
          if (!ld.raLocate(pj, "CPAR", par.getInt("CPAR")+"")) {
            ++tot;
            succ += ps.executeUpdate();
          } else {
            do {
              ps.setString(1, tsif);
              ps.setString(2, par.getInt("CPAR")+"");
              ps.setString(3, pj.getInt("PJ")+"");
              ps.setInt(4, 1);
              ps.setBoolean(5, false);
              ps.setString(6, getCard(par.getInt("CPAR")));
              ++tot;
              succ += ps.executeUpdate();
            } while (pj.next() && (pj.getInt("CPAR") == par.getInt("CPAR")));
          }
        }
        ps.close();
        System.out.println("Dodano "+succ+"/"+tot+" kartica");
        
        if (comm.length() > 0) {
          callSync();
          System.out.println("Proces završio.");
        }
        
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } finally {
      lastsucc = when + 1;
      System.out.println("export kartica, lastsucc " + lastsucc);
      release();
    }
  }
  
  public void findDoks() {
    sk = Skstavke.getDataModule().getTempSet(
        "CPAR VRDOK BROJDOK DATDOK DATDOSP ID IP SALDO", Aus.getKnjigCond().
        and(Condition.equal("POKRIVENO", "N")).
        and(Aus.getCurrGKDatumCond(Valid.getValid().getToday()).
        and(Condition.in("VRDOK", new String[] {"IRN", "UPL", "OKK"}))));
    sk.open();
    sk.setSort(new SortDescriptor(new String[] {"CPAR", "DATDOK"}));
    
    DataSet skl = Util.getSkladFromCorg();
    DataSet knj = OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
    Condition sklDok = Condition.in("VRDOK", new String[] {"ROT", "POD"});
    Condition orgDok = Condition.in("VRDOK", new String[] {"RAC", "TER", "ODB"});
    Condition period = Condition.between("DATDOK",
        hr.restart.util.Util.getUtil().getFirstDayOfYear(),
        Valid.getValid().getToday());

    String query =
        "SELECT max(doki.cpar) as cpar, max(doki.vrdok) as vrdok, max(doki.pnbz2) as pnbz2, " +
        "max(doki.datdok) as datdok,max(doki.datdosp) as datdosp, sum(stdoki.iprodsp) as iprodsp "+
        "FROM doki,stdoki WHERE "+Util.getUtil().getDoc("doki","stdoki")+" AND "+
        period.and(Condition.equal("STATKNJ", "N")).and((sklDok.and(Condition.in("CSKL", skl))).
        or(orgDok.and(Condition.in("CSKL", knj, "CORG")))).qualified("doki") +
        " GROUP BY doki.cskl, doki.god, doki.vrdok, doki.brdok";
    rob = Aus.q(query);
    rob.setSort(new SortDescriptor(new String[] {"CPAR", "DATDOK"}));
  }
  
  public String getCard(int cpar) {
    List doks = new ArrayList();
    BigDecimal saldo = Aus.zero0, dsaldo = Aus.zero0, nsaldo = Aus.zero0;
    Timestamp today = hr.restart.util.Util.getUtil().
              getFirstSecondOfDay(Valid.getValid().getToday());
    if (ld.raLocate(sk, "CPAR", Integer.toString(cpar))) do {
      if (sk.getBigDecimal("ID").signum() != 0) {
        saldo = saldo.add(sk.getBigDecimal("SALDO"));
        if (!today.after(sk.getTimestamp("DATDOSP")))
          nsaldo = nsaldo.add(sk.getBigDecimal("SALDO"));
        else dsaldo = dsaldo.add(sk.getBigDecimal("SALDO"));
      } else {
        saldo = saldo.subtract(sk.getBigDecimal("SALDO"));
        dsaldo = dsaldo.subtract(sk.getBigDecimal("SALDO"));
      }
      doks.add(sk.getString("VRDOK") + " " + sk.getString("BROJDOK") +
          " od " + Aus.formatTimestamp(sk.getTimestamp("DATDOK")) +
          " = " + Aus.formatBigDecimal(sk.getBigDecimal("SALDO")));
    } while (sk.next() && (sk.getInt("CPAR") == cpar));
    
    if (ld.raLocate(rob, "CPAR", Integer.toString(cpar))) do {

      saldo = saldo.add(rob.getBigDecimal("IPRODSP"));
      if (!today.after(rob.getTimestamp("DATDOSP")))
        nsaldo = nsaldo.add(rob.getBigDecimal("IPRODSP"));
      else dsaldo = dsaldo.add(rob.getBigDecimal("IPRODSP"));

      doks.add(rob.getString("VRDOK") + " " + rob.getString("PNBZ2") +
          " od " + Aus.formatTimestamp(rob.getTimestamp("DATDOK")) +
          " = " + Aus.formatBigDecimal(rob.getBigDecimal("IPRODSP")));
    } while (rob.next() && (rob.getInt("CPAR") == cpar));
    
    if (nsaldo.signum() * dsaldo.signum() == -1)
      dsaldo = Aus.zero2;
    
    String card = "\n";
    card += "Ukupni saldo kupca: " + Aus.formatBigDecimal(saldo) + "\n";
    card += "Dospjeli saldo: " + Aus.formatBigDecimal(dsaldo) + "\n";
    if (ld.raLocate(dM.getDataModule().getPartneri(), "CPAR", Integer.toString(cpar)))
      card += "Limit kreditiranja: " + Aus.formatBigDecimal(
          dM.getDataModule().getPartneri().getBigDecimal("LIMKRED")) + "\n";
    
    card += "\n\n";
    if (doks.size() > 0) {
      card += "Otvoreni dokumenti:\n\n";
      for (int i = 0; i < doks.size(); i++) {
        if (card.length() + ((String) doks.get(i)).length() > 3700) break;
        card += doks.get(i) + "\n";
      }
    }
    return card;
  }

  public void exportData() {
    int when = Aus.getNumber(hr.restart.util.IntParam.getTag("salepod.hour"));
    if (when <= 0) when = 5;
    System.out.println("when " + when);
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Timestamp(System.currentTimeMillis()));
    int hour = cal.get(cal.HOUR_OF_DAY);
    System.out.println("hour " + hour);
    if (hour > lastsucc) lastsucc = hour;
    System.out.println("lastsucc " + lastsucc);
    if (lastsucc == when) {
      exportKartica();
      return;
    }
    
    tsif = frmParam.getParam("sisfun", "salespodSifra", "39001",
        "Šifra tvrtke za salespod");
    
    comm = frmParam.getParam("sisfun", "salepodComm", "",
        "Komanda za sinkronizaciju salepod-a");
        
    dM.getDataModule().installPodConnection();
    
    if (!dM.getDataModule().isPod()) {
      return;
    }
    
    if (isBusy()) return;
    
    try {
      if (comm.length() > 0) {
        callSync();
        System.out.println("Proces završio.");
      }
      
      
    String god = Valid.getValid().findYear();
    god = Integer.toString(Aus.getNumber(god) - 1);
    
    DataSet shema = Aus.q("SELECT * FROM shkonta WHERE app='sisfun' and vrdok='SPD'");
    String parq = "SELECT * from partneri where exists " +
      "(select * from doki WHERE doki.cpar = partneri.cpar " +
      "and doki.god>='#GOD')";
    if (ld.raLocate(shema, "POLJE", "PAR")) {
      parq = shema.getString("SQLCONDITION");
    }
    parq = new VarStr(parq).replace("#GOD", god).toString();
    System.out.println(parq);
    
    DataSet par = Aus.q(parq);
    
    String pjq = "SELECT * from pjpar where exists " +
      "(select * from doki WHERE doki.cpar = pjpar.cpar " +
      "and doki.god>='#GOD')";
    if (ld.raLocate(shema, "POLJE", "PJ")) {
      pjq = shema.getString("SQLCONDITION");
    }
    pjq = new VarStr(pjq).replace("#GOD", god).toString();
    System.out.println(pjq);
    
    DataSet pj = Aus.q(pjq);
    
    String npjq = "SELECT cpar,0 as pj,nazpar as nazpj,mj as mjpj,pbr as pbrpj,adr as adrpj,tel as telpj from partneri where exists " +
      "(select * from doki WHERE doki.cpar = partneri.cpar " +
      "and doki.god>='#GOD') and not exists (select * from pjpar where pjpar.cpar=partneri.cpar)";
    if (ld.raLocate(shema, "POLJE", "NPJ")) {
      npjq = shema.getString("SQLCONDITION");
    }
    npjq = new VarStr(npjq).replace("#GOD", god).toString();
    System.out.println(npjq);
  
    DataSet npj = Aus.q(npjq);
    
    
    String artq = "SELECT artikli.cart, artikli.nazart, artikli.nazpri, " +
    "artikli.bc, artikli.aktiv, artikli.cpor, artikli.nazpak, artikli.brjed, grupart.cgrart, grupart.nazgrart, stanje.vc, stanje.mc " +
    "FROM artikli, stanje, grupart WHERE artikli.cart = stanje.cart "+
    "AND artikli.cgrart = grupart.cgrart and stanje.god>='#GOD' and stanje.cskl='206'"; 
    if (ld.raLocate(shema, "POLJE", "ART")) {
      artq = shema.getString("SQLCONDITION");
    }
    artq = new VarStr(artq).replace("#GOD", god).toString();
    System.out.println(artq);
    
    StorageDataSet art = Aus.q(artq);
    
    String[] cc = {"NAZART", "NAZPRI", "AKTIV", "CPOR", "CGRART", "NAZGRART", "VC", "MC"};
    String partq = "SELECT artikli.cart, artikli.nazart, artikli.nazpri, " +
    "artikli.bc, artikli.aktiv, artikli.cpor, '99' as cgrart, " +
    "'Reklamni materijal' as nazgrart, artikli.vc, artikli.mc " +
    "FROM artikli";
    if (ld.raLocate(shema, "POLJE", "PART")) {
      partq = shema.getString("SQLCONDITION");
    }

    System.out.println(partq);
    
    Connection dod = dM.getDataModule().getDodConnection();
    StorageDataSet part = null;
    if (dod != null) try {
      part = new StorageDataSet();
      part.setColumns(art.cloneColumns());
      part.open();
      ResultSet rs = dod.createStatement().executeQuery(partq);
      LoadingConversionRules lcr = new LoadingConversionRules(part, rs.getMetaData());
      while (rs.next()) lcr.fillRow(part, rs);
      rs.close();
      /*for (part.first(); part.inBounds(); part.next()) {
        art.insertRow(false);
        dM.copyColumns(part, art, cc);
        art.setInt("CART", part.getInt("CART") + 50000);
        art.setString("BC", art.getInt("CART") + "");
      }*/
    } catch (Exception e) {
      e.printStackTrace();
    }

    Connection crc = dM.getDataModule().getPodConnection();
    if (crc == null) return;
    
    try {    
      Statement d = crc.createStatement();
      d.executeUpdate("DELETE FROM Input_Kupac");
      d.close();
      
      PreparedStatement ps = crc.prepareStatement("INSERT INTO Input_Kupac(" +
          "TvrtkaSifra,Sifra,Naziv,NazivSearch,Mbr,PorezniObveznik," +
          "Adresa1,Adresa2,Oib,Kontakt,Napomena,Aktivan) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"); 
      
      int tot = 0, succ = 0;
      for (par.first(); par.inBounds(); par.next()) {
        ps.setString(1, tsif);
        ps.setString(2, par.getInt("CPAR")+"");
        ps.setString(3, par.getString("NAZPAR"));
        ps.setString(4, Aus.convertToAscii(par.getString("NAZPAR")));
        ps.setString(5, par.getString("MB"));
        ps.setBoolean(6, true);
        ps.setString(7, par.getString("ADR"));
        ps.setString(8, par.getInt("PBR") + " " + par.getString("MJ"));
        ps.setString(9, par.getString("OIB"));
        ps.setString(10, par.getString("TEL"));
        ps.setString(11, "");
        ps.setBoolean(12, true);
        ++tot;
        succ += ps.executeUpdate();
      }
      ps.close();
      System.out.println("Dodano "+succ+"/"+tot+" partnera");
      
      Statement dj = crc.createStatement();
      dj.executeUpdate("DELETE FROM Input_KupacLokacija");
      dj.close();
      
      PreparedStatement ls = crc.prepareStatement("INSERT INTO Input_KupacLokacija(" +
          "TvrtkaSifra,KupacSifra,Sifra,Naziv,NazivSearch," +
          "Adresa1,Adresa2,Aktivan,PutnikSifra,Kontakt,Napomena) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
      tot = succ = 0;
      for (pj.first(); pj.inBounds(); pj.next()) {
        ls.setString(1, tsif);
        ls.setString(2, pj.getInt("CPAR")+"");
        ls.setString(3, pj.getInt("PJ")+"");
        ls.setString(4, pj.getString("NAZPJ"));
        ls.setString(5, Aus.convertToAscii(pj.getString("NAZPJ")));
        ls.setString(6, pj.getString("ADRPJ"));
        ls.setString(7, pj.getInt("PBRPJ") + " " + pj.getString("MJPJ"));
        ls.setBoolean(8, true);
        ls.setString(9, "");
        ls.setString(10, pj.getString("TELPJ"));
        ls.setString(11, "");
        ++tot;
        succ += ls.executeUpdate();
      }
      System.out.println("Dodano "+succ+"/"+tot+" jedinica");
      
      tot = succ = 0;
      for (npj.first(); npj.inBounds(); npj.next()) {
        ls.setString(1, tsif);
        ls.setString(2, npj.getInt("CPAR")+"");
        ls.setString(3, npj.getInt("PJ")+"");
        ls.setString(4, npj.getString("NAZPJ"));
        ls.setString(5, Aus.convertToAscii(npj.getString("NAZPJ")));
        ls.setString(6, npj.getString("ADRPJ"));
        ls.setString(7, npj.getInt("PBRPJ") + " " + npj.getString("MJPJ"));
        ls.setBoolean(8, true);
        ls.setString(9, "");
        ls.setString(10, npj.getString("TELPJ"));
        ls.setString(11, "");
        ++tot;
        succ += ls.executeUpdate();
      }
      System.out.println("Dodano "+succ+"/"+tot+" default jedinica");
      
      Statement da = crc.createStatement();
      da.executeUpdate("DELETE FROM Input_Proizvod");
      da.close();
      da = crc.createStatement();
      da.executeUpdate("DELETE FROM Input_Cjenik");
      da.close();
      da = crc.createStatement();
      da.executeUpdate("DELETE FROM Input_CjenikStavka");
      da.close();

      PreparedStatement as = crc.prepareStatement("INSERT INTO Input_Proizvod(" +
          "Tvrtkasifra,ProizvodSifra,ProizvodNaziv,ProizvodNazivKratki,EAN,Privatan,IncrementStep," +
          "Aktivan,PorezVrstaSifra,PorezSifra,PorezPostotak,GrupaSifra,GrupaNaziv," +
          "GrupaSort,SortUkupno,SortGrupa,GrupaNazivNivo1,GrupaSortNivo1) " +
          "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      
      PreparedStatement cs = crc.prepareStatement("INSERT INTO Input_CjenikStavka(" +
          "TvrtkaSifra,CjenikNaziv,GrupaNaziv,GrupaSort,GrupaAktivna,ProizvodSifra," +
          "Sort,SortGrupa,OsnovicaCijena,UkupnaCijena,Rabat,StavkaAktivna) " +
          "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
      
      tot = succ = 0;
      for (int rep = 0; rep < 2; rep++) {
      for (art.first(); art.inBounds(); art.next()) {
        as.setString(1, tsif);
        if (rep == 0) {
          as.setString(2, art.getInt("CART")+"");
          as.setString(5, art.getString("BC"));
        } else {
          as.setString(2, (art.getInt("CART")+50000)+"");
          as.setString(5, (art.getInt("CART")+50000)+"");
        }
        as.setString(3, art.getString("NAZART"));
        as.setString(4, max(art.getString("NAZPRI"),30));
        as.setBoolean(6, false);
        as.setInt(7, 1);
        as.setBoolean(8, art.getString("AKTIV").equals("D"));
        as.setString(9, "PDV");
        if (art.getString("CPOR").equals("2")) {
          as.setString(10, "PDV0");
          as.setBigDecimal(11, Aus.zero2);
        } else {
          as.setString(10, "PDV25");
          as.setBigDecimal(11, new BigDecimal("25.00"));
        }
        as.setString(12, art.getString("CGRART"));
        as.setString(13, art.getString("NAZGRART"));
        as.setString(14, art.getString("CGRART"));
        as.setString(15, max(art.getString("NAZPRI"), 50));
        as.setString(16, max(art.getString("NAZPRI"), 50));
        as.setString(17, art.getString("NAZGRART"));
        as.setString(18, art.getString("CGRART"));
        ++tot;
        succ += as.executeUpdate();        
      
        cs.setString(1, tsif);
        cs.setString(2, "OSNOVNI");
        cs.setString(3, art.getString("NAZGRART"));
        cs.setString(4, art.getString("CGRART"));
        cs.setBoolean(5, true);
        if (rep==0)
          cs.setString(6, art.getInt("CART")+"");
        else 
          cs.setString(6, (art.getInt("CART")+50000)+"");
        cs.setString(7, max(art.getString("NAZPRI"), 50));
        cs.setString(8, max(art.getString("NAZPRI"), 50));
        if (art.getString("NAZPAK").equals("salespod")) {
          cs.setBigDecimal(9, art.getBigDecimal("VC").
              multiply(art.getBigDecimal("BRJED")).
              setScale(2, BigDecimal.ROUND_HALF_UP));
          cs.setBigDecimal(10, art.getBigDecimal("MC").
              multiply(art.getBigDecimal("BRJED")).
              setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
          cs.setBigDecimal(9, art.getBigDecimal("VC"));
          cs.setBigDecimal(10, art.getBigDecimal("MC"));
        }
        cs.setBigDecimal(11, Aus.zero2);
        cs.setBoolean(12, art.getString("AKTIV").equals("D"));
        ++tot;
        succ += cs.executeUpdate();
      }
      if (part == null) break; art = part;
      }
      System.out.println("Dodano "+succ+"/"+tot+" artikala i cjenika");
      
      PreparedStatement czs = crc.prepareStatement("INSERT INTO Input_Cjenik(" +
          "TvrtkaSifra,CjenikNaziv,DatumVrijemeAktivacije,Pretpostavljen," +
          "KoristiCijene,KoristiRabat,Rabat,OmoguciOdabir) " +
          "VALUES (?,?,?,?,?,?,?,?)");
      
      czs.setString(1, tsif);
      czs.setString(2, "OSNOVNI");
      czs.setTimestamp(3, Util.getUtil().findFirstDayOfYear());
      czs.setBoolean(4, true);
      czs.setBoolean(5, true);
      czs.setBoolean(6, false);
      czs.setBigDecimal(7, Aus.zero2);
      czs.setBoolean(8, true);

      System.out.println("Dodano "+czs.executeUpdate()+" zaglavlja cjenika");

      importDoc();

      if (comm.length() > 0)
        callSync();          

    } catch (SQLException e) {
      e.printStackTrace();
    }

    } finally {
      release();
    }
  }
  
  void callSync() {
    System.out.println("Calling sync... ");
    System.out.println(comm);
    Process proc;
    try {
      proc = Runtime.getRuntime().exec(comm);
      proc.waitFor();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  String max(String orig, int len) {
    if (orig.length() <= len) return orig;
    return orig.substring(0, len);
  }
  
  
  void importDoc() throws SQLException {
    String cskl = frmParam.getParam("sisfun", "salepodCskl", "",
      "Šifra skladišta za salepod narudžbe");
  
    String[] acc = {"CART", "CART1", "BC", "NAZART", "JM"};
    

    Connection crc = dM.getDataModule().getPodConnection();
    Statement mark = crc.createStatement();
    
    mark.executeUpdate("UPDATE Export_DokumentZag SET StatusPrijenosa=1" +
    		" WHERE StatusPrijenosa=0 and TvrtkaSifra='"+tsif+"'");
    mark.close();
    
    Set ids = new HashSet();
    List zags = new ArrayList();
    List stavs = new ArrayList();
    
    System.out.println("Zaglavlja:");
    Statement load = crc.createStatement();
    ResultSet zag = load.executeQuery("SELECT * FROM Export_DokumentZag WHERE StatusPrijenosa=1");
    while (zag.next()) {      
      ids.add(new Integer(zag.getInt("IDDokumentZag")));
      System.out.print(zag.getInt("IDDokumentZag"));
      System.out.print("#");
      System.out.print(zag.getString("TvrtkaSifra"));
      System.out.print("#");
      System.out.print(zag.getString("KupacSifra"));
      System.out.print("#");
      System.out.print(zag.getString("KupacLokacijaSifra"));
      System.out.print("#");
      System.out.print(zag.getString("KupacLokacijaDostaveSifra"));
      System.out.print("#");
      System.out.print(zag.getString("NacinPlacanjaSifra"));
      System.out.print("#");
      System.out.print(zag.getString("NacinDostaveNaziv"));
      System.out.print("#");
      System.out.print(zag.getString("CjenikNaziv"));
      System.out.print("#");      
      System.out.print(zag.getString("PutnikSifra"));
      System.out.print("#");
      System.out.print(zag.getString("VrstaTransakcijeSifra"));
      System.out.print("#");
      System.out.print(zag.getInt("KnjigGodina"));
      System.out.print("#");
      System.out.print(zag.getString("URBroj"));
      System.out.print("#");
      System.out.print(zag.getTimestamp("DatumDVO"));
      System.out.print("#");
      System.out.print(zag.getTimestamp("DatumValute"));
      System.out.print("#");
      System.out.print(zag.getTimestamp("DatumVrijemeDostave"));
      System.out.print("#");
      System.out.print(zag.getString("BrojNarudzbeKupca"));
      System.out.print("#");
      System.out.print(zag.getTimestamp("DatumVrijemeUnosa"));
      System.out.print("#");
      System.out.print(zag.getString("Napomena"));
      System.out.print("#");
      System.out.print(zag.getBoolean("Hitnost"));
      System.out.print("#");
      System.out.print(zag.getInt("ImportJobID"));
      System.out.print("#");
      System.out.print(zag.getInt("StatusPrijenosa"));
      System.out.print("#");
      System.out.print(zag.getTimestamp("DatumVrijemePrijenosa"));
      System.out.println();
      
      Zag z = new Zag();
      z.id = zag.getInt("IDDokumentZag");
      z.cpar = Aus.getNumber(zag.getString("KupacSifra"));
      z.pj = Aus.getNumber(zag.getString("KupacLokacijaSifra"));
      z.cagent = Aus.getNumber(zag.getString("PutnikSifra"));
      z.dvo = new Timestamp(zag.getTimestamp("DatumDVO").getTime());
      z.dosp = new Timestamp(zag.getTimestamp("DatumValute").getTime());
      z.nap = zag.getString("VrstaTransakcijeSifra") + ", " +
          zag.getString("NacinPlacanjaSifra") + "\n" + 
          zag.getString("Napomena");
      if (z.nap.length() > 200)
        z.nap = z.nap.substring(0, 200);
      zags.add(z);
    }
    load.close();
    
    System.out.println("Stavke:");
    Statement sload = crc.createStatement();
    ResultSet stav = sload.executeQuery("SELECT * FROM Export_DokumentStav WHERE "+
        Condition.in("DokumentZagID", ids.toArray()));
    while (stav.next()) {
      System.out.print(stav.getInt("IDDokumentStav"));
      System.out.print("#");
      System.out.print(stav.getInt("DokumentZagID"));
      System.out.print("#");
      System.out.print(stav.getInt("Rbr"));
      System.out.print("#");
      System.out.print(stav.getString("ProizvodSifra"));
      System.out.print("#");
      System.out.print(stav.getString("PakiranjeSifra"));
      System.out.print("#");
      System.out.print(stav.getInt("Kolicina"));
      System.out.print("#");
      System.out.print(stav.getBigDecimal("Cijena"));
      System.out.print("#");
      System.out.print(stav.getBigDecimal("Iznos"));
      System.out.print("#");
      System.out.print(stav.getBigDecimal("RabatPostotak"));
      System.out.print("#");
      System.out.print(stav.getBigDecimal("RabatIznos"));
      System.out.print("#");
      System.out.print(stav.getBigDecimal("OsnovicaIznos"));
      System.out.print("#");
      System.out.print(stav.getString("PorezSifra"));
      System.out.print("#");
      System.out.print(stav.getBigDecimal("PorezPostotak"));
      System.out.print("#");
      System.out.print(stav.getBigDecimal("PorezIznos"));
      System.out.print("#");
      System.out.print(stav.getBigDecimal("UkupnoIznos"));
      System.out.print("#");
      System.out.print(stav.getString("Napomena"));
      System.out.println();
      
      Stav s = new Stav();
      s.id = stav.getInt("DokumentZagID");
      s.rbr = stav.getInt("Rbr");
      s.cart = Aus.getNumber(stav.getString("ProizvodSifra"));
      s.kol = stav.getInt("Kolicina");
      s.fc = stav.getBigDecimal("Cijena");
      stavs.add(s);
    }
    sload.close();
    
    Statement deact = crc.createStatement();
    
    deact.executeUpdate("UPDATE Export_DokumentZag SET StatusPrijenosa=2" +
            " WHERE StatusPrijenosa=1 and TvrtkaSifra='"+tsif+"'");
    deact.close();
    
    
    for (int iz = 0; iz < zags.size(); iz++) {
      Zag z = (Zag) zags.get(iz);
      QueryDataSet dzg = doki.getDataModule().getTempSet(Condition.nil);
      QueryDataSet dst = stdoki.getDataModule().getTempSet(Condition.nil);
      
      dzg.open();
      dst.open();
      
      dzg.insertRow(false);
      dzg.setString("CUSER", "sistem");
      dzg.setString("CSKL", cskl);
      dzg.setString("VRDOK", "NKU");
      dzg.setInt("CPAR", z.cpar);
      if (z.pj > 0)
        dzg.setInt("PJ", z.pj);
      dzg.setInt("CAGENT", z.cagent);
      dzg.setTimestamp("DATDOK", z.dvo);
      dzg.setTimestamp("DVO", z.dvo);
      dzg.setTimestamp("DATDOSP", z.dosp);
      dzg.setString("GOD", hr.restart.util.Util.getUtil().getYear(z.dvo));
      dzg.setString("OPIS", z.nap); 
      for (int is = 0; is < stavs.size(); is++) {
        Stav s = (Stav) stavs.get(is);
        if (s.id == z.id) {
          dst.insertRow(false);
          dM.copyColumns(dzg, dst, Util.mkey);
          dst.setShort("RBR", (short) s.rbr);
          dst.setInt("RBSID", s.rbr);
          dst.setInt("CART", s.cart);
          dst.setBigDecimal("KOL", new BigDecimal(s.kol));
          dst.setBigDecimal("FC", s.fc);
          
          if (ld.raLocate(dM.getDataModule().getArtikli(), "CART", ""+s.cart)) {
            dM.copyColumns(dM.getDataModule().getArtikli(), dst, acc);
            dst.post();
            if (dM.getDataModule().getArtikli().getString("NAZPAK").equals("salespod")) {
              Aus.mul(dst, "KOL", dM.getDataModule().getArtikli(), "BRJED");
              Aus.div(dst, "FC", dM.getDataModule().getArtikli().getBigDecimal("BRJED"));
            }
          } 
        }
      }
      saveOrder(dzg, dst);
    }
    
    if (zags.size() > 0) {
      String users = frmParam.getParam("robno", "salepodNotify", "", "Popis korisnika za notifikaciju salepod");
      System.out.println("Users: " + users);
      String[] us = new VarStr(users).split();
      for (int i = 0; i < us.length; i++) {
        System.out.println("Sending to " + us[i]);
          MsgDispatcher.send("salespod", us[i], "Dohvaæeno " + zags.size() + " narudžbi sa salespod servera.");
      }
    }
  }
  
  private void saveOrder(final QueryDataSet zag, final QueryDataSet st) {
    new raLocalTransaction() {
      public boolean transaction() throws Exception {
        Util.getUtil().getBrojDokumenta(zag);
        for (st.first(); st.inBounds(); st.next())
          st.setInt("BRDOK", zag.getInt("BRDOK"));
        
        raTransaction.saveChanges(zag);
        raTransaction.saveChanges(st);
        return true;
      }
    }.execTransaction();
  }
  
  class Zag {
    int id, cpar, pj, cagent;
    Timestamp dvo, dosp;
    String nap;    
  }
  
  class Stav {
    int id, rbr, cart, kol;
    BigDecimal fc;
  }
  
  public static void main(String[] args) {
    
    MsgDispatcher.install(false);
    dM.getDataModule();
    
    int spod = Aus.getNumber(hr.restart.util.IntParam.getTag("salepod.delay"));
    if (spod > 0)
      raSalepodTrans.install(spod);
   
    try {
      Thread.currentThread().join();
    } catch (InterruptedException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }
}

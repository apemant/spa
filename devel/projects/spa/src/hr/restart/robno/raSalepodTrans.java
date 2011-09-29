package hr.restart.robno;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.borland.dx.dataset.DataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;


public class raSalepodTrans {
  
  String tsif;
  lookupData ld = lookupData.getlookupData();
  
  static boolean busy = false;
  
  public static synchronized boolean isBusy() {
    if (busy) return true;
    busy = true;
    return false;
  }
  
  public static synchronized void release() {
    busy = false;
  }

  public void exportData() {
    tsif = frmParam.getParam("sisfun", "salespodSifra", "39001",
        "Šifra tvrtke za salespod");
        
    dM.getDataModule().installPodConnection();
    
    if (!dM.getDataModule().isPod()) {
      return;
    }
    
    if (isBusy()) return;
    
    try {
      
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
    
    String artq = "SELECT artikli.cart, artikli.nazart, artikli.nazpri, " +
    "artikli.bc, artikli.aktiv, artikli.cpor, grupart.cgrart, grupart.nazgrart, stanje.vc, stanje.mc " +
    "FROM artikli, stanje, grupart WHERE artikli.cart = stanje.cart "+
    "AND artikli.cgrart = grupart.cgrart and stanje.god>='#GOD' and stanje.cskl='206'"; 
    if (ld.raLocate(shema, "POLJE", "ART")) {
      artq = shema.getString("SQLCONDITION");
    }
    artq = new VarStr(artq).replace("#GOD", god).toString();
    System.out.println(artq);
    
    DataSet art = Aus.q(artq);
    
    
    Connection crc = dM.getDataModule().getPodConnection();
    if (crc == null) return;
    
    try {    
      Statement d = crc.createStatement();
      d.executeUpdate("DELETE FROM Input_Kupac");
      d.close();
      
      PreparedStatement ps = crc.prepareStatement("INSERT INTO Input_Kupac(" +
          "TvrtkaSifra,Sifra,Naziv,NazivSearch,Mbr,PorezniObveznik," +
          "Adresa1,Adresa2,Oib,Kontakt,Napomena,Aktivan) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"); 
      
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
        System.out.println("Dodano "+ps.executeUpdate()+" partnera");
      }
      ps.close();
      
      Statement dj = crc.createStatement();
      dj.executeUpdate("DELETE FROM Input_KupacLokacija");
      dj.close();
      
      PreparedStatement ls = crc.prepareStatement("INSERT INTO Input_KupacLokacija(" +
          "TvrtkaSifra,KupacSifra,Sifra,Naziv,NazivSearch," +
          "Adresa1,Adresa2,Aktivan,PutnikSifra,Kontakt,Napomena) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
      
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
        
        System.out.println("Dodano "+ls.executeUpdate()+" jedinica");
      }
      
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
      
      for (art.first(); art.inBounds(); art.next()) {
        as.setString(1, tsif);
        as.setString(2, art.getInt("CART")+"");
        as.setString(3, art.getString("NAZART"));
        as.setString(4, max(art.getString("NAZPRI"),30));
        as.setString(5, art.getString("BC"));
        as.setBoolean(6, false);
        as.setInt(7, 1);
        as.setBoolean(8, art.getString("AKTIV").equals("D"));
        as.setString(9, "PDV");
        if (art.getString("CPOR").equals("2")) {
          as.setString(10, "PDV0");
          as.setBigDecimal(11, Aus.zero2);
        } else {
          as.setString(10, "PDV23");
          as.setBigDecimal(11, new BigDecimal("23.00"));
        }
        as.setString(12, art.getString("CGRART"));
        as.setString(13, art.getString("NAZGRART"));
        as.setString(14, art.getString("CGRART"));
        as.setString(15, art.getString("NAZPRI"));
        as.setString(16, art.getString("NAZPRI"));
        as.setString(17, art.getString("NAZGRART"));
        as.setString(18, art.getString("CGRART"));
        System.out.println("Dodano "+as.executeUpdate()+" artikala");
      
        cs.setString(1, tsif);
        cs.setString(2, "OSNOVNI");
        cs.setString(3, art.getString("NAZGRART"));
        cs.setString(4, art.getString("CGRART"));
        cs.setBoolean(5, true);
        cs.setString(6, art.getInt("CART")+"");
        cs.setString(7, art.getString("NAZPRI"));
        cs.setString(8, art.getString("NAZPRI"));
        cs.setBigDecimal(9, art.getBigDecimal("VC"));
        cs.setBigDecimal(10, art.getBigDecimal("MC"));
        cs.setBigDecimal(11, Aus.zero2);
        cs.setBoolean(12, art.getString("AKTIV").equals("D"));
        
        System.out.println("Dodano "+cs.executeUpdate()+" cjenika");
      }
      
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
      
    } catch (SQLException e) {
      e.printStackTrace();
    }
    
    } finally {
      release();
    }
  }
  
  String max(String orig, int len) {
    if (orig.length() <= len) return orig;
    return orig.substring(0, len);
  }
  
  
  void importDoc() throws SQLException {
    Connection crc = dM.getDataModule().getPodConnection();
    Statement mark = crc.createStatement();
    
    mark.executeUpdate("UPDATE Export_DokumentZag SET StatusPrijenosa=1" +
    		" WHERE StatusPrijenosa=0 and TvrtkaSifra='"+tsif+"'");
    mark.close();
    
    Set ids = new HashSet();
    
    System.out.println("Zaglavlja:");
    Statement load = crc.createStatement();
    ResultSet zag = load.executeQuery("SELECT * FROM Export_DokumentZag WHERE StatusPrijenosa=1");
    while (zag.next()) {
      ids.add(Integer.valueOf(zag.getInt("IDDokumentZag")));
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
    }
    load.close();
    
    System.out.println("Stavke:");
    Statement sload = crc.createStatement();
    ResultSet stav = sload.executeQuery("SELECT * FROM Export_DokumentStav WHERE "+
        Condition.in("DokumentZagID", ids.toArray()));
    while (stav.next()) {
      System.out.print(stav.getInt("IDDokumentStav"));
      System.out.print("#");
      System.out.print(stav.getInt("IDDokumentZag"));
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
    }
    sload.close();
    
    Statement deact = crc.createStatement();
    
    deact.executeUpdate("UPDATE Export_DokumentZag SET StatusPrijenosa=2" +
            " WHERE StatusPrijenosa=1 and TvrtkaSifra='"+tsif+"'");
    deact.close();
  }
}

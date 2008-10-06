/****license*****************************************************************
**   file: raTransaction.java
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

import hr.restart.baza.Refresher;

import java.sql.Connection;

/**
 *
 * <p>Title: raTransaction </p>
 * <p>Description: Niz statickih metoda koji handlaju transakcije</p>
 * <pre>
 * Evo nekoliko korisnih primjera za koristenje transakcija:
 *   Primjer 1: klasican komad bilo kakvog koda u transakciji
 *      hr.restart.util.raLocalTransaction mytransaction = new hr.restart.util.raLocalTransaction() {
 *          public boolean transaction() {
 *            dm.getAgenti().open();
 *            dm.getAgenti().insertRow(true);
 *            dm.getAgenti().setInt("CAGENT",7);
 *          //dm.getAgenti().saveChanges(); ne valja jer automatski commita transakciju ma sto mu rekao
 *            saveChanges(dm.getAgenti());
 *            //koristiti metodu raTransaction.runSQL(String) umjesto Valid.runSQL(String) u transakciji jer baca exception
 *            raTransaction.runSQL("UPDATE PARTNERI WHERE DI = 'V' SET CAGENT = 7");
 *            dm.getAgenti().insertRow(true);
 *            dm.getAgenti().setInt("CAGENT",1);
 *            saveChanges(dm.getAgenti());
 *            return true;
 *          }
 *        };
 *        mytransaction.execTransaction();
 *
 *   Primjer 2: Kada se u transakciji treba izvesti niz SQL komandi
 *        raTransaction.getLocalTransaction(new String[] {
 *            "DELETE FROM Agenti where cagent in (7,8)"
 *            ,"INSERT INTO Agenti (CAGENT) values(7)"
 *            ,"INSERT INTO AGENTI (CAGENT) values(8)"
 *            ,"UPDATE PARTNERI WHERE DI = 'V' SET CAGENT = 7"
 *            ,"INSERT INTO AGENTI (CAGENT) values(1)"//kaboom!!!
 *            }).execTransaction();
 *
 *   Primjer 3: Koristenje prepared statementa u transakciji
 *        final java.sql.PreparedStatement delag = raTransaction.getPreparedStatement("DELETE FROM Agenti where cagent in (?,?)");
 *        final java.sql.PreparedStatement insag = raTransaction.getPreparedStatement("INSERT INTO Agenti (CAGENT) values(?)");
 *        new raLocalTransaction() {
 *          public boolean transaction() throws Exception {
 *            delag.setInt(1,7);//zamjenjuje prvi upitnik u zadanom query stringu sa integerom 7
 *            delag.setInt(2,8);//zamjenjuje drugi upitnik u zadanom query stringu sa integerom 8
 *            delag.execute();  //izvrsava prepared statement
 *            insag.setInt(1,7);
 *            insag.execute();
 *            insag.setInt(1,8);
 *            insag.execute();
 *            insag.setInt(1,1);
 *            insag.execute();
 *            return true;
 *          }
 *        }.execTransaction();
 *        //ili ako treba jednostavno izvrsiti niz prepared statementa u transakciji
 *        //dovoljno ih je pripremiti i izvrsiti na ovaj nacin
 *        try {
 *          delag.setInt(1,7);
 *          delag.setInt(2,8);
 *          insag.setInt(1,8);
 *        } catch (Exception ex) {}
 *        //-- izvrsava DELETE FROM AGENTI WHERE CAGENT IN (7,8) i INSERT INTO AGENTI (CAGENT) VALUES(8)
 *        raTransaction.getLocalTransaction(new java.sql.PreparedStatement[] {delag,insag}).execTransaction();
 *
 *   Primjer 4: Modificirali smo podatke u nekoliko datasetova i trebaa ih snimiti u transakciji
 *        dm.getAgenti().open();
 *        dm.getAgenti().insertRow(true);
 *        dm.getAgenti().setInt("CAGENT",7);
 *
 *        dm.getBanke().open();
 *        dm.getBanke().insertRow(true);
 *        dm.getBanke().setString("CBANKA","PBZ");
 *        dm.getBanke().setString("NAZIV","Privredna banka");
 *
 *        dm.getBankepl().open();
 *        dm.getBankepl().insertRow(true);
 *        dm.getBankepl().setInt("CBANKE",3);
 *        dm.getBankepl().setString("NAZBANKE","Probna banka");
 *
 *        raTransaction.saveChangesInTransaction(new com.borland.dx.sql.dataset.QueryDataSet[] {dm.getAgenti(),dm.getBanke(),dm.getBankepl()});
 *
 * </pre>
 */
public abstract class raTransaction {
///transaction start
  /**
   * Pokrece transakciju na defaultnoj bazi (hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection())
   * vidi startTransaction(Connection)
   * @return true ako je uspjelo, false ako nije
   */
  public static boolean startTransaction() {
    return startTransaction(hr.restart.baza.dM.getDataModule().getDatabase1());
  }
  /**
   * Starta transakciju (con.setAutoCommit(false))
   * @param con konekcija (baza) na kojoj se izvodi transakcija
   * @return true ako je uspjelo, false ako nije
   */
  public static boolean startTransaction(Connection con) {
    try {
      con.setAutoCommit(false);
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
  /**
   * starta transakciju na vise konekcija istovremeno
   * @param cons konekcije
   * @return true ako je uspio, false ako nije
   */
  public static boolean startTransaction(Connection[] cons) {
    for (int i = 0; i < cons.length; i++) {
      if (!startTransaction(cons[i])) return false;
    }
    return true;
  }

/**
 * Starta transakciju na borl. bazi (db.setAutoCommit(false))
 * @param db baza (com.borland.dx.sql.dataset.Database) na kojoj se izvodi transakcija
 * @return true ako je uspjelo, false ako nije
 */
public static boolean startTransaction(com.borland.dx.sql.dataset.Database db) {
  try {
//    db.getJdbcConnection().setAutoCommit(false);
//System.out.println("db.setAutoCommit(false)");
    db.setAutoCommit(false);
    return true;
  }
  catch (Exception ex) {
    ex.printStackTrace();
    return false;
  }
}
/**
 * starta transakciju na vise baza istovremeno
 * @param dbs borlandovi databasovi (com.borland.dx.sql.dataset.Database)
 * @return true ako je uspio, false ako nije
 */
public static boolean startTransaction(com.borland.dx.sql.dataset.Database[] dbs) {
  for (int i = 0; i < dbs.length; i++) {
    if (!startTransaction(dbs[i])) return false;
  }
  return true;
}

//// transaction commit
/**
 * Commita (potvrdjuje) transakciju na defaultnoj bazi
 * (commitTransaction(hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection()))
 * @return true ako je uspjelo, false ako nije
 */
  public static boolean commitTransaction() {
    return commitTransaction(hr.restart.baza.dM.getDataModule().getDatabase1());
  }

  /**
   * Commita (potvrdjuje) transakciju na zadanoj konekciji.
   * Ako je na konekciji ukljucen autocommit
   * odnosno nije startana transakcija vraca false i ne radi nista
   * @param con konekcija (baza) na kojoj se izvodi transakcija
   * @return true ako je uspjelo, false ako nije
   */
  public static boolean commitTransaction(Connection con) {
    try {
      if (con.getAutoCommit()) throw new Exception("con.getAutoCommit() = "+con.getAutoCommit());
//      if (con.getAutoCommit()) return false;
      con.commit();
      con.setAutoCommit(true);
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  /**
   * commita transakciju na vise konekcija istovremeno
   * @param cons konekcije
   * @return true ako je uspio, false ako nije
   */
  public static boolean commitTransaction(Connection[] cons) {
    for (int i = 0; i < cons.length; i++) {
      if (!commitTransaction(cons[i])) return false;
    }
    return true;
  }
///
/**
 * Commita (potvrdjuje) transakciju na zadanoj bazi (com.borland.dx.sql.dataset.Database).
 * Ako je na bazi ukljucen autocommit
 * odnosno nije startana transakcija vraca false i ne radi nista
 * @param db baza (com.borland.dx.sql.dataset.Database) na kojoj se izvodi transakcija
 * @return true ako je uspjelo, false ako nije
 */
public static boolean commitTransaction(com.borland.dx.sql.dataset.Database db) {
  try {
    if (db.getAutoCommit()) throw new Exception("db.getAutoCommit() = "+db.getAutoCommit());
//    db.getJdbcConnection().commit();
//    db.getJdbcConnection().setAutoCommit(true);
    db.commit();
    db.setAutoCommit(true);
    return true;
  }
  catch (Exception ex) {
    ex.printStackTrace();
    return false;
  }
}

/**
 * commita transakciju na vise baza istovremeno
 * @param dbs baze
 * @return true ako je uspio, false ako nije
 */
public static boolean commitTransaction(com.borland.dx.sql.dataset.Database[] dbs) {
  for (int i = 0; i < dbs.length; i++) {
    if (!commitTransaction(dbs[i])) return false;
  }
  return true;
}

////transaction rollback

  /**
   * Rollbacka transakciju (vraca stanje podataka kakvo je bilo prije startTransaction)
   * na defaultnoj bazi
   * (rollbackTransaction(hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection()))
   * @return true ako je uspjelo, false ako nije
   */
  public static boolean rollbackTransaction() {
    return rollbackTransaction(hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection());
  }

  /**
   * Rollbacka transakciju (vraca stanje podataka kakvo je bilo prije startTransaction)
   * na zadanoj konekciji. Ako je na konekciji ukljucen autocommit
   * odnosno nije startana transakcija vraca false i ne radi nista
   * @param con konekcija (baza) na kojoj se izvodi transakcija
   * @return true ako je uspjelo, false ako nije
   */
  public static boolean rollbackTransaction(Connection con) {
    try {
      if (con.getAutoCommit()) {
//        System.out.println("con.getAutoCommit() is true! Returning false");
        return false;
      }
      con.rollback();
      con.setAutoCommit(true);
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  /**
   * rollbacka transakciju na vise konekcija istovremeno
   * @param cons konekcije
   * @return true ako je uspio, false ako nije
   */
  public static boolean rollbackTransaction(Connection[] cons) {
    for (int i = 0; i < cons.length; i++) {
      if (!rollbackTransaction(cons[i])) return false;
    }
    return true;
  }
////
  /**
   * Rollbacka transakciju (vraca stanje podataka kakvo je bilo prije startTransaction)
   * na zadanoj bazi. Ako je na bazi ukljucen autocommit
   * odnosno nije startana transakcija vraca false i ne radi nista
   * @param db baza na kojoj se izvodi transakcija
   * @return true ako je uspjelo, false ako nije
   */
  public static boolean rollbackTransaction(com.borland.dx.sql.dataset.Database db) {
    try {
      if (db.getAutoCommit()) {
//        System.out.println("db.getAutoCommit() is true! Returning false");
        return false;
      }
//      db.getJdbcConnection().rollback();
//      db.getJdbcConnection().setAutoCommit(true);
      db.rollback();
      db.setAutoCommit(true);
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  /**
   * rollbacka transakciju na vise baza istovremeno
   * @param dbs baze (com.borland.dx.sql.dataset.Database)
   * @return true ako je uspio, false ako nije
   */
  public static boolean rollbackTransaction(com.borland.dx.sql.dataset.Database[] dbs) {
    for (int i = 0; i < dbs.length; i++) {
//System.out.println("RBTBDB: Trying to rollback transaction od bdb "+dbs[i]);
      if (!rollbackTransaction(dbs[i])) return false;
    }
    return true;
  }

  /**
   * Snima dataset bez da commita transakciju.
   * To je, nazalost, jedini nacin da se snimi QueryDataset bez da odmah commita transakciju
   * @param ds Querydataset koji se snima
   */
  public static void saveChanges(com.borland.dx.sql.dataset.QueryDataSet ds) {
    if (ds.saveChangesSupported()) {       
      Refresher.postpone();
      if (ds instanceof hr.restart.baza.raDataSet) {
        hr.restart.baza.raDataSet rds = (hr.restart.baza.raDataSet) ds;
        rds.enableSync(false);
        try {
          rds.getDatabase().saveChanges(new com.borland.dx.dataset.DataSet[] {rds},false);
        } finally {
        //rds.propagateChanges();
          rds.enableSync(true);
        }
      } else ds.getDatabase().saveChanges(new com.borland.dx.dataset.DataSet[] {ds},false);
    }
  }

  /**
   * Snima zadane QueryDatasetove u transakciji.
   * @param qdses zadani querydatasetovi koje treba snimiti u transakciji
   * @return true ako je sve proslo, false ako se pojavila greska i nista nije snimljeno
   */
  public static boolean saveChangesInTransaction(final com.borland.dx.sql.dataset.QueryDataSet[] qdses) {
    com.borland.dx.sql.dataset.Database[] dbs = new com.borland.dx.sql.dataset.Database[qdses.length];
    for (int i = 0; i < dbs.length; i++) {
      dbs[i] = qdses[i].getDatabase();
    }
    return new raLocalTransaction(dbs) {
      public boolean transaction() {
        for (int i = 0; i < qdses.length; i++) {
          this.saveChanges(qdses[i]);
        }
        return true;
      }
    }.execTransaction();
  }
/////////
  ////
  ////
////////
  /**
   * @param querys niz queryja koje treba izvrsiti u transakciji
   * @param conns niz konekcija nad kojima treba izvrsiti queryje
   * @return raLocalTransaction kod kojeg je samo potrebno pozvati metodu execTransaction() da bi izvrsio zadane queryje na zadanim konekcijama
   */
  public static raLocalTransaction getLocalTransaction(final String[] querys, final Connection[] conns) {
    if (querys.length!=conns.length) throw new IllegalArgumentException("Broj querya i broj connectiona mora biti isti.");
    return new raLocalTransaction(conns) {
      public boolean transaction() throws Exception {
        for (int i = 0; i < querys.length; i++) {
          runSQL(querys[i], conns[i]);
        }
        return true;
      }
    };
  }

  /**
   * @param querys niz queryja koje treba izvrsiti u transakciji
   * @param conn konekcija nad kojom treba izvrsiti queryje
   * @return raLocalTransaction kod kojeg je samo potrebno pozvati metodu execTransaction() da bi izvrsio zadane queryje na zadanim konekcijama
   */
  public static raLocalTransaction getLocalTransaction(String[] querys, Connection conn) {
    Connection[] conns = new Connection[querys.length];
    for (int i = 0; i < conns.length; i++) {
      conns[i] = conn;
    }
    return getLocalTransaction(querys,conns);
  }

  /**
   * @param querys niz queryja koje treba izvrsiti u transakciji
   * @param dbs niz baza nad kojima treba izvrsiti queryje
   * @return raLocalTransaction kod kojeg je samo potrebno pozvati metodu execTransaction() da bi izvrsio zadane queryje na zadanim konekcijama
   */
  public static raLocalTransaction getLocalTransaction(String[] querys, com.borland.dx.sql.dataset.Database[] dbs) {
    Connection[] conns = new Connection[dbs.length];
    for (int i = 0; i < dbs.length; i++) {
      conns[i] = dbs[i].getJdbcConnection();
    }
    return getLocalTransaction(querys,conns);
  }

  /**
   * @param querys niz queryja koje treba izvrsiti u transakciji
   * @param db baza nad kojom treba izvrsiti queryje
   * @return raLocalTransaction kod kojeg je samo potrebno pozvati metodu execTransaction() da bi izvrsio zadane queryje na zadanim konekcijama
   */
  public static raLocalTransaction getLocalTransaction(String[] querys, com.borland.dx.sql.dataset.Database db) {
    return getLocalTransaction(querys,db.getJdbcConnection());
  }

  /**
   * primjer: raTransaction.getLocalTransaction(new String[] {"INSERT INTO Agenti (CAGENT) values(7)","INSERT INTO AGENTI (CAGENT) values(8)"}).execTransaction();
   * @param querys niz queryja koje treba izvrsiti u transakciji
   * @return raLocalTransaction kod kojeg je samo potrebno pozvati metodu execTransaction() da bi izvrsio zadane queryje na zadanim konekcijama
   */
  public static raLocalTransaction getLocalTransaction(String[] querys) {
    return getLocalTransaction(querys,hr.restart.baza.dM.getDataModule().getDatabase1());
  }

/**
 * @param pstatements niz prepared statementa koje treba izvrsiti u transakciji metodom execute
 * @return raLocalTransaction kod kojeg je samo potrebno pozvati metodu execTransaction() da bi izvrsio zadane queryje na zadanim konekcijama
 */
public static raLocalTransaction getLocalTransaction(final java.sql.PreparedStatement[] pstatements) {
  Connection[] conns = new Connection[pstatements.length];
  for (int i = 0; i < pstatements.length; i++) {
    try {
      conns[i] = pstatements[i].getConnection();
    }
    catch (Exception ex) {
      conns[i] = null;
    }
  }
  return new raLocalTransaction(conns) {
    public boolean transaction() throws Exception {
      for (int i = 0; i < pstatements.length; i++) {
        pstatements[i].execute();
      }
      return true;
    }
  };
}

  /**
   * Izvrsava INSERT, UPDATE, DELETE ili DDL query na zadanoj bazi i vraca broj afektiranih slogova
   * @param query string query koji se treba izvrsiti na zadanoj bazi
   * @param db zadana baza
   * @return broj slogova afektiranim queryjem (valjda)
   * @throws Exception uglavnom SQLException ako je query krivo napisan ili je nesto poslo po zlu
   */
  public static int runSQL(String query, com.borland.dx.sql.dataset.Database db) throws Exception {
//    db.executeStatement(query);
    Refresher.postpone();
    return db.getJdbcConnection().createStatement().executeUpdate(query);
  }

  /**
   * Izvrsava INSERT, UPDATE, DELETE ili DDL query na zadanoj konekciji i vraca broj afektiranih slogova
   * @param query string query koji se treba izvrsiti na zadanoj konekciji
   * @param con zadana konekcija
   * @return broj slogova afektiranim queryjem
   * @throws Exception uglavnom SQLException ako je query krivo napisan ili je nesto poslo po zlu
   */
  public static int runSQL(String query, java.sql.Connection con) throws Exception {
    Refresher.postpone();
    return con.createStatement().executeUpdate(query);
  }

  /**
   * Izvrsava INSERT, UPDATE, DELETE ili DDL query na defaultnoj bazii i vraca broj afektiranih slogova
   * @param query string query koji se treba izvrsiti na zadanoj bazi
   * @return broj slogova afektiranim queryjem
   * @throws Exception uglavnom SQLException ako je query krivo napisan ili je nesto poslo po zlu
   */
  public static int runSQL(String query) throws Exception {
    return runSQL(query,hr.restart.baza.dM.getDataModule().getDatabase1());
  }

  /**
   * Vraca java.sql.PreparedStatement sa zadane konekcije
   * @param query string koji moze izgledati ovako UPDATE AGENTI WHERE CAGENT = ? SET NAZAGENT = ?
   * @param con konekcija na kojoj bi se trebao izvrsiti PreparedStatement
   * @return java.sql.PreparedStatement kojem poslije mozemo substituirati upitnike
   * sa java.sql.PreparedStatement.setInt(1,5) ili java.sql.PreparedStatement.setString(2,"Tajni agent 007")
   * i u datom trenutku izvrsiti ga sa java.sql.PreparedStatement.execute()
   */
  public static java.sql.PreparedStatement getPreparedStatement(String query, Connection con) {
    try {
      return con.prepareStatement(query);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * Vraca java.sql.PreparedStatement sa zadane baze
   * @param query string koji moze izgledati ovako UPDATE AGENTI WHERE CAGENT = ? SET NAZAGENT = ?
   * @param db baza na kojoj bi se trebao izvrsiti PreparedStatement
   * @return java.sql.PreparedStatement kojem poslije mozemo substituirati upitnike
   * sa java.sql.PreparedStatement.setInt(1,5) ili java.sql.PreparedStatement.setString(2,"Tajni agent 007")
   * i u datom trenutku izvrsiti ga sa java.sql.PreparedStatement.execute()
   */
  public static java.sql.PreparedStatement getPreparedStatement(String query, com.borland.dx.sql.dataset.Database db) {
    return getPreparedStatement(query,db.getJdbcConnection());
  }

  /**
   * Vraca java.sql.PreparedStatement sa defaultne baze
   * @param query string koji moze izgledati ovako UPDATE AGENTI WHERE CAGENT = ? SET NAZAGENT = ?
   * @return java.sql.PreparedStatement kojem poslije mozemo substituirati upitnike
   * sa java.sql.PreparedStatement.setInt(1,5) ili java.sql.PreparedStatement.setString(2,"Tajni agent 007")
   * i u datom trenutku izvrsiti ga sa java.sql.PreparedStatement.execute()
   */
  public static java.sql.PreparedStatement getPreparedStatement(String query) {
    return getPreparedStatement(query,hr.restart.baza.dM.getDataModule().getDatabase1());
  }

  public static void main(String[] args) {
    System.out.println("TransactionIsolation = "+hr.restart.baza.dM.getDataModule().getDatabase1().getTransactionIsolation());

    System.out.println("TRANSACTION_NONE "+java.sql.Connection.TRANSACTION_NONE);
    System.out.println("TRANSACTION_READ_COMMITTED "+java.sql.Connection.TRANSACTION_READ_COMMITTED);
    System.out.println("TRANSACTION_READ_UNCOMMITTED "+java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
    System.out.println("TRANSACTION_REPEATABLE_READ "+java.sql.Connection.TRANSACTION_REPEATABLE_READ);
    System.out.println("TRANSACTION_SERIALIZABLE "+java.sql.Connection.TRANSACTION_SERIALIZABLE);
  }
}
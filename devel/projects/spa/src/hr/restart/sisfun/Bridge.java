/****license*****************************************************************
**   file: Bridge.java
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

package hr.restart.sisfun;

import hr.restart.baza.KreirDrop;
import hr.restart.baza.Orgpl;
import hr.restart.baza.Pos;
import hr.restart.baza.Stpos;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.db.raPreparedStatement;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;


public class Bridge implements Server.Service {
  
  public static int PORT = 2712;
  public static String ENC = "UTF-8";
  
  public void serve(InputStream in, OutputStream out) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(in, ENC));
    PrintWriter pw = new PrintWriter(new BufferedWriter(
        new OutputStreamWriter(out, ENC)), true);
    pw.println("READY");
    
    while (true) {
      String line = br.readLine();
      if (line == null || line.equals("END")) break;
      if (line.startsWith("GET ")) getTable(line.substring(4), pw);
      else if (line.startsWith("SELECT ")) query(line, pw);
      else if (line.startsWith("PUT ")) store(line.substring(4), br, pw);
      else if (line.equals("ADD ORDER")) addOrder(br, pw);
      else pw.println("UNKNOWN COMMAND: " + line);
    }
    System.out.println("ending connection");
    out.close();
    in.close();
  }
  
  void query(String q, PrintWriter out) {
    try {
      QueryDataSet ds = Aus.q(q);
      VarStr f = new VarStr();
      for (int i = 0; i < ds.columnCount(); i++)
        if (ds.getColumn(i).getSqlType() != java.sql.Types.NULL)
          f.append(ds.getColumn(i).getColumnName().toLowerCase()).append(",");
      f.chop();
      
      out.println("BEGIN " + f.toString());
      System.out.println("sending table: " + ds);
      int chars = 0;
      for (ds.first(); ds.inBounds(); ds.next()) {
        String line = KreirDrop.getFieldsLine(ds, "#");
        out.println(line);
        chars += line.length();
      }
      out.println("END");
      System.out.println("sent cca " + chars + " characters");
    } catch (Exception e) {
      out.println("ERROR: " + e);
      e.printStackTrace();
    }
  }
  
  void fillData(BufferedReader in, String[] cols, 
      KreirDrop kd, QueryDataSet dest) throws IOException {
    String line;
    Variant v = new Variant();
    while ((line = in.readLine()) != null) {
      if (line.equals("END")) break;
      dest.insertRow(false);
      String[] parts = new VarStr(line).split('#');
      for (int i = 0; i < cols.length; i++) {
        String val = new VarStr(parts[i]).trim().
           replaceAll("\\n", "\n").replaceAll("</sep>", "#").toString();
        if (val.length() > 0) {
          v.setFromString(kd.getColumn(cols[i]).getDataType(), val);
          dest.setVariant(cols[i], v);
        }
      }
    }
  }
  
  void store(String table, BufferedReader in, PrintWriter out) 
      throws IOException {
    final KreirDrop kd = KreirDrop.getModuleByName(table);
    if (kd == null) {
      out.println("UNKNOWN TABLE: " + table);
      return;
    }
    String line = in.readLine();
    if (!line.startsWith("BEGIN ")) {
      out.println("BEGIN expected");
      return;
    }
    final QueryDataSet row = kd.getTempSet("1=0");
    row.open();
    
    String[] cols = new VarStr(line.substring(6)).splitTrimmed(',');
    fillData(in, cols, kd, row);

    raLocalTransaction trans = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        raPreparedStatement add = 
          new raPreparedStatement(row, raPreparedStatement.INSERT);
        raPreparedStatement update = 
          new raPreparedStatement(row, raPreparedStatement.UPDATE);
        
        for (row.first(); row.inBounds(); row.next()) {
          update.setKeys(row);
          update.setValues(row);
          if (update.executeUpdate() < 1) {
            add.setValues(row);
            add.executeUpdate();
          }
        }
        return true;
      }
    };
    if (trans.execTransaction())
      out.println("OVER");
    else out.println("ERROR");
  }
  
  void addOrder(BufferedReader in, PrintWriter out) throws IOException {
    KreirDrop kdz = Pos.getDataModule();
    KreirDrop kds = Stpos.getDataModule();
    final QueryDataSet zag = kdz.getTempSet("1=0");
    zag.open();
    final QueryDataSet stav = kds.getTempSet("1=0");
    stav.open();
    
    String line = in.readLine();
    if (!line.startsWith("BEGIN ")) {
      out.println("BEGIN expected");
      return;
    }
    
    String[] cols = new VarStr(line.substring(6)).splitTrimmed(',');
    fillData(in, cols, kdz, zag);
    if (zag.rowCount() != 1) {
      out.println("ERROR - only one order expected");
      return;
    }
    if (zag.getString("CSKL").length() == 0)
      zag.setString("CSKL", OrgStr.getKNJCORG(false));
    if (zag.getString("VRDOK").length() == 0)
      zag.setString("VRDOK", "NAR");
    if (zag.getString("CPRODMJ").length() == 0)
      zag.setString("CPRODMJ", "000");
    
    line = in.readLine();
    if (!line.startsWith("BEGIN ")) {
      out.println("BEGIN expected");
      return;
    }
    cols = new VarStr(line.substring(6)).splitTrimmed(',');
    fillData(in, cols, kds, stav);
    raLocalTransaction trans = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        hr.restart.robno.Util.getUtil().getBrojDokumenta(zag);
        BigDecimal uk = Aus.zero2;
        for (stav.first(); stav.inBounds(); stav.next()) {
          stav.setInt("BRDOK", zag.getInt("BRDOK"));
          stav.setString("CSKL", zag.getString("CSKL"));
          stav.setString("GOD", zag.getString("GOD"));
          stav.setString("VRDOK", zag.getString("VRDOK"));
          stav.setString("CPRODMJ", zag.getString("CPRODMJ"));
          uk = uk.add(stav.getBigDecimal("UKUPNO"));
        }
        zag.setBigDecimal("UKUPNO", uk);
        raTransaction.saveChanges(zag);
        raTransaction.saveChanges(stav);
        return true;
      }
    };
    if (trans.execTransaction())
      out.println("OVER "+zag.getInt("BRDOK"));
    else out.println("ERROR");
  }
  
  void getTable(String table, PrintWriter out) {
    KreirDrop kd = KreirDrop.getModuleByName(table);
    if (kd == null) {
      out.println("UNKNOWN TABLE: " + table);
      return;
    }
    StorageDataSet ds = kd.getReadonlySet();
    Util.fillAsyncData(ds, kd.getQueryDataSet().getOriginalQueryString());
    
    out.println("BEGIN " + VarStr.join(kd.colnames, ","));
    System.out.println("sending table: " + table);
    int chars = 0;
    for (ds.first(); ds.inBounds(); ds.next()) {
      String line = KreirDrop.getFieldsLine(ds, "#");
      out.println(line);
      chars += line.length();
    }

    out.println("END");
    System.out.println("sent cca " + chars + " characters");
  }
  
  public static void test() {
    try {
      Socket sock = new Socket("localhost", PORT);
      BufferedReader br = new BufferedReader(
          new InputStreamReader(sock.getInputStream(), ENC));
      PrintWriter pw = new PrintWriter(new BufferedWriter(
          new OutputStreamWriter(sock.getOutputStream(), ENC)), true);

      String ack = br.readLine();
      if (ack == null || !ack.equals("READY"))
        throw new Exception("Server not ready: " + ack);
      
      pw.println("GET artikli");
      String ret = br.readLine();
      if (ret == null || !ret.startsWith("BEGIN"))
        throw new Exception("Server not ready: " + ret);
      
      do {
        System.out.println(ret);
        ret = br.readLine();
      } while (ret != null && !ret.equals("END"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void start() {
    dM.getDataModule().loadModules();
    try {
      new Server(System.out, 5).addService(new Bridge(), Bridge.PORT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

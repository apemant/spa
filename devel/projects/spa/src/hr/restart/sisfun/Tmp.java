package hr.restart.sisfun;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.KreirDrop;
import hr.restart.baza.Shkonta;
import hr.restart.baza.dM;
import hr.restart.util.VarStr;


public class Tmp {
  
  static class Change {
    String oldq, newq, changes;
    public Change(String old) {
      oldq = old;
      changes = "";
    }
    public String toString() {
      if (newq == null) return oldq;
      return oldq + "\n->\n" + newq + "\nchanges:\n" + changes;
    }
  }
  
  static class Range {
    int beg, end;
    public Range(int b, int e) {
      beg = b;
      end = e;
    }
  }
  
  public static void updateSheme() {
    updateSheme(0);
  }
  
  public static void updateSheme(int mode) {
    dM.getDataModule().loadModules();
    
    QueryDataSet ds = Shkonta.getDataModule().getTempSet();
    ds.open();
    VarStr buf = new VarStr();
    VarStr tmp = new VarStr();
    ArrayList bad = new ArrayList();
    ArrayList non = new ArrayList();
    ArrayList mod = new ArrayList();
    for (ds.first(); ds.inBounds(); ds.next()) {
      boolean change = false;
      Change ch = new Change(ds.getString("SQLCONDITION"));
      if (ch.oldq.length() < 5) continue;
      buf.clear().append(ch.oldq);
      int sel = buf.indexOfIgnoreCase("select ");
      int from = buf.indexOfIgnoreCase(" from ");
      int where = buf.indexOfIgnoreCase(" where ");
      int gb = buf.indexOfIgnoreCase(" group by ");
      int ob = buf.indexOfIgnoreCase(" order by ");
      if (ob > 0) buf.truncate(ob);
      String[] cols = tmp.clear().append(buf.mid(sel + 7, from)).splitTrimmed(',');
      String[] tables = tmp.clear().append(buf.mid(from + 6, where).toLowerCase()).splitTrimmed(',');
      String[] groups = (gb < 0) ? null : tmp.clear().
            append(buf.from(gb + 10).toLowerCase()).splitTrimmed(',');
      String middle = gb > 0 ? buf.mid(where + 7, gb) : buf.from(where + 7);
      if (groups != null) {
        for (int i = 0; i < groups.length; i++) {
          if (groups[i].indexOf('.') < 0) {
            String tab = findTable(groups[i], tables);
            if (tab == null) {
              bad.add(ch);
              continue;
            }
            ch.changes += "group by: " + groups[i] + " -> ";
            groups[i] = tab + "." + groups[i];
            ch.changes += groups[i] + "\n";
            change = true;
          }
        }
      }
      
      for (int i = 0; i < cols.length; i++) {
        String col = cols[i].toLowerCase();
        int as = col.lastIndexOf(" as ");
        if (as > 0) col = col.substring(0, as);
        if (groups != null && col.indexOf("max") >= 0) {
          boolean gr = false;
          for (int g = 0; g < groups.length; g++) {
            if (col.indexOf(groups[g]) >= 0 ||
                col.indexOf(groups[g].substring(
                    groups[g].indexOf('.') + 1)) >= 0) {
              ch.changes += "grouped: " + cols[i] + " -> " + groups[g] + "\n";
              cols[i] = groups[g];
              change = gr = true;
              break;
            }
          }
          if (gr) continue;
        }
        boolean ink = false;
        int beg = 0;
        ArrayList ids = new ArrayList();
        for (int c = 0; c < col.length(); c++) {
          if (!ink && Character.isLetter(col.charAt(c))) {
            beg = c;
            ink = true;
          } else if (ink && col.charAt(c) != '.' && 
              !Character.isLetterOrDigit(col.charAt(c))) {
            ids.add(new Range(beg, c));
            ink = false;
          }
        }
        if (ink) ids.add(new Range(beg, col.length()));
        tmp.clear().append(cols[i]);
        for (int j = ids.size() - 1; j >= 0; j--) {
          Range r = (Range) ids.get(j);
          String id = col.substring(r.beg, r.end);
          if (id.indexOf('.') < 0) {
            for (int t = 0; t < tables.length; t++) {
              KreirDrop kd = KreirDrop.getModuleByName(tables[t]);
              for (int c = 0; c < kd.getColumns().length; c++) {
                if (kd.getColumns()[c].getColumnName().equalsIgnoreCase(id)) {
                  id = tables[t] + "." + id;
                  break;
                }
              }
              if (id.indexOf('.') > 0) {
                ch.changes += "qualify: " + tmp.mid(r.beg, r.end) + " -> " + id + "\n";
                tmp.replace(r.beg, r.end, id);
                cols[i] = tmp.toString();
                change = true;
                break;
              }
            }
          }          
        }
        
      }
      if (change) {
        buf.clear().append("SELECT ");
        buf.append(VarStr.join(cols, ','));
        buf.append(" FROM ");
        buf.append(VarStr.join(tables, ','));
        buf.append(" WHERE ").append(middle);
        if (groups != null) 
          buf.append(" GROUP BY ").append(VarStr.join(groups, ','));
        ch.newq = buf.toString();
        ds.setString("SQLCONDITION", ch.newq);
        mod.add(ch);
      } else non.add(ch);
    }
    if (mode == 0) {
      System.out.println("unchanged: " + non.size());
      for (int i = 0; i < non.size(); i++)
        System.out.println(non.get(i));
      System.out.println();
      System.out.println("changed: " + mod.size());
      for (int i = 0; i < mod.size(); i++)
        System.out.println(mod.get(i));
      System.out.println();
      System.out.println("bad: " + bad.size());
      for (int i = 0; i < bad.size(); i++)
        System.out.println(bad.get(i));
    } else {
      System.out.println("unchanged: " + non.size());
      System.out.println();
      System.out.println("changed: " + mod.size());
      for (int i = 0; i < mod.size(); i++)
        System.out.println(((Change) mod.get(i)).changes);
      System.out.println();
      System.out.println("bad: " + bad.size());
      if (mode == 2) ds.saveChanges();
    }
  }
  
  static String findTable(String col, String[] tables) {
    for (int i = 0; i < tables.length; i++) {
      KreirDrop mod = KreirDrop.getModuleByName(tables[i]);
      if (mod != null && mod.getColumn(col) != null) return tables[i];
    }
    return null;
  }

}

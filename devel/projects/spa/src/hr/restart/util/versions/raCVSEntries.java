/****license*****************************************************************
**   file: raCVSEntries.java
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
 * raCVSEntries.java
 *
 * Created on 2003. prosinac 24, 11:11
 */

package hr.restart.util.versions;

import hr.restart.sisfun.TextFile;
import hr.restart.sisfun.frmParam;
import hr.restart.util.VarStr;

import java.io.File;
import java.io.FileFilter;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * zahvaca rekurzivno sve redove iz CVS/Entries fileova u citak i sortabilan set 
 * @author  andrej
 */
public class raCVSEntries {
  private String rootcvsdir;
  private TreeMap entriesMap = new TreeMap();
  private HashSet entriesFiles = new HashSet();
  /*private String lineSeparator = System.getProperty("line.separator","\n");*/
  private Timestamp nowTime;
  private boolean localTime;
  /** Creates a new instance of raCVSEntries */
  public raCVSEntries(String _rootcvsdir) {
    rootcvsdir = _rootcvsdir;
    nowTime = new Timestamp(System.currentTimeMillis());
    localTime = frmParam.getParam("sisfun", "localCVSTime", "D", "Uzeti lokalno vrijeme " +
       "promjene fajla, umjesto datuma iz CVS/Entries? (D,N)", true).equalsIgnoreCase("D");
    createEntries();
  }
  public TreeMap getEntries() {
      return entriesMap;
  }
  private void createEntries() {
    File rootDir = new File(rootcvsdir);
    if (!rootDir.exists()) throw new IllegalArgumentException("Zadani direktorij ne postoji: "+rootcvsdir);
    if (!rootDir.isDirectory()) throw new IllegalArgumentException(rootcvsdir+" nije direktorij!");
    if (localTime) fillFiles(rootDir);
    else {
      fillEntriesFiles(rootDir);
      fillEntriesMap();
    }
  }
  
  private void fillFiles(File dir) {
    File[] subfiles = dir.listFiles(new filesFilter());
    for (int i = 0; i < subfiles.length; i++)
      entriesMap.put(subfiles[i], new Timestamp(
          subfiles[i].lastModified()));
    
    File[] subdirs = dir.listFiles(new directoryFilter());
    for (int i = 0; i < subdirs.length; i++)      
      fillFiles(subdirs[i]);
  }
  
  private void fillEntriesFiles(File dir) {
    File[] subdirs = dir.listFiles(new directoryFilter());
    for (int i=0; i<subdirs.length; i++) {
      File[] subfiles = subdirs[i].listFiles(new entriesFilter());
      if (subfiles.length > 0) {
        for (int j=0; j<subfiles.length; j++) 
          entriesFiles.add(subfiles[j]);
      } else {
        fillEntriesFiles(subdirs[i]);
      }
    }
  }
  
  private void fillEntriesMap() {
    VarStr buf = new VarStr();
    String line;
    for (Iterator i = entriesFiles.iterator(); i.hasNext(); ) {
      File item = (File) i.next();
      TextFile tf = TextFile.read(item);
      if (tf != null) {
        while ((line = tf.in()) != null)
          if (line.startsWith("/"))
            parseLine(item, buf.clear().append(line));
        tf.close();
      }
      //String contents = FileHandler.readFile(item.getAbsolutePath());
      //StringTokenizer lines = new StringTokenizer(contents, lineSeparator);
      //while (lines.hasMoreTokens()) {
        //String line = lines.nextToken();
//        File key = getKey(item,line);
//        if (key != null) entriesMap.put(key,parseLine(line));
//      }
    }
  }
  
  private void parseLine(File entryFile, VarStr line) {
    try {
      String[] parts = line.split('/');
      File f = new File(entryFile.getParentFile().getParentFile(), parts[1]);
      if (f.exists() && f.isFile())
        entriesMap.put(f, localTime ? new Timestamp(f.lastModified()) : toTimestamp(parts[3]));
    } catch (Exception ex) {
      System.out.println("parseLine: something wrong with "+line);
      //ex.printStackTrace();
    }
  }
  
/*  private Object parseLine(java.lang.String line) {
    StringTokenizer linetokens = new StringTokenizer(line,"/");
    String lastToken="";
    while (linetokens.hasMoreTokens()) lastToken = linetokens.nextToken();
    return toTimestamp(lastToken);
  }*/
  
  private Timestamp toTimestamp(String p) {
    try {
      StringTokenizer datetokens = new StringTokenizer(p," ");
      datetokens.nextToken(); //day of week
      String monthname = datetokens.nextToken();
      String date = datetokens.nextToken();
      String time = datetokens.nextToken();
      String year = datetokens.nextToken();
      //trebam: 2003-12-27 00:07:28.912
      return Timestamp.valueOf(year+"-"+toMonth(monthname)+"-"+date+" "+time+".000");
    } catch (Exception ex) {
      System.out.println("toTimestamp: something wrong with "+p);
      //ex.printStackTrace();
      return nowTime;
    }
  }
  private String toMonth(String p) {
    if (p.equals("Jan")) return "01";
    if (p.equals("Feb")) return "02";
    if (p.equals("Mar")) return "03";
    if (p.equals("Apr")) return "04";
    if (p.equals("May")) return "05";
    if (p.equals("Jun")) return "06";
    if (p.equals("Jul")) return "07";
    if (p.equals("Aug")) return "08";
    if (p.equals("Sep")) return "09";
    if (p.equals("Oct")) return "10";
    if (p.equals("Nov")) return "11";
    if (p.equals("Dec")) return "12";
    return "12";
  }
  
/*  private File getKey(java.io.File entryFile, java.lang.String line) {
    //entryFile <path>/CVS/Entries a treba mi path + imefilea
    StringTokenizer linetokens = new StringTokenizer(line,"/");
//    linetokens.nextToken();
    String fname = linetokens.nextToken();
    String fpath = entryFile.getParentFile().getParentFile().getAbsolutePath()+File.separator+fname;
    File f = new File(fpath);
    if (validateFile(f)) return f;
    return null;
  } */
  
  /*boolean validateFile(java.io.File f) {
    if (!f.exists()) return false;
    if (!f.isFile()) return false;
//if (!f.getName().toLowerCase().endsWith(".java")) return false;
    return true;
  }*/

  class directoryFilter implements FileFilter {
    public boolean accept(File pathname) {
      return pathname.isDirectory();
    }
  }
  class entriesFilter implements FileFilter {
    public boolean accept(File pathname) {
      if (pathname.isDirectory()) return false;
      return pathname.getName().equalsIgnoreCase("entries");
    }
  }
  class filesFilter implements FileFilter {
    public boolean accept(File pathname) {
      if (pathname.isDirectory()) return false;
      String name = pathname.getName().toLowerCase();
      return (name.endsWith(".java") ||
          name.endsWith(".template") || name.endsWith(".sav") ||
          name.endsWith("tabledef.txt") || name.endsWith(".jrxml"));
    }
  }
  //-----
  public static void main(String[] args) {
//    hr.restart.util.Util.redirectSystemOut();
    Timestamp maxts = new Timestamp(0);
    raCVSEntries c = new raCVSEntries(args[0]);
    for (Iterator i = c.entriesMap.keySet().iterator(); i.hasNext(); ) {
      Object item = i.next();
      Timestamp thists = (Timestamp)c.entriesMap.get(item);
      if (thists.compareTo(maxts) >= 0) maxts = thists;
//      System.out.println("key = "+item);
//      System.out.println("line = "+c.entriesMap.get(item));
    }
    System.out.println(maxts);
  }
  
  
}

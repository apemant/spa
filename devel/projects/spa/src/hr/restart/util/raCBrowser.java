/****license*****************************************************************
**   file: raCBrowser.java
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class raCBrowser {
//  private NameComparator ncomparator;
  ArrayList allclassnames = new ArrayList();
  ArrayList allclasses = new ArrayList();
  String[] allclassnames_array;
  Class[] allclasses_array;
  static String[] static_classnames;
  private HashSet static_classnames_list;
  Class instanceFilter;
  String[] classFilter;
  String[] classNotFilter;
  String[] packagesFilter;
  String[] packagesNotFilter;

  /**
   * Primjeri koristenja:
   * <pre>
   * 1) raCBrowser cbrw = new raCBrowser(javax.swing.LookAndFeel.class,null,null,new String[] {"plaf"},new String[] {"Basic","Multi"});
   * 2) raCBrowser cbrw2 = new raCBrowser(hr.restart.baza.KreirDrop.class,new String[] {"hr.restart.baza"},null,null,null);
   * nakon instanciranja
   * Class[] foundClasses = cbrw.getClasses();
   * String[] foundClassNames = cbrw.getClassNames();
   * </pre>
   * @param _insoff uzima samo klase koje su instance _insoff
   * @param _packages paketi koje pretrazuje
   * @param _not_packages paketi koje NE pretrazuje
   * @param _filters pretrazuje samo klase cije puno ime (npr. javax.swing.JTextField) sadrzi zadane stringove
   * @param _not_filters NE pretrazuje klase cije puno ime sadrzi zadane stringove
   */
  public raCBrowser(Class _insoff,String[] _packages,String[] _not_packages,String[] _filters,String[] _not_filters) {
    instanceFilter = _insoff;
    classFilter = _filters;
    classNotFilter = _not_filters;
    packagesFilter = _packages;
    packagesNotFilter = _not_packages;
    if (static_classnames == null) {
      static_classnames_is_null();
    }
    ArrayList entries = new ArrayList();
    for (int i = 0; i < static_classnames.length; i++) {
      String stclname = static_classnames[i];
      if (validateFilters(stclname)) {
        entries.add(stclname);
      }
    }
    addClasses(entries);
    sort();
  }
  private void static_classnames_is_null() {
    static_classnames_list = new HashSet();
    //cita iz java.class.path i sun.boot.class.path
    String classpath = System.getProperty("java.class.path")+File.pathSeparator+System.getProperty("sun.boot.class.path");
//
//    java.util.jar.Manifest mf;
/*
    sysoutTEST ST = new sysoutTEST(false);
    String sres = this.getClass().getResource("").toString();
    System.out.println("sres");
    StringTokenizer jartoks = new StringTokenizer(sres,"jar");
    ST.prn(jartoks);
*/
/*
    System.out.println("resource = "+resurs);
    System.out.println("file = "+resurs.getFile());
sysoutTEST ST = new sysoutTEST(false);
ST.showMembers(resurs);*/
//
    StringTokenizer tokens = new StringTokenizer(classpath,File.pathSeparator);
    while (tokens.hasMoreTokens()) {
      String tok = tokens.nextToken();
      File ftok = new File(tok);
      if (ftok.isDirectory()) {
        try {
          DirBrowser dbrw = new DirBrowser(ftok,tok);
//          addClasses(dbrw.entries);
        }
        catch (Exception ex) {

        }
      } else {
        try {
          ZipFile zf = new ZipFile(ftok);
          ZipBrowser zbrw = new ZipBrowser(zf);
//          addClasses(zbrw.entries);
        }
        catch (Exception ex) {

        }
      }
    }
    static_classnames = (String[])static_classnames_list.toArray(new String[0]);
  }
  private void sort() {
    allclassnames_array = (String[])allclassnames.toArray(new String[0]);
    java.util.Arrays.sort(allclassnames_array);
    allclasses_array = (Class[])allclasses.toArray(new Class[0]);
    java.util.Arrays.sort(allclasses_array,new cbClassComparator());
  }

  private void addClasses(ArrayList entries) {
    for (int i = 0; i < entries.size(); i++) {
      String sitem = entries.get(i).toString();
      try {
        Class _class = Class.forName(sitem);
        if ((instanceFilter != null && instanceFilter.isAssignableFrom(_class)) || instanceFilter == null) {
          if (allclassnames.add(_class.getName()))
            allclasses.add(_class);
        }
      }
      catch (Throwable err) {

      }
    }
  }

  private boolean validateFilters(String str) {
    boolean pf = false;
    boolean cf = false;

    if (packagesFilter != null) {
      for (int i = 0; i < packagesFilter.length; i++) {
        if (str.startsWith(packagesFilter[i])) {
          pf = true;
          break;
        }
      }
    } else pf = true;

    if (packagesNotFilter != null) {
      for (int i = 0; i < packagesNotFilter.length; i++) {
        if (str.startsWith(packagesNotFilter[i])) {
          return false;
        }
      }
    }

    if (classFilter != null) {
      for (int i = 0; i < classFilter.length; i++) {
        if (str.indexOf(classFilter[i]) >= 0) {
          cf = true;
          break;
        }
      }
    } else cf = true;

    if (classNotFilter != null) {
      for (int i = 0; i < classNotFilter.length; i++) {
        if (str.indexOf(classNotFilter[i]) >= 0) {
          return false;
        }
      }
    }
    return cf&&pf;
  }

  /**
   * Vraca pronadjena imena klasa u String arrayu sortirano po abecedi
   * tako da se moze Object obj = Class.forName(getClassNames()[17]).newInstance()
   */
  public String[] getClassNames() {
    return allclassnames_array;
  }

  /**
   * Vraca pronadjene klase u Class arrayu abecedno sortirano po Class.getName()
   * tako da se moze Object obj = getClasses()[17].newInstance()
   */
  public Class[] getClasses() {
    return allclasses_array;
  }

  private void addName(String name, ArrayList entries) {
    if( name.endsWith( ".class" ) ) {
      StringTokenizer stok  = new StringTokenizer( name, "." );
      String token          = stok.nextToken();
      VarStr vtoken = new VarStr(token).replaceAll('/','.');
      String stoken = vtoken.replaceAll(File.separatorChar,'.').toString();
      static_classnames_list.add(stoken);
/*
      if (validateFilters(stoken)) {
        entries.add( stoken );
      }
*/
    }
  }

  class ZipBrowser {

    private ArrayList entries       = new ArrayList();

    public ZipBrowser( final ZipFile zip ) throws IOException {
      Enumeration allEntries  = zip.entries();
      ZipEntry zipEntry       = null;
      String name;
      while( allEntries.hasMoreElements() ) {
        zipEntry = (ZipEntry)allEntries.nextElement();
        name = zipEntry.getName();
        addName(name,entries);
      }
    }
  }

  class DirBrowser {

    private ArrayList entries       = new ArrayList();
    private String minuspath;
    public DirBrowser( final File _file, String _minuspath ) throws IOException {
      minuspath = _minuspath;
      if (!_file.isDirectory()) return;
      String name;
      browseDir(_file);
    }
    private void browseDir(final File file) {
      File[] allFiles  = file.listFiles();
      for (int i = 0; i < allFiles.length; i++) {
        File oneFile = allFiles[i];
        if (oneFile.isDirectory()) {
          browseDir(oneFile);
        } else {
          String filepath = new VarStr(oneFile.getPath()).replace(minuspath.concat(File.separator),"").toString();
          addName(filepath,entries);
        }
      }
    }
  }
  class cbClassComparator implements java.util.Comparator {
    public int compare(Object o1,
                   Object o2) {
      return o1.toString().compareTo(o2.toString());
    }
  }
//test
  public static void main(String[] args) {

    sysoutTEST ST = new sysoutTEST(false);
    TimeTrack TT = new TimeTrack(false);
    TT.Start("Prvi put");
    raCBrowser cbrw = new raCBrowser(javax.swing.LookAndFeel.class,null,null,new String[] {"plaf"},new String[] {"Basic","Multi"});
    TT.Stop();
    ST.prn("nasao "+cbrw.getClassNames().length);

    TT.Start("Drugi put");
    raCBrowser cbrw2 = new raCBrowser(hr.restart.baza.KreirDrop.class,new String[] {"hr.restart.baza"},null,null,null);
    TT.Stop();
    ST.prn("nasao2 "+cbrw2.getClassNames().length);

    TT.Start("Treci put");
    raCBrowser cbrw3 = new raCBrowser(javax.swing.LookAndFeel.class,null,null,new String[] {"plaf"},new String[] {"Basic","Multi"});
    TT.Stop();
    ST.prn("nasao3 "+cbrw3.getClassNames().length);

    TT.Start("4. put");
    javax.swing.plaf.metal.DefaultMetalTheme m;
    raCBrowser cbrw4 = new raCBrowser(javax.swing.plaf.metal.MetalTheme.class,null,null,new String[] {"plaf"},null);
    TT.Stop();
    ST.prn("nasao4 "+cbrw4.getClassNames().length);
//    ST.prn(cbrw4.getClassNames());
    System.out.println(cbrw.getClasses()[5]);
    System.out.println(cbrw.getClassNames()[5]);
    System.out.println(cbrw2.getClasses()[105]);
    System.out.println(cbrw2.getClassNames()[105]);
    System.out.println(cbrw2.getClasses()[190]);
    System.out.println(cbrw2.getClassNames()[190]);

System.out.println("cache = "+raCBrowser.static_classnames.length);
    System.exit(0);


  }
}
/****license*****************************************************************
**   file: raPatchMaker.java
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
 * raPatchMaker.java
 *
 * Created on December 27, 2003, 6:15 PM
 */

package hr.restart.util.versions;

import hr.restart.util.FileHandler;
import hr.restart.util.VarStr;
import hr.restart.util.raProcess;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JLabel;

/**
 *
 * @author  andrej
 */
public class raPatchMaker {
    raCVSEntries cvsentries;
    PatchFilter patchfilter;
    HashSet classFiles;
    HashSet otherFiles;
    String cvsPath;
    String classesPath;
    String encoding;
    String[] otherFilesFilter = {".template",".sav", "tabledef.txt",".jrxml",".png",".gif",".jpg"};
    String jarscript;
    String makescript;
    String tmpclassesPath = "tmp_makepatch";
    private String javac = "javac";
    private String jar = "jar";

    /** Creates a new instance of raPatchMaker */
    
    public raPatchMaker(String _cvsPath, String _classesPath, Timestamp _datefrom, Timestamp _dateto, String _encoding) {
      cvsentries = new raCVSEntries(_cvsPath);
      patchfilter = new PatchFilter(_datefrom,_dateto);
      cvsPath = _cvsPath;
      classesPath = _classesPath;
      encoding = _encoding;
    }
    public File make() {
      raProcess.runChild(new Runnable() {
        public void run() {
          if (classFiles == null) fillclasses(getCVSEntries());
          output("creating scripts ...");
          makeScript();
          output("getting additional files ...");
          copyOther();
          output("compiling and packing ...");
          runScript();
        }
      });
      if (raProcess.isCompleted()) return new File(getPatchName());
      return null;
    }
    public TreeMap getCVSEntries() {
      return cvsentries.getEntries();
    }
    private void copyOther() {
      FileHandler.copyFiles(otherFiles, cvsPath, tmpclassesPath);
    }
    public void removeDateRange() {
      patchfilter = new PatchFilter(null,null);
    }
    public void clean() {
      output("cleaning ...");
      File tmpdir = new File(tmpclassesPath);
      tmpdir.mkdirs();
      FileHandler.delTree(tmpdir);
      classFiles = null;
      otherFiles = null;
      new File("patchmaker.options").delete();
      new File("patchmaker.classes").delete();
      output("patch "+getPatchName()+" done!");
    }
    void fillclasses(java.util.AbstractMap fromMap) {
      output("finding files ...");
      classFiles = new HashSet();
      otherFiles = new HashSet();
      for (Iterator i = fromMap.keySet().iterator(); i.hasNext(); ) {
        File item = (File)i.next();
        if (patchfilter.accept((Timestamp)fromMap.get(item))) {
//          System.out.println(item +" accepted with "+getCVSEntries().get(item));
          if (item.getName().toLowerCase().endsWith(".java")) {
            classFiles.add(item.getAbsolutePath());
          } else {
            
            if (matchesExtension(item.getName())) otherFiles.add(item);
          }
        }
      }
    }
    
    public boolean matchesExtension(String fileName) {
      boolean matches = false;
      for (int x=0; x<otherFilesFilter.length; x++)
        if (fileName.toLowerCase().endsWith(otherFilesFilter[x])) {
          return true;
        }
      return false;
    }
    
    public void setJavac(String p) {
      javac = p;
    }
    public void setJar(String p) {
      jar = p;
    }
    

    private void makeScript() {
      
      File tmpdirforclasses = new File(tmpclassesPath);
      tmpdirforclasses.mkdirs();
      FileHandler.delTree(tmpdirforclasses);
      String options = "-encoding "+encoding+"\n"+
                       "-sourcepath "+cvsPath+"\n"+
                       "-classpath "+classesPath+"\n"+
                       "-d "+tmpdirforclasses+"\n"+
                       "-g \n";
      FileHandler.writeConverted(options, "patchmaker.options", null);
      
      String classes = "";
      for (Iterator i = classFiles.iterator(); i.hasNext(); ) {
        classes = classes + i.next().toString() + "\n";
      }
      FileHandler.writeConverted(classes, "patchmaker.classes", null);
      makescript = javac+" -J-Xmx512m @patchmaker.options @patchmaker.classes";
      String patchName = getPatchName();
      jarscript = jar+" -cf "+patchName+" -C "+tmpclassesPath+" .";
/*      for (Iterator i = classFiles.iterator(); i.hasNext(); ) {
          jarscript = jarscript + " " +i.next().toString();
      }*/
      FileHandler.writeConverted(makescript+"\n"+jarscript, "makepatch.bat", null);
    }
    private String getPatchName() {
      Timestamp date = patchfilter.datefrom;
      if (date == null) date = new Timestamp(System.currentTimeMillis());
      long fdate = Long.parseLong(new StringBuffer(
          new VarStr(new java.sql.Date(date.getTime()).toString()).replaceAll("-","").toString()
        ).reverse().toString());
//      System.out.println("formated date = "+fdate);
      return "000"+(99999999-fdate) + "patch.jar";
    }
    private void runScript() {
      try {
          int succ;
          int ch;
          output("compiling...");
          Process makeproc = Runtime.getRuntime().exec(makescript);
          succ = makeproc.waitFor();
          if (succ!=0) {
            output("Errors compiling!");
            while ((ch = makeproc.getErrorStream().read()) > -1) System.out.write(ch);
            raProcess.fail();
            return;
          }
          output("packing...");
          Process jarproc = Runtime.getRuntime().exec(jarscript);
          jarproc.waitFor();
          if (succ!=0) {
            output("Errors packing!");
            while ((ch = jarproc.getErrorStream().read()) > -1) System.out.write(ch);
            raProcess.fail();
            return;
          }
          //output("patch "+System.getProperty("user.dir")+"/"+getPatchName()+" done!");
      } catch (Exception e) {
        e.printStackTrace();
        raProcess.fail();
      }
    }
    
    private JLabel outputComp;
    public void setOutputComp(JLabel p) {
      outputComp = p;
    }
    private void output(String txt) {
      System.out.println(txt);
      if (!(raProcess.isCompleted() || raProcess.isInterrupted())) raProcess.setMessage(txt, true);
      if (outputComp != null) outputComp.setText(txt);
    }
    class PatchFilter {
        Timestamp datefrom;
        Timestamp dateto;
        public PatchFilter(Timestamp _datefrom, Timestamp _dateto) {
            datefrom = _datefrom;
            dateto = _dateto;
        }
        public boolean accept(Timestamp t) {
//            System.out.println("should I accept "+t+" ?");
//            System.out.println("Is "+t+" between "+datefrom+" and "+dateto+"?");
            boolean b = acc2(t);
//            System.out.println(b+"!!!");
            return b;
        }
        private boolean acc2(Timestamp t) {
            if (t == null) return true;
            if (datefrom == null && dateto == null) return true;
            if (datefrom == null) return t.before(dateto);
            if (dateto == null) return t.after(datefrom);
            return t.after(datefrom) && t.before(dateto);
        }
    }
    
    
    public static void main(String[] args) {
/*      String _cvsPath = args[0];
      String _classesPath = args[1];
      Timestamp _datefrom;
      try {
        System.out.println(args[2]+" -> "+args[2]+" 00:00:00.000");
        _datefrom = Timestamp.valueOf(args[2]+" 00:00:00.000");
      } catch (Exception ex) {
          System.out.println(ex);
          _datefrom = null;
      }
      Timestamp _dateto;
      try {
          System.out.println(args[3]+" -> "+args[3]+" 00:00:00.000");
          _dateto = Timestamp.valueOf(args[3]+" 00:00:00.000");
      } catch (Exception ex) {
          System.out.println(ex);
          _dateto = null;
      }
      new raPatchMaker(_cvsPath, _classesPath, _datefrom, _dateto).make();
*/ 
/*
      String tmpclassesPath = "tmp_makepatch";
      File tmpdirforclasses = new File(tmpclassesPath);
      tmpdirforclasses.mkdirs();
      raPatchMaker.delTree(tmpdirforclasses);
 */
/*
      raPatchMaker rpm = new raPatchMaker(hr.restart.util.IntParam.getTag("patchmaker.source"), hr.restart.util.IntParam.getTag("patchmaker.class"), new Timestamp(0),null);
      rpm.fillclasses();
      FileHandler.copyFiles(rpm.otherFiles, hr.restart.util.IntParam.getTag("patchmaker.source"), "/home/andrej/temp/dir4copyfiles");
      HashSet clf = new HashSet();
      for (Iterator i = rpm.classFiles.iterator(); i.hasNext(); ) {
        String item = i.next().toString();
        clf.add(new File(item));
      }
      FileHandler.copyFiles(clf, hr.restart.util.IntParam.getTag("patchmaker.source"), "/home/andrej/temp/dir4copyfiles");
*/

 }

}

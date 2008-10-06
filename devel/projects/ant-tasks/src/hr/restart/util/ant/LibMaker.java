/*
 * LibMaker.java
 *
 * Created on 2004. ožujak 01, 12:18
 */

package hr.restart.util.ant;

import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.Project;
import java.io.File;
import java.util.*;
/**
 *
 * @author  andrej
 */
public class LibMaker extends Jar {
  
  File basedir;
  File destdir;
  File parent;
  String subparent;
  String prefix;
  String subsufix;
  
  public LibMaker() {
  }
  public void execute() {
    setProject(getProject());
    String[] incldirs = getDirectoryScanner(basedir).getIncludedDirectories();

    for (int i=0; i<incldirs.length; i++) {
      try {
        File incldir = new File(basedir.getAbsolutePath()+File.separator+incldirs[i]);
        if (incldir.getParentFile().getAbsolutePath().equalsIgnoreCase(parent.getAbsolutePath())) {
          String fileName = prefix+incldir.getName();
          String includesdir = "**/"+incldirs[i]+"/";
          String subparentdir = includesdir+subparent+"/";
          makeJar(fileName+".jar", includesdir, subparentdir);
          if (new File(incldir.getAbsolutePath()+File.separator+subparent).exists()) {
            makeJar(fileName+subsufix+".jar", subparentdir, null);
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
  
  public void makeJar(String fileName, String includes, String excludes) {
    /*
    Jar jar = new Jar();
    jar.setProject(getProject());
    jar.setBasedir(basedir);
    jar.setDestFile(new File(destdir.getAbsolutePath()+File.separator+fileName));
    if (includes!=null) jar.setIncludes(includes);
    if (excludes!=null) jar.setExcludes(excludes);
    jar.execute();
     */
    setBasedir(basedir);
    setDestFile(new File(destdir.getAbsolutePath()+File.separator+fileName));
    if (includes!=null) setIncludes(includes);
    if (excludes!=null) setExcludes(excludes);
    super.execute();

  }
  
  public void setBasedir(File f) {
    basedir=f;
    super.setBasedir(basedir);
  }
  
  public void setDestdir(File f) {
    destdir = f;
  }
  
  public void setParent(File f) {
    parent = f;
  }
  
  public void setSubparent(String s) {
    subparent = s;
  }
  
  public void setPrefix(String s) {
    prefix = s;
  }
  
  public void setSubsufix(String s) {
    subsufix = s;
  }
}

/*
 * Created on Aug 17, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.ant;

import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author andrej
 * <pre>
 * Parses Manifest file in given jar file and puts requested atrribute in given ant property 
 * usage:
 * 		(mfparser
 *		jarfile="${projects.dir}/spa/lib/ra-spa.jar"
 *		section="common"
 *		attribute="Version"
 *	    addproperty="last.build.version"
 *  	/)*
 * *"(" and ")" are "<" and ">"
 * </pre>  
 */
public class ManifestParser extends Task {
  private String jarFile;
  private String section;
  private String attribute;
  private String addproperty;

  public void execute () throws BuildException {
    if (jarFile == null || attribute == null || addproperty == null) {
      throw new BuildException("jarfile, entryname and addproperty tags are required !");
    }
    String attr = getManifestAttribute();
    getProject().setProperty(addproperty, attr);
  }
  
  private String getManifestAttribute() {
    try {
	  	JarFile jarfile = new JarFile(jarFile,false);
	  	Manifest mf = jarfile.getManifest();
	  	String v = null;
	  	if (section == null) {
	  	  v = mf.getMainAttributes().getValue(attribute);
	  	} else {
	  	  v = mf.getAttributes(section).getValue(attribute);
	  	}
	  	if (v!=null) return v;
		} catch (Exception e) {
		  e.printStackTrace();
		}
		return "NA";
  }
  public void setAddproperty(String addproperty) {
    this.addproperty = addproperty;
  }
  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }
  public void setSection(String section) {
    this.section = section;
  }
  public void setJarFile(String jarFile) {
    this.jarFile = jarFile;
  }
}

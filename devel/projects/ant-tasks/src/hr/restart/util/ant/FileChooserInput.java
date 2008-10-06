/*
 * Created on Aug 10, 2004 by Andrej
*/
package hr.restart.util.ant;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileFilter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.taskdefs.Input;


/**
 * @author andrej
 *
 * <pre>
 * usage example:
 * 		
 * 		(taskdef name="filechooser" classname="hr.restart.util.ant.FileChooserInput" classpath="${projects.dir}/ant-tasks/jars/ra-ant-tasks.jar"/)
 *    (filechooser
 *		fileextension="ra-spa.jar"
 *		defaultpath="${projects.dir}/spa"
 *		addproperty="file.path"
 * 		usegui="true"
 *	  /) * 
 *		*"(" and ")" are "<" and ">"
 * </pre>
 */
public class FileChooserInput extends Input {
  private String defaultPath = null;
  private FileFilter ffilter;
  private String filterDescription = "";
  private String useGUI = null;
  public void execute () throws BuildException {
    InputHandler defaultHandler = getProject().getInputHandler();
    FileInputHandler handler;
    if (checkGUI()) {
      handler = new FileChooserInputHandler();
    } else {
      handler = new FileTextChooserInputHandler();
    }
    if (ffilter != null) handler.setFilter(ffilter,filterDescription);
    if (defaultPath!=null) handler.setDefaultDirectoryPath(defaultPath);
    setMessage(handler.getMessage());
    setValidargs(handler.getValidArgs());
    getProject().setInputHandler(handler);
    super.execute();
    getProject().setInputHandler(defaultHandler);
  }
  
  private boolean checkGUI() {
    if (useGUI != null) {
      if (!new Boolean(useGUI).booleanValue()) return false;//jdk1.3 compilant
    }
    try {
      if (Toolkit.getDefaultToolkit() != null) {
        return true;
      }
    } catch (Error e) {
      System.out.println(e);
    }
    System.out.println("GUI not available");
    return false;
  }
  /**
   * @param defaultPath The defaultPath to set.
   */
  public void setDefaultPath(String defaultPath) {
    this.defaultPath = defaultPath;
  }
  
  public void setFileExtension(String ext) {
    filterDescription = ext;
    //if (!ext.trim().startsWith(".")) ext = "."+ext.trim();
    final String innerext = ext;
    ffilter = new FileFilter() {
      public boolean accept(File pathname) {
        if (pathname.isDirectory()) return true;
        return pathname.getName().endsWith(innerext);
      }
    };
  }
  public void setUseGUI(String useGUI) {
    this.useGUI = useGUI;
  }
}

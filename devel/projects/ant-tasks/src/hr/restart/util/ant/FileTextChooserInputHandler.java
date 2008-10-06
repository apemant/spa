/*
 * Created on Aug 11, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.ant;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.input.DefaultInputHandler;
import org.apache.tools.ant.input.InputRequest;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileTextChooserInputHandler extends DefaultInputHandler implements FileInputHandler {
  private String defaultDirectoryPath = null;
  private FileFilter ffilter = null;
  private TreeSet foundFiles = new TreeSet();
  /* (non-Javadoc)
   * @see hr.restart.util.ant.FileInputHandler#setDefaultDirectoryPath(java.lang.String)
   */
  public void setDefaultDirectoryPath(String defaultDirectoryPath) {
    this.defaultDirectoryPath = defaultDirectoryPath;
  }
  private void parsePath() {
    if (defaultDirectoryPath == null) setDefaultDirectoryPath(System.getProperty("user.dir"));
    File dd = new File(defaultDirectoryPath);
    checkFilter();
    if (dd.isFile()) {
      if (ffilter.accept(dd)) {
        foundFiles.add(dd);
      }
    } else {
      makeTree(dd);
    }
  }
  private void checkFilter() {
    if (ffilter == null) ffilter = new FileFilter() {
      public boolean accept(File pathname) {
        return true;
      }
    };
  }
  private void makeTree(File dd) {
    if (dd.isDirectory()) {
      File[] files = dd.listFiles(ffilter);
      for (int i = 0; i < files.length; i++) {
        makeTree(files[i]);
      }
    } else {
      foundFiles.add(dd);
    }
  }
  /* (non-Javadoc)
   * @see hr.restart.util.ant.FileInputHandler#getDefaultDirectoryPath()
   */
  public String getDefaultDirectoryPath() {
    return defaultDirectoryPath;
  }
  private String validargs = null;
  public String getMessage() {
    parsePath();
    String msg = "";
    validargs = "";
    int idx = 0;
    for (Iterator iter = foundFiles.iterator(); iter.hasNext();) {
      String fn = ((File) iter.next()).getAbsolutePath();
      idx++;
      msg = msg + idx + ") " + fn + "\n";
      validargs = validargs + idx + ",";
    }
    return msg;
  }
  public String getValidArgs() {
    if (validargs == null) getMessage();
    return validargs;
  }
  /* (non-Javadoc)
   * @see org.apache.tools.ant.input.InputHandler#handleInput(org.apache.tools.ant.input.InputRequest)
   */
  public void handleInput(InputRequest request) throws BuildException {
    super.handleInput(request);
    int idx;
    try {
      idx = Integer.parseInt(request.getInput());
    } catch (NumberFormatException e) {
      throw new BuildException("File not selected");
    }
    request.setInput(getFoundFile(idx));
  }
  private String getFoundFile(int idx) {
    int i = 0;
    for (Iterator iter = foundFiles.iterator(); iter.hasNext();) {
      i++;
      String fn = ((File) iter.next()).getAbsolutePath();
      if (i==idx) return fn;
    }
    throw new BuildException("File not selected");
  }
  /* (non-Javadoc)
   * @see hr.restart.util.ant.FileInputHandler#setFilter(java.io.FileFilter)
   */
  public void setFilter(FileFilter fileFilter, String desc) {
    ffilter = fileFilter;
    //desc nije interesantan
  }
}

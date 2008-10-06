/*
 * Created on Aug 10, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.ant;

import java.io.File;
import java.io.FileFilter;
import javax.swing.JFileChooser;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.input.InputRequest;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileChooserInputHandler implements FileInputHandler {
  private String defaultDirectoryPath = System.getProperty("user.dir");
  private FileFilter fileFilter = null;
  private String fileFilterDescription = "";
  /* (non-Javadoc)
   * @see org.apache.tools.ant.input.InputHandler#handleInput(org.apache.tools.ant.input.InputRequest)
   */
  public void handleInput(InputRequest request) throws BuildException {
    // TODO Auto-generated method stub
    JFileChooser chooser = new JFileChooser(getDefaultDirectoryPath());
    if (fileFilter != null) chooser.setFileFilter(new JFChooserFilter(fileFilter,fileFilterDescription));
    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      request.setInput(chooser.getSelectedFile().getAbsolutePath());
    } else {
      throw new BuildException("File not chosen, unable to continue!");
    }
  }

  /**
   * @return Returns the defaultDirectoryPath.
   */
  public String getDefaultDirectoryPath() {
    return defaultDirectoryPath;
  }
  /**
   * @param defaultDirectoryPath The defaultDirectoryPath to set.
   */
  public void setDefaultDirectoryPath(String defaultDirectoryPath) {
    this.defaultDirectoryPath = defaultDirectoryPath;
  }
  /* (non-Javadoc)
   * @see hr.restart.util.ant.FileInputHandler#setFilter(java.io.FileFilter)
   */
  public void setFilter(FileFilter fileFilter, String desc) {
    this.fileFilter = fileFilter;
    fileFilterDescription = desc;
  }

  private final class JFChooserFilter extends javax.swing.filechooser.FileFilter {
    java.io.FileFilter filter;
    String fdesc;
    JFChooserFilter(java.io.FileFilter filter, String desc) {
      this.filter = filter;
      fdesc = desc;
    }
      /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {
      return filter.accept(f);
    }
    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
      return fdesc;
    }
  }

  public String getMessage() {
    return "";
  }
  public String getValidArgs() {
    return "";
  }
}

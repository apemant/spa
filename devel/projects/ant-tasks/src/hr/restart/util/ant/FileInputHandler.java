/*
 * Created on Aug 11, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.ant;

import java.io.FileFilter;

import org.apache.tools.ant.input.InputHandler;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface FileInputHandler extends InputHandler {
  public String getDefaultDirectoryPath();
  public void setDefaultDirectoryPath(String defaultDirectoryPath);
  public void setFilter(FileFilter fileFilter, String description);
  public String getMessage();
  public String getValidArgs();
}

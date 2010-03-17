package hr.restart.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.oroinc.net.ftp.FTPClient;
import com.oroinc.net.ftp.FTPReply;

public class FileTransferUtil {
  Properties props;
  boolean isFS;
  private FTPClient ncftp = null;
  public FileTransferUtil() {
  }
  /**
   * Properties opcenito trebaju biti type="ftp|fs" i serverlib (kao working folder)
   * Properties bi za ftp trebali biti: url, user, pass, serverlib
   * @param _p
   */
  public FileTransferUtil(Properties _p) {
    props = _p;
    isFS = _p.getProperty("type", "ftp").equalsIgnoreCase("fs"); 
  }
  protected FTPClient getNetComponentsFTPClient() throws Exception {
    if (ncftp != null && ncftp.isConnected()) return ncftp;
    ncftp = new FTPClient();
    ncftp.connect(props.getProperty("url"));
    int reply = ncftp.getReplyCode();
    if (!FTPReply.isPositiveCompletion(reply)) 
      throw new Exception("Server refuses connection");
    ncftp.login(props.getProperty("user"), props.getProperty("pass"));
    ncftp.setFileType(FTPClient.IMAGE_FILE_TYPE);
    if (!ncftp.changeWorkingDirectory(props.getProperty("serverlib")))
      throw new Exception("Invalid serverlib directory");
    return ncftp;
  }
  public boolean saveFile(File im, String name) {
    if (isFS) {
      try {
        im.renameTo(new File(props.getProperty("serverlib")+File.separator+name));
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    } //else go on ftp
    FTPClient ftp = null;
    try {
      ftp = getNetComponentsFTPClient();
      ftp.deleteFile(name);
      InputStream ims = new FileInputStream(im);
      try {
        if (!ftp.storeFile(name, ims))
          throw new Exception(ftp.getReplyString());
      } finally {
        try {
          ims.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      closeFTP();
    }
  }
  private void close(FTPClient ftp) {
    try {
      if (ftp != null) {
        ftp.disconnect();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void closeFTP() {
    close(ncftp);
    ncftp = null;
  }
  public static File lastLoadedFile;
  public File loadFile(String name) {
    return loadFile(name, false);
  }
  public File loadFile(String name, boolean stayConnected) {
    if (isFS) {
      try {
        return new File(props.getProperty("serverlib")+File.separator+name);
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
    if (lastLoadedFile != null) lastLoadedFile.delete();
    File temp = null;
    FTPClient ftp = null;
    try {
      ftp = getNetComponentsFTPClient();
      temp = File.createTempFile(name, "");
      OutputStream imo = new FileOutputStream(temp);
      try {
        ftp.retrieveFile(name, imo);
      } finally {
        try {
          imo.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      return temp;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (!stayConnected) closeFTP();//(ftp);
//      if (temp != null) temp.delete();
      lastLoadedFile = temp;
    }
  }
  
  public boolean deleteFile(String name) {
    if (isFS) {
      try {
        File f = new File(props.getProperty("serverlib")+File.separator+name);
        f.delete();
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    FTPClient ftp = null;
    try {
      ftp = getNetComponentsFTPClient();
      ftp.deleteFile(name);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      closeFTP();
    }
  }
  public String[] list() {
    if (isFS) {
      return new File(props.getProperty("serverlib")).list();
    }
    FTPClient ftp = null;
    try {
      ftp = getNetComponentsFTPClient();
      return ftp.listNames();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      closeFTP();
    }
  }
}

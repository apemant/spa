package hr.restart.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.oroinc.net.ftp.FTPClient;
import com.oroinc.net.ftp.FTPReply;

public class FileTransferUtil {
  Properties props;
  boolean isFS, isSSH;
  private FTPClient ncftp = null;
  
  private com.jcraft.jsch.Session sess = null;
  private com.jcraft.jsch.ChannelSftp sch = null;
  
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
    isSSH = _p.getProperty("type", "ftp").equalsIgnoreCase("sftp");
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
  
  protected com.jcraft.jsch.ChannelSftp getChannel() throws Exception {
    if (sch != null && sch.isConnected()) return sch;
    
    com.jcraft.jsch.JSch jsch = new JSch();
    sess = jsch.getSession(
        props.getProperty("user"), props.getProperty("url"), 
        Integer.parseInt(props.getProperty("port", "22")));
    
    sess.connect();
    
    com.jcraft.jsch.Channel channel = sess.openChannel("sftp");
    channel.connect();

    sch = (com.jcraft.jsch.ChannelSftp) channel;
    sch.cd(props.getProperty("serverlib"));
    return sch;
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
    if (isSSH) return loadFileSSH(name, stayConnected);
    
    if (lastLoadedFile != null) lastLoadedFile.delete();
    File temp = null;
    FTPClient ftp = null;
    try {
      ftp = getNetComponentsFTPClient();
      temp = File.createTempFile(name, "");
      temp.deleteOnExit();
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
  
  public File loadFileSSH(String name, boolean stayConnected) {
    if (lastLoadedFile != null) lastLoadedFile.delete();
    File temp = null;    
    try {
      getChannel();
      temp = File.createTempFile(name, "", new File("."));
      sch.get(name, temp.getName());

      return temp;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (!stayConnected && sess != null) sess.disconnect();
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

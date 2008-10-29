package hr.restart.util;

import hr.restart.ftpVersionWorker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.ImageIcon;

import com.oroinc.net.ftp.FTPClient;
import com.oroinc.net.ftp.FTPReply;


public class raImageUtil {
  Properties imp;
  
  public raImageUtil() {
    imp = ftpVersionWorker.getVersionProperties();
  }
  
  public boolean saveImage(File im, String name) {
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
      close(ftp);
    }
  }
  public static File lastLoadedFile;
  public ImageIcon loadImage(String name) {
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
      return new ImageIcon(temp.toURL());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      close(ftp);
//      if (temp != null) temp.delete();
      lastLoadedFile = temp;
    }
  }
  
  public boolean deleteImage(String name) {
    FTPClient ftp = null;
    try {
      ftp = getNetComponentsFTPClient();
      ftp.deleteFile(name);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      close(ftp);
    }
  }
  
  private void close(FTPClient ftp) {
    try {
      if (ftp != null) ftp.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private FTPClient getNetComponentsFTPClient() throws Exception {
    FTPClient ncftp = new FTPClient();
    ncftp.connect(imp.getProperty("url"));
    int reply = ncftp.getReplyCode();
    if (!FTPReply.isPositiveCompletion(reply)) 
      throw new Exception("Server refuses connection");
    ncftp.login(imp.getProperty("user"), imp.getProperty("pass"));
    ncftp.setFileType(FTPClient.IMAGE_FILE_TYPE);
    if (!ncftp.changeWorkingDirectory(imp.getProperty("serverlib")))
      throw new Exception("Invalid serverlib directory");
    if (!ncftp.changeToParentDirectory())
      throw new Exception("Invalid serverlib directory");
    if (!ncftp.changeWorkingDirectory("images") && 
        (!ncftp.makeDirectory("images") || !ncftp.changeWorkingDirectory("images")))
      throw new Exception("Can't access directory ../images");
    return ncftp;
  }
}

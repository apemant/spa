package hr.restart.util;

import hr.restart.ftpVersionWorker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Properties;

import javax.swing.ImageIcon;

import com.oroinc.net.ftp.FTPClient;
import com.oroinc.net.ftp.FTPReply;


public class raImageUtil {
  Properties imp;
  FileTransferUtil ftu;
  public raImageUtil() {
    imp = ftpVersionWorker.getVersionProperties();
    ftu = new FileTransferUtil(imp) {
      protected FTPClient getNetComponentsFTPClient() throws Exception {
        FTPClient ncftp = super.getNetComponentsFTPClient();
        if (!ncftp.changeToParentDirectory())
        throw new Exception("Invalid serverlib directory");
        if (!ncftp.changeWorkingDirectory("images") && 
          (!ncftp.makeDirectory("images") || !ncftp.changeWorkingDirectory("images")))
        throw new Exception("Can't access directory ../images");
        return ncftp;
      }
    };
  }
  
  public boolean saveImage(File im, String name) {
    return ftu.saveFile(im, name);
  }
  public static File getLastLoadedFile() {
    return FileTransferUtil.lastLoadedFile;
  }
  public ImageIcon loadImage(String name) {
    File temp = ftu.loadFile(name);
    if (temp != null) {
      try {
        return new ImageIcon(temp.toURL());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
  
  public boolean deleteImage(String name) {
    return ftu.deleteFile(name);
  }
  

  
}

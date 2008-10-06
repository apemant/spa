/****license*****************************************************************
**   file: raCompress.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class raCompress {

  private static raCompress rC ;


  public static raCompress getraCompress(){
    if (rC==null) rC = new raCompress();
    return rC;
  }

  public void  write(ZipOutputStream zos,FileInputStream is){
    try {
      int retval;                             //return value
      byte[] buf = new byte[4096];            //read buffer
      do {
        retval = is.read(buf, 0, 4096);
        if (retval != -1)
        {
          zos.write(buf, 0, retval);
        }
      }while (retval != -1);

    }
    catch (IOException ex) {
    }
  }

  private void makeFile (InputStream in, OutputStream out) throws IOException
    {
      byte[] buffer = new byte[1024];
      int len;

      while((len = in.read(buffer)) >= 0) {
        out.write(buffer, 0, len);
      }
      in.close();
      out.close();
    }

  public String[] unzip(String zipfile,String dir) throws Exception {

    ArrayList al= new ArrayList();
    ZipFile zp = new ZipFile(zipfile);
    Enumeration en = zp.entries();

    File fdir = new File(dir);
    if (fdir.exists() && fdir.isFile()) throw new IOException(dir+" exist and it is FILE you son of the bitch");
    else if (!fdir.exists()) {
      fdir.mkdir();
    }

    while (en.hasMoreElements()){
      ZipEntry entry = (ZipEntry)en.nextElement();
      if(entry.isDirectory()) {
        (new File(entry.getName())).mkdir();
        continue;
      }
      makeFile(zp.getInputStream(entry),new BufferedOutputStream(new FileOutputStream(fdir.getAbsolutePath()+fdir.separator+entry.getName())));
      al.add(fdir.getAbsolutePath()+File.separator+entry.getName());
    }
    return hr.restart.util.Valid.ArrayList2StringArray(al);
  }

  public String[] unzip(String zipfile) throws Exception {
    return unzip(zipfile,"");
  }

  public void add(String zipfile,String[] files){
    try {
      FileOutputStream os = new FileOutputStream(zipfile);
      ZipOutputStream zos = new ZipOutputStream(os);
      for (int i = 0;i<files.length;i++){
        FileInputStream is = new FileInputStream(files[i]);

        zos.putNextEntry(new ZipEntry(files[i].substring(
            files[i].lastIndexOf(File.separator)+1,files[i].length())));
        write(zos,is);
        zos.closeEntry();
        is.close();
      }
      zos.close();
      os.close();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }


  public void compress(String filename,String outfilename){

    try
    {
      FileInputStream is = new FileInputStream(filename);
      FileOutputStream os = new FileOutputStream(outfilename);
      ZipOutputStream zos = new ZipOutputStream(os);
      FileInputStream ios = new FileInputStream(outfilename);
      ZipInputStream zis = new ZipInputStream(ios);

      zos.putNextEntry(new ZipEntry(filename));
      zos.closeEntry();
      is.close();
      zos.close();
      os.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

}
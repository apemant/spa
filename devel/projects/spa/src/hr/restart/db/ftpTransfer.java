/****license*****************************************************************
**   file: ftpTransfer.java
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
package hr.restart.db;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ftpTransfer {
  private static ftpTransfer fT;
  private boolean ispis = false;

  static public ftpTransfer getftpTransfer(boolean ispis){
    if (fT == null) {
      fT = new ftpTransfer();
    }
    fT.ispis=ispis;
    return fT;
  }

  public synchronized String readLine(BufferedReader in){
    String readLine ="";
    String cLine =null;
    char[] buf = new char[2048];
    int length = 0;
    try {
      length =in.read(buf,0,buf.length);
      readLine = String.valueOf(buf,0,length-1);
      if (ispis) System.out.println(readLine);
      return readLine;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return "!!! Greška";
    }
  }

  public java.net.Socket getSocket(String addr,int port){
    try {
      java.net.Socket soc = new java.net.Socket(java.net.InetAddress.getByName(addr),port);
      soc.setSoTimeout(60000);
      return soc;
    }
    catch (IOException ex) {
      return null;
    }
  }

  public boolean stor(String addr,int port,String username,String passwd,String folder,File file){

    BufferedReader in  = null;
    BufferedWriter out  = null;
    try {
      java.net.Socket soc = getSocket(addr,port);
      in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
      out = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
      readLine(in);

      out.write("USER "+username);
      out.newLine();
      out.flush();
      readLine(in);

      out.write("PASS "+passwd);
      out.newLine();
      out.flush();
      readLine(in);
      out.write("CWD "+folder);
      out.newLine();
      out.flush();
      readLine(in);

      out.write("type i");
      out.newLine();
      out.flush();
      readLine(in);

//      stor(new hr.restart.util.FileHandler(file.getAbsolutePath()),out,in);
      stor(file,out,in);

      out.write("QUIT");
      out.newLine();
      out.flush();
      readLine(in);

      soc.close();
      return true;

  } catch (Exception ex){
    ex.printStackTrace();
    return false;
  }


  }
  private String getAddr(int[] numbers) {
    return String.valueOf(numbers[0])+"."+
        String.valueOf(numbers[1])+"."+
        String.valueOf(numbers[2])+"."+
        String.valueOf(numbers[3]);
  }

  private int getPort(int[] numbers) {
    return numbers[4]*256+numbers[5];
  }

  private int[] getValues(String acacutija) {

    int [] numbers = new int[6];
    int brojac=0;
    int begin = acacutija.indexOf('(');
    int end = acacutija.indexOf(')');
    String tmp = "";
    if(begin!=-1 && end !=-1 && begin < end)
      acacutija = acacutija.substring(begin+1,end+1);

    for (int i=0;i<acacutija.length();i++) {
      if (acacutija.substring(i,i+1).equalsIgnoreCase(",") || acacutija.substring(i,i+1).equalsIgnoreCase(")")) {
        try {
          numbers[brojac] = Integer.parseInt(tmp);
          brojac ++;
        }
        catch (NumberFormatException ex) {
          ex.printStackTrace();
        }
        tmp="";
      } else {
        tmp=tmp+acacutija.substring(i,i+1);
      }
    }
    return numbers;
  }

  private void stor(File file,BufferedWriter out,BufferedReader in) throws Exception {
    FileInputStream fis = new FileInputStream(file);
    out.write("PASV");
    out.newLine();
    out.flush();
    readLine(in);
    int[] poljesnova = getValues(readLine(in));
    java.net.Socket data = new java.net.Socket(java.net.InetAddress.getByName(getAddr(poljesnova)),getPort(poljesnova));
    data.setSoTimeout(60000);
    DataOutputStream dos = new DataOutputStream(data.getOutputStream());
    out.write("STOR "+file.getName());
    out.newLine();
    out.flush();
    readLine(in);
    dos.write(readData(fis));
    dos.flush();
    dos.close();
    data.close();
    readLine(in);

  }

  private void stor(hr.restart.util.FileHandler fh,BufferedWriter out,BufferedReader in) throws Exception {
    out.write("PASV");
    out.newLine();
    out.flush();
    readLine(in);
    int[] poljesnova = getValues(readLine(in));
    java.net.Socket data = new java.net.Socket(java.net.InetAddress.getByName(getAddr(poljesnova)),getPort(poljesnova));
    data.setSoTimeout(60000);
    DataOutputStream dos = new DataOutputStream(data.getOutputStream());
    out.write("STOR "+fh.getFileName());
    out.newLine();
    out.flush();
    readLine(in);
    dos.write(readData(fh));
    dos.flush();
    dos.close();
    data.close();
    readLine(in);
  }

  private void write(FileOutputStream fos,InputStream ds){

    try {
      int available=0;
      int b = 0;
      byte[] bajt = new byte[2048];
      for(;;) {
        b = ds.read(bajt,0,bajt.length);
        if (b==-1) {
          break;
        }
        fos.write(bajt,0,b);
      }
      ds.close();
      fos.close();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  private byte[] readData(InputStream ds) {
    try {

      int len = ds.available();
      byte[] b = new byte[len];
      ds.read(b);
      ds.close();
      return b;
    }
    catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  private byte[] readData(hr.restart.util.FileHandler fh) {

    try {
      fh.openRead();
      int len = fh.fileInputStream.available();
      byte[] b = new byte[len];
      fh.fileInputStream.read(b);
      fh.close();
      return b;
    }
    catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }

  }

  public synchronized String command(String command,String param,BufferedWriter out,BufferedReader in) throws IOException{

    String fullcommand=(command.trim()+" "+param.trim()).trim();
    if (ispis)   System.out.println(command+" "+param);
    out.write(fullcommand);
    out.newLine();
    out.flush();
    return readLine(in);

  }

  public boolean get(String addr,int port,String username,String passwd,String folder,File file){
    return get(addr,port,username,passwd,folder,file,null);
  }

  public boolean get(String addr,int port,String username,String passwd,String folder,File file,String localfolder){

    String filename=null;
    if (localfolder !=null && !localfolder.equalsIgnoreCase("")) {
      File dir = new File(localfolder);
      if (!dir.exists()) {
        dir.mkdir();
      }
      if (!dir.isDirectory()){
        return false;
      }
      filename=dir.getAbsolutePath()+File.separator+file.getName();
    }
//System.out.println(filename);

    File newfile = (filename==null)?file:new File(filename);
    BufferedReader in  = null;
    BufferedWriter out  = null;


    try {
      java.net.Socket soc = getSocket(addr,port);
      in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
      out = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
      readLine(in);
      command("USER",username,out,in);
      command("PASS",passwd,out,in);
      command("CWD",folder,out,in);
      command("type i","",out,in);
      readLine(in);
      int[] poljesnova = getValues(command("PASV","",out,in));
      java.net.Socket data = getSocket(getAddr(poljesnova),getPort(poljesnova));
      if (data== null){
        System.out.println(getPort(poljesnova));
        System.out.println("data==null");
        return false;
      }

//      FileOutputStream(file)

//      FileOutputStream fos = new FileOutputStream(file);
      FileOutputStream fos = new FileOutputStream(newfile);
//      DataInputStream dis = new DataInputStream(data.getInputStream());
//      hr.restart.util.FileHandler fh = new hr.restart.util.FileHandler(file.getName());
//      hr.restart.util.FileHandler fh = new hr.restart.util.FileHandler(filename);

//      if (fh==null) System.out.println("fh==null");
      if (!analize(command("RETR",file.getName(),out,in))) {
        fos.close();
        data.close();
        command("QUIT","",out,in);
        in.close();
        out.close();
        soc.close();
        return false;
      }
      write(fos,data.getInputStream());
      fos.flush();
      fos.close();
      data.close();
      readLine(in);
      command("QUIT","",out,in);
      in.close();
      out.close();
      soc.close();
    }
    catch (IOException ex) {
      ex.printStackTrace();
      return false;
    }

    return true;
  }
  public boolean analize(String str){


    try {
      if (Integer.valueOf(str.substring(0,3)).intValue()>499){
//    System.out.println("str.substring(0,3)="+str.substring(0,3));
        return false;
      }
    }
    catch (NumberFormatException ex) {
      return true;
    }
    return true;

  }
}
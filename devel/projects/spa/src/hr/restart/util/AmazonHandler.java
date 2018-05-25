/****license*****************************************************************
**   file: AmazonHandler.java
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import com.amazonaws.services.rds.model.ModifyDBInstanceRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;


public class AmazonHandler {
  
  /*private static String accessId = "AKIAI3P6NGYXQQVZCGQA";
  private static String secretKey = "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";
  
  public static String bucket = "spa-backup-eu-central-1-917935665604";*/
  private static String accessId = "AKIAIVVPKTWMFRYM7ZXQ";
  
  private static String secretKey = "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";
  
  public static String bucket = "restart-spa-backup";
  
  public static String company = "restart";
  
  ProgressListener track;
  AmazonS3 conn;
  AmazonRDS db;

  public AmazonHandler() {
    String aid = IntParam.getTag("backup.id");
    if (aid == null || aid.length() == 0) aid = accessId;
    
    String skey = IntParam.getTag("backup.key");
    if (skey == null || skey.length() == 0) skey = secretKey;
    
    String buck = IntParam.getTag("backup.bucket");
    if (buck == null || buck.length() == 0) buck = bucket;
    
    String comp = IntParam.getTag("backup.company");
    if (comp == null || comp.length() == 0) comp = company;
    
    IntParam.setTag("backup.company", comp);
    
    accessId = aid;
    secretKey = skey;
    bucket = buck;
    company = comp;
    track = new ProgressListener() {
      long upload = 0, total = 0;
      public void progressChanged(ProgressEvent e) {
        if (e.getEventType() == ProgressEventType.TRANSFER_STARTED_EVENT) {
          upload = 0;
          if (raProcess.isRunning())
            raProcess.setMessage("Slanje backup datoteke na server...", true);
          else System.out.println("Upload database...");
        }
        if (e.getEventType() == ProgressEventType.REQUEST_CONTENT_LENGTH_EVENT) {
          total = e.getBytes();
          System.out.println("Total size = " + total);
        }
        if (e.getEventType() == ProgressEventType.REQUEST_BYTE_TRANSFER_EVENT) {
          upload += e.getBytes();
          if (raProcess.isRunning()) 
            raProcess.setMessage("Poslano " + (upload/1024) + " / " + (total/1024) + " KB ...", false);
        }
        if (e.getEventType() == ProgressEventType.TRANSFER_COMPLETED_EVENT) {
          if (!raProcess.isRunning())
            System.out.println("Upload completed.");
        }
        if (e.getEventType() == ProgressEventType.TRANSFER_FAILED_EVENT) {
          if (!raProcess.isRunning())
            System.out.println("Upload failed!!");
        }
      }
    };
    
    conn = get();
    
    db = getDb();
  }
  
  public AmazonS3 get() {
    AWSCredentials credentials = new BasicAWSCredentials(accessId, secretKey);

    ClientConfiguration clientConfig = new ClientConfiguration();
    clientConfig.setProtocol(Protocol.HTTPS);
    
    return AmazonS3ClientBuilder.standard()
                            .withClientConfiguration(clientConfig)
                            .withRegion(Regions.EU_CENTRAL_1)
                            .withCredentials(new AWSStaticCredentialsProvider(credentials))
                            .build();
  }
  
  public AmazonRDS getDb() {
    AWSCredentials credentials = new BasicAWSCredentials(accessId, secretKey);

    ClientConfiguration clientConfig = new ClientConfiguration();
    clientConfig.setProtocol(Protocol.HTTPS);
    
    return AmazonRDSClientBuilder.standard()
                        .withClientConfiguration(clientConfig)
                        .withRegion(Regions.EU_CENTRAL_1)
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .build();
  }
  
  public void setClass(String clazz) {
    ModifyDBInstanceRequest req = new ModifyDBInstanceRequest();
    req.setDBInstanceClass(clazz);
    
    System.out.println("Setting instance class to: " + clazz);
    
    db.modifyDBInstance(req);
  }
  
  public boolean putFile(File f) {
    try {
      PutObjectRequest req = new PutObjectRequest(bucket, company + "/" + f.getName(), f);
      req.setGeneralProgressListener(track);
      conn.putObject(req);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  
  public void maintenance(String[] bases) {
    try {
      Set backs = new HashSet();
      
      ObjectListing list = conn.listObjects(bucket, company + "/");
      do {
        List sums = list.getObjectSummaries();
        for (Iterator i = sums.iterator(); i.hasNext(); ) {
          S3ObjectSummary os = (S3ObjectSummary) i.next();
          backs.add(new Entry(os.getKey(), os.getLastModified()));
        }
        list = conn.listNextBatchOfObjects(list);
      } while (list.isTruncated());
      System.out.println("all backups: " + backs);
      
      for (int i = 0; i < bases.length; i++)
        backs.removeAll(findRequired(backs, bases[i]));
      
      System.out.println("for deletion: " + backs);
      for (Iterator i = backs.iterator(); i.hasNext(); )
        conn.deleteObject(bucket, ((Entry) i.next()).name);
      System.out.println("... over.");
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public Set getEverything() {
    try {
      Set backs = new HashSet();
      
      ObjectListing list = null;
      do {
        list = list == null ? conn.listObjects(bucket) : conn.listNextBatchOfObjects(list);
        List sums = list.getObjectSummaries();
        for (Iterator i = sums.iterator(); i.hasNext(); ) {
          S3ObjectSummary os = (S3ObjectSummary) i.next();
          backs.add(new Entry(os.getKey(), os.getLastModified()));
        }
      } while (list.isTruncated());
      
      return backs;      
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  Set findRequired(Set backs, String base) {
    List sub = new ArrayList();
    for (Iterator i = backs.iterator(); i.hasNext(); ) {
      Entry e = (Entry) i.next();
      if (e.name.startsWith(company + "/raBackup-" + base))
        sub.add(e);
    }
    if (sub.size() <= 4) return new HashSet(sub);
    
    Collections.sort(sub, new Comparator() {
      public int compare(Object o1, Object o2) {
        return ((Entry) o1).date.compareTo(((Entry) o2).date);
      }
    });
    
    Set ret = new HashSet();
    ret.add(sub.get(0));
    ret.add(sub.get(sub.size() - 1));
    Date last = ((Entry) sub.get(sub.size() - 1)).date;
    
    for (int i = sub.size() - 1; i > 0; i--) {
      Entry e = (Entry) sub.get(i);
      long diff = last.getTime() - e.date.getTime();
      int days = (int) ((diff + 43200000L) / 86400000L);
      if (days < 3 || i >= sub.size() - 3) ret.add(e);
    }

    return ret;
  }
  
  public static class Entry {
    String name;
    Date date;
    
    public Entry(String name, Date date) {
      this.name = name;
      this.date = date;
    }
    
    public int hashCode() {
      return name.hashCode();
    }
    
    public boolean equals(Object obj) {
      if (obj instanceof Entry)
        return name.equals(((Entry) obj).name);
      return false;
    }
    
    public String toString() {
      return name;
    }
  }

}

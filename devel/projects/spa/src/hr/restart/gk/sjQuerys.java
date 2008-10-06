/****license*****************************************************************
**   file: sjQuerys.java
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
package hr.restart.gk;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class sjQuerys {
  static String amorOpis=", 'Amortizacija                    ' as tekst";
  static String isprOpis=", 'Ispravak                        ' as tekst";
  static String iskpOpis=", 'Isknjiženje osnovice u pripremi ' as tekst";
  static String uposOpis=", 'Osnovica u upotrebi             ' as tekst";
  static String iskoOpis=", 'Isknjiženje osnovice            ' as tekst";
  static String iskiOpis=", 'Isknjiženje ispravka            ' as tekst";
  static String sdvrOpis=", 'Ostatak sadašnje vrijednosti    ' as tekst";
  static String zadoOpis=", 'Zaduženje osnovice              ' as tekst";
  static String zadiOpis=", 'Zaduženje ispravka              ' as tekst";

  public sjQuerys() {
  }
  public static String getOSKnjizenjaPriprema() {
    String str="SELECT os_sredstvo.corg2 as corg, os_sredstvo.brojkonta as konto, 0.00 as idug, os_promjene.osnduguje as ipot"+iskpOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo"+
               " WHERE tippromjene='U' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status='P' "+
               "UNION "+
               "SELECT os_sredstvo.corg2 as corg, os_sredstvo.brojkonta as konto, os_promjene.osnduguje as idug, 0.00 as ipot"+uposOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo"+
               " WHERE tippromjene='U' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status='A' ";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getOSKnjizenjeAmor() {
    String str="SELECT os_obrada2.corg as corg, kontoamor as konto, 0.00 as idug, amortizacija as ipot"+amorOpis+
               " FROM os_obrada2, os_kontaisp"+
               " WHERE os_obrada2.brojkonta=os_kontaisp.brojkonta and amortizacija!=0 "+
               "UNION "+
               "SELECT os_obrada2.corg as corg, kontoisp as konto, amortizacija as idug, 0.00 as ipot"+isprOpis+
               " FROM os_obrada2, os_kontaisp"+
               " WHERE os_obrada2.brojkonta=os_kontaisp.brojkonta and amortizacija!=0 ";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getOSKnjizenjeLikvidacija() {
    String str="SELECT os_sredstvo.corg2 as corg, os_kontaisp.kontoisp as konto, 0.00 as idug, os_promjene.isppotrazuje as ipot"+isprOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo, os_kontaisp"+
               " WHERE tippromjene='A' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status=os_promjene.status and os_sredstvo.status='A' and os_kontaisp.brojkonta=os_sredstvo.brojkonta and os_promjene.dokument='LIK' "+
               "UNION "+
               "SELECT os_sredstvo.corg2 as corg, os_kontaisp.kontoamor as konto, os_promjene.isppotrazuje as idug, 0.00 as ipot"+amorOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo, os_kontaisp"+
               " WHERE tippromjene='A' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status=os_promjene.status and os_sredstvo.status='A' and os_kontaisp.brojkonta=os_sredstvo.brojkonta and os_promjene.dokument='LIK' "+
               "UNION "+
               "SELECT os_sredstvo.corg2 as corg, os_sredstvo.brojkonta as konto, 0.00 as idug, os_promjene.osnpotrazuje as ipot"+iskoOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo, os_kontaisp"+
               " WHERE tippromjene='L' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status=os_promjene.status and os_sredstvo.status='A' and os_kontaisp.brojkonta=os_sredstvo.brojkonta "+
               "UNION "+
               "SELECT os_sredstvo.corg2 as corg, os_kontaisp.kontoisp as konto, os_promjene.ispduguje as idug, 0.00 as ipot"+iskiOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo, os_kontaisp"+
               " WHERE tippromjene='L' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status=os_promjene.status and os_sredstvo.status='A' and os_kontaisp.brojkonta=os_sredstvo.brojkonta "+
               "UNION "+
               "SELECT os_sredstvo.corg2 as corg, os_kontaisp.kontosadvr as konto, (os_promjene.osnpotrazuje-os_promjene.ispduguje) as idug, 0.00 as ipot"+sdvrOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo, os_kontaisp"+
               " WHERE tippromjene='L' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status=os_promjene.status and os_sredstvo.status='A' and os_kontaisp.brojkonta=os_sredstvo.brojkonta ";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getOSKnjizenjeChangeCORG() {
    String str="SELECT os_sredstvo.corg2 as corg, os_kontaisp.kontoisp as konto, 0.00 as idug, os_promjene.isppotrazuje as ipot"+isprOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo, os_kontaisp"+
               " WHERE tippromjene='A' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status=os_promjene.status and os_sredstvo.status='A' and os_kontaisp.brojkonta=os_sredstvo.brojkonta and os_promjene.dokument='ORG' and os_sredstvo.aktiv='N'"+
               "UNION "+
               "SELECT os_sredstvo.corg2 as corg, os_kontaisp.kontoamor as konto, os_promjene.isppotrazuje as idug, 0.00 as ipot"+amorOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo, os_kontaisp"+
               " WHERE tippromjene='A' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status=os_promjene.status and os_sredstvo.status='A' and os_kontaisp.brojkonta=os_sredstvo.brojkonta and os_promjene.dokument='ORG' and os_sredstvo.aktiv='N'"+
               "UNION "+
               "SELECT os_sredstvo.corg2 as corg, os_sredstvo.brojkonta as konto, 0.00 as idug, os_promjene.osnpotrazuje as ipot"+iskoOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo, os_kontaisp"+
               " WHERE tippromjene='L' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status=os_promjene.status and os_sredstvo.status='A' and os_kontaisp.brojkonta=os_sredstvo.brojkonta and os_promjene.dokument='ORG' and os_sredstvo.aktiv='N'"+
               "UNION "+
               "SELECT os_sredstvo.corg2 as corg, os_kontaisp.kontoisp as konto, os_promjene.ispduguje as idug, 0.00 as ipot"+iskiOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo, os_kontaisp"+
               " WHERE tippromjene='L' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status=os_promjene.status and os_sredstvo.status='A' and os_kontaisp.brojkonta=os_sredstvo.brojkonta and os_promjene.dokument='ORG' and os_sredstvo.aktiv='N'"+
               "UNION "+
               "SELECT os_sredstvo.corg2 as corg, os_sredstvo.brojkonta as konto, os_promjene.osnduguje as idug, 0.00 as ipot"+zadoOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo, os_kontaisp"+
               " WHERE tippromjene='O' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status=os_promjene.status and os_sredstvo.status='A' and os_kontaisp.brojkonta=os_sredstvo.brojkonta and os_sredstvo.aktiv='D'"+
               "UNION "+
               "SELECT os_sredstvo.corg2 as corg, os_kontaisp.kontoisp as konto, 0.00 as idug, os_promjene.isppotrazuje as ipot"+zadiOpis+
               " FROM os_promjene, os_vrpromjene, os_sredstvo, os_kontaisp"+
               " WHERE tippromjene='O' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_promjene.statusknj='N' and os_sredstvo.corg=os_promjene.corg and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.status=os_promjene.status and os_sredstvo.status='A' and os_kontaisp.brojkonta=os_sredstvo.brojkonta and os_sredstvo.aktiv='D'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String updateOS_Log(String nalog) {
    String str="update OS_LOG set cnaloga='"+nalog+"', STATKNJ='K'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String updateOS_Meta() {
    String str="update OS_METAOBRADA set corg2='ok'";
    System.out.println("SQL: "+str);
    return str;
  }
}
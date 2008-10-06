/****license*****************************************************************
**   file: raServerSideRobnoInterface.java
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

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 *
 *
 *     Ovo je "draft" verzija serverske strane prihvata podataka.
 * Stvar funkcionira na slijedeci nacin
 * 1.) Provjerava se da li je dosao koji novi zip fajl s podacima za prihvat
 *
 * Slijedece tocke se odvijaju za svaki pronadjeni novi zip file :
 *
 * 2.) raspakira se zipFile
 * 3.) iz dobijenih *.dat i *.def se pune odgovarajuce tabele
 * 4.) kreira se povratni izvjestaj za svaki slog o prihvatu da li je prošao ili ne
 * 5.) oznacava se zip fajl kao prenesen
 * 6.) povratni izvještaj se komprimira za prihvat klijenta kod slijedeceg spajanja
 * 7.) Razno i sat razredne zajednice
 *
 */

public interface raServerSideRobnoInterface {


  /**
   *
   *  @return true ako postoje nove zip "f(r)ajle" za prihvat
   *
   */

  public boolean isNewZipFilesExist();

  /**
   *
   */
  public String[] getZipFiles4Batch();
  public String[] getZipFiles();

  /**
   *
   */

  public void decompressZipFile(String zipFile);
  public void fromFileToDatabase();
  public void returnTransferKeys();
  public void makeReturnZipFile(String zipFile);

}
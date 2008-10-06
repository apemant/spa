/****license*****************************************************************
**   file: raTransferNotifier.java
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
package hr.restart.baza;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public interface raTransferNotifier {

  int LOAD_STARTED = 1;
  int INSERT_STARTED = 2;
  int DUMP_STARTED = 3;
  int ROW_LOADED = 4;
  int ROW_INSERTED = 5;
  int ROW_STORED = 6;
  int LOAD_FINISHED = 7;
  int INSERT_FINISHED = 8;
  int DUMP_FINISHED = 9;
  
  int DUMP_SEGMENT_FINISHED = 10;
  
  int ROW_INSERT_FAILED = -1;

  void rowTransfered(String table, int action, int row, Object data);
}

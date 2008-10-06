/****license*****************************************************************
**   file: raReplicator.java
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

public interface raReplicator {
  public boolean repl_1(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to);
  public boolean repl_2(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to);
  public boolean repl_3(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to);
  public boolean repl_4(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to);
  public boolean repl_5(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to);
  public boolean repl_6(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to);
  public boolean repl_7(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to);
  public boolean repl_8(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to);
  public boolean repl_9(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to);
}
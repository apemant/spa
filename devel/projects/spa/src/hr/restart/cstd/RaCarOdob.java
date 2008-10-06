/****license*****************************************************************
**   file: RaCarOdob.java
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
package hr.restart.cstd;

import hr.restart.util.raSifraNaziv;

public class RaCarOdob extends raSifraNaziv {
    hr.restart.baza.dM dm;

    hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();

    public RaCarOdob() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        dm = hr.restart.baza.dM.getDataModule();
        this.setRaDataSet(dm.getCarodob());
        this.setRaColumnSifra("CODOB");
        this.setRaColumnNaziv("NAZIV");
        this.setRaText("Odobrenja");
    }

    public boolean DeleteCheck() {
        return true;
    }
}

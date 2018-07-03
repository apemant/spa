/*jadclipse*/// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) nonlb space radix(10) lradix(10) 

package com.borland.dx.sql.dataset;

import com.borland.dx.dataset.*;
import com.borland.jb.io.InputStreamToByteArray;
import com.borland.sql.SQLAdapter;
import java.beans.Beans;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

// Referenced classes of package com.borland.dx.sql.dataset:
//            r, RuntimeMetaData, QueryDescriptor, b, 
//            c, ConnectionUpdateListener, Database, ConnectionUpdateEvent

public abstract class JdbcProvider extends Provider
    implements ConnectionUpdateListener, c, LoadCancel {

    public void closeStatement() {
        try {
            if (r != null)
                r.close();
            f();
        }
        catch (SQLException sqlexception) {
            DataSetException.throwExceptionChain(sqlexception);
        }
    }

    static String a(String s1) {
        if (s1 == null)
            return null;
        int i1;
        for (i1 = s1.length(); i1 > 0 && s1.charAt(i1 - 1) == ' '; i1--);
        return i1 >= s1.length() ? s1 : s1.substring(0, i1);
    }

    private final boolean a(SQLException sqlexception) {
        for (; sqlexception != null; sqlexception = sqlexception.getNextException())
            if (sqlexception.getNextException() != null);

        return true;
    }

    final void a(Database database, StorageDataSet storagedataset, ResultSet resultset) {
        if (resultset != null && storagedataset != null)
            synchronized (storagedataset) {
                ProviderHelp.failIfOpen(storagedataset);
                try {
                    Column acolumn[] = RuntimeMetaData.a(database, storagedataset.getMetaDataUpdate(), resultset);
                    int ai[] = ProviderHelp.initData(storagedataset, acolumn, true, false);
                    a(storagedataset, resultset, ai);
                }
                catch (SQLException sqlexception) {
                    DataSetException.SQLException(sqlexception);
                }
                catch (IOException ioexception) {
                    DataSetException.throwExceptionChain(ioexception);
                }
            }
    }

    private final synchronized InputStream a(InputStream inputstream) throws IOException {
        if (w == null)
            w = new byte[102400];
        int i1 = 0;
        byte abyte0[] = null;
        try {
            while ((i1 = inputstream.read(w)) > 0) 
                if (abyte0 == null) {
                    abyte0 = new byte[i1];
                    System.arraycopy(w, 0, abyte0, 0, i1);
                } else {
                    byte abyte1[] = new byte[i1 + abyte0.length];
                    System.arraycopy(abyte0, 0, abyte1, 0, abyte0.length);
                    System.arraycopy(w, 0, abyte1, abyte0.length, i1);
                    abyte0 = abyte1;
                }
        }
        catch (Exception exception) {
            abyte0 = null;
            InputStream inputstream1 = null;
            return inputstream1;
        }
        if (abyte0 == null)
            abyte0 = new byte[0];
        return new InputStreamToByteArray(abyte0);
    }

    final void a(StorageDataSet storagedataset, ResultSet resultset, int ai[]) throws SQLException, IOException, DataSetException {
        if (a == null) {
            if (!resultset.next()) {
                b(true);
                storagedataset.startLoading(this, m, p, n);
                storagedataset.endLoading();
                return;
            }
            l = storagedataset.startLoading(this, m, p, n);
            if (t != null)
                a = t.init(l);
            else
                a = l;
            o = storagedataset.getColumns();
            b = storagedataset;
        }
        int i1 = ai.length + 1;
        boolean flag = false;
        boolean flag1 = ProviderHelp.isCopyProviderStreams(storagedataset);
        try {
            boolean flag2 = true;
            if (resultset instanceof SQLAdapter) {
                SQLAdapter sqladapter = (SQLAdapter)resultset;
                sqladapter.adapt(2, null);
                flag2 = !sqladapter.adapt(1, null);
                if (sqladapter.adapt(3, null))
                    flag1 = false;
            }
            v = false;
            k = 0;
label0:
            do {
                int i2 = 0;
                do
                    if (++i2 < i1) {
                        int j1 = ai[i2 - 1];
                        Variant variant = a[j1];
                        switch (variant.getSetType()) {
                        case 16: // '\020'
                            String s1 = resultset.getString(i2);
                            if (s1 != null) {
                                if (flag2 && o[j1].getSqlType() == 1 && s1 != null)
                                    s1 = a(s1);
                                variant.setString(s1);
                            } else {
                                variant.setAssignedNull();
                            }
                            break;

                        case 12: // '\f'
                            InputStream inputstream = resultset.getBinaryStream(i2);
                            if (inputstream != null) {
                                if (flag1 || a((Object) inputstream))
                                    inputstream = a(inputstream);
                                if (inputstream != null)
                                    variant.setInputStream(inputstream);
                                else
                                    variant.setAssignedNull();
                            } else {
                                variant.setAssignedNull();
                            }
                            break;

                        case 10: // '\n'
                          java.math.BigDecimal bigdecimal =resultset.getBigDecimal(i2, o[j1].getScale());
                            if (bigdecimal != null)
                                variant.setBigDecimal(bigdecimal);
                            else
                                variant.setAssignedNull();
                            break;

                        case 4: // '\004'
                            int k1 = resultset.getInt(i2);
                            if (k1 == 0 && resultset.wasNull())
                                variant.setAssignedNull();
                            else
                                variant.setInt(k1);
                            break;

                        case 11: // '\013'
                            boolean flag3 = resultset.getBoolean(i2);
                            if (!resultset.wasNull())
                                variant.setBoolean(flag3);
                            else
                                variant.setAssignedNull();
                            break;

                        case 2: // '\002'
                            byte byte0 = resultset.getByte(i2);
                            if (!resultset.wasNull())
                                variant.setByte(byte0);
                            else
                                variant.setAssignedNull();
                            break;

                        case 3: // '\003'
                            short word0 = resultset.getShort(i2);
                            if (!resultset.wasNull())
                                variant.setShort(word0);
                            else
                                variant.setAssignedNull();
                            break;

                        case 5: // '\005'
                            long l1 = resultset.getLong(i2);
                            if (!resultset.wasNull())
                                variant.setLong(l1);
                            else
                                variant.setAssignedNull();
                            break;

                        case 6: // '\006'
                            float f1 = resultset.getFloat(i2);
                            if (!resultset.wasNull())
                                variant.setFloat(f1);
                            else
                                variant.setAssignedNull();
                            break;

                        case 7: // '\007'
                            double d1 = resultset.getDouble(i2);
                            if (!resultset.wasNull())
                                variant.setDouble(d1);
                            else
                                variant.setAssignedNull();
                            break;

                        case 13: // '\r'
                            java.sql.Date date = resultset.getDate(i2);
                            if (date != null)
                                variant.setDate(date);
                            else
                                variant.setAssignedNull();
                            break;

                        case 14: // '\016'
                            java.sql.Time time = resultset.getTime(i2);
                            if (time != null)
                                variant.setTime(time);
                            else
                                variant.setAssignedNull();
                            break;

                        case 15: // '\017'
                            java.sql.Timestamp timestamp = resultset.getTimestamp(i2);
                            if (timestamp != null)
                                variant.setTimestamp(timestamp);
                            else
                                variant.setAssignedNull();
                            break;

                        case 17: // '\021'
                            Object obj = resultset.getObject(i2);
                            if (obj != null)
                                variant.setObject(obj);
                            else
                                variant.setAssignedNull();
                            break;
                        }
                    } else {
                        if (t != null)
                            t.coerceToColumn(o, l);
                        storagedataset.loadRow(m);
                        if (!v && (j <= 0 || ++k < j))
                            continue label0;
                        flag = true;
                        break label0;
                    }
                while (true);
            } while (resultset.next());
        }
        finally {
            if (v || !flag || q.getLoadOption() == 0 || q.getLoadOption() == 1 || !resultset.next())
                b(true);
        }
    }

    private static final boolean a(Object obj) {
        if (!u)
            try {
                boolean flag = com.borland.dx.sql.dataset.r.a(obj);
                return flag;
            }
            catch (Throwable throwable) {
                u = true;
            }
        return false;
    }

    public final void cancelLoad() {
        v = true;
    }

    ResultSet g() throws SQLException {
        return null;
    }

    private void c(boolean flag) throws SQLException, DataSetException {
        try {
            a(r, f, s);
        }
        catch (IOException ioexception) {
            DataSetException.throwExceptionChain(ioexception);
        }
        catch (SQLException sqlexception) {
            if (flag && k == 0 && (f = g()) != null)
                c(false);
            else
                DataSetException.throwExceptionChain(sqlexception);
        }
    }

    public void executeTask() throws Exception {
        c(true);
    }

    synchronized void a(boolean flag) {
        Database database = q.getDatabase();
        if (flag)
            database.addConnectionUpdateListener(this);
        else
            database.removeConnectionUpdateListener(this);
        g = flag;
    }

    void ifBusy() {
        if (g)
            DataSetException.queryInProcess();
    }

    public void ifBusy(StorageDataSet storagedataset) {
        ifBusy();
    }

    public void connectionOpening(ConnectionUpdateEvent connectionupdateevent) {
    }

    public void canChangeConnection(ConnectionUpdateEvent connectionupdateevent) throws Exception {
        ifBusy();
    }

    public void connectionClosed(ConnectionUpdateEvent connectionupdateevent) {
    }

    public void connectionChanged(ConnectionUpdateEvent connectionupdateevent) {
    }

    static Coercer b(StorageDataSet storagedataset) {
        Coercer coercer = null;
        int j1 = storagedataset.getColumnCount();
        int k1 = 0;
        int l1 = 0;
        Variant avariant[] = null;
        for (int j2 = 0; j2 < j1; j2++) {
            Column column = storagedataset.getColumn(j2);
            int i2 = column.getSqlType();
            if (i2 == 0)
                continue;
            int i1 = RuntimeMetaData.sqlTypeToVariantType(i2);
            if (column.getDataType() == i1)
                continue;
            if (avariant == null) {
                avariant = new Variant[j1];
                k1 = j2;
            }
            l1 = j2 + 1;
            avariant[j2] = new Variant(i1);
        }

        if (avariant != null)
            coercer = new Coercer(storagedataset, avariant, k1, l1);
        return coercer;
    }

    private final void b() throws SQLException {
        if (i == null) {
            Database database = q.getDatabase();
            i = RuntimeMetaData.a(database, r.getMetaDataUpdate(), f);
            a(i);
            for (int ci = 0; ci < i.length; ci++) {
              if (i[ci].getDataType() == Variant.BIGDECIMAL && i[ci].getScale() == 0)
                i[ci].setScale(-1);
            }
        } else {
            try {
                s = ProviderHelp.initData(r, i, false, false);
                t = b(r);
                return;
            }
            catch (Exception exception) { }
        }
        s = ProviderHelp.initData(r, i, true, c());
        t = b(r);
    }

    void a(Column acolumn[]) {
    }

    void b(boolean flag) throws SQLException {
        if (flag || !d)
            try {
                try {
                    if (f != null)
                        a(f);
                }
                finally {
                    f = null;
                    if (a != null)
                        b.endLoading();
                }
            }
            finally {
                w = null;
                a = null;
                l = null;
                b = null;
                f = null;
                if (g)
                    a(false);
            }
    }

    void a() {
        try {
            b(true);
        }
        catch (Exception exception) { }
    }

    void f() throws SQLException {
        s = null;
        i = null;
        b(true);
    }

    boolean d() {
        if (h)
            return true;
        else
            return r != null && ProviderHelp.isProviderPropertyChanged(r);
    }

    void d(boolean flag) {
        if (r != null && flag)
            ProviderHelp.setProviderPropertyChanged(r, true);
        h = flag;
    }

    private boolean c() {
        return x;
    }

    public void setAccumulateResults(boolean flag) {
        x = flag;
        d(true);
    }

    public boolean isAccumulateResults() {
        return x;
    }

    public boolean isLoadAsInserted() {
        return m == 4;
    }

    public void setLoadAsInserted(boolean flag) {
        if (flag)
            m = 4;
        else
            m = m;
    }

    void a(QueryDescriptor querydescriptor) {
        q = querydescriptor;
    }

    public StorageDataSet fetchDataSet() {
        if (r != null && r.getProvider() != this)
            r = null;
        return r;
    }

    void a(StorageDataSet storagedataset) {
        if (storagedataset != null && r != null && r != storagedataset)
            DataSetException.providerOwned();
        r = storagedataset;
    }

    private void h() {
        p = false;
        n = false;
        int i1 = q.getLoadOption();
        if (Beans.isDesignTime()) {
            j = r.getMaxDesignRows();
            if (j != 0 && i1 == 4)
                j = 1;
        } else {
            if (i1 == 4)
                n = true;
            j = r.getMaxRows();
            if (j != 0 && i1 == 4)
                j = 1;
            if (j == -1 && i1 == 2)
                j = 25;
            p = i1 == 1 && !r.isDetailDataSetWithFetchAsNeeded();
        }
    }

    public synchronized void close(StorageDataSet storagedataset, boolean flag) {
        try {
            b b1 = c;
            if (b1 != null) {
                if (!flag)
                    cancelLoad();
                b1.a();
            } else
            if (flag && f != null && !d) {
                j = -1;
                c(false);
            }
            b(false);
        }
        catch (SQLException sqlexception) {
            DataSetException.SQLException(sqlexception);
        }
    }

    public void provideMoreData(StorageDataSet storagedataset) {
        if (!hasMoreData(storagedataset))
            return;
        try {
            c(false);
        }
        catch (Exception exception) {
            DataSetException.providerFailed(exception);
        }
    }

    public boolean hasMoreData(StorageDataSet storagedataset) {
        return !p && f != null && !d;
    }

    public void provideData(StorageDataSet storagedataset, boolean flag) {
        a(storagedataset);
        if (flag && !q.isExecuteOnOpen())
            return;
        ifBusy();
        a(true);
        try {
            if (d())
                f();
            f = e();
            d = true;
            b();
            d(false);
            d = false;
            h();
            if (j == 0)
                b(true);
            else
            if (!p) {
                c(true);
            } else {
                c = new b(this);
                c.start();
            }
        }
        catch (Exception exception) {
            d = false;
            a();
            a(exception);
        }
    }

    abstract void a(Exception exception);

    abstract void a(ResultSet resultset) throws SQLException;

    abstract ResultSet e() throws SQLException;

    public JdbcProvider() {
        m = 8;
    }

    private int m;
    private static final long e = 1L;
    private transient StorageDataSet b;
    private transient Coercer t;
    private transient Column o[];
    private transient Variant l[];
    private transient Variant a[];
    private transient boolean p;
    private transient boolean n;
    private transient b c;
    transient StorageDataSet r;
    private transient ResultSet f;
    private boolean x;
    private transient boolean d;
    private transient boolean g;
    private transient Column i[];
    private transient int s[];
    private transient boolean h;
    private transient boolean v;
    private transient byte w[];
    private transient int k;
    private transient int j;
    private transient QueryDescriptor q;
    private static boolean u;
}


/*
    DECOMPILATION REPORT

    Decompiled from: /home/ante/projekti/devel/projects/spa/thirdparty-jars/xdx.jar
    Total time: 11 ms
    Jad reported messages/errors:
    Exit status: 0
    Caught exceptions:
*/
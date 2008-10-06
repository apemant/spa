/*jadclipse*/// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 

package com.borland.dx.memorystore;

import com.borland.dx.dataset.*;
import java.util.Locale;

// Referenced classes of package com.borland.dx.memorystore:
//            g, e, f, h, 
//            k, m, i, j, 
//            b, c, a, n, 
//            q, r, o, p, 
//            u, v, s, t

public class MemoryData extends IndexData
{

    public boolean isMemoryData()
    {
        return true;
    }

    final void a(int l)
    {
        if(g == null)
        {
            g = new TableDataSet();
            g.setColumns(i.cloneColumns());
            g.setResolvable(false);
            g.open();
            h = new Variant();
        }
        g.insertRow(false);
        for(int i1 = 0; i1 < j.length; i1++)
        {
            getVariant(l, i1, h);
            g.setVariant(i1, h);
        }

        g.post();
        emptyStoreRow(l);
    }

    final void c()
    {
        if(g != null)
        {
            g.close();
            g = null;
            h = null;
        }
    }

    public MatrixData setColumns(StorageDataSet storagedataset, Column acolumn[])
    {
        return null;
    }

    public MatrixData closeDataSet(StorageDataSet storagedataset, int l, AggManager aggmanager, StorageDataSet storagedataset1, int i1, boolean flag)
    {
        return this;
    }

    public final void getOriginalVariant(long l, int i1, Variant variant)
    {
        getVariant(c.d((int)l), i1, variant);
    }

    public final void getOriginalRow(long l, Variant avariant[])
    {
        getRowData(c.d((int)l), avariant);
    }

    public final DirectIndex createIndex(StorageDataSet storagedataset, SortDescriptor sortdescriptor, RowFilterListener rowfilterlistener, DataRow datarow, RowVariant arowvariant[], int l, int i1)
    {
        com.borland.dx.dataset.InternalRow internalrow = null;
        if(rowfilterlistener != null)
            internalrow = getInternalReadRow(storagedataset);
        Object obj = null;
        boolean flag = false;
        m m1 = null;
        if(sortdescriptor != null)
        {
            flag = sortdescriptor.isSortAsInserted();
            int j1 = sortdescriptor.keyCount();
            int i2 = j1 + (flag ? 1 : 0);
            q aq[] = new q[i2];
            Column acolumn[] = new Column[i2];
            int k2;
            for(k2 = 0; k2 < j1; k2++)
            {
                Column column = storagedataset.getColumn(sortdescriptor.getKeys()[k2]);
                if(!column.isSortable())
                    DataSetException.notSortable();
                acolumn[k2] = column;
                aq[k2] = j[column.getOrdinal()];
            }

            int l2 = getRowCount();
            if(flag)
            {
                m1 = new m(null);
                int i3 = aq.length <= 1 ? l2 : aq[0].a;
                if(i3 > ((q) (m1)).a)
                    m1.a(i3);
                aq[k2] = m1;
                k2++;
            }
            if(i2 > 0 && k2 >= i2)
            {
                obj = new e(sortdescriptor, rowfilterlistener, internalrow, this, j, l, i1, a, aq, acolumn);
                if(sortdescriptor.isUnique())
                    f = true;
            }
        }
        if(obj == null)
            obj = new i(sortdescriptor, rowfilterlistener, internalrow, this, l, i1, a);
        for(int k1 = 0; k1 < b; k1++)
            ((DirectIndex) (obj)).loadStore(k1);

        if(flag)
        {
            int l1 = ((Index) (obj)).lastRow() + 1 + 1;
            for(int j2 = 1; j2 < l1; j2++)
                m1.g(j2, j2);

        }
        ((DataIndex) (obj)).sort();
        return ((DirectIndex) (obj));
    }

    final void a(m m1)
    {
        m1.e(j[0].a, b);
    }

    private final void b(int l, int i1)
    {
        int j1 = 0;
        if(c == null)
            c = new m(null);
        a(c);
        j1 = c.d(l);
        if(j1 == 0)
            j1 = b();
        a(l, j1);
        a.g(j1, 16);
        c.g(l, j1);
    }

    private final void a(int l, Variant avariant[])
    {
        for(int i1 = 0; i1 < j.length; i1++)
            j[i1].b(l, avariant[i1]);

    }

    private final void a(int l, int i1)
    {
        for(int j1 = 0; j1 < j.length; j1++)
            j[j1].c(l, i1);

    }

    private final int b()
    {
        for(int l = 0; l < j.length; l++)
            j[l].a();

        a.a();
        return b++;
    }

    public final void getRowData(long l, Variant avariant[])
    {
        for(int i1 = 0; i1 < j.length; i1++)
            j[i1].a((int)l, avariant[i1]);

    }

    public final void getVariant(long l, int i1, Variant variant)
    {
        j[i1].a((int)l, variant);
    }

    public MatrixData restructure(StorageDataSet storagedataset, CalcFieldsListener calcfieldslistener, CalcAggFieldsListener calcaggfieldslistener)
    {
        return this;
    }

    public boolean getNeedsRestructure()
    {
        return false;
    }

    public final boolean copyStreams()
    {
        return true;
    }

    private final void b(int l)
    {
        for(int i1 = 0; i1 < super.indexesLength; i1++)
            super.indexes[i1].prepareUpdate(l);

    }

    public void replaceStoreRow(long l, RowVariant arowvariant[], int i1)
    {
        for(int j1 = 0; j1 < arowvariant.length; j1++)
            j[j1].b((int)l, arowvariant[j1]);

        setStatus(l, i1);
    }

    public void restoreStoreRow(long l)
    {
        int i1 = c.d((int)l);
        b((int)l);
        if(i1 != 0)
        {
            a(i1, (int)l);
            c(i1);
        }
        indexUpdate(l);
        if(super.updateIndex != null)
            super.updateIndex.deleteStore(l);
        setStatus(l, 8);
    }

    public final void updateStoreRow(long l, RowVariant arowvariant[], Column acolumn[])
    {
        if(f)
            a(l, arowvariant, true);
        int i1 = saveRow(getStatus((int)l));
        b((int)l);
        if(super.saveOriginal)
        {
            b((int)l, i1);
            if((i1 & 2) == 0)
            {
                i1 |= 2;
                setStatus(l, i1);
                super.updateIndex.addStore(l);
            } else
            {
                setStatus(l, i1);
            }
        }
        if(acolumn != null)
        {
            for(int l1 = 0; l1 < acolumn.length; l1++)
            {
                int j1 = acolumn[l1].getOrdinal();
                j[j1].b((int)l, arowvariant[j1]);
            }

        } else
        {
            for(int k1 = 0; k1 < arowvariant.length; k1++)
                j[k1].b((int)l, arowvariant[k1]);

        }
        indexUpdate(l);
    }

    private void a(long l, RowVariant arowvariant[], boolean flag)
    {
        for(int i1 = 0; i1 < super.indexesLength; i1++)
            super.indexes[i1].uniqueCheck(l, arowvariant, flag);

    }

    private final void c(int l)
    {
        for(int i1 = 0; i1 < j.length; i1++)
            j[i1].b(l, RowVariant.getNullVariant());

    }

    public final void emptyStoreRow(long l)
    {
        indexDelete(l);
        int i1 = a.d((int)l);
        if((i1 & 6) != 0)
            if((i1 & 4) != 0)
            {
                if(super.insertIndex != null)
                    super.insertIndex.deleteStore(l);
            } else
            if((i1 & 2) != 0 && super.updateIndex != null)
            {
                super.updateIndex.deleteStore(l);
                c(c.d((int)l));
            }
        if(super.deleteIndex != null)
            super.deleteIndex.deleteStore(l);
        a.g((int)l, 0);
        c((int)l);
    }

    public final void deleteStoreRow(long l)
    {
        int i1 = a.d((int)l);
        a.g((int)l, i1 | 1);
    }

    public final long insertRow(ReadRow readrow, RowVariant arowvariant[], int l)
    {
        if(f)
            a(0L, arowvariant, false);
        int i1 = b();
        a.g(i1, l);
        for(int j1 = 0; j1 < arowvariant.length; j1++)
            j[j1].b(i1, arowvariant[j1]);

        if(super.insertIndex != null && (l & 4) != 0 && super.resolvable)
            super.insertIndex.addStore(i1);
        indexAdd(i1);
        return (long)i1;
    }

    public boolean isEmpty()
    {
        return getRowCount() <= 1;
    }

    public final int getRowCount()
    {
        return b;
    }

    public final void setStatus(long l, int i1)
    {
        a.g((int)l, i1);
    }

    public final int getStatus(long l)
    {
        return a.d((int)l);
    }

    h a()
    {
        if(d == null || d.d >= 6)
        {
            return d = new h();
        } else
        {
            d.d += 2;
            return d;
        }
    }

    public void dropColumn(int l)
    {
        q aq[] = new q[j.length - 1];
        MatrixData.setNeedsRecalc(i, true);
        System.arraycopy(j, 0, aq, 0, l);
        if(l + 1 < j.length)
            System.arraycopy(j, l + 1, aq, l, j.length - (l + 1));
        j = aq;
    }

    private final q a(Column column, h h1)
    {
        switch(column.getCalcType())
        {
        case 2: // '\002'
        case 3: // '\003'
            return new r(h1);
        }
        switch(column.getDataType())
        {
        case 16: // '\020'
            Locale locale = column.getLocale();
            if(locale == null || locale.getLanguage().equals("en"))
                return new c(h1);
            else
                return new k(h1, locale);

        case 12: // '\f'
            return new u(h1);

        case 2: // '\002'
            return new s(h1);

        case 3: // '\003'
            return new f(h1);

        case 4: // '\004'
            return new m(h1);

        case 11: // '\013'
            return new t(h1);

        case 6: // '\006'
            return new n(h1);

        case 7: // '\007'
            return new o(h1);

        case 5: // '\005'
            return new j(h1);

        case 10: // '\n'
          if (column.getScale() >=0 && column.getScale() <=7)
            return new obd(h1, column.getScale());
          return new v(h1);

        case 14: // '\016'
            return new b(h1);

        case 15: // '\017'
            return new a(h1);

        case 13: // '\r'
            return new p(h1);

        case 17: // '\021'
            return new g(h1);

        case 8: // '\b'
        case 9: // '\t'
        default:
            return null;
        }
    }

    public final boolean validColumnType(Column column)
    {
        switch(column.getDataType())
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        case 6: // '\006'
        case 7: // '\007'
        case 10: // '\n'
        case 11: // '\013'
        case 12: // '\f'
        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
        case 16: // '\020'
        case 17: // '\021'
            return true;

        case 8: // '\b'
        case 9: // '\t'
        default:
            return false;
        }
    }

    public void updateProperties(StorageDataSet storagedataset)
    {
        super.resolvable = storagedataset.isResolvable();
        if(super.insertIndex == null && super.resolvable)
        {
            super.insertIndex = openIndex(storagedataset, null, null, 4, 0, true);
            super.deleteIndex = openIndex(storagedataset, null, null, 1, 0, true);
            super.updateIndex = openIndex(storagedataset, null, null, 2, 0, true);
        }
    }

    public void openData(StorageDataSet storagedataset, boolean flag)
    {
        updateProperties(storagedataset);
    }

    public void moveColumn(int l, int i1)
    {
        MatrixData.setNeedsRecalc(i, true);
        q q1 = j[l];
        int j1 = l;
        if(i1 < l)
            for(; j1 > i1; j1--)
                j[j1] = j[j1 - 1];

        else
            for(; i1 > j1; j1++)
                j[j1] = j[j1 + 1];

        j[j1] = q1;
        dropAllIndexes();
    }

    public void changeColumn(int l, Column column, Column column1)
    {
        if(column.getDataType() != column1.getDataType())
        {
            a(column1);
            j[l] = a(column1, a());
            dropAllIndexes();
        }
    }

    public final void addColumn(Column column)
    {
        a(column);
        MatrixData.setNeedsRecalc(i, true);
        int l = j != null ? j.length + 1 : 1;
        q aq[] = new q[l];
        if(l > 1)
            System.arraycopy(j, 0, aq, 0, l - 1);
        aq[l - 1] = a(column, a());
        if(l > 1)
        {
            aq[l - 1].e(aq[0].a, b);
        } else
        {
            b = 1;
            a.g = 1;
        }
        j = aq;
        dropAllIndexes();
    }

    private final void a(Column column)
    {
        if(!validColumnType(column))
            DataSetException.invalidColumnType(column);
    }

    MemoryData(StorageDataSet storagedataset)
    {
        a = new m(null);
        j = new q[0];
        b = 1;
        MatrixData.setNeedsRecalc(storagedataset, true);
        i = storagedataset;
    }

    StorageDataSet i;
    Variant h;
    TableDataSet g;
    private boolean e;
    private h d;
    private boolean f;
    private int b;
    q j[];
    private m c;
    private m a;
}



/***** DECOMPILATION REPORT *****

    DECOMPILED FROM: /home/abf/projekti/devel/projects/spa/thirdparty-jars/dx.jar


    TOTAL TIME: 133 ms


    JAD REPORTED MESSAGES/ERRORS:


    EXIT STATUS:    0


    CAUGHT EXCEPTIONS:

 ********************************/
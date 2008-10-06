/*jadclipse*/// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 

package com.borland.dx.memorystore;

import com.borland.dx.dataset.*;

// Referenced classes of package com.borland.dx.memorystore:
//            i, MemoryData, l, q, 
//            k, m

class e extends i
{

    public int moveRow(int i1, int j1)
    {
        int k1 = i1 + j1;
        if(k1 > super.f)
            j1 = super.f - i1;
        if(k1 < 0)
            j1 = -i1;
        k1 = i1 + j1;
        if(b.length > 1)
        {
            i = b.length - 1;
            if(k1 < i1)
            {
                int l1 = a(super.b[i1], true);
                if(k1 < l1)
                    j1 = i1 - l1;
            } else
            {
                int i2 = a(super.b[i1], false);
                if(k1 > i2)
                    j1 = i1 - i2;
            }
        }
        if(j1 != 0)
        {
            long l2 = super.b[i1];
            int j2 = d.d((int)l2);
            deleteStore(l2);
            c = i1 + j1;
            addStore(l2);
            c = -1;
            return j1;
        } else
        {
            return 0;
        }
    }

    public void setInsertPos(int i1)
    {
        c = i1;
    }

    public void uniqueCheck(long l1, RowVariant arowvariant[], boolean flag)
    {
        if(super.descriptor.isUnique() && super.f > -1)
        {
            for(int i1 = 0; i1 < h; i1++)
                if(!flag || arowvariant[g[i1].getOrdinal()].changed)
                {
                    for(int j1 = 0; j1 < h; j1++)
                        b[j1].b(0, arowvariant[g[j1].getOrdinal()]);

                    i = h;
                    a(0, true);
                    if(super.h == 0)
                        ValidationException.duplicateKey(super.e.i, super.descriptor);
                    return;
                }

        }
    }

    private final int h(int i1)
    {
        if(super.locatePartialIndex != -1)
        {
            if(c(i1))
                return i1;
        } else
        {
            for(; d(0, super.b[i1]) == 0 && i1 > -1; i1--)
                if(h == super.locateColumnCount || c(i1))
                    return i1;

        }
        return -1;
    }

    private final int i(int i1)
    {
        int j1 = super.f + 1;
        if(super.locatePartialIndex != -1)
        {
            for(; d(0, super.b[i1]) > 0 && i1 < j1; i1++);
            if(c(i1))
                return i1;
        } else
        {
            for(; d(0, super.b[i1]) == 0 && i1 < j1; i1++)
                if(h == super.locateColumnCount || c(i1))
                    return i1;

        }
        return -1;
    }

    final int a(int i1, boolean flag)
    {
        int j1 = 0;
        int k1 = super.f;
        byte byte0 = -1;
        do
        {
            int l1 = (j1 + k1) / 2;
            d(i1, super.b[l1]);
            if(super.h == 0)
            {
                if(flag)
                {
                    if(k1 == l1)
                        return l1;
                    k1 = l1;
                } else
                {
                    if(j1 == l1)
                    {
                        if(j1 == k1)
                            return l1;
                        d(i1, super.b[k1]);
                        if(super.h == 0)
                        {
                            return k1;
                        } else
                        {
                            super.h = 0;
                            return l1;
                        }
                    }
                    j1 = l1;
                }
            } else
            if(super.h > 0)
            {
                if(j1 >= k1)
                    return l1;
                j1 = l1 + 1;
            } else
            if(super.h < 0)
            {
                if(k1 <= j1)
                    return l1;
                k1 = l1 - 1;
            }
        } while(true);
    }

    public int locate(int i1, int j1)
    {
        if(i1 > super.f)
            return -1;
        int k1 = 0;
        if(h <= super.locateColumnCount)
            for(; k1 < h && g[k1].getOrdinal() == super.locateColumns[k1].getOrdinal(); k1++);
        i = k1;
        if(i != super.locateColumnCount || ((j1 & 8) != 0) != j)
            return super.locate(i1, j1);
        if((j1 & 2) != 0)
            return i(i1);
        if((j1 & 4) != 0)
            return h(i1);
        boolean flag = (j1 & 64) == 0;
        if((j1 & 32) != 0)
            flag = true;
        int l1 = a(0, flag);
        if(super.h == 0 || super.locatePartialIndex != -1)
        {
            if(h == super.locateColumnCount && super.locatePartialIndex == -1)
                return l1;
            if(flag)
                return i(l1);
            else
                return h(l1);
        } else
        {
            return -1;
        }
    }

    public void sort()
    {
        for (int ci = 0; ci < b.length; ci++)
          if(b[ci] instanceof k)
            ((k)b[ci]).bkb(j);
        
        f.a();
        c(0, super.f);
        if(!j)
            f.b();
        if(super.descriptor.isUnique())
            f();
        else
            d();
        if(e)
            e();
        
        for (int ci = 0; ci < b.length; ci++)
          if(b[ci] instanceof k)
            ((k)b[ci]).bkbc();
    }

    private final void e()
    {
        int i1 = (super.f + 1) / 2;
        for(int k1 = 0; k1 < i1; k1++)
        {
            int j1 = super.b[k1];
            super.b[k1] = super.b[super.f - k1];
            super.b[super.f - k1] = j1;
        }

    }

    private final void f()
    {
        int l1 = super.f + 1;
        int j2 = 0;
        int i1 = 0;
        do
        {
            if(++i1 >= l1)
                break;
            int i2 = f.a(i1, j2, false);
            if(f.l == 0 && i2 > -1)
                j2--;
            if(f.l == 0)
            {
                super.e.a(super.b[i1]);
            } else
            {
                int j1 = i1 + j2;
                if(i2 < j1)
                {
                    int k1 = super.b[i1];
                    while(i2 < j1) 
                        super.b[j1] = super.b[--j1];
                    super.b[j1] = k1;
                } else
                if(j2 < 0)
                    super.b[j1] = super.b[i1];
            }
        } while(true);
        super.f += j2;
    }

    private final void d()
    {
        int l1 = super.f + 1;
        int i1 = 0;
        do
        {
            if(++i1 >= l1)
                break;
            int i2 = f.a(i1, 0, true);
            if(i2 < i1)
            {
                int k1 = super.b[i1];
                int j1;
                for(j1 = i1; i2 < j1;)
                    super.b[j1] = super.b[--j1];

                super.b[j1] = k1;
            }
        } while(true);
    }

    private final void c(int i1, int j1)
    {
        while(j1 - i1 > 4) 
        {
            int k1 = (i1 + j1) / 2;
            if(f.a(super.b[i1], super.b[k1]) > 0)
            {
                int l1 = super.b[i1];
                super.b[i1] = super.b[k1];
                super.b[k1] = l1;
            }
            if(f.a(super.b[i1], super.b[j1]) > 0)
            {
                int i2 = super.b[i1];
                super.b[i1] = super.b[j1];
                super.b[j1] = i2;
            }
            if(f.a(super.b[k1], super.b[j1]) > 0)
            {
                int j2 = super.b[k1];
                super.b[k1] = super.b[j1];
                super.b[j1] = j2;
            }
            int l2 = i1;
            int i3 = j1 - 1;
            int k2 = super.b[k1];
            super.b[k1] = super.b[i3];
            super.b[i3] = k2;
            f.a(i3);
            do
            {
                f.b(l2, i3);
                l2 = f.e;
                i3 = f.a;
                if(l2 >= i3)
                    break;
                k2 = super.b[l2];
                super.b[l2] = super.b[i3];
                super.b[i3] = k2;
            } while(true);
            k2 = super.b[l2];
            super.b[l2] = super.b[j1 - 1];
            super.b[j1 - 1] = k2;
            if(l2 - i1 > j1 - l2)
            {
                c(l2 + 1, j1);
                j1 = l2 - 1;
            } else
            {
                c(i1, l2 - 1);
                i1 = l2 + 1;
            }
        }
    }

    final int d(int i1, int j1)
    {
        f.a(i, i1, j1);
        if(e)
            super.h = -f.l;
        else
            super.h = f.l;
        return super.h;
    }

    final void a(int i1, int j1)
    {
        f.a(i1, j1);
        if(e)
            super.h = -f.l;
        else
            super.h = f.l;
    }

    public final void updateStore(long l1)
    {
        int i1 = (int)l1;
        if(a(i1))
        {
            if(a > 0)
            {
                a(i1, super.b[a - 1]);
                if(super.h <= 0)
                {
                    g(a);
                    addStore(i1);
                    a = -1;
                    return;
                }
            }
            if(a < super.f)
            {
                a(i1, super.b[a + 1]);
                if(super.h >= 0)
                {
                    g(a);
                    addStore(i1);
                    a = -1;
                    return;
                }
            }
        } else
        {
            g(a);
        }
        a = -1;
    }

    public final void prepareUpdate(long l1)
    {
        a = findClosest(l1);
    }

    public final boolean addStore(long l1)
    {
        if(a((int)l1))
        {
            if(d != null)
            {
                if(super.e.getRowCount() >= super.a)
                    a();
                d.a(super.b, (int)l1, c, super.f + 1);
            }
            int i1 = findClosest(l1);
            if(i1 < 0)
                i1 = 0;
            b(i1, (int)l1);
            return true;
        } else
        {
            return false;
        }
    }

    void d(int i1)
    {
        super.d(i1);
        if(d != null)
            super.e.a(d);
        f.a(super.b);
    }

    e(SortDescriptor sortdescriptor, RowFilterListener rowfilterlistener, InternalRow internalrow, MemoryData memorydata, q aq[], int i1, int j1, 
            m m1, q aq1[], Column acolumn[])
    {
        super(sortdescriptor, rowfilterlistener, internalrow, memorydata, i1, j1, m1);
        j = sortdescriptor.isCaseInsensitive();
        f = new l(super.b, aq1, sortdescriptor);
        b = aq1;
        g = acolumn;
        h = acolumn.length;
        if(sortdescriptor.isSortAsInserted())
        {
            h--;
            d = (m)aq1[aq1.length - 1];
        }
        e = sortdescriptor.isDescending();
    }

    private int c;
    private m d;
    private int i;
    private l f;
    private int a;
    private boolean j;
    private boolean e;
    private int h;
    private Column g[];
    private q b[];
}



/***** DECOMPILATION REPORT *****

    DECOMPILED FROM: /home/abf/projekti/devel/projects/spa/thirdparty-jars/dx.jar


    TOTAL TIME: 146 ms


    JAD REPORTED MESSAGES/ERRORS:


    EXIT STATUS:    0


    CAUGHT EXCEPTIONS:

 ********************************/
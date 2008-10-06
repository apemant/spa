/*jadclipse*/// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 

package com.borland.dx.memorystore;

import java.math.BigDecimal;

import com.borland.dx.dataset.Variant;

// Referenced classes of package com.borland.dx.memorystore:
//            q, h

class obd extends q
{

    void b(int i, Variant variant)
    {
        if(variant.isNull())
        {
            a[i] = 9223372036854775807L;
            f(i, variant.getType());
        } else
        {
            if(super.j)
                super.d.b[i] &= ~super.e;
            a[i] = variant.getBigDecimal().setScale(sc, BigDecimal.ROUND_HALF_UP).
                    unscaledValue().longValue();
        }
    }

    void a(int i, Variant variant)
    {
        if(super.j && (super.d.b[i] & super.e) != 0)
            super.d.a(i, variant, super.e, super.l);
        else
            variant.setBigDecimal(BigDecimal.valueOf(a[i], sc));
    }

    int b(int i, int k)
    {
        return a(i, k);
    }

    int a(int i, int k)
    {
        if(super.j && (super.k = super.d.a(i, k, super.e)) != 0)
            return super.k;
        long l = a[i];
        long l1 = a[k];
        if(l < l1)
            return -1;
        return l <= l1 ? 0 : 1;
    }

    void a(int i)
    {
        long al[] = new long[i];
        System.arraycopy(a, 0, al, 0, super.a);
        a = al;
        super.a = a.length;
    }

    void c(int i, int k)
    {
        a[k] = a[i];
        if(super.j)
            super.d.b(i, k, super.e);
    }

    public obd(h h1, int sc)
    {
        super(h1);
        a = new long[16];
        super.a = a.length;
        this.sc = sc;
    }

    long a[];
    long b;
    int sc;
}



/***** DECOMPILATION REPORT *****

    DECOMPILED FROM: /home/abf/projekti/devel/projects/spa/thirdparty-jars/dx.jar


    TOTAL TIME: 73 ms


    JAD REPORTED MESSAGES/ERRORS:


    EXIT STATUS:    0


    CAUGHT EXCEPTIONS:

 ********************************/
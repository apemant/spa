/*jadclipse*/// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 

package com.borland.dx.memorystore;

import com.borland.dx.dataset.Variant;
import java.text.*;
import java.util.Locale;

// Referenced classes of package com.borland.dx.memorystore:
//            c, h, q

class k extends c
{

    void b()
    {
      throw new UnsupportedOperationException("Method b() has been hijackeD! Run!!!");
    }
    
    void bkb(boolean ic) {
      if (ic && b == null) {
        b = new CollationKey[((q) this).a];
        for(int i = 1; i < ((q) this).a; i++)
            b[i] = bd.getCollationKey(super.a[i] == null ? "" : super.a[i].replace(' ', '_'));
      } else if (!ic && a == null) {
        a = new CollationKey[((q) this).a];
        for(int j = 1; j < ((q) this).a; j++)
            a[j] = ad.getCollationKey(super.a[j] == null ? "" : super.a[j].replace(' ', '_'));
      }
    }
    
    void bkbc() {
      b = null;
      a = null;
    }

    void b(int i, Variant variant)
    {
        super.b(i, variant);
        if(b != null)
        {
            b[i] = bd.getCollationKey(super.a[i] == null ? "" : super.a[i].replace(' ', '_'));
        }
        if (a != null) {
            a[i] = ad.getCollationKey(super.a[i] == null ? "" : super.a[i].replace(' ', '_'));
        }
    }

    void a(int i)
    {
        int j = ((q) this).a;
        super.a(i);
        if(b != null)
        {
            CollationKey acollationkey[] = new CollationKey[i];
            System.arraycopy(b, 0, acollationkey, 0, j);
            b = acollationkey;
        }
        if(a != null)
        {
          CollationKey acollationkey[] = new CollationKey[i];
          System.arraycopy(a, 0, acollationkey, 0, j);
          a = acollationkey;
        }
    }

    int b(int i, boolean flag, boolean flag1)
    {
        if(flag1)
        {
            if(flag) {
                if (b == null)
                  while((super.k = bd.compare(super.a[super.h[--i]], super.a[c])) < 0) ;
                else while((super.k = b[super.h[--i]].compareTo(b[c])) < 0) ;
            } else {
              if (a == null)
                while((super.k = ad.compare(super.a[super.h[--i]], super.a[c])) < 0) ;
              else while((super.k = a[super.h[--i]].compareTo(a[c])) < 0) ;
            }
        } else if(flag) {
          if (b == null)
            while((super.k = bd.compare(super.a[super.h[--i]], super.a[c])) > 0) ;
          else while((super.k = b[super.h[--i]].compareTo(b[c])) > 0) ;
        } else {
          if (a == null)
            while((super.k = ad.compare(super.a[super.h[--i]], super.a[c])) > 0) ;
          else while((super.k = a[super.h[--i]].compareTo(a[c])) > 0) ;
        }
        return i;
    }

    int a(int i, boolean flag, boolean flag1)
    {
        if(flag1)
        {
            if(flag) {
              if (b == null)
                while((super.k = bd.compare(super.a[super.h[++i]], super.a[c])) > 0) ;
              else while((super.k = b[super.h[++i]].compareTo(b[c])) > 0) ;
            } else {
              if (a == null)
                while((super.k = ad.compare(super.a[super.h[++i]], super.a[c])) > 0) ;
              else while((super.k = a[super.h[++i]].compareTo(a[c])) > 0) ;
            }
        } else if(flag) {
          if (b == null)
            while((super.k = bd.compare(super.a[super.h[++i]], super.a[c])) < 0) ;
          else while((super.k = b[super.h[++i]].compareTo(b[c])) < 0) ;
        } else {
          if (a == null)
            while((super.k = ad.compare(super.a[super.h[++i]], super.a[c])) < 0) ;
          else while((super.k = a[super.h[++i]].compareTo(a[c])) < 0) ;
        }
        return i;
    }

    void a(int ai[], int i)
    {
        super.h = ai;
        super.b = super.a[i];
        c = i;
    }

    final int b(int i, int j)
    {
        if(super.j && (super.k = super.d.a(i, j, super.e)) != 0)
            return super.k;
        if (b == null)
          return bd.compare(super.a[i], super.a[j]);
        return b[i].compareTo(b[j]);
    }

    final int a(int i, int j)
    {
        if(super.j && (super.k = super.d.a(i, j, super.e)) != 0)
            return super.k;
        if (a == null)
          return ad.compare(super.a[i], super.a[j]);
        return a[i].compareTo(a[j]);
    }

    public k(h h1, Locale locale)
    {
        super(h1);
        
        ad = (RuleBasedCollator)Collator.getInstance(locale);
        ad.setStrength(Collator.TERTIARY);
        
        bd = (RuleBasedCollator)Collator.getInstance(locale);
        bd.setStrength(Collator.SECONDARY);
        
        b = null;
        a = null;
    }

    int c;
    CollationKey a[];
    CollationKey b[];
    RuleBasedCollator ad;
    RuleBasedCollator bd;
}


/***** DECOMPILATION REPORT *****

    DECOMPILED FROM: /home/abf/projekti/devel/projects/spa/thirdparty-jars/dx.jar


    TOTAL TIME: 139 ms


    JAD REPORTED MESSAGES/ERRORS:


    EXIT STATUS:    0


    CAUGHT EXCEPTIONS:

 ********************************/
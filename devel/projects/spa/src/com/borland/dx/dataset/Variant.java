/*jadclipse*/// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 

package com.borland.dx.dataset;

import com.borland.jb.util.ExceptionChain;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.borland.dx.dataset:
//            VariantException, Res, LocalDateUtil

public class Variant
    implements Serializable, Cloneable
{

    private void a(ObjectInputStream objectinputstream)
        throws IOException, ClassNotFoundException
    {
        objectinputstream.defaultReadObject();
        m = objectinputstream.readObject();
    }

    private void a(ObjectOutputStream objectoutputstream)
        throws IOException
    {
        objectoutputstream.defaultWriteObject();
        if(m instanceof Serializable)
            objectoutputstream.writeObject(m);
        else
            objectoutputstream.writeObject(null);
    }

    public Object clone()
    {
        Variant variant = new Variant(j);
        variant.setVariant(this);
        return variant;
    }

    public void subtract(Variant variant, Variant variant1)
    {
        if(variant.isNull() && isNull())
            variant1.setVariant(this);
        else
            switch(e)
            {
            case 2: // '\002'
            case 3: // '\003'
            case 4: // '\004'
                variant1.setInt(o - variant.getAsInt());
                break;

            case 5: // '\005'
                variant1.setLong(n - variant.getAsLong());
                break;

            case 6: // '\006'
                variant1.setFloat(p - variant.getAsFloat());
                break;

            case 7: // '\007'
                variant1.setDouble(q - variant.getAsDouble());
                break;

            case 10: // '\n'
                variant1.setBigDecimal(getBigDecimal().subtract(variant.getAsBigDecimal()));
                break;

            case 0: // '\0'
            case 1: // '\001'
                variant1.setVariant(variant);
                break;
            }
    }

    public void add(Variant variant, Variant variant1)
    {
        if(variant.isNull() && isNull())
            variant1.setVariant(this);
        else
            switch(e)
            {
            case 2: // '\002'
                variant1.setByte((byte)(o + variant.getAsInt()));
                break;

            case 3: // '\003'
                variant1.setShort((short)(o + variant.getAsInt()));
                break;

            case 4: // '\004'
                variant1.setInt(o + variant.getAsInt());
                break;

            case 5: // '\005'
                variant1.setLong(n + variant.getAsLong());
                break;

            case 13: // '\r'
                variant1.setDate(getDate().getTime() + variant.getDate().getTime());
                // fall through

            case 14: // '\016'
                variant1.setTime(getTime().getTime() + variant.getTime().getTime());
                // fall through

            case 6: // '\006'
                variant1.setFloat(p + variant.getAsFloat());
                break;

            case 7: // '\007'
                variant1.setDouble(q + variant.getAsDouble());
                break;

            case 10: // '\n'
                variant1.setBigDecimal(getBigDecimal().add(variant.getAsBigDecimal()));
                break;

            case 0: // '\0'
            case 1: // '\001'
                variant1.setVariant(variant);
                break;
            }
    }

    public int compareTo(Variant variant)
    {
        if(isNull())
            return variant.isNull() ? 0 : -1;
        if(variant.isNull())
            return 1;
        switch(e)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            return a(o, variant.getAsInt());

        case 5: // '\005'
            return a(n, variant.getAsLong());

        case 6: // '\006'
            return a(p, variant.getAsFloat());

        case 7: // '\007'
            return a(q, variant.getAsDouble());

        case 10: // '\n'
            return getBigDecimal().compareTo(variant.getAsBigDecimal());

        case 13: // '\r'
            return a(variant);

        case 14: // '\016'
            return b(variant);

        case 15: // '\017'
            return a(f, variant.getTimestamp());

        case 11: // '\013'
            return a(t, variant.getBoolean());

        case 16: // '\020'
            return i.compareTo(variant.getString());

        case 8: // '\b'
        case 9: // '\t'
        case 12: // '\f'
        default:
            return 0;
        }
    }

    private final int a(boolean flag, boolean flag1)
    {
        if(flag == flag1)
            return 0;
        return !flag ? -1 : 1;
    }

    private final int a(Timestamp timestamp, Timestamp timestamp1)
    {
        int i1 = a(timestamp.getTime(), timestamp1.getTime());
        if(i1 == 0)
            return timestamp.getNanos() - timestamp1.getNanos();
        else
            return i1;
    }

    private final int a(Variant variant)
    {
        int i1 = getAsInt();
        int j1 = variant.getAsInt();
        return a(i1, j1);
    }

    private final int b(Variant variant)
    {
        int i1 = getAsInt();
        int j1 = variant.getAsInt();
        return a(i1, j1);
    }

    private final int a(float f1, float f2)
    {
        if(f1 < f2)
            return -1;
        return f1 <= f2 ? 0 : 1;
    }

    private final int a(double d1, double d2)
    {
        if(d1 < d2)
            return -1;
        return d1 <= d2 ? 0 : 1;
    }

    private final int a(long l1, long l2)
    {
        if(l1 < l2)
            return -1;
        return l1 <= l2 ? 0 : 1;
    }

    private final int a(int i1, int j1)
    {
        if(i1 < j1)
            return -1;
        return i1 <= j1 ? 0 : 1;
    }

    private boolean a(InputStream inputstream, InputStream inputstream1)
    {
        if(inputstream == inputstream1)
            return true;
        if(inputstream == null || inputstream1 == null)
            return false;
        if(!inputstream.markSupported() || !inputstream1.markSupported())
            return false;
        try
        {
            inputstream.reset();
            inputstream1.reset();
            int i1 = 1;
            byte abyte0[] = new byte[1024];
            byte abyte1[] = new byte[1024];
            while(i1 > 0) 
            {
                i1 = inputstream.read(abyte0);
                int j1 = inputstream1.read(abyte1);
                if(i1 != j1)
                {
                    boolean flag1 = false;
                    return flag1;
                }
                int k1 = 0;
                while(k1 < i1) 
                {
                    if(abyte0[k1] != abyte1[k1])
                    {
                        boolean flag2 = false;
                        return flag2;
                    }
                    k1++;
                }
            }
        }
        catch(IOException ioexception)
        {
            boolean flag = false;
            return flag;
        }
        return true;
    }

    private boolean a(char ac[], char ac1[])
    {
        int i1 = ac.length;
        if(i1 != ac1.length)
            return false;
        for(int j1 = 0; j1 < i1; j1++)
            if(ac[j1] != ac1[j1])
                return false;

        return true;
    }

    public final boolean equals(Variant variant)
    {
        if(e != variant.e)
        {
            if(e <= 1 || variant.e <= 1)
                return false;
            a(variant.e, e, true);
        }
        switch(e)
        {
        case 0: // '\0'
        case 1: // '\001'
            return variant.e == e;

        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            return o == variant.o;

        case 11: // '\013'
            return t == variant.t;

        case 6: // '\006'
            return p == variant.p;

        case 7: // '\007'
            return q == variant.q;

        case 15: // '\017'
            if(f.getNanos() != variant.getTimestamp().getNanos())
                return false;
            else
                return f.getTime() == variant.getTimestamp().getTime();

        case 13: // '\r'
        case 14: // '\016'
            return getAsLong() == variant.getAsLong();

        case 5: // '\005'
            return n == variant.n;

        case 10: // '\n'
            if(getBigDecimal() == variant.getBigDecimal())
                return true;
            else
                return u.compareTo(variant.u) == 0;

        case 16: // '\020'
            if(i == variant.i)
                return true;
            else
                return i.equals(variant.i);

        case 18: // '\022'
            if(o == -2)
                getByteArray();
            variant.getByteArray();
            return c(variant);

        case 12: // '\f'
            if(o >= 0 && o == variant.o)
                return c(variant);
            else
                return a(getInputStream(), variant.getInputStream());

        case 17: // '\021'
            if(o != -3 && variant.o != -3)
                return a(getInputStream(), variant.getInputStream());
            if(getObject() == variant.getObject())
                return true;
            else
                return getObject().equals(variant.getObject());

        case 8: // '\b'
        case 9: // '\t'
        default:
            return false;
        }
    }

    private final boolean c(Variant variant)
    {
        if(o == variant.o)
        {
            if(s == variant.s)
                return true;
            int i1;
            for(i1 = 0; i1 < o && s[i1] == variant.s[i1]; i1++);
            return i1 == o;
        } else
        {
            return false;
        }
    }

    public boolean equalsInstance(Variant variant)
    {
        if(e == variant.e)
            switch(e)
            {
            default:
                break;

            case 12: // '\f'
                if(o == -2 && variant.o == -2)
                    return getInputStream() == variant.getInputStream();
                break;

            case 17: // '\021'
                if(o == -3 && variant.o == -3)
                    return getObject() == variant.getObject();
                break;
            }
        return equals(variant);
    }

    public final String toString()
    {
        switch(e)
        {
        case 0: // '\0'
        case 1: // '\001'
            return "";

        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            return Integer.toString(o, 10);

        case 6: // '\006'
            return Float.toString(p);

        case 7: // '\007'
            return Double.toString(q);

        case 5: // '\005'
            return Long.toString(n, 10);

        case 10: // '\n'
            if(t)
                return i;
            if(u == null)
                return "";
            else
                return u.toString();

        case 11: // '\013'
            return t ? "true" : "false";

        case 16: // '\020'
            if(i == null)
                return "";
            else
                return i;

        case 13: // '\r'
            return o != -5 ? LocalDateUtil.getLocalDateAsString(this) : r.toString();

        case 14: // '\016'
            return o != -6 ? LocalDateUtil.getLocalTimeAsString(this) : h.toString();

        case 15: // '\017'
            return f.toString();

        case 18: // '\022'
            if(s == null)
                return "";
            else
                return new String(s, 0, o);

        case 17: // '\021'
            getObject();
            if(m == null)
                return "";
            else
                return m.toString();

        case 12: // '\f'
            getInputStream();
            if(m == null)
                return "";
            else
                return m.toString();

        case 8: // '\b'
        case 9: // '\t'
        default:
            return "";
        }
    }

    public void setAsObject(Object obj, int i1)
    {
        switch(i1)
        {
        case 1: // '\001'
            setAssignedNull();
            break;

        case 0: // '\0'
            setUnassignedNull();
            break;

        case 4: // '\004'
            setInt(((Integer)obj).intValue());
            break;

        case 2: // '\002'
            setByte(((Byte)obj).byteValue());
            break;

        case 3: // '\003'
            setShort(((Short)obj).shortValue());
            break;

        case 6: // '\006'
            setFloat(((Float)obj).floatValue());
            break;

        case 7: // '\007'
            setDouble(((Double)obj).doubleValue());
            break;

        case 5: // '\005'
            setLong(((Long)obj).longValue());
            break;

        case 10: // '\n'
            setBigDecimal((BigDecimal)obj);
            break;

        case 11: // '\013'
            setBoolean(((Boolean)obj).booleanValue());
            break;

        case 16: // '\020'
            setString(obj.toString());
            break;

        case 13: // '\r'
            setDate((Date)obj);
            break;

        case 14: // '\016'
            setTime((Time)obj);
            break;

        case 15: // '\017'
            setTimestamp((Timestamp)obj);
            break;

        case 18: // '\022'
            setByteArray((byte[])obj, ((byte[])obj).length);
            break;

        case 17: // '\021'
            setObject(obj);
            break;

        case 12: // '\f'
            setInputStream((InputStream)obj);
            break;
        }
    }

    public Object getAsObject()
    {
        switch(e)
        {
        case 0: // '\0'
        case 1: // '\001'
            return null;

        case 4: // '\004'
            return new Integer(o);

        case 2: // '\002'
            return new Byte((byte)o);

        case 3: // '\003'
            return new Short((short)o);

        case 6: // '\006'
            return new Float(p);

        case 7: // '\007'
            return new Double(q);

        case 5: // '\005'
            return new Long(n);

        case 10: // '\n'
            if(t)
                return new BigDecimal(i);
            if(u == null)
                return new BigDecimal("0");
            else
                return u;

        case 11: // '\013'
            return new Boolean(t);

        case 16: // '\020'
            if(i == null)
                return "";
            else
                return i;

        case 13: // '\r'
            return new Date(getDate().getTime());

        case 14: // '\016'
            return new Time(getTime().getTime());

        case 15: // '\017'
            return new Timestamp(f.getTime());

        case 18: // '\022'
            if(s == null)
                return null;
            else
                return s;

        case 17: // '\021'
            getObject();
            if(m == null)
                return null;
            else
                return m;

        case 12: // '\f'
            getInputStream();
            if(m == null)
                return null;
            else
                return m;

        case 8: // '\b'
        case 9: // '\t'
        default:
            return null;
        }
    }

    public final int getStoreType()
    {
        switch(e)
        {
        case 12: // '\f'
        default:
            if(m == null && s != null)
                return 18;
            break;

        case 18: // '\022'
            if(s == null && m != null && o == -2)
                return 12;
            break;
        }
        return e;
    }

    public final int getSetType()
    {
        return j;
    }

    public final int getType()
    {
        return e;
    }

    public final boolean isNull()
    {
        return e <= 1;
    }

    public final boolean isUnassignedNull()
    {
        return e == 0;
    }

    public final boolean isAssignedNull()
    {
        return e == 1;
    }

    public final void setUnassignedNull()
    {
        e = 0;
    }

    public final void setAssignedNull()
    {
        e = 1;
    }

    public final void setNull(int i1)
    {
        if(i1 == 0)
            e = 0;
        else
            e = 1;
    }

    public final void setAsDate(Variant variant)
    {
        switch(variant.e)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        case 6: // '\006'
        case 7: // '\007'
        case 10: // '\n'
        case 11: // '\013'
        case 14: // '\016'
        case 15: // '\017'
            setDate(variant.getAsLong());
            return;

        case 0: // '\0'
            setUnassignedNull();
            return;

        case 1: // '\001'
            setAssignedNull();
            return;

        case 13: // '\r'
            setDate(variant.getDate());
            // fall through

        case 8: // '\b'
        case 9: // '\t'
        case 12: // '\f'
        default:
            a(e, 13, false);
            return;
        }
    }

    public final void setAsTimestamp(Variant variant)
    {
        switch(variant.e)
        {
        case 8: // '\b'
        case 9: // '\t'
        case 12: // '\f'
        default:
            break;

        case 15: // '\017'
            setTimestamp(variant.getTimestamp());
            break;

        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        case 6: // '\006'
        case 7: // '\007'
        case 10: // '\n'
        case 11: // '\013'
            setTimestamp(variant.getAsLong());
            return;

        case 13: // '\r'
            setTimestamp(variant.getAsLong());
            f.setHours(0);
            f.setMinutes(0);
            f.setSeconds(0);
            return;

        case 14: // '\016'
            long l1 = variant.getAsLong();
            l1 %= 86400000;
            if(l1 < (long)0)
                l1 += 86400000;
            setTimestamp(l1);
            return;

        case 0: // '\0'
            setUnassignedNull();
            return;

        case 1: // '\001'
            setAssignedNull();
            return;
        }
        a(e, 15, false);
    }

    public final void setAsTime(Variant variant)
    {
        switch(variant.e)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        case 6: // '\006'
        case 7: // '\007'
        case 10: // '\n'
        case 11: // '\013'
        case 13: // '\r'
        case 15: // '\017'
            setTime(variant.getAsLong());
            return;

        case 0: // '\0'
            setUnassignedNull();
            return;

        case 1: // '\001'
            setAssignedNull();
            return;

        case 14: // '\016'
            setTime(variant.getTime());
            // fall through

        case 8: // '\b'
        case 9: // '\t'
        case 12: // '\f'
        default:
            a(e, 14, false);
            return;
        }
    }

    public final boolean getAsBoolean()
    {
        switch(e)
        {
        case 11: // '\013'
            return t;

        case 16: // '\020'
            return Boolean.valueOf(i).booleanValue();

        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            return o != 0;

        case 5: // '\005'
            return n != (long)0;

        case 6: // '\006'
            return p != (float)0;

        case 7: // '\007'
            return q != (double)0;

        case 10: // '\n'
            return u.doubleValue() != (double)0;

        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
            return getAsLong() != (long)0;

        case 8: // '\b'
        case 9: // '\t'
        case 12: // '\f'
        default:
            return false;
        }
    }

    public final BigDecimal getAsBigDecimal()
    {
        switch(e)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            return new BigDecimal(o);

        case 5: // '\005'
            return BigDecimal.valueOf(n, 0);

        case 6: // '\006'
            return new BigDecimal(p);

        case 7: // '\007'
            return new BigDecimal(q);

        case 10: // '\n'
            return getBigDecimal();

        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
            return BigDecimal.valueOf(getAsLong());

        case 0: // '\0'
        case 1: // '\001'
            return new BigDecimal(0.0D);

        case 8: // '\b'
        case 9: // '\t'
        case 11: // '\013'
        case 12: // '\f'
        default:
            a(e, 10, true);
            break;
        }
        return null;
    }

    public final float getAsFloat()
    {
        switch(e)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            return (float)o;

        case 5: // '\005'
            return (float)n;

        case 6: // '\006'
            return p;

        case 7: // '\007'
            return (float)q;

        case 10: // '\n'
            return getBigDecimal().floatValue();

        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
            return (float)getAsLong();

        case 0: // '\0'
        case 1: // '\001'
            return 0.0F;

        case 8: // '\b'
        case 9: // '\t'
        case 11: // '\013'
        case 12: // '\f'
        default:
            a(e, 6, true);
            break;
        }
        return 0.0F;
    }

    public final double getAsDouble()
    {
        switch(e)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            return (double)o;

        case 5: // '\005'
            return (double)n;

        case 6: // '\006'
            return (double)p;

        case 7: // '\007'
            return q;

        case 10: // '\n'
            return getBigDecimal().doubleValue();

        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
            return (double)getAsLong();

        case 0: // '\0'
        case 1: // '\001'
            return 0.0D;

        case 8: // '\b'
        case 9: // '\t'
        case 11: // '\013'
        case 12: // '\f'
        default:
            a(e, 7, true);
            break;
        }
        return 0.0D;
    }

    public final long getAsLong()
    {
        switch(e)
        {
        case 11: // '\013'
            return (long)(t ? 1 : 0);

        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            return (long)o;

        case 5: // '\005'
            return n;

        case 6: // '\006'
            return (long)p;

        case 7: // '\007'
            return (long)q;

        case 10: // '\n'
            return getBigDecimal().longValue();

        case 14: // '\016'
            return getTime().getTime();

        case 13: // '\r'
            return getDate().getTime();

        case 0: // '\0'
        case 1: // '\001'
            return 0L;

        case 15: // '\017'
            int i1 = f.getNanos();
            f.setNanos(0);
            long l1 = f.getTime();
            f.setNanos(i1);
            return l1;

        case 8: // '\b'
        case 9: // '\t'
        case 12: // '\f'
        default:
            a(e, 5, true);
            return 0L;
        }
    }

    public final int getAsInt()
    {
        switch(e)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            return o;

        case 11: // '\013'
            return t ? 1 : 0;

        case 14: // '\016'
            if(o == -6)
            {
                n = LocalDateUtil.getLocalTimeAsLong(h, null);
                o = -7;
            }
            return (int)n;

        case 13: // '\r'
            if(o == -5)
            {
                n = LocalDateUtil.getLocalDateAsLong(r, null);
                o = -7;
            }
            return (int)n;

        case 15: // '\017'
            return (int)getAsLong();

        case 5: // '\005'
            return (int)n;

        case 6: // '\006'
            return (int)p;

        case 7: // '\007'
            return (int)q;

        case 10: // '\n'
            return getBigDecimal().intValue();

        case 0: // '\0'
        case 1: // '\001'
            return 0;

        case 8: // '\b'
        case 9: // '\t'
        case 12: // '\f'
        default:
            a(e, 4, true);
            return 0;
        }
    }

    public final short getAsShort()
    {
        switch(e)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            return (short)o;

        case 11: // '\013'
            return (short)(t ? 1 : 0);

        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
            return (short)(int)getAsLong();

        case 5: // '\005'
            return (short)(int)n;

        case 6: // '\006'
            return (short)(int)p;

        case 7: // '\007'
            return (short)(int)q;

        case 10: // '\n'
            return (short)getBigDecimal().intValue();

        case 0: // '\0'
        case 1: // '\001'
            return 0;

        case 8: // '\b'
        case 9: // '\t'
        case 12: // '\f'
        default:
            a(e, 3, true);
            break;
        }
        return 0;
    }

    public final Object getObject()
    {
        if(e != 17 && e != 18 && e != 12)
            a(e, 17, true);
        if(o != -3 && (o == -2 && m != null || s != null))
            try
            {
                InputStream inputstream = getInputStream();
                try
                {
                    inputstream.reset();
                }
                catch(IOException ioexception) { }
                setObject((new ObjectInputStream(inputstream)).readObject());
            }
            catch(Exception exception)
            {
                VariantException.fire(ExceptionChain.getOriginalMessage(exception));
            }
        return m;
    }

    public final boolean isSetAsObject()
    {
        return o == -3;
    }

    public final void setObject(Object obj)
    {
        if(j != 17 && j != 0)
            a(j, 17, false);
        e = 17;
        o = -3;
        m = obj;
    }

    private final void a(int i1)
    {
        VariantException.fire(Res.a.format(113, new String[] {
            typeName(i1)
        }));
    }

    public final void setFromString(int i1, String s1)
    {
        if(j != i1 && j != 0)
            a(j, i1, false);
        if(s1 == null)
            e = 16;
        switch(i1)
        {
        case 2: // '\002'
            setByte(Byte.parseByte(s1));
            break;

        case 3: // '\003'
            setShort(Short.parseShort(s1));
            break;

        case 4: // '\004'
            setInt(Integer.parseInt(s1));
            break;

        case 5: // '\005'
            setLong(Long.parseLong(s1));
            break;

        case 6: // '\006'
            setFloat(Float.valueOf(s1).floatValue());
            break;

        case 7: // '\007'
            setDouble(Double.valueOf(s1).doubleValue());
            break;

        case 10: // '\n'
            setBigDecimal(new BigDecimal(s1));
            break;

        case 11: // '\013'
            setBoolean(Boolean.valueOf(s1).booleanValue());
            break;

        case 15: // '\017'
            e = 15;
            f = Timestamp.valueOf(s1);
            break;

        case 13: // '\r'
            LocalDateUtil.setLocalDateAsLong(this, s1);
            break;

        case 14: // '\016'
            LocalDateUtil.setLocalTimeAsLong(this, s1);
            break;

        case 16: // '\020'
            setString(s1);
            break;

        case 8: // '\b'
        case 9: // '\t'
        case 12: // '\f'
        default:
            a(e);
            break;
        }
    }

    public final void setAsVariant(Variant variant)
    {
        switch(j)
        {
        case 16: // '\020'
            setString(variant.toString());
            break;

        case 2: // '\002'
            setByte((byte)variant.getAsInt());
            break;

        case 3: // '\003'
            setShort((short)variant.getAsInt());
            break;

        case 4: // '\004'
            setInt(variant.getAsInt());
            break;

        case 11: // '\013'
            setBoolean(variant.getAsBoolean());
            break;

        case 15: // '\017'
            setAsTimestamp(variant);
            break;

        case 13: // '\r'
            setAsDate(variant);
            break;

        case 14: // '\016'
            setAsTime(variant);
            break;

        case 5: // '\005'
            setLong(variant.getAsLong());
            break;

        case 6: // '\006'
            setFloat(variant.getAsFloat());
            break;

        case 7: // '\007'
            setDouble(variant.getAsDouble());
            break;

        case 10: // '\n'
            setBigDecimal(variant.getAsBigDecimal());
            break;

        case 1: // '\001'
            setAssignedNull();
            break;

        case 17: // '\021'
            setObject(variant.getAsObject());
            break;

        case 8: // '\b'
        case 9: // '\t'
        case 12: // '\f'
        default:
            if(j == 0 || j == variant.e || variant.isNull())
                setVariant(variant);
            else
                a(j);
            break;
        }
    }

    public final void setVariant(Variant variant)
    {
        switch(variant.e)
        {
        case 16: // '\020'
            setString(variant.i);
            break;

        case 2: // '\002'
            setByte((byte)variant.o);
            break;

        case 3: // '\003'
            setShort((short)variant.o);
            break;

        case 4: // '\004'
            setInt(variant.o);
            break;

        case 11: // '\013'
            setBoolean(variant.t);
            break;

        case 15: // '\017'
            setTimestamp(variant.getTimestamp());
            break;

        case 13: // '\r'
            if(variant.o != -4)
                setDate(variant.r);
            e = variant.e;
            o = variant.o;
            n = variant.n;
            break;

        case 14: // '\016'
            if(variant.o != -4)
                setTime(variant.h);
            e = variant.e;
            o = variant.o;
            n = variant.n;
            break;

        case 5: // '\005'
            setLong(variant.n);
            break;

        case 6: // '\006'
            setFloat(variant.p);
            break;

        case 7: // '\007'
            setDouble(variant.q);
            break;

        case 10: // '\n'
            setBigDecimal(variant.u);
            break;

        case 12: // '\f'
            if(j == 0 || j == variant.e)
                d(variant);
            else
                setInputStream(variant.getInputStream());
            break;

        case 18: // '\022'
            if(j == 0 || j == variant.e)
            {
                d(variant);
            } else
            {
                byte abyte0[] = variant.getByteArray();
                setByteArray(abyte0, variant.o);
            }
            break;

        case 17: // '\021'
            if(j == 0 || j == variant.e)
                d(variant);
            else
                setObject(variant.getObject());
            break;

        case 0: // '\0'
        case 1: // '\001'
            if(j != variant.e && j != 0)
                a(variant.e, j, false);
            e = variant.e;
            break;

        case 8: // '\b'
        case 9: // '\t'
        default:
            a(variant.e);
            break;
        }
    }

    private final void d(Variant variant)
    {
        e = variant.e;
        o = variant.o;
        s = variant.s;
        m = variant.m;
    }

    public final void setInputStream(int i1, InputStream inputstream)
    {
        if(j != 12 && j != 18 && j != 17 && j != 0)
            a(j, 12, false);
        if(inputstream == null)
            e = 1;
        else
        if(j != 0)
            e = j;
        else
            e = 12;
        o = -2;
        s = null;
        m = inputstream;
    }

    public final void setInputStream(InputStream inputstream)
    {
        setInputStream(12, inputstream);
    }

    public final void setBinaryStream(InputStream inputstream)
    {
        setInputStream(inputstream);
    }

    public final void setArrayLength(int i1)
    {
        o = i1;
    }

    public final void setByteArray(int i1, byte abyte0[], int j1)
    {
        if(j != 18 && j != 12 && j != 17 && j != 0)
            a(j, 18, false);
        s = abyte0;
        if(abyte0 == null)
            e = 1;
        else
        if(j != 0)
            e = j;
        else
            e = i1;
        m = null;
        o = j1;
    }

    public final void setByteArray(byte abyte0[], int i1)
    {
        setByteArray(18, abyte0, i1);
    }

    public final void setTimestamp(long l1)
    {
        if(j != 15 && j != 0)
            a(j, 15, false);
        e = 15;
        if(f == null)
            f = new Timestamp(System.currentTimeMillis());
        f.setTime((l1 / (long)1000) * (long)1000);
        int i1 = (int)((l1 % (long)1000) * (long)1000000);
        if(i1 < 0)
        {
            i1 = 1000000000 + i1;
            f.setTime((l1 / (long)1000 - (long)1) * (long)1000);
        }
        f.setNanos(i1);
    }

    public final void setTimestamp(long l1, int i1)
    {
        if(j != 15 && j != 0)
            a(j, 15, false);
        e = 15;
        if(f == null)
            f = new Timestamp(System.currentTimeMillis());
        f.setTime(l1);
        f.setNanos(i1);
    }

    public final void setTime(int i1)
    {
        if(j != 14 && j != 0)
            a(j, 14, false);
        e = 14;
        o = -4;
        n = i1;
    }

    public final void setTime(long l1)
    {
        if(j != 14 && j != 0)
            a(j, 14, false);
        e = 14;
        o = -6;
        if(h == null)
            h = new Time(System.currentTimeMillis());
        h.setTime(l1);
    }

    public final void setDate(int i1)
    {
        if(j != 13 && j != 0)
            a(j, 13, false);
        e = 13;
        o = -4;
        n = i1;
    }

    public final void setDate(long l1)
    {
        if(j != 13 && j != 0)
            a(j, 13, false);
        e = 13;
        o = -5;
        if(r == null)
            r = new Date(System.currentTimeMillis());
        r.setTime(l1);
    }

    public final void setTimestamp(Timestamp timestamp)
    {
        if(j != 15 && j != 0)
            a(j, 15, false);
        if(timestamp == null)
        {
            e = 1;
            f = null;
        } else
        {
            e = 15;
            if(f == null)
                f = new Timestamp(System.currentTimeMillis());
            f.setTime(timestamp.getTime());
            f.setNanos(timestamp.getNanos());
        }
    }

    public final void setTime(Time time)
    {
        if(j != 14 && j != 0)
            a(j, 14, false);
        if(time == null)
        {
            e = 1;
            h = null;
        } else
        {
            e = 14;
            o = -6;
            if(h == null)
                h = new Time(time.getTime());
            else
                h.setTime(time.getTime());
        }
    }

    public final void setDate(Date date)
    {
        if(j != 13 && j != 0)
            a(j, 13, false);
        if(date == null)
        {
            e = 1;
            r = null;
        } else
        {
            e = 13;
            o = -5;
            if(r == null)
                r = new Date(date.getTime());
            else
                r.setTime(date.getTime());
        }
    }

    public final void setBigDecimal(BigDecimal bigdecimal)
    {
        if(j != 10 && j != 0)
            a(j, 10, false);
        e = bigdecimal != null ? 10 : 1;
        if (e == 10 && bigdecimal.signum() == 0) u = d;
        else u = bigdecimal;
        t = false;
    }

    public final void setString(String s1)
    {
        if(j != 16 && j != 0)
            a(j, 16, false);
        e = s1 != null ? 16 : 1;
        if (e == 1) i = null;
        else {
          int slen = s1.length();
          if (slen == 0) i = a;
          else if (slen == 1) {
            char sch = s1.charAt(0);
            if (sch >= 512) i = s1;
            else {
              i = charStringPool[sch];
              if (i == null)
                i = charStringPool[sch] = s1;
            }
          } else if (slen <= maxPoolStringLength && 
              stringPool.size() < poolThreshold[slen]) {
            i = (String) stringPool.get(s1);
            if (i == null) stringPool.put(s1, i = s1);
          } else i = s1;
        }
    }

    public final void setFloat(float f1)
    {
        if(j != 0 && j != 6)
            a(j, 6, false);
        e = 6;
        p = f1;
    }

    public final void setDouble(double d1)
    {
        if(j != 0 && j != 7)
            a(j, 7, false);
        e = 7;
        q = d1;
    }

    public final void setBoolean(boolean flag)
    {
        if(j != 0 && j != 11)
            a(j, 11, false);
        e = 11;
        t = flag;
    }

    public final void setLong(long l1)
    {
        if(j != 0 && (j < 2 || j > 5))
            a(j, 5, false);
        e = 5;
        n = l1;
    }

    public final void setByte(byte byte0)
    {
        if(j != 0 && j < 2 || j > 5)
            a(j, 2, false);
        e = 2;
        o = byte0;
    }

    public final void setShort(short word0)
    {
        if(j != 0 && j != 3)
            a(j, 3, false);
        e = 3;
        o = word0;
    }

    public final void setInt(int i1)
    {
        if(j != 0 && j != 4)
            a(j, 4, false);
        e = 4;
        o = i1;
    }

    public final InputStream getInputStream()
    {
        if(e != 12 && e != 17 && e != 18)
            a(e, 12, true);
        if(o != -2 && m == null && s != null)
        {
            setInputStream(new ByteArrayInputStream(s, 0, o));
            s = null;
        }
        return (InputStream)m;
    }

    public final InputStream getBinaryStream()
    {
        return getInputStream();
    }

    public final int getArrayLength()
    {
        return o;
    }

    public final byte[] getByteArray()
    {
        if(e != 18 && e != 12 && e != 17)
            a(e, 18, true);
        if(s == null && o == -2)
        {
            InputStream inputstream = (InputStream)m;
            try
            {
                o = inputstream.available();
                s = new byte[o];
                inputstream.read(s);
                setByteArray(s, o);
            }
            catch(IOException ioexception)
            {
                byte abyte0[] = null;
                return abyte0;
            }
            m = null;
        }
        return s;
    }

    public final Timestamp getTimestamp()
    {
        if(e != 15)
            a(e, 15, true);
        return f;
    }

    public final Time getTime()
    {
        if(e != 14)
            a(e, 14, true);
        if(o == -4)
        {
            LocalDateUtil.setAsLocalTime(this, n, null);
            o = -7;
        }
        return h;
    }

    public final Date getDate()
    {
        if(e != 13)
            a(e, 13, true);
        if(o == -4)
        {
            LocalDateUtil.setAsLocalDate(this, n, null);
            o = -7;
        }
        return r;
    }

    public final BigDecimal getBigDecimal()
    {
        if(e != 10)
            a(e, 10, true);
        if(t)
        {
            u = new BigDecimal(i);
            t = false;
        }
        return u;
    }

    public final String getString()
    {
        if(e != 16)
            a(e, 16, true);
        return i;
    }

    public final float getFloat()
    {
        if(e != 6)
            a(e, 6, true);
        return p;
    }

    public final double getDouble()
    {
        if(e != 7)
            a(e, 7, true);
        return q;
    }

    public final boolean getBoolean()
    {
        if(e != 11)
            a(e, 11, true);
        return t;
    }

    public final long getLong()
    {
        if(e != 5)
            a(e, 5, true);
        return n;
    }

    public final byte getByte()
    {
        if(e != 2)
            a(e, 2, true);
        return (byte)o;
    }

    public final short getShort()
    {
        if(e != 2 && e != 3)
            a(e, 3, true);
        return (short)o;
    }

    public final int getInt()
    {
        if(e != 4)
            a(e, 4, true);
        return o;
    }

    private void a(int i1, int j1, boolean flag)
    {
        if(i1 <= 1 && b(i1, j1))
        {
            return;
        } else
        {
            byte byte0 = ((byte)(flag ? 47 : 83));
            VariantException.fire(Res.a.format(byte0, new String[] {
                typeName(i1), typeName(j1)
            }));
            return;
        }
    }

    private boolean b(int i1, int j1)
    {
        switch(j1)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            o = 0;
            break;

        case 15: // '\017'
            setTimestamp(0L, 0);
            break;

        case 14: // '\016'
            setTime(0L);
            break;

        case 13: // '\r'
            setDate(0L);
            break;

        case 5: // '\005'
            n = 0L;
            break;

        case 11: // '\013'
            t = false;
            break;

        case 6: // '\006'
            p = 0.0F;
            break;

        case 7: // '\007'
            q = 0.0D;
            break;

        case 16: // '\020'
            i = a;
            break;

        case 10: // '\n'
            u = d;
            t = false;
            break;

        case 12: // '\f'
        case 17: // '\021'
            o = 0;
            m = c;
            break;

        case 18: // '\022'
            s = b;
            break;

        case 8: // '\b'
        case 9: // '\t'
        default:
            return false;
        }
        e = i1;
        return true;
    }

    public final Object getDisplayValue()
    {
        switch(e)
        {
        case 0: // '\0'
        case 1: // '\001'
            return "";

        case 17: // '\021'
            return getObject();

        case 12: // '\f'
            return getInputStream();
        }
        return toString();
    }

    public static long getTimeZoneOffset()
    {
        if(!l)
        {
            Date date = new Date(70, 0, 1);
            g = date.getTime();
            l = true;
        }
        return g;
    }

    public static int typeId(String s1)
    {
        for(int i1 = 2; i1 <= 18; i1++)
            if(s1.equals(typeName(i1)))
                return i1;

        return 0;
    }

    public static int typeOf(String s1)
    {
        if(s1.equals("STRING"))
            return 16;
        if(s1.equals("DATE"))
            return 13;
        if(s1.equals("TIME"))
            return 14;
        if(s1.equals("TIMESTAMP"))
            return 15;
        if(s1.equals("INT"))
            return 4;
        if(s1.equals("BIGDECIMAL"))
            return 10;
        if(s1.equals("ASSIGNED_NULL"))
            return 1;
        if(s1.equals("UNASSIGNED_NULL"))
            return 0;
        if(s1.equals("BYTE"))
            return 2;
        if(s1.equals("SHORT"))
            return 3;
        if(s1.equals("LONG"))
            return 5;
        if(s1.equals("DOUBLE"))
            return 7;
        if(s1.equals("FLOAT"))
            return 6;
        if(s1.equals("BOOLEAN"))
            return 11;
        if(s1.equals("BINARY_STREAM"))
            return 12;
        if(s1.equals("INPUTSTREAM"))
            return 12;
        if(s1.equals("BYTE_ARRAY"))
            return 18;
        if(s1.equals("OBJECT"))
        {
            return 17;
        } else
        {
            VariantException.fire(Res.a.format(103, new String[] {
                s1
            }));
            return 0;
        }
    }

    public static String typeName(int i1)
    {
        switch(i1)
        {
        case 1: // '\001'
            return "ASSIGNED_NULL";

        case 0: // '\0'
            return "UNASSIGNED_NULL";

        case 2: // '\002'
            return "BYTE";

        case 3: // '\003'
            return "SHORT";

        case 4: // '\004'
            return "INT";

        case 5: // '\005'
            return "LONG";

        case 6: // '\006'
            return "FLOAT";

        case 7: // '\007'
            return "DOUBLE";

        case 10: // '\n'
            return "BIGDECIMAL";

        case 11: // '\013'
            return "BOOLEAN";

        case 12: // '\f'
            return "INPUTSTREAM";

        case 13: // '\r'
            return "DATE";

        case 14: // '\016'
            return "TIME";

        case 15: // '\017'
            return "TIMESTAMP";

        case 16: // '\020'
            return "STRING";

        case 18: // '\022'
            return "BYTE_ARRAY";

        case 17: // '\021'
            return "OBJECT";

        case 8: // '\b'
        case 9: // '\t'
        default:
            return "UNKNOWN";
        }
    }

    public Variant()
    {
    }

    public Variant(int i1)
    {
        j = i1;
    }
    
    // single char String table (cca. 20 kB)
    private static String[] charStringPool = new String[512];
    
    // pool of dataset short strings (>2MB)
    public static Map stringPool = Collections.synchronizedMap(new HashMap());
    
    // max length of pooled string
    private static final int maxPoolStringLength = 12;
    
    // array of thresholds for accepting strings of certain length
    private static final int[] poolThreshold = {0, 0, 40000, 39000, 35000, 34000, 33000,
      32500, 32000, 28000, 27000, 26000, 25000};
    
    private static long g;
    private static boolean l;
    private static byte b[] = new byte[0];
    private static ByteArrayInputStream c = new ByteArrayInputStream(b);
    private static BigDecimal d = new BigDecimal(0.0D);
    private static String a = "";
    private transient Object m;
    private Timestamp f;
    private Time h;
    private Date r;
    private BigDecimal u;
    private byte s[];
    private String i;
    private double q;
    private float p;
    private long n;
    private int o;
    private boolean t;
    private int e;
    private int j;
    public static final int MaxTypes = 18;
    private static final int ab = -7;
    private static final int v = -6;
    private static final int z = -5;
    private static final int x = -4;
    private static final int w = -3;
    private static final int y = -2;
    public static final Variant nullVariant = new Variant(0);
    public static final String UnknownType_S = "UNKNOWN";
    public static final String ObjectType_S = "OBJECT";
    public static final String StringType_S = "STRING";
    public static final String ByteArrayType_S = "BYTE_ARRAY";
    public static final String TimestampType_S = "TIMESTAMP";
    public static final String TimeType_S = "TIME";
    public static final String DateType_S = "DATE";
    /**
     * @deprecated Field BinaryStreamType_S is deprecated
     */
    public static final String BinaryStreamType_S = "BINARY_STREAM";
    public static final String InputStreamType_S = "INPUTSTREAM";
    public static final String BooleanType_S = "BOOLEAN";
    public static final String BigDecimalType_S = "BIGDECIMAL";
    public static final String DoubleType_S = "DOUBLE";
    public static final String FloatType_S = "FLOAT";
    public static final String LongType_S = "LONG";
    public static final String IntType_S = "INT";
    public static final String ShortType_S = "SHORT";
    public static final String ByteType_S = "BYTE";
    public static final String UnassignedNull_S = "UNASSIGNED_NULL";
    public static final String AssignedNull_S = "ASSIGNED_NULL";
    public static final int BYTE_ARRAY = 18;
    public static final int OBJECT = 17;
    public static final int STRING = 16;
    public static final int TIMESTAMP = 15;
    public static final int TIME = 14;
    public static final int DATE = 13;
    public static final int INPUTSTREAM = 12;
    /**
     * @deprecated Field BINARY_STREAM is deprecated
     */
    public static final int BINARY_STREAM = 12;
    public static final int BOOLEAN = 11;
    public static final int BIGDECIMAL = 10;
    public static final int DOUBLE = 7;
    public static final int FLOAT = 6;
    public static final int LONG = 5;
    public static final int INT = 4;
    public static final int SHORT = 3;
    public static final int BYTE = 2;
    public static final int NULL_TYPES = 1;
    public static final int ASSIGNED_NULL = 1;
    public static final int UNASSIGNED_NULL = 0;
    private static final long k = 200L;
}



/***** DECOMPILATION REPORT *****

    DECOMPILED FROM: /home/abf/projekti/devel/projects/spa/thirdparty-jars/dx.jar


    TOTAL TIME: 245 ms


    JAD REPORTED MESSAGES/ERRORS:


    EXIT STATUS:    0


    CAUGHT EXCEPTIONS:

 ********************************/
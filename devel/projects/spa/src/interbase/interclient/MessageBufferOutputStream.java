// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package interbase.interclient;

import java.io.*;
import java.sql.SQLException;
import sun.io.CharToByteConverter;

// Referenced classes of package interbase.interclient:
//            CharacterEncodingException, ErrorKey, UnsupportedCharacterSetException, Utils

final class MessageBufferOutputStream extends ByteArrayOutputStream
{

    MessageBufferOutputStream()
    {
        super(1024);
        super.count = 8;
    }

    private void align(int i)
    {
        int j = super.count % i;
        if(j != 0)
        {
            for(int k = 0; k < i - j; k++)
                write(0);

        }
    }

    String getOpcode()
    {
        return null;
    }

    int messageSize()
    {
        return size() - 8;
    }

    public synchronized void reset()
    {
        super.count = 8;
    }

    void setConverter(CharToByteConverter chartobyteconverter)
    {
        ctb_ = chartobyteconverter;
        encoding_ = ctb_.getCharacterEncoding();
        fiHack = "Cp1250".equals(encoding_);
    }

    void writeArrayId(int ai[])
    {
        writeInt(ai[0]);
        writeInt(ai[1]);
    }

    void writeBlobId(int ai[])
    {
        writeInt(ai[0]);
        writeInt(ai[1]);
    }

    void writeBoolean(boolean flag)
    {
        write(flag ? 1 : 0);
    }

    void writeByte(int i)
    {
        write(i);
    }

    void writeChar(int i)
    {
        align(2);
        write(i >>> 8 & 0xff);
        write(i & 0xff);
    }

    void writeDouble(double d)
    {
        writeLong(Double.doubleToLongBits(d));
    }

    void writeFirstInt(int i)
    {
        super.buf[3] = (byte)(i >>> 24 & 0xff);
        super.buf[2] = (byte)(i >>> 16 & 0xff);
        super.buf[1] = (byte)(i >>> 8 & 0xff);
        super.buf[0] = (byte)(i & 0xff);
    }

    void writeFloat(float f)
    {
        writeInt(Float.floatToIntBits(f));
    }

    void writeInt(int i)
    {
        align(4);
        write(i >>> 24 & 0xff);
        write(i >>> 16 & 0xff);
        write(i >>> 8 & 0xff);
        write(i & 0xff);
    }

    void writeLDBytes(int i, byte abyte0[])
    {
        writeShort(i + 1);
        write(abyte0, 0, i);
        write(0);
    }

    void writeLDBytes(byte abyte0[])
    {
        writeLDBytes(abyte0.length, abyte0);
    }
    
    private String fiStr(String s) {
      if (!fiHack || s.indexOf('\u03A6') < 0) return s;
      return s.replace('\u03A6', 'ÿ');
    }

    void writeLDChars(String s)
        throws SQLException
    {
        try
        {
            writeLDBytes(fiStr(s).getBytes(encoding_));
        }
        catch(UnsupportedEncodingException _ex)
        {
            throw new UnsupportedCharacterSetException(ErrorKey.unsupportedCharacterSet__0__, encoding_);
        }
    }

    void writeLDChars(String s, char ac[], byte abyte0[])
        throws SQLException
    {
        int i = s.length();
        try
        {
            for(int j = 0; j < i; j++) {
                ac[j] = s.charAt(j);
                if (fiHack && ac[j] == '\u03A6')
                  ac[j] = 'ÿ';
            }

            int k = ctb_.convert(ac, 0, i, abyte0, 0, abyte0.length);
            writeLDBytes(k, abyte0);
        }
        catch(CharConversionException charconversionexception)
        {
            throw new CharacterEncodingException(ErrorKey.characterEncoding__write_0__, Utils.getMessage(charconversionexception));
        }
    }

    void writeLDSQLText(String s)
        throws SQLException
    {
        try
        {
            writeLDBytes(fiStr(s).getBytes("8859_1"));
        }
        catch(UnsupportedEncodingException _ex)
        {
            throw new UnsupportedCharacterSetException(ErrorKey.unsupportedCharacterSet__0__, "8859_1");
        }
    }

    void writeLong(long l)
    {
        align(8);
        write((int)(l >>> 56) & 0xff);
        write((int)(l >>> 48) & 0xff);
        write((int)(l >>> 40) & 0xff);
        write((int)(l >>> 32) & 0xff);
        write((int)(l >>> 24) & 0xff);
        write((int)(l >>> 16) & 0xff);
        write((int)(l >>> 8) & 0xff);
        write((int)l & 0xff);
    }

    void writeSecondInt(int i)
    {
        super.buf[7] = (byte)(i >>> 24 & 0xff);
        super.buf[6] = (byte)(i >>> 16 & 0xff);
        super.buf[5] = (byte)(i >>> 8 & 0xff);
        super.buf[4] = (byte)(i & 0xff);
    }

    void writeShort(int i)
    {
        align(2);
        write(i >>> 8 & 0xff);
        write(i & 0xff);
    }

    void writeTimestampId(int ai[])
    {
        writeInt(ai[0]);
        writeInt(ai[1]);
    }

    void writeUnicodeChars(String s)
    {
        int i = s.length();
        for(int j = 0; j < i; j++)
        {
            char c = s.charAt(j);
            write(c >>> 8 & 0xff);
            write(c & 0xff);
        }

    }

    private static final int MIN_OUTPUT_BUFFER_SIZE__ = 1024;
    private static final int HEADER_LENGTH__ = 8;
    private CharToByteConverter ctb_;
    private String encoding_;
    private boolean fiHack;
    
    static {
      System.out.println("Output fi hack detected.");
    }
}

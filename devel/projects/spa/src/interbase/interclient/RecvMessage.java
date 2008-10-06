// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package interbase.interclient;

import java.io.*;
import java.sql.SQLException;
import java.sql.SQLWarning;
import sun.io.ByteToCharConverter;

// Referenced classes of package interbase.interclient:
//            BufferCache, BugCheckException, CharacterEncodingException, CommunicationException, 
//            CorruptDatabaseException, DeadlockException, DriverNotCapableException, ErrorCodes, 
//            ErrorKey, Globals, InvalidArgumentException, InvalidOperationException, 
//            LockConflictException, MessageCodes, OutOfMemoryException, RemoteProtocolException, 
//            SQLException, UnauthorizedUserException, UnavailableDatabaseFileException, UnavailableInterBaseServerException, 
//            UnsupportedCharacterSetException, UpdateConflictException, Utils

final class RecvMessage
{

    RecvMessage(int i, InputStream inputstream, boolean flag, int j, int k, ByteToCharConverter bytetocharconverter)
    {
        messageLength_ = i;
        in_ = inputstream;
        byteswap_ = flag;
        headerEndOfStream_ = j != 0;
        headerReserved_ = k;
        btc_ = bytetocharconverter;
        encoding_ = bytetocharconverter.getCharacterEncoding();
        fiHack = "Cp1250".equals(encoding_);
        buffered_ = false;
        pos_ = oldPos_ = 0;
        count_ = i;
    }

    private void align(int i)
        throws SQLException
    {
        if(pos_ >= count_)
            throw new BugCheckException(ErrorKey.bugCheck__0__, 101);
        int j = (pos_ + oldPos_) % i;
        if(j != 0)
            skip(i - j);
    }

    synchronized void bufferOut()
        throws SQLException
    {
        try
        {
            if(!buffered_)
            {
                int i = messageLength_ - pos_;
                buf_ = Globals.cache__.takeBuffer(i);
                int k;
                for(int j = 0; j < i; j += k)
                    k = in_.read(buf_, j, i - j);

                oldPos_ = pos_;
                pos_ = 0;
                count_ = i;
                buffered_ = true;
                in_ = null;
            }
        }
        catch(IOException ioexception)
        {
            throw new CommunicationException(ErrorKey.communication__io_exception_on_read_0__, Utils.getMessage(ioexception));
        }
    }

    interbase.interclient.SQLException createSQLException(int i, int j, int k, int l, String s)
    {
        switch(k)
        {
        case 335544389: 
        case 335544430: 
            return new OutOfMemoryException(i, k, l, s);

        case 335544333: 
            return new BugCheckException(i, k, l, s);

        case 335544344: 
            return new UnavailableDatabaseFileException(i, k, l, s);

        case 335544375: 
            return new UnavailableInterBaseServerException(i, k, l, s);

        case 335544345: 
            return new LockConflictException(i, k, l, s);

        case 335544451: 
            return new UpdateConflictException(i, k, l, s);

        case 335544336: 
            return new DeadlockException(i, k, l, s);

        case 335544335: 
        case 335544405: 
        case 335544649: 
            return new CorruptDatabaseException(i, k, l, s);

        case 335544472: 
            return new UnauthorizedUserException(i, k, l, s);

        case 11: // '\013'
            return new OutOfMemoryException(i);

        case 9: // '\t'
            return new BugCheckException(i, j);

        case 10: // '\n'
            return new RemoteProtocolException(i);

        case 7: // '\007'
            return new DriverNotCapableException(i);

        case 2: // '\002'
            return new InvalidOperationException(i);

        case 129: 
            return new InvalidArgumentException(i);

        case 1: // '\001'
            return new CommunicationException(i);

        case 128: 
            return new UnsupportedCharacterSetException(i, j);
        }
        return new interbase.interclient.SQLException(i, k, l, s);
    }

    SQLWarning createSQLWarning(int i, int j, int k, int l, String s)
    {
        interbase.interclient.SQLException sqlexception = createSQLException(i, j, k, l, s);
        return new SQLWarning(sqlexception.getMessage(), sqlexception.getSQLState(), sqlexception.getErrorCode());
    }

    void destroy()
        throws SQLException
    {
        if(buffered_)
        {
            Globals.cache__.returnBuffer(buf_);
            buf_ = null;
        } else
        {
            int i = count_ - pos_;
            skip(i);
            in_ = null;
        }
    }

    boolean getHeaderEndOfStream()
    {
        return headerEndOfStream_;
    }

    int getHeaderReserved()
    {
        return headerReserved_;
    }

    int getPosition()
    {
        return pos_;
    }

    SQLException get_EXCEPTIONS()
        throws SQLException
    {
        SQLException sqlexception;
        int i;
        for(sqlexception = null; (i = readUnsignedByte()) == 1; sqlexception = Utils.accumulateSQLExceptions(sqlexception, makeSQLException()));
        if(i != 2)
            sqlexception = Utils.accumulateSQLExceptions(sqlexception, new RemoteProtocolException(ErrorKey.remoteProtocol__unexpected_token_from_server_0__, 102));
        return sqlexception;
    }

    boolean get_ROW_DATUM()
        throws SQLException
    {
        switch(readUnsignedByte())
        {
        case 1: // '\001'
            return true;

        case 2: // '\002'
            return false;
        }
        throw new RemoteProtocolException(ErrorKey.remoteProtocol__unexpected_token_from_server_0__, 103);
    }

    boolean get_SUCCESS()
        throws SQLException
    {
        switch(readUnsignedByte())
        {
        case 1: // '\001'
            return true;

        case 2: // '\002'
            return false;
        }
        throw new RemoteProtocolException(ErrorKey.remoteProtocol__unexpected_token_from_server_0__, 100);
    }

    SQLWarning get_WARNINGS()
        throws SQLException
    {
        SQLWarning sqlwarning;
        int i;
        for(sqlwarning = null; (i = readUnsignedByte()) == 1; sqlwarning = (SQLWarning)Utils.accumulateSQLExceptions(sqlwarning, makeSQLWarning()));
        if(i != 2)
            throw new RemoteProtocolException(ErrorKey.remoteProtocol__unexpected_token_from_server_0__, 101);
        else
            return sqlwarning;
    }

    SQLException makeSQLException()
        throws SQLException
    {
        int i = readInt();
        int j = readInt();
        int k = readInt();
        int l = readInt();
        String s = readLDString();
        return createSQLException(i, j, k, l, s);
    }

    SQLWarning makeSQLWarning()
        throws SQLException
    {
        int i = readInt();
        int j = readInt();
        int k = readInt();
        int l = readInt();
        String s = readLDString();
        return createSQLWarning(i, j, k, l, s);
    }

    void mark()
    {
        mark_ = pos_;
    }

    int[] readArrayId()
        throws SQLException
    {
        int ai[] = new int[2];
        ai[0] = readInt();
        ai[1] = readInt();
        return ai;
    }

    int[] readBlobId()
        throws SQLException
    {
        int ai[] = new int[2];
        ai[0] = readInt();
        ai[1] = readInt();
        return ai;
    }

    boolean readBoolean()
        throws SQLException
    {
        if(pos_ >= count_)
            throw new BugCheckException(ErrorKey.bugCheck__0__, 102);
        if(buffered_)
            return buf_[pos_++] != 0;
        try
        {
            pos_++;
            return in_.read() != 0;
        }
        catch(IOException ioexception)
        {
            throw new CommunicationException(ErrorKey.communication__io_exception_on_read_0__, Utils.getMessage(ioexception));
        }
    }

    byte readByte()
        throws SQLException
    {
        if(pos_ >= count_)
            throw new BugCheckException(ErrorKey.bugCheck__0__, 103);
        if(buffered_)
            return buf_[pos_++];
        try
        {
            pos_++;
            return (byte)in_.read();
        }
        catch(IOException ioexception)
        {
            throw new CommunicationException(ErrorKey.communication__io_exception_on_read_0__, Utils.getMessage(ioexception));
        }
    }

    double readDouble()
        throws SQLException
    {
        return Double.longBitsToDouble(readLong());
    }

    float readFloat()
        throws SQLException
    {
        return Float.intBitsToFloat(readInt());
    }

    int readInt()
        throws SQLException
    {
        try
        {
            align(4);
            int i;
            int j;
            int k;
            int l;
            if(buffered_)
            {
                i = buf_[pos_++] & 0xff;
                j = buf_[pos_++] & 0xff;
                k = buf_[pos_++] & 0xff;
                l = buf_[pos_++] & 0xff;
            } else
            {
                pos_ += 4;
                i = in_.read();
                j = in_.read();
                k = in_.read();
                l = in_.read();
            }
            if(byteswap_)
                return i + (j << 8) + (k << 16) + (l << 24);
            else
                return (i << 24) + (j << 16) + (k << 8) + l;
        }
        catch(IOException ioexception)
        {
            throw new CommunicationException(ErrorKey.communication__io_exception_on_read_0__, Utils.getMessage(ioexception));
        }
    }

    byte[] readLDBytes(int i)
        throws SQLException
    {
        if(pos_ >= count_)
            throw new BugCheckException(ErrorKey.bugCheck__0__, 105);
        int j = readUnsignedShort();
        int k = i != 0 ? Math.min(j, i) : j;
        byte abyte0[] = new byte[k];
        if(buffered_)
        {
            System.arraycopy(buf_, pos_, abyte0, 0, k);
            pos_ += j;
            return abyte0;
        } else
        {
            throw new BugCheckException(ErrorKey.bugCheck__0__, 106);
        }
    }

    int readLDChars(char ac[], int i)
        throws SQLException
    {
        if(pos_ >= count_)
            throw new BugCheckException(ErrorKey.bugCheck__0__, 107);
        try
        {
            int j = readUnsignedShort();
            if(buffered_)
            {
                int k = btc_.convert(buf_, pos_, pos_ + j, ac, 0, i);
                if (fiHack)
                  for (int ci = 0; ci < k; ci++)
                    if (ac[ci] == 'ÿ') ac[ci] = '\u03A6';
                pos_ += j;
                return k;
            } else
            {
                throw new BugCheckException(ErrorKey.bugCheck__0__, 108);
            }
        }
        catch(CharConversionException charconversionexception)
        {
            throw new CharacterEncodingException(ErrorKey.characterEncoding__read_0__, Utils.getMessage(charconversionexception));
        }
    }

    String readLDSQLText()
        throws SQLException
    {
        return readLDString("8859_1");
    }

    String readLDString()
        throws SQLException
    {
        return readLDString(encoding_);
    }

    String readLDString(String s)
        throws SQLException
    {
        try
        {
            int i = readUnsignedShort();
            if(buffered_)
            {
                String s1 = new String(buf_, pos_, i, s);
                if (fiHack && s1.indexOf('ÿ') >= 0)
                  s1 = s1.replace('ÿ', '\u03A6');
                pos_ += i;
                return s1;
            } else
            {
                throw new BugCheckException(ErrorKey.bugCheck__0__, 109);
            }
        }
        catch(UnsupportedEncodingException _ex)
        {
            throw new UnsupportedCharacterSetException(ErrorKey.unsupportedCharacterSet__0__, s);
        }
    }

    long readLong()
        throws SQLException
    {
        align(8);
        if(byteswap_)
            return ((long)readInt() & 0xffffffffL) + ((long)readInt() << 32);
        else
            return ((long)readInt() << 32) + ((long)readInt() & 0xffffffffL);
    }

    short readShort()
        throws SQLException
    {
        try
        {
            align(2);
            int i;
            int j;
            if(buffered_)
            {
                i = buf_[pos_++] & 0xff;
                j = buf_[pos_++] & 0xff;
            } else
            {
                pos_ += 2;
                i = in_.read();
                j = in_.read();
            }
            if(byteswap_)
                return (short)(i + (j << 8));
            else
                return (short)((i << 8) + j);
        }
        catch(IOException ioexception)
        {
            throw new CommunicationException(ErrorKey.communication__io_exception_on_read_0__, Utils.getMessage(ioexception));
        }
    }

    int[] readTimestampId()
        throws SQLException
    {
        int ai[] = new int[2];
        ai[0] = readInt();
        ai[1] = readInt();
        return ai;
    }

    int readUnsignedByte()
        throws SQLException
    {
        if(pos_ >= count_)
            throw new BugCheckException(ErrorKey.bugCheck__0__, 104);
        if(buffered_)
            return buf_[pos_++] & 0xff;
        try
        {
            pos_++;
            return in_.read();
        }
        catch(IOException ioexception)
        {
            throw new CommunicationException(ErrorKey.communication__io_exception_on_read_0__, Utils.getMessage(ioexception));
        }
    }

    int readUnsignedShort()
        throws SQLException
    {
        try
        {
            align(2);
            int i;
            int j;
            if(buffered_)
            {
                i = buf_[pos_++] & 0xff;
                j = buf_[pos_++] & 0xff;
            } else
            {
                pos_ += 2;
                i = in_.read();
                j = in_.read();
            }
            if(byteswap_)
                return i + (j << 8);
            else
                return (i << 8) + j;
        }
        catch(IOException ioexception)
        {
            throw new CommunicationException(ErrorKey.communication__io_exception_on_read_0__, Utils.getMessage(ioexception));
        }
    }

    void reset()
    {
        pos_ = mark_;
    }

    void setPosition(int i)
    {
        pos_ = i;
    }

    private void skip(int i)
        throws SQLException
    {
        pos_ += i;
        if(!buffered_)
            try
            {
                for(int j = 0; j < i; j = (int)((long)j + in_.skip(i - j)));
            }
            catch(IOException ioexception)
            {
                throw new CommunicationException(ErrorKey.communication__io_exception_on_read_0__, Utils.getMessage(ioexception));
            }
    }

    void skipBlobId()
        throws SQLException
    {
        align(4);
        skip(8);
    }

    void skipDouble()
        throws SQLException
    {
        align(8);
        skip(8);
    }

    void skipFloat()
        throws SQLException
    {
        align(4);
        skip(4);
    }

    void skipInt()
        throws SQLException
    {
        align(4);
        skip(4);
    }

    void skipLDBytes()
        throws SQLException
    {
        if(pos_ >= count_)
        {
            throw new BugCheckException(ErrorKey.bugCheck__0__, 110);
        } else
        {
            skip(readUnsignedShort());
            return;
        }
    }

    void skipShort()
        throws SQLException
    {
        align(2);
        skip(2);
    }

    void skipTimestampId()
        throws SQLException
    {
        align(4);
        skip(8);
    }

    private boolean buffered_;
    private byte buf_[];
    private int pos_;
    private int oldPos_;
    private int count_;
    private int messageLength_;
    private InputStream in_;
    private boolean byteswap_;
    private int mark_;
    boolean headerEndOfStream_;
    boolean fiHack;
    int headerReserved_;
    ByteToCharConverter btc_;
    String encoding_;
    static {
      System.out.println("Input fi hack detected.");
    }
}

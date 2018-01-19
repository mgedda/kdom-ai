package kingdominoplayer.utils.collections;

/*
 * User: zayenz<br/>
 * Date: 2018-01-18<br/>
 * Time: 14:38<br/>
 */

import it.unimi.dsi.fastutil.bytes.AbstractByteSet;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import it.unimi.dsi.fastutil.objects.*;

import java.util.Map;

/**
 * Map from bytes to objects that uses a 256-element map to store the values.
 */
public class PositiveByteArrayLinkedMap<Value> implements Byte2ObjectMap<Value>
{
    private final Object[] iContents;
    private byte[] iUsedSlots;
    private int iSize;

    public PositiveByteArrayLinkedMap()
    {
        iContents = new Object[128];
        iUsedSlots = new byte[16];
        iSize = 0;
    }

    public PositiveByteArrayLinkedMap(final byte maxValue, final byte expectedSize)
    {
        iContents = new Object[maxValue+1];
        iUsedSlots = new byte[expectedSize];
        iSize = 0;
    }

    @Override
    public Value put(final byte key, final Value value)
    {
        @SuppressWarnings("unchecked")
        final Value oldValue = (Value) iContents[key];
        if (oldValue == null)
        {
            iSize += 1;
            if (iSize == iUsedSlots.length)
            {
                final byte[] usedSlots = new byte[2 * iUsedSlots.length];
                System.arraycopy(iUsedSlots, 0, usedSlots, 0, iUsedSlots.length);
                iUsedSlots = usedSlots;
            }
            iUsedSlots[iSize-1] = key;
        }

        iContents[key] = value;

        return oldValue;
    }

    @Override
    public Value remove(final byte key)
    {
        @SuppressWarnings("unchecked")
        final Value oldValue = (Value) iContents[key];

        if (oldValue != null)
        {
            iContents[key] = null;
            for (int i = 0; i < iSize; i++)
            {
                if (iUsedSlots[i] == key)
                {
                    System.arraycopy(iUsedSlots, i+1, iUsedSlots, i, iSize - i - 1);
                    --iSize;
                }
            }
        }

        return oldValue;
    }

    @Override
    public int size()
    {
        return iSize;
    }

    @Override
    public boolean isEmpty()
    {
        return iSize == 0;
    }

    @Override
    public boolean containsValue(final Object value)
    {
        if (value == null)
        {
            for (byte usedSlot : iUsedSlots)
            {
                if (iContents[usedSlot] == null)
                {
                    return true;
                }
            }
        }
        else
        {
            for (byte usedSlot : iUsedSlots)
            {
                if (value.equals(iContents[usedSlot]))
                {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public void putAll(final Map m)
    {
        for (Object entryObject : m.entrySet())
        {
            final Map.Entry entry = (Map.Entry) entryObject;
            final byte key = (Byte) entry.getKey();
            @SuppressWarnings("unchecked")
            final Value value = (Value) entry.getValue();
            put(key, value);
        }
    }

    @Override
    public void defaultReturnValue(final Value rv)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value defaultReturnValue()
    {
        return null;
    }

    @Override
    public ObjectSet<Entry<Value>> byte2ObjectEntrySet()
    {
        return new AbstractObjectSet<Entry<Value>>() {
            @Override
            public ObjectIterator<Entry<Value>> iterator()
            {
                return new ObjectIterator<Entry<Value>>() {
                    int iPosition = -1;
                    @Override
                    public boolean hasNext()
                    {
                        return iPosition < iSize-1;
                    }

                    @Override
                    public Entry<Value> next()
                    {
                        iPosition += 1;
                        return new Entry<Value>()
                        {
                            @Override
                            public byte getByteKey()
                            {
                                return iUsedSlots[iPosition];
                            }

                            @Override
                            public Value getValue()
                            {
                                //noinspection unchecked
                                return (Value) iContents[iUsedSlots[iPosition]];
                            }

                            @Override
                            public Value setValue(final Value value)
                            {
                                //noinspection unchecked
                                final Value old = (Value) iContents[iUsedSlots[iPosition]];
                                iContents[iUsedSlots[iPosition]] = value;
                                return old;
                            }
                        };
                    }
                };
            }

            @Override
            public int size()
            {
                return iSize;
            }
        };
    }

    @Override
    public ByteSet keySet()
    {
        return new AbstractByteSet()
        {
            @Override
            public ByteIterator iterator()
            {
                return new ByteIterator() {
                    int iPosition = -1;

                    @Override
                    public boolean hasNext()
                    {
                        return iPosition < iSize - 1;
                    }

                    @Override
                    public byte nextByte()
                    {
                        iPosition += 1;
                        return iUsedSlots[iPosition];
                    }
                };
            }

            @Override
            public int size()
            {
                return iSize;
            }
        };
    }

    @Override
    public ObjectCollection<Value> values()
    {
        return new AbstractObjectCollection<Value>()
        {
            @Override
            public ObjectIterator<Value> iterator()
            {
                return new ObjectIterator<Value>()
                {
                    int iPosition = -1;

                    @Override
                    public boolean hasNext()
                    {
                        return iPosition < iSize - 1;
                    }

                    @Override
                    public Value next()
                    {
                        iPosition += 1;
                        //noinspection unchecked
                        return (Value) iContents[iUsedSlots[iPosition]];
                    }
                };
            }

            @Override
            public int size()
            {
                return iSize;
            }
        };
    }

    @Override
    public Value get(final byte key)
    {
        //noinspection unchecked
        return (Value) iContents[key];
    }

    @Override
    public boolean containsKey(final byte key)
    {
        return iContents[key] != null;
    }

}

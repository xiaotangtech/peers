package net.sourceforge.peers.G729.spi.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Text implements CharSequence {
    protected byte[] chars;
    protected int pos;
    protected int len;
    private int linePointer;
    private int hash = -1;
    private byte zero_byte = 48;
    private byte nine_byte = 57;
    private byte a_byte = 97;
    private byte f_byte = 102;
    private byte A_byte = 65;
    private byte F_byte = 70;
    private byte minus_byte = 45;

    public Text() {
    }

    protected Text(Text another) {
        this.chars = another.chars;
        this.pos = another.pos;
        this.len = another.len;
    }

    public Text(String s) {
        this.chars = s.getBytes();
        this.pos = 0;
        this.len = this.chars.length;
    }

    public Text(int i) {
        this.chars = new byte[10];

        int remainder;
        for(remainder = i; remainder >= 10; remainder /= 10) {
            this.chars[this.len++] = (byte)(remainder % 10 + 48);
        }

        this.chars[this.len++] = (byte)(remainder + 48);
        int n = this.len / 2;

        for(int k = 0; k < n; ++k) {
            byte b = this.chars[this.len - 1 - k];
            this.chars[this.len - 1 - k] = this.chars[k];
            this.chars[k] = b;
        }

        this.pos = 0;
    }

    public Text(byte[] data, int pos, int len) {
        this.chars = data;
        this.pos = pos;
        this.len = len;
    }

    public void strain(byte[] data, int pos, int len) {
        this.chars = data;
        this.pos = pos;
        this.len = len;
        this.linePointer = 0;
    }

    public int length() {
        return this.len;
    }

    public char charAt(int index) {
        return (char)this.chars[this.pos + index];
    }

    public void copy(Text destination) {
        destination.strain(this.chars, this.pos, this.len);
    }

    public void duplicate(Text destination) {
        System.arraycopy(this.chars, this.pos, destination.chars, destination.pos, this.len);
        destination.len = this.len;
    }

    public int divide(char separator, Text[] parts) {
        int pointer = this.pos;
        int limit = this.pos + this.len;
        int mark = pointer;

        int count;
        for(count = 0; pointer < limit; ++pointer) {
            if (this.chars[pointer] == separator) {
                parts[count].strain(this.chars, mark, pointer - mark);
                mark = pointer + 1;
                ++count;
                if (count == parts.length - 1) {
                    break;
                }
            }
        }

        if (limit > mark) {
            parts[count].strain(this.chars, mark, limit - mark);
            ++count;
        }

        return count;
    }

    public int divide(char[] separators, Text[] parts) {
        int pointer = this.pos;
        int limit = this.pos + this.len;
        int mark = pointer;
        int count = 0;

        for(int k = 0; pointer < limit; ++pointer) {
            if (this.chars[pointer] == separators[k]) {
                parts[count].strain(this.chars, mark, pointer - mark);
                mark = pointer + 1;
                ++k;
                ++count;
            }
        }

        if (limit > mark) {
            parts[count].strain(this.chars, mark, limit - mark);
            ++count;
        }

        return count;
    }

    public CharSequence subSequence(int start, int end) {
        return this.subtext(start, end);
    }

    public Text subtext(int start, int end) {
        return new Text(this.chars, this.pos + start, end - start);
    }

    public Text subtext(int start) {
        return new Text(this.chars, this.pos + start, this.len);
    }

    public Collection<Text> split(char separator) {
        int pointer = this.pos;
        int limit = this.pos + this.len;
        int mark = pointer;

        ArrayList tokens;
        for(tokens = new ArrayList(); pointer < limit; ++pointer) {
            if (this.chars[pointer] == separator) {
                tokens.add(new Text(this.chars, mark, pointer - mark));
                mark = pointer + 1;
            }
        }

        tokens.add(new Text(this.chars, mark, limit - mark));
        return tokens;
    }

    public Collection<Text> split(Text separator) {
        ArrayList<Text> tokens = new ArrayList();
        int pointer = this.pos;
        int limit = this.pos + this.len;
        int mark = pointer;
        if (separator.length() == 0) {
            tokens.add(new Text(this.chars, pointer, pointer - pointer));
            return tokens;
        } else {
            for(int index = 0; pointer < limit; ++pointer) {
                if (this.chars[pointer] == separator.charAt(index)) {
                    ++index;
                } else {
                    index = 0;
                }

                if (index == separator.length()) {
                    tokens.add(new Text(this.chars, mark, pointer - mark));
                    mark = pointer + 1;
                    index = 0;
                }
            }

            tokens.add(new Text(this.chars, mark, limit - mark));
            return tokens;
        }
    }

    public int indexOf(Text value) {
        int pointer = this.pos;
        int limit = this.pos + this.len;
        int mark = pointer;
        if (value.length() == 0) {
            return 0;
        } else {
            for(int index = 0; pointer < limit; ++pointer) {
                if (this.chars[pointer] == value.charAt(index)) {
                    ++index;
                } else {
                    index = 0;
                    ++mark;
                    pointer = mark;
                }

                if (index == value.length()) {
                    return mark;
                }
            }

            return -1;
        }
    }

    public int indexOf(char value) {
        int limit = this.pos + this.len;

        for(int pointer = this.pos; pointer < limit; ++pointer) {
            if (value == this.chars[pointer]) {
                return pointer;
            }
        }

        return -1;
    }

    public void trim() {
        try {
            while(this.len > 0 && this.chars[this.pos] == 32) {
                ++this.pos;
                --this.len;
            }

            while(this.len > 0 && (this.chars[this.pos + this.len - 1] == 32 || this.chars[this.pos + this.len - 1] == 10 || this.chars[this.pos + this.len - 1] == 13)) {
                --this.len;
            }

        } catch (Exception var2) {
            System.out.println("len:" + this.len + ",pos:" + this.pos);
        }
    }

    public Text nextLine() {
        if (this.linePointer == 0) {
            this.linePointer = this.pos;
        } else {
            ++this.linePointer;
        }

        int mark = this.linePointer;

        for(int limit = this.pos + this.len; this.linePointer < limit && this.chars[this.linePointer] != 10; ++this.linePointer) {
            ;
        }

        return new Text(this.chars, mark, this.linePointer - mark);
    }

    public boolean hasMoreLines() {
        return this.linePointer < this.pos + this.len;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (!(other instanceof Text)) {
            return false;
        } else {
            Text t = (Text)other;
            return this.len != t.len ? false : this.compareChars(t.chars, t.pos);
        }
    }

    public int hashCode() {
        if (this.hash == -1) {
            this.hash = 469 + Arrays.hashCode(this.chars);
        }

        return this.hash;
    }

    private boolean compareChars(byte[] chars, int pos) {
        for(int i = 0; i < this.len; ++i) {
            if (this.differentChars((char)this.chars[i + this.pos], (char)chars[i + pos])) {
                return false;
            }
        }

        return true;
    }

    public boolean startsWith(Text pattern) {
        return pattern == null ? false : this.subSequence(0, pattern.len).equals(pattern);
    }

    private boolean differentChars(char c1, char c2) {
        if ('A' <= c1 && c1 < 'a') {
            c1 = (char)(c1 + 32);
        }

        if ('A' <= c2 && c2 < 'a') {
            c2 = (char)(c2 + 32);
        }

        return c1 != c2;
    }

    public String toString() {
        return (new String(this.chars, this.pos, this.len)).trim();
    }

    public int toInteger() throws NumberFormatException {
        int res = 0;
        int i = 1;
        byte currChar = this.chars[this.pos];
        boolean isMinus = false;
        if (currChar == this.minus_byte) {
            isMinus = true;
        } else {
            if (currChar < this.zero_byte || currChar > this.nine_byte) {
                throw new NumberFormatException("value is not numeric");
            }

            res += currChar - this.zero_byte;
        }

        while(i < this.len) {
            currChar = this.chars[this.pos + i];
            if (currChar < this.zero_byte || currChar > this.nine_byte) {
                throw new NumberFormatException("value is not numeric");
            }

            res *= 10;
            res += currChar - this.zero_byte;
            ++i;
        }

        if (isMinus) {
            return 0 - res;
        } else {
            return res;
        }
    }

    public long toLong() throws NumberFormatException {
        long res = 0L;
        int i = 1;
        byte currChar = this.chars[this.pos];
        boolean isMinus = false;
        if (currChar == this.minus_byte) {
            isMinus = true;
        } else {
            if (currChar < this.zero_byte || currChar > this.nine_byte) {
                throw new NumberFormatException("value is not numeric");
            }

            res += (long)(currChar - this.zero_byte);
        }

        while(i < this.len) {
            currChar = this.chars[this.pos + i];
            if (currChar < this.zero_byte || currChar > this.nine_byte) {
                throw new NumberFormatException("value is not numeric");
            }

            res *= 10L;
            res += (long)(currChar - this.zero_byte);
            ++i;
        }

        if (isMinus) {
            return 0L - res;
        } else {
            return res;
        }
    }

    public int hexToInteger() throws NumberFormatException {
        int res = 0;

        for(int i = 0; i < this.len; ++i) {
            res <<= 4;
            byte currChar = this.chars[this.pos + i];
            if (currChar >= this.zero_byte && currChar <= this.nine_byte) {
                res |= currChar - this.zero_byte;
            } else if (currChar >= this.a_byte && currChar <= this.f_byte) {
                res |= currChar - this.a_byte + 10;
            } else {
                if (currChar < this.A_byte || currChar > this.F_byte) {
                    System.out.println("THROWING 1");
                    throw new NumberFormatException("value is not numeric");
                }

                res |= currChar - this.a_byte + 10;
            }
        }

        return res;
    }

    private int pow(int a) {
        int res = 1;

        for(int i = 0; i < a; ++i) {
            res *= 10;
        }

        return res;
    }

    private int digit(int pos) {
        return this.chars[pos] - 48;
    }

    public void write(ByteBuffer buffer) {
        buffer.put(this.chars, this.pos, this.len);
    }

    public void copyRemainder(Text other) {
        other.chars = this.chars;
        other.pos = this.linePointer + 1;
        other.len = this.len - this.linePointer - 1;
    }

    public boolean contains(char c) {
        for(int k = this.pos; k < this.len; ++k) {
            if (this.chars[k] == c) {
                return true;
            }
        }

        return false;
    }
}
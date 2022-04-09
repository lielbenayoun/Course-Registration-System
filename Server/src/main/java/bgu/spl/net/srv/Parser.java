package bgu.spl.net.srv;

import java.util.Arrays;

public class Parser {
    private final byte[] bytes;
    private short index;

    public Parser(){
        this.bytes = new byte[1024];
        this.index = 0;
    }

    public void encode(short num){
        this.bytes[this.index] = (byte)((num & 0xFF00) / 0x100);
        this.bytes[this.index + 1] = (byte)(num & 0xFF);
        this.index += 2;
    }

    public void encode(String str){
        for(int i = 0; i < str.length(); ++i)
            this.bytes[this.index++] = (byte)(str.charAt(i));
        this.bytes[this.index] = 0;
        this.index++;
    }

    public byte[] arr(){
        return Arrays.copyOfRange(this.bytes, 0, this.index);
    }
}

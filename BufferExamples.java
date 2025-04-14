package org.example;

import io.vertx.core.buffer.Buffer;

public class BufferExamples
{
    public static void main(String[] args)
    {
        Buffer buff = Buffer.buffer();


        buff.appendString("abc");
        buff.appendInt(10);
        buff.appendString("def");


            System.out.println("int value at " + 2 + " is " + buff.getInt(3));
        System.out.println("string value at " + 3 + " is " + buff.getString(0,3));

    }
}

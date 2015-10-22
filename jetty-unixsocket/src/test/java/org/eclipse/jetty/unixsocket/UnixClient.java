package org.eclipse.jetty.unixsocket;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.util.Date;

import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;

public class UnixClient
{
    public static void main(String[] args) throws Exception
    {
        java.io.File path = new java.io.File("/tmp/jetty.sock");
        String data = "blah blah "+new Date();
        UnixSocketAddress address = new UnixSocketAddress(path);
        UnixSocketChannel channel = UnixSocketChannel.open(address);
        System.out.println("connected to " + channel.getRemoteSocketAddress());
        
        PrintWriter w = new PrintWriter(Channels.newOutputStream(channel));
        InputStreamReader r = new InputStreamReader(Channels.newInputStream(channel));
        
        while (true)
        {
            w.print(data);
            w.flush();

            CharBuffer result = CharBuffer.allocate(1024);
            r.read(result);
            result.flip();
            System.out.println("read from server: " + result.toString());
            if (!result.toString().equals(data))
            {
                System.out.println("ERROR: data mismatch");
            }
            else
            {
                System.out.println("SUCCESS");
            }
            
            Thread.sleep(10000);
        }
    }
}


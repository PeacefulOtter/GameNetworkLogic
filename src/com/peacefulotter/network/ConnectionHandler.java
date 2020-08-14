package com.peacefulotter.network;

import com.peacefulotter.game.Game;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler implements Runnable
{
    private final Socket socket;
    private final Game game;
    private final Connection connection;
    private OutputStream outputStream;

    private boolean isRunning;

    public ConnectionHandler( Socket socket, Game game, Connection connection )
    {
        this.socket = socket;
        this.game = game;
        this.connection = connection;
        this.isRunning = true;
    }

    @Override
    public void run()
    {
        try ( InputStream inputStream = socket.getInputStream();
              OutputStream outputStream = socket.getOutputStream() )
        {
            this.outputStream = outputStream;
            processIncomingData( inputStream );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }


    public void processIncomingData( InputStream inStream ) throws IOException
    {

        while ( isRunning )
        {
            DataInputStream dis = new DataInputStream( inStream );
            byte[] data = new byte[ dis.readInt() ];
            dis.readFully( data );

            String msg = new String( data ).trim();
            System.out.println(msg);
            connection.sendData( data );
        }
    }

    public void sendData( byte[] data ) throws IOException
    {
        var dos = new DataOutputStream( outputStream );
        dos.writeInt( data.length );
        dos.write( data, 0, data.length );
    }
}

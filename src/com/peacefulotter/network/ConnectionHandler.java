package com.peacefulotter.network;

import com.peacefulotter.game.Game;
import com.peacefulotter.packets.InitPacket;
import com.peacefulotter.packets.LoginPacket;
import com.peacefulotter.packets.Packet;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public final class ConnectionHandler implements Runnable
{
    private final boolean isServer;
    private final Socket socket;
    private final Game game;
    private final Connection connection;
    private OutputStream outputStream;


    public ConnectionHandler( Socket socket, Game game, Connection connection )
    {
        this( socket, game, connection, false );
    }

    public ConnectionHandler( Socket socket, Game game, Connection connection, boolean isServer )
    {
        this.socket = socket;
        this.game = game;
        this.connection = connection;
        this.isServer = isServer;
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
        boolean done = false;

        while ( !done )
        {
            DataInputStream dis = new DataInputStream( inStream );
            byte[] data = new byte[ dis.readInt() ];
            dis.readFully( data );

            String msg = new String( data ).trim();
            System.out.println("[ConnectionHandler] Incoming data : " + msg);
            parsePacket( data );
        }
    }

    private void parsePacket( byte[] data )
    {
        String msg = new String( data ).trim();
        Packet.PacketTypes type = Packet.PacketTypes.getType( msg.split( ";" )[ 0 ] );

        System.out.println("[ConnectionHandler] type : " + type.name() );

        switch ( Objects.requireNonNull( type ) )
        {
            case INIT:
                break;
            case MOVE:
                break;
            case LOGIN:
                game.handlePacket( new LoginPacket( data ) );
                break;
            default:
                break;
        }


        if ( isServer )
            connection.sendData( data );
    }


    public boolean sendData( byte[] data )
    {
        try
        {
            DataOutputStream dos = new DataOutputStream( outputStream );
            dos.writeInt( data.length );
            dos.write( data, 0, data.length );
            return true;
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return false;
        }
    }
}

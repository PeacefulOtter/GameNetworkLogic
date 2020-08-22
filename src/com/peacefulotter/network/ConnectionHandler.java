package com.peacefulotter.network;

import com.peacefulotter.Utils.Logger;
import com.peacefulotter.game.Game;
import com.peacefulotter.packets.LoginPacket;
import com.peacefulotter.packets.Packet;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public final class ConnectionHandler implements Runnable
{
    private static final int BYTES_LENGTH = 128;
    private static final int MAX_TRIES = 10;

    private static int id = 0;

    private final Game game;
    private final Connection connection;
    private final Socket socket;
    private final boolean isServer;

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

    /**
     * Get the data received from the Server or a Client
     * and parse it
     */
    @Override
    public void run()
    {
        Logger.log( getClass(), "Running on " + ++id );

        if ( socket.isClosed() )
        {
            Logger.log(getClass(), "Socket closed!");
            return;
        }

        readData();
    }

    /*
    Faster, but blocks the thread for some reasons...
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[ BYTES_LENGTH ];
            int length;
            while ((length = inStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String r = result.toString( StandardCharsets.UTF_8 );
     */
    private void readData()
    {
        try
        {
            Logger.log( getClass(), "Reading the input stream" );

            DataInputStream dis = new DataInputStream( socket.getInputStream() );
            byte[] data = new byte[ dis.readInt() ];
            dis.readFully( data );

            parsePacket( data );

            // sends the data to all users
            if ( isServer )
                connection.sendData( data );
        }
        catch ( IOException e )
        {
            System.out.println("catch");
            e.printStackTrace();
        }
    }

    private void parsePacket( byte[] data )
    {
        String msg = new String( data ).trim();
        Logger.log( getClass(), "Received Message : " + msg + ".");
        Packet.PacketTypes type = Packet.PacketTypes.getType( msg.split( ";" )[ 0 ] );

        assert type != null;
        Logger.log( getClass(), "Parsing packet of type " + type.name() );

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
    }

    /**
     * Send the data through the output stream
     * @param data : bytes to send
     * @return boolean - success = true, failure = false
     */
    public boolean sendData( byte[] data )
    {
        Logger.log( getClass(), "Sending data " + new String( data ).trim() + " on " + id );

        try
        {
            DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
            dos.writeInt( data.length );
            dos.write( data, 0, data.length );
            Logger.log( getClass(), "Finished sending data on " + id );
            return true;
        } catch ( IOException e )
        {
            Logger.log( "Broken Pipe on " + id + ", not sending the data" );
        }

        return false;
    }
}

package com.peacefulotter.network;

import com.peacefulotter.Utils.Logger;
import com.peacefulotter.game.Game;
import com.peacefulotter.packets.LoginPacket;
import com.peacefulotter.packets.Packet;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

public final class ConnectionHandler implements Runnable
{
    private static final int BYTES_LENGTH = 128;
    private static final int MAX_TRIES = 10;

    private static int id = 0;

    private final boolean isServer;
    private final Game game;
    private final Connection connection;
    private final Socket socket;

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

    private void readData()
    {
        try
        {
            // new String( data ).trim()
            Logger.log( getClass(), "Reading the input stream" );
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = socket.getInputStream().read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String r = result.toString( StandardCharsets.UTF_8 );
            Logger.log( getClass(), "Incoming data " + r );
            System.out.println(r);
            //parsePacket( data );
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
        System.out.println(Collections.singletonList(data));
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

        // sends the data to all users
        if ( isServer )
            connection.sendData( data );
    }

    /**
     * Send the data through the output stream
     * @param data : bytes to send
     * @return boolean - success = true, failure = false
     */
    public boolean sendData( byte[] data )
    {
        Logger.log( getClass(), "Sending data on " + id );

        try
        {
            DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
            dos.writeInt( data.length );
            dos.write( data, 0, data.length );
            Logger.log( getClass(), "Finished sending data on " + id );
            return true;
        } catch ( IOException e )
        {
            e.printStackTrace( Logger.err );
        }

        return false;
    }
}

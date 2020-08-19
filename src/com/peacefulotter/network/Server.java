package com.peacefulotter.network;

import com.peacefulotter.Utils.Logger;
import com.peacefulotter.Utils.RandomInteger;
import com.peacefulotter.game.Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public final class Server implements Connection
{
    private final Map<Integer, ConnectionHandler> connections;

    private Server( int port )
    {
        this.connections = new HashMap<>();

        try ( ServerSocket server = new ServerSocket( port ) )
        {
            Game game = new Game( this );
            new Thread( game ).start();

            while ( true )
            {
                Socket socket = server.accept();
                Logger.log( getClass(), "Received socket : " + socket );

                ConnectionHandler handler = new ConnectionHandler( socket, game, this, true );
                connections.put( RandomInteger.generate(), handler );
                new Thread( handler ).start();
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace( Logger.err );
        }
    }

    public static void main( String[] args )
    {
        int port = Integer.parseInt( args[ 0 ] );
        Logger.log( Server.class, "Listening on port : " + port );
        new Server( port );
    }

    /**
     * The server always sends data to all the connections
     * @param data : bytes to send
     */
    @Override
    public void sendData( byte[] data )
    {
        Logger.log( getClass(), "Sending data to " + connections );
        for (  Map.Entry<Integer, ConnectionHandler> entry : connections.entrySet() )
        {
            sendDataAndRemove( entry.getKey(), entry.getValue(), data );
        }
    }

    @Override
    public void sendDataTo( byte[] data, int connectionId )
    {
        sendDataAndRemove( connectionId, connections.get( connectionId ), data );
    }

    private void sendDataAndRemove( Integer id, ConnectionHandler handler, byte[] data )
    {
        boolean done = handler.sendData( data );
        if ( !done ) removeConnection( id );
    }

    private void removeConnection( int connectionId )
    {
        connections.remove( connectionId );
    }
}

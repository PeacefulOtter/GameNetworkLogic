package com.peacefulotter.network;

import com.peacefulotter.Utils.Logger;
import com.peacefulotter.Utils.RandomInteger;
import com.peacefulotter.game.Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Server implements Connection
{
    private final Map<Integer, ConnectionHandler> connections;
    private final Set<Integer> removeQueue;

    private int socketConnectionId;

    private Server( int port )
    {
        this.connections = new HashMap<>();
        this.removeQueue = new HashSet<>();

        try ( ServerSocket server = new ServerSocket( port ) )
        {
            Game game = new Game( this );
            new Thread( game ).start();

            while ( true )
            {
                Socket socket = server.accept();
                Logger.log( getClass(), "Received socket : " + socket );

                ConnectionHandler handler = new ConnectionHandler( socket, game, this, true );
                socketConnectionId = RandomInteger.generate();
                connections.put( socketConnectionId, handler );

                new Thread( handler ).start();
            }
        }
        catch ( IOException e )
        {
            Logger.err( e.getMessage(), e.getStackTrace() );
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
        Logger.log( getClass(), "Sending data to the connections" );

        for (  Map.Entry<Integer, ConnectionHandler> entry : connections.entrySet() )
            if ( socketConnectionId != entry.getKey() )
                sendDataAndRemove( entry.getKey(), entry.getValue(), data );

        removeConnections();
    }

    @Override
    public void sendDataTo( byte[] data, int connectionId )
    {
        sendDataAndRemove( connectionId, connections.get( connectionId ), data );
    }

    private void sendDataAndRemove( Integer id, ConnectionHandler handler, byte[] data )
    {
        boolean done = handler.sendData( data );
        if ( !done ) removeQueue.add( id );
    }

    private void removeConnections()
    {
        removeQueue.forEach( connections::remove );
    }
}

package com.peacefulotter.network;

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
            game.init(); // init update simulate

            while ( true )
            {
                Socket socket = server.accept();
                System.out.println("[Server] Received socket : " + socket);
                ConnectionHandler handler = new ConnectionHandler( socket, game, this, true );
                connections.put( RandomInteger.generate(), handler );
                new Thread( handler ).start();
            }
        }
        //catch ( E )
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public static void main( String[] args )
    {
        int port = Integer.parseInt( args[ 0 ] );
        System.out.println( "[Server] Listening on port : " + port);
        new Server( port );
    }

    /**
     * The server always sends data to all the connections
     * @param data
     */
    @Override
    public void sendData( byte[] data )
    {
        for (  Map.Entry<Integer, ConnectionHandler> entry : connections.entrySet() )
        {
            boolean done = entry.getValue().sendData( data );
            if ( !done ) removeConnection( entry.getKey() );
        }
    }

    @Override
    public void sendDataTo( byte[] data, int connectionId )
    {
        boolean done = connections.get( connectionId ).sendData( data );
        if ( !done ) removeConnection( connectionId );
    }

    private void removeConnection( double connectionId )
    {
        connections.remove( connectionId );
    }
}

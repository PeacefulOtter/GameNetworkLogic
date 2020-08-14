package com.peacefulotter.network;

import com.peacefulotter.game.Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server implements Connection
{
    private final Map<Double, ConnectionHandler> connections;
    private boolean isRunning;

    public Server( int port )
    {
        this.connections = new HashMap<>();

        try ( ServerSocket server = new ServerSocket( port ) )
        {
            Game game = new Game( this );
            isRunning = true;

            while ( isRunning )
            {
                Socket socket = server.accept();
                ConnectionHandler handler = new ConnectionHandler( socket, game, this );
                connections.put( generateConnectionId(), handler );
                new Thread( handler ).start();
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * The server always sends data to all the connections
     * @param data
     */
    @Override
    public void sendData( byte[] data )
    {
        for (  ConnectionHandler handler : connections.values() )
        {
            try { handler.sendData( data ); }
            catch( IOException e ) { e.printStackTrace(); } // remove handler from the list
        }
    }

    private void removeConnection( int connectionId )
    {

    }

    // generate double between 1000 and 2000
    private double generateConnectionId()
    {
        double res;
        do { res = ( Math.random() + 1 ) * 1000; }
        while ( connections.containsKey(  res  ) );
        return res;
    }
}

package com.peacefulotter.network;

import com.peacefulotter.Utils.Logger;
import com.peacefulotter.Utils.RandomInteger;
import com.peacefulotter.game.ClientGame;
import com.peacefulotter.game.Game;
import com.peacefulotter.packets.LoginPacket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;


public final class Client implements Connection
{
    private final static int MAX_TRIES = 10;
    private final int connectionId;
    private final String username;
    private Game game;
    private ConnectionHandler handler;

    private Client( String address, int port )
    {
        this( address, port, "Dummy" );
    }

    private Client( String address, int port, String username )
    {
        this.connectionId = RandomInteger.generate();
        this.username = username;
        listenForIncomingSocket( address, port );
    }

    public static void main( String[] args )
    {
        String address = args[ 0 ];
        int port = Integer.parseInt( args[ 1 ] );
        if ( args.length > 2 )
            new Client( address, port, args[ 2 ] );
        else
            new Client( address, port );
    }

    private void listenForIncomingSocket( String address, int port )
    {
        game = new ClientGame(this);

        boolean connected = false;
        int tries = 0;
        do
        {
            try
            {
                Socket s = new Socket( address, port );
                Logger.log( getClass(), "Received Socket from server " + s );
                //System.out.println( new DataInputStream( s.getInputStream() ).readInt() );
                handler = new ConnectionHandler( s, game, this );

                new Thread( handler ).start();
                new Thread( game ).start();

                connected = true;
            }
            catch ( IOException e )
            {
              e.printStackTrace( Logger.err );
            }
        } while ( !connected && ++tries < MAX_TRIES );
    }

    public void login()
    {
        new LoginPacket( connectionId, username ).writeData( this );
    }

    @Override
    public void sendData( byte[] data )
    {
        Logger.log( getClass(), "Sending data" + Collections.singletonList( data ) );
        boolean done = handler.sendData( data );
        if ( !done ) game.end();
    }

    @Override
    public void sendDataTo( byte[] data, int connectionId )
    {
        sendData( data );
    }
}

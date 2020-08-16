package com.peacefulotter.network;

import com.peacefulotter.Utils.RandomInteger;
import com.peacefulotter.game.ClientGame;
import com.peacefulotter.game.Game;
import com.peacefulotter.packets.LoginPacket;

import java.io.IOException;
import java.net.Socket;


public final class Client implements Connection
{
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
        boolean connected = false;
        do
        {
            try
            {
                Socket s = new Socket( address, port );
                System.out.println("[Client] Received Socket from server");
                game = new ClientGame( this );
                handler = new ConnectionHandler( s, game, this );
                new Thread( handler ).start();
                new Thread( game ).start();
                game.init(); // init update simulate
                connected = true;
            }
            catch ( IOException e )
            {
              e.printStackTrace();
            }
        } while ( !connected );
    }

    public void login()
    {
        new LoginPacket( connectionId, username ).writeData( this );
    }

    @Override
    public void sendData( byte[] data )
    {
        boolean done = handler.sendData( data );
        if ( !done ) game.end();
    }

    @Override
    public void sendDataTo( byte[] data, int connectionId )
    {
        sendData( data );
    }
}

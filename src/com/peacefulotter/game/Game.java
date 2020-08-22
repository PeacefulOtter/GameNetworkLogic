package com.peacefulotter.game;

import com.peacefulotter.Utils.Logger;
import com.peacefulotter.network.Client;
import com.peacefulotter.network.Connection;
import com.peacefulotter.packets.InitPacket;
import com.peacefulotter.packets.LoginPacket;
import com.peacefulotter.packets.MovePacket;

public class Game implements Runnable
{
    private final Connection connection;

    public Game( Connection connection )
    {
        this.connection = connection;
    }

    public void init()
    {
        Logger.log( this.getClass(), "Game Init" );
    }

    public void end()
    {
        Logger.log( this.getClass(), "Ending game" );
    }

    // Use this for settings from the server
    public void handlePacket( InitPacket packet )
    {

    }

    public void handlePacket( LoginPacket packet )
    {
        Logger.log( this.getClass(), "Handling Login Packet" );
        Logger.log( this.getClass(), "Username : " + packet.getUsername() );
    }

    public void handlePacket( MovePacket packet )
    {

    }

    @Override
    public void run()
    {
        Logger.log( this.getClass(), "Game is Running" );
        init();
    }

    /*
    public void handlePacket( OtherPacket packet )
    {

    }
     */
}

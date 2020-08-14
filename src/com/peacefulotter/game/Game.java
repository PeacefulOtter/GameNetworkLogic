package com.peacefulotter.game;

import com.peacefulotter.network.Connection;
import com.peacefulotter.packets.InitPacket;
import com.peacefulotter.packets.MovePacket;

public class Game
{
    private final Connection connection;

    public Game( Connection connection )
    {
        this.connection = connection;
    }

    public void init()
    {
        new InitPacket().writeData( connection );
    }

    public void handlePacket( InitPacket packet )
    {

    }

    public void handlePacket( MovePacket packet )
    {

    }

    /*
    public void handlePacket( OtherPacket packet )
    {

    }
     */
}

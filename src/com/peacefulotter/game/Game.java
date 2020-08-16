package com.peacefulotter.game;

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

    }

    public void end() {
        System.out.println("[Game] Ending game");
    }

    public void handlePacket( InitPacket packet )
    {

    }

    public void handlePacket( LoginPacket packet )
    {
        System.out.println("[Game] Handling Login Packet" );
        System.out.println("[Game] Username : " + packet.getUsername());
    }

    public void handlePacket( MovePacket packet )
    {

    }

    @Override
    public void run()
    {

    }

    /*
    public void handlePacket( OtherPacket packet )
    {

    }
     */
}

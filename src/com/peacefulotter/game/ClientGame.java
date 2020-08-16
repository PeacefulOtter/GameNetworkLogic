package com.peacefulotter.game;

import com.peacefulotter.network.Client;

public class ClientGame extends Game
{
    private final Client client;

    public ClientGame( Client connection )
    {
        super( connection );
        this.client = connection;
    }

    @Override
    public void init()
    {
        System.out.println("[Game] Sending Login Packet");
        client.login();
        super.init();
    }
}

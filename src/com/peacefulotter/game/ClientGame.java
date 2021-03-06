package com.peacefulotter.game;

import com.peacefulotter.Utils.Logger;
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
        super.init();
        Logger.log( this.getClass(), "Sending Login Packet" );
        client.login();
    }
}

package com.peacefulotter.network;

public interface Connection
{
    void sendData( byte[] data );
    void sendDataTo( byte[] data, int connectionId );
}

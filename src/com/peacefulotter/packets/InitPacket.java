package com.peacefulotter.packets;


public class InitPacket extends Packet
{
    public InitPacket()
    {
        super( PacketTypes.INIT );
    }

    public InitPacket( byte[] data )
    {
        this();
        System.out.println("[InitPacket] " + new String( data ).trim() );
    }

    @Override
    protected byte[] getData()
    {
        return new byte[ 0 ];
    }
}

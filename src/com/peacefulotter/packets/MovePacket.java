package com.peacefulotter.packets;

public class MovePacket extends Packet
{
    public MovePacket()
    {
        super( PacketTypes.MOVE );
    }

    public MovePacket( byte[] data )
    {
        this();
        System.out.println("[MovePacket] " + new String( data ).trim() );
    }

    @Override
    protected byte[] getData()
    {
        return formatPacketData( getIdentifier(), "direction:", "velocity:" );
    }
}

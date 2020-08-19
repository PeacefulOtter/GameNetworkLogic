package com.peacefulotter.packets;

import com.peacefulotter.Utils.Logger;

public class MovePacket extends Packet
{
    public MovePacket()
    {
        super( PacketTypes.MOVE );
    }

    public MovePacket( byte[] data )
    {
        this();
        Logger.log( this.getClass(), new String( data ).trim() );
    }

    @Override
    protected byte[] getData()
    {
        return formatPacketData( getIdentifier(), "direction:", "velocity:" );
    }
}

package com.peacefulotter.packets;

import com.peacefulotter.network.Connection;

import java.util.StringJoiner;

abstract public class Packet
{
    private final PacketTypes type;

    protected Packet( PacketTypes type )
    {
        this.type = type;
    }

    protected String getIdentifier() { return type.name(); }

    protected abstract byte[] getData();

    public void writeData( Connection connection )
    {
        connection.sendData( getData() );
    }

    public void writeDataTo( Connection connection, int connectionId )
    {
        connection.sendDataTo( getData(), connectionId );
    }


    protected byte[] formatPacketData( String ...args )
    {
        StringJoiner sj = new StringJoiner( ";" );
        for ( String s : args )
            sj.add( s );
        return sj.toString().getBytes();
    }

    public enum PacketTypes
    {
        INIT(),
        MOVE(),
        LOGIN();

        public static PacketTypes getType( String identifier )
        {
            for ( PacketTypes t : PacketTypes.values() )
                if ( t.name().equals( identifier ) )
                    return t;

            return null;
        }
    }
}

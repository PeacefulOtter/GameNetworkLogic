package com.peacefulotter.packets;

public class LoginPacket extends Packet
{
    private final int connectionId;
    private final String username;

    public LoginPacket( int connectionId, String username )
    {
        super( PacketTypes.LOGIN );
        this.connectionId = connectionId;
        this.username = username;
    }

    public LoginPacket( byte[] data )
    {
        super( PacketTypes.LOGIN );
        String msg = new String( data ).trim();
        System.out.println("[LoginPacket] constructor msg : " + msg  );
        String[] split = msg.split( ";" );
        this.connectionId =  Integer.parseInt( split[ 1 ] );
        this.username = split[ 2 ];
    }

    public String getUsername() { return username; }

    @Override
    protected byte[] getData()
    {
        return formatPacketData( getIdentifier(), String.valueOf( connectionId ), username );
    }
}

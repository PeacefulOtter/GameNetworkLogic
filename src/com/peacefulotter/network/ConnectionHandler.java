package com.peacefulotter.network;

import com.peacefulotter.Utils.Logger;
import com.peacefulotter.game.Game;
import com.peacefulotter.packets.InitPacket;
import com.peacefulotter.packets.LoginPacket;
import com.peacefulotter.packets.Packet;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.Objects;

public final class ConnectionHandler implements Runnable
{
    private static final int MAX_TRIES = 10;

    private static int id = 0;

    private final boolean isServer;
    private final Game game;
    private final Connection connection;
    private final Socket socket;

    public ConnectionHandler( Socket socket, Game game, Connection connection )
    {
        this( socket, game, connection, false );
    }

    public ConnectionHandler( Socket socket, Game game, Connection connection, boolean isServer )
    {
        this.socket = socket;
        this.game = game;
        this.connection = connection;
        this.isServer = isServer;
    }

    @Override
    public void run()
    {
        Logger.log( getClass(), "Running on " + ++id );
        try
        {
            if ( !socket.isClosed() )
                processIncomingData( socket.getInputStream() );
        }
        catch ( IOException e )
        {
            e.printStackTrace( Logger.err );
            System.exit( 1 );
        }
    }


    public void processIncomingData( InputStream inStream ) throws IOException
    {
        Logger.log( getClass(), " Processing data " + socket.isClosed() );
        boolean done = false;
        int tries = 0;

        do
        {
            try ( DataInputStream dis = new DataInputStream( inStream ) )
            {
                byte[] data = new byte[ dis.readByte() ];
                try
                {
                    dis.readFully( data );
                }
                catch( EOFException e )
                {
                    Logger.log( getClass(), "Incoming data is empty " );
                    return;
                }

                Logger.log( getClass(), "Incoming data : " + new String( data ).trim() );

                parsePacket( data );
                done = true;
            }
            catch( SocketException e )
            {
                e.printStackTrace( Logger.err );
            }

            // inStream.close(); =======================
        } while( !done && ++tries < MAX_TRIES );
    }

    private void parsePacket( byte[] data )
    {
        String msg = new String( data ).trim();
        Packet.PacketTypes type = Packet.PacketTypes.getType( msg.split( ";" )[ 0 ] );

        assert type != null;
        Logger.log( getClass(), "type : " + type.name() );

        switch ( Objects.requireNonNull( type ) )
        {
            case INIT:
                break;
            case MOVE:
                break;
            case LOGIN:
                game.handlePacket( new LoginPacket( data ) );
                break;
            default:
                break;
        }

        if ( isServer )
            connection.sendData( data );
    }


    public boolean sendData( byte[] data )
    {
        Logger.log( getClass(), "Sending data on " + id );
        ByteBuffer buffer = ByteBuffer.allocateDirect( data.length ).put( data );
        try ( WritableByteChannel outputChannel = Channels.newChannel( socket.getOutputStream() ) )
        {
            Logger.log( getClass(), buffer.toString() );
            buffer.flip();
            outputChannel.write( buffer );
        }
        catch( IOException e )
        {
            e.printStackTrace( Logger.err );
        }

        /*try ( DataOutputStream dos = new DataOutputStream( outputStream ) )
        {
            Logger.log( getClass(), "Sending data with " + outputStream );
            dos.writeInt( data.length );
            dos.write( data, 0, data.length );
            return true;
        } catch ( IOException e )
        {
            e.printStackTrace( Logger.err );
        }
        return false;*/
        return true;
    }
}

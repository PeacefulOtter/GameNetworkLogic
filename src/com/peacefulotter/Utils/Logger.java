package com.peacefulotter.Utils;

import java.io.PrintStream;
import java.time.LocalTime;
import java.util.StringJoiner;

public class Logger
{
    public static void log(String msg )
    {
        log( "", msg );
    }

    public static void log( Class<?> c, String msg )
    {
        log( "[" + c.getSimpleName() +  "]", msg );
    }

    public static void log( String prefix, String msg )
    {
        System.out.println( getTime() + " " + prefix + " " + msg );
    }

    public static void err( String errMessage, StackTraceElement[] trace )
    {
        StringJoiner sj = new StringJoiner( "\n" );
        sj.add( errMessage );
        for ( StackTraceElement traceElement : trace )
            sj.add( "\tat " + traceElement );

        log( "= ERR =", sj.toString() );
    }

    private static String getTime()
    {
        return LocalTime.now().toString().split( "\\." )[ 0 ];
    }
}

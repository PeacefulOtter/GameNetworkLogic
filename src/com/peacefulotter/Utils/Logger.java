package com.peacefulotter.Utils;

import java.io.PrintStream;
import java.time.LocalTime;

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

    private static String getTime()
    {
        return LocalTime.now().toString().split( "\\." )[ 0 ];
    }

    public static final PrintStream err = new ExceptionLogger();

    public static final class ExceptionLogger extends PrintStream
    {
        public ExceptionLogger()
        {
            super( System.err );
        }

        @Override
        public void println( String x )
        {
            log( x );
        }
    }
}

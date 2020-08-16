package com.peacefulotter.Utils;

import java.util.HashSet;
import java.util.Set;

// generate a round int between 1000 and 2000

public class RandomDouble
{
    private static final Set<Integer> values = new HashSet<>();

    public static int generate()
    {
        int res;
        do { res = (int) Math.round( ( Math.random() + 1 ) * 1000 ); }
        while ( values.contains(  res  ) );
        values.add( res );
        return res;
    }
}

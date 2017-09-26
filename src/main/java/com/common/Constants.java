package com.common;

/**
 * Created by nandeesh.b on 9/25/2017.
 */
//@Component
//@PropertySource("classpath:application.properties")
public class Constants {

    public static final long MAX_RRQUEST_ID = 9223372036854775806l;
    //@Value("queue.size")
    public static int MAX_QUEUE_SIZE=1000;
                //Integer.parseInt(env.getProperty("queue.size"));

    private Constants() {
    }
}

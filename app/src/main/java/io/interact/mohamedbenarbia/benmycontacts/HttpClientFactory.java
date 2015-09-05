package io.interact.mohamedbenarbia.benmycontacts;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * This factory creates HttpClients. It should be used each time a httpclient is needed.
 * Created by MohamedBenArbia on 05/09/15.
 */
public class HttpClientFactory {


    private static AbstractHttpClient httpClient;


    public static synchronized AbstractHttpClient getInstance() {
        if (httpClient == null) {
            httpClient = create();
        }

        return httpClient;
    }



    private static AbstractHttpClient create() {

        AbstractHttpClient result = new DefaultHttpClient();
        return result;
    }

}

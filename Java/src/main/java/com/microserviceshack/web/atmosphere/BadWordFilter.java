package com.microserviceshack.web.atmosphere;

import org.atmosphere.cpr.BroadcastFilter;

/**
 * An example of a filter replacing bad words.
 */
public class BadWordFilter implements BroadcastFilter {
    @Override
    public BroadcastAction filter(Object originalMessage, Object message) {

        if (message instanceof Response) {
            Response m = (Response) message;
            m.text = m.text.replace(".NET", "***");
            return new BroadcastAction(m);
        } else {
            return new BroadcastAction(message);
        }
    }
}
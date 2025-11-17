package org.zk.linkman.commons.produces;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.Produces;

public class JacksonProducer {

    @Produces
    public ObjectMapper getObjectMapper(){

        return new ObjectMapper();
    }
}

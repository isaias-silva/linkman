package org.zk.linkman.encurtador.services;


import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.zk.linkman.encurtador.tools.ValuesUtils;

@ApplicationScoped
public class CacheService {

    @CacheResult(cacheName = "users-code")
    public Integer getCode(Integer id){

        return ValuesUtils.generateRandomCode(6);

    }
    @CacheInvalidate(cacheName = "users-code")
    public void clearCode(Integer id){}



}

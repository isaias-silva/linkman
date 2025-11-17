package org.zk.linkman.mail;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.zk.linkman.commons.services.QueueService;


import static java.lang.System.getenv;

@ApplicationScoped
public class Bootstrap {

    @Inject
    QueueService queueService;

    @Scheduled(every = "60s")
    void consumeMessages() throws Exception {
        System.out.println("verify queue...");
        queueService.consume(getenv("MAIL_QUEUE"),(msg)->{
            System.out.println(msg);
        });
    }



}
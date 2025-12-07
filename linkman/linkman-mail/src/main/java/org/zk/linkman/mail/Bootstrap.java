package org.zk.linkman.mail;

import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.zk.linkman.commons.dto.ValidateCodeDto;
import org.zk.linkman.commons.services.QueueService;
import org.zk.linkman.mail.services.MailService;


import java.util.Map;

import static java.lang.System.getenv;

@ApplicationScoped
public class Bootstrap {

    @Inject
    QueueService queueService;

    @Inject
    MailService mailService;

    @Scheduled(every = "60s")
    void consumeMessages() throws Exception {
        System.out.println("verify queue...");

        queueService.consume(getenv("MAIL_QUEUE"), ValidateCodeDto.class, (msg) -> {

            ValidateCodeDto validateCodeDto = (ValidateCodeDto) msg.data();

            Uni<Void> sendResult = mailService.send(validateCodeDto.mail(),
                    "código de verificação",
                    Map.of("code", validateCodeDto.code(),
                            "title","olá!",
                            "message","foi solicitado um código de verificação se não foi você não compartilhe com ninguém."));


            sendResult.invoke(() -> System.out.println("Email enviado para: " + validateCodeDto.mail()));
        });
    }


}
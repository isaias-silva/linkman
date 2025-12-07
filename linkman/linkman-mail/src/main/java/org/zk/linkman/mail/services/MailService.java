package org.zk.linkman.mail.services;

import io.quarkus.mailer.MailTemplate;
import io.quarkus.qute.Location;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


import java.util.Map;

@ApplicationScoped
public class MailService {

    @Location("default")
    MailTemplate defaultTemplate;

    public Uni<Void> send(final String mail, final String subject, Map<String, ?> data) {
        MailTemplate.MailTemplateInstance instance = defaultTemplate.to(mail).subject(subject);
        data.forEach(instance::data);

        return instance.send();
    }
}

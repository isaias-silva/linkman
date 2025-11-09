package org.zk.linkman.providers;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.zk.linkman.constants.QueueActions;
import org.zk.linkman.dto.QueueMessage;
import org.zk.linkman.dto.RequestInfo;
import org.zk.linkman.services.infra.QueueService;

import java.io.IOException;
import java.util.Date;

import static java.lang.System.getenv;

@Provider
public class AccessLinkFilter implements ContainerRequestFilter {
    @Inject
    private QueueService queueService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String path = containerRequestContext.getUriInfo().getPath();
        if (path.startsWith("/l/")) {
            String ip = containerRequestContext.getHeaderString("X-Forwarded-For");
            RequestInfo info = new RequestInfo(path, ip, new Date().toString());

            QueueMessage<RequestInfo> queueMessage = new QueueMessage<>(QueueActions.ACCESS_METRICS, info);

            queueService.send(getenv("METRICS_QUEUE"),queueMessage );

        }

    }
}

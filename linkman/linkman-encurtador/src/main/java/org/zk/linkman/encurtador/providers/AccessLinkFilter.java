package org.zk.linkman.encurtador.providers;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.zk.linkman.commons.dto.QueueMessage;
import org.zk.linkman.commons.services.QueueService;
import org.zk.linkman.encurtador.constants.QueueActions;
import org.zk.linkman.encurtador.dto.RequestInfo;


import java.util.Date;

import static java.lang.System.getenv;

@Provider
public class AccessLinkFilter implements ContainerRequestFilter {
    @Inject
    private QueueService queueService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String path = containerRequestContext.getUriInfo().getPath();
        if (path.startsWith("/l/")) {
            String ip = containerRequestContext.getHeaderString("X-Forwarded-For");
            RequestInfo info = new RequestInfo(path, ip, new Date().toString());

            QueueMessage<RequestInfo> queueMessage = new QueueMessage<>(QueueActions.ACCESS_METRICS, info);

		try {
                    queueService.send(getenv("METRICS_QUEUE"), queueMessage);

                } catch (Exception e) {

                    throw new RuntimeException(e);
                }

	}

    }
}
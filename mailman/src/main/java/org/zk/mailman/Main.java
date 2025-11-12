package org.zk.mailman;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Main {

	@Incoming("mail")
	void processMail(String messageBody) {
		System.out.println("📩 Mensagem recebida na fila de email:");
		System.out.println(messageBody);


	}

}
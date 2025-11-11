package org.zk.mailman;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Main {

	@Incoming("test")
	void observer(){

	}

}
package com.dianping.demo.akka.spring;

import akka.actor.*;

import static akka.pattern.Patterns.ask;

import akka.util.Timeout;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import scala.concurrent.Await;
import scala.concurrent.CanAwait;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

/**
 * A main class to start up the application.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // create a spring context and scan the classes
        final AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext();
        ctx.scan("com.dianping.demo.akka.spring");
        ctx.refresh();

        // get hold of the actor system
        ActorSystem system = ctx.getBean(ActorSystem.class);
        // use the Spring Extension to create props for a named actor bean
//        ActorRef counter = system.actorOf(
//                SpringExtension.SpringExtProvider.get(system).props("CountingActor"), "counter");
        ActorRef counter = system.actorOf(Props.create(new UntypedActorFactory() {
            @Override
            public Actor create() throws Exception {
                return ctx.getBean(CountingActor.class);
            }
        }));

        // tell it to count three times
        counter.tell(new CountingActor.Count(), null);
        counter.tell(new CountingActor.Count(), null);
        counter.tell(new CountingActor.Count(), null);

        // print the result
        FiniteDuration duration = FiniteDuration.create(3, TimeUnit.SECONDS);
        Future<Object> result = ask(counter, new CountingActor.Get(),
                Timeout.durationToTimeout(duration));
        System.out.println(result.result(Duration.create(1, TimeUnit.SECONDS), null));
        try {
            System.out.println("Got back " + Await.result(result, duration));
        } catch (Exception e) {
            System.err.println("Failed getting result: " + e.getMessage());
            throw e;
        } finally {
            system.shutdown();
            system.awaitTermination();
        }
    }
}

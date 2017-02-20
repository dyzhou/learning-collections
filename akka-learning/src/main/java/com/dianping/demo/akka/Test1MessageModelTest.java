/*
 * Create Author  : shang.gao
 * Create Date    : 2016-02-03
 * Project        : akka-learning
 * File Name      : Test1MessageModelTest.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */
package com.dianping.demo.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * 功能描述:  tell & ask<p>
 *
 *
 * @author : shang.gao <p>
 *
 * @version 1.0 2016-02-03
 *
 * @since akka-learning 1.0
 */
public class Test1MessageModelTest extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof MessageUtils.WithResultMessage) {
            System.out.println("get result message");

            Thread.sleep(3000);

            getSender().tell("Hello,man", this.self());
        } else if (o instanceof MessageUtils.NoResultMessage) {
            System.out.println("get no result message");
        } else {
            unhandled(o);
        }
    }

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("Test1MessageModelTest");
        ActorRef actorRef = system.actorOf(Props.create(Test1MessageModelTest.class));
        actorRef.tell(new MessageUtils.NoResultMessage(), actorRef);


        Future<Object> future = Patterns.ask(actorRef, new MessageUtils.WithResultMessage(), Timeout.intToTimeout(5000));

        String result = (String) Await.result(future,Duration.create(10, TimeUnit.SECONDS));
        System.out.println(result);


//
//        final ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();
//        futures.add(Patterns.ask(actorRef, new MessageUtils.WithResultMessage(), Timeout.intToTimeout(5000)));
//        Future<Iterable<Object>> aggregate = Futures.sequence(futures, system.dispatcher());
//        Future<String> future = aggregate.map(new Mapper<Iterable<Object>, String>() {
//            @Override
//            public String apply(Iterable<Object> parameter) {
//                return parameter.iterator().next().toString();
//            }
//
//        }, system.dispatcher());


    }
}

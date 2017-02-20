/*
 * Create Author  : shang.gao
 * Create Date    : 2016-02-04
 * Project        : akka-learning
 * File Name      : FaultHandlingDocSample.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */
package com.dianping.demo.akka;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import akka.util.Timeout;
import com.dianping.demo.akka.FaultHandlingDocSample.StorageApi.StorageException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.stop;
import static akka.pattern.Patterns.ask;

/**
 * 功能描述:  <p>
 *
 *
 * @author : shang.gao <p>
 *
 * @version 1.0 2016-02-04
 *
 * @since akka-learning 1.0
 */
public class FaultHandlingDocSample {
    /**
     * Runs the sample
     */
    public static void main(String[] args) {
        Config config = ConfigFactory.parseString("akka.loglevel = DEBUG \n" + "akka.actor.debug.lifecycle = on");

        ActorSystem system = ActorSystem.create("FaultToleranceSample", config);
        ActorRef worker = system.actorOf(Props.create(Worker.class), "worker");
        ActorRef listener = system.actorOf(Props.create(Listener.class), "listener");
        // start the work and listen on progress
        // note that the listener is used as sender of the tell,
        // i.e. it will receive replies from the worker
        worker.tell(MessageObject.Start, listener);
    }

    /**
     * Listens on progress from the worker and shuts down the system when enough
     * work has been done.
     */
    public static class Listener extends UntypedActor {
        final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

        @Override
        public void preStart() {
            // If we don't get any progress within 15 seconds then the service is unavailable
            getContext().setReceiveTimeout(Duration.apply(15, TimeUnit.SECONDS));
        }

        public void onReceive(Object msg) {
            log.debug("received message {}", msg);
            if (msg instanceof MessageObject.Progress) {
                MessageObject.Progress progress = (MessageObject.Progress) msg;
                log.info("Current progress: {} %", progress.percent);
                if (progress.percent >= 100.0) {
                    log.info("That's all, shutting down");
                    getContext().system().shutdown();
                }
            } else if (msg == "") { //Actors.receiveTimeout()
                // No progress within 15 seconds, ServiceUnavailable
                log.error("Shutting down due to unavailable service");
                getContext().system().shutdown();
            } else {
                unhandled(msg);
            }
        }
    }

    /***
     * message object
     */
    public interface MessageObject {
        public static final Object Start = "Start";
        public static final Object Do = "Do";

        public static class Progress {
            public final double percent;

            public Progress(double percent) {
                this.percent = percent;
            }

            public String toString() {
                return String.format("%s(%s)", getClass().getSimpleName(), percent);
            }
        }
    }


    /**
     * Worker performs some work when it receives the Start message. It will
     * continuously notify the sender of the Start message of current Progress.
     * The Worker supervise the CounterService.
     */
    public static class Worker extends UntypedActor {
        final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
        final Timeout askTimeout = new Timeout(scala.concurrent.duration.Duration.apply(5, TimeUnit.SECONDS));

        // The sender of the initial Start message will continuously be notified about progress
        ActorRef progressListener;
        final ActorRef counterService = getContext().actorOf(Props.create(CounterService.class), "counter");
        final int totalCount = 51;

        // Stop the CounterService child if it throws ServiceUnavailable
        private static SupervisorStrategy strategy = new OneForOneStrategy(-1, Duration.Inf(),
                new Function<Throwable, SupervisorStrategy.Directive>() {
                    @Override
                    public SupervisorStrategy.Directive apply(Throwable t) {
                        if (t instanceof CounterServiceApi.ServiceUnavailable) {
                            return stop();
                        } else {
                            return escalate();
                        }
                    }
                });

        @Override
        public SupervisorStrategy supervisorStrategy() {
            return strategy;
        }

        public void onReceive(Object msg) {
            log.debug("received message {}", msg);
            if (msg.equals(MessageObject.Start) && progressListener == null) {
                progressListener = getSender();
                getContext().system().scheduler().schedule(Duration.Zero(), Duration.apply(1, TimeUnit.SECONDS), getSelf(), MessageObject.Do, getContext().dispatcher(), getSelf());
            } else if (msg.equals(MessageObject.Do)) {
                counterService.tell(new CounterServiceApi.Increment(1), getSelf());
                counterService.tell(new CounterServiceApi.Increment(1), getSelf());
                counterService.tell(new CounterServiceApi.Increment(1), getSelf());

                // Send current progress to the initial sender
//                pipe(ask(counterService, CounterServiceApi.GetCurrentCount, askTimeout)
//                        .mapTo(manifest(CounterServiceApi.CurrentCount.class))
//                        .map(new Mapper<CounterServiceApi.CurrentCount, WorkerApi.Progress>() {
//                            public WorkerApi.Progress apply(CounterServiceApi.CurrentCount c) {
//                                return new WorkerApi.Progress(100.0 * c.count / totalCount);
//                            }
//                        }))
//                        .to(progressListener);
            } else {
                unhandled(msg);
            }
        }
    }

    public interface CounterServiceApi {

        public static final Object GetCurrentCount = "GetCurrentCount";

        public static class CurrentCount {
            public final String key;
            public final long count;

            public CurrentCount(String key, long count) {
                this.key = key;
                this.count = count;
            }

            public String toString() {
                return String.format("%s(%s, %s)", getClass().getSimpleName(), key, count);
            }
        }

        public static class Increment {
            public final long n;

            public Increment(long n) {
                this.n = n;
            }

            public String toString() {
                return String.format("%s(%s)", getClass().getSimpleName(), n);
            }
        }

        public static class ServiceUnavailable extends RuntimeException {
            public ServiceUnavailable(String msg) {
                super(msg);
            }
        }

    }


    /**
     * Adds the value received in Increment message to a persistent counter.
     * Replies with CurrentCount when it is asked for CurrentCount. CounterService
     * supervise Storage and Counter.
     */
    public static class CounterService extends UntypedActor {

        // Reconnect message
        static final Object Reconnect = "Reconnect";

        private static class SenderMsgPair {
            final ActorRef sender;
            final Object msg;

            SenderMsgPair(ActorRef sender, Object msg) {
                this.msg = msg;
                this.sender = sender;
            }
        }

        final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
        final String key = getSelf().path().name();
        ActorRef storage;
        ActorRef counter;
        final List<SenderMsgPair> backlog = new ArrayList<SenderMsgPair>();
        final int MAX_BACKLOG = 10000;

        // Restart the storage child when StorageException is thrown.
        // After 3 restarts within 5 seconds it will be stopped.
        private static SupervisorStrategy strategy = new OneForOneStrategy(3, Duration.apply(5, TimeUnit.SECONDS),
                new Function<Throwable, SupervisorStrategy.Directive>() {
                    @Override
                    public SupervisorStrategy.Directive apply(Throwable t) {
                        if (t instanceof StorageException) {
                            return restart();
                        } else {
                            return escalate();
                        }
                    }
                });

        @Override
        public SupervisorStrategy supervisorStrategy() {
            return strategy;
        }

        @Override
        public void preStart() {
            initStorage();
        }

        /**
         * The child storage is restarted in case of failure, but after 3 restarts,
         * and still failing it will be stopped. Better to back-off than
         * continuously failing. When it has been stopped we will schedule a
         * Reconnect after a delay. Watch the child so we receive Terminated message
         * when it has been terminated.
         */
        void initStorage() {
            storage = getContext().watch(getContext().actorOf(Props.create(Storage.class), "storage"));
            // Tell the counter, if any, to use the new storage
            if (counter != null)
                counter.tell(new CounterApi.UseStorage(storage), getSelf());
            // We need the initial value to be able to operate
            storage.tell(new StorageApi.Get(key), getSelf());
        }

        @Override
        public void onReceive(Object msg) {
            log.debug("received message {}", msg);
            if (msg instanceof StorageApi.Entry && ((StorageApi.Entry) msg).key.equals(key) && counter == null) {
                // Reply from Storage of the initial value, now we can create the Counter
                final long value = ((StorageApi.Entry) msg).value;
                counter = getContext().actorOf(Props.create(new UntypedActorFactory() {
                    public Actor create() {
                        return new Counter(key, value);
                    }
                }));
                // Tell the counter to use current storage
                counter.tell(new CounterApi.UseStorage(storage), getSelf());
                // and send the buffered backlog to the counter
                for (SenderMsgPair each : backlog) {
                    counter.tell(each.msg, each.sender);
                }
                backlog.clear();
            } else if (msg instanceof CounterServiceApi.Increment) {
                forwardOrPlaceInBacklog(msg);
            } else if (msg.equals(CounterServiceApi.GetCurrentCount)) {
                forwardOrPlaceInBacklog(msg);
            } else if (msg instanceof Terminated) {
                // After 3 restarts the storage child is stopped.
                // We receive Terminated because we watch the child, see initStorage.
                storage = null;
                // Tell the counter that there is no storage for the moment
                counter.tell(new CounterApi.UseStorage(null), getSelf());
                // Try to re-establish storage after while
                getContext().system().scheduler().scheduleOnce(Duration.apply(10, TimeUnit.SECONDS), getSelf(), Reconnect, getContext().dispatcher(), getSender());
            } else if (msg.equals(Reconnect)) {
                // Re-establish storage after the scheduled delay
                initStorage();
            } else {
                unhandled(msg);
            }
        }

        void forwardOrPlaceInBacklog(Object msg) {
            // We need the initial value from storage before we can start delegate to the counter.
            // Before that we place the messages in a backlog, to be sent to the counter when
            // it is initialized.
            if (counter == null) {
                if (backlog.size() >= MAX_BACKLOG)
                    throw new CounterServiceApi.ServiceUnavailable("CounterService not available, lack of initial value");
                backlog.add(new SenderMsgPair(getSender(), msg));
            } else {
                counter.forward(msg, getContext());
            }
        }
    }

    public interface CounterApi {
        public static class UseStorage {
            public final ActorRef storage;

            public UseStorage(ActorRef storage) {
                this.storage = storage;
            }

            public String toString() {
                return String.format("%s(%s)", getClass().getSimpleName(), storage);
            }
        }
    }


    /**
     * The in memory count variable that will send current value to the Storage,
     * if there is any storage available at the moment.
     */
    public static class Counter extends UntypedActor {
        final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
        final String key;
        long count;
        ActorRef storage;

        public Counter(String key, long initialValue) {
            this.key = key;
            this.count = initialValue;
        }

        @Override
        public void onReceive(Object msg) {
            log.debug("received message {}", msg);
            if (msg instanceof CounterApi.UseStorage) {
                storage = ((CounterApi.UseStorage) msg).storage;
                storeCount();
            } else if (msg instanceof CounterServiceApi.Increment) {
                count += ((CounterServiceApi.Increment) msg).n;
                storeCount();
            } else if (msg.equals(CounterServiceApi.GetCurrentCount)) {
                getSender().tell(new CounterServiceApi.CurrentCount(key, count), getSelf());
            } else {
                unhandled(msg);
            }
        }

        void storeCount() {
            // Delegate dangerous work, to protect our valuable state.
            // We can continue without storage.
            if (storage != null) {
                storage.tell(new StorageApi.Store(new StorageApi.Entry(key, count)), getSelf());
            }
        }
    }

    public interface StorageApi {

        public static class Store {
            public final Entry entry;

            public Store(Entry entry) {
                this.entry = entry;
            }

            public String toString() {
                return String.format("%s(%s)", getClass().getSimpleName(), entry);
            }
        }

        public static class Entry {
            public final String key;
            public final long value;

            public Entry(String key, long value) {
                this.key = key;
                this.value = value;
            }

            public String toString() {
                return String.format("%s(%s, %s)", getClass().getSimpleName(), key, value);
            }
        }

        public static class Get {
            public final String key;

            public Get(String key) {
                this.key = key;
            }

            public String toString() {
                return String.format("%s(%s)", getClass().getSimpleName(), key);
            }
        }

        public static class StorageException extends RuntimeException {
            public StorageException(String msg) {
                super(msg);
            }
        }
    }


    /**
     * Saves key/value pairs to persistent storage when receiving Store message.
     * Replies with current value when receiving Get message. Will throw
     * StorageException if the underlying data store is out of order.
     */
    public static class Storage extends UntypedActor {

        final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
        final DummyDB db = DummyDB.instance;

        @Override
        public void onReceive(Object msg) {
            log.debug("received message {}", msg);
            if (msg instanceof StorageApi.Store) {
                StorageApi.Store store = (StorageApi.Store) msg;
                db.save(store.entry.key, store.entry.value);
            } else if (msg instanceof StorageApi.Get) {
                StorageApi.Get get = (StorageApi.Get) msg;
                Long value = db.load(get.key);
                getSender().tell(new StorageApi.Entry(get.key, value == null ? Long.valueOf(0L) : value), getSelf());
            } else {
                unhandled(msg);
            }
        }
    }

    public static class DummyDB {
        public static final DummyDB instance = new DummyDB();
        private final Map<String, Long> db = new HashMap<String, Long>();

        private DummyDB() {
        }

        public synchronized void save(String key, Long value) throws StorageException {
            if (11 <= value && value <= 14)
                throw new StorageException("Simulated store failure " + value);
            db.put(key, value);
        }

        public synchronized Long load(String key) throws StorageException {
            return db.get(key);
        }
    }
}

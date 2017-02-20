package com.dianping.demo.akka.spring;

import org.springframework.stereotype.Service;

/**
 * A simple service that can increment a number.
 */
@Service("CountingService")
public class CountingService {
    /**
     * Increment the given number by one.
     */
    public int increment(int count) {
        return count + 1;
    }
}

package com.subscription.plan.openai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class AsyncTestServiceTest {

    @Autowired
    private AsyncTestService asyncTestService;

    @Test
    void test() {
        System.out.println("start");
        asyncTestService.getWebFluxExample().doOnNext(System.out::println).blockLast();
    }

    @Test
    void test1() {
        System.out.println("start");
        asyncTestService.getAsyncExample().join();
    }

}
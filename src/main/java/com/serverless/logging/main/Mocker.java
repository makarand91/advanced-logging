package com.serverless.logging.main;

import com.serverless.logging.annotations.Identifier;
import com.serverless.logging.annotations.ProcessFlow;
import com.serverless.logging.annotations.Tags;
import com.serverless.logging.annotations.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Mocker {


    @Scheduled(fixedDelay = Long.MAX_VALUE)
    @ProcessFlow("Sample Process")
    public void test() {
        System.out.println("test");

        test1(1234);

        test1(4567);

        taskMethod(99999999);

        log.info("sample log");

    }

    @ProcessFlow
    public void test1(@Identifier int session) {
        log.info("sample log2");
        System.out.println("test");

    }


    @Task(value="Sampletask",tags=@Tags({"tag1","tag2"}))
    public void taskMethod(@Identifier int session) {
        log.info("sample log2");
        System.out.println("test");
        secondTask("Helloooooo","a233H55fuadyyy-dch");


    }
    @Task(value="InnerTask",tags=@Tags({"tag3","tag4"}))
    public void secondTask(String kuchbhi , @Identifier String id)
    {
        log.info("inner task log`");
    }



}

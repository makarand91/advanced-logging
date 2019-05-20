package com.serverless.logging.main;

import com.serverless.logging.annotations.ProcessFlow;
import org.springframework.stereotype.Service;

@Service
public class SLPointCut {

    @ProcessFlow
    public void Test()
    {
        System.out.println("sample");
    }

}

package com.example.experiment;

import com.example.experiment.service.CodeRunnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoRun {

    @Autowired private ApplicationContext context;

    @Autowired private CodeRunnerService codeRunnerService;

    @EventListener(ApplicationReadyEvent.class)
    public void runDemo() {
        codeRunnerService.runAndGetOutputFor("""
                                                     public class Coderunner {
                                                         public static void main(String[] args) {
                                                             System.out.println(args[0]);
                                                             System.out.println("Hello world!");
                                                             System.out.println(1/0);
                                                             System.out.println("Hello world2!");
                                                             System.out.print("Hello");
                                                             System.out.print(" world!");
                                                         }
                                                     }""");
        SpringApplication.exit(context, () -> 0);
    }

}

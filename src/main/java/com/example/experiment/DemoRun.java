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
                                                              
                                                         public boolean isPalindrome(String s) {
                                                             int lo = 0, hi = s.length() - 1;
                                                             while (lo < hi) { // if lo >= hi, stops!
                                                                 // i stops at a letter, or lo == hi
                                                                 while (lo < hi && !isValid(s.charAt(lo)))
                                                                     lo += 1;
                                                                 // j stops at a letter, or lo == hi
                                                                 while (lo < hi && !isValid(s.charAt(hi)))
                                                                     hi -= 1;
                                                                 // check
                                                                 if (lo < hi) { // ensure in-bound & letter comparison (because they don't meet yet)
                                                                     if (isCharDiff(s.charAt(lo), s.charAt(hi))) {return false;}
                                                                 }
                                                                 lo += 1;
                                                                 hi -= 1; // update, remember to move (otherwise it caseus infinite loop)
                                                             }
                                                             return true;
                                                         }
                                                              
                                                         private boolean isValid(char ch) {
                                                             return Character.isLetterOrDigit(ch);
                                                         }
                                                              
                                                         private boolean isCharDiff(char ch1, char ch2) {
                                                             return Character.toLowerCase(ch1) != Character.toLowerCase(ch2);
                                                         }
                                                              
                                                         public static void main(String[] args) {
                                                             System.out.println(isPalindrome("asffsa"));
                                                         }
                                                              
                                                     }
                                                                                                 """);
        SpringApplication.exit(context, () -> 0);
    }

}

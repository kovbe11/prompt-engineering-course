package com.example.experiment.service;

//import com.spotify.docker.client.DefaultDockerClient;
//import com.spotify.docker.client.DockerClient;
//import com.spotify.docker.client.exceptions.DockerException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DockerServiceTest {
    //pasted from old version test

//    private static DockerClient dockerClient;
//    private DockerService dockerService = new DockerService(dockerClient);
//
//    @BeforeAll
//    public static void beforeAll() {
//        try{
//            dockerClient = DefaultDockerClient.fromEnv().build();
//        }catch (Exception ex){
//            throw new RuntimeException(ex);
//        }
//    }
//
//    @BeforeEach
//    public void beforeEach() {
//        dockerService = new DockerService(dockerClient);
//    }
//
//    @Test
//    public void createImage() throws DockerException, IOException, InterruptedException {
//        File dockerfile = new File(getClass().getResource("/docker/Dockerfile").getFile());
//        dockerService.createImage(dockerfile, "helloworld-1");
//    }
//
//    @Test
//    public void runContainer() throws DockerException, InterruptedException {
//        dockerService.runContainer("helloworld-1");
//    }
//
//    @Test
//    public void getLogs() throws DockerException, InterruptedException {
//        final var output = dockerService.getOutputFromContainer("5d1f34a53ed7f4a71f8fa0063485c5361bb22392105dbc5d56bcabcc1f25d774");
//        assertEquals("Hello world\n", output);
//    }
//
//    @Test
//    public void remove() throws DockerException, InterruptedException {
//        dockerService.deleteImage("helloworld-1");
//        dockerService.deleteContainer("5d1f34a53ed7f4a71f8fa0063485c5361bb22392105dbc5d56bcabcc1f25d774");
//    }
}

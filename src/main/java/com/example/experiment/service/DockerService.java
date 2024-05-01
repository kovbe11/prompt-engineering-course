package com.example.experiment.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Frame;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class DockerService {

    private final DockerClient dockerClient;

    @SneakyThrows
    public String createImage(Path dockerFilePath, String imageName) {
        final var contextPath = dockerFilePath.getParent();
        BuildImageCmd buildImageCmd = dockerClient.buildImageCmd()
                .withDockerfile(dockerFilePath.toFile())
                .withBaseDirectory(contextPath.toFile())
                .withTags(Set.of(imageName));
        return buildImageCmd.start().awaitImageId(5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    public String runContainer(String imageId) {

        final var createContainerResponse = dockerClient.createContainerCmd(imageId)
                .withCmd("java", "Coderunner", "Some argument")
                .withArgsEscaped(true)
                .exec();
        final String containerId = createContainerResponse.getId();
        log.info("Container created with id: {}", containerId);

        dockerClient.startContainerCmd(containerId).exec();
        log.info("Container started with id: {}", containerId);

        WaitContainerResultCallback resultCallback = new WaitContainerResultCallback();
        dockerClient.waitContainerCmd(createContainerResponse.getId()).exec(resultCallback);
        resultCallback.awaitStatusCode(5, TimeUnit.SECONDS);

        log.info("Container finished with id: {}", containerId);

        return containerId;
    }

    @SneakyThrows
    public String getLogs(String containerId) {
        final var logCmd = dockerClient.logContainerCmd(containerId)
                .withStdOut(true)
                .withStdErr(true)
                .withFollowStream(false);
        final StringBuffer sb = new StringBuffer();
        try {
            logCmd.exec(new ResultCallback.Adapter<Frame>() {
                @Override
                public void onNext(Frame item) {
                    sb.append(item.toString().replace("STDOUT: ", ""));
                    sb.append("\n");
                }
            }).awaitCompletion(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error while getting container logs:", e);
        }
        return sb.toString();
    }

    @SneakyThrows
    public void deleteImage(String imageName) {
        dockerClient.removeImageCmd(imageName).exec();
    }

    @SneakyThrows
    public void deleteContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId).exec();
    }

}

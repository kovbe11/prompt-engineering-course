package com.example.experiment.service;

import com.example.experiment.domain.CodeRun;
import com.example.experiment.repository.CodeRunRepository;
import com.example.experiment.service.exception.CompilationFailedException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@AllArgsConstructor
@Slf4j
public class CodeRunnerService {

    public static final String COMPILER_ISSUES = "The command '/bin/sh -c javac Coderunner.java' returned a non-zero code: 1";
    public static final String SEPARATOR = "-";
    public static final String OUTPUT_EXTENSION = ".txt";
    public static final String FOLDER_PREFIX = "coderuns/";
    public static final String JAVA_FILE_NAME = "Coderunner.java";
    public static final String OUTPUT_FILE_NAME_PREFIX = "output";


    private final DockerService dockerService;
    private final Path template;
    private final RandomNameGenerator randomNameGenerator;
    private final CodeRunRepository codeRunRepository;

    @SneakyThrows
    public String runAndGetOutputFor(String code) {
        final var runId = randomNameGenerator.generateCodeRunId();
        CodeRun codeRun = CodeRun.builder().runId(runId).input(code).runCompleted(false).runCompiled(false).build();
        codeRun = codeRunRepository.save(codeRun);
        String imageId = null;
        String output;
        String containerId;
        final Path currentDir = Path.of(FOLDER_PREFIX, runId);
        try {
            final var created = Files.createDirectories(currentDir);
            codeRun.setRunFolderPath(created.toString());
            final var dockerFilePath = Path.of(FOLDER_PREFIX, runId, "Dockerfile");
            final var dockerFile = Files.copy(template, dockerFilePath, StandardCopyOption.REPLACE_EXISTING);
            final var javaFile = Files.createFile(created.resolve(JAVA_FILE_NAME));
            Files.writeString(javaFile, code);
            imageId = dockerService.createImage(dockerFile, runId);
            codeRun.setImageId(imageId);
            codeRun.setRunCompiled(true);
        } catch (Exception e) {
            log.error("Error while creating image", e);
            if (e.getMessage().contains(COMPILER_ISSUES)) {
                throw new CompilationFailedException(runId, imageId);
            }
            throw e;
        } finally {
            codeRun = codeRunRepository.save(codeRun);
        }
        if (imageId == null) {throw new IllegalStateException("Image ID is null");}

        try {
            containerId = dockerService.runContainer(imageId);
            codeRun.setContainerId(containerId);
            codeRun.setRunCompleted(true);
        } catch (Exception e) {
            log.error("Error while running code", e);
            throw e;
        } finally {
            codeRun = codeRunRepository.save(codeRun);
        }

        try {
            output = dockerService.getLogs(containerId);
            codeRun.setOutput(output);
        } catch (Exception e) {
            log.error("Error while getting logs", e);
            throw e;
        } finally {
            codeRunRepository.save(codeRun);
        }

        try {
            final var outputFile = Files.createFile(currentDir.resolve(OUTPUT_FILE_NAME_PREFIX + SEPARATOR + containerId + OUTPUT_EXTENSION));
            Files.writeString(outputFile, output);
        } catch (Exception e) {
            log.error("Error while writing output to file", e);
        }

        return output;
    }

}

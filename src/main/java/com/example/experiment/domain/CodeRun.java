package com.example.experiment.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeRun extends CreatedAt {

    @Id private String runId;

    @Column(unique = true) private String containerId;

    private String imageId;

    @Column(columnDefinition = "TEXT") private String input;
    private String output;

    private boolean runCompleted;
    private boolean runCompiled;

    private String runFolderPath;

}

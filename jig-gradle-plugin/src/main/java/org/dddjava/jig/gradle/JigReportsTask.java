package org.dddjava.jig.gradle;

import org.dddjava.jig.application.service.ClassFindFailException;
import org.dddjava.jig.application.service.ImplementationService;
import org.dddjava.jig.domain.model.implementation.Implementations;
import org.dddjava.jig.domain.model.implementation.raw.RawSourceLocations;
import org.dddjava.jig.infrastructure.configuration.Configuration;
import org.dddjava.jig.presentation.view.JigDocument;
import org.dddjava.jig.presentation.view.handler.HandlerMethodArgumentResolver;
import org.dddjava.jig.presentation.view.handler.JigDocumentHandlers;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JigReportsTask extends DefaultTask {

    @TaskAction
    void outputReports() {
        Project project = getProject();
        JigConfig config = project.getExtensions().findByType(JigConfig.class);

        List<JigDocument> jigDocuments = config.documentTypes();
        Configuration configuration = new Configuration(config.asProperties(), new JigConfigurationContext(config));

        getLogger().info("現在の設定を表示します。\n{}", config.propertiesText());

        long startTime = System.currentTimeMillis();
        getLogger().quiet("プロジェクト情報の取り込みをはじめます");
        try {
            ImplementationService implementationService = configuration.implementationService();
            JigDocumentHandlers jigDocumentHandlers = configuration.documentHandlers();

            RawSourceLocations rawSourceLocations = new GradleProject(project).rawSourceLocations();
            Implementations implementations = implementationService.implementations(rawSourceLocations);

            Path outputDirectory = outputDirectory(config);
            for (JigDocument jigDocument : jigDocuments) {
                getLogger().quiet("{} を出力します", jigDocument);
                jigDocumentHandlers.handle(jigDocument, new HandlerMethodArgumentResolver(implementations), outputDirectory);
            }
        } catch (ClassFindFailException e) {
            getLogger().quiet(e.warning().text());
        }
        getLogger().quiet("合計時間: {} ms", System.currentTimeMillis() - startTime);
    }

    Path outputDirectory(JigConfig config) {
        Project project = getProject();
        Path path = Paths.get(config.getOutputDirectory());
        if (path.isAbsolute()) return path;

        return project.getBuildDir().toPath().resolve("jig");
    }
}

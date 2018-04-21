package jig.gradle;

import jig.application.service.*;
import jig.diagram.plantuml.PlantumlDriver;
import jig.domain.model.characteristic.CharacteristicRepository;
import jig.domain.model.datasource.SqlRepository;
import jig.domain.model.declaration.annotation.AnnotationDeclarationRepository;
import jig.domain.model.japanese.JapaneseNameRepository;
import jig.domain.model.relation.RelationRepository;
import jig.infrastructure.JigPaths;
import jig.infrastructure.PrefixRemoveIdentifierFormatter;
import jig.infrastructure.PropertySpecificationContext;
import jig.infrastructure.asm.AsmSpecificationReader;
import jig.infrastructure.javaparser.JavaparserJapaneseReader;
import jig.infrastructure.mybatis.MyBatisSqlReader;
import jig.infrastructure.onmemoryrepository.*;
import org.gradle.api.plugins.Convention;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ServiceFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFactory.class);

    final CharacteristicRepository characteristicRepository = new OnMemoryCharacteristicRepository();
    final RelationRepository relationRepository = new OnMemoryRelationRepository();
    final SqlRepository sqlRepository = new OnMemorySqlRepository();
    final JapaneseNameRepository japaneseNameRepository = new OnMemoryJapaneseNameRepository();
    final AnnotationDeclarationRepository annotationDeclarationRepository = new OnMemoryAnnotationDeclarationRepository();

    AnalyzeService analyzeService(Convention convention) {
        JavaPluginConvention javaPluginConvention = convention.findPlugin(JavaPluginConvention.class);
        if (javaPluginConvention == null) {
            throw new AssertionError("JavaPluginが適用されていません。");
        }
        JigPaths jigPaths = jigPaths(javaPluginConvention);

        // TODO extensionで変更できるようにする
        PropertySpecificationContext specificationContext = new PropertySpecificationContext();

        return new AnalyzeService(
                jigPaths,
                new SpecificationService(new AsmSpecificationReader(specificationContext)),
                new DependencyService(
                        characteristicRepository,
                        relationRepository,
                        annotationDeclarationRepository),
                new GlossaryService(
                        new JavaparserJapaneseReader(
                                japaneseNameRepository,
                                jigPaths
                        ),
                        japaneseNameRepository
                ),
                new DatasourceService(
                        new MyBatisSqlReader(),
                        sqlRepository
                )
        );
    }

    private JigPaths jigPaths(JavaPluginConvention javaPluginConvention) {
        SourceSet mainSourceSet = javaPluginConvention.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);
        File srcDir = mainSourceSet.getJava().getSrcDirs().iterator().next();
        File classesOutputDir = mainSourceSet.getOutput().getClassesDir();
        File resourceOutputDir = mainSourceSet.getOutput().getResourcesDir();

        return new JigPaths(
                classesOutputDir.getAbsolutePath(),
                resourceOutputDir.getAbsolutePath(),
                srcDir.getAbsolutePath()
        );
    }

    ReportService reportService(String outputOmitPrefixPath) {
        return new ReportService(
                characteristicRepository,
                relationRepository,
                sqlRepository,
                new PrefixRemoveIdentifierFormatter(outputOmitPrefixPath),
                annotationDeclarationRepository,
                new GlossaryService(
                        // TODO nullなんとかする
                        null,
                        japaneseNameRepository));
    }

    PlantumlDriver diagramService(String outputOmitPrefix) {
        return new PlantumlDriver(new PrefixRemoveIdentifierFormatter(outputOmitPrefix), japaneseNameRepository);
    }
}

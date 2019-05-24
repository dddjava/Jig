package org.dddjava.jig.infrastructure.kotlinparser;

import org.dddjava.jig.domain.model.implementation.analyzed.alias.PackageNames;
import org.dddjava.jig.domain.model.implementation.analyzed.alias.TypeNames;
import org.dddjava.jig.domain.model.implementation.raw.*;
import org.dddjava.jig.infrastructure.codeparser.SourceCodeParser;
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys;
import org.jetbrains.kotlin.cli.common.messages.MessageCollector;
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles;
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment;
import org.jetbrains.kotlin.com.intellij.openapi.project.Project;
import org.jetbrains.kotlin.com.intellij.psi.PsiManager;
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.kotlin.config.CompilerConfiguration;
import org.jetbrains.kotlin.idea.KotlinFileType;
import org.jetbrains.kotlin.psi.KtFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class KotlinparserJapaneseReader implements SourceCodeParser {
    @Override
    public PackageNames readPackages(PackageInfoSources packageInfoSources) {
        return new PackageNames(new ArrayList<>());
    }

    @Override
    public TypeNames readTypes(SourceCodes<? extends SourceCode> sources) {
        KotlinSources kotlinSources = (KotlinSources) sources;
        KotlinSourceVisitor visitor = new KotlinSourceVisitor();

        for (KotlinSource kotlinSource : kotlinSources.list()) {
            KtFile ktFile = readKotlinSource(kotlinSource);
            if (ktFile == null) {
                continue;
            }

            ktFile.accept(visitor);
        }

        return new TypeNames(visitor.typeJapaneseNames, visitor.methodList);
    }

    private KtFile sourceToKtFile(KotlinSource kotlinSource, String source) {
        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.Companion.getNONE());
        KotlinCoreEnvironment environment = KotlinCoreEnvironment.createForProduction(() -> { }, configuration, EnvironmentConfigFiles.JVM_CONFIG_FILES);
        Project project = environment.getProject();
        LightVirtualFile virtualFile = new LightVirtualFile(kotlinSource.sourceFilePath().fineName(), KotlinFileType.INSTANCE, source);
        return (KtFile) PsiManager.getInstance(project).findFile(virtualFile);
    }

    private KtFile readKotlinSource(KotlinSource source) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(source.toInputStream(), Charset.forName("utf8")))) {
            String sourceCode = bufferedReader.lines().collect(Collectors.joining("\n"));
            return sourceToKtFile(source, sourceCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSupport(PackageInfoSources packageInfoSources) {
        return false;
    }

    @Override
    public boolean isSupport(SourceCodes<? extends SourceCode> sourceCodes) {
        return sourceCodes instanceof KotlinSources;
    }
}
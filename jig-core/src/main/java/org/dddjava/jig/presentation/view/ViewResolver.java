package org.dddjava.jig.presentation.view;

import org.dddjava.jig.domain.model.categories.CategoryAngles;
import org.dddjava.jig.domain.model.declaration.namespace.PackageIdentifierFormatter;
import org.dddjava.jig.domain.model.japanese.JapaneseNameFinder;
import org.dddjava.jig.domain.model.networks.packages.PackageNetwork;
import org.dddjava.jig.domain.model.services.ServiceAngles;
import org.dddjava.jig.presentation.view.graphvizj.*;
import org.dddjava.jig.presentation.view.poi.PoiView;
import org.dddjava.jig.presentation.view.poi.report.Reports;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ViewResolver {

    private final PackageIdentifierFormatter packageIdentifierFormatter;
    private final MethodNodeLabelStyle methodNodeLabelStyle;

    public ViewResolver(PackageIdentifierFormatter packageIdentifierFormatter, @Value("${methodNodeLabelStyle:SIMPLE}") String methodNodeLabelStyle) {
        this.packageIdentifierFormatter = packageIdentifierFormatter;
        this.methodNodeLabelStyle = MethodNodeLabelStyle.valueOf(methodNodeLabelStyle.toUpperCase(Locale.ENGLISH));
    }

    public JigView<PackageNetwork> dependencyWriter(JapaneseNameFinder japaneseNameFinder) {
        return new GraphvizjView<>(new PackageDependencyDiagram(packageIdentifierFormatter, japaneseNameFinder));
    }

    public JigView<ServiceAngles> serviceMethodCallHierarchy(JapaneseNameFinder japaneseNameFinder) {
        return new GraphvizjView<>(new ServiceMethodCallDiagram(japaneseNameFinder, methodNodeLabelStyle));
    }

    public JigView<Reports> applicationList() {
        return new PoiView();
    }

    public JigView<CategoryAngles> enumUsage(JapaneseNameFinder japaneseNameFinder) {
        return new GraphvizjView<>(new EnumUsageDiagram(japaneseNameFinder));
    }

    public JigView<Reports> domainList() {
        return new PoiView();
    }

    public JigView<Reports> branchList() {
        return new PoiView();
    }
}

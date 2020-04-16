package org.dddjava.jig.application.service;

import org.dddjava.jig.domain.model.jigdocument.JigLogger;
import org.dddjava.jig.domain.model.jigdocument.Warning;
import org.dddjava.jig.domain.model.jigloaded.relation.class_.ClassRelations;
import org.dddjava.jig.domain.model.jigloaded.relation.packages.PackageRelations;
import org.dddjava.jig.domain.model.jigloader.RelationsFactory;
import org.dddjava.jig.domain.model.jigloader.analyzed.AnalyzedImplementation;
import org.dddjava.jig.domain.model.jigmodel.businessrules.BusinessRules;
import org.dddjava.jig.domain.model.jigpresentation.diagram.BusinessRuleRelationDiagram;
import org.dddjava.jig.domain.model.jigpresentation.diagram.PackageRelationDiagram;
import org.springframework.stereotype.Service;

/**
 * 依存関係サービス
 */
@Service
public class DependencyService {

    JigLogger jigLogger;
    BusinessRuleService businessRuleService;

    public DependencyService(BusinessRuleService businessRuleService, JigLogger jigLogger) {
        this.businessRuleService = businessRuleService;
        this.jigLogger = jigLogger;
    }

    /**
     * パッケージの関連を取得する
     */
    public PackageRelationDiagram packageDependencies(AnalyzedImplementation analyzedImplementation) {
        BusinessRules businessRules = businessRuleService.businessRules(analyzedImplementation);

        if (businessRules.empty()) {
            jigLogger.warn(Warning.ビジネスルールが見つからないので出力されない通知);
            return PackageRelationDiagram.empty();
        }

        ClassRelations classRelations = RelationsFactory.createClassRelations(analyzedImplementation.typeByteCodes());
        PackageRelations packageRelations = PackageRelations.fromClassRelations(classRelations);

        return new PackageRelationDiagram(businessRules.identifiers().packageIdentifiers(), packageRelations, classRelations);
    }

    /**
     * ビジネスルールの関連を取得する
     */
    public BusinessRuleRelationDiagram businessRuleNetwork(AnalyzedImplementation analyzedImplementation) {
        BusinessRuleRelationDiagram businessRuleRelationDiagram = new BusinessRuleRelationDiagram(
                businessRuleService.businessRules(analyzedImplementation),
                RelationsFactory.createClassRelations(analyzedImplementation.typeByteCodes()));
        return businessRuleRelationDiagram;
    }
}

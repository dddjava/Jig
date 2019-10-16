package org.dddjava.jig.domain.model.businessrules;

import org.dddjava.jig.domain.model.declaration.package_.PackageIdentifier;
import org.dddjava.jig.domain.model.declaration.package_.PackageIdentifierFormatter;
import org.dddjava.jig.domain.model.diagram.DotText;
import org.dddjava.jig.domain.model.diagram.JigDocument;
import org.dddjava.jig.domain.model.diagram.Node;
import org.dddjava.jig.domain.model.diagram.RelationText;
import org.dddjava.jig.domain.model.interpret.alias.AliasFinder;
import org.dddjava.jig.domain.model.interpret.alias.TypeAlias;
import org.dddjava.jig.domain.model.interpret.relation.class_.ClassRelation;
import org.dddjava.jig.domain.model.interpret.relation.class_.ClassRelations;
import org.dddjava.jig.presentation.view.JigDocumentContext;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * ビジネスルールの関連網
 */
public class BusinessRuleNetwork {

    BusinessRules businessRules;
    ClassRelations classRelations;

    public BusinessRuleNetwork(BusinessRules businessRules, ClassRelations classRelations) {
        this.businessRules = businessRules;
        this.classRelations = classRelations;
    }

    public List<BusinessRulePackage> groups() {
        BusinessRulePackages businessRulePackages = businessRules.businessRulePackages();
        return businessRulePackages.list;
    }

    public BusinessRuleRelations relations() {
        List<BusinessRuleRelation> list = new ArrayList<>();
        for (ClassRelation classRelation : classRelations.list()) {
            if (businessRules.contains(classRelation.from()) && businessRules.contains(classRelation.to())) {
                list.add(new BusinessRuleRelation(classRelation));
            }
        }
        return new BusinessRuleRelations(list);
    }

    public DotText toDotText(JigDocumentContext jigDocumentContext, PackageIdentifierFormatter packageIdentifierFormatter, AliasFinder aliasFinder) {
        StringJoiner graph = new StringJoiner("\n", "digraph {", "}")
                .add("label=\"" + jigDocumentContext.diagramLabel(JigDocument.BusinessRuleRelationDiagram) + "\";")
                .add("node [shape=box,style=filled,fillcolor=lightgoldenrod];");

        // nodes
        List<BusinessRulePackage> list = groups();
        for (BusinessRulePackage businessRulePackage : list) {
            PackageIdentifier packageIdentifier = businessRulePackage.packageIdentifier();
            StringJoiner subgraph = new StringJoiner("\n", "subgraph \"cluster_" + packageIdentifier.asText() + "\" {", "}")
                    .add("label=\"" + packageIdentifier.format(packageIdentifierFormatter) + "\"");

            List<BusinessRule> businessRules = businessRulePackage.businessRules().list();
            for (BusinessRule businessRule : businessRules) {
                TypeAlias typeAlias = aliasFinder.find(businessRule.type().identifier());
                String aliasLine = "";
                if (typeAlias.exists()) {
                    aliasLine = typeAlias.asText() + "\n";
                }
                Node node = Node.of(businessRule.type().identifier())
                        .label(aliasLine + businessRule.type().identifier().asSimpleText());
                subgraph.add(node.asText());
            }

            graph.add(subgraph.toString());
        }

        // relations
        BusinessRuleRelations relations = relations();
        RelationText relationText = new RelationText();
        for (BusinessRuleRelation relation : relations.list()) {
            relationText.add(relation.from(), relation.to());
        }
        graph.add(relationText.asText());

        return new DotText(graph.toString());
    }
}

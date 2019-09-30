package org.dddjava.jig.presentation.view.graphvizj;

import org.dddjava.jig.domain.model.categories.CategoryAngle;
import org.dddjava.jig.domain.model.categories.CategoryAngles;
import org.dddjava.jig.domain.model.declaration.type.TypeIdentifier;
import org.dddjava.jig.domain.model.declaration.type.TypeIdentifiers;
import org.dddjava.jig.domain.model.diagram.Node;
import org.dddjava.jig.domain.model.diagram.RelationText;
import org.dddjava.jig.domain.model.diagram.Subgraph;
import org.dddjava.jig.domain.model.interpret.alias.AliasFinder;
import org.dddjava.jig.domain.model.interpret.alias.TypeAlias;
import org.dddjava.jig.domain.model.diagram.JigDocument;
import org.dddjava.jig.presentation.view.JigDocumentContext;

import java.util.Collections;
import java.util.StringJoiner;

import static java.util.stream.Collectors.joining;

public class CategoryUsageDiagram implements DotTextEditor<CategoryAngles> {

    private final AliasFinder aliasFinder;
    JigDocumentContext jigDocumentContext;

    public CategoryUsageDiagram(AliasFinder aliasFinder) {
        this.aliasFinder = aliasFinder;
        this.jigDocumentContext = JigDocumentContext.getInstance();
    }

    @Override
    public DotTexts edit(CategoryAngles categoryAngles) {
        if (categoryAngles.isEmpty()) {
            return new DotTexts(Collections.singletonList(DotText.empty()));
        }

        TypeIdentifiers enumTypes = categoryAngles.typeIdentifiers();

        String enumsText = enumTypes.list().stream()
                .map(enumType -> Node.of(enumType)
                        .label(typeNameOf(enumType))
                        .asText())
                .collect(joining("\n"));

        RelationText relationText = new RelationText();
        for (CategoryAngle categoryAngle : categoryAngles.list()) {
            for (TypeIdentifier userType : categoryAngle.userTypeIdentifiers().list()) {
                relationText.add(userType, categoryAngle.typeIdentifier());
            }
        }

        String userLabel = categoryAngles.userTypeIdentifiers().list().stream()
                .map(typeIdentifier ->
                        Node.of(typeIdentifier)
                                .label(typeNameOf(typeIdentifier))
                                .notEnum()
                                .asText())
                .collect(joining("\n"));

        String legendText = new Subgraph("legend")
                .label(jigDocumentContext.label("legend"))
                .add(new Node(jigDocumentContext.label("enum")).asText())
                .add(new Node(jigDocumentContext.label("not_enum")).notEnum().asText())
                .toString();

        return new DotTexts(new StringJoiner("\n", "digraph JIG {", "}")
                .add("label=\"" + jigDocumentContext.diagramLabel(JigDocument.CategoryUsageDiagram) + "\";")
                .add("rankdir=LR;")
                .add(Node.DEFAULT)
                .add(legendText)
                .add("{ rank=same;")
                .add(enumsText)
                .add("}")
                .add(relationText.asText())
                .add(userLabel)
                .toString());
    }

    private String typeNameOf(TypeIdentifier typeIdentifier) {
        TypeAlias typeAlias = aliasFinder.find(typeIdentifier);
        if (typeAlias.exists()) {
            return typeAlias.asText() + "\\n" + typeIdentifier.asSimpleText();
        }
        return typeIdentifier.asSimpleText();
    }
}

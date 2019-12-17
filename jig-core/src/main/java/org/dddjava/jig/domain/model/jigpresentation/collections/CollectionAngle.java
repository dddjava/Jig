package org.dddjava.jig.domain.model.jigpresentation.collections;

import org.dddjava.jig.domain.model.declaration.method.MethodDeclarations;
import org.dddjava.jig.domain.model.declaration.type.TypeIdentifier;
import org.dddjava.jig.domain.model.declaration.type.TypeIdentifiers;
import org.dddjava.jig.domain.model.jigloaded.relation.class_.ClassRelations;

/**
 * コレクションの切り口
 *
 * 以下を推測したい
 * ・ロジックがいろいろ書かれていそう --> そのクラスのロジックの書き方を重点レビュー
 * ・ロジックがほとんどなさそう --> そのクラスを使っているクラス側にロジックが書かれていないか、レビュー
 */
public class CollectionAngle {

    CollectionType collectionType;
    ClassRelations classRelations;

    public CollectionAngle(CollectionType collectionType, ClassRelations classRelations) {
        this.collectionType = collectionType;
        this.classRelations = classRelations;
    }

    public TypeIdentifier typeIdentifier() {
        return collectionType.typeIdentifier();
    }

    public TypeIdentifiers userTypeIdentifiers() {
        return classRelations.collectTypeIdentifierWhichRelationTo(typeIdentifier());
    }

    public MethodDeclarations methods() {
        return collectionType.methods();
    }

    public CollectionField field() {
        return collectionType.field();
    }
}

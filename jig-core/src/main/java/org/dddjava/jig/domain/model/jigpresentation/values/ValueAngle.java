package org.dddjava.jig.domain.model.jigpresentation.values;

import org.dddjava.jig.domain.model.declaration.type.TypeIdentifier;
import org.dddjava.jig.domain.model.declaration.type.TypeIdentifiers;
import org.dddjava.jig.domain.model.jigloaded.relation.class_.ClassRelations;
import org.dddjava.jig.domain.model.jigmodel.businessrules.ValueKind;

/**
 * 値の切り口
 */
public class ValueAngle {

    ValueKind valueKind;
    TypeIdentifier typeIdentifier;
    TypeIdentifiers userTypeIdentifiers;

    public ValueAngle(ValueKind valueKind, ClassRelations allClassRelations, ValueType valueType) {
        this.valueKind = valueKind;
        this.typeIdentifier = valueType.typeIdentifier();
        this.userTypeIdentifiers = allClassRelations.collectTypeIdentifierWhichRelationTo(typeIdentifier);
    }

    public TypeIdentifier typeIdentifier() {
        return typeIdentifier;
    }

    public TypeIdentifiers userTypeIdentifiers() {
        return userTypeIdentifiers;
    }
}

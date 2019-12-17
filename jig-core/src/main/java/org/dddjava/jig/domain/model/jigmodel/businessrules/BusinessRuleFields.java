package org.dddjava.jig.domain.model.jigmodel.businessrules;

import org.dddjava.jig.domain.model.declaration.field.FieldDeclarations;
import org.dddjava.jig.domain.model.declaration.field.FieldType;
import org.dddjava.jig.domain.model.declaration.type.TypeIdentifier;

import java.util.List;
import java.util.Set;

public class BusinessRuleFields {
    FieldDeclarations fieldDeclarations;

    public BusinessRuleFields(FieldDeclarations fieldDeclarations) {
        this.fieldDeclarations = fieldDeclarations;
    }

    public boolean satisfyCollection() {
        // ListかSetの1フィールド
        return (fieldDeclarations.matches(new TypeIdentifier(List.class))
                || fieldDeclarations.matches(new TypeIdentifier(Set.class)));
    }

    public FieldType onlyOneFieldType() {
        return fieldDeclarations.onlyOneField().fieldType();
    }

    public FieldDeclarations fieldDeclarations() {
        return fieldDeclarations;
    }

    public boolean satisfyValue(ValueKind valueKind) {
        return valueKind.matches(fieldDeclarations);
    }
}

package org.dddjava.jig.domain.model.values;

import org.dddjava.jig.domain.model.declaration.field.FieldDeclarations;
import org.dddjava.jig.domain.model.declaration.type.TypeIdentifier;
import org.dddjava.jig.domain.model.implementation.bytecode.TypeByteCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * 値の種類
 */
public enum ValueKind {
    IDENTIFIER {
        @Override
        boolean matches(TypeByteCode typeByteCode) {
            if (typeByteCode.isEnum()) return false;
            return typeByteCode.fieldDeclarations().matches(new TypeIdentifier(String.class));
        }
    },
    NUMBER {
        @Override
        boolean matches(TypeByteCode typeByteCode) {
            return typeByteCode.fieldDeclarations().matches(new TypeIdentifier(BigDecimal.class));
        }
    },
    DATE {
        @Override
        boolean matches(TypeByteCode typeByteCode) {
            return typeByteCode.fieldDeclarations().matches(new TypeIdentifier(LocalDate.class));
        }
    },
    TERM {
        @Override
        boolean matches(TypeByteCode typeByteCode) {
            return typeByteCode.fieldDeclarations().matches(new TypeIdentifier(LocalDate.class), new TypeIdentifier(LocalDate.class));
        }
    },
    COLLECTION {
        @Override
        boolean matches(TypeByteCode typeByteCode) {
            FieldDeclarations fieldDeclarations = typeByteCode.fieldDeclarations();
            return fieldDeclarations.matches(new TypeIdentifier(List.class)) || fieldDeclarations.matches(new TypeIdentifier(Set.class));
        }
    };

    abstract boolean matches(TypeByteCode typeByteCode);
}

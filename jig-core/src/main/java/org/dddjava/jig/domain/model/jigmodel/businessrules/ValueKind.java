package org.dddjava.jig.domain.model.jigmodel.businessrules;

import org.dddjava.jig.domain.model.declaration.field.FieldDeclarations;
import org.dddjava.jig.domain.model.declaration.type.TypeIdentifier;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 値の種類
 */
public enum ValueKind {
    IDENTIFIER {
        @Override
        public boolean matches(FieldDeclarations fieldDeclarations) {
            return fieldDeclarations.matches(new TypeIdentifier(String.class));
        }
    },
    NUMBER {
        @Override
        public boolean matches(FieldDeclarations fieldDeclarations) {
            return fieldDeclarations.matches(new TypeIdentifier(BigDecimal.class))
                    || fieldDeclarations.matches(new TypeIdentifier(Long.class))
                    || fieldDeclarations.matches(new TypeIdentifier(Integer.class))
                    || fieldDeclarations.matches(new TypeIdentifier(long.class))
                    || fieldDeclarations.matches(new TypeIdentifier(int.class));
        }
    },
    DATE {
        @Override
        public boolean matches(FieldDeclarations fieldDeclarations) {
            return fieldDeclarations.matches(new TypeIdentifier(LocalDate.class));
        }
    },
    TERM {
        @Override
        public boolean matches(FieldDeclarations fieldDeclarations) {
            return fieldDeclarations.matches(new TypeIdentifier(LocalDate.class), new TypeIdentifier(LocalDate.class));
        }
    };

    abstract boolean matches(FieldDeclarations fieldDeclarations);
}

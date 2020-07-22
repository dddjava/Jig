package org.dddjava.jig.domain.model.jigmodel.lowmodel.alias;

import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.type.TypeIdentifier;

/**
 * 型別名
 */
public class TypeAlias {
    TypeIdentifier typeIdentifier;
    Alias alias;

    public TypeAlias(TypeIdentifier typeIdentifier, Alias alias) {
        this.typeIdentifier = typeIdentifier;
        this.alias = alias;
    }

    public static TypeAlias empty(TypeIdentifier typeIdentifier) {
        return new TypeAlias(typeIdentifier, Alias.empty());
    }

    public TypeIdentifier typeIdentifier() {
        return typeIdentifier;
    }

    public boolean exists() {
        return alias.exists();
    }

    public String asText() {
        return alias.toString();
    }

    public String asTextOrDefault(String defaultText) {
        if (exists()) {
            return asText();
        }
        return defaultText;
    }

    public boolean markedCore() {
        return alias.toString().startsWith("*");
    }

    public String nodeLabel() {
        String aliasLine = "";
        if (exists()) {
            aliasLine = asText() + "\n";
        }
        return aliasLine + typeIdentifier().asSimpleText();
    }
}

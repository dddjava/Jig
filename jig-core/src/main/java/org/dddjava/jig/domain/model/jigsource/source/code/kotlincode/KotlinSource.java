package org.dddjava.jig.domain.model.jigsource.source.code.kotlincode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * .ktソース
 */
public class KotlinSource {

    KotlinSourceFile kotlinSourceFile;
    byte[] value;

    public KotlinSource(KotlinSourceFile kotlinSourceFile, byte[] value) {
        this.kotlinSourceFile = kotlinSourceFile;
        this.value = value;
    }

    public KotlinSourceFile sourceFilePath() {
        return kotlinSourceFile;
    }

    public InputStream toInputStream() {
        return new ByteArrayInputStream(value);
    }

    @Override
    public String toString() {
        return "KotlinSource[" + kotlinSourceFile + "]";
    }
}

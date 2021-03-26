package org.dddjava.jig.domain.model.jigmodel.lowmodel.alias;

import java.util.stream.Stream;

/**
 * ドキュメントコメント
 *
 * 通常はソースコードから読み取るJavadoc
 */
public class DocumentationComment {

    String value;
    volatile String firstSentence = null;

    private DocumentationComment(String value) {
        this.value = value;
    }

    public static DocumentationComment empty() {
        return new DocumentationComment("");
    }

    public boolean exists() {
        return value.length() > 0;
    }

    public String summaryText() {
        if (firstSentence != null) return firstSentence;
        if (value.isEmpty()) {
            firstSentence = "";
            return firstSentence;
        }

        firstSentence = Stream.of(this.value.indexOf("\n"), this.value.indexOf("。"))
                .filter(length -> length >= 0)
                .min(Integer::compareTo)
                .map(end -> this.value.substring(0, end))
                .orElse(this.value);
        return firstSentence; // 改行も句点も無い場合はそのまま返す
    }

    public static DocumentationComment fromText(String sourceText) {
        return new DocumentationComment(sourceText);
    }

    public boolean markedCore() {
        return value.startsWith("*");
    }

    public String fullText() {
        return value;
    }

    public String bodyText() {
        String firstSentence = summaryText();
        if (firstSentence.equals(value)) {
            return "";
        }

        return value.substring(firstSentence.length());
    }
}

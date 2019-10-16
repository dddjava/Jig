package org.dddjava.jig.presentation.view.graphvizj;

import org.dddjava.jig.domain.model.diagram.DotText;

import java.util.Collections;
import java.util.List;

public class DotTexts {
    List<DotText> values;

    public DotTexts(List<DotText> values) {
        this.values = values;
    }

    public DotTexts(DotText dotText) {
        this(Collections.singletonList(dotText));
    }

    public List<DotText> list() {
        return values;
    }

    public boolean isEmpty() {
        if (values.isEmpty()) {
            return true;
        }
        return values.stream().allMatch(DotText::isEmpty);
    }
}

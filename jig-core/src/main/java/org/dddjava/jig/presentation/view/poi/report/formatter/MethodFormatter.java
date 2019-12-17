package org.dddjava.jig.presentation.view.poi.report.formatter;

import org.dddjava.jig.domain.model.jigloaded.richmethod.Method;
import org.dddjava.jig.presentation.view.poi.report.ConvertContext;
import org.dddjava.jig.presentation.view.report.ReportItem;

class MethodFormatter implements ReportItemFormatter {

    ConvertContext convertContext;

    MethodFormatter(ConvertContext convertContext) {
        this.convertContext = convertContext;
    }

    @Override
    public boolean canFormat(Object item) {
        return item instanceof Method;
    }

    @Override
    public String format(ReportItem itemCategory, Object item) {
        Method method = (Method) item;
        switch (itemCategory) {
            case クラス名:
            case クラス別名:
            case メソッドシグネチャ:
            case メソッド別名:
            case メソッド戻り値の型:
            case メソッド戻り値の型の別名:
            case メソッド引数の型の別名:
                return new MethodDeclarationFormatter(convertContext).format(itemCategory, method.declaration());
            case 分岐数:
                return method.decisionNumber().asText();
        }

        throw new IllegalArgumentException(itemCategory.name());
    }
}

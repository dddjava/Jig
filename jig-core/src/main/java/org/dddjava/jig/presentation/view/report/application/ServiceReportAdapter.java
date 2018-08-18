package org.dddjava.jig.presentation.view.report.application;

import org.dddjava.jig.domain.model.declaration.method.MethodDeclaration;
import org.dddjava.jig.domain.model.declaration.type.TypeIdentifier;
import org.dddjava.jig.domain.model.services.ServiceAngle;
import org.dddjava.jig.presentation.view.report.ReportItem;
import org.dddjava.jig.presentation.view.report.ReportItemFor;
import org.dddjava.jig.presentation.view.report.ReportTitle;

@ReportTitle("SERVICE")
public class ServiceReportAdapter {

    ServiceAngle angle;

    public ServiceReportAdapter(ServiceAngle angle) {
        this.angle = angle;
    }

    @ReportItemFor(ReportItem.クラス名)
    @ReportItemFor(ReportItem.クラス和名)
    public TypeIdentifier declaringType() {
        return angle.declaringType();
    }

    @ReportItemFor(ReportItem.メソッドシグネチャ)
    @ReportItemFor(ReportItem.メソッド和名)
    @ReportItemFor(ReportItem.メソッド戻り値の型)
    @ReportItemFor(ReportItem.メソッド戻り値の型の和名)
    @ReportItemFor(ReportItem.メソッド引数の型の和名)
    public MethodDeclaration method() {
        return angle.method();
    }

    @ReportItemFor(ReportItem.イベントハンドラ)
    public boolean usingFromController() {
        return angle.usingFromController();
    }

    @ReportItemFor(value = ReportItem.汎用文字列, label = "使用しているフィールドの型", order = 1)
    public String usingFields() {
        return angle.usingFields();
    }

    @ReportItemFor(value = ReportItem.汎用文字列, label = "使用しているリポジトリのメソッド", order = 2)
    public String usingRepositoryMethods() {
        return angle.usingRepositoryMethods();
    }

    @ReportItemFor(value = ReportItem.汎用真偽値, label = "stream使用", order = 3)
    public boolean useStream() {
        return angle.useStream();
    }
}

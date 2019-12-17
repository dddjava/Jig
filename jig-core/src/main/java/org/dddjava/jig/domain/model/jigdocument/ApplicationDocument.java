package org.dddjava.jig.domain.model.jigdocument;

/**
 * アプリケーションドキュメント
 */
public enum ApplicationDocument {

    /**
     * 機能一覧
     *
     * 機能を提供するメソッドの一覧。
     * 三層（プレゼンテーション層、アプリケーション層、データソース層）の情報を提供する。
     * アプリケーションの状況把握に使用できる。
     *
     * 制限事項: {@link org.dddjava.jig.infrastructure.mybatis.MyBatisSqlReader}
     */
    ApplicationList,

    /**
     * 分岐数一覧
     *
     * メソッドごとの分岐数の一覧。
     */
    BranchList,

    /**
     * サービスメソッド呼び出しダイアグラム
     *
     * サービスクラスのメソッド呼び出しを可視化する。
     */
    ServiceMethodCallHierarchyDiagram,

    /**
     * 真偽値サービス関連ダイアグラム
     *
     * 真偽値を返すサービスと使用しているメソッドを可視化する。
     * 真偽値にビジネスルールが埋もれていないかの検出に使用できる。
     */
    BooleanServiceDiagram;
}

package org.dddjava.jig.application.service;

import org.dddjava.jig.domain.basic.ClassFindFailException;
import org.dddjava.jig.domain.model.implementation.ProjectData;
import org.dddjava.jig.domain.model.implementation.bytecode.ByteCodeFactory;
import org.dddjava.jig.domain.model.implementation.bytecode.ByteCodeSources;
import org.dddjava.jig.domain.model.implementation.bytecode.ByteCodes;
import org.dddjava.jig.domain.model.implementation.datasource.SqlReader;
import org.dddjava.jig.domain.model.implementation.datasource.SqlSources;
import org.dddjava.jig.domain.model.implementation.datasource.Sqls;
import org.dddjava.jig.infrastructure.LocalProject;
import org.springframework.stereotype.Service;

/**
 * 取り込みサービス
 */
@Service
public class ImplementationService {

    final GlossaryService glossaryService;

    final ByteCodeFactory byteCodeFactory;
    final SqlReader sqlReader;

    public ImplementationService(ByteCodeFactory byteCodeFactory, GlossaryService glossaryService, SqlReader sqlReader) {
        this.byteCodeFactory = byteCodeFactory;
        this.glossaryService = glossaryService;
        this.sqlReader = sqlReader;
    }

    /**
     * プロジェクト情報を読み取る
     */
    public ProjectData readProjectData(LocalProject target) {
        ByteCodes byteCodes = readByteCode(target.getByteCodeSources());
        Sqls sqls = readSql(target.getSqlSources());

        ProjectData projectData = ProjectData.from(byteCodes, sqls);

        glossaryService.importJapanese(target.getTypeNameSources());
        glossaryService.importJapanese(target.getPackageNameSources());

        return projectData;
    }

    /**
     * ソースからバイトコードを読み取る
     */
    public ByteCodes readByteCode(ByteCodeSources byteCodeSources) {
        if (byteCodeSources.notFound()) {
            throw new ClassFindFailException();
        }

        return byteCodeFactory.readFrom(byteCodeSources);
    }

    /**
     * ソースからSQLを読み取る
     */
    public Sqls readSql(SqlSources sqlSources) {
        return sqlReader.readFrom(sqlSources);
    }
}

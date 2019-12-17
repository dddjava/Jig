package org.dddjava.jig.domain.model.jigloaded.alias;

import org.dddjava.jig.domain.model.jigsource.source.code.kotlincode.KotlinSources;

/**
 * KotlinSourceから別名を読み取る
 */
public interface KotlinSourceAliasReader {

    TypeAliases readAlias(KotlinSources kotlinSources);
}

package org.dddjava.jig.domain.model.jigmodel.applications.repositories;

import org.dddjava.jig.domain.model.declaration.type.ParameterizedType;
import org.dddjava.jig.domain.model.declaration.type.ParameterizedTypes;
import org.dddjava.jig.domain.model.declaration.type.TypeIdentifier;
import org.dddjava.jig.domain.model.jigloaded.richmethod.Method;
import org.dddjava.jig.domain.model.jigmodel.architecture.Architecture;
import org.dddjava.jig.domain.model.jigmodel.architecture.BuildingBlock;
import org.dddjava.jig.domain.model.jigsource.bytecode.MethodByteCode;
import org.dddjava.jig.domain.model.jigsource.bytecode.TypeByteCode;
import org.dddjava.jig.domain.model.jigsource.bytecode.TypeByteCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * データソースメソッド一覧
 */
public class DatasourceMethods {
    List<DatasourceMethod> list;

    public DatasourceMethods(TypeByteCodes typeByteCodes, Architecture architecture) {
        this.list = new ArrayList<>();
        for (TypeByteCode concreteByteCode : typeByteCodes.list()) {
            if (!BuildingBlock.DATASOURCE.satisfy(concreteByteCode, architecture)) {
                continue;
            }

            ParameterizedTypes parameterizedTypes = concreteByteCode.parameterizedInterfaceTypes();
            for (ParameterizedType parameterizedType : parameterizedTypes.list()) {
                TypeIdentifier interfaceTypeIdentifier = parameterizedType.typeIdentifier();

                for (TypeByteCode interfaceByteCode : typeByteCodes.list()) {
                    if (!interfaceTypeIdentifier.equals(interfaceByteCode.typeIdentifier())) {
                        continue;
                    }

                    for (MethodByteCode interfaceMethodByteCode : interfaceByteCode.methodByteCodes()) {
                        concreteByteCode.methodByteCodes().stream()
                                .filter(datasourceMethodByteCode -> interfaceMethodByteCode.sameSignature(datasourceMethodByteCode))
                                // 0 or 1
                                .forEach(concreteMethodByteCode -> list.add(new DatasourceMethod(
                                        new Method(interfaceMethodByteCode),
                                        new Method(concreteMethodByteCode),
                                        concreteMethodByteCode.usingMethods()))
                                );
                    }
                }
            }
        }
    }

    public List<DatasourceMethod> list() {
        return list;
    }

    public boolean empty() {
        return list.isEmpty();
    }

    public RepositoryMethods repositoryMethods() {
        return list.stream().map(DatasourceMethod::repositoryMethod)
                .collect(Collectors.collectingAndThen(Collectors.toList(), RepositoryMethods::new));
    }
}

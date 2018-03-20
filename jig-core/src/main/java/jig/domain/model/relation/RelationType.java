package jig.domain.model.relation;

import jig.domain.model.specification.MethodSpecification;
import jig.domain.model.specification.Specification;
import jig.domain.model.thing.Identifier;
import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum RelationType {
    DEPENDENCY,
    FIELD,
    METHOD,
    METHOD_RETURN_TYPE,
    METHOD_PARAMETER,
    METHOD_USE_TYPE,
    IMPLEMENT,
    METHOD_USE_METHOD;

    public Relation of(Identifier from, Identifier to) {
        return new Relation(from, to, this);
    }

    public static void register(RelationRepository repository, Specification specification) {

        specification.interfaceNames.list().forEach(name -> {
            repository.register(new Relation(specification.identifier, name, IMPLEMENT));
        });

        specification.fieldDescriptors.forEach(descriptor -> {
            Type fieldType = Type.getType(descriptor.toString());
            repository.register(RelationType.FIELD.of(specification.identifier, new Identifier(fieldType.getClassName())));
        });

        specification.methodSpecifications.forEach(methodDescriptor -> {
            String descriptor = methodDescriptor.descriptor;
            String name = methodDescriptor.methodName;
            Identifier classIdentifier = specification.identifier;

            // パラメーターの型
            Type[] argumentTypes = Type.getArgumentTypes(descriptor);

            // メソッド
            String argumentsString = Arrays.stream(argumentTypes).map(Type::getClassName).collect(Collectors.joining(",", "(", ")"));
            Identifier methodIdentifier = new Identifier(classIdentifier.value() + "." + name + argumentsString);
            repository.register(RelationType.METHOD.of(classIdentifier, methodIdentifier));

            // 戻り値の型
            Identifier returnTypeIdentifier = methodDescriptor.getReturnTypeName();
            repository.register(RelationType.METHOD_RETURN_TYPE.of(methodIdentifier, returnTypeIdentifier));

            for (Type type : argumentTypes) {
                Identifier argumentTypeIdentifier = new Identifier(type.getClassName());
                repository.register(RelationType.METHOD_PARAMETER.of(methodIdentifier, argumentTypeIdentifier));
            }

            for (Identifier interfaceIdentifier : specification.interfaceNames.list()) {
                repository.register(RelationType.IMPLEMENT.of(methodIdentifier, interfaceIdentifier.concat(methodIdentifier)));
            }

            registerMethodInstruction(repository, methodDescriptor);
        });
    }

    private static void registerMethodInstruction(RelationRepository repository, MethodSpecification methodSpecification) {
        methodSpecification.usingFieldTypeIdentifiers.forEach(fieldTypeName -> {
            repository.register(RelationType.METHOD_USE_TYPE.of(methodSpecification.identifier, fieldTypeName));
        });

        methodSpecification.usingMethodIdentifiers.forEach(methodName -> {
            repository.register(RelationType.METHOD_USE_METHOD.of(methodSpecification.identifier, methodName));
        });
    }
}

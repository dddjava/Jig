package org.dddjava.jig.domain.model.jigpresentation.collections;

import org.dddjava.jig.domain.model.jigloaded.relation.class_.ClassRelations;

import java.util.ArrayList;
import java.util.List;

/**
 * コレクションの切り口一覧
 */
public class CollectionAngles {

    List<CollectionAngle> list;

    public CollectionAngles(CollectionTypes collectionTypes, ClassRelations classRelations) {
        this.list = new ArrayList<>();
        for (CollectionType collectionType : collectionTypes.list()) {
            list.add(new CollectionAngle(collectionType, classRelations));
        }
    }

    public List<CollectionAngle> list() {
        return list;
    }
}

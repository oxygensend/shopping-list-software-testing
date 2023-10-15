package com.oxygensend.backend.domain.shooping_list;

import java.util.List;
import java.util.Set;

public interface ProductRepository {

    List<Product> findByNames(Set<String> names);
}

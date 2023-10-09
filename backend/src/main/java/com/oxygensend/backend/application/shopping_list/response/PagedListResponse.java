package com.oxygensend.backend.application.shopping_list.response;

import java.util.List;

public record PagedListResponse<T>(List<T> data, long numberOfElements, long numberOfPages) {


}

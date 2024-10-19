package com.nikafom.englishAssistant.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PaginationUtil {
    public static Pageable getPageRequest(Integer page, Integer perPage, String sort, Sort.Direction order) {
        if(page == null) {
            page = 0;
        } else if(page > 0) {
            page = page -1;
        }

        if(perPage == null) {
            perPage = 10;
        }

        if(order == null || sort == null) {
            return PageRequest.of(page, perPage);
        } else {
            return PageRequest.of(page, perPage, Sort.by(order, sort));
        }
    }
}

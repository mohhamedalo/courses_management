package com.example.demo2.mapper;

import com.example.demo2.dto.response.CategoryResponse;
import com.example.demo2.entity.Category;

public class CategoryMapper {

//    public static BookResponse toResponse(Book b) {
//        String fullName = b.getAuthor().getFirstName() + " " + b.getAuthor().getLastName();
//        return new BookResponse(
//                b.getId(),
//                b.getTitle(),
//                b.getStatus(),
//                b.getCreatedAt(),
//                b.getAuthor().getId(),
//                fullName
//        );
//    }

    public static CategoryResponse toResponse(Category c) {
        return new CategoryResponse(
                c.getId(),
                c.getName(),
                c.getDescription()
        );
    }
}

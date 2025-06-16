package com.example.project_system.repository;

import com.example.project_system.entity.Book;
import com.example.project_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUser(User user);

    Optional<Book> findByUserUsernameAndIsbn(String username, String isbn);
}
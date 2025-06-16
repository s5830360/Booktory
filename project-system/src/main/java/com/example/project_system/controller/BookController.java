package com.example.project_system.controller;

import com.example.project_system.entity.Book;
import com.example.project_system.repository.BookRepository;
import com.example.project_system.dto.BookStatusUpdateRequest;
import com.example.project_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import jakarta.servlet.http.HttpSession;
import com.example.project_system.entity.User;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getBooksByUsername(@RequestParam String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("유저를 찾을 수 없습니다.");
        }

        User user = userOpt.get();
        List<Book> books = bookRepository.findByUser(user);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<String> saveBook(@RequestBody Book book) {
        if (book.getUser() == null || book.getUser().getUsername() == null) {
            return ResponseEntity.badRequest().body("user 정보가 필요합니다.");
        }

        Optional<User> userOpt = userRepository.findByUsername(book.getUser().getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("해당 유저가 존재하지 않습니다.");
        }

        Optional<Book> existing = bookRepository.findByUserUsernameAndIsbn(book.getUser().getUsername(), book.getIsbn());
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 책입니다.");
        }

        book.setUser(userOpt.get());
        bookRepository.save(book);
        return ResponseEntity.ok("Book saved");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBookStatus(@PathVariable Long id, @RequestBody BookStatusUpdateRequest request) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Book book = optionalBook.get();
        book.setStatus(request.getStatus());
        bookRepository.save(book);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
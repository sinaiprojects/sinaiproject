package com.example.project2.repository;

import com.example.project2.model.notes;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface notesrepository extends JpaRepository<notes, Long> {
  List<notes> findByPublished(boolean published);
  List<notes> findByTitleContaining(String title);
}
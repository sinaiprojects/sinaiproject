package com.example.project2.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.project2.model.notes;
import com.example.project2.repository.notesrepository;
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class notescontroller {
  @Autowired
  notesrepository notesrepository;
  @GetMapping("/notes")
  public ResponseEntity<List<notes>> getAllTutorials(@RequestParam(required = false) String title) {
    try {
      List<notes> notes = new ArrayList<notes>();
      if (title == null)
      notesrepository.findAll().forEach(notes::add);
      else
      notesrepository.findByTitleContaining(title).forEach(notes::add);
      if (notes.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(notes, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @GetMapping("/notes/{id}")
  public ResponseEntity<notes> getTutorialById(@PathVariable("id") long id) {
    Optional<notes> notesData = notesrepository.findById(id);
    if (notesData.isPresent()) {
      return new ResponseEntity<>(notesData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  @PostMapping("/notes")
  public ResponseEntity<notes> createTutorial(@RequestBody notes notes) {
    try {
        notes _notes = notesrepository
          .save(new notes(notes.getTitle(), notes.getDescription(), false));
      return new ResponseEntity<>(_notes, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @PutMapping("/_notes/{id}")
  public ResponseEntity<notes> updateTutorial(@PathVariable("id") long id, @RequestBody notes notes) {
    Optional<notes> notesData = notesrepository.findById(id);
    if (notesData.isPresent()) {
        notes _notes = notesData.get();
        _notes.setTitle(notes.getTitle());
        _notes.setDescription(notes.getDescription());
        _notes.setPublished(notes.isPublished());
      return new ResponseEntity<>(notesrepository.save(_notes), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  @DeleteMapping("/notes/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
    try {
        notesrepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @DeleteMapping("/notes")
  public ResponseEntity<HttpStatus> deleteAllTutorials() {
    try {
        notesrepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @GetMapping("/notes/published")
  public ResponseEntity<List<notes>> findByPublished() {
    try {
      List<notes> notes = notesrepository.findByPublished(true);
      if (notes.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(notes, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}

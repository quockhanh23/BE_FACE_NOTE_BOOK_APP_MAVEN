package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.exeption;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String msg) {
    super(msg);
  }
}

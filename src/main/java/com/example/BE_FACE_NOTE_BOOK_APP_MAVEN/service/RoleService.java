package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;


import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Role;

public interface RoleService {

    Iterable<Role> findAll();

    void save(Role role);

    Role findByName(String name);
}

package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;


import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Role;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.RoleRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Iterable<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}

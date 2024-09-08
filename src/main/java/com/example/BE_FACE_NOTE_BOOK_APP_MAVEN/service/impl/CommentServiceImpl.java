package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.CommentDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Comment;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.CommentRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findAllByUserId(Long id) {
        return commentRepository.findAllByUserId(id);
    }

    @Override
    public List<Comment> getCommentByIdPost(Long id) {
        return commentRepository.getCommentByIdPost(id);
    }

    @Override
    public List<Comment> getCommentTrue() {
        return commentRepository.getCommentTrue();
    }

    @Override
    public CommentDTO mapper(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public void create(Comment comment) {
        comment.setNumberLike(0L);
        comment.setNumberDisLike(0L);
        comment.setCreateAt(LocalDateTime.now());
        comment.setDelete(false);
        comment.setEditAt(null);
    }

    @Override
    public void saveAll(List<Comment> comments) {
        commentRepository.saveAll(comments);
    }

    @Override
    public List<Comment> findAllByIdIn(List<Long> inputList) {
        return commentRepository.findAllByIdIn(inputList);
    }

    @Override
    public List<Comment> findAllByPostIdIn(List<Long> post_id) {
        return commentRepository.findAllByPostIdInAndDeleteAtIsNull(post_id);
    }
}

package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.PostDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.exeption.InvalidException;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final LikePostService likePostService;

    private final DisLikePostService disLikePostService;

    private final IconHeartService iconHeartService;

    private final CommentService commentService;

    private final AnswerCommentService answerCommentService;

    private final LikeCommentRepository likeCommentRepository;

    private final DisLikeCommentRepository disLikeCommentRepository;

    private final AnswerCommentRepository answerCommentRepository;

    private final HidePostRepository hidePostRepository;

    private final ModelMapper modelMapper;

//    @PersistenceContext
//    private EntityManager entityManager;

    @Override
    @Cacheable(cacheNames = "findAllPost")
    public Iterable<Post2> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post2> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    @CacheEvict(cacheNames = {"allPost", "findAllPostByUser", "findAllPost"}, allEntries = true)
    public Post2 save(Post2 post) {
        return postRepository.save(post);
    }

    @Override
    @Cacheable(cacheNames = "findAllPostByUser", key = "#id")
    public Page<Post2> findAllPostByUser(Long id, Pageable pageable) {
        return postRepository.findAllPostByUser(id, pageable);
    }

    @Override
    public Page<Post2> allPost(Long id, Pageable pageable) {
        return postRepository.AllPost(id, pageable);
    }

    @Override
    @CacheEvict(cacheNames = {"allPost", "findAllPostByUser", "findAllPost", "findAllByUserIdAndDeleteTrue"},
            allEntries = true)
    public void delete(Post2 entity) {
        postRepository.delete(entity);
    }

    @Override
    public PostDTO mapper(Post2 post2) {
        return modelMapper.map(post2, PostDTO.class);
    }

    @Override
    public void createDefaultPost(Post2 post2) {
        if (StringUtils.isEmpty(post2.getStatus())) {
            post2.setStatus(Constants.STATUS_PUBLIC);
        }
        post2.setEditAt(null);
        post2.setDelete(false);
        post2.setCreateAt(new Date());
        post2.setIconHeart(0L);
        post2.setNumberDisLike(0L);
        post2.setNumberLike(0L);
    }

    @Override
    @CacheEvict(cacheNames = {"allPost", "findAllPostByUser", "findAllPost", "findAllByUserIdAndDeleteTrue"},
            allEntries = true)
    public void saveAll(List<Post2> post2List) {
        postRepository.saveAll(post2List);
    }

    @Override
    public List<PostDTO> changeDTO(List<Post2> post2List) {
        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post2 post2 : post2List) {
            UserDTO userDTO = modelMapper.map(post2.getUser(), UserDTO.class);
            PostDTO postDTO = modelMapper.map(post2, PostDTO.class);
            postDTO.setUserDTO(userDTO);
            postDTOList.add(postDTO);
        }
        return postDTOList;
    }

    @Override
    @Cacheable(cacheNames = "findAllByUserIdAndDeleteTrue", key = "#user_id")
    public List<Post2> findAllByUserIdAndDeleteTrue(Long user_id) {
        return postRepository.findAllByUserIdAndDeleteIsTrue(user_id);
    }

    @Override
    public List<PostDTO> filterListPost(List<Post2> post2List) {
        List<PostDTO> postDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(post2List)) {
            postDTOList = changeDTO(post2List);
            List<Long> listPostId = post2List.stream().map(Post2::getId).collect(Collectors.toList());
            List<LikePost> likePosts = likePostService.findAllByPostIdIn(listPostId);
            List<DisLikePost> disLikePosts = disLikePostService.findAllByPostIdIn(listPostId);
            List<IconHeart> iconHearts = iconHeartService.findAllByPostIdIn(listPostId);
            List<Comment> commentList = commentService.findAllByPostIdIn(listPostId);
            for (PostDTO postDTO : postDTOList) {
                Long idPost = postDTO.getId();
                List<LikePost> likePostList = likePosts.stream()
                        .filter(item -> item.getPost().getId().equals(idPost)).toList();
                List<DisLikePost> disLikePostList = disLikePosts.stream()
                        .filter(item -> item.getPost().getId().equals(idPost)).toList();
                List<IconHeart> iconHeartList = iconHearts.stream()
                        .filter(item -> item.getPost().getId().equals(idPost)).toList();
                List<Comment> comments = commentList.stream()
                        .filter(item -> item.getPost().getId().equals(idPost)).toList();
                List<Long> listCommentId = comments.stream().map(Comment::getId).collect(Collectors.toList());
                List<AnswerComment> answerCommentList = answerCommentService.findAllByCommentIdIn(listCommentId);
                int countAllCommentAndAnswerComment = listCommentId.size() + answerCommentList.size();
                postDTO.setNumberLike((long) likePostList.size());
                postDTO.setNumberDisLike((long) disLikePostList.size());
                postDTO.setIconHeart((long) iconHeartList.size());
                postDTO.setCountAllComment(countAllCommentAndAnswerComment);
            }
        }
        return postDTOList;
    }

    @Override
    public void deleteRelateOfComment(List<Comment> comments) {
        List<Long> listIdComment = comments.stream().map(Comment::getId).collect(Collectors.toList());
        List<LikeComment> likeComments = likeCommentRepository.findAllByCommentIdIn(listIdComment);
        List<DisLikeComment> disLikeComments = disLikeCommentRepository.findAllByCommentIdIn(listIdComment);
        List<AnswerComment> answerCommentList = answerCommentService.findAllByCommentIdIn(listIdComment);
        likeCommentRepository.deleteAllInBatch(likeComments);
        disLikeCommentRepository.deleteAllInBatch(disLikeComments);
        answerCommentRepository.deleteAllInBatch(answerCommentList);
    }

    @Override
    public Post2 checkExistPost(Long idPost) {
        Optional<Post2> post2Optional = findById(idPost);
        if (post2Optional.isEmpty()) throw new InvalidException(MessageResponse.NOT_FOUND_POST + idPost);
        return post2Optional.get();
    }

    @Override
    public Page<PostDTO> allPostPublic(Long idUser, String type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post2> post2Page = allPost(idUser, pageable);
        List<PostDTO> postDTOList;
        Page<PostDTO> pagePostDTO = null;
        if (CollectionUtils.isEmpty(post2Page.getContent())) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        if (type.equals("detailUser")) {
            Page<Post2> post2PageDetailUser = findAllPostByUser(idUser, pageable);
            postDTOList = filterListPost(post2PageDetailUser.getContent());
            pagePostDTO = new PageImpl<>(postDTOList,
                    post2PageDetailUser.getPageable(),
                    post2PageDetailUser.getTotalElements()
            );
            return pagePostDTO;
        }
        if (type.equals("getAll")) {
            postDTOList = filterListPost(post2Page.getContent());
            pagePostDTO = new PageImpl<>(postDTOList,
                    post2Page.getPageable(),
                    post2Page.getTotalElements()
            );
            return pagePostDTO;
        }
        if (type.equals("getAllByUser")) {
            postDTOList = filterListPost(post2Page.getContent());
            List<HidePost> hidePosts = hidePostRepository.findAllByIdUser(idUser);
            if (!CollectionUtils.isEmpty(hidePosts)) {
                List<Long> listIdPost = hidePosts.stream().map(HidePost::getIdPost).toList();
                for (Long id : listIdPost) {
                    postDTOList.stream().filter(item -> item.getId().equals(id)).findFirst()
                            .ifPresent(post2 -> post2.setContent(null));
                }
            }
            pagePostDTO = new PageImpl<>(postDTOList,
                    post2Page.getPageable(),
                    post2Page.getTotalElements()
            );
        }
        return pagePostDTO;
    }

//    public List<Post2> getListLCReFile(String lcRef, String requestCode, Integer productType) {
//        StringBuffer sql = new StringBuffer("Select * from tbl_ref_file where is_deleted = false ");
//        if (lcRef != null && !"".equals(lcRef)) {
//            sql.append(" and lc_ref =:lcRef ");
//        }
//        if (requestCode != null && !"".equals(requestCode)) {
//            sql.append(" and request_code = :requestCode ");
//        }
//        if (productType != null) {
//            sql.append(" and product_type = :productType ");
//        }
//
//        Query query = (Query) entityManager.createNativeQuery(sql.toString(), Comment.class);
//        if (lcRef != null && !"".equals(lcRef)) {
//            query.setParameter("lcRef", lcRef);
//        }
//        if (requestCode != null && !"".equals(requestCode)) {
//            query.setParameter("requestCode", requestCode);
//        }
//        if (productType != null) {
//            query.setParameter("productType", productType);
//        }
//        if (requestCode == null && productType == null) {
//            query.setParameter("lcRef", lcRef);
//        }
//        if (lcRef == null && productType == null) {
//            query.setParameter("requestCode", requestCode);
//        }
//        if (lcRef == null && requestCode == null) {
//            query.setParameter("productType", productType);
//        }
//        return query.getResultList();
//    }

}

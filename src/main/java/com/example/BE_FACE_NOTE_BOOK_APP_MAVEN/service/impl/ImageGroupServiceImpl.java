package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.ImageGroup;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.ImageGroupRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.ImageGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageGroupServiceImpl implements ImageGroupService {

    @Autowired
    private ImageGroupRepository imageGroupRepository;

    @Override
    public ImageGroup createImageGroupDefault(String image, Long idTheGroup, Long idUserUpLoad) {
        ImageGroup imageGroup = new ImageGroup();
        imageGroup.setStatus(Constants.STATUS_PUBLIC);
        imageGroup.setIdTheGroup(idTheGroup);
        imageGroup.setIdUserUpLoad(idUserUpLoad);
        imageGroup.setDeleteAt(null);
        return imageGroup;
    }

    @Override
    public void save(ImageGroup imageGroup) {
        imageGroupRepository.save(imageGroup);
    }
}

package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.ShortNews;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.ShortNewsRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.ShortNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ShortNewsServiceImpl implements ShortNewsService {

    private final ShortNewsRepository shortNewsRepository;

    @Autowired
    public ShortNewsServiceImpl(ShortNewsRepository shortNewsRepository) {
        this.shortNewsRepository = shortNewsRepository;
    }

    @Override
    public Iterable<ShortNews> findAll() {
        return shortNewsRepository.findAll();
    }

    @Override
    public Optional<ShortNews> findById(Long id) {
        return shortNewsRepository.findById(id);
    }

    @Override
    @CacheEvict(cacheNames = {"getListShortNewInTrash", "myShortNew", "findAllShortNewsPublic"}, allEntries = true)
    public ShortNews save(ShortNews shortNews) {
        return shortNewsRepository.save(shortNews);
    }

    @Override
    @CacheEvict(cacheNames = {"getListShortNewInTrash", "myShortNew", "findAllShortNewsPublic"}, allEntries = true)
    public void delete(ShortNews entity) {
        shortNewsRepository.delete(entity);
    }

    @Override
    public void createDefaultShortNews(ShortNews shortNews) {
        shortNews.setCreateAt(new Date());
        shortNews.setToDay(new Date());
        shortNews.setExpired(3);
        shortNews.setRemaining(3);
        if (StringUtils.isEmpty(shortNews.getImage())) {
            shortNews.setImage(Constants.ImageDefault.DEFAULT_IMAGE_SHORT_NEW);
        }
    }

    @Override
    @CacheEvict(cacheNames = {"getListShortNewInTrash", "myShortNew", "findAllShortNewsPublic"}, allEntries = true)
    public void saveAll(List<ShortNews> shortNews) {
        shortNewsRepository.saveAll(shortNews);
    }

    @Override
    public boolean checkYear(int year) {
        if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else if (year % 4 == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ShortNews> findAllShortNews() {
        return shortNewsRepository.findAllShortNews();
    }

    @Override
    @Cacheable(cacheNames = "findAllShortNewsPublic")
    public List<ShortNews> findAllShortNewsPublic() {
        return shortNewsRepository.findAllShortNewsPublic();
    }

    @Override
    @Cacheable(cacheNames = "myShortNew", key = "#idUser")
    public List<ShortNews> myShortNew(Long idUser) {
        return shortNewsRepository.myShortNew(idUser);
    }

    @Override
    @Cacheable(cacheNames = "getListShortNewInTrash", key = "#idUser")
    public List<ShortNews> getListShortNewInTrash(Long idUser) {
        return shortNewsRepository.getListShortNewInTrash(idUser);
    }
}

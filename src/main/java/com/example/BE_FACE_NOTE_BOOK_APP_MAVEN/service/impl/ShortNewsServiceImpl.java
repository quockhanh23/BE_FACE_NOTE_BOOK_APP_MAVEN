package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.ShortNewsExpiredStatus;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.ShortNews;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.ShortNewsRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.ShortNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
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
    public void createShortNews(ShortNews shortNews, User user) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 3); // Cộng thêm 3 ngày
        Date expirationDate = calendar.getTime();
        shortNews.setCreatedAt(date);
        shortNews.setExpirationDate(expirationDate);
        shortNews.setDelete(false);
        shortNews.setExpired(ShortNewsExpiredStatus.N);
        if (StringUtils.isEmpty(shortNews.getImage())) {
            shortNews.setImage(Constants.ImageDefault.DEFAULT_IMAGE_SHORT_NEW);
        }
        if (StringUtils.isEmpty(shortNews.getStatus())) {
            shortNews.setStatus(Constants.STATUS_PUBLIC);
        }
        shortNews.setUser(user);
    }

    @Override
    @CacheEvict(cacheNames = {"getListShortNewInTrash", "myShortNew", "findAllShortNewsPublic"}, allEntries = true)
    public void saveAll(List<ShortNews> shortNews) {
        shortNewsRepository.saveAll(shortNews);
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

    @Override
    public void checkExpiryDate() {
        List<ShortNews> shortNews = shortNewsRepository.checkExpiryDate();
        if (CollectionUtils.isEmpty(shortNews)) return;
        Date currentDate = new Date();
        for (ShortNews shortNew : shortNews) {
            if (currentDate.compareTo(shortNew.getExpirationDate()) > 0) {
                shortNew.setExpired(ShortNewsExpiredStatus.Y);
                shortNew.setUpdatedAt(currentDate);
            }
        }
        shortNewsRepository.saveAll(shortNews);
    }
}

package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.BlackList;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.BlackListRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BlackListServiceImpl implements BlackListService {

    private final BlackListRepository blackListRepository;

    @Autowired
    public BlackListServiceImpl(BlackListRepository blackListRepository) {
        this.blackListRepository = blackListRepository;
    }

    @Override
    public void create(BlackList blackList) {
        blackListRepository.save(blackList);
    }

    @Override
    public void createDefault(BlackList blackList, Long idUserLogin, Long idBlock, String type) {
        try {
            if (Constants.CREATE.equals(type)) {
                blackList.setCreateAt(new Date());
                blackList.setIdUserSendBlock(idUserLogin);
                blackList.setIdUserOnTheList(idBlock);
                blackList.setStatus(Constants.BLOCKED);
                blackListRepository.save(blackList);
            }
            if (Constants.UPDATE.equals(type)) {
                blackList.setEditAt(new Date());
                blackList.setStatus(Constants.UN_BLOCKED);
                blackListRepository.save(blackList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(BlackList blackList) {
        blackListRepository.delete(blackList);
    }

    @Override
    public void saveAll(List<BlackList> blackLists) {
        blackListRepository.saveAll(blackLists);
    }

    @Override
    public Optional<BlackList> findById(Long id) {
        return blackListRepository.findById(id);
    }

    @Override
    public Optional<BlackList> findBlock(Long idSendBlock, Long idBlock) {
        return blackListRepository.findBlock(idSendBlock, idBlock);
    }

    @Override
    public List<BlackList> listBlockedByIdUser(Long idSendBlock) {
        return blackListRepository.listBlockedByIdUser(idSendBlock);
    }
}

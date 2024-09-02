package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    @Query(value = "select * from black_list where (id_user_send_block = :idSendBlock) and (id_user_on_the_list = :idBlock)", nativeQuery = true)
    Optional<BlackList> findBlock(@Param("idSendBlock") Long idSendBlock, @Param("idBlock") Long idBlock);

    @Query(value = "select * from black_list where id_user_send_block =:idSendBlock and status = 'Blocked' order by create_at desc", nativeQuery = true)
    List<BlackList> listBlockedByIdUser(@Param("idSendBlock") Long idSendBlock);
}

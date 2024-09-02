package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;


import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Set<User> findAllByIdIn(@Param("inputList") Set<Long> inputList);

    List<User> findAllByIdIn(@Param("inputList") List<Long> inputList);

    @Query(value = "select * from user_table where (username =:userName) and (email =:email)", nativeQuery = true)
    Optional<User> findUserByEmailAndUserName(@Param("userName") String userName, @Param("email") String email);

    @Query(value = "select *\n" +
            "from user_table\n" +
            "where email LIKE concat('%', :searchText, '%') or full_name LIKE concat('%', :searchText, '%')", nativeQuery = true)
    List<User> findAllByEmailOrUsername(@Param("searchText") String searchText);

    @Modifying
    @Query(value = "select * from user_table join user_role on user_table.id = user_role.user_id where role_id = 1",
            nativeQuery = true)
    List<User> findAllRoleUser();

    @Modifying
    @Query(value = "select * from user_table where status = 'Banned'", nativeQuery = true)
    List<User> findAllUserBanned();

    @Modifying
    @Query(value = "select *\n" +
            "from user_table\n" +
            "where id not in (select id_friend\n" +
            "                 from friend_relation\n" +
            "                 where (id_user = :idUser)\n" +
            "                   and status_friend in ('Waiting', 'Friend'))\n" +
            "  and id not in (select user_id from user_role where role_id = 2)\n" +
            "  and user_table.status = 'Active'\n" +
            "  and id != :idUser\n" +
            "order by id desc;", nativeQuery = true)
    List<User> friendSuggestion(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select *\n" +
            "from user_table\n" +
            "where id not in (select user_id from user_role where role_id = 2)\n" +
            "  and id not in (select id_friend\n" +
            "                 from friend_relation\n" +
            "                 where id_user = :idUser\n" +
            "                   and friend_relation.status_friend = 'waiting'\n" +
            "                   and id_user = user_login_id)\n" +
            "  and id not in (select id_friend\n" +
            "                 from friend_relation\n" +
            "                 where id_user = :idUser\n" +
            "                   and friend_relation.status_friend = 'waiting'\n" +
            "                   and id_user != user_login_id)\n" +
            "  and id not in\n" +
            "      (select id_friend from friend_relation where id_user = :idUser and friend_relation.status_friend = 'Friend')\n" +
            "  and status = 'Active';\n", nativeQuery = true)
    List<User> listPeople(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select *\n" +
            "from user_table\n" +
            "where id in (select id_friend from friend_relation where id_user = :idUser and status_friend = 'Friend')",
            nativeQuery = true)
    List<User> allFriendByUserId(@Param("idUser") Long idUser);


    @Modifying
    @Query(value = "select *\n" +
            "from user_table\n" +
            "where id not in (select id_friend\n" +
            "                 from friend_relation\n" +
            "                 where id_user = :idUser\n" +
            "                   and friend_relation.status_friend = 'Friend')", nativeQuery = true)
    List<User> listPeopleNoImpact(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "SELECT *\n" +
            "FROM user_table u\n" +
            "WHERE (u.email LIKE concat('%', :searchText, '%')\n" +
            "    or u.full_name LIKE concat('%', :searchText, '%'))\n" +
            "  and (u.id not in\n" +
            "       (select id_user_on_the_list from black_list where id_user_send_block = :idSendBlock and status = 'Blocked'))\n" +
            "  and (u.id not in (select id from user_table where status = 'Banned'))", nativeQuery = true)
    List<User> findAllByEmailAndFullName(String searchText, @Param("idSendBlock") Long idSendBlock);


    @Modifying
    @Query(value = "select *\n" +
            "from user_table ut\n" +
            "where (id in (select id_friend from friend_relation where id_user = :idUser and status_friend = 'Friend'))\n" +
            "  and (ut.full_name LIKE concat('%', :searchText, '%') or ut.email LIKE concat('%', :searchText, '%'))",
            nativeQuery = true)
    List<User> searchFriend(@Param("searchText") String searchText, @Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select *\n" +
            "from user_table\n" +
            "where id not in (select user_id from user_role where role_id = 2)\n" +
            "  and id not in (select id_friend\n" +
            "                 from friend_relation\n" +
            "                 where id_user = :idUser\n" +
            "                   and friend_relation.status_friend = 'waiting'\n" +
            "                   and id_user = user_login_id)\n" +
            "  and id not in (select id_friend\n" +
            "                 from friend_relation\n" +
            "                 where id_user = :idUser\n" +
            "                   and friend_relation.status_friend = 'waiting'\n" +
            "                   and id_user != user_login_id)\n" +
            "  and id not in\n" +
            "      (select id_friend from friend_relation where id_user = :idUser and friend_relation.status_friend = 'Friend')\n" +
            "  and id not in (select id from user_table where status = 'Banned')\n" +
            "  and status = 'Active'\n" +
            "  and (email LIKE concat('%', :searchText, '%') or full_name LIKE concat('%', :searchText, '%'));", nativeQuery = true)
    List<User> searchAll(@Param("searchText") String searchText, @Param("idUser") Long idUser);
}

package com.eva.demo.repository;

import com.eva.demo.domain.ShareTrade;
import com.eva.demo.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareTradeRepository extends JpaRepository<ShareTrade, Long> {

    @Query("select st from ShareTrade  st " +
            "where st.shareName = :shareName " +
            "and st.users != :buyer " +
            "and st.deleted = false " +
            "order by st.oneLotPrice")
    List<ShareTrade> findAllByShareNameAndDeletedFalseOrderByOneLotPrice(@Param("shareName") String shareName, @Param("buyer") Users buyer);
}

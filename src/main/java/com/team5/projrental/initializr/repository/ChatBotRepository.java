package com.team5.projrental.initializr.repository;

import com.team5.projrental.entities.ChatBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatBotRepository extends JpaRepository<ChatBot, Long>,ChatBotQueryRepository{

    @Query("select cb from ChatBot cb where cb.depth = 1")
    List<ChatBot> findByDepthInit();
}
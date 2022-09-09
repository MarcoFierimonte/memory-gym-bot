package com.f90.telegram.bot.memorygymbot.repo;

import com.f90.telegram.bot.memorygymbot.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, Long> {

    User findByChatId(Long chatId);

}

package com.f90.telegram.bot.memorygymbot.service;

import com.f90.telegram.bot.memorygymbot.dto.IrregularVerbDTO;
import com.f90.telegram.bot.memorygymbot.mapper.IrregularVerbMapper;
import com.f90.telegram.bot.memorygymbot.repo.IrregularVerbRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IrrebularVerbService {

    private final IrregularVerbRepo irregularVerbRepo;

    public List<IrregularVerbDTO> findAll() {
        return IrregularVerbMapper.toIrregularVerbDTOs(irregularVerbRepo.findAll());
    }

    public List<IrregularVerbDTO> random(Integer number) {
        return IrregularVerbMapper.toIrregularVerbDTOs(irregularVerbRepo.random(number).getMappedResults());
    }

}

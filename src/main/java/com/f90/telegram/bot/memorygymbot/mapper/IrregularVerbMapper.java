package com.f90.telegram.bot.memorygymbot.mapper;

import com.f90.telegram.bot.memorygymbot.dto.IrregularVerbDTO;
import com.f90.telegram.bot.memorygymbot.model.IrregularVerb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IrregularVerbMapper {

    private IrregularVerbMapper() {
        // empty
    }

    // -- IrregularVerb -- //

    public static IrregularVerb toIrregularVerb(IrregularVerbDTO in) {
        if (in == null) {
            return null;
        }
        return IrregularVerb.builder()
                .infinitive(in.getInfinitive())
                .simplePast(in.getSimplePast())
                .pastParticiple(in.getPastParticiple())
                .build();
    }

    public static List<IrregularVerb> toIrregularVerbs(List<IrregularVerbDTO> in) {
        final List<IrregularVerb> out;
        if (in != null) {
            out = new ArrayList<>(in.size());
            in.forEach(irregularVerb -> out.add(toIrregularVerb(irregularVerb)));
        } else {
            out = Collections.emptyList();
        }
        return out;
    }

    // -- IrregularVerb DTO -- //

    public static IrregularVerbDTO toIrregularVerbDTO(IrregularVerb in) {
        if (in == null) {
            return null;
        }
        return IrregularVerbDTO.builder()
                .infinitive(in.getInfinitive())
                .simplePast(in.getSimplePast())
                .pastParticiple(in.getPastParticiple())
                .build();
    }

    public static List<IrregularVerbDTO> toIrregularVerbDTOs(List<IrregularVerb> in) {
        final List<IrregularVerbDTO> out;
        if (in != null) {
            out = new ArrayList<>(in.size());
            in.forEach(irregularVerb -> out.add(toIrregularVerbDTO(irregularVerb)));
        } else {
            out = Collections.emptyList();
        }
        return out;
    }
}

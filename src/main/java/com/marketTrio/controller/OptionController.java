package com.marketTrio.controller;

import com.marketTrio.domain.OptionEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    private List<OptionEntity> options = new ArrayList<>();

    @PostMapping
    public OptionEntity addOption(@RequestBody OptionEntity option) {
        options.add(option);
        return option;
    }

    @GetMapping
    public List<OptionEntity> getOptions() {
        return options;
    }
}

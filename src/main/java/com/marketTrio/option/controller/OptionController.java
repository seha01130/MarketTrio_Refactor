package com.marketTrio.option.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketTrio.option.domain.OptionEntity;

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

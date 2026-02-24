package com.marketTrio.option.controller;

import com.marketTrio.option.domain.OptionEntity;

public class OptionCommand {
	private OptionEntity option;

	public OptionEntity getOption() { return option; }
	public void setOption(OptionEntity option) { this.option = option; }
}

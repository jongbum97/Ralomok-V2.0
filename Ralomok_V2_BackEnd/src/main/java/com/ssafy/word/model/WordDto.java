package com.ssafy.word.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WordDto {
	
	private String title;
	private String link;
	private String description;
	private String thumbnail;
	
}

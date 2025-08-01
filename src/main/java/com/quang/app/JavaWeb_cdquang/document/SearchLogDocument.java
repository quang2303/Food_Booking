package com.quang.app.JavaWeb_cdquang.document;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchLogDocument {
	private String keyword;
	private String searchedAt;
}

package com.quang.app.JavaWeb_cdquang.document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDocument {
	private Integer id;
	private String name;
	private String description;
	private Long price;
	private String image;
	private Boolean status;
	private String type;
	private String createAt;
	private String updateAt;
}

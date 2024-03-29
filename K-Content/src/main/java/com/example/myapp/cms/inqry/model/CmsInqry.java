package com.example.myapp.cms.inqry.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmsInqry {
	private int inqryId;
	private int inqryRefId;
	private int inqryGroupOrd;
	private String inqryTitle;
	private String inqryCntnt;
	private String inqryPwd;
	private String inqryRegistDate;
	private String inqryUpdateDate;
	private String inqryDeleteYn;
	private String inqryMberId;
}

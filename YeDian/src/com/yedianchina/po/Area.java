package com.yedianchina.po;

public  class Area implements java.io.Serializable {
  
	private static final long serialVersionUID = 1L;
	private Integer areaId;
	private Integer parentId;
	private String areaName;
	private Integer sort;

	// Constructors

	/** default constructor */
	public Area() {
	}

	/** minimal constructor */
	public Area(String areaName) {
		this.areaName = areaName;
	}

	/** full constructor */
	public Area(Integer parentId, String areaName, Integer sort) {
		this.parentId = parentId;
		this.areaName = areaName;
		this.sort = sort;
	}

	// Property accessors

	public Integer getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
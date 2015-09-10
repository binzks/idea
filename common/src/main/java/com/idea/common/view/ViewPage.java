package com.idea.common.view;

public class ViewPage {
	private Integer currentPage = 1; // 当前页
	private Integer totalPages = 0; // 总页数
	private Integer totalRows = 0; // 总行数
	private Integer rowSize = 0; // 每页行数
	private Integer endPage = 1;
	private Integer startPage = 1;

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		if (null == currentPage) {
			this.currentPage = 1;
		} else {
			this.currentPage = currentPage;
		}
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
		this.totalPages = (int) Math.ceil((double) totalRows / (double) rowSize);
		endPage = totalPages - currentPage > 5 ? currentPage + 5 : totalPages;
		startPage = currentPage;
	}

	public Integer getRowSize() {
		return rowSize;
	}

	public void setRowSize(Integer rowSize) {
		this.rowSize = rowSize;
	}

	public int getStart() {
		return (this.currentPage - 1) * this.rowSize;
	}

	public Integer getEndPage() {
		return endPage;
	}

	public void setEndPage(Integer endPage) {
		this.endPage = endPage;
	}

	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

}

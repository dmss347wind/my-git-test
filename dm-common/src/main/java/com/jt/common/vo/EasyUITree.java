package com.jt.common.vo;

public class EasyUITree {
		private Long id; //节点id
		private String text;//节点名称
		private String state;//节点状态 closed或者open
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		
}

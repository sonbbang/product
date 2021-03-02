package com.amorepacific.product.service.lru;

import com.amorepacific.product.domain.Product;

public class Node {

	private Long id; // 해시테이블 키
	private Product data; // 해시테이블 데이터
	private Node prevNode; // 이전 노드
	private Node nextNode; // 다음 노드

	public Node() {
	}

	public Node(Long id, Product data) {
		this.id = id;
		this.data = data;
	}

	// getter, setter, toString
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getData() {
		return data;
	}

	public void setData(Product data) {
		this.data = data;
	}

	public Node getPrevNode() {
		return prevNode;
	}

	public void setPrevNode(Node prevNode) {
		this.prevNode = prevNode;
	}

	public Node getNextNode() {
		return nextNode;
	}

	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}
}

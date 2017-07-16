package com.yzc.message;

public interface MessageService<T> {
	
	void send(T message);

	T receive();

}

package com.nagarro.interview.security.jwt;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTokenValut {
	private static ConcurrentHashMap<String, String> tokenValut;

	public InMemoryTokenValut() {
		tokenValut = new ConcurrentHashMap<String, String>();
	}

	public void putToken(String key, String value) {
		tokenValut.put(key, value);
	}

	public void removeTokenByKey(String key) {
		tokenValut.remove(key);
	}

	public String getTokenByUserName(String key) {
		return tokenValut.get(key);
	}

}
package com.example.java.grpc;

public interface GrpcConnectionManager<Key, Connection> {

	public void send(Key key, String msg);

	public void put(Key key, Connection con);

	public void remove(Key key);

}

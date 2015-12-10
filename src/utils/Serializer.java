package utils;

public interface Serializer {
	 void push(Object o);
	  Object pop();
	  void write() throws Exception;
	  int read() throws Exception;
}

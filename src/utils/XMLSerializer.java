package utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

public class XMLSerializer implements Serializer {

	private Stack stack = new Stack();
	private File file;

	public XMLSerializer(File file) {
		this.file = file;
	}

	public void push(Object o) {
		stack.push(o);
	}

	public Object pop() {
		return stack.pop();
	}

	@SuppressWarnings("unchecked")
	public void read() throws Exception {
		ObjectInputStream is = null; // is: in-stream

		try {
			XStream xstream = new XStream(new DomDriver());
			is = xstream.createObjectInputStream(new FileReader(file));
			// while (obj != null)
			// {
			// stack.push(obj);
			// obj = is.readObject();
			// }

			// Read the whole stack instead of reading each of object from the
			// stored stack to push to the current running stack
			stack = (Stack) is.readObject();
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	public void write() throws Exception {
		ObjectOutputStream os = null; // out-stream

		try {
			XStream xstream = new XStream(new DomDriver());
			os = xstream.createObjectOutputStream(new FileWriter(file));
			// Object obj = is.readObject();
			// while (obj != null)
			// {
			// stack.push(obj);
			// obj = is.readObject();
			// }

			//Write the whole stack object instead of items in the stack
			os.writeObject(stack);
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}
}
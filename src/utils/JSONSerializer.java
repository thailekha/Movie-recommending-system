package utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

import com.thoughtworks.xstream.MarshallingStrategy;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.reflection.SerializableConverter;

public class JSONSerializer implements Serializer{

	private Stack buffer = new Stack();
	private File file;
	
	public JSONSerializer(File file) {
		this.file = file;
	}
	
	@Override
	public void push(Object o) {
		// TODO Auto-generated method stub
		buffer.push(o);
	}

	@Override
	public Object pop() {
		// TODO Auto-generated method stub
		if (buffer.isEmpty())
			return null;
		else
			return buffer.pop();
	}

	@Override
	public void write() throws Exception {
		// TODO Auto-generated method stub
		ObjectOutputStream os = null;
		
		try {
			XStream xstream = new XStream(new JettisonMappedXmlDriver());
//			xstream.setMode(XStream.NO_REFERENCES);
//	        xstream.alias("stack",Stack.class);
			os = xstream.createObjectOutputStream(new FileWriter(file));
			//os.writeObject(xstream.toXML(buffer));
			os.writeObject(buffer);
		}	
		finally {
			if(os != null)
				os.close();
		}
	}

	@Override
	public void read() throws Exception {
		// TODO Auto-generated method stub
		
		ObjectInputStream is = null;
		
		try {
			XStream xstream = new XStream(new JettisonMappedXmlDriver());
			System.out.println(new SerializableConverter(xstream.getMapper(), xstream.getReflectionProvider()).canConvert(java.util.Stack.class));
			is = xstream.createObjectInputStream(new FileReader(file));
			//String data = (String) is.readObject();
			//buffer = (Stack) xstream.fromXML(data);
			buffer = (Stack) is.readObject();
		} 
//		catch (ConversionException e) {
//			System.out.println(e.getShortMessage());
//		}
		finally {
			if(is != null)
				is.close();
		}
	}
	
}

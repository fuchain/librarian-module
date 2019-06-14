package com.fpt.edu.configs;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Date;

public class CustomLocalDateTimeSerializer extends StdSerializer<Date> {
	public CustomLocalDateTimeSerializer(Class<Date> t) {
		super(t);
	}

	public CustomLocalDateTimeSerializer() {
		this(null);
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
				gen.writeString(String.valueOf(value.getTime()/1000));
	}
}

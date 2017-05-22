package com.pkh.pkec.phone.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateConverter implements Converter<String, Date> {

	static private Logger logger = LoggerFactory.getLogger(CustomDateConverter.class);
	
	public Date convert(String source) {
		if ((source == null)||(source.length()<1)){ 
			return null;
		}
		
		SimpleDateFormat format;
		//根据是否有冒号，确定是否有时分秒,确定相应的时间格式
		if (source.indexOf(':')<0){
			format = new SimpleDateFormat("yyyy-MM-dd");
		}else{
   		    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		try {
			return format.parse(source);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return null;
	}
}

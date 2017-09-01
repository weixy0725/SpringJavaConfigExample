package com.example.rabbitmq.util;

import java.io.UnsupportedEncodingException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import net.sf.json.JSONObject;

/**
 * @description 对发送的信息进行json格式的封装
 * @author xinyuan.wei
 * @time 2017年8月1日 下午3:13:30
 */
public class FastJsonMessageConverter extends AbstractMessageConverter {

	public static final String DEFAULT_CHARSET = "UTF-8";

	@SuppressWarnings("static-access")
	@Override
	protected Message createMessage(Object message, MessageProperties messageProperties) {
		byte[] bytes = null;

		JSONObject json = new JSONObject();
		json.put("data", message);

		try {
			bytes = json.toString().getBytes(this.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new MessageConversionException("Failed to convert Message content:" + e);
		}

		if (bytes != null) {
			messageProperties.setContentLength(bytes.length);
		}
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		messageProperties.setContentEncoding(this.DEFAULT_CHARSET);

		return new Message(bytes, messageProperties);
	}

	@Override
	public Object fromMessage(Message message) throws MessageConversionException {
		return null;
	}

}

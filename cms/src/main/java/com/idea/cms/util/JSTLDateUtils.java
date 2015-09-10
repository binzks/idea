package com.idea.cms.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class JSTLDateUtils extends TagSupport {

	private String value;

	private String pattern;

	public void setValue(String value) {
		this.value = value;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int doStartTag() throws JspException {
		String vv = String.valueOf(value);
		long time = Long.valueOf(vv);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		SimpleDateFormat dateformat = new SimpleDateFormat(pattern);
		String s = dateformat.format(c.getTime());
		try {
			pageContext.getOut().write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}

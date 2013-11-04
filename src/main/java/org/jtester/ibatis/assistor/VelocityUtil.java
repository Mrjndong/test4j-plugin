/*
 * Copyright 2012 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package org.jtester.ibatis.assistor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.eclipse.core.runtime.Path;
import org.jtester.database.ui.tree.BaseTreeNode;
import org.jtester.plugin.CommonConstants;
import org.jtester.plugin.helper.Resources;

/**
 * 类VelocityUtil.java的实现描述：TODO 类实现描述
 * 
 * @author spark 2012-1-16 上午11:22:30
 */
public class VelocityUtil {
	public static InputStream getMergeOutput(String templateContext, BaseTreeNode model, String outputEncoding)
			throws IOException {
		// 展开children
		model.refresh();
		// 创建模板引擎，设置模板加载方式，并初始化模板引擎
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(VelocityEngine.RESOURCE_LOADER, "string");
		ve.setProperty("string.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.StringResourceLoader");
		ve.setProperty(VelocityEngine.INPUT_ENCODING, CommonConstants.UTF8_ENCODING);
		ve.setProperty(VelocityEngine.OUTPUT_ENCODING, outputEncoding);
		ve.setProperty(VelocityEngine.ENCODING_DEFAULT, outputEncoding);
		ve.init();

		StringResourceRepository repo = StringResourceLoader.getRepository();
		repo.putStringResource(CommonConstants.TEMPLATE_DEFAULT_NAME, templateContext);
		Template template = ve.getTemplate(CommonConstants.TEMPLATE_DEFAULT_NAME);
		VelocityContext context = new VelocityContext();
		context.put("model", model);

		context.put("user", System.getProperty("user.name"));
		context.put("date", (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()));
		context.put("time", (new SimpleDateFormat("HH:mm:ss")).format(new Date()));
		context.put("encoding", outputEncoding);

		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		writer.close();

		ByteArrayInputStream bais = new ByteArrayInputStream(writer.toString().getBytes(outputEncoding));
		return bais;
	}

	public static String readTemplate(String templatePath) throws IOException {
		String templateContext = Resources.getContentsRelativeToPlugin(new Path(templatePath));

		return templateContext;
	}
}

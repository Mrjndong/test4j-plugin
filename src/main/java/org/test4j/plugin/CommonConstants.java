/*
 * Copyright 2012 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package org.test4j.plugin;

import java.io.File;

/**
 * 类CommonConstants.java的实现描述：TODO 类实现描述 
 * @author spark 2012-1-16 上午11:23:31
 */
public class CommonConstants {
    public static final String XML_EXTENSION = ".xml";
    public static final String JAVA_EXTENSION = ".java";
    public static final String ENTITY_EXTENSION = "Entity";
    public static String UTF8_ENCODING = "UTF-8";
    public static String GBK_ENCODING = "GBK";
    public static String TEMPLATE_PATH = "template";
    public static String TEMPLATE_DEFAULT_NAME = "TEMPLATE";
    public static String DOMAIN_SQLMAP_TEMPLATE = TEMPLATE_PATH + File.separatorChar + "DomainSqlmap.xml.vm";
    public static String DOMAIN_ENTITY_TEMPLATE = TEMPLATE_PATH + File.separatorChar + "DomainEntity.java.vm";
}

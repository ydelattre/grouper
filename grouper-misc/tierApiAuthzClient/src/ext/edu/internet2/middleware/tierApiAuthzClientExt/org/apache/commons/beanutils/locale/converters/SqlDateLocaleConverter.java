/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.converters;

import java.sql.Date;
import java.text.ParseException;
import java.util.Locale;

/**
 * <p>Standard {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
 * implementation that converts an incoming
 * locale-sensitive String into a <code>java.sql.Date</code> object,
 * optionally using a default value or throwing a 
 * {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.ConversionException}
 * if a conversion error occurs.</p>
 *
 * @author Yauheny Mikulski
 */

public class SqlDateLocaleConverter extends DateLocaleConverter {


    // ----------------------------------------------------------- Constructors

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs. The locale is the default locale for
     * this instance of the Java Virtual Machine and an unlocalized pattern is used
     * for the convertion.
     *
     */
    public SqlDateLocaleConverter() {

        this(false);
    }

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs. The locale is the default locale for
     * this instance of the Java Virtual Machine.
     *
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public SqlDateLocaleConverter(boolean locPattern) {

        this(Locale.getDefault(), locPattern);
    }

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs. An unlocalized pattern is used for the convertion.
     *
     * @param locale        The locale
     */
    public SqlDateLocaleConverter(Locale locale) {

        this(locale, false);
    }

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs.
     *
     * @param locale        The locale
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public SqlDateLocaleConverter(Locale locale, boolean locPattern) {

        this(locale, (String) null, locPattern);
    }

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs. An unlocalized pattern is used for the convertion.
     *
     * @param locale        The locale
     * @param pattern       The convertion pattern
     */
    public SqlDateLocaleConverter(Locale locale, String pattern) {

        this(locale, pattern, false);
    }

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs.
     *
     * @param locale        The locale
     * @param pattern       The convertion pattern
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public SqlDateLocaleConverter(Locale locale, String pattern, boolean locPattern) {

        super(locale, pattern, locPattern);
    }

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs. The locale is the default locale for
     * this instance of the Java Virtual Machine and an unlocalized pattern is used
     * for the convertion.
     *
     * @param defaultValue  The default value to be returned
     */
    public SqlDateLocaleConverter(Object defaultValue) {

        this(defaultValue, false);
    }

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs. The locale is the default locale for
     * this instance of the Java Virtual Machine.
     *
     * @param defaultValue  The default value to be returned
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public SqlDateLocaleConverter(Object defaultValue, boolean locPattern) {

        this(defaultValue, Locale.getDefault(), locPattern);
    }

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs. An unlocalized pattern is used for the convertion.
     *
     * @param defaultValue  The default value to be returned
     * @param locale        The locale
     */
    public SqlDateLocaleConverter(Object defaultValue, Locale locale) {

        this(defaultValue, locale, false);
    }

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs.
     *
     * @param defaultValue  The default value to be returned
     * @param locale        The locale
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public SqlDateLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {

        this(defaultValue, locale, null, locPattern);
    }

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs. An unlocalized pattern is used for the convertion.
     *
     * @param defaultValue  The default value to be returned
     * @param locale        The locale
     * @param pattern       The convertion pattern
     */
    public SqlDateLocaleConverter(Object defaultValue, Locale locale, String pattern) {

        this(defaultValue, locale, pattern, false);
    }

    /**
     * Create a {@link edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs.
     *
     * @param defaultValue  The default value to be returned
     * @param locale        The locale
     * @param pattern       The convertion pattern
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public SqlDateLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {

        super(defaultValue, locale, pattern, locPattern);
    }


    // --------------------------------------------------------- Methods

    /**
     * Convert the specified locale-sensitive input object into an output object of the
     * specified type.
     *
     * @param value The input object to be converted
     * @param pattern The pattern is used for the convertion
     * @return The converted value
     *
     * @exception edu.internet2.middleware.tierApiAuthzClientExt.org.apache.commons.beanutils.ConversionException if conversion
     * cannot be performed successfully
     * @throws ParseException if an error occurs parsing a String to a Number
     */
    protected Object parse(Object value, String pattern) throws ParseException {

        return new Date(((java.util.Date) super.parse(value, pattern)).getTime());
    }
}

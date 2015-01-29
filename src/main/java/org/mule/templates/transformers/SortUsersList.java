/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import com.google.common.collect.Lists;

/**
 * This transformer will take to list as input and create a third one that will be the merge of the previous two. The identity of an element of the list is
 * defined by its email.
 * 
 * @author cesar.garcia
 */
public class SortUsersList extends AbstractMessageTransformer {

	public static Comparator<Map<String, String>> recordComparator = new Comparator<Map<String, String>>() {

		public int compare(Map<String, String> user1, Map<String, String> user2) {

			String key1 = buildKey(user1);
			String key2 = buildKey(user2);

			return key1.compareTo(key2);

		}

		private String buildKey(Map<String, String> user) {
			StringBuilder key = new StringBuilder();

			if (StringUtils.isNotBlank(user.get("IDInA")) && StringUtils.isNotBlank(user.get("IDInB"))) {
				key.append("~~");
				key.append(user.get("IDInA"));
				key.append(user.get("IDInB"));
				key.append(user.get("Email"));
			}

			if (StringUtils.isNotBlank(user.get("IDInA")) && StringUtils.isBlank(user.get("IDInB"))) {
				key.append(user.get("IDInA"));
				key.append("~");
				key.append(user.get("Email"));
			}

			if (StringUtils.isBlank(user.get("IDInA")) && StringUtils.isNotBlank(user.get("IDInB"))) {
				key.append("~");
				key.append(user.get("IDInB"));
				key.append(user.get("Email"));
			}

			return key.toString();
		}

	};

	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		List<Map<String, String>> sortedUsersList = Lists.newArrayList((Iterator<Map<String, String>>) message.getPayload());
		Collections.sort(sortedUsersList, recordComparator);

		return sortedUsersList;

	}

}

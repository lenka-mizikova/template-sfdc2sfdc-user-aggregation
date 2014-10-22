/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.routing.AggregationContext;
import org.mule.templates.integration.AbstractTemplateTestCase;

import com.google.common.collect.Lists;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class SFDCUsersMergeAggregationStrategyTest extends AbstractTemplateTestCase {
	
	@Mock
	private MuleContext muleContext;
  
	
	@Test
	public void testAggregate() throws Exception {
		List<Map<String, String>> usersA = SFDCUsersMergeTest.createUserLists("A", 0, 1);
		List<Map<String, String>> usersB = SFDCUsersMergeTest.createUserLists("B", 1, 2);
		
		MuleEvent testEventA = getTestEvent("");
		MuleEvent testEventB = getTestEvent("");
		
		testEventA.getMessage().setPayload(usersA.iterator());
		testEventB.getMessage().setPayload(usersB.iterator());
		
		List<MuleEvent> testEvents = new ArrayList<MuleEvent>();
		testEvents.add(testEventA);
		testEvents.add(testEventB);
		
		AggregationContext aggregationContext = new AggregationContext(getTestEvent(""), testEvents);
		
		SFDCUserMergeAggregationStrategy sfdcuserMerge = new SFDCUserMergeAggregationStrategy();
		Iterator<Map<String, String>> iterator = (Iterator<Map<String, String>>) sfdcuserMerge.aggregate(aggregationContext).getMessage().getPayload();
		List<Map<String, String>> mergedList = Lists.newArrayList(iterator);

		Assert.assertEquals("The merged list obtained is not as expected", SFDCUsersMergeTest.createExpectedList(), mergedList);

	}
	
}

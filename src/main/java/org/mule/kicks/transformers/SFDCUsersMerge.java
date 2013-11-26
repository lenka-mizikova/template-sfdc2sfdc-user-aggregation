package org.mule.kicks.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * This transformer will take to list as input and create a third one that will
 * be the merge of the previous two. The identity of an element of the list is
 * defined by its email.
 * 
 * @author
 */
public class SFDCUsersMerge extends AbstractMessageTransformer {

	private static final String QUERY_COMPANY_A = "usersFromOrgA";
	private static final String QUERY_COMPANY_B = "usersFromOrgB";

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		List<Map<String, String>> usersFromOrgAList = message.getInvocationProperty(QUERY_COMPANY_A);
		List<Map<String, String>> usersFromOrgBList = message.getInvocationProperty(QUERY_COMPANY_B);

		List<Map<String, String>> mergedUsersList = new ArrayList<Map<String, String>>();

		for (Map<String, String> userFromA : usersFromOrgAList) {
			Map<String, String> newMergedUser = new HashMap<String, String>();
			newMergedUser.put("Email", userFromA.get("Email"));
			newMergedUser.put("Name", userFromA.get("Name"));
			newMergedUser.put("IDInA", userFromA.get("Id"));
			newMergedUser.put("UserNameInA", userFromA.get("Username"));

			Map<String, String> userFromB = getUserFromList(userFromA.get("Email"), usersFromOrgBList);
			if (userFromB != null) {
				newMergedUser.put("IDInB", userFromB.get("Id"));
				newMergedUser.put("UserNameInB", userFromB.get("Username"));
			} else {
				newMergedUser.put("IDInB", "");
				newMergedUser.put("UserNameInB", "");
			}

			mergedUsersList.add(newMergedUser);
		}

		for (Map<String, String> userFromB : usersFromOrgBList) {
			Map<String, String> userFromA = getUserFromList(userFromB.get("Email"), mergedUsersList);
			if (userFromA == null) {
				Map<String, String> newMergedUser = new HashMap<String, String>();

				newMergedUser.put("Email", userFromB.get("Email"));
				newMergedUser.put("Name", userFromB.get("Name"));
				newMergedUser.put("IDInA", "");
				newMergedUser.put("UserNameInA", "");

				newMergedUser.put("IDInB", userFromB.get("Id"));
				newMergedUser.put("UserNameInB", userFromB.get("Username"));

				mergedUsersList.add(newMergedUser);

			}
		}

		return mergedUsersList;
	}

	private Map<String, String> getUserFromList(String userMail, List<Map<String, String>> orgList) {
		for (Map<String, String> user : orgList) {
			if (user.get("Email").equals(userMail)) {
				return user;
			}
		}
		return null;
	}

}
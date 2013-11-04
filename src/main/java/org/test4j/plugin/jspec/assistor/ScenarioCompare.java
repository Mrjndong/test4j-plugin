package org.test4j.plugin.jspec.assistor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScenarioCompare {

	public static String getCompareHtml(String story) {
		return story;
	}
}

class MethodParameter {
	static final String SCENARIO_NAME = "$_SCENARIO_NAME_$";
	private String methodName;
	private Set<String> paraNames;
	private List<Map<String, String>> paraValues;

	MethodParameter(String name) {
		this.methodName = name;
		this.paraNames = new HashSet<String>();
		this.paraNames.add(SCENARIO_NAME);
		this.paraValues = new ArrayList<Map<String, String>>();
	}

	void addParameter(String paraName) {
		this.paraNames.add(paraName);
	}

	void addScenarioMethod(String scenario, Map<String, String> map) {
		map.put(SCENARIO_NAME, scenario);
		this.paraValues.add(map);
	}

	@Override
	public String toString() {
		return "MethodParameter[" + methodName + "]";
	}
}
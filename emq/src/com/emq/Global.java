package com.emq;

import java.util.HashMap;
import java.util.Map;

public class Global {

	// 下拉框全部
	public static Map IS_ALL = new HashMap();

	// 下拉框全部请选择
	public static Map HAS_SELECT = new HashMap();

	public static String TREE_ROOT = "root";

	// 供电局+县区市+变电站 树类型标志
	public static String TREE_TYPE_1 = "1";

	// 供电局+维护班组+人员 树类型标志
	public static String TREE_TYPE_2 = "2";

	// 用电户+变电站 树类型标志
	public static String TREE_TYPE_3 = "3";

	// 线路+变电站 树类型标志
	public static String TREE_TYPE_4 = "4";

	// 供电局+县区市+变电站 供电局树形层次标志
	public static String TREE_LEVEL_1_1 = "1_1";

	// 供电局+县区市+变电站 县区市树形层次标志
	public static String TREE_LEVEL_1_2 = "1_2";

	// 供电局+县区市+变电站 变电站树形层次标志(增加层次扩展,暂未用到)
	public static String TREE_LEVEL_1_3 = "1_3";

	// 供电局+维护班组+人员 供电局树形层次标志
	public static String TREE_LEVEL_2_1 = "2_1";

	// 供电局+维护班组+人员 维护班组树形层次标志
	public static String TREE_LEVEL_2_2 = "2_2";

	// 供电局+维护班组+人员 人员树形层次标志(增加层次扩展,暂未用到)
	public static String TREE_LEVEL_2_3 = "2_3";

	static {

		// 是否有全部属性设置

		IS_ALL.put("code", "all");
		IS_ALL.put("text", "-全部-");

		// 是否包含请选择属性设置

		HAS_SELECT.put("code", "");
		HAS_SELECT.put("text", "-请选择-");
	}

}
package com.emq.util;

import com.emq.model.CommonCheckbox;

public class Global {

	public static CommonCheckbox IS_ALL = new CommonCheckbox();

	public static CommonCheckbox HAS_SELECT = new CommonCheckbox();

	static {

		// 是否有全部属性设置

		IS_ALL.setCode("");
		IS_ALL.setText("--全部--");

		// 是否包含请选择属性设置

		HAS_SELECT.setCode(null);
		HAS_SELECT.setText("--请选择--");
	}

}

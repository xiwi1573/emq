package com.emq.model;

import com.emq.exception.GISException;
import com.emq.logger.Logger;

/**
 * 地图控制命令抽象类
 * <p>
 * 所有对地图的操作抽象为命令，实现不同的子类以支持不同的地图操作。
 * <p>
 * AbstractGISMapControlCommand提供了访问地图容器的getGISMap方法，返回AbstractGISMap对象，
 * 子类通过调用AbstractGISMap的方法来改变地图的状态、属性， 完成所定义的操作。
 * <p>
 * 提供execute方法给外部调用,子类应实现抽象方法doExecute来完成所需的操作。
 * 
 * @author guqiong
 * @created 2009-9-21
 */
public abstract class AbstractGISMapControlCommand {
	// 操作的地图对象
	protected AbstractGISMap map;

	private static Logger log = Logger
			.getLogger(AbstractGISMapControlCommand.class);

	protected AbstractGISMapControlCommand(AbstractGISMap map) {
		this.map = map;
	}

	/**
	 * 执行对地图操作，返回操作成功后的地图模型
	 * 
	 * @return AbstractGISMap
	 */
	public AbstractGISMap execute() throws GISException {
		this.doExecute();
		map.refresh();
		return map;
	}

	protected AbstractGISMap getGISMap() {
		return map;
	}

	/**
	 * 子类执行的操作
	 */
	protected abstract void doExecute() throws GISException;

}

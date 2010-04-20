package com.emq.ui.dwr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.emq.model.ConditionObject;

/**
 * 卷烟零售点dwr查询接口实现
 * 
 * @author guqiong
 * @created 2009-9-25
 * @history 2009-10-14 guqiong 增加半径范围查询方法的实现 2009-10-16 guqiong 实现接口新增的方法
 *          <p>
 *          findXmlByCondition
 *          <p>
 *          findXmlByPoint
 *          <p>
 *          findXmlByRadius
 *          <p>
 *          findXmlByRect
 *          <p>
 *          findXmlByRegion 2009-10-20 抽取方法至AbstractGISMapDataFinder
 */
public class CigStoreFinderImpl extends AbstractGISMapDataFinder implements
		CigStoreFinder {
	private static Logger log = Logger.getLogger(CigStoreFinderImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.ui.dwr.CigStoreFinder#findByCondition(java.lang.String,
	 * com.icss.km.gis.model.ConditionObject, javax.servlet.http.HttpSession)
	 */
	public List<Map<String, String>> findByCondition(String mapDefRefId,
			ConditionObject co, HttpSession session) throws GISException {
		return super.findByCondition(mapDefRefId, co, session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.ui.dwr.CigStoreFinder#highlightByCondition(java.lang.
	 * String, com.icss.km.gis.model.ConditionObject,
	 * javax.servlet.http.HttpSession)
	 */
	public void highlightByCondition(String mapDefRefId, ConditionObject co,
			HttpSession session) throws GISException {
		super.highlightByCondition(mapDefRefId, co, session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.ui.dwr.AbstractGISMapDataFinder#getViewColumnMetaData()
	 */
	protected Map<String, String> getViewColumnMetaData() throws Exception {
		Map<String, String> fieldDesc = new HashMap<String, String>();
		fieldDesc.put("ID", "xkzhm");
		fieldDesc.put("许可证编号", "xkzhm");
		fieldDesc.put("商店名称", "khmc");
		fieldDesc.put("经营地址", "jydz");
		fieldDesc.put("证件状态", "zjzt");
		fieldDesc.put("最近订货数量", "zjdhl");
		fieldDesc.put("本月订货数量", "bydhl");
		fieldDesc.put("三月订货数量", "sydhl");
		fieldDesc.put("X1", "X1");
		fieldDesc.put("Y1", "Y1");
		return fieldDesc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.ui.dwr.CigStoreFinder#findXmlByCondition(java.lang.String
	 * , java.lang.String, com.icss.km.gis.model.ConditionObject,
	 * javax.servlet.http.HttpSession)
	 */
	public String findXmlByCondition(String mapDefRefId, String returnColumns,
			ConditionObject co, HttpSession session) throws GISException {
		return super
				.findXmlByCondition(mapDefRefId, returnColumns, co, session);
	}

}

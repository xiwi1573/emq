package com.emq.ui.dwr;

import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.mapinfo.dp.Attribute;
import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.dp.TableInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List; 

/**
 * 将FeatureSet形式的数据转换为其他格式
 * <p>
 * FeatureSet：mapxtreme的图元记录集
 * 
 * @author guqiong
 * @created 2009-9-25
 * 
 */
public class FeatureSetTools {

	private static Logger log = Logger.getLogger(FeatureSetTools.class);

	/**
	 * 包装FeatureSet为List<Map<String, String>>
	 * List里放置每条记录为一个map，map的key为属性中文名，value为值
	 * 
	 * @param fs
	 *            图元集合
	 * @fieldDesc 字段中文描述 key为key为中文名称,value为字段名称
	 * @return List<Map<String, String>> 字段描述
	 */
	public static List<Map<String, String>> convert(List fsList,
			Map<String, String> fieldDesc) throws GISException {
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		try {
			for(int i=0;i<fsList.size();i++){
				FeatureSet fs = (FeatureSet)fsList.get(i); 
				TableInfo tableInfo = fs.getTableInfo();
				while (1 == 1) {
					Feature f = fs.getNextFeature();
					Map<String, String> row = new HashMap<String, String>();
					if (f == null)
						break;
					for (String desc : fieldDesc.keySet()) {
						String column = fieldDesc.get(desc);
						String value = getFieldValue(f, tableInfo, column);
						if(desc.equals("ID")&&value.equals("无证户")){
							row.put(desc, getFieldValue(f, tableInfo, "ID"));
						}else{
							row.put(desc, getFieldValue(f, tableInfo, column));
						}
					}
					rows.add(row);
				}
			}
			return rows;
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 获取图元的字段值
	 * 
	 * @param f
	 *            图元
	 * @param tableInfo
	 *            表定义信息
	 * @param columnName
	 *            字段名，支持多字段用逗号分隔, 格式col0,col1,仅用于多值主键的目的
	 * @return 字段值 支持多字段用逗号分隔,格式value0, value1
	 * @throws Exception
	 */
	protected static String getFieldValue(Feature f, TableInfo tableInfo,
			String columnName) throws Exception {
		int colIndex = tableInfo.getColumnIndex(columnName);
		if(colIndex==-1){
			return "";
		}
		Attribute attr = f.getAttribute(colIndex);
		// TODO:格式处理,处理日期类型等
		String value = attr.isNull() ? "" : attr.getString();
		return value;
	}

	/**
	 * 包装FeatureSet为xml 格式, 如“<?xml version="1.0" encoding="UTF-8"?><rows><row
	 * id="a"><cell>AA</cell><cell>BB</cell> <cell>CC</cell></row></rows>”,
	 * 顺序按colIndexes中的顺序,FeatureSet必须有一个单主键。
	 * 
	 * @param fs
	 *            图元集合
	 * @param colIndexes
	 *            需要包装的字段索引列表，从0开始
	 * @return xml数据，只含字段索引列表中的字段
	 * @throws GISException
	 */
//	public static String convert2Xml(List fsList, List<Integer> colIndexes)
//			throws GISException {
//		XMLConvertor xmlConvertor = new DhtmlGridDataConvertor();
//		return xmlConvertor.convert(fsList, colIndexes);
//	}
}

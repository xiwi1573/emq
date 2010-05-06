package com.emq.ui.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.log4j.Logger;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.emq.exception.ErrorMsgConstants;
import com.emq.service.BaseService;
import com.emq.util.DateUtil;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

/**
 * 
 * 文件导入Servlet
 */
public class FileImportServlet extends HttpServlet {

	private static final long serialVersionUID = 8968112076191958384L;
	/**
	 * 用于存放上传文件的临时目录
	 */
	public static final String UPLOAD_DIR = "\\upload\\";	
	/**
	 * 参数名：type 文件类型
	 */
	public static final String TYPE = "type"; 
	/**
	 * 支持最大文件大小
	 */
	public static final int MAX_FILE_SIZE = 500 * 2048;	
	
	String importSuccessLog = ""; //用于记录导入成功的Log
	String importFailLog = "";//用于记录导入失败的LOG
	
	private static Logger logger = Logger.getLogger(FileImportServlet.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	} 
	/*
	 * (non-Javadoc)
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		//设置输出属性
		res.setContentType("text/html; charset=utf-8");
		res.setCharacterEncoding("utf-8");
		PrintWriter out = res.getWriter(); 
		String type = req.getParameter("type"); 
		try { 
			List<String> files = smartUploadFile(req, res);
			for(String fileName: files){
				File f = new File(fileName);
				if (!f.exists())
					throw new Exception(ErrorMsgConstants.EMQ_UI_01);
				//解析
				List sqlList = parseFile(fileName, type);
				//保存	 
				BaseService baseService = getBaseService();	
				boolean flag = baseService.exeUpdateSqlByBach(sqlList);;
				f.delete();
				if(true == flag){//保存成功
					out.println("{success:true}");
					out.flush(); 
				}else{
					out.println("{success:false");
					out.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.println("{success:false,message:'" + e.getMessage() + "'}");
			out.flush();
			logger.error(e);
		}
	}
	
	
	/**
	 * 获取baseService
	 * @return
	 */
	private BaseService getBaseService() {
		ApplicationContext factory=new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		BaseService baseService = (BaseService)factory.getBean("baseService");
		return baseService; 
	}
	

	/**
	 * 表格的列定义是否正确
	 * @param sheet
	 * @return
	 * 	
	 * true:正确
	 */
	private boolean isValidColumns(Sheet sheet,String type){ 
		//检查保证至少有一行
		int rows = sheet.getRows(); 
		if(rows<1) {
			importFailLog += "该文件中没有数据\n";
			return false;
		}
		return true;
	}
	
	/**
	 * 获取单元格内的值
	 * @param cell
	 * @return
	 */
	private Object getCellValue(Cell cell){
		Object value = null;
		//日期型
		if (cell.getType().equals(CellType.DATE)) {  
			value = DateUtil.addZeroTime(((DateCell) cell).getDate());
		} 
		//数值
		if (cell.getType().equals(CellType.NUMBER)) {
			value = ((NumberCell) cell).getValue();
		}
		 
		//字符串
		if(cell.getType().equals(CellType.LABEL)){
			//去掉多余的空格
			value = cell.getContents().trim();
			//去掉换行
			value = value.toString().replace("\n",";");
			value = value == "" ? null : value;
			//统一转码GBK
			if(value!=null){
				try{
					byte[] bs = value.toString().getBytes();
					value =  new String(bs, "GBK");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			//替换特殊字符×
			value = value.toString().replace("?","×");
		}
		//空
		if(cell.getType().equals(CellType.EMPTY)){
			value = "";
		}
		return value;
	}
	
	/**
	 * 解析Excel2007
	 * @param fileName,type:el-电能关口台帐模板,mi-互感器关口台帐模板
	 * @return
	 */
	private List<String> parseFileByXlsx(String fileName,String type) throws Exception{
		List sqlList = new ArrayList();
		XSSFWorkbook xwb = null;
		try{
			xwb = new XSSFWorkbook(fileName);
			XSSFSheet sheet = xwb.getSheetAt(0);
			//检查文件
			int rows = sheet.getPhysicalNumberOfRows(); 
			if(rows<1) {
				importFailLog += "该文件中没有数据\n";
				throw new Exception(ErrorMsgConstants.EMQ_UI_02);
			}
			XSSFRow row;   
			String cell;
			if(type.equals("el")){
//				循环解析每行数据(从第三行开始)
				for (int i = sheet.getFirstRowNum()+2; i < sheet.getPhysicalNumberOfRows(); i++) {
					StringBuffer sql = new StringBuffer("insert into EMQ_PASS_BOOK(STATION_NAME,BELONG_UNIT_NAME," +
							"MEASURE_POINT_NAME,SUB_POINT_NAME,SUB_POINT_ADDRESS,MEASURE_POINT_TYPE,MEASURE_POINT_KIND," +
							"PASS_TYPE,E_CONFIGURE,E_MODEL,E_TYPE,E_MANUFACTURER,E_FACTORY_NUMBER,E_CONNECTION,E_TENSION," +
							"E_ELECTRICITY,E_RANK,E_MESSAGE_INTERFACE,E_MESSAGE_TYPE,E_COL_MODEL,E_COL_FACTORY,E_TV_RATE," +
							"E_TA_RATE,E_MULTIPLE,E_CHECK_TIME,E_CHECK_RESULT,T_MODEL,T_TYPE,T_FACTORY,T_DISCREPANCY," +
							"T_UNMBER,T_TENSION_FIRST,T_TENSION_SECOND,T_TENSION_SECOND_CHARGE,T_RANK,T_TV_KIND," +
							"T_IS_SPECIAL,T_IS_SPECIAL_TV,T_CHECK_TIME,T_CHECK_RESULT,EI_MODEL,EI_TYPE,EI_MANUFACTURER," +
							"EI_DISCREPANCY,EI_UNMBER,EI_RATED_TENSION_FIRST,EI_RATED_TENSION_SECOND," +
							"EI_RATED_TENSION_SECOND_CHARGE,EI_VERACITY_RANK,EI_TV_KIND,EI_IS_SPECIAL,EI_IS_SPECIAL_TV," +
							"EI_CHECK_TIME,EI_CHECK_RESULT,LT_MODEL,LT_TYPE,LT_MANUFACTURER,LT_UNMBER,GPS_MODEL,GPS_TYPE," +
							"GPS_MANUFACTURER,GPS_UNMBER,TV_AREA,TV_LENGTH,TA_LENGTH,TV_TIME,TV_RESULT,TV_CHARGE_TIME," +
							"TV_CHARGE_RESULT,TA_CHARGE_TIME,TA_CHARGE_RESULT) values(");
				    row = sheet.getRow(i);
				    StringBuffer sqlValue = new StringBuffer();
				    for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
				        // 获取单元格内容，   
				        cell = row.getCell(j).toString();
				        if(cell==null||cell.equals("null")){
				        	cell = "";
				        }
				        sqlValue.append("'"+cell+"',");
				    }
				    if(sqlValue.length()>220){
						sql.append(sqlValue.deleteCharAt(sqlValue.length()-1)+")");
						sqlList.add(sql.toString());
					}
				}
			}else{
				List rowspanList = this.getRowspanList(sheet);
				logger.info("复杂2007表格共有"+rowspanList.size()+"条数据！");
				XSSFRow firstRow = sheet.getRow(2);
//				循环解析每行数据(从第三行开始)
				for(int x=0;x<rowspanList.size();x++){
					int startRow = ((Integer)rowspanList.get(x)).intValue();
					int endRow = 0;
					if(x!=rowspanList.size()-1){
						endRow = ((Integer)rowspanList.get(x+1)).intValue()-1;
					}else{
						endRow = sheet.getPhysicalNumberOfRows()-1;
					}
					StringBuffer sql = new StringBuffer("insert into EMQ_PASS_BOOK(STATION_NAME,BELONG_UNIT_NAME," +
							"MEASURE_POINT_NAME,SUB_POINT_NAME,SUB_POINT_ADDRESS,MEASURE_POINT_TYPE,MEASURE_POINT_KIND," +
							"PASS_TYPE,E_CONFIGURE,E_MODEL,E_TYPE,E_MANUFACTURER,E_FACTORY_NUMBER,E_CONNECTION,E_TENSION," +
							"E_ELECTRICITY,E_RANK,E_MESSAGE_INTERFACE,E_MESSAGE_TYPE,E_COL_MODEL,E_COL_FACTORY,E_TV_RATE," +
							"E_TA_RATE,E_MULTIPLE,E_CHECK_TIME,E_CHECK_RESULT,T_MODEL,T_TYPE,T_FACTORY,T_DISCREPANCY," +
							"T_UNMBER,T_TENSION_FIRST,T_TENSION_SECOND,T_TENSION_SECOND_CHARGE,T_RANK,T_TV_KIND," +
							"T_IS_SPECIAL,T_IS_SPECIAL_TV,T_CHECK_TIME,T_CHECK_RESULT,EI_MODEL,EI_TYPE,EI_MANUFACTURER," +
							"EI_DISCREPANCY,EI_UNMBER,EI_RATED_TENSION_FIRST,EI_RATED_TENSION_SECOND," +
							"EI_RATED_TENSION_SECOND_CHARGE,EI_VERACITY_RANK,EI_TV_KIND,EI_IS_SPECIAL,EI_IS_SPECIAL_TV," +
							"EI_CHECK_TIME,EI_CHECK_RESULT,LT_MODEL,LT_TYPE,LT_MANUFACTURER,LT_UNMBER,GPS_MODEL,GPS_TYPE," +
							"GPS_MANUFACTURER,GPS_UNMBER,TV_AREA,TV_LENGTH,TA_LENGTH,TV_TIME,TV_RESULT,TV_CHARGE_TIME," +
							"TV_CHARGE_RESULT,TA_CHARGE_TIME,TA_CHARGE_RESULT) values(");
					StringBuffer sqlValue = new StringBuffer();
					for(int j=firstRow.getFirstCellNum();j<=firstRow.getPhysicalNumberOfCells();j++){
						sqlValue.append("'");
						for(int i=startRow;i<=endRow;i++){
							row = sheet.getRow(i);
							cell = row.getCell(j).toString();
							if(cell==null||cell.equals("null")){
					        	cell = "";
					        }
							if(!cell.equals("")){
								sqlValue.append(cell+";");
							}
						}
						if(sqlValue.lastIndexOf(";")==sqlValue.length()-1){
							sqlValue.deleteCharAt(sqlValue.length()-1);
						}
						sqlValue.append("',");
					}
					if(sqlValue.length()>220){
						sql.append(sqlValue.deleteCharAt(sqlValue.length()-1)+")");
						sqlList.add(sql.toString());
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error(ErrorMsgConstants.EMQ_UI_01, e);
			throw new Exception(ErrorMsgConstants.EMQ_UI_01);
		}finally{
			File f = new File(fileName);
			if(f.exists()){
				f.delete();
			}
		}
		return sqlList;
	}
	
	/**
	 * 解析Excel2003
	 * @param fileName,type:el-电能关口台帐模板,mi-互感器关口台帐模板
	 * @return
	 */
	private List<String> parseFileByXls(String fileName,String type) throws Exception{
		List sqlList = new ArrayList();
		jxl.Workbook wb = null; 
		List<String> list = new ArrayList<String>(); 
		InputStream is = null;
		try {
			is = new FileInputStream(fileName);
			// 得到工作薄中的第一个工作表
			wb = Workbook.getWorkbook(is);
			jxl.Sheet sheet = wb.getSheet(0);
//			检查文件
			int rows = sheet.getRows(); 
			if(rows<1) {
				importFailLog += "该文件中没有数据\n";
				throw new Exception(ErrorMsgConstants.EMQ_UI_02);
			}
			if(type.equals("el")){
//				循环解析每行数据(从第三行开始)
				for(int i=2;i<rows;i++){
					StringBuffer sql = new StringBuffer("insert into EMQ_PASS_BOOK(STATION_NAME,BELONG_UNIT_NAME," +
							"MEASURE_POINT_NAME,SUB_POINT_NAME,SUB_POINT_ADDRESS,MEASURE_POINT_TYPE,MEASURE_POINT_KIND," +
							"PASS_TYPE,E_CONFIGURE,E_MODEL,E_TYPE,E_MANUFACTURER,E_FACTORY_NUMBER,E_CONNECTION,E_TENSION," +
							"E_ELECTRICITY,E_RANK,E_MESSAGE_INTERFACE,E_MESSAGE_TYPE,E_COL_MODEL,E_COL_FACTORY,E_TV_RATE," +
							"E_TA_RATE,E_MULTIPLE,E_CHECK_TIME,E_CHECK_RESULT,T_MODEL,T_TYPE,T_FACTORY,T_DISCREPANCY," +
							"T_UNMBER,T_TENSION_FIRST,T_TENSION_SECOND,T_TENSION_SECOND_CHARGE,T_RANK,T_TV_KIND," +
							"T_IS_SPECIAL,T_IS_SPECIAL_TV,T_CHECK_TIME,T_CHECK_RESULT,EI_MODEL,EI_TYPE,EI_MANUFACTURER," +
							"EI_DISCREPANCY,EI_UNMBER,EI_RATED_TENSION_FIRST,EI_RATED_TENSION_SECOND," +
							"EI_RATED_TENSION_SECOND_CHARGE,EI_VERACITY_RANK,EI_TV_KIND,EI_IS_SPECIAL,EI_IS_SPECIAL_TV," +
							"EI_CHECK_TIME,EI_CHECK_RESULT,LT_MODEL,LT_TYPE,LT_MANUFACTURER,LT_UNMBER,GPS_MODEL,GPS_TYPE," +
							"GPS_MANUFACTURER,GPS_UNMBER,TV_AREA,TV_LENGTH,TA_LENGTH,TV_TIME,TV_RESULT,TV_CHARGE_TIME," +
							"TV_CHARGE_RESULT,TA_CHARGE_TIME,TA_CHARGE_RESULT) values(");
					StringBuffer sqlValue = new StringBuffer();
					for(int j=0;j<=sheet.getColumns();j++){
						sqlValue.append("'"+this.getCellValue(sheet.getCell(j, i))+"',");
					}
					if(sqlValue.length()>220){
						sql.append(sqlValue.deleteCharAt(sqlValue.length()-1)+")");
						sqlList.add(sql.toString());
					}
				}
			}else{
				List rowspanList = this.getRowspanList(sheet);
				logger.info("复杂表格共有"+rowspanList.size()+"条数据！");
//				循环解析每行数据(从第三行开始)
				for(int x=0;x<rowspanList.size();x++){
					int startRow = ((Integer)rowspanList.get(x)).intValue();
					int endRow = 0;
					if(x!=rowspanList.size()-1){
						endRow = ((Integer)rowspanList.get(x+1)).intValue()-1;
					}else{
						endRow = sheet.getRows()-1;
					}
					StringBuffer sql = new StringBuffer("insert into EMQ_PASS_BOOK(STATION_NAME,BELONG_UNIT_NAME," +
							"MEASURE_POINT_NAME,SUB_POINT_NAME,SUB_POINT_ADDRESS,MEASURE_POINT_TYPE,MEASURE_POINT_KIND," +
							"PASS_TYPE,E_CONFIGURE,E_MODEL,E_TYPE,E_MANUFACTURER,E_FACTORY_NUMBER,E_CONNECTION,E_TENSION," +
							"E_ELECTRICITY,E_RANK,E_MESSAGE_INTERFACE,E_MESSAGE_TYPE,E_COL_MODEL,E_COL_FACTORY,E_TV_RATE," +
							"E_TA_RATE,E_MULTIPLE,E_CHECK_TIME,E_CHECK_RESULT,T_MODEL,T_TYPE,T_FACTORY,T_DISCREPANCY," +
							"T_UNMBER,T_TENSION_FIRST,T_TENSION_SECOND,T_TENSION_SECOND_CHARGE,T_RANK,T_TV_KIND," +
							"T_IS_SPECIAL,T_IS_SPECIAL_TV,T_CHECK_TIME,T_CHECK_RESULT,EI_MODEL,EI_TYPE,EI_MANUFACTURER," +
							"EI_DISCREPANCY,EI_UNMBER,EI_RATED_TENSION_FIRST,EI_RATED_TENSION_SECOND," +
							"EI_RATED_TENSION_SECOND_CHARGE,EI_VERACITY_RANK,EI_TV_KIND,EI_IS_SPECIAL,EI_IS_SPECIAL_TV," +
							"EI_CHECK_TIME,EI_CHECK_RESULT,LT_MODEL,LT_TYPE,LT_MANUFACTURER,LT_UNMBER,GPS_MODEL,GPS_TYPE," +
							"GPS_MANUFACTURER,GPS_UNMBER,TV_AREA,TV_LENGTH,TA_LENGTH,TV_TIME,TV_RESULT,TV_CHARGE_TIME," +
							"TV_CHARGE_RESULT,TA_CHARGE_TIME,TA_CHARGE_RESULT) values(");
					StringBuffer sqlValue = new StringBuffer();
					for(int j=0;j<=sheet.getColumns();j++){
						sqlValue.append("'");
						for(int i=startRow;i<=endRow;i++){
							String tempValue = this.getCellValue(sheet.getCell(j, i)).toString();
							if(!tempValue.equals("")){
								sqlValue.append(tempValue+";");
							}
						}
						if(sqlValue.lastIndexOf(";")==sqlValue.length()-1){
							sqlValue.deleteCharAt(sqlValue.length()-1);
						}
						sqlValue.append("',");
					}
					if(sqlValue.length()>220){
						sql.append(sqlValue.deleteCharAt(sqlValue.length()-1)+")");
						sqlList.add(sql.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ErrorMsgConstants.EMQ_UI_01, e);
			throw new Exception(ErrorMsgConstants.EMQ_UI_01);
		}finally{
			if(wb != null)
				wb.close();
			if(is != null) 
				try{is.close();}catch(Exception e){};
			File f = new File(fileName);
			if(f.exists()){
				f.delete();
			}
		}
		return sqlList;
	}
	
	/**
	 * 获取各条数据跨的行数2003
	 * @param sheet
	 * @return
	 */
	private List getRowspanList(Sheet sheet){
		List rowspanList = new ArrayList();
		int maxRow = sheet.getRows();
		int rowspan = 2;
		Range[] ranges = sheet.getMergedCells();
		for(int j=0;j<maxRow;){
			int tempRowspan = 0;
			for(int i=0;i<ranges.length;i++){
				Range range = ranges[i];
				int topLeftRow = range.getTopLeft().getRow();
				if(topLeftRow==rowspan){
					int bottomRightRow = range.getBottomRight().getRow();
					int temp = bottomRightRow-topLeftRow;
					if(tempRowspan<temp){
						tempRowspan = temp;
					}
				}
			}
			rowspanList.add(rowspan);
			rowspan += tempRowspan+1;
			j=rowspan;
		}
		return rowspanList;
	}
	
	/**
	 * 获取各条数据跨的行数2007
	 * @param sheet
	 * @return
	 */
	private List getRowspanList(XSSFSheet sheet){
		List rowspanList = new ArrayList();
		int maxRow = sheet.getPhysicalNumberOfRows();
		int rowspan = 2;
		List ranges = new ArrayList();
		for(int i=0;i<sheet.getNumMergedRegions();i++){
			ranges.add(sheet.getMergedRegion(i));
		}
		for(int j=0;j<maxRow;){
			int tempRowspan = 0;
			for(int i=0;i<ranges.size();i++){
				CellRangeAddress range = (CellRangeAddress)ranges.get(i);
				int topLeftRow = range.getFirstRow();
				if(topLeftRow==rowspan){
					int bottomRightRow = range.getLastRow();
					int temp = bottomRightRow-topLeftRow;
					if(tempRowspan<temp){
						tempRowspan = temp;
					}
				}
			}
			rowspanList.add(rowspan);
			rowspan += tempRowspan+1;
			j=rowspan;
		}
		return rowspanList;
	}
	/**
	 * 解析文件获取SQL
	 * @param
	 * 		fileName 文件名（带路径）
	 * @return
	 * 		List<EmFeeAirticket>
	 * @throws HYEMException
	 */
	private List<String> parseFile(String fileName, String type) throws Exception{
		List sqlList = new ArrayList();
		String fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		if(fileType.equalsIgnoreCase("xlsx")){
			sqlList = this.parseFileByXlsx(fileName,type);
		}else if(fileType.equalsIgnoreCase("xls")){
			sqlList = this.parseFileByXls(fileName,type);
		}
		return sqlList;
	}

	/**
	 * 保存上传的文件到磁盘
	 * @param path
	 * 			保存路径
	 * @param file
	 * 			上传文件
	 * @throws SmartUploadException
	 * @throws IOException
	 */
	private String saveFile(String path, com.jspsmart.upload.File file) throws SmartUploadException, IOException {
		//获取文件名 
		String fileName =  path + UPLOAD_DIR + file.getFileName(); 
		File dir = new File(path + UPLOAD_DIR);
		if(!dir.exists())
			dir.mkdirs();
		File exsit = new File(fileName);
		if(exsit.exists())
			exsit.delete(); 
		file.saveAs(fileName, com.jspsmart.upload.File.SAVEAS_PHYSICAL); 
		return fileName;
	}
	/**
	 * 上传文件
	 * @param req
	 * @param res
	 * @return
	 * 		上传文件名(带路径)列表
	 * @throws HYEMException
	 */
	private List<String> smartUploadFile(HttpServletRequest req, HttpServletResponse res)
			throws  Exception {
		List<String> fileList = new ArrayList<String>(); 
		try{
			//上传文件
		  	SmartUpload supload = new SmartUpload();
			supload.initialize(this.getServletConfig(), req, res);
			supload.setAllowedFilesList("xls,xlsx");
			supload.setMaxFileSize(MAX_FILE_SIZE);
			supload.upload();
			//保存文件
			com.jspsmart.upload.Files files = supload.getFiles();			
			int fcount = files.getCount(); 
			for (int i = 0; i < fcount; i++) {
				com.jspsmart.upload.File file = files.getFile(i);
				if (file.isMissing()) {
					continue;
				} 
				String path = req.getSession().getServletContext().getRealPath("");
				String fileName = saveFile(path, file); 
				fileList.add(fileName);
			} 
			 
		}catch(Exception e){
			e.printStackTrace();
			importFailLog +="上传文件失败！\n";
			logger.error(ErrorMsgConstants.EMQ_UI_01, e);
			throw new Exception(ErrorMsgConstants.EMQ_UI_01);
		}
		return fileList;
	} 
}

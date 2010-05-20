package com.emq.ui.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
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
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.log4j.Logger;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.emq.exception.ErrorMsgConstants;
import com.emq.service.BaseService;
import com.emq.util.DateUtil;
import com.emq.util.TimeUtil;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

/**
 * 
 * 文件导入Servlet
 * type:  passBook 上传到服务器并保存到对应人员的文件夹，并解析Excel文件将数据存入数据库
 *        commonFile 上传到服务器保存到upload\commonFile文件夹下，供下载用
 *        picture  设备获取其供页面展示用的图片文件，保存在imgs文件夹下
 */
public class FileImportServlet extends HttpServlet {

	private static final long serialVersionUID = 8968112076191958384L;
	/**
	 * 用于存放上传文件的临时目录
	 */
	public String UPLOAD_DIR = "\\upload\\";	
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
			String userName = "test";
			String personUnit = "testUnit";
			if(type.equalsIgnoreCase("passBook")){
				UPLOAD_DIR = "\\upload\\"+userName+"\\";
			}else if(type.equalsIgnoreCase("commonFile")){
				UPLOAD_DIR = "\\upload\\commonFile\\";
			}else if(type.equalsIgnoreCase("picture")){
				UPLOAD_DIR = "\\imgs\\";
			}else{
				UPLOAD_DIR = "\\upload\\";
			}
			List<String> files = smartUploadFile(req, res,userName,personUnit);
			boolean flag = true;
			if(type.equals("passBook")){
				for(String fileName: files){
					File f = new File(fileName);
					if (!f.exists())
						throw new Exception(ErrorMsgConstants.EMQ_UI_01);
					BaseService baseService = getBaseService();	
					int fileId = baseService.getImportMaxId();
					String name = fileName.substring(fileName.lastIndexOf("\\")+1, fileName.length());
					//解析
					List sqlList = parseFile(fileName, type,fileId);
					//保存	 
					flag = baseService.exeUpdateSqlByBach(sqlList);
				}
			}
			if(true == flag){//保存成功
				out.println("{success:true}");
				out.flush(); 
			}else{
				out.println("{success:false");
				out.flush();
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
//				替换特殊字符×
				value = value.toString().replace("?","×");
			}
		}
		//空
		if(cell.getType().equals(CellType.EMPTY)){
			value = "";
		}
		return value;
	}
	
	/**
	 * 解析Excel2007
	 * @return
	 */
	private List<String> parseFileByXlsx(String fileName,int fileId) throws Exception{
		List sqlList = new ArrayList();
		XSSFWorkbook xwb = null;
		String tableColName = "insert into EMQ_PASS_BOOK(STATION_NAME,BELONG_UNIT_NAME," +
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
			"TV_CHARGE_RESULT,TA_CHARGE_TIME,TA_CHARGE_RESULT,FILE_ID) values(";
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
			this.fillMergedCell(sheet);
			for (int i = sheet.getFirstRowNum()+2; i < sheet.getPhysicalNumberOfRows(); i++) {
				StringBuffer sql = new StringBuffer(tableColName);
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
					sql.append(sqlValue+"'"+fileId+"')");
					sqlList.add(sql.toString());
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error(ErrorMsgConstants.EMQ_UI_01, e);
			throw new Exception(ErrorMsgConstants.EMQ_UI_01);
		}finally{
			File f = new File(fileName+"_");
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
	private List<String> parseFileByXls(String fileName,int fileId) throws Exception{
		List sqlList = new ArrayList();
		Workbook rw = jxl.Workbook.getWorkbook(new File(fileName)); 
		WritableWorkbook wb = null;
		String tableColName = "insert into EMQ_PASS_BOOK(STATION_NAME,BELONG_UNIT_NAME," +
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
			"TV_CHARGE_RESULT,TA_CHARGE_TIME,TA_CHARGE_RESULT,FILE_ID) values(";
		try {
			//拷贝
			wb = Workbook.createWorkbook(new File(fileName+"_"),rw);
//			 得到工作薄中的第一个工作表
			WritableSheet sheet = wb.getSheet(0);
//			检查文件
			int rows = sheet.getRows(); 
			if(rows<1) {
				importFailLog += "该文件中没有数据\n";
				throw new Exception(ErrorMsgConstants.EMQ_UI_02);
			}
			this.fillMergedCell(sheet);
//			循环解析每行数据(从第三行开始)
			for(int i=2;i<rows;i++){
				StringBuffer sql = new StringBuffer(tableColName);
				StringBuffer sqlValue = new StringBuffer();
				for(int j=0;j<sheet.getColumns();j++){
					sqlValue.append("'"+this.getCellValue(sheet.getCell(j, i))+"',");
				}
				if(sqlValue.length()>220){
					sql.append(sqlValue+"'"+fileId+"')");
					sqlList.add(sql.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ErrorMsgConstants.EMQ_UI_01, e);
			throw new Exception(ErrorMsgConstants.EMQ_UI_01);
		}finally{
			if(wb != null){
				wb.close();
			}
			if(rw != null){
				rw.close();
			}
			File f = new File(fileName+"_");
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
	 * 补充合并单元格值2003
	 * @param sheet
	 * @return
	 */
	private void fillMergedCell(WritableSheet sheet){
		Range[] ranges = sheet.getMergedCells();
		for(int i=0;i<ranges.length;i++){
			Range range = ranges[i];
			int topLeftRow = range.getTopLeft().getRow();
			int topLeftCol = range.getTopLeft().getColumn();
			int bottomRightRow = range.getBottomRight().getRow();
			String firstCellValue = this.getCellValue(sheet.getCell(topLeftCol, topLeftRow)).toString();
			try{
				if(topLeftRow>=2){
					for(int x=topLeftRow+1;x<=bottomRightRow;x++){
						Cell cell = sheet.getCell(topLeftCol, x);
						if(cell.getType().equals(CellType.LABEL)){
							Label label = (Label)cell;
							label.setString(firstCellValue);
						}else if(cell.getType().equals(CellType.EMPTY)){
							Label temp = new Label(cell.getColumn(),cell.getRow(),firstCellValue);
					        sheet.addCell(temp);     
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
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
	 * 补充合并单元格值2007
	 * @param sheet
	 * @return
	 */
	private void fillMergedCell(XSSFSheet sheet){
		List ranges = new ArrayList();
		for(int i=0;i<sheet.getNumMergedRegions();i++){
			ranges.add(sheet.getMergedRegion(i));
		}
		for(int i=0;i<ranges.size();i++){
			CellRangeAddress range = (CellRangeAddress)ranges.get(i);
			int topLeftRow = range.getFirstRow();
			int topLeftCol = range.getFirstColumn();
			int bottomRightRow = range.getLastRow();
			String firstCellValue = sheet.getRow(topLeftRow).getCell(topLeftCol).toString();
			if(topLeftRow>=2){
				for(int x=topLeftRow+1;x<=bottomRightRow;x++){
					XSSFCell cell = sheet.getRow(x).getCell(topLeftCol);
					if(cell.getCellType()==XSSFCell.CELL_TYPE_STRING){
						cell.setCellValue(firstCellValue);
					}
				}
			}
		}
	}
	/**
	 * 解析文件获取SQL
	 * @param
	 * 		fileName 文件名（带路径）
	 * @return
	 * 		List<EmFeeAirticket>
	 * @throws HYEMException
	 */
	private List<String> parseFile(String fileName, String type,int fileId) throws Exception{
		List sqlList = new ArrayList();
		String fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		if(fileType.equalsIgnoreCase("xlsx")){
			sqlList = this.parseFileByXlsx(fileName,fileId);
		}else if(fileType.equalsIgnoreCase("xls")){
			sqlList = this.parseFileByXls(fileName,fileId);
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
	private String saveFile(String path, com.jspsmart.upload.File file,String userName,String personUnit) throws SmartUploadException, IOException {
		//获取文件名 
		String fileName =  TimeUtil.dateToString(new Date(),"yyyyMMddHHmmss")+"."+file.getFileExt(); 
		String filePath = path+UPLOAD_DIR+fileName;
		File dir = new File(path + UPLOAD_DIR);
		if(!dir.exists())
			dir.mkdirs();
		File exsit = new File(filePath);
		if(exsit.exists()){
			exsit.delete(); 
		}
		//保存记录
		BaseService baseService = getBaseService();	
		String oldName = file.getFileName();
		String fileType = file.getFileExt();
		if(fileType.equals("xls")||fileType.equals("xlsx")){
			fileType = "Excel";
		}else if(fileType.equals("doc")||fileType.equals("docx")){
			fileType = "Word";
		}else if(fileType.equals("png")||fileType.equals("gif")||fileType.equals("jpg")){
			fileType = "Picture";
		}else{
			fileType = "Other";
		}
		file.saveAs(filePath, com.jspsmart.upload.File.SAVEAS_PHYSICAL); 
		baseService.exeUpdateSql("insert into EMQ_BOOK_IMPORT(NEW_FILE_NAME,OLD_FILE_NAME,FILE_PATH,IMPORT_PERSON,PERSON_UNIT,IMPORT_TIME,STATE,FILE_TYPE) values('"+fileName+"','"+oldName+"','"+filePath+"','"+userName+"','"+personUnit+"',getDate(),0,'"+fileType+"')");
		return filePath;
	}
	/**
	 * 上传文件
	 * @param req
	 * @param res
	 * @return
	 * 		上传文件名(带路径)列表
	 * @throws HYEMException
	 */
	private List<String> smartUploadFile(HttpServletRequest req, HttpServletResponse res,String userName,String personUnit)
			throws  Exception {
		List<String> fileList = new ArrayList<String>(); 
		try{
			//上传文件
		  	SmartUpload supload = new SmartUpload();
			supload.initialize(this.getServletConfig(), req, res);
//			supload.setAllowedFilesList("xls,xlsx");
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
				String fileName = saveFile(path, file,userName,personUnit); 
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

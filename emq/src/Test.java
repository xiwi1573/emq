import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Test {
	
	public List testCombox(){
		List list = new ArrayList();
		for(int i=0;i<10;i++){
			Map map = new HashMap();
			map.put("code", i+"1");
			map.put("text", i+"ming");
			list.add(map);
		}
		return list;
	}

}

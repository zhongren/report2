package com.newproj.report.school.dal.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.newproj.core.mybatis.AbstractBaseMapper;
import com.newproj.core.mybatis.Table;
import com.newproj.report.export.bean.SchoolCollectionData;

@Repository
@Table("tb_school_collection")
public class SchoolCollectionDao extends AbstractBaseMapper{
	
	//@Autowired
	//private DbHelper dbHelper;
	
	 public List<SchoolCollectionData>   getSchoolCollections(String sid) throws SQLException{
		/* String sql = "SELECT sc.content as value,c.title as collectionName,q.name as secondName,(select name from tb_quota where id =q.parent_id ) as firstName " + 
		 		" from tb_school_collection as sc " + 
		 		"	 inner join tb_collection as c on sc.collection_id = c.id " + 
		 		"	 inner join tb_quota as q  on q.id = sc.quota_id " + 
		 		" where sc.school_id = ? order by q.parent_id ";
		 List<SchoolCollectionData> list = dbHelper.getBeanList(sql, SchoolCollectionData.class, sid);
		 return  list ;*/
		return null ;
	 }
	
	

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}

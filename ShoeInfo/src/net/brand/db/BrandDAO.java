package net.brand.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.board.action.Criteria;
import net.sneaker.db.SneakerDTO;

public class BrandDAO {

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql="";
	
	// 디비 연결(커넥션 풀 사용)
	private Connection getConnection() throws Exception{
		// Context 객체를 생성
		Context init = new InitialContext();
		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/coduo25");
		con = ds.getConnection();
		return con;
	}
	// 자원 해제 
	public void closeDB(){
		try {
			if(rs !=null) rs.close();
			if(pstmt !=null) pstmt.close();
			if(con !=null) con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//전체 브랜드 수 구하는 함수
	public int countBrand(){
		int num = 0;
		try {
			con = getConnection();
			sql = "select * from shoeinfo_brand";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				num += 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return num;
	}
	
	//새로운 브랜드 저장하는 함수
	public void insertNewBrand(BrandDTO bdto) {
		int num = 0;
		try {
			con = getConnection();
			sql = "select Max(brand_num) from shoeinfo_brand";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()){
				num = rs.getInt(1) + 1;
			}
			
			sql = "insert into shoeinfo_brand values(?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, bdto.getCountry_name());
			pstmt.setString(3, bdto.getBrand_logo());
			pstmt.setString(4, bdto.getBrand_name());
			pstmt.setString(5, bdto.getBrand_id());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}
	
	//모든 브랜드 정보 리스트 가져오는 함수 (criteria 안쓰고 가져오기)
		public ArrayList getAllBrandList() {
			ArrayList<BrandDTO> brandList = new ArrayList<BrandDTO>();
			try {
				con = getConnection();
				sql = "select * from shoeinfo_brand order by brand_name";
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()){
					BrandDTO bdto = new BrandDTO();
					bdto.setCountry_name(rs.getString("country_name"));
					bdto.setBrand_logo(rs.getString("brand_logo"));
					bdto.setBrand_name(rs.getString("brand_name"));
					bdto.setBrand_id(rs.getString("brand_id"));
					brandList.add(bdto);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeDB();
			}
			return brandList;
		}
	
	//모든 브랜드 정보 리스트 가져오는 함수 (criteria 써서 가져오기)
	public ArrayList getAllBrandList(Criteria cri) {
		ArrayList<BrandDTO> brandList = new ArrayList<BrandDTO>();
		try {
			con = getConnection();
			sql = "select * from shoeinfo_brand order by brand_name limit ?,?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, cri.getPageStart());
			pstmt.setInt(2, cri.getPerpageNum());
			rs = pstmt.executeQuery();
			while(rs.next()){
				BrandDTO bdto = new BrandDTO();
				bdto.setCountry_name(rs.getString("country_name"));
				bdto.setBrand_logo(rs.getString("brand_logo"));
				bdto.setBrand_name(rs.getString("brand_name"));
				bdto.setBrand_id(rs.getString("brand_id"));
				brandList.add(bdto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return brandList;
	}
	
	// 브랜드 상세정보 가져오는 함수
	public BrandDTO getBrandDetail(String brand_id) {
		BrandDTO bdto = null;
		try {
			con = getConnection();
			sql = "select * from shoeinfo_brand where brand_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, brand_id);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				//신발이 있으면
				bdto = new BrandDTO();
				bdto.setBrand_num(rs.getInt("brand_num"));
				bdto.setCountry_name(rs.getString("country_name"));
				bdto.setBrand_logo(rs.getString("brand_logo"));
				bdto.setBrand_name(rs.getString("brand_name"));
				bdto.setBrand_id(rs.getString("brand_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return bdto;
	}
	
	// 브랜드 정보 수정하는 함수
	public void updateBrandInfo(BrandDTO bdto, String old_brand_id) {
		int check = 0;
		try {
			con = getConnection();
			sql = "select brand_name from shoeinfo_brand where brand_num = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, bdto.getBrand_num());
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				sql = "update shoeinfo_brand set country_name = ?, brand_logo = ?, brand_name = ?, brand_id = ? where brand_num = ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, bdto.getCountry_name());
				pstmt.setString(2, bdto.getBrand_logo());
				pstmt.setString(3, bdto.getBrand_name());
				pstmt.setString(4, bdto.getBrand_id());
				pstmt.setInt(5, bdto.getBrand_num());
				
				check = pstmt.executeUpdate();
				if(check > 0){
					//onlineinfo table에 바뀐 brand_id 모두 바꾸기
					sql = "update shoeinfo_onlineinfo set brand_id = ? where brand_id = ?";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, bdto.getBrand_id());
					pstmt.setString(2, old_brand_id);
					pstmt.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}
	
	//브랜드별 국가리스트 가져오는 함수
	public List<BrandDTO> searchCountryList_bybrand() {
		List<BrandDTO> brandList = new ArrayList();
		try {
			con = getConnection();
			sql = "select distinct country_name from shoeinfo_brand";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BrandDTO bdto = new BrandDTO();
				bdto.setCountry_name(rs.getString("country_name"));
				brandList.add(bdto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return brandList;
	}
	
	//국가별 브랜드 찾아오는 함수
	public List<BrandDTO> searchBrand(String country_name) {
		List<BrandDTO> brandNameList = new ArrayList();
		try {
			con = getConnection();
			sql = "select distinct brand_name from shoeinfo_brand where country_name = ? order by brand_name";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, country_name);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BrandDTO bdto = new BrandDTO();
				bdto.setBrand_name(rs.getString("brand_name"));
				brandNameList.add(bdto);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeDB();
		}
		return brandNameList;
	}
	
	//country와 brand_name으로 brand_id값 찾아오는 함수
	public String searchBrand_id(String country_name, String brand_name) {
		String brand_id = "";
		try {
			con = getConnection();
			sql = "select brand_id from shoeinfo_brand where country_name = ? AND brand_name = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, country_name);
			pstmt.setString(2, brand_name);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				brand_id = rs.getString("brand_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return brand_id;
	}
		
		
		
		
}
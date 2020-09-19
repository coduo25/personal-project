package net.admin.action;

import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.board.action.Criteria;
import net.board.action.PageMaker;
import net.brand.db.BrandDTO;
import net.member.db.MemberDrawDTO;
import net.online.db.OnlineDAO;
import net.online.db.OnlineDTO;
import net.sneaker.db.SneakerDTO;

public class OnlineInfoListAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		//로그인 정보 가져오기
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute("email");
		String usr_position = (String) session.getAttribute("usr_position");
		ActionForward forward = new ActionForward();
		if(user == null || !usr_position.equals("admin")){
			forward.setPath("./SneakerList.go");
			forward.setRedirect(true);
			return forward;
		}
		
		//total 게시판 글 수 
		int total = 0;
		
		// ------- 페이징 처리 ---------
		String pageNum = request.getParameter("pageNum");
		if(pageNum == null){
			pageNum = "1";
		}
		int currentPage = Integer.parseInt(pageNum);
		
		int pageSize = 50;
		
		Criteria cri = new Criteria();
		
		cri.setPage(currentPage);
		cri.setPerpageNum(pageSize);
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		
		OnlineDAO odao = new OnlineDAO();
		
		Vector vec = (Vector) odao.getAllOnlineInfo(cri);
		
		ArrayList<OnlineDTO> onlineInfoList = (ArrayList<OnlineDTO>) vec.get(0);
		ArrayList<SneakerDTO> sneakerInfoList = (ArrayList<SneakerDTO>) vec.get(1);
		ArrayList<BrandDTO> brandInfoList = (ArrayList<BrandDTO>) vec.get(2);
		
		total = odao.countOnlineInfo();
		pageMaker.setTotalCount(total);
		
		request.setAttribute("onlineInfoList", onlineInfoList);
		request.setAttribute("sneakerInfoList", sneakerInfoList);
		request.setAttribute("brandInfoList", brandInfoList);
		request.setAttribute("cri", cri);
		request.setAttribute("pageMaker", pageMaker);
		request.setAttribute("pageNum", currentPage);
		
		forward.setPath("./admin/admin_onlineInfoList.jsp");
		forward.setRedirect(false);
		return forward;
	}
	
}

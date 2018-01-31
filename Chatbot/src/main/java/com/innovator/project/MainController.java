package com.innovator.project;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class MainController {
	
	@Autowired
	private VolunteerDAO volunteerDAO;
	
	@RequestMapping(value = "/keyboard", method = RequestMethod.GET)
	public String keyboard() {
		
		System.out.println("/keyboard");
		
		JSONObject jobj = new JSONObject();
		jobj.put("type", "text");
		
		return jobj.toJSONString();
	}
	
	
	@RequestMapping(value = "/message", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String message(@RequestBody JSONObject resObj) {
		
		System.out.println("/message");
		System.out.println(resObj.toJSONString());
		
	
		String content;
		content = (String)resObj.get("content");
		JSONObject jobjRes = new JSONObject();
		JSONObject jobjText = new JSONObject();
		
		if(content.contains("사용방법")) {
			jobjText.put("text", "1. 소개\n"
					+ "2. 지원요건\n"
					+ "3. 지원하기\n"
					+ "\n\n"
					+ "ex)소개");
		}
		else if(content.contains("소개")) {
			jobjText.put("text", "안녕하세요\n" + 
					"먼저 학우분의 관심에 감사인사드립니다.\n" + 
					"저희는 백석대학교 정보통신학부 소프트웨어학과 LAB 입니다.\n" + 
					"위치는 본부동 11층 1114호에 있으며, ... 중략...\n");
		}
		else if(content.contains("지원요건")) {
			jobjText.put("text", "저희 LAB은 현재 2~3학년 학우분들을 대상으로 지원 받고 있으며,\n" + 
					"남학우분의 경우에는 군복무를 마치신 학우분을 대상으로 지원 받고 있습니다.\n" + 
					"아직 군복무를 마치지 못한 남학우분은 군복무를 마치신 후에 다시 지원부탁드립니다. " +
					"...중략...\n");
		}
		else if(content.contains("지원하기")) {
			jobjText.put("text", "'지원요건' 내용에 부합하신 분이라는 가정하에 지원접수 절차를 진행하도록 하겠습니다.\n" + 
					"\n" + 
					"안내 메세지를 잘 읽으시고 응답해주시면 됩니다.\n" + 
					"\n" + 
					"이름/성별/학번/학과/핸드폰번호/면접가능한 요일 및 시간대\n");
		}
		else if(content.contains("sudo echo all volunteer")) {
			List<Volunteer> list = executeSelectQuery();
			if(!list.isEmpty()) {
				int size = list.size();
				String info = "";
				for(int i = 0; i < size; i++) {
					info += list.get(i).getId() + ". " + list.get(i).getContent() + "\n";
				}
				jobjText.put("text", info);
			}
			else {
				jobjText.put("text", "등록된 지원자 정보가 없습니다.");
			}
		}
		else if(content.contains("sudo echo volunteer")) {
			jobjText.put("text", "성공");
		}
		else if(checkCondition(content)) {
			if(executeInsertQuery(content) > 0) {
				jobjText.put("text", "지원접수가 되었습니다."
						+ "추후 연락드리겠습니다."
						+ "감사합니다.");
			}
			else {
				jobjText.put("text", "지원접수를 실패하였습니다."
						+ "다시 입력해주세요");
			}
		}
		else {
			jobjText.put("text", "다시 입력해주세요");
		}
		
		jobjRes.put("message", jobjText);
		System.out.println(jobjRes.toJSONString());
		
		return jobjRes.toJSONString();
	}
	
	private int executeInsertQuery(String content) {
		return volunteerDAO.insert(new Volunteer(0, content));
	}
	
	private List<Volunteer> executeSelectQuery() {
		return volunteerDAO.listForBeanPropertyRowMapper();
	}
	
	private boolean checkCondition(String content) {
		int index = 0;
		int count = 0;
		
		while(content.indexOf("/") > -1) {
			count++;
			index = content.indexOf("/");
			content = content.substring(index+1);
		}
		return count == 5;
	}

}
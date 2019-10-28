package me.maxwin.view;

public class MessageItem {

	// map.put("ID", obj.getString("ID"));
	// map.put("IsRead", obj.getString("IsRead"));
	// map.put("Body", obj.getString("Body"));
	// map.put("Type", obj.getString("Type"));
	// map.put("Title", obj.getString("Title"));
	// map.put("CreateTime",obj.getString("CreateTime"));
	// map.put("slideView", new SlidingDeleteSli

	public String ID;
	public String IsRead;
	public String Body;
	public String Type;
	public String Title;
	public String CreateTime;

	public SlidingDeleteSlideView slideView;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getIsRead() {
		return IsRead;
	}

	public void setIsRead(String isRead) {
		IsRead = isRead;
	}

	public String getBody() {
		return Body;
	}

	public void setBody(String body) {
		Body = body;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public SlidingDeleteSlideView getSlideView() {
		return slideView;
	}

	public void setSlideView(SlidingDeleteSlideView slideView) {
		this.slideView = slideView;
	}
	
	
	
	
	
}

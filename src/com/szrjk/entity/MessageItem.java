package com.szrjk.entity;

public class MessageItem
{
	

	// ��Ϣ����
	private int type;
	// �û�Id
	private long userId;
	// �û���
	private String nikeName;
	// ����
	private String content;
	// ʱ��
	private long time;
	// ��ת����
	private String targetUrl;
	// �Ƿ��Ѷ�
	private boolean isReaded;
	
	private String msg_id;
	private String display_type;
	private String body;
	private String custom;
	private int random_min;
//	{"message":"用户加关注","id":1,"declaringClass":"com.szjk.dhome.type.PushMessageType","code":"user.push.msg.addFocus"}
//
	private String message;
	private long id;
	private String declaringClass;  
	private String code;
	
	
	public MessageItem()
	{

	}

	public MessageItem(long id, String name, String content, int type,
					   long time, long userId, String targetUrl, boolean isReaded)
	{
		super();
		this.id = id;
		this.nikeName = name;
		this.content = content;
		this.type = type;
		this.time = time;
		this.userId = userId;
		this.targetUrl = targetUrl;
		this.isReaded = isReaded;
	}

	public boolean getIsReaded()
	{
		return isReaded;
	}

	public void setIsReaded(boolean isReaded)
	{
		this.isReaded = isReaded;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public String getNikeName()
	{
		return nikeName;
	}

	public void setNikeName(String nikeName)
	{
		this.nikeName = nikeName;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getTargetUrl()
	{
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl)
	{
		this.targetUrl = targetUrl;
	}
}

package org.papdt.miscol.bean;

public class MistakeOperationException extends Exception{

	private static final long serialVersionUID = -5634711023659701761L;

	public MistakeOperationException(Mistake mistake, String why) {
		super("对错题" + mistake.toString() + "进行操作时发生异常,原因为" + why);
	}
	
}


import java.io.DataOutputStream;

public class Client {
	String name;
	int num;
	boolean sex;
	int roomNumber;
	DataOutputStream dos;
	
	
	public Client(String name, int num, boolean sex){
		this.name = name;
		this.num = num;
		this.sex = sex;
	}

	@Override
	public boolean equals(Object obj) {
		if(((Client)obj).sex != this.sex && ((Client)obj).num == this.num){
			return true;
		}else{
			return false;
		}
	}
}

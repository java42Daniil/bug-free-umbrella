package connections.dto;

public class Connection {
private int id;
private String ipAddress;
private int port;
public Connection(int id, String ipAddress, int port) {
	this.id = id;
	this.ipAddress = ipAddress;
	this.port = port;
}
public Connection() {
	
}
public int getId() {
	return id;
}
public String getIpAddress() {
	return ipAddress;
}
public int getPort() {
	return port;
}


}
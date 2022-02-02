package connections.service;

import java.util.HashMap;
import java.util.Iterator;

import connections.dto.Connection;

public class ConnectionsPoolImpl implements ConnectionsPool {
	private static class Node {
		Connection connection;
		Node prev;
		Node next;

		Node(Connection c) {
			this.connection = c;
		}
	}
	private static class ConnectionsList implements Iterable{
		//[YG] No iterator is required
		private class ConnectionsListIterator implements Iterator{
			Node currentNode = head;
			
			@Override
			public boolean hasNext() {
				
				return currentNode != null;
			}
			@Override
			public Integer next() {
				int res = currentNode.connection.getId();
				currentNode = currentNode.next;
				return res;
				
			}
			
		}
		Node head = null;
		Node tail = null;

		private void addNode(Connection connection) {
			Node newNode = new Node(connection);
			if (head == null) {
				head = tail = newNode;
			} else {
				addHeadNode(newNode);
			}	
		}
		//[YG] the method shouldn't be public
		public void setNewHeadNode(Node currentNode) {
			if (currentNode != tail) {
				removeCenterNode(currentNode);
			} else {
				removeLastNode();
			}
			addHeadNode(currentNode);

		}
		private void removeLastNode() {
			tail = tail.prev;
			tail.next = null;
		}

		private void removeCenterNode(Node currentActiveConnectionNode) {
			Node beforReorder = currentActiveConnectionNode.prev;
			Node afterReorder = currentActiveConnectionNode.next;
			beforReorder.next = afterReorder;
			afterReorder.prev = beforReorder;
		}
		private void addHeadNode(Node newNode) {
			newNode.prev = null;
			newNode.next = head;
			head.prev = newNode;
			head = newNode;
		}
		@Override
		//[YG] no need iterator
		public Iterator iterator() {
			return new ConnectionsListIterator();
		}

		

		
	}

	ConnectionsList list = new ConnectionsList();
	HashMap<Integer, Node> mapConnections = new HashMap<>();
	int connectionsPoolLimit;

	public ConnectionsPoolImpl(int limit) {
		connectionsPoolLimit = limit;
	}

	@Override
	public boolean addConnection(Connection connection) {
		int id = connection.getId();
		if (mapConnections.containsKey(id)) {
			return false;
		}
		if (mapConnections.size() == connectionsPoolLimit) {
			int idOfLastNode = list.tail.connection.getId();
			list.removeLastNode();
			mapConnections.remove(idOfLastNode);
		}
		list.addNode(connection);
		mapConnections.put(id, list.head);
		return true;
	}

	@Override
	public Connection getConnection(int id) {
		Node currentConnection = mapConnections.get(id);
		if (currentConnection == null) {
			return null;
		}
		if (currentConnection.prev != null) {
			list.setNewHeadNode(currentConnection);
		}
		return currentConnection.connection;
	}
	
	public int[] getConnectionsIdList() {
		Iterator it = list.iterator();
		int res[] = new int[mapConnections.size()];
		for(int i =0; i< res.length; i++) {
			res[i]=(int) it.next();
		}
		return res;
	}
}

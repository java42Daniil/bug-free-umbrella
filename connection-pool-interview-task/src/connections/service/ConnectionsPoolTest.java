package connections.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import connections.dto.Connection;

class ConnectionPoolTests {
	private static final int ID1 = 1;
	private static final int ID2 = 2;
	private static final int ID3 = 3;
	private static final int LIMIT = 5;
	private static String IPADRESS = "192.192.192.192";
	private static int PORT = 1234;
	Connection conn1 = new Connection(ID1, IPADRESS, PORT);
	Connection conn2 = new Connection(ID2, IPADRESS, PORT);
	Connection conn3 = new Connection(ID3, IPADRESS, PORT);
	
	ConnectionsPool pool = new ConnectionsPoolImpl(LIMIT);

	
	@BeforeEach
	void setUp() throws Exception {
		pool.addConnection(conn1);
		pool.addConnection(conn2);
		pool.addConnection(conn3);
	}

	@Test
	void testAddConnection() {
		assertFalse(pool.addConnection(conn1));
		assertTrue(pool.addConnection(new Connection(ID1+100, IPADRESS, PORT)));
	}
	@Test
	void testGetConnection() {
		pool.addConnection(new Connection(ID1+100, IPADRESS, PORT));
		assertNull(pool.getConnection(ID1));
		assertEquals(conn2, pool.getConnection(ID2));
		Connection conn = new Connection(ID1+102, IPADRESS, PORT);
		pool.addConnection(conn);
		assertNull(pool.getConnection(ID3));
		assertEquals(conn, pool.getConnection(103));
	}

}
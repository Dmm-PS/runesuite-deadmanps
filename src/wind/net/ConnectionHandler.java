package wind.net;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import wind.Punishments;
import wind.util.Stopwatch;

/**
 * The network security that handles and validates all incoming connections
 * received by the server to ensure that the server does not fall victim to
 * attacks by a socket flooder or a connection from a banned host.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class ConnectionHandler {

    /**
     * The throttle interval for incoming connections.
     */
    private static final long CONNECTION_INTERVAL = 1000;

    /**
     * The maximum amount of connections that can be active at a time.
     */
    private static final int CONNECTION_AMOUNT = 1;

    /**
     * The concurrent map of registered connections.
     */
    private static final Map<String, Connection> CONNECTIONS = new ConcurrentHashMap<>(16, 0.9f, 2);

    /**
     * The default constructor.
     * 
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private ConnectionHandler() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    @SuppressWarnings("unused")
	private static final int 
    		/* valid login */
    		SUCCESSFUL_LOGIN = 2,
    		/* Blocked for invalid username or ip ban */
    		BLOCKED_CONNECTION = 4,
    		/* The player has too many connections with the current IP. */
    		LOGIN_LIMIT_EXCEEDED = 9,
    		/* The player ha sattempted to login incorrectly too many times.. */
    		LOGIN_ATTEMPTS_EXCEEDED = 16;
    		
    
    /**
     * Evaluates this host and returns a login response that determines the
     * result of evaluation.
     * 
     * @param host
     *            the host that will be evaluated.
     * @return the login response as a result of evaluating the host.
     */
    public static int evaluate(String host) {
        if (ConnectionHandler.isLocal(host))
            return SUCCESSFUL_LOGIN;
        Optional<Connection> connection = Optional.ofNullable(CONNECTIONS.putIfAbsent(host, new Connection()));
        if (connection.isPresent()) {
            Connection c = connection.get();
            if (c.throttleLimit()) { 
                return LOGIN_ATTEMPTS_EXCEEDED;
            } else if (Punishments.isIpBanned(host)) {
    			return BLOCKED_CONNECTION;
    		}
            c.increment();
            c.getThrottler().reset();
        }
        return SUCCESSFUL_LOGIN;
    }

    /**
     * Removes this host from the connection map or reduces the amount of
     * connections currently registered to this host.
     * 
     * @param host
     *            the host that will be removed.
     * @throws IllegalStateException
     *             if the specified host is not registered within the connection
     *             map.
     */
    public static void remove(String host) {
        if (ConnectionHandler.isLocal(host))
            return;
        Optional<Connection> op = Optional.ofNullable(CONNECTIONS.get(host));
        Connection c = op.orElseThrow(() -> new IllegalStateException("Host was not registered with the connection map!"));
        if (c.decrement() < 1)
            CONNECTIONS.remove(c);
    }

    /**
     * Determines if the specified host is connecting locally.
     * 
     * @param host
     *            the host to check if connecting locally.
     * @return {@code true} if the host is connecting locally, {@code false}
     *         otherwise.
     */
    public static boolean isLocal(String host) {
        return host.equals("127.0.0.1") || host.equals("localhost") || host.equals("97.88.49.29");
    }

    /**
     * The container that represents a host within the connection map.
     * 
     * @author lare96 <http://github.com/lare96>
     */
    private static final class Connection {

        /**
         * The amount of sessions bound to this connection.
         */
        private final AtomicInteger amount = new AtomicInteger();

        /**
         * The stopwatch used to time connection intervals.
         */
        private final Stopwatch throttler = new Stopwatch().reset();

        /**
         * Determines if the maximum amount of connections has been reached.
         * 
         * @return {@code true} if the amount of connections is above or equal
         *         to {@link ConnectionHandler#CONNECTION_AMOUNT}, {@code false}
         *         otherwise.
         */
        @SuppressWarnings("unused")
		public boolean sessionLimit() {
            return amount.get() >= CONNECTION_AMOUNT;
        }

        /**
         * Determines if the host is connecting too fast and needs to be
         * throttled.
         * 
         * @return {@code true} if the host needs to be throttled, {@code false}
         *         otherwise.
         */
        public boolean throttleLimit() {
            return throttler.elapsedTime() <= CONNECTION_INTERVAL;
        }

        /**
         * Increments the amount of sessions bound to this connection.
         * 
         * @return the amount after the increment completes.
         */
        public int increment() {
            return amount.incrementAndGet();
        }

        /**
         * Decrements the amount of sessions bound to this connection.
         * 
         * @return the amount after the decrement completes.
         */
        public int decrement() {
            return amount.decrementAndGet();
        }

        /**
         * Gets the amount of sessions bound to this connection.
         * 
         * @return the amount of sessions bound to this connection.
         */
        @SuppressWarnings("unused")
        public int getAmount() {
            return amount.get();
        }

        /**
         * Gets the stopwatch used to time connection intervals.
         * 
         * @return the stopwatch used to time connection intervals.
         */
        public Stopwatch getThrottler() {
            return throttler;
        }
    }
}
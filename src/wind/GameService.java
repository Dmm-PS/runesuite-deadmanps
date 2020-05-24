package wind;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * The most important class of this server that sequentially executes, or
 * sequences all game related code.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class GameService implements Runnable {

    /**
     * The logger that will print important information.
     */
    private static Logger logger = Logger.getLogger(GameService.class.getName());

    /**
     * The executor that will execute various {@code Thread.MIN_PRIORITY}
     * services. This executor implementation will allocate a maximum of 1
     * thread that will timeout after 45 {@code SECONDS} of inactivity.
     */
    private static ScheduledExecutorService logicService = GameService.createLogicService();

    @Override
    public void run() {
        try {
        	Server.process();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "An error has occured during the main game sequence!", t);
            // TODO: Save all players
        }
    }

    /**
     * Creates and configures the {@link GameService#logicService} that will
     * execute {@code Thread.MIN_PRIORITY} services. The returned executor is
     * <b>unconfigurable</b> meaning it's configuration can no longer be
     * modified.
     * 
     * @return the newly created and configured logic service.
     */
    private static ScheduledExecutorService createLogicService() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("LogicServiceThread").build());
        executor.setKeepAliveTime(45, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return Executors.unconfigurableScheduledExecutorService(executor);
    }

    /**
     * Gets the executor that will execute various {@code Thread.MIN_PRIORITY}
     * services.
     * 
     * @return the executor that will execute various
     *         {@code Thread.MIN_PRIORITY} services.
     */
    public static ScheduledExecutorService getLogicService() {
        return logicService;
    }
}
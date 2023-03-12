package io.grasscutter.server;

import io.grasscutter.game.data.ResourceLoader;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.objects.lang.TextContainer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ServerThread extends Thread {
    private final DedicatedServer server;
    private final Logger logger;

    private boolean isRunning = false;
    private long nextTick;
    private float maxTick = 20;
    private float maxUse = 0;

    private final double[] tickAverage = {
        20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20
    };
    private final double[] useAverage = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public ServerThread(DedicatedServer server) {
        this.server = server;
        this.logger = LoggerFactory.getLogger("Server Thread");
    }

    @Override
    public synchronized void start() {
        super.start();

        // Load game resources.
        ResourceLoader.loadResources();
    }

    /**
     * TODO: Write documentation.
     *
     * <p>Calculate the tick average time. Call {@link DedicatedServer#tick()} afterwards.
     */
    private void tick() {
        long tickTime = System.currentTimeMillis();

        long time = tickTime - this.nextTick;
        if (time < -25) {
            try {
                Thread.sleep(Math.max(5, -time - 25));
            } catch (InterruptedException ignored) {
                Log.warn(this.logger, new TextContainer("server.dedicated.interrupted"));
            }
        }

        long tickTimeNano = System.nanoTime();
        if ((tickTime - this.nextTick) < -25) {
            return;
        }

        this.server.tick(); // Perform a global server tick.

        long nowNano = System.nanoTime();

        float tick =
                (float) Math.min(20, 1000000000 / Math.max(1000000, ((double) nowNano - tickTimeNano)));
        float use = (float) Math.min(1, ((double) (nowNano - tickTimeNano)) / 50000000);

        if (this.maxTick > tick) {
            this.maxTick = tick;
        }

        if (this.maxUse < use) {
            this.maxUse = use;
        }

        System.arraycopy(this.tickAverage, 1, this.tickAverage, 0, this.tickAverage.length - 1);
        this.tickAverage[this.tickAverage.length - 1] = tick;

        System.arraycopy(this.useAverage, 1, this.useAverage, 0, this.useAverage.length - 1);
        this.useAverage[this.useAverage.length - 1] = use;

        if ((this.nextTick - tickTime) < -1000) {
            this.nextTick = tickTime;
        } else {
            this.nextTick += 50;
        }
    }

    /**
     * Gets the average time between ticks. (in ticks)
     *
     * @return The average time between ticks.
     */
    public double getTickAverage() {
        return Arrays.stream(this.tickAverage).sum() / this.tickAverage.length;
    }

    /** Call for the thread to die. */
    public void end() {
        // Stop the server thread.
        this.isRunning = false;
    }

    @Override
    @SuppressWarnings("BusyWait")
    public void run() {
        // Set the server thread to running.
        this.isRunning = true;

        // Complicated math to tick the server every 20ms.
        this.nextTick = System.currentTimeMillis();

        try {
            while (this.isRunning) {
                try {
                    this.tick(); // Perform a server tick.

                    // Calculate the time for the next tick.
                    long next = this.nextTick;
                    long current = System.currentTimeMillis();

                    if (next - 0.1 > current) {
                        long allocated = next - current - 1;

                        if (allocated > 0) {
                            Thread.sleep(allocated, 900000);
                        }
                    }
                } catch (RuntimeException exception) {
                    Log.error(this.logger, new TextContainer("exception.error"), exception);
                }
            }
        } catch (Throwable exception) {
            Log.error(this.logger, new TextContainer("server.dedicated.tick_error"), exception);
            Log.error(this.logger, this.server.dumpThreads());
        }
    }
}

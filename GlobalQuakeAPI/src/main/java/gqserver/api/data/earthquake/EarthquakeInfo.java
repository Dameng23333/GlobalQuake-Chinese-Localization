package gqserver.api.data.earthquake;

import java.io.Serializable;
import java.util.UUID;

public record EarthquakeInfo(UUID uuid, int revisionID) implements Serializable {
    public static final long serialVersionUID = 0L;
    public static final int REMOVED = -1;
}

package globalquake.database;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Station implements Serializable {

    @Serial
    private static final long serialVersionUID = 4798409607248332882L;

    private final double lat;

    private final double lon;

    private final double alt;

    private final String stationCode;
    private final String stationSite;
    private final Collection<Channel> channels;
    private Channel selectedChannel = null;

    public Station(String stationCode, String stationSite, double lat, double lon, double alt) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
        this.stationCode = stationCode;
        this.stationSite = stationSite;
        this.channels = new ArrayList<>();
    }

    public Collection<Channel> getChannels() {
        return channels;
    }

    public String getStationSite() {
        return stationSite;
    }

    public String getStationCode() {
        return stationCode;
    }

    public Channel getSelectedChannel() {
        return selectedChannel;
    }

    public void setSelectedChannel(Channel selectedChannel) {
        this.selectedChannel = selectedChannel;
    }

    public double getAlt() {
        return alt;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "Station{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", alt=" + alt +
                ", stationCode='" + stationCode + '\'' +
                ", stationSite='" + stationSite + '\'' +
                ", channels=" + channels +
                ", selectedChannel=" + selectedChannel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Double.compare(lat, station.lat) == 0 && Double.compare(lon, station.lon) == 0 && Double.compare(alt, station.alt) == 0 && Objects.equals(stationCode, station.stationCode) && Objects.equals(stationSite, station.stationSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon, alt, stationCode, stationSite);
    }

    public boolean hasAvailableChannel() {
        return getChannels().stream().anyMatch(Channel::isAvailable);
    }

    public void selectBestChannel() {
        if(selectedChannel != null){
            return;
        }

        for(Channel channel : getChannels()){
            if(channel.isAvailable()){
                selectedChannel = channel;
                return;
            }
        }

        var anyChannel = getChannels().stream().findAny();
        anyChannel.ifPresent(channel -> selectedChannel = channel);
    }
}

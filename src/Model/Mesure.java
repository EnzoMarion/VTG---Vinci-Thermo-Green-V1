package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Mesure {
    private int id;
    private String numZone;
    private String horoDate;
    private float temperature;

    public Mesure() {
        this.id = 0;
        this.numZone = "";
        this.horoDate = "";
        this.temperature = 0.0f;
    }

    public Mesure(int id, String numZone, String horoDate, float temperature) {
        this.id = id;
        this.numZone = numZone;
        this.horoDate = horoDate;
        this.temperature = temperature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumZone() {
        return numZone;
    }

    public void setNumZone(String numZone) {
        this.numZone = numZone;
    }

    public String getHoroDate() {
        return horoDate;
    }

    public void setHoroDate(String horoDate) {
        this.horoDate = horoDate;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getCelsius() {
        return (temperature - 32.0f) / 1.8f;
    }

    public static Mesure fromString(String line) throws ParseException {
        String[] parts = line.split(";");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Format de mesure invalide : " + line);
        }

        int id = Integer.parseInt(parts[0].trim());
        String numZone = parts[1].trim();
        String horoDate = parts[2].trim();
        float temperature = Float.parseFloat(parts[3].trim());

        return new Mesure(id, numZone, horoDate, temperature);
    }

    @Override
    public String toString() {
        return id + ";" + numZone + ";" + horoDate + ";" + temperature;
    }
}

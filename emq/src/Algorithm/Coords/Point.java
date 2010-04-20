package Algorithm.Coords;

public class Point 
{
    double longitude;
    double latitude;
    double x;
    double y;
    
    public void setX(double x)
    {
        this.x = x;
    }
    
    public double getX()
    {
        return this.x;
    }
    
    public void setY(double y)
    {
        this.y = y;
    }
    
    public double getY()
    {
        return this.y;
    }
    
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
    
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }
    
    public double getLongitude()
    {
        return longitude;
    }
    
    public double getLatitude()
    {
        return latitude;
    }
}
package server.auxilary;

/**
 * Created by ghost on 2017/04/06.
 */
public enum AccessLevels
{
    NO_ACCESS(0),//No rights
    STANDARD(1),//Read/Write rights - limited
    ADMIN(2),//Read/Write rights - slightly less limited
    SUPERUSER(3);//Read/Write rights

    private int level;

    private AccessLevels(int level)
    {
        this.level=level;
    }

    public int getLevel()
    {
        return this.level;
    }
}

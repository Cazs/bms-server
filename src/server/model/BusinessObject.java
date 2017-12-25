package server.model;

/**
 * Created by ghost on 2017/01/04.
 */
public interface BusinessObject
{
    String get_id();
    void set_id(String id);
    void parse(String var, Object val);
    Object get(String var);
    boolean isValid();
    boolean isMarked();
    void setMarked(boolean marked);
    String asJSON();
}

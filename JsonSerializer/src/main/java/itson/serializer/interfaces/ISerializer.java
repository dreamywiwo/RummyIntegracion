/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.serializer.interfaces;

/**
 *
 * @author Dana Chavez
 */
public interface ISerializer {
    public <T> T deserialize(String payload, Class<T> type);
    public <T> String serialize(T obj);
}

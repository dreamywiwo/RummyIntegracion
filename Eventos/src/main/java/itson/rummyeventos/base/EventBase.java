/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.rummyeventos.base;

/**
 *
 * @author Dana Chavez
 */
public class EventBase implements Event {

    private String topic;
    private String eventType;

    public EventBase() {
    }

    public EventBase(String topic, String eventType) {
        this.topic = topic;
        this.eventType = eventType;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public String getTopic() {
        return topic;
    }

}

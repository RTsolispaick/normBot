package org.opp.data.models;

import org.opp.data.models.types.Platform;

/**
 * Сообщение пользователя
 */
public class Response {
    private final Platform PLATFORM;
    private final Long ID;
    private final String RESPONSE;

    /**
     * Создаёт сообщение пользователю
     * @param user пользоваетль, которому адресуется сообщение
     * @param message текст сообщения
     */
    public Response(User user, String message) {
        this.PLATFORM = user.getPlatform();
        this.ID = user.getChat_id();
        this.RESPONSE = message;
    }

    public String getResponse() {
        return this.RESPONSE;
    }

    public Platform getPlatform() {
        return this.PLATFORM;
    }

    public Long getID() {
        return this.ID;
    }
}
package org.opp.bots;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.model.events.messages.MessageNew;
import api.longpoll.bots.model.objects.basic.Message;
import org.opp.core.ManagerResponse;
import org.opp.data.models.User;
import org.opp.data.models.types.Platform;
import org.opp.data.repositories.UserRepository;

/**
 * Класс вк бота
 */
public class VKBot extends LongPollBot implements Bot{
    private final ManagerResponse managerResponse;
    private final UserRepository userRepository;
    private final String token;

    public VKBot(String token, ManagerResponse managerResponse) {
        this.token = token;
        this.userRepository = new UserRepository();
        this.managerResponse = managerResponse;
        managerResponse.setBotVK(this);
    }

    @Override
    public void onMessageNew(MessageNew messageNew) {
        Message message = messageNew.getMessage();
        if (message.hasText()) {
            Long chat_id = (long) message.getFromId();
            String name = "Somename";

            try {
                name = getVkUserName(chat_id.toString());
            }catch (VkApiException e){
                System.err.println(e);
            }

            User user = userRepository.login(Platform.VK, chat_id, name);
            managerResponse.definitionOfResponse(message.getText().toLowerCase(), user);
            userRepository.update(user);
        }
    }
    public String getVkUserName(String USER_ID) throws VkApiException{
        return vk.users.get()
                .setUserIds(USER_ID)
                .execute().getResponse().get(0).getFirstName();
    }

    @Override
    public String getAccessToken() {
        return token;
    }

    @Override
    public void sendMessage (Long recipient_id, String message) {
        try {
            vk.messages.send()
                    .setPeerId(Math.toIntExact(recipient_id))
                    .setMessage(message)
                    .execute();
        } catch (VkApiException e) {
            System.err.printf("can't send message with text %s to telegram user with id \"%s\"%n",
                    message, recipient_id);
        }
    }
}
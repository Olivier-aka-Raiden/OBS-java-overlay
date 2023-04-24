package dev.nipafx.livefx.spring;

import dev.nipafx.livefx.twitch.TwitchChatBot;
import dev.nipafx.livefx.twitch.TwitchEventSubscriber;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class TwitchListenersLauncher implements ApplicationRunner {

	private final TwitchChatBot chatBot;
	private final TwitchEventSubscriber eventSubscriber;

	public TwitchListenersLauncher(TwitchChatBot chatBot, TwitchEventSubscriber eventSubscriber) {
		this.chatBot = chatBot;
		this.eventSubscriber = eventSubscriber;
	}

	@Order(1)
	@Override
	public void run(ApplicationArguments args) throws Exception {
		chatBot.connectAndListen();
		eventSubscriber.connectAndSubscribe();
	}

}

package dev.nipafx.livefx.twitch;

import dev.nipafx.livefx.command.AddChatMessage;
import dev.nipafx.livefx.command.Commander;
import dev.nipafx.livefx.twitch.ChatMessage.Join;
import dev.nipafx.livefx.twitch.ChatMessage.NameList;
import dev.nipafx.livefx.twitch.ChatMessage.Ping;
import dev.nipafx.livefx.twitch.ChatMessage.TextMessage;
import dev.nipafx.livefx.twitch.ChatMessage.Unknown;
import dev.nipafx.livefx.twitch.ChatMessage.Welcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;

public class TwitchChatBot {

	private static final URI TWITCH_IRC_URL = URI.create("wss://irc-ws.chat.twitch.tv:443");
	private static final String TWITCH_CHAT_USER_NAME = "nipafx";
	private static final String TWITCH_CHAT_USER_TOKEN = System.getenv("TWITCH_TOKEN");
	private static final String TWITCH_CHAT_CHANNEL_NAME = "nipafx";

	private static final Logger LOG = LoggerFactory.getLogger(TwitchChatBot.class);

	private final Commander commander;

	public TwitchChatBot(Commander commander) {
		this.commander = commander;
	}

	public void connectAndListen() {
		if (TWITCH_CHAT_USER_TOKEN == null || TWITCH_CHAT_USER_TOKEN.isEmpty())
			throw new IllegalArgumentException("No Twitch chat user token available - set environment variable 'TWITCH_TOKEN'");

		HttpClient
				.newHttpClient()
				.newWebSocketBuilder()
				.buildAsync(TWITCH_IRC_URL, new WebSocketListener())
				.whenComplete((websocket, throwable) -> {
					if (websocket != null)
						LOG.info("Successfully connected to Twitch IRC");
					if (throwable != null)
						LOG.error("Could not connect to Twitch IRC", throwable);
				});
	}

	private void sendPong(WebSocket webSocket, String message) {
		LOG.debug("Sending PONG...");
		webSocket.sendText("PONG :" + message, true);
	}

	private void interpretMessage(TextMessage message) {
		commander.sendCommand(new AddChatMessage(message.nick(), message.text(), ""));
	}

	private class WebSocketListener implements Listener {

		@Override
		public void onOpen(WebSocket webSocket) {
			LOG.info("Opened web socket connection to Twitch IRC");
			LOG.debug("Sending PASS...");
			webSocket.sendText("PASS oauth:" + TWITCH_CHAT_USER_TOKEN, true)
					.thenCompose(websocket -> {
						LOG.debug("Sending NICK...");
						return websocket.sendText("NICK " + TWITCH_CHAT_USER_NAME, true);
					})
					.thenCompose(websocket -> {
						LOG.debug("Joining...");
						return websocket.sendText("JOIN #" + TWITCH_CHAT_CHANNEL_NAME, true);
					});
			Listener.super.onOpen(webSocket);
		}

		@Override
		public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
			throw new IllegalStateException("Caught a ping but didn't reply");
		}

		@Override
		public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
			throw new IllegalStateException("Caught a pong but didn't reply");
		}

		@Override
		public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
			var msg = data.toString();
			LOG.trace("Received text message {}", msg);
			switch (ChatMessage.Factory.create(msg)) {
				case Welcome(var text) -> LOG.debug("Welcome to channel: {}", text);
				case Join(var text) -> LOG.debug("Joined channel: {}", text);
				case NameList(var text) -> LOG.debug("Name list: {}", text);
				case Ping(var text) -> sendPong(webSocket, text);
				case TextMessage message -> interpretMessage(message);
				case Unknown(var text) -> LOG.warn("Unknown Twitch text: {}", text);
			}
			return Listener.super.onText(webSocket, data, last);
		}

		@Override
		public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
			LOG.info("Connection to Twitch IRC closed with status code {}", statusCode);
			return Listener.super.onClose(webSocket, statusCode, reason);
		}

		@Override
		public void onError(WebSocket webSocket, Throwable error) {
			LOG.error("Connection to Twitch IRC closed with an error", error);
			Listener.super.onError(webSocket, error);
		}

	}

}

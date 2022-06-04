package br.inatel.sd.labmqtt.client;

import java.util.Random;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public class SensorTemperaturaPublisherLoop {

	public static void main(String[] args) throws InterruptedException, MqttSecurityException, MqttException {
		
		//1.
		String publisherId = UUID.randomUUID().toString();
		IMqttClient publisher = new MqttClient( MyConstants.URI_BROKER, publisherId);
		
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		publisher.connect(options);
		
		for (int i=0; i<10; i++) {
	
			try {
				MqttMessage msg = getTemperaturaSolo();
				msg.setQos(0);
				msg.setRetained(true);
				
				publisher.publish(MyConstants.TOPIC_1, msg);
		
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Thread.sleep( 2000 );
		}
		
		publisher.disconnect();
	}

	private static MqttMessage getTemperaturaSolo() {
		Random r = new Random();
		double temperatura = 80 + r.nextDouble() * 20.0;
		byte[] payload = String.format("T:%04.2f", temperatura).getBytes();
		return new MqttMessage(payload);
	}
}
